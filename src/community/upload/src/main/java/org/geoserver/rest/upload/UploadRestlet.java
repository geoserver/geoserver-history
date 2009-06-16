package org.geoserver.rest.upload;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.ext.fileupload.RestletFileUpload;

import org.vfny.geoserver.global.GeoserverDataDirectory;

import org.geoserver.rest.format.DataFormat;
import org.geoserver.rest.format.MapJSONFormat;

/**
 * Restlet to allow uploading files and serving them back, possibly with some server-side filter
 * applied.  For example, one might want to allow uploading images and then provide a thumbnail
 * version.
 *
 * @author David Winslow <dwinslow@opengeo.org>
 */
public class UploadRestlet extends Restlet{
    /**
     * The the directory in which files will be stored.
     */
    private File rootPath = GeoserverDataDirectory.getGeoserverDataDirectory();

    /**
     * A map of mimetype-&gt;file extension to use when generating filenames for stored files.
     */
    private Map mimeTypes; 
    
    private static Logger LOG = 
        org.geotools.util.logging.Logging.getLogger("org.geoserver.rest.upload"); 

    private UniqueIDGenerator myIDGenerator = new UniqueIDGenerator();

    private FileStorage fileStorage;

    private UploadFilter uploadFilter;

    public void setTypeMap(Map m){
        mimeTypes = m;
    }

    public Map getTypeMap(){
        return mimeTypes;
    }

    public UploadFilter getUploadFilter() {
        return this.uploadFilter;
    }

    public void setUploadFilter(UploadFilter filter) {
        this.uploadFilter = filter;
    }

    public void setFileStorage(FileStorage fs) {
        this.fileStorage = fs;
    }

    public FileStorage getFileStorage() {
        return this.fileStorage;
    }

    public void setRootPath(String path) throws IOException {
        this.rootPath = new File(GeoserverDataDirectory.getGeoserverDataDirectory(), path);
        myIDGenerator.setWatchedFolder(this.rootPath);
    }

    public String getRootPath() {
        return this.rootPath.toString();
    }

    public void handle(Request request, Response response){
        try {
            if (request.getMethod().equals(Method.GET)) {
                doGet(request, response);
            } else if (request.getMethod().equals(Method.POST)){
                doPost(request, response);
            } else {
                response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
        } catch (IOException ioe) {
            response.setStatus(Status.SERVER_ERROR_INTERNAL);
        }
    }

    /**
     * Handle a GET request by simply serving a static file.
     *
     * @param req the Request object being handled
     * @param resp the Response to which the file should be written
     */
    protected void doGet(Request req, Response resp)
        throws IOException {
            String file = (String)req.getAttributes().get("file");
            File f = getSecuredFile(file);

            if (file == null || !f.exists()){ 
                resp.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                return;
            }

            MediaType mimetype = new MediaType(new MimetypesFileTypeMap().getContentType(f));
            resp.setEntity(new FileRepresentation(f, mimetype, 10));
    }

    /**
     * Handle a POST request by reading in the file and possibly applying some filter.
     *
     * @param req the Request object being handled
     * @param resp the Response to which the result should be written
     */
    protected void doPost(Request req, Response resp)
        throws IOException {

        if (req.getEntity().getMediaType().equals(MediaType.MULTIPART_FORM_DATA, true)) {
            try {
                FileItemFactory factory = new DiskFileItemFactory();
                RestletFileUpload upload = new RestletFileUpload(factory);
                List items = upload.parseRequest(req);
                FileItem file = null;

                Iterator it = items.iterator();
                while (it.hasNext()) {
                    FileItem temp = (FileItem)it.next();
                    if (temp.getFieldName().equals("file") && !temp.isFormField()){
                        file = temp;
                    }
                }

                if (file == null){
                    resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    return;
                }
                
                if (uploadFilter.filter(file)) {
                    File temp = File.createTempFile("gs_upload", null);
                    file.write(temp);
                    resp.setEntity(doUpload(file, temp));
                    temp.delete();
                } else { 
                    resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            } catch (Exception e) {
                resp.setStatus(Status.SERVER_ERROR_INTERNAL);
            }
        } else { 
            if (uploadFilter.filter(req.getEntity())) {
                File temp = File.createTempFile("gs_upload", null);
                OutputStream tempStream = 
                    new BufferedOutputStream(new FileOutputStream(temp));

                FlatFileStorage.copyStream(req.getEntity().getStream(), tempStream);

                tempStream.flush();
                tempStream.close();

                resp.setEntity(doUpload(null, temp));

                temp.delete();
            } else { 
                resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
        }
    }

    private Representation doUpload(FileItem item, File content) throws IOException {
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }

        Map<String, List<String>> m = new HashMap<String, List<String>>();
        List<String> l = fileStorage.handleUpload(item, content, myIDGenerator, rootPath);
        m.put("files", l);
        DataFormat outputFormat = new MapJSONFormat();
        return outputFormat.toRepresentation(m);
    }

    /**
     * Ensure that files written and saved by this restlet are children of the target directory.
     *
     * @param path the name of the file within the target directory
     * @return the corresponding File object, or null if that File would not be a child of the 
     *     target dir
     * @throws IOException if errors occur while creating the File object
     */
    private File getSecuredFile(String path) throws IOException{
        File f = new File(rootPath, path);
        if (f.getCanonicalPath().startsWith(rootPath.getCanonicalPath())){
            return f;
        }
        return null;
    }
}

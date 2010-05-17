package org.geoserver.rest.upload;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
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
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.OutputRepresentation;
import org.restlet.ext.fileupload.RestletFileUpload;

import org.vfny.geoserver.global.GeoserverDataDirectory;

import org.geoserver.data.util.IOUtils;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.rest.format.MapJSONFormat;
import org.geoserver.rest.RestletException;

/**
 * Restlet to allow uploading files and serving them back, possibly with some server-side filter
 * applied.  For example, one might want to allow uploading images and then provide a thumbnail
 * version.
 *
 * @author David Winslow <dwinslow@opengeo.org>
 */
public class UploadRestlet extends Restlet {
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
            } else if (request.getMethod().equals(Method.PUT)){
                doPut(request, response);
            } else if (request.getMethod().equals(Method.DELETE)) {
                doDelete(request, response);
            } else {
                response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
        } catch (Exception ioe) {
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
        throws Exception {
            String file = (String)req.getAttributes().get("file");

            if (file == null) {
                listFiles(req, resp);
                return;
            }

            File f = getSecuredFile(file);

            if (!f.exists()){ 
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
    protected void doPost(Request req, Response resp) throws IOException {
        if (req.getEntity().getMediaType() != null && 
                req.getEntity().getMediaType().equals(MediaType.MULTIPART_FORM_DATA, true)) {
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

                File temp = File.createTempFile("gs_upload", null);
                file.write(temp);
                
                if (uploadFilter.filter(file.getContentType(), temp)) {
                    doUpload(file.getContentType(), temp, req, resp);
                    if (!temp.delete()) {
                        LOG.log(Level.WARNING, "Failure to delete temp file in upload module");
                    }
                } else { 
                    resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            } catch (Exception e) {
                resp.setStatus(Status.SERVER_ERROR_INTERNAL);
            }
        } else { 
            File temp = File.createTempFile("gs_upload", null);
            IOUtils.copy(req.getEntity().getStream(), temp);
            String contentType = "";

            if (req.getEntity().getMediaType() != null) {
                contentType = req.getEntity().getMediaType().toString();
            }

            if (uploadFilter.filter(contentType, temp)) {
                doUpload(contentType, temp, req, resp);

                if (!temp.delete()) {
                    LOG.log(Level.WARNING, "Unable to delete temporary file from upload");
                }
            } else { 
                resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
        }
    }

    /**
     * Handle a PUT request by ensuring that the requested file exists before overwriting it.
     *
     * @param req the Request being handled
     * @param resp the Response to use for reporting status
     */
    protected void doPut(Request req, Response resp) throws IOException {
        String file = (String)req.getAttributes().get("file");
        if (file == null) {
            resp.setStatus(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
            return;
        }

        File f = getSecuredFile(file);
        if (!f.exists()){ 
            resp.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return;
        }

        File temp = File.createTempFile("gs_upload", null);
        IOUtils.copy(req.getEntity().getStream(), temp);

        String contentType = "";
        if (req.getEntity().getMediaType() != null) {
            contentType = req.getEntity().getMediaType().toString();
        }

        if (uploadFilter.filter(contentType, temp)) {
            if (!temp.renameTo(f)) {
                throw new RestletException(
                        "Unable to rename temp file to persisted file in upload restlet",
                        Status.SERVER_ERROR_INTERNAL); 
            }
        } else {
            resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return;
        }
    }

    private void doUpload(String contentType, File content, Request req, Response resp) 
    throws IOException {
        if (!rootPath.exists() && !rootPath.mkdirs()) {
            throw new RestletException(
                    "Unable to create storage directory for uploaded files",
                    Status.SERVER_ERROR_INTERNAL); 
        }

        List<String> l = fileStorage.handleUpload(contentType, content, myIDGenerator, rootPath);
        Map<String, List<String>> m = new HashMap<String, List<String>>();
        m.put("files", l);
        DataFormat outputFormat = new MapJSONFormat();
        resp.setEntity(outputFormat.toRepresentation(m));
        resp.setStatus(Status.SUCCESS_CREATED);

        if (l.size() == 1) {
            resp.setRedirectRef(new Reference(req.getResourceRef(), l.get(0)));
        }
    }

    private void listFiles(Request req, Response resp) throws Exception {
        resp.setEntity(new OutputRepresentation(MediaType.APPLICATION_JSON) {
            public InputStream getStream() {
                throw new UnsupportedOperationException();
            }

            public void write(OutputStream out) throws IOException {
                PrintWriter writer = 
                    new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));

                writer.print("{maps: [");
                File[] children = rootPath.listFiles();
                for (int i = 0; i < children.length; i++) {
                    File child = children[i];

                    writer.print("{id:\"");
                    writer.print(child.getName().replace("\"", "\\\""));
                    writer.print("\",config:");

                    BufferedReader reader = new BufferedReader(new FileReader(child));

                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        writer.println(line);
                    }

                    writer.print("}");

                    if (i != children.length -1) {
                        writer.print(",");
                    };

                    reader.close();
                }

                writer.print("]}");
                writer.flush();
                writer.close();
            }
        });
    }

    /**
     * Handle a DELETE request by simply deleting the file on disk.
     *
     * @param req the Request object being handled
     * @param resp the Response to which the result should be written
     */
    protected void doDelete(Request req, Response resp)
        throws Exception {
            String file = (String)req.getAttributes().get("file");

            File f = getSecuredFile(file);

            if (!f.exists()){ 
                resp.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                return;
            }

            if (f.delete()) {
                resp.setStatus(Status.SUCCESS_OK);
            } else {
                throw new RestletException("Unable to delete file on server", 
                        Status.SERVER_ERROR_INTERNAL);
            }
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

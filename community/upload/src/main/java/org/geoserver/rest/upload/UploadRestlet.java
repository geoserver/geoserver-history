package org.geoserver.rest.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;
import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.FileRepresentation;
import org.restlet.ext.fileupload.RestletFileUpload;

import org.vfny.geoserver.global.GeoserverDataDirectory;

import org.geoserver.rest.DataFormat;
import org.geoserver.rest.JSONFormat;

/**
 * Restlet to allow uploading files and serving them back, possibly with some server-side filter
 * applied.  For example, one might want to allow uploading images and then provide a thumbnail
 * version.
 *
 * @author David Winslow <dwinslow@openplans.org>
 */
public class UploadRestlet extends Restlet{
    /**
     * The the directory in which files will be stored.
     */
    private static File ourRootPath;

    /**
     * A map of mimetype->file extension to use when generating filenames for stored files.
     */
    private Map mimeTypes;

    private static Logger LOG = org.geotools.util.logging.Logging.getLogger("org.geoserver.community");

    static private UniqueIDGenerator ourIDGenerator;

    static private ThumbnailFilter myThumbnailFilter = new ThumbnailFilter();

    static {
        ourIDGenerator = new UniqueIDGenerator();
    }

    public void setTypeMap(Map m){
        mimeTypes = m;
    }

    public Map getTypeMap(){
        return mimeTypes;
    }

    public void handle(Request request, Response response){
        if (ourRootPath == null){
            try{
                ourRootPath = GeoserverDataDirectory.findCreateConfigDir("uploads");
                ourIDGenerator.setWatchedFolder(ourRootPath);
            } catch (Exception e){
                LOG.info("Error while setting up the upload restlet: " + e);
                e.printStackTrace();
            }
        }

        try{
            if (request.getMethod().equals(Method.GET)){
                doGet(request, response);
            } else if (request.getMethod().equals(Method.POST)){
                doPost(request, response);
            } else {
                response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
        } catch (IOException ioe){
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
            try{
                FileItemFactory factory = new DiskFileItemFactory();
                RestletFileUpload upload = new RestletFileUpload(factory);
                List items = upload.parseRequest(req);
                FileItem file = null;

                Iterator it = items.iterator();
                while (it.hasNext()){
                    FileItem temp = (FileItem)it.next();
                    if (temp.getFieldName().equals("file") && !temp.isFormField()){
                        file = temp;
                    }
                }

                if (file == null || !file.getContentType().startsWith("image/")){
                    resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                } else /*if (file.getContentType().startsWith("image/"))*/{
                    Map m = new HashMap();
                    List l = myThumbnailFilter.handleUpload(file, ourIDGenerator, ourRootPath);
                    m.put("files", l);
                    DataFormat outputFormat = new JSONFormat(MediaType.TEXT_PLAIN);
                    resp.setEntity(outputFormat.makeRepresentation(m));
                } 
            } catch (Exception e){
                resp.setStatus(Status.SERVER_ERROR_INTERNAL);
                e.printStackTrace();
            }
    }

    /**
     * Ensure that files written and saved by this restlet are children of the target directory.
     *
     * @param path the name of the file within the target directory
     * @return the corresponding File object, or null if that File would not be a child of the target dir
     * @throws IOException if errors occur while creating the File object
     */
    private File getSecuredFile(String path) throws IOException{
        File f = new File(ourRootPath, path);
        if (f.getCanonicalPath().startsWith(ourRootPath.getCanonicalPath())){
            return f;
        }
        return null;
    }
}

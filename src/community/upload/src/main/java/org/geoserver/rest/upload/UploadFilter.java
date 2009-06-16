package org.geoserver.rest.upload;

import org.restlet.resource.Representation;
import org.apache.commons.fileupload.FileItem;

import java.io.IOException;

/**
 * The UploadFilter interface defines a mechanism for restricting uploaded files based on their 
 * content.  For example, one could restrict based on mimetype or adherence to a particular XML
 * schema.
 *
 * @author David Winslow <dwinslow@opengeo.org>
 */
public interface UploadFilter {
    /**
     * Assess a FileItem for acceptability according to this filter's criterion.
     *
     * @param file the FileItem that was uploaded
     * @return a boolean value; true if the upload should be accepted, false otherwise
     * @throws IOException if an error occurs while trying to read the data
     */
    public boolean filter(FileItem file) throws IOException;

    /**
     * Assess a Restlet Representation for acceptability according to this filter's criterion.
     *
     * @param rep the Representation for consideration
     * @return a boolean value; true if the upload should be accepted, false otherwise
     * @throws IOException if an error occurs while trying to read the data
     */
    public boolean filter(Representation repr) throws IOException;
}

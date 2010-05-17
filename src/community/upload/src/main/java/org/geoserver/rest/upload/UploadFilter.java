package org.geoserver.rest.upload;

import org.restlet.resource.Representation;

import java.io.File;
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
     * @param contentType the reported mimetype of the uploaded data
     * @param content a temporary File with the data that was uploaded
     * @return a boolean value; true if the upload should be accepted, false otherwise
     * @throws IOException if an error occurs while trying to read the data
     */
    public boolean filter(String contentType, File content) throws IOException;
}

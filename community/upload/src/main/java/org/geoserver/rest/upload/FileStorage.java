package org.geoserver.rest.upload;

import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * The FileStorage interface provides a method for storing files to some medium.
 *
 * @author David Winslow <dwinslow@opengeo.org>
 */
public interface FileStorage {
    /**
     * Store a file from an upload.
     *
     * @param file the FileItem from the upload
     * @param generator a UniqueIDGenerator that can be used to ensure uniqueness of ids for new 
     *     files
     *
     * @return a List<String> containing identifiers that the client can use to retrieve the 
     *     uploaded files
     */
    public List<String> handleUpload(
        String contentType, 
        File content,
        UniqueIDGenerator generator, 
        File uploadDirectory
    ) throws IOException;
}

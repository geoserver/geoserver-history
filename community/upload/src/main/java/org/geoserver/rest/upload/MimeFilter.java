package org.geoserver.rest.upload;

import java.io.File;
import java.util.regex.Pattern;

/**
 * The MimeFilter class implements an upload filter based on reported mimetype.  The mimetype is 
 * matched against a regular expression, modifiable via the setMimePattern() method.
 *
 * @author David Winslow <dwinslow@opengeo.org>
 */
public class MimeFilter implements UploadFilter {
    private Pattern mimePattern;

    /**
     * Set the mime pattern for this filter.
     * 
     * @param p the pattern as a String containing a regular expression compatible with 
     *     java.util.regex.Pattern
     */
    public void setMimePattern(String p) {
        mimePattern = Pattern.compile(p);
    }

    public boolean filter(String contentType, File file) {
        return mimePattern.matcher(contentType).matches();
    }
}

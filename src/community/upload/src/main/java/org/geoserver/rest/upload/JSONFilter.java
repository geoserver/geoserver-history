package org.geoserver.rest.upload;

import net.sf.json.JSON;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * The JSONFilter class implements an upload filter which refuses files that are not valid JSON.  
 * The mimetype is matched against a regular expression, modifiable via the setMimePattern() method.
 *
 * @author David Winslow <dwinslow@opengeo.org>
 */
public class JSONFilter implements UploadFilter {
    public boolean filter(String contentType, File file) throws IOException {
        JSONObject json = parseStream(new FileInputStream(file));
        return json != null && !json.isNullObject() && !json.isEmpty();
    }

    private JSONObject parseStream(InputStream stream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return JSONObject.fromObject(builder.toString());
        } catch (Exception e) {
            // todo: logging here
            e.printStackTrace();
        }

        return null;
    }
}

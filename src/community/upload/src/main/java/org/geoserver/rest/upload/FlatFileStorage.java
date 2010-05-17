package org.geoserver.rest.upload;

import org.apache.commons.fileupload.FileItem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple FileStorage implementation that simply writes an uploaded file as-is to disk.
 *
 * @author David Winslow <dwinslow@opengeo.org>
 */
public class FlatFileStorage implements FileStorage {
    public List<String> handleUpload(
        String contentType, 
        File content,
        UniqueIDGenerator generator, 
        File uploadDirectory
    ) throws IOException {
        String originalName = "";

        String name = generator.generate(originalName); 

        InputStream in = new BufferedInputStream(new FileInputStream(content));

        File storedFile = new File(uploadDirectory, name);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(storedFile));

        copyStream(in, out);

        in.close();
        out.flush();
        out.close();

        List<String> result = new ArrayList<String>();
        result.add(name);
        return result;
    }

    static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buff = new byte[1024];
        int len = 0;

        while ((len = in.read(buff, 0, buff.length)) >= 0) {
            out.write(buff, 0, len);
        }
    }
}

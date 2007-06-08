package org.geoserver.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A simple class to support reloadable property files. Watches last modified
 * date on the specified file, and allows to read a Properties out of it.
 * 
 * @author Administrator
 * 
 */
public class PropertyFileWatcher {
    File file;

    private long lastModified = Long.MIN_VALUE;

    public PropertyFileWatcher(File file) {
        this.file = file;
    }

    public Properties getProperties() throws IOException {
        Properties p = new Properties();
        if (file.exists()) {
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                p.load(is);
                lastModified = file.lastModified();
            } finally {
                if (is != null)
                    is.close();
            }
        }
        return p;
    }

    public boolean isStale() {
        return file.exists() && file.lastModified() > lastModified;
    }
}

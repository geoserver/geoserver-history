/**
 * 
 */
package org.vfny.geoserver.control.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Implementation backing onto a properties file.
 *
 * @author Jesse
 */
public class PropertyPreferenceStoreStrategy implements IPreferenceStoreStrategy {
    private Properties properties;
    private String file;

    public PropertyPreferenceStoreStrategy(String propertiesFile)
        throws IOException {
        properties = new Properties();

        File file = new File(propertiesFile);

        if (file.exists()) {
            properties.load(new FileInputStream(propertiesFile));
        }

        this.file = propertiesFile;
    }

    public PropertyPreferenceStoreStrategy(Properties properties) {
        this.properties = properties;
    }

    public synchronized String get(String key) {
        return properties.getProperty(key);
    }

    /* (non-Javadoc)
     * @see org.vfny.geoserver.control.internal.IPreferenceStore#put(java.lang.String, java.lang.String)
     */
    public synchronized void put(String key, String value) {
        properties.put(key, value);
    }

    public synchronized void flush() throws IOException {
        File to = new File(file);

        if (!to.exists()) {
            to.createNewFile();
        }

        properties.store(new FileOutputStream(to),
            "Geoserver preferences store used by the Preferences Module");
    }

    public synchronized void unset(String key) {
        properties.remove(key);
    }

    public String[] keys() {
        return (String[]) properties.keySet().toArray(new String[0]);
    }
}

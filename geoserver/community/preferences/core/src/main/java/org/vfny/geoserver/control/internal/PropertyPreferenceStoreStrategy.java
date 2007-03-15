/* Copyright (c) 2001 - 2007 TOPP - http://topp.openplans.org.
 * All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible in the
 * license.txt file of the documents directory off the root directory.
 */
package org.vfny.geoserver.control.internal;

import org.vfny.geoserver.control.IValidator;
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
    private IValidator validator;

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

    public synchronized void put(String key, String value) {
        if (validator.isValid(key, value)) {
            properties.put(key, value);
        }
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

    public void setValidator(IValidator validator) {
        this.validator = validator;
    }
}

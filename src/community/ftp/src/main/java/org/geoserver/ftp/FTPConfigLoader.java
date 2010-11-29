/* Copyright (c) 2001 - 2010 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.geoserver.config.util.XStreamPersister;
import org.geoserver.config.util.XStreamPersisterFactory;
import org.geoserver.platform.GeoServerResourceLoader;

/**
 * Loads and saves the GeoSever FTP Service {@link FTPConfig configuration} from and to the
 * {@code ftp.xml} file inside the GeoServer data directory.
 * 
 * @author groldan
 * 
 */
class FTPConfigLoader {

    private static final String CONFIG_FILE_NAME = "ftp.xml";

    private GeoServerResourceLoader resourceLoader;

    private XStreamPersisterFactory persisterFactory;

    public FTPConfigLoader(final GeoServerResourceLoader resourceLoader,
            final XStreamPersisterFactory persisterFactory) {
        this.resourceLoader = resourceLoader;
        this.persisterFactory = persisterFactory;
    }

    public FTPConfig load() {
        InputStream in = null;
        try {
            File configFile = findConfigFile();
            if (!configFile.exists()) {
                FTPConfig ftpConfig = new FTPConfig();
                save(ftpConfig);
                return ftpConfig;
            }
            in = new FileInputStream(configFile);
            XStreamPersister xmlPersister = getPersister();
            FTPConfig config = xmlPersister.load(in, FTPConfig.class);
            return config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignore) {
                    // ignore
                }
            }
        }
    }

    private XStreamPersister getPersister() {
        XStreamPersister xmlPersister = persisterFactory.createXMLPersister();
        xmlPersister.getXStream().alias("ftp", FTPConfig.class);
        return xmlPersister;
    }

    public void save(FTPConfig config) {
        File configFile;
        OutputStream out = null;
        try {
            configFile = findConfigFile();
            out = new FileOutputStream(configFile);
            XStreamPersister xmlPersister = getPersister();
            xmlPersister.save(config, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private File findConfigFile() throws IOException {
        File configFile = resourceLoader.find(CONFIG_FILE_NAME);
        if (configFile == null) {
            configFile = new File(resourceLoader.getBaseDirectory(), CONFIG_FILE_NAME);
        }
        return configFile;
    }
}

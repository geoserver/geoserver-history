package org.geoserver.gwc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.catalog.MetadataMap;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInitializer;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.config.util.XStreamPersisterFactory;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.wms.WMSInfo;
import org.geotools.util.logging.Logging;

import com.thoughtworks.xstream.XStream;

/**
 * Initializes the integrated GWC based on GeoServer specific GWC configuration at
 * {@code <data dir>/gwc-gs.xml}
 * 
 * @author groldan
 * 
 */
public class GWCConfigPersister implements GeoServerInitializer {

    private static final Logger LOGGER = Logging.getLogger(GWCConfigPersister.class);

    private static final String GWC_CONFIG_FILE = "gwc-gs.xml";

    private final XStreamPersisterFactory persisterFactory;

    private final GeoServerResourceLoader resourceLoader;

    private GWCConfig config;

    public GWCConfigPersister(final XStreamPersisterFactory xspf,
            final GeoServerResourceLoader resourceLoader) {
        this.persisterFactory = xspf;
        this.resourceLoader = resourceLoader;
    }

    /**
     * @see org.geoserver.config.GeoServerInitializer#initialize(org.geoserver.config.GeoServer)
     */
    public void initialize(final GeoServer geoServer) throws Exception {
        LOGGER.info("Initializing GeoServer specific GWC configuration from " + GWC_CONFIG_FILE);
        final File configFile = resourceLoader.find(GWC_CONFIG_FILE);
        if (configFile == null) {
            LOGGER.fine("GWC's GeoServer specific configuration not found, creating with old defaults");
            GWCConfig oldDefaults = GWCConfig.getOldDefaults();
            save(oldDefaults);
        } else {
            XStreamPersister xmlPersister = this.persisterFactory.createXMLPersister();
            configureXstream(xmlPersister.getXStream());
            try {
                InputStream in = new FileInputStream(configFile);
                try {
                    this.config = xmlPersister.load(in, GWCConfig.class);
                } finally {
                    in.close();
                }
                LOGGER.fine("GWC GeoServer specific configuration loaded from " + GWC_CONFIG_FILE);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error loading GWC GeoServer specific "
                        + "configuration from " + configFile.getAbsolutePath()
                        + ". Applying defaults.", e);
                this.config = new GWCConfig();
            }
        }

        // Check whether we're using the old way of storing this information, and get rid of it
        WMSInfo service = geoServer.getService(WMSInfo.class);
        if (service != null) {
            MetadataMap metadata = service.getMetadata();
            if (service != null && metadata != null) {
                String WMS_INTEGRATION_ENABLED_KEY = "GWC_WMS_Integration";
                Boolean storedValue = metadata.get(WMS_INTEGRATION_ENABLED_KEY, Boolean.class);
                if (storedValue != null) {
                    boolean enabled = storedValue.booleanValue();
                    this.config.setDirectWMSIntegrationEnabled(enabled);
                    metadata.remove(WMS_INTEGRATION_ENABLED_KEY);
                    geoServer.save(service);
                }
            }
        }
    }

    public GWCConfig getConfig() {
        return config;
    }

    /**
     * Saves and applies the integrated GWC's GeoServer specific configuration to the
     * {@code <data dir>/gwc-gs.xml} file.
     * 
     * @param config
     * 
     * @throws IOException
     */
    public void save(final GWCConfig config) throws IOException {
        LOGGER.finer("Saving integrated GWC configuration");
        File tmp = new File(getConfigRoot(), GWC_CONFIG_FILE + ".tmp");
        XStreamPersister xmlPersister = this.persisterFactory.createXMLPersister();
        configureXstream(xmlPersister.getXStream());
        OutputStream out = new FileOutputStream(tmp);
        try {
            xmlPersister.save(config, out);
        } finally {
            out.close();
        }
        File configFile = new File(getConfigRoot(), GWC_CONFIG_FILE);
        tmp.renameTo(configFile);
        this.config = config;
        LOGGER.finer("Integrated GWC configuration saved to " + configFile.getAbsolutePath());
    }

    private void configureXstream(XStream xs) {
        xs.alias("GeoServerGWCConfig", GWCConfig.class);
        xs.alias("defaultCachingGridSetIds", ArrayList.class);
        xs.alias("defaultCoverageCacheFormats", ArrayList.class);
        xs.alias("defaultVectorCacheFormats", ArrayList.class);
        xs.alias("defaultOtherCacheFormats", ArrayList.class);
    }

    private File getConfigRoot() {
        return this.resourceLoader.getBaseDirectory();
    }
}

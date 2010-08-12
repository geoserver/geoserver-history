package org.geoserver.gwc;

import org.geoserver.catalog.Catalog;
import org.geowebcache.util.ApplicationContextProvider;

public class GeoServerDataDirLocator extends org.geowebcache.storage.DefaultStorageFinder {

    private final String path;
    
    public GeoServerDataDirLocator(ApplicationContextProvider ctxProvider, Catalog cat) {
        // Ignored
        super(ctxProvider);
        path = cat.getResourceLoader().getBaseDirectory().getAbsolutePath() + "/gwc";
    }
    
    public synchronized String getDefaultPath() {
        return path;
    }
}

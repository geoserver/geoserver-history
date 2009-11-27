package org.geoserver.gwc;

import java.util.logging.Logger;

import org.geotools.util.logging.Logging;
import org.geowebcache.layer.wms.WMSLayer;
import org.geowebcache.mime.MimeType;
import org.geowebcache.storage.StorageBroker;
import org.geowebcache.storage.StorageException;
import org.geowebcache.storage.TileRangeObject;

public class GWCCleanser {
    private static Logger log = Logging.getLogger("org.geoserver.gwc.GWCCleanser");
    
    final private StorageBroker sb;
    
    public GWCCleanser(StorageBroker sb) {
        this.sb = sb;
    }
    
    public void deleteLayer(String layerName) {
        CleanserThread thread = new CleanserThread(layerName);
        thread.start();
    }
    
    public void expireLayer(WMSLayer layer) {
        // Synchronously mark all tiles as expired
        // TODO
        //(String layerName, String gridSetId, int zoomStart, 
        //        int zoomStop, long[][] rangeBounds, MimeType mimeType, String parameters);
        
        //TileRangeObject trObj = new TileRangeObject(null, null, 0, 0, null, null, null);
        
        //try {
        //    sb.expire(trObj);
        //} catch (StorageException e) {
        //    log.severe("Expiring cache for "+layer.getName()+ " failed: " + e.getMessage());
        //}
        
        // Delete all tiles in a new thread
        deleteLayer(layer.getName());
    }
    
    private class CleanserThread extends Thread {
        String layerName;
        
        CleanserThread(String layerName) {
            this.layerName = layerName;
        }

        public void run() {
            log.info("Deleting GWC cache for " + layerName);
            try {
                sb.delete(layerName);
            } catch (StorageException e) {
                log.severe("Failed to clear cache for " + layerName + ", " + e.getMessage());
            }
            log.info("GWC cache for " + layerName + " deleted successfully");
        }

    }
}

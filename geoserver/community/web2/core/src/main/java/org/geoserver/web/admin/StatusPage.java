package org.geoserver.web.admin;

import org.geoserver.web.GeoServerBasePage;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.jai.JAIInfo;
import org.geotools.data.DataStore;
import org.geotools.data.LockingManager;

import javax.media.jai.JAI;

import java.util.List;
import java.util.Iterator;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import com.sun.media.jai.util.SunTileCache;

public class StatusPage extends ServerAdminPage {
    public StatusPage(){
        //TODO: if we just provide the values directly as the models they won't be refreshed on a page reload (ugh).
        add(new Label("locks", Long.toString(getLockCount())));
        add(new Label("connections", Long.toString(getConnectionCount())));
        add(new Label("memory", Long.toString(Runtime.getRuntime().freeMemory() / 1024) + "kB"));
        add(new Label("jvm.version", System.getProperty("java.vendor") + ": " + System.getProperty("java.version")));
        add(new Label("jai.available", 
                    Boolean.toString(ClassLoader.getSystemClassLoader().getResource("javax/media/jai/buildVersion") != null))
           );

        JAI jai = ((JAIInfo) 
                getGeoServerApplication()
                .getGeoServer()
                .getGlobal()
                .getMetadata()
                .get(JAIInfo.KEY))
            .getJAI();

        SunTileCache jaiCache = ((JAIInfo) 
                getGeoServerApplication()
                .getGeoServer()
                .getGlobal()
                .getMetadata()
                .get(JAIInfo.KEY))
            .getTileCache();

        add(new Label("jai.memory.available",
                    Long.toString(jaiCache.getMemoryCapacity()))
           );

        add(new Label("jai.memory.used", 
                    Long.toString(jaiCache.getCacheMemoryUsed()))
           );
        add(new Label("jai.memory.threshold",
                    Float.toString(100.0f * jaiCache.getMemoryThreshold()))
           );
        add(new Label("jai.tile.threads", 
                    Integer.toString(jai.getTileScheduler().getParallelism()))
           );
        add(new Label("jai.tile.priority",
                    Integer.toString(jai.getTileScheduler().getPriority()))
           );

        add(new Link("free.locks"){
                public void onClick(){
                }
            });
        add(new Link("free.memory"){
                public void onClick(){
                }
            });
        add(new Link("free.memory.jai"){
                public void onClick(){
                }
            });
        add(new Link("persist"){
                public void onClick(){
                }
            });
        add(new Link("revert"){
                public void onClick(){
                }
            });

        add(new Label("reload.date.geoserver", "Jul 14, 3:07 PM"));
        add(new Label("reload.date.configuration", "Jul 14, 3:07 PM"));
        add(new Label("reload.date.xml", "Mar 14, 2:15 PM"));
    }

    private synchronized int getLockCount(){
        int count = 0;

        for (Iterator i = getDataStores().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                // Don't count locks from disabled datastores.
                continue;
            }

            try {
                DataStore store = meta.getDataStore(null);
                LockingManager lockingManager = store.getLockingManager();
                if (lockingManager != null){
                    // we can't actually *count* locks right now?
                    // count += lockingManager.getLockSet().size();
                }
            } catch (IllegalStateException notAvailable) {
                continue; 
            } catch (Throwable huh) {
                continue;
            }
        }

        return count;
    }

    private synchronized int getConnectionCount() {
        int count = 0;

        for (Iterator i = getDataStores().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                // Don't count connections from disabled datastores.
                continue; 
            }

            try {
                DataStore dataStore = meta.getDataStore(null);
            } catch (Throwable notAvailable) {
                //TODO: Logging.
                continue; 
            }

            count += 1;
        }

        return count;
    }

    private List<DataStoreInfo> getDataStores(){
        return getGeoServerApplication().getGeoServer().getCatalog().getDataStores();
    }

}

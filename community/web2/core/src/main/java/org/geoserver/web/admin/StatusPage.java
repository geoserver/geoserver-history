package org.geoserver.web.admin;

import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.util.MapModel;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.jai.JAIInfo;
import org.geotools.data.DataStore;
import org.geotools.data.LockingManager;
import org.vfny.geoserver.global.GeoServer;

import javax.media.jai.JAI;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import com.sun.media.jai.util.SunTileCache;

public class StatusPage extends ServerAdminPage {

    /**
     * The map used as the model source so the label contents are updated
     */
    private final Map<String, String> values;

    private static final String KEY_LOCKS = "locks";

    private static final String KEY_CONNECTIONS = "connections";

    private static final String KEY_MEMORY = "memory";

    private static final String KEY_JVM_VERSION = "jvm_version";

    private static final String KEY_JAI_AVAILABLE = "jai_available";

    private static final String KEY_JAI_MAX_MEM = "jai_max_mem";

    private static final String KEY_JAI_MEM_USAGE = "jai_mem_usage";

    private static final String KEY_JAI_MEM_THRESHOLD = "jai_mem_threshold";

    private static final String KEY_JAI_TILE_THREADS = "jai_tile_threads";

    private static final String KEY_JAI_TILE_THREAD_PRIORITY = "jai_tile_thread_priority";

    public StatusPage() {
        values = new HashMap<String, String>();
        updateModel();

        // TODO: if we just provide the values directly as the models they won't
        // be refreshed on a page reload (ugh).
        add(new Label("locks", new MapModel(values, KEY_LOCKS)));
        add(new Label("connections", new MapModel(values, KEY_CONNECTIONS)));
        add(new Label("memory", new MapModel(values, KEY_MEMORY)));
        add(new Label("jvm.version", new MapModel(values, KEY_JVM_VERSION)));
        add(new Label("jai.available", new MapModel(values, KEY_JAI_AVAILABLE)));
        add(new Label("jai.memory.available", new MapModel(values, KEY_JAI_MAX_MEM)));
        add(new Label("jai.memory.used", new MapModel(values, KEY_JAI_MEM_USAGE)));
        add(new Label("jai.memory.threshold", new MapModel(values, KEY_JAI_MEM_THRESHOLD)));
        add(new Label("jai.tile.threads", new MapModel(values, KEY_JAI_TILE_THREADS)));
        add(new Label("jai.tile.priority", new MapModel(values, KEY_JAI_TILE_THREAD_PRIORITY)));

        add(new Link("free.locks") {
            private static final long serialVersionUID = 1L;

            public void onClick() {
                // TODO: see GEOS-2130
                updateModel();
            }
        });
        add(new Link("free.memory") {
            private static final long serialVersionUID = 1L;

            public void onClick() {
                System.gc();
                System.runFinalization();
                updateModel();
            }
        });
        
        add(new Link("free.memory.jai") {
            private static final long serialVersionUID = 1L;

            public void onClick() {
                SunTileCache jaiCache = ((JAIInfo) getGeoServer().getGlobal().getMetadata().get(
                        JAIInfo.KEY)).getTileCache();
                final long capacityBefore = jaiCache.getMemoryCapacity();
                jaiCache.flush();
                jaiCache.setMemoryCapacity(0); // to be sure we realease all tiles
                System.gc();
                System.runFinalization();
                jaiCache.setMemoryCapacity(capacityBefore);
            }
        });

        add(new Label("reload.date.geoserver", "Jul 14, 3:07 PM"));
        add(new Label("reload.date.configuration", "Jul 14, 3:07 PM"));
        add(new Label("reload.date.xml", "Mar 14, 2:15 PM"));
    }

    private void updateModel() {
        values.put(KEY_LOCKS, Long.toString(getLockCount()));
        values.put(KEY_CONNECTIONS, Long.toString(getConnectionCount()));
        values.put(KEY_MEMORY, formatUsedMemory());
        values.put(KEY_JVM_VERSION, System.getProperty("java.vendor") + ": "
                + System.getProperty("java.version"));
        values.put(KEY_JAI_AVAILABLE, Boolean.toString(ClassLoader.getSystemClassLoader()
                .getResource("javax/media/jai/buildVersion") != null));

        JAI jai = ((JAIInfo) getGeoServer().getGlobal().getMetadata().get(JAIInfo.KEY)).getJAI();

        SunTileCache jaiCache = ((JAIInfo) getGeoServer().getGlobal().getMetadata()
                .get(JAIInfo.KEY)).getTileCache();

        values.put(KEY_JAI_MAX_MEM, Long.toString(jaiCache.getMemoryCapacity()));
        values.put(KEY_JAI_MEM_USAGE, Long.toString(jaiCache.getCacheMemoryUsed()));
        values.put(KEY_JAI_MEM_THRESHOLD, Float.toString(100.0f * jaiCache.getMemoryThreshold()));
        values.put(KEY_JAI_TILE_THREADS, Integer.toString(jai.getTileScheduler().getParallelism()));
        values.put(KEY_JAI_TILE_THREAD_PRIORITY, Integer.toString(jai.getTileScheduler()
                .getPriority()));
    }

    /**
     * @return a human friendly string for the VM used memory
     */
    private String formatUsedMemory() {
        final Runtime runtime = Runtime.getRuntime();
        final double usedBytes = runtime.totalMemory() - runtime.freeMemory();
        final long KB = 1024;
        final long MB = KB * KB;
        final long GB = KB * MB;
        final NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(2);

        String formattedUsedMemory;
        if (usedBytes > GB) {
            formattedUsedMemory = formatter.format(usedBytes / GB) + " GB";
        } else if (usedBytes > MB) {
            formattedUsedMemory = formatter.format(usedBytes / MB) + " MB";
        } else {
            formattedUsedMemory = formatter.format(usedBytes / KB) + " KB";
        }

        return formattedUsedMemory;
    }

    private synchronized int getLockCount() {
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
                if (lockingManager != null) {
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
                // TODO: Logging.
                continue;
            }

            count += 1;
        }

        return count;
    }

    private List<DataStoreInfo> getDataStores() {
        return getGeoServer().getCatalog().getDataStores();
    }

}

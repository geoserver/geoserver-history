package org.geoserver.gwc;

import static org.geowebcache.seed.GWCTask.TYPE.TRUNCATE;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.geowebcache.GeoWebCacheException;
import org.geowebcache.grid.BoundingBox;
import org.geowebcache.grid.GridSet;
import org.geowebcache.grid.GridSubset;
import org.geowebcache.grid.SRS;
import org.geowebcache.layer.TileLayer;
import org.geowebcache.layer.TileLayerDispatcher;
import org.geowebcache.mime.MimeType;
import org.geowebcache.seed.GWCTask;
import org.geowebcache.seed.TileBreeder;
import org.geowebcache.storage.StorageBroker;
import org.geowebcache.storage.StorageException;
import org.geowebcache.storage.TileRange;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.DisposableBean;

/**
 * Spring bean acting as a facade to GWC for the GWC/GeoServer integration classes so that they
 * don't need to worry about GWC complexities nor API changes.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * 
 */
public class GWC implements DisposableBean {

    private static Logger log = Logging.getLogger("org.geoserver.gwc.GWC");

    private final CatalogConfiguration config;

    private final TileLayerDispatcher tld;

    private final StorageBroker storageBroker;

    private final TileBreeder tileBreeder;

    public GWC(final StorageBroker sb, final TileLayerDispatcher tld,
            final TileBreeder tileBreeder, final CatalogConfiguration config) {
        this.tld = tld;
        this.storageBroker = sb;
        this.tileBreeder = tileBreeder;
        this.config = config;
    }

    public void truncate(final String layerName) {
        try {
            // TODO: async
            storageBroker.delete(layerName);
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }
    }

    public void truncate(final String layerName, final ReferencedEnvelope bounds)
            throws GeoWebCacheException {

        final TileLayer tileLayer = tld.getTileLayer(layerName);

        List<GWCTask> truncateTasks = new ArrayList<GWCTask>(tileLayer.getGridSubsets().size()
                * tileLayer.getMimeTypes().size());
        /*
         * Create a truncate task for each gridSubset (CRS) and format
         */
        for (GridSubset layerGrid : tileLayer.getGridSubsets().values()) {
            final GridSet gridSet = layerGrid.getGridSet();
            final String gridSetId = gridSet.getName();
            final SRS srs = gridSet.getSRS();
            CoordinateReferenceSystem gridSetCrs;
            try {
                gridSetCrs = CRS.decode("EPSG:" + srs.getNumber());
            } catch (Exception e) {
                throw new RuntimeException("Can't decode SRS for layer '" + layerName + "': ESPG:"
                        + srs.getNumber());
            }

            ReferencedEnvelope truncateBounds;

            try {
                truncateBounds = bounds.transform(gridSetCrs, true);
            } catch (Exception e) {
                throw new RuntimeException("Can't transform requested bounds to layer gridset "
                        + gridSetId);
            }

            // TODO: make sure bounds is in the same CRS than the layer
            final double minx = truncateBounds.getMinX();
            final double miny = truncateBounds.getMinY();
            final double maxx = truncateBounds.getMaxX();
            final double maxy = truncateBounds.getMaxY();
            final BoundingBox reqBounds = new BoundingBox(minx, miny, maxx, maxy);
            final long[][] coverageIntersections = layerGrid.getCoverageIntersections(reqBounds);
            final int zoomStart = layerGrid.getZoomStart();
            final int zoomStop = layerGrid.getZoomStop();
            final String parameters = null;// how do I get these?

            for (MimeType mime : tileLayer.getMimeTypes()) {
                TileRange tileRange;
                tileRange = new TileRange(layerName, gridSetId, zoomStart, zoomStop,
                        coverageIntersections, mime, parameters);
                GWCTask[] singleTask;
                singleTask = tileBreeder.createTasks(tileRange, tileLayer, TRUNCATE, 1, false);
                truncateTasks.add(singleTask[0]);
            }

        }

        GWCTask[] tasks = truncateTasks.toArray(new GWCTask[truncateTasks.size()]);
        tileBreeder.dispatchTasks(tasks);
    }

    /**
     * 
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    public void destroy() throws Exception {
    }

    public void addOrReplaceLayer(TileLayer layer) {
        tld.getLayers();
        tld.add(layer);
        log.finer(layer.getName() + " added to TileLayerDispatcher");
    }

    public synchronized void removeLayer(String prefixedName) {
        truncate(prefixedName);
        config.removeLayer(prefixedName);
        tld.remove(prefixedName);
    }

    public void reload() {
        try {
            tld.reInit();
        } catch (GeoWebCacheException gwce) {
            log.fine("Unable to reinit TileLayerDispatcher gwce.getMessage()");
        }
    }

    public void createLayer(LayerInfo layerInfo) {
        TileLayer tileLayer = config.createLayer(layerInfo);
        addOrReplaceLayer(tileLayer);
    }

    public void createLayer(LayerGroupInfo lgi) {
        TileLayer tileLayer = config.createLayer(lgi);
        addOrReplaceLayer(tileLayer);
    }

}

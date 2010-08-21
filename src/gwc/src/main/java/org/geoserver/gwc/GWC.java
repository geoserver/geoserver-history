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
            final CoordinateReferenceSystem gridSetCrs;
            try {
                gridSetCrs = CRS.decode("EPSG:" + srs.getNumber());
            } catch (Exception e) {
                throw new RuntimeException("Can't decode SRS for layer '" + layerName + "': ESPG:"
                        + srs.getNumber());
            }

            ReferencedEnvelope truncateBoundsInGridsetCrs;

            try {
                truncateBoundsInGridsetCrs = bounds.transform(gridSetCrs, true);
            } catch (Exception e) {
                log.info("Can't truncate layer " + layerName
                        + ": error transforming requested bounds to layer gridset " + gridSetId
                        + ": " + e.getMessage());
                continue;
            }

            final double minx = truncateBoundsInGridsetCrs.getMinX();
            final double miny = truncateBoundsInGridsetCrs.getMinY();
            final double maxx = truncateBoundsInGridsetCrs.getMaxX();
            final double maxy = truncateBoundsInGridsetCrs.getMaxY();
            final BoundingBox reqBounds = new BoundingBox(minx, miny, maxx, maxy);
            /*
             * layerGrid.getCoverageIntersections is not too robust, so we better check the
             * requested bounds intersect the layer bounds
             */
            final BoundingBox layerBounds = layerGrid.getCoverageBestFitBounds();
            if (!layerBounds.intersects(reqBounds)) {
                log.fine("Requested truncation bounds do not intersect cached layer bounds, ignoring truncate request");
                continue;
            }
            final BoundingBox intersection = BoundingBox.intersection(layerBounds, reqBounds);
            final long[][] coverageIntersections = layerGrid.getCoverageIntersections(intersection);
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

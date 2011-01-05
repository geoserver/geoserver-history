/* Copyright (c) 2010 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.gwc;

import static org.geowebcache.seed.GWCTask.TYPE.TRUNCATE;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.wms.GetMapRequest;
import org.geoserver.wms.MapLayerInfo;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.geowebcache.GeoWebCacheException;
import org.geowebcache.conveyor.ConveyorTile;
import org.geowebcache.grid.BoundingBox;
import org.geowebcache.grid.GridSet;
import org.geowebcache.grid.GridSubset;
import org.geowebcache.grid.SRS;
import org.geowebcache.layer.TileLayer;
import org.geowebcache.layer.TileLayerDispatcher;
import org.geowebcache.mime.MimeException;
import org.geowebcache.mime.MimeType;
import org.geowebcache.seed.GWCTask;
import org.geowebcache.seed.TileBreeder;
import org.geowebcache.storage.StorageBroker;
import org.geowebcache.storage.StorageException;
import org.geowebcache.storage.TileRange;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;

import com.vividsolutions.jts.geom.Envelope;

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

    /**
     * Tries to dispatch a tile request represented by a GeoServer WMS {@link GetMapRequest} through
     * GeoWebCache, and returns the {@link ConveyorTile} if succeeded or {@code null} if it wasn't
     * possible.
     * <p>
     * Preconditions:
     * <ul>
     * <li><code>{@link GetMapRequest#isTiled() request.isTiled()} == true</code>
     * </ul>
     * </p>
     * 
     * @param request
     * @return
     */
    public ConveyorTile dispatch(final GetMapRequest request) {
        Assert.isTrue(request.isTiled(), "isTiled");
        // Assert.notNull(request.getTilesOrigin(), "getTilesOrigin");

        if (!isCachingPossible(request)) {
            return null;
        }

        // request.isTransparent()??
        // request.getEnv()??
        // request.getFormatOptions()??
        final List<MapLayerInfo> layers = request.getLayers();
        if (layers.size() != 1) {
            return null;
        }
        final String layerName = layers.get(0).getName();
        final TileLayer tileLayer;
        try {
            tileLayer = this.tld.getTileLayer(layerName);
        } catch (GeoWebCacheException e) {
            return null;
        }

        final MimeType mimeType;
        try {
            mimeType = MimeType.createFromFormat(request.getFormat());
            List<MimeType> tileLayerFormats = tileLayer.getMimeTypes();
            if (!tileLayerFormats.contains(mimeType)) {
                return null;
            }
        } catch (MimeException me) {
            // not a GWC supported format
            return null;
        }
        ConveyorTile tileResp = null;

        try {
            HttpServletRequest servletReq = null;
            HttpServletResponse servletResp = null;
            final String gridSetId;
            long[] tileIndex;
            try {
                String srs = request.getSRS();
                int epsgId = Integer.parseInt(srs.substring(srs.indexOf(':') + 1));
                SRS srs2 = SRS.getSRS(epsgId);
                GridSubset gridSubset = tileLayer.getGridSubsetForSRS(srs2);
                gridSetId = gridSubset.getName();
                Envelope bbox = request.getBbox();
                BoundingBox tileBounds = new BoundingBox(bbox.getMinX(), bbox.getMinY(),
                        bbox.getMaxX(), bbox.getMaxY());
                tileIndex = gridSubset.closestIndex(tileBounds);
            } catch (Exception e) {
                return null;
            }

            String fullParameters = null;
            String modifiedParameters = null;
            ConveyorTile tileReq;
            tileReq = new ConveyorTile(storageBroker, layerName, gridSetId, tileIndex, mimeType,
                    fullParameters, modifiedParameters, servletReq, servletResp);

            tileResp = tileLayer.getTile(tileReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tileResp;
    }

    /**
     * Determines whether the given {@link GetMapRequest} is a candidate to match a GWC tile or not.
     * 
     * @param request
     * @return {@code true} if {@code request} <b>might</b>
     */
    private boolean isCachingPossible(GetMapRequest request) {

        if (request.getFormatOptions() != null && !request.getFormatOptions().isEmpty()) {
            return false;
        }
        if (0.0 != request.getAngle()) {
            return false;
        }
        // if (null != request.getBgColor()) {
        // return false;
        // }
        if (0 != request.getBuffer()) {
            return false;
        }
        if (null != request.getCQLFilter() && !request.getCQLFilter().isEmpty()) {
            return false;
        }
        if (!Double.isNaN(request.getElevation())) {
            return false;
        }
        if (null != request.getFeatureId() && !request.getFeatureId().isEmpty()) {
            return false;
        }
        if (null != request.getFilter() && !request.getFilter().isEmpty()) {
            return false;
        }
        if (null != request.getPalette()) {
            return false;
        }
        if (null != request.getRemoteOwsType() || null != request.getRemoteOwsURL()) {
            return false;
        }
        if (null != request.getSld() || null != request.getSldBody()) {
            return false;
        }
        if (null != request.getStartIndex()) {
            return false;
        }
        if (null != request.getTime() && !request.getTime().isEmpty()) {
            return false;
        }
        if (null != request.getViewParams() && !request.getViewParams().isEmpty()) {
            return false;
        }
        return true;
    }

    public List<TileLayer> getLayers() {
        return new ArrayList<TileLayer>(tld.getLayers().values());
    }

    public List<TileLayer> getLayers(final String namespacePrefixFilter) {
        if (namespacePrefixFilter == null) {
            return getLayers();
        }

        final Catalog catalog = config.getCatalog();

        final NamespaceInfo namespaceFilter = catalog.getNamespaceByPrefix(namespacePrefixFilter);
        if (namespaceFilter == null) {
            return getLayers();
        }

        List<TileLayer> filteredLayers = new ArrayList<TileLayer>();

        NamespaceInfo layerNamespace;
        String layerName;

        for (TileLayer tileLayer : getLayers()) {
            layerName = tileLayer.getName();
            LayerInfo layerInfo = catalog.getLayerByName(layerName);
            if (layerInfo != null) {
                layerNamespace = layerInfo.getResource().getNamespace();
                if (namespaceFilter.equals(layerNamespace)) {
                    filteredLayers.add(tileLayer);
                }
            }
        }

        return filteredLayers;
    }
}

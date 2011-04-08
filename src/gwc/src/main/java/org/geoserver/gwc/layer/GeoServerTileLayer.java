package org.geoserver.gwc.layer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.MetadataMap;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.gwc.GWC;
import org.geoserver.gwc.GWCConfig;
import org.geoserver.wms.GetMapRequest;
import org.geoserver.wms.WMS;
import org.geoserver.wms.WebMap;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.geowebcache.GeoWebCacheException;
import org.geowebcache.conveyor.ConveyorTile;
import org.geowebcache.filter.parameters.ParameterException;
import org.geowebcache.filter.parameters.ParameterFilter;
import org.geowebcache.filter.parameters.StringParameterFilter;
import org.geowebcache.grid.BoundingBox;
import org.geowebcache.grid.GridSetBroker;
import org.geowebcache.grid.GridSubset;
import org.geowebcache.grid.GridSubsetFactory;
import org.geowebcache.grid.OutsideCoverageException;
import org.geowebcache.io.ByteArrayResource;
import org.geowebcache.io.Resource;
import org.geowebcache.layer.MetaTile;
import org.geowebcache.layer.TileLayer;
import org.geowebcache.layer.meta.ContactInformation;
import org.geowebcache.layer.meta.LayerMetaInformation;
import org.geowebcache.mime.FormatModifier;
import org.geowebcache.mime.MimeType;
import org.geowebcache.util.GWCVars;
import org.springframework.util.Assert;

public class GeoServerTileLayer extends TileLayer {

    private static final Logger LOGGER = Logging.getLogger(GeoServerTileLayer.class);

    private final GeoServerTileLayerInfo info;

    public static final String GWC_SEED_INTERCEPT_TOKEN = "GWC_SEED_INTERCEPT";

    public static final ThreadLocal<WebMap> WEB_MAP = new ThreadLocal<WebMap>();

    private GWC mediator;

    private transient final String layerId;

    private transient final String layerGroupId;

    public GeoServerTileLayer(final GWC mediator, final LayerGroupInfo layerGroup) {
        this.mediator = mediator;
        this.layerId = null;
        this.layerGroupId = layerGroup.getId();
        this.name = layerGroup.getName();
        GWCConfig configDefaults = mediator.getConfig();
        this.info = GeoServerTileLayerInfo.create(null, layerGroup.getMetadata(), configDefaults);
        setDefaults();
    }

    public GeoServerTileLayer(final GWC mediator, final LayerInfo layerInfo) {
        this.mediator = mediator;
        this.layerId = layerInfo.getId();
        this.layerGroupId = null;
        final ResourceInfo resourceInfo = layerInfo.getResource();
        super.name = resourceInfo.getPrefixedName();
        GWCConfig configDefaults = mediator.getConfig();
        this.info = GeoServerTileLayerInfo.create(layerInfo.getResource(), layerInfo.getMetadata(),
                configDefaults);
        setDefaults();
    }

    private void setDefaults() {
        super.metaWidthHeight = new int[] { info.getMetaTilingX(), info.getMetaTilingY() };
        // TODO: be careful to update super.mimeFormats when modifying info.mimeFormats
        super.mimeFormats = info.getMimeFormats();
        if (info.isCacheNonDefaultStyles()) {
            super.parameterFilters = createStylesParameterFilters();
        } else {
            LOGGER.fine("NOT Creating GeoServerTileLayer for " + getName() + " as option is off");
        }

        // set default properties that doesn't need initialization
        super.backendTimeout = 0;
        super.cacheBypassAllowed = Boolean.TRUE;
        super.expireCache = null;
        super.expireCacheList = null;
        super.expireClients = null;
        super.expireClientsList = null;
        super.formatModifiers = null;
        super.requestFilters = null;
        super.updateSources = null;
        super.useETags = false;
        super.formats = null;// set by super.initialize based on mimeFormats
        super.metaInformation = null;// see getMetaInformation() override
        super.queryable = null;// see isQueryable() override
        super.wmsStyles = null;// see getStyles() override
        setBackendTimeout(120);
    }

    public void setMetaTilingFactors(final int metaTilingX, final int metaTilingY) {
        Assert.isTrue(metaTilingX > 0);
        Assert.isTrue(metaTilingY > 0);
        super.metaWidthHeight = new int[] { metaTilingX, metaTilingY };
    }

    /**
     * Returns whether caching is enabled for this tile layer.
     * <p>
     * Uses the {@code GWC.enabled} property of the backing {@link LayerInfo}'s or
     * {@link LayerGroupInfo}'s {@link MetadataMap}
     * </p>
     * 
     * @see org.geowebcache.layer.TileLayer#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return info.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        info.setEnabled(enabled);
        mediator.save(info);
    }

    public int getGutter() {
        return info.getGutter();
    }

    public void setGutter(int gutter) {
        info.setGutter(gutter);
        mediator.save(info);
    }

    /**
     * 
     * @see org.geowebcache.layer.TileLayer#isQueryable()
     * @see WMS#isQueryable(LayerGroupInfo)
     * @see WMS#isQueryable(LayerInfo)
     */
    @Override
    public boolean isQueryable() {
        return mediator.isQueryable(info);
    }

    @Override
    protected boolean initializeInternal(GridSetBroker gridSetBroker) {

        ReferencedEnvelope latLongBbox = getLatLonBbox();

        final Hashtable<String, GridSubset> subSets = getGrids(latLongBbox, gridSetBroker);
        super.subSets = subSets;

        return true;
    }

    private ReferencedEnvelope getLatLonBbox() throws IllegalStateException {
        ReferencedEnvelope latLongBbox;
        if (getLayerInfo() == null) {
            LayerGroupInfo groupInfo = getLayerGroupInfo();
            try {
                ReferencedEnvelope bounds = groupInfo.getBounds();
                boolean longitudeFirst = true;
                boolean lenient = true;
                latLongBbox = bounds.transform(CRS.decode("EPSG:4326", longitudeFirst), lenient);
            } catch (Exception e) {
                String msg = "Can't get lat long bounds for layer group " + groupInfo.getName();
                LOGGER.log(Level.WARNING, msg, e);
                throw new IllegalStateException(msg, e);
            }
        } else {
            ResourceInfo resourceInfo = getResourceInfo();
            latLongBbox = resourceInfo.getLatLonBoundingBox();
        }
        return latLongBbox;
    }

    /**
     * Creates parameter filters for each additional layer style
     * 
     * @return
     */
    private List<ParameterFilter> createStylesParameterFilters() {
        final LayerInfo layerInfo = getLayerInfo();
        if (layerInfo == null) {
            return null;
        }
        final Set<StyleInfo> additionalStyles = layerInfo.getStyles();
        if (additionalStyles == null || additionalStyles.size() == 0) {
            LOGGER.finer("Layer " + layerInfo.getName()
                    + " has no additional styles, no parameter filters added");
            return null;
        }

        final String defaultStyle = layerInfo.getDefaultStyle().getName();
        List<String> possibleValues = new ArrayList<String>(1 + additionalStyles.size());
        possibleValues.add(defaultStyle);
        for (StyleInfo style : additionalStyles) {
            String styleName = style.getName();
            possibleValues.add(styleName);
        }
        List<ParameterFilter> parameterFilters = new ArrayList<ParameterFilter>(1);
        StringParameterFilter styleParamFilter = new StringParameterFilter();
        styleParamFilter.key = "STYLES";
        styleParamFilter.defaultValue = defaultStyle;
        styleParamFilter.values = possibleValues;
        parameterFilters.add(styleParamFilter);
        LOGGER.fine("Created STYLES parameter filter for layer " + getName() + " and styles "
                + possibleValues);
        return parameterFilters;
    }

    /**
     * @return the {@link LayerInfo} for this layer, or {@code null} if it's backed by a
     *         {@link LayerGroupInfo} instead
     */
    public LayerInfo getLayerInfo() {
        if (layerId == null) {
            return null;
        }
        LayerInfo layerInfo = mediator.getLayerInfoById(layerId);
        return layerInfo;
    }

    /**
     * @return the {@link LayerGroupInfo} for this layer, or {@code null} if it's backed by a
     *         {@link LayerInfo} instead
     */
    public LayerGroupInfo getLayerGroupInfo() {
        if (layerGroupId == null) {
            return null;
        }
        LayerGroupInfo layerGroupInfo = mediator.getLayerGroupById(layerGroupId);
        return layerGroupInfo;
    }

    private ResourceInfo getResourceInfo() {
        LayerInfo layerInfo = getLayerInfo();
        return layerInfo == null ? null : layerInfo.getResource();
    }

    /**
     * Overrides to return a dynamic view of the backing {@link LayerInfo} or {@link LayerGroupInfo}
     * metadata adapted to GWC
     * 
     * @see org.geowebcache.layer.TileLayer#getMetaInformation()
     */
    @Override
    public LayerMetaInformation getMetaInformation() {
        LayerMetaInformation meta = null;
        String title = getName();
        String description = "";
        List<String> keywords = Collections.emptyList();
        List<ContactInformation> contacts = Collections.emptyList();

        ResourceInfo resourceInfo = getResourceInfo();
        if (resourceInfo != null) {
            title = resourceInfo.getDescription();
            description = resourceInfo.getAbstract();
            keywords = resourceInfo.getKeywords();
        }
        meta = new LayerMetaInformation(title, description, keywords, contacts);
        return meta;
    }

    /**
     * Overrides to dynamically return the default style name associated to the layer
     * 
     * @see org.geowebcache.layer.TileLayer#getStyles()
     */
    @Override
    public String getStyles() {
        return getLayerInfo() == null ? null : getLayerInfo().getDefaultStyle().getName();
    }

    /**
     * @see org.geowebcache.layer.TileLayer#getFeatureInfo
     * @see GWC#dispatchOwsRequest
     */
    @Override
    public Resource getFeatureInfo(ConveyorTile convTile, BoundingBox bbox, int height, int width,
            int x, int y) throws GeoWebCacheException {

        Map<String, String> params = buildGetFeatureInfo(convTile, bbox, height, width, x, y);
        FakeHttpServletResponse response;
        try {
            Cookie[] cookies = null;
            response = mediator.dispatchOwsRequest(params, cookies);
        } catch (Exception e) {
            throw new GeoWebCacheException(e);
        }
        return new ByteArrayResource(response.getBytes());
    }

    private Map<String, String> buildGetFeatureInfo(ConveyorTile convTile, BoundingBox bbox,
            int height, int width, int x, int y) {
        Map<String, String> wmsParams = new HashMap<String, String>();
        wmsParams.put("SERVICE", "WMS");
        wmsParams.put("VERSION", "1.1.1");
        wmsParams.put("REQUEST", "GetFeatureInfo");
        wmsParams.put("LAYERS", getName());
        wmsParams.put("STYLES", "");
        wmsParams.put("QUERY_LAYERS", getName());
        wmsParams.put("FORMAT", getMimeTypes().get(0).getFormat());
        wmsParams.put("EXCEPTIONS", GetMapRequest.SE_XML);

        wmsParams.put("INFO_FORMAT", convTile.getMimeType().getFormat());

        GridSubset gridSubset = convTile.getGridSubset();

        wmsParams.put("SRS", gridSubset.getSRS().toString());
        wmsParams.put("HEIGHT", String.valueOf(height));
        wmsParams.put("WIDTH", String.valueOf(width));
        wmsParams.put("BBOX", bbox.toString());
        wmsParams.put("X", String.valueOf(x));
        wmsParams.put("Y", String.valueOf(y));

        Map<String, String> fullParameters = convTile.getFullParameters();
        if (fullParameters.isEmpty()) {
            fullParameters = getDefaultParameterFilters();
        }
        wmsParams.putAll(fullParameters);

        return wmsParams;
    }

    @Override
    public ConveyorTile getTile(ConveyorTile tile) throws GeoWebCacheException, IOException,
            OutsideCoverageException {
        MimeType mime = tile.getMimeType();

        if (mime == null) {
            mime = this.formats.get(0);
        }

        if (!formats.contains(mime)) {
            throw new GeoWebCacheException(mime.getFormat() + " is not a supported format for "
                    + name);
        }

        String tileGridSetId = tile.getGridSetId();

        long[] gridLoc = tile.getTileIndex();

        // Final preflight check, throws OutsideCoverageException if necessary
        getGridSubset(tileGridSetId).checkCoverage(gridLoc);

        ConveyorTile returnTile;

        if (tryCacheFetch(tile)) {
            returnTile = finalizeTile(tile);// no need to go to the backend
        } else {
            int metaX = metaWidthHeight[0];
            int metaY = metaWidthHeight[1];
            if (!mime.supportsTiling()) {
                metaX = metaY = 1;
            }
            returnTile = getMetatilingReponse(tile, true, metaX, metaY);
            finalizeTile(returnTile);
        }

        sendTileRequestedEvent(returnTile);

        return returnTile;
    }

    private ConveyorTile getMetatilingReponse(ConveyorTile tile, boolean tryCacheFetch,
            final int metaX, final int metaY) throws GeoWebCacheException, IOException {

        final GeoServerMetaTile metaTile = createMetaTile(tile, metaX, metaY);

        WebMap map;
        try {
            map = dispatchGetMap(tile, metaTile);
        } catch (Exception e) {
            throw new GeoWebCacheException("Problem communicating with GeoServer", e);
        }

        metaTile.setWebMap(map);
        super.saveTiles(metaTile, tile);

        return tile;
    }

    private WebMap dispatchGetMap(ConveyorTile tile, final MetaTile metaTile) throws Exception {

        Map<String, String> params = buildGetMap(tile, metaTile);
        WebMap map;
        try {
            HttpServletRequest actualRequest = tile.servletReq;
            Cookie[] cookies = actualRequest == null ? null : actualRequest.getCookies();

            mediator.dispatchOwsRequest(params, cookies);
            map = WEB_MAP.get();
        } finally {
            WEB_MAP.remove();
        }

        return map;
    }

    private GeoServerMetaTile createMetaTile(ConveyorTile tile, final int metaX, final int metaY) {
        GeoServerMetaTile metaTile;

        String tileGridSetId = tile.getGridSetId();
        GridSubset gridSubset = getGridSubset(tileGridSetId);
        MimeType responseFormat = tile.getMimeType();
        FormatModifier formatModifier = null;
        long[] tileGridPosition = tile.getTileIndex();
        int gutter = getGutter();
        metaTile = new GeoServerMetaTile(gridSubset, responseFormat, formatModifier,
                tileGridPosition, metaX, metaY, gutter);

        return metaTile;
    }

    private Map<String, String> buildGetMap(final ConveyorTile tile, final MetaTile metaTile)
            throws ParameterException {

        Map<String, String> params = new HashMap<String, String>();

        final MimeType mimeType = tile.getMimeType();
        final String gridSetId = tile.getGridSetId();
        final GridSubset gridSubset = super.subSets.get(gridSetId);

        int width = metaTile.getMetaTileWidth();
        int height = metaTile.getMetaTileHeight();
        String srs = gridSubset.getSRS().toString();
        String format = mimeType.getFormat();
        BoundingBox bbox = metaTile.getMetaTileBounds();

        params.put("SERVICE", "WMS");
        params.put("VERSION", "1.1.1");
        params.put("REQUEST", "GetMap");
        params.put("LAYERS", getName());
        params.put("SRS", srs);
        params.put("FORMAT", format);
        params.put("WIDTH", String.valueOf(width));
        params.put("HEIGHT", String.valueOf(height));
        params.put("BBOX", bbox.toString());

        params.put("EXCEPTIONS", GetMapRequest.SE_XML);
        params.put("STYLES", "");
        params.put("TRANSPARENT", "true");
        params.put(GWC_SEED_INTERCEPT_TOKEN, "true");

        Map<String, String> filteredParams = tile.getFullParameters();
        if (filteredParams.isEmpty()) {
            filteredParams = getDefaultParameterFilters();
        }
        params.putAll(filteredParams);

        return params;
    }

    private boolean tryCacheFetch(ConveyorTile tile) {
        int expireCache = this.getExpireCache((int) tile.getTileIndex()[2]);
        if (expireCache != GWCVars.CACHE_DISABLE_CACHE) {
            try {
                return tile.retrieve(expireCache * 1000L);
            } catch (GeoWebCacheException gwce) {
                LOGGER.info(gwce.getMessage());
                tile.setErrorMsg(gwce.getMessage());
                return false;
            }
        }
        return false;
    }

    private ConveyorTile finalizeTile(ConveyorTile tile) {
        if (tile.getStatus() == 0 && !tile.getError()) {
            tile.setStatus(200);
        }

        if (tile.servletResp != null) {
            setExpirationHeader(tile.servletResp, (int) tile.getTileIndex()[2]);
            setTileIndexHeader(tile);
        }

        return tile;
    }

    /**
     * @param tile
     */
    private void setTileIndexHeader(ConveyorTile tile) {
        tile.servletResp.addHeader("geowebcache-tile-index", Arrays.toString(tile.getTileIndex()));
    }

    @Override
    public ConveyorTile getNoncachedTile(ConveyorTile tile) throws GeoWebCacheException {
        try {
            return getMetatilingReponse(tile, false, 1, 1);
        } catch (IOException e) {
            throw new GeoWebCacheException(e);
        }
    }

    @Override
    public ConveyorTile doNonMetatilingRequest(ConveyorTile tile) throws GeoWebCacheException {
        try {
            return getMetatilingReponse(tile, true, 1, 1);
        } catch (IOException e) {
            throw new GeoWebCacheException(e);
        }
    }

    @Override
    public void seedTile(ConveyorTile tile, boolean tryCache) throws GeoWebCacheException,
            IOException {

        int metaX = metaWidthHeight[0];
        int metaY = metaWidthHeight[1];
        if (!tile.getMimeType().supportsTiling()) {
            metaX = metaY = 1;
        }
        getMetatilingReponse(tile, tryCache, metaX, metaY);
    }

    @Override
    public void acquireLayerLock() {
        // TODO Auto-generated method stub

    }

    @Override
    public void releaseLayerLock() {
        // TODO Auto-generated method stub

    }

    private Hashtable<String, GridSubset> getGrids(ReferencedEnvelope env,
            GridSetBroker gridSetBroker) {
        double minX = env.getMinX();
        double minY = env.getMinY();
        double maxX = env.getMaxX();
        double maxY = env.getMaxY();

        BoundingBox bounds4326 = new BoundingBox(minX, minY, maxX, maxY);

        BoundingBox bounds900913 = new BoundingBox(longToSphericalMercatorX(minX),
                latToSphericalMercatorY(minY), longToSphericalMercatorX(maxX),
                latToSphericalMercatorY(maxY));

        Hashtable<String, GridSubset> grids = new Hashtable<String, GridSubset>(2);

        GridSubset gridSubset4326 = GridSubsetFactory.createGridSubSet(
                gridSetBroker.WORLD_EPSG4326, bounds4326, 0, 25);

        grids.put(gridSetBroker.WORLD_EPSG4326.getName(), gridSubset4326);

        GridSubset gridSubset900913 = GridSubsetFactory.createGridSubSet(
                gridSetBroker.WORLD_EPSG3857, bounds900913, 0, 25);

        grids.put(gridSetBroker.WORLD_EPSG3857.getName(), gridSubset900913);

        return grids;
    }

    private double longToSphericalMercatorX(double x) {
        return (x / 180.0) * 20037508.34;
    }

    private double latToSphericalMercatorY(double y) {
        if (y > 85.05112) {
            y = 85.05112;
        }

        if (y < -85.05112) {
            y = -85.05112;
        }

        y = (Math.PI / 180.0) * y;
        double tmp = Math.PI / 4.0 + y / 2.0;
        return 20037508.34 * Math.log(Math.tan(tmp)) / Math.PI;
    }

}

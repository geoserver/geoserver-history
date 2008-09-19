/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.WMSExtensions;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageReadRequest;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.CoverageResponse.Status;
import org.geotools.coverage.io.impl.DefaultCoverageReadRequest;
import org.geotools.coverage.io.impl.range.DefaultRangeType;
import org.geotools.coverage.io.range.Axis;
import org.geotools.coverage.io.range.FieldType;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.map.DefaultMapLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.util.WCSUtils;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.RasterMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.map.metatile.MetatileMapProducer;

import com.vividsolutions.jts.geom.Envelope;

/**
 * A GetMapResponse object is responsible of generating a map based on a GetMap
 * request. The way the map is generated is independent of this class, wich will
 * use a delegate object based on the output format requested
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @author Simone Giannecchini - GeoSolutions SAS
 * @version $Id$
 */
public class GetMapResponse implements Response {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(GetMapResponse.class.getPackage().getName());

    private static FilterFactory filterFac = CommonFactoryFinder.getFilterFactory(null);

    /**
     * The map producer that will be used for the production of a map in the
     * requested format.
     */
    private GetMapProducer delegate;

    /**
     * The map context
     */
    private WMSMapContext map;

    /**
     * custom response headers
     */
    private HashMap responseHeaders;

    String headerContentDisposition;

    private ApplicationContext applicationContext;

    /**
     * Creates a new GetMapResponse object.
     * 
     * @param applicationContext
     */
    public GetMapResponse(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        responseHeaders = new HashMap();
    }

    /**
     * Returns any extra headers that this service might want to set in the HTTP
     * response object.
     * 
     */
    public HashMap getResponseHeaders() {
        return new HashMap(responseHeaders);
    }

    /**
     * Implements the map production logic for a WMS GetMap request, delegating
     * the encoding to the appropriate output format to a {@link GetMapProducer}
     * appropriate for the required format.
     * 
     * @param req
     *            a {@link GetMapRequest}
     * 
     * @throws ServiceException
     *             if an error occurs creating the map from the provided request
     * 
     * TODO: This method have become a 300+ lines monster, refactore it to
     * private methods from which names one can inferr what's going on... but
     * get a decent test coverage on it first as to avoid regressions as much as
     * possible
     */
    @SuppressWarnings("unchecked")
    public void execute(Request req) throws ServiceException {
        GetMapRequest request = (GetMapRequest) req;

        final String outputFormat = request.getFormat();

        this.delegate = getDelegate(outputFormat);
        // JD:make instance variable in order to release resources later
        // final WMSMapContext map = new WMSMapContext();
        map = new WMSMapContext(request);
        this.delegate.setMapContext(map);

        // enable on the fly meta tiling if request looks like a tiled one
        if (MetatileMapProducer.isRequestTiled(request, delegate)) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("Tiled request detected, activating on the fly meta tiler");
            }

            this.delegate = new MetatileMapProducer(request, (RasterMapProducer) delegate);
            this.delegate.setMapContext(map);
        }

        final MapLayerInfo[] layers = request.getLayers();
        final Style[] styles = (Style[]) request.getStyles().toArray(new Style[] {});
        final Filter[] filters = buildLayersFilters(request.getFilter(), layers);

        // DJB: the WMS spec says that the request must not be 0 area
        // if it is, throw a service exception!
        final Envelope env = request.getBbox();
        if (env == null) {
            throw new WmsException("GetMap requests must include a BBOX parameter.");
        }
        if (env.isNull() || (env.getWidth() <= 0) || (env.getHeight() <= 0)) {
            throw new WmsException(new StringBuffer("The request bounding box has zero area: ")
                    .append(env).toString());
        }

        // DJB DONE: replace by setAreaOfInterest(Envelope,
        // CoordinateReferenceSystem)
        // with the user supplied SRS parameter

        // if there's a crs in the request, use that. If not, assume its 4326
        final CoordinateReferenceSystem mapcrs = request.getCrs();

        // DJB: added this to be nicer about the "NONE" srs.
        if (mapcrs != null) {
            map.setAreaOfInterest(env, mapcrs);
        } else {
            map.setAreaOfInterest(env, DefaultGeographicCRS.WGS84);
        }

        map.setMapWidth(request.getWidth());
        map.setMapHeight(request.getHeight());
        map.setBgColor(request.getBgColor());
        map.setTransparent(request.isTransparent());
        map.setBuffer(request.getBuffer());
        map.setPaletteInverter(request.getPalette());

        // //
        //
        // Check to see if we really have something to display. Sometimes width
        // or height or both are non positivie or the requested area is null.
        //
        // ///
        if ((request.getWidth() <= 0) || (request.getHeight() <= 0)
                || (map.getAreaOfInterest().getLength(0) <= 0)
                || (map.getAreaOfInterest().getLength(1) <= 0)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER
                        .fine("We are not going to render anything because either the area is null or the dimensions are not positive.");
            }

            return;
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("setting up map");
        }

        try { // mapcontext can leak memory -- we make sure we done (see
            // finally block)

            // track the external caching strategy for any map layers
            boolean cachingPossible = request.getHttpServletRequest().getMethod().equals("GET");
            final String featureVersion = request.getFeatureVersion();
            int maxAge = Integer.MAX_VALUE;
            for (int i = 0; i < layers.length; i++) {
                final Style layerStyle = styles[i];
                final Filter layerFilter = filters[i];

                final DefaultMapLayer layer;
                if (layers[i].getType() == MapLayerInfo.TYPE_REMOTE_VECTOR) {
                    cachingPossible = false;

                    final FeatureSource<SimpleFeatureType, SimpleFeature> source = layers[i]
                            .getRemoteFeatureSource();
                    layer = new DefaultMapLayer(source, layerStyle);
                    layer.setTitle(layers[i].getName());

                    final DefaultQuery definitionQuery = new DefaultQuery(source.getSchema()
                            .getTypeName());
                    definitionQuery.setFilter(layerFilter);
                    definitionQuery.setVersion(featureVersion);
                    int maxFeatures = request.getMaxFeatures() != null ? request.getMaxFeatures()
                            : Integer.MAX_VALUE;
                    definitionQuery.setMaxFeatures(maxFeatures);

                    layer.setQuery(definitionQuery);
                    map.addLayer(layer);
                } else if (layers[i].getType() == MapLayerInfo.TYPE_VECTOR) {
                    if (cachingPossible) {
                        if (layers[i].getFeature().isCachingEnabled()) {
                            int nma = Integer.parseInt(layers[i].getFeature().getCacheMaxAge());

                            // suppose the map contains multiple cachable
                            // layers...we can only cache the combined map for
                            // the
                            // time specified by the shortest-cached layer.
                            if (nma < maxAge) {
                                maxAge = nma;
                            }
                        } else {
                            // if one layer isn't cachable, then we can't cache
                            // any of them. Disable caching.
                            cachingPossible = false;
                        }
                    }

                    FeatureSource<SimpleFeatureType, SimpleFeature> source;
                    // /////////////////////////////////////////////////////////
                    //
                    // Adding a feature layer
                    //
                    // /////////////////////////////////////////////////////////
                    try {
                        source = layers[i].getFeature().getFeatureSource(true);

                        // NOTE for the feature. Here there was some code that
                        // sounded like:
                        // * get the bounding box from feature source
                        // * eventually reproject it to the actual CRS used for
                        // map
                        // * if no intersection, don't bother adding the feature
                        // source to the map
                        // This is not an optimization, on the contrary,
                        // computing the bbox may be
                        // very expensive depending on the data size. Using
                        // sigma.openplans.org data
                        // and a tiled client like OpenLayers, it dragged the
                        // server to his knees
                        // and the client simply timed out
                    } catch (IOException exp) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, new StringBuffer("Getting feature source: ")
                                    .append(exp.getMessage()).toString(), exp);
                        }

                        throw new WmsException("Internal error", "", exp);
                    }

                    layer = new DefaultMapLayer(source, layerStyle);
                    layer.setTitle(layers[i].getName());

                    final DefaultQuery definitionQuery = new DefaultQuery(source.getSchema()
                            .getTypeName());
                    definitionQuery.setVersion(featureVersion);
                    definitionQuery.setFilter(layerFilter);

                    // check for startIndex + offset
                    final Integer startIndex = request.getStartIndex();
                    if (startIndex != null) {
                        QueryCapabilities queryCapabilities = source.getQueryCapabilities();
                        if (queryCapabilities.isOffsetSupported()) {
                            // fsource is required to support
                            // SortBy.NATURAL_ORDER so we don't bother checking
                            definitionQuery.setStartIndex(startIndex);
                        } else {
                            // source = new PagingFeatureSource(source,
                            // request.getStartIndex(), limit);
                            throw new WmsException("startIndex is not supported for the "
                                    + layers[i].getName() + " layer");
                        }
                    }

                    int maxFeatures = request.getMaxFeatures() != null ? request.getMaxFeatures()
                            : Integer.MAX_VALUE;
                    definitionQuery.setMaxFeatures(maxFeatures);

                    layer.setQuery(definitionQuery);
                    map.addLayer(layer);
                } else if (layers[i].getType() == MapLayerInfo.TYPE_RASTER) {
                    // /////////////////////////////////////////////////////////
                    //
                    // Adding a coverage layer
                    //
                    // /////////////////////////////////////////////////////////
                    GridCoverage2D coverage = null;
                    CoverageAccess cvAccess = layers[i].getCoverage().getCoverageAccess();
                    coverage = getCoverage(request, layers[i], env, mapcrs, cvAccess);
                    
                    if (coverage == null) continue;
                    try {
                        layer = new DefaultMapLayer(coverage, layerStyle);

                        layer.setTitle(layers[i].getName());
                        layer.setQuery(Query.ALL);
                        map.addLayer(layer);
                    } catch (IllegalArgumentException e) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, 
                            new StringBuffer("Wrapping GC in feature source: ")
                            .append(e.getLocalizedMessage()).toString(), e);
                        }

                        throw new WmsException(
                                null,
                                new StringBuffer("Internal error : unable to get reader for this coverage layer ")
                                .append(layers[i].toString()).toString());
                    }

                }
            }

            // enable simple watermarking
            if (this.delegate instanceof DefaultRasterMapProducer)
                ((DefaultRasterMapProducer) this.delegate).setWmPainter(new WatermarkPainter(
                        request));

            // /////////////////////////////////////////////////////////
            //
            // Producing the map in the requested format.
            //
            // /////////////////////////////////////////////////////////
            this.delegate.produceMap();

            if (cachingPossible) {
                responseHeaders.put("Cache-Control", "max-age=" + maxAge + ", must-revalidate");
            }

            final String contentDisposition = this.delegate.getContentDisposition();
            if (contentDisposition != null) {
                this.headerContentDisposition = contentDisposition;
            }
        } catch (Exception e) {
            clearMapContext();
            throw new WmsException(e, "Internal error ", "");
        }
    }

    /**
     * @param request
     *                the request where to get the output size and time, elevation and coverage
     *                specific dimensions to query the {@link CoverageSource} for
     * @param layer
     * @param areaOfInterest the extent to query in {@code mapCrs} units
     * @param mapcrs the CRS for the response
     * @param cvAccess the {@link CoverageAccess} to query
     * @return
     * 
     * @throws IOException if accessing the coverage fails
     *  
     * @see GetMapRequest#getWidth()
     * @see GetMapRequest#getHeight()
     * @see GetMapRequest#getTime()
     * @see GetMapRequest#getElevation()
     * @see GetMapRequest#getSampleDimensions()
     * @see CoverageAccess#access(org.opengis.feature.type.Name, Map, AccessType, org.geotools.factory.Hints, org.opengis.util.ProgressListener)
     * @see CoverageReadRequest
     * @see CoverageResponse
     */
    public static GridCoverage2D getCoverage(GetMapRequest request, final MapLayerInfo layer,
            final Envelope areaOfInterest, final CoordinateReferenceSystem mapcrs, CoverageAccess cvAccess) throws IOException {
        GridCoverage2D coverage = null;
        if (cvAccess == null) {
            throw new WmsException(null, new StringBuffer(
            "Internal error : unable to get reader for this coverage layer ")
            .append(layer.toString()).toString() + ". CoverageAccess can't be null");
        }
        // stripping the namespace
        String layerName = layer.getName();
        layerName = layerName.contains(":") ? layerName.substring(layerName.indexOf(":")+1) : layerName;
        final CoverageSource cvSource = cvAccess.access(new NameImpl(layerName), null, AccessType.READ_ONLY, null, null);
        try{
            // handle spatial domain subset, if needed
            GeneralEnvelope requestedEnvelope = new GeneralEnvelope(new double[] {areaOfInterest.getMinX(), areaOfInterest.getMinY()}, new double[] {areaOfInterest.getMaxX(), areaOfInterest.getMaxY()});
            requestedEnvelope.setCoordinateReferenceSystem(mapcrs);
            int[] lowers = new int[] {
                    0,
                    0};
            int[] highers = new int[] {
                    request.getWidth(),
                    request.getHeight()};

            Rectangle destinationSize = new Rectangle(lowers[0], lowers[1], highers[0], highers[1]);

            final CoverageReadRequest cvRequest = new DefaultCoverageReadRequest();
            
            cvRequest.setDomainSubset(destinationSize, requestedEnvelope);

            Set<FieldType> rangeSubset = new HashSet<FieldType>();
            RangeType fields = layer.getCoverage().getFields();
            if (fields != null && fields.getNumFieldTypes() > 0) {
                for (FieldType field : fields.getFieldTypes()) {
                    String fieldName = field.getName().getLocalPart();
                    if (layer.getFieldId() != null && layer.getFieldId().equalsIgnoreCase(fieldName))
                        rangeSubset.add(field);
                }
            }
            if (rangeSubset != null && rangeSubset.size() > 0) {
                RangeType range = new DefaultRangeType("", "", rangeSubset);
                cvRequest.setRangeSubset(range);
            }
            
            if (request.getTime() != null && request.getTime().size() > 0) {
                List<Date> timePositions = request.getTime();
                SortedSet<TemporalGeometricPrimitive> requestedTemporalSubset = new TreeSet<TemporalGeometricPrimitive>();
                
                for (Date tPos : timePositions) {
                    requestedTemporalSubset.add(new DefaultInstant(new DefaultPosition(tPos)));
                }

                cvRequest.setTemporalSubset(requestedTemporalSubset);
            }
            
            if (request.getElevation() != null) {
                Set<org.opengis.geometry.Envelope> verticalSubset = new TreeSet<org.opengis.geometry.Envelope>();
                verticalSubset.add(new GeneralEnvelope(request.getElevation(), request.getElevation()));
                cvRequest.setVerticalSubset(verticalSubset);
            }

            final CoverageResponse cvResponse = cvSource.read(cvRequest, null);
            

            if (!cvResponse.getStatus().equals(Status.SUCCESS))
                throw new IOException("The requested coverage could not be found."); // TODO: FIX THIS!!!
            
            if (cvResponse.getResults(null) == null || cvResponse.getResults(null).isEmpty())
                return null;
            
            /** if (cvResponse.getResults(null).size() > 1) ?? **/ // TODO: FIX THIS!!!
            coverage = (GridCoverage2D) cvResponse.getResults(new NullProgressListener()).iterator().next();
            
            if ((coverage == null) || !(coverage instanceof GridCoverage2D))
                return null;
            
            /**
             * Band Select (works on just one field)
             */
            GridCoverage2D bandSelectedCoverage = null;
            if (rangeSubset != null && rangeSubset.size() > 0) {
                if (rangeSubset.size() > 1) {
                    throw new WcsException("Multi field coverages are not supported yet");
                }

                final Map<String, String> requestDimensions = request.getSampleDimensions();
                
                for (FieldType field : rangeSubset) {
                    String axisName;
                    String selectedBandField = null;
                    for (Map.Entry<String, String> dimParam : requestDimensions.entrySet()) {
                        axisName = dimParam.getKey();
                        selectedBandField = dimParam.getValue();
                        axisName = normalizeCoverageFieldAxisName(layer.getName(), axisName);
                        
                        try {
                            Axis<?, ?> axis = field.getAxis(new NameImpl(axisName));
                            if (axis != null) {
                                //REVISIT: should check if selectBandField != "" too?
                                if (selectedBandField != null) {
                                    bandSelectedCoverage = bandSelect(coverage, axis, selectedBandField);
                                    //REVISIT: should break here?
                                }
                            }
                        } catch (Exception e) {
                            LOGGER.warning(e.getLocalizedMessage());
                        }
                        break;
                    }
                }
            }

            if (bandSelectedCoverage != null){
                coverage = bandSelectedCoverage;
            }

        }finally{
            cvSource.dispose();
        }
        
        return coverage;
    }

    private static String normalizeCoverageFieldAxisName(final String layerName, String axisName) {
        axisName = axisName.toLowerCase();
        String name = layerName;
        if(!axisName.startsWith("axis:")){
            //remove namespace prefix
            if(layerName.indexOf(':') != -1){
                name = layerName.substring(layerName.indexOf(':') + 1);
            }
            axisName = "axis:" + name + axisName;
        }
        return axisName;
    }

    /**
     * 
     * @param coverage
     * @param axis
     * @param selectedBandField
     * @return
     */
    private static GridCoverage2D bandSelect(GridCoverage2D coverage,
            Axis<?, ?> axis, String selectedBandField) {
        GridCoverage2D bandSelectedCoverage = null;
        int[] bands = null;
        if (selectedBandField.indexOf("/") <= 0) {
            String[] selectedBands = selectedBandField.indexOf(",") > 0 ? selectedBandField.split(",") : new String[] {selectedBandField};
            bands = new int[selectedBands.length];
            for (int s=0; s<selectedBands.length; s++)
                bands[s] = axis.getKey(Integer.parseInt(selectedBands[0])).intValue(null);
        } else if (selectedBandField.indexOf("/") > 0) {
            String[] selectedBands = selectedBandField.split("/");
            int min = Integer.parseInt(selectedBands[0]);
            int max = Integer.parseInt(selectedBands[selectedBands.length-1]);
            int res = (selectedBands.length > 2 ? Integer.parseInt(selectedBands[1]) : 1);

            bands = new int[(int) (Math.floor(max - min) / res + 1)];
            for (int b=0; b<bands.length; b++)
                bands[b] = axis.getKey(min + b*res).intValue(null);
        }

        // finally execute the band select
        try {
            bandSelectedCoverage = (GridCoverage2D) WCSUtils.bandSelect(coverage, bands);
        } catch (Exception e) {
            throw new WcsException("The selected Band values are not allowed for this Coverage.");
        }
        
        return bandSelectedCoverage;
    }

    /**
     * Returns the list of filters resulting of combining the layers definition
     * filters with the per layer filters made by the user.
     * <p>
     * If <code>requestFilters != null</code>, it shall contain the same
     * number of elements than <code>layers</code>, as filters are requested
     * one per layer.
     * </p>
     * 
     * @param requestFilters
     *            the list of filters sent by the user, or <code>null</code>
     * @param layers
     *            the layers requested in the GetMap request, where to get the
     *            per layer definition filters from.
     * @return a list of filters, one per layer, resulting of anding the user
     *         requested filter and the layer definition filter
     */
    private Filter[] buildLayersFilters(List<Filter> requestFilters, MapLayerInfo[] layers) {
        final int nLayers = layers.length;
        if (requestFilters == null || requestFilters.size() == 0) {
            requestFilters = Collections.nCopies(layers.length, (Filter) Filter.INCLUDE);
        } else if (requestFilters.size() != nLayers) {
            throw new IllegalArgumentException(
                    "requested filters and number of layers do not match");
        }
        Filter[] combinedList = new Filter[nLayers];
        Filter layerDefinitionFilter;
        Filter userRequestedFilter;
        Filter combined;

        MapLayerInfo layer;
        for (int i = 0; i < nLayers; i++) {
            layer = layers[i];
            userRequestedFilter = requestFilters.get(i);
            if (layer.getType() != MapLayerInfo.TYPE_RASTER) {
                layerDefinitionFilter = layer.getFeature().getDefinitionQuery();
                // heck, how I wish we use the null objects more
                if (layerDefinitionFilter == null) {
                    layerDefinitionFilter = Filter.INCLUDE;
                }
                combined = filterFac.and(layerDefinitionFilter, userRequestedFilter);
                combinedList[i] = combined;
            }
        }
        return combinedList;
    }

    /**
     * asks the internal GetMapDelegate for the MIME type of the map that it
     * will generate or is ready to, and returns it
     * 
     * @param gs
     *            DOCUMENT ME!
     * 
     * @return the MIME type of the map generated or ready to generate
     * 
     * @throws IllegalStateException
     *             if a GetMapDelegate is not setted yet
     */
    public String getContentType(GeoServer gs) throws IllegalStateException {
        if (this.delegate == null) {
            throw new IllegalStateException("No request has been processed");
        }

        return this.delegate.getContentType();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getContentEncoding() {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("returning content encoding null");
        }

        return null;
    }

    /**
     * if a GetMapDelegate is set, calls it's abort method. Elsewere do nothing.
     * 
     * @param gs
     *            DOCUMENT ME!
     */
    public void abort(Service gs) {
        if (this.delegate != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("asking delegate for aborting the process");
            }

            this.delegate.abort();
        }
    }

    /**
     * delegates the writing and encoding of the results of the request to the
     * <code>GetMapDelegate</code> wich is actually processing it, and has
     * been obtained when <code>execute(Request)</code> was called
     * 
     * @param out
     *            the output to where the map must be written
     * 
     * @throws ServiceException
     *             if the delegate throws a ServiceException inside its
     *             <code>writeTo(OuptutStream)</code>, mostly due to
     * @throws IOException
     *             if the delegate throws an IOException inside its
     *             <code>writeTo(OuptutStream)</code>, mostly due to
     * @throws IllegalStateException
     *             if this method is called before <code>execute(Request)</code>
     *             has succeed
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        try { // mapcontext can leak memory -- we make sure we done (see
            // finally block)

            if (this.delegate == null) {
                throw new IllegalStateException(
                        "No GetMapDelegate is setted, make sure you have called execute and it has succeed");
            }

            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer(new StringBuffer("asking delegate for write to ").append(out)
                        .toString());
            }

            this.delegate.writeTo(out);
        } finally {
            clearMapContext();
        }
    }

    /**
     * Clearing the map context is paramount, otherwise we end up with a memory
     * leak
     */
    void clearMapContext() {
        try {
            if (map != null && map.getLayerCount() > 0)
                map.clearLayerList();
        } catch (Exception e) // we dont want to propogate a new error
        {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, new StringBuffer("Getting feature source: ").append(
                        e.getMessage()).toString(), e);
            }
        }
    }

    /**
     * Finds out a {@link GetMapProducer} specialized in generating the
     * requested map format, registered in the spring context.
     * 
     * @param outputFormat
     *            a request parameter object wich holds the processed request
     *            objects, such as layers, bbox, outpu format, etc.
     * 
     * @return A specialization of <code>GetMapDelegate</code> wich can
     *         produce the requested output map format
     * 
     * @throws WmsException
     *             if no specialization is configured for the output format
     *             specified in <code>request</code> or if it can't be
     *             instantiated
     */
    private GetMapProducer getDelegate(final String outputFormat) throws WmsException {
        final GetMapProducer producer = WMSExtensions.findMapProducer(outputFormat,
                applicationContext);
        if (producer == null) {
            WmsException e = new WmsException("There is no support for creating maps in "
                    + outputFormat + " format");
            e.setCode("InvalidFormat");
            throw e;
        }
        // Tell the producer which of its output format names the
        // request
        // was made for, the producer may need it for its logic.
        // For example, result is different if requested image/png8
        // instead of image/png
        // I'm not so glad with this, but found a lot of producers were
        // made that way
        producer.setOutputFormat(outputFormat);
        return producer;
    }

    public String getContentDisposition() {
        return headerContentDisposition;
    }

    @Override
    protected void finalize() throws Throwable {
        clearMapContext();
    }

    /**
     * This is package visible only to allow getting to the delegate from inside
     * unit tests
     * 
     * @return
     */
    GetMapProducer getDelegate() {
        return delegate;
    }
}

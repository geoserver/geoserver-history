/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureResults;
import org.geotools.data.Query;
import org.geotools.feature.FeatureType;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.BBoxExpression;
import org.geotools.filter.Expression;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.LogicFilter;
import org.geotools.styling.Style;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.wms.GetMapRequest;
import org.vfny.geoserver.responses.Response;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Base class for delegates who creates a map based on a GetMap request.
 * Subclasses should implement one or more output format
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: GetMapDelegate.java,v 1.4.2.7 2004/01/06 23:03:13 dmzwiers Exp $
 */
public abstract class GetMapDelegate implements Response {
    private GetMapRequest request;

    /**
     * Creates a new GetMapDelegate object.
     */
    public GetMapDelegate() {
    }

    /**
     * Executes a Request, which must be a GetMapRequest.  Any other will
     * cause a class cast exception.
     *
     * @param request A valid GetMapRequest.
     *
     * @throws ServiceException If the request can not be executed.
     */
    public void execute(Request request) throws ServiceException {
        execute((GetMapRequest) request);
    }

    /**
     * Executes a GetMapRequest.  Builds the proper objects from the request
     * names.
     *
     * @param request A valid GetMapRequest.
     *
     * @throws WmsException If anything goes wrong.
     */
    protected void execute(GetMapRequest request) throws WmsException {
        this.request = request;

        FeatureTypeInfo[] layers = request.getLayers();
        Style[] styles = buildStyles(request.getStyles(), request.getGeoServer());
        Filter[] filters = request.getFilters();
        List attributes = request.getAttributes();

        Query[] queries = buildQueries(layers, filters, attributes);
        int nLayers = layers.length;
        FeatureResults[] resultLayers = new FeatureResults[nLayers];
        FeatureTypeInfo ftype = null;
        Filter filter = null;
        FeatureResults features = null;

        try {
            for (int i = 0; i < nLayers; i++) {
                ftype = layers[i];
                features = ftype.getFeatureSource().getFeatures(queries[i]);
                resultLayers[i] = features;
            }
        } catch (IOException ex) {
            throw new WmsException(ex, "Error getting features",
                getClass().getName() + ".execute()");
        }

        execute(layers, resultLayers, styles);
    }

    /**
     * Execute method for concrete children to implement.  Each param is an
     * array in the order things should be processed.
     *
     * @param requestedLayers Array of config information of the FeatureTypes
     *        to be processed.
     * @param resultLayers Matching array of results from the queries of the
     *        requested layers.
     * @param styles Matching array of the styles to process the results with.
     *
     * @throws WmsException For any problems executing.
     */
    protected abstract void execute(FeatureTypeInfo[] requestedLayers,
        FeatureResults[] resultLayers, Style[] styles)
        throws WmsException;

    /**
     * Creates the array of queries to be executed for the request.
     *
     * @param layers The layers to request against.
     * @param filters The matching filters to process with.
     * @param attributes The matching attributes to process with.
     *
     * @return An array of queries, matching the arrays passed in.
     *
     * @throws WmsException If the custom filter can't be constructed.
     */
    private Query[] buildQueries(FeatureTypeInfo[] layers, Filter[] filters,
        List attributes) throws WmsException {
        int nLayers = layers.length;
        int numFilters = (filters == null) ? 0 : filters.length;
        int numAttributes = attributes.size();

        Query[] queries = new Query[nLayers];
        GetMapRequest request = getRequest();
        Envelope requestExtent = request.getBbox();
        FilterFactory ffactory = FilterFactory.createFilterFactory();

        try {
            Filter finalLayerFilter;
            Filter customFilter;
            Query layerQuery;

            for (int i = 0; i < nLayers; i++) {
                FeatureType schema = layers[i].getSchema();

                if (numFilters == nLayers) {
                    customFilter = filters[i];
                } else {
                    customFilter = null;
                }

                finalLayerFilter = buildFilter(customFilter, requestExtent,
                        ffactory, schema);

                List layerProperties = (numAttributes == nLayers)
                    ? (List) attributes.get(i) : Collections.EMPTY_LIST;

                String[] props = guessProperties(layers[i], finalLayerFilter,
                        layerProperties);
                layerQuery = new DefaultQuery(finalLayerFilter, props);
                queries[i] = layerQuery;
            }
        } catch (IllegalFilterException ex) {
            throw new WmsException(ex,
                "Can't build layer queries: " + ex.getMessage(),
                getClass().getName() + "::parseFilters");
        }

        return queries;
    }

    /**
     * Builds the filter for a layer containing at leas the BBOX filter defined
     * by the extent queries (BBOX param), and optionally AND'ed with the
     * customized filter for that layer (from FILTERS param)
     *
     * @param filter The additional filter to process with.
     * @param requestExtent The extent to filter out.
     * @param ffactory A filterFactory to create new filters.
     * @param schema The FeatureTypeInfo of the request of this filter.
     *
     * @return A custom filter of the bbox and any optional custom filters.
     *
     * @throws IllegalFilterException For problems making the filter.
     */
    private Filter buildFilter(Filter filter, Envelope requestExtent,
        FilterFactory ffactory, FeatureType schema)
        throws IllegalFilterException {
        GeometryFilter bboxFilter;
        bboxFilter = ffactory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);

        BBoxExpression bboxExpr = ffactory.createBBoxExpression(requestExtent);
        Expression geomAttExpr = ffactory.createAttributeExpression(schema,
                schema.getDefaultGeometry().getName());
        bboxFilter.addLeftGeometry(geomAttExpr);
        bboxFilter.addRightGeometry(bboxExpr);

        Filter finalLayerFilter = bboxFilter;

        if ((filter != null) && (filter != Filter.NONE)) {
            finalLayerFilter = ffactory.createLogicFilter(AbstractFilter.LOGIC_AND);
            ((LogicFilter) finalLayerFilter).addFilter(bboxFilter);
            ((LogicFilter) finalLayerFilter).addFilter(filter);
        }

        return finalLayerFilter;
    }

	protected Style[] buildStyles(List styleNames,GeoServer gs)
        throws WmsException {
        Style[] styles = new Style[styleNames.size()];
        int i = 0;
        Data gc = gs.getData();
        
        for (Iterator it = styleNames.iterator(); it.hasNext(); i++) {
        	styles[i] = gc.getStyle((String) it.next());
        }

        return styles;
    }

    /**
     * Tries to guesss exactly wich property names are needed to query for a
     * given FeatureTypeInfo and the Filter that will be applied to it. By this
     * way, only the needed propertied will be queried to the underlying
     * FeatureSource in the hope that it will speed up the query
     * 
     * <p>
     * Note that just the attributes exposed by the FeatureTypeInfo will be
     * taken in count. a FeatureTypeInfo exposes all it's attributes except
     * if the subset of desiref exposed attributes are specified in the
     * catalog configuration.
     * </p>
     * 
     * <p>
     * This method guarantiees that at lest the default geometry attribute of
     * <code>layer</code> will be returned.
     * </p>
     *
     * @param layer The layer to process.
     * @param filter The filter to process with.
     * @param attributes The attributes to return.
     *
     * @return An array of the propertyNames needed.
     *
     * @task TODO: by now just returns the geometry att. Implement the rest of
     *       the method to find the rest of attributes needed by inspecting
     *       the filter (would be enough to get all the
     *       AttributeExpression's?). I think that the style should be taken
     *       in count too.
     */
    private String[] guessProperties(FeatureTypeInfo layer, Filter filter,
        List attributes) {
        FeatureType type = layer.getSchema();
        List atts = new ArrayList(attributes);
        String geom_name = type.getDefaultGeometry().getName();

        if (!atts.contains(geom_name)) {
            atts.add(geom_name);
        }

        String[] properties = (String[]) atts.toArray(new String[atts.size()]);

        return properties;
    }

    /**
     * Gets the map request.  Used by delegate children to find out more
     * information about the request.
     *
     * @return The request to be processed.
     */
    protected GetMapRequest getRequest() {
        return this.request;
    }

    /**
     * Evaluates if this Map producer can generate the map format specified by
     * <code>mapFormat</code>
     *
     * @param mapFormat the mime type of the output map format requiered
     *
     * @return true if class can produce a map in the passed format
     */
    public abstract boolean canProduce(String mapFormat);

    /**
     * Gets A list of the formats this delegate supports.
     *
     * @return A list of strings of the formats supported.
     */
    public abstract List getSupportedFormats();
}

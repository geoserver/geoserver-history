/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.featureInfo;

import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DefaultQuery;
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
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.wms.GetFeatureInfoRequest;
import org.vfny.geoserver.responses.Response;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Base class for GetFeatureInfo delegates responsible of creating
 * GetFeatureInfo responses in different formats.
 * 
 * <p>
 * Subclasses should implement one or more output formats, wich will be
 * returned in a list of mime type strings in
 * <code>getSupportedFormats</code>. For example, a subclass can be created to
 * write one of the following output formats:
 * 
 * <ul>
 * <li>
 * text/plain
 * </li>
 * <li>
 * text/html
 * </li>
 * </ul>
 * </p>
 * 
 * <p>
 * This abstract class takes care of executing the request in the sense of
 * taking the GetFeatureInfo request parameters such as query_layers, bbox, x,
 * y, etc., and the optional parameter FILTER, create the gt2 query objects
 * for each featuretype and executing it. This process leads to a set of
 * FeatureResults objects and its metadata, wich will be given to the
 * <code>execute(FeatureTypeInfo[] , FeatureResults[])</code> method, that a
 * subclass should implement as a matter of setting up any resource/state it
 * needs to later encoding.
 * </p>
 * 
 * <p>
 * So, it should be enough to a subclass to implement the following methods in
 * order to produce the requested output format:
 * 
 * <ul>
 * <li>
 * execute(FeatureTypeInfo[], FeatureResults[], int, int)
 * </li>
 * <li>
 * canProduce(String mapFormat)
 * </li>
 * <li>
 * getSupportedFormats()
 * </li>
 * <li>
 * writeTo(OutputStream)
 * </li>
 * </ul>
 * </p>
 *
 * @author Gabriel Roldan, Axios Engineering
 * @author Chris Holmes
 * @version $Id: GetFeatureInfoDelegate.java,v 1.1 2004/07/15 21:13:14 jmacgill Exp $
 */
public abstract class GetFeatureInfoDelegate implements Response {
    private GetFeatureInfoRequest request;

    /**
     * Creates a new GetMapDelegate object.
     */
    public GetFeatureInfoDelegate() {
    }

    /**
     * Executes a Request, which must be a GetMapRequest.  Any other will cause
     * a class cast exception.
     *
     * @param request A valid GetMapRequest.
     *
     * @throws ServiceException If the request can not be executed.
     */
    public void execute(Request request) throws ServiceException {
        execute((GetFeatureInfoRequest) request);
    }

    /**
     * Executes a GetFeatureInfo request.  Builds the proper objects from the
     * request names.
     *
     * @param request A valid GetMapRequest.
     *
     * @throws WmsException If anything goes wrong.
     */
    protected void execute(GetFeatureInfoRequest request)
        throws WmsException {
        this.request = request;

        //use the layer of the QUERY_LAYERS parameter, not the LAYERS one
        FeatureTypeInfo[] layers = request.getQueryLayers();

        //get the filters of the "GetMap" portion of the request
        Filter[] filters = request.getGetMapRequest().getFilters();

        Query[] queries = buildQueries(layers, filters);
        int x = request.getXPixel();
        int y = request.getYPixel();

        execute(layers, queries, x, y);
    }

    /**
     * DOCUMENT ME!
     *
     * @param gs DOCUMENT ME!
     */
    public void abort(Service gs) {
    }

    /**
     * Execute method for concrete children to implement.  Each param is an
     * array in the order things should be processed.
     *
     * @param requestedLayers Array of config information of the FeatureTypes
     *        to be processed.
     * @param queries Matching array of queries from the queries of the
     *        requested layers, already setted up with the bbox filter from
     *        the BBOX parameter of the GetMap request and to retrieve the
     *        specified attributes in the ATTRIBUTES request parameter.
     * @param x the X coordinate in pixels where the identification must be
     *        done relative to the image dimensions
     * @param y the Y coordinate in pixels where the identification must be
     *        done relative to the image dimensions
     *
     * @throws WmsException For any problems executing.
     */
    protected abstract void execute(FeatureTypeInfo[] requestedLayers,
        Query[] queries, int x, int y) throws WmsException;

    /**
     * Creates the array of queries to be executed for the request.
     * 
     * <p>
     * Each query is setted up to retrieve the features that matches the BBOX
     * specified in the GetMap request
     * </p>
     *
     * @param layers The layers to request against.
     * @param filters The matching filters to process with.
     *
     * @return An array of queries, matching the arrays passed in.
     *
     * @throws WmsException If the custom filter can't be constructed.
     */
    private Query[] buildQueries(FeatureTypeInfo[] layers, Filter[] filters)
        throws WmsException {
        int nLayers = layers.length;
        int numFilters = (filters == null) ? 0 : filters.length;
        Query[] queries = new Query[nLayers];
        GetFeatureInfoRequest infoRequest = getRequest();
        Envelope requestExtent = infoRequest.getGetMapRequest().getBbox();
        FilterFactory ffactory = FilterFactory.createFilterFactory();

        try {
            Filter finalLayerFilter;
            Filter customFilter;
            Query layerQuery;

            for (int i = 0; i < nLayers; i++) {
                FeatureType schema = layers[i].getFeatureType();

                if (numFilters == nLayers) {
                    customFilter = filters[i];
                } else {
                    customFilter = null;
                }

                finalLayerFilter = buildFilter(customFilter, requestExtent,
                        ffactory, schema);

                String[] props = guessProperties(layers[i], finalLayerFilter);
                layerQuery = new DefaultQuery(finalLayerFilter, props);
                queries[i] = layerQuery;
            }
        } catch (IllegalFilterException ex) {
            throw new WmsException(ex,
                "Can't build layer queries: " + ex.getMessage(),
                getClass().getName() + "::parseFilters");
        } catch (java.io.IOException e) {
            throw new WmsException(e);
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

    /**
     * Tries to guesss exactly wich property names are needed to query for a
     * given FeatureTypeInfo and the Filter that will be applied to it. By
     * this way, only the needed propertied will be queried to the underlying
     * FeatureSource in the hope that it will speed up the query
     * 
     * <p>
     * Note that just the attributes exposed by the FeatureTypeInfo will be
     * taken in count. a FeatureTypeInfo exposes all it's attributes except if
     * the subset of desiref exposed attributes are specified in the catalog
     * configuration.
     * </p>
     * 
     * <p>
     * This method guarantiees that at lest the default geometry attribute of
     * <code>layer</code> will be returned.
     * </p>
     *
     * @param layer The layer to process.
     * @param filter The filter to process with.
     *
     * @return An array of the propertyNames needed.
     *
     * @throws java.io.IOException DOCUMENT ME!
     *
     * @task TODO: by now just returns the geometry att. Implement the rest of
     *       the method to find the rest of attributes needed by inspecting
     *       the filter (would be enough to get all the
     *       AttributeExpression's?). I think that the style should be taken
     *       in count too.
     */
    private String[] guessProperties(FeatureTypeInfo layer, Filter filter)
        throws java.io.IOException {
        FeatureType type = layer.getFeatureType();
        List atts = new ArrayList();
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
    protected GetFeatureInfoRequest getRequest() {
        return this.request;
    }

    /**
     * Evaluates if this GetFeatureInfo producer can generate the map format
     * specified by <code>mapFormat</code>, where <code>mapFormat</code> is
     * the MIME type of the requested response.
     *
     * @param mapFormat the MIME type of the output map format requiered
     *
     * @return true if class can produce a map in the passed format
     */
    public boolean canProduce(String mapFormat) {
        return getSupportedFormats().contains(mapFormat);
    }

    /**
     * Gets A list of the formats this delegate supports.
     *
     * @return A list of strings of the formats supported.
     */
    public abstract List getSupportedFormats();
}

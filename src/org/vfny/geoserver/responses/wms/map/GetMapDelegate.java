/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.data.*;
import org.geotools.feature.*;
import org.geotools.filter.*;
import org.vfny.geoserver.*;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.wms.GetMapRequest;
import org.vfny.geoserver.responses.Response;
import java.io.*;
import java.util.*;


/**
 * base class for delegates who creates a map based on a GetMap request.
 * Subclasses should implement one or more output format
 *
 * @author Gabriel Roldán
 * @version $revision$
 */
public abstract class GetMapDelegate implements Response {
    private GetMapRequest request;

    /**
     * Creates a new GetMapDelegate object.
     */
    public GetMapDelegate() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public void execute(Request request) throws ServiceException {
        execute((GetMapRequest) request);
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws WmsException DOCUMENT ME!
     */
    protected void execute(GetMapRequest request) throws ServiceException {
        this.request = request;

        FeatureTypeConfig[] layers = request.getLayers();
        List styles = request.getStyles();
        Filter[] filters = request.getFilters();
        List attributes = request.getAttributes();

        Query[] queries = buildQueries(layers, filters, attributes);
        int nLayers = layers.length;
        FeatureResults[] resultLayers = new FeatureResults[nLayers];
        FeatureTypeConfig ftype = null;
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
     * DOCUMENT ME!
     *
     * @param resultLayers DOCUMENT ME!
     * @param styles DOCUMENT ME!
     *
     * @throws WmsException DOCUMENT ME!
     */
    protected abstract void execute(FeatureTypeConfig[] requestedLayers,
        FeatureResults[] resultLayers, List styles) throws WmsException;

    /**
     * DOCUMENT ME!
     *
     * @param layers DOCUMENT ME!
     * @param filters DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws WmsException DOCUMENT ME!
     */
    private Query[] buildQueries(FeatureTypeConfig[] layers, Filter[] filters,
                                 List attributes)
        throws WmsException {
        int nLayers = layers.length;
        int numFilters = (filters == null) ? 0 : filters.length;
        int numAttributes = attributes.size();

        Query[] queries = new Query[nLayers];
        GetMapRequest request = getRequest();
        Envelope requestExtent = request.getBbox();
        FilterFactory ffactory = FilterFactory.createFilterFactory();

        try {
            Filter finalLayerFilter, customFilter;
            Query layerQuery;

            for (int i = 0; i < nLayers; i++) {
                FeatureType schema = layers[i].getSchema();
                if(numFilters == nLayers)
                  customFilter = filters[i];
                else
                  customFilter = null;

                finalLayerFilter = buildFilter(customFilter, requestExtent,
                        ffactory, schema);

                List layerProperties = numAttributes == nLayers?
                        (List)attributes.get(i) : Collections.EMPTY_LIST;

                String[] props = guessProperties(layers[i],
                                                 finalLayerFilter,
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
     * builds the filter for a layer containing at leas the BBOX filter
     * defined by the extent queries (BBOX param), and optionally AND'ed
     * with the customized filter for that layer (from FILTERS param)
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
     * tryies to guesss exactly wich property names are needed to query for a
     * given FeatureType and the Filter that will be applied to it. By this
     * way, only the needed propertied will be queried to the underlying
     * FeatureSource in the hope that it will speed up the query
     *
     * <p>
     * Note that just the attributes exposed by the FeatureTypeConfig will be
     * taken in count. a FeatureTypeConfig exposes all it's attributes except
     * if the subset of desiref exposed attributes are specified in the
     * catalog configuration.
     * </p>
     *
     * <p>
     * This method guarantiees that at lest the default geometry attribute of
     * <code>layer</code> will be returned.
     * </p>
     *
     * @param layer DOCUMENT ME!
     * @param filter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @task TODO: by now just returns the geometry att. Implement the rest of
     *       the method to find the rest of attributes needed by inspecting
     *       the filter (would be enough to get all the
     *       AttributeExpression's?). I think that the style should be taken
     *       in count too.
     */
    private String[] guessProperties(FeatureTypeConfig layer, Filter filter, List attributes) {
        FeatureType type = layer.getSchema();
        List atts = new ArrayList(attributes);
        String geom_name = type.getDefaultGeometry().getName();
        if(!atts.contains(geom_name))
            atts.add(geom_name);

        String[] properties = (String[])atts.toArray(new String[atts.size()]);

        return properties;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected GetMapRequest getRequest() {
        return this.request;
    }

    /**
     * evaluates if this Map producer can generate the map format specified by
     * <code>mapFormat</code>
     *
     * @param mapFormat the mime type of the output map format requiered
     *
     * @return true if class can produce a map in the passed format
     */
    public abstract boolean canProduce(String mapFormat);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract List getSupportedFormats();
}

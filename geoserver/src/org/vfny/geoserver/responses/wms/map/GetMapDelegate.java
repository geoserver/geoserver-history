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
public abstract class GetMapDelegate implements Response
{
    private GetMapRequest request;

    /**
     * Creates a new GetMapDelegate object.
     */
    public GetMapDelegate()
    {
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public void execute(Request request) throws ServiceException
    {
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
    protected void execute(GetMapRequest request) throws ServiceException
    {
        this.request = request;

        FeatureTypeConfig[] layers = request.getLayers();

        List styles = request.getStyles();

        Query[] queries = buildQueries(layers, request.getFilters());

        int nLayers = layers.length;

        FeatureResults[] resultLayers = new FeatureResults[nLayers];
        FeatureTypeConfig ftype = null;
        Filter filter = null;
        FeatureResults features = null;

        try
        {
            for (int i = 0; i < nLayers; i++)
            {
                ftype = layers[i];
                features = ftype.getFeatureSource().getFeatures(queries[i]);
                resultLayers[i] = features;
            }
        }
        catch (IOException ex)
        {
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
    private Query[] buildQueries(FeatureTypeConfig[] layers, Filter[] filters)
        throws WmsException
    {
        int nLayers = layers.length;
        Query[] queries = new Query[nLayers];
        GetMapRequest request = getRequest();
        Envelope requestExtent = request.getBbox();
        FilterFactory ffactory = FilterFactory.createFilterFactory();

        try
        {
            GeometryFilter bboxFilter;
            Filter requestLayerFilter;
            Filter finalLayerFilter;
            int numFilters = (filters == null) ? 0 : filters.length;
            Query layerQuery;

            for (int i = 0; i < nLayers; i++)
            {
                FeatureType schema = layers[i].getSchema();
                bboxFilter = ffactory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);

                BBoxExpression bboxExpr = ffactory.createBBoxExpression(requestExtent);
                Expression geomAttExpr = ffactory.createAttributeExpression(schema,
                        schema.getDefaultGeometry().getName());
                bboxFilter.addLeftGeometry(geomAttExpr);
                bboxFilter.addRightGeometry(bboxExpr);

                requestLayerFilter = (numFilters == nLayers) ? filters[i] : null;

                if ((requestLayerFilter == null)
                        || (requestLayerFilter == Filter.NONE))
                {
                    finalLayerFilter = bboxFilter;
                }
                else
                {
                    finalLayerFilter = ffactory.createLogicFilter(AbstractFilter.LOGIC_AND);
                    ((LogicFilter) finalLayerFilter).addFilter(bboxFilter);
                    ((LogicFilter) finalLayerFilter).addFilter(requestLayerFilter);
                }

                String[] props = guessProperties(layers[i], finalLayerFilter);
                layerQuery = new DefaultQuery(finalLayerFilter, props);
                queries[i] = layerQuery;
            }
        }
        catch (IllegalFilterException ex)
        {
            throw new WmsException(ex,
                "Can't build layer queries: " + ex.getMessage(),
                getClass().getName() + "::parseFilters");
        }

        return queries;
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
    private String[] guessProperties(FeatureTypeConfig layer, Filter filter)
    {
        FeatureType type = layer.getSchema();
        String[] properties = new String[1];
        properties[0] = type.getDefaultGeometry().getName();

        return properties;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected GetMapRequest getRequest()
    {
        return this.request;
    }
}

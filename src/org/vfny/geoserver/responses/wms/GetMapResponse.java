/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.factory.FactoryFinder;
import org.geotools.feature.FeatureType;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.IllegalFilterException;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleAttributeExtractor;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.wms.GetMapRequest;
import org.vfny.geoserver.responses.Response;
import org.vfny.geoserver.responses.wms.map.GetMapProducer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A GetMapResponse object is responsible of generating a map based on a GetMap
 * request. The way the map is generated is independent of this class, wich
 * will use a delegate object based on the output format requested
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id: GetMapResponse.java,v 1.11 2004/03/14 23:29:30 groldan Exp $
 */
public class GetMapResponse implements Response {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(GetMapResponse.class.getPackage()
                                                                              .getName());

    /**
     * The map producer that will be used for the production of a map in the
     * requested format.
     */
    private GetMapProducer delegate;

    /**
     * Creates a new GetMapResponse object.
     */
    public GetMapResponse() {
        // intentionally left blank
    }

    /**
     * DOCUMENT ME!
     *
     * @param req DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws WmsException DOCUMENT ME!
     */
    public void execute(Request req) throws ServiceException {
        GetMapRequest request = (GetMapRequest) req;

        final String outputFormat = request.getFormat();

        this.delegate = getDelegate(outputFormat);

        final FeatureTypeInfo[] layers = request.getLayers();
        final Style[] styles = buildStyles(request.getStyles(), request.getWMS());

        final WMSMapContext map = new WMSMapContext();

        // @task TODO: replace by setAreaOfInterest(Envelope,
        // CoordinateReferenceSystem)
        // with the user supplied SRS parameter
        map.setAreaOfInterest(request.getBbox());
        map.setMapWidth(request.getWidth());
        map.setMapHeight(request.getHeight());
        map.setBgColor(request.getBgColor());
        map.setTransparent(request.isTransparent());

        LOGGER.fine("setting up map");

        MapLayer layer;

        FeatureSource source;
        for (int i = 0; i < layers.length; i++) {
            Style style = styles[i];

            try {
                source = layers[i].getFeatureSource();
            } catch (IOException exp) {
                LOGGER.log(Level.SEVERE,
                    "Getting feature source: " + exp.getMessage(), exp);
                throw new WmsException(null,
                    "Internal error : " + exp.getMessage());
            }

            checkStyle(style, source);
            layer = new DefaultMapLayer(source, style);

            Filter definitionFilter = layers[i].getDefinitionQuery();

            if (definitionFilter != null) {
                Query definitionQuery = new DefaultQuery(source.getSchema()
                                                               .getTypeName(),
                        definitionFilter);
                layer.setQuery(definitionQuery);
            }

            map.addLayer(layer);
        }

        this.delegate.produceMap(map);
    }

    /**
     * asks the internal GetMapDelegate for the MIME type of the map that it
     * will generate or is ready to, and returns it
     *
     * @param gs DOCUMENT ME!
     *
     * @return the MIME type of the map generated or ready to generate
     *
     * @throws IllegalStateException if a GetMapDelegate is not setted yet
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
        LOGGER.finer("returning content encoding null");

        return null;
    }

    /**
     * if a GetMapDelegate is set, calls it's abort method. Elsewere do
     * nothing.
     *
     * @param gs DOCUMENT ME!
     */
    public void abort(Service gs) {
        if (this.delegate != null) {
            LOGGER.fine("asking delegate for aborting the process");
            this.delegate.abort();
        }
    }

    /**
     * delegates the writing and encoding of the results of the request to the
     * <code>GetMapDelegate</code> wich is actually processing it, and has
     * been obtained when <code>execute(Request)</code> was called
     *
     * @param out the output to where the map must be written
     *
     * @throws ServiceException if the delegate throws a ServiceException
     *         inside its <code>writeTo(OuptutStream)</code>, mostly due to
     * @throws IOException if the delegate throws an IOException inside its
     *         <code>writeTo(OuptutStream)</code>, mostly due to
     * @throws IllegalStateException if this method is called before
     *         <code>execute(Request)</code> has succeed
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        if (this.delegate == null) {
            throw new IllegalStateException(
                "No GetMapDelegate is setted, make sure you have called execute and it has succeed");
        }

        LOGGER.finer("asking delegate for write to " + out);
        this.delegate.writeTo(out);
    }

    /**
     * Creates a GetMapDelegate specialized in generating the requested map
     * format
     *
     * @param outputFormat a request parameter object wich holds the processed
     *        request objects, such as layers, bbox, outpu format, etc.
     *
     * @return A specialization of <code>GetMapDelegate</code> wich can produce
     *         the requested output map format
     *
     * @throws WmsException if no specialization is configured for the output
     *         format specified in <code>request</code> or if it can't be
     *         instantiated
     */
    static GetMapProducer getDelegate(String outputFormat)
        throws WmsException {
        LOGGER.finer("request format is " + outputFormat);

        GetMapProducerFactorySpi mpf = null;
        Iterator mpfi = FactoryFinder.factories(GetMapProducerFactorySpi.class);

        while (mpfi.hasNext()) {
            mpf = (GetMapProducerFactorySpi) mpfi.next();

            if (mpf.canProduce(outputFormat)) {
                break;
            }

            mpf = null;
        }

        if (mpf == null) {
            throw new WmsException("There is no support for creating maps in "
                + outputFormat + " format", "InvalidFormat");
        }

        GetMapProducer producer = mpf.createMapProducer(outputFormat);

        return producer;
    }

    /**
     * Convenient mehtod to inspect the available
     * <code>GetMapProducerFactorySpi</code> and return the set of all the map
     * formats' MIME types that the producers can handle
     *
     * @return a Set&lt;String&gt; with the supported mime types.
     */
    public static Set getMapFormats() {
        Set mapFormats = new HashSet();
        GetMapProducerFactorySpi mpf;
        Iterator mpfi = FactoryFinder.factories(GetMapProducerFactorySpi.class);

        while (mpfi.hasNext()) {
            mpf = (GetMapProducerFactorySpi) mpfi.next();
            mapFormats.addAll(mpf.getSupportedFormats());
        }

        return mapFormats;
    }

    /**
     * DOCUMENT ME!
     *
     * @param styleNames DOCUMENT ME!
     * @param gs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws WmsException DOCUMENT ME!
     */
    protected Style[] buildStyles(List styleNames, WMS gs)
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
     * Checks to make sure that the style passed in can process the
     * FeatureSource.
     *
     * @param style The style to check
     * @param source The source requested.
     *
     * @throws WmsException DOCUMENT ME!
     */
    private void checkStyle(Style style, FeatureSource source)
        throws WmsException {
        FeatureType fType = source.getSchema();
        StyleAttributeExtractor sae = new StyleAttributeExtractor();
        sae.visit(style);

        String[] styleAttributes = sae.getAttributeNames();

        for (int i = 0; i < styleAttributes.length; i++) {
            String attName = styleAttributes[i];

            if (fType.getAttributeType(attName) == null) {
                throw new WmsException(
                    "The requested Style can not be used with "
                    + "this featureType.  The style specifies an attribute of "
                    + attName + " and the featureType definition is: " + fType);
            }
        }
    }
}

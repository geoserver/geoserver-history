/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package com.orci.geoserver.wfs.getnearest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.xml.transform.TransformerException;

import org.geotools.data.FeatureResults;
import org.geotools.feature.FeatureCollection;
import org.geotools.gml.producer.FeatureTransformer;
import org.geotools.gml.producer.FeatureTransformer.FeatureTypeNamespaces;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.NameSpaceInfo;


/**
 * handles the encoding the results of a GetNearest or GetNearestWithLock
 * request's results to GML2 and GML2-GZIP formats.<p>GML2-GZIP format is
 * just GML2 with gzip compression. If GML2-GZIP format was requested,
 * <code>getContentEncoding()</code> will retutn <code>"gzip"</code>,
 * otherwise will return <code>null</code></p>
 *
 * @author Gabriel Rold?n
 * @version $Id: GML2FeatureResponseDelegate.java,v 1.10 2004/09/09 16:48:54 cholmesny Exp $
 */
public class GML2GetNearestResponseDelegate implements GetNearestResponseDelegate {
    private static final int NO_FORMATTING = -1;
    private static final int INDENT_SIZE = 2;
    public static final String formatName = "GML2";
    public static final String formatNameCompressed = "GML2-GZIP";

    /**
     * This is a "magic" class provided by Geotools that writes out GML
     * for an array of FeatureResults.<p>This class seems to do all the
     * work, if you have a problem with GML you will need to hunt it down. We
     * supply all of the header information in the execute method, and work
     * through the featureList in the writeTo method.</p>
     *  <p>This value will be <code>null</code> until execute is
     * called.</p>
     */
    private FeatureTransformer transformer;

    /** will be true if GML2-GZIP output format was requested */
    private boolean compressOutput = false;

    /**
     * the results of a getfeature request wich this object will encode
     * as GML2
     */
    private GetNearestResults results;

    /**
             * empty constructor required to be instantiated through
             * this.class.newInstance()
             */
    public GML2GetNearestResponseDelegate() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param outputFormat DOCUMENT ME!
     *
     * @return true if <code>outputFormat</code> is GML2 or GML2-GZIP
     */
    public boolean canProduce(String outputFormat) {
        return formatName.equalsIgnoreCase(outputFormat)
        || formatNameCompressed.equalsIgnoreCase(outputFormat);
    }

    /**
     * prepares for encoding into GML2 format, optionally compressing
     * its output in gzip, if outputFormat is equal to GML2-GZIP
     *
     * @param outputFormat DOCUMENT ME!
     * @param results DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void prepare(String outputFormat, GetNearestResults results)
        throws IOException {
        this.compressOutput = formatNameCompressed.equalsIgnoreCase(outputFormat);
        this.results = results;

        GetNearestRequest request = results.getRequest();
        GeoServer config = request.getWFS().getGeoServer();
        transformer = new FeatureTransformer();

        FeatureTypeNamespaces ftNames = transformer.getFeatureTypeNamespaces();
        int maxFeatures = 999;
        int serverMaxFeatures = config.getMaxFeatures();

        if (maxFeatures > serverMaxFeatures) {
            maxFeatures = serverMaxFeatures;
        }

        StringBuffer typeNames = new StringBuffer();
        FeatureResults features;
        FeatureTypeInfo meta = null;
        NameSpaceInfo namespace;
        int resCount = results.getResultsetsCount();
        Map ftNamespaces = new HashMap(resCount);

        for (int resIndex = 0; resIndex < resCount; resIndex++) {
            features = results.getFeatures(resIndex);
            meta = results.getTypeInfo(resIndex);
            namespace = meta.getDataStoreInfo().getNameSpace();

            String uri = namespace.getUri();
            ftNames.declareNamespace(features.getSchema(), namespace.getPrefix(), uri);

            if (ftNamespaces.containsKey(uri)) {
                String location = (String) ftNamespaces.get(uri);
                ftNamespaces.put(uri, location + "," + meta.getName());
            } else {
                ftNamespaces.put(uri,
                    request.getBaseUrl() + "wfs/" + "DescribeFeatureType?typeName="
                    + meta.getName());
            }
        }

        System.setProperty("javax.xml.transform.TransformerFactory",
            "org.apache.xalan.processor.TransformerFactoryImpl");

        transformer.setIndentation(config.isVerbose() ? INDENT_SIZE : (NO_FORMATTING));
        transformer.setNumDecimals(config.getNumDecimals());
        transformer.setFeatureBounding(request.getWFS().isFeatureBounding());
        transformer.setEncoding(request.getWFS().getGeoServer().getCharSet());

        String wfsSchemaLoc = request.getSchemaBaseUrl() + "wfs/1.0.0/WFS-basic.xsd";

        transformer.addSchemaLocation("http://www.opengis.net/wfs", wfsSchemaLoc);

        for (Iterator it = ftNamespaces.keySet().iterator(); it.hasNext();) {
            String uri = (String) it.next();
            transformer.addSchemaLocation(uri, (String) ftNamespaces.get(uri));
        }

        transformer.setGmlPrefixing(request.getWFS().getCiteConformanceHacks());

        transformer.setSrsName(request.getWFS().getSrsPrefix() + meta.getSRS());
    }

    /**
     * DOCUMENT ME!
     *
     * @param gs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType(GeoServer gs) {
        return compressOutput ? "application/gzip" : gs.getMimeType();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentEncoding() {
        //        return compressOutput ? "gzip" : null;
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param output DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void encode(OutputStream output) throws ServiceException, IOException {
        if (results == null) {
            throw new IllegalStateException("It seems prepare() has not been called"
                + " or has not succeed");
        }

        GZIPOutputStream gzipOut = null;

        if (compressOutput) {
            gzipOut = new GZIPOutputStream(output);
            output = gzipOut;
        }

        // execute should of set all the header information
        // including the lockID
        //
        // execute should also fail if all of the locks could not be aquired
        List resultsList = results.getFeatures();
        FeatureCollection[] featureResults = (FeatureCollection[]) resultsList.toArray(new FeatureCollection[resultsList
                .size()]);

        try {
            transformer.transform(featureResults, output);

            //we need to "finish" here because if not,it is possible that the gzipped
            //content do not gets completely written
            if (gzipOut != null) {
                gzipOut.finish();
                gzipOut.flush();
            }
        } catch (TransformerException gmlException) {
            ServiceException serviceException = new ServiceException(results.getRequest().getHandle()
                    + " error:" + gmlException.getMessage());
            serviceException.initCause(gmlException);
            throw serviceException;
        }
    }

    public String getContentDisposition(String featureTypeName) {
        if (compressOutput) {
            return "attachment; filename=" + featureTypeName + ".gml.gz";
        } else {
            return "inline; filename=" + featureTypeName + ".gml";
        }
    }
}

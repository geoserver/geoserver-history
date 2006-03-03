/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.responses;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.xml.transform.TransformerException;

import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureResults;
import org.geotools.gml.producer.GML3FeatureTransformer;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.vfny.geoserver.wfs.requests.FeatureRequest;
import org.xml.sax.helpers.NamespaceSupport;


/**
 * handles the encoding the results of a GetFeature or GetFeatureWithLock
 * request's results to GML3 and GML3-GZIP formats.
 * 
 * <p>
 * GML2-GZIP format is just GML2 with gzip compression. If GML2-GZIP format was
 * requested, <code>getContentEncoding()</code> will retutn
 * <code>"gzip"</code>, otherwise will return <code>null</code>
 * </p>
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 * @todo by now its practically a copy of GML2FeatureResponseDelegate. Will extract commons
 * out to a single base class while in the move to fm branch
 */
public class GML3FeatureResponseDelegate implements FeatureResponseDelegate {
    private static final int NO_FORMATTING = -1;
    private static final int INDENT_SIZE = 2;

    public static final String formatName = "GML3";
    public static final String formatNameCompressed = "GML3-GZIP";
    
    /**
     * This is a "magic" class provided by Geotools that writes out GML for an
     * array of FeatureResults.
     * 
     * <p>
     * This class seems to do all the work, if you have a problem with GML you
     * will need to hunt it down. We supply all of the header information in
     * the execute method, and work through the featureList in the writeTo
     * method.
     * </p>
     * 
     * <p>
     * This value will be <code>null</code> until execute is called.
     * </p>
     */
    private GML3FeatureTransformer transformer;

    /** will be true if GML3-GZIP output format was requested */
    private boolean compressOutput = false;

    /**
     * the results of a getfeature request wich this object will encode as GML2
     */
    private GetFeatureResults results;

    /**
     * empty constructor required to be instantiated through
     * this.class.newInstance()
     */
    public GML3FeatureResponseDelegate() {
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
     * prepares for encoding into GML2 format, optionally compressing its
     * output in gzip, if outputFormat is equal to GML2-GZIP
     *
     * @param outputFormat DOCUMENT ME!
     * @param results DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void prepare(String outputFormat, GetFeatureResults results)
        throws IOException {
        this.compressOutput = formatNameCompressed.equalsIgnoreCase(outputFormat);
        this.results = results;

        FeatureRequest request = results.getRequest();
        GeoServer config = request.getWFS().getGeoServer();
        transformer = new GML3FeatureTransformer();

        //FeatureTypeNamespaces ftNames = transformer.getFeatureTypeNamespaces();
        NamespaceSupport namespaces = transformer.getFeatureNamespaces();
        
        int maxFeatures = request.getMaxFeatures();
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
            
            //@todo: this method of declaring namespace throws an exception
            /*
            ftNames.declareNamespace(features.getSchema(),
                namespace.getPrefix(), uri);
            */
            namespaces.declarePrefix(namespace.getPrefix(), uri);
            
            if (ftNamespaces.containsKey(uri)) {
                String location = (String) ftNamespaces.get(uri);
                ftNamespaces.put(uri, location + "," + meta.getName());
            } else {
                ftNamespaces.put(uri,
                    request.getBaseUrl() + "wfs/"
                    + "DescribeFeatureType?typeName=" + meta.getName());
            }
        }

        System.setProperty("javax.xml.transform.TransformerFactory",
            "org.apache.xalan.processor.TransformerFactoryImpl");

        transformer.setIndentation(config.isVerbose() ? INDENT_SIZE
                                                      : (NO_FORMATTING));
        transformer.setNumDecimals(config.getNumDecimals());
        //JD: uncomment, we need to merge the boundedBy stuff onto this branch
        //transformer.setFeatureBounding(request.getWFS().isFeatureBounding());
        transformer.setEncoding(request.getWFS().getGeoServer().getCharSet());

        String wfsSchemaLoc = request.getSchemaBaseUrl()
            + "wfs/1.0.0/WFS-basic.xsd";

        transformer.addSchemaLocation("http://www.opengis.net/wfs", wfsSchemaLoc);

        for (Iterator it = ftNamespaces.keySet().iterator(); it.hasNext();) {
            String uri = (String) it.next();
            transformer.addSchemaLocation(uri, (String) ftNamespaces.get(uri));
        }

        transformer.setGmlPrefixing(request.getWFS().getCiteConformanceHacks());

        FeatureLock featureLock = results.getFeatureLock();

        if (featureLock != null) {
            transformer.setLockId(featureLock.getAuthorization());
        }

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
        return gs.getMimeType();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentEncoding() {
        return compressOutput ? "gzip" : null;
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
    public void encode(OutputStream output)
        throws ServiceException, IOException {
        if (results == null) {
            throw new IllegalStateException(
                "It seems prepare() has not been called"
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
        FeatureResults[] featureResults = (FeatureResults[]) resultsList
            .toArray(new FeatureResults[resultsList.size()]);

        try {
            transformer.transform(featureResults, output);

            //we need to "finish" here because if not,it is possible that the gzipped
            //content do not gets completely written
            if (gzipOut != null) {
                gzipOut.finish();
                gzipOut.flush();
            }
        } catch (TransformerException gmlException) {
            ServiceException serviceException = new ServiceException(results.getRequest()
                                                                            .getHandle()
                    + " error:" + gmlException.getMessage());
            serviceException.initCause(gmlException);
            throw serviceException;
        }
    }
}

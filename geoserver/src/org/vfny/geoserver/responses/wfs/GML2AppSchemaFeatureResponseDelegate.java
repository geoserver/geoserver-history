/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import  org.vfny.geoserver.responses.wfs.ASFeatureTransformer;
import  org.vfny.geoserver.responses.wfs.ASFeatureTransformer.FeatureTypeNamespaces;


import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureResults;

// the imports below available through org.vfny.geoserver.responses.wfs.ASFeatureTransformer
// import org.geotools.gml.producer.FeatureTransformer;
// import org.geotools.gml.producer.FeatureTransformer.FeatureTypeNamespaces;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.vfny.geoserver.requests.wfs.FeatureRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.xml.transform.TransformerException;

import java.util.logging.Logger;

/**
 *
 * This is a direct copy of GML2FeatureResponseDelegate; it could be subclassed
 * except that it expects to opertae on FeatureTransformer, which I have
 * found I could not sub class, and which I need to subclass for outputting
 * nested XML application - see ASFeatureTransformer
 * 
 * Handles the encoding the results of a GetFeature request's results to 
 * GML2-AS or GML2-GZIP-AS. "AS" means Application Schema, as per the 
 * xpath attributes in a schema.xml document, accompanying the feature
 * type's info.xml.
 * 
 * <p>
 * GML2-GZIP format is just GML2 with gzip compression. If GML2-GZIP format was
 * requested, <code>getContentEncoding()</code> will retutn
 * <code>"gzip"</code>, otherwise will return <code>null</code>
 * </p>
 *
 * @author Gabriel Roldán
 * @version $Id$
 */
public class GML2AppSchemaFeatureResponseDelegate implements FeatureResponseDelegate {

    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.responses.wfs");
	
	private static final int NO_FORMATTING = -1;
    private static final int INDENT_SIZE = 2;

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
    private ASFeatureTransformer transformer;

    /** will be true if GML2-GZIP output format was requested */
    private boolean compressOutput = false;

    /**
     * the results of a getfeature request wich this object will encode as GML2
     */
    private GetFeatureResults results;

    /**
     * empty constructor required to be instantiated through
     * this.class.newInstance()
     */
    public GML2AppSchemaFeatureResponseDelegate() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param outputFormat DOCUMENT ME!
     *
     * @return true if <code>outputFormat</code> is GML2 or GML2-GZIP
     */
    public boolean canProduce(String outputFormat) {
        return "GML2-AS".equalsIgnoreCase(outputFormat)
        || "GML2-AS-GZIP".equalsIgnoreCase(outputFormat);
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
    	throws IOException  {
    	prepare(outputFormat, results, new ASFeatureTransformer());
    }
    
    /**
     * prepares for encoding into GML2 format, optionally compressing its
     * output in gzip, if outputFormat is equal to GML2-GZIP
     *
     * @param outputFormat DOCUMENT ME!
     * @param results DOCUMENT ME!
     * @param featureTransformer - FeatureTranformer or subclass for different output style
     *
     * @throws IOException DOCUMENT ME!
     */
    public void prepare(String outputFormat, GetFeatureResults results,
    		ASFeatureTransformer featureTransformer)
        throws IOException {
        this.compressOutput = "GML2-GZIP".equalsIgnoreCase(outputFormat);
        this.results = results;

        FeatureRequest request = results.getRequest();
        GeoServer config = request.getWFS().getGeoServer();
        // transformer = new FeatureTransformer();
        transformer = featureTransformer;        

        FeatureTypeNamespaces ftNames = transformer.getFeatureTypeNamespaces();
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
            ftNames.declareNamespace(features.getSchema(),
                namespace.getPrefix(), uri);

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
	// transformer.setEncoding(request.getWFS().getGeoServer().getCharSet());
        String wfsSchemaLoc = request.getSchemaBaseUrl()
            + "wfs/1.0.0/WFS-basic.xsd";

        transformer.addSchemaLocation("http://www.opengis.net/wfs", wfsSchemaLoc);

        for (Iterator it = ftNamespaces.keySet().iterator(); it.hasNext();) {
            String uri = (String) it.next();
            transformer.addSchemaLocation(uri, (String) ftNamespaces.get(uri));
        }

        transformer.setGmlPrefixing(request.getWFS().isGmlPrefixing());
	
        FeatureLock featureLock = results.getFeatureLock();

        if (featureLock != null) {
            transformer.setLockId(featureLock.getAuthorization());
        }

        transformer.setSrsName("http://www.opengis.net/gml/srs/epsg.xml#"
            + meta.getSRS());
     
        // set feature type - XML element structure association in the transformer
        // XML element structure derived from schema.xml for WFS feature type
        transformer.setXMLelementStructure(results);
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
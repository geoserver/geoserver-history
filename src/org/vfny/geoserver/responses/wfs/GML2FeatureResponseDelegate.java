/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureResults;
import org.geotools.gml.producer.FeatureTransformer;
import org.geotools.gml.producer.FeatureTransformer.FeatureTypeNamespaces;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.vfny.geoserver.requests.wfs.FeatureRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import javax.xml.transform.TransformerException;


/**
 * handles the encoding the results of a GetFeature or GetFeatureWithLock
 * request's results to GML2 and GML2.gz formats.
 * 
 * <p>
 * GML2.gz format is just GML2 with gzip compression. If GML2.gz format was
 * requested, <code>getContentEncoding()</code> will retutn
 * <code>"gzip"</code>, otherwise will return <code>null</code>
 * </p>
 *
 * @author Gabriel Roldán
 * @version $Id: GML2FeatureResponseDelegate.java,v 1.2 2004/03/12 10:19:44 cholmesny Exp $
 */
public class GML2FeatureResponseDelegate implements FeatureResponseDelegate {
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
    private FeatureTransformer transformer;

    /** will be true if GML2.gz output format was requested */
    private boolean compressOutput = false;

    /** the results of a getfeature request wich this object will encode as GML2 */
    private GetFeatureResults results;

    /**
     * empty constructor required to be instantiated through
     * this.class.newInstance()
     */
    public GML2FeatureResponseDelegate() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param outputFormat DOCUMENT ME!
     *
     * @return true if <code>outputFormat</code> is GML2 or GML2.gz
     */
    public boolean canProduce(String outputFormat) {
        return "GML2".equalsIgnoreCase(outputFormat)
        || "GML2.gz".equalsIgnoreCase(outputFormat);
    }

    /**
     * prepares for encoding into GML2 format, optionally compressing its
     * output in gzip, if outputFormat is equal to GML2.gz
     *
     * @param outputFormat DOCUMENT ME!
     * @param results DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void prepare(String outputFormat, GetFeatureResults results)
        throws IOException {
        this.compressOutput = "GML2.gz".equalsIgnoreCase(outputFormat);
        this.results = results;

        FeatureRequest request = results.getRequest();
        GeoServer config = request.getWFS().getGeoServer();
        transformer = new FeatureTransformer();

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

        for (int resIndex = 0; resIndex < resCount; resIndex++) {
            features = results.getFeatures(resIndex);
            meta = results.getTypeInfo(resIndex);
            namespace = meta.getDataStoreInfo().getNameSpace();
            ftNames.declareNamespace(features.getSchema(),
                namespace.getPrefix(), namespace.getUri());
            typeNames.append(namespace.getPrefix() + ":"
                + meta.getFeatureType().getTypeName());

            if ((resIndex < (resCount - 1)) && (maxFeatures > 0)) {
                typeNames.append(",");
            }
        }

        System.setProperty("javax.xml.transform.TransformerFactory",
            "org.apache.xalan.processor.TransformerFactoryImpl");

        //bad hardcode - 2 is to indent 2 spaces, -1 is to do no indenting.
        transformer.setIndentation(config.isVerbose() ? 2 : (-1));

        String wfsSchemaLoc = request.getBaseUrl() + "wfs/1.0.0/"
            + "WFS-basic.xsd";
        String fSchemaLoc = request.getBaseUrl() + "wfs/"
            + "DescribeFeatureType?typeName=" + typeNames.toString();
        transformer.addSchemaLocation("http://www.opengis.net/wfs", wfsSchemaLoc);
        namespace = meta.getDataStoreInfo().getNameSpace();
        transformer.addSchemaLocation(namespace.getUri(), fSchemaLoc);
        transformer.setGmlPrefixing(true); //TODO: make this a user config

        FeatureLock featureLock = results.getFeatureLock();

        if (featureLock != null) {
            // TODO: chris needs to add the lock authorization to
            //       the transformer header info
            transformer.setLockId(featureLock.getAuthorization());
        }

        transformer.setSrsName("http://www.opengis.net/gml/srs/epsg.xml#"
            + meta.getSRS());
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

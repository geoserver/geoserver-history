/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.data.FeatureStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.gml.producer.FeatureTransformer;
import org.geotools.gml.producer.FeatureTransformer.FeatureTypeNamespaces;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.vfny.geoserver.requests.wfs.FeatureRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.transform.TransformerException;


/**
 * Returns a shapefile from a GetFeature response.  This is still a bit of an
 * experiment, but we are returning a zipped shapefile at the moment.
 * 
 * <p>
 * This version just uses a Shapefile DataStore to write out the files.  Right
 * now it will write out the first shapefile, and it only does the dbf and shp
 * files.  The rest should not be too hard to handle though.  In the future we
 * should use the Shapefile's writing methods directly, but this may take a
 * bit of refactoring.  It would be nice if instead of the DataStore low
 * level writer (which is probably getting deprecated anyways), we had output
 * writers for DataStores, that would write to a stream.  This would open up
 * the possibility of a gt2gt converter, read in with a postgis datastore,
 * write to a shapefile.
 * </p>
 * 
 * <p>
 * The other big problem we have right now is that when you go to the url the
 * method that a browser gives you to save is not very nice.  The default for
 * mine seems to be GetFeature - not even GetFeature.zip or anything nice like
 * that.
 * </p>
 * 
 * <p>
 * There also seem to be problems parsing with a postgis datastore, we get the
 * shapefile expecting a double, but getting a multi line string.  Not clear
 * at all why, it is weird, because it doesn't happen write at the beginning,
 * seems about half way or something.
 * </p>
 * 
 * <p>
 * At present we only write out the first of the FeatureResults, but should not
 * be too hard to write them all out, just fill up the zip file with  several
 * different shapefiles.
 * </p>
 * 
 * <p>
 * Another interesting thought could be to have all generated files like this
 * just return a url location to the browser.  Then the user could go to the
 * file and download it.  This gets into interesting implications for
 * geoserver as a real spatial data download manager.
 * </p>
 *
 * @author Chris Holmes
 * @version $Id$
 *
 * @task TODO: lots of cleanup.  Get working with more than one feature result,
 *       get rid of duplicate code in writing out shp and dbf files, and add
 *       shx,  (and prj?).  And try to get shapefile writer working directly,
 *       instead of writing to a temp file using the DataStore.
 * @task HACK: Since we are just writing to temp files right now we should move
 *       all of that to the prepare method, and just use the encode method to
 *       write out the results.  That's the whole point of preparing, so we
 *       get errors before the output stream heads out.
 */
public class ShapeFeatureResponseDelegate implements FeatureResponseDelegate {
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wfs");

    /** will be true if Shape-GZIP output format was requested */
    private boolean compressOutput = false;

    /**
     * the results of a getfeature request wich this object will encode as
     * Shape
     */
    private GetFeatureResults results;

    /**
     * empty constructor required to be instantiated through
     * this.class.newInstance()
     */
    public ShapeFeatureResponseDelegate() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param outputFormat DOCUMENT ME!
     *
     * @return true if <code>outputFormat</code> is Shape or Shape-GZIP
     */
    public boolean canProduce(String outputFormat) {
        return "Shape".equalsIgnoreCase(outputFormat)
        || "SHAPE-ZIP".equalsIgnoreCase(outputFormat);
    }

    /**
     * prepares for encoding into Shape format, optionally compressing its
     * output in gzip, if outputFormat is equal to SHAPE-ZIP
     *
     * @param outputFormat DOCUMENT ME!
     * @param results DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void prepare(String outputFormat, GetFeatureResults results)
        throws IOException {
        this.compressOutput = "SHAPE-ZIP".equalsIgnoreCase(outputFormat);
        this.results = results;

        FeatureRequest request = results.getRequest();
        GeoServer config = request.getWFS().getGeoServer();

        //transformer = new FeatureTransformer();
        //FeatureTypeNamespaces ftNames = transformer.getFeatureTypeNamespaces();
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

        //TODO: move the majority of the encode stuff here.
    }

    /**
     * DOCUMENT ME!
     *
     * @param gs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType(GeoServer gs) {
        return "application/zip"; //gs.getMimeType();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentEncoding() {
        return "zip"; //return compressOutput ? "gzip" : null;
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

        File tempZip = new File(System.getProperty("java.io.tmpdir"),
                "shape.zip");
        FileOutputStream tempFileOS = new FileOutputStream(tempZip);
        ZipOutputStream zipOut = null;
        LOGGER.info("zip out location is: " + tempZip);
        zipOut = new ZipOutputStream(output);
        output = zipOut;

        List resultsList = results.getFeatures();
        FeatureResults[] featureResults = (FeatureResults[]) resultsList
            .toArray(new FeatureResults[resultsList.size()]);
        FeatureReader reader = featureResults[0].reader();
        String name = featureResults[0].getSchema().getTypeName();
        File file = new File(System.getProperty("java.io.tmpdir"), name);
        ShapefileDataStore sfds = new ShapefileDataStore(file.toURL());
        sfds.createSchema(featureResults[0].getSchema());

        FeatureStore store = (FeatureStore) sfds.getFeatureSource(name);
        store.addFeatures(reader);

        //zipOut.
        ZipEntry entry = new ZipEntry(name + ".shp");
        zipOut.putNextEntry(entry);

        try {
            LOGGER.info("first feature is: "
                + store.getFeatures().reader().next());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //put this junk in a real method, get rid of silly code duplication.
        InputStream is = new FileInputStream(file.getAbsolutePath() + ".shp");
        int c;

        while (-1 != (c = is.read())) {
            output.write(c);
        }

        zipOut.closeEntry();
        entry = new ZipEntry(name + ".dbf");
        zipOut.putNextEntry(entry);

        InputStream dbf = new FileInputStream(file.getAbsolutePath() + ".dbf");
        c = 0;

        while (-1 != (c = dbf.read())) {
            output.write(c);
        }

        zipOut.closeEntry();

        if (zipOut != null) {
            zipOut.finish();
            zipOut.flush();
        }

        //don't think we actually want this here...
        zipOut.close();
    }
}

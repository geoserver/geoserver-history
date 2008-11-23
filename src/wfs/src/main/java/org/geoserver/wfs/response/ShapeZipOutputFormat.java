/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.response;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geotools.data.FeatureStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.namespace.QName;


/**
 *
 * This class returns a shapefile encoded results of the users's query.
 *
 * Based on ShapeFeatureResponseDelegate.java from geoserver 1.5.x
 *
 * @author originally authored by Chris Holmes, The Open Planning Project, cholmes@openplans.org
 * @author ported to gs 1.6.x by Saul Farber, MassGIS, saul.farber@state.ma.us
 *
 */
public class ShapeZipOutputFormat extends WFSGetFeatureOutputFormat implements ApplicationContextAware {
    private final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(this.getClass().toString());
    private String outputFileName;
    private ApplicationContext applicationContext;

    public ShapeZipOutputFormat() {
        super("SHAPE-ZIP");
    }

    /**
     * @see WFSGetFeatureOutputFormat#getMimeType(Object, Operation)
     */
    public String getMimeType(Object value, Operation operation)
        throws ServiceException {
        return "application/zip";
    }
    
    public String getCapabilitiesElementName() {
        return "SHAPE-ZIP";
    }

    /**
     * We abuse this method to pre-discover the query typenames so we know what to set in the
     * content-disposition header.
     */
    protected boolean canHandleInternal(Operation operation) {
        GetFeatureType request = (GetFeatureType) OwsUtils.parameter(operation.getParameters(),
                GetFeatureType.class);
        outputFileName = ((QName) ((QueryType) request.getQuery().get(0)).getTypeName().get(0))
            .getLocalPart();

        return true;
    }

    public String[][] getHeaders(Object value, Operation operation)
        throws ServiceException {
        return (String[][]) new String[][] {
            { "Content-Disposition", "attachment; filename=" + outputFileName + ".zip" }
        };
    }

    /**
     * @see WFSGetFeatureOutputFormat#write(Object, OutputStream, Operation)
     */
    @SuppressWarnings("unchecked")
    protected void write(FeatureCollectionType featureCollection, OutputStream output,
        Operation getFeature) throws IOException, ServiceException {
        //We might get multiple featurecollections in our response (multiple queries?) so we need to
        //write out multiple shapefile sets, one for each query response.
        File tempDir = createTempDirectory();
        ZipOutputStream zipOut = new ZipOutputStream(output);
        
        try {
            Iterator<FeatureCollection<SimpleFeatureType, SimpleFeature>> outputFeatureCollections;
            outputFeatureCollections = featureCollection.getFeature().iterator();
            FeatureCollection<SimpleFeatureType, SimpleFeature> curCollection;

            while (outputFeatureCollections.hasNext()) {
                curCollection = outputFeatureCollections.next();
                writeCollectionToShapefile(curCollection, tempDir, getShapefileCharset(getFeature));

                String name = curCollection.getSchema().getTypeName();
                String outputName = name.replaceAll("\\.", "_");

                // read in and write out .shp
                File f;
                ZipEntry entry;
                addFile(tempDir, zipOut, name, outputName, ".shp");
                addFile(tempDir, zipOut, name, outputName, ".dbf");
                addFile(tempDir, zipOut, name, outputName, ".shx");
                addFile(tempDir, zipOut, name, outputName, ".prj");
                addFile(tempDir, zipOut, name, outputName, ".cst");
            }

            zipOut.finish();
            zipOut.flush();

            // This is an error, because this closes the output stream too... it's
            // not the right place to do so
            // zipOut.close();
        } finally {
            // make sure we remove the temp directory and its contents completely now
            if (!removeDirectory(tempDir)) {
                LOGGER.warning("Could not delete temp directory: " + tempDir.getAbsolutePath());
            }
        }
    }

    private void addFile(File tempDir, ZipOutputStream zipOut, String name, String outputName,
            final String extension) throws IOException, FileNotFoundException {
        File f = new File(tempDir, name + extension);
            if(f.exists()) {
            ZipEntry entry = new ZipEntry(outputName + extension);
            zipOut.putNextEntry(entry);
    
            InputStream in = new FileInputStream(f);
            int c;
            byte[] buffer = new byte[4 * 1024];
            while (-1 != (c = in.read(buffer))) {
                zipOut.write(buffer,0,c);
            }
            zipOut.closeEntry();
            in.close();
        }
    }

    /**
     * Looks up the charset parameter, either in the GetFeature request or as a global parameter
     * @param getFeature
     * @return the found charset, or the platform's default one if none was specified
     */
    private Charset getShapefileCharset(Operation getFeature) {
        Charset result = null;
        
        GetFeatureType gft = (GetFeatureType) getFeature.getParameters()[0];
        if(gft.getFormatOptions() != null && gft.getFormatOptions().get("CHARSET") != null) {
           result = (Charset) gft.getFormatOptions().get("CHARSET");
        } else {
            final String charsetName = GeoServerExtensions.getProperty("GS-SHAPEFILE-CHARSET", applicationContext);
            if(charsetName != null)
                result = Charset.forName(charsetName);
        }
        
        return result != null ? result : Charset.defaultCharset();
    }

    private boolean removeDirectory(File tempDir) {
        if (!tempDir.exists() || !tempDir.isDirectory()) {
            return false;
        }

        File[] files = tempDir.listFiles();

        if (files == null) {
            return false;
        }

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                removeDirectory(files[i]);
            } else {
                files[i].delete();
            }
        }

        return tempDir.delete();
    }

    /**
     * Write one featurecollection to an appropriately named shapefile.
     * @param c the featurecollection to write
     * @param tempDir the temp directory into which it should be written
     */
    private void writeCollectionToShapefile(FeatureCollection<SimpleFeatureType, SimpleFeature> c, File tempDir, Charset charset) {
        SimpleFeatureType schema = c.getSchema();

        try {
            File file = new File(tempDir, schema.getTypeName() + ".shp");
            ShapefileDataStore sfds = new ShapefileDataStore(file.toURL());
            
            // handle shapefile encoding
            // and dump the charset into a .cst file, for debugging and control purposes
            // (.cst is not a standard extension)
            sfds.setStringCharset(charset);
            File charsetFile = new File(tempDir, schema.getTypeName()+ ".cst");
            PrintWriter pw = null;
            try {
                pw  = new PrintWriter(charsetFile);
                pw.write(charset.name());
            } finally {
                if(pw != null) pw.close();
            }

            try {
                sfds.createSchema(schema);
            } catch (NullPointerException e) {
                LOGGER.warning(
                    "Error in shapefile schema. It is possible you don't have a geometry set in the output. \n"
                    + "Please specify a <wfs:PropertyName>geom_column_name</wfs:PropertyName> in the request");
                throw new ServiceException(
                    "Error in shapefile schema. It is possible you don't have a geometry set in the output.");
            }

            FeatureStore<SimpleFeatureType, SimpleFeature> store;
            store = (FeatureStore<SimpleFeatureType, SimpleFeature>) sfds.getFeatureSource(schema.getTypeName());
            store.addFeatures(c);
            try {
                if(schema.getCoordinateReferenceSystem() != null)
                    sfds.forceSchemaCRS(schema.getCoordinateReferenceSystem());
            } catch(Exception e) {
                LOGGER.log(Level.WARNING, "Could not properly create the .prj file", e);
            }
        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING,
                "Error while writing featuretype '" + schema.getTypeName() + "' to shapefile.", ioe);
            throw new ServiceException(ioe);
        }
    }

    /**
     * Creates a temporary directory into which we'll write the various shapefile components for this response.
     *
     * Strategy is to leverage the system temp directory, then create a sub-directory.
     * @return
     */
    private File createTempDirectory() {
        try {
            File dummyTemp = File.createTempFile("blah", null);
            String sysTempDir = dummyTemp.getParentFile().getAbsolutePath();
            dummyTemp.delete();

            File reqTempDir = new File(sysTempDir + File.separator + "wfsshptemp" + Math.random());
            reqTempDir.mkdir();

            return reqTempDir;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,
                "Unable to properly create a temporary directory when trying to output a shapefile.  Is the system temp directory writeable?",
                e);
            throw new ServiceException(
                "Error in shapefile schema. It is possible you don't have a geometry set in the output.");
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

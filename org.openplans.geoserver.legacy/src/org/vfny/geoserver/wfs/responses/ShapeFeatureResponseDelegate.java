/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.responses;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.data.FeatureStore;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.GeoServer;



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
 *       instead of writing to a temp file using the DataStore. - can't do, easily.
 *       Not worth the time as it won't speed it up and will take much time to 
 *       implement - brent
 * @task HACK: Since we are just writing to temp files right now we should move
 *       all of that to the prepare method, and just use the encode method to
 *       write out the results.  That's the whole point of preparing, so we
 *       get errors before the output stream heads out.
 */
public class ShapeFeatureResponseDelegate implements FeatureResponseDelegate {
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.wfs.responses");

    String tempDir = null;
    public static final String formatName = "SHAPE-ZIP";
    
    /** will be true if Shape-ZIP output format was requested */
    //private boolean compressOutput = false;	// already in ZIP by default

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
        return formatName.equalsIgnoreCase(outputFormat);
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
        throws IOException 
    {
        //this.compressOutput = true;
        this.results = results;

        if (results == null) {
            throw new IllegalStateException(
                "It seems prepare() has not succeed. <results> is null");
        }
        
        tempDir = System.getProperty("java.io.tmpdir");
        
        if (tempDir == null) {
        	throw new NullPointerException("<tempDir> is null. " +
        			"There is a problem with the java.io.tempdir directory.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param gs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType(GeoServer gs) {
        return "application/zip";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentEncoding() {
        //I don't think this is right, but I think perhaps it doesnt matter
        //right now, this field has to do with the charSet.  Right now 
        //shapefile hardcodes the charset to IS0-8859-1, so we should probably
        //return that for this param.  I don't know that it'd even get used 
        //though.  We should still do the fix to allow shapefiles to be 
        //configured with different charsets. -ch
        return formatName;
    }

    /**
     * encode()
     * 
     * Description:
     * This will save out the features to a shapefile using the ShapefileWriter and 
     * DbaseWriter that live underneath the ShapefileDataStore. Once they are written out, 
     * they are read back in and streamed out to a zip output stream to the user.
     * Each entry, .shp and .dbf, are read in separately but bundled in the same zip file.
     *
     * @param output DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void encode(OutputStream output)
        throws ServiceException, IOException 
    {
        if (results == null) {
            throw new IllegalStateException(
                "It seems prepare() has not been called"
                + " or has not succeed");
        }

        ZipOutputStream zipOut = new ZipOutputStream(output);
        output = zipOut;
        
        List resultsList = results.getFeatures();
        FeatureResults[] featureResults = (FeatureResults[]) resultsList
            .toArray(new FeatureResults[resultsList.size()]);
        FeatureReader reader = featureResults[0].reader();
        String name = featureResults[0].getSchema().getTypeName();
        
        String namePath = tempDir+name;
        
        try {
	        writeOut(name, tempDir, featureResults, reader);
        } catch (IOException e)
        {
        	throw e;
        }
        
        // BEGIN RELOADING and ZIPPING
        
        // read in and write out .shp
        ZipEntry entry = new ZipEntry(name + ".shp");
        zipOut.putNextEntry(entry);
        InputStream shp_in = new FileInputStream(namePath + ".shp");
		readInWriteOutBytes(output, shp_in);
        zipOut.closeEntry();
        shp_in.close();
        
        // read in and write out .dbf
        entry = new ZipEntry(name + ".dbf");
        zipOut.putNextEntry(entry);
        InputStream dbf_in = new FileInputStream(namePath + ".dbf");
        readInWriteOutBytes(output, dbf_in);
        zipOut.closeEntry();
        dbf_in.close();
        
        zipOut.finish();
        zipOut.flush();
        zipOut.close();
    }

	/**
	 * readInWriteOutBytes
	 * 
	 * Description:
	 * Reads in the bytes from the input stream and writes them to the output stream.
	 * 
	 * @param output
	 * @param in
	 * @throws IOException
	 */
	private void readInWriteOutBytes(OutputStream output, InputStream in) throws IOException {
		int c;

        while (-1 != (c = in.read())) {
            output.write(c);
        }
	}
    
    
    /**
     * 
     * Description:
     * Write the old way Chris had it. It creates a ShapefileDataStore to write out the
     * shapefile, which we don't think is quick, O(n^2): addFeature() loads all stored 
     * features and then adds the next one and writes out again, or memory friendly: loading
     * all thos features.
     * This theory needs testing. RESPONSE: Tested. It only reads through the entire shapefile
     * if the shapefile is not yep opened. The first time you add a feature to the shapefile, 
     * it has to crawl to the end, O(n). It saves this point and uses it as reference for
     * future appends. So each time after when you add another feature, it jsut calls that 
     * pointer to the end, and appends there. So it it only O(n) once, then constant time 
     * after that. - brent
     * 
     * In the end, a shapefile is written to disk.
     * 
     * @param name
     * @param featureResults
     * @param reader
     * @throws IOException
     */
    private void writeOut(String name,
    						String tempDir, 
				    		FeatureResults[] featureResults, 
				    		FeatureReader reader) 
    throws IOException
    {
    	//File file = new File(System.getProperty("java.io.tmpdir"), name+".zip");
    	File file = new File(tempDir, name/*+".zip"*/);
    	
    	ShapefileDataStore sfds = new ShapefileDataStore(file.toURL());
    	sfds.createSchema(featureResults[0].getSchema());
    	
    	FeatureStore store = (FeatureStore) sfds.getFeatureSource(name);
    	store.addFeatures(reader);
    }
  
    
}

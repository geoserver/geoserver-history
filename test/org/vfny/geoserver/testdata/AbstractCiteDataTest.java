/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.testdata;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.feature.FeatureType;
import org.geotools.feature.SchemaException;
import org.geotools.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * <p>
 * The properties files with CITE data are copied to the users' temp directory
 * only the first time <code>getCiteDataStore()</code> is called, for each
 * test run (i.e., they're not copied in <code>setUp()</code>).
 * </p>
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public abstract class AbstractCiteDataTest extends TestCase {
    /** DOCUMENT ME! */
    public static String BASIC_POLYGONS_TYPE = "BasicPolygons";

    /** DOCUMENT ME! */
    public static String BRIDGES_TYPE = "Bridges";

    /** DOCUMENT ME! */
    public static String BUILDINGS_TYPE = "Buildings";

    /** DOCUMENT ME! */
    public static String DIVIDED_ROUTES_TYPE = "DividedRoutes";

    /** DOCUMENT ME! */
    public static String FORESTS_TYPE = "Forests";

    /** DOCUMENT ME! */
    public static String LAKES_TYPE = "Lakes";

    /** DOCUMENT ME! */
    public static String MAP_NEATLINE_TYPE = "MapNeatline";

    /** DOCUMENT ME! */
    public static String NAMED_PLACES_TYPE = "NamedPlaces";

    /** DOCUMENT ME! */
    public static String PONDS_TYPE = "Ponds";

    /** DOCUMENT ME! */
    public static String ROAD_SEGMENTS_TYPE = "RoadSegments";

    /** DOCUMENT ME! */
    public static String STREAMS_TYPE = "Streams";

    /**
     * Convenient array with all the CITE type names for dealing with copying
     * and deleting files
     */
    private static String[] CITE_TYPE_NAMES = {
            BASIC_POLYGONS_TYPE, BRIDGES_TYPE, BUILDINGS_TYPE,
            DIVIDED_ROUTES_TYPE, FORESTS_TYPE, LAKES_TYPE, MAP_NEATLINE_TYPE,
            NAMED_PLACES_TYPE, PONDS_TYPE, ROAD_SEGMENTS_TYPE, STREAMS_TYPE
        };

    /**
     * Since the PropertyDataStore does not provides CRS support, we force
     * feature types to be in WGS84
     */
    static CoordinateReferenceSystem FORCED_WGS84 = GeographicCRS.WGS84;

    /** User temp dir, where to store .property files containing cite data */
    private File tempDir;

    /** DOCUMENT ME! */
    private PropertyDataStore propsDS;

    /**
     * Creates a new CiteTestData object.
     */
    public AbstractCiteDataTest() {
        super("Cite Data based test case, using the propery datastore");
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void tearDown() throws Exception {
        deleteTempFiles();
        super.tearDown();
    }

    /**
     * Returns a <code>DataStore</code> containing CITE feature types.
     *
     * @return a property files backed DataStore which forces all the
     *         FeatureTypes it serves to be in WGS84 CRS.
     *
     * @throws IOException DOCUMENT ME!
     */
    public DataStore getCiteDataStore() throws IOException {
        if (this.propsDS == null) {
            writeTempFiles();
            this.propsDS = new ForceWGS84PropertyDataStore(this.tempDir);
            assertContainsCiteTypes(this.propsDS);
        }

        return this.propsDS;
    }

    /**
     * Throws an assertion error if some of the CITE types are not contained in
     * the passed DataStore.
     *
     * @param ds DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void assertContainsCiteTypes(DataStore ds)
        throws IOException {
        List typeNames = Arrays.asList(ds.getTypeNames());

        for (int i = 0; i < CITE_TYPE_NAMES.length; i++) {
            assertTrue(CITE_TYPE_NAMES[i] + " not found",
                typeNames.contains(CITE_TYPE_NAMES[i]));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writeTempFiles() throws IOException {
        this.tempDir = new File(System.getProperty("java.io.tmpdir"));

        if (!this.tempDir.exists() || !this.tempDir.isDirectory()) {
            throw new IOException(this.tempDir.getAbsolutePath()
                + " is not a writable directory");
        }

        for (int i = 0; i < CITE_TYPE_NAMES.length; i++) {
            writeTempFile(CITE_TYPE_NAMES[i]);
        }
    }

    /**
     * Since it's called from inside tearDown, first checks that  tempDir were
     * created and if so, deletes the temporary files.
     *
     * @throws IOException DOCUMENT ME!
     */
    private void deleteTempFiles() throws IOException {
        if (this.tempDir == null) {
            return;
        }

        for (int i = 0; i < CITE_TYPE_NAMES.length; i++) {
            deleteTempFile(CITE_TYPE_NAMES[i]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param typeName DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws NullPointerException DOCUMENT ME!
     */
    private void writeTempFile(final String typeName) throws IOException {
        final String fileName = typeName + ".properties";

        File outFile = new File(this.tempDir, fileName);

        //perhaps it was not deleted in a previous, broken run...
        deleteTempFile(typeName);

        //Atomically creates a new, empty file named by this abstract 
        //pathname if and only if a file with this name does not yet exist.
        outFile.createNewFile();

        // Request that the file or directory denoted by this abstract 
        // pathname be deleted when the virtual machine terminates.
        outFile.deleteOnExit();

        String resourceName = "/org/vfny/geoserver/testdata/" + fileName;

        InputStream in = getClass().getResourceAsStream(resourceName);

        if (in == null) {
            throw new NullPointerException(resourceName
                + " not found in classpath");
        }

        OutputStream out = new java.io.FileOutputStream(outFile);
        byte[] buff = new byte[512];
        int count;

        while ((count = in.read(buff)) > -1) {
            out.write(buff, 0, count);
        }

        in.close();
        out.flush();
        out.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param typeName DOCUMENT ME!
     */
    private void deleteTempFile(String typeName) {
        deleteTempFile(new File(this.tempDir, typeName + ".properties"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     */
    private void deleteTempFile(File f) {
        f.delete();
    }

    /**
     * DOCUMENT ME!
     *
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private static class ForceWGS84PropertyDataStore extends PropertyDataStore {
        /**
         * Creates a new ForceWGS84PropertyDataStore object.
         *
         * @param dir DOCUMENT ME!
         */
        public ForceWGS84PropertyDataStore(File dir) {
            super(dir);
        }

        /**
         * DOCUMENT ME!
         *
         * @param typeName DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws IOException DOCUMENT ME!
         * @throws DataSourceException DOCUMENT ME!
         */
        public FeatureType getSchema(String typeName) throws IOException {
            FeatureType schema = super.getSchema(typeName);

            try {
                return DataUtilities.createSubType(schema, null, FORCED_WGS84);
            } catch (SchemaException e) {
                throw new DataSourceException(e.getMessage(), e);
            }
        }

        /**
         * DOCUMENT ME!
         */

        /*
           public FeatureReader getFeatureReader(Query query,
               Transaction transaction) throws IOException {
               FeatureReader reader = super.getFeatureReader(query, transaction);
               try {
                   return new ForceCoordinateSystemFeatureReader(reader,
                       AbstractCiteDataTest.FORCED_WGS84);
               } catch (SchemaException e) {
                   throw new DataSourceException(e.getMessage(), e);
               }
           }
         */
    }
}

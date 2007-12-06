/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.testdata;

import junit.framework.TestCase;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.feature.FeatureType;
import org.geotools.feature.SchemaException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryFinder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


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
    private static final Logger LOGGER = Logger.getLogger(AbstractCiteDataTest.class.getPackage()
                                                                                    .getName());

    /** featuretype name for CITE BasicPolygons features */
    public static String BASIC_POLYGONS_TYPE = "BasicPolygons";

    /** featuretype name for CITE Bridges features */
    public static String BRIDGES_TYPE = "Bridges";

    /** featuretype name for CITE Buildings features */
    public static String BUILDINGS_TYPE = "Buildings";

    /** featuretype name for CITE Divided Routes features */
    public static String DIVIDED_ROUTES_TYPE = "DividedRoutes";

    /** featuretype name for CITE Forests features */
    public static String FORESTS_TYPE = "Forests";

    /** featuretype name for CITE Lakes features */
    public static String LAKES_TYPE = "Lakes";

    /** featuretype name for CITE Map Neatliine features */
    public static String MAP_NEATLINE_TYPE = "MapNeatline";

    /** featuretype name for CITE Named Places features */
    public static String NAMED_PLACES_TYPE = "NamedPlaces";

    /** featuretype name for CITE Ponds features */
    public static String PONDS_TYPE = "Ponds";

    /** featuretype name for CITE Road Segments features */
    public static String ROAD_SEGMENTS_TYPE = "RoadSegments";

    /** featuretype name for CITE Streams features */
    public static String STREAMS_TYPE = "Streams";

    /**
     * used to create default styles for cite types loading them from
     * test-data/styles/&lt;cite-typename&gt;.sld
     */
    private static final StyleFactory sFac = StyleFactoryFinder
        .createStyleFactory();

    /** DOCUMENT ME! */
    private static final int SHOW_TIMEOUT = 200;

    /** DOCUMENT ME! */
    private static final boolean INTERACTIVE = true;

    /**
     * Convenient array with all the CITE type names for dealing with copying
     * and deleting files
     */
    public static String[] CITE_TYPE_NAMES = {
            BASIC_POLYGONS_TYPE, BRIDGES_TYPE, BUILDINGS_TYPE,
            DIVIDED_ROUTES_TYPE, FORESTS_TYPE, LAKES_TYPE, MAP_NEATLINE_TYPE,
            NAMED_PLACES_TYPE, PONDS_TYPE, ROAD_SEGMENTS_TYPE, STREAMS_TYPE
        };

    /**
     * Since the PropertyDataStore does not provides CRS support, we force
     * feature types to be in WGS84
     */
    static CoordinateReferenceSystem FORCED_WGS84 = DefaultGeographicCRS.WGS84;

    /** User temp dir, where to store .property files containing cite data */
    private File tempDir;

    /** the DataStore instance that provides cite test data */
    private PropertyDataStore propsDS;

    /**
     * Creates a new CiteTestData object.
     */
    public AbstractCiteDataTest() {
        super("Cite Data based test case, using the propery datastore");
    }

    /**
     * does nothing but what super.setUp() does.
     *
     * @throws Exception should not do.
     */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * deletes temporary files
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
     * DOCUMENT ME!
     *
     * @param citeTypeName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected Style getDefaultStyle(String citeTypeName)
        throws Exception {
        return getStyle(citeTypeName + ".sld");
    }

    /**
     * DOCUMENT ME!
     *
     * @param styleName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected Style getStyle(String styleName) throws Exception {
        SLDParser parser = new SLDParser(sFac);
        URL styleRes = AbstractCiteDataTest.class.getResource(
                "test-data/styles/" + styleName);
        parser.setInput(styleRes);

        Style s = parser.readXML()[0];

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param frameName DOCUMENT ME!
     * @param image DOCUMENT ME!
     */
    protected void showImage(String frameName, final BufferedImage image) {
        showImage(frameName, SHOW_TIMEOUT, image);
    }

    /**
     * Shows <code>image</code> in a Frame.
     *
     * @param frameName
     * @param timeOut
     * @param image
     */
    protected void showImage(String frameName, long timeOut,
        final BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        if (((System.getProperty("java.awt.headless") == null)
                || !System.getProperty("java.awt.headless").equals("true"))
                && INTERACTIVE) {
            Frame frame = new Frame(frameName);
            frame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        e.getWindow().dispose();
                    }
                });

            Panel p = new Panel(null) { //no layout manager so it respects setSize
                    public void paint(Graphics g) {
                        g.drawImage(image, 0, 0, this);
                    }
                };

            frame.add(p);
            p.setSize(width, height);
            frame.pack();
            frame.setVisible(true);

            try {
                Thread.sleep(timeOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            frame.dispose();
        }
    }

    /**
     * Asserts that the image is not blank, in the sense that there must be
     * pixels different from the passed background color.
     *
     * @param testName the name of the test to throw meaningfull messages if
     *        something goes wrong
     * @param image the imgage to check it is not "blank"
     * @param bgColor the background color for which differing pixels are
     *        looked for
     */
    protected void assertNotBlank(String testName, BufferedImage image,
        Color bgColor) {
        int pixelsDiffer = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) != bgColor.getRGB()) {
                    ++pixelsDiffer;
                }
            }
        }

        LOGGER.info(testName + ": pixel count="
            + (image.getWidth() * image.getHeight()) + " non bg pixels: "
            + pixelsDiffer);
        assertTrue(testName + " image is comlpetely blank", 0 < pixelsDiffer);
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
        final File envTmpDir = new File(System.getProperty("java.io.tmpdir"));

        this.tempDir = new File(envTmpDir, "cite_test_datastore");

        if (this.tempDir.exists()) {
            this.tempDir.delete();
        }

        this.tempDir.mkdir();

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

        String resourceName = "test-data/featureTypes/" + fileName;

        InputStream in = AbstractCiteDataTest.class.getResourceAsStream(resourceName);

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
    static class ForceWGS84PropertyDataStore extends PropertyDataStore {
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

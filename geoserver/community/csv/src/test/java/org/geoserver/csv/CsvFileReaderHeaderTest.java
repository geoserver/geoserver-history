package org.geoserver.csv;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureType;

public class CsvFileReaderHeaderTest extends TestCase {

    public void testCorrectFile() throws Exception {
        CsvFileReader reader = new CsvFileReader(getCsvFile("/csv/full.csv"));
        FeatureType ft = reader.getFeatureType();
        assertTrue(ft.getTypeName().contains("full"));
        assertEquals(4, ft.getAttributeCount());

        // check zip code
        assertEquals("zipCode", ft.getAttributeType(0).getLocalName());
        assertEquals(String.class, ft.getAttributeType(0).getBinding());
        assertEquals("ZIP Code", reader.getDescription(0));

        // check totalPopulationSF3
        assertEquals("totalPopulationSF3", ft.getAttributeType(1)
                .getLocalName());
        assertEquals(Double.class, ft.getAttributeType(1).getBinding());
        assertEquals("Total Population SF3", reader.getDescription(1));
    }

    public void testMissingHeaders() throws Exception {
        try {
            new CsvFileReader(getCsvFile("/csv/onlyDescriptions.csv"));
            fail("Should have failed, only one header line");
        } catch (Exception e) {
            // ok
        }

        try {
            new CsvFileReader(getCsvFile("/csv/notypes.csv"));
            fail("Should have failed, only one header line");
        } catch (Exception e) {
            // ok
        }
    }

    public void testInconsistentHeaders() throws Exception {
        try {
            new CsvFileReader(getCsvFile("/csv/inconsistent.csv"));
            fail("Should have failed, incosistent headers");
        } catch (Exception e) {
            // ok
        }
    }

    public void testUnknownTypes() throws Exception {
        try {
            new CsvFileReader(getCsvFile("/csv/invalidTypes.csv"));
            fail("Should have failed, invalid types");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("blob"));
        }
    }

    public void testReadDataCount() throws Exception {
        CsvFileReader reader = new CsvFileReader(getCsvFile("/csv/full.csv"));
        FeatureIterator it = reader.getFeatures();
        assertTrue(it.hasNext());
        assertNotNull(it.next());
        assertTrue(it.hasNext());
        assertNotNull(it.next());
        assertTrue(it.hasNext());
        assertNotNull(it.next());
        assertFalse(it.hasNext());
        try {
            it.next();
            fail("Should have failed, only three features in the file");
        } catch(NoSuchElementException e) {
            // ok
        }
    }
    
    public void testReadDataFeatures() throws Exception {
        CsvFileReader reader = new CsvFileReader(getCsvFile("/csv/full.csv"));
        FeatureIterator it = reader.getFeatures();
        it.hasNext();
        Feature f = it.next();
        assertEquals(4, f.getAttributes(null).length);
        assertEquals("90001", f.getAttribute("zipCode"));
        assertEquals(new Double(54.587), f.getAttribute("totalPopulationSF3"));
        assertEquals(new Integer(8), f.getAttribute("nonHispanic"));
        assertEquals(new Double(46.402), f.getAttribute("hispanicAllRaces"));
        it.close();
    }

    private File getCsvFile(final String fileLocation)
            throws URISyntaxException {
        return new File(CsvFileReaderHeaderTest.class.getResource(fileLocation)
                .toURI());
    }
}

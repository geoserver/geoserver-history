package org.geoserver.catalog.impl;

import javax.xml.namespace.QName;

import junit.framework.Test;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.data.test.MockData;
import org.geoserver.test.GeoServerTestSupport;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.CRS;
import org.opengis.feature.type.Name;

public class CatalogBuilderTest extends GeoServerTestSupport {

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new CatalogBuilderTest());
    }
    
    @Override
    protected void populateDataDirectory(MockData dataDirectory)
            throws Exception {
        super.populateDataDirectory(dataDirectory);
        dataDirectory.addWellKnownCoverageTypes();
    }
    
    public void testFeatureTypeNoSRS() throws Exception {
        // build a feature type (it's already in the catalog, but we just want to
        // check it's built as expected
        // LINES is a feature type with a native SRS, so we want the bounds to be there
        Catalog cat = getCatalog();
        CatalogBuilder cb = new CatalogBuilder(cat);
        cb.setStore(cat.getDataStoreByName(MockData.BRIDGES.getPrefix()));
        FeatureTypeInfo fti = cb.buildFeatureType(toName(MockData.BRIDGES));
        
        // perform basic checks, this has no srs so no lat/lon bbox computation possible
        assertNull(fti.getSRS());
        assertNull(fti.getNativeCRS());
        assertNotNull(fti.getNativeBoundingBox());
        assertNull(fti.getNativeBoundingBox().getCoordinateReferenceSystem());
        assertNull(fti.getLatLonBoundingBox());
    }
    
    public void testFeatureType() throws Exception {
        // build a feature type (it's already in the catalog, but we just want to
        // check it's built as expected
        // LINES is a feature type with a native SRS, so we want the bounds to be there
        Catalog cat = getCatalog();
        CatalogBuilder cb = new CatalogBuilder(cat);
        cb.setStore(cat.getDataStoreByName(MockData.LINES.getPrefix()));
        FeatureTypeInfo fti = cb.buildFeatureType(toName(MockData.LINES));
        
        // perform basic checks
        assertEquals("EPSG:32615", fti.getSRS());
        assertEquals(CRS.decode("EPSG:32615", true), fti.getCRS());
        assertNotNull(fti.getNativeBoundingBox());
        assertNotNull(fti.getLatLonBoundingBox());
    }
    
    public void testCoverage() throws Exception {
        // build a feature type (it's already in the catalog, but we just want to
        // check it's built as expected
        // LINES is a feature type with a native SRS, so we want the bounds to be there
        Catalog cat = getCatalog();
        CatalogBuilder cb = new CatalogBuilder(cat);
        cb.setStore(cat.getCoverageStoreByName(MockData.TASMANIA_DEM.getLocalPart()));
        CoverageInfo fti = cb.buildCoverage();
        
        // perform basic checks
        assertEquals(CRS.decode("EPSG:4326", true), fti.getCRS());
        assertEquals("EPSG:4326", fti.getSRS());
        assertNotNull(fti.getNativeCRS());
        assertNotNull(fti.getNativeBoundingBox());
        assertNotNull(fti.getLatLonBoundingBox());
    }
    
    Name toName(QName qname) {
        return new NameImpl(qname.getNamespaceURI(), qname.getLocalPart());
    }
    
}

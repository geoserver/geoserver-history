package org.geoserver.csv;

import java.util.Arrays;

import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureType;

public class PostgisDDLDelegateTest extends AbstractPostgisTest {

    public void testDrop() throws Exception {
        FeatureType ft = DataUtilities.createType("testData",
                "fid:String,houseNumber:int,ratio:double");
        store.createSchema(ft);
        FeatureType created = store.getSchema(ft.getTypeName());
        assertEquals("fid", created.getAttributeType(0).getLocalName());
        assertEquals(String.class, created.getAttributeType(0).getBinding());
        assertEquals("houseNumber", created.getAttributeType(1).getLocalName());
        assertEquals(Integer.class, created.getAttributeType(1).getBinding());
        assertEquals("ratio", created.getAttributeType(2).getLocalName());
        assertEquals(Double.class, created.getAttributeType(2).getBinding());

        ddl.dropTable("testData");
        assertFalse(Arrays.asList(store.getTypeNames()).contains("testData"));
    }

    public void testCreateDropView() throws Exception {
        FeatureType ft = DataUtilities.createType("testData",
                "fid:String,address:Double,testData:int");
        store.createSchema(ft);
        ddl.createView("road", "testData", "fid", "testData", "testDataView");
        FeatureType view = store.getSchema("testDataView");
        FeatureType geom = store.getSchema("road");
        assertEquals(geom.getDefaultGeometry(), view.getDefaultGeometry());
        // both the fid and the extra att show up
        assertEquals(geom.getAttributeCount() + 2, view.getAttributeCount());
        assertEquals(1, DataUtilities.compare(geom, view));
        assertNotNull(view.getAttributeType("testData"));

        ddl.dropView("testDataView");
        assertFalse(Arrays.asList(store.getTypeNames())
                .contains("testDataView"));

        ddl.dropTable("testData");
        assertFalse(Arrays.asList(store.getTypeNames()).contains("testData"));
    }
}

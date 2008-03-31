package org.geoserver.csv;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.geotools.data.DataUtilities;
import org.geotools.data.Transaction;
import org.geotools.data.postgis.PostgisDataStore;
import org.geotools.data.postgis.PostgisDataStoreFactory;
import org.geotools.feature.FeatureType;
import org.geotools.test.OnlineTestCase;

public class PostgisDDLDelegateTest extends OnlineTestCase {

    private PostgisDataStore store;
    private PostgisDDLDelegate ddl;

    
    @Override
    protected String getFixtureId() {
        return "csv-postgis";
    }
    
    @Override
    protected void connect() throws Exception {
        store = (PostgisDataStore) new PostgisDataStoreFactory().createDataStore(fixture);
        
        String[] typeNames = store.getTypeNames();
        Connection conn = store.getConnection(Transaction.AUTO_COMMIT);
        Statement st = conn.createStatement();
        for (int i = 0; i < typeNames.length; i++) {
            try {
                st.execute("drop table \"" + typeNames[i] + "\" cascade");
            } catch(SQLException e1) {
                try {
                    st.execute("drop view \"" + typeNames[i] + "\" cascade");
                } catch(SQLException e2) {
                    
                }
            }
        }
        st.execute("delete from geometry_columns");
        
        st.execute("CREATE TABLE road (fid varchar PRIMARY KEY, id int )");
        st.execute("SELECT AddGeometryColumn('public', 'road', 'geom', 0, 'LINESTRING', 2);");
        st.execute("ALTER TABLE road add name varchar;");
        
        st.close();
        conn.close();
        
        ddl = new PostgisDDLDelegate(store);
    }
    
    @Override
    protected void disconnect() throws Exception {
        if(store != null)
            store.dispose();
    }
    
    public void testCreateDrop() throws Exception {
        FeatureType ft = DataUtilities.createType("testData", "fid:String,houseNumber:int,ratio:double");
        ddl.createSchema(ft, "fid");
        FeatureType created = store.getSchema(ft.getTypeName());
        assertEquals("houseNumber", created.getAttributeType(0).getLocalName());
        assertEquals(Integer.class, created.getAttributeType(0).getBinding());
        assertEquals("ratio", created.getAttributeType(1).getLocalName());
        assertEquals(Double.class, created.getAttributeType(1).getBinding());
        
        ddl.dropTable("testData");
        assertFalse(Arrays.asList(store.getTypeNames()).contains("testData"));
    }
    
    public void testCreateDropView() throws Exception {
        FeatureType ft = DataUtilities.createType("testData", "fid:String,address:Double,testData:int");
        ddl.createSchema(ft, "fid");
        ddl.createView("road", "testData", "fid", "testData", "testDataView");
        FeatureType view = store.getSchema("testDataView");
        FeatureType geom = store.getSchema("road");
        assertEquals(geom.getDefaultGeometry(), view.getDefaultGeometry());
        // both the fid and the extra att show up
        assertEquals(geom.getAttributeCount() + 2, view.getAttributeCount());
        assertEquals(1, DataUtilities.compare(geom, view));
        assertNotNull(view.getAttributeType("testData"));
        
        ddl.dropView("testDataView");
        assertFalse(Arrays.asList(store.getTypeNames()).contains("testDataView"));
        
        ddl.dropTable("testData");
        assertFalse(Arrays.asList(store.getTypeNames()).contains("testData"));
    }
}

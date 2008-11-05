package org.geoserver.wfsv.security;

import static org.easymock.EasyMock.*;

import org.acegisecurity.AcegiSecurityException;
import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;
import org.geoserver.security.decorators.ReadOnlyFeatureCollection;
import org.geoserver.security.decorators.SecuredObjects;
import org.geotools.data.DataUtilities;
import org.geotools.data.VersioningDataStore;
import org.geotools.data.VersioningFeatureStore;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

public class ReadOnlyVersioningFeatureStoreTest extends SecuredVersioningTest {

    private static final String SCHEMA = "testSchema";
    private VersioningDataStore dsMock;
    private SimpleFeatureType schema;
    private VersioningFeatureStore fsMock;
    private ReadOnlyVersioningFeatureStore secured;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        // nice dummy schema
        schema = DataUtilities.createType(SCHEMA, "id:int");
        
        // create a data store
        dsMock = createNiceMock(VersioningDataStore.class);
        replay(dsMock);
        
        // create a feature store
        fsMock = createNiceMock(VersioningFeatureStore.class);
        fsMock.removeFeatures(Filter.INCLUDE);
        fsMock.rollback("0", Filter.INCLUDE, null);
        expect(fsMock.getDataStore()).andReturn(dsMock);
        expect(fsMock.getSchema()).andReturn(schema).anyTimes();
        replay(fsMock);
        
        // create secured version
        secured = (ReadOnlyVersioningFeatureStore) SecuredObjects.secure(
                fsMock, WrapperPolicy.RO_CHALLENGE);
    }

    public void testRemove() throws Exception {
        try {
            secured.removeFeatures(Filter.INCLUDE);
            fail("Should have thrown a security exception");
        } catch(AcegiSecurityException e) {
            // fine
        }
    }
    
    public void testRollback() throws Exception {
        try {
            secured.rollback("'", Filter.INCLUDE, null);
            fail("Should have thrown a security exception");
        } catch(AcegiSecurityException e) {
            // fine
        }
    }
}

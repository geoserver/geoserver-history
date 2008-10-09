package org.geoserver.security.decorators;

import static org.easymock.EasyMock.*;

import org.acegisecurity.AcegiSecurityException;
import org.geoserver.security.SecureObjectsTest;
import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

public class ReadOnlyDataStoreTest extends SecureObjectsTest {

    private DataStore ds;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        FeatureStore fs = createNiceMock(FeatureStore.class);
        replay(fs);
        ds = createNiceMock(DataStore.class);
        expect(ds.getFeatureSource("blah")).andReturn(fs);
        replay(ds);
    }

    public void testDontChallenge() throws Exception {
        ReadOnlyDataStore ro = new ReadOnlyDataStore(ds, WrapperPolicy.HIDE);
        ReadOnlyFeatureSource fs = (ReadOnlyFeatureSource) ro.getFeatureSource("blah");
        assertEquals(WrapperPolicy.HIDE, fs.policy);

        try {
            ro.createSchema(null);
            fail("Should have failed with an unsupported operation exception");
        } catch (UnsupportedOperationException e) {
            //
        }
        try {
            ro.updateSchema((String) null, null);
            fail("Should have failed with an unsupported operation exception");
        } catch (UnsupportedOperationException e) {
            //
        }

        try {
            ro.updateSchema((Name) null, null);
            fail("Should have failed with an unsupported operation exception");
        } catch (UnsupportedOperationException e) {
            //
        }
        try {
            ro.getFeatureWriter("states", Transaction.AUTO_COMMIT);
            fail("Should have failed with an unsupported operation exception");
        } catch (UnsupportedOperationException e) {
            //
        }
        try {
            ro.getFeatureWriter("states", Filter.INCLUDE,
                    Transaction.AUTO_COMMIT);
            fail("Should have failed with an unsupported operation exception");
        } catch (UnsupportedOperationException e) {
            //
        }
        try {
            ro.getFeatureWriterAppend("states", Transaction.AUTO_COMMIT);
            fail("Should have failed with an unsupported operation exception");
        } catch (UnsupportedOperationException e) {
            //
        }
    }
    
    public void testChallenge() throws Exception {
        ReadOnlyDataStore ro = new ReadOnlyDataStore(ds, WrapperPolicy.RO_CHALLENGE);
        ReadOnlyFeatureStore fs = (ReadOnlyFeatureStore) ro.getFeatureSource("blah");
        assertEquals(WrapperPolicy.RO_CHALLENGE, fs.policy);

        try {
            ro.createSchema(null);
            fail("Should have failed with a security exception");
        } catch (AcegiSecurityException e) {
            //
        }
        try {
            ro.updateSchema((String) null, null);
            fail("Should have failed with a security exception");
        } catch (AcegiSecurityException e) {
            //
        }

        try {
            ro.updateSchema((Name) null, null);
            fail("Should have failed with a security exception");
        } catch (AcegiSecurityException e) {
            //
        }
        try {
            ro.getFeatureWriter("states", Transaction.AUTO_COMMIT);
            fail("Should have failed with a security exception");
        } catch (AcegiSecurityException e) {
            //
        }
        try {
            ro.getFeatureWriter("states", Filter.INCLUDE,
                    Transaction.AUTO_COMMIT);
            fail("Should have failed with a security exception");
        } catch (AcegiSecurityException e) {
            //
        }
        try {
            ro.getFeatureWriterAppend("states", Transaction.AUTO_COMMIT);
            fail("Should have failed with a security exception");
        } catch (AcegiSecurityException e) {
            //
        }
    }
}

package org.geoserver.security.decorators;

import static org.easymock.EasyMock.*;
import junit.framework.TestCase;

import org.acegisecurity.AcegiSecurityException;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

public class ReadOnlyDataStoreTest extends TestCase {

    private DataStore ds;

    @Override
    protected void setUp() throws Exception {
        FeatureStore fs = createNiceMock(FeatureStore.class);
        replay(fs);
        ds = createNiceMock(DataStore.class);
        expect(ds.getFeatureSource("blah")).andReturn(fs);
        replay(ds);
    }

    public void testDontChallenge() throws Exception {
        ReadOnlyDataStore ro = new ReadOnlyDataStore(ds, false);
        ReadOnlyFeatureSource fs = (ReadOnlyFeatureSource) ro.getFeatureSource("blah");
        assertFalse(fs.challenge);

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
        ReadOnlyDataStore ro = new ReadOnlyDataStore(ds, true);
        ReadOnlyFeatureStore fs = (ReadOnlyFeatureStore) ro.getFeatureSource("blah");
        assertTrue(fs.challenge);

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

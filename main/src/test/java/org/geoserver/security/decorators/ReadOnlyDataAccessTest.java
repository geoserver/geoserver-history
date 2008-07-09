package org.geoserver.security.decorators;

import static org.easymock.EasyMock.*;

import java.io.IOException;

import junit.framework.TestCase;

import org.acegisecurity.AcegiSecurityException;
import org.geotools.data.DataAccess;
import org.geotools.data.FeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.FeatureType;

public class ReadOnlyDataAccessTest extends TestCase {

    private DataAccess da;
    private NameImpl name;

    @Override
    protected void setUp() throws Exception {
        FeatureSource fs = createNiceMock(FeatureSource.class);
        replay(fs);
        FeatureType schema = createNiceMock(FeatureType.class);
        replay(schema);
        da = createNiceMock(DataAccess.class);
        name = new NameImpl("blah");
        expect(da.getFeatureSource(name)).andReturn(fs);
        replay(da);
    }

    public void testDontChallenge() throws Exception {
        ReadOnlyDataAccess ro = new ReadOnlyDataAccess(da, false);
        ReadOnlyFeatureSource fs = (ReadOnlyFeatureSource) ro.getFeatureSource(name);
        assertFalse(fs.challenge);

        // check the easy ones, those that are not implemented in a read only
        // collection
        try {
            ro.createSchema(null);
            fail("Should have failed with an unsupported operation exception");
        } catch (UnsupportedOperationException e) {
        }
        try {
            ro.updateSchema(null, null);
            fail("Should have failed with an unsupported operation exception");
        } catch (UnsupportedOperationException e) {
        }
    }
    
    public void testChallenge() throws Exception {
        ReadOnlyDataAccess ro = new ReadOnlyDataAccess(da, true);
        ReadOnlyFeatureSource fs = (ReadOnlyFeatureSource) ro.getFeatureSource(name);
        assertTrue(fs.challenge);

        // check the easy ones, those that are not implemented in a read only
        // collection
        try {
            ro.createSchema(null);
            fail("Should have failed with a security exception");
        } catch (AcegiSecurityException e) {
        }
        try {
            ro.updateSchema(null, null);
            fail("Should have failed with a security exception");
        } catch (AcegiSecurityException e) {
        }
    }

}

package org.geoserver.security.decorators;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.Iterator;

import org.acegisecurity.AcegiSecurityException;
import org.geoserver.security.SecureObjectsTest;
import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

public class ReadOnlyFeatureCollectionTest extends SecureObjectsTest {

    private FeatureCollection fc;

    private Feature feature;

    private SortBy sort;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        feature = createNiceMock(Feature.class);
        replay(feature);
        Iterator it = createNiceMock(Iterator.class);
        replay(it);
        sort = createNiceMock(SortBy.class);
        replay(sort);
        SimpleFeatureType schema = createNiceMock(SimpleFeatureType.class);
        expect(schema.getTypeName()).andReturn("testSchema").anyTimes();
        replay(schema);
        fc = createNiceMock(FeatureCollection.class);
        expect(fc.iterator()).andReturn(it).anyTimes();
        expect(fc.sort(sort)).andReturn(fc).anyTimes();
        expect(fc.subCollection(Filter.INCLUDE)).andReturn(fc).anyTimes();
        expect(fc.getSchema()).andReturn(schema).anyTimes();
        replay(fc);
    }

    public void testHide() throws Exception {

        ReadOnlyFeatureCollection ro = new ReadOnlyFeatureCollection(fc, WrapperPolicy.HIDE);

        // check the easy ones, those that are not implemented in a read only
        // collection
        try {
            ro.add(feature);
            fail("Should have failed with an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // ok
        }
        try {
            ro.addAll(new ArrayList());
            fail("Should have failed with an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // ok
        }
        try {
            ro.clear();
            fail("Should have failed with an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // ok
        }
        try {
            ro.remove(feature);
            fail("Should have failed with an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // ok
        }
        try {
            ro.removeAll(new ArrayList());
            fail("Should have failed with an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // ok
        }
        try {
            ro.retainAll(new ArrayList());
            fail("Should have failed with an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // ok
        }

        // let's check the iterator, should allow read but not remove
        Iterator roit = ro.iterator();
        roit.hasNext();
        roit.next();
        try {
            roit.remove();
            fail("Should have failed with an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // ok
        }

        // check derived collections are still read only and share the same
        // challenge policy
        ReadOnlyFeatureCollection sorted = (ReadOnlyFeatureCollection) ro
                .sort(sort);
        assertEquals(ro.policy, sorted.policy);
        ReadOnlyFeatureCollection sub = (ReadOnlyFeatureCollection) ro
                .subCollection(Filter.INCLUDE);
        assertEquals(ro.policy, sorted.policy);
    }

    public void testChallenge() throws Exception {

        ReadOnlyFeatureCollection ro = new ReadOnlyFeatureCollection(fc, WrapperPolicy.RO_CHALLENGE);

        // check the easy ones, those that are not implemented in a read only
        // collection
        try {
            ro.add(feature);
            fail("Should have failed with an acegi security exception");
        } catch (AcegiSecurityException e) {
            // ok
        }
        try {
            ro.addAll(new ArrayList());
            fail("Should have failed with an acegi security exception");
        } catch (AcegiSecurityException e) {
            // ok
        }
        try {
            ro.clear();
            fail("Should have failed with an acegi security exception");
        } catch (AcegiSecurityException e) {
            // ok
        }
        try {
            ro.remove(feature);
            fail("Should have failed with an acegi security exception");
        } catch (AcegiSecurityException e) {
            // ok
        }
        try {
            ro.removeAll(new ArrayList());
            fail("Should have failed with an acegi security exception");
        } catch (AcegiSecurityException e) {
            // ok
        }
        try {
            ro.retainAll(new ArrayList());
            fail("Should have failed with an acegi security exception");
        } catch (AcegiSecurityException e) {
            // ok
        }

        // let's check the iterator, should allow read but not remove
        Iterator roit = ro.iterator();
        roit.hasNext();
        roit.next();
        try {
            roit.remove();
            fail("Should have failed with an acegi security exception");
        } catch (AcegiSecurityException e) {
            // ok
        }

        // check derived collections are still read only and share the same
        // challenge policy
        ReadOnlyFeatureCollection sorted = (ReadOnlyFeatureCollection) ro
                .sort(sort);
        assertEquals(ro.policy, sorted.policy);
        ReadOnlyFeatureCollection sub = (ReadOnlyFeatureCollection) ro
                .subCollection(Filter.INCLUDE);
        assertEquals(ro.policy, sorted.policy);
    }
}

package org.geoserver.security.decorators;

import org.springframework.security.SpringSecurityException;
import org.geoserver.security.AbstractAuthorizationTest;
import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;

public class ReadOnlyDecoratorsTest extends AbstractAuthorizationTest {

    public void testSecuredLayerInfoFeatures() {
        SecuredLayerInfo ro = new SecuredLayerInfo(statesLayer,
                WrapperPolicy.HIDE);

        assertFalse(statesLayer.getResource() instanceof SecuredFeatureTypeInfo);
        assertTrue(ro.getResource() instanceof SecuredFeatureTypeInfo);
        assertSame(ro.policy,
                ((SecuredFeatureTypeInfo) ro.getResource()).policy);
    }

    public void testSecuredLayerInfoCoverages() {
        SecuredLayerInfo ro = new SecuredLayerInfo(arcGridLayer,
                WrapperPolicy.HIDE);

        assertFalse(arcGridLayer.getResource() instanceof SecuredCoverageInfo);
        assertTrue(ro.getResource() instanceof SecuredCoverageInfo);
        assertSame(ro.policy, ((SecuredCoverageInfo) ro.getResource()).policy);
    }

    public void testSecuredFeatureTypeInfoHide() throws Exception {
        SecuredFeatureTypeInfo ro = new SecuredFeatureTypeInfo(states,
                WrapperPolicy.HIDE);
        ReadOnlyFeatureSource fs = (ReadOnlyFeatureSource) ro.getFeatureSource(
                null, null);
        assertEquals(ReadOnlyFeatureSource.class, fs.getClass());
        assertEquals(WrapperPolicy.HIDE, fs.policy);
        SecuredDataStoreInfo store = (SecuredDataStoreInfo) ro.getStore();
        assertSame(WrapperPolicy.HIDE, ((SecuredDataStoreInfo) store).policy);
    }

    public void testSecuredFeatureTypeInfoMetadata() throws Exception {
        SecuredFeatureTypeInfo ro = new SecuredFeatureTypeInfo(states,
                WrapperPolicy.METADATA);
        try {
            ro.getFeatureSource(null, null);
            fail("This should have failed with a security exception");
        } catch (SpringSecurityException e) {
            // ok
        }
        SecuredDataStoreInfo store = (SecuredDataStoreInfo) ro.getStore();
        assertSame(WrapperPolicy.METADATA,
                ((SecuredDataStoreInfo) store).policy);
    }

    public void testSecuredTypeInfoReadOnly() throws Exception {
        SecuredFeatureTypeInfo ro = new SecuredFeatureTypeInfo(states,
                WrapperPolicy.RO_CHALLENGE);
        ReadOnlyFeatureStore fs = (ReadOnlyFeatureStore) ro.getFeatureSource(
                null, null);
        assertEquals(WrapperPolicy.RO_CHALLENGE, fs.policy);
        SecuredDataStoreInfo store = (SecuredDataStoreInfo) ro.getStore();
        assertSame(WrapperPolicy.RO_CHALLENGE,
                ((SecuredDataStoreInfo) store).policy);
    }

    public void testSecuredDataStoreInfoHide() throws Exception {
        SecuredDataStoreInfo ro = new SecuredDataStoreInfo(statesStore,
                WrapperPolicy.HIDE);
        ReadOnlyDataStore dataStore = (ReadOnlyDataStore) ro.getDataStore(null);
        assertEquals(WrapperPolicy.HIDE, dataStore.policy);
    }

    public void testSecuredDataStoreInfoMetadata() throws Exception {
        SecuredDataStoreInfo ro = new SecuredDataStoreInfo(statesStore,
                WrapperPolicy.METADATA);
        try {
            ReadOnlyDataStore dataStore = (ReadOnlyDataStore) ro.getDataStore(null);
            fail("This should have failed with a security exception");
        } catch (SpringSecurityException e) {
            // ok
        }
    }

}

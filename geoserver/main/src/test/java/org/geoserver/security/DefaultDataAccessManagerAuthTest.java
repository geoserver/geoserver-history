package org.geoserver.security;
import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.TestingAuthenticationToken;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;


public class DefaultDataAccessManagerAuthTest extends TestCase {
    
    private Authentication rwUser;
    private Authentication roUser;
    private TestingAuthenticationToken anonymous;
    private Authentication milUser;
    private Catalog catalog;
    private WorkspaceInfo toppWs;
    private WorkspaceInfo nurcWs;
    private LayerInfo statesLayer;
    private LayerInfo landmarksLayer;
    private LayerInfo basesLayer;
    private LayerInfo archGridLayer;
    private LayerInfo roadsLayer;

    @Override
    protected void setUp() throws Exception {
        rwUser = new TestingAuthenticationToken("lullaby", "supersecret", new GrantedAuthority[] {
                new GrantedAuthorityImpl("READER"), new GrantedAuthorityImpl("WRITER") });
        roUser = new TestingAuthenticationToken("lullaby", "supersecret", new GrantedAuthority[] {
                new GrantedAuthorityImpl("READER")});
        anonymous = new TestingAuthenticationToken("anonymous", null, null);
        milUser = new TestingAuthenticationToken("military", "supersecret", new GrantedAuthority[] {
                new GrantedAuthorityImpl("MILITARY") });
        
        catalog = createNiceMock(Catalog.class);
        expect(catalog.getWorkspace((String) anyObject())).andReturn(createNiceMock(WorkspaceInfo.class)).anyTimes(); 
        replay(catalog);
        
        toppWs = createNiceMock(WorkspaceInfo.class);
        expect(toppWs.getName()).andReturn("topp").anyTimes();
        replay(toppWs);
        
        nurcWs = createNiceMock(WorkspaceInfo.class);
        expect(nurcWs.getName()).andReturn("nurc").anyTimes();
        replay(nurcWs);
        
        statesLayer = buildLayer("states", toppWs);
        roadsLayer = buildLayer("roads", toppWs);
        landmarksLayer = buildLayer("landmarks", toppWs);
        basesLayer = buildLayer("bases", toppWs);
        archGridLayer = buildLayer("arcgrid", nurcWs);
    }
    
    private LayerInfo buildLayer(String name, WorkspaceInfo ws) {
        StoreInfo store = createNiceMock(StoreInfo.class);
        expect(store.getWorkspace()).andReturn(ws).anyTimes();
        replay(store);
        
        ResourceInfo resource = createNiceMock(ResourceInfo.class);
        expect(resource.getStore()).andReturn(store).anyTimes();
        expect(resource.getName()).andReturn(name).anyTimes();
        replay(resource);
        
        LayerInfo layer = createNiceMock(LayerInfo.class);
        expect(layer.getName()).andReturn(name).anyTimes();
        expect(layer.getResource()).andReturn(resource).anyTimes();
        replay(layer);
        
        return layer;
    }

    private DefaultDataAccessManager buildManager(String propertyFile) throws IOException {
        Properties props = new Properties();
        props.load(getClass().getResourceAsStream(propertyFile));
        return new DefaultDataAccessManager(catalog, props);
    }
    
    public void testWideOpen() throws Exception {
        DefaultDataAccessManager manager = buildManager("wideOpen.properties");
        checkUserAccessFlat(manager, anonymous, true, true);
    }

    public void testLockedDown() throws Exception {
        DefaultDataAccessManager manager = buildManager("lockedDown.properties");
        checkUserAccessFlat(manager, anonymous, false, false);
        checkUserAccessFlat(manager, roUser, false, false);
        checkUserAccessFlat(manager, rwUser, true, true);
    }
    
    public void testPublicRead() throws Exception {
        DefaultDataAccessManager manager = buildManager("publicRead.properties");
        checkUserAccessFlat(manager, anonymous, true, false);
        checkUserAccessFlat(manager, roUser, true, false);
        checkUserAccessFlat(manager, rwUser, true, true);
    }
    
    private void checkUserAccessFlat(DefaultDataAccessManager manager, Authentication user, boolean expectedRead, boolean expectedWrite) {
        // states as a layer
        assertEquals(expectedRead, manager.canAccess(user, statesLayer, AccessMode.READ));
        assertEquals(expectedWrite, manager.canAccess(user, statesLayer, AccessMode.WRITE));
        // states as a resource
        final ResourceInfo resource = statesLayer.getResource();
        assertEquals(expectedRead, manager.canAccess(user, resource, AccessMode.READ));
        assertEquals(expectedWrite, manager.canAccess(user, resource, AccessMode.WRITE));
        // the topp ws
        assertEquals(expectedRead, manager.canAccess(user, toppWs, AccessMode.READ));
        assertEquals(expectedWrite, manager.canAccess(user, toppWs, AccessMode.WRITE));
    }
    
    public void testComplex() throws Exception {
        DefaultDataAccessManager wo = buildManager("complex.properties");
        
        // check non configured ws inherits root configuration, auth read, nobody write
        assertFalse(wo.canAccess(anonymous, nurcWs, AccessMode.READ));
        assertFalse(wo.canAccess(anonymous, nurcWs, AccessMode.WRITE));
        assertTrue(wo.canAccess(roUser, nurcWs, AccessMode.READ));
        assertFalse(wo.canAccess(rwUser, nurcWs, AccessMode.WRITE));
        
        // check the same goes for a layer in that namespace
        assertFalse(wo.canAccess(anonymous, archGridLayer, AccessMode.READ));
        assertFalse(wo.canAccess(anonymous, archGridLayer, AccessMode.WRITE));
        assertTrue(wo.canAccess(roUser, archGridLayer, AccessMode.READ));
        assertFalse(wo.canAccess(rwUser, archGridLayer, AccessMode.WRITE));
        
        // check access to the topp workspace (everybody read, nobody for write)
        assertTrue(wo.canAccess(anonymous, toppWs, AccessMode.READ));
        assertFalse(wo.canAccess(anonymous, toppWs, AccessMode.WRITE));
        assertTrue(wo.canAccess(roUser, toppWs, AccessMode.READ));
        assertFalse(wo.canAccess(rwUser, toppWs, AccessMode.WRITE));
        
        // check non configured layer in topp ws inherits topp security attributes
        assertTrue(wo.canAccess(anonymous, roadsLayer, AccessMode.READ));
        assertFalse(wo.canAccess(anonymous, roadsLayer, AccessMode.WRITE));
        assertTrue(wo.canAccess(roUser, roadsLayer, AccessMode.READ));
        assertFalse(wo.canAccess(rwUser, roadsLayer, AccessMode.WRITE));
        
        // check states uses its own config (auth for read, auth for write)
        assertFalse(wo.canAccess(anonymous, statesLayer, AccessMode.READ));
        assertFalse(wo.canAccess(anonymous, statesLayer, AccessMode.WRITE));
        assertTrue(wo.canAccess(roUser, statesLayer, AccessMode.READ));
        assertFalse(wo.canAccess(roUser, statesLayer, AccessMode.WRITE));
        assertTrue(wo.canAccess(rwUser, statesLayer, AccessMode.WRITE));
        assertTrue(wo.canAccess(rwUser, statesLayer, AccessMode.WRITE));
        
        // check landmarks uses its own config (all can for read, auth for write)
        assertTrue(wo.canAccess(anonymous, landmarksLayer, AccessMode.READ));
        assertFalse(wo.canAccess(anonymous, landmarksLayer, AccessMode.WRITE));
        assertTrue(wo.canAccess(roUser, landmarksLayer, AccessMode.READ));
        assertFalse(wo.canAccess(roUser, landmarksLayer, AccessMode.WRITE));
        assertTrue(wo.canAccess(rwUser, landmarksLayer, AccessMode.READ));
        assertTrue(wo.canAccess(rwUser, statesLayer, AccessMode.WRITE));
        
        // check military is off limits for anyone but the military users
        assertFalse(wo.canAccess(anonymous, basesLayer, AccessMode.READ));
        assertFalse(wo.canAccess(anonymous, basesLayer, AccessMode.WRITE));
        assertFalse(wo.canAccess(roUser, basesLayer, AccessMode.READ));
        assertFalse(wo.canAccess(roUser, basesLayer, AccessMode.WRITE));
        assertFalse(wo.canAccess(rwUser, basesLayer, AccessMode.READ));
        assertFalse(wo.canAccess(rwUser, basesLayer, AccessMode.WRITE));
        assertTrue(wo.canAccess(milUser, basesLayer, AccessMode.READ));
        assertTrue(wo.canAccess(milUser, basesLayer, AccessMode.WRITE));
    }
}

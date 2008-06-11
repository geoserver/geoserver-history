package org.geoserver.security;
import org.acegisecurity.Authentication;
import org.geoserver.catalog.ResourceInfo;


public class DefaultDataAccessManagerAuthTest extends AbstractAuthorizationTest {

    public void testWideOpen() throws Exception {
        DefaultDataAccessManager manager = buildManager("wideOpen.properties");
        checkUserAccessFlat(manager, anonymous, true, true);
    }

    public void testLockedDown() throws Exception {
        DefaultDataAccessManager manager = buildManager("lockedDown.properties");
        checkUserAccessFlat(manager, anonymous, false, false);
        checkUserAccessFlat(manager, roUser, false, false);
        checkUserAccessFlat(manager, rwUser, true, true);
        checkUserAccessFlat(manager, root, true, true);
    }
    
    public void testPublicRead() throws Exception {
        DefaultDataAccessManager manager = buildManager("publicRead.properties");
        checkUserAccessFlat(manager, anonymous, true, false);
        checkUserAccessFlat(manager, roUser, true, false);
        checkUserAccessFlat(manager, rwUser, true, true);
        checkUserAccessFlat(manager, root, true, true);
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
        assertTrue(wo.canAccess(root, nurcWs, AccessMode.WRITE));
        
        // check the same goes for a layer in that namespace
        assertFalse(wo.canAccess(anonymous, arcGridLayer, AccessMode.READ));
        assertFalse(wo.canAccess(anonymous, arcGridLayer, AccessMode.WRITE));
        assertTrue(wo.canAccess(roUser, arcGridLayer, AccessMode.READ));
        assertFalse(wo.canAccess(rwUser, arcGridLayer, AccessMode.WRITE));
        assertTrue(wo.canAccess(root, arcGridLayer, AccessMode.WRITE));
        
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

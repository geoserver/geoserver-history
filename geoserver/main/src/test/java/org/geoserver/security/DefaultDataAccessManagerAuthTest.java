package org.geoserver.security;
import java.io.IOException;
import java.util.Properties;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.TestingAuthenticationToken;
import static org.easymock.EasyMock.*;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;

import junit.framework.TestCase;


public class DefaultDataAccessManagerAuthTest extends TestCase {
    
    private TestingAuthenticationToken rwUser;
    private TestingAuthenticationToken roUser;
    private TestingAuthenticationToken anonymous;
    private Catalog catalog;
    private WorkspaceInfo toppWs;
    private LayerInfo statesLayer;

    @Override
    protected void setUp() throws Exception {
        rwUser = new TestingAuthenticationToken("lullaby", "supersecret", new GrantedAuthority[] {
                new GrantedAuthorityImpl("READER"), new GrantedAuthorityImpl("WRITER") });
        roUser = new TestingAuthenticationToken("lullaby", "supersecret", new GrantedAuthority[] {
                new GrantedAuthorityImpl("READER")});
        anonymous = new TestingAuthenticationToken("anonymous", null, null);
        
        catalog = createNiceMock(Catalog.class);
        expect(catalog.getWorkspace((String) anyObject())).andReturn(createNiceMock(WorkspaceInfo.class)).anyTimes(); 
        replay(catalog);
        
        toppWs = createNiceMock(WorkspaceInfo.class);
        replay(toppWs);
        
        statesLayer = buildLayer("states", toppWs);
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
        DefaultDataAccessManager wo = buildManager("wideOpen.properties");
        checkUserAccessFlat(wo, anonymous, true);
    }

    private void checkUserAccessFlat(DefaultDataAccessManager wo, Authentication user, boolean expected) {
        assertEquals(expected, wo.canAccess(user, statesLayer, AccessMode.READ));
        assertEquals(expected, wo.canAccess(user, statesLayer, AccessMode.WRITE));
        final ResourceInfo resource = statesLayer.getResource();
        assertEquals(expected, wo.canAccess(user, resource, AccessMode.READ));
        assertEquals(expected, wo.canAccess(user, resource, AccessMode.WRITE));
        assertEquals(expected, wo.canAccess(user, toppWs, AccessMode.READ));
    }
    
    public void testLockedDown() throws Exception {
        DefaultDataAccessManager wo = buildManager("lockedDown.properties");
        checkUserAccessFlat(wo, anonymous, false);
        checkUserAccessFlat(wo, roUser, false);
        checkUserAccessFlat(wo, rwUser, true);
    }
}

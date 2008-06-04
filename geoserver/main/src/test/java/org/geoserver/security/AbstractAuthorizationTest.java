package org.geoserver.security;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.util.Properties;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.TestingAuthenticationToken;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;

import junit.framework.TestCase;

public abstract class AbstractAuthorizationTest extends TestCase {

    protected Authentication rwUser;
    protected Authentication roUser;
    protected Authentication anonymous;
    protected Authentication milUser;
    protected Catalog catalog;
    protected WorkspaceInfo toppWs;
    protected WorkspaceInfo nurcWs;
    protected LayerInfo statesLayer;
    protected LayerInfo landmarksLayer;
    protected LayerInfo basesLayer;
    protected LayerInfo arcGridLayer;
    protected LayerInfo roadsLayer;

    @Override
    protected void setUp() throws Exception {
        rwUser = new TestingAuthenticationToken("rw", "supersecret", new GrantedAuthority[] {
                new GrantedAuthorityImpl("READER"), new GrantedAuthorityImpl("WRITER") });
        roUser = new TestingAuthenticationToken("ro", "supersecret", new GrantedAuthority[] {
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
        
        statesLayer = buildLayer("states", toppWs, FeatureTypeInfo.class);
        roadsLayer = buildLayer("roads", toppWs, FeatureTypeInfo.class);
        landmarksLayer = buildLayer("landmarks", toppWs, FeatureTypeInfo.class);
        basesLayer = buildLayer("bases", toppWs, FeatureTypeInfo.class);
        arcGridLayer = buildLayer("arcgrid", nurcWs, CoverageInfo.class);
    }
    
    protected LayerInfo buildLayer(String name, WorkspaceInfo ws, Class<? extends ResourceInfo> resourceClass) {
        StoreInfo store;
        if(resourceClass.equals(CoverageInfo.class))
          store = createNiceMock(CoverageStoreInfo.class);
        else
          store = createNiceMock(DataStoreInfo.class);
        expect(store.getWorkspace()).andReturn(ws).anyTimes();
        replay(store);
        
        ResourceInfo resource = createNiceMock(resourceClass);
        expect(resource.getStore()).andReturn(store).anyTimes();
        expect(resource.getName()).andReturn(name).anyTimes();
        replay(resource);
        
        LayerInfo layer = createNiceMock(LayerInfo.class);
        expect(layer.getName()).andReturn(name).anyTimes();
        expect(layer.getResource()).andReturn(resource).anyTimes();
        replay(layer);
        
        return layer;
    }
    
    protected DefaultDataAccessManager buildManager(String propertyFile) throws IOException {
        Properties props = new Properties();
        props.load(getClass().getResourceAsStream(propertyFile));
        return new DefaultDataAccessManager(catalog, props);
    }
}

package org.geoserver.ows;

import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.security.AbstractAuthorizationTest;
import org.geoserver.security.DataAccessManager;
import org.geoserver.security.SecureCatalogImpl;

public class LocalWorkspaceSecureCatalogTest extends AbstractAuthorizationTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        populateCatalog();
    }
    
    public void testAccessToLayer() throws Exception {
        DataAccessManager def = buildManager("wideOpen.properties");
        LocalWorkspaceDataAccessManager mgr = new LocalWorkspaceDataAccessManager();
        mgr.setDelegate(def);
        
        SecureCatalogImpl sc = new SecureCatalogImpl(catalog, mgr) {};
        assertNotNull(sc.getLayerByName("topp:states"));
        
        WorkspaceInfo ws = sc.getWorkspaceByName("nurc");
        LocalWorkspace.set(ws);
        assertNull(sc.getLayerByName("topp:states"));
    }
    
    @Override
    protected void tearDown() throws Exception {
        LocalWorkspace.remove();
    }
}

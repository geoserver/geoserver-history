package org.geoserver.ows;

import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.security.DataAccessManager;
import org.geoserver.security.DataAccessManagerAdapter;
import org.geoserver.security.SecureCatalogImpl;
import org.geoserver.security.impl.AbstractAuthorizationTest;

public class LocalWorkspaceSecureCatalogTest extends AbstractAuthorizationTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        populateCatalog();
    }
    
    public void testAccessToLayer() throws Exception {
        DataAccessManager def = buildLegacyAccessManager("wideOpen.properties");
        LocalWorkspaceDataAccessManager mgr = new LocalWorkspaceDataAccessManager();
        mgr.setDelegate(def);
        
        SecureCatalogImpl sc = new SecureCatalogImpl(catalog, new DataAccessManagerAdapter(mgr)) {};
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

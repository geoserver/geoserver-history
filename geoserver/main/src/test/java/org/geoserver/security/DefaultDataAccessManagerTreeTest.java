package org.geoserver.security;
import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.TestingAuthenticationToken;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.WorkspaceInfo;

/**
 * Tests parsing of the property file into a security tree, and the functionality of the
 * tree as well (building the tree by hand is tedious)
 * @author Andrea Aime - TOPP
 *
 */
public class DefaultDataAccessManagerTreeTest extends TestCase {
    
    private Catalog catalog;
    private TestingAuthenticationToken rwUser;
    private TestingAuthenticationToken roUser;
    private TestingAuthenticationToken anonymous;

    @Override
    protected void setUp() throws Exception {
        catalog = createNiceMock(Catalog.class);
        expect(catalog.getWorkspace((String) anyObject())).andReturn(createNiceMock(WorkspaceInfo.class)).anyTimes(); 
        replay(catalog);
        
        rwUser = new TestingAuthenticationToken("lullaby", "supersecret", new GrantedAuthority[] {
                new GrantedAuthorityImpl("READER"), new GrantedAuthorityImpl("WRITER") });
        roUser = new TestingAuthenticationToken("lullaby", "supersecret", new GrantedAuthority[] {
                new GrantedAuthorityImpl("READER")});
        anonymous = new TestingAuthenticationToken("anonymous", null, null);
    }
    
    private SecureTreeNode buildTree(String propertyFile) throws IOException {
        Properties props = new Properties();
        props.load(getClass().getResourceAsStream(propertyFile));
        return new DefaultDataAccessManager(catalog, props).root;
    }
    
    public void testWideOpen() throws Exception {
        SecureTreeNode root = buildTree("wideOpen.properties");
        assertEquals(0, root.children.size());
        assertEquals(0, root.getAuthorizedRoles(AccessMode.READ).size());
        assertEquals(0, root.getAuthorizedRoles(AccessMode.WRITE).size());
        assertTrue(root.canAccess(anonymous, AccessMode.READ));
        assertTrue(root.canAccess(anonymous, AccessMode.WRITE));
    }

    public void testLockedDown() throws Exception {
        SecureTreeNode root = buildTree("lockedDown.properties");
        assertEquals(0, root.children.size());
        final Set<String> readRoles = root.getAuthorizedRoles(AccessMode.READ);
        assertEquals(1, readRoles.size());
        assertTrue(readRoles.contains("WRITER"));
        final Set<String> writeRoles = root.getAuthorizedRoles(AccessMode.WRITE);
        assertEquals(1, writeRoles.size());
        assertTrue(writeRoles.contains("WRITER"));
        assertFalse(root.canAccess(anonymous, AccessMode.READ));
        assertFalse(root.canAccess(anonymous, AccessMode.WRITE));
        assertFalse(root.canAccess(roUser, AccessMode.READ));
        assertFalse(root.canAccess(roUser, AccessMode.WRITE));
        assertTrue(root.canAccess(rwUser, AccessMode.READ));
        assertTrue(root.canAccess(rwUser, AccessMode.WRITE));
    }
    
    public void testPublicRead() throws Exception {
        SecureTreeNode root = buildTree("publicRead.properties");
        assertEquals(0, root.children.size());
        assertEquals(0, root.getAuthorizedRoles(AccessMode.READ).size());
        final Set<String> writeRoles = root.getAuthorizedRoles(AccessMode.WRITE);
        assertEquals(1, writeRoles.size());
        assertTrue(writeRoles.contains("WRITER"));
        assertTrue(root.canAccess(anonymous, AccessMode.READ));
        assertFalse(root.canAccess(anonymous, AccessMode.WRITE));
        assertTrue(root.canAccess(roUser, AccessMode.READ));
        assertFalse(root.canAccess(roUser, AccessMode.WRITE));
        assertTrue(root.canAccess(rwUser, AccessMode.READ));
        assertTrue(root.canAccess(rwUser, AccessMode.WRITE));
    }
}

package org.geoserver.security;

import java.util.Set;

import junit.framework.TestCase;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.TestingAuthenticationToken;

public class SecureTreeNodeTest extends TestCase {

    private TestingAuthenticationToken rwUser;
    private TestingAuthenticationToken roUser;
    private TestingAuthenticationToken anonymous;

    @Override
    protected void setUp() throws Exception {
        rwUser = new TestingAuthenticationToken("lullaby", "supersecret", new GrantedAuthority[] {
                new GrantedAuthorityImpl("reader"), new GrantedAuthorityImpl("writer") });
        roUser = new TestingAuthenticationToken("lullaby", "supersecret", new GrantedAuthority[] {
                new GrantedAuthorityImpl("reader")});
        anonymous = new TestingAuthenticationToken("anonymous", null, null);
    }

    public void testEmptyRoot() {
        SecureTreeNode root = new SecureTreeNode();

        // smoke tests
        assertNull(root.getChild("NotThere"));
        assertEquals(SecureTreeNode.EVERYBODY, root.getAuthorizedRoles(AccessMode.READ));
        assertEquals(SecureTreeNode.EVERYBODY, root.getAuthorizedRoles(AccessMode.WRITE));

        // empty, deepest node is itself
        SecureTreeNode node = root.getDeepestNode(new String[] { "a", "b" });
        assertSame(root, node);

        // allows access to everyone
        assertTrue(root.canAccess(anonymous, AccessMode.WRITE));
        assertTrue(root.canAccess(anonymous, AccessMode.READ));
    }
    
    
}

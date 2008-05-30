package org.geoserver.security;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;

/**
 * Represents a hierarchical security tree node. The tree as a whole is
 * represented by its root
 * 
 * @author Andrea Aime - TOPP
 * 
 */
class SecureTreeNode {
    Map<String, SecureTreeNode> children = new HashMap<String, SecureTreeNode>();

    SecureTreeNode parent;

    Map<AccessMode, Set<String>> authorizedRoles = new HashMap<AccessMode, Set<String>>();

    /**
     * Builds a child of the specified parent node
     * 
     * @param parent
     */
    private SecureTreeNode(SecureTreeNode parent) {
        this.parent = parent;
    }

    /**
     * Builds the root of a security tree
     */
    SecureTreeNode() {
        // nothing to do
    }

    /**
     * Returns a child with the specified name, or null
     * 
     * @param name
     * @return
     */
    SecureTreeNode getChild(String name) {
        return children.get(name);
    }

    /**
     * Adds a child to this path element
     * 
     * @param name
     * @return
     */
    SecureTreeNode addChild(String name) {
        if (getChild(name) != null)
            throw new IllegalArgumentException("This pathElement " + name
                    + " is already among my children");

        SecureTreeNode child = new SecureTreeNode(this);
        children.put(name, child);
        return child;
    }

    /**
     * Tells if the user is allowed to access the PathElement with the specified
     * access mode. If no information can be found in the current node, the
     * decision is delegated to the parent. If the root is reached and it has no
     * security definition, access will be granted. Otherwise, the first path
     * element with a role list for the specified access mode will return true
     * if the user has a {@link GrantedAuthority} matching one of the specified
     * roles, false otherwise
     * 
     * @param user
     * @param mode
     * @return
     */
    boolean canAccess(Authentication user, AccessMode mode) {
        Set<String> roles = authorizedRoles.get(mode);

        // if we don't know, we ask the parent, otherwise we assume
        // the object is unsecured
        if (roles == null || roles.isEmpty()) {
            if (parent == null)
                return true;
            else
                return parent.canAccess(user, mode);
        }

        // let's scan thru the the authorities granted to the user and
        // see if he matches any of the write roles
        if(user.getAuthorities() == null)
            return false;
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (roles.contains(authority.getAuthority()))
                return true;
        }
        return false;
    }

    /**
     * Returns the authorized roles for the specified access mode. If the collection is empty,
     * we assume everybody can access this node (a non accessible node is of no interest, and
     * there is no way to specify that explicitly in the property file either, the way is
     * to make it accessible only to a role that no user is assigned to)
     */
    Set<String> getAuthorizedRoles(AccessMode mode) {
        Set<String> roles = authorizedRoles.get(mode);
        if(roles == null)
            return Collections.emptySet();
        else
            return roles;
    }

    /**
     * Sets the authorized roles for the specified access mode
     */
    void setAuthorizedRoles(AccessMode mode, Set<String> roles) {
        authorizedRoles.put(mode, roles);
    }

    /**
     * Utility method that drills down from the current node using the specified
     * list of child names, and returns the latest element found along that path
     * (might not be correspondent to the full path specified, security paths
     * can be incomplete, the definition of the parent applies to the missing
     * children as well)
     * 
     * @param pathElements
     * @return
     */
    SecureTreeNode getDeepestNode(String[] pathElements) {
        SecureTreeNode curr = this;
        for (int i = 0; i < pathElements.length; i++) {
            final SecureTreeNode next = curr.getChild(pathElements[i]);
            if (next == null)
                return curr;
            else
                curr = next;
        }
        return curr;
    }
}
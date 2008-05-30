package org.geoserver.security;

/**
 * A list of ways a resource can be accessed according to the data security layer 
 * @author Andrea Aime - TOPP
 *
 */
public enum AccessMode {
    /**
     * Pure read access, see but not touch
     */
    READ("r"), 
    /**
     * Write access, that is, see and modify
     */
    WRITE("w");
    
    String alias;
    
    /**
     * Builds an access mode
     * @param alias a shortcut for the access mode
     */
    AccessMode(String alias) {
        this.alias = alias;
    }
    
    /**
     * Locates the access mode by its alias
     * @param alias
     * @return the access mode, or null if not found
     */
    public static AccessMode getByAlias(String alias) {
        for (AccessMode mode : AccessMode.values()) {
            if(mode.alias.equals(alias))
                return mode;
        }
        return null;
    }
}

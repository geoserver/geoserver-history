package org.vfny.geoserver.issues.enums;

/**
 * Indicates whether a situation is resolved(fixed), unresolved or unknown.
 * 
 * @author jones
 * @since 1.0.0
 */
public enum Resolution {
    /**
     * The situation has yet to be resolved
     */
    UNRESOLVED,
    /**
     * The user has looked at the situation
     */
    IN_PROGRESS,
    /**
     * The situation has been resolved
     */
    RESOLVED,
    /**
     * User input is required to determine whether the situation has been resolved
     */
    UNKNOWN
}

package org.vfny.geoserver.issues.listeners;

/**
 * Enumerates the different types of issue changes that issuesListListener can be notified of.
 * @author Jesse
 * @since 1.1.0
 */
public enum IssueChangeType {

    /**
     * Indications the description of the issue has changed.
     */
    DESCRIPTION,
    /**
     * Indications the priority of the issue has changed.
     */
    PRIORITY,
    /**
     * Indications the resolution of the issue has changed.
     */
    RESOLUTION,
    /**
     * Indications the bounds of the issue has changed
     */
    BOUNDS,
    /**
     * Indicates that something else has changed, may be issue specific.
     */
    OTHER
}

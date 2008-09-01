package org.vfny.geoserver.issues;

import java.util.Collection;

/**
 * The issue service interface for GeoServer.
 * This service allows you to add, modify and retrieve issues.
 * @author quintona
 * @since 1.1.0
 */
public interface IIssueService {
    /**
     * Adds issues to the back end
     * All the issues within this list must relate to the same target type
     * @param issues issues to added.
     */
    void addIssues(Collection<IIssue> issues);
    /**
     * Saves the issue to the storage
     * @param issue issue to save
     */
    void modifyIssue(IIssue issue);
    /**
     * Removes the issues from storage
     * All of these issues must be related to the same target type
     * @param issues issues to remove.
     */
    void removeIssues(Collection<IIssue> issues );
    /**
     * Returns a list of issues, the issues should be ordered in the correct order.
     * @return a list of issues
     * @throws IOException
     */
    Collection<IIssue> getIssues();
    /**
     * Returns a list of issues, the issues should be ordered in the correct order.
     * @param target The target object
     * @return a list of issues
     * @throws IOException
     */
    Collection<IIssue> getIssues(Target target);
}


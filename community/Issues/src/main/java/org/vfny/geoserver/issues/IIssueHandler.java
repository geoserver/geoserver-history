package org.vfny.geoserver.issues;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * The interface for any bean that wishes to extend the issue service by
 * providing the ability to handle a particular target type that an issue
 * relates to.
 * @author quintona
 * @since 1.1.0
 */
public interface IIssueHandler {
    /**
     * Called by the issue service to determine if this handler can hanle 
     * a particular target type
     * @param targetType
     * @return true if this handler can handle the target type
     */
    public boolean canHandle(String issueType);
    /**
     * Adds issues to the back end
     * @param issues issues to added.
     */
    void addIssues(List<IIssue> issues) throws IOException;
    /**
     * Saves the issue to the storage
     * @param issue issue to save
     */
    void modifyIssue(IIssue issue) throws IOException;
    /**
     * Removes the issues from storage
     * All of these issues must be related to the same target type
     * @param issues issues to remove.
     */
    void removeIssues(Collection<IIssue> issues ) throws IOException;
    /**
     * Returns a list of issues, the issues should be ordered in the correct order.
     * @param groupId The group of issues to return
     * @return a list of issues
     * @throws IOException
     */
    Collection<IIssue> getIssues(String groupId) throws IOException;
    /**
     * Returns a list of issues, the issues should be ordered in the correct order.
     * @return a list of issues
     * @throws IOException
     */
    Collection<IIssue> getIssues() throws IOException;
}

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
    public boolean canHandle(Object target);
    /**
     * Adds issues to the back end
     * @param issues issues to added.
     */
    void addIssues(Object target, List<IIssue> issues) throws IOException;
    /**
     * Saves the issue to the storage
     * @param issue issue to save
     */
    void modifyIssue(Object target,IIssue issue) throws IOException;
    /**
     * Removes the issues from storage
     * All of these issues must be related to the same target type
     * @param issues issues to remove.
     */
    void removeIssues(Object target, Collection<IIssue> issues ) throws IOException;
    /**
     * Returns a list of issues, the issues should be ordered in the correct order.
     * @param target The target that the issues must relate to
     * @return a list of issues
     * @throws IOException
     */
    Collection<IIssue> getIssues(Object target) throws IOException;
}

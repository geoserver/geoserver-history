package org.vfny.geoserver.issues;

import java.io.IOException;
import java.util.Collection;
import java.util.List;



/**
 * Provides the methods the {@link StrategizedIssuesList} requires for accessing the remote store.
 * 
 * @author Jesse
 * @since 1.1.0
 */
public interface IListStrategy {

    /**
     * Adds issues to the back end
     *
     * @param issues issues to add.
     */
    void addIssues(List<? extends IIssue> issues) throws IOException;
    /**
     * Saves the issue to the storage
     * 
     * @param issue issue to save
     */
    void modifyIssue(IIssue issue) throws IOException;
    /**
     * Removes the issues from storage
     *
     * @param issues issues to remove.
     */
    void removeIssues( Collection< ? extends IIssue> issues ) throws IOException;
    /**
     * Returns a list of issues, the issues should be ordered in the correct order. 
     *
     * @return a list of issues
     */
    Collection< ? extends IIssue> getIssues() throws IOException;
}


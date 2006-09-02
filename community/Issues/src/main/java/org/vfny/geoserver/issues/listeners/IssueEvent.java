package org.vfny.geoserver.issues.listeners;

import org.vfny.geoserver.issues.IIssue;

/**
 * Event data representing a Issue change event
 * @author Jesse
 * @since 1.1.0
 */
public class IssueEvent extends AbstractIssueEvent {

    private final IssueChangeType change;
    /**
     * @param change the type of change that has taken place
     */
    public IssueEvent(IIssue source, IssueChangeType change, Object newValue, Object oldValue){
        super( source, newValue, oldValue);
        this.change=change;
    }

    /**
     * Returns the type of change that has taken place
     *
     * @return the type of change that has taken place
     */
    public IssueChangeType getChange() {
        return change;
    }
}

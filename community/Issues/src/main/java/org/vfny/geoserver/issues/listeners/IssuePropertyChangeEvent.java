package org.vfny.geoserver.issues.listeners;

import org.vfny.geoserver.issues.IIssue;

/**
 * Represents the event where one of an issue's properties ({@link IIssue#getProperty(String)} has changed
 * @author Jesse
 * @since 1.1.0
 */
public class IssuePropertyChangeEvent extends AbstractIssueEvent {

    private final String propertyKey;

    /**
     * New Instance
     * 
     * @param propertyKey the property that has changed
     */
    public IssuePropertyChangeEvent( IIssue source, String propertyKey, Object newValue, Object oldValue ) {
        super(source, newValue, oldValue);
        this.propertyKey=propertyKey;
    }

    /**
     * Returns the name of the property that has changed
     *
     * @return the name of the property that has changed
     */
    public String getPropertyKey() {
        return propertyKey;
    }

}

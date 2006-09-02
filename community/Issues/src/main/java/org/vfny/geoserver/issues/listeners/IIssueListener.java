package org.vfny.geoserver.issues.listeners;

/**
 * Indicates that something in the issue has changed.
 * 
 * @author Jesse
 * @since 1.1.0
 */
public interface IIssueListener {
    /**
     * Called when some of the data of an issue has changed. 
     * Changes to {@link IIssue#getProperty(String)} will cause {@link #notifyPropertyChanged(IIssue, String, Object, Object)} to
     * be called.
     *
     *@param event the event data
     */
    void notifyChanged( IssueEvent event);
    
    /**
     * Called when a property of an issue ({@link IIssue#getProperty(String)}) is changed.
     *
     * @param event event data.
     */
    void notifyPropertyChanged( IssuePropertyChangeEvent event);
}

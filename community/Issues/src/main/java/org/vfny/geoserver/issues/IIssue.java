package org.vfny.geoserver.issues;

import java.util.List;

import org.vfny.geoserver.issues.enums.Priority;
import org.vfny.geoserver.issues.enums.Resolution;
import org.vfny.geoserver.issues.listeners.IIssueListener;

import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * The issue interface. Represents a single issue. Issues can be grouped
 * by group id. They have the following basic properties:<BR>
 * -Description<BR>
 * -Priority<BR>
 * -Resolution<BR>
 * -Bounds<BR>
 * -groupId<BR>
 * -memento<BR>
 * -viewMemento<BR>
 * -issueType<BR>
 * 
 * @author quintona
 * @since 1.0.0
 */
public interface IIssue {
    /**
     * Adds a {@link IIssueListener} to the issue.  The {@link IIssuesManager} listens to the issue in order to know which issues need
     * to be saved.  If an implementation does not raise events when a property of the issue is changed and it does not call 
     * {@link IIssuesManager#save()} then the change will not be saved.
     *
     * @param listener listener to add.
     */
    public void addIssueListener(IIssueListener listener);
    /**
     * Removes a {@link IIssueListener} from the issue.
     *
     * @param listener listener to remove.
     */
    public void removeIssueListener(IIssueListener listener);
    /**
     * Gets the description of the Issue. Should be a single line and around one line. Should be
     * translateable.
     * 
     * @return the description of the Issue.
     */
    String getDescription();
    /**
     * Sets the description of the issue.
     * 
     * @param description
     */
    void setDescription( String description );
    /**
     * Returns the priority of the issue.
     * 
     * @return the priority of the issue.
     */
    Priority getPriority();
    /**
     * Sets the priority of the issue.
     */
    void setPriority( Priority priority );
    /**
     * Indicates whether the issue has been resolved. This method <b>MUST</b> return quickly. If
     * the resolution state cannot be determined quickly then either {@linkplain Resolution#UNKNOWN}
     * or {@linkplain Resolution#UNRESOLVED} should be returned.
     * 
     * @return true if the issue no longer exists.
     */
    Resolution getResolution();
    /**
     * Sets the state of resolution.
     * 
     * @param newResolution the new state.
     */
    void setResolution( Resolution newResolution );
    /**
     * Returns the id of the issue.  
     *
     * @return id of the issue.
     */
    String getId();
    /**
     * Sets the id of the issue.  This should only be called by the Issues list never by client code.
     *
     * @param newId
     */
    void setId(String newId);
    /**
     * The GroupID groups sets of Issues.  
     * A potential use is to group Issues by analysis operation so that groups of Issues can be
     * deleted/resolved/replaced as a group.
     */
    String getGroupId();
    /**
     * Gets a property of the issue.
     * This allows the interface to be more flexible.
     * @param property the name/key of the property
     */
    Object getProperty( String property );
    /**
     * Returns an array of all the property names in the issue.  For correctness
     * all the names should be able to passed to getProperty() without throwing an exception.
     * 
     * @return an array of all the property names in the issue
     */
    String[] getPropertyNames();
    /**
     * Allows properties to be added to the issue. allows the interface to be flexible
     * @param key
     * @param value
     */
    void addProperty(String key, Object value);
    /**
     * Called by framework to initialize the Issue.
     * @param memento The saved state of a Issue. May be null.
     * @param viewMemento the memento for initializing the view, may be null
     * @param issueId the id the issue is assigned.  {@link #getId()} must return the same id.
     * @param groupId the group id of the issue.
     * @param bounds the bounds of the issue.
     */
    void init( IMemento memento, IMemento viewMemento, String issueId, String groupId, ReferencedEnvelope bounds );
    /**
     * Populates the memento with data specialized for the issues type.  Most data (see below) is saved by the framework.  Only data, such as properties,
     * or other data required for executing {@link #fixIssue(IViewPart, IEditorPart)} needs to be saved in the memento.
     * 
     * <p>State that is saved by the framework and is not required to be saved in this memento include:</p>
     * <ul>
     * <li>id</li>
     * <li>priority</li>
     * <li>group id</li>
     * <li>description</li>
     * <li>resolution</li>
     * <li>view memento</li>
     * <li>bounds</li>
     * <li>issue extension point</li>
     * </ul>  
     * @param memento the memento to populate with state data.
     */
    void save( IMemento memento );
    /**
     * Returns the id of the extension definition for this type of Issue.
     * This is required so the framework can load the correct issue class during issue loading.
     * If the class is can not be loaded then a Placeholder issue will be created instead which will
     * notify the user that a plugin is required to manage the issue.  
     * <p> The extension id is formed by the namespace of the plugin combined with the name of the extension element.</p>
     * <p>For example:  The FeatureIssue's extension id would be: <em>net.refractions.udig.issues.featureIssue</em> because the issue implementation is in
     * the Issues plugin (which the id <em>net.refractions.udig.issues</em>) and the extension name is featureIssue.  The two are combined to form the id of the
     * extension</p> 
     */
    String getIssueType();
    /**
     * Returns the area that this issue affects.  May return null.
     *
     * @return Returns the area that this issue affects.  May return null.
     */
    ReferencedEnvelope getBounds();
}


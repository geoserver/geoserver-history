package org.vfny.geoserver.issues;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.vfny.geoserver.issues.enums.Priority;
import org.vfny.geoserver.issues.enums.Resolution;

/**
 * The issue interface. Represents a single issue. 
 * They have the following basic properties:<BR>
 * -id<BR>
 * -Description<BR>
 * -Priority<BR>
 * -Resolution<BR>
 * -Bounds<BR>
 * -memento<BR>
 * -viewMemento<BR>
 * -target<BR>
 * 
 * @author quintona
 * @since 1.0.0
 */
public interface IIssue {
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
    int getId();
    /**
     * Sets the area that this issue effects
     * 
     * @param bounds The reference bounds
     */
    void setBounds(ReferencedEnvelope bounds);
    /**
     * Returns the area that this issue affects.  May return null.
     *
     * @return Returns the area that this issue affects.  May return null.
     */
    ReferencedEnvelope getBounds();
    /**
     * Called by the issue service. Passed an existing memento that must
     * be populated by the user of the service
     * @param memento
     */
    void save(IMemento memento);
    /**
     * Returns an existing memento. It is assumed that the user of the service
     * has the keys to extract the state from the memento
     * @return
     */
    IMemento getMemento();
    /**
     * Called by the issue service. Passed an existing memento that must
     * be populated by the user of the service
     * @param memento
     */
    void saveViewMemento(IMemento memento);
    /**
     *
     * @return
     */
    IMemento getViewMemento();
    /**
     *
     * @param target
     */
    void setTarget(Target target);
    /**
     *
     * @return
     */
    Target getTarget();
}


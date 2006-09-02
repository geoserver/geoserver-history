package org.vfny.geoserver.issues;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewPart;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.vfny.geoserver.issues.enums.Priority;
import org.vfny.geoserver.issues.enums.Resolution;
import org.vfny.geoserver.issues.listeners.IIssueListener;

public class Issue implements IIssue {

    private String id;
    
    public Issue(String id){
        this.id = id;
    }
    
    public void addIssueListener( IIssueListener listener ) {
    }

    public void removeIssueListener( IIssueListener listener ) {
    }

    public String getViewPartId() {
        return null;
    }

    public void getViewMemento( IMemento memento ) {
    }

    public String getEditorID() {
        return null;
    }

    public IEditorInput getEditorInput() {
        return null;
    }

    public String getPerspectiveID() {
        return null;
    }

    public String getProblemObject() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public void setDescription( String description ) {
    }

    public Priority getPriority() {
        return null;
    }

    public void setPriority( Priority priority ) {
    }

    public void fixIssue( IViewPart part, IEditorPart editor ) {
    }

    public Resolution getResolution() {
        return null;
    }

    public void setResolution( Resolution newResolution ) {
    }

    public String getId() {
        return id;
    }

    public void setId( String newId ) {
        this.id = newId;
    }

    public String getGroupId() {
        return null;
    }

    public Object getProperty( String property ) {
        return null;
    }

    public String[] getPropertyNames() {
        return null;
    }

    public void init( IMemento memento, IMemento viewMemento, String issueId, String groupId,
            ReferencedEnvelope bounds ) {
    }

    public void save( IMemento memento ) {
    }

    public String getExtensionID() {
        return null;
    }

    public ReferencedEnvelope getBounds() {
        return null;
    }

}

package org.vfny.geoserver.issues;

import java.util.List;

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

    public Resolution getResolution() {
        return null;
    }

    public void setResolution( Resolution newResolution ) {
    }

    public String getId() {
        return null;
    }

    public void setId( String newId ) {
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

    public void addProperty( String key, Object value ) {
    }

    public void init( IMemento memento, IMemento viewMemento, String issueId, String groupId, ReferencedEnvelope bounds ) {
    }

    public void save( IMemento memento ) {
    }

    public String getIssueType() {
        return null;
    }

    public ReferencedEnvelope getBounds() {
        return null;
    }
}

package org.vfny.geoserver.issues;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class ListStrategy implements IListStrategy {

    public void addIssues( List< ? extends IIssue> issues ) throws IOException {
        
    }

    public void modifyIssue( IIssue issue ) throws IOException {
    }

    public void removeIssues( Collection< ? extends IIssue> issues ) throws IOException {
    }

    public Collection< ? extends IIssue> getIssues() throws IOException {
        return null;
    }

}

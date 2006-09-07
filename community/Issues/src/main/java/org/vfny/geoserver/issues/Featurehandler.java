package org.vfny.geoserver.issues;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class Featurehandler implements IIssueHandler {

    public boolean canHandle( String issueType ) {
        return false;
    }

    public void addIssues( List<IIssue> issues ) throws IOException {
    }

    public void modifyIssue( IIssue issue ) throws IOException {
    }

    public void removeIssues( Collection<IIssue> issues ) throws IOException {
    }

    public Collection<IIssue> getIssues( String groupId ) throws IOException {
        return null;
    }

    public Collection<IIssue> getIssues() throws IOException {
        return null;
    }

}

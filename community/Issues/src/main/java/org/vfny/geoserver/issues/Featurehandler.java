package org.vfny.geoserver.issues;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class Featurehandler implements IIssueHandler {

    public boolean canHandle( Object target ) {
        return false;
    }

    public void addIssues( Object target, List<IIssue> issues ) throws IOException {
    }

    public void modifyIssue( Object target, IIssue issue ) throws IOException {
    }

    public void removeIssues( Object target, Collection<IIssue> issues ) throws IOException {
    }

    public Collection<IIssue> getIssues( Object target ) throws IOException {
        return null;
    }

}

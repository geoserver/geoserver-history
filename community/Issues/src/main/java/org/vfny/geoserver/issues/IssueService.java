package org.vfny.geoserver.issues;

import java.io.IOException;
import java.util.Collection;
import java.util.List;


public class IssueService implements IIssueService {

    public Collection<IIssue> getIssues()throws IOException{
        return null;
    }
    
    public Collection< ? extends IIssue> getIssues( ITarget target ) throws IOException {
        return null;
    }

    public void addIssues(List<IIssue> issues)
        throws IOException {
        
    }

    public void modifyIssue(IIssue issue)
        throws IOException {
        
    }

    public void removeIssues(Collection<IIssue> issues)
        throws IOException {
        
    }

    

    
}

package org.vfny.geoserver.issues;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.vfny.geoserver.issues.dao.IssuesDao;


public class IssueService implements IIssueService {

    private IssuesDao issuesDao;
    
    public Collection<IIssue> getIssues()throws IOException{
        return null;
    }
    
    public Collection< ? extends IIssue> getIssues(Target target ) throws IOException {
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
    
    public void setIssueDao(IssuesDao issueDao){
        this.issuesDao = issueDao;
    }

    

    
}

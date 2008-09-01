package org.vfny.geoserver.issues;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.vfny.geoserver.issues.dao.IssuesDao;


public class IssueService implements IIssueService {

    private IssuesDao issuesDao;
    
    public Collection<IIssue> getIssues(){
        return issuesDao.getAllIssues();
    }
    
    public Collection<IIssue> getIssues(Target target ){
        return issuesDao.findByTarget(target);
    }

    public void addIssues(Collection<IIssue> issues){
        issuesDao.insertIssues(getIssueValueObjects(issues));
    }

    public void modifyIssue(IIssue issue){
        IIssue issueVo = new Issue(issue);
        issuesDao.updateIssue(issueVo);
    }

    public void removeIssues(Collection<IIssue> issues){
        issuesDao.removeIssues(getIssueValueObjects(issues));
    }
    
    public void setIssuesDao(IssuesDao issuesDao){
        this.issuesDao = issuesDao;
    }

    public IssuesDao getIssuesDao(){
        return issuesDao;
    }
    
    private Collection<IIssue> getIssueValueObjects(Collection<IIssue> issues){
        Collection<IIssue> issueVos = new LinkedList<IIssue>();
        Iterator<IIssue> it = issues.iterator();
        while(it.hasNext()){
            IIssue newIssue = new Issue(it.next());
            issueVos.add(newIssue);
        }
        return issueVos;
    }

    
}

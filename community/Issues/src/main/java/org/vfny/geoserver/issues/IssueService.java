package org.vfny.geoserver.issues;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IssueService implements IIssueService {

    private List<IIssueHandler> issueHandlers = new LinkedList<IIssueHandler>();
    
    public Collection<IIssue> getIssues( Object target ) throws IOException {
        Collection<IIssue> issues = new LinkedList<IIssue>();
        Iterator<IIssueHandler> it = issueHandlers.iterator();
        while(it.hasNext()){
            IIssueHandler handler = it.next();
            if(handler.canHandle(target)){
                issues.addAll(handler.getIssues(target));
            }
        }
        return issues;
    }
    
    void setIssueHandlers(List<IIssueHandler> issueHandlers){
        this.issueHandlers = issueHandlers;
    }

    public void addIssues( Object target, List<IIssue> issues ) throws IOException {
        Iterator<IIssueHandler> it = issueHandlers.iterator();
        while(it.hasNext()){
            IIssueHandler handler = it.next();
            if(handler.canHandle(target)){
                handler.addIssues(target,issues);
            }
        }
    }

    public void modifyIssue( Object target, IIssue issue ) throws IOException {
        Iterator<IIssueHandler> it = issueHandlers.iterator();
        while(it.hasNext()){
            IIssueHandler handler = it.next();
            if(handler.canHandle(target)){
                handler.modifyIssue(target,issue);
            }
        }
    }

    public void removeIssues( Object target, Collection<IIssue> issues ) throws IOException {
        Iterator<IIssueHandler> it = issueHandlers.iterator();
        while(it.hasNext()){
            IIssueHandler handler = it.next();
            if(handler.canHandle(target)){
                handler.removeIssues(target,issues);
            }
        }
    }

}

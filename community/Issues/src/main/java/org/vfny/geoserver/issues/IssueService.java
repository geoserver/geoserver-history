package org.vfny.geoserver.issues;

import java.io.IOException;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class IssueService implements IIssueService {
    private List<IIssueHandler> issueHandlers = new LinkedList<IIssueHandler>();

    public Collection<IIssue> getIssues()
        throws IOException {
        Collection<IIssue> issues = new LinkedList<IIssue>();
        Iterator<IIssueHandler> it = issueHandlers.iterator();
        while (it.hasNext()) {
            IIssueHandler handler = it.next();
            issues.addAll(handler.getIssues());
        }

        return issues;
    }
    
    public Collection< ? extends IIssue> getIssues( String groupId ) throws IOException {
        Collection<IIssue> issues = new LinkedList<IIssue>();
        Iterator<IIssueHandler> it = issueHandlers.iterator();
        while (it.hasNext()) {
            IIssueHandler handler = it.next();
            issues.addAll(handler.getIssues(groupId));
        }

        return issues;
    }

    void setIssueHandlers(List<IIssueHandler> issueHandlers) {
        this.issueHandlers = issueHandlers;
    }

    public void addIssues(List<IIssue> issues)
        throws IOException {
        /*Iterator<IIssueHandler> it = issueHandlers.iterator();

        while (it.hasNext()) {
            IIssueHandler handler = it.next();

            if (handler.canHandle(target)) {
                handler.addIssues(target, issues);
            }
        }*/
    }

    public void modifyIssue(IIssue issue)
        throws IOException {
        /*Iterator<IIssueHandler> it = issueHandlers.iterator();

        while (it.hasNext()) {
            IIssueHandler handler = it.next();

            if (handler.canHandle(target)) {
                handler.modifyIssue(target, issue);
            }
        }*/
    }

    public void removeIssues(Collection<IIssue> issues)
        throws IOException {
        /*Iterator<IIssueHandler> it = issueHandlers.iterator();

        while (it.hasNext()) {
            IIssueHandler handler = it.next();

            if (handler.canHandle(target)) {
                handler.removeIssues(target, issues);
            }
        }*/
    }

    
}

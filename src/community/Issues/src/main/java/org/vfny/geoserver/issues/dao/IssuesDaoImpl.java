package org.vfny.geoserver.issues.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.vfny.geoserver.issues.IIssue;
import org.vfny.geoserver.issues.Issue;
import org.vfny.geoserver.issues.Target;
import org.vfny.geoserver.issues.TargetWrapper;

public class IssuesDaoImpl extends HibernateDaoSupport implements IssuesDao {

	@SuppressWarnings("unchecked")
    public IIssue findIssueById( int id ) {
	      List<IIssue> list=getHibernateTemplate().find("from Issue where id=?",id);
		  return list.get(0);
    }

	@SuppressWarnings("unchecked")
    public Collection<IIssue> findByTarget( Target target ) {
    	List<IIssue> list=getHibernateTemplate().find("from Issue where targetstring=?",
    			TargetWrapper.getStringFromTarget(target));
    	return list;
    }

    public void insertIssue( IIssue issue ) {
    	getHibernateTemplate().save(issue);
    }

    public void insertIssues( Collection<IIssue> issues ) {
    	Iterator<IIssue> it = issues.iterator();
    	while(it.hasNext())
    	{
    		insertIssue(it.next());
    	}
    }

    public void updateIssue( IIssue issue ) {
    	getHibernateTemplate().update(issue);    	
    }

    public void updateIssues( Collection<IIssue> issues ) {
    	Iterator<IIssue> it = issues.iterator();
    	while(it.hasNext())
    	{
    		updateIssue(it.next());
    	}    	
    }

    @SuppressWarnings("unchecked")
    public Collection<IIssue> getAllIssues() {
    	return getHibernateTemplate().loadAll(Issue.class);
    }

    public void removeIssue( IIssue issue ) {
    	getHibernateTemplate().delete(issue);
    }

    public void removeIssues( Collection<IIssue> issues ) {
    	Iterator<IIssue> it = issues.iterator();
    	while(it.hasNext())
    	{
    		removeIssue(it.next());
    	}      	
    }

}

package org.vfny.geoserver.issues.dao;

import java.util.Collection;
import java.util.List;

import org.vfny.geoserver.issues.IIssue;
import org.vfny.geoserver.issues.Target;

/**
 * Hibernate data access object for issues
 * @author Pablo Casado
 *
 */
public interface IssuesDao {
	   IIssue findIssueById(int id);
	   Collection< ? extends IIssue> findByTarget(Target target);
	   void insertIssue(IIssue issue);
	   void insertIssues(Collection< ? extends IIssue> issues);
	   void updateIssue(IIssue issue);
	   void updateIssues(Collection< ? extends IIssue> issues);
	   Collection< ? extends IIssue> getAllIssues();
	   void removeIssue(IIssue issue);
	   void removeIssues(Collection< ? extends IIssue>  issues);
}

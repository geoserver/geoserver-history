package org.vfny.geoserver.issues.dao;

import java.util.Collection;

import org.vfny.geoserver.issues.IIssue;
import org.vfny.geoserver.issues.Target;

/**
 * Hibernate data access object for issues
 * @author Pablo Casado
 *
 */
public interface IssuesDao {
	IIssue findIssueById(int id);
	Collection<IIssue> findByTarget(Target target);
	void insertIssue(IIssue issue);
	void insertIssues(Collection<IIssue> issues);
	void updateIssue(IIssue issue);
	void updateIssues(Collection<IIssue> issues);
	Collection<IIssue> getAllIssues();
	void removeIssue(IIssue issue);
	void removeIssues(Collection<IIssue>  issues);
}

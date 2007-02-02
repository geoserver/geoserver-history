package org.vfny.geoserver.control;

import java.util.List;

/**
 * IStatusReporter is the entry point for someone wishing to query the status of
 * modules. It provides a method to retrieve all of the IStatusReports. It is 
 * available remotely using the Spring Remoting API.
 * 
 * @author Richard Gould
 *
 */
public interface IStatusReporter {
	
	/**
	 * Aggregates all of the available IStatusReports from various sources that
	 * it knows about. This could include actually querying the modules
	 * themselves for their statuses.
	 * 
	 * @return a List of type IStatusReport representing the statuses of multiple modules
	 */
	public List getStatusReports();
	
	/**
	 * Resets the server's application context.
	 */
	public void reset();
}

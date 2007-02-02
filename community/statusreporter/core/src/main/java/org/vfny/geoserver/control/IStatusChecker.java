package org.vfny.geoserver.control;

/**
 * IStatusChecker is the interface used by IStatusReporter to check on the 
 * status of a module. It is responsible for querying the modules themselves and
 * creating an IStatusReport.
 * 
 * @author Richard Gould
 *
 */
public interface IStatusChecker {
	
	/**
	 * Queries a given module for its status and builds a report containing the
	 * module name, status, and a corresponding message (if applicable)
	 * 
	 * @return the status report generated from the module
	 */
	public IStatusReport checkStatus();
}

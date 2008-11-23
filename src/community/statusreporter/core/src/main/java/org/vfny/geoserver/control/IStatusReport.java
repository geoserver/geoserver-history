package org.vfny.geoserver.control;

import java.io.Serializable;

/**
 * An IStatusReport represents the results of querying a module regarding its
 * status. It contains information regarding the modules status, represented by
 * the three static constants OKAY, WARNING, and ERROR. It also contains an
 * associated message and an identifier that can be used to trace this report
 * back to the module.
 * 
 * Because IStatusReports are intended to be sent over remote channels, they 
 * must all be Serializable. 
 *  
 * @author Richard Gould
 *
 */
public interface IStatusReport extends Serializable {
	
	/**
	 * Indicates that everything is working properly in the intended module.
	 * A message is not usually associated with this status.
	 */
	public static final int OKAY = 0;
	
	/**
	 * Indicates that something is potentially wrong in the module, but this is 
	 * not a fatal problem. A message about the problem could be retrieved using
	 * getMessage().
	 */
	public static final int WARNING = 1;
	
	/**
	 * Indicates that something is wrong with the module and it is not 
	 * functioning. A message about the problem should be available by calling
	 * getMessage().
	 */
	public static final int ERROR = 2;
	
	/**
	 * The module name is a human readable String used to identify this module.
	 *    
	 * @return the name of the module that this report is based upon.
	 */
	public String getModuleName();
	
	/**
	 * Message is an Exception containing a message or a stack trace relevant to
	 * the current status. Message could be null, for instance if status was 
	 * OKAY.
	 *   
	 * @return a message from the module relevant to the status
	 */
	public Exception getMessage();
	
	/**
	 * Status can be one of the three static constants OKAY, WARNING or ERROR.
	 * 
	 * @return the status indicator of the module that this report is based upon
	 */
	public int getStatus();
}

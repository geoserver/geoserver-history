package org.vfny.geoserver.control.internal;

import org.vfny.geoserver.control.IStatusReport;

/**
 * Basic read-only bean implementation of IStatusReport. Supplies getters for 
 * each field, which can be passed in through the constructor.
 * 
 * @author Richard Gould
 *
 */
public class DefaultStatusReport implements IStatusReport {

	private static final long serialVersionUID = 5178736895237853217L;
	private int status;
	private Exception message;
	private String name;

	/**
	 * status can be one of IStatusReport's three constants: OKAY, WARNING, or
	 * ERROR.
	 */
	public DefaultStatusReport(String name, int status, Exception message) {
		super();
		this.name = name;
		this.status = status;
		this.message = message;
	}

	public Exception getMessage() {
		return message;
	}

	/**
	 * status should be one of IStatusReport's three constants: OKAY, WARNING, 
	 * or ERROR.
	 */
	public int getStatus() {
		return status;
	}

	public String getModuleName() {
		return name;
	}
}

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

/**
 * Thrown when there is an error in configuration.
 *
 * @author Chris Holmes
 * @version $Id: ConfigurationException.java,v 1.1.2.2 2004/01/05 22:14:40 dmzwiers Exp $
 */
public class ConfigurationException extends Exception {
    /**
     * Constructs a new instance of ConfigurationException
     *
     * @param msg A message explaining the exception
     */
    public ConfigurationException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new instance of ConfigurationException
     *
     * @param msg A message explaining the exception
     * @param exp the throwable object which caused this exception
     */
    public ConfigurationException(String msg, Throwable exp) {
        super(msg, exp);
    }

	/**
	 * Constructs a new instance of ConfigurationException
	 *
	 * @param exp the throwable object which caused this exception
	 */
	public ConfigurationException(Throwable exp) {
		super(exp);
	}
}

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

/**
 * Thrown when there is an error in configuration.
 *
 * @author Chris Holmes
 * @version $Id: ConfigurationException.java,v 1.2 2003/12/16 18:46:07 cholmesny Exp $
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
}

/* Copyright (c) 2001 - 2007 TOPP - http://topp.openplans.org.
 * All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible in the
 * license.txt file of the documents directory off the root directory.
 */
package org.vfny.geoserver.control;

/**
 * Interface for a validator that validates the preferences.
 * 
 * @author Jesse
 */
public interface IValidator {
    /**
     * Returns true if the value is a legal value for the key.
     *
     * @param key preference key
     * @param value potential value for preference
     *
     * @return true if the value is a legal value for the key.
     */
    boolean isValid(String key, String value);
}

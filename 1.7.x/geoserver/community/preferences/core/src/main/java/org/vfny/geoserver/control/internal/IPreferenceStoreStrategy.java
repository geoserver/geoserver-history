/* Copyright (c) 2001 - 2007 TOPP - http://topp.openplans.org.
 * All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible in the
 * license.txt file of the documents directory off the root directory.
 */
package org.vfny.geoserver.control.internal;

import org.vfny.geoserver.control.IValidator;

/**
 * <b>NOT API.</b>
 * 
 * A "strategy" object that the PreferenceControllerImpl uses to store its data.
 * 
 * @author Jesse
 */
public interface IPreferenceStoreStrategy {
    /**
     * Gets the value of the preference
     *
     * @param key preference key
     *
     * @return the value
     */
    String get(String key);

    /**
     * Sets the prefence
     *
     * @param key the preference key
     * @param value the new value.
     */
    void put(String key, String value);

    /**
     * Clears the preference so the default is used.
     *
     * @param key the preference key;
     */
    void unset(String key);

    /**
     * Returns all the keys of the existing preferences
     *
     * @return all the keys of the existing preferences
     */
    String[] keys();

    /**
     * Sets the validator that this strategy uses.
     * 
     * @param validator new validator.
     */
	void setValidator(IValidator validator);
}

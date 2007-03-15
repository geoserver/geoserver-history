/* Copyright (c) 2001 - 2007 TOPP - http://topp.openplans.org.
 * All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible in the
 * license.txt file of the documents directory off the root directory.
 */
package org.vfny.geoserver.control;

/**
 * An interface for obtaining and setting preferences.  A default value may be optionally set.  
 * 
 * @author Jesse
 */
public interface IPreferenceStore {
    /**
     * Returns the preference as a string.  If the default is not set
     * "" is returned.
     *
     * @param key indicates the preference to return.
     *
     * @return the preference as a string
     */
    String getString(String key);

    /**
     * Returns the preference as an integer.  If the default is not set
     * 0 is returned.
     *
     * @param key indicates the preference to return.
     *
     * @return the preference as an integer
     */
    int getInt(String key);

    /**
     * Returns the preference as a float.  If the default is not set 0
     * is returned.
     *
     * @param key indicates the preference to return.
     *
     * @return the preference as a float.
     */
    float getFloat(String key);

    /**
     * Returns the preference as a boolean.  If the default is not set
     * false is returned.
     *
     * @param key indicates the preference to return.
     *
     * @return the preference as a boolean
     */
    boolean getBoolean(String key);

    /**
     * Returns the preference as a char.  If the default is not set ' '
     * is returned.
     *
     * @param key indicates the preference to return.
     *
     * @return the preference as a char
     */
    char getChar(String key);

    /**
     * Returns the preference as a double.  If the default is not set 0
     * is returned.
     *
     * @param key indicates the preference to return.
     *
     * @return the preference as an double
     */
    double getDouble(String key);

    /**
     * Returns the preference as a long.  If the default is not set 0
     * is returned.
     *
     * @param key indicates the preference to return.
     *
     * @return the preference as an long
     */
    long getLong(String key);

    /**
     * Sets the preference to value.
     *
     * @param key the preference to set.
     * @param value the new value
     */
    void set(String key, String value);

    /**
     * Sets the preference to value.
     *
     * @param key the preference to set.
     * @param value the new value
     */
    void set(String key, int value);

    /**
     * Sets the preference to value.
     *
     * @param key the preference to set.
     * @param value the new value
     */
    void set(String key, float value);

    /**
     * Sets the preference to value.
     *
     * @param key the preference to set.
     * @param value the new value
     */
    void set(String key, boolean value);

    /**
     * Sets the preference to value.
     *
     * @param key the preference to set.
     * @param value the new value
     */
    void set(String key, char value);

    /**
     * Sets the preference to value.
     *
     * @param key the preference to set.
     * @param value the new value
     */
    void set(String key, double value);

    /**
     * Sets the preference to value.
     *
     * @param key the preference to set.
     * @param value the new value
     */
    void set(String key, long value);

    /**
     * Clears the preference so the default is used.
     *
     * @param key the preference key;
     */
    void unset(String key);

    /**
     * Sets the default for the preference to value.
     *
     * @param key the preference to set.
     * @param value the new default value
     */
    void setDefault(String key, String value);

    /**
     * Sets the default for the preference to value.
     *
     * @param key the preference to set.
     * @param value the new default value
     */
    void setDefault(String key, int value);

    /**
     * Sets the default for the preference to value.
     *
     * @param key the preference to set.
     * @param value the new default value
     */
    void setDefault(String key, float value);

    /**
     * Sets the default for the preference to value.
     *
     * @param key the preference to set.
     * @param value the new default value
     */
    void setDefault(String key, boolean value);

    /**
     * Sets the default for the preference to value.
     *
     * @param key the preference to set.
     * @param value the new default value
     */
    void setDefault(String key, char value);

    /**
     * Sets the default for the preference to value.
     *
     * @param key the preference to set.
     * @param value the new default value
     */
    void setDefault(String key, double value);

    /**
     * Sets the default for the preference to value.
     *
     * @param key the preference to set.
     * @param value the new default value
     */
    void setDefault(String key, long value);

    /**
     * Returns all the keys of the existing preferences
     *
     * @return all the keys of the existing preferences
     */
    String[] keys();
}

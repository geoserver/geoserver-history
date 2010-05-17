/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class implements some library function for all the sub classes in this
 * package. This class follows the Layer Supertype pattern as defined in
 * Fowler's Patterns of Enterprise Application Architecture. Here the pattern
 * is described in great detail between pages 475 and 479. The main idea is to
 * have a super class contain all the common functionality of the sub-classes
 * which does not pertain to particular manipulations within the classes.
 *
 * @author Gabriel Rold�n
 * @author dzwiers
 * @version $Id$
 */
abstract class GlobalLayerSupertype {
    /** for debugging */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.global");

    /**
     * getDTO purpose.
     *
     * <p>
     * A hook to get the DTO object of all subclasses.  This method is
     * dangerous as it return the original.  It is not intended to be public,
     * and should only be accessed  without a clone being created inside the
     * package.
     * </p>
     *
     * @return A DTO object to be casted as appropriate.
     */
    abstract Object toDTO();

    /**
     * checks <code>s</code> for nullity and if so, returns an empty String,
     * else just returns <code>s</code>
     *
     * @param s String
     *
     * @return String non-null String (null -> "")
     */
    protected String notNull(String s) {
        return (s == null) ? "" : s;
    }

    /**
     * get purpose.
     *
     * <p>
     * Gets a String from a map of Strings
     * </p>
     *
     * @param map Map the map to extract the string from
     * @param key String the key for the map.
     *
     * @return String the value in the map.
     *
     * @see Map
     */
    public static String get(Map map, String key) {
        if (map.containsKey(key)) {
            return (String) map.get(key);
        }

        return null;
    }

    /**
     * get purpose.
     *
     * <p>
     * Gets a File from a map given the specified key. If the file is not found
     * the default file is returned.
     * </p>
     *
     * @param map Map the map to extract the file from
     * @param key String the key to extract the value for
     * @param defaultFile The default value should the key not exist.
     *
     * @return File a File as described above.
     */
    public static File get(Map map, String key, File defaultFile) {
        if (map.containsKey(key)) {
            return (File) map.get(key);
        }

        return defaultFile;
    }

    /**
     * get purpose.
     *
     * <p>
     * Gets a String from a map of Strings, and returns the default if the
     * string does not exist.
     * </p>
     *
     * @param map Map the map to extract the string from
     * @param key String the key for the map.
     * @param defaultValue The default value should the key not exist.
     *
     * @return String the value in the map.
     *
     * @see Map
     */
    public static String get(Map map, String key, String defaultValue) {
        if (map.containsKey(key)) {
            return (String) map.get(key);
        }

        return defaultValue;
    }

    /**
     * get purpose.
     *
     * <p>
     * Gets a List from a map given the specified key. If the list is not found
     * the default list is returned.
     * </p>
     *
     * @param map Map the map to extract the file from
     * @param key String the key to extract the value for
     * @param defaultList The default value should the key not exist.
     *
     * @return List a List as described above.
     */
    public static List get(Map map, String key, List defaultList) {
        if (map.containsKey(key)) {
            return (List) map.get(key);
        }

        return defaultList;
    }

    /**
     * get purpose.
     *
     * <p>
     * Gets a Map from a map given the specified key. If the map is not found
     * the default map is returned.
     * </p>
     *
     * @param map Map the map to extract the file from
     * @param key String the key to extract the value for
     * @param defaultMap The default value should the key not exist.
     *
     * @return Map a Map as described above.
     */
    public static Map get(Map map, String key, Map defaultMap) {
        if (map.containsKey(key)) {
            return (Map) map.get(key);
        }

        return defaultMap;
    }

    /**
     * get purpose.
     *
     * <p>
     * Gets a int from a map given the specified key. If the int is not found
     * the default int is returned.
     * </p>
     *
     * @param map Map the map to extract the file from
     * @param key String the key to extract the value for
     * @param defaultValue The default value should the key not exist.
     *
     * @return int an int as described above.
     */
    public static int get(Map map, String key, int defaultValue) {
        if (map.containsKey(key)) {
            return Integer.parseInt((String) map.get(key));
        } else {
            return defaultValue;
        }
    }

    /**
     * get purpose.
     *
     * <p>
     * Gets a boolean from a map given the specified key. If the boolean is not
     * found the default boolean is returned.
     * </p>
     *
     * @param map Map the map to extract the file from
     * @param key String the key to extract the value for
     * @param defaultValue The default value should the key not exist.
     *
     * @return boolean an boolean as described above.
     */
    public static boolean get(Map map, String key, boolean defaultValue) {
        if (map.containsKey(key)) {
            return Boolean.getBoolean((String) map.get(key));
        } else {
            return defaultValue;
        }
    }

    /**
     * get purpose.
     *
     * <p>
     * Gets a Charset from a map given the specified key. If the Charset is not
     * found the default Charset is returned.
     * </p>
     *
     * @param map Map the map to extract the file from
     * @param key String the key to extract the value for
     * @param defaultCharSet The default value should the key not exist.
     *
     * @return Charset an boolean as described above.
     */
    public static Charset get(Map map, String key, Charset defaultCharSet) {
        if (map.containsKey(key)) {
            return (Charset) map.get(key);
        }

        return defaultCharSet;
    }

    /**
     * get purpose.
     *
     * <p>
     * Gets a Level from a map given the specified key. If the Level is not
     * found the default Level is returned.
     * </p>
     *
     * @param map Map the map to extract the file from
     * @param key String the key to extract the value for
     * @param defaultLevel The default value should the key not exist.
     *
     * @return Level an boolean as described above.
     */
    public static Level get(Map map, String key, Level defaultLevel) {
        if (map.containsKey(key)) {
            return (Level) map.get(key);
        }

        return defaultLevel;
    }

    /**
     * get purpose.
     *
     * <p>
     * Gets a URL from a map given the specified key. If the URL is not found
     * the default URL is returned.
     * </p>
     *
     * @param map Map the map to extract the file from
     * @param key String the key to extract the value for
     * @param defaultUrl The default value should the key not exist.
     *
     * @return URL an boolean as described above.
     */
    public static URL get(Map map, String key, URL defaultUrl) {
        if (map.containsKey(key)) {
            return (URL) map.get(key);
        }

        return defaultUrl;
    }

    /**
     * get purpose.
     *
     * <p>
     * Gets a Class from a map given the specified key. If the Class is not
     * found the default Class is returned.
     * </p>
     *
     * @param map Map the map to extract the file from
     * @param key String the key to extract the value for
     * @param defaultType The default value should the key not exist.
     *
     * @return Class an boolean as described above.
     */
    public static Class get(Map map, String key, Class defaultType) {
        if (!map.containsKey(key)) {
            return defaultType;
        }

        Object value = map.get(key);

        if (value instanceof Class) {
            return (Class) value;
        }

        if (value instanceof String) {
            try {
                return Class.forName((String) value);
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.FINEST, e.getMessage(), e);
            }
        }

        return defaultType;
    }
}

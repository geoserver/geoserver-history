/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataStoreFactorySpi.Param;


/**
 * A collecitno of utilties for dealing with GeotTools DataStore.
 *
 * @author Richard Gould, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: DataStoreUtils.java,v 1.6 2004/02/09 23:29:41 dmzwiers Exp $
 */
public abstract class DataStoreUtils {
    public static DataStore aquireDataStore(Map params)
        throws IOException {
        return DataStoreFinder.getDataStore(params);
    }

    /**
     * When loading from DTO use the params to locate factory.
     * 
     * <p>
     * bleck
     * </p>
     *
     * @param params
     *
     * @return
     */
    public static DataStoreFactorySpi aquireFactory(Map params) {
        for (Iterator i = DataStoreFinder.getAvailableDataStores();
                i.hasNext();) {
            DataStoreFactorySpi factory = (DataStoreFactorySpi) i.next();

            if (factory.canProcess(params)) {
                return factory;
            }
        }

        return null;
    }

    /**
     * After user has selected Description can aquire Factory based on
     * description.
     * 
     * <p>
     * Use factory for:
     * </p>
     * 
     * <ul>
     * <li>
     * List of Params (attrb name, help text)
     * </li>
     * <li>
     * Checking user's input with factory.canProcess( params )
     * </li>
     * </ul>
     * 
     *
     * @param description
     *
     * @return
     */
    public static DataStoreFactorySpi aquireFactory(String description) {
        for (Iterator i = DataStoreFinder.getAvailableDataStores();
                i.hasNext();) {
            DataStoreFactorySpi factory = (DataStoreFactorySpi) i.next();

            if (factory.getDescription().equals(description)) {
                return factory;
            }

            if (factory.getClass().toString().equals(description)) {
                return factory;
            }
        }

        return null;
    }

    /**
     * Utility method for finding Params
     *
     * @param factory DOCUMENT ME!
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Param find(DataStoreFactorySpi factory, String key) {
        return find(factory.getParametersInfo(), key);
    }

    /**
     * Utility methods for find param by key
     *
     * @param params DOCUMENT ME!
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Param find(Param[] params, String key) {
        for (int i = 0; i < params.length; i++) {
            if (key.equalsIgnoreCase(params[i].key)) {
                return params[i];
            }
        }

        return null;
    }

    /**
     * Returns the descriptions for the available DataStores.
     * 
     * <p>
     * Arrrg! Put these in the select box.
     * </p>
     *
     * @return Descriptions for user to choose from
     */
    public static List listDataStoresDescriptions() {
        List list = new ArrayList();

        for (Iterator i = DataStoreFinder.getAvailableDataStores();
                i.hasNext();) {
            DataStoreFactorySpi factory = (DataStoreFactorySpi) i.next();
            list.add(factory.getDescription());
        }

        return list;
    }

    public static Map defaultParams(String description) {
        return defaultParams(aquireFactory(description));
    }

    public static Map defaultParams(DataStoreFactorySpi factory) {
        Map defaults = new HashMap();
        Param[] params = factory.getParametersInfo();

        for (int i = 0; i < params.length; i++) {
            String key = params[i].key;
            String value = null;

            if (params[i].required) {
                value = "";
            }

            String description = factory.getDescription();

            if ("dbtype".equals(key)) {
                if ("PostGIS spatial database".equals(description)) {
                    value = "postgis";
                }

                if ("Oracle Spatial Database".equals(description)) {
                    value = "oracle";
                }

                if ("ESRI ArcSDE 8.x".equals(description)) {
                    value = "arcsde";
                }
            }

            if (value != null) {
                defaults.put(key, value);
            }
        }

        return defaults;
    }

    /**
     * Convert map to real values based on factory Params.
     * 
     * <p>
     * The resulting map should still be checked with factory.acceptsMap( map )
     * </p>
     *
     * @param factory
     * @param params
     *
     * @return Map with real values that may be acceptable to Factory
     *
     * @throws IOException DOCUMENT ME!
     */
    public static Map toConnectionParams(DataStoreFactorySpi factory, Map params)
        throws IOException {
        Map map = new HashMap(params.size());

        Param[] info = factory.getParametersInfo();

        // Convert Params into the kind of Map we actually need
        for (Iterator i = params.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();

            Object value = find(info, key).lookUp(params);

            if (value != null) {
                map.put(key, value);
            }
        }

        return map;
    }
}

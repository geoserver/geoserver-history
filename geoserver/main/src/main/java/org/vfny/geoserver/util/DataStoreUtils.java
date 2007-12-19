/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.util;

import com.vividsolutions.jts.geom.Envelope;
import org.geoserver.feature.FeatureSourceUtils;
import org.geoserver.feature.retype.RetypingDataStore;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFactorySpi.Param;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.vfny.geoserver.global.DataStoreInfo;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;


/**
 * A collecitno of utilties for dealing with GeotTools DataStore.
 *
 * @author Richard Gould, Refractions Research, Inc.
 * @author $Author: cholmesny $ (last modification)
 * @version $Id$
 */
public abstract class DataStoreUtils {
    /**
     * Uses the standard datastore factory mechanism, but first manipulates the
     * specified parameters so that data dir relative paths gets turned into
     * absolute ones
     * @param params
     * @param sc
     * @return
     * @throws IOException
     */
    public static DataStore acquireDataStore(Map params, ServletContext sc)
        throws IOException {
        //DJB: changed this for geoserver_data_dir   	
        //String baseDir = sc.getRealPath("/");
        File baseDir = GeoserverDataDirectory.getGeoserverDataDirectory();

        return getDataStore(getParams(params, baseDir.getAbsolutePath()));
    }

    /**
     * Looks up the datastore using the given params, verbatim, and then
     * eventually wraps it into a renaming wrapper so that feature type
     * names are good ones from the wfs point of view (that is, no ":" in the type names)
     * @param params
     * @return
     */
    public static DataStore getDataStore(Map params) throws IOException {
        DataStore store = DataStoreFinder.getDataStore(params);

        if (store == null) {
            return null;
        }

        String[] names = store.getTypeNames();

        for (int i = 0; i < names.length; i++) {
            if (names[i].indexOf(":") >= 0) {
                return new RetypingDataStore(store);
            }
        }

        return store;
    }

    public static Map getParams(Map m, ServletContext sc) {
        File data_dir = GeoserverDataDirectory.getGeoserverDataDirectory();
        String baseDir = data_dir.getPath();

        return getParams(m, baseDir);
    }

    /**
     * Get Connect params.
     * <p>
     * This is used to smooth any relative path kind of issues for any
     * file URLS. This code should be expanded to deal with any other context
     * sensitve isses dataStores tend to have.
     * </p>
     */
    protected static Map getParams(Map m, String baseDir) {
        return DataStoreInfo.getParams(m, baseDir);
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
        for (Iterator i = DataStoreFinder.getAvailableDataStores(); i.hasNext();) {
            DataStoreFactorySpi factory = (DataStoreFactorySpi) i.next();

            if (factory.canProcess(params)) {
                return factory;
            }
        }

        return null;
    }

    /**
     * After user has selected Description can aquire Factory based on
     * display name.
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
     * @param diplayName
     *
     * @return
     */
    public static DataStoreFactorySpi aquireFactory(String displayName) {
        for (Iterator i = DataStoreFinder.getAvailableDataStores(); i.hasNext();) {
            DataStoreFactorySpi factory = (DataStoreFactorySpi) i.next();

            if (factory.getDisplayName().equals(displayName)) {
                return factory;
            }

            if (factory.getClass().toString().equals(displayName)) {
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

        for (Iterator i = DataStoreFinder.getAvailableDataStores(); i.hasNext();) {
            DataStoreFactorySpi factory = (DataStoreFactorySpi) i.next();
            list.add(factory.getDisplayName());
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
            Param param = params[i];
            String key = param.key;
            String value = null;

            //if (param.required ) {
            if (param.sample != null) {
                // Required params may have nice sample values
                //
                value = param.text(param.sample);
            }

            if (value == null) {
                // or not
                value = "";
            }

            //}
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

    /**
     * @deprecated use {@link org.geoserver.feature.FeatureSourceUtils#
     */
    public static Envelope getBoundingBoxEnvelope(FeatureSource fs)
        throws IOException {
        return FeatureSourceUtils.getBoundingBoxEnvelope(fs);
    }
}

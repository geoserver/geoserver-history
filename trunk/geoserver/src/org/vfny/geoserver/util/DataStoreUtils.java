/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.DataStoreFactorySpi.Param;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.DataStoreInfo;
import org.vfny.geoserver.global.GeoserverDataDirectory;

import com.vividsolutions.jts.geom.Envelope;

/**
 * A collection of utilties for dealing with GeotTools DataStore.
 * 
 * @author Richard Gould, Refractions Research, Inc.
 * @author Simone Giannecchini
 * @author $Author: cholmesny $ (last modification)
 * @version $Id: DataStoreUtils.java,v 1.12 2004/09/21 21:14:48 cholmesny Exp $
 */
public final class DataStoreUtils {
	private final static Logger LOGGER = Logger.getLogger(DataStoreUtils.class
			.toString());

	private DataStoreUtils() {
	}

	public static DataStore acquireDataStore(Map params, ServletContext sc)
			throws IOException {
		// DJB: changed this for geoserver_data_dir
		// String baseDir = sc.getRealPath("/");
		final File baseDir = GeoserverDataDirectory
				.getGeoserverDataDirectory(sc);
		final DataStore store = DataStoreFinder.getDataStore(getParams(params,
				baseDir.getAbsolutePath()));
		if (store == null) {
			// TODO: this should throw an exception, but the classes using
			// this class aren't ready to actually get it...
			return null;
		} else {
			return store;
		}
	}

	public static Map getParams(Map m, ServletContext sc) {
		String baseDir = sc.getRealPath("/");
		return getParams(m, baseDir);
	}

	/**
	 * Get Connect params.
	 * <p>
	 * This is used to smooth any relative path kind of issues for any file
	 * URLS. This code should be expanded to deal with any other context
	 * sensitve isses dataStores tend to have.
	 * </p>
	 */
	public static Map getParams(Map m, String baseDir) {
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
		DataStoreFactorySpi factory;
		for (Iterator i = DataStoreFinder.getAvailableDataStores(); i.hasNext();) {
			factory = (DataStoreFactorySpi) i.next();

			if (factory.canProcess(params)) {
				return factory;
			}
		}

		return null;
	}

	/**
	 * After user has selected Description can aquire GDSFactory based on
	 * display name.
	 * 
	 * <p>
	 * Use factory for:
	 * </p>
	 * 
	 * <ul>
	 * <li> List of Params (attrb name, help text) </li>
	 * <li> Checking user's input with factory.canProcess( params ) </li>
	 * </ul>
	 * 
	 * 
	 * @param diplayName
	 * 
	 * @return
	 */
	public static DataStoreFactorySpi aquireFactory(String displayName) {
		DataStoreFactorySpi factory;
		for (Iterator i = DataStoreFinder.getAvailableDataStores(); i.hasNext();) {
			factory = (DataStoreFactorySpi) i.next();

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
	 * @param factory
	 *            DOCUMENT ME!
	 * @param key
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public static Param find(DataStoreFactorySpi factory, String key) {
		return find(factory.getParametersInfo(), key);
	}

	/**
	 * Utility methods for find param by key
	 * 
	 * @param params
	 *            DOCUMENT ME!
	 * @param key
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public static Param find(Param[] params, String key) {
		final int length = params.length;
		for (int i = 0; i < length; i++) {
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
		final List list = new ArrayList();
		DataStoreFactorySpi factory;
		for (Iterator i = DataStoreFinder.getAvailableDataStores(); i.hasNext();) {
			factory = (DataStoreFactorySpi) i.next();
			list.add(factory.getDisplayName());
		}

		return list;
	}

	public static Map defaultParams(String description) {
		return defaultParams(aquireFactory(description));
	}

	public static Map defaultParams(DataStoreFactorySpi factory) {
		final Map defaults = new HashMap();
		final Param[] params = factory.getParametersInfo();
		final int length = params.length;
		Param param;
		String key;
		String value = null;
		for (int i = 0; i < length; i++) {
			param = params[i];
			key = param.key;

			// if (param.required ) {
			if (param.sample != null) {
				// Required params may have nice sample values
				//
				value = param.text(param.sample);
			}
			if (value == null) {
				// or not
				value = "";
			}
			// }
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
	 * @return Map with real values that may be acceptable to GDSFactory
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	public static Map toConnectionParams(DataStoreFactorySpi factory, Map params)
			throws IOException {
		final Map map = new HashMap(params.size());
		final Param[] info = factory.getParametersInfo();
		String key;
		Object value;
		// Convert Params into the kind of Map we actually need
		for (Iterator i = params.keySet().iterator(); i.hasNext();) {
			key = (String) i.next();
			value = find(info, key).lookUp(params);

			if (value != null) {
				map.put(key, value);
			}
		}

		return map;
	}

	/**
	 * Get the envelope of this feature source. It uses a
	 * <code>ReferencedEnvelope</code> internally.
	 * 
	 * @param fs
	 * @return
	 * @throws IOException
	 */
	public static Envelope getBoundingBoxEnvelope(FeatureSource fs)
			throws IOException {
		Envelope ev = fs.getBounds();
		final GeometryAttributeType defaultGeometry = fs.getSchema()
				.getDefaultGeometry();
		final CoordinateReferenceSystem crs;
		if (defaultGeometry == null) {
			LOGGER
					.info("Unable to find a crs for this feature source, adopting WGS84");
			crs = DefaultGeographicCRS.WGS84;
		} else
			crs = defaultGeometry.getCoordinateSystem();
		if (ev == null || ev.isNull()) {
			try {
				ev = fs.getFeatures().getBounds();
			} catch (Throwable t) {
				ev = new Envelope();
			}
		}
		return new ReferencedEnvelope(ev, crs);
	}
}

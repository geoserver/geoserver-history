/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.geotools.data.coverage.grid.GridFormatFactorySpi;
import org.geotools.data.coverage.grid.GridFormatFinder;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.spatialschema.geometry.Envelope;
import org.vfny.geoserver.global.FormatInfo;

/**
 * A collection of utilties for dealing with GeotTools Format.
 * 
 * @author Richard Gould, Refractions Research, Inc.
 * @author cholmesny
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 * @version $Id: DataFormatUtils.java,v 1.12 2004/09/21 21:14:48 cholmesny Exp $
 */
public abstract class DataFormatUtils {
	public static Format acquireFormat(String type, ServletContext sc)
			throws IOException {
		Format[] formats = GridFormatFinder.getFormatArray();
		Format format = null;
		final int length = formats.length;
		for (int i = 0; i < length; i++) {
			if (formats[i].getName().equals(type)) {
				format = formats[i];
				break;
			}
		}

		if (format == null) {
			throw new IOException("Cannot handle format: " + type);
		} else {
			return format;
		}
	}

	public static Map getParams(Map m, ServletContext sc) {
		String baseDir = sc.getRealPath("/");
		return Collections.synchronizedMap(getParams(m, baseDir));
	}

	/**
	 * Get Connect params.
	 */
	public static Map getParams(Map m, String baseDir) {
		return Collections.synchronizedMap(FormatInfo.getParams(m, baseDir));
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
	public static ParameterValue find(Format format, String key) {
		return find(format.getReadParameters(), key);
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
	public static ParameterValue find(ParameterValueGroup params, String key) {
		List list = params.values();
		Iterator it = list.iterator();
		ParameterDescriptor descr;
		ParameterValue val;
		while (it.hasNext()) {
			val = (ParameterValue) it.next();
			descr = (ParameterDescriptor) val.getDescriptor();
			if (key.equalsIgnoreCase(descr.getName().toString())) {
				return val;
			}
		}

		return null;
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
	public static Format aquireFactory(Map params, String type) {
		Format[] formats = GridFormatFinder.getFormatArray();
		Format format = null;
		for (int i = 0; i < formats.length; i++) {
			format = formats[i];
			if (format.getName().equals(type))
				return format;
		}

		return null;
	}

	/**
	 * After user has selected Description can aquire Format based on
	 * description.
	 * 
	 * @param description
	 * 
	 * @return
	 */
	public static Format aquireFactory(String description) {
		Format[] formats = GridFormatFinder.getFormatArray();
		Format format = null;
		final int length = formats.length;
		for (int i = 0; i < length; i++) {
			format = formats[i];
			if (format.getDescription().equals(description))
				return format;
		}

		return null;
	}

	/**
	 * Returns the descriptions for the available DataFormats.
	 * 
	 * <p>
	 * Arrrg! Put these in the select box.
	 * </p>
	 * 
	 * @return Descriptions for user to choose from
	 */
	public static List listDataFormatsDescriptions() {
		List list = new ArrayList();
		Format[] formats = GridFormatFinder.getFormatArray();

		final int length = formats.length;
		for (int i = 0; i < length; i++) {
			if (!list.contains(formats[i].getDescription())) {
				list.add(formats[i].getDescription());
			}
		}

		return Collections.synchronizedList(list);
	}

	public static List listDataFormats() {
		List list = new ArrayList();
		Format[] formats = GridFormatFinder.getFormatArray();
		final int length = formats.length;
		for (int i = 0; i < length; i++) {
			if (!list.contains(formats[i])) {
				list.add(formats[i]);
			}
		}

		return Collections.synchronizedList(list);
	}

	public static Map defaultParams(String description) {
		return Collections
				.synchronizedMap(defaultParams(aquireFactory(description)));
	}

	public static Map defaultParams(Format factory) {
		Map defaults = new HashMap();
		ParameterValueGroup params = factory.getReadParameters();

		if (params != null) {
			List list = params.values();
			Iterator it = list.iterator();
			ParameterDescriptor descr = null;
			ParameterValue val = null;
			while (it.hasNext()) {
				val = (ParameterValue) it.next();
				descr = (ParameterDescriptor) val.getDescriptor();

				String key = descr.getName().toString();
				Object value = null;

				if (val.getValue() != null) {
					// Required params may have nice sample values
					//
					if ("values_palette".equalsIgnoreCase(key))
						value = val.getValue();
					else
						value = val.getValue().toString();
				}
				if (value == null) {
					// or not
					value = "";
				}
				if (value != null) {
					defaults.put(key, value);
				}
			}
		}

		return Collections.synchronizedMap(defaults);
	}

	/**
	 * Convert map to real values based on factory Params.
	 * 
	 * @param factory
	 * @param params
	 * 
	 * @return Map with real values that may be acceptable to Factory
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	public static Map toParams(GridFormatFactorySpi factory, Map params)
			throws IOException {
		Map map = new HashMap(params.size());

		ParameterValueGroup info = factory.createFormat().getReadParameters();

		// Convert Params into the kind of Map we actually need
		for (Iterator i = params.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();

			Object value = find(info, key).getValue();

			if (value != null) {
				map.put(key, value);
			}
		}

		return Collections.synchronizedMap(map);
	}

	public static Envelope getBoundingBoxEnvelope(GridCoverage gc)
			throws IOException {
		Envelope ev = gc.getEnvelope();

		return ev;
	}

	/**
	 * @param sourceCRS
	 * @param targetEnvelope
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public static com.vividsolutions.jts.geom.Envelope adjustEnvelope(
			final CoordinateReferenceSystem sourceCRS,
			GeneralEnvelope targetEnvelope) throws IndexOutOfBoundsException {
		final CoordinateSystem cs = sourceCRS.getCoordinateSystem();
		boolean lonFirst = true;
		if (cs.getAxis(0).getDirection().absolute().equals(AxisDirection.NORTH)) {
			lonFirst = false;
		}
		boolean swapXY = !lonFirst;

		// latitude index
		final int latIndex = lonFirst ? 1 : 0;

		final AxisDirection latitude = cs.getAxis(latIndex).getDirection();
		final AxisDirection longitude = cs.getAxis((latIndex + 1) % 2)
				.getDirection();
		final boolean[] reverse = new boolean[] {
				lonFirst ? !longitude.equals(AxisDirection.EAST) : !latitude
						.equals(AxisDirection.NORTH),
				lonFirst ? !latitude.equals(AxisDirection.NORTH) : !longitude
						.equals(AxisDirection.EAST) };

		com.vividsolutions.jts.geom.Envelope envelope = new com.vividsolutions.jts.geom.Envelope();
		envelope.init(reverse[(latIndex + 1) % 2] ? targetEnvelope
				.getUpperCorner().getOrdinate(swapXY ? 1 : 0) : targetEnvelope
				.getLowerCorner().getOrdinate(swapXY ? 1 : 0),
				reverse[(latIndex + 1) % 2] ? targetEnvelope.getLowerCorner()
						.getOrdinate(swapXY ? 1 : 0) : targetEnvelope
						.getUpperCorner().getOrdinate(swapXY ? 1 : 0),
				reverse[latIndex] ? targetEnvelope.getUpperCorner()
						.getOrdinate(swapXY ? 0 : 1) : targetEnvelope
						.getLowerCorner().getOrdinate(swapXY ? 0 : 1),
				reverse[latIndex] ? targetEnvelope.getLowerCorner()
						.getOrdinate(swapXY ? 0 : 1) : targetEnvelope
						.getUpperCorner().getOrdinate(swapXY ? 0 : 1));
		return envelope;
	}
}

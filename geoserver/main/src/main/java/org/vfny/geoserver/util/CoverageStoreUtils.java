/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.util;

import org.geotools.data.coverage.grid.GridFormatFactorySpi;
import org.geotools.data.coverage.grid.GridFormatFinder;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.CRSUtilities;
import org.opengis.coverage.grid.Format;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.vfny.geoserver.global.CoverageStoreInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.ServletContext;


/**
 * A collection of utilties for dealing with GeotTools Format.
 *
 * @author Richard Gould, Refractions Research, Inc.
 * @author cholmesny
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 * @version $Id: CoverageStoreUtils.java,v 1.12 2004/09/21 21:14:48 cholmesny
 *          Exp $
 */
public final class CoverageStoreUtils {
    private final static Logger LOGGER = Logger.getLogger(CoverageStoreUtils.class.toString());
    public final static Format[] formats = GridFormatFinder.getFormatArray();

    private CoverageStoreUtils() {
    }

    public static Format acquireFormat(String type, ServletContext sc)
        throws IOException {
        final Format[] formats = GridFormatFinder.getFormatArray();

        for (int i = 0; i < formats.length; i++) {
            final Format format = formats[i];

            if (formats[i].getName().equals(type)) {
                return format;
            }
        }

        throw new IOException("Cannot handle format: " + type);
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

        while (it.hasNext()) {
            final ParameterValue val = (ParameterValue) it.next();
            final ParameterDescriptor descr = (ParameterDescriptor) val.getDescriptor();

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
     * @return
     */
    public static Format aquireFactoryByType(String type) {
        final Format[] formats = GridFormatFinder.getFormatArray();

        for (int i = 0; i < formats.length; i++) {
            final Format format = formats[i];

            if (format.getName().equals(type)) {
                return format;
            }
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
        final Format[] formats = GridFormatFinder.getFormatArray();

        for (int i = 0; i < formats.length; i++) {
            final Format format = formats[i];

            if (format.getDescription().equals(description)) {
                return format;
            }
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
        final List list = new ArrayList(155);
        final Format[] formats = GridFormatFinder.getFormatArray();

        for (int i = 0; i < formats.length; i++) {
            final String desc = formats[i].getDescription();

            if (!list.contains(desc)) {
                list.add(desc);
            }
        }

        return Collections.synchronizedList(list);
    }

    public static List listDataFormats() {
        final List list = new ArrayList(15);
        final Format[] formats = GridFormatFinder.getFormatArray();

        for (int i = 0; i < formats.length; i++) {
            final Format format = formats[i];

            if (!list.contains(format)) {
                list.add(format);
            }
        }

        return Collections.synchronizedList(list);
    }

    public static Map defaultParams(String description) {
        return Collections.synchronizedMap(defaultParams(aquireFactory(description)));
    }

    public static Map defaultParams(Format factory) {
        Map defaults = new HashMap();
        ParameterValueGroup params = factory.getReadParameters();

        if (params != null) {
            final List list = params.values();
            final Iterator it = list.iterator();

            while (it.hasNext()) {
                final ParameterValue val = (ParameterValue) it.next();
                final ParameterDescriptor descr = (ParameterDescriptor) val.getDescriptor();

                final String key = descr.getName().toString();
                Object value = null;

                if (val.getValue() != null) {
                    // Required params may have nice sample values
                    //
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
     * @return Map with real values that may be acceptable to GDSFactory
     *
     * @throws IOException
     *             DOCUMENT ME!
     */
    public static Map toParams(GridFormatFactorySpi factory, Map params)
        throws IOException {
        final Map map = new HashMap(params.size());
        final ParameterValueGroup info = factory.createFormat().getReadParameters();

        // Convert Params into the kind of Map we actually need
        for (Iterator i = params.keySet().iterator(); i.hasNext();) {
            final String key = (String) i.next();
            final Object value = find(info, key).getValue();

            if (value != null) {
                map.put(key, value);
            }
        }

        return Collections.synchronizedMap(map);
    }

    /**
     * Retrieve a WGS84 lon,lat envelope from the provided one.
     *
     * @param sourceCRS
     * @param targetEnvelope
     * @return
     * @throws IndexOutOfBoundsException
     * @throws FactoryException
     * @throws TransformException
     */
    public static GeneralEnvelope getWGS84LonLatEnvelope(GeneralEnvelope envelope)
        throws IndexOutOfBoundsException, FactoryException, TransformException {
        final CoordinateReferenceSystem sourceCRS = envelope.getCoordinateReferenceSystem();

        ////
        //
        // Do we need to transform?
        //
        ////
        if (CRSUtilities.equalsIgnoreMetadata(sourceCRS, DefaultGeographicCRS.WGS84)) {
            return new GeneralEnvelope(envelope);
        }

        ////
        //
        //transform
        //
        ////
        final CoordinateReferenceSystem targetCRS = DefaultGeographicCRS.WGS84;
        final MathTransform mathTransform = CRS.findMathTransform(sourceCRS, targetCRS, true);
        final GeneralEnvelope targetEnvelope;

        if (!mathTransform.isIdentity()) {
            targetEnvelope = CRSUtilities.transform(mathTransform, envelope);
        } else {
            targetEnvelope = new GeneralEnvelope(envelope);
        }

        targetEnvelope.setCoordinateReferenceSystem(targetCRS);

        return targetEnvelope;
    }
}

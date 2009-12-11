/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.data.util;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GeneralGridGeometry;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.BufferedCoordinateOperationFactory;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationNotFoundException;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@geo-solutions.it)
 * @author $Author: Simone Giannecchini (simone.giannecchini@geo-solutions.it)
 */
public class CoverageUtils {

    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CoverageUtils.class.toString());
    public static final int TRANSPARENT = 0;
    public static final int OPAQUE = 1;
    
    public static GeneralParameterValue[] getParameters(ParameterValueGroup params) {
        final List parameters = new ArrayList();
        final String readGeometryKey = AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString();

        if ((params != null) && (params.values().size() > 0)) {
            List list = params.values();
            final Iterator it = list.iterator();
            while (it.hasNext()) {
                final ParameterValue val = (ParameterValue) it.next();

                if (val != null) {
                    final ParameterDescriptor descr = (ParameterDescriptor) val.getDescriptor();
                    final String _key = descr.getName().toString();

                    if ("namespace".equals(_key)) {
                        // skip namespace as it is *magic* and
                        // appears to be an entry used in all dataformats?
                        //
                        continue;
                    }


                    // IGNORING READ_GRIDGEOMETRY2D param
                    if (_key.equalsIgnoreCase(readGeometryKey)) {
                        continue;
                    }
                    final Object value = val.getValue();

                    parameters.add(new DefaultParameterDescriptor(_key, value.getClass(), null,value).createValue());
                }
            }

            return (!parameters.isEmpty())
            ? (GeneralParameterValue[]) parameters.toArray(new GeneralParameterValue[parameters.size()])
            : null;
        } else {
            return null;
        }
    }

    public static GeneralParameterValue[] getParameters(ParameterValueGroup params, Map values) {
        return getParameters(params, values, false);
    }

    public static GeneralParameterValue[] getParameters(ParameterValueGroup params, Map values,
        boolean readGeom) {
        final List<ParameterValue<?>> parameters = new ArrayList<ParameterValue<?>>();
        final String readGeometryKey = AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString();

        if ((params != null) && (params.values().size() > 0)) {
            final List<GeneralParameterValue> elements = params.values();
            for (GeneralParameterValue elem: elements) {
                final ParameterValue<?> val = (ParameterValue<?>)elem;

                if (val != null) {
                    final ParameterDescriptor<?> descr = val.getDescriptor();
                    final String _key = descr.getName().toString();

                    if ("namespace".equals(_key)) {
                        // skip namespace as it is *magic* and
                        // appears to be an entry used in all dataformats?
                        //
                        continue;
                    }

                    // /////////////////////////////////////////////////////////
                    //
                    // request param for better management of coverage
                    //
                    // /////////////////////////////////////////////////////////
                    if (_key.equalsIgnoreCase(readGeometryKey) && !readGeom) {
                        // IGNORING READ_GRIDGEOMETRY2D param
                        continue;
                    }

                    // /////////////////////////////////////////////////////////
                    //
                    // format specific params
                    //
                    // /////////////////////////////////////////////////////////
                    final Object value = CoverageUtils.getCvParamValue(_key, val, values);
                    parameters.add(new DefaultParameterDescriptor(_key, descr.getValueClass(), null, value).createValue());
                    
                }
            }

            return (!parameters.isEmpty())? 
            		(GeneralParameterValue[]) parameters.toArray(new GeneralParameterValue[parameters.size()]): null;
        } else {
            return null;
        }
    }

    public static Map getParametersKVP(ParameterValueGroup params) {
        final Map parameters = new HashMap();
        final String readGeometryKey = AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString();

        if ((params != null) && (params.values().size() > 0)) {
            final List list = params.values();
            final Iterator it = list.iterator();
            while (it.hasNext()) {
                final ParameterValue val = (ParameterValue) it.next();

                if (val != null) {
                    final ParameterDescriptor descr = (ParameterDescriptor) val.getDescriptor();

                    final String _key = descr.getName().toString();

                    if ("namespace".equals(_key)) {
                        // skip namespace as it is *magic* and
                        // appears to be an entry used in all dataformats?
                        //
                        continue;
                    }

                    // /////////////////////////////////////////////////////////
                    //
                    // request param for better management of coverage
                    //
                    // /////////////////////////////////////////////////////////
                    if (_key.equalsIgnoreCase(readGeometryKey)) {
                        // IGNORING READ_GRIDGEOMETRY2D param
                        continue;
                    }

                    Object value = val.getValue();
                    String text = "";

                    if (value == null) {
                        text = null;
                    } else if (value instanceof String) {
                        text = (String) value;
                    } else {
                        text = value.toString();
                    }

                    parameters.put(_key, (text != null) ? text : "");
                }
            }

            return parameters;
        } else {
            return parameters;
        }
    }

    /**
     * @param paramValues
     * @param key
     * @param param
     * @return
     */
    public static Object getCvParamValue(final String key, ParameterValue param,
        final List paramValues, final int index) {
        Object value = null;

        try {
            if (key.equalsIgnoreCase("crs")) {
                if ((getParamValue(paramValues, index) != null)
                        && (((String) getParamValue(paramValues, index)).length() > 0)) {
                    if ((paramValues.get(index) != null)
                            && (((String) paramValues.get(index)).length() > 0)) {
                        value = CRS.parseWKT((String) paramValues.get(index));
                    }
                } else {
                    LOGGER.info("Unable to find a crs for the coverage param, using EPSG:4326");
                    value = CRS.decode("EPSG:4326");
                }
            } else if (key.equalsIgnoreCase("envelope")) {
                if ((getParamValue(paramValues, index) != null)
                        && (((String) getParamValue(paramValues, index)).length() > 0)) {
                    String tmp = (String) getParamValue(paramValues, index);

                    if ((tmp.indexOf("[") > 0) && (tmp.indexOf("]") > tmp.indexOf("["))) {
                        tmp = tmp.substring(tmp.indexOf("[") + 1, tmp.indexOf("]")).trim();
                        tmp = tmp.replaceAll(",", "");

                        String[] strCoords = tmp.split(" ");
                        double[] coords = new double[strCoords.length];

                        if (strCoords.length == 4) {
                            for (int iT = 0; iT < 4; iT++) {
                                coords[iT] = Double.parseDouble(strCoords[iT].trim());
                            }

                            value = (org.opengis.geometry.Envelope) new GeneralEnvelope(new double[] {
                                        coords[0], coords[1]
                                    }, new double[] { coords[2], coords[3] });
                        }
                    }
                }
            } else {
                Class[] clArray = { getParamValue(paramValues, index).getClass() };
                Object[] inArray = { getParamValue(paramValues, index) };
                value = param.getValue().getClass().getConstructor(clArray).newInstance(inArray);
            }

            // Intentionally generic exception catched
        } catch (Exception e) {
            value = null;

            // errors.add("paramValue[" + i + "]",
            // new ActionError("error.dataFormatEditor.param.parse", key,
            // getParamValue(i).getClass(), e));
        }

        return value;
    }

    private static String getParamValue(final List paramValues, final int index) {
        return (String) paramValues.get(index);
    }

    /**
     * @param params
     * @param key
     * @param param
     * @return
     */
    public static Object getCvParamValue(final String key, ParameterValue param, final Map params) {
        Object value = null;

        try {
            if (key.equalsIgnoreCase("crs")) {
                if ((params.get(key) != null) && (((String) params.get(key)).length() > 0)) {
                    value = CRS.parseWKT((String) params.get(key));
                } else {
                    LOGGER.info("Unable to find a crs for the coverage param, using EPSG:4326");
                    value = CRS.decode("EPSG:4326");
                }
            } else if (key.equalsIgnoreCase("envelope")) {
                if ((params.get(key) != null) && (((String) params.get(key)).length() > 0)) {
                    String tmp = (String) params.get(key);

                    if ((tmp.indexOf("[") > 0) && (tmp.indexOf("]") > tmp.indexOf("["))) {
                        tmp = tmp.substring(tmp.indexOf("[") + 1, tmp.indexOf("]")).trim();
                        tmp = tmp.replaceAll(",", "");

                        String[] strCoords = tmp.split(" ");
                        double[] coords = new double[strCoords.length];

                        if (strCoords.length == 4) {
                            for (int iT = 0; iT < 4; iT++) {
                                coords[iT] = Double.parseDouble(strCoords[iT].trim());
                            }

                            value = (org.opengis.geometry.Envelope) new GeneralEnvelope(new double[] {
                                        coords[0], coords[1]
                                    }, new double[] { coords[2], coords[3] });
                        }
                    }
                }
            } else if (key.equalsIgnoreCase(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName()
                                                                                      .toString())) {
                if ((params.get(key) != null) && params.get(key) instanceof String
                        && (((String) params.get(key)).length() > 0)) {
                    String tmp = (String) params.get(key);

                    if ((tmp.indexOf("[") > 0) && (tmp.indexOf("]") > tmp.indexOf("["))) {
                        tmp = tmp.substring(tmp.indexOf("[") + 1, tmp.indexOf("]")).trim();
                        tmp = tmp.replaceAll(",", "");

                        String[] strCoords = tmp.split(" ");
                        double[] coords = new double[strCoords.length];

                        if (strCoords.length == 4) {
                            for (int iT = 0; iT < 4; iT++) {
                                coords[iT] = Double.parseDouble(strCoords[iT].trim());
                            }

                            value = (org.opengis.geometry.Envelope) new GeneralEnvelope(new double[] {
                                        coords[0], coords[1]
                                    }, new double[] { coords[2], coords[3] });
                        }
                    }
                } else if ((params.get(key) != null)
                        && params.get(key) instanceof GeneralGridGeometry) {
                    value = params.get(key);
                }
            } else if (key.equalsIgnoreCase("InputTransparentColor")
                    || key.equalsIgnoreCase("OutputTransparentColor")) {
                if (params.get(key) != null) {
                    value = Color.decode((String) params.get(key));
                } else {
                    Class[] clArray = { Color.class };
                    Object[] inArray = { params.get(key) };
                    value = param.getValue().getClass().getConstructor(clArray).newInstance(inArray);
                }
            }else if (key.equalsIgnoreCase("BackgroundValues")) {
                if (params.get(key) != null) {
                    String temp = (String) params.get(key);
                    String[] elements = temp.split(",");
                    final double[] backgroundValues = new double[elements.length];
                    for(int i=0;i<elements.length;i++)
                    	backgroundValues[i]=Double.valueOf(elements[i]);
                    value=backgroundValues;
                    
                } 
            } 
            else if (key.equalsIgnoreCase("InputImageThresholdValue")) {
                if (params.get(key) != null) {
                    String temp = (String) params.get(key);
                    value=Double.valueOf(temp);
                    
                } 
            } 
            else {
                Class[] clArray = { String.class };
                Object[] inArray = { params.get(key) };
                value = param.getValue().getClass().getConstructor(clArray).newInstance(inArray);
            }
        } catch (Exception e) {
            value = param.getValue();
        }

        return value;
    }
     
}
/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 * @author lreed@refractions.net
 */

package org.geoserver.wps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.opengis.wps.DataType;
import net.opengis.wps.InputReferenceType;
import net.opengis.wps.InputType;
import net.opengis.wps.LiteralDataType;

import com.vividsolutions.jts.geom.Geometry;

import org.geotools.data.Parameter;

import org.geoserver.wps.transmute.ComplexTransmuter;
import org.geoserver.wps.transmute.DoubleTransmuter;
import org.geoserver.wps.transmute.LiteralTransmuter;
import org.geoserver.wps.transmute.PolygonTransmuter;
import org.geoserver.wps.transmute.Transmuter;

public class DataTransformer
{
    private Map<Class<?>, Class<?>>     transmuters      = new HashMap<Class<?>,  Class<?>>();
    private Map<String,   Parameter<?>> inputParameters;

    public DataTransformer()
    {
        this.transmuters.put(Double.class,   DoubleTransmuter.class);
        this.transmuters.put(Geometry.class, PolygonTransmuter.class);
    }

    // Returns Map ready for execution.
    public Map<String, Object> decodeInputs(final List<InputType> inputs, final Map<String, Parameter<?>> parameters)
    {
        Map<String, Object> inputMap = new HashMap<String, Object>();

        this.inputParameters = parameters;

        for(InputType input : inputs)
        {
            String identifier = input.getIdentifier().getValue();

            if (null != input.getData())
            {
                // Parse Data into java object
                inputMap.put(identifier, this.decodeInputData(input));

                continue;
            }

            if (null != input.getReference())
            {
                // Fetch external resource
                inputMap.put(identifier, this.decodeReferenceData(identifier, input.getReference()));
            }
        }

        return inputMap;
    }

    // We will assume that all external reference data is complex
    private Object decodeReferenceData(final String identifier, final InputReferenceType reference)
    {
        Object            data       = null;
        Parameter<?>      param      = this.inputParameters.get(identifier);
        URL               url        = null;
        ComplexTransmuter transmuter = (ComplexTransmuter)this.getTransmuter(param.type);

        try
        {
            url = new URL(reference.getHref());
        } catch(MalformedURLException e) {
            throw new WPSException("NoApplicableCode", "Malformed parameter URL.");
        }

        try
        {
            data = transmuter.decode(url.openStream());
        } catch(IOException e) {
            throw new WPSException("NoApplicableCode", "IOException.");
        }

        return data;
    }

    private Object decodeInputData(final InputType input)
    {
        Object   output = null;
        DataType data   = input.getData();

        if (null != data.getLiteralData())
        {
            String       parameterName = input.getIdentifier().getValue();
            Parameter<?> parameter     = this.inputParameters.get(parameterName);
            output                     = this.decodeLiteralData(data.getLiteralData(), parameter.type);
        }

        if (null != data.getComplexData())
        {
            // Parse complex data
        }

        if (null != data.getBoundingBoxData())
        {
            // Parse bounding box data
        }

        return output;
    }

    private Object decodeLiteralData(final LiteralDataType input, final Class<?> type)
    {
        Object data = null;

        LiteralTransmuter transmuter = (LiteralTransmuter)this.getTransmuter(type);

        data = transmuter.decode(input.getValue());

        return data;
    }

    // Encode process results for transmission back to client.
    public Object encodeOutputs(final Map<String, Object> outputs, final Map<String, Parameter<?>> resultInfo)
    {
        Object obj = null;

        Class<?> transmuter;

        for(String name : outputs.keySet())
        {
            transmuter = this.transmuters.get(this.getParameterType(name, resultInfo));

            if (null == transmuter)
            {
                throw new WPSException("NoApplicableCode", "XXX");
            }
        }

        return obj;
    }

    // Given a parameter name and the man in which it is defined, return the Class used to represent its data.
    private Class<?> getParameterType(final String paramName, final Map<String, Parameter<?>> parameters)
    {
        Parameter<?> parameter = parameters.get(paramName);

        if (null == parameter)
        {
            throw new WPSException("NoApplicableCode", "No input or output parameter '" + paramName + "'.");
        }

        return parameter.getClass();
    }

    // Return a transmuter for a given Java type
    public Transmuter getTransmuter(Class<?> type)
    {
        Class<?> transmuterClass = this.transmuters.get(type);

        if (null == transmuterClass)
        {
            throw new WPSException("NoApplicableCode", "No transmuter registered for type " + type.toString() + "'.");
        }

        Transmuter transmuter;

        try
        {
            transmuter = (Transmuter)transmuterClass.getConstructor().newInstance();
        } catch(Exception e) {
            throw new WPSException("NoApplicableCode", "Could not instantiate transmuter.");
        }

        return transmuter;
    }
}
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.opengis.wps.ComplexDataType;
import net.opengis.wps.DataType;
import net.opengis.wps.InputReferenceType;
import net.opengis.wps.InputType;
import net.opengis.wps.LiteralDataType;

import com.vividsolutions.jts.geom.Geometry;

import org.geotools.data.Parameter;

import org.geoserver.wps.transmute.ComplexTransmuter;
import org.geoserver.wps.transmute.DoubleTransmuter;
import org.geoserver.wps.transmute.LiteralTransmuter;
import org.geoserver.wps.transmute.PolygonGML2Transmuter;
import org.geoserver.wps.transmute.Transmuter;

public class DataTransformer
{
    private List<Transmuter>            transmuters        = new ArrayList<Transmuter>();
    private Map<Class<?>, Transmuter>   defaultTransmuters = new HashMap<Class<?>, Transmuter>();
    private Map<String,   Parameter<?>> inputParameters;

    public DataTransformer()
    {
        this.defaultTransmuters.put(Double.class,   new DoubleTransmuter());
        this.defaultTransmuters.put(Geometry.class, new PolygonGML2Transmuter());

        // Add all default transmuters to master transmuters list
        this.transmuters.addAll(this.defaultTransmuters.values());
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
        ComplexTransmuter transmuter = (ComplexTransmuter)this.getDefaultTransmuter(param.type);

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

        String       parameterName = input.getIdentifier().getValue();
        Parameter<?> parameter     = this.inputParameters.get(parameterName);

        if (null != data.getLiteralData())
        {
            output = this.decodeLiteralData(data.getLiteralData(), parameter.type);
        }

        if (null != data.getComplexData())
        {
            output = this.decodeComplexData(data.getComplexData(), parameter.type);
        }

        if (null != data.getBoundingBoxData())
        {
            // Parse bounding box data
        }

        return output;
    }

    private Object decodeComplexData(final ComplexDataType input, final Class<?> type)
    {
        Object data   = null;

        ComplexTransmuter transmuter = (ComplexTransmuter)this.getComplexTransmuter(type, input.getSchema());

        // XXX get data to parse
        Object feature0 = input.getMixed().getValue(0);

        //data = transmuter.decode(XXX);

        return data;
    }

    private Object decodeLiteralData(final LiteralDataType input, final Class<?> type)
    {
        Object data = null;

        LiteralTransmuter transmuter = (LiteralTransmuter)this.getDefaultTransmuter(type);

        data = transmuter.decode(input.getValue());

        return data;
    }

    // Encode process results for transmission back to client.
    public Map<String, Object> encodeOutputs(final Map<String, Object> outputs, final Map<String, Parameter<?>> resultInfo)
    {
        Map<String, Object> encoded = new HashMap<String, Object>();

        Class<?> transmuter;

        for(String name : outputs.keySet())
        {
            //transmuter = this.transmuters.get(this.getParameterType(name, resultInfo));

            //if (null == transmuter)
            //{
            //    throw new WPSException("NoApplicableCode", "XXX");
            //}
        }

        return encoded;
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

    public ComplexTransmuter getComplexTransmuter(final Class<?> type, final String schema)
    {
        for(Transmuter transmuter : this.transmuters)
        {
            if (false == transmuter instanceof ComplexTransmuter)
            {
                continue;
            }

            if (false == ((ComplexTransmuter)transmuter).getSchema().equals(schema))
            {
                continue;
            }

            if (type != transmuter.getType())
            {
                continue;
            }

            return (ComplexTransmuter)transmuter;
        }

        throw new WPSException("NoApplicableCode", "Could not find ComplexTransmuter for '" + schema + "'.");
    }

    // Return default a transmuter for a given Java type
    public Transmuter getDefaultTransmuter(final Class<?> type)
    {
        Transmuter transmuter = this.defaultTransmuters.get(type);

        if (null == transmuter)
        {
            throw new WPSException("NoApplicableCode", "No transmuter default registered for type " + type.toString() + "'.");
        }

        return transmuter;
    }
}
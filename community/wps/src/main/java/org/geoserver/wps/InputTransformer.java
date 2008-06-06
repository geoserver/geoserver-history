/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 * @author lreed@refractions.net
 */

package org.geoserver.wps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.process.Parameter;

import net.opengis.wps.DataType;
import net.opengis.wps.InputType;
import net.opengis.wps.InputReferenceType;
import net.opengis.wps.LiteralDataType;

public class InputTransformer
{
	private List<InputType>           inputs;
	private Map<String, Parameter<?>> parameters;

	public InputTransformer(final List<InputType> inputs, final Map<String, Parameter<?>> parameterInfo)
	{
		this.inputs     = inputs;
		this.parameters = parameterInfo;
	}

	public Map<String, Object> transform()
	{
		Map<String, Object> inputMap = new HashMap<String, Object>();

		for(InputType input : inputs)
		{
			if (null != input.getData())
			{
				// Parse Data into java object
				inputMap.put(input.getIdentifier().getValue(), this.parseInputData(input));

				continue;
			}

			if (null != input.getReference())
			{
				// Fetch external resource
				inputMap.put(input.getIdentifier().getValue(), this.parseReferenceData(input.getReference()));
			}
		}

		return inputMap;
	}

	private Parameter<?> getParameter(final String name)
	{
		return this.parameters.get(name);
	}
	
	private Object parseInputData(final InputType input)
	{
		Object output = null;

		DataType data = input.getData();

		if (null != data.getLiteralData())
		{
			String parameterName = input.getIdentifier().getValue();
			Parameter<?> parameter = this.getParameter(parameterName);
			output = this.parseLiteralData(data.getLiteralData(), parameter.type);
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

	private Object parseReferenceData(final InputReferenceType reference)
	{
		Object data = null;

		// XXX
		
		return data;
	}

	/*
		Infer type from process parameter.
	*/
	private Object parseLiteralData(final LiteralDataType input, final Class<?> type)
	{
		Object data = null;

		if (null == input.getDataType())
		{
			data = this.inferType(input.getValue(), type);
		} else {
			// More complex type
		}

		return data;
	}

	private Object inferType(final String value, final Class<?> type)
	{
		Object data = value;

		label: try
		{
			if (Integer.class == type)
			{
				data = Integer.getInteger(value);

				break label;
			}

			if (Float.class == type)
			{
				data = Float.valueOf(value);

				break label;
			}

			if (Double.class == type)
			{
				data = Double.valueOf(value);

				break label;
			}

			if (String.class == type)
			{
				break label;
			}

			throw new WPSException("");
		} catch(Exception e) {
			throw new WPSException("InvalidParameterType", "Could not convert paramter to object.");
		}

		return data;
	}
}
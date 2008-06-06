/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 * @author lreed@refractions.net
 */

package org.geoserver.wps;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.process.Process;
import org.geotools.process.Parameter;
import org.opengis.util.ProgressListener;

import net.opengis.wps.ExecuteType;
import net.opengis.wps.DataInputsType1;
import net.opengis.wps.InputType;
import net.opengis.wps.DataType;
import net.opengis.ows11.CodeType;

public class Executor
{
	private Process             process;
	private Map<String, Object> inputs;
	
	public Executor(ExecuteType request)
	{
		CodeType       identifier = request.getIdentifier();
		ProcessFactory factory    = this.findProcessFactory(identifier);

		if (null == factory)
		{
			throw new WPSException("InvalidParameterValue", "Identifier");	// TODO
		}

		// Check inputs
		Map<String, Parameter<?>> parameterInfo = factory.getParameterInfo();
		DataInputsType1           requestInputs = request.getDataInputs();

		this.checkInputs(parameterInfo, requestInputs);

		// Parse inputs
		InputTransformer inputTransformer = new InputTransformer(request.getDataInputs().getInput(), parameterInfo);
		this.inputs = inputTransformer.transform();

		// Get it ready to execute
		this.process = factory.create();
	}

	public Map<String, Object> execute()
	{
		ProgressListener progress = null;

		return process.execute(this.inputs, progress);
	}
	
	private void checkInputs(Map<String, Parameter<?>> processParameters, DataInputsType1 requestInputs)
	{
		List<String> requestInputNames = new ArrayList<String>();
		List<String> processInputNames = new ArrayList<String>();

		for(InputType input : (List<InputType>)requestInputs.getInput())
		{
			requestInputNames.add(input.getIdentifier().getValue());
		}

		processInputNames.addAll(processParameters.keySet());

		// Check for missing input parameters
		for(String processInputName : processInputNames)
		{
			if (false == requestInputNames.contains(processInputName))
			{
				throw new WPSException("MissingParameterValue", processInputName);
			}
		}

		requestInputNames.removeAll(processInputNames);

		// Check for unknown input types
		String unknownParameters = new String("");
		for(String unknownName : requestInputNames)
		{
			if (false == "".equals(unknownParameters))
			{
				unknownParameters += ", ";
			}

			unknownParameters += unknownName;
		}

		if (false == "".equals(unknownParameters))
		{
			throw new WPSException("NoApplicableCode", "Uknown input parameters: " + unknownParameters);
		}

		return;
	}

	private ProcessFactory findProcessFactory(CodeType name)
    {
        for(ProcessFactory pf : Processors.getProcessFactories())
        {
            if (pf.getName().equals(name.getValue()))
            {
                return pf;
            }
        }

        return null;
    }
}
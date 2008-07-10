/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.geotools.data.Parameter;
import org.geotools.process.Process;
import org.geotools.process.Processors;
import org.geotools.process.ProcessFactory;

import net.opengis.ows11.CodeType;
import net.opengis.wps.InputType;
import net.opengis.wps.ExecuteType;
import net.opengis.wps.DataInputsType1;
import org.opengis.util.ProgressListener;

/**
 * Executes the process defined in an Execute request
 *
 * @author Lucas Reed, Refractions Research Inc
 */
public class Executor {
    private Process             process;
    private Map<String, Object> inputs;
    private ProcessFactory      factory;

    /**
     * Checks and decodes the process inputs from the request
     * @param request
     * @param wps
     */
    public Executor(ExecuteType request, WPS wps) {
        CodeType identifier = request.getIdentifier();
        this.factory        = this.findProcessFactory(identifier);

        if (null == factory) {
            throw new WPSException("InvalidParameterValue", "Identifier");
        }

        // Check inputs
        Map<String, Parameter<?>> parameterInfo = factory.getParameterInfo();
        DataInputsType1           requestInputs = request.getDataInputs();

        this.checkInputs(parameterInfo, requestInputs);

        // Parse inputs
        DataTransformer dataTransformer = new DataTransformer(request.getBaseUrl());
        this.inputs = dataTransformer.decodeInputs(request.getDataInputs().getInput(), parameterInfo);

        // Get it ready to execute
        this.process = factory.create();
    }

    /**
     * Returns the ProcessFactory for the Execute request
     * @return
     */
    public ProcessFactory getProcessFactory() {
        return this.factory;
    }

    /**
     * Executes process and returns results as Java data types
     * @return
     */
    public Map<String, Object> execute() {
        ProgressListener progress = null;

        Map<String, Object> outputs = process.execute(this.inputs, progress);

        this.checkOutputs(outputs);

        return outputs;
    }

    /**
     * Partial output validation.
     * @param outputs
     */
    private void checkOutputs(Map<String, Object> outputs)
    {
    	Map<String, Parameter<?>> resultInfo = this.factory.getResultInfo(null);

    	for(String key : resultInfo.keySet())
    	{
    		if (0 != resultInfo.get(key).minOccurs)
    		{
    			if (null == outputs || false == outputs.containsKey(key))
    			{
    				throw new WPSException("NoApplicableCode", "Process returned null value where one is expected.");
    			}
    		}
    	}
    }

    /**
     * Checks request inputs against those of the requested process implementation
     * @param processParameters
     * @param requestInputs
     */
    private void checkInputs(Map<String, Parameter<?>> processParameters,
        DataInputsType1 requestInputs) {
        List<String> requestInputNames = new ArrayList<String>();
        List<String> processInputNames = new ArrayList<String>();

        for(InputType input : (List<InputType>)requestInputs.getInput()) {
            requestInputNames.add(input.getIdentifier().getValue());
        }

        processInputNames.addAll(processParameters.keySet());

        // Check for missing input parameters
        for(String processInputName : processInputNames) {
            if (false == requestInputNames.contains(processInputName)) {
                throw new WPSException("MissingParameterValue", processInputName);
            }
        }

        requestInputNames.removeAll(processInputNames);

        // Check for unknown input types
        StringBuffer unknownParameters = new StringBuffer("");
        for(String unknownName : requestInputNames) {
            if (false == "".equals(unknownParameters.toString())) {
                unknownParameters.append(", ");
            }

            unknownParameters.append(unknownName);
        }

        if (false == "".equals(unknownParameters.toString())) {
            throw new WPSException("NoApplicableCode", "Uknown input parameters: " + unknownParameters);
        }

        return;
    }

    private ProcessFactory findProcessFactory(CodeType name) {
        for(ProcessFactory pf : Processors.getProcessFactories()) {
            if (pf.getName().equals(name.getValue())) {
                return pf;
            }
        }

        return null;
    }
}
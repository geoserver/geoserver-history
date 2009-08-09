/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.xacml.extensions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.xacml.cond.EvaluationResult;
import com.sun.xacml.cond.FunctionBase;
import com.sun.xacml.ctx.Status;

/**
 * @author Christian Mueller
 * 
 *         Abstract base class for XACML functions.
 * 
 * 
 */
public abstract class XACMLFunctionBase extends FunctionBase {

    protected static final String NAME_PREFIX = "org:geotools:xacml:function:";

    public XACMLFunctionBase(String functionName, int functionId, String paramType,
            boolean paramIsBag, int numParams, int minParams, String returnType, boolean returnsBag) {
        super(functionName, functionId, paramType, paramIsBag, numParams, minParams, returnType,
                returnsBag);
    }

    public XACMLFunctionBase(String functionName, int functionId, String paramType,
            boolean paramIsBag, int numParams, String returnType, boolean returnsBag) {
        super(functionName, functionId, paramType, paramIsBag, numParams, returnType, returnsBag);
    }

    public XACMLFunctionBase(String functionName, int functionId, String returnType,
            boolean returnsBag) {
        super(functionName, functionId, returnType, returnsBag);
    }

    public XACMLFunctionBase(String functionName, int functionId, String[] paramTypes,
            boolean[] paramIsBag, String returnType, boolean returnsBag) {
        super(functionName, functionId, paramTypes, paramIsBag, returnType, returnsBag);
    }

    /**
     * @param t
     *            a Throwable
     * @return an EvaluationResult indicating a processing error
     */
    protected EvaluationResult exceptionError(Throwable t) {

        Logger log = Logger.getLogger(this.getClass().getName());
        log.log(Level.SEVERE, t.getMessage(), t);

        List<String> codeList = new ArrayList<String>();
        codeList.add(Status.STATUS_PROCESSING_ERROR);
        return new EvaluationResult(new Status(codeList, t.getLocalizedMessage()));
    }

}

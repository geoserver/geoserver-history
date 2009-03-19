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



package org.geotools.xacml.geoxacml.test;

import java.io.FileInputStream;

import org.geotools.xacml.geoxacml.config.GeoXACML;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.sun.xacml.PDP;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;
import com.sun.xacml.ctx.Status;

/**
 * @author Christian Mueller
 *
 * Test for converting untis to metres or square metres
 */
public class ConvertTest extends TestCase {


	
	
	public ConvertTest() {
		super();
						
	}

	public ConvertTest(String arg0) {
		super(arg0);
		
	}
	
	@Override
	protected void setUp() throws Exception {
		GeoXACML.initialize();
		TestSupport.initOutputDir();
	}
	
	
	
	
	public void testConvertToSquareMetre() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("convert","ConvertToSquareMetrePolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("convert","ConvertToSquareMetreRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testConvertToMetre() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("convert","ConvertToMetrePolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("convert","ConvertToMetreRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}

	
}

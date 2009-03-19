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
 * Test for geomtry set functions
 */
public class SetTests extends TestCase {


	
	
	public SetTests() {
		super();
						
	}

	public SetTests(String arg0) {
		super(arg0);
		
	}
	
	@Override
	protected void setUp() throws Exception {
		GeoXACML.initialize();
		TestSupport.initOutputDir();
	}
	
	
	
	
	public void testBagIntersection() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("set","BagIntersectionPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("set","BagIntersectionRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testBagIntersection1() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("set","BagIntersectionPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("set","BagIntersectionRequest1.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testBagUnion() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("set","BagUnionPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("set","BagUnionRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testBagUnion1() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("set","BagUnionPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("set","BagUnionRequest1.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}

	public void testBagAtLeastOneMemberOf() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("set","BagAtLeastOneMemberOfPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("set","BagAtLeastOneMemberOfRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testBagAtLeastOneMemberOf1() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("set","BagAtLeastOneMemberOfPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("set","BagAtLeastOneMemberOfRequest1.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}

	public void testBagSubset() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("set","BagSubsetPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("set","BagSubsetRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testBagSubset1() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("set","BagSubsetPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("set","BagSubsetRequest1.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}

	public void testBagEquals() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("set","BagEqualsPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("set","BagEqualsRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testBagEquals1() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("set","BagEqualsPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("set","BagEqualsRequest1.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
}

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
 * Tests for geomtry construct functions
 */
public class ConstructTests extends TestCase {

		
	
	public ConstructTests() {
		super();
						
	}

	public ConstructTests(String arg0) {
		super(arg0);
		
	}
	
	@Override
	protected void setUp() throws Exception {
		GeoXACML.initialize();
		TestSupport.initOutputDir();
	}
	
	
	
	
	public void testBuffer() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","BufferPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","BufferRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testBuffer1() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","BufferPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","BufferRequest1.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}

	
	public void testUnion() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","UnionPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","UnionRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testUnion1() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","UnionPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","UnionRequest1.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testIntersection() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","IntersectionPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","IntersectionRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testIntersection1() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","IntersectionPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","IntersectionRequest1.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}

	public void testDifference() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","DifferencePolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","DifferenceRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testDifference1() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","DifferencePolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","DifferenceRequest1.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}

	public void testSymDifference() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","SymDifferencePolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","SymDifferenceRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testSymDifference1() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","SymDifferencePolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","SymDifferenceRequest1.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}

	public void testBoundary() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","BoundaryPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","BoundaryRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testBoundary1() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","BoundaryPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","BoundaryRequest1.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}

	public void testCentroid() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","CentroidPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","CentroidRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testCentroid1() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","CentroidPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","CentroidRequest1.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}

	public void testConvexHull() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","ConvexHullPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","ConvexHullRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testConvexHull1() {
		
	    PDP pdp = TestSupport.getPDP(TestSupport.getFNFor("construct","ConvexHullPolicy.xml"));
	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("construct","ConvexHullRequest1.xml")));
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

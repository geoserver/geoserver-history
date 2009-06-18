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


package org.geotools.xacml.rbac.test;

import java.io.FileInputStream;

import org.geotools.xacml.geoxacml.config.GeoXACML;
import org.geotools.xacml.test.TestSupport;

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
 * Tests for RBAC
 *
 */
public class RBACTest extends TestCase {

		
		
	public RBACTest() {
		super();
						
	}

	public RBACTest(String arg0) {
		super(arg0);
		
	}
	
	@Override
	protected void setUp() throws Exception {
		GeoXACML.initialize();
		TestSupport.initOutputDir();
	}
	
	
		
	public void testEmployee() {
		
	    PDP pdp = TestSupport.getRBAC_PDP();
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("rbac","EmployeeRequest.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testIsEmployee1() {
		
		PDP pdp = TestSupport.getRBAC_PDP();	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("rbac","EmployeeRequest1.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testIsEmployee2() {
		
		PDP pdp = TestSupport.getRBAC_PDP();	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("rbac","EmployeeRequest2.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}

	public void testIsEmployee3() {
		
		PDP pdp = TestSupport.getRBAC_PDP();	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("rbac","EmployeeRequest3.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_NOT_APPLICABLE);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}


	public void testManagerJunior() {
		
		PDP pdp = TestSupport.getRBAC_PDP();	    	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("rbac","ManagerRequestJunior.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}
	
	public void testManagerSenior() {
		
		PDP pdp = TestSupport.getRBAC_PDP();	    	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("rbac","ManagerRequestSenior.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
	    ResponseCtx response= pdp.evaluate(request);
	    Result result = (Result)response.getResults().iterator().next();
	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
	}

//      TODO, activate
//	public void testHPORAliceEmployee() {
//		
//	    PDP pdp = TestSupport.getRBAC_PDP();
//	    RequestCtx request = null;
//		try {
//			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("rbac","HPORAliceEmployeeRequest.xml")));
//		} catch (Exception e) {
//			e.printStackTrace();
//			Assert.fail(e.getMessage());
//		}
//		
//	    ResponseCtx response= pdp.evaluate(request);
//	    Result result = (Result)response.getResults().iterator().next();
//	    assertTrue(result.getDecision()==Result.DECISION_PERMIT);
//	    assertTrue(result.getStatus().getCode().iterator().next().equals(Status.STATUS_OK));
//	}
	
	public void testHPORAliceManager() {
		
		PDP pdp = TestSupport.getRBAC_PDP();	    	    	    	    
	    RequestCtx request = null;
		try {
			request = RequestCtx.getInstance(new FileInputStream(TestSupport.getFNFor("rbac","HPORAliceManagerRequest.xml")));
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

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.geoxacml;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.intercept.web.FilterInvocation;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.easymock.EasyMock;
import org.geoserver.xacml.acegi.XACMLFilterDecisionVoter;

import junit.framework.TestCase;

/**
 * Testing URL matching
 * 
 * 
 * @author Christian Mueller
 * 
 */
public class XACMLURLMatchingTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }


    public void testWeb() {
        HttpServletRequest mockRequest = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(mockRequest.getMethod()).andReturn("GET").anyTimes();
        EasyMock.replay(mockRequest);
        
        FilterInvocation filter = org.easymock.classextension.EasyMock.createMock(FilterInvocation.class);
        org.easymock.classextension.EasyMock.expect(filter.getRequestUrl()).andReturn("/security/geoxacml").anyTimes();
        org.easymock.classextension.EasyMock.expect(filter.getHttpRequest()).andReturn(mockRequest).anyTimes();
        org.easymock.classextension.EasyMock.replay(filter);
        
        XACMLFilterDecisionVoter voter = new XACMLFilterDecisionVoter();
        int result = voter.vote(null, filter, new ConfigAttributeDefinition());
        assertTrue(AccessDecisionVoter.ACCESS_GRANTED==result);
    }

}

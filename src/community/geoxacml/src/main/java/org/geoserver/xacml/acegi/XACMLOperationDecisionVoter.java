/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.xacml.acegi;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.geoserver.platform.Operation;
import org.geoserver.xacml.geoxacml.GeoXACMLConfig;
import org.geoserver.xacml.geoxacml.XACMLConstants;
import org.geoserver.xacml.geoxacml.XACMLUtil;

import com.ibm.gsk.ikeyman.command.Constants;
import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;
import com.sun.xacml.ctx.Subject;

/**
 * Acegi Decision Voter using XACML policies
 * 
 * @author Christian Mueller
 *
 */
public class XACMLOperationDecisionVoter implements AccessDecisionVoter {

     
    
    public boolean supports(ConfigAttribute attr) {
        return true;
    }

    public boolean supports(Class aClass) {
        return Operation.class.isAssignableFrom(aClass);
    }

    public int vote(Authentication auth, Object request, ConfigAttributeDefinition arg2) {
        List<RequestCtx> ctxList = buildRequestCtx(auth,(Operation) request);
        int decision = evaluateContextList(ctxList); 
                
        switch (decision) {
        case Result.DECISION_DENY: 
            return AccessDecisionVoter.ACCESS_DENIED; 
        case Result.DECISION_PERMIT: 
            return AccessDecisionVoter.ACCESS_GRANTED;
            // TODO, not sure here
        case Result.DECISION_NOT_APPLICABLE:  
            return AccessDecisionVoter.ACCESS_GRANTED;
        case Result.DECISION_INDETERMINATE:
            Logger log = Logger.getLogger(this.getClass().getName());
            log.severe("GeoXACML Error for for "+ request + " : "+ auth);
            return AccessDecisionVoter.ACCESS_DENIED;                         
        }
        throw new RuntimeException("Never should reach this point");
                    
    }
    
    private int evaluateContextList(List <RequestCtx> requestCtxtList) {
        int permitCount=0,denyCount=0, notApplicableCount=0;
        for (RequestCtx ctx: requestCtxtList) {
            int decision = evaluateContex(ctx);
            if (decision==Result.DECISION_INDETERMINATE) // Error
                return decision;
            if (decision==Result.DECISION_DENY) denyCount++;
            if (decision==Result.DECISION_PERMIT) permitCount++;
            if (decision==Result.DECISION_NOT_APPLICABLE) notApplicableCount++;            
        }
        
        if (permitCount > 0 && denyCount > 0) {
            Logger log = Logger.getLogger(this.getClass().getName());
            log.severe("GeoXACML Error: having "+permitCount+ " permits and "+ denyCount + " denies");
            return Result.DECISION_INDETERMINATE;
        }
        if (permitCount > 0) return Result.DECISION_PERMIT;
        if (denyCount > 0) return Result.DECISION_DENY;
        return Result.DECISION_NOT_APPLICABLE;
    }
    
    private int evaluateContex(RequestCtx ctx) {
        ResponseCtx response = GeoXACMLConfig.getPDP().evaluate(ctx);
        Set<Result> results = response.getResults();
        if (results.size()!=1) {
            Logger log = Logger.getLogger(this.getClass().getName());
            log.severe("GeoXACML Error: Response has more than one result");
            log.severe(XACMLUtil.asXMLString(response));
            return Result.DECISION_INDETERMINATE;
        }
        Result result = results.iterator().next();        
        return result.getDecision();
        
    }
    
    private List<RequestCtx> buildRequestCtx(Authentication auth, Operation operation) {
        
        List<RequestCtx> resultList = new ArrayList<RequestCtx>(auth.getAuthorities().length);
        Set<Attribute> actions= new HashSet<Attribute>(1);        
        actions.add(new Attribute(XACMLConstants.ActionAttributeURI,null,null,new StringAttribute(operation.getId())));
        Set<Attribute> environment= Collections.emptySet();
        
        Set<Attribute> resources= new HashSet<Attribute>(2);
        resources.add(new Attribute(XACMLConstants.ResourceAttributeURI,null,null,new StringAttribute(operation.getService().getId())));
        resources.add(new Attribute(XACMLConstants.ResourceTypeURI,null,null,new StringAttribute(XACMLConstants.ResourceTypeOperation)));
        
        for (GrantedAuthority gauth: auth.getAuthorities()) {
            URI roleURI = null;
            try {
                roleURI=new URI(gauth.getAuthority());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            AttributeValue role = new AnyURIAttribute(roleURI);
            Attribute roleAttribute = new Attribute(XACMLConstants.RoleAttributeURI,null,null,role);
            Set<Attribute> subjectAttributes = new HashSet<Attribute>(1);
            subjectAttributes.add(roleAttribute);
            Subject subject = new Subject(subjectAttributes);            
            Set<Subject> subjects= new HashSet<Subject>(1);
            subjects.add(subject);
                                                                                           
            RequestCtx ctx = new RequestCtx(subjects,resources,actions,environment);
            System.out.println(XACMLUtil.asXMLString(ctx));
            resultList.add(ctx);
        }
                              
        return resultList;
        
    }

}

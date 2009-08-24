/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.xacml.acegi;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.InsufficientAuthenticationException;
import org.acegisecurity.vote.AbstractAccessDecisionManager;
import org.acegisecurity.vote.AccessDecisionVoter;

/**
 * Acegi AccessDecsionsManger implementation for Services
 * 
 * @author Christian Mueller
 * 
 */
public class XACMLFilterAccessDecisionManager extends AbstractAccessDecisionManager {

    public void decide(Authentication auth, Object arg1, ConfigAttributeDefinition arg2)
            throws AccessDeniedException, InsufficientAuthenticationException {

        AccessDecisionVoter voter = (AccessDecisionVoter) this.getDecisionVoters().get(0);
        int decision = voter.vote(auth, arg1, arg2);
        if (decision != AccessDecisionVoter.ACCESS_GRANTED) {
            throw new AccessDeniedException("Access Denied: " + arg1.toString());
        }
    }

}

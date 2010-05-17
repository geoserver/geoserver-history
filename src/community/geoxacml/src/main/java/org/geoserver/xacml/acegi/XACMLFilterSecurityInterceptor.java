/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.acegi;

import org.acegisecurity.intercept.ObjectDefinitionSource;
import org.acegisecurity.intercept.web.FilterSecurityInterceptor;

/**
 * Url based authorization
 * 
 * 
 * @author Christian Mueller
 * 
 */
public class XACMLFilterSecurityInterceptor extends FilterSecurityInterceptor {

    @Override
    public ObjectDefinitionSource obtainObjectDefinitionSource() {
        return XACMLFilterDefinitionSource.Singleton;
    }

}

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.acegi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.acegisecurity.intercept.AbstractSecurityInterceptor;
import org.acegisecurity.intercept.InterceptorStatusToken;
import org.acegisecurity.intercept.ObjectDefinitionSource;
import org.geoserver.ows.security.OperationInterceptor;
import org.geoserver.platform.Operation;


public class XACMLOperationSecurityInterceptor extends AbstractSecurityInterceptor implements OperationInterceptor{

    @Override
    public Class getSecureObjectClass() {
        return Operation.class;
    }

    @Override
    public ObjectDefinitionSource obtainObjectDefinitionSource() {
        return XACMLOperationDefinitionSource.Singleton;
    }

    public Object invoke(Operation operation, Method method, Object serviceBean, Object[] parameters)
    throws InvocationTargetException, IllegalArgumentException, IllegalAccessException {
        
        InterceptorStatusToken token = super.beforeInvocation(operation);

        try {
            return method.invoke(serviceBean, parameters);
        } finally {
            super.afterInvocation(token, null);
        }   
     }

}

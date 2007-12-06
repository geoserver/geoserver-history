/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.security;

import org.acegisecurity.intercept.AbstractSecurityInterceptor;
import org.acegisecurity.intercept.InterceptorStatusToken;
import org.acegisecurity.intercept.ObjectDefinitionSource;
import org.geoserver.ows.security.OperationInterceptor;
import org.geoserver.platform.Operation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class OperationSecurityInterceptor extends AbstractSecurityInterceptor
    implements OperationInterceptor {
    private OperationDefinitionSource objectDefinitionSource;

    public Class getSecureObjectClass() {
        return Operation.class;
    }

    public ObjectDefinitionSource obtainObjectDefinitionSource() {
        return this.objectDefinitionSource;
    }

    public void setObjectDefinitionSource(OperationDefinitionSource newSource) {
        this.objectDefinitionSource = newSource;
    }

    protected InterceptorStatusToken beforeInvocation(Object object) {
        return super.beforeInvocation(object);
    }

    public Object invoke(Operation operation, Method method,
        Object serviceBean, Object[] parameters)
        throws InvocationTargetException, IllegalArgumentException,
            IllegalAccessException {
        InterceptorStatusToken token = super.beforeInvocation(operation);

        try {
            return method.invoke(serviceBean, parameters);
        } finally {
            super.afterInvocation(token, null);
        }
    }
}

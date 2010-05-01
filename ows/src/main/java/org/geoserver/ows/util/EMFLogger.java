/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows.util;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * A generic service object invocation logger based on EMF reflection
 * @author Justin DeOliveira, TOPP
 */
public class EMFLogger implements MethodInterceptor {
    /**
     * Logging instance
     */
    Logger logger;
    
    public EMFLogger(String logPackage) {
        logger = org.geotools.util.logging.Logging.getLogger(logPackage);
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        StringBuffer log = new StringBuffer();
        log.append("\n" + "Request: " + invocation.getMethod().getName());

        if (invocation.getArguments().length > 0) {
            EObject requestBean = null;

            for (int i = 0; i < invocation.getArguments().length; i++) {
                Object argument = (Object) invocation.getArguments()[i];

                if (argument instanceof EObject) {
                    requestBean = (EObject) argument;

                    break;
                }
            }

            if (requestBean != null) {
                log(requestBean, 1, log);
            }
        }

        Object result = invocation.proceed();
        logger.info(log.toString());

        return result;
    }

    void log(EObject object, int level, StringBuffer log) {
        List properties = object.eClass().getEAllStructuralFeatures();

        for (Iterator p = properties.iterator(); p.hasNext();) {
            EStructuralFeature property = (EStructuralFeature) p.next();
            Object value = object.eGet(property);

            log.append("\n");

            for (int i = 0; i < level; i++)
                log.append("\t");

            log.append(property.getName());

            if (value instanceof EObject && (level < 2)) {
                log.append(":");
                log((EObject) value, level + 1, log);
            } else {
                log.append(" = " + value);
            }
        }
    }
}

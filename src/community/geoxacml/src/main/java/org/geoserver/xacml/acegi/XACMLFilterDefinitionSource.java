/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.acegi;

import java.util.Collections;
import java.util.Iterator;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.intercept.ObjectDefinitionSource;

/**
 * Acegi ObjectDefinitonSource implementation for Services
 * 
 * @author Christian Mueller
 * 
 */
public class XACMLFilterDefinitionSource implements ObjectDefinitionSource {

    private static final ConfigAttributeDefinition ConfigDef = new ConfigAttributeDefinition();

    public final static XACMLFilterDefinitionSource Singleton = new XACMLFilterDefinitionSource();

    public ConfigAttributeDefinition getAttributes(Object obj) throws IllegalArgumentException {
        return ConfigDef;
    }

    public Iterator getConfigAttributeDefinitions() {
        return Collections.EMPTY_SET.iterator();
    }

    public boolean supports(Class aClass) {
        return true;
    }

}

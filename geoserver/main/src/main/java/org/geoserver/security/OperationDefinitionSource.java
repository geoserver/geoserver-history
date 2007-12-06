/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.security;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.ConfigAttributeEditor;
import org.acegisecurity.intercept.ObjectDefinitionSource;
import org.geoserver.platform.Operation;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Returns security attributes (roles) for an OWS {@link Operation} based on the
 * contents of a property file
 *
 * @author Andrea Aime - TOPP
 *
 */
public class OperationDefinitionSource implements ObjectDefinitionSource {
    /** logger */
    static Logger LOGGER = Logger.getLogger("org.geoserver.security");
    Map definitions;
    PropertyFileWatcher operationDefinitionFile;

    public ConfigAttributeDefinition getAttributes(Object object)
        throws IllegalArgumentException {
        if (!(object instanceof Operation)) {
            throw new IllegalArgumentException(
                "Only Operation objects are supported by the OperationDefinitionSource");
        }

        // check if we have config attributes in the map
        Operation op = (Operation) object;
        checkDefinitions();

        OperationKey key = new OperationKey(op.getService().getId(), op.getId());
        ConfigAttributeDefinition result = (ConfigAttributeDefinition) definitions
            .get(key);

        if (result == null) {
            // try to fall back on some service definitions
            key.operation = null;
            result = (ConfigAttributeDefinition) definitions.get(key);
        }

        return result;
    }

    public Iterator getConfigAttributeDefinitions() {
        checkDefinitions();

        if (definitions != null) {
            return definitions.values().iterator();
        }

        return Collections.EMPTY_SET.iterator();
    }

    public boolean supports(Class clazz) {
        return Operation.class.isAssignableFrom(clazz);
    }

    void checkDefinitions() {
        if ((definitions == null)
                || ((operationDefinitionFile != null)
                && operationDefinitionFile.isStale())) {
            try {
                definitions = new HashMap();

                if (operationDefinitionFile == null) {
                    File dd = GeoserverDataDirectory.getGeoserverDataDirectory();
                    File securityDir = GeoserverDataDirectory.findConfigDir(dd,
                            "security");
                    File securityDefinitions = new File(securityDir,
                            "service.properties");
                    operationDefinitionFile = new PropertyFileWatcher(securityDefinitions);
                }

                ConfigAttributeEditor editor = new ConfigAttributeEditor();
                Properties p = operationDefinitionFile.getProperties();

                for (Iterator it = p.keySet().iterator(); it.hasNext();) {
                    try {
                        String key = (String) it.next();
                        OperationKey okey = new OperationKey(key);
                        editor.setAsText(p.getProperty(key));

                        ConfigAttributeDefinition cad = (ConfigAttributeDefinition) editor
                            .getValue();
                        definitions.put(okey, cad);
                    } catch (IllegalArgumentException e) {
                        LOGGER.log(Level.SEVERE,
                            "Skipping invalid operation security configuration element",
                            e);
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE,
                    "An error occurred loading service security definitions ", e);
            }
        }
    }

    private class OperationKey {
        String service;
        String operation;

        public OperationKey(String service, String operation) {
            this.service = service;
            this.operation = operation;
        }

        public OperationKey(String key) throws IllegalArgumentException {
            if (key.indexOf('.') != -1) {
                String[] elements = key.split("\\.");

                if (elements.length > 2) {
                    throw new IllegalArgumentException(
                        "A valid operation key is made of 'service.method', or just 'service'. ");
                }

                service = elements[0].trim();
                operation = elements[1].trim();
            } else {
                service = key;
                operation = null;
            }

            if ("".equals(service)) {
                throw new IllegalArgumentException("Service must be specified");
            }

            if ("".equals(operation)) {
                operation = null;
            }
        }

        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = (PRIME * result)
                + ((operation == null) ? 0 : operation.hashCode());
            result = (PRIME * result)
                + ((service == null) ? 0 : service.hashCode());

            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null) {
                return false;
            }

            if (getClass() != obj.getClass()) {
                return false;
            }

            final OperationKey other = (OperationKey) obj;

            if (operation == null) {
                if (other.operation != null) {
                    return false;
                }
            } else if (!operation.equals(other.operation)) {
                return false;
            }

            if (service == null) {
                if (other.service != null) {
                    return false;
                }
            } else if (!service.equals(other.service)) {
                return false;
            }

            return true;
        }
    }
}

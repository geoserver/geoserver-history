/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows;


/**
 * Bean wrapper for an Open Web Service (OWS).
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public final class Service {
    /**
     * Identifier for the service.
     */
    String id;

    /**
     * The service itself.
     */
    Object service;

    public Service(String id, Object service) {
        this.id = id;
        this.service = service;
    }

    public String getId() {
        return id;
    }

    public Object getService() {
        return service;
    }
}

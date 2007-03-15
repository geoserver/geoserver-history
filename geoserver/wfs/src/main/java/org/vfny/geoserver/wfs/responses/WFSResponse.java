/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.responses;

import org.vfny.geoserver.Response;
import org.vfny.geoserver.global.WFS;


/**
 * Abstract base class for all WFS Resonse implementations.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
public abstract class WFSResponse implements Response {
    /** Reference to the WFS service. */
    WFS wfs;

    /**
     * Sets the wfs service reference.
     *
     * @param wfs DOCUMENT ME!
     */
    public void setWFS(WFS wfs) {
        this.wfs = wfs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return The wfs service reference.
     */
    public WFS getWFS() {
        return wfs;
    }
}

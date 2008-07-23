/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps.kvp;

import net.opengis.wps.WpsFactory;
import org.geoserver.ows.kvp.EMFKvpRequestReader;

/**
 * WPS KVP Request Reader
 *
 * @author Lucas Reed, Refractions Research Inc
 */
public class WPSKvpRequestReader extends EMFKvpRequestReader {
    public WPSKvpRequestReader(Class<?> requestBean) {
        super(requestBean, WpsFactory.eINSTANCE);
    }

    protected WpsFactory getWpsFactory() {
        return (WpsFactory)factory;
    }
}

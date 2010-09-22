/* Copyright (c) 2010 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.map;

import org.geoserver.wms.WMSMapContext;
import org.geoserver.wms.response.Map;

/**
 * An already encoded {@link Map} that holds the raw response content in a byte array.
 * 
 * @author Gabriel Roldan
 */
public class RawMap extends Map {

    private WMSMapContext mapContext;

    private byte[] mapContents;

    public RawMap(final WMSMapContext mapContext, final byte[] mapContents, final String mimeType) {
        this.mapContext = mapContext;
        this.mapContents = mapContents;
        setMimeType(mimeType);
    }

    public byte[] getMapContents() {
        return mapContents;
    }

    public WMSMapContext getMapContext() {
        return mapContext;
    }

}

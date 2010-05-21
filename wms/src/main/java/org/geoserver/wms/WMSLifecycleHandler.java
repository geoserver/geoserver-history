/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import org.geoserver.config.impl.GeoServerLifecycleHandler;
import org.geotools.renderer.style.FontCache;
import org.geotools.renderer.style.ImageGraphicFactory;
import org.geotools.renderer.style.SVGGraphicFactory;

/**
 * Drops imaging caches
 * @author aaime
 */
public class WMSLifecycleHandler implements GeoServerLifecycleHandler {

    public void onDispose() {
        // nothing to do
    }

    public void onReload() {
        // clear the caches for good measure
        onReset();
    }

    public void onReset() {
        // kill the image and font caches
        ImageGraphicFactory.resetCache();
        SVGGraphicFactory.resetCache();
        FontCache.getDefaultInstance().resetCache();
    }

}

/* Copyright (c) 2010 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.svg;

import org.geoserver.platform.ExtensionFilter;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.wms.WMS;

/**
 * An extension filter that tells {@link GeoServerExtensions#extensions(Class)} (where
 * {@code Class == GetMapOutputFormat.class}) whether the {@link SVGStreamingMapOutputFormat} or the
 * {@link SVGBatikMapOutputFormat} is to be excluded based on the {@link WMS#getSvgRenderer()}
 * config option.
 * 
 * @author Gabriel Roldan
 * 
 */
public class SVGStrategyExclusionFilter implements ExtensionFilter {

    private WMS wms;

    public SVGStrategyExclusionFilter() {
        //
    }

    /**
     * Setter for the WMS facade, needs to be a property instead of a constructor argument,
     * otherwise it's not yet resolved by spring by the time this ExtensionFilter is created.
     * 
     * @param wms
     */
    public void setWMS(WMS wms) {
        this.wms = wms;
    }

    /**
     * @see ExtensionFilter#exclude(String, Object)
     */
    public boolean exclude(String beanId, Object bean) {
        boolean exclude = false;
        if (bean instanceof SVGStreamingMapOutputFormat) {
            exclude = !SVG.canHandle(wms, WMS.SVG_SIMPLE);
        } else if (bean instanceof SVGBatikMapOutputFormat) {
            exclude = !SVG.canHandle(wms, WMS.SVG_BATIK);
        }
        return exclude;
    }

}

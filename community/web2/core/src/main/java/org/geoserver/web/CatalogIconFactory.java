/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.ResourceReference;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.LayerInfo.Type;
import org.geoserver.web.data.DataStorePanelInfo;
import org.geotools.data.DataAccessFactory;

/**
 * Utility class used to lookup icons for various catalog objects
 */
@SuppressWarnings("serial")
public class CatalogIconFactory implements Serializable {

    public static final ResourceReference RASTER_ICON = new ResourceReference(
            GeoServerBasePage.class, "img/icons/geosilk/raster.png");

    public static final ResourceReference VECTOR_ICON = new ResourceReference(
            GeoServerBasePage.class, "img/icons/geosilk/vector.png");

    public static final ResourceReference UNKNOWN_ICON = new ResourceReference(
            GeoServerBasePage.class, "img/icons/silk/error.png");
    
    public static final ResourceReference GROUP_ICON = new ResourceReference(
            GeoServerBasePage.class, "img/icons/silk/layers.png");

    /**
     * Returns the appropriate icon for the specified layer
     * @param info
     * @return
     */
    public ResourceReference getLayerIcon(LayerInfo info) {
        ResourceReference icon = UNKNOWN_ICON;
        if(info.getType() == Type.VECTOR)
            icon = VECTOR_ICON;
        else if(info.getType() == Type.RASTER)
            icon = RASTER_ICON;
        return icon;
    }
    
    /**
     * Returns the appropriate icon given the 
     * @param factoryClass
     * @return
     */
    public ResourceReference getStoreIcon(Class<?> factoryClass) {
        // look for the associated panel info if there is one
        List<DataStorePanelInfo> infos = GeoServerApplication.get()
                .getBeansOfType(DataStorePanelInfo.class);
        for (DataStorePanelInfo panelInfo : infos) {
            if (panelInfo.getFactoryClass().equals(factoryClass))
                return new ResourceReference(panelInfo.getIconBase(), panelInfo
                        .getIcon());
        }

        if (DataAccessFactory.class.isAssignableFrom(factoryClass))
            // fall back on generic vector icon otherwise
            return new ResourceReference(GeoServerApplication.class,
                    "img/icons/geosilk/vector.png");
        else
            return new ResourceReference(GeoServerApplication.class,
                    "img/icons/geosilk/raster.png");

    }
    
}

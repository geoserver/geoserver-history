package org.geoserver.web.services;

import org.geoserver.web.ComponentInfo;
import org.geoserver.web.GeoServerBasePage;

/**
 * Extension point for service configuration pages.
 *  
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class ServicePageInfo extends ComponentInfo<GeoServerBasePage> {

    /** the icon for the service */
    String icon;
    
    /**
     * The icon for the service.
     * <p>
     * This icon must be 16px by 16px and is typically used in conjunction 
     * with a link to the page.
     * </p>
     */
    public String getIcon() {
        return icon;
    }
    
    /**
     * Sets the icon for the service.
     */
    public void setIcon( String icon ) {
        this.icon = icon;
    }
}

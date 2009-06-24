package org.geoserver.catalog;

import java.awt.Dimension;

/**
 *  The AttributionInfo interface describes a data provider for attribution, such as in the WMS 
 *  Capabilities document.
 *
 *  @author David Winslow <dwinslow@opengeo.org>
 */
public interface AttributionInfo extends Info {
    public String getTitle();
    public String getHref();
    public String getLogoUrl();
    public String getLogoType();
    public int getLogoWidth();
    public int getLogoHeight();

    public void setTitle(String title);
    public void setHref(String href);
    public void setLogoUrl(String href);
    public void setLogoType(String type);
    public void setLogoWidth(int width);
    public void setLogoHeight(int height);
}

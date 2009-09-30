package org.geoserver.services.hibernate.beans;

import org.geoserver.services.hibernate.intf.WatermarkInfoHb;
import org.geoserver.wms.WatermarkInfo.Position;

public class WatermarkInfoImplHb implements WatermarkInfoHb {

    private Integer id;

    private boolean enabled;

    private Position position;

    private int transparency;

    private String url;

    public WatermarkInfoImplHb() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getTransparency() {
        return transparency;
    }

    public void setTransparency(int transparency) {
        this.transparency = transparency;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

}

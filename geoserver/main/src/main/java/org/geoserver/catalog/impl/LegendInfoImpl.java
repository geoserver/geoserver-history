package org.geoserver.catalog.impl;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LegendInfo;

public class LegendInfoImpl implements LegendInfo {

    String id;

    int width;

    int height;

    String format;

    String onlineResource;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getOnlineResource() {
        return onlineResource;
    }

    public void setOnlineResource(String onlineResource) {
        this.onlineResource = onlineResource;
    }

}

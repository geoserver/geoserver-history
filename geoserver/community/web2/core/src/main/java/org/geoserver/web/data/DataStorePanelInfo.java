/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data;

import org.geoserver.web.ComponentInfo;

/**
 * Used to declare a data store panel information and its icon. Both are
 * optional, you can specify the configuration panel but not the icon, or the
 * opposite.
 * 
 * @author aaime
 * 
 */
/*
 * TODO: specify the type of component that will fit in here
 */
public class DataStorePanelInfo extends ComponentInfo {
    Class factoryClass;

    String icon;

    Class iconBase;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Class getIconBase() {
        return iconBase;
    }

    /**
     * Used as the reference class to locate the datastore icon (since the
     * component might not be there)
     * 
     * @param iconBase
     */
    public void setIconBase(Class iconBase) {
        this.iconBase = iconBase;
    }

    public Class getFactoryClass() {
        return factoryClass;
    }

    public void setFactoryClass(Class factoryClassName) {
        this.factoryClass = factoryClassName;
    }

}

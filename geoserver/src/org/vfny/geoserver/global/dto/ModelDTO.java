/* Copyright (c) 2001 - 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import org.vfny.geoserver.config.data.CatalogConfig;
import org.vfny.geoserver.config.wfs.WFSConfig;
import org.vfny.geoserver.config.wms.WMSConfig;


/**
 * Data Transfer Object for GeoServer Model information.
 * <p>
 * Jody - Does this class need to go?
 * </p>
 * <p>
 * This class is intended to be used as a set of references for  the other
 * major portions of the configuration to be represented in memory. It is only
 * being preserved while global retargets itself at these classes.
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ModelDTO.java,v 1.1.2.1 2004/01/04 06:21:33 jive Exp $
 */
public class ModelDTO implements DataStructure {
    /**
     * The catalog configuration data structure represented in memory.
     *
     * @see org.vfny.geoserver.config.data.GlobalCatalog
     */
    private CatalogConfig catalog;

    /**
     * The server configuration data structure represented in memory.
     *
     * @see org.vfny.geoserver.config.GlobalData
     */
    private GlobalDTO global;

    /**
     * The wfs configuration data structure represented in memory.
     *
     * @see org.vfny.geoserver.config.wfs.GlobalWFS
     */
    private WFSConfig wfs;

    /**
     * The wms configuration data structure represented in memory.
     *
     * @see org.vfny.geoserver.config.wms.GlobalWMS
     */
    private WMSConfig wms;

    /**
     * ModelConfig constructor.
     * 
     * <p>
     * Creates and returns a ModelConfig object which contains the default
     * settings.
     * </p>
     *
     * @see defaultSettings()
     */
    public ModelDTO() {
        defaultSettings();
    }

    /**
     * ModelConfig constructor.
     * 
     * <p>
     * Creates a copy of the ModelConfig object provided. If the object
     * provided is null,  default values are used. The data contained in the
     * old ModelConfig object is cloned  rather than reference copied.
     * </p>
     *
     * @param m The ModelConfig object to copy.
     */
    public ModelDTO(ModelDTO m) {
        if (m == null) {
            defaultSettings();

            return;
        }

        catalog = (CatalogConfig) m.getCatalog().clone();
        global = (GlobalDTO) m.getGlobal().clone();
        wfs = (WFSConfig) m.getWfs().clone();
        wms = (WMSConfig) m.getWms().clone();
    }

    /**
     * defaultSettings purpose.
     * 
     * <p>
     * Sets up the class with default values. This method should only be
     * called by a class constructor.
     * </p>
     */
    private void defaultSettings() {
        catalog = new CatalogConfig();
        global = new GlobalDTO();
        wfs = new WFSConfig();
        wms = new WMSConfig();
    }

    /**
     * Implement clone.
     * 
     * <p>
     * Creates a clone of the current object.
     * </p>
     *
     * @return A clone of this object.
     *
     * @see java.lang.Object#clone()
     * @see org.vfny.geoserver.config.GlobalData#clone()
     * @see org.vfny.geoserver.config.data.GlobalCatalog#clone()
     * @see org.vfny.geoserver.config.wfs.GlobalWFS#clone()
     * @see org.vfny.geoserver.config.wms.GlobalWMS#clone()
     */
    public Object clone() {
        return new ModelDTO(this);
    }

    /**
     * Implement equals.
     * 
     * <p>
     * Recursively determines if the two objects are equal.
     * </p>
     *
     * @param obj An instance of a ModelConfig Object to be tested for
     *        equality.
     *
     * @return true when the two objects are recursively equal.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @see org.vfny.geoserver.config.GlobalData#equals()
     * @see org.vfny.geoserver.config.data.GlobalCatalog#equals()
     * @see org.vfny.geoserver.config.wfs.GlobalWFS#equals()
     * @see org.vfny.geoserver.config.wms.GlobalWMS#equals()
     */
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ModelDTO)) {
            return false;
        }

        ModelDTO m = (ModelDTO) obj;

        if (m == null) {
            return false;
        }

        boolean r = true;

        if (catalog != null) {
            r = r && catalog.equals(m.getCatalog());
        } else if (m.getCatalog() != null) {
            return false;
        }

        if (global != null) {
            r = r && global.equals(m.getGlobal());
        } else if (m.getGlobal() != null) {
            return false;
        }

        if (wfs != null) {
            r = r && wfs.equals(m.getWfs());
        } else if (m.getWfs() != null) {
            return false;
        }

        if (wms != null) {
            r = r && wms.equals(m.getWms());
        } else if (m.getWms() != null) {
            return false;
        }

        return r;
    }

    /**
     * getCatalog purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public CatalogConfig getCatalog() {
        return catalog;
    }

    /**
     * getGlobal purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public GlobalDTO getGlobal() {
        return global;
    }

    /**
     * getWfs purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public WFSConfig getWfs() {
        return wfs;
    }

    /**
     * getWms purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public WMSConfig getWms() {
        return wms;
    }

    /**
     * setCatalog purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param catalog
     */
    public void setCatalog(CatalogConfig catalog) {
        this.catalog = catalog;
    }

    /**
     * setGlobal purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param global
     */
    public void setGlobal(GlobalDTO global) {
        this.global = global;
    }

    /**
     * setWfs purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param wfs
     */
    public void setWfs(WFSConfig wfs) {
        this.wfs = wfs;
    }

    /**
     * setWms purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param wms
     */
    public void setWms(WMSConfig wms) {
        this.wms = wms;
    }
}

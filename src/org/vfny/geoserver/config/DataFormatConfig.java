/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.opengis.coverage.grid.*;
import org.geotools.data.coverage.grid.GridFormatFactorySpi;
import org.geotools.data.coverage.grid.GridFormatFinder;
import org.vfny.geoserver.action.data.DataFormatUtils;
import org.vfny.geoserver.global.dto.FormatInfoDTO;


/**
 * DataStoreInfo purpose.
 * 
 * <p>
 * Used to describe a datastore, typically one specified in the catalog.xml
 * config file.
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: DataStoreConfig.java,v 1.11 2004/04/30 02:49:24 sploreg Exp $
 */
public class DataFormatConfig {
    /** unique datasore identifier */
    private String id;

//    /** unique namespace to refer to this datastore */
//    private String nameSpaceId;
    private String type;
    private String url;

    /** wether this data store is enabled */
    private boolean enabled = true;

    /** a short description about this data store */
    private String title;

    /** a short description about this data store */
    private String _abstract;

    /** connection parameters to create the DataStoreInfo */
    private Map parameters;

    /** Config ONLY! DataStoreFactory used to test params */
    private GridFormatFactorySpi factory;

    /**
     * Create a new DataStoreConfig from a dataStoreId and factoryDescription
     * 
     * <p>
     * Creates a DataStoreInfo to represent an instance with default data.
     * </p>
     *
     * @param dataStoreId Description of DataStore (see DataStoreUtils)
     * @param factoryDescription DOCUMENT ME!
     *
     * @see defaultSettings()
     */
    public DataFormatConfig(String dataFormatId, String factoryDescription) {
        this(dataFormatId, DataFormatUtils.aquireFactory(factoryDescription));
    }

    /** Creates a new DataStoreConfig for the provided factory. */
    public DataFormatConfig(String dataFormatId, GridFormatFactorySpi factory) {
    	this.factory = factory;
        id = dataFormatId;
        //nameSpaceId = "";
        type = factory.createFormat().getName();
        url = "file:data/coverages/";
        enabled = true;
        title = "";
        _abstract = "";
        parameters = DataFormatUtils.defaultParams(factory);
    }

    /**
     * DataStoreInfo constructor.
     * 
     * <p>
     * Creates a copy of the DataStoreInfoDTO provided. All the datastructures
     * are cloned.
     * </p>
     *
     * @param dto The datastore to copy.
     */
    public DataFormatConfig(FormatInfoDTO dto) {
        reset(dto);
    }

    /**
     * Called to update Config class based on DTO information
     *
     * @param dto DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public void reset(FormatInfoDTO dto) {
        if (dto == null) {
            throw new NullPointerException("Non null FormatInfoDTO required");
        }

        factory = DataFormatUtils.aquireFactory(dto.getParameters(), dto.getType());

        id = dto.getId();
//        nameSpaceId = dto.getNameSpaceId();
        type = dto.getType();
        url = dto.getUrl();
        enabled = dto.isEnabled();
        _abstract = dto.getAbstract();
        parameters = new HashMap(dto.getParameters());
    }

    /**
     * Implement loadDTO.
     * 
     * <p>
     * Populates the data fields with the DataStoreInfoDTO provided.
     * </p>
     *
     * @param ds the DataStoreInfoDTO to use.
     *
     * @throws NullPointerException DOCUMENT ME!
     *
     * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
     */
    public void update(FormatInfoDTO ds) {
        if (ds == null) {
            throw new NullPointerException(
                "FormatInfo Data Transfer Object required");
        }

        id = ds.getId();
//        nameSpaceId = ds.getNameSpaceId();
        type = ds.getType();
        url = ds.getUrl();
        enabled = ds.isEnabled();
        _abstract = ds.getAbstract();

        try {
            parameters = new HashMap(ds.getParameters()); //clone?
        } catch (Exception e) {
        	parameters = new HashMap();
        }
    }

    /**
     * Implement toDTO.
     * 
     * <p>
     * Create a DataStoreInfoDTO from the current config object.
     * </p>
     *
     * @return The data represented as a DataStoreInfoDTO
     *
     * @see org.vfny.geoserver.config.DataStructure#toDTO()
     */
    public FormatInfoDTO toDTO() {
    	FormatInfoDTO ds = new FormatInfoDTO();
        ds.setId(id);
//        ds.setNameSpaceId(nameSpaceId);
        ds.setType(type);
        ds.setUrl(url);
        ds.setEnabled(enabled);
        ds.setAbstract(_abstract);

        try {
            ds.setParameters(new HashMap(parameters));
        } catch (Exception e) {
            // default already created  	
        }

        return ds;
    }

    /**
     * getAbstract purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * getConnectionParams purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public Map getParameters() {
        return parameters;
    }

    /**
     * isEnabled purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * This is the DataStore ID
     * 
     * <p>
     * 
     * </p>
     *
     * @return
     */
    public String getId() {
        return id;
    }

//    /**
//     * getNameSpace purpose.
//     * 
//     * <p>
//     * Description ...
//     * </p>
//     *
//     * @return
//     */
//    public String getNameSpaceId() {
//        return nameSpaceId;
//    }

    /**
     * getTitle purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * setAbstract purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setAbstract(String string) {
        if (string != null) {
            _abstract = string;
        }
    }

    /**
     * setConnectionParams purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param map
     */
    public void setParameters(Map map) {
        parameters = map;
    }

    /**
     * setEnabled purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param b
     */
    public void setEnabled(boolean b) {
        enabled = b;
    }

//    /**
//     * setNameSpace purpose.
//     * 
//     * <p>
//     * Description ...
//     * </p>
//     *
//     * @param support
//     */
//    public void setNameSpaceId(String support) {
//        if (support != null) {
//            nameSpaceId = support;
//        }
//    }

    /**
     * setTitle purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setTitle(String string) {
        if (string != null) {
            title = string;
        }
    }

    // Access to Dyanmic Content

    /**
     * It would be nice if we did not throw this away - but life is too short
     *
     * @return Real DataStore generated by this DataStoreConfig
     *
     * @throws IOException If DataStore could not be aquired
     */
    public Format findDataFormat(ServletContext sc) throws IOException {
        return DataFormatUtils.acquireFormat(type,sc);
    }
	/**
	 * @return Returns the factory.
	 */
	public GridFormatFactorySpi getFactory() {
		return factory;
	}
	/**
	 * @param factory The factory to set.
	 */
	public void setFactory(GridFormatFactorySpi factory) {
		this.factory = factory;
	}
/**
 * @return Returns the type.
 */
public String getType() {
	return type;
}
/**
 * @param type The type to set.
 */
public void setType(String type) {
	this.type = type;
}
	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}

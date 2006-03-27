/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.util.Locale;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.opengis.coverage.grid.*;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataStore;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.config.NameSpaceConfig;
import org.vfny.geoserver.config.StyleConfig;


/**
 * Represents a User for GeoServer.
 * 
 * <p>
 * Used as a typesafe Session container. This is an alternative to using calls
 * to request.getAttributes( key ) and casting.
 * </p>
 * 
 * <p>
 * The User object is saved in session scope by ConfigAction:
 * </p>
 * <pre><code>
 * HttpSession session = request.getSession();
 * User user = request.getAttributes( UserContainer.WEB_CONTAINER_KEY );
 * if( user == null ){
 *     user = new UserContainer( request.getLocal() );
 *     session.setAttributes( UserContainer.WEB_CONTAINER_KEY, user );
 * }
 * </code></pre>
 * 
 * <p>
 * This class is based on the UserContainer class outlined in the book
 * "Programming Jakarta Struts" by Chuck Cavaness.
 * </p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author jive
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id: UserContainer.java,v 1.10 2004/03/09 05:38:18 jive Exp $
 */
public class UserContainer implements HttpSessionBindingListener {
    public final static String SESSION_KEY = "GEOSERVER.USER";

	/**
	 * User name for this user
	 * 
	 * @uml.property name="username" multiplicity="(0 1)"
	 */
	private String username;

	/**
	 * User's locale
	 * 
	 * @uml.property name="locale" multiplicity="(0 1)"
	 */
	private Locale locale;

	/**
	 * Selected dataFormatId
	 * 
	 * @uml.property name="dataFormatID" multiplicity="(0 1)"
	 */
	private String dataFormatID;

	/**
	 * Selected dataStoreId
	 * 
	 * @uml.property name="dataStoreID" multiplicity="(0 1)"
	 */
	private String dataStoreID;

	/**
	 * Selected prefix
	 * 
	 * @uml.property name="prefix" multiplicity="(0 1)"
	 */
	private String prefix;

	/**
	 * Selected CoverageStoreConfig held in session for creation/editing.
	 * 
	 * @uml.property name="dataFormatConfig"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private CoverageStoreConfig dataFormatConfig;

	/**
	 * Selected DataStoreConfig held in session for creation/editing.
	 * 
	 * @uml.property name="dataStoreConfig"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private DataStoreConfig dataStoreConfig;

	/**
	 * Cached Format being worked on.
	 * 
	 * <p>
	 * This should agree with the value of dataFormatConfig.
	 * </p>
	 * 
	 * @uml.property name="dataFormat"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private Format dataFormat;

	/**
	 * Cached DataStore being worked on.
	 * 
	 * <p>
	 * This should agree with the value of dataStoreConfig.
	 * </p>
	 * 
	 * @uml.property name="dataStore"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private DataStore dataStore;

	/**
	 * Cached NamespaceConfig held in session for creation/editing.
	 * 
	 * @uml.property name="namespaceConfig"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private NameSpaceConfig namespaceConfig;

	/**
	 * Selected styleId
	 * 
	 * @uml.property name="style"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private StyleConfig style;

	/**
	 * Selected FeatureType Config held in session for editing/creation.
	 * 
	 * <p>
	 * Pending: Make change over to UserContainer.
	 * </p>
	 * 
	 * @uml.property name="featureTypeConfig"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private FeatureTypeConfig featureTypeConfig;

	/**
	 * 
	 * @uml.property name="coverageConfig"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private CoverageConfig coverageConfig;

	/**
	 * Cached FeatureType being worked on.
	 * 
	 * <p>
	 * This should agree with the value of featureTypeConfig.
	 * </p>
	 * 
	 * <p></p>
	 * 
	 * @uml.property name="featureType"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private FeatureType featureType;

	/**
	 * 
	 * @uml.property name="coverage"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private GridCoverage2D coverage;

	/**
	 * Selected AttributeType being worked on.
	 * 
	 * <p>
	 * Pending: Make change over to User Container.
	 * </p>
	 * 
	 * <p></p>
	 * 
	 * @uml.property name="attributeTypeConfig"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private AttributeTypeInfoConfig attributeTypeConfig;

	/**
	 * Cached AttributeType being worked on.
	 * 
	 * <p>
	 * This should agree with the value of attributeTypeConfig.
	 * </p>
	 * 
	 * <p></p>
	 * 
	 * @uml.property name="attributeType"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private AttributeType attributeType;

	/**
	 * New DataStore info before it is added to DataConfig.
	 * 
	 * <p>
	 * Unlike the DataStores in DataConfig this one does not yet have to work.
	 * </p>
	 * 
	 * @uml.property name="newDataStore"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private DataStoreConfig newDataStore;


    /**
     * Create User Container for the current locale
     */
    public UserContainer() {
        this(Locale.getDefault());
    }

    /**
     * Create User Container for the provided locale
     *
     * @param local DOCUMENT ME!
     */
    public UserContainer(Locale local) {
    }

	/**
	 * User's Locale.
	 * 
	 * <p>
	 * Used to format messages. Should be used in conjunction with
	 * internatalization support.
	 * </p>
	 * 
	 * @return Locale for the User.
	 * 
	 * @uml.property name="locale"
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Set the user's Locale.
	 * 
	 * @param locale User's locale.
	 * 
	 * @uml.property name="locale"
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}


    /**
     * Session callback.
     *
     * @param arg0
     *
     * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent arg0) {
        // not needed
    }

    /**
     * Clean up user resources when unbound from session.
     *
     * @param arg0
     *
     * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueUnbound(HttpSessionBindingEvent arg0) {
        cleanUp();
    }

    /**
     * Clean up user resources.
     */
    private void cleanUp() {
        locale = null;
        dataStoreID = null;
    }

	/**
	 * Access attributeType property.
	 * 
	 * @return Returns the attributeType.
	 * 
	 * @uml.property name="attributeType"
	 */
	public AttributeType getAttributeType() {
		return attributeType;
	}

	/**
	 * Set attributeType to attributeType.
	 * 
	 * @param attributeType The attributeType to set.
	 * 
	 * @uml.property name="attributeType"
	 */
	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	/**
	 * Access attributeTypeConfig property.
	 * 
	 * @return Returns the attributeTypeConfig.
	 * 
	 * @uml.property name="attributeTypeConfig"
	 */
	public AttributeTypeInfoConfig getAttributeTypeConfig() {
		return attributeTypeConfig;
	}

	/**
	 * Set attributeTypeConfig to attributeTypeConfig.
	 * 
	 * @param attributeTypeConfig The attributeTypeConfig to set.
	 * 
	 * @uml.property name="attributeTypeConfig"
	 */
	public void setAttributeTypeConfig(
		AttributeTypeInfoConfig attributeTypeConfig) {
		this.attributeTypeConfig = attributeTypeConfig;
	}

	/**
	 * Access dataStore property.
	 * 
	 * @return Returns the dataStore.
	 * 
	 * @uml.property name="dataStore"
	 */
	public DataStore getDataStore() {
		return dataStore;
	}

	/**
	 * Set dataStore to dataStore.
	 * 
	 * @param dataStore The dataStore to set.
	 * 
	 * @uml.property name="dataStore"
	 */
	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	/**
	 * Access dataStoreConfig property.
	 * 
	 * @return Returns the dataStoreConfig.
	 * 
	 * @uml.property name="dataStoreConfig"
	 */
	public DataStoreConfig getDataStoreConfig() {
		return dataStoreConfig;
	}

	/**
	 * Set dataStoreConfig to dataStoreConfig.
	 * 
	 * @param dataStoreConfig The dataStoreConfig to set.
	 * 
	 * @uml.property name="dataStoreConfig"
	 */
	public void setDataStoreConfig(DataStoreConfig dataStoreConfig) {
		this.dataStoreConfig = dataStoreConfig;
	}


    /**
     * Access dataStoreID property.
     *
     * @return Returns the dataStoreID.
     */
    public String getDataStoreID() {
        return dataStoreConfig.getId();
    }

	/**
	 * Access featureType property.
	 * 
	 * @return Returns the featureType.
	 * 
	 * @uml.property name="featureType"
	 */
	public FeatureType getFeatureType() {
		return featureType;
	}

	/**
	 * Access featureTypeConfig property.
	 * 
	 * @return Returns the featureTypeConfig.
	 * 
	 * @uml.property name="featureTypeConfig"
	 */
	public FeatureTypeConfig getFeatureTypeConfig() {
		return featureTypeConfig;
	}

	/**
	 * Access newDataStore property.
	 * 
	 * @return Returns the newDataStore.
	 * 
	 * @uml.property name="newDataStore"
	 */
	public DataStoreConfig getNewDataStore() {
		return newDataStore;
	}

	/**
	 * Set newDataStore to newDataStore.
	 * 
	 * @param newDataStore The newDataStore to set.
	 * 
	 * @uml.property name="newDataStore"
	 */
	public void setNewDataStore(DataStoreConfig newDataStore) {
		this.newDataStore = newDataStore;
	}

	/**
	 * Access username property.
	 * 
	 * @return Returns the username.
	 * 
	 * @uml.property name="username"
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set username to username.
	 * 
	 * @param username The username to set.
	 * 
	 * @uml.property name="username"
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Set dataStoreID to dataStoreID.
	 * 
	 * @param dataStoreID The dataStoreID to set.
	 * 
	 * @uml.property name="dataStoreID"
	 */
	public void setDataStoreID(String dataStoreID) {
		this.dataStoreID = dataStoreID;
	}

	/**
	 * Set featureType to featureType.
	 * 
	 * @param featureType The featureType to set.
	 * 
	 * @uml.property name="featureType"
	 */
	public void setFeatureType(FeatureType featureType) {
		this.featureType = featureType;
	}

	/**
	 * Set featureTypeConfig to featureTypeConfig.
	 * 
	 * @param featureTypeConfig The featureTypeConfig to set.
	 * 
	 * @uml.property name="featureTypeConfig"
	 */
	public void setFeatureTypeConfig(FeatureTypeConfig featureTypeConfig) {
		this.featureTypeConfig = featureTypeConfig;
	}

	/**
	 * Access namespaceConfig property.
	 * 
	 * @return Returns the namespaceConfig.
	 * 
	 * @uml.property name="namespaceConfig"
	 */
	public NameSpaceConfig getNamespaceConfig() {
		return namespaceConfig;
	}

	/**
	 * Set namespaceConfig to namespaceConfig.
	 * 
	 * @param namespaceConfig The namespaceConfig to set.
	 * 
	 * @uml.property name="namespaceConfig"
	 */
	public void setNamespaceConfig(NameSpaceConfig namespaceConfig) {
		this.namespaceConfig = namespaceConfig;
	}

	/**
	 * Access prefix property.
	 * 
	 * @return Returns the prefix.
	 * 
	 * @uml.property name="prefix"
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Set prefix to prefix.
	 * 
	 * @param prefix The prefix to set.
	 * 
	 * @uml.property name="prefix"
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Access style property.
	 * 
	 * @return Returns the style.
	 * 
	 * @uml.property name="style"
	 */
	public StyleConfig getStyle() {
		return style;
	}

	/**
	 * Set style to style.
	 * 
	 * @param style The style to set.
	 * 
	 * @uml.property name="style"
	 */
	public void setStyle(StyleConfig style) {
		this.style = style;
	}

	/**
	 * @return Returns the dataFormat.
	 * 
	 * @uml.property name="dataFormat"
	 */
	public Format getDataFormat() {
		return dataFormat;
	}

	/**
	 * @param dataFormat The dataFormat to set.
	 * 
	 * @uml.property name="dataFormat"
	 */
	public void setDataFormat(Format dataFormat) {
		this.dataFormat = dataFormat;
	}

	/**
	 * @return Returns the dataFormatConfig.
	 * 
	 * @uml.property name="dataFormatConfig"
	 */
	public CoverageStoreConfig getDataFormatConfig() {
		return dataFormatConfig;
	}

	/**
	 * @param dataFormatConfig The dataFormatConfig to set.
	 * 
	 * @uml.property name="dataFormatConfig"
	 */
	public void setDataFormatConfig(CoverageStoreConfig dataFormatConfig) {
		this.dataFormatConfig = dataFormatConfig;
	}

	/**
	 * @return Returns the dataFormatID.
	 * 
	 * @uml.property name="dataFormatID"
	 */
	public String getDataFormatID() {
		return dataFormatID;
	}

	/**
	 * @param dataFormatID The dataFormatID to set.
	 * 
	 * @uml.property name="dataFormatID"
	 */
	public void setDataFormatID(String dataFormatID) {
		this.dataFormatID = dataFormatID;
	}

	/**
	 * @return Returns the coverageConfig.
	 * 
	 * @uml.property name="coverageConfig"
	 */
	public CoverageConfig getCoverageConfig() {
		return coverageConfig;
	}

	/**
	 * @param coverageConfig The coverageConfig to set.
	 * 
	 * @uml.property name="coverageConfig"
	 */
	public void setCoverageConfig(CoverageConfig coverageConfig) {
		this.coverageConfig = coverageConfig;
	}

	/**
	 * @return Returns the coverage.
	 * 
	 * @uml.property name="coverage"
	 */
	public GridCoverage2D getCoverage() {
		return coverage;
	}

	/**
	 * @param coverage The coverage to set.
	 * 
	 * @uml.property name="coverage"
	 */
	public void setCoverage(GridCoverage2D coverage) {
		this.coverage = coverage;
	}

}

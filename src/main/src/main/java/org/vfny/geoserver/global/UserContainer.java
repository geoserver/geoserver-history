/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataStore;
import org.opengis.coverage.grid.*;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.config.NameSpaceConfig;
import org.vfny.geoserver.config.StyleConfig;
import java.util.Locale;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;


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
 * @author $Author: jive $ (last modification)
 * @version $Id$
 */
public class UserContainer implements HttpSessionBindingListener {
    public final static String SESSION_KEY = "GEOSERVER.USER";

    /** User name for this user */
    private String username;

    /** User's locale */
    private Locale locale;

    /**
     * Selected dataFormatId
     *
     */
    private String dataFormatID;

    /**
    /** Selected dataStoreId */
    private String dataStoreID;

    /** Selected prefix */
    private String prefix;

    /**
     * Selected CoverageStoreConfig held in session for creation/editing.
     *
     */
    private CoverageStoreConfig dataFormatConfig;

    /**
    * Selected DataStoreConfig held in session for creation/editing.
    */
    private DataStoreConfig dataStoreConfig;

    /**
     * Cached Format being worked on.
     *
     * <p>
     * This should agree with the value of dataFormatConfig.
     * </p>
     *
     */
    private Format dataFormat;

    /**
    * Cached DataStore being worked on.
    *
    * <p>
    * This should agree with the value of dataStoreConfig.
    * </p>
    */
    private DataStore dataStore;

    /**
     * Cached NamespaceConfig held in session for creation/editing.
     */
    private NameSpaceConfig namespaceConfig;

    /** Selected styleId */
    private StyleConfig style;

    /**
     * Selected FeatureType Config held in session for editing/creation.
     *
     * <p>
     * Pending: Make change over to UserContainer.
     * </p>
         */
    private FeatureTypeConfig featureTypeConfig;

    /**
     *
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
    */
    private SimpleFeatureType featureType;

    /**
     *
     */
    private GridCoverage2D coverage;

    /**
    * Selected AttributeDescriptor being worked on.
    *
    * <p>
    * Pending: Make change over to User Container.
    * </p>
    *
    * <p></p>
    */
    private AttributeTypeInfoConfig attributeTypeConfig;

    /**
     * Cached AttributeDescriptor being worked on.
     *
     * <p>
     * This should agree with the value of attributeTypeConfig.
     * </p>
     *
     * <p></p>
     */
    private AttributeDescriptor attributeType;

    /**
     * New DataStore info before it is added to DataConfig.
     *
     * <p>
     * Unlike the DataStores in DataConfig this one does not yet have to work.
     * </p>
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
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Set the user's Locale.
     *
     * @param locale User's locale.
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
     */
    public AttributeDescriptor getAttributeType() {
        return attributeType;
    }

    /**
     * Set attributeType to attributeType.
     *
     * @param attributeType The attributeType to set.
     */
    public void setAttributeType(AttributeDescriptor attributeType) {
        this.attributeType = attributeType;
    }

    /**
     * Access attributeTypeConfig property.
     *
     * @return Returns the attributeTypeConfig.
     */
    public AttributeTypeInfoConfig getAttributeTypeConfig() {
        return attributeTypeConfig;
    }

    /**
     * Set attributeTypeConfig to attributeTypeConfig.
     *
     * @param attributeTypeConfig The attributeTypeConfig to set.
     */
    public void setAttributeTypeConfig(AttributeTypeInfoConfig attributeTypeConfig) {
        this.attributeTypeConfig = attributeTypeConfig;
    }

    /**
     * Access dataStore property.
     *
     * @return Returns the dataStore.
     */
    public DataStore getDataStore() {
        return dataStore;
    }

    /**
     * Set dataStore to dataStore.
     *
     * @param dataStore The dataStore to set.
     */
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Access dataStoreConfig property.
     *
     * @return Returns the dataStoreConfig.
     */
    public DataStoreConfig getDataStoreConfig() {
        return dataStoreConfig;
    }

    /**
     * Set dataStoreConfig to dataStoreConfig.
     *
     * @param dataStoreConfig The dataStoreConfig to set.
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
     */
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    /**
     * Access featureTypeConfig property.
     *
     * @return Returns the featureTypeConfig.
     */
    public FeatureTypeConfig getFeatureTypeConfig() {
        return featureTypeConfig;
    }

    /**
     * Access newDataStore property.
     *
     * @return Returns the newDataStore.
     */
    public DataStoreConfig getNewDataStore() {
        return newDataStore;
    }

    /**
     * Set newDataStore to newDataStore.
     *
     * @param newDataStore The newDataStore to set.
     */
    public void setNewDataStore(DataStoreConfig newDataStore) {
        this.newDataStore = newDataStore;
    }

    /**
     * Access username property.
     *
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set username to username.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set dataStoreID to dataStoreID.
     *
     * @param dataStoreID The dataStoreID to set.
     */
    public void setDataStoreID(String dataStoreID) {
        this.dataStoreID = dataStoreID;
    }

    /**
     * Set featureType to featureType.
     *
     * @param featureType The featureType to set.
     */
    public void setFeatureType(SimpleFeatureType featureType) {
        this.featureType = featureType;
    }

    /**
     * Set featureTypeConfig to featureTypeConfig.
     *
     * @param featureTypeConfig The featureTypeConfig to set.
     */
    public void setFeatureTypeConfig(FeatureTypeConfig featureTypeConfig) {
        this.featureTypeConfig = featureTypeConfig;
    }

    /**
     * Access namespaceConfig property.
     *
     * @return Returns the namespaceConfig.
     */
    public NameSpaceConfig getNamespaceConfig() {
        return namespaceConfig;
    }

    /**
     * Set namespaceConfig to namespaceConfig.
     *
     * @param namespaceConfig The namespaceConfig to set.
     */
    public void setNamespaceConfig(NameSpaceConfig namespaceConfig) {
        this.namespaceConfig = namespaceConfig;
    }

    /**
     * Access prefix property.
     *
     * @return Returns the prefix.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Set prefix to prefix.
     *
     * @param prefix The prefix to set.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Access style property.
     *
     * @return Returns the style.
     */
    public StyleConfig getStyle() {
        return style;
    }

    /**
     * Set style to style.
     *
     * @param style The style to set.
     */
    public void setStyle(StyleConfig style) {
        this.style = style;
    }

    /**
     * @return Returns the dataFormat.
     *
     */
    public Format getDataFormat() {
        return dataFormat;
    }

    /**
     * @param dataFormat The dataFormat to set.
     *
     */
    public void setDataFormat(Format dataFormat) {
        this.dataFormat = dataFormat;
    }

    /**
     * @return Returns the dataFormatConfig.
     *
     */
    public CoverageStoreConfig getDataFormatConfig() {
        return dataFormatConfig;
    }

    /**
     * @param dataFormatConfig The dataFormatConfig to set.
     *
     */
    public void setDataFormatConfig(CoverageStoreConfig dataFormatConfig) {
        this.dataFormatConfig = dataFormatConfig;
    }

    /**
     * @return Returns the dataFormatID.
     *
     */
    public String getDataFormatID() {
        return dataFormatID;
    }

    /**
     * @param dataFormatID The dataFormatID to set.
     *
     */
    public void setDataFormatID(String dataFormatID) {
        this.dataFormatID = dataFormatID;
    }

    /**
     * @return Returns the coverageConfig.
     *
     */
    public CoverageConfig getCoverageConfig() {
        return coverageConfig;
    }

    /**
     * @param coverageConfig The coverageConfig to set.
     *
     */
    public void setCoverageConfig(CoverageConfig coverageConfig) {
        this.coverageConfig = coverageConfig;
    }

    /**
     * @return Returns the coverage.
     *
     */
    public GridCoverage2D getCoverage() {
        return coverage;
    }

    /**
     * @param coverage The coverage to set.
     *
     */
    public void setCoverage(GridCoverage2D coverage) {
        this.coverage = coverage;
    }
}

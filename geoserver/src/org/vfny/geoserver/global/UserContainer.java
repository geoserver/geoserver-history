/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geotools.data.DataStore;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
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
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: UserContainer.java,v 1.4 2004/01/21 00:26:07 dmzwiers Exp $
 */
public class UserContainer implements HttpSessionBindingListener {
    public final static String SESSION_KEY = "GEOSERVER.USER";

    /** User name for this user */
    public String username;

    /** User's locale */
    private Locale locale;

    /** Selected dataStoreId */
    private String dataStoreID;

    /**
     * Selected DataStoreConfig held in session for creation.
     * 
     * <p>
     * Pending: Make the change over to UserContainer.
     * </p>
     */
    private DataStoreConfig dataStoreConfig;

    /**
     * Cached DataStore being worked on.
     * 
     * <p>
     * This should agree with the value of dataStoreConfig.
     * </p>
     */
    private DataStore dataStore;

    /**
     * Selected FeatureType Config held in session for editing/creation.
     * 
     * <p>
     * Pending: Make change over to UserContainer.
     * </p>
     */
    private FeatureTypeConfig featureTypeConfig;

    /**
     * Cached FeatureType being worked on.
     * 
     * <p>
     * This should agree with the value of featureTypeConfig.
     * </p>
     * 
     * <p></p>
     */
    private FeatureType featureType;

    /**
     * Selected AttributeType being worked on.
     * 
     * <p>
     * Pending: Make change over to User Container.
     * </p>
     * 
     * <p></p>
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
     */
    private AttributeType attributeType;

    /**
     * New DataStore info before it is added to DataConfig.
     * 
     * <p>
     * Unlike the DataStores in DataConfig this one does not yet have to work.
     * </p>
     */
    public DataStoreConfig newDataStore;

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
    public AttributeType getAttributeType() {
        return attributeType;
    }

    /**
     * Set attributeType to attributeType.
     *
     * @param attributeType The attributeType to set.
     */
    public void setAttributeType(AttributeType attributeType) {
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
    public void setAttributeTypeConfig(
        AttributeTypeInfoConfig attributeTypeConfig) {
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
    public FeatureType getFeatureType() {
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
    public void setFeatureType(FeatureType featureType) {
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
}

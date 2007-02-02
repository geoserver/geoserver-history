/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;


/**
 * Bean containing the properties of an Open Web Service (OWS).
 * <p>
 * An OWS bean is a proper java bean which contains properties common to
 * various types of open web services. This class is intended to be subclassed.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class OWS {
    /**
     * Service enabled flag
     */
    private boolean enabled;

    /**
     * Url pointing to the service
     */
    private URL onlineResource;

    /**
     * Name
     */
    private String name;

    /**
     * Title
     */
    private String title;

    /**
     * Abstract
     */
    private String serverAbstract;

    /**
     * Fees for using the service.
     */
    private String fees;

    /**
     * Service access constraints.
     */
    private String accessConstraints;

    /**
     * Maintainer of the service.
     */
    private String maintainer;

    /**
     * Character set to use.
     */
    private Charset charSet;

    /**
     * List of keywords associated with the service.
     */
    private String[] keywords;

    /**
     * Client properties
     */
    private Map clientProperties;

    /**
     * Flag indicating wether the service should be verbose or not.
     */
    private boolean isVerbose;

    /**
     * Number of decimals used when encoding data.
     */
    private int numDecimals;

    /**
     * Local used to look up schemas.
     */
    private String schemaBaseURL;

    /**
    * <p>
    * Returns whether is service is enabled.
    * </p>
    *
    * @return true when enabled.
    */
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * <p>
     * Returns the Online Resource for this Service.
     * </p>
     *
     * @return URL The Online resource.
     */
    public URL getOnlineResource() {
        return onlineResource;
    }

    public void setOnlineResource(URL onlineResource) {
        this.onlineResource = onlineResource;
    }

    /**
     * <p>
     * A description of this service.
     * </p>
     *
     * @return String This Service's abstract.
     */
    public String getAbstract() {
        return serverAbstract;
    }

    public void setAbtract(String serverAbstract) {
        this.serverAbstract = serverAbstract;
    }

    /**
     * <p>
     * A description of this service's access constraints.
     * </p>
     *
     * @return String This service's access constraints.
     */
    public String getAccessConstraints() {
        return accessConstraints;
    }

    public void setAccessConstraints(String accessConstraints) {
        this.accessConstraints = accessConstraints;
    }

    /**
     * <p>
     * A description of the fees for this service.
     * </p>
     *
     * @return String the fees for this service.
     */
    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    /**
     * <p>
     * A list of the keywords for this service.
     * </p>
     *
     * @return List keywords for this service
     */
    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    /**
     * <p>
     * The name of the maintainer for this service.
     * </p>
     *
     * @return String maintainer for this service.
     */
    public String getMaintainer() {
        return maintainer;
    }

    public void setMaintainer(String maintainer) {
        this.maintainer = maintainer;
    }

    /**
     * <p>
     * The name for this service.
     * </p>
     *
     * @return String the service's name.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>
     * The title for this service.
     * </p>
     *
     * @return String the service's title.
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the character set for the service.
     */
    public Charset getCharSet() {
        return charSet;
    }

    public void setCharSet(Charset charSet) {
        this.charSet = charSet;
    }

    /**
     * <p>
     * Client properties for the service.
     * </p>
     */
    public Map getClientProperties() {
        return clientProperties;
    }

    public void setClientProperties(Map clientProperties) {
        this.clientProperties = clientProperties;
    }

    /**
     * Flag indicating wether the service should be verbose, for things like
     * responding to requests, etc...
     *
     * @return True if verboseness on, other wise false.
     */
    public boolean isVerbose() {
        return isVerbose;
    }

    public void setVerbose(boolean isVerbose) {
        this.isVerbose = isVerbose;
    }

    /**
     * The default number of decimals allowed in the data.
     *
     * @return int the default number of decimals allowed in the data.
     */
    public int getNumDecimals() {
        return numDecimals;
    }

    /**
     * Sets the number of decimals to be used with data.
     *
     * @param numDecimals Number of decimals.
     */
    public void setNumDecimals(int numDecimals) {
        this.numDecimals = numDecimals;
    }

    /**
     * Sets the base url from which to locate schemas from.
     *
     * @param schemaBaseURL
     */
    public void setSchemaBaseURL(String schemaBaseURL) {
        this.schemaBaseURL = schemaBaseURL;
    }

    /**
     * @return The base url from which to locate schemas from.
     */
    public String getSchemaBaseURL() {
        return schemaBaseURL;
    }
}

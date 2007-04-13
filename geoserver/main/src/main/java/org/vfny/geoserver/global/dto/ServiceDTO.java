/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import org.vfny.geoserver.global.MetaDataLink;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Data Transfer Object representing GeoServer Service information.
 *
 * <p>
 * ServiceConfig is intended to be extended to provide some basic data storage
 * facilities.  This class represents the basic properties of a web service.
 * </p>
 *
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 *
 * <p>
 * It is very tempting to force Web Services to completely define their own DTO
 * elements (rather than trying for reuse using subclassing or aggregation) -
 * simply to force each service to document what <b>it</b> means by each of
 * these peices of information.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id$
 */
public final class ServiceDTO implements DataTransferObject {
    /**
     * Represents when the Web Service is enabled/disabled.
     *
     * <p>
     * Example: <code>true</code>
     * </p>
     */
    private boolean enabled;

    /**
     * Online Reference URL for the web service.
     *
     * <p>
     * A location to look for when additional assistance is required.
     * </p>
     *
     * <p>
     * Example: <code>new URL("http://www.openplans.org/")</code>
     * </p>
     */
    private URL onlineResource;

    /**
     * The name of the service.
     *
     * <p>
     * Example: <code>FreeWFS</code>
     * </p>
     *
     * <p>
     * It is not clear from the examples if this name allows whitespace?
     * </p>
     */
    private String name;

    /**
     * The title of the service.
     *
     * <p>
     * Example: <code>The Open Planning Project Basemap Server</code>
     * </p>
     */
    private String title;

    /**
     * A short abstract about the service.
     *
     * <p>
     * Example:
     * <pre><code>
     * This is a test server.  It contains some basemap data from New York City.
     * </code></pre>
     * </p>
     */
    private String serverAbstract;

    /**
     * A list of keywords associated with the service.
     *
     * <p>
     * Example: <code>new String[]{"WFS","New York"}</code>
     * </p>
     */
    private List keywords = new ArrayList();

    /**
     * The fees associated with the service.
     *
     * <p>
     * When there are not any fees, the value  "NONE" is used.
     * </p>
     *
     * <p>
     * Example: <code>NONE</code>
     * </p>
     */
    private String fees;

    /**
     * The access constraints associated with the service. When there are not
     * any, the value "NONE" is used.
     *
     * <p>
     * Example: <code>"NONE"</code>
     * </p>
     */
    private String accessConstraints;

    /**
     * Name of the person who maintains the web service. Should ideally be
     * contact  information such as a email address.
     *
     * <p>
     * Example: <code>"The Open Planning Project"</code>
     * </p>
     */
    private String maintainer;
    private MetaDataLink metadataLink;

    /**
     * The output strategy to use when the service is performing a response.
     * <p>
     * Examples: SPEED,BUFFER,etc...
     * </p>
     */
    private String strategy;

    /**
     * The size of the buffer if the "PARTIAL-BUFFER" strategy is being used.
     */
    private int partialBufferSize;

    /**
     * ServiceConfig constructor.
     *
     * <p>
     * does nothing
     * </p>
     *
     * @see defaultSettings()
     */
    public ServiceDTO() {
    }

    /**
     * ServiceConfig constructor.
     *
     * <p>
     * This is equivalent to calling the clone method. When a null value is
     * passed in,  the default values are used. All non-primary datatypes are
     * cloned with the  exception of Strings (which have a singleton hash
     * table in memory representation).
     * </p>
     *
     * @param dto The ServiceConfig object to copy into the new ServiceConfig
     *        object.
     *
     * @throws NullPointerException If dto is null
     *
     * @see defaultSettings()
     * @see clone()
     */
    public ServiceDTO(ServiceDTO dto) {
        if (dto == null) {
            throw new NullPointerException("ServiceDTO object required");
        }

        enabled = dto.isEnabled();
        name = dto.getName();
        title = dto.getTitle();
        serverAbstract = dto.getAbstract();
        keywords = new ArrayList(dto.getKeywords());
        fees = dto.getFees();
        accessConstraints = dto.getAccessConstraints();
        maintainer = dto.getMaintainer();
        onlineResource = dto.getOnlineResource();
        metadataLink = dto.getMetadataLink();
        strategy = dto.getStrategy();
    }

    /**
     * Implements clone.
     *
     * @return An instance of a ServiceConfig object which represents a copy of
     *         the existing ServiceConfig Object.
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new ServiceDTO(this);
    }

    /**
     * Implement equals.
     *
     * @param other The ServiceConfig object which will be tested.
     *
     * @return true when the classes are equal.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if ((other == null) || !(other instanceof ServiceDTO)) {
            return false;
        }

        ServiceDTO dto = (ServiceDTO) other;

        if (enabled != dto.isEnabled()) {
            return false;
        }

        if ((name != null) ? (!name.equals(dto.name)) : (dto.name != null)) {
            return false;
        }

        if ((title != null) ? (!title.equals(dto.title)) : (dto.title != null)) {
            return false;
        }

        if ((serverAbstract != null) ? (!serverAbstract.equals(dto.getAbstract()))
                                         : (dto.serverAbstract != null)) {
            return false;
        }

        if (!keywords.equals(dto.keywords)) {
            return false;
        }

        if ((fees != null) ? (!fees.equals(dto.fees)) : (dto.fees != null)) {
            return false;
        }

        if ((accessConstraints != null) ? (!accessConstraints.equals(dto.accessConstraints))
                                            : (dto.accessConstraints != null)) {
            return false;
        }

        if ((maintainer != null) ? (!maintainer.equals(dto.maintainer)) : (dto.maintainer != null)) {
            return false;
        }

        if ((metadataLink != null) ? (!metadataLink.equals(dto.metadataLink))
                                       : (dto.metadataLink != null)) {
            return false;
        }

        if ((strategy != null) ? (!strategy.equals(dto.strategy)) : (dto.strategy != null)) {
            return false;
        }

        return true;
    }

    /**
     * Implement hashCode for ServiceDTO.
     *
     * @return Hashcode in agreement with equals method
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (enabled ? 1 : 0) | ((name != null) ? name.hashCode() : 0)
        | ((title != null) ? title.hashCode() : 0)
        | ((serverAbstract != null) ? serverAbstract.hashCode() : 0)
        | ((keywords != null) ? keywords.hashCode() : 0) | ((fees != null) ? fees.hashCode() : 0)
        | ((accessConstraints != null) ? accessConstraints.hashCode() : 0)
        | ((maintainer != null) ? maintainer.hashCode() : 0)
        | ((metadataLink != null) ? metadataLink.hashCode() : 0)
        | ((strategy != null) ? strategy.hashCode() : 0);
    }

    /**
     * Name of Service.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Online Reference URL for the web service.
     *
     * <p>
     * A location to look for when additional assistance is required.
     * </p>
     *
     * <p>
     * Example: <code>new URL("http://www.openplans.org/")</code>
     * </p>
     *
     * @return DOCUMENT ME!
     */
    public URL getOnlineResource() {
        return onlineResource;
    }

    /**
     * The title of the service.
     *
     * <p>
     * Example: <code>The Open Planning Project Basemap Server</code>
     * </p>
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return title;
    }

    /**
     * setName purpose.
     *
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * setOnlineResource purpose.
     *
     * @param url
     */
    public void setOnlineResource(URL url) {
        onlineResource = url;
    }

    /**
     * Sets the title of the service.
     *
     * <p>
     * Example: <code>The Open Planning Project Basemap Server</code>
     * </p>
     *
     * @param string Title of the Service
     */
    public void setTitle(String string) {
        title = string;
    }

    /**
     * getAbstract purpose.
     *
     * @return
     */
    public String getAbstract() {
        return serverAbstract;
    }

    /**
     * The access constraints associated with the service.
     *
     * <p>
     * When there are not any, the value "NONE" is used.
     * </p>
     *
     * <p>
     * Example: <code>"NONE"</code>
     * </p>
     *
     * @return DOCUMENT ME!
     */
    public String getAccessConstraints() {
        return accessConstraints;
    }

    /**
     * Represents when the Web Service is enabled/disabled.
     *
     * @return <code>true</code> is service is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * The fees associated with the service.
     *
     * <p>
     * When there are not any fees, the value  "NONE" is used.
     * </p>
     *
     * <p>
     * Example: <code>NONE</code>
     * </p>
     *
     * @return DOCUMENT ME!
     */
    public String getFees() {
        return fees;
    }

    /**
     * Keywords associated with the service.
     *
     * <p>
     * Example: <code>new String[]{"WFS","New York"}</code>
     * </p>
     *
     * @return DOCUMENT ME!
     */
    public List getKeywords() {
        return keywords;
    }

    /**
     * Name of the party who maintains the web service.
     *
     * <p>
     * Should ideally be contact information such as a email address.
     * </p>
     *
     * <p>
     * Example: <code>"The Open Planning Project"</code>
     * </p>
     *
     * @return The maintainer of this Service
     */
    public String getMaintainer() {
        return maintainer;
    }

    /**
     * Provides a short abstract about the service.
     *
     * <p>
     * Example:
     * <pre><code>
     * This is a test server.  It contains some basemap data from New York City.
     * </code></pre>
     * </p>
     *
     * @param serviceAbstract Abstract describing service
     */
    public void setAbstract(String serviceAbstract) {
        serverAbstract = serviceAbstract;
    }

    /**
     * Provide the access constraints associated with the service.
     *
     * <p>
     * When there are not any, use the value "NONE".
     * </p>
     *
     * <p>
     * Example: <code>"NONE"</code>
     * </p>
     *
     * @param constraints DOCUMENT ME!
     */
    public void setAccessConstraints(String constraints) {
        accessConstraints = constraints;
    }

    /**
     * setEnabled purpose.
     *
     * @param b
     */
    public void setEnabled(boolean b) {
        enabled = b;
    }

    /**
     * Provide the fees associated with the service.
     *
     * <p>
     * When there are not any fees, use the value "NONE".
     * </p>
     *
     * <p>
     * Example: <code>NONE</code>
     * </p>
     *
     * @param string DOCUMENT ME!
     */
    public void setFees(String string) {
        fees = string;
    }

    /**
     * Provide keywords associated with the service.
     *
     * <p>
     * Example: <code>new String[]{"WFS","New York"}</code>
     * </p>
     *
     * @param array DOCUMENT ME!
     */
    public void setKeywords(List array) {
        keywords = array;
    }

    /**
     * Provide the party that maintains the web service.
     *
     * <p>
     * Should ideally be contact information such as a email address.
     * </p>
     *
     * <p>
     * Example: <code>"The Open Planning Project"</code>
     * </p>
     *
     * @param string DOCUMENT ME!
     */
    public void setMaintainer(String string) {
        maintainer = string;
    }

    /**
     * @return Returns the metadataLink.
     *
     */
    public MetaDataLink getMetadataLink() {
        return metadataLink;
    }

    /**
     * @param metadataLink The metadataLink to set.
     *
     */
    public void setMetadataLink(MetaDataLink metadataLink) {
        this.metadataLink = metadataLink;
    }

    /**
     * Sets the strategy used by the service when performing a response.
     *
     */
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    /**
     * @return The strategy used by the service when performing a response.
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * @return The size of the buffer used by the PARTIAL-BUFFER strategy.
     * TODO: this should be factored out when config is splittable among modules.
     */
    public int getPartialBufferSize() {
        return partialBufferSize;
    }

    /**
     * Sets the size of the buffer used by the PARTIAL-BUFFER strategy.
     * TODO: this should be factored out when config is splittable among modules.
     */
    public void setPartialBufferSize(int partialBufferSize) {
        this.partialBufferSize = partialBufferSize;
    }
}

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geoserver.ows.OWS;
import org.vfny.geoserver.global.dto.ServiceDTO;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Default configuration for services. This class represents all the
 * commonalities to the WFS and WMS services.
 * 
 * <p>
 * WFS wfs = new WFS(dto); Service serv = (Service)WFS;
 * System.out.println(serv.getName());
 * </p>
 * 
 * @author Gabriel Rold?n
 * @author Chris Holmes
 * @version $Id$
 * 
 * @see WMS
 * @see WFS
 */
public abstract class Service extends OWS /* extends GlobalLayerSupertype */{
    // private boolean enabled;
    // private URL onlineResource;
    // private String name;
    // private String title;
    // private String serverAbstract;
    // private String[] keywords = new String[0];
    // private String fees;
    // private String accessConstraints;
    // private String maintainer;
    private String strategy;

    private MetaDataLink metadataLink;

    private int partialBufferSize;

    private GeoServer gs;

    private Data dt;

    /**
     * Service constructor.
     * 
     * <p>
     * Stores the new ServiceDTO data for this service.
     * </p>
     * 
     * @param config
     * 
     * @throws NullPointerException
     *             when the param is null
     */
    public Service(ServiceDTO dto) {
        if (dto == null) {
            throw new NullPointerException();
        }

        setEnabled(dto.isEnabled());
        setName(dto.getName());
        setTitle(dto.getTitle());
        setAbtract(dto.getAbstract());
        setKeywords(dto.getKeywords());
        setFees(dto.getFees());
        setAccessConstraints(dto.getAccessConstraints());
        setMaintainer(dto.getMaintainer());
        setOnlineResource(dto.getOnlineResource());
        metadataLink = dto.getMetadataLink();
        strategy = dto.getStrategy();
        partialBufferSize = dto.getPartialBufferSize();
    }

    /**
     * load purpose.
     * <p>
     * loads a new copy of data into this object.
     * </p>
     * 
     * @param dto
     */
    public void load(ServiceDTO dto) {
        if (dto == null) {
            throw new NullPointerException();
        }

        setEnabled(dto.isEnabled());
        setName(dto.getName());
        setTitle(dto.getTitle());
        setAbtract(dto.getAbstract());
        setKeywords(dto.getKeywords());
        setFees(dto.getFees());
        setAccessConstraints(dto.getAccessConstraints());
        setMaintainer(dto.getMaintainer());
        setOnlineResource(dto.getOnlineResource());
        metadataLink = dto.getMetadataLink();
        strategy = dto.getStrategy();
        partialBufferSize = dto.getPartialBufferSize();
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
     * @return The size of the buffer used by the PARTIAL-BUFFER strategy. TODO:
     *         this should be factored out when config is splittable among
     *         modules.
     */
    public int getPartialBufferSize() {
        return partialBufferSize;
    }

    /**
     * Sets the size of the buffer used by the PARTIAL-BUFFER strategy. TODO:
     * this should be factored out when config is splittable among modules.
     */
    public void setPartialBufferSize(int partialBufferSize) {
        this.partialBufferSize = partialBufferSize;
    }

    protected Object toDTO() {
        ServiceDTO dto = new ServiceDTO();
        dto.setAccessConstraints(getAccessConstraints());
        dto.setEnabled(isEnabled());
        dto.setFees(getFees());
        dto.setKeywords(getKeywords());
        dto.setMaintainer(getMaintainer());
        dto.setName(getName());
        dto.setOnlineResource(getOnlineResource());
        dto.setAbstract(getAbstract());
        dto.setTitle(getTitle());
        dto.setMetadataLink(metadataLink);
        dto.setStrategy(strategy);
        dto.setPartialBufferSize(partialBufferSize);

        return dto;
    }

    /**
     * Access dt property.
     * 
     * @return Returns the dt.
     */
    public Data getData() {
        return dt;
    }

    /**
     * Set dt to dt.
     * 
     * @param dt
     *            The dt to set.
     */
    public void setData(Data dt) {
        this.dt = dt;
    }

    /**
     * Access gs property.
     * 
     * @return Returns the gs.
     */
    public GeoServer getGeoServer() {
        return gs;
    }

    /**
     * Set gs to gs.
     * 
     * @param gs
     *            The gs to set.
     */
    public void setGeoServer(GeoServer gs) {
        this.gs = gs;
    }

    /**
     * @return Returns the metadataLink.
     * 
     */
    public MetaDataLink getMetadataLink() {
        return metadataLink;
    }

    /**
     * Returns the character encoding scheme the service shall use to encode all
     * its XML responses in.
     * 
     * @return the character set for the service to encode XML responses in.
     */
    public Charset getCharSet() {
        return getGeoServer().getCharSet();
    }

}

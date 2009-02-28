/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.Wrapper;
import org.geoserver.config.ServiceInfo;
import org.geoserver.ows.OWS;
import org.vfny.geoserver.global.dto.ServiceDTO;


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
 * 
 * @deprecated use {@link ServiceInfo}
 */
public class Service  implements OWS /* extends GlobalLayerSupertype*/ {
    //    private boolean enabled;
    //    private URL onlineResource;
    //    private String name;
    //    private String title;
    //    private String serverAbstract;
    //    private String[] keywords = new String[0];
    //    private String fees;
    //    private String accessConstraints;
    //    private String maintainer;
    //private String strategy;
    //private MetaDataLink metadataLink;
    //private int partialBufferSize;
    //private GeoServer gs;
    //private Data dt;

    protected ServiceInfo service;
    protected org.geoserver.config.GeoServer gs;
    
    ///**
    // * Service constructor.
    // *
    // * <p>
    // * Stores the new ServiceDTO data for this service.
    // * </p>
    // *
    // * @param config
    // *
    // * @throws NullPointerException when the param is null
    // */
    //public Service(ServiceDTO dto) {
    //    if (dto == null) {
    //        throw new NullPointerException();
    //    }
    //
    //    setEnabled(dto.isEnabled());
    //    setName(dto.getName());
    //    setTitle(dto.getTitle());
    //    setAbtract(dto.getAbstract());
    //    setKeywords(dto.getKeywords());
    //    setFees(dto.getFees());
    //    setAccessConstraints(dto.getAccessConstraints());
    //    setMaintainer(dto.getMaintainer());
    //    setOnlineResource(dto.getOnlineResource());
    //    metadataLink = dto.getMetadataLink();
    //    strategy = dto.getStrategy();
    //    partialBufferSize = dto.getPartialBufferSize();
    //}
    public Service( ServiceInfo service, org.geoserver.config.GeoServer gs ) {
        this.service = service;
        this.gs = gs;
    }
    
    /**
     * load purpose.
     * <p>
     * loads a new copy of data into this object.
     * </p>
     * @param dto
     */
    public void load(ServiceDTO dto) {
        if (dto == null) {
            throw new NullPointerException();
        }

        service.setEnabled( dto.isEnabled() );
        service.setName( dto.getName() );
        service.setTitle( dto.getTitle() );
        service.setAbstract( dto.getAbstract() );
        service.getKeywords().clear();
        service.getKeywords().addAll( dto.getKeywords() );
        service.setFees(dto.getFees());
        service.setAccessConstraints(dto.getAccessConstraints());
        service.setOnlineResource(
            dto.getOnlineResource() != null ? dto.getOnlineResource().toString() : null );
        
        if ( dto.getMetadataLink() != null ) {
            service.setMetadataLink(dto.getMetadataLink().getMetadataLink());
        }
        else {
            service.setMetadataLink(null);
        }
        
        setStrategy(dto.getStrategy());
        setPartialBufferSize( dto.getPartialBufferSize() );
        
        //setEnabled(dto.isEnabled());
        //setName(dto.getName());
        //setTitle(dto.getTitle());
        //setAbtract(dto.getAbstract());
        //setKeywords(dto.getKeywords());
        //setFees(dto.getFees());
        //setAccessConstraints(dto.getAccessConstraints());
        //setMaintainer(dto.getMaintainer());
        //setOnlineResource(dto.getOnlineResource());
        //metadataLink = dto.getMetadataLink();
        //stategy = dto.getStrategy();
        //partialBufferSize = dto.getPartialBufferSize();
    }

    /**
     * Sets the strategy used by the service when performing a response.
     *
     */
    public void setStrategy(String strategy) {
        service.setOutputStrategy(strategy);
        //this.strategy = strategy;
    }

    /**
     * @return The strategy used by the service when performing a response.
     */
    public String getStrategy() {
        return service.getOutputStrategy();
        //return strategy;
    }

    /**
     * @return The size of the buffer used by the PARTIAL-BUFFER strategy.
     * TODO: this should be factored out when config is splittable among modules.
     */
    public int getPartialBufferSize() {
        Integer i = (Integer) service.getMetadata().get( "partialBufferSize" );
        return i != null ? i : -1;
        //return partialBufferSize;
    }

    /**
     * Sets the size of the buffer used by the PARTIAL-BUFFER strategy.
     * TODO: this should be factored out when config is splittable among modules.
     */
    public void setPartialBufferSize(int partialBufferSize) {
        service.getMetadata().put( "partialBufferSize", partialBufferSize );
        //this.partialBufferSize = partialBufferSize;
    }

    public Object toDTO() {
        ServiceDTO dto = new ServiceDTO();
        
        dto.setAccessConstraints(service.getAccessConstraints());
        dto.setEnabled(service.isEnabled());
        dto.setFees(service.getFees());
        
        dto.setKeywords(service.getKeywords());
        dto.setMaintainer(service.getMaintainer());
        dto.setName(service.getName());
        
        if ( service.getOnlineResource() != null ) {
            try {
                dto.setOnlineResource(new URL( service.getOnlineResource() ));
            } 
            catch (MalformedURLException e) {
                throw new RuntimeException( e );
            }    
        }
        
        dto.setAbstract(service.getAbstract());
        dto.setTitle(service.getTitle());
        
        dto.setMetadataLink(getMetadataLink());    
        dto.setStrategy(getStrategy());
        dto.setPartialBufferSize(getPartialBufferSize());
        
        //dto.setAccessConstraints(getAccessConstraints());
        //dto.setEnabled(isEnabled());
        //dto.setFees(getFees());
        //dto.setKeywords(getKeywords());
        //dto.setMaintainer(getMaintainer());
        //dto.setName(getName());
        //dto.setOnlineResource(getOnlineResource());
        //dto.setAbstract(getAbstract());
        //dto.setTitle(getTitle());
        //dto.setMetadataLink(metadataLink);
        //dto.setStrategy(strategy);
        //dto.setPartialBufferSize(partialBufferSize);

        return dto;
    }

    /**
     * Access dt property.
     *
     * @return Returns the dt.
     */
    public Data getData() {
        return new Data( gs );
        //return dt;
    }
    
    public Data getRawData() {
        Catalog catalog = gs.getCatalog();
        if(catalog instanceof Wrapper && ((Wrapper) catalog).isWrapperFor(Catalog.class)) {
            catalog = ((Wrapper) catalog).unwrap(Catalog.class);
        }
        return new Data(gs, catalog);
    }

    /**
     * Set dt to dt.
     *
     * @param dt The dt to set.
     */
    public void setData(Data dt) {
        //this.dt = dt;
    }

    /**
     * Access gs property.
     *
     * @return Returns the gs.
     */
    public GeoServer getGeoServer() {
        return new GeoServer( gs );
        //return gs;
    }

    /**
     * Set gs to gs.
     *
     * @param gs The gs to set.
     */
    public void setGeoServer(GeoServer gs) {
        //this.gs = gs;
    }

    /**
     * @return Returns the metadataLink.
     *
     */
    public MetaDataLink getMetadataLink() {
        if ( service.getMetadataLink() != null ) {
            return new MetaDataLink( service.getMetadataLink() );
        }
        return null;
        //return metadataLink;
    }
  
    /**
     * Returns the character encoding scheme the service shall use to encode all its XML responses in.
     * 
     * @return the character set for the service to encode XML responses in.
     */
    public Charset getCharSet() {
        return getGeoServer().getCharSet();
    }

    public String getAbstract() {
        return service.getAbstract();
    }

    public void setAbstract(String serverAbstract) {
    }
    
    public String getAccessConstraints() {
        return service.getAccessConstraints();
    }

    public void setAccessConstraints(String accessConstraints) {
        service.setAccessConstraints(accessConstraints);
    }

    public String getFees() {
        return service.getFees();
    }
    
    public void setFees(String fees) {
        service.setFees(fees);
    }

    public String getId() {
        return service.getId();
    }

    public List getKeywords() {
        return service.getKeywords();
    }

    public String getMaintainer() {
        return service.getMaintainer();
    }

    public void setMaintainer(String maintainer) {
        service.setMaintainer(maintainer);
    }
    
    public String getName() {
        return service.getName();
    }
    
    public void setName(String name) {
        service.setName( name );
    }

    public URL getOnlineResource() {
        try {
            if ( service.getOnlineResource() == null ) {
                return null;
            }
            
            return new URL( service.getOnlineResource() );
        } catch (MalformedURLException e) {
            throw new RuntimeException( e );
        }
    }

    public void setOnlineResource(URL onlineResource) {
        service.setOnlineResource( onlineResource != null ? onlineResource.toString() : null );
    }
    
    public String getTitle() {
        return service.getTitle();
    }

    public boolean isEnabled() {
        return service.isEnabled();
    }

    public void setTitle(String title) {
        service.setTitle( title );
    }
    
    public void setEnabled(boolean enabled) {
        service.setEnabled(enabled);
    }

    public String getSchemaBaseURL() {
        return service.getSchemaBaseURL();
    }
    
    public void setSchemaBaseURL(String schemaBaseURL) {
        service.setSchemaBaseURL(schemaBaseURL);
    }
    
    public boolean isVerbose() {
        return service.isVerbose();
    }
    
    public void setVerbose(boolean verbose) {
        service.setVerbose(verbose);
    }
    
    public Map getClientProperties() {
        return service.getClientProperties();
    }

}

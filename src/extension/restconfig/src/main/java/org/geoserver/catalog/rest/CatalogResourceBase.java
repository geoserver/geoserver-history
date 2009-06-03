/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.catalog.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.config.ConfigurationListener;
import org.geoserver.config.GeoServer;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.rest.PageInfo;
import org.geoserver.rest.ReflectiveResource;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.rest.format.MediaTypes;
import org.geoserver.rest.format.ReflectiveXMLFormat;
import org.geotools.util.logging.Logging;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.dto.WCSDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;
import org.vfny.geoserver.global.xml.XMLConfigWriter;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.AbstractXmlWriter;

public abstract class CatalogResourceBase extends ReflectiveResource {

    /**
     * logger
     */
    static Logger LOGGER = Logging.getLogger( "org.geoserver.catalog.rest");
    /**
     * the catalog
     */
    protected Catalog catalog;
    /**
     * the class of the resource
     */
    protected Class clazz;
    
    public CatalogResourceBase(Context context,Request request, Response response, Class clazz,
            Catalog catalog) {
        super( context, request, response );
        this.clazz = clazz;
        this.catalog = catalog;
    }
    
    @Override
    protected DataFormat createHTMLFormat(Request request,Response response) {
        return new CatalogFreemarkerHTMLFormat( clazz, request, response, this );
    }
    
    /**
     * Method that persists the catalog after a change has been made.
     */
    protected void saveCatalog() throws Exception {
        saveCatalog( catalog );
    }
    
    /**
     * Static method to persist the catalog to be called by non subclasses. 
     */
    protected static void saveCatalog( Catalog catalog ) throws Exception {
        Data data = (Data) GeoServerExtensions.bean( "data" );
        DataConfig dataConfig = (DataConfig) GeoServerExtensions.bean( "dataConfig" );

        synchronized( GeoServer.CONFIGURATION_LOCK ) {
            XMLConfigWriter.store( data.toDTO() , catalog.getResourceLoader().getBaseDirectory() );
            dataConfig.update( data.toDTO() );
        }
        
        fireGeoServerChange();
    }
    
    protected void saveConfiguration() throws Exception {
        org.vfny.geoserver.global.GeoServer global = 
            (org.vfny.geoserver.global.GeoServer) GeoServerExtensions.bean( "geoServer");
        Service wfs = (Service) GeoServerExtensions.bean( "wfs" );
        Service wms = (Service) GeoServerExtensions.bean( "wms" );
        Service wcs = (Service) GeoServerExtensions.bean( "wcs" );
        WMSConfig wmsConfig =  (WMSConfig) GeoServerExtensions.bean( "wmsConfig" );

        synchronized (GeoServer.CONFIGURATION_LOCK) {
            XMLConfigWriter.store((WCSDTO)wcs.toDTO(),(WMSDTO)wms.toDTO(),(WFSDTO)wfs.toDTO(),
                global.toDTO(),catalog.getResourceLoader().getBaseDirectory());
            wmsConfig.update( (WMSDTO) wms.toDTO() );
        }
        
        fireGeoServerChange();
    }
    
    protected static void fireGeoServerChange() {
        GeoServer gs = GeoServerExtensions.bean(GeoServer.class);
        for (ConfigurationListener l : gs.getListeners() ) {
            try {
                l.reloaded();
            }
            catch( Throwable t ) {
                LOGGER.warning( "listener threw exception, turn logging to FINE to view stack trace" );
                LOGGER.log( Level.FINE, t.getLocalizedMessage(), t );
            }
        }
    }
    
    protected void encodeAlternateAtomLink( String link, HierarchicalStreamWriter writer ) {
        encodeAlternateAtomLink( link, writer, getFormatGet() );
    }
    
    protected void encodeAlternateAtomLink( String link, HierarchicalStreamWriter writer, DataFormat format ) {
        writer.startNode( "atom:link");
        writer.addAttribute("xmlns:atom", "http://www.w3.org/2005/Atom");
        writer.addAttribute( "rel", "alternate" );
        writer.addAttribute( "href", href(link,format) );
        
        if ( format != null ) {
            writer.addAttribute( "type", format.getMediaType().toString() );
        }
        
        writer.endNode();
    }
    
    protected void encodeLink( String link, HierarchicalStreamWriter writer ) {
        encodeLink( link, writer, getFormatGet() );
    }
    
    protected void encodeLink( String link, HierarchicalStreamWriter writer, DataFormat format ) {
        if ( getFormatGet() instanceof ReflectiveXMLFormat  ) {
            //encode as an atom link
            encodeAlternateAtomLink(link, writer, format);
        }
        else {
            //encode as a child element
            writer.startNode( "href" );
            writer.setValue( href( link, format) );
            writer.endNode();
        }
    }
    
    protected void encodeCollectionLink( String link, HierarchicalStreamWriter writer ) {
        encodeCollectionLink( link, writer, getFormatGet() );
    }
    
    protected void encodeCollectionLink( String link, HierarchicalStreamWriter writer, DataFormat format) {
        if ( format instanceof ReflectiveXMLFormat ) {
            //encode as atom link
            encodeAlternateAtomLink(link, writer, format);
        }
        else {
            //encode as a value
            writer.setValue( href( link, format ) );
        }
    }
    
    String href( String link, DataFormat format ) {
        PageInfo pg = getPageInfo();
        
        String href = null;
        if ( link.startsWith( "/") ) {
            //absolute, encode from "root"
            href = ResponseUtils.appendPath( pg.getRootURI(), link );
        }
        else {
            //encode as relative
            href = ResponseUtils.appendPath( pg.getPageURI(), link );
        }

        //try to figure out extension
        String ext = null;
        if ( format != null ) {
            ext = MediaTypes.getExtensionForMediaType( format.getMediaType() );
        }
        
        if ( ext == null ) {
            ext = pg.getExtension();
        }
        
        href += ext != null ? "."+ext : "";
        return href;
    }
}

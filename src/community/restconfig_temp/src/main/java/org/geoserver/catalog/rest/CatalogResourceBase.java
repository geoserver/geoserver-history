package org.geoserver.catalog.rest;

import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
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
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.dto.WCSDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;
import org.vfny.geoserver.global.xml.XMLConfigWriter;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

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
        synchronized( GeoServer.CONFIGURATION_LOCK ) {
            XMLConfigWriter.store( data.toDTO() , catalog.getResourceLoader().getBaseDirectory() );
        }
    }
    
    protected void saveConfiguration() throws Exception {
        org.vfny.geoserver.global.GeoServer global = 
            (org.vfny.geoserver.global.GeoServer) GeoServerExtensions.bean( "geoServer");
        Service wfs = (Service) GeoServerExtensions.bean( "wfs" );
        Service wms = (Service) GeoServerExtensions.bean( "wms" );
        Service wcs = (Service) GeoServerExtensions.bean( "wcs" );
        
        synchronized (GeoServer.CONFIGURATION_LOCK) {
            XMLConfigWriter.store((WCSDTO)wcs.toDTO(),(WMSDTO)wms.toDTO(),(WFSDTO)wfs.toDTO(),
                global.toDTO(),catalog.getResourceLoader().getBaseDirectory());    
        }
    }
    
    protected void encodeAlternateAtomLink( String link, HierarchicalStreamWriter writer ) {
        writer.startNode( "atom:link");
        writer.addAttribute("xmlns:atom", "http://www.w3.org/2005/Atom");
        writer.addAttribute( "rel", "alternate" );
        writer.addAttribute( "href", href(link) );
        
        DataFormat format = getFormatGet();
        if ( format != null ) {
            writer.addAttribute( "type", format.getMediaType().toString() );
        }
        
        writer.endNode();
    }
    
    protected void encodeLink( String link, HierarchicalStreamWriter writer ) {
        if ( getFormatGet() instanceof ReflectiveXMLFormat  ) {
            //encode as an atom link
            encodeAlternateAtomLink(link, writer);
        }
        else {
            //encode as a child element
            writer.startNode( "href" );
            writer.setValue( href( link ) );
            writer.endNode();
        }
    }
    
    protected void encodeCollectionLink( String link, HierarchicalStreamWriter writer ) {
        if ( getFormatGet() instanceof ReflectiveXMLFormat ) {
            //encode as atom link
            encodeAlternateAtomLink(link, writer);
        }
        else {
            //encode as a value
            writer.setValue( href( link ) );
        }
    }
    
    String href( String link ) {
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
        DataFormat format = getFormatGet();
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

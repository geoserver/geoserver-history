package org.geoserver.catalog.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServer;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.rest.ReflectiveResource;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.rest.format.ReflectiveJSONFormat;
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

public abstract class AbstractCatalogResource extends ReflectiveResource {

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
    
    public AbstractCatalogResource(Context context,Request request, Response response, Class clazz,
        Catalog catalog) {
        
        super(context, request, response);
        this.catalog = catalog;
        this.clazz = clazz;
    }

    @Override
    protected DataFormat createHTMLFormat(Request request,Response response) {
        return new CatalogFreemarkerHTMLFormat( clazz, request, response, this );
    }
       
    @Override
    protected ReflectiveXMLFormat createXMLFormat(Request request,Response response) {
        return new ReflectiveXMLFormat() {
            @Override
            protected void write(Object data, OutputStream output) throws IOException  {
                XStreamPersister p = new XStreamPersister.XML();
                p.setCatalog( catalog );
                p.save( data, output );
            }
            
            @Override
            protected Object read(InputStream in)
                    throws IOException {
                XStreamPersister p = new XStreamPersister.XML();
                p.setCatalog( catalog );
                return p.load( in, clazz );
            }
        };
    }
    
    @Override
    protected ReflectiveJSONFormat createJSONFormat(Request request,Response response) {
        return new ReflectiveJSONFormat() {
            @Override
            protected void write(Object data, OutputStream output)
                    throws IOException {
                XStreamPersister p = new XStreamPersister.JSON();
                p.setCatalog(catalog);
                p.save( data, output );
            }
            
            @Override
            protected Object read(InputStream input)
                    throws IOException {
                XStreamPersister p = new XStreamPersister.JSON();
                p.setCatalog(catalog);
                return p.load( input, clazz );
            }
        };
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
}

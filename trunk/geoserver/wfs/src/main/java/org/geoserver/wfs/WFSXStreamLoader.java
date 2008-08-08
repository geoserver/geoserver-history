package org.geoserver.wfs;

import org.geoserver.config.GeoServer;
import org.geoserver.config.util.XStreamServiceLoader;
import org.geoserver.platform.GeoServerResourceLoader;

/**
 * Loads and persist the {@link WFSInfo} object to and from xstream 
 * persistence.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WFSXStreamLoader extends XStreamServiceLoader<WFSInfo> {

    public WFSXStreamLoader(GeoServerResourceLoader resourceLoader) {
        super(resourceLoader, "wfs");
    }

    protected WFSInfo createServiceFromScratch(GeoServer gs) {
        WFSInfoImpl wfs = new WFSInfoImpl();
        wfs.setId( "wfs" );
        
        //gml2
        GMLInfoImpl gml2 = new GMLInfoImpl();
        gml2.setSrsNameStyle( GMLInfo.SrsNameStyle.XML );
        wfs.getGML().put( WFSInfo.Version.V_10 , gml2 );
        
        //gml3
        GMLInfoImpl gml3 = new GMLInfoImpl();
        gml3.setSrsNameStyle( GMLInfo.SrsNameStyle.URN );
        wfs.getGML().put( WFSInfo.Version.V_11 , new GMLInfoImpl() );
        
        return wfs;
    }

    public Class<WFSInfo> getServiceClass() {
        return WFSInfo.class;
    }

}

package org.geoserver.wfs;

import java.util.Map;

import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.util.LegacyServicesReader;
import org.geoserver.config.util.ServiceLoader;
import org.geoserver.wfs.GMLInfo.SrsNameStyle;

public class WFSLoader extends ServiceLoader {

    
    public ServiceInfo load(LegacyServicesReader reader, GeoServer geoServer)
            throws Exception {
        
        WFSInfo wfs = new WFSInfoImpl();
        
        Map<String,Object> properties = reader.wfs();
        load( wfs, properties, geoServer );
        
        wfs.setServiceLevel( WFSInfo.ServiceLevel.get( (Integer) properties.get( "serviceLevel") ) );
        
        //gml2
        GMLInfo gml = new GMLInfoImpl();
        gml.setFeatureBounding( (Boolean) properties.get( "featureBounding") );
        
        Boolean srsXmlStyle = (Boolean) properties.get( "srsXmlStyle" );
        if( srsXmlStyle ) {
            gml.setSrsNameStyle( SrsNameStyle.XML );    
        }
        else {
            gml.setSrsNameStyle( SrsNameStyle.NORMAL );
        }
        wfs.getGML().put( WFSInfo.Version.V_10 , gml );
        
        //gml3
        gml = new GMLInfoImpl();
        gml.setFeatureBounding(true);
        gml.setSrsNameStyle(SrsNameStyle.URN);
        wfs.getGML().put( WFSInfo.Version.V_11 , gml );
        
        return wfs;
    }

}

package org.geoserver.wms.xml;

import java.io.Reader;
import java.util.Map;

import javax.xml.namespace.QName;

import org.geoserver.ows.XmlRequestReader;
import org.geoserver.wms.kvp.GetMapKvpRequestReader;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.SLDParser;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.vfny.geoserver.wms.requests.GetMapRequest;

/**
 * Reads 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class SLDXmlRequestReader extends XmlRequestReader {

    StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    
    public SLDXmlRequestReader() {
        super("http://www.opengis.net/sld", "StyledLayerDescriptor" );
    }

    public void setStyleFactory(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }
    
   
    public Object read(Object request, Reader reader, Map kvp) throws Exception {
        if ( request == null ) {
            throw new IllegalArgumentException( "request must be not null" );
        }
        
        GetMapRequest getMap = (GetMapRequest) request;
        StyledLayerDescriptor sld = 
            new SLDParser( styleFactory, reader ).parseSLD();
        
        //process the sld 
        GetMapKvpRequestReader.processStandaloneSld(getMap, sld);
    
        return getMap;
    }
    
}

package org.geoserver.wms.kvp;

import java.util.Map;

import org.geoserver.ows.FlatKvpParser;
import org.geotools.styling.Style;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;

public class StylesKvpParser extends FlatKvpParser {

    Data catalog;
    
    public StylesKvpParser(Data catalog) {
        super("styles", Style.class );
        this.catalog = catalog;
    }
    
    protected Object parseToken(String token) throws Exception {
        if ( "".equals( token ) ) {
            //return null, this should flag request reader to use default for 
            // the associated layer
            return null;
        }
        
        Style style = catalog.getStyle( token );
        if ( style == null ) {
            String msg = "No such style: " + token;
            throw new WmsException(msg, "StyleNotDefined");
        }
        
        return style;
    }
}

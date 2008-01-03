package org.geoserver.wcs.kvp;

import org.geoserver.ows.KvpParser;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;

public class GridCSKvpParser extends KvpParser {

    public GridCSKvpParser() {
        super("GridCS", String.class);
    }
    
    @Override
    public Object parse(String value) throws Exception {
        if(!GridCS.GCSGrid2dSquare.getXmlConstant().equals(value))
            throw new WcsException("Unrecognized GridCS " + value, WcsExceptionCode.InvalidParameterValue, "GridCS");
        
        return value;
    }
}

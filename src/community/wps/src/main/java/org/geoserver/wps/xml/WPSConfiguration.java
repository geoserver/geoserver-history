package org.geoserver.wps.xml;

import java.util.Map;

import org.geotools.wps.WPS;

public class WPSConfiguration extends org.geotools.wps.WPSConfiguration {

    protected void registerBindings(Map bindings) {
        super.registerBindings(bindings);
        
        //binding overrides
        bindings.put( WPS.ComplexDataType, ComplexDataTypeBinding.class );
    }
}

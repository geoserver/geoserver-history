package org.geoserver.wps.ppio;

import java.net.URI;

public abstract class ReferencePPIO extends ProcessParameterIO {

    protected ReferencePPIO(Class type) {
        super(type);
    }

    protected ReferencePPIO(Class type, String identifier) {
        super(type, identifier);
    }

    public abstract String encode( Object value );
    
    public abstract Object decode( String uri ) throws Exception;

}

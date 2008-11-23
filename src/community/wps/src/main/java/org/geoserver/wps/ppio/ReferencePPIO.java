package org.geoserver.wps.ppio;

import java.net.URL;

public abstract class ReferencePPIO extends ProcessParameterIO {

    protected ReferencePPIO(Class externalType, Class internalType) {
        super(externalType, internalType);
    }
    
    public abstract URL encode( Object o ); 

}

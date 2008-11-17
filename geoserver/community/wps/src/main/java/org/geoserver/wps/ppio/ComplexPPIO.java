package org.geoserver.wps.ppio;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Process parameter input / output for arbitrary data on a specific mime type.
 * 
 * @author Lucas Reed, Refractions Research Inc
 * @author Justin Deoliveira, OpenGEO
 */
public abstract class ComplexPPIO extends ProcessParameterIO {

    /**
     * mime type of encoded content.
     */
    protected String mimeType;
    
    protected ComplexPPIO(Class type, String mimeType) {
        super(type);
        this.mimeType = mimeType;
    }

    public final String getMimeType() {
        return mimeType;
    }
    
    public abstract Object decode( InputStream input ) throws Exception;
    
    public Object decode( Object input ) throws Exception {
        return input;
    }
}

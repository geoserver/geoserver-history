package org.geoserver.wps.ppio;

import javax.xml.namespace.QName;

import org.xml.sax.ContentHandler;

public abstract class XMLPPIO extends ComplexPPIO {

    protected QName element;
    
    protected XMLPPIO(Class type, QName element) {
        this( type, "text/xml", element );
    }
    
    protected XMLPPIO(Class type, String mimeType, QName element) {
        super(type, mimeType);
        this.element = element;
    }

    public QName getElement() {
        return element;
    }
    
    public abstract void encode( Object object, ContentHandler handler ) throws Exception;

}

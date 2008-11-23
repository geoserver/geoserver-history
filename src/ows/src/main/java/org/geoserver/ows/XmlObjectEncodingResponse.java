/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.namespace.QName;

import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;

/**
 * A response designed to encode a specific object into XML
 * @author Andrea Aime - TOPP
 *
 */
public class XmlObjectEncodingResponse extends Response {

    String elementName;
    Class xmlConfiguration;
    
    public XmlObjectEncodingResponse(Class binding, String elementName, Class xmlConfiguration) {
        super( binding );
        this.elementName = elementName;
        this.xmlConfiguration = xmlConfiguration;
    }
    
    @Override
    public String getMimeType(Object value, Operation operation)
            throws ServiceException {
        return "application/xml";
    }

    @Override
    public void write(Object value, OutputStream output, Operation operation)
            throws IOException, ServiceException {
        try {
            Configuration c = (Configuration) xmlConfiguration.newInstance(); 
            Encoder e = new Encoder( c );
            e.encode( value, new QName( c.getXSD().getNamespaceURI(), elementName ), output );
        } 
        catch (Exception e) {
            throw (IOException) new IOException().initCause( e );
        }
    }

}
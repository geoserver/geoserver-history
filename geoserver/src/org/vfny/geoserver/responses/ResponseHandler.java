/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import org.vfny.geoserver.global.Service;
import org.xml.sax.SAXException;


/**
 * Interface used to handle the generation of a Capabilities response using
 * SAX generation methods.  It should call the appropriate startElement, 
 * characters, and endElement on the SAX Stream to correctly print the
 * appropriate Capabilities document based on the Service passed in.
 *
 * @author Gabriel Roldán
 * @version $Id: ResponseHandler.java,v 1.5 2004/09/05 17:17:58 cholmesny Exp $
 */
public interface ResponseHandler {
    void handleDocument(Service config) throws SAXException;

    void endDocument(Service config) throws SAXException;
}

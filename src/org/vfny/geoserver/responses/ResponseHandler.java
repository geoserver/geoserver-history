/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import org.vfny.geoserver.global.Service;
import org.xml.sax.SAXException;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: ResponseHandler.java,v 1.4 2004/01/31 00:27:24 jive Exp $
 */
public interface ResponseHandler {
    void handleDocument(Service config) throws SAXException;

    void endDocument(Service config) throws SAXException;
}

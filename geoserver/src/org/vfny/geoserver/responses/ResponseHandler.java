/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import org.vfny.geoserver.config.*;
import org.xml.sax.*;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public interface ResponseHandler {
    void handleDocument(ServiceConfig config) throws SAXException;

    void endDocument(ServiceConfig config) throws SAXException;
}

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import org.vfny.geoserver.global.ServiceConfig;
import org.xml.sax.SAXException;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: ResponseHandler.java,v 1.2.2.2 2003/12/30 23:08:26 dmzwiers Exp $
 */
public interface ResponseHandler {
    void handleDocument(ServiceConfig config) throws SAXException;

    void endDocument(ServiceConfig config) throws SAXException;
}

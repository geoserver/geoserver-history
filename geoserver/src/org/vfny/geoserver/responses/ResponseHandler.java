/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import org.vfny.geoserver.global.GlobalService;
import org.xml.sax.SAXException;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: ResponseHandler.java,v 1.2.2.3 2004/01/03 00:20:15 dmzwiers Exp $
 */
public interface ResponseHandler {
    void handleDocument(GlobalService config) throws SAXException;

    void endDocument(GlobalService config) throws SAXException;
}

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets.wfs;

import org.vfny.geoserver.*;
import org.vfny.geoserver.requests.readers.*;
import org.vfny.geoserver.requests.readers.wfs.LockKvpReader;
import org.vfny.geoserver.requests.readers.wfs.LockXmlReader;
import org.vfny.geoserver.responses.*;
import org.vfny.geoserver.responses.wfs.*;
import org.vfny.geoserver.servlets.*;
import java.util.*;


/**
 * Implements the WFS GetFeatureWithLock interface, which responds to requests
 * for GML, locking the features as well. This servlet accepts a
 * getFeatureWithLock request and returns GML2.1 structured XML docs.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: FeatureWithLock.java,v 1.1.2.1 2003/11/04 23:31:11 cholmesny Exp $
 */
public class FeatureWithLock extends WFService {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Response getResponseHandler() {
        return new LockResponse();
    }

    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected KvpRequestReader getKvpReader(Map params) {
        return new LockKvpReader(params);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected XmlRequestReader getXmlRequestReader() {
        return new LockXmlReader();
    }
}

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets.wfs;

import org.vfny.geoserver.*;
import org.vfny.geoserver.requests.readers.*;
import org.vfny.geoserver.requests.readers.wfs.*;
import org.vfny.geoserver.responses.*;
import org.vfny.geoserver.responses.wfs.*;
import org.vfny.geoserver.servlets.*;
import java.util.*;


/**
 * Implements the WFS Transaction interface, which performs insert, update and
 * delete functions on the dataset. This servlet accepts a Transaction request
 * and returns a TransactionResponse xml element.
 *
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldán
 * @version $Id: Transaction.java,v 1.1.2.1 2003/11/04 23:31:11 cholmesny Exp $
 */
public class Transaction extends WFService {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Response getResponseHandler() {
        return new TransactionResponse();
    }

    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected KvpRequestReader getKvpReader(Map params) {
        return new DeleteKvpReader(params);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected XmlRequestReader getXmlRequestReader() {
        return new TransactionXmlReader();
    }
}

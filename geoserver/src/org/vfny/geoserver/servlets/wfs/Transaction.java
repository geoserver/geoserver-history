/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets.wfs;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.requests.Requests;
import org.vfny.geoserver.requests.readers.KvpRequestReader;
import org.vfny.geoserver.requests.readers.XmlRequestReader;
import org.vfny.geoserver.requests.readers.wfs.DeleteKvpReader;
import org.vfny.geoserver.requests.readers.wfs.TransactionXmlReader;
import org.vfny.geoserver.responses.Response;
import org.vfny.geoserver.responses.wfs.TransactionResponse;
import org.vfny.geoserver.servlets.WFService;


/**
 * Implements the WFS Transaction interface, which performs insert, update and
 * delete functions on the dataset. This servlet accepts a Transaction request
 * and returns a TransactionResponse xml element.
 *
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldán
 * @version $Id: Transaction.java,v 1.8 2004/02/19 08:58:21 jive Exp $
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
    
    protected boolean isServiceEnabled(HttpServletRequest req){
    	if( !Requests.getWFS(req).isEnabled() ){
    		return false;
        }
        int serviceLevel = Requests.getWFS(req).getServiceLevel();
        return ( serviceLevel | WFSDTO.TRANSACTIONAL) != 0;    	
    }
}

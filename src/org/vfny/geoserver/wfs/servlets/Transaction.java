/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets.wfs;

import java.util.Map;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.requests.readers.KvpRequestReader;
import org.vfny.geoserver.requests.readers.XmlRequestReader;
import org.vfny.geoserver.requests.readers.wfs.DeleteKvpReader;
import org.vfny.geoserver.requests.readers.wfs.TransactionXmlReader;
import org.vfny.geoserver.responses.Response;
import org.vfny.geoserver.responses.wfs.TransactionResponse;
import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.servlets.WFService;


/**
 * Implements the WFS Transaction interface, which performs insert, update and
 * delete functions on the dataset. This servlet accepts a Transaction request
 * and returns a TransactionResponse xml element.
 *
 * @author Chris Holmes, TOPP
 * @author Gabriel Rold?n
 * @version $Id: Transaction.java,v 1.11 2004/06/03 19:34:36 cholmesny Exp $
 */
public class Transaction extends WFService {
    /**
     * Returns the handler for a transaction.
     *
     * @return An instance of the TransactionResponse class.
     */
    protected Response getResponseHandler() {
        return new TransactionResponse();
    }

    /**
     * Returns the delete kvp reader, as delete is the only transaction that
     * can be made with key value pairs.
     *
     * @param params A map of the kvps.
     *
     * @return The delete kvp reader.
     */
    protected KvpRequestReader getKvpReader(Map params) {
        return new DeleteKvpReader(params);
    }

    /**
     * Returns the xml reader for transactions.
     *
     * @return The transaction xml reader.
     *
     * @see TransactionXmlReader
     */
    protected XmlRequestReader getXmlRequestReader() {
        return new TransactionXmlReader();
    }

    /**
     * Override to always return a FILE strategy.  This ensures that errors
     * with commits will always be reported.  The expense of using a FILE
     * response here is not that great, as transaction responses are generally
     * not very long.  The importance of reporting an error with a commit is
     * far greater than returning a slightly quicker response time.
     *
     * @return The fileStrategy.
     *
     * @throws ServiceException If the fileStrategy is not availble.
     */
    protected AbstractService.ServiceStrategy getServiceStrategy()
        throws ServiceException {
        //There's probably a better way to do this, but I'm confused by
        //all the inner classes and interfaces and how to get access to
        //them.  This method appears to work, so we'll stick with it
        //until someone implements a cleaner way.
        Class strategyClass = (Class) serviceStrategys.get("FILE");

        return super.getServiceStrategy(strategyClass);
    }
}

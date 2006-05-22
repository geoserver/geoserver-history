/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.servlets;

import java.util.Map;

import org.vfny.geoserver.Response;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.servlets.FileStrategy;
import org.vfny.geoserver.servlets.ServiceStrategy;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wfs.requests.readers.DeleteKvpReader;
import org.vfny.geoserver.wfs.requests.readers.TransactionXmlReader;
import org.vfny.geoserver.wfs.responses.TransactionResponse;


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
	 * The file strategy prototype
	 */
	FileStrategy fileStrategy;
	
	public Transaction() {
		super("Transaction");
	}

	/**
	 * Sets the file strategy prototype to be used in calls to {@link #createServiceStrategy()}
	 * 
	 */
	public void setFileStrategy(FileStrategy fileStrategy) {
		this.fileStrategy = fileStrategy;
	}
	
	/**
	 * Returns the file strategy prototype.
	 * <p>
	 * This prototype is cloned in {@link #createServiceStrategy()}
	 * </p>
	 * 
	 */
	public FileStrategy getFileStrategy() {
		return fileStrategy;
	}
	
	
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
        return new DeleteKvpReader(params,this);
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
    protected ServiceStrategy createServiceStrategy() throws ServiceException {
	    	try {
			return (ServiceStrategy) fileStrategy.clone();
		} 
	    	catch (CloneNotSupportedException e) {
	    		String msg = "Unable to clone " + fileStrategy.getClass();
	    		throw new ServiceException(msg,e);
		}
    }
    
}

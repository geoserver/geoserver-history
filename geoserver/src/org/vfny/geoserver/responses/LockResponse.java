/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.responses;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.logging.Logger;
import org.geotools.filter.Filter;
import org.vfny.geoserver.requests.LockRequest;
import org.vfny.geoserver.config.TypeInfo;
import org.vfny.geoserver.config.TypeRepository;
import org.vfny.geoserver.config.ConfigInfo;


/**
 * Handles a Lock request and creates a LockResponse string.
 *
 *@author Chris Holmes, TOPP
 *@version $VERSION$
 */
public class LockResponse {

    /** Standard logging instance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.responses");
    
    /** The store of types, which also holds their locking information. */
    private static TypeRepository repository = TypeRepository.getInstance();

    /** indicates whether the output should be formatted. */
    private static boolean verbose = ConfigInfo.getInstance().formatOutput();

    /** the new line character to use in the response. */
    private static String nl = verbose ? "\n" : "";

    /** Constructor, which is required to take a request object. */ 
    private LockResponse () {}

    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     * @return XML response to send to client
     */ 
    public static String getXmlResponse(LockRequest request) 
	throws WfsException {
	
	LOGGER.finer("about to do Lock response on:" + request);
	List locks = request.getLocks();
	if (locks.size() == 0) {
	     throw new WfsException("A LockFeature request must contain at " +
				   "least one LOCK element");
	}
	LockRequest.Lock curLock = (LockRequest.Lock)locks.get(0);
	boolean lockAll = request.getLockAll();
	String lockId = null;
	String curTypeName = curLock.getFeatureType();
	Filter curFilter = curLock.getFilter();
	lockId = repository.lock(curTypeName, curFilter, 
				 lockAll, request.getExpiry());
	for(int i = 1, n = locks.size(); i < n; i++) {  
	    curLock = (LockRequest.Lock)locks.get(i);
	    curTypeName = curLock.getFeatureType();
	    curFilter = curLock.getFilter();
	    repository.addToLock(curTypeName, curFilter, lockAll, lockId);
	
	}
	return generateXml(lockId, lockAll, 
			   repository.getLockedFeatures(lockId),
			   repository.getNotLockedFeatures(lockId));
    }

    /**
     * Creates the xml for a lock response.
     * 
     * @param lockId the lockId to print in the response.
     * @param lockAll indicates if information about which features were locked
     * should be printed.
     * @param lockedFeatures a list of features locked by the request.  This is
     * not used if lockAll is false.
     * @param notLockedFeatures a list of features that matched the filter but
     * were not locked by the request.  This is not used if lockAll is false.
     */
    private static String generateXml(String lockId, boolean lockAll,
				      Set lockedFeatures, Set notLockedFeatures){
	String indent = verbose ? "   " : "";
	StringBuffer returnXml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	returnXml.append(nl + "<WFS_LockFeatureResponse " + nl);
	returnXml.append(indent + "xmlns=\"http://www.opengis.net/wfs\" " + nl);
	//this not needed yet, only when FeaturesLocked element used.
	//TODO: get rid of this hardcoding, and make a common utility to get all
	//these namespace imports, as everyone is using them, and changes should
	//go through to all the operations.
	if (!lockAll) {
	  returnXml.append(indent + 
			   "xmlns:ogc=\"http://www.opengis.net/ogc\" " + nl);
	}
	returnXml.append(indent + "xmlns:xsi=\"http://www.w3.org/2001/" + 
			 "XMLSchema-instance\" " + nl);
	returnXml.append(indent + "xsi:schemaLocation=\"http://www.opengis" +
			 ".net/wfs ../wfs/1.0.0/WFS-transaction.xsd\">" + nl);
	returnXml.append(indent + "<LockId>" + lockId + "</LockId>" + nl);
	if (!lockAll) {
	    returnXml.append(indent + "<FeaturesLocked>" + nl); 
		for (Iterator i = lockedFeatures.iterator();
		     i.hasNext();) {
		    returnXml.append(indent + indent);
		    returnXml.append("<ogc:FeatureId fid=\"" + i.next() + 
				     "\"/>" + nl);
		}
	    returnXml.append(indent + "</FeaturesLocked>" + nl);
	    if (notLockedFeatures != null && notLockedFeatures.size() > 0){
		returnXml.append("<FeaturesNotLocked>" + nl);
		for (Iterator i = notLockedFeatures.iterator();
		     i.hasNext();) {
		    returnXml.append(indent + indent);
		    returnXml.append("<ogc:FeatureId fid=\"" + 
				     i.next() + "\"/>" + nl);
		}
		returnXml.append("</FeaturesNotLocked>" + nl);
	    }
	}
	returnXml.append("</WFS_LockFeatureResponse>");

	return returnXml.toString();
    }


}

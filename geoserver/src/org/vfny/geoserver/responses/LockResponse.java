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
    
    private static TypeRepository repository = TypeRepository.getInstance();

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
	LockRequest.Lock curLock = (LockRequest.Lock)locks.get(0);
	boolean lockAll = request.getLockAll();
	String lockId = null;
	if (curLock == null) {
	    throw new WfsException("A LockFeature request must contain at " +
				   "least one LOCK element");
	} else {
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
	}
	return generateXml(lockId, lockAll, 
			   repository.getLockedFeatures(lockId),
			   repository.getNotLockedFeatures(lockId));
    }

    private static String generateXml(String lockId, boolean lockAll,
				      Set lockedFeatures, Set notLockedFeatures){
	String indent = "   ";
	StringBuffer returnXml = new StringBuffer("<?xml version=\"1.0\" ?>");
	returnXml.append("\n<WFS_LockFeatureResponse\n");
	returnXml.append(indent + "xmlns=\"http://www.opengis.net/wfs\"\n");
	//this not needed yet, only when FeaturesLocked element used.
	if (!lockAll) {
	  returnXml.append(indent +"xmlns:ogc=\"http://www.opengis.net/ogc\"\n");
	}
	returnXml.append(indent + "xmlns:xsi=\"http://www.w3.org/2001/" + 
			 "XMLSchema-instance\"\n");
	//REVISIT: this probably isn't right, need to learn about xml schemas
	returnXml.append(indent + "xsi:schemaLocation=\"http://www.opengis" +
			 ".net/wfs ../wfs/1.0.0/WFS-transaction.xsd\">\n");
	returnXml.append(indent + "<LockId>" + lockId + "</LockId>\n");
	if (!lockAll) {
	    returnXml.append(indent + "<FeaturesLocked>\n"); 
		for (Iterator i = lockedFeatures.iterator();
		     i.hasNext();) {
		    returnXml.append(indent + indent);
		    returnXml.append("<ogc:FeatureId fid=\"" + i.next() + "\"/>\n");
		}
	    returnXml.append(indent + "</FeaturesLocked>\n");
	    if (notLockedFeatures != null && notLockedFeatures.size() > 0){
		returnXml.append("<FeaturesNotLocked>\n");
		for (Iterator i = notLockedFeatures.iterator();
		     i.hasNext();) {
		    returnXml.append(indent + indent);
		    returnXml.append("<ogc:FeatureId fid=\"" + i.next() + "\"/>\n");
		}
		returnXml.append("</FeaturesNotLocked>\n");
	    }
	}
	returnXml.append(indent + "</WFS_LockFeatureResponse>");

	return returnXml.toString();
    }

    /**
     * Runs checks to see if the typename is already locked and to make
     * sure the lock does not have a filter.  
     * @param testLock the lock to check.
     * @throws WfsException if the features are already locked, or if
     * there is a filter (since we only support type locking now).
     * @tasks REVISIT: this method will completely change when we are able
     * to lock individual features.  Filters will be used, and the testing
     * for locked features will depend on SOME or ALL, since if the user
     * request SOME for the lockAction then it doesn't matter if a few features
     * are locked.
     */
    /*    private static void checkLock(LockRequest.Lock testLock) 
	throws WfsException{
    /* This functions by throwing WfsExceptions instead of returning a boolean,
     * because we want to  chain the exception all the way to the user.
	String typeName = testLock.getFeatureType();
	if(repository.getType(typeName) == null) {
	    throw new WfsException("typeName " + typeName + " not found on " +
				   "this server", testLock.getHandle());
	}
	//if (repository.isLocked(typeName)){
	    //Only when lockALL?  Kind of moot point right now.
	//   throw new WfsException("Could not lock " + typeName + " features" +
	//			   ", they are already locked", 
	//			   testLock.getHandle());
	//}
	//if (testLock.getFilter() != null) {
	//   throw new WfsException("Only FeatureType locking is currently "
	//			   + "supported, try your lock request " + 
	//			   "again without a filter element", 
	//			   testLock.getHandle());
	//}

     }*/


}

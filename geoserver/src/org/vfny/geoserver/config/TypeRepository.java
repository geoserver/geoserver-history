/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.vfny.geoserver.config.featureType.FeatureType;

/**
 * Reads all necessary feature type information to abstract away from servlets.
 * 
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $VERSION$
 */
public class TypeRepository {
        
    /** Class logger */
    private static Logger LOG = Logger.getLogger("org.vfny.geoserver.config");

    /** Castor-specified type to hold all the  */
    private static TypeRepository repository = null;

    private static ConfigInfo config = ConfigInfo.getInstance();

    private Random keyMaster = null;

    private Map types = new HashMap();

    //REVISIT: this may not be necessary, since we're never going to have that 
    //many featureTypes, so we could just iterate through the lock list to see 
    //if they are locked.  But we'll leave it in for now since it was like this
    //originally (though this was named locks), and if we do individual feature
    //locking this could hold them efficiently.
    private Map lockedFeatures = new HashMap();

    private Map locks = new HashMap();
    
    /** Initializes the database and request handler. */ 
    private TypeRepository() {
        Date seed = new Date();
        keyMaster = new Random(seed.getTime());
    }
    
    /**
     * Initializes the database and request handler.
     * @param featureTypeName The query from the request object.
     */ 
    public static TypeRepository getInstance() {
        if(repository == null) {
            File startDir = new File(config.getTypeDir());
            repository = new TypeRepository();
	    LOG.finer("about to read types at " + startDir);
            repository.readTypes(startDir, config);
        }
        return repository;
    }

    /**
     * Returns a capabilities XML fragment for a specific feature type.
     * @param version The version of the request (0.0.14 or 0.0.15)
     */ 
    public TypeInfo getType(String typeName) {
        return (TypeInfo) types.get(typeName);
    }

    /**
     * Adds a type to the repository, reading from the path given.
     * @param pathToTypeInfo the path to an info.xml file.
     */ 
    private void addType(String pathToTypeInfo) { 
	TypeInfo type = new TypeInfo(pathToTypeInfo);
	if (type.getName() != null) {
	    types.put(type.getName(), type);
	} else {
	    LOG.warning("Geoserver did not successfull read the feature" +
			" info.xml file at " + pathToTypeInfo + ".  Please " + 
			"make sure all elements are in info.xml");
	}
    }

    /**
     * Returns a capabilities XML fragment for a specific feature type.
     */
    public int typeCount() {        
        return types.size();
    }


    /**
     * Inidicates whether a featureType has been locked.
     * @param typeName the name of the feature to check for a lock.
     */ 
    public boolean isLocked(String typeName) {        
        return lockedFeatures.containsKey(typeName);
    }

    /**
     * Gets rid of all lockedFeatures held by this repository.  Used as a
     * convenience method for unit tests, but should eventually be used by
     * an admin tool to clear unclosed lockedFeatures.
     */
    public void clearLocks(){
	lockedFeatures = new HashMap();
	locks = new HashMap();
    }

    /**
     * indicates whether the lockId is the one that currently locks the
     * passed in typeName.
     *
     * @param typeName the featureType name to test the lockId against.
     * @param lockId the string of the id of the lock.
     */
    public boolean isCorrectLock(String typeName, String lockId){
	if(lockedFeatures.containsKey(typeName)) { 
	    String targetLockId = ((InternalLock)lockedFeatures.get(typeName)).getId();
            return (targetLockId.equals(lockId));
	} else {
	    return false;
	}
    }

    /**
     * A convenience method to lock a typeName for the default expiry
     * length of time.
     *
     * @param typeName the name of the featureType to lock.
     * @return  the id string of the lock, if successful, null otherwise.
    public synchronized String lock(String typeName){
	return lock(typeName, -1);
    }

    /**
     * Locks the given typeName for a length of expiry minutes.
     *
     * @param typeName the name of the featureType to lock.
     * @param expiry the length in minutes to lock the featureType for.
     * @return the id string of the lock, if successful, null otherwise.
     */ 
    public synchronized String lock(String typeName, int expiry){        
        if(lockedFeatures.containsKey(typeName)) {
            LOG.finer("no lock on: " + typeName);
	    //TODO: this common code should go in InternalLock
            return null;
        } else {
            String lockId = 
		String.valueOf(keyMaster.nextInt(Integer.MAX_VALUE));
	    InternalLock lock = new InternalLock(lockId, expiry);
	    lock.addFeatureType(typeName);
            locks.put(lockId, lock);
	    //lockedFeatures.put(typeName, lock);
            LOG.finer("locked " + typeName + " with: " + lock);
            return lockId;
        }
    }

    /**
     * Performs a lock operation with an already assigned lockValue.  This
     * is for cases when more than one Lock is requested in a LockFeature
     * request, as all must have the same lock.
     *
     * @param typeName the name of the Feature Type to lock.
     * @param lockValue the value to store as the lock.
     * @return the value of the lock assigned.  Will be the same as the 
     * lockValue argument.  Null if that type was already locked.
     */
     public synchronized String lock(String typeName, String lockId) {   
        if(lockedFeatures.containsKey(typeName)) {
	    //REVISIT: SOME vs. ALL, what to do if first is successful?
	    //probably grab the lock and call release for ALL.  Some we 
	    //don't handle yet.
            LOG.finest("no lock on: " + typeName);
            return null;
        } else {
	  //String lock = String.valueOf(keyMaster.nextInt(Integer.MAX_VALUE));
	    if (!locks.containsKey(lockId)){ 
		//REVISIT: this should only be called on valid locks, what to
		//do if that doesn't happen?  Maybe require expiry?
	    }
	    InternalLock existingLock = (InternalLock)locks.get(lockId);
	    existingLock.addFeatureType(typeName);
	    //lockedFeatures.put(typeName, lockId);
            LOG.finest("locked " + typeName + " with: " + lockId);
            return lockId;
        }
    }

    public synchronized boolean unlock(String typeName, String lockId) {
	return unlock(typeName, lockId, true);
    }

    /**
     * Returns a capabilities XML fragment for a specific feature type.
     * @param typeName The version of the request (0.0.14 or 0.0.15)
     * @param key The version of the request (0.0.14 or 0.0.15)
     */ 
    public synchronized boolean unlock(String typeName, String lockId, 
				       boolean releaseAll) {        	
        if(lockedFeatures.containsKey(typeName)) {
            InternalLock targetLock = (InternalLock) lockedFeatures.get(typeName);
	    String targetLockId = targetLock.getId();
            if(targetLockId.equals(lockId)) {
                targetLock.unlock(typeName, releaseAll);
		//lockedFeatures.remove(typeName);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    


    /**
     * This function lists all files in HTML for the meta-data pages.
     * 
     * Simple recursive function to read through all subdirectories and 
     * add all XML files with the name 'info.XXX' to the repository.
     * @param currentFile The top directory from which to start 
     * reading files.
     */
    private void readTypes(File currentFile, ConfigInfo config) {
        LOG.finest("examining: " + currentFile.getAbsolutePath());
        LOG.finest("is dir: " + currentFile.isDirectory());
        if(currentFile.isDirectory()) {
            File[] file = currentFile.listFiles();
            for(int i = 0, n = file.length; i < n; i++) {
                readTypes(file[i], config);
            } 
        } else if(isInfoFile(currentFile, config)) {
	    String curPath = currentFile.getAbsolutePath();
            LOG.finest("adding: " + curPath);
	    addType(curPath);
	    

        }
    }

    /**
     * tests whether a given file is a file containing type information.
     *
     * @param testFile the file to test.
     * @param config holds information as to the info file format.
     */
    private static boolean isInfoFile(File testFile, ConfigInfo config){
        String testName = testFile.getAbsolutePath();
        int start = testName.length() - config.INFO_FILE.length();
        int end = testName.length();
        return testName.substring(start, end).equals(config.INFO_FILE);
    }
    
    /**
     * Override of toString method. */
    public String toString() {
        StringBuffer returnString = new StringBuffer("\n  TypeRepository:");
        Collection typeList = types.values();
        Iterator i = typeList.iterator();
        while(i.hasNext()) {
            TypeInfo type = (TypeInfo) i.next();
            returnString.append("\n   " + type.getName());
            if(lockedFeatures.containsValue(type.getName())) {
                returnString.append(" (" + type.getName() + ")");
            }
        }
        return returnString.toString();
    }

    /**
     * Acts as the lock on features, managing the expiry and the releasing of
     * feature information.
     * 
     * @author Chris Holmes, TOPP
     * @version $VERSION$
     */
    private class InternalLock {
        
	/** Class logger */
	//private static Logger LOGGER = 
	//  Logger.getLogger("org.vfny.geoserver.config");
	
	/** Repository of Type and Lock information  */
	//private static TypeRepository repository = null;
	
	/** If no expiry is set then default to a day.*/
	public static final int DEFAULT_EXPIRY = 1440;
	
	/** to turn from expiry minutes to timer milliseconds */
	private static final int MILLISECONDS_PER_MINUTE = 1000 * 60;

	/** The Timer that keeps track of when to expire */
	private Timer expiryTimer = new Timer(true);
	
	/** The task that performs the expire when the time is up*/
	private ExpireTask expiry = new ExpireTask();

	/** The length of the expiry*/
	private int expiryTime;

	/** The string used to access this lock. */
	private String lockId;
	
	/** The feature Types held by this lock. */
	private Set featureTypes = new HashSet();

	
    /** Initializes a lock with the number of minutes until it should expire*/ 
	public InternalLock(String lockId, int expiryMinutes) {
	    LOG.finer("created new lock with id " + lockId + " and expiry "
			 + expiryMinutes);
	    this.lockId = lockId;
	    if (expiryMinutes < 0) {
		expiryMinutes = DEFAULT_EXPIRY;
	    } 
	    this.expiryTime = expiryMinutes * MILLISECONDS_PER_MINUTE;
	    expiryTimer.schedule(expiry, expiryTime);
	}
	
	/** 
	 * Gets the lockId string
	 * @return the lockId */
	public String getId(){
	    return lockId;
	}
	
	/**
	 * Adds a feature type to this internal lock.
	 * TODO: test with multiple featureTypes in one lock, with SOME 
	 * release action.
	 */
	public void addFeatureType(String typeName){
	    featureTypes.add(typeName); 
	    lockedFeatures.put(typeName, this);
	}

	//public void getFeatureTypes

	/**
	 * clears this lock and everything that it holds.
	 */
	public void release(){
	    Iterator featureIter = featureTypes.iterator();
	    while(featureIter.hasNext()){
		lockedFeatures.remove(featureIter.next().toString());
	    }
	    locks.remove(this.lockId);
	    expiryTimer.cancel();
	    
	}

	/**
	 * unlocks based on the releaseAll action.  If true then everything
	 * held by this lock is released, if not, then only the typeName
	 * featureType is.
	 *
	 * @param typeName the name of the featureType to unlock.
	 * @param releaseAll if true then get rid of this lock, if false then
	 * this only unlocks the feature of typeName.
	 */
	public void unlock(String typeName, boolean releaseAll){
	    if (releaseAll) {
		this.release();
	    } else {
		Iterator featureIter = featureTypes.iterator();
		while(featureIter.hasNext()){
		    if (featureIter.next().equals(typeName)){
			lockedFeatures.remove(featureIter.next().toString());
		    }
		}
		if (featureTypes.size() == 0) {
		    locks.remove(this.lockId);
		} else {
		    expiry.cancel();
		    expiry = new ExpireTask();
		    expiryTimer.schedule(expiry, expiryTime);
		}
	    }
	}

	private class ExpireTask extends TimerTask {
	    public void run() {
		LOG.fine("expiring lock: " + lockId);
		release();
		expiryTimer.cancel(); //Terminate the timer thread
	    }
	}
    }
	

}

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
 * @version $VERSION$
 */
public class TypeRepository {
        
    /** Class logger */
    private static Logger LOG = Logger.getLogger("org.vfny.geoserver.config");

    /** Castor-specified type to hold all the  */
    private static TypeRepository repository = null;

    /** Castor-specified type to hold all the  */
    private Random keyMaster = null;

    /** Castor-specified type to hold all the  */
    private Map types = new HashMap();

    /** Castor-specified type to hold all the  */
    private Map locks = new HashMap();

    
    /** Initializes the database and request handler. */ 
    private TypeRepository() {
        ConfigInfo config = ConfigInfo.getInstance();
        File startDir = new File(config.getTypeDir());
        repository.readTypes(startDir, config);

        Date seed = new Date();
        lockMaker = new Random(seed.getTime());
    }
    
    /**
     * Initializes the database and request handler.
     * @param featureTypeName The query from the request object.
     */ 
    public static TypeRepository getInstance() {
        if(repository == null) {
            repository = new TypeRepository();
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
     * Returns a capabilities XML fragment for a specific feature type.
     * @param type The version of the request (0.0.14 or 0.0.15)
     */ 
    private void addType(TypeInfo type) { 
        types.put(type.getName(), type);
    }

    /**
     * Returns a capabilities XML fragment for a specific feature type.
     * @param version The version of the request (0.0.14 or 0.0.15)
     */ 
    private int typeCount() {        
        return types.size();
    }


    /**
     * Returns a capabilities XML fragment for a specific feature type.
     * @param typeName The version of the request (0.0.14 or 0.0.15)
     */ 
    public synchronized String lock(String typeName) {        
        if(locks.containsValue(typeName)) {
            return null;
        } else {
            String key = new String(String.valueOf(keyMaster.nextInt()));
            types.put(typeName, typeName + key);
            return typeName + key;
        }
    }

    /**
     * Returns a capabilities XML fragment for a specific feature type.
     * @param version The version of the request (0.0.14 or 0.0.15)
     */ 
    public synchronized boolean unlock(String typeName, String key) {        
        if(locks.containsKey(typeName + key)) {
            types.remove(typeName + key);
            return true;
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
        if(currentFile.isDirectory()) {
            File[] file = currentFile.listFiles();
            for(int i = 0, n = file.length; i < n; i++) {
                readTypes(file[i], config);
            } 
        } else if(isInfoFile(currentFile, config)) {
            addType(new TypeInfo(currentFile.getAbsolutePath()));
        }
    }

    private static boolean isInfoFile(File testFile, ConfigInfo config){
        String testName = testFile.getAbsolutePath();
        int start = testName.length() - 8;
        int end = testName.length() - 4;
        return testName.substring(start, end).equals(config.TYPE_INFO);
    }

}

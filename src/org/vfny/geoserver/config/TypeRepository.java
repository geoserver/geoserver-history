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
    private Map types = new HashMap();

    
    /** Initializes the database and request handler. */ 
    private TypeRepository() {
        ConfigurationBean config = ConfigurationBean.getInstance();
        File startDir = new File(config.getTypeDir());
        repository.readTypes(startDir, config);
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
    public FeatureTypeBean getType(String name) {        
        return (FeatureTypeBean) types.get(name);
    }

    /**
     * Returns a capabilities XML fragment for a specific feature type.
     * @param version The version of the request (0.0.14 or 0.0.15)
     */ 
    private void addType(FeatureTypeBean type) {        
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
     * This function lists all files in HTML for the meta-data pages.
     * 
     * Simple recursive function to read through all subdirectories and 
     * add all XML files with the name 'info.XXX' to the repository.
     * @param currentFile The top directory from which to start 
     * reading files.
     */
    private void readTypes(File currentFile, ConfigurationBean config) {
        if(currentFile.isDirectory()) {
            File[] file = currentFile.listFiles();
            for(int i = 0, n = file.length; i < n; i++) {
                readTypes(file[i], config);
            } 
        } else if(isInfoFile(currentFile, config)) {
            addType(new FeatureTypeBean(currentFile.getAbsolutePath()));
        }
    }

    private static boolean isInfoFile(File testFile, ConfigurationBean config){
        String testName = testFile.getAbsolutePath();
        int start = testName.length() - 8;
        int end = testName.length() - 4;
        return testName.substring(start, end).equals(config.TYPE_INFO);
    }

}

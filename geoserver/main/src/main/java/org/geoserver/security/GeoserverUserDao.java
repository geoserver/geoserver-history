/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.security;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.userdetails.memory.UserMap;
import org.acegisecurity.userdetails.memory.UserMapEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * A simple DAO reading the property files
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public class GeoserverUserDao implements UserDetailsService {
    /** logger */
    static Logger LOGGER = Logger.getLogger("org.geoserver.security");

    private UserMap userMap;

    private PropertyFileWatcher userDefinitionsFile;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException,
            DataAccessException {
        checkUserMap();

        return userMap.getUser(username);
    }

    /**
     * Either loads the default property file on the first access, or reloads it
     * if it has been modified since last access.
     * 
     * @throws DataAccessResourceFailureException
     */
    private void checkUserMap() throws DataAccessResourceFailureException {
        if (userMap == null || userDefinitionsFile != null
                && userDefinitionsFile.isStale()) {
            try {
                if(userDefinitionsFile == null) {
                    File dd = GeoserverDataDirectory.getGeoserverDataDirectory();
                    File securityDir = GeoserverDataDirectory.findConfigDir(dd, "security");
                    File propFile = new File(securityDir, "users.properties");
                    if(!propFile.exists())
                        throw new DataAccessResourceFailureException("Could not find the "
                                + userDefinitionsFile + " user configuration file!");
                    userDefinitionsFile = new PropertyFileWatcher(propFile);  
                }

                userMap = new UserMap();
                UserMapEditor.addUsersFromProperties(userMap, userDefinitionsFile.getProperties());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An error occurred loading user definitions", e);
            }
        }
    }
}

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.security;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.userdetails.memory.UserMap;
import org.acegisecurity.userdetails.memory.UserMapEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.GeoserverDataDirectory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A simple DAO reading the property files
 *
 * @author Andrea Aime - TOPP
 *
 */
public class GeoserverUserDao implements UserDetailsService {
    /** logger */
    static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.security");
    private UserMap userMap;
    private PropertyFileWatcher userDefinitionsFile;
    private GeoServer geoServer;

    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException, DataAccessException {
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
        InputStream is = null;
        OutputStream os = null;
        if ((userMap == null) || ((userDefinitionsFile != null) && userDefinitionsFile.isStale())) {
            try {
                if (userDefinitionsFile == null) {
                    File securityDir = GeoserverDataDirectory.findCreateConfigDir("security");
                    File propFile = new File(securityDir, "users.properties");

                    if (!propFile.exists()) {
                        // we're probably dealing with an old data dir, create
                        // the file without
                        // chaning the username and password if possible
                        Properties p = new Properties();

                        if ((geoServer != null) && (geoServer.getAdminUserName() != null)
                                && !geoServer.getAdminUserName().trim().equals("")) {
                            p.put(geoServer.getAdminUserName(),
                                geoServer.getAdminPassword() + ",ROLE_ADMINISTRATOR");
                        } else {
                            p.put("admin", "geoserver,ROLE_ADMINISTRATOR");
                        }

                        os = new FileOutputStream(propFile);
                        p.store(os, "Format: name=password,ROLE1,...,ROLEN");
                        os.close();
                        
                        // setup a sample service.properties
                        File serviceFile = new File(securityDir, "service.properties");
                        os = new FileOutputStream(serviceFile);
                        is = GeoserverUserDao.class.getResourceAsStream("serviceTemplate.properties");
                        byte[] buffer = new byte[1024];
                        int count = 0;
                        while((count = is.read(buffer)) > 0) {
                            os.write(buffer, 0, count);
                        }
                    }

                    userDefinitionsFile = new PropertyFileWatcher(propFile);
                }

                userMap = new UserMap();
                UserMapEditor.addUsersFromProperties(userMap, userDefinitionsFile.getProperties());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An error occurred loading user definitions", e);
            } finally {
                if(is != null)
                    try { is.close(); } catch (IOException ei) { /* nothing to do */ }
                if(os != null)
                    try { os.close(); } catch (IOException eo) { /* nothing to do */ }
            }
        }
    }

    public GeoServer getGeoServer() {
        return geoServer;
    }

    public void setGeoServer(GeoServer geoServer) {
        this.geoServer = geoServer;
    }
}

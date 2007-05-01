/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.security;

import org.acegisecurity.ConfigAttributeEditor;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.userdetails.memory.UserMap;
import org.acegisecurity.userdetails.memory.UserMapEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GeoserverUserDao implements UserDetailsService {
    /** logger */
    static Logger LOGGER = Logger.getLogger("org.geoserver.security");
    private UserMap userMap;

    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException, DataAccessException {
        checkUserMap();

        return userMap.getUser(username);
    }

    private void checkUserMap() throws DataAccessResourceFailureException {
        if (userMap == null) {
            try {
                File dd = GeoserverDataDirectory.getGeoserverDataDirectory();
                File securityDir = GeoserverDataDirectory.findConfigDir(dd, "security");
                File userDefinitions = new File(securityDir, "users.properties");

                if (userDefinitions.exists()) {
                    userMap = new UserMap();

                    Properties p = new Properties();
                    p.load(new FileInputStream(userDefinitions));
                    UserMapEditor.addUsersFromProperties(userMap, p);
                } else {
                    throw new DataAccessResourceFailureException("Could not find the "
                        + userDefinitions + " user configuration file!");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An error occurred loading user definitions", e);
            }
        }
    }

    /**
     * Modifies the internal <code>UserMap</code> to reflect the
     * <code>Properties</code> instance passed. This helps externalise user
     * information to another file etc.
     *
     * @param props
     *            the account information in a <code>Properties</code> object
     *            format
     */
    public void setUserProperties(Properties props) {
        UserMap userMap = new UserMap();
        this.userMap = UserMapEditor.addUsersFromProperties(userMap, props);
    }
}

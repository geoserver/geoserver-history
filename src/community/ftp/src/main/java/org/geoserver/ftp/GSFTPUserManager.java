/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ftp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.geoserver.config.GeoServerDataDirectory;
import org.geotools.util.logging.Logging;

/**
 * Maps GeoServer users to
 * 
 * @author aaime
 * 
 */
public class GSFTPUserManager implements UserManager {
    static final Logger LOGGER = Logging.getLogger(GSFTPUserManager.class);

    /**
     * The role given to the administrators
     */
    static final String ADMIN_ROLE = "ROLE_ADMINISTRATOR";

    /**
     * The default user
     */
    static final String DEFAULT_USER = "admin";

    /**
     * The default password
     */
    static final String DEFAULT_PASSWORD = "geoserver";

    UserDetailsService userService;

    GeoServerDataDirectory dataDir;

    public GSFTPUserManager(UserDetailsService userService, GeoServerDataDirectory dataDir) {
        this.userService = userService;
        this.dataDir = dataDir;
    }

    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
        if (authentication instanceof UsernamePasswordAuthentication) {
            UsernamePasswordAuthentication upa = (UsernamePasswordAuthentication) authentication;
            try {
                // gather the user
                User user = getUserByName(upa.getUsername());
                if (user == null) {
                    throw new AuthenticationFailedException();
                }

                // password checks
                if (!user.getPassword().equals(upa.getPassword())) {
                    throw new AuthenticationFailedException();
                }
                
                // scary message for admins if the username/password has not been changed 
                if (DEFAULT_USER.equals(user.getName())
                        && DEFAULT_PASSWORD.equals(user.getPassword())) {
                    LOGGER.log(Level.SEVERE, "The default admin/password combination has not been "
                            + "modified, this makes the embedded FTP server an "
                            + "open file host for everybody to use!!!");
                }

                // is the user enabled?
                if (!user.getEnabled()) {
                    throw new AuthenticationFailedException();
                }

                return user;
            } catch (AuthenticationFailedException e) {
                throw e;
            } catch (Exception e) {
                LOGGER.log(Level.INFO, "FTP authentication failure", e);
                throw new AuthenticationFailedException(e);
            }
        } else {
            throw new AuthenticationFailedException();
        }
    }

    public void delete(String username) throws FtpException {
        throw new FtpException("No custom user handling on this instance");
    }

    public boolean doesExist(String username) throws FtpException {
        try {
            userService.loadUserByUsername(username);
            return true;
        } catch (UsernameNotFoundException e) {
            return false;
        } catch (Throwable t) {
            throw new FtpException("Internal error occurred", t);
        }
    }

    public String getAdminName() throws FtpException {
        throw new FtpException("No custom user handling on this instance");
    }

    public String[] getAllUserNames() throws FtpException {
        throw new FtpException("No custom user handling on this instance");
    }

    public User getUserByName(String username) throws FtpException {
        try {
            // check if we know the user
            UserDetails ud = userService.loadUserByUsername(username);
            if (ud == null) {
                return null;
            }

            // basic ftp user setup
            BaseUser user = new BaseUser();
            user.setName(ud.getUsername());
            user.setPassword(ud.getPassword());
            user.setEnabled(true);
            user.setHomeDirectory(dataDir.findOrCreateDataRoot().getAbsolutePath());

            // enable only admins
            for (GrantedAuthority authority : ud.getAuthorities()) {
                final String userRole = authority.getAuthority();
                if (ADMIN_ROLE.equals(userRole)) {
                    user.setEnabled(true);
                }
            }

            // allow writing
            List<Authority> authorities = new ArrayList<Authority>();
            authorities.add(new WritePermission());
            authorities.add(new ConcurrentLoginPermission(Integer.MAX_VALUE, Integer.MAX_VALUE));
            user.setAuthorities(authorities);

            return user;
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }

    public boolean isAdmin(String username) throws FtpException {
        return false;
    }

    public void save(User user) throws FtpException {
        throw new FtpException("No custom user handling on this instance");
    }

}

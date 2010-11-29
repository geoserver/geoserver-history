/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ftp;

import java.io.File;
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
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.geoserver.config.GeoServerDataDirectory;
import org.geotools.util.logging.Logging;

/**
 * Maps GeoServer users to Apache's FTP Server {@link User}s.
 * <p>
 * <h2>User home directory</h2>
 * If the logged in user has administrative privileges, the home directory is set to the geoserver
 * data root directory (e.g {@code <gs data dir>/data}). For non admin users, the home directory is
 * set to a subdirectory of the geoserver data root directory called the same than the user name
 * (e.g. {@code <gs data dir>/data/incoming/<user name>}).
 * </p>
 * 
 * @author aaime
 * @author groldan
 */
public class GSFTPUserManager implements org.apache.ftpserver.ftplet.UserManager {

    private static final Logger LOGGER = Logging.getLogger(GSFTPUserManager.class);

    /**
     * The role given to the administrators
     */
    private static final String ADMIN_ROLE = "ROLE_ADMINISTRATOR";

    /**
     * The default user
     */
    private static final String DEFAULT_USER = "admin";

    /**
     * The default password
     */
    private static final String DEFAULT_PASSWORD = "geoserver";

    private UserDetailsService userService;

    GeoServerDataDirectory dataDir;

    public GSFTPUserManager(UserDetailsService userService, GeoServerDataDirectory dataDir) {
        this.userService = userService;
        this.dataDir = dataDir;
    }

    /**
     * @param authentication
     *            one of {@link org.apache.ftpserver.usermanager.AnonymousAuthentication} or
     *            {@link org.apache.ftpserver.usermanager.UsernamePasswordAuthentication}
     * @throws AuthenticationFailedException
     *             if given an {@code AnonymousAuthentication}, or an invalid/disabled user
     *             credentials
     */
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

                // scary message for admins if the username/password has not
                // been changed
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

                // user authenticated, lets make sure his home directory exists
                File homeDirectory = new File(user.getHomeDirectory());
                if (!homeDirectory.exists()) {
                    LOGGER.fine("Creating FTP home directory for user " + user.getName() + " at "
                            + homeDirectory.getAbsolutePath());
                    homeDirectory.mkdirs();
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

    /**
     * @throws FtpException
     *             always, operation not supported.
     * @see org.apache.ftpserver.ftplet.UserManager#delete(java.lang.String)
     */
    public void delete(String username) throws FtpException {
        throw new FtpException("No custom user handling on this instance");
    }

    /**
     * @see org.apache.ftpserver.ftplet.UserManager#doesExist(java.lang.String)
     */
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

    /**
     * @see org.apache.ftpserver.ftplet.UserManager#getAdminName()
     */
    public String getAdminName() throws FtpException {
        throw new FtpException("No custom user handling on this instance");
    }

    /**
     * @see org.apache.ftpserver.ftplet.UserManager#getAllUserNames()
     */
    public String[] getAllUserNames() throws FtpException {
        throw new FtpException("No custom user handling on this instance");
    }

    /**
     * Maps a GeoServer user to an ftp {@link User} by means of the provided Spring Security's
     * {@link UserDetailsService}.
     * <p>
     * The user's home directory is set to the root geoserver data dir in the case of administrators
     * or to {@code <data dir>/incoming/<user name>} in case of non administrators.
     * 
     * @see org.apache.ftpserver.ftplet.UserManager#getUserByName(java.lang.String)
     */
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
            final File dataRoot = dataDir.findOrCreateDataRoot();

            // enable only admins and non anonymous users
            boolean isGSAdmin = false;
            for (GrantedAuthority authority : ud.getAuthorities()) {
                final String userRole = authority.getAuthority();
                if (ADMIN_ROLE.equals(userRole)) {
                    isGSAdmin = true;
                    break;
                }
            }

            if (isGSAdmin) {
                user.setHomeDirectory(dataRoot.getAbsolutePath());
            } else {
                /*
                 * This resolves the user's home directory to data/incoming/<user name> but does not
                 * create the directory if it does not already exist. That is left to when the user
                 * is authenticated, check the authenticate() method above.
                 */
                File userDir = new File(new File(dataRoot, "incoming"), user.getName());
                user.setHomeDirectory(userDir.getAbsolutePath());
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

    /**
     * @return {@code false}
     * @see org.apache.ftpserver.ftplet.UserManager#isAdmin(java.lang.String)
     */
    public boolean isAdmin(final String username) throws FtpException {
        return false;
    }

    /**
     * @throws FtpException
     *             always, operation not supported
     * @see org.apache.ftpserver.ftplet.UserManager#save(org.apache.ftpserver.ftplet.User)
     */
    public void save(User user) throws FtpException {
        throw new FtpException("No custom user handling on this instance");
    }

}

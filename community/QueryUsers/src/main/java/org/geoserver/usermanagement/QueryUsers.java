/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.usermanagement;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.ext.spring.SpringContext;
import org.springframework.dao.DataAccessException;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
  * This is the original Restlet that was written to provide the REST user
  * management interface.  It is currently disused but I'm keeping it around for
  * reference at the moment.
  * @author David Winslow <dwinslow@openplans.org>
  */
public class QueryUsers extends Application {
    private UserDetailsService myUserService;

    public QueryUsers(Context parentContext) {
        super(parentContext);
    }

    public void userExists(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Map kvPairs = KvpRequestReader.parseKvpSet(request.getQueryString());

        String message = "If you see this someone screwed up";
        String username = kvPairs.get("USERNAME").toString();

        try {
            UserDetails user = myUserService.loadUserByUsername(username);
            GrantedAuthority[] auths = user.getAuthorities();
            message = user.getUsername() + ": ";

            for (int i = 0; i < auths.length; i++) {
                message += (auths[i].toString() + "; ");
            }
        } catch (UsernameNotFoundException unfe) {
            message = "User " + username + " does not exist.";
        } catch (DataAccessException dae) {
            message = "Could not access database, please try again later.";
        }

        response.getOutputStream().write(message.getBytes());
    }

    public void createUser(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, ConfigurationException {
        Map kvPairs = KvpRequestReader.parseKvpSet(request.getQueryString());

        String username = kvPairs.get("USERNAME").toString();
        String passwd = kvPairs.get("PASSWORD").toString();
        String roles = kvPairs.get("ROLES").toString();

        String message = "user: " + username + "\n password: " + passwd + "\n roles: " + roles;

        File securityDir = GeoserverDataDirectory.findCreateConfigDir("security");
        File propFile = new File(securityDir, "users.properties");

        if (propFile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(propFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                Properties p = new Properties();
                p.load(bis);
                bis.close();
                p.setProperty(username, passwd + "," + roles);

                FileOutputStream fos = new FileOutputStream(propFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                p.store(bos, "Format: name=password,ROLE1,...,ROLEN");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(message);
        response.getOutputStream().write(message.getBytes());
    }

    public Restlet createRoot() {
        Router router = new Router();

        SpringContext springContext = new SpringContext(getContext());

        springContext.getXmlConfigRefs().add("war://WEB-INF/classes/applicationContext.xml");
        //       router.setRequiredScore(0);
        //       router.attach("/roles", new UserRestlet("Role Management Page"));
        router.attach("/user/{name}", new UserRestlet(null));
        router.attach("/dummy", new DummyRestlet(springContext));

        //       router.attach("/geoserver/users/{user}/roles", new UserRestlet("getting role information"));
        return router;
    }
}

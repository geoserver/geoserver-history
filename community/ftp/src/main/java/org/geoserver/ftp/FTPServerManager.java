/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ftp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.geotools.util.logging.Logging;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * Starts and stop the FTP server
 * 
 * @author Andrea Aime - OpenGeo
 */
public class FTPServerManager implements ApplicationListener {

    static final Logger LOGGER = Logging.getLogger(Logger.class);

    FtpServer ftp;

    UserManager userManager;

    public FTPServerManager(UserManager userManager) {
        this.userManager = userManager;
        configureServer();
    }
    
    void configureServer() {
        FtpServerFactory serverFactory = new FtpServerFactory();

        // configure a listener on port 8021
        ListenerFactory factory = new ListenerFactory();
        factory.setPort(8021);
        serverFactory.addListener("default", factory.createListener());
        
        // link the server user management to the GS one
        serverFactory.setUserManager(userManager);

        // start the server
        ftp = serverFactory.createServer();
    }

    public void startServer() throws FtpException {
        if(ftp.isStopped() || ftp.isSuspended()) {
            ftp.start();
        }
    }

    public void stopServer() {
        if(!ftp.isStopped()) {
            ftp.stop();
        }
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            try {
                startServer();
            } catch (FtpException e) {
                LOGGER.log(Level.SEVERE, "Could not start the embedded FTP server", e);
            }
        } else if (event instanceof ContextStoppedEvent || event instanceof ContextClosedEvent) {
            stopServer();
        }
    }

}

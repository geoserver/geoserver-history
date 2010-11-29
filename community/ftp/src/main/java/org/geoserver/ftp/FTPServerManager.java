/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ftp;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.geotools.util.logging.Logging;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * Starts and stop the FTP server.
 * 
 * @author Andrea Aime - OpenGeo
 */
public class FTPServerManager implements ApplicationListener {

    static final Logger LOGGER = Logging.getLogger(FTPServerManager.class);

    private FtpServer ftp;

    private UserManager userManager;

    private FtpLetFinder callbacks;

    private FTPConfigLoader loader;

    private FTPConfig config;

    /**
     * Sets up the {@link FtpServer FTP Server} managed by this bean using the provided
     * {@code userManager}
     * 
     * @param userManager
     */
    public FTPServerManager(final UserManager userManager, FtpLetFinder callbacks,
            FTPConfigLoader loader) {
        this.userManager = userManager;
        this.callbacks = callbacks;
        this.loader = loader;
        this.config = loader.load();
        configureServer();
    }

    /**
     * Creates the FTP server and sets up the FTP listeners by looking up the application context
     * for instances of the {@link FTPCallback} extension point.
     */
    private void configureServer() {
        FtpServerFactory serverFactory = new FtpServerFactory();

        // configure a listener on port 8021
        ListenerFactory factory = new ListenerFactory();
        factory.setPort(config.getFtpPort());
        serverFactory.addListener("default", factory.createListener());

        // link the server user management to the GS one
        serverFactory.setUserManager(userManager);

        // find out the listeners
        Map<String, Ftplet> ftplets = callbacks.getFtpLets();
        serverFactory.setFtplets(ftplets);

        // start the server
        ftp = serverFactory.createServer();
    }

    public void startServer() throws FtpException {
        if (!config.isEnabled()) {
            return;
        }
        if (ftp.isStopped() || ftp.isSuspended()) {
            LOGGER.info("Starting GeoServer FTP Server on port " + config.getFtpPort());
            ftp.start();
            LOGGER.info("GeoServer FTP Server started");
        }
    }

    public void stopServer() {
        if (!ftp.isStopped()) {
            LOGGER.info("Stopping GeoServer FTP Server on port " + config.getFtpPort());
            ftp.stop();
            LOGGER.info("GeoServer FTP Server stopped");
        }
    }

    /**
     * Listens to the application context events in order to automatically start/stop the FTP server
     * upon application startup/shutdown.
     * 
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     * @see ContextRefreshedEvent
     * @see ContextStoppedEvent
     * @see ContextClosedEvent
     */
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

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver;

import org.apache.catalina.Connector;
import org.apache.catalina.Context;
import org.apache.catalina.Deployer;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.logger.SystemOutLogger;
import org.apache.catalina.startup.Embedded;
import java.io.File;
import java.io.IOException;
import java.net.URL;

//Logging system
import java.util.logging.Logger;


/**
 * This class handles the starting and stopping of the embedded Tomcat server.
 * It does some rudementary checking to make sure that the required filesystem
 * and path variables are in place before it begins.
 *
 * @author Rob Hranac, TOPP
 * @version $Id: EmbeddedTomcat.java,v 1.6 2003/09/05 20:54:09 cholmesny Exp $
 */
public class EmbeddedTomcat {
    /** The logger for the filter module. */
    private static final Logger LOG = Logger.getLogger(
            "org.vfny.geoserver.EmbeddedTomcat");

    /** Sets the global test file for the instance to check if all is OK. */
    private static final String TEST_FILE = "/lib/catalina/embedded.jar";

    // TODO 3: Would be nicer to check entire directory structure.

    /** Points to the server working directory. */
    private static final String SERVER_DIR = "/server";

    /** Time to wait and run thread before looking for stop file (in ms). */
    private static final int STOP_CHECK_FREQUENCEY = 3000;

    /** Stop file name. */
    private static final String STOP_FILE = "/stop";

    /** Remembers the catalina path */
    private String path = null;

    /** Catalina embedded object */
    private Embedded embedded = null;

    /** Catalina host object */
    private Host host = null;

    /**
     * Default Constructor
     */
    public EmbeddedTomcat() {
    }

    /**
     * Basic Accessor setting the value of the context path
     *
     * @param path The catalina server path.
     */
    public void setPath(String path) {
        this.path = path + SERVER_DIR;
    }

    /**
     * Basic Accessor returning the value of the context path
     *
     * @return path The catalina server path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Starts the Tomcat server.
     *
     * @throws Exception for any problems
     */
    public void startTomcat() throws Exception {
        Engine engine = null;

        // Set the home directory
        System.setProperty("catalina.home", getPath());

        // Create an embedded server
        embedded = new Embedded();

        // print all log statments to standard error
        //uncomment two next statements for debugging.
        //embedded.setDebug(5);
        //Geotools.init("Log4JFormatter", Level.FINEST);
        embedded.setLogger(new SystemOutLogger());

        // Create an engine
        engine = embedded.createEngine();
        engine.setDefaultHost("localhost");

        // Create a default virtual host
        host = embedded.createHost("localhost", getPath() + "/webapps");

        // Create the ROOT context
        Context context = embedded.createContext("", getPath()
                + "/webapps/ROOT");
        context.setLogger(embedded.getLogger());

        //context.setDebug(5);
        host.addChild(context);

        engine.addChild(host);

        embedded.addEngine(engine);

        // Assemble and install a default HTTP connector
        Connector connector = embedded.createConnector(null, 8081, false);
        embedded.addConnector(connector);

        // Start the embedded server      
        embedded.start();
    }

    /**
     * This method Stops the Tomcat server.
     *
     * @throws Exception if anything goes wrong.
     */
    public void stopTomcat() throws Exception {
        // Stop the embedded server
        embedded.stop();
    }

    /**
     * Registers a WAR with the container.
     *
     * @param contextPath Path under which the application will be registered
     * @param warFile URL of the WAR to be registered.
     *
     * @throws Exception if anything goes wrong.
     */
    public void registerWAR(String contextPath, URL warFile)
        throws Exception {
        LOG.info("registered war: " + contextPath);

        if (contextPath == null) {
            throw new Exception("Invalid Path : " + contextPath);
        }

        if (contextPath.equals("/")) {
            contextPath = "";
        }

        if (warFile == null) {
            throw new Exception("Invalid WAR : " + warFile);
        }

        Deployer deployer = (Deployer) host;
        Context context = deployer.findDeployedApp(contextPath);

        if (context != null) {
            throw new Exception("Context " + contextPath + " Already Exists!");
        }

        deployer.install(contextPath, warFile);
        deployer.setLogger(new SystemOutLogger());
    }

    /**
     * Unregisters a WAR from the web server.
     *
     * @param contextPath - the context path to be removed
     *
     * @throws Exception if anything goes wrong.
     */
    public void unregisterWAR(String contextPath) throws Exception {
        Context context = host.map(contextPath);

        if (context != null) {
            embedded.removeContext(context);
        } else {
            throw new Exception("Context does not exist for named path : "
                + contextPath);
        }
    }

    /**
     * Handles server creation and destruction.
     *
     * @param args Arguments from the user (must be 'stop' or 'start').
     */
    public static void main(String[] args) {
        EmbeddedTomcat tomcat = new EmbeddedTomcat();

        // if geoserver home is not defined, make a guess that it is the current
        //  user directory.  check to make sure this is the case.  if this is not
        //  the case, then bail out with appropriate error message.
        // also, set path to the shut down file.
        if (System.getProperty("GEOSERVER_HOME") != null) {
            File testHome = new File(System.getProperty("GEOSERVER_HOME")
                    + TEST_FILE);

            if (testHome.exists()) {
                tomcat.setPath(System.getProperty("GEOSERVER_HOME"));
            } else {
                System.out.println("Startup/Shutdown command unsucessful...");
                System.out.println("You appear to have specified the ");
                System.out.println("GEOSERVER_HOME environmental variable");
                System.out.println("incorrectly.");
                System.exit(1);
            }
        } else {
            File testHome = new File(System.getProperty("user.dir") + TEST_FILE);

            if (testHome.exists()) {
                tomcat.setPath(System.getProperty("user.dir"));
            } else {
                System.out.println("Startup/Shutdown command unsucessful...");
                System.out.println("You have not specified the GEOSERVER_HOME ");
                System.out.println("environmental variable and you are not");
                System.out.println("starting GeoServer from the root GeoServer");
                System.out.println("directory.");
                System.exit(1);
            }
        }

        // handle start conditions
        File shutDown = new File(tomcat.getPath() + STOP_FILE);

        if (args[0].equals("start")) {
            try {
                // make sure to delete shut down signal file
                shutDown.delete();

                // start tomcat server and notify user
                System.out.println("Starting GeoServer.....");
                LOG.info("about to start tomcat");
                tomcat.startTomcat();

                // check for shutdown file at specified frequency
                while (!shutDown.exists()) {
                    Thread.sleep(STOP_CHECK_FREQUENCEY);
                }

                // stop server on termination
                System.out.println("stopping server...");
                tomcat.stopTomcat();
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // handle stop conditions
        else if (args[0].equals("stop")) {
            System.out.println("stopping server...");

            try {
                shutDown.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Usage: ");
            System.out.println(
                " java -cp $CLASSPATH org.vfny.geoserver.EmbeddedTomcat [command]");
            System.out.println(" where [command]: start (starts the server)");
            System.out.println("                  stop (stops the server)");
        }
    }
}

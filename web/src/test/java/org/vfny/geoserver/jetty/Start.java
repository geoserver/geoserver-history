package org.vfny.geoserver.jetty;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;


/**
 * Jetty starter, will run geoserver inside the Jetty web container.<br>
 * Useful for debugging, especially in IDE were you have direct dependencies
 * between the sources of the various modules (such as Eclipse). 
 * @author wolf
 *
 */
public class Start {
	private static final Logger log = LogManager.getLogManager().getLogger(Start.class.getName());
	
	public static void main(String[] args) {
		Server jettyServer = null;
        try {
            jettyServer = new Server();

            SocketConnector conn = new SocketConnector();
            conn.setPort(8080);
            jettyServer.setConnectors(new Connector[]{conn});

            WebAppContext wah = new WebAppContext();
            wah.setContextPath("/geoserver");
            wah.setWar("src/main/webapp");
            jettyServer.setHandler(wah);

            jettyServer.start();
        }
        catch (Exception e) {
            log.severe("Could not start the Jetty server: " + e);
            if (jettyServer != null) {
                try {
                    jettyServer.stop();
                }
                catch (Exception e1) {
                    log.severe("Unable to stop the jetty server: " + e1);
                }
            }
        }
	}
}

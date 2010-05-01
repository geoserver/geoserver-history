/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.opengis.filter.FilterFactory;


/** 
 * Utility class used to check wheter REMOTE_OWS_XXX related tests can be run against Sigma
 * or not.
 * @author Andrea Aime - TOPP
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class RemoteOWSTestSupport {

    // support for remote OWS layers
    public static final String TOPP_STATES = "topp:states";
    
    public static final String WFS_SERVER_URL = "http://demo.opengeo.org/geoserver/wfs?";
    
    static Boolean remoteStatesAvailable;
        
    public static boolean isRemoteStatesAvailable(Logger logger) {
        if(remoteStatesAvailable == null) {
            // let's see if the remote ows tests are enabled to start with
            String value = System.getProperty("remoteOwsTests");
            if(value == null || !"TRUE".equalsIgnoreCase(value)) {
                logger.log(Level.WARNING, "Skipping remote OWS test because they were not enabled via -DremoteOwsTests=true");
                remoteStatesAvailable = Boolean.FALSE;
            } else {
                // let's check if the remote WFS tests are runnable
                try {
                    WFSDataStoreFactory factory = new WFSDataStoreFactory();
                    Map<String, Serializable> params = new HashMap(factory.getImplementationHints());
                    URL url = new URL(WFS_SERVER_URL + "service=WFS&request=GetCapabilities");
                    params.put(WFSDataStoreFactory.URL.key, url);
                    params.put(WFSDataStoreFactory.TRY_GZIP.key, Boolean.TRUE);
                    //give it five seconds to respond...
                    params.put(WFSDataStoreFactory.TIMEOUT.key, Integer.valueOf(5000));
                    DataStore remoteStore = factory.createDataStore(params);
                    FeatureSource fs = remoteStore.getFeatureSource(TOPP_STATES);
                    remoteStatesAvailable = Boolean.TRUE;
                    // check a basic response can be answered correctly
                    DefaultQuery dq = new DefaultQuery(TOPP_STATES);
                    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
                    dq.setFilter(ff.greater(ff.property("PERSONS"), ff.literal(20000000)));
                    FeatureCollection fc = fs.getFeatures(dq);
                    if(fc.size() != 1) {
                        logger.log(Level.WARNING, "Remote database status invalid, there should be one and only one " +
                                "feature with more than 20M persons in topp:states");
                        remoteStatesAvailable = Boolean.FALSE;
                    }
                    
                    logger.log(Level.WARNING, "Remote OWS tests are enabled, remote server appears to be up");
                } catch(IOException e) {
                    logger.log(Level.WARNING, "Skipping remote OWS test, either demo  " +
                            "is down or the topp:states layer is not there", e);
                    remoteStatesAvailable = Boolean.FALSE;
                }
            }
        }
        return remoteStatesAvailable.booleanValue();
    }
    
}

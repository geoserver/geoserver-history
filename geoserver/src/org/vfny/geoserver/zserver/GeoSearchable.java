/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.zserver;

// Information Retrieval Interfaces
import java.util.Observer;
import java.util.Properties;
import java.util.logging.Logger;

import com.k_int.IR.IRQuery;
import com.k_int.IR.SearchTask;


/**
 * An implementation of searchable derived from DemoSearchable, which was A
 * sample implementation of searchable that returns random numbers of hits and
 * random result records.
 *
 * @author Chris Holmes, TOPP
 * @author:     Ian Ibbotson (ian.ibbotson at k-int.com) Company:     KI
 *         REVISIT: get rid of this class.  It may have a better purpose with
 *         other z39.50 implementations, but for our purposes it really does
 *         not do much.  Probably would make the most sense to just have our
 *         ZServerAssociation create its own tasks.
 * @version $Id: GeoSearchable.java,v 1.3.6.1 2003/12/30 23:00:41 dmzwiers Exp $ modified (simplified) from DemoSearchable:  Copyright:   Copyright (C) 1999-2001 Knowledge Integration Ltd.
 */
public class GeoSearchable implements com.k_int.IR.Searchable {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.zserver");
    private Properties properties = null;

    /**
     * no argument constructo.
     */
    public GeoSearchable() {
        LOGGER.finer("created GeoSearchable object");
    }

    /**
     * inits the server with these properties.
     *
     * @param p the properties to init with.
     */
    public void init(Properties p) {
        this.properties = p;
    }

    /**
     * destroy the searchable object. Shut down the searchable object
     * entirely. Release all held resources, make the object ready for  GC.
     * Try to release in here instead of on finalize.
     */
    public void destroy() {
    }

    /**
     * Provide information about the type of Searchable  object this
     * realisation is
     *
     * @return the type of searchable object.
     */
    public int getManagerType() {
        return com.k_int.IR.Searchable.SPECIFIC_SOURCE;
    }

    /**
     * Create a SearchTask. Evaluate the query with the Tasks evaluate method.
     *
     * @param q The query to get results for.
     * @param user_data not currently used, needed to implement interface.
     *
     * @return the search task with q as the query.
     */
    public SearchTask createTask(IRQuery q, Object user_data) {
        return this.createTask(q, user_data, null);
    }

    /**
     * Create the search task.   Evaluate the query with  the Tasks evaluate
     * method.
     *
     * @param q The query to get results for.
     * @param user_data not used (use null).
     * @param observers not implemented (use null).
     *
     * @return the search task with q as the query.
     */
    public SearchTask createTask(IRQuery q, Object user_data,
        Observer[] observers) {
        /* Implementation notes: If there is a need for user_data,
         * then create a new constructor for GeoSearchTask that uses it.
         * As for observers, check out SearchTask, which GeoSearchTask
         * extends. */
        GeoSearchTask retval = new GeoSearchTask(this, q);

        return retval;
    }

    /**
     * gets the properties of the server.
     *
     * @return a property class holding the server's information.
     */
    public Properties getServerProps() {
        return properties;
    }
}

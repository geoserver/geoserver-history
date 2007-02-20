/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;


/**
 * Interface used for ServiceMode strategy objects.
 *
 * <p>
 * This interface is used to plug-in the implementation for our doService request in
 * the mannerof the strategy pattern.
 * </p>
 * <p>
 * Service Strategies are created using the 'prototype' pattern. Upon each
 * service invocation a new instsance is created by cloning the intial prototype.
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface ServiceStrategy extends Cloneable {
    /**
     * DOCUMENT ME!
     *
     * @return A string used to identify the strategy.
     */
    public String getId();

    /**
     * Get a OutputStream we can use to add content.<p>JG - Can we
     * replace this with a Writer?</p>
     *
     * @param response
     *
     * @return
     *
     * @throws IOException
     */
    public OutputStream getDestination(HttpServletResponse response)
        throws IOException;

    /**
     * Complete opperation in the positive.<p>Gives service a chance to
     * finish with destination, and clean up any resources.</p>
     *
     * @throws IOException DOCUMENT ME!
     */
    public void flush() throws IOException;

    /**
     * Complete opperation in the negative.<p>Gives ServiceConfig a
     * chance to clean up resources</p>
     */
    public void abort();

    /**
     * Clones the service strategy.
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException;
}

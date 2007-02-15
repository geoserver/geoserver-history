/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;


/**
 * A default output strategy which simple writes all output to the output
 * stream of the response.
 * <p>
 * This is the output strategy used by {@link Dispatcher} when no other strategy
 * can be found.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class DefaultOutputStrategy implements ServiceStrategy {
    /**
     * @return The string "default".
     */
    public String getId() {
        return "default";
    }

    /**
     * @return response.getOutputStream();
     */
    public OutputStream getDestination(HttpServletResponse response)
        throws IOException {
        return response.getOutputStream();
    }

    /**
     * Calls response.getOutputStream().flush()
     */
    public void flush(HttpServletResponse response) throws IOException {
        response.getOutputStream().flush();
    }

    /**
     * Does nothing.
     */
    public void abort() {
        //do nothing
    }

    public Object clone() throws CloneNotSupportedException {
        return new DefaultOutputStrategy();
    }
}

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets;

import org.geoserver.ows.ServiceStrategy;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;


/**
 * A safe Service strategy that buffers output until writeTo completes.
 *
 * <p>
 * This strategy wastes memory, for saftey. It represents a middle ground
 * between SpeedStrategy and FileStrategy
 * </p>
 *
 * @author jgarnett
 */
public class BufferStrategy implements ServiceStrategy {
    /** DOCUMENT ME!  */
    ByteArrayOutputStream buffer = null;

    public String getId() {
        return "BUFFER";
    }

    /**
     * Provides a ByteArrayOutputStream for writeTo.
     *
     * @param response Response being processed.
     *
     * @return A ByteArrayOutputStream for writeTo opperation.
     *
     * @throws IOException DOCUMENT ME!
     */
    public OutputStream getDestination(HttpServletResponse response)
        throws IOException {
        buffer = new ByteArrayOutputStream(1024 * 1024);

        return buffer;
    }

    /**
     * Copies Buffer to Response output output stream.
     *
     * @throws IOException If the response outputt stream is unavailable.
     */
    public void flush(HttpServletResponse response) throws IOException {
        if ((buffer == null) || (response == null)) {
            return; // should we throw an Exception here
        }

        OutputStream out = response.getOutputStream();
        buffer.writeTo(out);

        buffer = null;
    }

    /**
     * Clears the buffer with out writing anything out to response.
     *
     * @see org.geoserver.ows.ServiceStrategy#abort()
     */
    public void abort() {
        buffer = null;
    }

    public Object clone() throws CloneNotSupportedException {
        return new BufferStrategy();
    }
}

package org.vfny.geoserver.servlets;

import java.io.BufferedOutputStream;
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
	
	public String getId() {
		return "BUFFER";
	}
	
    /** DOCUMENT ME!  */
    ByteArrayOutputStream buffer = null;

    /** DOCUMENT ME!  */
    private HttpServletResponse response;

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
        this.response = response;
        buffer = new ByteArrayOutputStream(1024 * 1024);

        return buffer;
    }

    /**
     * Copies Buffer to Response output output stream.
     *
     * @throws IOException If the response outputt stream is unavailable.
     */
    public void flush() throws IOException {
        if ((buffer == null) || (response == null)) {
            return; // should we throw an Exception here
        }

        OutputStream out = response.getOutputStream();
        BufferedOutputStream buffOut = new BufferedOutputStream(out, 1024 * 1024);
        buffer.writeTo(buffOut);
        buffOut.flush();
    }

    /**
     * Clears the buffer with out writing anything out to response.
     *
     * @see org.vfny.geoserver.servlets.ServiceStrategy#abort()
     */
    public void abort() {
        if (buffer == null) {
            return;
        }
    }
    
    public Object clone() throws CloneNotSupportedException {
		return new BufferStrategy();
    }
}

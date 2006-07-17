package org.vfny.geoserver.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.vfny.geoserver.util.PartialBufferedOutputStream;

/**
 * <b>PartialBufferStrategy</b><br>
 * Oct 19, 2005<br>
 * 
 * <b>Purpose:</b><br>
 * This strategy will buffer the response before it starts streaming it to the user. This 
 * will allow for errors to be caught early so a proper error message can be sent to the
 * user. Right now it buffers the first 20KB, enough for a full getCapabilities document.
 * 
 * @author Brent Owens (The Open Planning Project)
 * @version 
 */
public class PartialBufferStrategy implements ServiceStrategy 
{
    /** Class logger */
    protected static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.servlets");

    private PartialBufferedOutputStream out = null;

    private int bufferSize;
    
    public String getId() {
    		return "PARTIAL-BUFFER";
    }
    
    public void setBufferSize(int bufferSize) {
    		this.bufferSize = bufferSize;
    }
    
    private int bufferedSize() {
    		if (bufferSize > 0) {
    			return bufferSize;
    		}
    		
    		return PartialBufferedOutputStream.DEFAULT_BUFFER_SIZE;
    }
    
    /* (non-Javadoc)
	 * @see org.vfny.geoserver.servlets.AbstractService.ServiceStrategy#getDestination(javax.servlet.http.HttpServletResponse)
	 */
	public OutputStream getDestination(HttpServletResponse response) throws IOException 
	{
		out = new PartialBufferedOutputStream(response, bufferSize);
		return out;
	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.servlets.AbstractService.ServiceStrategy#flush()
	 */
	public void flush() throws IOException 
	{
		if (out != null)
		{
            out.forceFlush();
            out = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.servlets.AbstractService.ServiceStrategy#abort()
	 */
	public void abort() 
	{
		if (out != null)
		{
			try {
				if (out.abort())
					LOGGER.info("OutputStream was successfully aborted.");
				else
					LOGGER.warning("OutputStream could not be aborted in time. An error has occurred and could not be sent to the user.");
			} catch (IOException e) {
				LOGGER.warning("Error aborting OutputStream");
				e.printStackTrace();
			}
		}
	}
	
	public Object clone() throws CloneNotSupportedException {
		PartialBufferStrategy clone = new PartialBufferStrategy();
		clone.bufferSize = bufferSize;
		
		return clone;
	}
	
}
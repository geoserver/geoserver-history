/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <b>PartialBufferedOutputStream</b><br>
 * Oct 19, 2005<br>
 * 
 * <b>Purpose:</b><br>
 * I have modelled this class after the BufferedOutputStream. Several methods are
 * synchronized as a result. The interior of this class uses a ByteArrayOutputStream 
 * when it starts and then a BufferedOutputStream. This is essentially a decorator.
 * 
 * This class uses a temporary to-memory OutputStream for initial bufferring. This 
 * OutputStream is a ByteArrayOutputStream. Once a pre-defined BUFFER_SIZE has been 
 * reached, the output that is stored in the ByteArrayOutputStream is output to the 
 * real OutputStream to the response. Further data is then written to that response 
 * OutputStream.
 * 
 * For the first run of this, we will write to memory for the buffered part. Writing to 
 * disk is another option.
 * NOTE: If we switch to writing out to disk, we will have to clean up our temporary 
 * files with abort().
 * 
 * WARNING: IF you abuse the size of the buffer, you may leak memory if this OutputStream 
 * is not terminated. The ByteArrayOutputStream will hold onto its allocated memory even after 
 * you call reset() on it. So if you put in 60MB of buffer space, it will stay there until 
 * the object is no longer referenced and the garbage collector decides to pick it up. A
 * solution to this would be to use a FileOutputStream instead of the ByteArrayOutputStream.
 * 
 * @author Brent Owens (The Open Planning Project)
 * @version 
 */
public class PartialBufferedOutputStream extends OutputStream
{
	/** the number of bytes in a kilobyte */
	private final int KILOBYTE = 1024;
	
	/** Buffer size for the temporary output stream */
    private int BUFFER_SIZE = KILOBYTE;
    
    /** Whether we should start streaming to the user or not */
    private boolean sendToUser = false;

    /** Temporary output stream, the buffered one */
    private ByteArrayOutputStream out_buffer;
    
    /** Response output stream, the non-buffered one, this is passed in by the response */
    private OutputStream out_real;
    
    
	/**
	 * Constructor Defaults buffer size to 20KB
	 * @param response_out
	 */
	public PartialBufferedOutputStream(OutputStream response_out)
	{
		this(response_out, 20);	// default to 20KB
	}
	
	/**
	 * @param response_out the output stream to write to once the buffer is full
	 * @param kilobytes size, in kilobytes, of the buffer
	 */
	public PartialBufferedOutputStream(OutputStream response_out, int kilobytes)
	{
		if (kilobytes < 1) 
	        throw new IllegalArgumentException("Buffer size not greater than 0: " + kilobytes);
		
		BUFFER_SIZE = KILOBYTE * kilobytes;
		out_real = response_out;
		out_buffer = new ByteArrayOutputStream(BUFFER_SIZE);
	}

	/**
	 * <b>bufferCapacity</b><br>
	 * <br>
	 * <b>Description:</b><br>
	 * This will return the max capacity of the buffer in kilobytes.
	 * 
	 * @return the capacity of the buffer in kilobytes
	 */
	public int bufferCapacity()
	{
		return BUFFER_SIZE/KILOBYTE;
	}
	
	/**
	 * <b>bufferSize</b><br>
	 * <br>
	 * <b>Description:</b><br>
	 * This will return the size of the buffer, in bytes.
	 * 
	 * @return the size of the buffer in bytes
	 */
	public int bufferSize()
	{
		return out_buffer.size();
	}
	
	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 * 
	 * Remember that b is treated as a byte, the 8 low-order bits are read, 
	 * and the 24 remaining high-order bits of b are ignored.
	 */
	public synchronized void write(int b) throws IOException 
	{
		// if we have reached our limit, stream back to the user
		if (sendToUser || out_buffer.size() >= BUFFER_SIZE)
		{
			if (sendToUser)	// if we are already streaming data to the user
			{
				// continue sending data to the user's response stream
				out_real.write(b);
			}
			else // we aren't streaming to the user yet, so start
			{
				sendToUser = true;
				// copy data from out_buffer to out_real
				out_buffer.writeTo(out_real);
				out_buffer.reset();
				out_real.write(b);
			}
		}
		else	// we have not reached our buffer limit yet, so stream to the buffer
		{
			out_buffer.write(b);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#flush()
	 */
	public synchronized void flush() throws IOException 
	{
		if (out_buffer.size() > 0 && !sendToUser)
		{
			out_buffer.writeTo(out_real);
			out_buffer.reset();
		}
		
		out_real.flush();
	}
	
	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#close()
	 */
	public void close() throws IOException 
	{
		try {
			flush();
		} 
		catch (IOException ignored) {
		}
		
		out_buffer.close();
		out_real.close();
    }
	
	
}

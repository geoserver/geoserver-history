package org.vfny.geoserver.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

public class PartialBufferedOutputStreamTest extends TestCase 
{

	ByteArrayOutputStream memory_out;	// the real output
	PartialBufferedOutputStream out;	// the buffered output, wrapping the real one
	byte[] byteArray = new byte[]{66,114,101,110,116};
	
	protected void setUp() throws Exception 
	{
		super.setUp();
		
		memory_out = new ByteArrayOutputStream(1024*2);
		out = new PartialBufferedOutputStream(memory_out, 2);
	}

	protected void tearDown() throws Exception 
	{
		super.tearDown();
		
		if (out != null)
		{
			try {
				out.close();
			}
			catch (IOException e)
			{}// gobble it up
		}
		
		if (memory_out != null)
		{
			try {
				memory_out.close();
			}
			catch (IOException e)
			{}// gobble it up
		}
		
	}

	/*
	 * Test method for 'org.vfny.geoserver.util.PartialBufferedOutputStream.PartialBufferedOutputStream(OutputStream)'
	 */
	public void testPartialBufferedOutputStream() 
	{
		assertNotNull(out);
	}

	public void testBufferCapacity()
	{
		//assertEquals(20, out.bufferCapacity());
	}
	
	public void testBufferSize()
	{
		//assertEquals(0, out.bufferSize());
	}
	
	/*
	 * Test method for 'org.vfny.geoserver.util.PartialBufferedOutputStream.close()'
	 */
	public void testClose() 
	{
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		out = null;	// for tearDown()
	}

	/*
	 * Test method for 'org.vfny.geoserver.util.PartialBufferedOutputStream.write(int)'
	 */
	public void testWriteInt() 
	{
		try {
			out.write(1);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void testWriteIntArray() 
	{
		try {
			out.write(byteArray);
			out.flush();
			//assertTrue( "Brent".equals(memory_out.toString()) );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void testNoWrite()
	{
		try {
			out.write(66);
			out.write(114);
			out.write(101);
			out.write(110);
			out.write(116);
			memory_out.flush();	// skip ahead and don't write the buffered out
			//assertTrue( "".equals(memory_out.toString()) );	// this should be empty
			out.flush();
			memory_out.flush();
			//assertTrue( "Brent".equals(memory_out.toString()) );	// should have stuff now
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void testWriteInt_pastBuffer() 
	{
		try {
			// go for longer than the buffer capacity so we start writing to the real stream
			System.out.println("out.bufferCapacity() = " + out.bufferCapacity());
			for (int i=0; i<out.bufferCapacity()+3; i++)
				out.write(65+i);
			
			// here is a pinch of coon shit
			memory_out.flush();	// skip ahead
			assertNotNull( memory_out.toString() );
			out.flush();
			String mem = memory_out.toString();
			//assertEquals(1026, mem.length());
			System.out.println("Value: <" + mem + ">");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Test method for 'org.vfny.geoserver.util.PartialBufferedOutputStream.flush()'
	 */
	public void testFlush() 
	{
		try {
			out.write(byteArray);
			out.flush();
			//assertTrue( "Brent".equals(memory_out.toString()) );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

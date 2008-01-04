/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.filters;

import org.geoserver.test.GeoServerTestSupport; 
import javax.servlet.ServletInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.Reader;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletContext;

/**
 * Wrap a String up as a ServletInputStream so we can read it multiple times.
 * @author David Winslow <dwinslow@openplans.org>
 */
public class BufferedRequestStreamTest extends GeoServerTestSupport{
    BufferedRequestStream myBRS;
	String myTestString;

    public void setUp() throws Exception{
		super.setUp();
		myTestString = "Hello, this is a test";
		myBRS = new BufferedRequestStream(myTestString);
	}

    public void testReadLine() throws Exception{
		byte[] b = new byte[1024];
		int off = 0;
		int len = 1024;
		int amountRead = myBRS.readLine(b, off, len);
		String s = new String(b, 0, amountRead);
		assertEquals(s, myTestString);;
    }
}

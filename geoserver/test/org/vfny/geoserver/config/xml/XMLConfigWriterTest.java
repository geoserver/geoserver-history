/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2003, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.config.xml;

import java.io.File;

import junit.framework.TestCase;

import org.vfny.geoserver.config.ModelConfig;
/**
 * XMLConfigWriterTest purpose.
 * <p>
 * Description of XMLConfigWriterTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: XMLConfigWriterTest.java,v 1.1.2.2 2004/01/02 17:13:26 dmzwiers Exp $
 */
public class XMLConfigWriterTest extends TestCase {
	private static final String testPath1 = "C:/Java/workspace/Geoserver-ModelConfig/tests/test3/";
	private static final String testPath2 = "C:/Java/workspace/Geoserver-ModelConfig/tests/test2/";
	private static final String testPath3 = "C:/Java/workspace/Geoserver-ModelConfig/tests/test4/";
	private File root1 = null;
	private File root2 = null;
	private File root3 = null;
	/**
	 * Constructor for XMLConfigWriterTest.
	 * @param arg0
	 */
	public XMLConfigWriterTest(String arg0) {
		super(arg0);
		try{
			root1 = new File(testPath1);
			root2 = new File(testPath2);
			root3 = new File(testPath3);
		}catch(Exception e){}
	}

	public void testStoreBlank(){
		ModelConfig m = new ModelConfig();
		try{
			XMLConfigWriter cw = new XMLConfigWriter(m);
			cw.store(root1);
		}catch(ConfigException e){
			fail(e.toString());
		}
	}
	
	public void testRoundTrip(){
		try{
			XMLConfigReader cr = new XMLConfigReader(root2);
			XMLConfigWriter wr = new XMLConfigWriter(cr.getModel());
			wr.store(root3);
		}catch(ConfigException e){
			fail(e.toString());
		}
	}
}

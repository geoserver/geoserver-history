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
package org.vfny.geoserver.config;

import junit.framework.TestCase;
/**
 * WFSTest purpose.
 * <p>
 * Description of WFSTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WMSTest.java,v 1.2 2004/01/12 21:01:28 dmzwiers Exp $
 */
public class WMSTest extends TestCase {

	private WMSConfig a,b;
	/**
	 * Constructor for WFSTest.
	 * @param arg0
	 */
	public WMSTest(String arg0) {
		super(arg0);
		a = new WMSConfig();
	}

	/*
	 * Test for void NameSpaceConfig(NameSpaceConfig)
	 */
	/*public void testNameSpaceNameSpace() {
		//test requires equals.
		b = new WMSConfig(a);
		assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
	}*/

	/*
	 * Test for Object clone()
	 */
	/*public void testClone() {
		//test requires equals.
		b =(WMSConfig)a.clone();
		assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
	}*/

}

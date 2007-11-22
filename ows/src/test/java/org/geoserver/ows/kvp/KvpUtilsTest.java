/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows.kvp;

import java.util.Arrays;
import java.util.List;

import org.geoserver.ows.util.KvpUtils;

import junit.framework.TestCase;

public class KvpUtilsTest extends TestCase {
    public void testEmptyString() {
        assertEquals(0, KvpUtils.readFlat("").size());
    }
    
    public void testTrailingEmtpyStrings() {
        assertEquals(Arrays.asList(new String[] {"x", "", "x", "", ""}), KvpUtils.readFlat("x,,x,,"));
    }
    
    public void testEmtpyNestedString() {
    	List result = KvpUtils.readNested("");
    	assertEquals(1, result.size());
    	assertEquals(0, ((List) result.get(0)).size());
    }
    
    public void testStarNestedString() {
    	List result = KvpUtils.readNested("*");
    	assertEquals(1, result.size());
    	assertEquals(0, ((List) result.get(0)).size());
    }
}

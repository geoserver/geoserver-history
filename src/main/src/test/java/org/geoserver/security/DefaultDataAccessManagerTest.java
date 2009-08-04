package org.geoserver.security;

import junit.framework.TestCase;

public class DefaultDataAccessManagerTest extends TestCase {
    
    public void testParsePlain() {
        String[] result = DefaultDataAccessManager.parseElements("a.b.c");
        assertEquals(3, result.length);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);
        assertEquals("c", result[2]);
    }
    
    public void testParseSpaces() {
        String[] result = DefaultDataAccessManager.parseElements(" a  . b . c ");
        assertEquals(3, result.length);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);
        assertEquals("c", result[2]);
    }
    
    public void testParseEscapedDots() {
        String[] result = DefaultDataAccessManager.parseElements(" a\\.b . c ");
        assertEquals(2, result.length);
        assertEquals("a.b", result[0]);
        assertEquals("c", result[1]);
    }
}

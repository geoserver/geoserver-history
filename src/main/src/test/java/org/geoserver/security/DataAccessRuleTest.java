package org.geoserver.security;

import junit.framework.TestCase;

public class DataAccessRuleTest extends TestCase {

    public void testEqualRoot() {
        DataAccessRule rule1 = new DataAccessRule("*", "*", AccessMode.READ, null);
        DataAccessRule rule2 = new DataAccessRule("*", "*", AccessMode.READ, null);
        assertEquals(0, rule1.compareTo(rule2));
        assertEquals(rule1, rule2);
        assertEquals(rule1.hashCode(), rule2.hashCode());
    }
    
    public void testDifferentRoot() {
        DataAccessRule rule1 = new DataAccessRule("*", "*", AccessMode.READ, null);
        DataAccessRule rule2 = new DataAccessRule("*", "*", AccessMode.WRITE, null);
        assertEquals(-1, rule1.compareTo(rule2));
        assertFalse(rule1.equals(rule2));
    }
    
    public void testDifferenPath() {
        DataAccessRule rule1 = new DataAccessRule("topp", "layer1", AccessMode.READ, null);
        DataAccessRule rule2 = new DataAccessRule("topp", "layer2", AccessMode.READ, null);
        assertEquals(-1, rule1.compareTo(rule2));
        assertFalse(rule1.equals(rule2));
    }
}

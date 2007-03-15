/* Copyright (c) 2001 - 2007 TOPP - http://topp.openplans.org.
 * All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible in the
 * license.txt file of the documents directory off the root directory.
 */
import junit.framework.TestCase;
import org.vfny.geoserver.control.internal.PreferenceStoreImpl;
import org.vfny.geoserver.control.internal.PropertyPreferenceStoreStrategy;
import java.util.Properties;


public class IPreferenceControllerTest extends TestCase {
    private PreferenceStoreImpl populated;
    private PreferenceStoreImpl empty;
    private PreferenceStoreImpl defaults;

    protected void setUp() throws Exception {
        createPopulatedStore();
        createDefaultsStore();

        Properties props = new Properties();
        this.empty = new PreferenceStoreImpl(new PropertyPreferenceStoreStrategy(
                    props));
    }

    private void createDefaultsStore() {
        Properties props = new Properties();
        this.defaults = new PreferenceStoreImpl(new PropertyPreferenceStoreStrategy(
                    props));
        defaults.setDefault("string", "setString");
        defaults.setDefault("int", 1);
        defaults.setDefault("boolean", true);
        defaults.setDefault("float", 2.0);
        defaults.setDefault("char", 'c');
        defaults.setDefault("long", 10000);
        defaults.setDefault("double", 3.0);
    }

    private void createPopulatedStore() {
        Properties populatedProps = new Properties();
        this.populated = new PreferenceStoreImpl(new PropertyPreferenceStoreStrategy(
                    populatedProps));
        populatedProps.put("string", "setString");
        populatedProps.put("int", "1");
        populatedProps.put("boolean", "true");
        populatedProps.put("float", "2.0");
        populatedProps.put("char", "c");
        populatedProps.put("long", "10000");
        populatedProps.put("double", "3.0");
    }

    public void testGetString() {
        assertEquals("setString", populated.getString("string"));
        assertEquals("1", populated.getString("int"));
        assertEquals("", empty.getString("string"));
        assertEquals("setString", defaults.getString("string"));
    }

    public void testGetInt() {
        assertEquals(1, populated.getInt("int"));
        assertEquals(1, defaults.getInt("int"));

        try {
            populated.getInt("string");
            fail("Exception should have occurred");
        } catch (Exception e) {
        }

        assertEquals(0, empty.getInt("string"));
        empty.setDefault("string", (int) 1);
        assertEquals(1, empty.getInt("string"));
    }

    public void testGetFloat() {
        assertEquals(2.0, populated.getFloat("float"), 0.0000001);
        assertEquals(2.0, defaults.getFloat("float"), 0.0000001);

        try {
            populated.getFloat("string");
            fail("Exception should have occurred");
        } catch (Exception e) {
        }

        assertEquals(0.0, empty.getFloat("string"), 0.0000001);
        empty.setDefault("string", (float) 1);
        assertEquals(1.0, empty.getFloat("string"), 0.00000001);
    }

    public void testGetBoolean() {
        assertEquals(true, populated.getBoolean("boolean"));
        assertEquals(true, defaults.getBoolean("boolean"));

        try {
            populated.getBoolean("string");
            fail("Exception should have occurred");
        } catch (Exception e) {
        }

        assertEquals(false, empty.getBoolean("string"));
        empty.setDefault("string", true);
        assertEquals(true, empty.getBoolean("string"));
    }

    public void testGetChar() {
        assertEquals('c', populated.getChar("char"));
        assertEquals('c', defaults.getChar("char"));

        try {
            populated.getChar("string");
            fail("Exception should have occurred");
        } catch (Exception e) {
        }

        assertEquals(' ', empty.getChar("string"));
        empty.setDefault("string", 'v');
        assertEquals('v', empty.getChar("string"));
    }

    public void testGetDouble() {
        assertEquals(3.0, populated.getDouble("double"), 0.0000001);
        assertEquals(3.0, defaults.getDouble("double"), 0.0000001);

        try {
            populated.getDouble("string");
            fail("Exception should have occurred");
        } catch (Exception e) {
        }

        assertEquals(0.0, empty.getDouble("string"), 0.0000001);
        empty.setDefault("string", (double) 1);
        assertEquals(1.0, empty.getDouble("string"), 0.000001);
    }

    public void testGetLong() {
        assertEquals(10000, populated.getLong("long"));
        assertEquals(10000, defaults.getLong("long"));

        try {
            populated.getLong("string");
            fail("Exception should have occurred");
        } catch (Exception e) {
        }

        assertEquals(0, empty.getLong("string"));
        empty.setDefault("string", (long) 1);
        assertEquals(1, empty.getLong("string"));
    }

    public void testSetStringString() {
        empty.set("new", "value");
        assertEquals("value", empty.getString("new"));
    }

    public void testSetStringInt() {
        empty.set("new", (int) 2);
        assertEquals(2, empty.getInt("new"));
    }

    public void testSetStringFloat() {
        empty.set("new", (float) 2);
        assertEquals(2, empty.getFloat("new"), 0.00001);
    }

    public void testSetStringBoolean() {
        empty.set("new", true);
        assertEquals(true, empty.getBoolean("new"));
    }

    public void testSetStringChar() {
        empty.set("new", '2');
        assertEquals('2', empty.getChar("new"));
    }

    public void testSetStringDouble() {
        empty.set("new", (double) 2);
        assertEquals(2, empty.getDouble("new"), 0.00001);
    }

    public void testSetStringLong() {
        empty.set("new", (long) 2);
        assertEquals(2, empty.getLong("new"));
    }

    public void testUnset() throws Exception {
        populated.setDefault("string", "def");
        populated.unset("string");
        assertEquals("def", populated.getString("string"));
    }
}

package org.geoserver.ows.kvp;

import org.geoserver.ows.KvpParser;

/**
 * Parses integer kvp's of the form 'key=<integer>'.
 * <p>
 * 
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class IntegerKvpParser extends KvpParser {

    /**
     * Creates the parser specifying the name of the key to latch to.
     * 
     * @param key The key whose associated value to parse.
     */
    public IntegerKvpParser(String key) {
        super(key, Integer.class);
    }

    public Object parse(String value) throws Exception {
        return Integer.valueOf( value );
    }

}

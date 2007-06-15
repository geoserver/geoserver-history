package org.geoserver.ows.kvp;

import org.geoserver.ows.KvpParser;

import com.sun.xml.bind.DatatypeConverterImpl;

/**
 * Parses double kvp's of the form 'key=<boolean>'.
 * <p>
 * 
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class BooleanKvpParser extends KvpParser {

    /**
     * Creates the parser specifying the name of the key to latch to.
     * 
     * @param key The key whose associated value to parse.
     */
    public BooleanKvpParser(String key) {
        super( key, Boolean.class );
    }
    
    public Object parse(String value) throws Exception {
        return Boolean.valueOf( DatatypeConverterImpl.theInstance.parseBoolean(value.toLowerCase()) );
    }

}

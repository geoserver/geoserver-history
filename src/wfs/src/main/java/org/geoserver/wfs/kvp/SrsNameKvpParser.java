package org.geoserver.wfs.kvp;

import java.net.URI;

import org.geoserver.ows.FlatKvpParser;

/**
 * Kvp Parser which parses srsName strings like "epsg:4326" into a URI.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class SrsNameKvpParser extends FlatKvpParser {

    public SrsNameKvpParser() {
        super("srsName", URI.class);
        
    }
    
    protected Object parseToken(String token) throws Exception {
        return new URI(token);
    }

}

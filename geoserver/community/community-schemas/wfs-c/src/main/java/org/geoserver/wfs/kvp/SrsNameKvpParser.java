/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.kvp;

import org.geoserver.ows.FlatKvpParser;
import java.net.URI;


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

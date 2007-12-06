/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.test.ows;

import org.geoserver.ows.KvpParser;
import org.geoserver.ows.KvpRequestReader;
import org.geoserver.ows.util.CaseInsensitiveMap;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.test.GeoServerTestSupport;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Test class for testing instances of {@link KvpRequestReader}.
 * <p>
 * The {@link #parseKvp(Map)} method of this class sets up a kvp map and parses it
 * by processing instances of {@link KvpParser} in the application context.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public abstract class KvpRequestReaderTestSupport extends GeoServerTestSupport {
    /**
     * Parses a raw set of kvp's into a parsed set of kvps.
     *
     * @param kvp Map of String,String.
     */
    protected Map parseKvp(Map /*<String,String>*/ raw)
        throws Exception {
        //parse the raw values
        List parsers = GeoServerExtensions.extensions(KvpParser.class,
                applicationContext);
        Map kvp = new CaseInsensitiveMap(new HashMap());

        for (Iterator e = raw.entrySet().iterator(); e.hasNext();) {
            Map.Entry entry = (Map.Entry) e.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            Object parsed = null;

            for (Iterator p = parsers.iterator(); p.hasNext();) {
                KvpParser parser = (KvpParser) p.next();

                if (key.equalsIgnoreCase(parser.getKey())) {
                    parsed = parser.parse(val);

                    if (parsed != null) {
                        break;
                    }
                }
            }

            if (parsed == null) {
                parsed = val;
            }

            kvp.put(key, parsed);
        }

        return kvp;
    }
}

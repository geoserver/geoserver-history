/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.kvp;

import org.geoserver.ows.KvpParser;
import org.vfny.geoserver.wms.WmsException;
import java.awt.Color;


/**
 * Parses kvp of hte form &lt;key>=&lt;hex color value>.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class ColorKvpParser extends KvpParser {
    public ColorKvpParser(String key) {
        super(key, Color.class);
    }

    public Object parse(String value) throws Exception {
        try {
            return Color.decode(value);
        } catch (NumberFormatException nfe) {
            throw new WmsException("BGCOLOR " + value
                + " incorrectly specified (0xRRGGBB format expected)");
        }
    }
}

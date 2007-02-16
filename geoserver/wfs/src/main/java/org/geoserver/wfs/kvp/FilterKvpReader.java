/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.kvp;

import org.geoserver.ows.KvpParser;
import org.geoserver.ows.util.KvpUtils;
import org.geotools.filter.v1_0.OGCConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.opengis.filter.Filter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FilterKvpReader extends KvpParser {
    public FilterKvpReader() {
        super("filter", List.class);
    }

    public Object parse(String value) throws Exception {
        //create the parser
        Configuration configuration = new OGCConfiguration();
        Parser parser = new Parser(configuration);

        //seperate the individual filter strings
        List unparsed = KvpUtils.readFlat(value, KvpUtils.OUTER_DELIMETER);
        List filters = new ArrayList();

        Iterator i = unparsed.listIterator();

        while (i.hasNext()) {
            String string = (String) i.next();
            InputStream input = new ByteArrayInputStream(string.getBytes());

            try {
                Filter filter = (Filter) parser.parse(input);

                if (filter == null) {
                    throw new NullPointerException();
                }

                filters.add(filter);
            } catch (Exception e) {
                String msg = "Unable to parse filter: " + string;
                throw new RuntimeException(msg, e);
            }
        }

        return filters;
    }
}

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_0_0;

import org.geoserver.ows.XmlRequestReader;
import org.geotools.util.Version;
import org.geotools.xml.Parser;
import java.io.Reader;
import javax.xml.namespace.QName;


public class WfsXmlReader extends XmlRequestReader {
    /**
     * Xml Configuration
     */
    WFSConfiguration configuration;

    public WfsXmlReader(String element, WFSConfiguration configuration) {
        super(new QName(WFS.NAMESPACE, element), new Version("1.0.0"), "wfs");
        this.configuration = configuration;
    }

    public Object read(Object request, Reader reader) throws Exception {
        Parser parser = new Parser(configuration);

        return parser.parse(reader);
    }
}

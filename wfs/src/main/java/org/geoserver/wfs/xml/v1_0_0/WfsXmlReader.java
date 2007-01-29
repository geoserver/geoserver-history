/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_0_0;

import org.geoserver.ows.XmlRequestReader;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geotools.util.Version;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.vfny.geoserver.global.Data;
import java.io.InputStream;
import javax.xml.namespace.QName;


public class WfsXmlReader extends XmlRequestReader {
    Data catalog;
    FeatureTypeSchemaBuilder schemaBuilder;

    public WfsXmlReader(String element, Data catalog, FeatureTypeSchemaBuilder schemaBuilder) {
        super(new QName(WFS.NAMESPACE, element), new Version("1.0.0"));
        this.catalog = catalog;
        this.schemaBuilder = schemaBuilder;
    }

    public Object read(InputStream input) throws Exception {
        Configuration configuration = new WFSConfiguration(catalog, schemaBuilder);

        Parser parser = new Parser(configuration);

        return parser.parse(input);
    }
}

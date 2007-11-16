/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0;

import org.geoserver.ows.XmlRequestReader;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geotools.util.Version;
import org.geotools.xml.Parser;
import org.vfny.geoserver.global.Data;
import org.xml.sax.InputSource;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;


public class WfsXmlReader extends XmlRequestReader {
    /**
     * WFs configuration
     */
    WFS wfs;

    /**
     * Xml Configuration
     */
    WFSConfiguration configuration;

    public WfsXmlReader(String element, WFS wfs, WFSConfiguration configuration) {
        super(new QName(org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE, element), new Version("1.1.0"),
            "wfs");
        this.wfs = wfs;
        this.configuration = configuration;
    }

    public Object read(Object request, Reader reader, Map kvp) throws Exception {
        //check the strict flag to determine if we should validate or not
        Boolean strict = (Boolean) kvp.get("strict");
        if ( strict == null ) {
            strict = Boolean.FALSE;
        }
        
        //check for cite compliance, we always validate for cite
        if ( wfs.getCiteConformanceHacks() ) {
            strict = Boolean.TRUE;
        }
        
        //TODO: make this configurable?
        configuration.getProperties().add(Parser.Properties.PARSE_UNKNOWN_ELEMENTS);

        Parser parser = new Parser(configuration);
        parser.setValidating(strict.booleanValue());
       
        //set the input source with the correct encoding
        InputSource source = new InputSource(reader);
        source.setEncoding(wfs.getCharSet().name());

        Object parsed = parser.parse(source);

        //TODO: HACK, disabling validation for transaction
        //if (!"Transaction".equalsIgnoreCase(getElement().getLocalPart())) {
            if (!parser.getValidationErrors().isEmpty()) {
                WFSException exception = new WFSException("Invalid request", "InvalidParameterValue");

                for (Iterator e = parser.getValidationErrors().iterator(); e.hasNext();) {
                    Exception error = (Exception) e.next();
                    exception.getExceptionText().add(error.getLocalizedMessage());
                }

                throw exception;
            }
        //}

        return parsed;
    }
}

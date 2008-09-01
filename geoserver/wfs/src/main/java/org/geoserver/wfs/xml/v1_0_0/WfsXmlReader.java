/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_0_0;

import org.geoserver.ows.XmlRequestReader;
import org.geoserver.wfs.WFSException;
import org.geotools.util.Version;
import org.geotools.xml.Parser;
import org.vfny.geoserver.global.NameSpaceInfo;

import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * Xml reader for wfs 1.0.0 xml requests.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 * TODO: there is too much duplication with the 1.1.0 reader, factor it out.
 */
public class WfsXmlReader extends XmlRequestReader {
    /**
     * Xml Configuration
     */
    WFSConfiguration configuration;

    public WfsXmlReader(String element, WFSConfiguration configuration) {
        super(new QName(WFS.NAMESPACE, element), new Version("1.0.0"), "wfs");
        this.configuration = configuration;
    }

    public Object read(Object request, Reader reader, Map kvp) throws Exception {
        //check the strict flag to determine if we should validate or not
        Boolean strict = (Boolean) kvp.get("strict");
        if ( strict == null ) {
            strict = Boolean.FALSE;
        }
        
        //create the parser instance
        Parser parser = new Parser(configuration);
        
        //"inject" namespace mappings
        NameSpaceInfo[] namespaces = configuration.getCatalog().getNameSpaces();
        for ( int i = 0; i < namespaces.length; i++) {
            if ( namespaces[i].isDefault() ) 
                continue;
            
            parser.getNamespaces().declarePrefix( 
                namespaces[i].getPrefix(), namespaces[i].getURI());
        }
        //set validation based on strict or not
        parser.setValidating(strict.booleanValue());
        
        //parse
        Object parsed = parser.parse(reader); 
        
        //if strict was set, check for validation errors and throw an exception 
        if (strict.booleanValue() && !parser.getValidationErrors().isEmpty()) {
            WFSException exception = new WFSException("Invalid request", "InvalidParameterValue");

            for (Iterator e = parser.getValidationErrors().iterator(); e.hasNext();) {
                Exception error = (Exception) e.next();
                exception.getExceptionText().add(error.getLocalizedMessage());
            }

            throw exception;
        }
        
        return parsed;
    }
}

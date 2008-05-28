/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
    @author lreed@refractions.net
*/

package org.geoserver.wps.xml.v1_0_0;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.geoserver.ows.XmlRequestReader;
import org.geotools.util.Version;
import org.geotools.wps.WPSConfiguration;
import org.geotools.xml.Parser;
import org.xml.sax.InputSource;
import org.geoserver.wps.WPSException;
import org.geoserver.wps.WPS;

public class WpsXmlReader extends XmlRequestReader
{
    private WPS              wps;
    private WPSConfiguration configuration;

    public WpsXmlReader(String element, String version, WPSConfiguration configuration)
    {
        super(new QName(org.geotools.wps.WPS.NAMESPACE, element), new Version("1.0.0"), "wps");
        this.configuration = configuration;
    }

    public Object read(Object request, Reader reader, Map kvp) throws Exception
    {
        Parser parser = new Parser(configuration);
        parser.setValidating(true);

        // Namespace 'injection' ?

        InputSource source = new InputSource(reader);

        Object parsed;
        try
        {
            parsed = parser.parse(source);
        } catch(Exception e) {
            throw new WPSException("Could not parse XML request.", e);
        }

        if (!parser.getValidationErrors().isEmpty())
        {
            WPSException exception = new WPSException("Invalid request", "InvalidParameterValue");

            for(Exception error : (List<Exception>)parser.getValidationErrors())
            {
                exception.getExceptionText().add(error.getLocalizedMessage());
            }

            throw exception;
        }

        return parsed;
    }
}
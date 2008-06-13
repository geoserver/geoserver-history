/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 * @author lreed@refractions.net
 */

package org.geoserver.wps.transmute;

import java.io.InputStream;

import org.geoserver.wps.WPSException;
import org.geotools.xml.Configuration;
import org.geotools.gml2.GMLConfiguration;

import com.vividsolutions.jts.geom.Geometry;

public class PolygonGML2Transmuter implements ComplexTransmuter
{
    public String getSchema()
    {
        return "Polygon.xsd";
    }

    public Class<?> getType()
    {
    	return Geometry.class;
    }

    public Class<?> getXMLConfiguration()
    {
        return GMLConfiguration.class;
    }
    
    public String getMimeType()
    {
        return "text/xml; subtype=gml/2.1.2";
    }

    public String encode(Object obj)
    {
        return "XXX";
    }

    public Object decode(InputStream stream)
    {
        Object        decoded = null;
        Configuration config  = null;

        try
        {
            config = (Configuration)(this.getXMLConfiguration().getConstructor().newInstance());
        } catch(Exception e) {
            throw new WPSException("NoApplicableCode", "Failed to initialize XMLConfiguration");
        }

        org.geotools.xml.Parser parser = new org.geotools.xml.Parser(config);

        try
        {
            decoded = (Geometry)parser.parse(stream);
        } catch(Exception e) {
            throw new WPSException("NoApplicableCode", "Parsing error " + e);
        }

        return decoded;
    }
}
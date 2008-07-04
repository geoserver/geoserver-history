/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps.transmute;

import java.io.InputStream;

import org.geoserver.wps.WPSException;
import org.geotools.xml.Configuration;
import org.geotools.gml2.GMLConfiguration;

import com.vividsolutions.jts.geom.Geometry;

/**
 * ComplexTransmuter for JTS Geometry to/from GML2 Polygons
 *
 * @author Lucas Reed, Refractions Research Inc
 */
public class PolygonGML2Transmuter implements ComplexTransmuter
{
    /**
     * @see ComplexTransmuter#getSchema(String)
     */
    public String getSchema(String urlBase)
    {
        String a = urlBase + "ows?service=WPS&request=GetSchema&Identifier=Polygon.xsd";

        return a;
    }

    /**
     * @see Transmuter#getType()
     */
    public Class<?> getType()
    {
        return Geometry.class;
    }

    /**
     * @see ComplexTransmuter#getXMLConfiguration()
     */
    public Class<?> getXMLConfiguration()
    {
        return GMLConfiguration.class;
    }

    /**
     * @see ComplexTransmuter#getMimeType()
     */
    public String getMimeType()
    {
        return "text/xml; subtype=gml/2.1.2";
    }

    /**
     * @see ComplexTransmuter#decode(InputStream)
     */
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

    /**
     * @see ComplexTransmuter#encode(Object)
     */
    public Object encode(Object input)
    {
        throw new WPSException("NoApplicableCode", "Unimplemented encoder for PolygonGML2Transmuter");
    }
}
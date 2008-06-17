/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 * @author lreed@refractions.net
 */

package org.geoserver.wps.transmute;

import org.geoserver.wps.WPSException;

public class DoubleTransmuter implements LiteralTransmuter
{
    public Double decode(String encoded)
    {
        Double decoded;

        try
        {
            decoded = Double.valueOf(encoded);
        } catch(NumberFormatException e) {
            throw new WPSException("InvalidParameterType", "Could not convert paramter to object.");
        }

        return decoded;
    }

    public Class<?> getType()
    {
        return Double.class;
    }

    public String encode(Object value)
    {
        return ((Double)value).toString();
    }

    public String getEncodedType()
    {
        return "xs:double";
    }
}
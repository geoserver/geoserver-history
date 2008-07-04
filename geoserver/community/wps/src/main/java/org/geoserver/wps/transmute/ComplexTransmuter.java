/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps.transmute;

import java.io.InputStream;

/**
 * ComplexTransmuter interface
 *
 * @author Lucas Reed, Refractions Research Inc
 */
public interface ComplexTransmuter extends Transmuter
{
    /**
     * Returns absolute URL to the schema which defines the in
     */
    String getSchema(String urlBase);

    /**
     * Returns the class of the XMLConfiguration used to parse/encode
     * @return
     */
    Class<?> getXMLConfiguration();

    /**
     * Returns mime type of encoded data
     * @return
     */
    String getMimeType();

    /**
     * Used to decode external XML documents for use as process inputs
     * @param stream
     * @return
     */
    Object decode(InputStream stream);

    /**
     * Used to encode document for server storage
     * @param input
     * @return
     */
    Object encode(Object input);
}
/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 * @author lreed@refractions.net
 */

package org.geoserver.wps.transmute;

import java.io.InputStream;

public interface ComplexTransmuter extends Transmuter
{
    String   getSchema();

    Class<?> getXMLConfiguration();

    String   getMimeType();

    Object   decode(InputStream stream);

    Object   encode(Object obj);
}
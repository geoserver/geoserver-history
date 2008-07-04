/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import org.geoserver.platform.ServiceException;

/**
 * WPS Execption class
 *
 * @author Lucas Reed, Refractions Research Inc
 */
public class WPSException extends ServiceException
{
    public WPSException(String message)
    {
        super(message);
    }

    public WPSException(String code, String message)
    {
        super(message, code);
    }

    public WPSException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

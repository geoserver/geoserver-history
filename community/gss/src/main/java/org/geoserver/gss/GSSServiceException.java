/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.gss;

import org.geoserver.platform.ServiceException;


public class GSSServiceException extends ServiceException {
    private static final long serialVersionUID = 7001211051719019724L;

    public GSSServiceException(String msg, String code, String locator) {
        super(msg, code, locator);
    }

    public GSSServiceException(String msg, String code) {
        super(msg, code);
    }

    public GSSServiceException(String msg) {
        super(msg);
    }
    
    public GSSServiceException(String msg, Throwable cause) {
        super(msg);
        initCause(cause);
    }

}

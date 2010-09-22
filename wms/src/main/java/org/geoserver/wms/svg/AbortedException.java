/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.svg;


/**
 * @author Gabriel Rold?n
 * @version $Id$
 */
public class AbortedException extends Exception {
    public AbortedException(String msg) {
        super(msg);
    }
}

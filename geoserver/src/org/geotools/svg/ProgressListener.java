/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geotools.svg;

/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: ProgressListener.java,v 1.1.2.1 2003/11/16 11:27:37 groldan Exp $
 */
public interface ProgressListener {
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param max DOCUMENT ME!
     */
    public void progress(int value, int max);
}

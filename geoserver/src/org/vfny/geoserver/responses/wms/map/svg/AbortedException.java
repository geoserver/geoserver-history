package org.vfny.geoserver.responses.wms.map.svg;

/**
 * @author Gabriel Roldán
 * @version $Id: AbortedException.java,v 1.1 2004/03/14 16:15:22 groldan Exp $
 */

public class AbortedException extends Exception {
    public AbortedException(String msg) {
        super(msg);
    }
}

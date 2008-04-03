/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.util.requests.readers;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.servlets.AbstractService;
import java.io.Reader;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;


/**
 * This utility reads in XML requests and returns them as appropriate request
 * objects.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author Gabriel Rold?n
 * @version $Id$
 */
public abstract class XmlRequestReader {
    /** Class logger */
    protected static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.requests.readers");

    /** The service handling the request **/
    private Service serviceConfig;
    

    /**
    * DOCUMENT ME!
    *
    * @param reader DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ServiceException DOCUMENT ME!
    */
    public abstract Request read(Reader reader, HttpServletRequest req)
        throws ServiceException;

   
    /**
     * This will create a new XmlRequestReader
     * @param service The config of the service handling the request
     */
    public XmlRequestReader(Service service) {
        this.serviceConfig = service;;
    }
    
    
    public Service getService() {
        return serviceConfig;
    }
}

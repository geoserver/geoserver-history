/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.kvp;

import net.opengis.wfs.WfsFactory;
import org.eclipse.emf.ecore.EObject;
import org.geoserver.ows.KvpRequestReader;
import org.geoserver.ows.kvp.EMFKvpRequestReader;
import org.geoserver.ows.util.OwsUtils;
import org.geotools.xml.EMFUtils;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;


/**
 * Web Feature Service Key Value Pair Request reader.
 * <p>
 * This request reader makes use of the Eclipse Modelling Framework
 * reflection api.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WFSKvpRequestReader extends EMFKvpRequestReader {

    /**
     * Creates the Wfs Kvp Request reader.
     *
     * @param requestBean The request class, which must be an emf class.
     */
    public WFSKvpRequestReader(Class requestBean) {
        super(requestBean, WfsFactory.eINSTANCE);
    }
    
    protected WfsFactory getWfsFactory() {
        return (WfsFactory) factory;
    }
}

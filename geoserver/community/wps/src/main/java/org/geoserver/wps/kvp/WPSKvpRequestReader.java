/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
    @author lreed@refractions.net
*/

package org.geoserver.wps.kvp;

import net.opengis.wps.WpsFactory;
import org.geoserver.ows.kvp.EMFKvpRequestReader;

public class WPSKvpRequestReader extends EMFKvpRequestReader
{
    public WPSKvpRequestReader(Class requestBean)
    {
        super(requestBean, WpsFactory.eINSTANCE);
    }

    protected WpsFactory getWpsFactory()
    {
        return (WpsFactory)factory;
    }
}

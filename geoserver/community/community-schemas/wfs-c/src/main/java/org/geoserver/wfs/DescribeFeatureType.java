/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import net.opengis.wfs.DescribeFeatureTypeType;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;


/**
 * Web Feature Service DescribeFeatureType operation.
 * <p>
 * This operation returns an array of  {@link org.geoserver.data.feature.FeatureTypeInfo} metadata
 * objects corresponding to the feature type names specified in the request.
 * </p>
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 * @version $Id$
 */
public class DescribeFeatureType {
    /**
    * Catalog reference
    */
    private Data catalog;

    /**
     * WFS service
     */
    private WFS wfs;

    /**
         * Creates a new wfs DescribeFeatureType operation.
         *
         * @param wfs The wfs configuration
         * @param catalog The geoserver catalog.
         */
    public DescribeFeatureType(WFS wfs, Data catalog) {
        this.catalog = catalog;
        this.wfs = wfs;
    }

    public WFS getWFS() {
        return wfs;
    }

    public void setWFS(WFS wfs) {
        this.wfs = wfs;
    }

    public Data getCatalog() {
        return catalog;
    }

    public void setCatalog(Data catalog) {
        this.catalog = catalog;
    }

    public FeatureTypeInfo[] run(DescribeFeatureTypeType request)
        throws WFSException {
        List names = new ArrayList(request.getTypeName());

        //list of catalog handles
        Collection infos = catalog.getFeatureTypeInfos().values();
        ArrayList requested = new ArrayList();

        if (!names.isEmpty()) {
O: 
            for (Iterator t = names.iterator(); t.hasNext();) {
                QName name = (QName) t.next();

                for (Iterator h = infos.iterator(); h.hasNext();) {
                    FeatureTypeInfo meta = (FeatureTypeInfo) h.next();
                    String namespace = meta.getNameSpace().getURI();
                    String local = meta.getTypeName();

                    if (namespace.equals(name.getNamespaceURI())
                            && local.equals(name.getLocalPart())) {
                        //found, continue on and keep this handle in list
                        requested.add(meta);

                        continue O;
                    }
                }

                //not found
                String msg = "Could not find type: " + name;
                throw new WFSException(msg);
            }
        } else {
            //if there are no specific requested types then get all.
            requested.addAll(infos);
        }

        return (FeatureTypeInfo[]) requested.toArray(new FeatureTypeInfo[requested.size()]);
    }
}

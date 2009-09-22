/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import net.opengis.wfs.DescribeFeatureTypeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.config.GeoServer;


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
    private Catalog catalog;

    /**
     * WFS service
     */
    private WFSInfo wfs;

    /**
         * Creates a new wfs DescribeFeatureType operation.
         *
         * @param wfs The wfs configuration
         * @param catalog The geoserver catalog.
         */
    public DescribeFeatureType(WFSInfo wfs, Catalog catalog) {
        this.catalog = catalog;
        this.wfs = wfs;
    }

    public WFSInfo getWFS() {
        return wfs;
    }

    public void setWFS(WFSInfo wfs) {
        this.wfs = wfs;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public FeatureTypeInfo[] run(DescribeFeatureTypeType request)
        throws WFSException {
        List names = new ArrayList(request.getTypeName());

        final boolean citeConformance = getWFS().isCiteCompliant();
        if (!citeConformance) {
            // HACK: as per GEOS-1816, if strict cite compliance is not set, and
            // the user specified a typeName with no namespace prefix, we want
            // it to be interpreted as being in the GeoServer's "default
            // namespace". Yet, the xml parser did its job and since TypeName is
            // of QName type, not having a ns prefix means it got parsed as a
            // QName in the default namespace. That is, in the wfs namespace.
            List hackedNames = new ArrayList(names.size());
            final NamespaceInfo defaultNameSpace = catalog.getDefaultNamespace();
            if (defaultNameSpace == null) {
                throw new IllegalStateException("No default namespace configured in GeoServer");
            }
            final String defaultNsUri = defaultNameSpace.getURI();
            for (Iterator it = names.iterator(); it.hasNext();) {
                QName name = (QName) it.next();
                String nsUri = name.getNamespaceURI();
                if (org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE.equals(nsUri)) {
                    // for this one we need to assign the default geoserver
                    // namespace
                    name = new QName(defaultNsUri, name.getLocalPart());
                }
                hackedNames.add(name);
            }
            names = hackedNames;
        }

        //list of catalog handles
        Collection infos = catalog.getFeatureTypes();
        ArrayList requested = new ArrayList();

        if (!names.isEmpty()) {
O: 
            for (Iterator t = names.iterator(); t.hasNext();) {
                QName name = (QName) t.next();

                for (Iterator h = infos.iterator(); h.hasNext();) {
                    FeatureTypeInfo meta = (FeatureTypeInfo) h.next();
                    if(!meta.enabled())
                        continue;
                    
                    String namespace = meta.getNamespace().getURI();
                    String local = meta.getName();

                    if (namespace.equals(name.getNamespaceURI())
                            && local.equals(name.getLocalPart())) {
                        //found, continue on and keep this handle in list
                        requested.add(meta);

                        continue O;
                    }
                }

                //not found
                String msg = "Could not find type: " + name;
                if (citeConformance) {
                    msg += ". \nStrict WFS protocol conformance is being applied.\n"
                            + "Make sure the type name is correctly qualified";
                }
                throw new WFSException(msg);
            }
        } else {
            //if there are no specific requested types then get all the ones that
            //are enabled
            for (Iterator it = infos.iterator(); it.hasNext();) {
                FeatureTypeInfo ftInfo = (FeatureTypeInfo) it.next();
                if(ftInfo.enabled())
                    requested.add(ftInfo);
            }
        }

        return (FeatureTypeInfo[]) requested.toArray(new FeatureTypeInfo[requested.size()]);
    }
}

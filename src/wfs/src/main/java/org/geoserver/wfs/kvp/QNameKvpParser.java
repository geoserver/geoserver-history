/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.kvp;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.ows.FlatKvpParser;
import org.geoserver.wfs.WFSException;

import javax.xml.namespace.QName;


/**
 * Abstract kvp parser for parsing qualified names of the form "([prefix:]local)+".
 * <p>
 * This parser will parse strings of the above format into a list of
 * {@link javax.xml.namespace.QName}
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class QNameKvpParser extends FlatKvpParser {
    /**
     * catalog for namespace lookups.
     */
    Catalog catalog;

    public QNameKvpParser(String key, Catalog catalog) {
        super(key, QName.class);
        this.catalog = catalog;
    }

    /**
     * Parses the token representing a type name, ( <prefix>:<local>, or <local> )
     * into a {@link QName }.
     * <p>
     * If the latter form is supplied the QName is given the default namespace
     * as specified in the catalog.
     * </p>
     */
    protected Object parseToken(String token) throws Exception {
        int i = token.indexOf(':');

        if (i != -1) {
            String prefix = token.substring(0, i);
            String local = token.substring(i + 1);
            
            String uri = null;
            if(prefix != null && !"".equals(prefix)) {
                final NamespaceInfo namespace = catalog.getNamespaceByPrefix(prefix);
                if(namespace == null)
                    throw new WFSException("Unknown namespace [" + prefix + "]");
                uri = namespace.getURI();
            }

            return new QName(uri, local, prefix);
        } else {
            /*
            String uri = catalog.getDefaultNamespace().getURI();
            String prefix = catalog.getDefaultNamespace().getPrefix();
            String local = token;
            
            return new QName(uri, local, prefix);
            */
            return new QName(token);
        }
    }
    
  
}

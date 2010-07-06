/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.kvp;

import org.geoserver.catalog.Catalog;

/**
 * Parses a {@code typeName} GetFeature parameter the form "([prefix:]local)+".
 * <p>
 * This parser will parse strings of the above format into a list of
 * {@link javax.xml.namespace.QName}
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * @author groldan
 */
public class TypeNameKvpParser extends QNameKvpParser {

    public TypeNameKvpParser(String key, Catalog catalog) {
        super(key, catalog, false);
    }
    
}

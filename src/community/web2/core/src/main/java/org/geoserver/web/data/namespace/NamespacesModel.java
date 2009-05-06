/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.namespace;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.web.GeoServerApplication;

/**
 * Simple detachable model listing all the available namespaces
 * 
 * @author Gabriel Roldan
 */
@SuppressWarnings("serial")
public class NamespacesModel extends LoadableDetachableModel {

    @Override
    protected Object load() {
        Catalog catalog = GeoServerApplication.get().getCatalog();
        List<NamespaceInfo> namespaces = catalog.getNamespaces();
        return namespaces;
    }
}
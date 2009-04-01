/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.workspace;

import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.web.GeoServerApplication;

@SuppressWarnings("serial")
public class NamespaceDetachableModel extends LoadableDetachableModel {

    String id;
    
    public NamespaceDetachableModel( NamespaceInfo ns ) {
        this.id = ns.getId();
    }
    
    @Override
    protected Object load() {
        return GeoServerApplication.get().getCatalog().getNamespace( id );
    }

}

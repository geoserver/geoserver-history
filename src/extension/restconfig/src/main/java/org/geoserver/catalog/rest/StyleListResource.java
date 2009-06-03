/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.catalog.rest;

import java.util.Collection;
import java.util.HashSet;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.StyleInfo;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

import com.thoughtworks.xstream.XStream;

public class StyleListResource extends AbstractCatalogListResource {

    protected StyleListResource(Context context, Request request,
            Response response, Catalog catalog) {
        super(context, request, response, StyleInfo.class, catalog);
        
    }
    
    @Override
    protected void aliasCollection(String alias, XStream xstream) {
        //JD: this is a bit of hack, we check when we alias as to 
        // property alias the correct collection type, since in teh case
        // of listing all, a List is returned, but when referencing through
        // style, a Set is returned
        String layer = getAttribute("layer");
        if ( layer == null ) {
            super.aliasCollection(alias, xstream);
        }
        else {
            xstream.alias( alias, Collection.class, HashSet.class );
        }
    }
    
    @Override
    protected Collection handleListGet() throws Exception {
        String layer = getAttribute("layer");
        if ( layer != null ) {
            LOGGER.fine( "GET styles for layer " + layer );
            return catalog.getLayerByName( layer ).getStyles();
        }
        
        LOGGER.fine( "GET styles" );
        return catalog.getStyles();
    }

}

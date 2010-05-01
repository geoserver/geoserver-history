/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.catalog.rest;

import java.util.ArrayList;
import java.util.Collection;

import org.geoserver.catalog.Catalog;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.ows.util.OwsUtils;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public abstract class AbstractCatalogListResource extends CatalogResourceBase {

    protected AbstractCatalogListResource(Context context, Request request,
            Response response, Class clazz, Catalog catalog) {
        super(context, request, response, clazz, catalog);
    }

    @Override
    protected final Object handleObjectGet() throws Exception {
        return XStreamPersister.unwrapProxies( handleListGet() );
    }
    
    protected abstract Collection handleListGet() throws Exception;
    
    @Override
    protected void configureXStream(XStream xstream) {
        XStreamPersister xp = xpf.createXMLPersister();
        final String name = xp.getClassAliasingMapper().serializedClass( clazz );
        
        xstream.alias( name, clazz );
        aliasCollection( name + "s", xstream );
        
        xstream.registerConverter( 
            new CollectionConverter(xstream.getMapper()) {
                @Override
                protected void writeItem(Object item,
                        MarshallingContext context,
                        HierarchicalStreamWriter writer) {
                    
                    writer.startNode( name );
                    context.convertAnother( item );
                    writer.endNode();
                }
            }
        );
        xstream.registerConverter( 
            new Converter() {

                public boolean canConvert(Class type) {
                    return clazz.isAssignableFrom( type );
                }
                
                public void marshal(Object source,
                        HierarchicalStreamWriter writer,
                        MarshallingContext context) {
                    
                    String ref = null;
                    if ( OwsUtils.getter( clazz, "name", String.class ) != null ) {
                        ref = (String) OwsUtils.get( source, "name");
                    }
                    else if ( OwsUtils.getter( clazz, "id", String.class ) != null ) {
                        ref = (String) OwsUtils.get( source, "id");
                    }
                    else {
                        throw new RuntimeException( "Could not determine identifier for: " + clazz.getName());
                    }
                    writer.startNode( "name" );
                    writer.setValue(ref);
                    writer.endNode();
                    
                    encodeLink(ref, writer);
                }

                public Object unmarshal(HierarchicalStreamReader reader,
                        UnmarshallingContext context) {
                    return null;
                }

            }
        );
    }
    
    /**
     * Template method to alias the type of the collection.
     * <p>
     * The default works with list, subclasses may override for instance
     * to work with a Set.
     * </p>
     */
    protected void aliasCollection( String alias, XStream xstream ) {
        xstream.alias( alias, Collection.class, ArrayList.class);
    }
}

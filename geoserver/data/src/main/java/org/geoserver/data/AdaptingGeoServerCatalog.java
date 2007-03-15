/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.data;

import org.geotools.catalog.ResolveAdapterFactory;
import org.geotools.catalog.adaptable.AdaptingCatalog;
import org.geotools.catalog.adaptable.ResolveAdapterFactoryFinder;
import org.xml.sax.helpers.NamespaceSupport;
import java.io.IOException;
import java.util.List;


/**
 * GeoServer catalog which supports resolve adapting.
 * <p>
 * In order to perform resolve adapting for a particular class, a
 * {@link ResolveAdapterFactory} must be registered in a spring context.
 * Consider the following adapter factory:
 * <code>
 *         <pre>
 *         class MyResolveAdapterFactory {
 *
 *    boolean canAdapt( Resolve resolve, Class adaptee ) {
 *      return Foo.class.isAssignableFrom( adaptee );
 *    }
 *
 *    Object adapt( Resolve resolve, Class adaptee, ProgressLister monitor) {
 *      Foo foo = (Foo) resolve.resolve( adatpee, resolve ) ;
 *      return new FooAdapter( foo );
 *    }
 *  }
 *         </pre>
 * </code>
 * It would be registered as follows:
 * <code>
 *         <pre>
 *         &lt;bean id="fooAdapterFactory" class="org.xyz.FooAdapter"/>
 *         </pre>
 * </code>
 *
 * </p>
 *
 * @see {@link org.geotools.catalog.ResolveAdapterFactory}
 * @see {@link org.geotools.catalog.adaptable.AdaptingCatalog}.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class AdaptingGeoServerCatalog extends AdaptingCatalog implements GeoServerCatalog {
    public AdaptingGeoServerCatalog(ResolveAdapterFactoryFinder adapterFinder) {
        super(new DefaultGeoServerCatalog(), adapterFinder);
    }

    public AdaptingGeoServerCatalog(GeoServerCatalog catalog,
        ResolveAdapterFactoryFinder adapterFinder) {
        super(catalog, adapterFinder);
    }

    public NamespaceSupport getNamespaceSupport() {
        return ((DefaultGeoServerCatalog) resolve).getNamespaceSupport();
    }

    public List services(Class resolvee) throws IOException {
        return DefaultGeoServerCatalog.services(this, resolvee);
    }

    public List resources(Class resolvee) throws IOException {
        return DefaultGeoServerCatalog.resources(this, resolvee);
    }
}

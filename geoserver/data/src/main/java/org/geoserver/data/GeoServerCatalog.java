/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.data;

import org.geotools.catalog.Catalog;
import org.geotools.catalog.GeoResource;
import org.geotools.catalog.Service;
import org.xml.sax.helpers.NamespaceSupport;
import java.io.IOException;
import java.util.List;


/**
 * GeoServer catalog.
 * <p>
 * The GeoServer catalog extends the geotools catalog providing additional
 * convenience methods specific to geoserver.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public interface GeoServerCatalog extends Catalog {
    /**
     * The namespace / prefix mappings for the application.
     *
     * @return NamespaceSupport containing declared namespace uris and prefixed.
     */
    NamespaceSupport getNamespaceSupport();

    /**
     * Returns a list of service handles from the catalog which can resolve to
     * the supplied the class.
     *
     * @param resolvee The class to test a resolve to.
     *
     * @return List of {@link Service}, possbily empty, never null.
     */
    List services(Class resolvee) throws IOException;

    /**
     * Returns a list of resource handles from the catalog which can resolve to
     * the supplied the class.
     *
     * @param resolvee The class to test a resolve to.
     *
     * @return List of {@link GeoResource}, possbily empty, never null.
     */
    List resources(Class resolvee) throws IOException;

    /**
     * Loads the feature type metata from the catalog correspoding to a
     * feature type name.
     *
     * @param name A feature type name.
     * @param monitor Monitor for blocking I/O calls, may be null.
     *
     * @return A list of FeatureTypeInfo objects which match the given name,
     * or empty if no such match.
     *
     * @throws IOException Any I/O errors.
     *
     */

    //	List/*FeatureTypeInfo*/ featureTypes( String name, ProgressListener monitor) 
    //		throws IOException;
}

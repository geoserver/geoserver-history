/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog;

/**
 * Visitor for catalog objects.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public interface CatalogVisitor {
    
    /**
     * Visits the catalog
     */
    void visit( Catalog catalog );
    
    /**
     * Visits a workspace.
     */
    void visit( WorkspaceInfo workspace );
    
    /**
     * Visits a namespace.
     */
    void visit( NamespaceInfo workspace );
    
    /**
     * Visits a workspace.
     */
    //void visit( StoreInfo store );
    
    /**
     * Visits a data store.
     */
    void visit( DataStoreInfo dataStore ); 

    /**
     * Visits a coverage store.
     */
    void visit( CoverageStoreInfo coverageStore );
    
    /**
     * Visits a resource.
     */
    //void visit( ResourceInfo resource );
    
    /**
     * Visits a feature type.
     */
    void visit( FeatureTypeInfo featureType );
    
    /**
     * Visits a coverage.
     */
    void visit( CoverageInfo coverage );
    
    /**
     * Visits a layer.
     */
    void visit( LayerInfo layer );
    
    /**
     * Visits a style.
     */
    void visit( StyleInfo style );
    
    /**
     * Visits a layer group..
     */
    void visit( LayerGroupInfo layerGroup );
}

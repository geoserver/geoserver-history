/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.event.CatalogAddEvent;
import org.geoserver.catalog.event.CatalogListener;
import org.geoserver.catalog.event.CatalogModifyEvent;
import org.geoserver.catalog.event.CatalogPostModifyEvent;
import org.geoserver.catalog.event.CatalogRemoveEvent;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.styling.SLDTransformer;
import org.geotools.util.logging.Logging;

public class GeoServerPersister implements CatalogListener, ConfigurationListener {

    /**
     * logging instance
     */
    static Logger LOGGER = Logging.getLogger( "org.geoserver.config");
     
    GeoServerResourceLoader rl;
    XStreamPersister xp;
    
    public GeoServerPersister(GeoServerResourceLoader rl, XStreamPersister xp) {
        this.rl = rl;
        this.xp = xp;
    }
    
    public void handleAddEvent(CatalogAddEvent event) {
        Object source = event.getSource();
        try {
            if ( source instanceof WorkspaceInfo ) {
                addWorkspace( (WorkspaceInfo) source );
            }
            else if ( source instanceof NamespaceInfo ) {
                addNamespace( (NamespaceInfo) source );
            }
            else if ( source instanceof DataStoreInfo ) {
                addDataStore( (DataStoreInfo) source );
            }
            else if ( source instanceof FeatureTypeInfo ) {
                addFeatureType( (FeatureTypeInfo) source );
            }
            else if ( source instanceof CoverageStoreInfo ) {
                addCoverageStore( (CoverageStoreInfo) source );
            }
            else if ( source instanceof CoverageInfo ) {
                addCoverage( (CoverageInfo) source );
            }
            else if ( source instanceof LayerInfo ) {
                addLayer( (LayerInfo) source );
            }
            else if ( source instanceof StyleInfo ) {
                addStyle( (StyleInfo) source );
            }
            else if ( source instanceof LayerGroupInfo ) {
                addLayerGroup( (LayerGroupInfo) source );
            }
        }
        catch( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public void handleModifyEvent(CatalogModifyEvent event) {
    }
    
    public void handlePostModifyEvent(CatalogPostModifyEvent event) {
        Object source = event.getSource();
        try {
            if ( source instanceof DataStoreInfo ) {
                modifyDataStore( (DataStoreInfo) source );
            }
            else if ( source instanceof NamespaceInfo ) {
                modifyNamespace( (NamespaceInfo) source );
            }
            else if ( source instanceof FeatureTypeInfo ) {
                modifyFeatureType( (FeatureTypeInfo) source );
            }
            else if ( source instanceof CoverageStoreInfo ) {
                modifyCoverageStore( (CoverageStoreInfo) source );
            }
            else if ( source instanceof CoverageInfo ) {
                modifyCoverage( (CoverageInfo) source );
            }
            else if ( source instanceof LayerInfo ) {
                modifyLayer( (LayerInfo) source );
            }
            else if ( source instanceof StyleInfo ) {
                modifyStyle( (StyleInfo) source );
            }
            else if ( source instanceof LayerGroupInfo ) {
                modifyLayerGroup( (LayerGroupInfo) source );
            }
        }
        catch( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public void handleRemoveEvent(CatalogRemoveEvent event) {
        Object source = event.getSource();
        try {
            if ( source instanceof WorkspaceInfo ) {
                removeWorkspace( (WorkspaceInfo) source );
            }
            else if ( source instanceof NamespaceInfo ) {
                removeNamespace( (NamespaceInfo) source );
            }
            else if ( source instanceof DataStoreInfo ) {
                removeDataStore( (DataStoreInfo) source );
            }
            else if ( source instanceof FeatureTypeInfo ) {
                removeFeatureType( (FeatureTypeInfo) source );
            }
            else if ( source instanceof CoverageStoreInfo ) {
                removeCoverageStore( (CoverageStoreInfo) source );
            }
            else if ( source instanceof CoverageInfo ) {
                removeCoverage( (CoverageInfo) source );
            }
            else if ( source instanceof LayerInfo ) {
                removeLayer( (LayerInfo) source );
            }
            else if ( source instanceof StyleInfo ) {
                removeStyle( (StyleInfo) source );
            }
            else if ( source instanceof LayerGroupInfo ) {
                removeLayerGroup( (LayerGroupInfo) source );
            }
        }
        catch( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public void handleGlobalChange(GeoServerInfo global, List<String> propertyNames,
            List<Object> oldValues, List<Object> newValues) {
    }
    
    public void handlePostGlobalChange(GeoServerInfo global) {
        try {
            persist( global, new File( rl.getBaseDirectory(), "global.xml") );
        } 
        catch (IOException e) {
            throw new RuntimeException( e );
        }
    }
    
    public void handleLoggingChange(LoggingInfo logging, List<String> propertyNames,
            List<Object> oldValues, List<Object> newValues) {
    }
    
    public void handlePostLoggingChange(LoggingInfo logging) {
        try {
            persist( logging, new File( rl.getBaseDirectory(), "logging.xml") );
        } 
        catch (IOException e) {
            throw new RuntimeException( e );
        }
    }
    
    public void handleServiceAdded(ServiceInfo service) {
    }
    
    public void handleServiceChange(ServiceInfo service, List<String> propertyNames,
            List<Object> oldValues, List<Object> newValues) {
    }
    
    public void handlePostServiceChange(ServiceInfo service) {
    }
    
    public void reloaded() {
    }
    
    //workspaces
    void addWorkspace( WorkspaceInfo ws ) throws IOException {
        LOGGER.fine( "Persisting workspace " + ws.getName() );
        File dir = dir( ws, true );
        dir.mkdirs();
    }
    
    void removeWorkspace( WorkspaceInfo ws ) throws IOException {
        LOGGER.fine( "Removing workspace " + ws.getName() );
        File dir = dir( ws );
        FileUtils.deleteDirectory( dir );
    }
    
    //namespaces
    void addNamespace( NamespaceInfo ns ) throws IOException {
        File dir = dir( ns, true );
        dir.mkdirs();
        persist( ns, dir, "namespace.xml" );
    }
    
    void modifyNamespace( NamespaceInfo ns) throws IOException {
        File dir = dir( ns );
        persist( ns, dir, "namespace.xml" );
    }
    
    void removeNamespace( NamespaceInfo ns ) throws IOException {
        File dir = dir( ns );
        new File( dir, "namespace.xml" ).delete();
    }
    
    //datastores
    void addDataStore( DataStoreInfo ds ) throws IOException {
        File dir = dir( ds );
        dir.mkdir();
        
        persist( ds, dir, "datastore.xml" );
    }
    
    void modifyDataStore( DataStoreInfo ds ) throws IOException {
        File dir = dir( ds );
        persist( ds, dir, "datastore.xml" );
    }
    
    void removeDataStore( DataStoreInfo ds ) throws IOException {
        File dir = dir( ds );
        FileUtils.deleteDirectory( dir );
    }
    
    //feature types
    void addFeatureType( FeatureTypeInfo ft ) throws IOException {
        File dir = dir( ft );
        dir.mkdir();
        persist( ft, dir, "featuretype.xml" );
    }
    
    void modifyFeatureType( FeatureTypeInfo ft ) throws IOException {
        File dir = dir( ft );
        persist( ft, dir, "featuretype.xml");
    }
    
    void removeFeatureType( FeatureTypeInfo ft ) throws IOException {
        File dir = dir( ft );
        FileUtils.deleteDirectory( dir );
    }
    
    //coverage stores
    void addCoverageStore( CoverageStoreInfo cs ) throws IOException {
        File dir = dir( cs );
        dir.mkdir();
        
        persist( cs, dir, "coveragestore.xml" );
    }
    
    void modifyCoverageStore( CoverageStoreInfo cs ) throws IOException {
        File dir = dir( cs );
        persist( cs, dir, "coveragestore.xml" );
    }
    
    void removeCoverageStore( CoverageStoreInfo cs ) throws IOException {
        File dir = dir( cs );
        FileUtils.deleteDirectory( dir );
    }
    
    //coverages
    void addCoverage( CoverageInfo c ) throws IOException {
        File dir = dir( c );
        dir.mkdir();
        persist( c, dir, "coverage.xml" );
    }
    
    void modifyCoverage( CoverageInfo c ) throws IOException {
        File dir = dir( c );
        persist( c, dir, "coverage.xml");
    }
    
    void removeCoverage( CoverageInfo c ) throws IOException {
        File dir = dir( c );
        FileUtils.deleteDirectory( dir );
    }
    
    //layers
    void addLayer( LayerInfo l ) throws IOException {
        File dir = dir( l );
        dir.mkdir();
        persist( l, dir, "layer.xml" );
    }
    
    void modifyLayer( LayerInfo l ) throws IOException {
        File dir = dir( l );
        persist( l, dir, "layer.xml");
    }
    
    void removeLayer( LayerInfo l ) throws IOException {
        File dir = dir( l );
        FileUtils.deleteDirectory( dir );
    }
    
    //styles
    void addStyle( StyleInfo s ) throws IOException {
        dir( s, true );
        persist( s, file( s ) );
    }
    
    void modifyStyle( StyleInfo s ) throws IOException {
        persist( s, file( s ) );
        
        //save out sld
        File f = file(s);
        BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( f ) );
        SLDTransformer tx = new SLDTransformer();
        try {
            tx.transform( s.getSLD(),out );
            out.flush();
        } 
        catch (TransformerException e) {
            throw (IOException) new IOException().initCause( e );
        }
        finally {
            out.close();
        }
    }
    
    void removeStyle( StyleInfo s ) throws IOException {
        file( s ).delete();
    }
    
    //layer groups
    void addLayerGroup( LayerGroupInfo lg ) throws IOException {
        dir( lg, true );
        persist( lg, file( lg ) );
    }
    
    void modifyLayerGroup( LayerGroupInfo lg ) throws IOException {
        persist( lg, file( lg ) );
    }
    
    void removeLayerGroup( LayerGroupInfo lg ) throws IOException {
        file( lg ).delete();
    }
    
    void backupDirectory(File dir) throws IOException {
        File bak = new File( dir.getCanonicalPath() + ".bak");
        if ( bak.exists() ) {
            FileUtils.deleteDirectory( bak );
        }
        dir.renameTo( bak );
    }
    
    File dir( WorkspaceInfo ws ) throws IOException {
        return dir( ws, false );
    }
    
    File dir( WorkspaceInfo ws, boolean create ) throws IOException {
        File d = rl.find( "workspaces", ws.getName() );
        if ( d == null && create ) {
            d = rl.createDirectory( "workspaces", ws.getName() );
        }
        return d;
    }
    
    File dir( NamespaceInfo ns ) throws IOException {
        return dir( ns, false );
    }
    
    File dir( NamespaceInfo ns, boolean create ) throws IOException {
        File d = rl.find( "workspaces", ns.getPrefix() );
        if ( d == null && create ) {
            d = rl.createDirectory( "workspaces", ns.getPrefix() );
        }
        return d;
    }
    
    File dir( DataStoreInfo ds ) throws IOException {
        return new File( dir( ds.getWorkspace() ), ds.getName() );
    }
    
    File dir( FeatureTypeInfo ft ) throws IOException {
        return new File( dir( ft.getStore() ), ft.getName() );
    }
    
    File dir( CoverageStoreInfo cs ) throws IOException {
        return new File( dir( cs.getWorkspace() ), cs.getName() );
    }
    
    File dir( CoverageInfo c ) throws IOException {
        return new File( dir( c.getStore() ), c.getName() );
    }
    
    File dir( LayerInfo l ) throws IOException {
        if ( l.getResource() instanceof FeatureTypeInfo) {
            return dir( (FeatureTypeInfo) l.getResource() );
        }
        else if ( l.getResource() instanceof CoverageInfo ) {
            return dir( (CoverageInfo) l.getResource() );
        }
        return null;
    }
    
    File dir( StyleInfo s ) throws IOException {
        return dir( s, false );
    }
    
    File dir( StyleInfo s, boolean create ) throws IOException {
        File d = rl.find( "styles" );
        if ( d == null && create ) {
            d = rl.createDirectory( "styles" );
        }
        return d;
    }
    
    File file( StyleInfo s ) throws IOException {
        return new File( dir( s ), s.getName() + ".xml");
    }
    
    File dir( LayerGroupInfo lg ) throws IOException {
        return dir( lg, false );
    }
    
    File dir( LayerGroupInfo lg, boolean create ) throws IOException {
        File d = rl.find( "layergroups" );
        if ( d == null && create ) {
            d = rl.createDirectory( "layergroups"); 
        }
        return d;
    }
    
    File file( LayerGroupInfo lg ) throws IOException {
        return new File( dir( lg ), lg.getName() + ".xml" );
    }
    
    void persist( Object o, File dir, String filename ) throws IOException {
        persist( o, new File( dir, filename ) );
    }

    void persist( Object o, File f ) throws IOException {
        synchronized ( xp ) {
            BufferedOutputStream out = 
                new BufferedOutputStream( new FileOutputStream( f ) );
            xp.save( o, out );
            out.flush();
            out.close();
        }
        LOGGER.fine("Persisted " + o.getClass().getName() + " to " + f.getAbsolutePath() );
    }
}

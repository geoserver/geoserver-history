package org.geoserver.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.log4j.helpers.Loader;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.Wrapper;
import org.geoserver.catalog.impl.CatalogImpl;
import org.geoserver.catalog.util.LegacyCatalogImporter;
import org.geoserver.catalog.util.LegacyFeatureTypeInfoReader;
import org.geoserver.config.util.LegacyConfigurationImporter;
import org.geoserver.config.util.LegacyServiceLoader;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.config.util.XStreamServiceLoader;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.util.logging.Logging;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * Initializes GeoServer configuration and catalog on startup.
 * <p>
 * This class is registered in a spring context and post processes the 
 * singleton beans {@link Catalog} and {@link GeoServer}, populating them 
 * with data from the GeoServer data directory. 
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class GeoServerLoader implements BeanPostProcessor, DisposableBean, 
    ApplicationContextAware {

    static Logger LOGGER = Logging.getLogger( "org.geoserver" );
    
    GeoServerResourceLoader resourceLoader;
    GeoServer geoserver;
    
    public GeoServerLoader( GeoServerResourceLoader resourceLoader ) {
        this.resourceLoader = resourceLoader;
    }
    
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        GeoserverDataDirectory.init((WebApplicationContext)applicationContext);
    }
    
    public final Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    public final Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        if ( bean instanceof Catalog ) {
            //ensure this is not a wrapper but the real deal
            if ( bean instanceof Wrapper && ((Wrapper) bean).isWrapperFor(Catalog.class) ) {
                return bean;
            }
            
            //load
            try {
                loadCatalog( (Catalog) bean );
            } 
            catch (Exception e) {
                throw new RuntimeException( e );
            }
        }
        
        if ( bean instanceof GeoServer ) {
            geoserver = (GeoServer) bean;
            try {
                loadGeoServer( geoserver );
            } 
            catch (Exception e) {
                throw new RuntimeException( e );
            }
            //initialize();
        }
        
        return bean;
    }
    
    protected void loadCatalog(Catalog catalog) throws Exception {
        //create an xstream persister
        XStreamPersister xp = new XStreamPersister();
        
        //look for catalog2.xml
        File f = resourceLoader.find( "catalog2.xml" );
        if ( f != null ) {
            //load with xstream
            CatalogImpl catalog2 = depersist( xp, f, CatalogImpl.class );
            ((CatalogImpl)catalog).sync( catalog2 );
        } else {
            // import old style catalog
            File oldCatalog = resourceLoader.find( "catalog.xml" );
            if(oldCatalog != null) {
                CatalogImpl catalog2 = new CatalogImpl();
                new LegacyCatalogImporter(catalog2).imprt(resourceLoader.getBaseDirectory());
                ((CatalogImpl)catalog).sync( catalog2 );
            } 
        }
        
        //set the catalog to resolve references directly
        xp.setCatalog( catalog );
        
        //load feature types and coverage info files
        File workspaces = resourceLoader.find( "workspaces" );
        if ( workspaces == null ) {
            LOGGER.warning( "No workspaces found.");
            return;
        }
        
        //load resources
        for ( WorkspaceInfo ws : catalog.getWorkspaces() ) {
            File workspace = new File( workspaces, ws.getName() );
            if ( !workspace.exists() || workspace.isFile() ) {
                LOGGER.warning( "Ignoring workspace '" + ws.getName() + "', no such directory.");
                continue;
            }
            
            File[] ls = workspace.listFiles();
            for ( File dir : ls ) {
                if ( !dir.isDirectory() ) {
                    continue;
                }
                
                //load the resource
                File info = new File( dir, "resource.xml" );
                if ( info.exists() ) {
                    //read the file
                    try {
                        ResourceInfo resource = depersist( xp, info, ResourceInfo.class );
                        catalog.add( resource );
                    }
                    catch (Exception e) {
                        LOGGER.log( Level.WARNING, "Error occured loading resource '" + info.getAbsolutePath() + "'", e );
                    }
                }
                
                //load the layer
                File layerInfo = new File( dir, "layer.xml" );
                if ( layerInfo.exists() ) {
                    try {
                        LayerInfo layer = depersist( xp, layerInfo, LayerInfo.class );
                        catalog.add( layer );
                    }
                    catch( Exception e ) {
                        LOGGER.log( Level.WARNING, "Error occured loading layer '" + layerInfo.getAbsolutePath() + "'", e );
                    }
                }
            }
        }
    }
    
    protected void loadGeoServer( GeoServer geoServer ) throws Exception {
        File f = resourceLoader.find( "global.xml" );
        if ( f != null ) {
            BufferedInputStream in = new BufferedInputStream( new FileInputStream( f ) );
            GeoServerInfo global = new XStreamPersister().load( in, GeoServerInfo.class );
            geoServer.setGlobal( global );
            
            //load services
            List<XStreamServiceLoader> loaders = GeoServerExtensions.extensions( XStreamServiceLoader.class );
            for ( XStreamServiceLoader<ServiceInfo> l : loaders ) {
                try {
                    ServiceInfo s = l.load( geoServer );
                    geoServer.add( s );
                    
                    LOGGER.info( "Loaded service '" +  s.getId() + "', " + (s.isEnabled()?"enabled":"disabled") );
                }
                catch( Throwable t ) {
                    //TODO: log this
                    t.printStackTrace();
                }
                
            }
        } else {
            // fall back on the legacy configuration code
            if(resourceLoader.find("services.xml") != null) {
                new LegacyConfigurationImporter(geoServer).imprt(resourceLoader.getBaseDirectory());
            }
        }
        
        //load initializer extensions
        List<GeoServerInitializer> initializers = GeoServerExtensions.extensions( GeoServerInitializer.class );
        for ( GeoServerInitializer initer : initializers ) {
            try {
                initer.initialize( geoServer );
            }
            catch( Throwable t ) {
                //TODO: log this
                t.printStackTrace();
            }
        }
    }
    
    protected void initialize() {
        //load catalog
        LegacyCatalogImporter catalogImporter = new LegacyCatalogImporter();
        catalogImporter.setResourceLoader(resourceLoader);
        Catalog catalog = geoserver.getCatalog();
        if(catalog instanceof Wrapper && ((Wrapper) catalog).isWrapperFor(Catalog.class)) {
            catalog = ((Wrapper) catalog).unwrap(Catalog.class);
        }
        catalogImporter.setCatalog(catalog);
        
        try {
            catalogImporter.imprt( resourceLoader.getBaseDirectory() );
        }
        catch(Exception e) {
            throw new RuntimeException( e );
        }
        
        //load configuration
        LegacyConfigurationImporter importer = new LegacyConfigurationImporter();
        importer.setConfiguration(geoserver);
        
        try {
            importer.imprt( resourceLoader.getBaseDirectory() );
        } 
        catch (Exception e) {
            throw new RuntimeException( e );
        }
        
        //load initializer extensions
        List<GeoServerInitializer> initializers = GeoServerExtensions.extensions( GeoServerInitializer.class );
        for ( GeoServerInitializer initer : initializers ) {
            try {
                initer.initialize( geoserver );
            }
            catch( Throwable t ) {
                //TODO: log this
                t.printStackTrace();
            }
        }
    }
    
    public void reload() throws Exception {
        destroy();
        
        //reload catalog, make sure we reload the underlying catalog, not any wrappers
        Catalog catalog = geoserver.getCatalog();
        if ( catalog instanceof Wrapper ) {
            catalog = ((Wrapper)geoserver.getCatalog()).unwrap(Catalog.class);
            
        }
        loadCatalog( catalog );
        loadGeoServer(geoserver);
    }
    
    public void persist() throws Exception {
        //TODO: make the configuration backend pluggable... or loadable
        // from application context, or web.xml, or env variable, etc...
        XStreamPersister p = new XStreamPersister();
        BufferedOutputStream out = new BufferedOutputStream( 
            new FileOutputStream( resourceLoader.createFile( "catalog2.xml" ) )
        );
        
        //persist catalog
        Catalog catalog = geoserver.getCatalog();
        if( catalog instanceof Wrapper ) {
            catalog = ((Wrapper)catalog).unwrap( Catalog.class );
        }
        p.save( catalog, out );
        out.flush();
        out.close();
     
        //persist resources
        File workspaces = resourceLoader.findOrCreateDirectory( "workspaces" );
        for ( ResourceInfo r : catalog.getResources( ResourceInfo.class ) ) {
            WorkspaceInfo ws = r.getStore().getWorkspace();
            File workspace = new File( workspaces, ws.getName() );
            if ( !workspace.exists() ) {
                workspace.mkdir();
            }
            
            String dirName = r.getStore().getName() + "_" + r.getNativeName();
            dirName = URLEncoder.encode( dirName, "UTF-8" );
            
            File dir = new File( workspace, dirName );
            if ( !dir.exists() ) {
                dir.mkdir();
            }
            
            File info = new File( dir, "resource.xml" );
            try {
                persist( p, r, info );
            }
            catch( Exception e ) {
                LOGGER.log( Level.WARNING, "Error persisting '" + r.getName() + "'", e );
            }
            
            //persist layers publishing the resource
            LayerInfo l = catalog.getLayers( r ).get( 0 );
            try {
                persist( p, l, new File( dir, "layer.xml" ) );
            }
            catch( Exception e ) {
                LOGGER.log( Level.WARNING, "Error persisting layer '" + l.getName() + "'", e );
            }
        }
        
        
        //persist global
        try {
            persist( p, geoserver.getGlobal(), resourceLoader.createFile( "global.xml" ) );
        }
        catch( Exception e ) {
            LOGGER.log( Level.WARNING, "Error persisting global configuration.", e );
        }
        
        //persist services
        Collection services = geoserver.getServices();
        List<ServiceLoader> loaders = GeoServerExtensions.extensions( ServiceLoader.class );
        
        for ( Iterator s = services.iterator(); s.hasNext(); ) {
            ServiceInfo service = (ServiceInfo) s.next();
            for ( ServiceLoader loader : loaders ) {
                if (loader.getServiceClass().isInstance( service ) ) {
                    try {
                        loader.save( service, geoserver );
                        break;
                    }
                    catch( Throwable t ) {
                        LOGGER.warning( "Error persisting service: " + service.getId() );
                        LOGGER.log( Level.INFO, "", t );
                    }
                }
            }
        }
    }
    
    void persist( XStreamPersister xp, Object obj, File f ) throws Exception {
        BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( f  ) );
        xp.save( obj, out );    
        out.flush();
        out.close();
    }
    
    <T> T depersist( XStreamPersister xp, File f, Class<T> type ) throws Exception {
        BufferedInputStream in = new BufferedInputStream( new FileInputStream( f ) );
        T obj = xp.load( in, type );

        in.close();
        return obj;
    }
    
    public void destroy() throws Exception {
        //dispose
        geoserver.dispose();
    }
}

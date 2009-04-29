/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;

import javax.xml.namespace.QName;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.LockingManager;
import org.geotools.data.Transaction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.springframework.beans.factory.DisposableBean;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;
import org.vfny.geoserver.global.dto.CoverageStoreInfoDTO;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.DataStoreInfoDTO;
import org.vfny.geoserver.global.dto.DataTransferObjectFactory;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.global.dto.NameSpaceInfoDTO;
import org.vfny.geoserver.global.dto.StyleDTO;


/**
 * This class stores all the information that a catalog would (and CatalogConfig
 * used to).
 * <p>
 * All public methods besides constructors and stuff used for dependency injection
 * setters is synchronized to avoid response failures during the Geoserver reconfiguration
 * process (basically, each time you apply a new configuration set on the user interface).
 * </p>
 * <p>
 * A quick benchar did not show significant scalability loss. If one is to be encountered,
 * a more complex Reader/Write synchronization will be used, based on the java 5 concurrency
 * classes or their backport for java 1.4
 * </p>
 *
 * @author Gabriel Roldan, Axios Engineering
 * @author Chris Holmes
 * @author dzwiers
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 * @version $Id$
 * @deprecated use {@link Catalog}
 */
public class Data extends GlobalLayerSupertype /* implements Repository */implements DisposableBean {
    public static final String WEB_CONTAINER_KEY = "DATA";
    
    public static final Integer TYPE_VECTOR = LayerInfo.Type.VECTOR.getCode();
    //public static final Integer TYPE_VECTOR = new Integer(0);
    public static final Integer TYPE_RASTER = LayerInfo.Type.RASTER.getCode();
    //public static final Integer TYPE_RASTER = new Integer(1);

    ///** Default name of feature type information */
    //private static final String INFO_FILE = "info.xml";
    //
    /** used to create styles */
    private static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);

    private Map errors;
    
    org.geoserver.config.GeoServer gs;
    Catalog catalog;


    public Data( org.geoserver.config.GeoServer gs, Catalog catalog) {
        this.gs = gs;
        this.catalog = catalog;
        //errors = new HashMap();
    }
    
    public Data( org.geoserver.config.GeoServer gs) {
        this.gs = gs;
        this.catalog = gs.getCatalog();
        //errors = new HashMap();
    }

    
    public Catalog getCatalog() {
        return catalog;
    }
    
    public GeoServer getGeoServer() {
        return new GeoServer( gs );
        //return gs;
    }

    public void setDataDirectory(File dataDirectory) {
        throw new UnsupportedOperationException();
        //this.baseDir = dataDirectory;
    }

    public File getDataDirectory() {
        return GeoserverDataDirectory.getGeoserverDataDirectory();
        //return baseDir;
    }

    /*
     * Places the data in this container and innitializes it. Complex tests are
     * performed to detect existing datasources, while the remainder only
     * include simplistic id checks.
     *
     * @param config
     *
     * @throws NullPointerException
     */
    public synchronized void load(DataDTO config) {
        if (config == null) {
            throw new NullPointerException("Non null DataDTO required for load");
        }
        
        // Step 0: dispose datastore and readers as needed
        catalog.dispose();
      
        
        // Step 1: load formats, dataStores and Namespaces
        
        //nameSpaces = loadNamespaces(config);
        loadNamespaces(config);
        
        //formats = loadFormats(config);
        loadDrivers(config);
        
        //dataStores = loadDataStores(config);
        loadDataStores(config);
        
        //defaultNameSpace = (NameSpaceInfo) nameSpaces.get(config.getDefaultNameSpacePrefix());
    
        // Step 2: set up styles
        //styles = loadStyles(config);
        loadStyles(config);
        
        // Step 3: load featureTypes + coverages
        loadFeatureTypes(config);
        loadCoverages(config);
        //coverages = loadCoverages(config);
        
        //layerNames.clear();
        //featureTypes = loadFeatureTypes(config);
        //coverages = loadCoverages(config);
    }

    
    // ////////////////////////////////////////////////////////////////////////
    public synchronized Set getDataStores() {
        Set dataStores = new HashSet();
        for ( org.geoserver.catalog.DataStoreInfo ds : catalog.getDataStores() ) {
            dataStores.add( new DataStoreInfo( ds, catalog ) );
        }
        return dataStores;
        //return new HashSet(dataStores.values());
    }

    public synchronized Set getFormats() {
        Set coverageStores = new HashSet();
        for ( org.geoserver.catalog.CoverageStoreInfo cs : catalog.getCoverageStores() ) {
            coverageStores.add( new CoverageStoreInfo( cs, catalog ) );
        }
        return coverageStores;
        //return new HashSet(formats.values());
    }

    private final Map loadDrivers(DataDTO dto) {
        if ((dto == null) || (dto.getFormats() == null)) {
            return Collections.EMPTY_MAP; // we *are* allowed no datasets
        }
    
        List<String> dataDriverNames = new ArrayList<String>();        
        for (Iterator i = dto.getFormats().values().iterator(); i.hasNext();) {
            CoverageStoreInfoDTO formatDTO = (CoverageStoreInfoDTO) i.next();
            org.geoserver.catalog.CoverageStoreInfo cs = catalog.getFactory().createCoverageStore();
            new CoverageStoreInfo( cs, catalog ).load( formatDTO );
            
            org.geoserver.catalog.CoverageStoreInfo tmpCS = catalog.getCoverageStoreByName(cs.getName());
            if (tmpCS==null) catalog.add(cs);
            else {
                tmpCS.setDescription(cs.getDescription());
                tmpCS.setEnabled(cs.isEnabled());
                tmpCS.setName(cs.getName());
                tmpCS.setType(cs.getType());
                tmpCS.setURL(cs.getURL());
                tmpCS.setWorkspace(cs.getWorkspace());
                
                catalog.save(tmpCS);
            }
            
            dataDriverNames.add(cs.getName());
        }
        
        // cleaning up the Catalog
        for (org.geoserver.catalog.CoverageStoreInfo cs : catalog.getCoverageStores()) {
            if (!dataDriverNames.contains(cs.getName())) {
                catalog.remove(cs);
            }
        }
    
        return null;

    }

    /**
     * Configure a map of DataStoreInfo by dataStoreId.
     *
     * <p>
     * This method is order dependent and should be called by load( DataDTO ).
     * This method may have to be smarter in the face of reloads.
     * </p>
     *
     * <p>
     * Note that any disabled DTO will not be added to the map.
     * </p>
     *
     * <p>
     * This method is just to make laod( DataDTO ) readable, making it private
     * final will help
     * </p>
     *
     * @param dto
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException
     *             DOCUMENT ME!
     */
    private final Map loadDataStores(DataDTO dto) {
        if ((dto == null) || (dto.getDataStores() == null)) {
            return Collections.EMPTY_MAP; // we *are* allowed no datasets
        }
    
        List<String> dataDataStroreNames = new ArrayList<String>();
        for (Iterator i = dto.getDataStores().values().iterator(); i.hasNext();) {
            DataStoreInfoDTO dataStoreDTO = (DataStoreInfoDTO) i.next();
            org.geoserver.catalog.DataStoreInfo ds = catalog.getFactory().createDataStore();
            new DataStoreInfo(ds, catalog).load(dataStoreDTO);
            
            try {
                ds.getDataStore(null);
                ds.setError(null);
            }
            catch(Exception e) {
                LOGGER.warning("Error connecting to data store '" + dataStoreDTO.getId() + "'");
                LOGGER.log(Level.WARNING, "", e);
                ds.setEnabled(false);
                ds.setError(e);
            }
            
            org.geoserver.catalog.DataStoreInfo tmpDS = catalog.getDataStoreByName(ds.getName());
            if(tmpDS==null) catalog.add(ds);
            else {
                tmpDS.setDescription(ds.getDescription());
                tmpDS.setEnabled(ds.isEnabled());
                tmpDS.setName(ds.getName());
                tmpDS.setWorkspace(ds.getWorkspace());
                
                catalog.save(tmpDS);
            }
            
            dataDataStroreNames.add(ds.getName());
        }
    
        // cleaning up the Catalog
        for (org.geoserver.catalog.DataStoreInfo ds : catalog.getDataStores()) {
            if (!dataDataStroreNames.contains(ds.getName())) {
                catalog.remove(ds);
            }
        }

        return null;

    }

    /**
     * Configure a map of NamespaceInfo by prefix.
     *
     * <p>
     * This method is order dependent and should be called by load( DataDTO ).
     * This method may have to be smarter in the face of reloads.
     * </p>
     *
     * <p>
     * This method is just to make laod( DataDTO ) readable, making it private
     * final will help
     * </p>
     *
     * @param dto
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException
     *             DOCUMENT ME!
     */
    private final Map loadNamespaces(DataDTO dto) {
        if ((dto == null) || (dto.getNameSpaces() == null)) {
            return Collections.EMPTY_MAP; // we *are* allowed no datasets
        }
    
        List<String> dataNameSpaceNames = new ArrayList<String>();
        for (Iterator i = dto.getNameSpaces().values().iterator(); i.hasNext();) {
            NameSpaceInfoDTO namespaceDto = (NameSpaceInfoDTO) i.next();
            
            NamespaceInfo ns = catalog.getFactory().createNamespace();
            new NameSpaceInfo(ns, catalog).load(namespaceDto);
            
            NamespaceInfo tmpNS = catalog.getNamespaceByPrefix(ns.getPrefix());
            if(tmpNS==null) catalog.add(ns);
            else {
                tmpNS.setPrefix(ns.getPrefix());
                tmpNS.setURI(ns.getURI());
                
                catalog.save(tmpNS);
            }
            
            WorkspaceInfo ws = catalog.getFactory().createWorkspace();
            ws.setName(ns.getPrefix());
            
            WorkspaceInfo tmpWS = catalog.getWorkspaceByName(ws.getName());
            if(tmpWS==null) catalog.add(ws);
            else {
                tmpWS.setName(ws.getName());
                
                catalog.save(tmpWS);
            }
            
            if (namespaceDto.isDefault()) {
                catalog.setDefaultNamespace(ns);
                catalog.setDefaultWorkspace(ws);
            }
            
            dataNameSpaceNames.add(ns.getPrefix());
        }
    
        // cleaning up the Catalog
        for (org.geoserver.catalog.NamespaceInfo ns : catalog.getNamespaces()) {
            if (!dataNameSpaceNames.contains(ns.getPrefix())) {
                WorkspaceInfo ws = catalog.getWorkspaceByName(ns.getPrefix());
                catalog.remove(ws);
                catalog.remove(ns);
            }
        }

        return null;
        

    }

    private final void loadCoverages(DataDTO dto) {
        List<String> dataCoverageNames = new ArrayList<String>();
        for (Iterator i = dto.getCoverages().values().iterator(); i.hasNext();) {
            CoverageInfoDTO cDTO = (CoverageInfoDTO) i.next();
            org.geoserver.catalog.CoverageStoreInfo cs = catalog.getCoverageStoreByName(cDTO.getFormatId());
            
            if ( cs != null ) {
                if ( !cs.isEnabled() ) {
                    LOGGER.warning( "Ignoring coverage '" + cDTO.getName() + "', coverage store '" + cDTO.getFormatId() + "' is disabled." );
                }
            }
            else {
                LOGGER.warning( "Ignoring coverage '" + cDTO.getName() + "', data store '" + cDTO.getFormatId() + "' not found." );
            }
            
            org.geoserver.catalog.CoverageInfo ci = catalog.getFactory().createCoverage();

            CoverageStoreInfoDTO format = (CoverageStoreInfoDTO)dto.getFormats().get(cDTO.getFormatId());
            ci.setNamespace(catalog.getNamespaceByPrefix(format.getNameSpaceId()));

            LayerInfo layer = catalog.getFactory().createLayer();
            layer.setResource(ci);
            
            try {
                new CoverageInfo(layer,catalog).load( cDTO );
            }
            catch( Exception e ) {
                //only log if there was no problem with datastore
                if ( cs != null && cs.isEnabled() ) {
                    LOGGER.warning( "Error loading coverage'" + cDTO.getName() + "'");
                    LOGGER.log( Level.INFO, "", e );
                    ci.setEnabled(false);    
                }
            }
            
            if (ci.isEnabled() && ( cs == null || !cs.isEnabled())) {
                ci.setEnabled(false);
            }
            
            org.geoserver.catalog.CoverageInfo tmpCI = catalog.getCoverageByName(ci.getName());
            if(tmpCI==null) catalog.add(ci);
            else {

                
                catalog.save(tmpCI);
            }
            
            
            LayerInfo tmpLayer = catalog.getLayerByName(ci.getName());
            if(tmpLayer==null) {
                layer.setName(ci.getName());
                //layer.setDefaultStyle()
                layer.setType(LayerInfo.Type.RASTER);
                catalog.add(layer);
            } else {
                tmpLayer.setDefaultStyle(layer.getDefaultStyle());
                tmpLayer.setEnabled(layer.isEnabled());
                tmpLayer.setLegend(layer.getLegend());
                tmpLayer.setName(ci.getName());
                tmpLayer.setPath(layer.getPath());
                tmpLayer.setResource(tmpCI);
                tmpLayer.setType(layer.getType());
                
                catalog.save(tmpLayer);
            }
            
            dataCoverageNames.add(ci.getName());
        }
        
        // cleaning up the Catalog
        for (org.geoserver.catalog.CoverageInfo ci : catalog.getCoverages()) {
            if (!dataCoverageNames.contains(ci.getName())) {
                LayerInfo layer = catalog.getLayerByName(ci.getName());
                if (layer != null)
                    catalog.remove(layer);
                catalog.remove(ci);
            }
        }
        
        
    }

    /**
     * Configure a map of FeatureTypeInfo by prefix:typeName.
     *
     * <p>
     * Note that this map uses namespace prefix (not datastore ID like the the
     * configuration system). That is because this is the actual runtime, in
     * which we access FeatureTypes by namespace. The configuration system uses
     * dataStoreId which is assumed to be more stable across changes (one can
     * reassing a SimpleFeatureType to a different namespace, but not a different
     * dataStore).
     * </p>
     *
     * <p>
     * Note loadDataStores() and loadNamespaces() must be called prior to using
     * this function!
     * </p>
     *
     * @param dto
     *            configDTO
     *
     * @return
     *
     * @throws NullPointerException
     *             DOCUMENT ME!
     */
    private final void loadFeatureTypes(DataDTO dto) {
        List<String> dataFeatureTypeNames = new ArrayList<String>();
        for ( Iterator i = dto.getFeaturesTypes().values().iterator(); i.hasNext(); ) {
            FeatureTypeInfoDTO ftDTO = (FeatureTypeInfoDTO) i.next();
            org.geoserver.catalog.DataStoreInfo ds = catalog.getDataStoreByName(ftDTO.getDataStoreId());
            
            if ( ds != null ) {
                if ( !ds.isEnabled() ) {
                    LOGGER.warning( "Ignoring feature type '" + ftDTO.getName() + "', data store '" + ftDTO.getDataStoreId() + "' is disabled." );
                }
            }
            else {
                LOGGER.warning( "Ignoring feature type '" + ftDTO.getName() + "', data store '" + ftDTO.getDataStoreId() + "' not found." );
            }
            
            org.geoserver.catalog.FeatureTypeInfo fti = catalog.getFactory().createFeatureType();
            LayerInfo layer = catalog.getFactory().createLayer();
            layer.setResource( fti );
            
            try {
                new FeatureTypeInfo(layer,catalog).load( ftDTO );
            }
            catch( Exception e ) {
                //only log if there was no problem with datastore
                if ( ds != null && ds.isEnabled() ) {
                    LOGGER.warning( "Error loading feature type '" + ftDTO.getName() + "'");
                    LOGGER.log( Level.INFO, "", e );
                    fti.setEnabled(false);    
                }
            }
            
            if ( fti.isEnabled() && ( ds == null || !ds.isEnabled() ) ) {
                fti.setEnabled(false);
            }
            
            org.geoserver.catalog.FeatureTypeInfo tmpFti = catalog.getFeatureTypeByName(fti.getName()); 
            if(tmpFti==null) catalog.add(fti);
            else {

                
                catalog.save(tmpFti);
            }
            
            LayerInfo tmpLayer = catalog.getLayerByName(fti.getName());
            if(tmpLayer==null) {
                layer.setName(fti.getName());
                //layer.setDefaultStyle()
                layer.setType(LayerInfo.Type.VECTOR);
                catalog.add(layer);
            } else {
                tmpLayer.setDefaultStyle(layer.getDefaultStyle());
                tmpLayer.setEnabled(layer.isEnabled());
                tmpLayer.setLegend(layer.getLegend());
                tmpLayer.setName(fti.getName());
                tmpLayer.setPath(layer.getPath());
                tmpLayer.setResource(tmpFti);
                tmpLayer.setType(layer.getType());
                
                catalog.save(tmpLayer);
            }
            
            dataFeatureTypeNames.add(fti.getName());
        }
        
        // cleaning up the Catalog
        for (org.geoserver.catalog.FeatureTypeInfo fti : catalog.getFeatureTypes()) {
            if (!dataFeatureTypeNames.contains(fti.getName())) {
                LayerInfo layer = catalog.getLayerByName(fti.getName());
                catalog.remove(layer);
                catalog.remove(fti);
            }
        }

    }

    private List createAttrDTOsFromSchema(SimpleFeatureType featureType) {
        List attrList = DataTransferObjectFactory.generateAttributes(featureType);
    
        /*
         * new ArrayList(featureType.getAttributeCount()); for (int index = 0;
         * index < featureType.getAttributeCount(); index++) { AttributeDescriptor
         * attrib = featureType.getAttributeType(index); attrList.add(new
         * AttributeTypeInfoDTO(attrib)); }
         */
        return attrList;
    }

    /**
     * Generate map of geotools2 Styles by id.
     *
     * <p>
     * The filename specified by the StyleDTO will be used to generate the
     * resulting Styles.
     * </p>
     *
     * @param dto
     *            requested configuration
     *
     * @return Map of Style by id
     *
     * @throws NullPointerException
     *             If the style could not be loaded from the filename
     *
     * @see Data.loadStyle() for more information
     */
    private final Map loadStyles(DataDTO dto) {
        if ((dto == null) || (dto.getStyles() == null)) {
            return Collections.EMPTY_MAP; // we *are* allowed no datasets
        }
    
        List<String> dataStyleNames = new ArrayList<String>();
        for (Iterator i = dto.getStyles().values().iterator(); i.hasNext();) {
            StyleDTO styleDTO = (StyleDTO) i.next();
            StyleInfo s = catalog.getFactory().createStyle();
            s.setName( styleDTO.getId() );
            s.setFilename( styleDTO.getFilename().getName() );
            
            StyleInfo tmpStyle = catalog.getStyleByName(s.getName());
            if(tmpStyle==null) catalog.add(s);
            else {
                tmpStyle.setName(s.getName());
                tmpStyle.setFilename(s.getFilename());
                
                catalog.save(tmpStyle);
            }
            
            dataStyleNames.add(s.getName());
            
            //clear the resource pool
            catalog.getResourcePool().clear(s);
        }
        
        // cleaning up the Catalog
        for (org.geoserver.catalog.StyleInfo s : catalog.getStyles()) {
            if (!dataStyleNames.contains(s.getName())) {
                catalog.remove(s);
            }
        }

        return null;

    }

    /**
     * Status output
     *
     * @param title
     *            DOCUMENT ME!
     * @param status
     *            DOCUMENT ME!
     */
    static final void outputStatus(String title, Map status) {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(title);
        }

        for (Iterator i = status.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();

            if (value == Boolean.TRUE) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(new StringBuffer(key).append(": ready").toString());
                }
            } else if (value instanceof Throwable) {
                Throwable t = (Throwable) value;

                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, new StringBuffer(key).append(" not ready").toString(),
                        t);
                }
            } else {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(new StringBuffer(key).append(": '").append(value).append("'")
                                                        .toString());
                }
            }
        }
    }

    /**
     * Dynamically tries to connect to every DataStore!
     *
     * <p>
     * Returns a map of Exception by dataStoreId:typeName. If by some marvel the
     * we could connect to a FeatureSource we will record Boolean.TRUE.
     * </p>
     *
     * @return Map of Exception by dataStoreId:typeName
     */
    public synchronized Map statusDataStores() {
        Map m = new HashMap();
//        if ( errors == null ) {
            for (org.geoserver.catalog.FeatureTypeInfo ft : catalog.getFeatureTypes() ) {
                org.geoserver.catalog.DataStoreInfo ds = ft.getStore();
                if ( ds.getError() != null ) {
                    m.put( ft, ds.getError() );
                    continue;
                }
                
                m.put( ft, ft.isEnabled() );
            }
            
            return m;

    }

    /**
     * Dynamically tries to connect to every Namespace!
     *
     * <p>
     * Returns a map of Exception by prefix:typeName. If by some marvel the we
     * could connect to a FeatureSource we will record Boolean.TRUE.
     * </p>
     *
     * @return Map of Exception by prefix:typeName
     */
    public synchronized Map statusNamespaces() {
        Map m = new HashMap();
        return m;

    }

    /**
     * toDTO purpose.
     *
     * <p>
     * This method is package visible only, and returns a reference to the
     * GeoServerDTO. This method is unsafe, and should only be used with extreme
     * caution.
     * </p>
     *
     * @return DataDTO the generated object
     */
    public synchronized DataDTO toDTO() {
        DataDTO dto = new DataDTO();

        HashMap tmp = new HashMap();
        
        NameSpaceInfo[] nameSpaces = getNameSpaces();
        for ( int i = 0; i < nameSpaces.length; i++ ) {
            tmp.put(nameSpaces[i].getPrefix(), nameSpaces[i].toDTO());
        }
        

        dto.setNameSpaces(tmp);

        if ( getDefaultNameSpace() != null ) {
            dto.setDefaultNameSpacePrefix(getDefaultNameSpace().getPrefix());
        }
 

        tmp = new HashMap();
        for ( StyleInfo s : catalog.getStyles() ) {
            StyleDTO sdto = new StyleDTO();
            sdto.setId (s.getName());
            sdto.setFilename(GeoserverDataDirectory.findStyleFile(s.getFilename()));
            sdto.setDefault(false);
            tmp.put(s.getName(),sdto);
        }


        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer(new StringBuffer("setting styles to: ").append(tmp).toString());
        }

        dto.setStyles(tmp);

        tmp = new HashMap();
        for ( Iterator f = getFormats().iterator(); f.hasNext(); ) {
            CoverageStoreInfo format = (CoverageStoreInfo) f.next();
            tmp.put(format.getId(), format.toDTO());
        }

        dto.setFormats(tmp);

        tmp = new HashMap();
        for ( Iterator d = getDataStores().iterator(); d.hasNext(); ) {
            DataStoreInfo dataStore = (DataStoreInfo) d.next();
            tmp.put( dataStore.getId(), dataStore.toDTO() );
        }

  

        dto.setDataStores(tmp);

        tmp = new HashMap();
        for ( Iterator e = getFeatureTypeInfos().entrySet().iterator(); e.hasNext(); ) {
            Map.Entry entry = (Entry) e.next();
            String key = (String) entry.getKey();
            FeatureTypeInfo fti = (FeatureTypeInfo) entry.getValue();
            tmp.put( key, fti.toDTO() );
        }


        dto.setFeaturesTypes(tmp);
 
        tmp = new HashMap();
        for ( Iterator e = getCoverageInfos().entrySet().iterator(); e.hasNext(); ) {
            Map.Entry entry = (Entry) e.next();
            String key = (String) entry.getKey();
            CoverageInfo ci = (CoverageInfo) entry.getValue();
            tmp.put( key, ci.toDTO() );
        }
        //i = coverages.keySet().iterator();
        //
        //while (i.hasNext()) {
        //    CoverageInfo cvi = (CoverageInfo) coverages.get(i.next());
        //    tmp.put(cvi.getName(), cvi.toDTO());
        //}
        //
        dto.setCoverages(tmp);
        
        return dto;
    }

    /**
     * Locate a DataStoreInfo by its id attribute.
     *
     * @param id
     *            the DataStoreInfo id looked for
     *
     * @return the DataStoreInfo with id attribute equals to <code>id</code>
     *         or null if there no exists
     */
    public synchronized DataStoreInfo getDataStoreInfo(String id) {
        org.geoserver.catalog.DataStoreInfo ds = catalog.getDataStoreByName(id);
        return ds != null && ds.isEnabled() ? 
                new DataStoreInfo( ds, catalog ) : null;
 
    }

    /**
     * Locate a CoverageStoreInfo by its id attribute.
     *
     * @param id
     *            the CoverageStoreInfo id looked for
     *
     * @return the CoverageStoreInfo with id attribute equals to <code>id</code>
     *         or null if there no exists
     */
    public synchronized CoverageStoreInfo getFormatInfo(String id) {
        org.geoserver.catalog.CoverageStoreInfo cs = catalog.getCoverageStoreByName(id);
        return cs != null && cs.isEnabled() ?  
                new CoverageStoreInfo( cs, catalog ) : null;
        

    }

    /**
     * getNameSpaces purpose.
     *
     * <p>
     * List of all relevant namespaces
     * </p>
     *
     * @return NameSpaceInfo[]
     */
    public synchronized NameSpaceInfo[] getNameSpaces() {
        NameSpaceInfo[] ns = new NameSpaceInfo[catalog.getNamespaces().size()];
        int i = 0;
        for ( NamespaceInfo namespace : catalog.getNamespaces() ) {
            ns[i++] = new NameSpaceInfo( namespace, catalog );
        }
        return ns;
        
        //NameSpaceInfo[] ns = new NameSpaceInfo[nameSpaces.values().size()];
        //
        //return (NameSpaceInfo[]) new ArrayList(nameSpaces.values()).toArray(ns);
    }

    /**
     * getNameSpace purpose.
     *
     * <p>
     * The NameSpaceInfo from the specified prefix
     * </p>
     *
     * @param prefix
     *
     * @return NameSpaceInfo resulting from the specified prefix
     */
    public synchronized NameSpaceInfo getNameSpace(String prefix) {
        NamespaceInfo ns = catalog.getNamespaceByPrefix(prefix);
        return ns != null ? new NameSpaceInfo( ns, catalog ) : null;
        
     
    }

    /**
     * Returns the NamespaceINfo object corresponding to a particular
     * namespace uri.
     * <p>
     * If a namespace info object could not be found with mathces <param>uri</param>
     * then <code>null</code> is returned.
     * </p>
     *
     * @param uri A namespace uri, non-null
     *
     * @return NameSpaceInfo resulting from the specified uri.
     */
    public synchronized NameSpaceInfo getNameSpaceFromURI(String uri) {
        NamespaceInfo ns = catalog.getNamespaceByURI(uri);
        return ns != null ? new NameSpaceInfo( ns, catalog ) : null; 
        

    }

    /**
     * getDefaultNameSpace purpose.
     *
     * <p>
     * Returns the default NameSpaceInfo for this Data object.
     * </p>
     *
     * @return NameSpaceInfo the default name space
     *
     * @uml.property name="defaultNameSpace"
     */
    public synchronized NameSpaceInfo getDefaultNameSpace() {
        return catalog.getDefaultNamespace() != null ? 
            new NameSpaceInfo( catalog.getDefaultNamespace(), catalog ) : null;
        //return defaultNameSpace;
    }

    /**
     * getStyles purpose.
     *
     * <p>
     * A reference to the map of styles
     * </p>
     *  
     * @return Map A map containing the Styles.
     *
     * @uml.property name="styles"
     */
    public synchronized Map getStyles() {
        HashMap styles = new HashMap();
        for (StyleInfo s : catalog.getStyles() ) {
            try {
                styles.put( s.getName(),s.getStyle());
            } 
            catch (IOException e) {
                throw new RuntimeException( e );
            }
        }
        return styles;
        //return this.styles;
    }

    public synchronized Style getStyle(String id) {
        StyleInfo s = catalog.getStyleByName(id);
        try {
            return s != null ? s.getStyle() : null;
        } 
        catch (IOException e) {
            throw new RuntimeException( e );
        }
        //return (Style) styles.get(id);
    }

    /**
     * Locate FeatureTypeInfo by name
     *
     * <p>
     * The following searchness is used::
     *
     * <ul>
     * <li> search prefix:typeName for direct match with name </li>
     * <li> search prefix:typeName for match with defaultnamespaceprefix:name
     * </li>
     * <li> linear search of typeName for direct match </li>
     * </ul>
     * </p>
     *
     * <p>
     * Yes this is the magic method used by TransasctionResponse. If you
     * wondered what it was doing - this is it.
     * </p>
     *
     * @param name
     *            String The FeatureTypeInfo Name
     *
     * @return FeatureTypeInfo
     *
     * @throws NoSuchElementException
     */
    public synchronized FeatureTypeInfo getFeatureTypeInfo(String name)
        throws NoSuchElementException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(new StringBuffer("getting type ").append(name).toString());
        }
        
        org.geoserver.catalog.FeatureTypeInfo ft = null;
        
        int i = name.indexOf( ':' );
        if ( i > -1 ) {
            String prefix = name.substring(0,i);
            String local = name.substring(i+1);
            ft = catalog.getFeatureTypeByName(prefix, local);
        }
        else {
            ft = catalog.getFeatureTypeByName(name);    
        }
        
        if ( ft != null ) {
            List<LayerInfo> layers = catalog.getLayers(ft);
            return new FeatureTypeInfo( layers.get(0), catalog );
            
        }

        throw new NoSuchElementException("Could not locate FeatureTypeConfig '" + name + "'");
    }

    /**
     * Gets a FeatureTypeINfo from a qualified name.
     * <p>
     * This method calls through to {@link #getFeatureTypeInfo(String, String)}.
     * </p>
     * @param name The qualified name of the feature type.
     */
    public FeatureTypeInfo getFeatureTypeInfo(QName name) {
        return getFeatureTypeInfo(name.getLocalPart(), name.getNamespaceURI());
    }

    /**
     * Gets a FeatureTypeINfo from a qualfieid type name.
     * 
     */
    public synchronized FeatureTypeInfo getFeatureTypeInfo( Name name ) {
        return getFeatureTypeInfo(  name.getLocalPart(), name.getNamespaceURI() );
    }
    /**
     * Gets a FeatureTypeInfo from a local type name (ie unprefixed), and a uri.
     *
     * <p>
     * This method is slow, use getFeatureType(String typeName), where possible.
     * For not he only user should be TransactionFeatureHandler.
     * </p>
     *
     * <p>
     * TODO: Jody here - David is this still correct?
     * </p>
     *
     * @param typename
     *            Name NameSpaceInfo name
     * @param uri
     *            NameSpaceInfo uri
     *
     * @return FeatureTypeInfo
     */
    public synchronized FeatureTypeInfo getFeatureTypeInfo(String typename, String uri) {
        // For some reason I don't understand GR patched this to remove the namespace 
        // test, but this breaks if there are multiple feature types with the same
        // name in different namespaces. Now, to stay on the safe side, I will lookup
        // first based on both name and namespace, and return a pure name matcher only
        // if a full name + namespace match was not found
        
        // This will be returned if we matched only the name but not the namespace
        org.geoserver.catalog.FeatureTypeInfo ft = catalog.getFeatureTypeByName(uri,typename);
        if ( ft == null ) {
            ft = catalog.getFeatureTypeByName(typename);
        }
        
        if ( ft != null ) {
            return new FeatureTypeInfo( layer( ft) , catalog );
        }
        return null;

    }

    public synchronized CoverageInfo getCoverageInfo(String name)
        throws NoSuchElementException {
        LOGGER.fine("getting coverage " + name);

        org.geoserver.catalog.CoverageInfo coverage = null;

        int i = name.indexOf( ':' );
        if ( i > -1 ) {
            String prefix = name.substring(0,i);
            String local = name.substring(i+1);
            coverage = catalog.getCoverageByName(prefix, local);
        }
        else {
            coverage = catalog.getCoverageByName(name);
        }
        
        if ( coverage != null ) {
            initCoverage(coverage);
            
            return new CoverageInfo( layer(coverage),catalog); 
        }

        throw new NoSuchElementException("Could not locate CoverageConfig '" + name + "'");
    }

    /**
     * @param coverage
     */
    private void initCoverage(org.geoserver.catalog.CoverageInfo coverage) {
        if (coverage.getFields() == null) {
         // initializing fields, vertical and temporal extent
            try {
                org.geoserver.catalog.CoverageStoreInfo coverageStore = coverage.getStore();
                Driver driver = coverage.getStore().getDriver();
                Map params = new HashMap();
                params.put("url", GeoserverDataDirectory.findDataFile(coverageStore.getURL()).toURI().toURL());
                CoverageAccess cvAccess = driver.connect(params, null, null);
                if (cvAccess != null) {
                    CoverageSource cvSource = cvAccess.access(new NameImpl(coverage.getNativeName()), null, AccessType.READ_ONLY, null, null);
                    if (cvSource != null) {
                        coverage.setFields(cvSource.getRangeType(null));

                        CoordinateReferenceSystem compundCRS = cvSource.getCoordinateReferenceSystem(null);
                        Set<TemporalGeometricPrimitive> temporalExtent = cvSource.getTemporalDomain(null);
                        CoordinateReferenceSystem temporalCRS = null;
                        CoordinateReferenceSystem verticalCRS = null;
                        if (temporalExtent != null && !temporalExtent.isEmpty()) {
                            if (compundCRS instanceof CompoundCRS) {
                                temporalCRS = ((CompoundCRS) compundCRS).getCoordinateReferenceSystems().get(0);
                            }
                        }
                        Set<org.opengis.geometry.Envelope> verticalExtent = cvSource.getVerticalDomain(false, null);
                        if (verticalExtent != null && !verticalExtent.isEmpty()) {
                            if (compundCRS instanceof CompoundCRS) {
                                if (temporalCRS != null)
                                    verticalCRS = ((CompoundCRS) compundCRS).getCoordinateReferenceSystems().get(1);
                                else
                                    verticalCRS = ((CompoundCRS) compundCRS).getCoordinateReferenceSystems().get(0);
                            } 
                        }

                        coverage.setTemporalCRS(temporalCRS);
                        coverage.setTemporalExtent(temporalExtent);

                        coverage.setVerticalCRS(verticalCRS);
                        coverage.setVerticalExtent(verticalExtent);
                    }
                }
            } catch (MalformedURLException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    public synchronized CoverageInfo getCoverageInfo(String name, String uri) {
        org.geoserver.catalog.CoverageInfo coverage = catalog.getCoverageByName(uri, name);
        if ( coverage != null ) {
            initCoverage(coverage);
            
            return new CoverageInfo( layer(coverage), catalog );
        }
        
        return null;

    }
    
    /**
     * Returns the map layer info for the specified layer, or null if the layer
     * is not known
     * @param layerName
     * @return
     */
    public MapLayerInfo getMapLayerInfo(String layerName) {
        Integer layerType = getLayerType(layerName);
        if(layerType == TYPE_VECTOR)
            return new MapLayerInfo(getFeatureTypeInfo(layerName));
        else if(layerType == TYPE_RASTER)
            return new MapLayerInfo(getCoverageInfo(layerName));
        else
            return null;
    }
    
    public MapLayerInfo getMapLayerInfo(String layerName, String fieldName) {
        Integer layerType = getLayerType(layerName);
        if(layerType == TYPE_VECTOR)
            return new MapLayerInfo(getFeatureTypeInfo(layerName));
        else if(layerType == TYPE_RASTER)
            return new MapLayerInfo(getCoverageInfo(layerName), fieldName);
        else
            return null;
    }


    /**
     * Retrieve map of FeatureTypeInfo by prefix:typeName.
     *
     * <p>
     * Returns all the featuretype information objects
     * </p>
     *
     * @return Map of FetureTypeInfo by prefix:typeName
     */
    public synchronized Map getFeatureTypeInfos() {
        Map map = new HashMap();
        for ( org.geoserver.catalog.FeatureTypeInfo ft : catalog.getFeatureTypes() ) {
            if(ft.isEnabled())
                map.put( ft.getPrefixedName(), new FeatureTypeInfo( layer( ft ), catalog ) );
        }
        return map;
        //return Collections.unmodifiableMap(featureTypes);
    }

    LayerInfo layer(ResourceInfo r) {
        final List<LayerInfo> layers = catalog.getLayers(r);
        if(!layers.isEmpty()) {
            return layers.get(0);
        } else
            return null;
    }
    
    public synchronized Map getCoverageInfos() {
        Map map = new HashMap();
        for ( org.geoserver.catalog.CoverageInfo coverage : catalog.getCoverages() ) {
            initCoverage(coverage);
            
            if(coverage.isEnabled())
                map.put( coverage.getPrefixedName(), new CoverageInfo(layer(coverage), catalog));

        }
        return map;
        //return Collections.unmodifiableMap(coverages);
    }

    // TODO: detect if a user put a full url, instead of just one to be
    // resolved, and
    // use that instead.
    public Style loadStyle(String fileName, String base)
        throws IOException {
        return loadStyle(new File(base + fileName));
    }

    /**
     * Load GeoTools2 Style from a fileName
     *
     * @param fileName
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException
     *             DOCUMENT ME!
     */
    public Style loadStyle(File fileName) throws IOException {
        SLDParser stylereader = new SLDParser(styleFactory, fileName);
    
        return stylereader.readXML()[0];
    }


    /**
     * The number of connections currently held.
     *
     * <p>
     * We will need to modify DataStore to provide access to the current count
     * of its connection pool (if appropriate). Right now we are asumming a one
     * DataStore equals One "Connection".
     * </p>
     *
     * <p>
     * This is a good compromize since I just want to indicate the amount of
     * resources currently tied up by GeoServer.
     * </p>
     *
     * @return Number of available connections.
     */
    public synchronized int getConnectionCount() {
        int count = 0;
        DataStoreInfo meta;
        DataStore dataStore;

        for (Iterator i = getDataStores().iterator(); i.hasNext();) {
        //for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            try {
                dataStore = meta.getDataStore();
            } catch (Throwable notAvailable) {
                continue; // not available
            }

            // TODO: All DataStore to indicate number of connections
            count += 1;
        }

        return count;
    }

    /**
     * Count locks currently held.
     *
     * <p>
     * Not sure if this should be the number of features locked, or the number
     * of FeatureLocks in existence (a FeatureLock may lock several features.
     * </p>
     *
     * @return number of locks currently held
     */
    public synchronized int getLockCount() {
        int count = 0;
        DataStore dataStore;

        LockingManager lockingManager;

        for (Iterator i = getDataStores().iterator(); i.hasNext();) {
        //for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            try {
                dataStore = meta.getDataStore();
            } catch (IllegalStateException notAvailable) {
                continue; // not available
            } catch (Throwable huh) {
                continue; // not even working
            }

            lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            // TODO: implement LockingManger.getLockSet()
            // count += lockingManager.getLockSet();
        }

        return count;
    }

    /**
     * Release all feature locks currently held.
     *
     * <p>
     * This is the implementation for the Admin "free lock" action, transaction
     * locks are not released.
     * </p>
     *
     * @return Number of locks released
     */
    public synchronized int lockReleaseAll() {
        int count = 0;

        for (Iterator i = getDataStores().iterator(); i.hasNext();) {
        //for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IllegalStateException notAvailable) {
                continue; // not available
            } catch (Throwable huh) {
                continue; // not even working
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            // TODO: implement LockingManger.releaseAll()
            // count += lockingManager.releaseAll();
        }

        return count;
    }

    /**
     * Release lock by authorization
     *
     * @param lockID
     */
    public synchronized void lockRelease(String lockID) {
        boolean refresh = false;

        for (Iterator i = getDataStores().iterator(); i.hasNext();) {
            //for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IllegalStateException notAvailable) {
                continue; // not available
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            Transaction t = new DefaultTransaction("Refresh " + meta.getNameSpace());

            try {
                t.addAuthorization(lockID);

                if (lockingManager.release(lockID, t)) {
                    refresh = true;
                }
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }
            } finally {
                try {
                    t.close();
                } catch (IOException closeException) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST, closeException.getMessage(), closeException);
                    }
                }
            }
        }

        if (!refresh) {
            // throw exception? or ignore...
        }
    }

    /**
     * Refresh lock by authorization
     *
     * <p>
     * Should use your own transaction?
     * </p>
     *
     * @param lockID
     */
    public synchronized void lockRefresh(String lockID) {
        boolean refresh = false;
        
        for (Iterator i = getDataStores().iterator(); i.hasNext();) {
        //for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IllegalStateException notAvailable) {
                continue; // not available
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            Transaction t = new DefaultTransaction("Refresh " + meta.getNameSpace());

            try {
                t.addAuthorization(lockID);

                if (lockingManager.refresh(lockID, t)) {
                    refresh = true;
                }
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }
            } finally {
                try {
                    t.close();
                } catch (IOException closeException) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST, closeException.getMessage(), closeException);
                    }
                }
            }
        }

        if (!refresh) {
            // throw exception? or ignore...
        }
    }

    /**
     * Implement lockRefresh.
     *
     * @param lockID
     * @param t
     *
     * @return true if lock was found and refreshed
     *
     * @throws IOException
     *
     * @see org.geotools.data.Data#lockRefresh(java.lang.String,
     *      org.geotools.data.Transaction)
     */
    public synchronized boolean lockRefresh(String lockID, Transaction t)
        throws IOException {
        boolean refresh = false;

        for (Iterator i = getDataStores().iterator(); i.hasNext();) {
        //for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IllegalStateException notAvailable) {
                continue; // not available
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            if (lockingManager.refresh(lockID, t)) {
                refresh = true;
            }
        }

        return refresh;
    }

    /**
     * Implement lockRelease.
     *
     * @param lockID
     * @param t
     *
     * @return true if the lock was found and released
     *
     * @throws IOException
     *
     * @see org.geotools.data.Data#lockRelease(java.lang.String,
     *      org.geotools.data.Transaction)
     */
    public synchronized boolean lockRelease(String lockID, Transaction t)
        throws IOException {
        boolean release = false;

        for (Iterator i = getDataStores().iterator(); i.hasNext();) {
        //for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IllegalStateException notAvailable) {
                continue; // not available
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            if (lockingManager.release(lockID, t)) {
                release = true;
            }
        }

        return release;
    }

    /**
     * Implement lockExists.
     *
     * @param lockID
     *
     * @return true if lockID exists
     *
     * @see org.geotools.data.Data#lockExists(java.lang.String)
     */
    public synchronized boolean lockExists(String lockID) {
        
        for (Iterator i = getDataStores().iterator(); i.hasNext();) {
        //for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IllegalStateException notAvailable) {
                continue; // not available
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            if (lockingManager.exists(lockID)) {
                return true;
            }
        }

        return false;
    }

    //
    // GeoTools2 Catalog API
    // 

    /**
     * Set of available Namespace prefixes.
     *
     * @return Set of namespace Prefixes
     *
     * @see org.geotools.data.Catalog#getPrefixes()
     */
    public synchronized Set getPrefixes() {
        Set prefixes = new HashSet();
        for ( NamespaceInfo ns : catalog.getNamespaces() ) {
            prefixes.add( ns.getPrefix() );
        }
        return prefixes;
        //return Collections.unmodifiableSet(nameSpaces.keySet());
    }

    /**
     * Prefix of the defaultNamespace.
     *
     * @return prefix of the default namespace
     *
     * @see org.geotools.data.Catalog#getDefaultPrefix()
     */
    public synchronized String getDefaultPrefix() {
        return catalog.getDefaultNamespace().getPrefix();
        //return defaultNameSpace.getPrefix();
    }

    /**
     * Implement getNamespace.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param prefix
     *
     * @return
     *
     * @see org.geotools.data.Catalog#getNamespace(java.lang.String)
     */
    public synchronized NameSpaceInfo getNamespaceMetaData(String prefix) {
        return getNameSpace(prefix);
    }

    /**
     * Register a DataStore with this Catalog.
     *
     * <p>
     * This is part of the public CatalogAPI, the fact that we don't want to
     * support it here may be gounds for it's removal.
     * </p>
     *
     * <p>
     * GeoSever and the global package would really like to have <b>complete</b>
     * control over the DataStores in use by the application. It recognize that
     * this may not always be possible. As GeoServer is extend with additional
     * Modules (such as config) that wish to locate and talk to DataStores
     * independently of GeoServer the best we can do is ask them to register
     * with the this Catalog in global.
     * </p>
     *
     * <p>
     * This reveals what may be a deisgn flaw in GeoTools2 DataStore. We have
     * know way of knowing if the dataStore has already been placed into our
     * care as DataStores are not good at identifying themselves. To complicate
     * matters most keep a static connectionPool around in their GDSFactory - it
     * could be that the Factories are supposed to be smart enough to prevent
     * duplication.
     * </p>
     *
     * @param dataStore
     *
     * @throws IOException
     *
     * @see org.geotools.data.Catalog#registerDataStore(org.geotools.data.DataStore)
     */
    public synchronized void registerDataStore(DataStore dataStore)
        throws IOException {
    }

    /**
     * Convience method for Accessing FeatureSource by prefix:typeName.
     *
     * <p>
     * This method is part of the public Catalog API. It allows the Validation
     * framework to be writen using only public Geotools2 interfaces.
     * </p>
     *
     * @param prefix
     *            Namespace prefix in which the SimpleFeatureType available
     * @param typeName
     *            typeNamed used to identify SimpleFeatureType
     *
     * @return
     *
     * @throws IOException
     *             DOCUMENT ME!
     *
     * @see org.geotools.data.Catalog#getFeatureSource(java.lang.String,
     *      java.lang.String)
     */
    public synchronized FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource(
            String prefix, String typeName) throws IOException {
        if ((prefix == null) || (prefix == "")) {
            prefix = getDefaultPrefix();
            //prefix = defaultNameSpace.getPrefix();
        }

        NameSpaceInfo namespace = getNamespaceMetaData(prefix);
        FeatureTypeInfo featureType = namespace.getFeatureTypeInfo(typeName);
        DataStoreInfo dataStore = featureType.getDataStoreMetaData();

        return dataStore.getDataStore().getFeatureSource(typeName);
    }


    /**
     * Given a layer name will return its type, or null if the layer is not there
     * @param layerName the layer name, either fully qualified (namespace:name) or
     *        just the name if the layers happens to be in the default namespace
     * @return the layer type (see {@link #TYPE_VECTOR} and {@link #TYPE_RASTER})
     */
    public Integer getLayerType(String layerName) {
        int i = layerName.indexOf( ':' );
        if ( i > -1 ) {
            layerName = layerName.substring( i+1 );
        }
        
        LayerInfo layer = catalog.getLayerByName(layerName);
        if ( layer == null ) {
            return null;
        }
        return layer.getType().getCode();
        
    }

    /**
     * Returns an unmodifiable set of known layer names (feature type and coverages)
     * @return
     */
    public Set getLayerNames() {
        HashSet layerNames = new HashSet();
        for ( LayerInfo l : catalog.getLayers() ) {
            layerNames.add( l.getName() );
        }
        return layerNames;
        //return Collections.unmodifiableSet(layerNames.keySet());
    }
    
    public void destroy() throws Exception {
        final Data catalog = this;
        final Set dataStores = catalog.getDataStores();
        LOGGER.info("Disposing DataStores at GeoServer shutdown...");
        for (Iterator it = dataStores.iterator(); it.hasNext();) {
            DataStoreInfo dataStoreInfo = (DataStoreInfo) it.next();
            LOGGER.fine("Disposing " + dataStoreInfo.getId());
            try {
                dataStoreInfo.dispose();
            } catch (RuntimeException e) {
                LOGGER.log(Level.WARNING, "Caught exception while disposing datastore "
                        + dataStoreInfo.getId(), e);
            }
        }
        
        for (Iterator it = catalog.getFormats().iterator(); it.hasNext();) {
            CoverageStoreInfo info = (CoverageStoreInfo) it.next();
            info.dispose();
        }
        LOGGER.info("Done disposing datastores.");
    }

}

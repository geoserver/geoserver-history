package org.geoserver.config.hibernate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.GeophysicParamInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ModelInfo;
import org.geoserver.catalog.ModelRunInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.LayerInfo.Type;
import org.geoserver.catalog.ModelInfo.DataType;
import org.geoserver.catalog.ModelInfo.Discipline;
import org.geoserver.catalog.hibernate.HbNamespaceInfo;
import org.geoserver.catalog.hibernate.HbWorkspaceInfo;
import org.geoserver.catalog.hibernate.HibernateCatalog;
import org.geoserver.catalog.impl.GeophysicParamInfoImpl;
import org.geoserver.catalog.impl.ModelInfoImpl;
import org.geoserver.catalog.impl.ModelRunInfoImpl;
import org.geoserver.config.ConfigurationListener;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerFactory;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.impl.GeoServerFactoryImpl;
import org.geoserver.config.impl.GeoServerInfoImpl;
import org.geoserver.data.util.CoverageStoreUtils;
import org.geoserver.hibernate.dao.IGeoServerDAO;
import org.geoserver.jai.JAIInfo;
import org.geoserver.wcs.WCSInfoImpl;
import org.geoserver.wfs.GMLInfo;
import org.geoserver.wfs.GMLInfoImpl;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.WFSInfoImpl;
import org.geoserver.wfs.GMLInfo.SrsNameStyle;
import org.geoserver.wms.WMSInfoImpl;
import org.geoserver.wms.WatermarkInfo;
import org.geoserver.wms.WatermarkInfoImpl;
import org.geotools.coverage.grid.GeneralGridRange;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.util.logging.Logging;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.vfny.geoserver.global.GeoserverDataDirectory;

import com.vividsolutions.jts.geom.Envelope;

public class HibernateGeoServer implements GeoServer {

    private static final Logger LOGGER = Logging.getLogger("org.geoserver.config.hibernate");

    /**
     * factory to create config objects.
     */
    GeoServerFactory factory = new GeoServerFactoryImpl();

    HibernateCatalog catalog;

    List<ConfigurationListener> listeners = new ArrayList<ConfigurationListener>();

    private boolean createBootstrapConfig;

    /**
     * 
     */
    private IGeoServerDAO catalogDAO;

    /**
     * 
     */
    private HibernateGeoServer() {
        super();
        createBootstrapConfig = true;
    }

    /**
     * 
     */
    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = (HibernateCatalog) catalog;
    }

    public GeoServerFactory getFactory() {
        return factory;
    }

    public void setFactory(GeoServerFactory factory) {
        this.factory = factory;
    }

    public GeoServerInfo getGlobal() {
        LOGGER.finest("Querying geoserver global configuration");
        GeoServerInfo geoserver = this.catalogDAO.getGeoServer();
        if (geoserver == null) {
            if (createBootstrapConfig) {
                // this is an empty configuration! create the minimal set of required object
                LOGGER.info("Creating geoserver bootstrap configuration, no prior configuration found on the database");
                geoserver = serviceBootStrap();
                // catalog.bootStrap();
            } else {
                LOGGER.info("Explicitly asked to skip boot strap configuration, database is empty");
                geoserver = null;
            }
        }

        return geoserver;
    }

    public void setGlobal(GeoServerInfo configuration) {
        GeoServerInfoImpl currentGlobal = (GeoServerInfoImpl) getGlobal();
        if (currentGlobal == null) {
            this.catalogDAO.save(configuration);
        } else {
            // use merge, the argument instance may be from another session
            ((GeoServerInfoImpl) configuration).setId(currentGlobal.getId());
            this.catalogDAO.merge(configuration);
        }
    }

    public void save(GeoServerInfo geoServer) {
        setGlobal(geoServer);
    }

    /**
     * @see GeoServer#add(ServiceInfo)
     */
    public void add(ServiceInfo service) {
        final String serviceId = service.getId();
        if (serviceId == null) {
            throw new NullPointerException("service id must not be null");
        }

        ServiceInfo existing = getService(serviceId);

        if (existing != null) {
            throw new IllegalArgumentException("service with id '" + serviceId + "' already exists");
        }

        GeoServerInfo global = getGlobal();
        service.setGeoServer(global);

        this.catalogDAO.save(service);
    }

    /**
     * 
     */
    public void remove(ServiceInfo service) {
        this.catalogDAO.delete(service);
    }

    /**
     * 
     */
    public void save(ServiceInfo service) {
        this.catalogDAO.merge(service);
    }

    /**
     * 
     */
    public Collection<? extends ServiceInfo> getServices() {
        return getServices(ServiceInfo.class);
    }

    /**
     * 
     * @param clazz
     * @return
     */
    public Collection<? extends ServiceInfo> getServices(Class<?> clazz) {
        return this.catalogDAO.getServices(clazz);
    }

    /**
     * 
     */
    public <T extends ServiceInfo> T getService(Class<T> clazz) {
        for (ServiceInfo si : getServices(clazz)) {
            if (clazz.isAssignableFrom(si.getClass())) {
                return (T) si;
            }
        }

        return null;
    }

    /**
     * 
     * @param id
     * @return
     */
    public ServiceInfo getService(String id) {
        return getService(id, ServiceInfo.class);
    }

    /**
     * 
     */
    public <T extends ServiceInfo> T getService(String id, Class<T> clazz) {
        return this.catalogDAO.getService(id, clazz);
    }

    /**
     * 
     * @param name
     * @return
     */
    public ServiceInfo getServiceByName(String name) {
        return getServiceByName(name, ServiceInfo.class);
    }

    /**
     * 
     */
    public <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz) {
        return this.catalogDAO.getServiceByName(name, clazz);
    }

    public void addListener(ConfigurationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ConfigurationListener listener) {
        listeners.remove(listener);
    }

    public void dispose() {
        catalog.dispose();
        listeners.clear();
    }

    /**
     * Sets whether a minimal empty configuration should be created when {@link #getGlobal()} is
     * first called and the database is completelly empty. Defaults to true, and this method is
     * intended to be used by unit tests only
     * 
     * @param bootStrap
     */
    void setCreateBootstrapConfig(boolean bootStrap) {
        this.createBootstrapConfig = bootStrap;
    }

    /**
     * @return the catalogDAO
     */
    public IGeoServerDAO getCatalogDAO() {
        return catalogDAO;
    }

    /**
     * @param catalogDAO the catalogDAO to set
     */
    public void setCatalogDAO(IGeoServerDAO catalogDAO) {
        this.catalogDAO = catalogDAO;
    }

    // ////////////////////////////////////////////////////////////////////////
    //
    // BOOTSTRAP
    //
    // ////////////////////////////////////////////////////////////////////////
    private GeoServerInfo serviceBootStrap() {
        // ////////////////////////////////////////////////////////////////////
        //
        // Setting up GeoServer global configuration
        //
        // ////////////////////////////////////////////////////////////////////
        /**
         * Default GeoServer instance 
         */
        GeoServerInfo geoserver = createDefaultGeoServer();
        
        // do not call setGlobal or we'll get an infinite loop
        this.catalogDAO.save(geoserver);
        
        /**
         * Default WebService instances 
         */
        createDefaultServices(geoserver);
        
        /**
         * Default NameSpace instance 
         */
        HbNamespaceInfo defaultNameSpace = createDefaultNameSpace();
        
        this.catalogDAO.save(defaultNameSpace);

        /**
         * Default GeoWorkSpace instance 
         */
        HbWorkspaceInfo defaultWs = createDefaultWorkSpace();
        
        this.catalogDAO.save(defaultWs);
        
        // creating the default workspace
        catalog.bootStrap();

        // //
        // Default Raster Style
        // //
        StyleInfo rasterSLD = catalog.getFactory().createStyle();
        rasterSLD.setName("raster");
        rasterSLD.setFilename("raster.sld");
        
        this.catalogDAO.save(rasterSLD);
        
        /**
         * Populating DB with a Default Dataset 
         */
        try {
            createDefaultDataSet(defaultWs, rasterSLD);
        } catch (MismatchedDimensionException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return geoserver;
    }

    /**
     * @return
     */
    private HbWorkspaceInfo createDefaultWorkSpace() {
        HbWorkspaceInfo defaultWs = catalog.getFactory().createWorkspace();
        defaultWs.setDefault(Boolean.TRUE);
        defaultWs.setName("nurc");
        return defaultWs;
    }

    /**
     * @return
     */
    private HbNamespaceInfo createDefaultNameSpace() {
        HbNamespaceInfo defaultNameSpace = catalog.getFactory().createNamespace();
        defaultNameSpace.setDefault(Boolean.TRUE);
        defaultNameSpace.setPrefix("nurc");
        defaultNameSpace.setURI("http://www.nurc.nato.int");
        return defaultNameSpace;
    }

    /**
     * @param geoserver
     */
    private void createDefaultServices(GeoServerInfo geoserver) {
        WFSInfoImpl wfs = new WFSInfoImpl();
        wfs.setId("wfs");
        wfs.setName("wfs");
        wfs.setEnabled(true);

        wfs.setServiceLevel(WFSInfo.ServiceLevel.COMPLETE);

        // gml2
        GMLInfo gml = new GMLInfoImpl();
        gml.setFeatureBounding(Boolean.TRUE);
        gml.setSrsNameStyle(SrsNameStyle.NORMAL);
        wfs.getGML().put(WFSInfo.Version.V_10, gml);

        // gml3
        gml = new GMLInfoImpl();
        gml.setFeatureBounding(true);
        gml.setSrsNameStyle(SrsNameStyle.URN);
        wfs.getGML().put(WFSInfo.Version.V_11, gml);
        wfs.setGeoServer(geoserver);

        add(wfs);

        WMSInfoImpl wms = new WMSInfoImpl();
        wms.setName("wms");
        wms.setEnabled(true);
        WatermarkInfo wm = new WatermarkInfoImpl();
        wm.setEnabled(false);
        wm.setPosition(WatermarkInfo.Position.BOT_RIGHT);
        wms.setWatermark(wm);
        wms.setGeoServer(geoserver);

        add(wms);

        WCSInfoImpl wcs = new WCSInfoImpl();
        wcs.setId("wcs");
        wcs.setName("wcs");
        wcs.setEnabled(true);
        wcs.setGeoServer(geoserver);

        add(wcs);
    }
    
    /**
     * @return
     */
    private GeoServerInfo createDefaultGeoServer() {
        GeoServerInfo geoserver;
        geoserver = getFactory().createGlobal();
        geoserver.setContactInfo(getFactory().createContact());
        
        geoserver.setMaxFeatures(10000);
        geoserver.setNumDecimals(8);
        
        geoserver.setLoggingLevel("DEFAULT_LOGGING.properties");
        geoserver.setLoggingLocation("logs/geoserver.log");
        geoserver.setStdOutLogging(false);

        geoserver.setVerbose(false);
        geoserver.setVerboseExceptions(false);

        //jai
        JAIInfo jai = new JAIInfo();
        jai.setMemoryCapacity( (Double) 0.5);
        jai.setMemoryThreshold( (Double) 0.75 );
        jai.setTileThreads( (Integer) 5 );
        jai.setTilePriority( (Integer) 5 );
        jai.setImageIOCache( (Boolean) false );
        jai.setJPEGAcceleration( (Boolean) false );
        jai.setPNGAcceleration( (Boolean) false );
        jai.setRecycling( (Boolean) false );
        
        geoserver.getMetadata().put( JAIInfo.KEY, jai );
        return geoserver;
    }
    
    /** TEST-DATA **/

    /**
     * @param defaultWs
     * @param rasterSLD 
     * @throws MismatchedDimensionException
     * @throws RuntimeException
     * @throws ParseException 
     */
    private void createDefaultDataSet(final HbWorkspaceInfo defaultWs, final StyleInfo style)
            throws MismatchedDimensionException, RuntimeException, ParseException {
        // //
        // Defining sample models
        // //
        Map<String, ModelInfo> sampleModels = createSampleModels();
        
        try {
            // //
            // Defining sample model-runs
            // //
            /** Model Runs for Mercator **/
            List<ModelRunInfo> sampleMercatorRuns = createSampleMercatorRuns(sampleModels);
                // CoverageStores
                CoverageStoreInfo mercatorPsy2v3R1v_med_mean_20080924_R20080924_STORE = createSampleCoverageStore("ext-mercatorPsy2v3R1v_med_mean_20080924_R20080924", defaultWs, "/Mercator/20080924/", "ext-mercatorPsy2v3R1v_med_mean_20080924_R20080924", "nc", "NetCDF");
                CoverageStoreInfo mercatorPsy2v3R1v_med_mean_20080925_R20080924_STORE = createSampleCoverageStore("ext-mercatorPsy2v3R1v_med_mean_20080925_R20080924", defaultWs, "/Mercator/20080924/", "ext-mercatorPsy2v3R1v_med_mean_20080925_R20080924", "nc", "NetCDF");
                CoverageStoreInfo mercatorPsy2v3R1v_med_mean_20080926_R20080924_STORE = createSampleCoverageStore("ext-mercatorPsy2v3R1v_med_mean_20080926_R20080924", defaultWs, "/Mercator/20080924/", "ext-mercatorPsy2v3R1v_med_mean_20080926_R20080924", "nc", "NetCDF");
                // Coverages
                CoverageInfo mercatorPsy2v3R1v_med_mean_20080924_R20080924 = createSampleCoverage(
                        style, mercatorPsy2v3R1v_med_mean_20080924_R20080924_STORE, sampleMercatorRuns.get(0),
                        "ext-mercatorPsy2v3R1v_med_mean_20080924_R20080924_0", "ext-mercatorPsy2v3R1v_med_mean_20080924_R20080924_0", 
                        new String[] {}, new Double[] {6.875, 12.0, 42.00975799560547, 44.28925704956055}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 300, 300 }, null, 
                        "NetCDF"
                );
                CoverageInfo mercatorPsy2v3R1v_med_mean_20080925_R20080924 = createSampleCoverage(
                        style, mercatorPsy2v3R1v_med_mean_20080925_R20080924_STORE, sampleMercatorRuns.get(1),
                        "ext-mercatorPsy2v3R1v_med_mean_20080925_R20080924_0", "ext-mercatorPsy2v3R1v_med_mean_20080925_R20080924_0", 
                        new String[] {}, new Double[] {6.875, 12.0, 42.00975799560547, 44.28925704956055}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 300, 300 }, null, 
                        "NetCDF"
                );
                CoverageInfo mercatorPsy2v3R1v_med_mean_20080926_R20080924 = createSampleCoverage(
                        style, mercatorPsy2v3R1v_med_mean_20080926_R20080924_STORE, sampleMercatorRuns.get(2),
                        "ext-mercatorPsy2v3R1v_med_mean_20080926_R20080924_0", "ext-mercatorPsy2v3R1v_med_mean_20080926_R20080924_0", 
                        new String[] {}, new Double[] {6.875, 12.0, 42.00975799560547, 44.28925704956055}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 300, 300 }, null, 
                        "NetCDF"
                );

            
            /** Model Runs for NRL-NCOM **/
            List<ModelRunInfo> sampleNRLNCOMRuns20080924 = createSampleNRLNCOMRuns20080924(sampleModels);
                // CoverageStores 20080924
                CoverageStoreInfo salt_m08_nest0_20080924_STORE = createSampleCoverageStore("salt_m08_nest0_20080924", defaultWs, "/NRL/NCOM/20080924/", "salt_m08_nest0_20080924", "nc", "NetCDF");
                CoverageStoreInfo ssh_m08_nest0_20080924_STORE  = createSampleCoverageStore("ssh_m08_nest0_20080924",  defaultWs, "/NRL/NCOM/20080924/", "ssh_m08_nest0_20080924",  "nc", "NetCDF");
                CoverageStoreInfo temp_m08_nest0_20080924_STORE = createSampleCoverageStore("temp_m08_nest0_20080924", defaultWs, "/NRL/NCOM/20080924/", "temp_m08_nest0_20080924", "nc", "NetCDF");
                CoverageStoreInfo uvel_m08_nest0_20080924_STORE = createSampleCoverageStore("uvel_m08_nest0_20080924", defaultWs, "/NRL/NCOM/20080924/", "uvel_m08_nest0_20080924", "nc", "NetCDF");
                CoverageStoreInfo vvel_m08_nest0_20080924_STORE = createSampleCoverageStore("vvel_m08_nest0_20080924", defaultWs, "/NRL/NCOM/20080924/", "vvel_m08_nest0_20080924", "nc", "NetCDF");
                // Coverages 20080924
                CoverageInfo salt_m08_nest0_20080924 = createSampleCoverage(
                        style, salt_m08_nest0_20080924_STORE, sampleNRLNCOMRuns20080924.get(0),
                        "salt_m08_nest0_20080924_0", "salt_m08_nest0_20080924_0", 
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null, 
                        "NetCDF"
                );
                CoverageInfo ssh_m08_nest0_20080924 = createSampleCoverage(
                        style, ssh_m08_nest0_20080924_STORE, sampleNRLNCOMRuns20080924.get(1),
                        "ssh_m08_nest0_20080924_0", "ssh_m08_nest0_20080924_0", 
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null, 
                        "NetCDF"
                );
                CoverageInfo temp_m08_nest0_20080924 = createSampleCoverage(
                        style, temp_m08_nest0_20080924_STORE, sampleNRLNCOMRuns20080924.get(2),
                        "temp_m08_nest0_20080924_0", "temp_m08_nest0_20080924_0", 
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null, 
                        "NetCDF"
                );
                CoverageInfo uvel_m08_nest0_20080924 = createSampleCoverage(
                        style, uvel_m08_nest0_20080924_STORE, sampleNRLNCOMRuns20080924.get(3),
                        "uvel_m08_nest0_20080924_0", "uvel_m08_nest0_20080924_0", 
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null, 
                        "NetCDF"
                );
                CoverageInfo vvel_m08_nest0_20080924 = createSampleCoverage(
                        style, vvel_m08_nest0_20080924_STORE, sampleNRLNCOMRuns20080924.get(4),
                        "vvel_m08_nest0_20080924_0", "vvel_m08_nest0_20080924_0", 
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null, 
                        "NetCDF"
                );

            /** Model Runs for NRL-NCOM **/
            List<ModelRunInfo> sampleNRLNCOMRuns20080925 = createSampleNRLNCOMRuns20080925(sampleModels);
                // CoverageStores 20080925
                CoverageStoreInfo salt_m08_nest0_20080925_STORE = createSampleCoverageStore("salt_m08_nest0_20080925", defaultWs, "/NRL/NCOM/20080925/", "salt_m08_nest0_20080925", "nc", "NetCDF");
                CoverageStoreInfo temp_m08_nest0_20080925_STORE = createSampleCoverageStore("temp_m08_nest0_20080925", defaultWs, "/NRL/NCOM/20080925/", "temp_m08_nest0_20080925", "nc", "NetCDF");
                // Coverages 20080925
                CoverageInfo salt_m08_nest0_20080925 = createSampleCoverage(
                        style, salt_m08_nest0_20080925_STORE, sampleNRLNCOMRuns20080925.get(0),
                        "salt_m08_nest0_20080925_0", "salt_m08_nest0_20080925_0", 
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null, 
                        "NetCDF"
                );
                CoverageInfo temp_m08_nest0_20080925 = createSampleCoverage(
                        style, temp_m08_nest0_20080925_STORE, sampleNRLNCOMRuns20080925.get(1),
                        "temp_m08_nest0_20080925_0", "temp_m08_nest0_20080925_0", 
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null, 
                        "NetCDF"
                );

            /** Model Runs for NRL-SWAN **/
            List<ModelRunInfo> sampleNRLSWANRuns = createSampleNRLSWANRuns(sampleModels);
                // CoverageStores
                CoverageStoreInfo swan_2008092400_STORE = createSampleCoverageStore("SWAN_2008092400", defaultWs, "/NRL/Swan/", "2008092400", "nc", "NetCDF");
                CoverageStoreInfo swan_2008092412_STORE = createSampleCoverageStore("SWAN_2008092412", defaultWs, "/NRL/Swan/", "2008092412", "nc", "NetCDF");
                CoverageStoreInfo swan_2008092500_STORE = createSampleCoverageStore("SWAN_2008092500", defaultWs, "/NRL/Swan/", "2008092500", "nc", "NetCDF");
                // Coverages
                CoverageInfo swan_2008092400 = createSampleCoverage(
                        style, swan_2008092400_STORE, sampleNRLSWANRuns.get(0),
                        "2008092400_0", "2008092400_0", 
                        new String[] {}, new Double[] {6.5, 40.0, 13.5, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 300, 300 }, null, 
                        "NetCDF"
                );
                CoverageInfo swan_2008092412 = createSampleCoverage(
                        style, swan_2008092412_STORE, sampleNRLSWANRuns.get(1),
                        "2008092412_0", "2008092412_0", 
                        new String[] {}, new Double[] {6.5, 40.0, 13.5, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 300, 300 }, null, 
                        "NetCDF"
                );
                CoverageInfo swan_2008092500 = createSampleCoverage(
                        style, swan_2008092500_STORE, sampleNRLSWANRuns.get(2),
                        "2008092500_0", "2008092500_0", 
                        new String[] {}, new Double[] {6.5, 40.0, 13.5, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 300, 300 }, null, 
                        "NetCDF"
                );
            /** Model Runs for INGV-MFS_Sys2b **/
            List<ModelRunInfo> sampleINGVMREAAnalysesRuns = createSampleINGVMREAAnalysesRuns(sampleModels);
                // CoverageStores 
                CoverageStoreInfo INGV_A_MREA08_H_20080924_T_STORE = createSampleCoverageStore("MREA08-H-20080924-T-Analysis", defaultWs, "/INGV/MFS_Sys2b_hourly/Analyses/", "MREA08-H-20080924-T", "nc", "NetCDF");
                CoverageStoreInfo INGV_A_MREA08_H_20080924_U_STORE = createSampleCoverageStore("MREA08-H-20080924-U-Analysis", defaultWs, "/INGV/MFS_Sys2b_hourly/Analyses/", "MREA08-H-20080924-U", "nc", "NetCDF");
                CoverageStoreInfo INGV_A_MREA08_H_20080924_V_STORE = createSampleCoverageStore("MREA08-H-20080924-V-Analysis", defaultWs, "/INGV/MFS_Sys2b_hourly/Analyses/", "MREA08-H-20080924-V", "nc", "NetCDF");
                CoverageStoreInfo INGV_A_MREA08_H_20080925_T_STORE = createSampleCoverageStore("MREA08-H-20080925-T-Analysis", defaultWs, "/INGV/MFS_Sys2b_hourly/Analyses/", "MREA08-H-20080925-T", "nc", "NetCDF");
                CoverageStoreInfo INGV_A_MREA08_H_20080925_U_STORE = createSampleCoverageStore("MREA08-H-20080925-U-Analysis", defaultWs, "/INGV/MFS_Sys2b_hourly/Analyses/", "MREA08-H-20080925-U", "nc", "NetCDF");
                CoverageStoreInfo INGV_A_MREA08_H_20080925_V_STORE = createSampleCoverageStore("MREA08-H-20080925-V-Analysis", defaultWs, "/INGV/MFS_Sys2b_hourly/Analyses/", "MREA08-H-20080925-V", "nc", "NetCDF");
                // Coverages
                CoverageInfo INGV_A_MREA08_H_20080924_T_0 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080924_T_STORE, sampleINGVMREAAnalysesRuns.get(0),
                        "MREA08-H-20080924-T_0-Analysis", "MREA08-H-20080924-T_0", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                CoverageInfo INGV_A_MREA08_H_20080924_T_1 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080924_T_STORE, sampleINGVMREAAnalysesRuns.get(0),
                        "MREA08-H-20080924-T_1-Analysis", "MREA08-H-20080924-T_1", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                
                CoverageInfo INGV_A_MREA08_H_20080924_U_0 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080924_U_STORE, sampleINGVMREAAnalysesRuns.get(1),
                        "MREA08-H-20080924-U_0-Analysis", "MREA08-H-20080924-U_0", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                CoverageInfo INGV_A_MREA08_H_20080924_U_1 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080924_U_STORE, sampleINGVMREAAnalysesRuns.get(1),
                        "MREA08-H-20080924-U_1-Analysis", "MREA08-H-20080924-U_1", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                
                CoverageInfo INGV_A_MREA08_H_20080924_V_0 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080924_V_STORE, sampleINGVMREAAnalysesRuns.get(2),
                        "MREA08-H-20080924-V_0-Analysis", "MREA08-H-20080924-V_0", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                CoverageInfo INGV_A_MREA08_H_20080924_V_1 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080924_V_STORE, sampleINGVMREAAnalysesRuns.get(2),
                        "MREA08-H-20080924-V_1-Analysis", "MREA08-H-20080924-V_1", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                
                
                CoverageInfo INGV_A_MREA08_H_20080925_T_0 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080925_T_STORE, sampleINGVMREAAnalysesRuns.get(3),
                        "MREA08-H-20080925-T_0-Analysis", "MREA08-H-20080925-T_0", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                CoverageInfo INGV_A_MREA08_H_20080925_T_1 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080925_T_STORE, sampleINGVMREAAnalysesRuns.get(3),
                        "MREA08-H-20080925-T_1-Analysis", "MREA08-H-20080925-T_1", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                
                CoverageInfo INGV_A_MREA08_H_20080925_U_0 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080925_U_STORE, sampleINGVMREAAnalysesRuns.get(4),
                        "MREA08-H-20080925-U_0-Analysis", "MREA08-H-20080925-U_0", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                CoverageInfo INGV_A_MREA08_H_20080925_U_1 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080925_U_STORE, sampleINGVMREAAnalysesRuns.get(4),
                        "MREA08-H-20080925-U_1-Analysis", "MREA08-H-20080925-U_1", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                
                CoverageInfo INGV_A_MREA08_H_20080925_V_0 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080925_V_STORE, sampleINGVMREAAnalysesRuns.get(5),
                        "MREA08-H-20080925-V_0-Analysis", "MREA08-H-20080925-V_0", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                CoverageInfo INGV_A_MREA08_H_20080925_V_1 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080925_V_STORE, sampleINGVMREAAnalysesRuns.get(5),
                        "MREA08-H-20080925-V_1-Analysis", "MREA08-H-20080925-V_1", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                
            List<ModelRunInfo> sampleINGVMREASymulationRuns = createSampleINGVMREASimulationRuns(sampleModels);
                // CoverageStores 
                CoverageStoreInfo INGV_S_MREA08_H_20080924_T_STORE = createSampleCoverageStore("MREA08-H-20080924-T-Simulation", defaultWs, "/INGV/MFS_Sys2b_hourly/Simulation/", "MREA08-H-20080924-T", "nc", "NetCDF");
                CoverageStoreInfo INGV_S_MREA08_H_20080924_U_STORE = createSampleCoverageStore("MREA08-H-20080924-U-Simulation", defaultWs, "/INGV/MFS_Sys2b_hourly/Simulation/", "MREA08-H-20080924-U", "nc", "NetCDF");
                CoverageStoreInfo INGV_S_MREA08_H_20080924_V_STORE = createSampleCoverageStore("MREA08-H-20080924-V-Simulation", defaultWs, "/INGV/MFS_Sys2b_hourly/Simulation/", "MREA08-H-20080924-V", "nc", "NetCDF");
                // Coverages
                CoverageInfo INGV_S_MREA08_H_20080924_T_0 = createSampleCoverage(
                        style, INGV_S_MREA08_H_20080924_T_STORE, sampleINGVMREASymulationRuns.get(0),
                        "MREA08-H-20080924-T_0-Simulation", "MREA08-H-20080924-T_0", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                CoverageInfo INGV_S_MREA08_H_20080924_T_1 = createSampleCoverage(
                        style, INGV_S_MREA08_H_20080924_T_STORE, sampleINGVMREASymulationRuns.get(0),
                        "MREA08-H-20080924-T_1-Simulation", "MREA08-H-20080924-T_1", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                
                CoverageInfo INGV_S_MREA08_H_20080924_U_0 = createSampleCoverage(
                        style, INGV_S_MREA08_H_20080924_U_STORE, sampleINGVMREASymulationRuns.get(1),
                        "MREA08-H-20080924-U_0-Simulation", "MREA08-H-20080924-U_0", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                CoverageInfo INGV_S_MREA08_H_20080924_U_1 = createSampleCoverage(
                        style, INGV_S_MREA08_H_20080924_U_STORE, sampleINGVMREASymulationRuns.get(1),
                        "MREA08-H-20080924-U_1-Simulation", "MREA08-H-20080924-U_1", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                
                CoverageInfo INGV_S_MREA08_H_20080924_V_0 = createSampleCoverage(
                        style, INGV_S_MREA08_H_20080924_V_STORE, sampleINGVMREASymulationRuns.get(2),
                        "MREA08-H-20080924-V_0-Simulation", "MREA08-H-20080924-V_0", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
                CoverageInfo INGV_S_MREA08_H_20080924_V_1 = createSampleCoverage(
                        style, INGV_S_MREA08_H_20080924_V_STORE, sampleINGVMREASymulationRuns.get(2),
                        "MREA08-H-20080924-V_1-Simulation", "MREA08-H-20080924-V_1", 
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5}, 
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]", 
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null, 
                        "NetCDF"
                );
            
            // ////
            // Updating Variables
            // ////
            GeophysicParamInfo dynht = createSampleGeoPhysicalParameter("dynht", null);
            linkVariableToCoverages(dynht, new CoverageInfo[] {mercatorPsy2v3R1v_med_mean_20080924_R20080924, mercatorPsy2v3R1v_med_mean_20080925_R20080924, mercatorPsy2v3R1v_med_mean_20080926_R20080924});
            
            GeophysicParamInfo dynhterr = createSampleGeoPhysicalParameter("dynhterr", null);
            linkVariableToCoverages(dynhterr, new CoverageInfo[] {mercatorPsy2v3R1v_med_mean_20080924_R20080924, mercatorPsy2v3R1v_med_mean_20080925_R20080924, mercatorPsy2v3R1v_med_mean_20080926_R20080924});

            GeophysicParamInfo dynhtmean = createSampleGeoPhysicalParameter("dynhtmean", null);
            linkVariableToCoverages(dynhtmean, new CoverageInfo[] {mercatorPsy2v3R1v_med_mean_20080924_R20080924, mercatorPsy2v3R1v_med_mean_20080925_R20080924, mercatorPsy2v3R1v_med_mean_20080926_R20080924});

            GeophysicParamInfo salt = createSampleGeoPhysicalParameter("salt", new String[] {"salinity"});
            linkVariableToCoverages(salt, new CoverageInfo[] {mercatorPsy2v3R1v_med_mean_20080924_R20080924, mercatorPsy2v3R1v_med_mean_20080925_R20080924, mercatorPsy2v3R1v_med_mean_20080926_R20080924, salt_m08_nest0_20080924, salt_m08_nest0_20080925, temp_m08_nest0_20080924, temp_m08_nest0_20080925});

            GeophysicParamInfo ssh = createSampleGeoPhysicalParameter("ssh", new String[] {"surf_el"});
            linkVariableToCoverages(ssh, new CoverageInfo[] {ssh_m08_nest0_20080924});
            
            GeophysicParamInfo salterr = createSampleGeoPhysicalParameter("salterr", null);
            linkVariableToCoverages(salterr, new CoverageInfo[] {mercatorPsy2v3R1v_med_mean_20080924_R20080924, mercatorPsy2v3R1v_med_mean_20080925_R20080924, mercatorPsy2v3R1v_med_mean_20080926_R20080924});

            GeophysicParamInfo saltmean = createSampleGeoPhysicalParameter("saltmean", null);
            linkVariableToCoverages(saltmean, new CoverageInfo[] {mercatorPsy2v3R1v_med_mean_20080924_R20080924, mercatorPsy2v3R1v_med_mean_20080925_R20080924, mercatorPsy2v3R1v_med_mean_20080926_R20080924});

            GeophysicParamInfo temp = createSampleGeoPhysicalParameter("temp", new String[] {"temperature", "water_temp", "votemper"});
            linkVariableToCoverages(temp, new CoverageInfo[] {mercatorPsy2v3R1v_med_mean_20080924_R20080924, mercatorPsy2v3R1v_med_mean_20080925_R20080924, mercatorPsy2v3R1v_med_mean_20080926_R20080924, salt_m08_nest0_20080924, salt_m08_nest0_20080925, temp_m08_nest0_20080924, temp_m08_nest0_20080925, INGV_A_MREA08_H_20080924_T_0, INGV_A_MREA08_H_20080924_T_1, INGV_A_MREA08_H_20080925_T_0, INGV_A_MREA08_H_20080925_T_1, INGV_S_MREA08_H_20080924_T_0, INGV_S_MREA08_H_20080924_T_1});
            
            GeophysicParamInfo temperr = createSampleGeoPhysicalParameter("temperr", null);
            linkVariableToCoverages(temperr, new CoverageInfo[] {mercatorPsy2v3R1v_med_mean_20080924_R20080924, mercatorPsy2v3R1v_med_mean_20080925_R20080924, mercatorPsy2v3R1v_med_mean_20080926_R20080924});
            
            GeophysicParamInfo tempmean = createSampleGeoPhysicalParameter("tempmean", null);
            linkVariableToCoverages(tempmean, new CoverageInfo[] {mercatorPsy2v3R1v_med_mean_20080924_R20080924, mercatorPsy2v3R1v_med_mean_20080925_R20080924, mercatorPsy2v3R1v_med_mean_20080926_R20080924});
            
            GeophysicParamInfo water_u = createSampleGeoPhysicalParameter("u", new String[] {"water_u", "vozocrtx"});
            linkVariableToCoverages(water_u, new CoverageInfo[] {mercatorPsy2v3R1v_med_mean_20080924_R20080924, mercatorPsy2v3R1v_med_mean_20080925_R20080924, mercatorPsy2v3R1v_med_mean_20080926_R20080924, uvel_m08_nest0_20080924, INGV_A_MREA08_H_20080924_U_0, INGV_A_MREA08_H_20080924_U_1, INGV_A_MREA08_H_20080925_U_0, INGV_A_MREA08_H_20080925_U_1, INGV_S_MREA08_H_20080924_U_0, INGV_S_MREA08_H_20080924_U_1});
            
            GeophysicParamInfo water_v = createSampleGeoPhysicalParameter("v", new String[] {"water_v", "vomecrty"});
            linkVariableToCoverages(water_v, new CoverageInfo[] {mercatorPsy2v3R1v_med_mean_20080924_R20080924, mercatorPsy2v3R1v_med_mean_20080925_R20080924, mercatorPsy2v3R1v_med_mean_20080926_R20080924, vvel_m08_nest0_20080924, INGV_A_MREA08_H_20080924_V_0, INGV_A_MREA08_H_20080924_V_1, INGV_A_MREA08_H_20080925_V_0, INGV_A_MREA08_H_20080925_V_1, INGV_S_MREA08_H_20080924_V_0, INGV_S_MREA08_H_20080924_V_1});

            GeophysicParamInfo sig_wav_ht = createSampleGeoPhysicalParameter("sig_wav_ht", null);
            linkVariableToCoverages(sig_wav_ht, new CoverageInfo[] {swan_2008092400, swan_2008092412, swan_2008092500});

            GeophysicParamInfo mean_wav_dir = createSampleGeoPhysicalParameter("mean_wav_dir", null);
            linkVariableToCoverages(mean_wav_dir, new CoverageInfo[] {swan_2008092400, swan_2008092412, swan_2008092500});

            GeophysicParamInfo peak_wav_per = createSampleGeoPhysicalParameter("peak_wav_per", null);
            linkVariableToCoverages(peak_wav_per, new CoverageInfo[] {swan_2008092400, swan_2008092412, swan_2008092500});

            GeophysicParamInfo mean_wav_per = createSampleGeoPhysicalParameter("mean_wav_per", null);
            linkVariableToCoverages(mean_wav_per, new CoverageInfo[] {swan_2008092400, swan_2008092412, swan_2008092500});

        } catch (Exception e) {
            throw new RuntimeException("Error creating bootstrap configuration", e);
        }
    }

    /**
     * 
     * @param geoParam
     * @param coverages
     */
    private void linkVariableToCoverages(GeophysicParamInfo geoParam, CoverageInfo[] coverages) {
        for (CoverageInfo coverage : coverages) {
            List<ModelRunInfo> modelRuns = coverage.getModelRuns();
            
            if (modelRuns != null) {
                for(ModelRunInfo modelRun : modelRuns) {
                    ModelInfo model = modelRun.getModel();
                    
                    /** **/
                    if (model.getGeophysicalParameters() == null)
                        model.setGeophysicalParameters(new ArrayList<GeophysicParamInfo>());
                    
                    if (!model.getGeophysicalParameters().contains(geoParam)) {
                        List<GeophysicParamInfo> modelParams = model.getGeophysicalParameters();
                        modelParams.add(geoParam);
                        model.setGeophysicalParameters(modelParams);
                    }
                    
                    /** **/
                    if (geoParam.getModels() == null)
                        geoParam.setModels(new ArrayList<ModelInfo>());
                    
                    if (!geoParam.getModels().contains(model)) {
                        List<ModelInfo> paramModels = geoParam.getModels(); 
                        paramModels.add(model);
                        geoParam.setModels(paramModels);
                    }
                    
                    /** **/
                    if (modelRun.getGeophysicalParameters() == null)
                        modelRun.setGeophysicalParameters(new ArrayList<GeophysicParamInfo>());
                    
                    if (!modelRun.getGeophysicalParameters().contains(geoParam)) {
                        List<GeophysicParamInfo> modelRunGeoParamas = modelRun.getGeophysicalParameters();
                        modelRunGeoParamas.add(geoParam);
                        modelRun.setGeophysicalParameters(modelRunGeoParamas);
                    }
                    
                    /** **/
                    if (geoParam.getModelRuns() == null)
                        geoParam.setModelRuns(new ArrayList<ModelRunInfo>());
                    
                    if (!geoParam.getModelRuns().contains(modelRun)) {
                        List<ModelRunInfo> paramModelRuns = geoParam.getModelRuns();
                        paramModelRuns.add(modelRun);
                        geoParam.setModelRuns(paramModelRuns);
                    }
                    
                    this.catalogDAO.update(model);
                    this.catalogDAO.update(modelRun);
                }
            }
            
            /** **/
            if (geoParam.getGridCoverages() == null)
                geoParam.setGridCoverages(new ArrayList<CoverageInfo>());
            
            if (!geoParam.getGridCoverages().contains(coverage)) {
                List<CoverageInfo> paramGridCoverages = geoParam.getGridCoverages();
                paramGridCoverages.add(coverage);
                geoParam.setGridCoverages(paramGridCoverages);
            }

            /** **/
            if (coverage.getGeophysicalParameters() == null)
                coverage.setGeophysicalParameters(new ArrayList<GeophysicParamInfo>());
            
            if (!coverage.getGeophysicalParameters().contains(geoParam)) {
                List<GeophysicParamInfo> cvGeoParams = coverage.getGeophysicalParameters();
                cvGeoParams.add(geoParam);
                coverage.setGeophysicalParameters(cvGeoParams);
            }
            
            this.catalogDAO.update(geoParam);
            this.catalogDAO.update(coverage);
        }
    }

    /**
     * @return
     */
    private GeophysicParamInfo createSampleGeoPhysicalParameter(final String paramName, final String[] aliases) {
        GeophysicParamInfo geophysicalParam = new GeophysicParamInfoImpl();
        geophysicalParam.setName(paramName);
        geophysicalParam.setTitle(paramName);
        geophysicalParam.setDescription(paramName);
        
        if (aliases != null) {
            List<String> geophysicalParamAliases = new ArrayList<String>();
            for (String alias : aliases)
                geophysicalParamAliases.add(alias);
            geophysicalParam.setAlias(geophysicalParamAliases);
        }
        
        this.catalogDAO.save(geophysicalParam);
        
        return geophysicalParam;
    }

    /**
     * 
     * @return
     */
    private Map<String, ModelInfo> createSampleModels() {
        Map<String, ModelInfo> testModels = new HashMap<String, ModelInfo>();
        
        // ////////////////////////////////////////////////////////////////////
        //
        // DEFINING MODELS
        //
        // ////////////////////////////////////////////////////////////////////
        /** Mercator **/
        ModelInfoImpl mercator = createSampleModel("MERCATOR" , "NURC", "MilOC", Discipline.OCEAN, DataType.ANALYSYS_AND_FORECAST, "1");
        testModels.put("MERCATOR", mercator);

        /** NRL NCOM **/
        ModelInfoImpl nrl_ncom = createSampleModel("NRL-NCOM" , "NRL", "", Discipline.OCEAN, DataType.ANALYSYS_AND_FORECAST, "1");
        testModels.put("NRL-NCOM", nrl_ncom);

        /** NRL SWAN **/
        ModelInfoImpl nrl_swan = createSampleModel("NRL-SWAN" , "NRL", "", Discipline.OCEAN, DataType.ANALYSYS_AND_FORECAST, "1");
        testModels.put("NRL-SWAN", nrl_swan);

        /** NRL COAMPS **/
        ModelInfoImpl nrl_coamps = createSampleModel("NRL-COAMPS" , "NRL", "", Discipline.METEO, DataType.ANALYSYS_AND_FORECAST, "1");
        testModels.put("NRL-COAMPS", nrl_coamps);

        /** INGV MFS_Sys2b **/
        ModelInfoImpl ingv_MFS_Sys2b = createSampleModel("INGV-MFS_Sys2b" , "INGV", "", Discipline.OCEAN, DataType.ANALYSYS_AND_FORECAST, "1");
        testModels.put("INGV-MFS_Sys2b", ingv_MFS_Sys2b);
        
        /** **/
        return testModels;
    }

    /**
     * 
     * @param modelID
     * @param centerID
     * @param subCenterID
     * @param discipline
     * @param dataType
     * @param version
     * @return
     */
    private ModelInfoImpl createSampleModel(final String modelID, final String centerID, final String subCenterID, final Discipline discipline, final DataType dataType, final String version) {
        ModelInfoImpl model = new ModelInfoImpl();
        model.setAbstract(modelID + " Model.");
        model.setCenter(centerID);
        model.setDescription("This is a " + modelID + " model.");
        model.setDiscipline(discipline);
        model.setId(modelID);
        List keywords = new ArrayList();
            keywords.add(centerID);
            keywords.add(subCenterID);
            keywords.add(modelID);
        model.setKeywords(keywords);
        model.setName(modelID + "-MODEL");
        model.setSubCenter(subCenterID);
        model.setTitle(modelID + " MODEL");
        model.setTypeOfData(dataType);
        model.setVersion(version);
        
        this.catalogDAO.save(model);

        return model;
    }
    
    /**
     * 
     * @param sampleModels
     * @throws ParseException
     */
    private List<ModelRunInfo> createSampleMercatorRuns(Map<String, ModelInfo> sampleModels)
            throws ParseException {
        List<ModelRunInfo> modelRuns = new ArrayList<ModelRunInfo>();
        
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:sssZ");
        
        /** Model Run 0 **/
        ModelRunInfoImpl modelRun_MERCATOR_0 = createSampleModelRun(
                "MERCATOR-" + "2008-09-24T0200000Z" + "-" + "2008-09-24T0200000Z",
                sampleModels.get("MERCATOR"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T02:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-24T02:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 300.0, 300.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.875, 12.0, 42.00975799560547, 44.28925704956055 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                1, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        ); 
        modelRuns.add(modelRun_MERCATOR_0);
        
        /** Model Run 1 **/
        ModelRunInfoImpl modelRun_MERCATOR_1 = createSampleModelRun(
                "MERCATOR-" + "2008-09-25T0200000Z" + "-" + "2008-09-25T0200000Z",
                sampleModels.get("MERCATOR"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-25T02:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-25T02:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 300.0, 300.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.875, 12.0, 42.00975799560547, 44.28925704956055 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                1, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        ); 
        modelRuns.add(modelRun_MERCATOR_1);
        
        /** Model Run 2 **/
        ModelRunInfoImpl modelRun_MERCATOR_2 = createSampleModelRun(
                "MERCATOR-" + "2008-09-26T0200000Z" + "-" + "2008-09-26T0200000Z",
                sampleModels.get("MERCATOR"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-26T02:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-26T02:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 300.0, 300.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.875, 12.0, 42.00975799560547, 44.28925704956055 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                1, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_MERCATOR_2);
        
        // //
        // Updating Model
        // //
        List<ModelRunInfo> runs = new ArrayList<ModelRunInfo>();
        runs.add(modelRun_MERCATOR_0);
        runs.add(modelRun_MERCATOR_1);
        runs.add(modelRun_MERCATOR_2);
        sampleModels.get("MERCATOR").setModelRuns(runs);

        this.catalogDAO.update(sampleModels.get("MERCATOR"));
        
        /** **/
        return modelRuns;
    }
    
    /**
     * 
     * @param sampleModels
     * @throws ParseException
     */
    private List<ModelRunInfo> createSampleNRLNCOMRuns20080924(Map<String, ModelInfo> sampleModels)
            throws ParseException {
        List<ModelRunInfo> modelRuns = new ArrayList<ModelRunInfo>();
        
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:sssZ");
        
        /** Model Run 0 **/
        ModelRunInfoImpl modelRun_NRL_NCOM_0 = createSampleModelRun(
                "NRL-NCOM-" + "2008-09-24T0200000Z" + "-" + "2008-09-26T0200000Z" + "_0",
                sampleModels.get("NRL-NCOM"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T02:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-26T02:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 300.0, 300.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 0.5, 40.5, 14.855536460876465, 44.496002197265625 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                3, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_NRL_NCOM_0);
        
        /** Model Run 1 **/
        ModelRunInfoImpl modelRun_NRL_NCOM_1 = createSampleModelRun(
                "NRL-NCOM-" + "2008-09-24T0200000Z" + "-" + "2008-09-26T0200000Z" + "_1",
                sampleModels.get("NRL-NCOM"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T02:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-26T02:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 300.0, 300.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 0.5, 40.5, 14.855536460876465, 44.496002197265625 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                3, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_NRL_NCOM_1);
        
        /** Model Run 2 **/
        ModelRunInfoImpl modelRun_NRL_NCOM_2 = createSampleModelRun(
                "NRL-NCOM-" + "2008-09-24T0200000Z" + "-" + "2008-09-26T0200000Z" + "_2",
                sampleModels.get("NRL-NCOM"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T02:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-26T02:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 300.0, 300.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 0.5, 40.5, 14.855536460876465, 44.496002197265625 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                3, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_NRL_NCOM_2);
        
        /** Model Run 3 **/
        ModelRunInfoImpl modelRun_NRL_NCOM_3 = createSampleModelRun(
                "NRL-NCOM-" + "2008-09-24T0200000Z" + "-" + "2008-09-26T0200000Z" + "_3",
                sampleModels.get("NRL-NCOM"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T02:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-26T02:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 300.0, 300.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 0.5, 40.5, 14.855536460876465, 44.496002197265625 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                3, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_NRL_NCOM_3);
        
        /** Model Run 4 **/
        ModelRunInfoImpl modelRun_NRL_NCOM_4 = createSampleModelRun(
                "NRL-NCOM-" + "2008-09-24T0200000Z" + "-" + "2008-09-26T0200000Z" + "_4",
                sampleModels.get("NRL-NCOM"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T02:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-26T02:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 300.0, 300.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 0.5, 40.5, 14.855536460876465, 44.496002197265625 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                3, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_NRL_NCOM_4);
        
        // //
        // Updating Model
        // //
        List<ModelRunInfo> runs = new ArrayList<ModelRunInfo>();
        runs.add(modelRun_NRL_NCOM_0);
        runs.add(modelRun_NRL_NCOM_1);
        runs.add(modelRun_NRL_NCOM_2);
        runs.add(modelRun_NRL_NCOM_3);
        runs.add(modelRun_NRL_NCOM_4);
        sampleModels.get("NRL-NCOM").setModelRuns(runs);

        this.catalogDAO.update(sampleModels.get("NRL-NCOM"));
        
        /** **/
        return modelRuns;
    }

    /**
     * 
     * @param sampleModels
     * @throws ParseException
     */
    private List<ModelRunInfo> createSampleNRLNCOMRuns20080925(Map<String, ModelInfo> sampleModels)
            throws ParseException {
        List<ModelRunInfo> modelRuns = new ArrayList<ModelRunInfo>();
        
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:sssZ");
        
        /** Model Run 0 **/
        ModelRunInfoImpl modelRun_NRL_NCOM_0 = createSampleModelRun(
                "NRL-NCOM-" + "2008-09-25T0200000Z" + "-" + "2008-09-27T0200000Z" + "_0",
                sampleModels.get("NRL-NCOM"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-25T02:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-27T02:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 300.0, 300.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 0.5, 40.5, 14.855536460876465, 44.496002197265625 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                3, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_NRL_NCOM_0);
        
        /** Model Run 1 **/
        ModelRunInfoImpl modelRun_NRL_NCOM_1 = createSampleModelRun(
                "NRL-NCOM-" + "2008-09-25T0200000Z" + "-" + "2008-09-27T0200000Z" + "_1",
                sampleModels.get("NRL-NCOM"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-25T02:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-27T02:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 300.0, 300.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 0.5, 40.5, 14.855536460876465, 44.496002197265625 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                3, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_NRL_NCOM_1);
        
        // //
        // Updating Model
        // //
        List<ModelRunInfo> runs = new ArrayList<ModelRunInfo>();
        runs.add(modelRun_NRL_NCOM_0);
        runs.add(modelRun_NRL_NCOM_1);
        sampleModels.get("NRL-NCOM").setModelRuns(runs);

        this.catalogDAO.update(sampleModels.get("NRL-NCOM"));
        
        /** **/
        return modelRuns;
    }

    /**
     * 
     * @param sampleModels
     * @throws ParseException
     */
    private List<ModelRunInfo> createSampleNRLSWANRuns(Map<String, ModelInfo> sampleModels)
            throws ParseException {
        List<ModelRunInfo> modelRuns = new ArrayList<ModelRunInfo>();
        
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:sssZ");
        
        /** Model Run 0 **/
        ModelRunInfoImpl modelRun_NRL_SWAN_0 = createSampleModelRun(
                "NRL-SWAN-" + "2008-09-24T0200000Z" + "-" + "2008-09-24T1500000Z",
                sampleModels.get("NRL-SWAN"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T02:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-24T15:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 300.0, 300.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.5, 40.0, 13.5, 44.5 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "height", // Vertical Coordinate Meaning
                2, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_NRL_SWAN_0);
        
        /** Model Run 1 **/
        ModelRunInfoImpl modelRun_NRL_SWAN_1 = createSampleModelRun(
                "NRL-SWAN-" + "2008-09-24T1400000Z" + "-" + "2008-09-26T1400000Z",
                sampleModels.get("NRL-SWAN"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T14:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-26T14:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 300.0, 300.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.5, 40.0, 13.5, 44.5 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "height", // Vertical Coordinate Meaning
                2, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_NRL_SWAN_1);
        
        /** Model Run 2 **/
        ModelRunInfoImpl modelRun_NRL_SWAN_2 = createSampleModelRun(
                "NRL-SWAN-" + "2008-09-25T0200000Z" + "-" + "2008-09-27T0200000Z",
                sampleModels.get("NRL-SWAN"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-25T02:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-27T02:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 300.0, 300.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.5, 40.0, 13.5, 44.5 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "height", // Vertical Coordinate Meaning
                2, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_NRL_SWAN_2);
        
        // //
        // Updating Model
        // //
        List<ModelRunInfo> runs = new ArrayList<ModelRunInfo>();
        runs.add(modelRun_NRL_SWAN_0);
        runs.add(modelRun_NRL_SWAN_1);
        runs.add(modelRun_NRL_SWAN_2);
        sampleModels.get("NRL-SWAN").setModelRuns(runs);

        this.catalogDAO.update(sampleModels.get("NRL-SWAN"));
        
        /** **/
        return modelRuns;
    }
    
    /**
     * 
     * @param sampleModels
     * @throws ParseException
     */
    private List<ModelRunInfo> createSampleINGVMREAAnalysesRuns(Map<String, ModelInfo> sampleModels)
            throws ParseException {
        List<ModelRunInfo> modelRuns = new ArrayList<ModelRunInfo>();
        
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:sssZ");
        /** Model Run 0 **/
        ModelRunInfoImpl modelRun_INGV_MFS_Sys2b_0 = createSampleModelRun(
                "INGV-MFS_Sys2b-A-" + "2008-09-24T1500000Z" + "-" + "2008-09-25T1400000Z" + "_0",
                sampleModels.get("INGV-MFS_Sys2b"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T15:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-25T14:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 94.0, 45.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.5, 41.75, 12.3125, 44.5 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                1, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_INGV_MFS_Sys2b_0);
        
        /** Model Run 1 **/
        ModelRunInfoImpl modelRun_INGV_MFS_Sys2b_1 = createSampleModelRun(
                "INGV-MFS_Sys2b-A-" + "2008-09-24T1500000Z" + "-" + "2008-09-25T1400000Z" + "_1",
                sampleModels.get("INGV-MFS_Sys2b"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T15:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-25T14:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 94.0, 45.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.5, 41.75, 12.3125, 44.5 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                1, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_INGV_MFS_Sys2b_1);
        
        /** Model Run 2 **/
        ModelRunInfoImpl modelRun_INGV_MFS_Sys2b_2 = createSampleModelRun(
                "INGV-MFS_Sys2b-A-" + "2008-09-24T1500000Z" + "-" + "2008-09-25T1400000Z" + "_2",
                sampleModels.get("INGV-MFS_Sys2b"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T15:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-25T14:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 94.0, 45.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.5, 41.75, 12.3125, 44.5 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                1, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_INGV_MFS_Sys2b_2);
        
        /** Model Run 3 **/
        ModelRunInfoImpl modelRun_INGV_MFS_Sys2b_3 = createSampleModelRun(
                "INGV-MFS_Sys2b-A-" + "2008-09-25T1500000Z" + "-" + "2008-09-26T1400000Z" + "_0",
                sampleModels.get("INGV-MFS_Sys2b"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-25T15:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-26T14:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 94.0, 45.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.5, 41.75, 12.3125, 44.5 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                1, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_INGV_MFS_Sys2b_3);
        
        /** Model Run 4 **/
        ModelRunInfoImpl modelRun_INGV_MFS_Sys2b_4 = createSampleModelRun(
                "INGV-MFS_Sys2b-A-" + "2008-09-25T1500000Z" + "-" + "2008-09-26T1400000Z" + "_1",
                sampleModels.get("INGV-MFS_Sys2b"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-25T15:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-26T14:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 94.0, 45.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.5, 41.75, 12.3125, 44.5 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                1, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_INGV_MFS_Sys2b_4);

        /** Model Run 5 **/
        ModelRunInfoImpl modelRun_INGV_MFS_Sys2b_5 = createSampleModelRun(
                "INGV-MFS_Sys2b-A-" + "2008-09-25T1500000Z" + "-" + "2008-09-26T1400000Z" + "_2",
                sampleModels.get("INGV-MFS_Sys2b"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-25T15:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-26T14:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 94.0, 45.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.5, 41.75, 12.3125, 44.5 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                1, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_INGV_MFS_Sys2b_5);

        // //
        // Updating Model
        // //
        List<ModelRunInfo> runs = new ArrayList<ModelRunInfo>();
        runs.add(modelRun_INGV_MFS_Sys2b_0);
        runs.add(modelRun_INGV_MFS_Sys2b_1);
        runs.add(modelRun_INGV_MFS_Sys2b_2);
        runs.add(modelRun_INGV_MFS_Sys2b_3);
        runs.add(modelRun_INGV_MFS_Sys2b_4);
        runs.add(modelRun_INGV_MFS_Sys2b_5);
        sampleModels.get("INGV-MFS_Sys2b").setModelRuns(runs);

        this.catalogDAO.update(sampleModels.get("INGV-MFS_Sys2b"));
        
        /** **/
        return modelRuns;
    }
    
    /**
     * 
     * @param sampleModels
     * @throws ParseException
     */
    private List<ModelRunInfo> createSampleINGVMREASimulationRuns(Map<String, ModelInfo> sampleModels)
            throws ParseException {
        List<ModelRunInfo> modelRuns = new ArrayList<ModelRunInfo>();
        
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:sssZ");
        /** Model Run 0 **/
        ModelRunInfoImpl modelRun_INGV_MFS_Sys2b_0 = createSampleModelRun(
                "INGV-MFS_Sys2b-S-" + "2008-09-24T1500000Z" + "-" + "2008-09-25T1400000Z" + "_0",
                sampleModels.get("INGV-MFS_Sys2b"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T15:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-25T14:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 94.0, 45.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.5, 41.75, 12.3125, 44.5 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                1, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_INGV_MFS_Sys2b_0);
        
        /** Model Run 1 **/
        ModelRunInfoImpl modelRun_INGV_MFS_Sys2b_1 = createSampleModelRun(
                "INGV-MFS_Sys2b-S-" + "2008-09-24T1500000Z" + "-" + "2008-09-25T1400000Z" + "_1",
                sampleModels.get("INGV-MFS_Sys2b"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T15:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-25T14:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 94.0, 45.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.5, 41.75, 12.3125, 44.5 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                1, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_INGV_MFS_Sys2b_1);
        
        /** Model Run 2 **/
        ModelRunInfoImpl modelRun_INGV_MFS_Sys2b_2 = createSampleModelRun(
                "INGV-MFS_Sys2b-S-" + "2008-09-24T1500000Z" + "-" + "2008-09-25T1400000Z" + "_2",
                sampleModels.get("INGV-MFS_Sys2b"), // MODEL
                new String[] {}, // KeyWords
                sdf.parse("2008-09-24T15:00:000+0200"), // Execution Time 
                sdf.parse("2008-09-25T14:00:000+0200"), // Base Time
                new Double[] { 0.0, 0.0 }, // Grid Lowers
                new Double[] { 94.0, 45.0 }, // Grid Uppers
                new Double[] { 0.0, 0.0 }, // Grid Origin
                new Double[] { 1.0, 1.0 }, // Grid Offsets 
                new Double[] { 6.5, 41.75, 12.3125, 44.5 }, // Envelope
                DefaultGeographicCRS.WGS84, // CRS
                "depth", // Vertical Coordinate Meaning
                1, // Number of TAUs
                1, // TAU
                "day" // TAU Unit of Measure
        );
        modelRuns.add(modelRun_INGV_MFS_Sys2b_2);
        
        // //
        // Updating Model
        // //
        List<ModelRunInfo> runs = new ArrayList<ModelRunInfo>();
        runs.add(modelRun_INGV_MFS_Sys2b_0);
        runs.add(modelRun_INGV_MFS_Sys2b_1);
        runs.add(modelRun_INGV_MFS_Sys2b_2);
        sampleModels.get("INGV-MFS_Sys2b").setModelRuns(runs);

        this.catalogDAO.update(sampleModels.get("INGV-MFS_Sys2b"));
        
        /** **/
        return modelRuns;
    }
    
    /**
     * 
     * @param model
     * @param keywords
     * @param executionTime
     * @param baseTime
     * @param gridLowers
     * @param gridUppers
     * @param gridOrigin
     * @param gridOffsets
     * @param envelope
     * @param crs
     * @param verticalCoordinateMeaning
     * @param numTAU
     * @param TAU
     * @param TAUUnit
     * @return
     */
    private ModelRunInfoImpl createSampleModelRun(
            final String modelRunID,
            final ModelInfo model, final String[] keywords, 
            final Date executionTime, final Date baseTime, 
            final Double[] gridLowers, final Double[] gridUppers, final Double[] gridOrigin, final Double[] gridOffsets, 
            final Double[] envelope, final DefaultGeographicCRS crs, final String verticalCoordinateMeaning, 
            final int numTAU, final int TAU, final String TAUUnit) {
        ModelRunInfoImpl modelRun = new ModelRunInfoImpl();
        modelRun.setDescription(modelRunID + " is a Sample " + model.getName() + " Model Run.");
        modelRun.setExecutionTime(executionTime);
        modelRun.setBaseTime(baseTime);
        modelRun.setId(modelRunID);
        modelRun.setKeywords(Arrays.asList(keywords));
        modelRun.setModel(model);
        modelRun.setName(modelRunID);
        modelRun.setNumTAU(numTAU);
        modelRun.setCRS(crs);
        modelRun.setGridCRS("Grid:2D");
        modelRun.setGridCS("Grid:square2D");
        modelRun.setGridLowers(gridLowers);
        modelRun.setGridOffsets(gridOffsets);
        modelRun.setGridOrigin(gridOrigin);
        modelRun.setGridType("Grid:square2dIn2dCRS");
        modelRun.setGridUppers(gridUppers);
        modelRun.setVerticalCoordinateMeaning(verticalCoordinateMeaning);
        modelRun.setOutline(new ReferencedEnvelope(new Envelope(envelope[0], envelope[1], envelope[2], envelope[3]), crs));
        modelRun.setTAU(TAU);
        modelRun.setTAUunit(TAUUnit);
        modelRun.setUpdateSequence("0");

        this.catalogDAO.save(modelRun);

        return modelRun;
    }

    /**
     * 
     * @param defaultWs
     * @param coverageName
     * @param coverageExt
     * @param driverType
     * @return
     */
    private CoverageStoreInfo createSampleCoverageStore(final String coverageStoreName, final HbWorkspaceInfo defaultWs, final String coveragePath, final String coverageName, final String coverageExt, final String driverType) {
        CoverageStoreInfo coverageStore = catalog.getFactory().createCoverageStore();
        coverageStore.setDescription(coverageStoreName);
        coverageStore.setEnabled(true);
        coverageStore.setName(coverageStoreName);
        coverageStore.setType(driverType);
        coverageStore.setURL("file:coverages" + (coveragePath != null ? coveragePath : "/") + coverageName + "." + coverageExt);
        coverageStore.setWorkspace(defaultWs);

        this.catalogDAO.save(coverageStore);
        return coverageStore;
    }

    /**
     * 
     * @param style
     * @param coverageStore
     * @param modelRunInfo 
     * @param coverageName
     * @param coverageNativeName
     * @param keywords
     * @param envelope
     * @param srsName
     * @param nativeWKT
     * @param gridLow
     * @param gridHigh
     * @param geoTransform
     * @param nativeFormat
     * @return
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws IndexOutOfBoundsException
     * @throws TransformException
     * @throws IllegalArgumentException
     * @throws MalformedURLException
     * @throws IOException
     */
    private CoverageInfo createSampleCoverage(final StyleInfo style, final CoverageStoreInfo coverageStore, ModelRunInfo modelRun, final String coverageName, final String coverageNativeName, final String[] keywords, final Double[] envelope, final String srsName, final String nativeWKT, final int[] gridLow, final int[] gridHigh, final Map<String, Double> geoTransform, final String nativeFormat) 
            throws FactoryException, MismatchedDimensionException, IndexOutOfBoundsException, TransformException, IllegalArgumentException, MalformedURLException, IOException 
    {
        CoverageInfo coverage = catalog.getFactory().createCoverage();
        coverage.setStore(coverageStore);
        coverage.setName(coverageName);
        coverage.setNativeName(coverageNativeName);
        coverage.setTitle(coverageName);
        coverage.setDescription(coverageName + " is an example Test Coverage.");
        coverage.getKeywords().addAll(Arrays.asList(keywords));
        String userDefinedCrsIdentifier = srsName;
        String nativeCrsWkt = nativeWKT;
        coverage.setSRS(userDefinedCrsIdentifier);
        CoordinateReferenceSystem crs = CRS.parseWKT(nativeCrsWkt);
        coverage.setNativeCRS(crs);
        ReferencedEnvelope bounds = 
            new ReferencedEnvelope(
                envelope[0],
                envelope[1], 
                envelope[2], 
                envelope[3], 
                crs
            );
        coverage.setNativeBoundingBox(bounds);
        GeneralEnvelope boundsLatLon = CoverageStoreUtils.getWGS84LonLatEnvelope(new GeneralEnvelope(bounds));
        coverage.setLatLonBoundingBox(new ReferencedEnvelope(boundsLatLon));
        GeneralEnvelope gridEnvelope = new GeneralEnvelope(bounds);
        GeneralGridRange range = new GeneralGridRange(gridLow, gridHigh);
        if (geoTransform != null) {
            double[] matrix = new double[3 * 3];
            matrix[0] = geoTransform.get("scaleX") != null ? geoTransform.get("scaleX") : matrix[0];
            matrix[1] = geoTransform.get("shearX") != null ? geoTransform.get("shearX") : matrix[1];
            matrix[2] = geoTransform.get("translateX") != null ? geoTransform.get("translateX") : matrix[2];
            matrix[3] = geoTransform.get("shearY") != null ? geoTransform.get("shearY") : matrix[3];
            matrix[4] = geoTransform.get("scaleY") != null ? geoTransform.get("scaleY") : matrix[4];
            matrix[5] = geoTransform.get("translateY") != null ? geoTransform.get("translateY") : matrix[5];
            matrix[8] = 1.0;
            MathTransform gridToCRS = new DefaultMathTransformFactory().createAffineTransform(new GeneralMatrix(3, 3, matrix));
            coverage.setGrid(new GridGeometry2D(range, gridToCRS, crs));
        } else {
            coverage.setGrid(new GridGeometry2D(range, gridEnvelope));
        }
        Driver driver = coverageStore.getDriver();
        Map params = new HashMap();
        params.put("url", GeoserverDataDirectory.findDataFile(coverageStore.getURL()).toURI().toURL());
        CoverageAccess cvAccess_0 = driver.connect(params, null, null);
        if (cvAccess_0 != null) {
            CoverageSource cvSource = cvAccess_0.access(new NameImpl(coverage.getNativeName()), null, AccessType.READ_ONLY, null, null);
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
        coverage.setNativeFormat(nativeFormat);
        coverage.getSupportedFormats().addAll(Arrays.asList(new String[] { "GeoTIFF" }));
        coverage.setDefaultInterpolationMethod("nearest neighbor");
        coverage.getInterpolationMethods().addAll(Arrays.asList(new String[] { "nearest neighbor", "bilinear", "bicubic" }));
        coverage.getRequestSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
        coverage.getResponseSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
        coverage.setEnabled(coverageStore.isEnabled());
        coverage.setNamespace(this.catalogDAO.getDefaultNamespace());
        coverage.getMetadata().put("dirName", coverageStore.getName() + "_" + coverage.getName());

        this.catalogDAO.save(coverage);
                    
        LayerInfo layer = catalog.getFactory().createLayer();
        layer.setDefaultStyle(style);
        layer.setEnabled(coverage.isEnabled());
        //layer.setLegend(legend);
        layer.setName(coverage.getName());
        layer.setPath("/");
        layer.setResource(coverage);
        layer.setType(Type.RASTER);
        
        this.catalogDAO.save(layer);
        
        // //
        // Updating Model Runs
        // //
        List<CoverageInfo> coverages = new ArrayList<CoverageInfo>();
        coverages.add(coverage);
        if (modelRun.getGridCoverages() != null)
            coverages.addAll(modelRun.getGridCoverages());
        modelRun.setGridCoverages(coverages);

        this.catalogDAO.update(modelRun);
        
        // //
        // Updating Coverage
        // //
        List<ModelRunInfo> modelRuns = new ArrayList<ModelRunInfo>();
        modelRuns.add(modelRun);
        if (coverage.getModelRuns() != null)
            modelRuns.addAll(coverage.getModelRuns());
        coverage.setModelRuns(modelRuns);

        this.catalogDAO.update(coverage);
        
        
        return coverage;
    }
}
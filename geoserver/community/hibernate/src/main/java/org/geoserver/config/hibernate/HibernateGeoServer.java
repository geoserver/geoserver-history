package org.geoserver.config.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.ModelRunInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.ModelInfo.DataType;
import org.geoserver.catalog.ModelInfo.Discipline;
import org.geoserver.catalog.hibernate.HibernateCatalog;
import org.geoserver.catalog.impl.CoverageInfoImpl;
import org.geoserver.catalog.impl.CoverageStoreInfoImpl;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.global.GeoserverDataDirectory;

import com.vividsolutions.jts.geom.Envelope;

public class HibernateGeoServer implements GeoServer {

    /**
     * factory to create config objects.
     */
    GeoServerFactory factory = new GeoServerFactoryImpl();

    HibernateCatalog catalog;

    List<ConfigurationListener> listeners = new ArrayList<ConfigurationListener>();

    /**
     * hibernate session factory
     */
    SessionFactory sessionFactory;

    private Session session;

    private boolean createBootstrapConfig;

    public HibernateGeoServer() {
        createBootstrapConfig = true;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = (HibernateCatalog) catalog;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        if (this.session == null) {
            if (sessionFactory.getCurrentSession().isOpen()) {
                this.session = sessionFactory.getCurrentSession();
            } else {
                this.session = sessionFactory.openSession();
            }
        } else if (!this.session.isOpen()) {
            this.session = sessionFactory.openSession();
        }

        if (!this.session.getTransaction().isActive())
            this.session.beginTransaction();

        return this.session;
    }

    public GeoServerFactory getFactory() {
        return factory;
    }

    public void setFactory(GeoServerFactory factory) {
        this.factory = factory;
    }

    public GeoServerInfo getGlobal() {
        Session session = getSession();
        session.clear();
        Iterator i = session.createQuery("from " + GeoServerInfoImpl.class.getName()).iterate();
        GeoServerInfo geoserver;
        if (i.hasNext()) {
            geoserver = (GeoServerInfo) i.next();
        } else {
            if (createBootstrapConfig) {
                // this is an empty configuration! create the minimal set of required object
                geoserver = serviceBootStrap();
                catalog.bootStrap();
            } else {
                geoserver = null;
            }
        }

        return geoserver;
    }

    private GeoServerInfo serviceBootStrap() {
        GeoServerInfo geoserver;
        geoserver = getFactory().createGlobal();
        geoserver.setContactInfo(getFactory().createContact());
        Map<String, Serializable> tmp = geoserver.getMetadata();
        // do not call setGlobal or we'll get an infinite loop
        getSession().save(geoserver);

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
        

        /** TEST-DATA **/

        ModelInfoImpl model = new ModelInfoImpl();
        model.setAbstract("Abstract test.");
        model.setCenter("NURC");
        model.setCRS(DefaultGeographicCRS.WGS84);
        model.setDescription("This is a test model.");
        model.setDiscipline(Discipline.OCEAN);
        model.setGridCRS("Grid:2D");
        model.setGridCS("Grid:square2D");
        model.setGridLowers(new Double[] {0.0, 0.0});
        model.setGridOffsets(new Double[] {1.0, 1.0});
        model.setGridOrigin(new Double[] {0.0, 0.0});
        model.setGridType("Grid:square2dIn2dCRS");
        model.setGridUppers(new Double[] {300.0, 300.0});
        model.setId("TEST");
        List keywords = new ArrayList();
        keywords.add("NURC");
        keywords.add("MilOC");
        keywords.add("TEST");
        model.setKeywords(keywords);
        model.setName("TEST-OCEAN-MODEL");
        model.setSubCenter("MilOC");
        model.setTitle("TEST OCEAN MODEL");
        model.setTypeOfData(DataType.ANALYSYS_AND_FORECAST);
        model.setVersion("1");
        model.setVerticalCoordinateMeaning("depth");

        catalog.add(model);

        ModelRunInfoImpl modelRun = new ModelRunInfoImpl();
        modelRun.setBaseTime(new Date());
        modelRun.setDescription("A Test Model Run.");
        modelRun.setExecutionTime(new Date());
        modelRun.setId("TEST-RUN");
        modelRun.setKeywords(keywords);
        modelRun.setModel(model);
        modelRun.setName("TEST-RUN-001");
        modelRun.setNumTAU(6);
        modelRun.setOutline(new ReferencedEnvelope(new Envelope(-24.0,-1.0,32.0,45.0), DefaultGeographicCRS.WGS84));
        modelRun.setTAU(3);
        modelRun.setTAUunit("s");
        modelRun.setUpdateSequence("0");
        
        catalog.add(modelRun);
        
        List<ModelRunInfo> runs = new ArrayList<ModelRunInfo>();
        runs.add(modelRun);
        model.setModelRuns(runs);
        
        catalog.save(model);
        
        try {
                // creating the default workspace
                catalog.bootStrap();
                
                CoverageStoreInfo coverageStore = catalog.getFactory().createCoverageStore();
                coverageStore.setDescription("Example Test Coverage.");
                coverageStore.setEnabled(false);
                coverageStore.setName("TEST-COVERAGE-STORE");
                coverageStore.setType("NetCDF");
                coverageStore.setURL(getClass().getResource("updatedoagCF.nc").toURI().toString());
                
                catalog.add(coverageStore);
                
                CoverageInfo coverage = catalog.getFactory().createCoverage();
                coverage.setStore(coverageStore);
                coverage.setName("updatedoagCF_0");
                coverage.setNativeName("updatedoagCF_0");
                coverage.setTitle("TEST-COVERAGE-001");
                coverage.setDescription("An Example Test Coverage.");
                coverage.getKeywords().addAll(keywords);
                Map<String, Object> envelope = new HashMap<String, Object>();
                    envelope.put("x1", -24.0);
                    envelope.put("x2", -1.0);
                    envelope.put("y1", 32.0);
                    envelope.put("y2", 45.0);
                    envelope.put("srsName", "EPSG:4326");
                    envelope.put("crs", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]");
                String userDefinedCrsIdentifier = (String) envelope.get("srsName");
                String nativeCrsWkt = (String) envelope.get("crs");
                coverage.setSRS(userDefinedCrsIdentifier);
                CoordinateReferenceSystem crs = CRS.parseWKT(nativeCrsWkt);
                coverage.setNativeCRS( crs );
                ReferencedEnvelope bounds = new ReferencedEnvelope( 
                    (Double) envelope.get( "x1" ), (Double) envelope.get( "x2" ), 
                    (Double) envelope.get( "y1" ), (Double) envelope.get( "y2" ), 
                    crs
                );
                coverage.setNativeBoundingBox(bounds);
                GeneralEnvelope boundsLatLon = CoverageStoreUtils.getWGS84LonLatEnvelope(new GeneralEnvelope( bounds ) ); 
                coverage.setLatLonBoundingBox(new ReferencedEnvelope( boundsLatLon ) );
                GeneralEnvelope gridEnvelope = new GeneralEnvelope( bounds );
                Map grid = new HashMap();
                    grid.put("low", new int[] {0, 0});
                    grid.put("high", new int[] {300, 300});
                if ( grid != null ) {
                    int[] low = (int[]) grid.get("low");
                    int[] high = (int[]) grid.get("high");
                    GeneralGridRange range = new GeneralGridRange(low, high);
                    Map<String, Double> tx = (Map<String, Double>) grid.get("geoTransform");
                    if ( tx != null ) {
                        double[] matrix = new double[3 * 3];
                        matrix[0] = tx.get( "scaleX") != null ? tx.get( "scaleX") : matrix[0];
                        matrix[1] = tx.get( "shearX") != null ? tx.get( "shearX") : matrix[1];
                        matrix[2] = tx.get( "translateX") != null ? tx.get( "translateX") : matrix[2];
                        matrix[3] = tx.get( "shearY") != null ? tx.get( "shearY") : matrix[3];
                        matrix[4] = tx.get( "scaleY") != null ? tx.get( "scaleY") : matrix[4];
                        matrix[5] = tx.get( "translateY") != null ? tx.get( "translateY") : matrix[5];
                        matrix[8] = 1.0;
                        MathTransform gridToCRS = new DefaultMathTransformFactory().createAffineTransform( new GeneralMatrix(3,3,matrix));
                        coverage.setGrid( new GridGeometry2D(range,gridToCRS,crs) );
                    }
                    else {
                        coverage.setGrid( new GridGeometry2D( range, gridEnvelope ) );
                    }
                }
                else {
                    // new grid range
                    GeneralGridRange range = new GeneralGridRange(new int[] { 0, 0 }, new int[] { 1, 1 });
                    coverage.setGrid( new GridGeometry2D(range, gridEnvelope) );
                }
                Driver driver = coverageStore.getDriver();
                Map params = new HashMap();
                params.put("url", GeoserverDataDirectory.findDataFile(coverageStore.getURL()).toURI().toURL());
                CoverageAccess cvAccess = driver.connect(params, null, null);
                if (cvAccess != null) {
                    CoverageSource cvSource = cvAccess.access(new NameImpl(coverage.getName()), null, AccessType.READ_ONLY, null, null);
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
                coverage.setNativeFormat("NetCDF");
                coverage.getSupportedFormats().addAll(Arrays.asList(new String[] {"GeoTIFF"}));
                coverage.setDefaultInterpolationMethod("nearest neighbour");
                coverage.getInterpolationMethods().addAll(Arrays.asList(new String[] {"bilinear", "bicubic"}));
                coverage.getRequestSRS().addAll(Arrays.asList(new String[] {"EPSG:4326"}));
                coverage.getResponseSRS().addAll(Arrays.asList(new String[] {"EPSG:4326"}));
                coverage.setEnabled(coverageStore.isEnabled());
                //parameters
                //coverage.getParameters().putAll(cInfoReader.parameters());
                // link to namespace
                String prefix = coverageStore.getWorkspace().getId();
                coverage.setNamespace(catalog.getNamespaceByPrefix(prefix));
                
                catalog.add(coverage);
                
                List<CoverageInfo> coverages = new ArrayList<CoverageInfo>();
                coverages.add(coverage);
                modelRun.setGridCoverages(coverages);
                
                catalog.save(modelRun);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return geoserver;
    }

    public void setGlobal(GeoServerInfo configuration) {
        // GeoServerInfo current = getGlobal();
        // if (true || current == null || current.equals(configuration)) {
        // current = configuration;
        // } else {
        // try {
        // String id = current.getId();
        // //((GeoServerInfoImpl) configuration).setId(id);
        // PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        // propertyUtilsBean.copyProperties(current, configuration);
        // } catch (Exception e) {
        // throw new RuntimeException(e);
        // }
        // }
        //
        Session session = getSession();
        if (configuration.getId() == null) {
            session.save(configuration);
        } else {
            // use merge, the argument instance may be from another session
            session.merge(configuration);
        }
        session.getTransaction().commit();
    }

    public void save(GeoServerInfo geoServer) {
        setGlobal(geoServer);
        // getSession().update(geoServer);
        // // getSession().flush();
        // getSession().getTransaction().commit();
    }

    public void add(ServiceInfo service) {
        if (service.getGeoServer() == null)
            service.setGeoServer(getGlobal());
        if (getService(service.getId()) == null)
            getSession().save(service);
        else
            getSession().update(service);
        // getSession().flush();
        getSession().getTransaction().commit();
    }

    public void remove(ServiceInfo service) {
        getSession().delete(service);
        // getSession().flush();
        getSession().getTransaction().commit();
    }

    public void save(ServiceInfo service) {
        Session session = getSession();
        String id = service.getId();

        // use merge, the argument instance may be from another session
        session.merge(service);
        session.getTransaction().commit();
    }

    public Collection<? extends ServiceInfo> getServices() {
        return getServices(ServiceInfo.class);
    }

    public Collection<? extends ServiceInfo> getServices(Class<?> clazz) {
        List list = getSession().createQuery("from " + clazz.getName()).list();
        return list;
    }

    public <T extends ServiceInfo> T getService(Class<T> clazz) {
        for (ServiceInfo si : getServices(clazz)) {
            if (clazz.isAssignableFrom(si.getClass())) {
                return (T) si;
            }
        }

        return null;
    }

    public ServiceInfo getService(String id) {
        return getService(id, ServiceInfo.class);
    }

    public <T extends ServiceInfo> T getService(String id, Class<T> clazz) {
        Iterator i = getSession().createQuery(
                "from " + clazz.getName() + " where id = '" + id + "'").iterate();
        if (i.hasNext()) {
            T service = (T) i.next();
            return service;
        }

        return null;
    }

    public ServiceInfo getServiceByName(String name) {
        return getServiceByName(name, ServiceInfo.class);
    }

    public <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz) {
        Iterator i = getSession().createQuery(
                "from " + clazz.getName() + " where name = '" + name + "'").iterate();
        if (i.hasNext()) {
            T service = (T) i.next();
            return service;
        }

        return null;
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

        getSession().close();
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

}
package org.geoserver.config.hibernate;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ModelRunInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.LayerInfo.Type;
import org.geoserver.catalog.ModelInfo.DataType;
import org.geoserver.catalog.ModelInfo.Discipline;
import org.geoserver.catalog.hibernate.HbNamespaceInfo;
import org.geoserver.catalog.hibernate.HibernateCatalog;
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
import org.geoserver.hibernate.GeoServerDAO;
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
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
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
    private GeoServerDAO gsDAO;

    /**
     * 
     */
    private HibernateGeoServer() {
        createBootstrapConfig = true;
    }

    public HibernateGeoServer(GeoServerDAO gsDAO) {
        this();
        this.gsDAO = gsDAO;
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
        Iterator i = this.gsDAO.createQuery("from " + GeoServerInfoImpl.class.getName()).iterate();
        GeoServerInfo geoserver;
        if (i.hasNext()) {
            geoserver = (GeoServerInfo) i.next();
        } else {
            if (createBootstrapConfig) {
                // this is an empty configuration! create the minimal set of required object
                LOGGER
                        .info("Creating geoserver bootstrap configuration, no prior configuration found on the database");
                geoserver = serviceBootStrap();
                // catalog.bootStrap();
            } else {
                LOGGER.info("Explicitly asked to skip boot strap configuration, database is empty");
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
        this.gsDAO.save(geoserver);
        this.gsDAO.commit();

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
        model.setGridLowers(new Double[] { 0.0, 0.0 });
        model.setGridOffsets(new Double[] { 1.0, 1.0 });
        model.setGridOrigin(new Double[] { 0.0, 0.0 });
        model.setGridType("Grid:square2dIn2dCRS");
        model.setGridUppers(new Double[] { 300.0, 300.0 });
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:sssZ");
        ModelRunInfoImpl modelRun = new ModelRunInfoImpl();
        modelRun.setDescription("A Test Model Run.");
        try {
            modelRun.setExecutionTime(sdf.parse("2001-05-25T02:00:000+0200"));
            modelRun.setBaseTime(sdf.parse("2001-05-25T02:00:000+0200"));
        } catch (ParseException e1) {
            modelRun.setExecutionTime(new Date());
            modelRun.setBaseTime(new Date());
        }
        modelRun.setId("TEST-RUN");
        modelRun.setKeywords(keywords);
        modelRun.setModel(model);
        modelRun.setName("TEST-RUN-001");
        modelRun.setNumTAU(1);
        modelRun.setOutline(new ReferencedEnvelope(new Envelope(27.40437126159668, 32.995628356933594, 41.01088333129883, 42.589115142822266), DefaultGeographicCRS.WGS84));
        modelRun.setTAU(1);
        modelRun.setTAUunit("day");
        modelRun.setUpdateSequence("0");

        catalog.add(modelRun);

        List<ModelRunInfo> runs = new ArrayList<ModelRunInfo>();
        runs.add(modelRun);
        model.setModelRuns(runs);

        catalog.save(model);

        // creating the default workspace
        catalog.bootStrap();

        try {
            CoverageStoreInfo coverageStore = catalog.getFactory().createCoverageStore();
            coverageStore.setDescription("Example Test Coverage.");
            coverageStore.setEnabled(true);
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
            envelope.put("x1", 27.40437126159668);
            envelope.put("x2", 32.995628356933594);
            envelope.put("y1", 41.01088333129883);
            envelope.put("y2", 42.589115142822266);
            envelope.put("srsName", "EPSG:4326");
            envelope.put("crs", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]");
            String userDefinedCrsIdentifier = (String) envelope.get("srsName");
            String nativeCrsWkt = (String) envelope.get("crs");
            coverage.setSRS(userDefinedCrsIdentifier);
            CoordinateReferenceSystem crs = CRS.parseWKT(nativeCrsWkt);
            coverage.setNativeCRS(crs);
            ReferencedEnvelope bounds = 
                new ReferencedEnvelope(
                    (Double) envelope.get("x1"),
                    (Double) envelope.get("x2"), 
                    (Double) envelope.get("y1"), 
                    (Double) envelope.get("y2"), 
                    crs
                );
            coverage.setNativeBoundingBox(bounds);
            GeneralEnvelope boundsLatLon = CoverageStoreUtils.getWGS84LonLatEnvelope(new GeneralEnvelope(bounds));
            coverage.setLatLonBoundingBox(new ReferencedEnvelope(boundsLatLon));
            GeneralEnvelope gridEnvelope = new GeneralEnvelope(bounds);
            Map grid = new HashMap();
            grid.put("low", new int[] { 0, 0 });
            grid.put("high", new int[] { 300, 300 });
            if (grid != null) {
                int[] low = (int[]) grid.get("low");
                int[] high = (int[]) grid.get("high");
                GeneralGridRange range = new GeneralGridRange(low, high);
                Map<String, Double> tx = (Map<String, Double>) grid.get("geoTransform");
                if (tx != null) {
                    double[] matrix = new double[3 * 3];
                    matrix[0] = tx.get("scaleX") != null ? tx.get("scaleX") : matrix[0];
                    matrix[1] = tx.get("shearX") != null ? tx.get("shearX") : matrix[1];
                    matrix[2] = tx.get("translateX") != null ? tx.get("translateX") : matrix[2];
                    matrix[3] = tx.get("shearY") != null ? tx.get("shearY") : matrix[3];
                    matrix[4] = tx.get("scaleY") != null ? tx.get("scaleY") : matrix[4];
                    matrix[5] = tx.get("translateY") != null ? tx.get("translateY") : matrix[5];
                    matrix[8] = 1.0;
                    MathTransform gridToCRS = new DefaultMathTransformFactory().createAffineTransform(new GeneralMatrix(3, 3, matrix));
                    coverage.setGrid(new GridGeometry2D(range, gridToCRS, crs));
                } else {
                    coverage.setGrid(new GridGeometry2D(range, gridEnvelope));
                }
            } else {
                // new grid range
                GeneralGridRange range = new GeneralGridRange(new int[] { 0, 0 }, new int[] { 1, 1 });
                coverage.setGrid(new GridGeometry2D(range, gridEnvelope));
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
            coverage.getSupportedFormats().addAll(Arrays.asList(new String[] { "GeoTIFF" }));
            coverage.setDefaultInterpolationMethod("nearest neighbour");
            coverage.getInterpolationMethods().addAll(Arrays.asList(new String[] { "bilinear", "bicubic" }));
            coverage.getRequestSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage.getResponseSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage.setEnabled(coverageStore.isEnabled());
            // parameters
            // coverage.getParameters().putAll(cInfoReader.parameters());
            // link to namespace
            HbNamespaceInfo namespace = catalog.getDefaultNamespace();
            coverage.setNamespace(namespace);
            
            coverage.getMetadata().put("dirName", coverageStore.getName() + "_" + coverage.getName());

            catalog.add(coverage);

            StyleInfo style = catalog.getFactory().createStyle();
            style.setName("raster");
            style.setFilename("raster.sld");
            
            catalog.add(style);
            
            LayerInfo layer = catalog.getFactory().createLayer();
            layer.setDefaultStyle(style);
            layer.setEnabled(coverage.isEnabled());
            //layer.setLegend(legend);
            layer.setName(coverage.getName());
            layer.setPath("/");
            layer.setResource(coverage);
            layer.setType(Type.RASTER);
            
            catalog.add(layer);
            
            List<CoverageInfo> coverages = new ArrayList<CoverageInfo>();
            coverages.add(coverage);
            modelRun.setGridCoverages(coverages);

            catalog.save(modelRun);
        } catch (Exception e) {
            throw new RuntimeException("Error creating bootstrap configuration", e);
        }

        return geoserver;
    }

    public void setGlobal(GeoServerInfo configuration) {
        GeoServerInfoImpl currentGlobal = (GeoServerInfoImpl) getGlobal();
        if (currentGlobal == null) {
            this.gsDAO.save(configuration);
        } else {
            // use merge, the argument instance may be from another session
            ((GeoServerInfoImpl) configuration).setId(currentGlobal.getId());
            this.gsDAO.merge(configuration);
        }
        this.gsDAO.commit();
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

        this.gsDAO.save(service);
        this.gsDAO.commit();
    }

    public void remove(ServiceInfo service) {
        this.gsDAO.delete(service);
        this.gsDAO.commit();
    }

    public void save(ServiceInfo service) {
        String id = service.getId();

        // use merge, the argument instance may be from another session
        this.gsDAO.merge(service);
        this.gsDAO.commit();
    }

    public Collection<? extends ServiceInfo> getServices() {
        return getServices(ServiceInfo.class);
    }

    public Collection<? extends ServiceInfo> getServices(Class<?> clazz) {
        List list = this.gsDAO.createQuery("from " + clazz.getName()).list();
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
        Iterator i = this.gsDAO.createQuery("from " + clazz.getName() + " where id = '" + id + "'")
                .iterate();
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
        Iterator i = this.gsDAO.createQuery(
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
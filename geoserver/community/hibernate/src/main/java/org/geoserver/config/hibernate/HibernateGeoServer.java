package org.geoserver.config.hibernate;

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

    private GeoServerInfo serviceBootStrap() {
        GeoServerInfo geoserver;
        geoserver = getFactory().createGlobal();
        geoserver.setContactInfo(getFactory().createContact());
        //Map<String, Serializable> tmp = geoserver.getMetadata();
        
        // do not call setGlobal or we'll get an infinite loop
        this.catalogDAO.save(geoserver);

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
        
        HbNamespaceInfo defaultNameSpace = catalog.getFactory().createNamespace();
        defaultNameSpace.setDefault(Boolean.TRUE);
        defaultNameSpace.setPrefix("topp");
        defaultNameSpace.setURI("http://www.openplans.org/topp");
        
        this.catalogDAO.save(defaultNameSpace);

        HbWorkspaceInfo defaultWs = catalog.getFactory().createWorkspace();
        defaultWs.setDefault(Boolean.TRUE);
        defaultWs.setName("topp");
        
        this.catalogDAO.save(defaultWs);
        
        // creating the default workspace
        catalog.bootStrap();
        
        /** TEST-DATA **/
        // ////////////////////////////////////////////////////////////////////
        //
        // DEFINING MODELS
        //
        // ////////////////////////////////////////////////////////////////////
        ModelInfoImpl model = new ModelInfoImpl();
        model.setAbstract("Abstract test.");
        model.setCenter("NURC");
        model.setDescription("This is a test model.");
        model.setDiscipline(Discipline.OCEAN);
        model.setId("TEST");
        List keywords = new ArrayList();
            keywords.add("NURC");
            keywords.add("MilOC");
            keywords.add("TEST");
        //model.setKeywords(keywords);
        model.setName("TEST-OCEAN-MODEL");
        model.setSubCenter("MilOC");
        model.setTitle("TEST OCEAN MODEL");
        model.setTypeOfData(DataType.ANALYSYS_AND_FORECAST);
        model.setVersion("1");
        
        this.catalogDAO.save(model);

        ModelInfoImpl ncom = new ModelInfoImpl();
        ncom.setAbstract("Abstract ncom test.");
        ncom.setCenter("NURC");
        ncom.setDescription("This is a test ncom model.");
        ncom.setDiscipline(Discipline.OCEAN);
        ncom.setId("NCOM");
        List ncom_keywords = new ArrayList();
            ncom_keywords.add("NURC");
            ncom_keywords.add("MilOC");
            ncom_keywords.add("NCOM");
        //ncom.setKeywords(keywords);
        ncom.setName("NCOM-OCEAN-MODEL");
        ncom.setSubCenter("MilOC");
        ncom.setTitle("NCOM OCEAN MODEL");
        ncom.setTypeOfData(DataType.ANALYSYS_AND_FORECAST);
        ncom.setVersion("1");
        
        this.catalogDAO.save(ncom);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:sssZ");
        
        // ////////////////////////////////////////////////////////////////////
        //
        // Defining Model-Runs
        //
        // ////////////////////////////////////////////////////////////////////
        /** Model Run 0 **/
        ModelRunInfoImpl modelRun_0 = new ModelRunInfoImpl();
        modelRun_0.setDescription("A Test Model Run.");
        try {
            modelRun_0.setExecutionTime(sdf.parse("2001-05-25T02:00:000+0200"));
            modelRun_0.setBaseTime(sdf.parse("2001-05-25T02:00:000+0200"));
        } catch (ParseException e1) {
            modelRun_0.setExecutionTime(new Date());
            modelRun_0.setBaseTime(new Date());
        }
        modelRun_0.setId("TEST-RUN-0");
        modelRun_0.setKeywords(keywords);
        modelRun_0.setModel(model);
        modelRun_0.setName("TEST-RUN-000");
        modelRun_0.setNumTAU(1);
        modelRun_0.setCRS(DefaultGeographicCRS.WGS84);
        modelRun_0.setGridCRS("Grid:2D");
        modelRun_0.setGridCS("Grid:square2D");
        modelRun_0.setGridLowers(new Double[] { 0.0, 0.0 });
        modelRun_0.setGridOffsets(new Double[] { 1.0, 1.0 });
        modelRun_0.setGridOrigin(new Double[] { 0.0, 0.0 });
        modelRun_0.setGridType("Grid:square2dIn2dCRS");
        modelRun_0.setGridUppers(new Double[] { 300.0, 300.0 });
        modelRun_0.setVerticalCoordinateMeaning("depth");
        modelRun_0.setOutline(new ReferencedEnvelope(new Envelope(27.40437126159668, 32.995628356933594, 41.01088333129883, 42.589115142822266), DefaultGeographicCRS.WGS84));
        modelRun_0.setTAU(1);
        modelRun_0.setTAUunit("day");
        modelRun_0.setUpdateSequence("0");

        this.catalogDAO.save(modelRun_0);

        /** Model Run 1 **/
        ModelRunInfoImpl modelRun_1 = new ModelRunInfoImpl();
        modelRun_1.setDescription("A Test Model Run.");
        try {
            modelRun_1.setExecutionTime(sdf.parse("2008-09-24T02:00:000+0200"));
            modelRun_1.setBaseTime(sdf.parse("2008-09-24T02:00:000+0200"));
        } catch (ParseException e1) {
            modelRun_1.setExecutionTime(new Date());
            modelRun_1.setBaseTime(new Date());
        }
        modelRun_1.setId("TEST-RUN-1");
        modelRun_1.setKeywords(keywords);
        modelRun_1.setModel(model);
        modelRun_1.setName("TEST-RUN-001");
        modelRun_1.setNumTAU(1);
        modelRun_1.setCRS(DefaultGeographicCRS.WGS84);
        modelRun_1.setGridCRS("Grid:2D");
        modelRun_1.setGridCS("Grid:square2D");
        modelRun_1.setGridLowers(new Double[] { 0.0, 0.0 });
        modelRun_1.setGridOffsets(new Double[] { 1.0, 1.0 });
        modelRun_1.setGridOrigin(new Double[] { 0.0, 0.0 });
        modelRun_1.setGridType("Grid:square2dIn2dCRS");
        modelRun_1.setGridUppers(new Double[] { 300.0, 300.0 });
        modelRun_1.setVerticalCoordinateMeaning("depth");
        modelRun_1.setOutline(new ReferencedEnvelope(new Envelope(6.875, 12.0, 42.00975799560547, 44.28925704956055), DefaultGeographicCRS.WGS84));
        modelRun_1.setTAU(1);
        modelRun_1.setTAUunit("day");
        modelRun_1.setUpdateSequence("0");

        this.catalogDAO.save(modelRun_1);

        /** Model Run 2 **/
        ModelRunInfoImpl modelRun_2 = new ModelRunInfoImpl();
        modelRun_2.setDescription("A Test Model Run.");
        try {
            modelRun_2.setExecutionTime(sdf.parse("2008-09-25T02:00:000+0200"));
            modelRun_2.setBaseTime(sdf.parse("2008-09-25T02:00:000+0200"));
        } catch (ParseException e1) {
            modelRun_2.setExecutionTime(new Date());
            modelRun_2.setBaseTime(new Date());
        }
        modelRun_2.setId("TEST-RUN-2");
        modelRun_2.setKeywords(keywords);
        modelRun_2.setModel(model);
        modelRun_2.setName("TEST-RUN-002");
        modelRun_2.setNumTAU(1);
        modelRun_2.setCRS(DefaultGeographicCRS.WGS84);
        modelRun_2.setGridCRS("Grid:2D");
        modelRun_2.setGridCS("Grid:square2D");
        modelRun_2.setGridLowers(new Double[] { 0.0, 0.0 });
        modelRun_2.setGridOffsets(new Double[] { 1.0, 1.0 });
        modelRun_2.setGridOrigin(new Double[] { 0.0, 0.0 });
        modelRun_2.setGridType("Grid:square2dIn2dCRS");
        modelRun_2.setGridUppers(new Double[] { 300.0, 300.0 });
        modelRun_2.setVerticalCoordinateMeaning("depth");
        modelRun_2.setOutline(new ReferencedEnvelope(new Envelope(6.875, 12.0, 42.00975799560547, 44.28925704956055), DefaultGeographicCRS.WGS84));
        modelRun_2.setTAU(1);
        modelRun_2.setTAUunit("day");
        modelRun_2.setUpdateSequence("0");

        this.catalogDAO.save(modelRun_2);

        /** Model Run 3 **/
        ModelRunInfoImpl modelRun_3 = new ModelRunInfoImpl();
        modelRun_3.setDescription("A Test Model Run.");
        try {
            modelRun_3.setExecutionTime(sdf.parse("2008-09-26T02:00:000+0200"));
            modelRun_3.setBaseTime(sdf.parse("2008-09-26T02:00:000+0200"));
        } catch (ParseException e1) {
            modelRun_3.setExecutionTime(new Date());
            modelRun_3.setBaseTime(new Date());
        }
        modelRun_3.setId("TEST-RUN-3");
        modelRun_3.setKeywords(keywords);
        modelRun_3.setModel(model);
        modelRun_3.setName("TEST-RUN-003");
        modelRun_3.setNumTAU(1);
        modelRun_3.setCRS(DefaultGeographicCRS.WGS84);
        modelRun_3.setGridCRS("Grid:2D");
        modelRun_3.setGridCS("Grid:square2D");
        modelRun_3.setGridLowers(new Double[] { 0.0, 0.0 });
        modelRun_3.setGridOffsets(new Double[] { 1.0, 1.0 });
        modelRun_3.setGridOrigin(new Double[] { 0.0, 0.0 });
        modelRun_3.setGridType("Grid:square2dIn2dCRS");
        modelRun_3.setGridUppers(new Double[] { 300.0, 300.0 });
        modelRun_3.setVerticalCoordinateMeaning("depth");
        modelRun_3.setOutline(new ReferencedEnvelope(new Envelope(6.875, 12.0, 42.00975799560547, 44.28925704956055), DefaultGeographicCRS.WGS84));
        modelRun_3.setTAU(1);
        modelRun_3.setTAUunit("day");
        modelRun_3.setUpdateSequence("0");

        this.catalogDAO.save(modelRun_3);
        
        /** Model Run 4 **/
        ModelRunInfoImpl modelRun_4 = new ModelRunInfoImpl();
        modelRun_4.setDescription("A Test Model Run.");
        try {
            modelRun_4.setExecutionTime(sdf.parse("2008-09-25T02:00:000+0200"));
            modelRun_4.setBaseTime(sdf.parse("2008-09-27T02:00:000+0200"));
        } catch (ParseException e1) {
            modelRun_4.setExecutionTime(new Date());
            modelRun_4.setBaseTime(new Date());
        }
        modelRun_4.setId("TEST-RUN-4");
        modelRun_4.setKeywords(ncom_keywords);
        modelRun_4.setModel(ncom);
        modelRun_4.setName("TEST-RUN-004");
        modelRun_4.setNumTAU(1);
        modelRun_4.setCRS(DefaultGeographicCRS.WGS84);
        modelRun_4.setGridCRS("Grid:2D");
        modelRun_4.setGridCS("Grid:square2D");
        modelRun_4.setGridLowers(new Double[] { 0.0, 0.0 });
        modelRun_4.setGridOffsets(new Double[] { 1.0, 1.0 });
        modelRun_4.setGridOrigin(new Double[] { 0.0, 0.0 });
        modelRun_4.setGridType("Grid:square2dIn2dCRS");
        modelRun_4.setGridUppers(new Double[] { 295.0, 112.0 });
        modelRun_4.setVerticalCoordinateMeaning("depth");
        modelRun_4.setOutline(new ReferencedEnvelope(new Envelope(0.5, 40.5, 14.855536460876465, 44.496002197265625), DefaultGeographicCRS.WGS84));
        modelRun_4.setTAU(3);
        modelRun_4.setTAUunit("day");
        modelRun_4.setUpdateSequence("0");

        this.catalogDAO.save(modelRun_4);

        /** Model Run 5 **/
        ModelRunInfoImpl modelRun_5 = new ModelRunInfoImpl();
        modelRun_5.setDescription("A Test Model Run.");
        try {
            modelRun_5.setExecutionTime(sdf.parse("2008-09-25T02:00:000+0200"));
            modelRun_5.setBaseTime(sdf.parse("2008-09-27T02:00:000+0200"));
        } catch (ParseException e1) {
            modelRun_5.setExecutionTime(new Date());
            modelRun_5.setBaseTime(new Date());
        }
        modelRun_5.setId("TEST-RUN-5");
        modelRun_5.setKeywords(ncom_keywords);
        modelRun_5.setModel(ncom);
        modelRun_5.setName("TEST-RUN-005");
        modelRun_5.setNumTAU(1);
        modelRun_5.setCRS(DefaultGeographicCRS.WGS84);
        modelRun_5.setGridCRS("Grid:2D");
        modelRun_5.setGridCS("Grid:square2D");
        modelRun_5.setGridLowers(new Double[] { 0.0, 0.0 });
        modelRun_5.setGridOffsets(new Double[] { 1.0, 1.0 });
        modelRun_5.setGridOrigin(new Double[] { 0.0, 0.0 });
        modelRun_5.setGridType("Grid:square2dIn2dCRS");
        modelRun_5.setGridUppers(new Double[] { 295.0, 112.0 });
        modelRun_5.setVerticalCoordinateMeaning("depth");
        modelRun_5.setOutline(new ReferencedEnvelope(new Envelope(0.5, 40.5, 14.855536460876465, 44.496002197265625), DefaultGeographicCRS.WGS84));
        modelRun_5.setTAU(3);
        modelRun_5.setTAUunit("day");
        modelRun_5.setUpdateSequence("0");

        this.catalogDAO.save(modelRun_5);
        
        // //
        // Updating Model
        // //
        List<ModelRunInfo> runsAll = new ArrayList<ModelRunInfo>();
        runsAll.add(modelRun_0);
        runsAll.add(modelRun_1);
        runsAll.add(modelRun_2);
        runsAll.add(modelRun_3);
        model.setModelRuns(runsAll);

        this.catalogDAO.update(model);

        List<ModelRunInfo> ncom_runsAll = new ArrayList<ModelRunInfo>();
        ncom_runsAll.add(modelRun_4);
        ncom_runsAll.add(modelRun_5);
        ncom.setModelRuns(ncom_runsAll);

        this.catalogDAO.update(ncom);

        List<ModelRunInfo> runs_0 = new ArrayList<ModelRunInfo>();
        runs_0.add(modelRun_0);

        List<ModelRunInfo> runs_1 = new ArrayList<ModelRunInfo>();
        runs_1.add(modelRun_1);
        runs_1.add(modelRun_2);
        runs_1.add(modelRun_3);

        try {
            // ////////////////////////////////////////////////////////////////
            //
            // Defining Sample Coverage Stores
            //
            // ////////////////////////////////////////////////////////////////
            CoverageStoreInfo coverageStore_0 = catalog.getFactory().createCoverageStore();
            coverageStore_0.setDescription("updatedoadCF.");
            coverageStore_0.setEnabled(true);
            coverageStore_0.setName("TEST-COVERAGE-STORE-0");
            coverageStore_0.setType("NetCDF");
            coverageStore_0.setURL("file:coverages/updatedoagCF.nc" /* getClass().getResource("updatedoagCF.nc").toURI().toString() */);
            coverageStore_0.setWorkspace(defaultWs);

            this.catalogDAO.save(coverageStore_0);

            CoverageStoreInfo coverageStore_1 = catalog.getFactory().createCoverageStore();
            coverageStore_1.setDescription("ext-mercatorPsy2v3R1v_med_mean_20080924_R20080924.");
            coverageStore_1.setEnabled(true);
            coverageStore_1.setName("TEST-COVERAGE-STORE-1");
            coverageStore_1.setType("NetCDF");
            coverageStore_1.setURL("file:coverages/ext-mercatorPsy2v3R1v_med_mean_20080924_R20080924.nc");
            coverageStore_1.setWorkspace(defaultWs);

            this.catalogDAO.save(coverageStore_1);

            CoverageStoreInfo coverageStore_2 = catalog.getFactory().createCoverageStore();
            coverageStore_2.setDescription("ext-mercatorPsy2v3R1v_med_mean_20080925_R20080924.");
            coverageStore_2.setEnabled(true);
            coverageStore_2.setName("TEST-COVERAGE-STORE-2");
            coverageStore_2.setType("NetCDF");
            coverageStore_2.setURL("file:coverages/ext-mercatorPsy2v3R1v_med_mean_20080925_R20080924.nc");
            coverageStore_2.setWorkspace(defaultWs);

            this.catalogDAO.save(coverageStore_2);

            CoverageStoreInfo coverageStore_3 = catalog.getFactory().createCoverageStore();
            coverageStore_3.setDescription("ext-mercatorPsy2v3R1v_med_mean_20080926_R20080924.");
            coverageStore_3.setEnabled(true);
            coverageStore_3.setName("TEST-COVERAGE-STORE-3");
            coverageStore_3.setType("NetCDF");
            coverageStore_3.setURL("file:coverages/ext-mercatorPsy2v3R1v_med_mean_20080926_R20080924.nc");
            coverageStore_3.setWorkspace(defaultWs);

            this.catalogDAO.save(coverageStore_3);
            
            CoverageStoreInfo coverageStore_4 = catalog.getFactory().createCoverageStore();
            coverageStore_4.setDescription("salt_m08_nest0_20080925.");
            coverageStore_4.setEnabled(true);
            coverageStore_4.setName("TEST-COVERAGE-STORE-4");
            coverageStore_4.setType("NetCDF");
            coverageStore_4.setURL("file:coverages/salt_m08_nest0_20080925.nc");
            coverageStore_4.setWorkspace(defaultWs);

            this.catalogDAO.save(coverageStore_4);

            CoverageStoreInfo coverageStore_5 = catalog.getFactory().createCoverageStore();
            coverageStore_5.setDescription("temp_m08_nest0_20080925.");
            coverageStore_5.setEnabled(true);
            coverageStore_5.setName("TEST-COVERAGE-STORE-5");
            coverageStore_5.setType("NetCDF");
            coverageStore_5.setURL("file:coverages/temp_m08_nest0_20080925.nc");
            coverageStore_5.setWorkspace(defaultWs);

            this.catalogDAO.save(coverageStore_5);
            
            // //
            // Default Raster Style
            // //
            StyleInfo style = catalog.getFactory().createStyle();
            style.setName("raster");
            style.setFilename("raster.sld");
            
            this.catalogDAO.save(style);

            // ////////////////////////////////////////////////////////////////
            //
            // Defining Sample Coverages
            //
            // ////////////////////////////////////////////////////////////////
            /** Coverage 0 **/
            CoverageInfo coverage_0 = catalog.getFactory().createCoverage();
            coverage_0.setStore(coverageStore_0);
            coverage_0.setName("updatedoagCF_0");
            coverage_0.setNativeName("updatedoagCF_0");
            coverage_0.setTitle("TEST-COVERAGE-000");
            coverage_0.setDescription("An Example Test Coverage.");
            coverage_0.getKeywords().addAll(keywords);
            Map<String, Object> envelope_0 = new HashMap<String, Object>();
            envelope_0.put("x1", 27.40437126159668);
            envelope_0.put("x2", 32.995628356933594);
            envelope_0.put("y1", 41.01088333129883);
            envelope_0.put("y2", 42.589115142822266);
            envelope_0.put("srsName", "EPSG:4326");
            envelope_0.put("crs", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]");
            String userDefinedCrsIdentifier_0 = (String) envelope_0.get("srsName");
            String nativeCrsWkt_0 = (String) envelope_0.get("crs");
            coverage_0.setSRS(userDefinedCrsIdentifier_0);
            CoordinateReferenceSystem crs_0 = CRS.parseWKT(nativeCrsWkt_0);
            coverage_0.setNativeCRS(crs_0);
            ReferencedEnvelope bounds_0 = 
                new ReferencedEnvelope(
                    (Double) envelope_0.get("x1"),
                    (Double) envelope_0.get("x2"), 
                    (Double) envelope_0.get("y1"), 
                    (Double) envelope_0.get("y2"), 
                    crs_0
                );
            coverage_0.setNativeBoundingBox(bounds_0);
            GeneralEnvelope boundsLatLon_0 = CoverageStoreUtils.getWGS84LonLatEnvelope(new GeneralEnvelope(bounds_0));
            coverage_0.setLatLonBoundingBox(new ReferencedEnvelope(boundsLatLon_0));
            GeneralEnvelope gridEnvelope_0 = new GeneralEnvelope(bounds_0);
            Map grid_0 = new HashMap();
            grid_0.put("low", new int[] { 0, 0 });
            grid_0.put("high", new int[] { 300, 300 });
            if (grid_0 != null) {
                int[] low = (int[]) grid_0.get("low");
                int[] high = (int[]) grid_0.get("high");
                GeneralGridRange range = new GeneralGridRange(low, high);
                Map<String, Double> tx = (Map<String, Double>) grid_0.get("geoTransform");
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
                    coverage_0.setGrid(new GridGeometry2D(range, gridToCRS, crs_0));
                } else {
                    coverage_0.setGrid(new GridGeometry2D(range, gridEnvelope_0));
                }
            } else {
                // new grid range
                GeneralGridRange range = new GeneralGridRange(new int[] { 0, 0 }, new int[] { 1, 1 });
                coverage_0.setGrid(new GridGeometry2D(range, gridEnvelope_0));
            }
            Driver driver_0 = coverageStore_0.getDriver();
            Map params_0 = new HashMap();
            params_0.put("url", GeoserverDataDirectory.findDataFile(coverageStore_0.getURL()).toURI().toURL());
            CoverageAccess cvAccess_0 = driver_0.connect(params_0, null, null);
            if (cvAccess_0 != null) {
                CoverageSource cvSource = cvAccess_0.access(new NameImpl(coverage_0.getName()), null, AccessType.READ_ONLY, null, null);
                if (cvSource != null) {
                    coverage_0.setFields(cvSource.getRangeType(null));
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
                    coverage_0.setTemporalCRS(temporalCRS);
                    coverage_0.setTemporalExtent(temporalExtent);
                    coverage_0.setVerticalCRS(verticalCRS);
                    coverage_0.setVerticalExtent(verticalExtent);
                }
            }
            coverage_0.setNativeFormat("NetCDF");
            coverage_0.getSupportedFormats().addAll(Arrays.asList(new String[] { "GeoTIFF" }));
            coverage_0.setDefaultInterpolationMethod("nearest neighbor");
            coverage_0.getInterpolationMethods().addAll(Arrays.asList(new String[] { "nearest neighbor", "bilinear", "bicubic" }));
            coverage_0.getRequestSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage_0.getResponseSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage_0.setEnabled(coverageStore_0.isEnabled());
            coverage_0.setNamespace(this.catalogDAO.getDefaultNamespace());
            coverage_0.getMetadata().put("dirName", coverageStore_0.getName() + "_" + coverage_0.getName());

            this.catalogDAO.save(coverage_0);
                        
            LayerInfo layer_0 = catalog.getFactory().createLayer();
            layer_0.setDefaultStyle(style);
            layer_0.setEnabled(coverage_0.isEnabled());
            //layer.setLegend(legend);
            layer_0.setName(coverage_0.getName());
            layer_0.setPath("/");
            layer_0.setResource(coverage_0);
            layer_0.setType(Type.RASTER);
            
            this.catalogDAO.save(layer_0);
            
            /** Coverage 1 **/
            CoverageInfo coverage_1 = catalog.getFactory().createCoverage();
            coverage_1.setStore(coverageStore_1);
            coverage_1.setName("ext-mercatorPsy2v3R1v_med_mean_20080924_R20080924_0");
            coverage_1.setNativeName("ext-mercatorPsy2v3R1v_med_mean_20080924_R20080924_0");
            coverage_1.setTitle("TEST-COVERAGE-001");
            coverage_1.setDescription("An Example Test Coverage.");
            coverage_1.getKeywords().addAll(keywords);
            Map<String, Object> envelope_1 = new HashMap<String, Object>();
            envelope_1.put("x1", 6.875);
            envelope_1.put("x2", 12.0);
            envelope_1.put("y1", 42.00975799560547);
            envelope_1.put("y2", 44.28925704956055);
            envelope_1.put("srsName", "EPSG:4326");
            envelope_1.put("crs", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]");
            String userDefinedCrsIdentifier_1 = (String) envelope_1.get("srsName");
            String nativeCrsWkt_1 = (String) envelope_1.get("crs");
            coverage_1.setSRS(userDefinedCrsIdentifier_1);
            CoordinateReferenceSystem crs_1 = CRS.parseWKT(nativeCrsWkt_1);
            coverage_1.setNativeCRS(crs_1);
            ReferencedEnvelope bounds_1 = 
                new ReferencedEnvelope(
                    (Double) envelope_1.get("x1"),
                    (Double) envelope_1.get("x2"), 
                    (Double) envelope_1.get("y1"), 
                    (Double) envelope_1.get("y2"), 
                    crs_1
                );
            coverage_1.setNativeBoundingBox(bounds_1);
            GeneralEnvelope boundsLatLon_1 = CoverageStoreUtils.getWGS84LonLatEnvelope(new GeneralEnvelope(bounds_1));
            coverage_1.setLatLonBoundingBox(new ReferencedEnvelope(boundsLatLon_1));
            GeneralEnvelope gridEnvelope_1 = new GeneralEnvelope(bounds_1);
            Map grid_1 = new HashMap();
            grid_1.put("low", new int[] { 0, 0 });
            grid_1.put("high", new int[] { 300, 300 });
            if (grid_1 != null) {
                int[] low = (int[]) grid_1.get("low");
                int[] high = (int[]) grid_1.get("high");
                GeneralGridRange range = new GeneralGridRange(low, high);
                Map<String, Double> tx = (Map<String, Double>) grid_1.get("geoTransform");
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
                    coverage_1.setGrid(new GridGeometry2D(range, gridToCRS, crs_1));
                } else {
                    coverage_1.setGrid(new GridGeometry2D(range, gridEnvelope_1));
                }
            } else {
                // new grid range
                GeneralGridRange range = new GeneralGridRange(new int[] { 0, 0 }, new int[] { 1, 1 });
                coverage_1.setGrid(new GridGeometry2D(range, gridEnvelope_1));
            }
            Driver driver_1 = coverageStore_1.getDriver();
            Map params_1 = new HashMap();
            params_1.put("url", GeoserverDataDirectory.findDataFile(coverageStore_1.getURL()).toURI().toURL());
            CoverageAccess cvAccess_1 = driver_1.connect(params_1, null, null);
            if (cvAccess_1 != null) {
                CoverageSource cvSource = cvAccess_1.access(new NameImpl(coverage_1.getName()), null, AccessType.READ_ONLY, null, null);
                if (cvSource != null) {
                    coverage_1.setFields(cvSource.getRangeType(null));
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
                    coverage_1.setTemporalCRS(temporalCRS);
                    coverage_1.setTemporalExtent(temporalExtent);
                    coverage_1.setVerticalCRS(verticalCRS);
                    coverage_1.setVerticalExtent(verticalExtent);
                }
            }
            coverage_1.setNativeFormat("NetCDF");
            coverage_1.getSupportedFormats().addAll(Arrays.asList(new String[] { "GeoTIFF" }));
            coverage_1.setDefaultInterpolationMethod("nearest neighbor");
            coverage_1.getInterpolationMethods().addAll(Arrays.asList(new String[] { "nearest neighbor", "bilinear", "bicubic" }));
            coverage_1.getRequestSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage_1.getResponseSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage_1.setEnabled(coverageStore_1.isEnabled());
            coverage_1.setNamespace(this.catalogDAO.getDefaultNamespace());
            coverage_1.getMetadata().put("dirName", coverageStore_1.getName() + "_" + coverage_1.getName());

            this.catalogDAO.save(coverage_1);
                        
            LayerInfo layer_1 = catalog.getFactory().createLayer();
            layer_1.setDefaultStyle(style);
            layer_1.setEnabled(coverage_1.isEnabled());
            //layer.setLegend(legend);
            layer_1.setName(coverage_1.getName());
            layer_1.setPath("/");
            layer_1.setResource(coverage_1);
            layer_1.setType(Type.RASTER);
            
            this.catalogDAO.save(layer_1);

            /** Coverage 2 **/
            CoverageInfo coverage_2 = catalog.getFactory().createCoverage();
            coverage_2.setStore(coverageStore_2);
            coverage_2.setName("ext-mercatorPsy2v3R1v_med_mean_20080925_R20080924_0");
            coverage_2.setNativeName("ext-mercatorPsy2v3R1v_med_mean_20080925_R20080924_0");
            coverage_2.setTitle("TEST-COVERAGE-002");
            coverage_2.setDescription("An Example Test Coverage.");
            coverage_2.getKeywords().addAll(keywords);
            Map<String, Object> envelope_2 = new HashMap<String, Object>();
            envelope_2.put("x1", 6.875);
            envelope_2.put("x2", 12.0);
            envelope_2.put("y1", 42.00975799560547);
            envelope_2.put("y2", 44.28925704956055);
            envelope_2.put("srsName", "EPSG:4326");
            envelope_2.put("crs", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]");
            String userDefinedCrsIdentifier_2 = (String) envelope_2.get("srsName");
            String nativeCrsWkt_2 = (String) envelope_2.get("crs");
            coverage_2.setSRS(userDefinedCrsIdentifier_2);
            CoordinateReferenceSystem crs_2 = CRS.parseWKT(nativeCrsWkt_2);
            coverage_2.setNativeCRS(crs_2);
            ReferencedEnvelope bounds_2 = 
                new ReferencedEnvelope(
                    (Double) envelope_2.get("x1"),
                    (Double) envelope_2.get("x2"), 
                    (Double) envelope_2.get("y1"), 
                    (Double) envelope_2.get("y2"), 
                    crs_2
                );
            coverage_2.setNativeBoundingBox(bounds_2);
            GeneralEnvelope boundsLatLon_2 = CoverageStoreUtils.getWGS84LonLatEnvelope(new GeneralEnvelope(bounds_2));
            coverage_2.setLatLonBoundingBox(new ReferencedEnvelope(boundsLatLon_2));
            GeneralEnvelope gridEnvelope_2 = new GeneralEnvelope(bounds_2);
            Map grid_2 = new HashMap();
            grid_2.put("low", new int[] { 0, 0 });
            grid_2.put("high", new int[] { 300, 300 });
            if (grid_2 != null) {
                int[] low = (int[]) grid_2.get("low");
                int[] high = (int[]) grid_2.get("high");
                GeneralGridRange range = new GeneralGridRange(low, high);
                Map<String, Double> tx = (Map<String, Double>) grid_2.get("geoTransform");
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
                    coverage_2.setGrid(new GridGeometry2D(range, gridToCRS, crs_2));
                } else {
                    coverage_2.setGrid(new GridGeometry2D(range, gridEnvelope_2));
                }
            } else {
                // new grid range
                GeneralGridRange range = new GeneralGridRange(new int[] { 0, 0 }, new int[] { 1, 1 });
                coverage_2.setGrid(new GridGeometry2D(range, gridEnvelope_2));
            }
            Driver driver_2 = coverageStore_2.getDriver();
            Map params_2 = new HashMap();
            params_2.put("url", GeoserverDataDirectory.findDataFile(coverageStore_2.getURL()).toURI().toURL());
            CoverageAccess cvAccess_2 = driver_2.connect(params_2, null, null);
            if (cvAccess_2 != null) {
                CoverageSource cvSource = cvAccess_2.access(new NameImpl(coverage_2.getName()), null, AccessType.READ_ONLY, null, null);
                if (cvSource != null) {
                    coverage_2.setFields(cvSource.getRangeType(null));
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
                    coverage_2.setTemporalCRS(temporalCRS);
                    coverage_2.setTemporalExtent(temporalExtent);
                    coverage_2.setVerticalCRS(verticalCRS);
                    coverage_2.setVerticalExtent(verticalExtent);
                }
            }
            coverage_2.setNativeFormat("NetCDF");
            coverage_2.getSupportedFormats().addAll(Arrays.asList(new String[] { "GeoTIFF" }));
            coverage_2.setDefaultInterpolationMethod("nearest neighbor");
            coverage_2.getInterpolationMethods().addAll(Arrays.asList(new String[] { "nearest neighbor", "bilinear", "bicubic" }));
            coverage_2.getRequestSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage_2.getResponseSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage_2.setEnabled(coverageStore_2.isEnabled());
            coverage_2.setNamespace(this.catalogDAO.getDefaultNamespace());
            coverage_2.getMetadata().put("dirName", coverageStore_2.getName() + "_" + coverage_2.getName());

            this.catalogDAO.save(coverage_2);
                        
            LayerInfo layer_2 = catalog.getFactory().createLayer();
            layer_2.setDefaultStyle(style);
            layer_2.setEnabled(coverage_2.isEnabled());
            //layer.setLegend(legend);
            layer_2.setName(coverage_2.getName());
            layer_2.setPath("/");
            layer_2.setResource(coverage_2);
            layer_2.setType(Type.RASTER);
            
            this.catalogDAO.save(layer_2);

            /** Coverage 3 **/
            CoverageInfo coverage_3 = catalog.getFactory().createCoverage();
            coverage_3.setStore(coverageStore_3);
            coverage_3.setName("ext-mercatorPsy2v3R1v_med_mean_20080926_R20080924_0");
            coverage_3.setNativeName("ext-mercatorPsy2v3R1v_med_mean_20080926_R20080924_0");
            coverage_3.setTitle("TEST-COVERAGE-003");
            coverage_3.setDescription("An Example Test Coverage.");
            coverage_3.getKeywords().addAll(keywords);
            Map<String, Object> envelope_3 = new HashMap<String, Object>();
            envelope_3.put("x1", 6.875);
            envelope_3.put("x2", 12.0);
            envelope_3.put("y1", 42.00975799560547);
            envelope_3.put("y2", 44.28925704956055);
            envelope_3.put("srsName", "EPSG:4326");
            envelope_3.put("crs", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]");
            String userDefinedCrsIdentifier_3 = (String) envelope_3.get("srsName");
            String nativeCrsWkt_3 = (String) envelope_3.get("crs");
            coverage_3.setSRS(userDefinedCrsIdentifier_3);
            CoordinateReferenceSystem crs_3 = CRS.parseWKT(nativeCrsWkt_3);
            coverage_3.setNativeCRS(crs_3);
            ReferencedEnvelope bounds_3 = 
                new ReferencedEnvelope(
                    (Double) envelope_3.get("x1"),
                    (Double) envelope_3.get("x2"), 
                    (Double) envelope_3.get("y1"), 
                    (Double) envelope_3.get("y2"), 
                    crs_3
                );
            coverage_3.setNativeBoundingBox(bounds_3);
            GeneralEnvelope boundsLatLon_3 = CoverageStoreUtils.getWGS84LonLatEnvelope(new GeneralEnvelope(bounds_3));
            coverage_3.setLatLonBoundingBox(new ReferencedEnvelope(boundsLatLon_3));
            GeneralEnvelope gridEnvelope_3 = new GeneralEnvelope(bounds_3);
            Map grid_3 = new HashMap();
            grid_3.put("low", new int[] { 0, 0 });
            grid_3.put("high", new int[] { 300, 300 });
            if (grid_3 != null) {
                int[] low = (int[]) grid_3.get("low");
                int[] high = (int[]) grid_3.get("high");
                GeneralGridRange range = new GeneralGridRange(low, high);
                Map<String, Double> tx = (Map<String, Double>) grid_3.get("geoTransform");
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
                    coverage_3.setGrid(new GridGeometry2D(range, gridToCRS, crs_3));
                } else {
                    coverage_3.setGrid(new GridGeometry2D(range, gridEnvelope_3));
                }
            } else {
                // new grid range
                GeneralGridRange range = new GeneralGridRange(new int[] { 0, 0 }, new int[] { 1, 1 });
                coverage_3.setGrid(new GridGeometry2D(range, gridEnvelope_3));
            }
            Driver driver_3 = coverageStore_3.getDriver();
            Map params_3 = new HashMap();
            params_3.put("url", GeoserverDataDirectory.findDataFile(coverageStore_3.getURL()).toURI().toURL());
            CoverageAccess cvAccess_3 = driver_3.connect(params_3, null, null);
            if (cvAccess_3 != null) {
                CoverageSource cvSource = cvAccess_3.access(new NameImpl(coverage_3.getName()), null, AccessType.READ_ONLY, null, null);
                if (cvSource != null) {
                    coverage_3.setFields(cvSource.getRangeType(null));
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
                    coverage_3.setTemporalCRS(temporalCRS);
                    coverage_3.setTemporalExtent(temporalExtent);
                    coverage_3.setVerticalCRS(verticalCRS);
                    coverage_3.setVerticalExtent(verticalExtent);
                }
            }
            coverage_3.setNativeFormat("NetCDF");
            coverage_3.getSupportedFormats().addAll(Arrays.asList(new String[] { "GeoTIFF" }));
            coverage_3.setDefaultInterpolationMethod("nearest neighbor");
            coverage_3.getInterpolationMethods().addAll(Arrays.asList(new String[] { "nearest neighbor", "bilinear", "bicubic" }));
            coverage_3.getRequestSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage_3.getResponseSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage_3.setEnabled(coverageStore_3.isEnabled());
            coverage_3.setNamespace(this.catalogDAO.getDefaultNamespace());
            coverage_3.getMetadata().put("dirName", coverageStore_3.getName() + "_" + coverage_3.getName());

            this.catalogDAO.save(coverage_3);
                        
            LayerInfo layer_3 = catalog.getFactory().createLayer();
            layer_3.setDefaultStyle(style);
            layer_3.setEnabled(coverage_3.isEnabled());
            //layer.setLegend(legend);
            layer_3.setName(coverage_3.getName());
            layer_3.setPath("/");
            layer_3.setResource(coverage_3);
            layer_3.setType(Type.RASTER);
            
            this.catalogDAO.save(layer_3);

            /** Coverage 4 **/
            CoverageInfo coverage_4 = catalog.getFactory().createCoverage();
            coverage_4.setStore(coverageStore_4);
            coverage_4.setName("salt_m08_nest0_20080925_0");
            coverage_4.setNativeName("salt_m08_nest0_20080925_0");
            coverage_4.setTitle("TEST-COVERAGE-004");
            coverage_4.setDescription("An Example Test Coverage.");
            coverage_4.getKeywords().addAll(ncom_keywords);
            Map<String, Object> envelope_4 = new HashMap<String, Object>();
            envelope_4.put("x1", 0.5);
            envelope_4.put("x2", 14.855536460876465);
            envelope_4.put("y1", 40.5);
            envelope_4.put("y2", 44.496002197265625);
            envelope_4.put("srsName", "EPSG:4326");
            envelope_4.put("crs", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]");
            String userDefinedCrsIdentifier_4 = (String) envelope_4.get("srsName");
            String nativeCrsWkt_4 = (String) envelope_4.get("crs");
            coverage_4.setSRS(userDefinedCrsIdentifier_4);
            CoordinateReferenceSystem crs_4 = CRS.parseWKT(nativeCrsWkt_4);
            coverage_4.setNativeCRS(crs_4);
            ReferencedEnvelope bounds_4 = 
                new ReferencedEnvelope(
                    (Double) envelope_4.get("x1"),
                    (Double) envelope_4.get("x2"), 
                    (Double) envelope_4.get("y1"), 
                    (Double) envelope_4.get("y2"), 
                    crs_4
                );
            coverage_4.setNativeBoundingBox(bounds_4);
            GeneralEnvelope boundsLatLon_4 = CoverageStoreUtils.getWGS84LonLatEnvelope(new GeneralEnvelope(bounds_4));
            coverage_4.setLatLonBoundingBox(new ReferencedEnvelope(boundsLatLon_4));
            GeneralEnvelope gridEnvelope_4 = new GeneralEnvelope(bounds_4);
            Map grid_4 = new HashMap();
            grid_4.put("low", new int[] { 0, 0 });
            grid_4.put("high", new int[] { 295, 112 });
            if (grid_4 != null) {
                int[] low = (int[]) grid_4.get("low");
                int[] high = (int[]) grid_4.get("high");
                GeneralGridRange range = new GeneralGridRange(low, high);
                Map<String, Double> tx = (Map<String, Double>) grid_4.get("geoTransform");
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
                    coverage_4.setGrid(new GridGeometry2D(range, gridToCRS, crs_4));
                } else {
                    coverage_4.setGrid(new GridGeometry2D(range, gridEnvelope_4));
                }
            } else {
                // new grid range
                GeneralGridRange range = new GeneralGridRange(new int[] { 0, 0 }, new int[] { 1, 1 });
                coverage_4.setGrid(new GridGeometry2D(range, gridEnvelope_3));
            }
            Driver driver_4 = coverageStore_4.getDriver();
            Map params_4 = new HashMap();
            params_4.put("url", GeoserverDataDirectory.findDataFile(coverageStore_4.getURL()).toURI().toURL());
            CoverageAccess cvAccess_4 = driver_4.connect(params_4, null, null);
            if (cvAccess_4 != null) {
                CoverageSource cvSource = cvAccess_4.access(new NameImpl(coverage_4.getName()), null, AccessType.READ_ONLY, null, null);
                if (cvSource != null) {
                    coverage_4.setFields(cvSource.getRangeType(null));
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
                    coverage_4.setTemporalCRS(temporalCRS);
                    coverage_4.setTemporalExtent(temporalExtent);
                    coverage_4.setVerticalCRS(verticalCRS);
                    coverage_4.setVerticalExtent(verticalExtent);
                }
            }
            coverage_4.setNativeFormat("NetCDF");
            coverage_4.getSupportedFormats().addAll(Arrays.asList(new String[] { "GeoTIFF" }));
            coverage_4.setDefaultInterpolationMethod("nearest neighbor");
            coverage_4.getInterpolationMethods().addAll(Arrays.asList(new String[] { "nearest neighbor", "bilinear", "bicubic" }));
            coverage_4.getRequestSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage_4.getResponseSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage_4.setEnabled(coverageStore_4.isEnabled());
            coverage_4.setNamespace(this.catalogDAO.getDefaultNamespace());
            coverage_4.getMetadata().put("dirName", coverageStore_4.getName() + "_" + coverage_4.getName());

            this.catalogDAO.save(coverage_4);
                        
            LayerInfo layer_4 = catalog.getFactory().createLayer();
            layer_4.setDefaultStyle(style);
            layer_4.setEnabled(coverage_4.isEnabled());
            //layer.setLegend(legend);
            layer_4.setName(coverage_4.getName());
            layer_4.setPath("/");
            layer_4.setResource(coverage_4);
            layer_4.setType(Type.RASTER);
            
            this.catalogDAO.save(layer_4);
            
            /** Coverage 5 **/
            CoverageInfo coverage_5 = catalog.getFactory().createCoverage();
            coverage_5.setStore(coverageStore_5);
            coverage_5.setName("temp_m08_nest0_20080925_0");
            coverage_5.setNativeName("temp_m08_nest0_20080925_0");
            coverage_5.setTitle("TEST-COVERAGE-005");
            coverage_5.setDescription("An Example Test Coverage.");
            coverage_5.getKeywords().addAll(ncom_keywords);
            Map<String, Object> envelope_5 = new HashMap<String, Object>();
            envelope_5.put("x1", 0.5);
            envelope_5.put("x2", 14.855536460876465);
            envelope_5.put("y1", 40.5);
            envelope_5.put("y2", 44.496002197265625);
            envelope_5.put("srsName", "EPSG:4326");
            envelope_5.put("crs", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]");
            String userDefinedCrsIdentifier_5 = (String) envelope_5.get("srsName");
            String nativeCrsWkt_5 = (String) envelope_5.get("crs");
            coverage_5.setSRS(userDefinedCrsIdentifier_5);
            CoordinateReferenceSystem crs_5 = CRS.parseWKT(nativeCrsWkt_5);
            coverage_5.setNativeCRS(crs_5);
            ReferencedEnvelope bounds_5 = 
                new ReferencedEnvelope(
                    (Double) envelope_5.get("x1"),
                    (Double) envelope_5.get("x2"), 
                    (Double) envelope_5.get("y1"), 
                    (Double) envelope_5.get("y2"), 
                    crs_5
                );
            coverage_5.setNativeBoundingBox(bounds_5);
            GeneralEnvelope boundsLatLon_5 = CoverageStoreUtils.getWGS84LonLatEnvelope(new GeneralEnvelope(bounds_5));
            coverage_5.setLatLonBoundingBox(new ReferencedEnvelope(boundsLatLon_5));
            GeneralEnvelope gridEnvelope_5 = new GeneralEnvelope(bounds_5);
            Map grid_5 = new HashMap();
            grid_5.put("low", new int[] { 0, 0 });
            grid_5.put("high", new int[] { 295, 112 });
            if (grid_5 != null) {
                int[] low = (int[]) grid_5.get("low");
                int[] high = (int[]) grid_5.get("high");
                GeneralGridRange range = new GeneralGridRange(low, high);
                Map<String, Double> tx = (Map<String, Double>) grid_5.get("geoTransform");
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
                    coverage_5.setGrid(new GridGeometry2D(range, gridToCRS, crs_5));
                } else {
                    coverage_5.setGrid(new GridGeometry2D(range, gridEnvelope_5));
                }
            } else {
                // new grid range
                GeneralGridRange range = new GeneralGridRange(new int[] { 0, 0 }, new int[] { 1, 1 });
                coverage_5.setGrid(new GridGeometry2D(range, gridEnvelope_3));
            }
            Driver driver_5 = coverageStore_4.getDriver();
            Map params_5 = new HashMap();
            params_5.put("url", GeoserverDataDirectory.findDataFile(coverageStore_5.getURL()).toURI().toURL());
            CoverageAccess cvAccess_5 = driver_5.connect(params_5, null, null);
            if (cvAccess_5 != null) {
                CoverageSource cvSource = cvAccess_5.access(new NameImpl(coverage_5.getName()), null, AccessType.READ_ONLY, null, null);
                if (cvSource != null) {
                    coverage_5.setFields(cvSource.getRangeType(null));
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
                    coverage_5.setTemporalCRS(temporalCRS);
                    coverage_5.setTemporalExtent(temporalExtent);
                    coverage_5.setVerticalCRS(verticalCRS);
                    coverage_5.setVerticalExtent(verticalExtent);
                }
            }
            coverage_5.setNativeFormat("NetCDF");
            coverage_5.getSupportedFormats().addAll(Arrays.asList(new String[] { "GeoTIFF" }));
            coverage_5.setDefaultInterpolationMethod("nearest neighbor");
            coverage_5.getInterpolationMethods().addAll(Arrays.asList(new String[] { "nearest neighbor", "bilinear", "bicubic" }));
            coverage_5.getRequestSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage_5.getResponseSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
            coverage_5.setEnabled(coverageStore_5.isEnabled());
            coverage_5.setNamespace(this.catalogDAO.getDefaultNamespace());
            coverage_5.getMetadata().put("dirName", coverageStore_5.getName() + "_" + coverage_5.getName());

            this.catalogDAO.save(coverage_5);
                        
            LayerInfo layer_5 = catalog.getFactory().createLayer();
            layer_5.setDefaultStyle(style);
            layer_5.setEnabled(coverage_5.isEnabled());
            //layer.setLegend(legend);
            layer_5.setName(coverage_5.getName());
            layer_5.setPath("/");
            layer_5.setResource(coverage_5);
            layer_5.setType(Type.RASTER);
            
            this.catalogDAO.save(layer_5);
            
            // //
            // Updating Model Runs
            // //
            List<CoverageInfo> coverages_0 = new ArrayList<CoverageInfo>();
            coverages_0.add(coverage_0);
            modelRun_0.setGridCoverages(coverages_0);

            this.catalogDAO.update(modelRun_0);

            List<CoverageInfo> coverages_1 = new ArrayList<CoverageInfo>();
            coverages_1.add(coverage_1);
            modelRun_1.setGridCoverages(coverages_1);

            this.catalogDAO.update(modelRun_1);

            List<CoverageInfo> coverages_2 = new ArrayList<CoverageInfo>();
            coverages_2.add(coverage_2);
            modelRun_2.setGridCoverages(coverages_2);

            this.catalogDAO.update(modelRun_2);

            List<CoverageInfo> coverages_3 = new ArrayList<CoverageInfo>();
            coverages_3.add(coverage_3);
            modelRun_3.setGridCoverages(coverages_3);

            this.catalogDAO.update(modelRun_3);
            
            List<CoverageInfo> coverages_4 = new ArrayList<CoverageInfo>();
            coverages_4.add(coverage_4);
            modelRun_4.setGridCoverages(coverages_4);

            this.catalogDAO.update(modelRun_4);

            List<CoverageInfo> coverages_5 = new ArrayList<CoverageInfo>();
            coverages_5.add(coverage_5);
            modelRun_5.setGridCoverages(coverages_5);

            this.catalogDAO.update(modelRun_5);

            // ////
            // Updating Variables
            // ////
            List<ModelInfo> models = new ArrayList<ModelInfo>();
            models.add(model);

            GeophysicParamInfo dynht = new GeophysicParamInfoImpl();
            dynht.setName("dynht");
            dynht.setTitle("dynht");
            dynht.setDescription("dynht");
            dynht.setModels(models);
            dynht.setModelRuns(runsAll);
            dynht.setGridCoverages(coverages_0);
            
            this.catalogDAO.save(dynht);

            GeophysicParamInfo dynhterr = new GeophysicParamInfoImpl();
            dynhterr.setName("dynhterr");
            dynhterr.setTitle("dynhterr");
            dynhterr.setDescription("dynhterr");
            dynhterr.setModels(models);
            dynhterr.setModelRuns(runsAll);
            dynhterr.setGridCoverages(coverages_0);

            this.catalogDAO.save(dynhterr);

            GeophysicParamInfo dynhtmean = new GeophysicParamInfoImpl();
            dynhtmean.setName("dynhtmean");
            dynhtmean.setTitle("dynhtmean");
            dynhtmean.setDescription("dynhtmean");
            dynhtmean.setModels(models);
            dynhtmean.setModelRuns(runsAll);
            dynhtmean.setGridCoverages(coverages_0);

            this.catalogDAO.save(dynhtmean);

            GeophysicParamInfo salt = new GeophysicParamInfoImpl();
            salt.setName("salt");
            List<String> saltAliases = new ArrayList<String>();
            saltAliases.add("salinity");
			salt.setAlias(saltAliases);
            salt.setTitle("salt");
            salt.setDescription("salt");
            salt.setModels(models);
            salt.setModelRuns(runsAll);
            List<CoverageInfo> saltCoverages = new ArrayList<CoverageInfo>();
            saltCoverages.addAll(coverages_0);
            saltCoverages.addAll(coverages_1);
            saltCoverages.addAll(coverages_2);
            saltCoverages.addAll(coverages_3);
            saltCoverages.addAll(coverages_4);
            salt.setGridCoverages(saltCoverages);

            this.catalogDAO.save(salt);

            GeophysicParamInfo salterr = new GeophysicParamInfoImpl();
            salterr.setName("salterr");
            salterr.setTitle("salterr");
            salterr.setDescription("salterr");
            salterr.setModels(models);
            salterr.setModelRuns(runsAll);
            salterr.setGridCoverages(coverages_0);

            this.catalogDAO.save(salterr);

            GeophysicParamInfo saltmean = new GeophysicParamInfoImpl();
            saltmean.setName("saltmean");
            saltmean.setTitle("saltmean");
            saltmean.setDescription("saltmean");
            saltmean.setModels(models);
            saltmean.setModelRuns(runsAll);
            saltmean.setGridCoverages(coverages_0);

            this.catalogDAO.save(saltmean);

            GeophysicParamInfo temp = new GeophysicParamInfoImpl();
            temp.setName("temp");
            List<String> tempAliases = new ArrayList<String>();
            tempAliases.add("temperature");
            tempAliases.add("water_temp");
			temp.setAlias(tempAliases);
            temp.setTitle("temp");
            temp.setDescription("temp");
            temp.setModels(models);
            temp.setModelRuns(runsAll);
            List<CoverageInfo> tempCoverages = new ArrayList<CoverageInfo>();
            tempCoverages.addAll(coverages_0);
            tempCoverages.addAll(coverages_1);
            tempCoverages.addAll(coverages_2);
            tempCoverages.addAll(coverages_3);
            tempCoverages.addAll(coverages_5);
            temp.setGridCoverages(tempCoverages);

            this.catalogDAO.save(temp);

            GeophysicParamInfo temperr = new GeophysicParamInfoImpl();
            temperr.setName("temperr");
            temperr.setTitle("temperr");
            temperr.setDescription("temperr");
            temperr.setModels(models);
            temperr.setModelRuns(runsAll);
            temperr.setGridCoverages(coverages_0);

            this.catalogDAO.save(temperr);

            GeophysicParamInfo tempmean = new GeophysicParamInfoImpl();
            tempmean.setName("tempmean");
            tempmean.setTitle("tempmean");
            tempmean.setDescription("tempmean");
            tempmean.setModels(models);
            tempmean.setModelRuns(runsAll);
            tempmean.setGridCoverages(coverages_0);

            this.catalogDAO.save(tempmean);

            GeophysicParamInfo u = new GeophysicParamInfoImpl();
            u.setName("u");
            u.setTitle("u");
            u.setDescription("u");
            u.setModels(models);
            u.setModelRuns(runs_1);
            List<CoverageInfo> uCoverages = new ArrayList<CoverageInfo>();
            uCoverages.addAll(coverages_1);
            uCoverages.addAll(coverages_2);
            uCoverages.addAll(coverages_3);
            u.setGridCoverages(uCoverages);

            this.catalogDAO.save(u);

            GeophysicParamInfo v = new GeophysicParamInfoImpl();
            v.setName("v");
            v.setTitle("v");
            v.setDescription("v");
            v.setModels(models);
            v.setModelRuns(runs_1);
            List<CoverageInfo> vCoverages = new ArrayList<CoverageInfo>();
            vCoverages.addAll(coverages_1);
            vCoverages.addAll(coverages_2);
            vCoverages.addAll(coverages_3);
            v.setGridCoverages(vCoverages);

            this.catalogDAO.save(v);

            List<GeophysicParamInfo> geophysicParamsAll = new ArrayList<GeophysicParamInfo>();
            geophysicParamsAll.add(dynht);
            geophysicParamsAll.add(dynhterr);
            geophysicParamsAll.add(dynhtmean);
            geophysicParamsAll.add(salt);
            geophysicParamsAll.add(salterr);
            geophysicParamsAll.add(saltmean);
            geophysicParamsAll.add(temp);
            geophysicParamsAll.add(temperr);
            geophysicParamsAll.add(u);
            geophysicParamsAll.add(v);

            model.setGeophysicalParameters(geophysicParamsAll);

            this.catalogDAO.update(model);

            ncom.setGeophysicalParameters(geophysicParamsAll);

            this.catalogDAO.update(ncom);

            /** UpdateOAG **/
            List<GeophysicParamInfo> geophysicParams_0 = new ArrayList<GeophysicParamInfo>();
            geophysicParams_0.add(dynht);
            geophysicParams_0.add(dynhterr);
            geophysicParams_0.add(dynhtmean);
            geophysicParams_0.add(salt);
            geophysicParams_0.add(salterr);
            geophysicParams_0.add(saltmean);
            geophysicParams_0.add(temp);
            geophysicParams_0.add(temperr);
            geophysicParams_0.add(tempmean);

            modelRun_0.setGeophysicalParameters(geophysicParams_0);
            
            this.catalogDAO.update(modelRun_0);
            
            coverage_0.setGeophysicalParameters(geophysicParams_0);

            this.catalogDAO.update(coverage_0);

            /** ext-Mercatore **/
            List<GeophysicParamInfo> geophysicParams_1 = new ArrayList<GeophysicParamInfo>();
            geophysicParams_1.add(salt);
            geophysicParams_1.add(temp);
            geophysicParams_1.add(u);
            geophysicParams_1.add(v);

            modelRun_1.setGeophysicalParameters(geophysicParams_1);
            modelRun_2.setGeophysicalParameters(geophysicParams_1);
            modelRun_3.setGeophysicalParameters(geophysicParams_1);
            
            this.catalogDAO.update(modelRun_1);
            this.catalogDAO.update(modelRun_2);
            this.catalogDAO.update(modelRun_3);
            
            coverage_1.setGeophysicalParameters(geophysicParams_1);
            coverage_2.setGeophysicalParameters(geophysicParams_1);
            coverage_3.setGeophysicalParameters(geophysicParams_1);

            this.catalogDAO.update(coverage_1);
            this.catalogDAO.update(coverage_2);
            this.catalogDAO.update(coverage_3);
            
            /** NCOM **/
            List<GeophysicParamInfo> geophysicParams_2 = new ArrayList<GeophysicParamInfo>();
            geophysicParams_2.add(salt);

            modelRun_4.setGeophysicalParameters(geophysicParams_2);
            
            this.catalogDAO.update(modelRun_4);
            
            coverage_4.setGeophysicalParameters(geophysicParams_2);

            this.catalogDAO.update(coverage_4);

            List<GeophysicParamInfo> geophysicParams_3 = new ArrayList<GeophysicParamInfo>();
            geophysicParams_3.add(temp);

            modelRun_5.setGeophysicalParameters(geophysicParams_3);
            
            this.catalogDAO.update(modelRun_5);
            
            coverage_5.setGeophysicalParameters(geophysicParams_3);

            this.catalogDAO.update(coverage_5);

        } catch (Exception e) {
            throw new RuntimeException("Error creating bootstrap configuration", e);
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

}
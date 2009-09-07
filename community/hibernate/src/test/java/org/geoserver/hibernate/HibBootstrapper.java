/*
 */

package org.geoserver.hibernate;

import com.vividsolutions.jts.io.ParseException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.LayerInfo.Type;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.hibernate.beans.WorkspaceInfoImplHb;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.JAIInfo;
import org.geoserver.data.util.CoverageStoreUtils;
import org.geoserver.services.hibernate.beans.GMLInfoImplHb;
import org.geoserver.services.hibernate.beans.WCSInfoImplHb;
import org.geoserver.services.hibernate.beans.WFSInfoImplHb;
import org.geoserver.services.hibernate.beans.WMSInfoImplHb;
import org.geoserver.services.hibernate.beans.WatermarkInfoImplHb;
import org.geoserver.wfs.GMLInfo;
import org.geoserver.wfs.GMLInfo.SrsNameStyle;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wms.WatermarkInfo;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 *
 * @author ETj <etj at geo-solutions.it>
 */
public class HibBootstrapper {

//    private ServiceDAO serviceDAO;
//    private CatalogDAO catalogDAO;

    private Catalog resourceCatalog;
    private GeoServer serviceCatalog;

    public HibBootstrapper(Catalog resourceCatalog, GeoServer serviceCatalog) {
        this.resourceCatalog = resourceCatalog;
        this.serviceCatalog = serviceCatalog;
    }

    public GeoServerInfo createBaseObjects() {

        GeoServerInfo geoserver = createDefaultGeoServer();
        createDefaultServices(geoserver);
        NamespaceInfo defaultNameSpace = createDefaultNamespace();
        WorkspaceInfo defaultWs = createDefaultWorkspace();

        return geoserver;
    }


    public void createSampleCoverages(WorkspaceInfo defaultWs) {
        // Default Raster Style
        StyleInfo rasterSLD = resourceCatalog.getFactory().createStyle();
        rasterSLD.setName("raster");
        rasterSLD.setFilename("raster.sld");

        resourceCatalog.add(rasterSLD);

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

    }

    public NamespaceInfo createDefaultNamespace() {
        NamespaceInfo ns = resourceCatalog.getFactory().createNamespace();
        ns.setPrefix("nurc");
        ns.setURI("http://www.nurc.nato.int");
//        ns.setPrefix("topp");
//        ns.setURI("http://www.opengeo.org");
        resourceCatalog.add(ns);
        resourceCatalog.setDefaultNamespace(ns);
        return ns;
    }

    public WorkspaceInfo createDefaultWorkspace() {
        WorkspaceInfoImplHb ws = (WorkspaceInfoImplHb) resourceCatalog.getFactory().createWorkspace();
        ws.setDefault(Boolean.TRUE);
        ws.setName("nurc");
//        ws.setName("topp");
        resourceCatalog.add(ws);
        resourceCatalog.setDefaultWorkspace(ws);
        return ws;
    }

    public GeoServerInfo createDefaultGeoServer() {
        GeoServerInfo geoserver;
        geoserver = this.serviceCatalog.getFactory().createGlobal();
        geoserver.setContact(this.serviceCatalog.getFactory().createContact());

//        geoserver.setMaxFeatures(10000);
        geoserver.setNumDecimals(8);

//        geoserver.setLoggingLevel("DEFAULT_LOGGING.properties");
//        geoserver.setLoggingLocation("logs/geoserver.log");
//        geoserver.setStdOutLogging(false);

        geoserver.setVerbose(false);
        geoserver.setVerboseExceptions(false);

        //jai
        JAIInfo jai = serviceCatalog.getFactory().createJAI();
        jai.setMemoryCapacity( (Double) 0.5);
        jai.setMemoryThreshold( (Double) 0.75 );
        jai.setTileThreads( (Integer) 5 );
        jai.setTilePriority( (Integer) 5 );
        jai.setImageIOCache( (Boolean) false );
        jai.setJpegAcceleration( (Boolean) false );
        jai.setPngAcceleration( (Boolean) false );
        jai.setRecycling( (Boolean) false );
        jai.setAllowNativeMosaic(false);

        geoserver.setJAI(jai);

        serviceCatalog.setGlobal(geoserver);

        return geoserver;
    }

    private void createDefaultServices(GeoServerInfo geoserver) {

        WFSInfoImplHb wfs = new WFSInfoImplHb();
        wfs.setId("wfs");
        wfs.setName("wfs");
        wfs.setTitle("Test WFS");
        wfs.setEnabled(true);

        wfs.setServiceLevel(WFSInfo.ServiceLevel.COMPLETE);

        // gml2
        GMLInfo gml = new GMLInfoImplHb();
        gml.setSrsNameStyle(SrsNameStyle.NORMAL);
        wfs.getGML().put(WFSInfo.Version.V_10, gml);

        // gml3
        gml = new GMLInfoImplHb();
        gml.setSrsNameStyle(SrsNameStyle.URN);
        wfs.getGML().put(WFSInfo.Version.V_11, gml);
        wfs.setGeoServer(serviceCatalog);

        this.serviceCatalog.add(wfs);

        WMSInfoImplHb wms = new WMSInfoImplHb();
        wms.setName("wms");
        wms.setId("wms");
        wms.setTitle("Test WMS");
        wms.setEnabled(true);
        WatermarkInfo wm = new WatermarkInfoImplHb();
        wm.setEnabled(false);
        wm.setPosition(WatermarkInfo.Position.BOT_RIGHT);
        wms.setWatermark(wm);
        wms.setGeoServer(serviceCatalog);

        this.serviceCatalog.add(wms);

        WCSInfoImplHb wcs = new WCSInfoImplHb();
        wcs.setId("wcs");
        wcs.setName("wcs");
        wcs.setTitle("Test WCS");
        wcs.setEnabled(true);
        wcs.setGeoServer(serviceCatalog);

        this.serviceCatalog.add(wcs);
    }



    private void createDefaultDataSet(final WorkspaceInfo defaultWs, final StyleInfo style)
            throws MismatchedDimensionException, RuntimeException, ParseException {
        // //
        // Defining sample models
        // //
//        Map<String, ModelInfo> sampleModels = createSampleModels();

        try {
            // //
            // Defining sample model-runs
            // //
            /** Model Runs for Mercator **/
//            List<ModelRunInfo> sampleMercatorRuns = createSampleMercatorRuns(sampleModels);
                // CoverageStores
                CoverageStoreInfo mercatorPsy2v3R1v_med_mean_20080924_R20080924_STORE = createSampleCoverageStore("ext-mercatorPsy2v3R1v_med_mean_20080924_R20080924", defaultWs, "/Mercator/20080924/", "ext-mercatorPsy2v3R1v_med_mean_20080924_R20080924", "nc", "NetCDF");
                CoverageStoreInfo mercatorPsy2v3R1v_med_mean_20080925_R20080924_STORE = createSampleCoverageStore("ext-mercatorPsy2v3R1v_med_mean_20080925_R20080924", defaultWs, "/Mercator/20080924/", "ext-mercatorPsy2v3R1v_med_mean_20080925_R20080924", "nc", "NetCDF");
                CoverageStoreInfo mercatorPsy2v3R1v_med_mean_20080926_R20080924_STORE = createSampleCoverageStore("ext-mercatorPsy2v3R1v_med_mean_20080926_R20080924", defaultWs, "/Mercator/20080924/", "ext-mercatorPsy2v3R1v_med_mean_20080926_R20080924", "nc", "NetCDF");
                // Coverages
                CoverageInfo mercatorPsy2v3R1v_med_mean_20080924_R20080924 = createSampleCoverage(
                        style, mercatorPsy2v3R1v_med_mean_20080924_R20080924_STORE, //sampleMercatorRuns.get(0),
                        "ext-mercatorPsy2v3R1v_med_mean_20080924_R20080924_0", "ext-mercatorPsy2v3R1v_med_mean_20080924_R20080924_0",
                        new String[] {}, new Double[] {6.875, 12.0, 42.00975799560547, 44.28925704956055},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 300, 300 }, null,
                        "NetCDF"
                );
                CoverageInfo mercatorPsy2v3R1v_med_mean_20080925_R20080924 = createSampleCoverage(
                        style, mercatorPsy2v3R1v_med_mean_20080925_R20080924_STORE, //sampleMercatorRuns.get(1),
                        "ext-mercatorPsy2v3R1v_med_mean_20080925_R20080924_0", "ext-mercatorPsy2v3R1v_med_mean_20080925_R20080924_0",
                        new String[] {}, new Double[] {6.875, 12.0, 42.00975799560547, 44.28925704956055},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 300, 300 }, null,
                        "NetCDF"
                );
                CoverageInfo mercatorPsy2v3R1v_med_mean_20080926_R20080924 = createSampleCoverage(
                        style, mercatorPsy2v3R1v_med_mean_20080926_R20080924_STORE, //sampleMercatorRuns.get(2),
                        "ext-mercatorPsy2v3R1v_med_mean_20080926_R20080924_0", "ext-mercatorPsy2v3R1v_med_mean_20080926_R20080924_0",
                        new String[] {}, new Double[] {6.875, 12.0, 42.00975799560547, 44.28925704956055},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 300, 300 }, null,
                        "NetCDF"
                );


            /** Model Runs for NRL-NCOM **/
//            List<ModelRunInfo> sampleNRLNCOMRuns20080924 = createSampleNRLNCOMRuns20080924(sampleModels);
                // CoverageStores 20080924
                CoverageStoreInfo salt_m08_nest0_20080924_STORE = createSampleCoverageStore("salt_m08_nest0_20080924", defaultWs, "/NRL/NCOM/20080924/", "salt_m08_nest0_20080924", "nc", "NetCDF");
                CoverageStoreInfo ssh_m08_nest0_20080924_STORE  = createSampleCoverageStore("ssh_m08_nest0_20080924",  defaultWs, "/NRL/NCOM/20080924/", "ssh_m08_nest0_20080924",  "nc", "NetCDF");
                CoverageStoreInfo temp_m08_nest0_20080924_STORE = createSampleCoverageStore("temp_m08_nest0_20080924", defaultWs, "/NRL/NCOM/20080924/", "temp_m08_nest0_20080924", "nc", "NetCDF");
                CoverageStoreInfo uvel_m08_nest0_20080924_STORE = createSampleCoverageStore("uvel_m08_nest0_20080924", defaultWs, "/NRL/NCOM/20080924/", "uvel_m08_nest0_20080924", "nc", "NetCDF");
                CoverageStoreInfo vvel_m08_nest0_20080924_STORE = createSampleCoverageStore("vvel_m08_nest0_20080924", defaultWs, "/NRL/NCOM/20080924/", "vvel_m08_nest0_20080924", "nc", "NetCDF");
                // Coverages 20080924
                CoverageInfo salt_m08_nest0_20080924 = createSampleCoverage(
                        style, salt_m08_nest0_20080924_STORE, //sampleNRLNCOMRuns20080924.get(0),
                        "salt_m08_nest0_20080924_0", "salt_m08_nest0_20080924_0",
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null,
                        "NetCDF"
                );
                CoverageInfo ssh_m08_nest0_20080924 = createSampleCoverage(
                        style, ssh_m08_nest0_20080924_STORE, //sampleNRLNCOMRuns20080924.get(1),
                        "ssh_m08_nest0_20080924_0", "ssh_m08_nest0_20080924_0",
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null,
                        "NetCDF"
                );
                CoverageInfo temp_m08_nest0_20080924 = createSampleCoverage(
                        style, temp_m08_nest0_20080924_STORE, //sampleNRLNCOMRuns20080924.get(2),
                        "temp_m08_nest0_20080924_0", "temp_m08_nest0_20080924_0",
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null,
                        "NetCDF"
                );
                CoverageInfo uvel_m08_nest0_20080924 = createSampleCoverage(
                        style, uvel_m08_nest0_20080924_STORE, //sampleNRLNCOMRuns20080924.get(3),
                        "uvel_m08_nest0_20080924_0", "uvel_m08_nest0_20080924_0",
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null,
                        "NetCDF"
                );
                CoverageInfo vvel_m08_nest0_20080924 = createSampleCoverage(
                        style, vvel_m08_nest0_20080924_STORE, //sampleNRLNCOMRuns20080924.get(4),
                        "vvel_m08_nest0_20080924_0", "vvel_m08_nest0_20080924_0",
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null,
                        "NetCDF"
                );

            /** Model Runs for NRL-NCOM **/
//            List<ModelRunInfo> sampleNRLNCOMRuns20080925 = createSampleNRLNCOMRuns20080925(sampleModels);
                // CoverageStores 20080925
                CoverageStoreInfo salt_m08_nest0_20080925_STORE = createSampleCoverageStore("salt_m08_nest0_20080925", defaultWs, "/NRL/NCOM/20080925/", "salt_m08_nest0_20080925", "nc", "NetCDF");
                CoverageStoreInfo temp_m08_nest0_20080925_STORE = createSampleCoverageStore("temp_m08_nest0_20080925", defaultWs, "/NRL/NCOM/20080925/", "temp_m08_nest0_20080925", "nc", "NetCDF");
                // Coverages 20080925
                CoverageInfo salt_m08_nest0_20080925 = createSampleCoverage(
                        style, salt_m08_nest0_20080925_STORE, //sampleNRLNCOMRuns20080925.get(0),
                        "salt_m08_nest0_20080925_0", "salt_m08_nest0_20080925_0",
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null,
                        "NetCDF"
                );
                CoverageInfo temp_m08_nest0_20080925 = createSampleCoverage(
                        style, temp_m08_nest0_20080925_STORE, //sampleNRLNCOMRuns20080925.get(1),
                        "temp_m08_nest0_20080925_0", "temp_m08_nest0_20080925_0",
                        new String[] {}, new Double[] {0.5, 14.855536460876465, 40.5, 44.496002197265625},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 295, 112 }, null,
                        "NetCDF"
                );

            /** Model Runs for NRL-SWAN **/
//            List<ModelRunInfo> sampleNRLSWANRuns = createSampleNRLSWANRuns(sampleModels);
                // CoverageStores
                CoverageStoreInfo swan_2008092400_STORE = createSampleCoverageStore("SWAN_2008092400", defaultWs, "/NRL/Swan/", "2008092400", "nc", "NetCDF");
                CoverageStoreInfo swan_2008092412_STORE = createSampleCoverageStore("SWAN_2008092412", defaultWs, "/NRL/Swan/", "2008092412", "nc", "NetCDF");
                CoverageStoreInfo swan_2008092500_STORE = createSampleCoverageStore("SWAN_2008092500", defaultWs, "/NRL/Swan/", "2008092500", "nc", "NetCDF");
                // Coverages
                CoverageInfo swan_2008092400 = createSampleCoverage(
                        style, swan_2008092400_STORE, //sampleNRLSWANRuns.get(0),
                        "2008092400_0", "2008092400_0",
                        new String[] {}, new Double[] {6.5, 40.0, 13.5, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 300, 300 }, null,
                        "NetCDF"
                );
                CoverageInfo swan_2008092412 = createSampleCoverage(
                        style, swan_2008092412_STORE, //sampleNRLSWANRuns.get(1),
                        "2008092412_0", "2008092412_0",
                        new String[] {}, new Double[] {6.5, 40.0, 13.5, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 300, 300 }, null,
                        "NetCDF"
                );
                CoverageInfo swan_2008092500 = createSampleCoverage(
                        style, swan_2008092500_STORE, //sampleNRLSWANRuns.get(2),
                        "2008092500_0", "2008092500_0",
                        new String[] {}, new Double[] {6.5, 40.0, 13.5, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 300, 300 }, null,
                        "NetCDF"
                );
            /** Model Runs for INGV-MFS_Sys2b **/
//            List<ModelRunInfo> sampleINGVMREAAnalysesRuns = createSampleINGVMREAAnalysesRuns(sampleModels);
                // CoverageStores
                CoverageStoreInfo INGV_A_MREA08_H_20080924_T_STORE = createSampleCoverageStore("MREA08-H-20080924-T-Analysis", defaultWs, "/INGV/MFS_Sys2b_hourly/Analyses/", "MREA08-H-20080924-T", "nc", "NetCDF");
                CoverageStoreInfo INGV_A_MREA08_H_20080924_U_STORE = createSampleCoverageStore("MREA08-H-20080924-U-Analysis", defaultWs, "/INGV/MFS_Sys2b_hourly/Analyses/", "MREA08-H-20080924-U", "nc", "NetCDF");
                CoverageStoreInfo INGV_A_MREA08_H_20080924_V_STORE = createSampleCoverageStore("MREA08-H-20080924-V-Analysis", defaultWs, "/INGV/MFS_Sys2b_hourly/Analyses/", "MREA08-H-20080924-V", "nc", "NetCDF");
                CoverageStoreInfo INGV_A_MREA08_H_20080925_T_STORE = createSampleCoverageStore("MREA08-H-20080925-T-Analysis", defaultWs, "/INGV/MFS_Sys2b_hourly/Analyses/", "MREA08-H-20080925-T", "nc", "NetCDF");
                CoverageStoreInfo INGV_A_MREA08_H_20080925_U_STORE = createSampleCoverageStore("MREA08-H-20080925-U-Analysis", defaultWs, "/INGV/MFS_Sys2b_hourly/Analyses/", "MREA08-H-20080925-U", "nc", "NetCDF");
                CoverageStoreInfo INGV_A_MREA08_H_20080925_V_STORE = createSampleCoverageStore("MREA08-H-20080925-V-Analysis", defaultWs, "/INGV/MFS_Sys2b_hourly/Analyses/", "MREA08-H-20080925-V", "nc", "NetCDF");
                // Coverages
                CoverageInfo INGV_A_MREA08_H_20080924_T_0 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080924_T_STORE,
                        "MREA08-H-20080924-T_0-Analysis", "MREA08-H-20080924-T_0",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );
                CoverageInfo INGV_A_MREA08_H_20080924_T_1 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080924_T_STORE,
                        "MREA08-H-20080924-T_1-Analysis", "MREA08-H-20080924-T_1",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );

                CoverageInfo INGV_A_MREA08_H_20080924_U_0 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080924_U_STORE, //sampleINGVMREAAnalysesRuns.get(1),
                        "MREA08-H-20080924-U_0-Analysis", "MREA08-H-20080924-U_0",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );
                CoverageInfo INGV_A_MREA08_H_20080924_U_1 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080924_U_STORE, //sampleINGVMREAAnalysesRuns.get(1),
                        "MREA08-H-20080924-U_1-Analysis", "MREA08-H-20080924-U_1",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );

                CoverageInfo INGV_A_MREA08_H_20080924_V_0 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080924_V_STORE, //sampleINGVMREAAnalysesRuns.get(2),
                        "MREA08-H-20080924-V_0-Analysis", "MREA08-H-20080924-V_0",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );
                CoverageInfo INGV_A_MREA08_H_20080924_V_1 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080924_V_STORE, //sampleINGVMREAAnalysesRuns.get(2),
                        "MREA08-H-20080924-V_1-Analysis", "MREA08-H-20080924-V_1",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );


                CoverageInfo INGV_A_MREA08_H_20080925_T_0 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080925_T_STORE, //sampleINGVMREAAnalysesRuns.get(3),
                        "MREA08-H-20080925-T_0-Analysis", "MREA08-H-20080925-T_0",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );
                CoverageInfo INGV_A_MREA08_H_20080925_T_1 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080925_T_STORE, //sampleINGVMREAAnalysesRuns.get(3),
                        "MREA08-H-20080925-T_1-Analysis", "MREA08-H-20080925-T_1",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );

                CoverageInfo INGV_A_MREA08_H_20080925_U_0 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080925_U_STORE, //sampleINGVMREAAnalysesRuns.get(4),
                        "MREA08-H-20080925-U_0-Analysis", "MREA08-H-20080925-U_0",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );
                CoverageInfo INGV_A_MREA08_H_20080925_U_1 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080925_U_STORE, //sampleINGVMREAAnalysesRuns.get(4),
                        "MREA08-H-20080925-U_1-Analysis", "MREA08-H-20080925-U_1",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );

                CoverageInfo INGV_A_MREA08_H_20080925_V_0 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080925_V_STORE, //sampleINGVMREAAnalysesRuns.get(5),
                        "MREA08-H-20080925-V_0-Analysis", "MREA08-H-20080925-V_0",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );
                CoverageInfo INGV_A_MREA08_H_20080925_V_1 = createSampleCoverage(
                        style, INGV_A_MREA08_H_20080925_V_STORE, //sampleINGVMREAAnalysesRuns.get(5),
                        "MREA08-H-20080925-V_1-Analysis", "MREA08-H-20080925-V_1",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );

//            List<ModelRunInfo> sampleINGVMREASymulationRuns = createSampleINGVMREASimulationRuns(sampleModels);
                // CoverageStores
                CoverageStoreInfo INGV_S_MREA08_H_20080924_T_STORE = createSampleCoverageStore("MREA08-H-20080924-T-Simulation", defaultWs, "/INGV/MFS_Sys2b_hourly/Simulation/", "MREA08-H-20080924-T", "nc", "NetCDF");
                CoverageStoreInfo INGV_S_MREA08_H_20080924_U_STORE = createSampleCoverageStore("MREA08-H-20080924-U-Simulation", defaultWs, "/INGV/MFS_Sys2b_hourly/Simulation/", "MREA08-H-20080924-U", "nc", "NetCDF");
                CoverageStoreInfo INGV_S_MREA08_H_20080924_V_STORE = createSampleCoverageStore("MREA08-H-20080924-V-Simulation", defaultWs, "/INGV/MFS_Sys2b_hourly/Simulation/", "MREA08-H-20080924-V", "nc", "NetCDF");
                // Coverages
                CoverageInfo INGV_S_MREA08_H_20080924_T_0 = createSampleCoverage(
                        style, INGV_S_MREA08_H_20080924_T_STORE, //sampleINGVMREASymulationRuns.get(0),
                        "MREA08-H-20080924-T_0-Simulation", "MREA08-H-20080924-T_0",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );
                CoverageInfo INGV_S_MREA08_H_20080924_T_1 = createSampleCoverage(
                        style, INGV_S_MREA08_H_20080924_T_STORE, //sampleINGVMREASymulationRuns.get(0),
                        "MREA08-H-20080924-T_1-Simulation", "MREA08-H-20080924-T_1",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );

                CoverageInfo INGV_S_MREA08_H_20080924_U_0 = createSampleCoverage(
                        style, INGV_S_MREA08_H_20080924_U_STORE, //sampleINGVMREASymulationRuns.get(1),
                        "MREA08-H-20080924-U_0-Simulation", "MREA08-H-20080924-U_0",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );
                CoverageInfo INGV_S_MREA08_H_20080924_U_1 = createSampleCoverage(
                        style, INGV_S_MREA08_H_20080924_U_STORE, //sampleINGVMREASymulationRuns.get(1),
                        "MREA08-H-20080924-U_1-Simulation", "MREA08-H-20080924-U_1",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );

                CoverageInfo INGV_S_MREA08_H_20080924_V_0 = createSampleCoverage(
                        style, INGV_S_MREA08_H_20080924_V_STORE, //sampleINGVMREASymulationRuns.get(2),
                        "MREA08-H-20080924-V_0-Simulation", "MREA08-H-20080924-V_0",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );
                CoverageInfo INGV_S_MREA08_H_20080924_V_1 = createSampleCoverage(
                        style, INGV_S_MREA08_H_20080924_V_STORE, //sampleINGVMREASymulationRuns.get(2),
                        "MREA08-H-20080924-V_1-Simulation", "MREA08-H-20080924-V_1",
                        new String[] {}, new Double[] {6.5, 41.75, 12.3125, 44.5},
                        "EPSG:4326", "GEOGCS[\"WGS84(DD)\", \r\n  DATUM[\"WGS84\", \r\n    SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], \r\n  PRIMEM[\"Greenwich\", 0.0], \r\n  UNIT[\"degree\", 0.017453292519943295], \r\n  AXIS[\"Geodetic longitude\", EAST], \r\n  AXIS[\"Geodetic latitude\", NORTH]]",
                        new int[] { 0, 0 }, new int[] { 94, 45 }, null,
                        "NetCDF"
                );

        } catch (Exception e) {
            throw new RuntimeException("Error creating bootstrap configuration", e);
        }
    }

    private CoverageStoreInfo createSampleCoverageStore(final String coverageStoreName, 
                    final WorkspaceInfo defaultWs, final String coveragePath,
                    final String coverageName, final String coverageExt, final String driverType) {

        CoverageStoreInfo coverageStore = resourceCatalog.getFactory().createCoverageStore();
        coverageStore.setDescription(coverageStoreName);
        coverageStore.setEnabled(true);
        coverageStore.setName(coverageStoreName);
        coverageStore.setType(driverType);
        coverageStore.setURL("file:coverages" + (coveragePath != null ? coveragePath : "/") + coverageName + "." + coverageExt);
        coverageStore.setWorkspace(defaultWs);

        resourceCatalog.add(coverageStore);
//        this.catalogDAO.save(coverageStore);
        return coverageStore;
    }

    private CoverageInfo createSampleCoverage(final StyleInfo style, final CoverageStoreInfo coverageStore,
                final String coverageName, final String coverageNativeName, final String[] keywords,
                final Double[] envelope, final String srsName, final String nativeWKT,
                final int[] gridLow, final int[] gridHigh,
                final Map<String, Double> geoTransform, final String nativeFormat)
            throws FactoryException, MismatchedDimensionException, IndexOutOfBoundsException,
            TransformException, IllegalArgumentException, MalformedURLException, IOException
    {
        CoverageInfo coverage = resourceCatalog.getFactory().createCoverage();
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
//        GeneralGridRange range = new GeneralGridRange(gridLow, gridHigh);
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
//            coverage.setGrid(new GridGeometry2D(range, gridToCRS, crs));
        } else {
//            coverage.setGrid(new GridGeometry2D(range, gridEnvelope));
        }
//        Driver driver = coverageStore.getDriver();
        Map params = new HashMap();
        params.put("url", GeoserverDataDirectory.findDataFile(coverageStore.getURL()).toURI().toURL());
//        CoverageAccess cvAccess_0 = driver.connect(params, null, null);
//        if (cvAccess_0 != null) {
//            CoverageSource cvSource = cvAccess_0.access(new NameImpl(coverage.getNativeName()), null, AccessType.READ_ONLY, null, null);
//            if (cvSource != null) {
//                coverage.setFields(cvSource.getRangeType(null));
//                CoordinateReferenceSystem compundCRS = cvSource.getCoordinateReferenceSystem(null);
//                Set<TemporalGeometricPrimitive> temporalExtent = cvSource.getTemporalDomain(null);
//                CoordinateReferenceSystem temporalCRS = null;
//                CoordinateReferenceSystem verticalCRS = null;
//                if (temporalExtent != null && !temporalExtent.isEmpty()) {
//                    if (compundCRS instanceof CompoundCRS) {
//                        temporalCRS = ((CompoundCRS) compundCRS).getCoordinateReferenceSystems().get(0);
//                    }
//                }
//                Set<org.opengis.geometry.Envelope> verticalExtent = cvSource.getVerticalDomain(false, null);
//                if (verticalExtent != null && !verticalExtent.isEmpty()) {
//                    if (compundCRS instanceof CompoundCRS) {
//                        if (temporalCRS != null)
//                            verticalCRS = ((CompoundCRS) compundCRS).getCoordinateReferenceSystems().get(1);
//                        else
//                            verticalCRS = ((CompoundCRS) compundCRS).getCoordinateReferenceSystems().get(0);
//                    }
//                }
//                coverage.setTemporalCRS(temporalCRS);
//                coverage.setTemporalExtent(temporalExtent);
//                coverage.setVerticalCRS(verticalCRS);
//                coverage.setVerticalExtent(verticalExtent);
//            }
//        }
        coverage.setNativeFormat(nativeFormat);
        coverage.getSupportedFormats().addAll(Arrays.asList(new String[] { "GeoTIFF" }));
        coverage.setDefaultInterpolationMethod("nearest neighbor");
        coverage.getInterpolationMethods().addAll(Arrays.asList(new String[] { "nearest neighbor", "bilinear", "bicubic" }));
        coverage.getRequestSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
        coverage.getResponseSRS().addAll(Arrays.asList(new String[] { "EPSG:4326" }));
        coverage.setEnabled(coverageStore.isEnabled());
        coverage.setNamespace(resourceCatalog.getDefaultNamespace());
        coverage.getMetadata().put("dirName", coverageStore.getName() + "_" + coverage.getName());

        resourceCatalog.add(coverage);
//        this.catalogDAO.save(coverage);

        LayerInfo layer = resourceCatalog.getFactory().createLayer();
        layer.setDefaultStyle(style);
        layer.setEnabled(coverage.isEnabled());
        //layer.setLegend(legend);

        layer.setName(coverage.getName());
        layer.setPath("/");
        layer.setResource(coverage);
        layer.setType(Type.RASTER);

        resourceCatalog.add(layer);
        resourceCatalog.add(coverage);
//        this.catalogDAO.save(layer);
//        this.catalogDAO.update(coverage);

        return coverage;
    }

    public void setCatalog(Catalog catalog) {
        this.resourceCatalog = catalog;
    }

//    public void setCatalogDAO(CatalogDAO catalogDAO) {
//        this.catalogDAO = catalogDAO;
//    }

    public void setGeoServer(GeoServer geoServer) {
        this.serviceCatalog = geoServer;
    }

//    public void setServiceDAO(ServiceDAO serviceDAO) {
//        this.serviceDAO = serviceDAO;
//    }

    
}

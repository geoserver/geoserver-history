/* 
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.services.hibernate.beans.GMLInfoImplHb;
import org.geoserver.services.hibernate.beans.WCSInfoImplHb;
import org.geoserver.services.hibernate.beans.WFSInfoImplHb;
import org.geoserver.services.hibernate.beans.WMSInfoImplHb;
import org.geoserver.services.hibernate.beans.WatermarkInfoImplHb;
import org.geoserver.wfs.GMLInfo;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.GMLInfo.SrsNameStyle;
import org.geoserver.wms.WatermarkInfo;

/**
 * Provides methods to create basic objects with default values.
 * <P>
 * Can be used for tests, or for the real initialization of the GS DB catalog. -- we may want
 * however to init the DB with data read from existing XML files: it would be easier to import
 * legacy files, or to boot GS with a customized set of settings.
 * 
 * @author ETj <etj at geo-solutions.it>
 */
public class HibDefaultsFactoryImpl {

    public static GeoServerInfo createDefaultGeoServer(GeoServer serviceCatalog) {
        GeoServerInfo geoserver = serviceCatalog.getFactory().createGlobal();
        geoserver.setContact(serviceCatalog.getFactory().createContact());

        geoserver.setNumDecimals(8);

        geoserver.setVerbose(false);
        geoserver.setVerboseExceptions(false);

        geoserver.setJAI(serviceCatalog.getFactory().createJAI());

        serviceCatalog.setGlobal(geoserver);
        return geoserver;
    }

    public static void createDefaultServices(GeoServer serviceCatalog, GeoServerInfo geoserver) {

        createWFS(geoserver, serviceCatalog);
        createWMS(geoserver, serviceCatalog);
        createWCS(geoserver, serviceCatalog);
    }

    public static void createWFS(GeoServerInfo geoserver, GeoServer config) {
        WFSInfoImplHb wfs = new WFSInfoImplHb();
        wfs.setId("wfs");
        wfs.setName("wfs");
        wfs.setTitle("Default WFS");
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
        wfs.setGeoServer(config);
        config.add(wfs);
    }

    public static void createWCS(GeoServerInfo geoserver, GeoServer config) {
        WCSInfoImplHb wcs = new WCSInfoImplHb();
        wcs.setId("wcs");
        wcs.setName("wcs");
        wcs.setTitle("Default WCS");
        wcs.setEnabled(true);
        wcs.setGeoServer(config);
        List<String> exceptionFormats = new ArrayList<String>();
        exceptionFormats.add("application/vnd.ogc.se_xml");
        wcs.setExceptionFormats(exceptionFormats);
        config.add(wcs);
    }

    public static void createWMS(GeoServerInfo geoserver, GeoServer config) {
        WMSInfoImplHb wms = new WMSInfoImplHb();
        wms.setName("wms");
        wms.setId("wms");
        wms.setTitle("Default WMS");
        wms.setEnabled(true);
        WatermarkInfo wm = new WatermarkInfoImplHb();
        wm.setEnabled(false);
        wm.setPosition(WatermarkInfo.Position.BOT_RIGHT);
        wms.setWatermark(wm);
        wms.setGeoServer(config);
        config.add(wms);
    }

    public static void createDefaultXXXXspace(GeoServer geoServer) {
        Catalog resourceCatalog = geoServer.getCatalog();

        NamespaceInfo ns = resourceCatalog.getFactory().createNamespace();
        ns.setPrefix("topp");
        ns.setURI("http://www.opengeo.org");
        resourceCatalog.add(ns);
        resourceCatalog.setDefaultNamespace(ns);

        ns = resourceCatalog.getFactory().createNamespace();
        ns.setPrefix("it.geosolutions");
        ns.setURI("http://www.geo-solutions.it");
        resourceCatalog.add(ns);

        WorkspaceInfoImpl ws = (WorkspaceInfoImpl) resourceCatalog.getFactory()
                .createWorkspace();
        ws.setName("topp");
        resourceCatalog.add(ws);
        resourceCatalog.setDefaultWorkspace(ws);

        ws = (WorkspaceInfoImpl) resourceCatalog.getFactory().createWorkspace();
        ws.setName("it.geosolutions");
        resourceCatalog.add(ws);
    }

}

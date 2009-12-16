/*
 */

package org.geoserver.hibernate;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.JAIInfo;
import org.geoserver.services.hibernate.beans.GMLInfoImplHb;
import org.geoserver.wcs.WCSInfoImpl;
import org.geoserver.wfs.GMLInfo;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.GMLInfo.SrsNameStyle;
import org.geoserver.wfs.WFSInfoImpl;
import org.geoserver.wms.WMSInfoImpl;
import org.geoserver.wms.WatermarkInfo;
import org.geoserver.wms.WatermarkInfoImpl;

/**
 * 
 * @author ETj <etj at geo-solutions.it>
 */
public class HibBootstrapper {

    private Catalog resourceCatalog;

    private GeoServer serviceCatalog;

    public HibBootstrapper(Catalog resourceCatalog, GeoServer serviceCatalog) {
        this.resourceCatalog = resourceCatalog;
        this.serviceCatalog = serviceCatalog;
    }

    public GeoServerInfo createBaseObjects() {

        GeoServerInfo geoserver = createDefaultGeoServer();
        createDefaultServices(geoserver);
        // NamespaceInfo defaultNameSpace = createDefaultNamespace();
        // WorkspaceInfo defaultWs = createDefaultWorkspace();

        return geoserver;
    }

    public NamespaceInfo createDefaultNamespace() {
        NamespaceInfo ns = resourceCatalog.getFactory().createNamespace();
        ns.setPrefix("nurc");
        ns.setURI("http://www.nurc.nato.int");
        resourceCatalog.add(ns);
        resourceCatalog.setDefaultNamespace(ns);
        return ns;
    }

    public WorkspaceInfo createDefaultWorkspace() {
        WorkspaceInfoImpl ws = (WorkspaceInfoImpl) resourceCatalog.getFactory()
                .createWorkspace();
        ws.setDefault(Boolean.TRUE);
        ws.setName("nurc");
        resourceCatalog.add(ws);
        resourceCatalog.setDefaultWorkspace(ws);
        return ws;
    }

    public GeoServerInfo createDefaultGeoServer() {
        GeoServerInfo geoserver;
        geoserver = this.serviceCatalog.getFactory().createGlobal();
        geoserver.setContact(this.serviceCatalog.getFactory().createContact());

        geoserver.setNumDecimals(8);

        geoserver.setVerbose(false);
        geoserver.setVerboseExceptions(false);

        // jai
        JAIInfo jai = serviceCatalog.getFactory().createJAI();
        jai.setMemoryCapacity((Double) 0.5);
        jai.setMemoryThreshold((Double) 0.75);
        jai.setTileThreads((Integer) 5);
        jai.setTilePriority((Integer) 5);
        jai.setImageIOCache((Boolean) false);
        jai.setJpegAcceleration((Boolean) false);
        jai.setPngAcceleration((Boolean) false);
        jai.setRecycling((Boolean) false);
        jai.setAllowNativeMosaic(false);

        geoserver.setJAI(jai);

        serviceCatalog.setGlobal(geoserver);

        return geoserver;
    }

    private void createDefaultServices(GeoServerInfo geoserver) {

        WFSInfoImpl wfs = new WFSInfoImpl();
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

        WMSInfoImpl wms = new WMSInfoImpl();
        wms.setName("wms");
        wms.setId("wms");
        wms.setTitle("Test WMS");
        wms.setEnabled(true);
        WatermarkInfo wm = new WatermarkInfoImpl();
        wm.setEnabled(false);
        wm.setPosition(WatermarkInfo.Position.BOT_RIGHT);
        wms.setWatermark(wm);
        wms.setGeoServer(serviceCatalog);

        this.serviceCatalog.add(wms);

        WCSInfoImpl wcs = new WCSInfoImpl();
        wcs.setId("wcs");
        wcs.setName("wcs");
        wcs.setTitle("Test WCS");
        wcs.setEnabled(true);
        wcs.setGeoServer(serviceCatalog);

        this.serviceCatalog.add(wcs);
    }

    public void setCatalog(Catalog catalog) {
        this.resourceCatalog = catalog;
    }

    public void setGeoServer(GeoServer geoServer) {
        this.serviceCatalog = geoServer;
    }

}

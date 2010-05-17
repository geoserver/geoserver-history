/*
 */

package org.geoserver.config.hibernate;

import java.util.logging.Logger;

import org.geoserver.config.ServiceInfo;
import org.geoserver.config.impl.ServiceInfoImpl;
import org.geoserver.hibernate.Hibernable;
import org.geoserver.services.hibernate.beans.GMLInfoImplHb;
import org.geoserver.wcs.WCSInfoImpl;
import org.geoserver.wfs.GMLInfo;
import org.geoserver.wfs.WFSInfoImpl;
import org.geoserver.wfs.WFSInfo.Version;
import org.geoserver.wms.WMSInfoImpl;
import org.geotools.util.logging.Logging;

/**
 * 
 * @author ETj <etj at geo-solutions.it>
 */
public class HibServiceTranslator {
    private static final Logger LOGGER = Logging.getLogger(HibServiceTranslator.class);

    private HibGeoServerFactoryImpl factory;

    public HibServiceTranslator(HibGeoServerFactoryImpl factory) {
        this.factory = factory;
    }

    public ServiceInfoImpl translate(ServiceInfo service) {
        if (service instanceof WCSInfoImpl)
            return translateWCS((WCSInfoImpl) service);
        else if (service instanceof WFSInfoImpl)
            return translateWFS((WFSInfoImpl) service);
        else if (service instanceof WMSInfoImpl)
            return translateWMS((WMSInfoImpl) service);
        else if (service instanceof ServiceInfoImpl)
            return translateSI((ServiceInfoImpl) service);
        else if (service instanceof Hibernable) {
            LOGGER.warning("Will not translate a " + service.getClass().getName());
            return (ServiceInfoImpl) service;
        } else {
            LOGGER.warning("Can't translate a " + service.getClass().getName());
            throw new IllegalArgumentException("Can't translate a " + service.getClass().getName());
        }
    }

    private void copySI(ServiceInfoImpl src, ServiceInfoImpl dest) {
        dest.setId(src.getId());
        dest.setGeoServer(src.getGeoServer());
        dest.setEnabled(src.isEnabled());
        dest.setName(src.getName());
        dest.setTitle(src.getTitle());
        dest.setMaintainer(src.getMaintainer());
        dest.setAbstract(src.getAbstract());
        dest.setAccessConstraints(src.getAccessConstraints());
        dest.setFees(src.getFees());
        dest.setMetadataLink(src.getMetadataLink());
        dest.setCiteCompliant(src.isCiteCompliant());
        dest.setOnlineResource(src.getOnlineResource());
        dest.setSchemaBaseURL(src.getSchemaBaseURL());
        dest.setVerbose(src.isVerbose());
        dest.setOutputStrategy(src.getOutputStrategy());
        dest.setMetadata(src.getMetadata());

        for (Object version : src.getVersions())
            dest.getVersions().add(version);
        for (Object o : src.getKeywords())
            dest.getKeywords().add(o);
        for (Object o : src.getExceptionFormats())
            dest.getExceptionFormats().add(o);

        // ETJ TODO
        // for (Map.Entry entry : (Set<Map.Entry>)src.getMetadata().entrySet() )
        // dest.getMetadata().put(entry.getKey(), entry.getValue());
        // for (Map.Entry entry : (Set<Map.Entry>)src.getClientProperties().entrySet() )
        // dest.getClientProperties().put(entry.getKey(), entry.getValue());
    }

    private ServiceInfoImpl translateSI(ServiceInfoImpl serviceInfo) {
        ServiceInfoImpl ret = (ServiceInfoImpl) factory.createService();
        copySI(serviceInfo, ret);
        return ret;
    }

    private ServiceInfoImpl translateWCS(WCSInfoImpl wcs) {
        WCSInfoImpl ret = new WCSInfoImpl();
        copySI(wcs, ret);
        ret.setGMLPrefixing(wcs.isGMLPrefixing());
        return ret;
    }

    private ServiceInfoImpl translateWFS(WFSInfoImpl wfs) {
        WFSInfoImpl ret = new WFSInfoImpl();
        copySI(wfs, ret);
        ret.setFeatureBounding(wfs.isFeatureBounding());
        ret.setMaxFeatures(wfs.getMaxFeatures());
        ret.setServiceLevel(wfs.getServiceLevel());

        for (Version version : wfs.getGML().keySet()) {
            GMLInfo info = wfs.getGML().get(version);
            GMLInfoImplHb infohb = translateGMLInfo(info);
            ret.getGML().put(version, infohb);
        }

        return ret;
    }

    private GMLInfoImplHb translateGMLInfo(GMLInfo info) {
        GMLInfoImplHb ret = new GMLInfoImplHb();
        ret.setSrsNameStyle(info.getSrsNameStyle());
        return ret;
    }

    private ServiceInfoImpl translateWMS(WMSInfoImpl wms) {
        WMSInfoImpl ret = new WMSInfoImpl();
        copySI(wms, ret);

        ret.setMaxRequestMemory(wms.getMaxRequestMemory());
        ret.setWatermark(wms.getWatermark());
        ret.setInterpolation(wms.getInterpolation());
        for (String srs : wms.getSRS()) {
            ret.getSRS().add(srs);
        }
        ret.setMaxBuffer(wms.getMaxBuffer());
        ret.setMaxRenderingTime(wms.getMaxRenderingTime());
        ret.setMaxRenderingErrors(wms.getMaxRenderingErrors());

        return ret;
    }

//    private WatermarkInfoImplHb translate(WatermarkInfo watermark) {
//        if (watermark == null)
//            return null;
//
//        WatermarkInfoImplHb ret = new WatermarkInfoImplHb();
//        // ret.setId(watermark.get);
//        ret.setEnabled(watermark.isEnabled());
//        ret.setPosition(watermark.getPosition());
//        ret.setTransparency(watermark.getTransparency());
//        ret.setURL(watermark.getURL());
//
//        return ret;
//    }

}

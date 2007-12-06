/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.vfny.geoserver.global.dto.CATALOGDTO;
import org.vfny.geoserver.global.dto.ServiceDTO;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * CATALOG
 *
 * <p>
 * Represents the GeoServer information required to configure an instance of the
 * CATALOG Service. This class holds the currently used configuration and is
 * instantiated initially by the GeoServerPlugIn at start-up, but may be
 * modified by the Configuration Interface during runtime. Such modifications
 * come from the GeoServer Object in the SessionContext.
 * </p>
 *
 * <p>
 * CATALOG catalog = new CATALOG(dto); System.out.println(catalog.getName());
 * System.out.println(catalog.getAbstract());
 * </p>
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public final class CATALOG extends Service {
    /** CATALOG version spec implemented */
    private static final String CATALOG_VERSION = "1.0.0";
    public static final String WEB_CONTAINER_KEY = "CATALOG";

    /** list of WMS Exception Formats */
    private static final String[] EXCEPTION_FORMATS = {
            "application/vnd.ogc.se_xml", "application/vnd.ogc.se_inimage",
            "application/vnd.ogc.se_blank"
        };

    /**
     * CATALOG constructor.
     *
     * <p>
     * Stores the data specified in the CATALOGDTO object in this CATALOG Object for
     * GeoServer to use.
     * </p>
     *
     * @param config
     *            The data intended for GeoServer to use.
     */
    public CATALOG(CATALOGDTO config) {
        super(config.getService());
        setId("catalog");
    }

    /**
     * Creates the CATALOG service by getting the CATALOGDTO object from the
     * config and calling {@link #CATALOG(CATALOGDTO)}.
     *
     * @throws ConfigurationException
     */
    public CATALOG(Config config, Data data, GeoServer geoServer)
        throws ConfigurationException {
        this(defaultCatalogDto());
        setId("catalog");
        setData(data);
        setGeoServer(geoServer);
    }

    /**
     * CATALOG constructor.
     *
     * <p>
     * Package constructor intended for default use by GeoServer
     * </p>
     *
     * @see GeoServer#GeoServer()
     */
    CATALOG() {
        super(new ServiceDTO());
        setId("catalog");
    }

    /**
     * load purpose.
     *
     * <p>
     * Loads a new data set into this object.
     * </p>
     *
     * @param config
     */
    public void load(CATALOGDTO config) {
        super.load(config.getService());
    }

    /**
     * Implement toDTO.
     *
     * <p>
     * Package method used by GeoServer. This method may return references, and
     * does not clone, so extreme caution sould be used when traversing the
     * results.
     * </p>
     *
     * @return CATALOGDTO An instance of the data this class represents. Please see
     *         Caution Above.
     *
     * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
     * @see CATALOGDTO
     */
    public Object toDTO() {
        CATALOGDTO dto = new CATALOGDTO();
        dto.setService((ServiceDTO) super.toDTO());

        return dto;
    }

    /**
     * Returns the version of this CATALOG Instance.
     *
     * @return static version name
     */
    public static String getVersion() {
        return CATALOG_VERSION;
    }

    /**
     * getExceptionFormats purpose.
     *
     * <p>
     * Returns a static list of Exception Formats in as Strings
     * </p>
     *
     * @return String[] a static list of Exception Formats
     */
    public String[] getExceptionFormats() {
        return EXCEPTION_FORMATS;
    }

    /**
     * This is a very poor, but effective tempory method of setting a
     * default service value for WCS, until we get a new config system.
     *
     * @return
     */
    private static CATALOGDTO defaultCatalogDto() {
        CATALOGDTO dto = new CATALOGDTO();
        ServiceDTO service = new ServiceDTO();
        service.setName("Catalog Service");
        service.setTitle("Catalog Service");
        service.setEnabled(true);

        ArrayList keywords = new ArrayList();
        keywords.add("CATALOG");
        keywords.add("GEOSERVER");
        service.setKeywords(keywords);

        MetaDataLink mdl = new MetaDataLink();
        mdl.setAbout("http://geoserver.org");
        mdl.setType("undef");
        mdl.setMetadataType("other");
        mdl.setContent("NONE");
        service.setMetadataLink(mdl);
        service.setFees("NONE");
        service.setAccessConstraints("NONE");
        service.setMaintainer(
            "http://jira.codehaus.org/secure/BrowseProject.jspa?id=10311");

        try {
            service.setOnlineResource(new URL("http://geoserver.org"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        dto.setService(service);

        return dto;
    }
}

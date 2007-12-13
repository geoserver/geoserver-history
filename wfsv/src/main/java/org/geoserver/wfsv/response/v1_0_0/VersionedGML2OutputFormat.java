/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv.response.v1_0_0;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.ResultTypeType;

import org.geoserver.ows.util.OwsUtils;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.Operation;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.xml.GML2OutputFormat;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;


/**
 * Encodes features in Geographic Markup Language (GML) version 2 adding the
 * versioning attributes to the mix.
 *
 * <p>
 * GML2-GZIP format is just GML2 with gzip compression. If GML2-GZIP format was
 * requested, <code>getContentEncoding()</code> will retutn
 * <code>"gzip"</code>, otherwise will return <code>null</code>
 * </p>
 *
 * @author Gabriel Rold?n
 * @author Andrea Aime
 * @version $Id$
 */
public class VersionedGML2OutputFormat extends GML2OutputFormat {
    /**
     * Creates the producer with a reference to the GetFeature operation
     * using it.
     */
    public VersionedGML2OutputFormat(WFS wfs, GeoServer geoServer, Data catalog) {
        super(wfs, geoServer, catalog);
    }
    
    protected String wfsSchemaLocation(WFS wfs, String baseUrl) {
        return ResponseUtils.appendPath(RequestUtils.proxifiedBaseURL(baseUrl, wfs.getGeoServer().getProxyBaseUrl()),
                "schemas/wfs/1.0.0/WFS-versioning.xsd");
    }

    protected String typeSchemaLocation(WFS wfs, FeatureTypeInfo meta, String baseUrl) {
        final String proxifiedBase = RequestUtils.proxifiedBaseURL(baseUrl, wfs.getGeoServer().getProxyBaseUrl());
        return ResponseUtils.appendQueryString(proxifiedBase + "wfs",
            "service=WFSV&version=1.0.0&request=DescribeVersionedFeatureType&typeName=" + meta.getName());
    }
    
    
    
    public boolean canHandle(Operation operation) {
        // GetVersionedFeature operation?
        if ("GetVersionedFeature".equalsIgnoreCase(operation.getId())) {
            // also check that the resultType is "results"
            GetFeatureType request = (GetFeatureType) OwsUtils.parameter(
                    operation.getParameters(), GetFeatureType.class);

            if (request.getResultType() == ResultTypeType.RESULTS_LITERAL) {
                return true;
            }
        }

        return false;
    }

}

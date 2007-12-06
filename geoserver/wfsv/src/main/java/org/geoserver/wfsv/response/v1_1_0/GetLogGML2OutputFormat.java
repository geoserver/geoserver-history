/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv.response.v1_1_0;

import net.opengis.wfs.ResultTypeType;
import net.opengis.wfsv.GetLogType;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.platform.Operation;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.xml.GML2OutputFormat;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;


public class GetLogGML2OutputFormat extends GML2OutputFormat {
    public GetLogGML2OutputFormat(WFS wfs, GeoServer geoserver, Data catalog) {
        super(wfs, geoserver, catalog);
    }

    public boolean canHandle(Operation operation) {
        //GetFeature operation?
        if ("GetLog".equalsIgnoreCase(operation.getId())) {
            //also check that the resultType is "results"
            GetLogType request = (GetLogType) OwsUtils.parameter(operation
                    .getParameters(), GetLogType.class);

            return request.getResultType() == ResultTypeType.RESULTS_LITERAL;
        }

        return false;
    }
}

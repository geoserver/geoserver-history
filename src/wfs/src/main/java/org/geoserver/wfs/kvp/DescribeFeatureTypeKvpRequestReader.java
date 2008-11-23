/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.kvp;

import net.opengis.wfs.DescribeFeatureTypeType;
import java.util.Map;

import org.geoserver.ows.util.RequestUtils;
import org.geoserver.wfs.WebFeatureService;
import org.geotools.util.Version;


public class DescribeFeatureTypeKvpRequestReader extends WFSKvpRequestReader {
    WebFeatureService wfs;
    
    public DescribeFeatureTypeKvpRequestReader(WebFeatureService wfs) {
        super(DescribeFeatureTypeType.class);
        this.wfs = wfs;
    }

    public Object read(Object request, Map kvp, Map rawKvp) throws Exception {
        //let super do its thing
        request = super.read(request, kvp, rawKvp);

        //do an additional check for outputFormat, because the default 
        // in wfs 1.1 is not the default for wfs 1.0
        DescribeFeatureTypeType describeFeatureType = (DescribeFeatureTypeType) request;

        if (!describeFeatureType.isSetOutputFormat()) {
            Version version = RequestUtils.version( describeFeatureType.getVersion());
            version = RequestUtils.matchHighestVersion(version, wfs.getVersions());
            
            if (!version.toString().equals( "1.0.0")) {
                //set 1.1 default
                describeFeatureType.setOutputFormat("text/xml; subtype=gml/3.1.1");
            } else {
                //set 1.0 default
                describeFeatureType.setOutputFormat("XMLSCHEMA");
            }
        }

        return request;
    }
}

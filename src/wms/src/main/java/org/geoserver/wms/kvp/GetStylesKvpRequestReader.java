/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.kvp;

import org.geoserver.ows.KvpRequestReader;
import org.geoserver.wms.WMS;
import org.vfny.geoserver.wms.requests.GetStylesRequest;

public class GetStylesKvpRequestReader extends KvpRequestReader {

    WMS wms;

    public GetStylesKvpRequestReader(WMS wms) {
        super(GetStylesRequest.class);
        this.wms = wms;
    }

    
    @Override
    public Object createRequest() throws Exception {
        return new GetStylesRequest(wms);
    }
}

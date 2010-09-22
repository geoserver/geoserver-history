/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.kvp;

import java.util.Map;

import org.geoserver.ows.KvpRequestReader;
import org.geoserver.wms.GetCapabilitiesRequest;

/**
 * This utility reads in a GetCapabilities KVP request and turns it into an appropriate internal
 * CapabilitiesRequest object, upon request.
 * 
 * @author Rob Hranac, TOPP
 * @author Gabriel Roldan
 * @version $Id$
 */
public class CapabilitiesKvpReader extends KvpRequestReader {

    public CapabilitiesKvpReader() {
        super(GetCapabilitiesRequest.class);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public GetCapabilitiesRequest read(Object req, Map kvp, Map rawKvp) throws Exception {
        GetCapabilitiesRequest request = (GetCapabilitiesRequest) super.read(req, kvp, rawKvp);
        request.setRawKvp(rawKvp);
        
        if (null == request.getVersion() || request.getVersion().length() == 0) {
            String version = (String) rawKvp.get("WMTVER");
            request.setVersion(version);
        }
        return request;
    }

}

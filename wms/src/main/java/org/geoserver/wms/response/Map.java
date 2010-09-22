/* Copyright (c) 2010 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.response;

import java.util.HashMap;

public abstract class Map {

    private String mimeType;

    private java.util.Map<String, String> responseHeaders;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setResponseHeader(String name, String value) {
        if (responseHeaders == null) {
            responseHeaders = new HashMap<String, String>();
        }
        responseHeaders.put(name, value);
    }

    public String[][] getResponseHeaders() {
        if (responseHeaders == null || responseHeaders.size() > 0) {
            return null;
        }
        String[][] headers = new String[responseHeaders.size()][2];
        int index = 0;
        for (java.util.Map.Entry<String, String> entry : responseHeaders.entrySet()) {
            headers[index][0] = entry.getKey();
            headers[index][1] = entry.getValue();
            index++;
        }
        return headers;
    }
}

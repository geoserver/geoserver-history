/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.impl;

import java.io.IOException;

import org.geoserver.catalog.CatalogVisitor;
import org.geoserver.catalog.WMSLayerInfo;
import org.geoserver.catalog.WMSStoreInfo;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.opengis.util.ProgressListener;

@SuppressWarnings("serial")
public class WMSLayerInfoImpl extends ResourceInfoImpl implements WMSLayerInfo {

    public WMSLayerInfoImpl(CatalogImpl catalog) {
        super(catalog);
    }

    public Layer getWMSLayer(ProgressListener listener) throws IOException {
        // check which actual name we have to use
        String name = getName();
        if (getNativeName() != null) {
            name = getNativeName();
        }

        WMSCapabilities caps = getStore().getWebMapServer(listener).getCapabilities();
        for (Layer layer : caps.getLayerList()) {
            if (name.equals(layer.getName())) {
                return layer;
            }
        }

        throw new IOException("Could not find layer " + getName()
                + " in the server capabilitiles document");
    }

    public void accept(CatalogVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public WMSStoreInfo getStore() {
        return (WMSStoreInfo) super.getStore();
    }

}

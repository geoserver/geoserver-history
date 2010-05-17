/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geoserver.wms.MapLayerInfo;
import org.geoserver.wms.WMS;


/**
 * Holds the pre-validated parameters of a <code>DescribeLayer</code> request.
 *
 * <p>
 * This pre-validation must to be done by the request reader, so the content
 * of this object is assumed to be valid.
 * </p>
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class DescribeLayerRequest extends WMSRequest {

    /**
     * Creates a new DescribeLayerRequest
     * @param wms The WMS config object.
     */
    public DescribeLayerRequest(WMS wms) {
        super("DescribeLayer", wms );
    }
    
    /**
     * Holds the FeatureTypes parsed from the
     * request's <code>LAYERS</code> parameter.
     */
    private List layers = new ArrayList(2);

    public void addLayer(MapLayerInfo layer) {
        if (layer == null) {
            throw new NullPointerException();
        }

        layers.add(layer);
    }

    public List getLayers() {
        return new ArrayList(layers);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("DescribeLayerRequesr[layers=");

        for (Iterator it = layers.iterator(); it.hasNext();) {
            sb.append(((MapLayerInfo) it.next()).getName());

            if (it.hasNext()) {
                sb.append(',');
            }
        }

        sb.append(']');

        return sb.toString();
    }
}

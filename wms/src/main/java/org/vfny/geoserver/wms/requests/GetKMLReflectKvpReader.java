/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import com.vividsolutions.jts.geom.Envelope;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.servlets.WMService;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


/**
 * GetKMLReflectKvpReader:
 *
 * This class is a refinement of GetMapKvpReader. It just moves some
 * of the mandatory parameters to "optional" parameters. This is to allow
 * the kml reflector (KMLReflector) to accept brief/simple requests and it
 * will fill in the rest of the information.
 *
 * @author Brent Owens
 *
 */
public class GetKMLReflectKvpReader extends GetMapKvpReader {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.requests.readers.wms");

    public GetKMLReflectKvpReader(Map kvpPairs, WMService service) {
        super(kvpPairs, service);
        setStylesRequired(false);
    }

    /**
     * Optional parameters are:
     * width
     * height
     * format
     */
    public void parseOptionalParameters(GetMapRequest request)
        throws WmsException {
        super.parseOptionalParameters(request);

        // pulled out some of the mandatory params from superclass and made them optional:
        if (keyExists("WIDTH") && keyExists("HEIGHT")) {
            try {
                int width = Integer.parseInt(getValue("WIDTH"));
                int height = Integer.parseInt(getValue("HEIGHT"));
                request.setWidth(width);
                request.setHeight(height);
            } catch (NumberFormatException ex) {
                throw new WmsException(
                    "WIDTH and HEIGHT incorrectly specified, they must be integers");
            }
        }

        // FORMAT parameter, this might be KML, KMZ, or an image
        if (keyExists("FORMAT")) {
            String format = getValue("FORMAT");
            request.setFormat(format);
        }
    }

    /**
     * Mandatory parameters are 'bbox' and 'layers'. Styles are optional, but they
     * are parsed at the same time as layers.
     */
    public void parseMandatoryParameters(GetMapRequest request, boolean parseStylesLayers)
        throws WmsException {
        Envelope bbox = parseBbox(getValue("BBOX"));
        request.setBbox(bbox);

        //let styles and layers parsing for the end to give more trivial parameters 
        //a chance to fail before incurring in retrieving the SLD or SLD_BODY
        if (parseStylesLayers) {
            parseLayersAndStyles(request);
        }
    }

    /**
     * Changed from the parent class to allow for missing style parameter.
     * The parameter is optional now.
     */
    protected void parseLayersAndStyles(GetMapRequest request)
        throws WmsException {
        String sldParam = getValue("SLD");
        String sldBodyParam = getValue("SLD_BODY");

        if (sldBodyParam != null) {
            LOGGER.fine("Getting layers and styles from SLD_BODY");
            parseSldBodyParam(request);
        } else if (sldParam != null) {
            LOGGER.fine("Getting layers and styles from reomte SLD");
            parseSldParam(request);
        } else {
            MapLayerInfo[] featureTypes = parseLayersParam(request);
            List styles = null;
            request.setLayers(featureTypes);

            if (isStylesRquired()) {
                styles = parseStylesParam(request, featureTypes);
                request.setStyles(styles);
            } else {
                if (keyExists("STYLES")) {
                    styles = parseStylesParam(request, featureTypes);
                    request.setStyles(styles);
                }
            }
        }
    }
}

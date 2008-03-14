/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.requests.DescribeLayerRequest;
import org.vfny.geoserver.wms.requests.GetFeatureInfoRequest;
import org.vfny.geoserver.wms.requests.GetLegendGraphicRequest;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.requests.WMSCapabilitiesRequest;
import org.vfny.geoserver.wms.responses.DescribeLayerResponse;
import org.vfny.geoserver.wms.responses.GetFeatureInfoResponse;
import org.vfny.geoserver.wms.responses.GetLegendGraphicResponse;
import org.vfny.geoserver.wms.responses.GetMapResponse;
import org.vfny.geoserver.wms.responses.WMSCapabilitiesResponse;
import org.vfny.geoserver.wms.servlets.Capabilities;
import org.vfny.geoserver.wms.servlets.DescribeLayer;
import org.vfny.geoserver.wms.servlets.GetFeatureInfo;
import org.vfny.geoserver.wms.servlets.GetLegendGraphic;
import org.vfny.geoserver.wms.servlets.GetMap;

import com.vividsolutions.jts.geom.Envelope;

public class DefaultWebMapService implements WebMapService,
        ApplicationContextAware {
    /**
     * default for 'format' parameter.
     */
    public static String FORMAT = "image/png";

    /**
     * default for 'styles' parameter.
     */
    public static List STYLES = Collections.EMPTY_LIST;

    /**
     * default for 'height' parameter.
     */
    public static int HEIGHT = 512;

    /**
     * default for 'height' parameter.
     */
    public static int WIDTH = 512;

    /**
     * default for 'srs' parameter.
     */
    public static String SRS = "EPSG:4326";

    /**
     * default for 'transparent' parameter.
     */
    public static Boolean TRANSPARENT = Boolean.TRUE;

    /**
     * default for 'bbox' paramter
     */
    public static ReferencedEnvelope BBOX = new ReferencedEnvelope(
            new Envelope(-180, 180, -90, 90), DefaultGeographicCRS.WGS84);

    /**
     * The current getMap request
     */
    GetMapRequest getMap = null;

    /**
     * Application context
     */
    ApplicationContext context;

    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        this.context = context;
    }

    public WMSCapabilitiesResponse getCapabilities(
            WMSCapabilitiesRequest request) {
        Capabilities capabilities = (Capabilities) context
                .getBean("wmsGetCapabilities");

        return (WMSCapabilitiesResponse) capabilities.getResponse();
    }

    public WMSCapabilitiesResponse capabilities(WMSCapabilitiesRequest request) {
        return getCapabilities(request);
    }

    public DescribeLayerResponse describeLayer(DescribeLayerRequest request) {
        DescribeLayer describeLayer = (DescribeLayer) context
                .getBean("wmsDescribeLayer");

        return (DescribeLayerResponse) describeLayer.getResponse();
    }

    public GetMapResponse getMap(GetMapRequest request) {
        GetMap getMap = (GetMap) context.getBean("wmsGetMap");

        return (GetMapResponse) getMap.getResponse();
    }

    public GetMapResponse map(GetMapRequest request) {
        return getMap(request);
    }

    public GetFeatureInfoResponse getFeatureInfo(GetFeatureInfoRequest request) {
        GetFeatureInfo getFeatureInfo = 
            (GetFeatureInfo) context.getBean("wmsGetFeatureInfo");

        return (GetFeatureInfoResponse) getFeatureInfo.getResponse();
    }

    public GetLegendGraphicResponse getLegendGraphic(
            GetLegendGraphicRequest request) {
        GetLegendGraphic getLegendGraphic = 
            (GetLegendGraphic) context.getBean("wmsGetLegendGraphic");

        return (GetLegendGraphicResponse) getLegendGraphic.getResponse();
    }

    // refector operations
    public GetMapResponse reflect(GetMapRequest request) {
        return getMapReflect(request);
    }

    public GetMapResponse getMapReflect(GetMapRequest request) {
        getMap = (GetMapRequest) request;

        // set the defaults
        if (getMap.getFormat() == null) {
            getMap.setFormat(FORMAT);
        }

        if ((getMap.getStyles() == null) || getMap.getStyles().isEmpty()) {
            // set styles to be the defaults for the specified layers
            // TODO: should this be part of core WMS logic? is so lets throw
            // this
            // into the GetMapKvpRequestReader
            if ((getMap.getLayers() != null) && (getMap.getLayers().length > 0)) {
                ArrayList styles = new ArrayList(getMap.getLayers().length);

                for (int i = 0; i < getMap.getLayers().length; i++) {
                    styles.add(getMap.getLayers()[i].getDefaultStyle());
                }

                getMap.setStyles(styles);
            } else {
                getMap.setStyles(STYLES);
            }
        }

        this.autoSetBoundsAndSize();

        return getMap(getMap);
    }

    /**
     * This method tries to automatically determine SRS, bounding box and output
     * size based on the layers provided by the user and any other parameters.
     * 
     * If bounds are not specified by the user, they are automatically se to the
     * union of the bounds of all layers.
     * 
     * The size of the output image defaults to 512 pixels, the height is
     * automatically determined based on the width to height ratio of the
     * requested layers. This is also true if either height or width are
     * specified by the user. If both height and width are specified by the
     * user, the automatically determined bounding box will be adjusted to fit
     * inside these bounds.
     */
    private void autoSetBoundsAndSize() {
        // Get the layers
        MapLayerInfo[] layers = getMap.getLayers();

        // Determine the SRS first
        String reqSRS = getMap.getSRS();
        CoordinateReferenceSystem reqCRS = getMap.getCrs();
        boolean useNativeBounds = true;
        if (getMap.getSRS() == null || reqSRS.equalsIgnoreCase("EPSG:4326")) {
            useNativeBounds = false;
            reqSRS = "EPSG:4326";
            getMap.setSRS(reqSRS);
            try {
                reqCRS = CRS.decode(reqSRS);
            } catch (NoSuchAuthorityCodeException e) {
                e.printStackTrace();
            } catch (FactoryException e) {
                e.printStackTrace();
            }
            getMap.setCrs(reqCRS);
        } else {
            for (int i = 0; useNativeBounds && i < layers.length; i++) {
                if (layers[i] == null) {
                    continue;
                } else if (useNativeBounds = layers[i].getFeature() != null) {
                    useNativeBounds = layers[i].getFeature().getSRS()
                            .equalsIgnoreCase(reqSRS);
                } else if (layers[i].getRemoteFeatureSource() != null) {
                    // Not sure about this, but how to find the SRS?
                    useNativeBounds = false;
                } else if (layers[i].getCoverage() != null) {
                    useNativeBounds = false;
                } else {
                    // ?
                }
            }
        }

        // Ready to determine the bounds based on the layers, if not specified
        Envelope layerbbox = null;

        boolean specifiedBbox = (getMap.getBbox() != null);

        if (specifiedBbox) {
            layerbbox = getMap.getBbox();
        } else {
            // Get the bounding box from the layers
            for (int i = 0; i < layers.length; i++) {
                Envelope curbbox = null;

                FeatureTypeInfo curFTI = layers[i].getFeature();
                try {
                    if (curFTI != null) {
                        // Local feature type
                        if (useNativeBounds) {
                            curbbox = curFTI.getLatLongBoundingBox();
                        } else {
                            curbbox = curFTI.getBoundingBox();
                        }

                    } else if (layers[i].getRemoteFeatureSource() != null) {
                        // This layer was requested through a remote SLD or
                        // something similar
                        curbbox = 
                            layers[i].getRemoteFeatureSource().getBounds();

                    } else if (layers[i].getCoverage() != null) {
                        // This is a coverage?
                        // The following is awfully convoluted... too many
                        // Envelope classes
                        GeneralEnvelope genEnv = layers[i].getCoverage()
                                .getWGS84LonLatEnvelope();
                        double[] ll = genEnv.getLowerCorner().getCoordinates();
                        double[] ur = genEnv.getUpperCorner().getCoordinates();
                        curbbox = new Envelope(ll[0], ll[1], ur[0], ur[1]);
                    } else {
                        // ?
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (i == 0) {
                    layerbbox = new Envelope(curbbox);
                } else {
                    layerbbox.expandToInclude(curbbox);
                }
            }
            // Reproject if we have to
            if (!useNativeBounds) {
                try {
                    ReferencedEnvelope ref = new ReferencedEnvelope(layerbbox,
                            CRS.decode("EPSG:4326"));
                    layerbbox = ref.transform(reqCRS, true);
                } catch (NoSuchAuthorityCodeException e) {
                    e.printStackTrace();
                } catch (TransformException e) {
                    e.printStackTrace();
                } catch (FactoryException e) {
                    e.printStackTrace();
                }
            }
        }

        // Just in case
        if (layerbbox == null)
            layerbbox = BBOX;

        double bbheight = layerbbox.getHeight();
        double bbwidth = layerbbox.getWidth();
        double bbratio = bbwidth / bbheight;

        if (!specifiedBbox) {
            // Zoom out 4%, accomodates for reprojection etc
            layerbbox.expandBy(layerbbox.getWidth() / 50,
                    layerbbox.getHeight() / 50);
        }

        double mheight = getMap.getHeight();
        double mwidth = getMap.getWidth();

        if (mheight > 0.5 && mwidth > 0.5 && specifiedBbox) {
            // This person really doesnt want our help
        } else {
            if (mheight > 0.5 && mwidth > 0.5) {
                // Fully specified, need to adjust bbox
                double mratio = mwidth / mheight;
                // Adjust bounds to be less than ideal to meet spec
                if (bbratio > mratio) {
                    // Too wide, need to increase height of bb
                    double diff = ((bbwidth / mratio) - bbheight) / 2;
                    layerbbox.expandBy(0, diff);
                } else {
                    // Too tall, need to increase width of bb
                    double diff = ((bbheight * mratio) - bbwidth) / 2;
                    layerbbox.expandBy(diff, 0);
                }
            } else if (mheight > 0.5) {
                mwidth = bbratio * mheight;
            } else {
                if (mwidth > 0.5) {
                    // We're set
                } else {
                    // Fall through to the default
                    mwidth = WIDTH;
                }
                mheight = mwidth / bbratio;
            }

            // Actually set the bounding box and size of image
            getMap.setBbox(layerbbox);
            getMap.setWidth((int) mwidth);
            getMap.setHeight((int) mheight);
        }
    }
}

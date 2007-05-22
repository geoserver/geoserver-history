/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.wms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.opengis.spatialschema.geometry.MismatchedDimensionException;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.action.HTMLEncoder;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.form.wms.WMSContentForm;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.UserContainer;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WMSContentAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, UserContainer user,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        WMSContentForm contentForm = (WMSContentForm) form;

        Locale locale = (Locale) request.getLocale();
        MessageResources messages = getResources(request);
        final String SUBMIT = HTMLEncoder.decode(messages.getMessage(locale, "label.submit"));
        final String GENERATE_BBOX = HTMLEncoder.decode(messages.getMessage(locale,
                    "config.data.calculateBoundingBox.label"));
        final String ADD_NEW_LAYERGROUP = "Add New Layer-Group";
        final String REMOVE_LAYERGROUP = "Remove";

        String action = contentForm.getAction();

        boolean enabled = contentForm.isEnabled();

        if (contentForm.isEnabledChecked() == false) {
            enabled = false;
        }

        String onlineResource = contentForm.getOnlineResource();

        WMSConfig config = getWMSConfig();

        config.setEnabled(enabled);
        config.setOnlineResource(new URL(onlineResource));

        if (SUBMIT.equals(action)) {
            return executeSubmit(mapping, action, contentForm, config, request, response);
        } else if (ADD_NEW_LAYERGROUP.equals(action)) {
            return executeAddNewLayerGroup(mapping, action, contentForm, config, request, response);
        } else if (action.startsWith(REMOVE_LAYERGROUP)) {
            return executeRemoveLayerGroup(mapping, action, contentForm, config, request, response);
        } else if (action.startsWith(GENERATE_BBOX)) {
            return executeGenerateBBOX(mapping, action, contentForm, config, request, response);
        }

        return mapping.findForward("config.wms.content");
    }

    private ActionForward executeGenerateBBOX(ActionMapping mapping, String action,
        WMSContentForm contentForm, WMSConfig config, HttpServletRequest request,
        HttpServletResponse response) {
        HashMap layerMap = new HashMap();
        HashMap styleMap = new HashMap();
        HashMap envelopeMap = new HashMap();

        if (action.indexOf(" ") <= 0) {
            return mapping.findForward("config.wms.content");
        }

        final String selectedLayer = action.substring(action.indexOf(" ") + 1, action.length());
        int selectedLayerIndex = -1;

        int bmi;
        Iterator it;

        for (bmi = 0, it = contentForm.getBaseMapTitles().iterator(); it.hasNext(); bmi++) {
            String baseMapTitle = (String) it.next();

            String baseMapLayers = (String) contentForm.getBaseMapLayers().get(bmi);
            String baseMapStyles = (String) contentForm.getBaseMapStyles().get(bmi);
            GeneralEnvelope envelope = (GeneralEnvelope) contentForm.getBaseMapEnvelopes().get(bmi);

            /*
             * System.out.println("******************* contentAction: title=" +
             * baseMapTitle + ", layers=" + baseMapLayers + ", styles=" +
             * baseMapStyles);
             */
            layerMap.put(baseMapTitle, baseMapLayers);
            styleMap.put(baseMapTitle, baseMapStyles);
            envelopeMap.put(baseMapTitle, envelope);

            if (selectedLayer.equals(baseMapTitle)) {
                selectedLayerIndex = bmi;

                Data catalog = (Data) getServlet().getServletContext()
                                          .getAttribute(Data.WEB_CONTAINER_KEY);
                GeneralEnvelope selectedEnvelope = null;
                String[] layerNames = baseMapLayers.split(",");

                for (int i = 0; i < layerNames.length; i++) {
                    String layerName = layerNames[i].trim();

                    Integer layerType = (Integer) catalog.getLayerType(layerName);

                    /*layerType = (layerType != null) ? layerType
                                                    : (Integer) catalog.getLayerNames()
                                                                       .get(layerName.substring(layerName
                                .indexOf(":") + 1, layerName.length()));*/
                    if (layerType != null) {
                        if (layerType.intValue() == MapLayerInfo.TYPE_VECTOR) {
                            FeatureTypeInfo ftype = catalog.getFeatureTypeInfo(layerName);
                            ftype = ((ftype != null) ? ftype
                                                     : catalog.getFeatureTypeInfo(layerName
                                    .substring(layerName.indexOf(":") + 1, layerName.length())));

                            if (selectedEnvelope == null) {
                                ReferencedEnvelope ftEnvelope = null;

                                try {
                                    if (ftype.getBoundingBox() instanceof ReferencedEnvelope) {
                                        ftEnvelope = (ReferencedEnvelope) ftype.getBoundingBox();
                                    } else {
                                        // TODO Add Action Errors
                                        return mapping.findForward("config.wms.content");
                                    }
                                } catch (IOException e) {
                                    // TODO Add Action Errors
                                    return mapping.findForward("config.wms.content");
                                }

                                selectedEnvelope = new GeneralEnvelope(new double[] {
                                            ftEnvelope.getMinX(), ftEnvelope.getMinY()
                                        },
                                        new double[] { ftEnvelope.getMaxX(), ftEnvelope.getMaxY() });
                                selectedEnvelope.setCoordinateReferenceSystem(ftEnvelope
                                    .getCoordinateReferenceSystem());
                            } else {
                                final CoordinateReferenceSystem dstCRS = selectedEnvelope
                                    .getCoordinateReferenceSystem();

                                ReferencedEnvelope ftEnvelope = null;

                                try {
                                    if (ftype.getBoundingBox() instanceof ReferencedEnvelope) {
                                        ftEnvelope = (ReferencedEnvelope) ftype.getBoundingBox();
                                        ftEnvelope.transform(dstCRS, true);
                                    } else {
                                        // TODO Add Action Errors
                                        return mapping.findForward("config.wms.content");
                                    }
                                } catch (TransformException e) {
                                    // TODO Add Action Errors
                                    return mapping.findForward("config.wms.content");
                                } catch (FactoryException e) {
                                    // TODO Add Action Errors
                                    return mapping.findForward("config.wms.content");
                                } catch (IOException e) {
                                    // TODO Add Action Errors
                                    return mapping.findForward("config.wms.content");
                                }

                                ReferencedEnvelope newEnvelope = new ReferencedEnvelope(dstCRS);
                                newEnvelope.init(selectedEnvelope.getLowerCorner().getOrdinate(0),
                                    selectedEnvelope.getUpperCorner().getOrdinate(0),
                                    selectedEnvelope.getLowerCorner().getOrdinate(1),
                                    selectedEnvelope.getUpperCorner().getOrdinate(1));

                                newEnvelope.expandToInclude(ftEnvelope);

                                selectedEnvelope = new GeneralEnvelope(new double[] {
                                            newEnvelope.getMinX(), newEnvelope.getMinY()
                                        },
                                        new double[] { newEnvelope.getMaxX(), newEnvelope.getMaxY() });
                                selectedEnvelope.setCoordinateReferenceSystem(dstCRS);
                            }
                        } else if (layerType.intValue() == MapLayerInfo.TYPE_RASTER) {
                            CoverageInfo cv = catalog.getCoverageInfo(layerName);
                            cv = ((cv != null) ? cv
                                               : catalog.getCoverageInfo(layerName.substring(layerName
                                        .indexOf(":") + 1, layerName.length())));

                            if (selectedEnvelope == null) {
                                selectedEnvelope = cv.getEnvelope();
                            } else {
                                final CoordinateReferenceSystem cvCRS = cv.getCrs();
                                final CoordinateReferenceSystem dstCRS = selectedEnvelope
                                    .getCoordinateReferenceSystem();

                                ReferencedEnvelope cvEnvelope = new ReferencedEnvelope(cvCRS);
                                cvEnvelope.init(cv.getEnvelope().getLowerCorner().getOrdinate(0),
                                    cv.getEnvelope().getUpperCorner().getOrdinate(0),
                                    cv.getEnvelope().getLowerCorner().getOrdinate(1),
                                    cv.getEnvelope().getUpperCorner().getOrdinate(1));

                                try {
                                    cvEnvelope.transform(dstCRS, true);
                                } catch (TransformException e) {
                                    // TODO Add Action Errors
                                    return mapping.findForward("config.wms.content");
                                } catch (FactoryException e) {
                                    // TODO Add Action Errors
                                    return mapping.findForward("config.wms.content");
                                }

                                ReferencedEnvelope newEnvelope = new ReferencedEnvelope(dstCRS);
                                newEnvelope.init(selectedEnvelope.getLowerCorner().getOrdinate(0),
                                    selectedEnvelope.getUpperCorner().getOrdinate(0),
                                    selectedEnvelope.getLowerCorner().getOrdinate(1),
                                    selectedEnvelope.getUpperCorner().getOrdinate(1));

                                newEnvelope.expandToInclude(cvEnvelope);

                                selectedEnvelope = new GeneralEnvelope(new double[] {
                                            newEnvelope.getMinX(), newEnvelope.getMinY()
                                        },
                                        new double[] { newEnvelope.getMaxX(), newEnvelope.getMaxY() });
                                selectedEnvelope.setCoordinateReferenceSystem(dstCRS);
                            }
                        }
                    }
                }

                if (selectedEnvelope != null) {
                    envelope.setCoordinateReferenceSystem(selectedEnvelope
                        .getCoordinateReferenceSystem());
                    envelope.setEnvelope(selectedEnvelope);
                    envelopeMap.put(baseMapTitle, envelope);
                    contentForm.setBaseMapEnvelope(selectedLayerIndex, envelope);
                }
            }
        }

        if (selectedLayerIndex < 0) {
            return mapping.findForward("config.wms.content");
        }

        /*config.setBaseMapLayers(layerMap);
        config.setBaseMapStyles(styleMap);
        config.setBaseMapEnvelopes(envelopeMap);*/
        return mapping.findForward("config.wms.content");
    }

    private ActionForward executeRemoveLayerGroup(ActionMapping mapping, String action,
        WMSContentForm contentForm, WMSConfig config, HttpServletRequest request,
        HttpServletResponse response) {
        HashMap layerMap = new HashMap();
        HashMap styleMap = new HashMap();
        HashMap envelopeMap = new HashMap();

        if (action.indexOf(" ") <= 0) {
            return mapping.findForward("config.wms.content");
        }

        final String removedLayer = action.substring(action.indexOf(" ") + 1, action.length());
        int removedLayerIndex = -1;

        int bmi;
        Iterator it;

        for (bmi = 0, it = contentForm.getBaseMapTitles().iterator(); it.hasNext(); bmi++) {
            String baseMapTitle = (String) it.next();

            if (!removedLayer.equals(baseMapTitle)) {
                String baseMapLayers = (String) contentForm.getBaseMapLayers().get(bmi);
                String baseMapStyles = (String) contentForm.getBaseMapStyles().get(bmi);
                GeneralEnvelope envelope = (GeneralEnvelope) contentForm.getBaseMapEnvelopes()
                                                                        .get(bmi);

                /*
                             * System.out.println("******************* contentAction: title=" +
                             * baseMapTitle + ", layers=" + baseMapLayers + ", styles=" +
                             * baseMapStyles);
                             */
                layerMap.put(baseMapTitle, baseMapLayers);
                styleMap.put(baseMapTitle, baseMapStyles);
                envelopeMap.put(baseMapTitle, envelope);
            } else {
                removedLayerIndex = bmi;
            }
        }

        if (removedLayerIndex < 0) {
            return mapping.findForward("config.wms.content");
        }

        contentForm.getBaseMapTitles().remove(removedLayerIndex);
        contentForm.getBaseMapLayers().remove(removedLayerIndex);
        contentForm.getBaseMapStyles().remove(removedLayerIndex);
        contentForm.getBaseMapEnvelopes().remove(removedLayerIndex);
        contentForm.getMinCPs().remove(new Integer(removedLayerIndex));
        contentForm.getMaxCPs().remove(new Integer(removedLayerIndex));

        config.setBaseMapLayers(layerMap);
        config.setBaseMapStyles(styleMap);
        config.setBaseMapEnvelopes(envelopeMap);

        return mapping.findForward("config.wms.content");
    }

    private ActionForward executeAddNewLayerGroup(ActionMapping mapping, String action,
        WMSContentForm contentForm, WMSConfig config, HttpServletRequest request,
        HttpServletResponse response) throws ServletException {
        HashMap layerMap = new HashMap();
        HashMap styleMap = new HashMap();
        HashMap envelopeMap = new HashMap();

        int bmi;
        Iterator it;

        for (bmi = 0, it = contentForm.getBaseMapTitles().iterator(); it.hasNext(); bmi++) {
            String baseMapTitle = (String) it.next();
            String baseMapLayers = (String) contentForm.getBaseMapLayers().get(bmi);
            String baseMapStyles = (String) contentForm.getBaseMapStyles().get(bmi);

            GeneralEnvelope envelope = (GeneralEnvelope) contentForm.getBaseMapEnvelopes().get(bmi);

            /*
                         * System.out.println("******************* contentAction: title=" +
                         * baseMapTitle + ", layers=" + baseMapLayers + ", styles=" +
                         * baseMapStyles);
                         */
            layerMap.put(baseMapTitle, baseMapLayers);
            styleMap.put(baseMapTitle, baseMapStyles);
            envelopeMap.put(baseMapTitle, envelope);
        }

        String name = "<new_layer_group>";
        int num = 2;

        while ((config.getBaseMapLayers() != null) && (config.getBaseMapLayers().get(name) != null)) {
            name = "<new_layer_group" + num + ">";
            num++;
        }

        GeneralEnvelope defaultEnvelope = new GeneralEnvelope(new double[] { -180.0, -90.0 },
                new double[] { 180.0, 90.0 });

        try {
            defaultEnvelope.setCoordinateReferenceSystem(CRS.decode("EPSG:4326"));
        } catch (MismatchedDimensionException e) {
            throw new ServletException(e);
        } catch (NoSuchAuthorityCodeException e) {
            throw new ServletException(e);
        } catch (FactoryException e) {
            throw new ServletException(e);
        }

        layerMap.put(name, "");
        styleMap.put(name, "");
        envelopeMap.put(name, defaultEnvelope);

        contentForm.getBaseMapTitles().add(name);
        contentForm.getBaseMapLayers().add("");
        contentForm.getBaseMapStyles().add("");
        contentForm.getBaseMapEnvelopes().add(defaultEnvelope);

        config.setBaseMapLayers(layerMap);
        config.setBaseMapStyles(styleMap);
        config.setBaseMapEnvelopes(envelopeMap);

        return mapping.findForward("config.wms.content");
    }

    private ActionForward executeSubmit(ActionMapping mapping, String action,
        WMSContentForm contentForm, WMSConfig config, HttpServletRequest request,
        HttpServletResponse response) {
        HashMap layerMap = new HashMap();
        HashMap styleMap = new HashMap();
        HashMap envelopeMap = new HashMap();

        int bmi;
        Iterator it;

        for (bmi = 0, it = contentForm.getBaseMapTitles().iterator(); it.hasNext(); bmi++) {
            String baseMapTitle = (String) it.next();
            String baseMapLayers = (String) contentForm.getBaseMapLayers().get(bmi);
            String baseMapStyles = (String) contentForm.getBaseMapStyles().get(bmi);
            GeneralEnvelope envelope = (GeneralEnvelope) contentForm.getBaseMapEnvelopes().get(bmi);
            /*System.out.println("******************* contentAction: title=" + baseMapTitle + ", layers="
            + baseMapLayers + ", styles=" + baseMapStyles);*/
            layerMap.put(baseMapTitle, baseMapLayers);
            styleMap.put(baseMapTitle, baseMapStyles);
            envelopeMap.put(baseMapTitle, envelope);
        }

        config.setBaseMapLayers(layerMap);
        config.setBaseMapStyles(styleMap);
        config.setBaseMapEnvelopes(envelopeMap);

        getApplicationState().notifyConfigChanged();

        return mapping.findForward("config");
    }
}

/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers.wms;

import org.geotools.feature.FeatureType;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.readers.WmsKvpRequestReader;
import org.vfny.geoserver.requests.wms.GetLegendGraphicRequest;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;


/**
 * Key/Value pair parsed for a GetLegendGraphic request. When calling
 * <code>getRequest</code> produces a {@linkPlain
 * org.vfny.geoserver.requests.wms.GetLegendGraphicRequest}
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class GetLegendGraphicKvpReader extends WmsKvpRequestReader {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(GetLegendGraphicKvpReader.class.getPackage()
                                                                                         .getName());

    /**
     * Creates a new GetLegendGraphicKvpReader object.
     *
     * @param params DOCUMENT ME!
     */
    public GetLegendGraphicKvpReader(Map params) {
        super(params);
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws WmsException DOCUMENT ME!
     */
    public Request getRequest(HttpServletRequest request)
        throws ServiceException {
        GetLegendGraphicRequest glgr = new GetLegendGraphicRequest();
        glgr.setHttpServletRequest(request);

        String version = super.getRequestVersion();

        if (!GetLegendGraphicRequest.SLD_VERSION.equals(version)) {
            throw new WmsException("Invalid SLD version number \"" + version + "\"");
        }

        String layer = getValue("LAYER");
        FeatureTypeInfo fti;
        FeatureType ft;

        try {
            fti = glgr.getWMS().getData().getFeatureTypeInfo(layer);
            ft = fti.getFeatureType();
        } catch (NoSuchElementException e) {
            throw new WmsException(layer + " layer does not exists.",
                "LayerNotDefined");
        } catch (IOException e) {
            throw new WmsException(
                "Can't obtain the schema for the required layer.");
        }

        glgr.setLayer(ft);

        String format = getValue("FORMAT");

        if (!org.vfny.geoserver.responses.wms.GetLegendGraphicResponse
                .supportsFormat(format)) {
            throw new WmsException("Invalid graphic format: " + format,
                "InvalidFormat");
        }
        glgr.setFormat(format);

        parseOptionalParameters(glgr, fti);

        return glgr;
    }

    /**
     * DOCUMENT ME!
     *
     * @param req DOCUMENT ME!
     * @param ft DOCUMENT ME!
     *
     * @throws WmsException DOCUMENT ME!
     *
     * @task TODO: validate EXCEPTIONS parameter
     */
    private void parseOptionalParameters(GetLegendGraphicRequest req,
        FeatureTypeInfo ft) throws WmsException {
        parseStyleAndRule(req, ft);

        //not used by now
        String featureType = getValue("FEATURETYPE");

        String scale = getValue("SCALE");

        if ((scale != null) && !"".equals(scale)) {
            double scaleFactor = Double.valueOf(scale).doubleValue();
            req.setScale(scaleFactor);
        }

        String width = getValue("WIDTH");

        if ((width != null) && !"".equals(width)) {
            int legendW = Integer.valueOf(width).intValue();
            req.setScale(legendW);
        }

        String height = getValue("HEIGHT");

        if ((height != null) && !"".equals(height)) {
            int legendH = Integer.valueOf(height).intValue();
            req.setScale(legendH);
        }

        String exceptions = getValue("EXCEPTIONS");

        if (exceptions != null) {
            req.setExceptionsFormat(exceptions);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param req
     * @param ftype
     *
     * @throws WmsException
     * @throws UnsupportedOperationException DOCUMENT ME!
     *
     * @task TODO: support SLD_BODY parsing
     */
    private void parseStyleAndRule(GetLegendGraphicRequest req,
        FeatureTypeInfo ftype) throws WmsException {
        String style = getValue("STYLE");

        String sld = getValue("SLD");
        String sldBody = getValue("SLD_BODY");

        if ((sld != null) || (sldBody != null)) {
            throw new UnsupportedOperationException(
                "SLD_BODY support is pending of implementation (blame Gabriel!)");
        }

        LOGGER.fine("looking for style " + style);

        Style sldStyle = null;

        if ((style != null) && !"".equals(style)) {
            sldStyle = req.getWMS().getData().getStyle(style);
        } else {
            sldStyle = ftype.getDefaultStyle();
        }

        req.setStyle(sldStyle);

        String rule = getValue("RULE");
        Rule sldRule = extractRule(sldStyle, rule);

        if (sldRule != null) {
            req.setRule(sldRule);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param sldStyle
     * @param rule
     *
     * @return DOCUMENT ME!
     *
     * @throws WmsException
     */
    private Rule extractRule(Style sldStyle, String rule)
        throws WmsException {
        Rule sldRule = null;

        if ((rule != null) && !"".equals(rule)) {
            FeatureTypeStyle[] fts = sldStyle.getFeatureTypeStyles();

            for (int i = 0; i < fts.length; i++) {
                Rule[] rules = fts[i].getRules();

                for (int r = 0; r < rules.length; r++) {
                    if (rule.equalsIgnoreCase(rules[r].getName())) {
                        sldRule = rules[r];
                        LOGGER.fine("found requested rule: " + rule);

                        break;
                    }
                }
            }

            if (sldRule == null) {
                throw new WmsException("Style " + sldStyle.getName()
                    + " does not contains a rule named " + rule);
            }
        }

        return sldRule;
    }
}

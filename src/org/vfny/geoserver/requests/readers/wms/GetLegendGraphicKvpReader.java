/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers.wms;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.geotools.feature.FeatureType;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.readers.WmsKvpRequestReader;
import org.vfny.geoserver.requests.wms.GetLegendGraphicRequest;


/**
 * Key/Value pair set parsed for a GetLegendGraphic request. When calling
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
     * Factory to create styles from inline or remote SLD documents (aka, from
     * SLD_BODY or SLD parameters).
     */
    private static final StyleFactory styleFactory = StyleFactory
        .createStyleFactory();

    /**
     * Creates a new GetLegendGraphicKvpReader object.
     *
     * @param params map of key/value pairs with the parameters for a
     *        GetLegendGraphic request
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
     * @throws ServiceException see <code>throws WmsException</code>
     * @throws WmsException if some invalid parameter was passed.
     */
    public Request getRequest(HttpServletRequest request)
        throws ServiceException {
        GetLegendGraphicRequest glgr = new GetLegendGraphicRequest();
        glgr.setHttpServletRequest(request);

        String version = super.getRequestVersion();

        if (!GetLegendGraphicRequest.SLD_VERSION.equals(version)) {
            throw new WmsException("Invalid SLD version number \"" + version
                + "\"");
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

        // not used by now
        String featureType = getValue("FEATURETYPE");

        String scale = getValue("SCALE");

        if ((scale != null) && !"".equals(scale)) {
            double scaleFactor = Double.valueOf(scale).doubleValue();
            req.setScale(scaleFactor);
        }

        String width = getValue("WIDTH");

        if ((width != null) && !"".equals(width)) {
            int legendW = Integer.valueOf(width).intValue();
            req.setWidth(legendW);
        }

        String height = getValue("HEIGHT");

        if ((height != null) && !"".equals(height)) {
            int legendH = Integer.valueOf(height).intValue();
            req.setHeight(legendH);
        }

        String exceptions = getValue("EXCEPTIONS");

        if (exceptions != null) {
            req.setExceptionsFormat(exceptions);
        }
    }

    /**
     * Parses the STYLE, SLD and SLD_BODY parameters, as well as RULE.
     * 
     * <p>
     * STYLE, SLD and SLD_BODY are mutually exclusive. STYLE refers to a named
     * style known by the server and applicable to the requested layer (i.e.,
     * it is exposed as one of the layer's styles in the Capabilities
     * document). SLD is a URL to an externally available SLD document, and
     * SLD_BODY is a string containing the SLD document itself.
     * </p>
     * 
     * <p>
     * As I don't completelly understand which takes priority over which from
     * the spec, I assume the precedence order as follow: SLD, SLD_BODY,
     * STYLE, in decrecent order of precedence.
     * </p>
     *
     * @param req
     * @param ftype
     *
     * @throws WmsException
     */
    private void parseStyleAndRule(GetLegendGraphicRequest req,
        FeatureTypeInfo ftype) throws WmsException {
        String style = getValue("STYLE");
        String sld = getValue("SLD");
        String sldBody = getValue("SLD_BODY");

        LOGGER.fine("looking for style " + style);

        Style sldStyle = null;

        if (sld != null) {
            LOGGER.finer("taking style from SLD parameter");
            sldStyle = loadRemoteStyle(sld); // may throw an exception
        } else if (sldBody != null) {
            LOGGER.finer("taking style from SLD_BODY parameter");
            sldStyle = parseSldBody(sldBody); // may throw an exception
        } else if ((style != null) && !"".equals(style)) {
            LOGGER.finer("taking style from STYLE parameter");
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
     * Loads a remote SLD document and parses it to a Style object
     *
     * @param sldUrl an URL to a SLD document
     *
     * @return the document parsed to a Style object
     *
     * @throws WmsException if <code>sldUrl</code> is not a valid URL, a stream
     *         can't be opened or a parsing error occurs
     */
    private Style loadRemoteStyle(String sldUrl) throws WmsException {
        InputStream in;

        try {
            URL url = new URL(sldUrl);
            in = url.openStream();
        } catch (MalformedURLException e) {
            throw new WmsException(e,
                "Not a valid URL to an SLD document " + sldUrl,
                "loadRemoteStyle");
        } catch (IOException e) {
            throw new WmsException(e, "Can't open the SLD URL " + sldUrl,
                "loadRemoteStyle");
        }

        return parseSld(in);
    }

    /**
     * Parses a SLD Style from a xml string
     *
     * @param sldBody the string containing the SLD document
     *
     * @return the SLD document string parsed to a Style object
     *
     * @throws WmsException if a parsing error occurs.
     */
    private Style parseSldBody(String sldBody) throws WmsException {
        return parseSld(new StringBufferInputStream(sldBody));
    }

    /**
     * Parses the content of the given input stream to an SLD Style, provided
     * that a valid SLD document can be read from <code>xmlIn</code>.
     *
     * @param xmlIn where to read the SLD document from.
     *
     * @return the parsed Style
     *
     * @throws WmsException if a parsing error occurs
     */
    private Style parseSld(InputStream xmlIn) throws WmsException {
        SLDParser parser = new SLDParser(styleFactory, xmlIn);
        Style[] styles = null;

        try {
            styles = parser.readXML();
        } catch (RuntimeException e) {
            throw new WmsException(e);
        }

        if ((styles == null) || (styles.length == 0)) {
            throw new WmsException("Document contains no styles");
        }

        return styles[0];
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

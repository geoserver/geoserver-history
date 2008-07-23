/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.wms;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.config.WMSConfig;

/**
 * DOCUMENT ME!
 * 
 * @author User To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WMSContentForm extends ActionForm {
    private boolean enabled;

    private String onlineResource;

    private List baseMapLayers = new ArrayList();

    private List baseMapStyles = new ArrayList();

    private List baseMapTitles = new ArrayList();

    private List baseMapEnvelopes = new ArrayList();

    private int envIndex = -1;

    private HashMap minCPs = new HashMap();

    private HashMap maxCPs = new HashMap();

    private CoordinateReferenceSystem targetCRS = null;

    private int selectedLayer;

    /*
     * Because of the way that STRUTS works, if the user does not check the
     * enabled box, or unchecks it, setEnabled() is never called, thus we must
     * monitor setEnabled() to see if it doesn't get called. This must be
     * accessible, as ActionForms need to know about it -- there is no way we
     * can tell whether we are about to be passed to an ActionForm or not.
     * 
     * Probably a better way to do this, but I can't think of one. -rgould
     */
    private boolean enabledChecked = false;

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getOnlineResource() {
        return onlineResource;
    }

    // ---------------------------------------------------------------
    public void setBaseMapTitles(List titles) {
        baseMapTitles = titles;
    }

    public void setBaseMapTitle(int index, String value) {
        baseMapTitles.set(index, value);
    }

    public List getBaseMapTitles() {
        return baseMapTitles;
    }

    public String getBaseMapTitle(int index) {
        return (String) baseMapTitles.get(index);
    }

    // ---------------------------------------------------------------
    public List getBaseMapLayers() {
        return baseMapLayers;
    }

    public String getBaseMapLayers(int index) {
        return (String) baseMapLayers.get(index);
    }

    public void setBaseMapLayers(int index, String value) {
        baseMapLayers.set(index, value);
    }

    public void setBaseMapLayers(List layers) {
        baseMapLayers = layers;
    }

    // ---------------------------------------------------------------
    public List getBaseMapStyles() {
        return baseMapStyles;
    }

    public String getBaseMapStyles(int index) {
        return (String) baseMapStyles.get(index);
    }

    public void setBaseMapStyles(int index, String value) {
        baseMapStyles.set(index, value);
    }

    public void setBaseMapStyles(List styles) {
        baseMapStyles = styles;
    }

    // ---------------------------------------------------------------

    // ---------------------------------------------------------------
    public void setBaseMapEnvelopes(List envelopes) {
        baseMapEnvelopes = envelopes;
    }

    public void setBaseMapEnvelope(int index, GeneralEnvelope value) {
        baseMapEnvelopes.set(index, value);
    }

    public List getBaseMapEnvelopes() {
        return baseMapEnvelopes;
    }

    public GeneralEnvelope getBaseMapEnvelope(int index) {
        return (GeneralEnvelope) baseMapEnvelopes.get(index);
    }

    public double getMinX(int index) {
        GeneralEnvelope envelope = getBaseMapEnvelope(index);

        return envelope.getLowerCorner().getOrdinate(0);
    }

    public double getMinY(int index) {
        GeneralEnvelope envelope = getBaseMapEnvelope(index);

        return envelope.getLowerCorner().getOrdinate(1);
    }

    public double getMaxX(int index) {
        GeneralEnvelope envelope = getBaseMapEnvelope(index);

        return envelope.getUpperCorner().getOrdinate(0);
    }

    public double getMaxY(int index) {
        GeneralEnvelope envelope = getBaseMapEnvelope(index);

        return envelope.getUpperCorner().getOrdinate(1);
    }

    public void setMinX(int index, double value) {
        /*
         * GeneralEnvelope oldEnvelope = getBaseMapEnvelope(index);
         * GeneralEnvelope newEnvelope = null; double[] minCP = new double[] {
         * value, oldEnvelope.getLowerCorner().getOrdinate(1) }; double[] maxCP =
         * new double[] { oldEnvelope.getUpperCorner().getOrdinate(0),
         * oldEnvelope.getUpperCorner().getOrdinate(1) }; newEnvelope = new
         * GeneralEnvelope(minCP, maxCP);
         * newEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());
         * setBaseMapEnvelope(index, newEnvelope);
         */
        double[] minCP = (double[]) minCPs.get(new Integer(index));

        if (minCP == null) {
            minCP = new double[2];
        }

        minCP[0] = value;
        minCPs.put(new Integer(index), minCP);
    }

    public void setMinY(int index, double value) {
        /*
         * GeneralEnvelope oldEnvelope = getBaseMapEnvelope(index);
         * GeneralEnvelope newEnvelope = null; double[] minCP = new double[] {
         * oldEnvelope.getLowerCorner().getOrdinate(0), value }; double[] maxCP =
         * new double[] { oldEnvelope.getUpperCorner().getOrdinate(0),
         * oldEnvelope.getUpperCorner().getOrdinate(1) }; newEnvelope = new
         * GeneralEnvelope(minCP, maxCP);
         * newEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());
         * setBaseMapEnvelope(index, newEnvelope);
         */
        double[] minCP = (double[]) minCPs.get(new Integer(index));

        if (minCP == null) {
            minCP = new double[2];
        }

        minCP[1] = value;
        minCPs.put(new Integer(index), minCP);
    }

    public void setMaxX(int index, double value) {
        /*
         * GeneralEnvelope oldEnvelope = getBaseMapEnvelope(index);
         * GeneralEnvelope newEnvelope = null; double[] minCP = new double[] {
         * oldEnvelope.getLowerCorner().getOrdinate(0),
         * oldEnvelope.getLowerCorner().getOrdinate(1) }; double[] maxCP = new
         * double[] { value, oldEnvelope.getUpperCorner().getOrdinate(1) };
         * newEnvelope = new GeneralEnvelope(minCP, maxCP);
         * newEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());
         * setBaseMapEnvelope(index, newEnvelope);
         */
        double[] maxCP = (double[]) maxCPs.get(new Integer(index));

        if (maxCP == null) {
            maxCP = new double[2];
        }

        maxCP[0] = value;
        maxCPs.put(new Integer(index), maxCP);
    }

    public void setMaxY(int index, double value) {
        /*
         * GeneralEnvelope oldEnvelope = getBaseMapEnvelope(index);
         * GeneralEnvelope newEnvelope = null; double[] minCP = new double[] {
         * oldEnvelope.getLowerCorner().getOrdinate(0),
         * oldEnvelope.getLowerCorner().getOrdinate(1) }; double[] maxCP = new
         * double[] { oldEnvelope.getUpperCorner().getOrdinate(0), value };
         * newEnvelope = new GeneralEnvelope(minCP, maxCP);
         * newEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());
         * setBaseMapEnvelope(index, newEnvelope);
         */
        double[] maxCP = (double[]) maxCPs.get(new Integer(index));

        if (maxCP == null) {
            maxCP = new double[2];
        }

        maxCP[1] = value;
        maxCPs.put(new Integer(index), maxCP);
    }

    public String getSrsName(int index) {
        GeneralEnvelope envelope = getBaseMapEnvelope(index);

        if (envelope.getCoordinateReferenceSystem().getName().toString().equalsIgnoreCase(
                "EPSG:WGS 84")) {
            return "EPSG:4326";
        } else if (envelope.getCoordinateReferenceSystem().getIdentifiers().toArray().length > 0) {
            return envelope.getCoordinateReferenceSystem().getIdentifiers().toArray()[0].toString();
        } else {
            return "EPSG:4326";
        }
    }

    public void setSrsName(int index, String value) throws NoSuchAuthorityCodeException,
            FactoryException {
        envIndex = index;
        try {
            targetCRS = CRS.decode(value);
        } catch (Exception e) {
            targetCRS = null;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param b
     */
    public void setEnabled(boolean b) {
        enabledChecked = true;
        enabled = b;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setOnlineResource(String string) {
        onlineResource = string;
    }

    /**
     * Action requested by user
     */
    private String action;

    private Set capabilitiesCrs;

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);

        enabledChecked = false;

        ServletContext context = getServlet().getServletContext();
        WMSConfig config = (WMSConfig) context.getAttribute(WMSConfig.CONFIG_KEY);

        this.enabled = config.isEnabled();

        URL url = config.getOnlineResource();

        if (url != null) {
            this.onlineResource = url.toString();
        } else {
            this.onlineResource = "";
        }

        if (config.getBaseMapLayers() != null) {
            Iterator it = config.getBaseMapLayers().keySet().iterator();
            String baseMapTitle;
            String baseMapLayerValues;
            String baseMapStyleValues;
            GeneralEnvelope baseMapEnvelopeValues;

            List baseMapTitles = new ArrayList();
            List baseMapLayers = new ArrayList();
            List baseMapStyles = new ArrayList();
            List baseMapEnvelopes = new ArrayList();

            while (it.hasNext()) {
                baseMapTitle = (String) it.next();
                baseMapLayerValues = (String) config.getBaseMapLayers().get(baseMapTitle);
                baseMapStyleValues = (String) config.getBaseMapStyles().get(baseMapTitle);
                baseMapEnvelopeValues = (GeneralEnvelope) config.getBaseMapEnvelopes().get(
                        baseMapTitle);

                baseMapTitles.add(baseMapTitle);
                baseMapLayers.add(baseMapLayerValues);
                baseMapStyles.add(baseMapStyleValues);
                baseMapEnvelopes.add(baseMapEnvelopeValues);
            }

            this.baseMapTitles = baseMapTitles;
            this.baseMapLayers = baseMapLayers;
            this.baseMapStyles = baseMapStyles;
            this.baseMapEnvelopes = baseMapEnvelopes;
        }
        this.capabilitiesCrs = new TreeSet(config.getCapabilitiesCrs());
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if ((targetCRS == null) && (envIndex >= 0)) {
            errors.add("onlineResource", new ActionError("error.wms.onlineResource.required",
                    onlineResource));
        } else if (envIndex >= 0) {
            GeneralEnvelope envelope = getBaseMapEnvelope(envIndex);

            // CoordinateReferenceSystem sourceCRS =
            // envelope.getCoordinateReferenceSystem();
            /*
             * final MathTransform srcCRSTodestCRS =
             * CRS.findMathTransform(sourceCRS, targetCRS, true); envelope =
             * CRSUtilities.transform(srcCRSTodestCRS, envelope);
             */

            // GeneralEnvelope newEnvelope = new GeneralEnvelope(minCP, maxCP);
            // newEnvelope.setCoordinateReferenceSystem(sourceCRS);
            envelope.setEnvelope(new GeneralEnvelope((double[]) minCPs.get(new Integer(envIndex)),
                    (double[]) maxCPs.get(new Integer(envIndex))));
            envelope.setCoordinateReferenceSystem(targetCRS);
            setBaseMapEnvelope(envIndex, envelope);
        }

        if ((onlineResource == null) || "".equals(onlineResource)) {
            errors.add("onlineResource", new ActionError("error.wms.onlineResource.required",
                    onlineResource));
        } else {
            try {
                URL url = new URL(onlineResource);
            } catch (MalformedURLException badURL) {
                errors.add("onlineResource", new ActionError("error.wms.onlineResource.malformed",
                        badURL));
            }
        }

        return errors;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public boolean isEnabledChecked() {
        return enabledChecked;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param b
     */
    public void setEnabledChecked(boolean b) {
        enabledChecked = b;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public HashMap getMaxCPs() {
        return maxCPs;
    }

    public HashMap getMinCPs() {
        return minCPs;
    }

    /**
     * @return the selectedLayer
     */
    public int getSelectedLayer() {
        return selectedLayer;
    }

    /**
     * @param selectedLayer
     *            the selectedLayer to set
     */
    public void setSelectedLayer(int selectedLayer) {
        this.selectedLayer = selectedLayer;
    }

    public Set getCapabilitiesCrsList() {
        return new TreeSet(capabilitiesCrs);
    }
    
    public String getCapabilitiesCrs() {
        StringBuffer sb = new StringBuffer();
        for (Iterator it = capabilitiesCrs.iterator(); it.hasNext();) {
            String epsgCode = (String) it.next();
            sb.append(epsgCode);
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public void setCapabilitiesCrs(final String commaSeparatedCrsCodes) {
        Set codes = new TreeSet();
        StringTokenizer st = new StringTokenizer(commaSeparatedCrsCodes.trim(), ",");
        while(st.hasMoreTokens()){
            String code = st.nextToken();
            code = code.trim().toUpperCase();
            if(code.indexOf(':') == -1){
                code = "EPSG:" + code;
            }
            try{
                CRS.decode(code);
            }catch(Exception e){
                //not an epsg code, ignore
                continue;
            }
            codes.add(code);
        }
        this.capabilitiesCrs = codes;
    }
}

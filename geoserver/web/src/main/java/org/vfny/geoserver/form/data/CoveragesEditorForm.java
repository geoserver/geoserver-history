/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.SI;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.data.Parameter;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.feature.type.Name;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.TemporalDatum;
import org.opengis.referencing.datum.VerticalDatum;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.vfny.geoserver.action.HTMLEncoder;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.util.Requests;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public final class CoveragesEditorForm extends ActionForm {
    /**
     *
     */
    private static final long serialVersionUID = 1831907046971087321L;

    /**
     *
     */
    private String formatId;

    /**
     * Identify Style used to render this feature type
     */
    private String styleId;

    /** Sorted Set of available styles */
    private SortedSet panelStyleIds;
    private SortedSet typeStyles;
    private String[] otherSelectedStyles;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String wmsPath;

    /**
     *
     */
    private String label;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private String metadataLink;
    private String boundingBoxMinX;
    private String boundingBoxMinY;
    private String boundingBoxMaxX;
    private String boundingBoxMaxY;

    /**
     *
     */
    private String keywords;

    /**
     * Sorted Set of available styles
     */
    private SortedSet styles;

    /**
     *
     */
    private String srsName;

    /**
     *
     */
    private String nativeCRS;

    /**
     *
     */
    private String WKTString;

    /**
     *
     */
    private String requestCRSs;

    /**
     *
     */
    private String responseCRSs;

    /**
     *
     */
    private String nativeFormat;

    /**
     *
     */
    private String supportedFormats;

    /**
     *
     */
    private String defaultInterpolationMethod;

    /**
     *
     */
    private String interpolationMethods;

    /**
     * Action requested by user
     */
    private String action;

    /**
     *
     */
    private String newCoverage;
    private List paramHelp;
    private List paramKeys;
    private List paramValues;
    
    /**
     * 
     */
    private List<String> fieldNames;
    private List<String> fieldDescriptions;
    private List<String> fieldUnitOfMeasures;

    /**
     * 
     */
    private String verticalExtentMinZ;
    private String verticalExtentMaxZ;

    /**
     * 
     */
    private String temporalExtentBegin;
    private String temporalExtentEnd;

    private long timeOrigin;

    private int timeAxisDimension;

    private String timeAxisUnit;

    private String timeAxisDirection;

    private String timeDateOrigin;

    private String verticalDatumType;

    private int verticalAxisDimension;

    private String verticalAxisAbbr;

    private String verticalAxisDirection;

    private String verticalAxisUnit;

    /**
     * Set up CoverageEditor from from Web Container.
     *
     * <p>
     * The key DataConfig.SELECTED_COVERAGE is used to look up the selected from
     * the web container.
     * </p>
     *
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);

        // //
        //
        //
        //
        // //
        action = "";
        newCoverage = "";

        final DataConfig config = ConfigRequests.getDataConfig(request);
        final UserContainer user = Requests.getUserContainer(request);
        final CoverageConfig type = user.getCoverageConfig();

        if (type == null) {
            // TODO Not sure what to do, user must have bookmarked?
            return; // Action should redirect to Select screen?
        }

        this.formatId = type.getFormatId();
        this.styleId = type.getDefaultStyle();

        // //
        //
        //
        //
        // //
        final CoverageConfig cvConfig = (CoverageConfig) request.getSession().getAttribute(DataConfig.SELECTED_COVERAGE);
        final GeneralEnvelope bounds = cvConfig.getEnvelope();

        if (bounds.isNull()) {
            boundingBoxMinX = "";
        } else {
            boundingBoxMinX = Double.toString(bounds.getLowerCorner().getOrdinate(0));
            boundingBoxMinY = Double.toString(bounds.getLowerCorner().getOrdinate(1));
            boundingBoxMaxX = Double.toString(bounds.getUpperCorner().getOrdinate(0));
            boundingBoxMaxY = Double.toString(bounds.getUpperCorner().getOrdinate(1));
        }

        // //
        //
        //
        //
        // //
        Set<Envelope> verticalExtent = cvConfig.getVerticalExtent();
        if (verticalExtent != null && !verticalExtent.isEmpty()) {
            VerticalDatum verticalDatum = ((VerticalCRS)cvConfig.getVerticalCRS()).getDatum();
            CoordinateSystemAxis verticalAxis = ((VerticalCRS)cvConfig.getVerticalCRS()).getCoordinateSystem().getAxis(0);

            this.verticalDatumType = verticalDatum.getVerticalDatumType().identifier();
            this.verticalAxisDimension = ((VerticalCRS)cvConfig.getVerticalCRS()).getCoordinateSystem().getDimension();
            this.verticalAxisAbbr = verticalAxis.getAbbreviation();
            this.verticalAxisDirection = verticalAxis.getDirection().identifier();
            this.verticalAxisUnit = verticalAxis.getUnit().toString();
            
            Envelope vEnvelope = verticalExtent.iterator().next();
            verticalExtentMinZ = String.valueOf(vEnvelope.getMinimum(0));
            verticalExtentMaxZ = String.valueOf(vEnvelope.getMaximum(0));
        } else {
            verticalExtentMinZ = "";
            verticalExtentMaxZ = "";
        }
        
        // //
        //
        //
        //
        // //
        Set<TemporalGeometricPrimitive> temporalExtent = cvConfig.getTemporalExtent();
        if (temporalExtent != null && !temporalExtent.isEmpty()) {
            TemporalDatum temporalDatum = ((TemporalCRS)cvConfig.getTemporalCRS()).getDatum();
            CoordinateSystemAxis timeAxis = ((TemporalCRS)cvConfig.getTemporalCRS()).getCoordinateSystem().getAxis(0);
            
            long origin = temporalDatum.getOrigin().getTime();
            UnitConverter toMillis = timeAxis.getUnit().getConverterTo(SI.MILLI(SI.SECOND));
            
            this.timeOrigin = origin;
            this.timeDateOrigin = new DefaultPosition(new Date(origin)).getDateTime().toString();
            this.timeAxisDimension = ((TemporalCRS)cvConfig.getTemporalCRS()).getCoordinateSystem().getDimension();
            this.timeAxisDirection = timeAxis.getDirection().identifier();
            this.timeAxisUnit = timeAxis.getUnit().toString();
            
            
            for (Iterator<TemporalGeometricPrimitive> i=temporalExtent.iterator(); i.hasNext();) {
                TemporalGeometricPrimitive temporalObject = i.next();
                
                if (temporalObject instanceof Period) {
                    /*
                    long beginTime = ((Period) temporalObject).getBeginning().getPosition().getDate().getTime();
                    long endTime = ((Period) temporalObject).getEnding().getPosition().getDate().getTime();
                    temporalExtentBegin = new DefaultPosition(new Date((long)toMillis.convert(beginTime) + origin)).getDateTime().toString();
                    temporalExtentEnd = new DefaultPosition(new Date((long)toMillis.convert(endTime) + origin)).getDateTime().toString();
                    */
                    temporalExtentBegin = ((Period) temporalObject).getBeginning().getPosition().getDateTime().toString();
                    temporalExtentEnd = ((Period) temporalObject).getEnding().getPosition().getDateTime().toString();
                } else if (temporalObject instanceof Instant) {
                    if (temporalExtentBegin == null || temporalExtentBegin.length() == 0)
                        temporalExtentBegin = ((Instant) temporalObject).getPosition().getDateTime().toString();
                    if (!i.hasNext())
                        temporalExtentEnd = ((Instant) temporalObject).getPosition().getDateTime().toString();
                }
            }
        } else {
            temporalExtentBegin = "";
            temporalExtentEnd = "";
        }
        
        // //
        //
        //
        //
        // //
        this.fieldNames = new ArrayList<String>();
        this.fieldDescriptions = new ArrayList<String>();
        this.fieldUnitOfMeasures = new ArrayList<String>();

        RangeType range = cvConfig.getFields();
        if (range != null && range.getNumFieldTypes() > 0) {
            for (Iterator<Name> i=range.getFieldTypeNames().iterator(); i.hasNext();) {
                String fieldName = i.next().getLocalPart();
                fieldNames.add(fieldName);
                if (range.getFieldType(fieldName).getDescription() != null)
                    fieldDescriptions.add(range.getFieldType(fieldName).getDescription().toString());
                else
                    fieldDescriptions.add("");
                
                if (range.getFieldType(fieldName).getUnitOfMeasure() != null)
                    fieldUnitOfMeasures.add(range.getFieldType(fieldName).getUnitOfMeasure().toString());
                else 
                    fieldUnitOfMeasures.add("");
            }
        }
        
        // //
        //
        //
        //
        // //
        srsName = cvConfig.getSrsName();
        WKTString = cvConfig.getSrsWKT();

        // //
        //
        //
        //
        // //
        name = cvConfig.getName();
        wmsPath = cvConfig.getWmsPath();
        label = cvConfig.getLabel();
        description = cvConfig.getDescription();
        metadataLink = ((cvConfig.getMetadataLink() != null) ? cvConfig.getMetadataLink().getAbout() : null);
        nativeFormat = cvConfig.getNativeFormat();
        defaultInterpolationMethod = cvConfig.getDefaultInterpolationMethod();

        // //
        //
        //
        //
        // //
        StringBuffer buf = new StringBuffer();

        // Keywords
        if (cvConfig.getKeywords() != null) {
            Object keyword;

            for (Iterator i = cvConfig.getKeywords().iterator(); i.hasNext();) {
                keyword = i.next();
                buf.append(keyword.toString());

                if (i.hasNext()) {
                    buf.append(" ");
                }
            }

            this.keywords = buf.toString();
        }

        // //
        //
        //
        //
        // //
        if (cvConfig.getRequestCRSs() != null) {
            buf = new StringBuffer();

            // RequestCRSs
            String CRS;

            for (Iterator i = cvConfig.getRequestCRSs().iterator(); i.hasNext();) {
                CRS = (String) i.next();
                buf.append(CRS.toUpperCase());

                if (i.hasNext()) {
                    buf.append(",");
                }
            }

            this.requestCRSs = buf.toString();
        }

        // //
        //
        //
        //
        // //
        if (cvConfig.getResponseCRSs() != null) {
            buf = new StringBuffer();

            // ResponseCRSs
            String CRS;

            for (Iterator i = cvConfig.getResponseCRSs().iterator(); i.hasNext();) {
                CRS = (String) i.next();
                buf.append(CRS.toUpperCase());

                if (i.hasNext()) {
                    buf.append(",");
                }
            }

            this.responseCRSs = buf.toString();
        }

        // //
        //
        //
        //
        // //
        if (cvConfig.getSupportedFormats() != null) {
            buf = new StringBuffer();

            // SupportedFormats
            String format;

            for (Iterator i = cvConfig.getSupportedFormats().iterator(); i.hasNext();) {
                format = (String) i.next();
                buf.append(format.toUpperCase());

                if (i.hasNext()) {
                    buf.append(",");
                }
            }

            this.supportedFormats = buf.toString();
        }

        // //
        //
        //
        //
        // //
        if (cvConfig.getInterpolationMethods() != null) {
            buf = new StringBuffer();

            // InterpolationMethods
            String intMethod;

            for (Iterator i = cvConfig.getInterpolationMethods().iterator(); i.hasNext();) {
                intMethod = (String) i.next();
                buf.append(intMethod.toLowerCase());

                if (i.hasNext()) {
                    buf.append(",");
                }
            }

            this.interpolationMethods = buf.toString();
        }

        // //
        //
        //
        //
        // //
        styles = new TreeSet();

        StyleConfig sc;

        for (Iterator i = config.getStyles().values().iterator(); i.hasNext();) {
            sc = (StyleConfig) i.next();
            styles.add(sc.getId());

            if (sc.isDefault()) {
                if ((styleId == null) || "".equals(styleId)) {
                    styleId.equals(sc.getId());
                }
            }
        }

        typeStyles = new TreeSet();

        for (Iterator i = type.getStyles().iterator(); i.hasNext();) {
            String styleName = (String) i.next();
            typeStyles.add(styleName);
        }

        if (styles instanceof org.vfny.geoserver.form.data.AttributeDisplay) {
            // TODO why I am here?
        }

        /**
         * Sync params
         */
        List paramHelp = new ArrayList();
        List paramKeys = new ArrayList();
        List paramValues = new ArrayList();
        if (type.getParameters() != null) {
            Iterator it = type.getParameters().keySet().iterator();
            String paramKey;
            String paramValue;

            while (it.hasNext()) {
                paramKey = (String) it.next();
                paramValue = (String) type.getParameters().get(paramKey);
                paramHelp.add(paramKey);
                paramKeys.add(paramKey);
                paramValues.add(paramValue);
            }
        }

        this.paramHelp = paramHelp;
        this.paramKeys = paramKeys;
        this.paramValues = paramValues;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        Locale locale = (Locale) request.getLocale();
        MessageResources messages = (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);
        final String ENVELOPE = HTMLEncoder.decode(messages.getMessage(locale, "config.data.calculateBoundingBox.label"));
        final String LOOKUP_SRS = HTMLEncoder.decode(messages.getMessage(locale, "config.data.lookupSRS.label"));

        // Pass Attribute Management Actions through without
        // much validation.
        if (action.startsWith("Up") || action.startsWith("Down") || action.startsWith("Remove") || ENVELOPE.equals(action)) {
            return errors;
        }

        DataConfig data = ConfigRequests.getDataConfig(request);

        // Check selected style exists
        if (!(data.getStyles().containsKey(styleId) || "".equals(styleId))) {
            errors.add("styleId", new ActionError("error.styleId.notFound", styleId));
        }

        // //
        //
        //
        //
        // //
        if (!LOOKUP_SRS.equals(action)) {
            if(!srsName.toUpperCase().startsWith("EPSG:")) {
                srsName = "EPSG:" + srsName;
            }
            try {
                CRS.decode(srsName);
            } catch(Exception e) {
                errors.add("envelope", new ActionError("config.data.coverage.srs", srsName));
            }
            
            requestCRSs = responseCRSs = srsName;
        }

        // //
        //
        //
        //
        // //
        if ("".equals(boundingBoxMinX) || "".equals(boundingBoxMinY) || "".equals(boundingBoxMaxX) || "".equals(boundingBoxMaxY)) {
            errors.add("envelope", new ActionError("error.envelope.required"));
        } else {
            try {
                Double.parseDouble(boundingBoxMinX);
                Double.parseDouble(boundingBoxMinY);
                Double.parseDouble(boundingBoxMaxX);
                Double.parseDouble(boundingBoxMaxY);
            } catch (NumberFormatException badNumber) {
                errors.add("envelope", new ActionError("error.envelope.invalid", badNumber));
            }
        }

        // //
        //
        //
        //
        // //
        if ("".equals(name)) {
            errors.add("name", new ActionError("error.coverage.name.required"));
        } else if (name.toString().indexOf(" ") > 0) {
            errors.add("name", new ActionError("error.coverage.name.invalid"));
        }

        // //
        //
        //
        //
        // //
        final DataConfig dataConfig = getDataConfig(request);
        final CoverageStoreConfig cvConfig = dataConfig.getDataFormat(formatId);

        if (cvConfig == null) {
            // something is horribly wrong no FormatID selected!
            // The JSP needs to not include us if there is no
            // selected Format
            //
            throw new RuntimeException("selectedDataFormatId required in Session");
        }

        // Retrieve connection params
        final Driver factory = cvConfig.getFactory();
        final Map<String, Parameter<?>> connectionParams = (factory.getConnectParameterInfo() != null ? factory.getConnectParameterInfo() : new HashMap<String, Parameter<?>>());

        // TODO: FIX THIS !!!
//        // Convert Params into the kind of Map we actually need
//        //
//        if (paramKeys != null) {
//            Boolean maxSize;
//            String size = null;
//            ControllerConfig cc;
//            Object value;
//            String key;
//            ParameterValue param;
//            final int length = paramKeys.size();
//            final String readGeometryKey = AbstractGridFormat.READ_GRIDGEOMETRY2D.getName()
//                                                                                 .toString();
//
//            for (int i = 0; i < length; i++) {
//                key = (String) getParamKey(i);
//
//                // //
//                //
//                // Ignore the parameters used for decimation at run time
//                //
//                // //
//                if (key.equalsIgnoreCase(readGeometryKey)) {
//                    continue;
//                }
//
//                param = CoverageStoreUtils.find(info, key);
//
//                if (param == null) {
//                    errors.add("paramValue[" + i + "]",
//                        new ActionError("error.dataFormatEditor.param.missing", key,
//                            factory.getDescription()));
//
//                    continue;
//                }
//
//                maxSize = (Boolean) request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
//
//                if ((maxSize != null) && (maxSize.booleanValue())) {
//                    size = null;
//                    cc = mapping.getModuleConfig().getControllerConfig();
//
//                    if (cc == null) {
//                        size = Long.toString(CommonsMultipartRequestHandler.DEFAULT_SIZE_MAX);
//                    } else {
//                        size = cc.getMaxFileSize(); // struts-config :
//                                                    // <controller
//                                                    // maxFileSize="nK" />
//                    }
//
//                    errors.add("styleID", new ActionError("error.file.maxLengthExceeded", size));
//
//                    return errors;
//                }
//
//                value = CoverageUtils.getCvParamValue(key, param, paramValues, i);
//
//                if (value != null) {
//                    connectionParams.put(key, value);
//                }
//            }
//        }

        return errors;
    }

    /**
     * Access Catalog Configuration Model from the WebContainer.
     *
     * @param request
     *
     * @return Configuration model for Catalog information.
     */
    protected DataConfig getDataConfig(HttpServletRequest request) {
        return (DataConfig) request.getSession().getServletContext().getAttribute(DataConfig.CONFIG_KEY);
    }

    /**
     * @return Returns the defaultInterpolationMethod.
     */
    public String getDefaultInterpolationMethod() {
        return defaultInterpolationMethod;
    }

    /**
     * @param defaultInterpolationMethod
     *            The defaultInterpolationMethod to set.
     */
    public void setDefaultInterpolationMethod(String defaultInterpolationMethod) {
        this.defaultInterpolationMethod = defaultInterpolationMethod;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the keywords.
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * @param keywords
     *            The keywords to set.
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * @return Returns the label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label
     *            The label to set.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return Returns the boundingBoxMaxX.
     */
    public String getMaxX() {
        return boundingBoxMaxX;
    }

    /**
     * @param boundingBoxMaxX
     *            The boundingBoxMaxX to set.
     */
    public void setMaxX(String boundingBoxMaxX) {
        this.boundingBoxMaxX = boundingBoxMaxX;
    }

    /**
     * @return Returns the boundingBoxMaxY.
     */
    public String getMaxY() {
        return boundingBoxMaxY;
    }

    /**
     * @param boundingBoxMaxY
     *            The boundingBoxMaxY to set.
     */
    public void setMaxY(String boundingBoxMaxY) {
        this.boundingBoxMaxY = boundingBoxMaxY;
    }

    /**
     * @return Returns the boundingBoxMinX.
     */
    public String getMinX() {
        return boundingBoxMinX;
    }

    /**
     * @param boundingBoxMinX
     *            The boundingBoxMinX to set.
     */
    public void setMinX(String boundingBoxMinX) {
        this.boundingBoxMinX = boundingBoxMinX;
    }

    /**
     * @return Returns the boundingBoxMinY.
     */
    public String getMinY() {
        return boundingBoxMinY;
    }

    /**
     * @param boundingBoxMinY
     *            The boundingBoxMinY to set.
     */
    public void setMinY(String boundingBoxMinY) {
        this.boundingBoxMinY = boundingBoxMinY;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the nativeFormat.
     */
    public String getNativeFormat() {
        return nativeFormat;
    }

    /**
     * @param nativeFormat
     *            The nativeFormat to set.
     */
    public void setNativeFormat(String nativeFormat) {
        this.nativeFormat = nativeFormat;
    }

    /**
     * @return Returns the srsName.
     */
    public String getSrsName() {
        return srsName;
    }

    /**
     * @param srsName
     *            The srsName to set.
     */
    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }

    /**
     * @return Returns the metadataLink.
     */
    public String getMetadataLink() {
        return metadataLink;
    }

    /**
     * @param metadataLink
     *            The metadataLink to set.
     */
    public void setMetadataLink(String metadataLink) {
        this.metadataLink = metadataLink;
    }

    /**
     * @return Returns the interpolationMethods.
     */
    public String getInterpolationMethods() {
        return interpolationMethods;
    }

    /**
     * @param interpolationMethods
     *            The interpolationMethods to set.
     */
    public void setInterpolationMethods(String interpolationMethods) {
        this.interpolationMethods = interpolationMethods;
    }

    /**
     * @return Returns the requestCRSs.
     */
    public String getRequestCRSs() {
        return requestCRSs;
    }

    /**
     * @param requestCRSs
     *            The requestCRSs to set.
     */
    public void setRequestCRSs(String requestCRSs) {
        this.requestCRSs = requestCRSs;
    }

    /**
     * @return Returns the responseCRSs.
     */
    public String getResponseCRSs() {
        return responseCRSs;
    }

    /**
     * @param responseCRSs
     *            The responseCRSs to set.
     */
    public void setResponseCRSs(String responseCRSs) {
        this.responseCRSs = responseCRSs;
    }

    /**
     * @return Returns the supportedFormats.
     */
    public String getSupportedFormats() {
        return supportedFormats;
    }

    /**
     * @param supportedFormats
     *            The supportedFormats to set.
     */
    public void setSupportedFormats(String supportedFormats) {
        this.supportedFormats = supportedFormats;
    }

    /**
     * @return Returns the action.
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action
     *            The action to set.
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return Returns the formatId.
     */
    public String getFormatId() {
        return formatId;
    }

    /**
     * @param formatId
     *            The formatId to set.
     */
    public void setFormatId(String formatId) {
        this.formatId = formatId;
    }

    /**
     * @return Returns the newCoverage.
     */
    public String getNewCoverage() {
        return newCoverage;
    }

    /**
     * @param newCoverage
     *            The newCoverage to set.
     */
    public void setNewCoverage(String newCoverage) {
        this.newCoverage = newCoverage;
    }

    public SortedSet getStyles() {
        return styles;
    }

    public void setStyles(SortedSet styles) {
        this.styles = styles;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getWKTString() {
        return WKTString;
    }

    public void setWKTString(String string) {
        WKTString = string;
    }

    public String getWmsPath() {
        return wmsPath;
    }

    public void setWmsPath(String wmsPath) {
        this.wmsPath = wmsPath;
    }

    /**
     * @param paramHelp
     *            The paramHelp to set.
     */
    public void setParamHelp(ArrayList paramHelp) {
        this.paramHelp = paramHelp;
    }

    /**
     * @return Returns the paramKeys.
     */
    public List getParamKeys() {
        return paramKeys;
    }

    /**
     * @param paramKeys
     *            The paramKeys to set.
     */
    public void setParamKeys(List paramKeys) {
        this.paramKeys = paramKeys;
    }

    /**
     * @return Returns the paramValues.
     */
    public List getParamValues() {
        return paramValues;
    }

    /**
     * @param paramValues
     *            The paramValues to set.
     */
    public void setParamValues(List paramValues) {
        this.paramValues = paramValues;
    }

    public Map getParams() {
        final Map map = new HashMap();

        if (paramKeys != null) {
            final int size = paramKeys.size();

            for (int i = 0; i < size; i++) {
                map.put(paramKeys.get(i), paramValues.get(i));
            }
        }

        return map;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index
     *            DOCUMENT ME!
     *
     * @return
     */
    public String getParamKey(int index) {
        return (String) paramKeys.get(index).toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index
     *            DOCUMENT ME!
     *
     * @return
     */
    public String getParamValue(int index) {
        return (String) paramValues.get(index).toString();
    }

    /**
     * @param paramKey
     *            The paramHelp to set.
     */

    /*public void setParamKey(int index, String value) {
            this.paramKeys.set(index, value);
    }*/

    /**
     * @param paramValue
     *            The paramHelp to set.
     */
    public void setParamValue(int index, String value) {
        this.paramValues.set(index, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index
     * @param value
     *            DOCUMENT ME!
     */
    public void setParamValues(int index, String value) {
        paramValues.set(index, value);
    }

    /**
     * Index property paramHelp
     *
     * @return DOCUMENT ME!
     */
    public String[] getParamHelp() {
        return (String[]) paramHelp.toArray(new String[paramHelp.size()]);
    }

    /**
     * Index property paramHelp
     *
     * @param index
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getParamHelp(int index) {
        return (String) paramHelp.get(index).toString();
    }

    public String[] getOtherSelectedStyles() {
        return otherSelectedStyles;
    }

    public void setOtherSelectedStyles(String[] otherSelectedStyles) {
        this.otherSelectedStyles = otherSelectedStyles;
    }

    public SortedSet getPanelStyleIds() {
        return panelStyleIds;
    }

    public SortedSet getTypeStyles() {
        return typeStyles;
    }

    /**
     * @return the verticalExtentMinZ
     */
    public String getVerticalExtentMinZ() {
        return verticalExtentMinZ;
    }

    /**
     * @return the verticalExtentMaxZ
     */
    public String getVerticalExtentMaxZ() {
        return verticalExtentMaxZ;
    }

    /**
     * @return the temporalExtentBegin
     */
    public String getTemporalExtentBegin() {
        return temporalExtentBegin;
    }

    /**
     * @return the temporalExtentEnd
     */
    public String getTemporalExtentEnd() {
        return temporalExtentEnd;
    }

    /**
     * @return the timeOrigin
     */
    public long getTimeOrigin() {
        return timeOrigin;
    }

    /**
     * @return the timeAxisDimension
     */
    public int getTimeAxisDimension() {
        return timeAxisDimension;
    }

    /**
     * @return the timeAxisUnit
     */
    public String getTimeAxisUnit() {
        return timeAxisUnit;
    }

    /**
     * @return the timeAxisDirection
     */
    public String getTimeAxisDirection() {
        return timeAxisDirection;
    }

    /**
     * @return the timeDateOrigin
     */
    public String getTimeDateOrigin() {
        return timeDateOrigin;
    }

    /**
     * @return the verticalDatumType
     */
    public String getVerticalDatumType() {
        return verticalDatumType;
    }

    /**
     * @return the verticalAxisDimension
     */
    public int getVerticalAxisDimension() {
        return verticalAxisDimension;
    }

    /**
     * @return the verticalAxisAbbr
     */
    public String getVerticalAxisAbbr() {
        return verticalAxisAbbr;
    }

    /**
     * @return the verticalAxisDirection
     */
    public String getVerticalAxisDirection() {
        return verticalAxisDirection;
    }

    /**
     * @return the verticalAxisUnit
     */
    public String getVerticalAxisUnit() {
        return verticalAxisUnit;
    }

    /**
     * @return the fieldNames
     */
    public List<String> getFieldNames() {
        return fieldNames;
    }

    /**
     * @return the fieldDescriptions
     */
    public List<String> getFieldDescriptions() {
        return fieldDescriptions;
    }

    /**
     * @return the fieldUnitOfMeasures
     */
    public List<String> getFieldUnitOfMeasures() {
        return fieldUnitOfMeasures;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index
     *            DOCUMENT ME!
     *
     * @return
     */
    public String getFieldName(int index) {
        return fieldNames.get(index);
    }

}

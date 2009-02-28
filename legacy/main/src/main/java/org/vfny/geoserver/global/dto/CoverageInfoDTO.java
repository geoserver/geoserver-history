/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import org.geotools.geometry.GeneralEnvelope;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.global.CoverageDimension;
import org.vfny.geoserver.global.MetaDataLink;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public final class CoverageInfoDTO implements DataTransferObject {
    /**
     *
     */
    private String formatId;

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
    private MetaDataLink metadataLink;

    /**
     *
     */
    private String dirName;

    /**
     *
     */
    private List keywords;

    /**
     *
     */
    private GeneralEnvelope envelope;

    /**
     *
     */
    private GeneralEnvelope lonLatWGS84Envelope;

    /**
     *
     */
    private GridGeometry grid;

    /**
     *
     */
    private CoverageDimension[] dimensions;

    /**
     *
     */
    private InternationalString[] dimensionNames;

    /**
     *
     */
    private List requestCRSs;

    /**
     *
     */
    private List responseCRSs;

    /**
     *
     */
    private String nativeFormat;

    /**
     *
     */
    private List supportedFormats;

    /**
     *
     */
    private String defaultInterpolationMethod;

    /**
     *
     */
    private List interpolationMethods;

    /**
     *
     */
    private String userDefinedCrsIdentifier;

    /**
     *
     */
    private String nativeCrsWkt;

    /**
     * Default style used to render this Coverage with WMS
     */
    private String defaultStyle;

    /** Other Style Names. */
    private ArrayList styles = new ArrayList();

    /**
         * String representation of connection parameter values
         */
    private Map parameters;

    public CoverageInfoDTO() {
    }

    public CoverageInfoDTO(CoverageInfoDTO dto) {
        if (dto == null) {
            throw new NullPointerException("Non null CoverageInfoDTO required");
        }

        formatId = dto.getFormatId();
        name = dto.getName();
        wmsPath = dto.getWmsPath();
        label = dto.getLabel();
        description = dto.getDescription();
        metadataLink = dto.getMetadataLink();
        dirName = dto.getDirName();

        try {
            keywords = CloneLibrary.clone(dto.getKeywords()); // clone?
        } catch (Exception e) {
            keywords = new LinkedList();
        }

        userDefinedCrsIdentifier = dto.getUserDefinedCrsIdentifier();
        nativeCrsWkt = dto.getNativeCrsWKT();
        envelope = dto.getEnvelope();
        lonLatWGS84Envelope = dto.getLonLatWGS84Envelope();
        grid = dto.getGrid();
        dimensions = dto.getDimensions();
        dimensionNames = dto.getDimensionNames();

        try {
            requestCRSs = CloneLibrary.clone(dto.getRequestCRSs());
        } catch (CloneNotSupportedException e1) {
            requestCRSs = new LinkedList();
        }

        try {
            responseCRSs = CloneLibrary.clone(dto.getResponseCRSs());
        } catch (CloneNotSupportedException e2) {
            responseCRSs = new LinkedList();
        }

        nativeFormat = dto.getNativeFormat();

        try {
            supportedFormats = CloneLibrary.clone(dto.getSupportedFormats());
        } catch (CloneNotSupportedException e3) {
            supportedFormats = new LinkedList();
        }

        defaultInterpolationMethod = dto.getDefaultInterpolationMethod();

        try {
            interpolationMethods = CloneLibrary.clone(dto.getInterpolationMethods());
        } catch (CloneNotSupportedException e4) {
            interpolationMethods = new LinkedList();
        }

        defaultStyle = dto.getDefaultStyle();
        styles = dto.getStyles();
        parameters = dto.getParameters();
    }

    public Object clone() {
        return new CoverageInfoDTO(this);
    }

    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof CoverageInfoDTO)) {
            return false;
        }

        CoverageInfoDTO f = (CoverageInfoDTO) obj;
        boolean r = true;
        r = r && (formatId == f.getFormatId());

        if (envelope != null) {
            r = r && envelope.equals(f.getEnvelope());
        } else if (f.getEnvelope() != null) {
            return false;
        }

        r = r && (userDefinedCrsIdentifier == f.getUserDefinedCrsIdentifier());
        r = r && (nativeCrsWkt == f.getNativeCrsWKT());
        r = r && (name == f.getName());
        r = r && (wmsPath == f.getWmsPath());
        r = r && (description == f.getDescription());
        r = r && (label == f.getLabel());
        r = r && (metadataLink == f.getMetadataLink());

        if (keywords != null) {
            r = r && EqualsLibrary.equals(keywords, f.getKeywords());
        } else if (f.getKeywords() != null) {
            return false;
        }

        r = r && (defaultStyle == f.getDefaultStyle());
        r = r && (styles == f.getStyles());
        r = r && (dirName == f.getDirName());
        r = r && (envelope == f.getEnvelope());
        r = r && (grid == f.getGrid());

        return r;
    }

    public int hashCode() {
        int r = 1;

        if (name != null) {
            r *= name.hashCode();
        }

        if (formatId != null) {
            r *= formatId.hashCode();
        }

        if (label != null) {
            r *= label.hashCode();
        }

        if (envelope != null) {
            r *= envelope.hashCode();
        }

        if (grid != null) {
            r *= grid.hashCode();
        }

        if (userDefinedCrsIdentifier != null) {
            r *= userDefinedCrsIdentifier.hashCode();
        }

        if (nativeCrsWkt != null) {
            r *= nativeCrsWkt.hashCode();
        }

        return r;
    }

    /**
     * List of keywords (limitied to text).
     *
     * @return List of Keywords about this FeatureType
     *
     * @uml.property name="keywords"
     */
    public List getKeywords() {
        return keywords;
    }

    /**
     * Convience method for dataStoreId.typeName.
     *
     * <p>
     * This key may be used to store this FeatureType in a Map for later.
     * </p>
     *
     * @return dataStoreId.typeName
     */
    public String getKey() {
        return getFormatId() + DataConfig.SEPARATOR + getName();
    }

    /**
     * Name of featureType, must match typeName provided by DataStore.
     *
     * @return typeName of FeatureType
     *
     * @uml.property name="name"
     */
    public String getName() {
        return name;
    }

    /**
     * setKeywords purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param list
     *
     * @uml.property name="keywords"
     */
    public void setKeywords(List list) {
        keywords = list;
    }

    /**
     * setKeywords purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param key
     *
     * @return boolean true when added.
     */
    public boolean addKeyword(String key) {
        if (keywords == null) {
            keywords = new LinkedList();
        }

        return keywords.add(key);
    }

    /**
     * setKeywords purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param key
     *
     * @return true whwn removed
     */
    public boolean removeKeyword(String key) {
        return keywords.remove(key);
    }

    /**
     * setName purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     *
     * @uml.property name="name"
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * getDirName purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @return
     *
     * @uml.property name="dirName"
     */
    public String getDirName() {
        return dirName;
    }

    /**
     * setDirName purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     *
     * @uml.property name="dirName"
     */
    public void setDirName(String string) {
        dirName = string;
    }

    public String toString() {
        return "[CoverageInfoDTO: " + name + ", formatId: " + formatId + ", envelope: " + envelope
        + "\n  SRS: " + userDefinedCrsIdentifier + ", dirName: " + dirName + ", label: " + label
        + "\n  description: " + description;
    }

    /**
     * @return Returns the description.
     *
     * @uml.property name="description"
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     *
     * @uml.property name="description"
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the formatId.
     *
     * @uml.property name="formatId"
     */
    public String getFormatId() {
        return formatId;
    }

    /**
     * @param formatId
     *            The formatId to set.
     *
     * @uml.property name="formatId"
     */
    public void setFormatId(String formatId) {
        this.formatId = formatId;
    }

    /**
     * @return Returns the label.
     *
     * @uml.property name="label"
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label
     *            The label to set.
     *
     * @uml.property name="label"
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return Returns the metadataLink.
     *
     * @uml.property name="metadataLink"
     */
    public MetaDataLink getMetadataLink() {
        return metadataLink;
    }

    /**
     * @param metadataLink
     *            The metadataLink to set.
     *
     * @uml.property name="metadataLink"
     */
    public void setMetadataLink(MetaDataLink metadataLink) {
        this.metadataLink = metadataLink;
    }

    /**
     * @return Returns the defaultInterpolationMethod.
     *
     * @uml.property name="defaultInterpolationMethod"
     */
    public String getDefaultInterpolationMethod() {
        return defaultInterpolationMethod;
    }

    /**
     * @param defaultInterpolationMethod
     *            The defaultInterpolationMethod to set.
     *
     * @uml.property name="defaultInterpolationMethod"
     */
    public void setDefaultInterpolationMethod(String defaultInterpolationMethod) {
        this.defaultInterpolationMethod = defaultInterpolationMethod;
    }

    /**
     * @return Returns the envelope.
     *
     * @uml.property name="envelope"
     */
    public GeneralEnvelope getEnvelope() {
        return envelope;
    }

    /**
     * @param envelope
     *            The envelope to set, in user defined CRS.
     *
     * @uml.property name="envelope"
     */
    public void setEnvelope(GeneralEnvelope envelope) {
        this.envelope = envelope;
    }

    /**
     * @return Returns the interpolationMethods.
     *
     * @uml.property name="interpolationMethods"
     */
    public List getInterpolationMethods() {
        return interpolationMethods;
    }

    /**
     * @param interpolationMethods
     *            The interpolationMethods to set.
     *
     * @uml.property name="interpolationMethods"
     */
    public void setInterpolationMethods(List interpolationMethods) {
        this.interpolationMethods = interpolationMethods;
    }

    /**
     * @return Returns the nativeFormat.
     *
     * @uml.property name="nativeFormat"
     */
    public String getNativeFormat() {
        return nativeFormat;
    }

    /**
     * @param nativeFormat
     *            The nativeFormat to set.
     *
     * @uml.property name="nativeFormat"
     */
    public void setNativeFormat(String nativeFormat) {
        this.nativeFormat = nativeFormat;
    }

    /**
     * @return Returns the requestCRSs.
     *
     * @uml.property name="requestCRSs"
     */
    public List getRequestCRSs() {
        return requestCRSs;
    }

    /**
     * @param requestCRSs
     *            The requestCRSs to set.
     *
     * @uml.property name="requestCRSs"
     */
    public void setRequestCRSs(List requestCRSs) {
        this.requestCRSs = requestCRSs;
    }

    /**
     * @return Returns the responseCRSs.
     *
     * @uml.property name="responseCRSs"
     */
    public List getResponseCRSs() {
        return responseCRSs;
    }

    /**
     * @param responseCRSs
     *            The responseCRSs to set.
     *
     * @uml.property name="responseCRSs"
     */
    public void setResponseCRSs(List responseCRSs) {
        this.responseCRSs = responseCRSs;
    }

    /**
     * @return Returns the user defined srsName.
     *
     * @uml.property name="srsName"
     */
    public String getUserDefinedCrsIdentifier() {
        return userDefinedCrsIdentifier;
    }

    /**
     * @param srsName
     *            The user defined srsName to set.
     *
     * @uml.property name="srsName"
     */
    public void setUserDefinedCrsIdentifier(String srsName) {
        this.userDefinedCrsIdentifier = srsName;
    }

    /**
     * @return Returns the supportedFormats.
     *
     * @uml.property name="supportedFormats"
     */
    public List getSupportedFormats() {
        return supportedFormats;
    }

    /**
     * @param supportedFormats
     *            The supportedFormats to set.
     *
     * @uml.property name="supportedFormats"
     */
    public void setSupportedFormats(List supportedFormats) {
        this.supportedFormats = supportedFormats;
    }

    /**
     * getDefaultStyle purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @return
     *
     * @uml.property name="defaultStyle"
     */
    public String getDefaultStyle() {
        // HACK: So our UI doesn't seem to allow the setting of styles or
        // default styles or anything, despite the fact that shit chokes when
        // none
        // is present. This is making it so the beta release can not have any
        // data
        // stores added to it. This is a hacky ass way to get around it, just
        // write out a normal style if it is null. This can obviously be done
        // better, and I have no idea why this default style shit is required -
        // wfs
        // does not care about a style. Should be able to seamlessly at least do
        // something for wms.
        if ((defaultStyle == null) || defaultStyle.equals("")) {
            defaultStyle = "raster";
        }

        return defaultStyle;
    }

    /**
     * setDefaultStyle purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     *
     * @uml.property name="defaultStyle"
     */
    public void setDefaultStyle(String string) {
        defaultStyle = string;
    }

    /**
     *
     * @uml.property name="grid"
     */
    public GridGeometry getGrid() {
        return grid;
    }

    /**
     *
     * @uml.property name="grid"
     */
    public void setGrid(GridGeometry grid) {
        this.grid = grid;
    }

    /**
     *
     * @uml.property name="dimensionNames"
     */
    public InternationalString[] getDimensionNames() {
        return dimensionNames;
    }

    /**
     *
     * @uml.property name="dimensionNames"
     */
    public void setDimensionNames(InternationalString[] dimentionNames) {
        this.dimensionNames = dimentionNames;
    }

    /**
     * @return Returns the dimensions.
     *
     * @uml.property name="dimensions"
     */
    public CoverageDimension[] getDimensions() {
        return dimensions;
    }

    /**
     * @param dimensions
     *            The dimensions to set.
     *
     * @uml.property name="dimensions"
     */
    public void setDimensions(CoverageDimension[] dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * @return the native CRS WKT
     */
    public String getNativeCrsWKT() {
        return nativeCrsWkt;
    }

    /**
     * @param srsWKT the native CRS WKT
     */
    public void setNativeCrsWKT(String srsWKT) {
        this.nativeCrsWkt = srsWKT;
    }

    public GeneralEnvelope getLonLatWGS84Envelope() {
        return lonLatWGS84Envelope;
    }

    public void setLonLatWGS84Envelope(GeneralEnvelope latLonEnvelope) {
        this.lonLatWGS84Envelope = latLonEnvelope;
    }

    public String getWmsPath() {
        return wmsPath;
    }

    public void setWmsPath(String wmsPath) {
        this.wmsPath = wmsPath;
    }

    /**
     * Handling multiple styles
     */
    public ArrayList getStyles() {
        return styles;
    }

    public void addStyle(String styleName) {
        if (!styles.contains(styleName)) {
            styles.add(styleName);
        }
    }

    public void setStyles(ArrayList styles) {
        this.styles = styles;
    }

    public Map getParameters() {
        return parameters;
    }

    public synchronized void setParameters(Map parameters) {
        this.parameters = parameters;
    }
}

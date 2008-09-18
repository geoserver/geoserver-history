/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.unit.Unit;

import org.geoserver.catalog.MetadataLinkInfo;
import org.geoserver.catalog.impl.MetadataLinkInfoImpl;
import org.geoserver.data.util.CoverageStoreUtils;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.Identifier;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.opengis.util.InternationalString;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.MetaDataLink;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;


/**
 * User interface Coverage staging area.
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 * @version $Id$
 */
public class CoverageConfig {
    
    private static final InternationalString[] DIMENSION_NAMES = {
        new SimpleInternationalString("x"),
        new SimpleInternationalString("y"),
        new SimpleInternationalString("z"),
        new SimpleInternationalString("t")
    };
    
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
    Set<TemporalGeometricPrimitive> temporalExtent;

    /**
     * 
     */
    Set<Envelope> verticalExtent;
    
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
    private String srsName;

    /**
     *
     */
    private String srsWKT;

    /**
     *
     */
    private CoordinateReferenceSystem crs;

    /**
     * The default style name.
     */
    private String defaultStyle;

    /**
    * Other WMS Styles
    */
    private ArrayList styles;

    /**
     * String representation of connection parameter values
     */
    private Map parameters;

    private RangeType fields;

    private CoordinateReferenceSystem temporalCRS;

    private CoordinateReferenceSystem verticalCRS;

    /**
     * Creating a coverage config from gridcoverages information
     * 
     * @param formatID
     * @param driver
     * @param cvAccess
     * @param name
     * @param request
     */
    public CoverageConfig(String formatId, Driver driver, CoverageAccess cvAccess, Name name,
            DataConfig catalog) throws ConfigurationException {
        ///////////////////////////////////////////////////////////////////////
        //
        // Initial checks
        //
        ///////////////////////////////////////////////////////////////////////
        if ((formatId == null) || (formatId.length() == 0)) {
            throw new ConfigurationException("formatId is required for CoverageConfig");
        }
        if (driver == null) {
            throw new ConfigurationException(new StringBuilder("Cannot handle driver: ").append(
                    formatId).toString());
        }
        if(cvAccess==null){
            throw new ConfigurationException(new StringBuilder("Cannot handle null coverage access for driver: ").append(
                    formatId).toString());
        }
        
        ///////////////////////////////////////////////////////////////////////
        //
        // Get the coverage config as needed
        //
        ///////////////////////////////////////////////////////////////////////
        this.formatId = formatId;
        final CoverageStoreConfig cvConfig = catalog.getDataFormat(formatId);
        if (cvConfig == null) {
            // something is horribly wrong no FormatID selected!
            // The JSP needs to not include us if there is no
            // selected Format
            //
            throw new ConfigurationException("selectedCoverageSetId required in Session");
        }

        ///////////////////////////////////////////////////////////////////////
        //
        // Now get all the information from the Reader and fill up the config
        //
        ///////////////////////////////////////////////////////////////////////
        CoverageSource cvSource = null;
        try {
            cvSource = cvAccess.access(name, cvAccess.getConnectParameters(), AccessType.READ_ONLY, null, null);
            crs = /* cvSource.getCoordinateReferenceSystem(null) */ cvSource.getHorizontalDomain(false, null).get(0).getCoordinateReferenceSystem();
            srsName = (((crs != null) && !crs.getIdentifiers().isEmpty()) ? crs.getIdentifiers().toArray()[0].toString() : "UNKNOWN");
            srsWKT = ((crs != null) ? crs.toWKT() : "UNKNOWN");
            BoundingBox bbox = cvSource.getHorizontalDomain(false, null).get(0);
            envelope = new GeneralEnvelope(bbox);
            envelope.setCoordinateReferenceSystem(crs);

            CoordinateReferenceSystem compundCRS = cvSource.getCoordinateReferenceSystem(null);
            temporalExtent = cvSource.getTemporalDomain(null);
            if (temporalExtent != null && !temporalExtent.isEmpty()) {
                if (compundCRS instanceof CompoundCRS) {
                    temporalCRS = ((CompoundCRS) compundCRS).getCoordinateReferenceSystems().get(0);
                }
            }
            verticalExtent = cvSource.getVerticalDomain(false, null);
            if (verticalExtent != null && !verticalExtent.isEmpty()) {
                if (compundCRS instanceof CompoundCRS) {
                    if (temporalCRS != null)
                        verticalCRS = ((CompoundCRS) compundCRS).getCoordinateReferenceSystems().get(1);
                    else
                        verticalCRS = ((CompoundCRS) compundCRS).getCoordinateReferenceSystems().get(0);
                } 
            }
            final GeneralGridEnvelope originalRange = new GeneralGridEnvelope(cvSource.getRasterDomain(false, null).get(0), 2);
            grid = new GridGeometry2D(originalRange, cvSource.getGridToWorldTransform(false, null),crs);
            try {
                lonLatWGS84Envelope = CoverageStoreUtils.getWGS84LonLatEnvelope(envelope);
            } catch (IndexOutOfBoundsException e) {
                final ConfigurationException newEx = new ConfigurationException(new StringBuffer("Converting Envelope to Lat-Lon WGS84: ").append(e.getLocalizedMessage()).toString());
                newEx.initCause(e);
                throw newEx;
            } catch (FactoryException e) {
                final ConfigurationException newEx = new ConfigurationException(new StringBuffer("Converting Envelope to Lat-Lon WGS84: ").append(e.getLocalizedMessage()).toString());
                newEx.initCause(e);
                throw newEx;
            } catch (TransformException e) {
                final ConfigurationException newEx = new ConfigurationException(new StringBuffer("Converting Envelope to Lat-Lon WGS84: ").append(e.getLocalizedMessage()).toString());
                newEx.initCause(e);
                throw newEx;
            }

            fields = cvSource.getRangeType(null);
        } catch (IOException e) {
            ConfigurationException exception = new ConfigurationException("It was not possible to access to the Coverage Source due to the following error: " + e.getLocalizedMessage());
            exception.setStackTrace(e.getStackTrace());
            throw exception;
        }
        
        // ////////////////////////////////////////////////////////////////////
        //
        // Setting-up metadata info
        //
        // ////////////////////////////////////////////////////////////////////

        StringBuilder cvName =null;
        int count = 0;
        while (true) {
            final StringBuilder key = new StringBuilder(cvSource.getName(null).getLocalPart());
            if (count > 0) {
                key.append("_").append(count);
            }

            Map coverages = catalog.getCoverages();
            Set cvKeySet = coverages.keySet();
            boolean key_exists = false;

            for (Iterator it = cvKeySet.iterator(); it.hasNext();) {
                String cvKey = ((String) it.next()).toLowerCase();
                if (cvKey.endsWith(key.toString().toLowerCase())) {
                    key_exists = true;
                }
            }

            if (!key_exists) {
                cvName = key;
                break;
            } else {
                count++;
            }
        }

        this.name = cvName.toString();
        wmsPath = "/";
        label = new StringBuffer(this.name).append(" is a ").append(driver.getDescription()).toString();
        description = new StringBuffer("Generated from ").append(formatId).toString();
        
        MetadataLinkInfo ml = new MetadataLinkInfoImpl();
        if (cvAccess.getInfo(null) != null && cvAccess.getInfo(null).getSource() != null)
            ml.setAbout(cvAccess.getInfo(null).getSource().toString());
        else ml.setAbout("UNKNOWN");
        ml.setMetadataType("other");
        metadataLink = new MetaDataLink(ml);
     // TODO: FIX THIS!!!
        //metadataLink.setAbout(format.getDocURL());
        //metadataLink.setMetadataType("other");
        keywords = new ArrayList(10);
        keywords.add("WCS");
        keywords.add(formatId);
        keywords.add(name);
        nativeFormat = driver.getName();
        dirName = new StringBuffer(formatId).append("_").append(name).toString();
        requestCRSs = new ArrayList(); 
        if ((crs.getIdentifiers() != null)
                && !crs.getIdentifiers().isEmpty()) {
            requestCRSs.add(((Identifier) crs.getIdentifiers().toArray()[0]).toString());
        }
        responseCRSs = new ArrayList(); 
        if ((crs.getIdentifiers() != null)
                && !crs.getIdentifiers().isEmpty()) {
            responseCRSs.add(((Identifier) crs.getIdentifiers().toArray()[0]).toString());
        }
        supportedFormats = new ArrayList(); 
        final Driver[] drivers = CoverageStoreUtils.drivers;
        for (int i = 0; i < drivers.length; i++) {
            final String dName = drivers[i].getName();

            if (dName.equalsIgnoreCase("WorldImage")) {
                // TODO check if coverage can encode Format
                supportedFormats.add("GIF");
                supportedFormats.add("PNG");
                supportedFormats.add("JPEG");
                supportedFormats.add("TIFF");
            } else if (dName.toLowerCase().startsWith("geotiff")) {
                // TODO check if coverage can encode Format
                supportedFormats.add("GeoTIFF");
            } else {
                // TODO check if coverage can encode Format
//                supportedFormats.add("GeoTIFF");
                supportedFormats.add(dName);
            }
        }
        // TODO make me parametric
        defaultInterpolationMethod = "nearest neighbor"; 
        interpolationMethods = new ArrayList(10);
        interpolationMethods.add("nearest neighbor");
        interpolationMethods.add("bilinear");
        interpolationMethods.add("bicubic");
        //interpolationMethods.add("bicubic_2");
        defaultStyle = "raster";
        styles = new ArrayList();

        /**
         * ReadParameters ...
         */
        parameters = cvSource.getReadParameterInfo();
    }

    /**
     * This method tries to put in order problems with 16 bits characters.
     *
     * @param label2
     * @param uom
     */
    private void parseUom(StringBuffer label2, Unit uom) {
        String uomString = uom.toString();
        uomString = uomString.replaceAll("�", "^2");
        uomString = uomString.replaceAll("�", "^3");
        uomString = uomString.replaceAll("�", "A");
        uomString = uomString.replaceAll("�", "");
        label2.append(uomString);
    }

    public CoverageConfig(CoverageInfoDTO dto) {
        if (dto == null) {
            throw new NullPointerException("Non null CoverageInfoDTO required");
        }

        formatId = dto.getFormatId();
        name = dto.getName();
        wmsPath = dto.getWmsPath();
        label = dto.getLabel();
        description = dto.getDescription();
        metadataLink = dto.getMetadataLink();
        keywords = dto.getKeywords();
        crs = dto.getCrs();
        verticalCRS = dto.getVerticalCRS();
        temporalCRS = dto.getTemporalCRS();
        srsName = dto.getSrsName();
        srsWKT = dto.getSrsWKT();
        envelope = dto.getEnvelope();
        verticalExtent = dto.getVerticalExtent();
        temporalExtent = dto.getTemporalExtent();
        lonLatWGS84Envelope = dto.getLonLatWGS84Envelope();
        grid = dto.getGrid();
        fields = dto.getFields();
        nativeFormat = dto.getNativeFormat();
        dirName = dto.getDirName();
        requestCRSs = dto.getRequestCRSs();
        responseCRSs = dto.getResponseCRSs();
        supportedFormats = dto.getSupportedFormats();
        defaultInterpolationMethod = dto.getDefaultInterpolationMethod();
        interpolationMethods = dto.getInterpolationMethods();
        defaultStyle = dto.getDefaultStyle();
        styles = dto.getStyles();
        parameters = dto.getParameters();
    }

    public CoverageInfoDTO toDTO() {
        CoverageInfoDTO c = new CoverageInfoDTO();
        c.setFormatId(formatId);
        c.setName(name);
        c.setWmsPath(wmsPath);
        c.setLabel(label);
        c.setDescription(description);
        c.setMetadataLink(metadataLink);
        c.setKeywords(keywords);
        c.setCrs(crs);
        c.setVerticalCRS(verticalCRS);
        c.setTemporalCRS(temporalCRS);
        c.setSrsName(srsName);
        c.setSrsWKT(srsWKT);
        c.setEnvelope(envelope);
        c.setVerticalExtent(verticalExtent);
        c.setTemporalExtent(temporalExtent);
        c.setLonLatWGS84Envelope(lonLatWGS84Envelope);
        c.setGrid(grid);
        c.setFields(fields);
        c.setNativeFormat(nativeFormat);
        c.setDirName(dirName);
        c.setRequestCRSs(requestCRSs);
        c.setResponseCRSs(responseCRSs);
        c.setSupportedFormats(supportedFormats);
        c.setDefaultInterpolationMethod(defaultInterpolationMethod);
        c.setInterpolationMethods(interpolationMethods);
        c.setDefaultStyle(defaultStyle);
        c.setStyles(styles);
        c.setParameters(parameters);

        return c;
    }

    public String getKey() {
        return getFormatId() + DataConfig.SEPARATOR + getName();
    }

    public String toString() {
        return "CoverageConfig[name: " + name + " dewcription: " + description + " srsName: "
        + srsName + "]";
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
     * @return Returns the dirName.
     *
     * @uml.property name="dirName"
     */
    public String getDirName() {
        return dirName;
    }

    /**
     * @param dirName
     *            The dirName to set.
     *
     * @uml.property name="dirName"
     */
    public void setDirName(String dirName) {
        this.dirName = dirName;
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
     *            The envelope to set.
     *
     * @uml.property name="envelope"
     */
    public void setEnvelope(GeneralEnvelope envelope) {
        this.envelope = envelope;
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
     * @return Returns the keywords.
     *
     * @uml.property name="keywords"
     */
    public List getKeywords() {
        return keywords;
    }

    /**
     * @param keywords
     *            The keywords to set.
     *
     * @uml.property name="keywords"
     */
    public void setKeywords(List keywords) {
        this.keywords = keywords;
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
     * @return Returns the name.
     *
     * @uml.property name="name"
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     *
     * @uml.property name="name"
     */
    public void setName(String name) {
        this.name = name;
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
     * @return Returns the srsName.
     *
     * @uml.property name="srsName"
     */
    public String getSrsName() {
        return srsName;
    }

    /**
     * @param srsName
     *            The srsName to set.
     *
     * @uml.property name="srsName"
     */
    public void setSrsName(String srsName) {
        this.srsName = srsName;
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
     *
     * @uml.property name="crs"
     */
    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    /**
     *
     * @uml.property name="crs"
     */
    public void setCrs(CoordinateReferenceSystem crs) {
        this.crs = crs;
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

    public String getDefaultStyle() {
        return defaultStyle;
    }

    public void setDefaultStyle(String defaultStyle) {
        this.defaultStyle = defaultStyle;
    }

    public ArrayList getStyles() {
        return styles;
    }

    public void setStyles(ArrayList styles) {
        this.styles = styles;
    }

    public void addStyle(String style) {
        if (!this.styles.contains(style)) {
            this.styles.add(style);
        }
    }

    public String getSrsWKT() {
        return srsWKT;
    }

    public void setSrsWKT(String srsWKT) {
        this.srsWKT = srsWKT;
    }

    public GeneralEnvelope getLonLatWGS84Envelope() {
        return lonLatWGS84Envelope;
    }

    public String getWmsPath() {
        return wmsPath;
    }

    public void setWmsPath(String wmsPath) {
        this.wmsPath = wmsPath;
    }

    public Map getParameters() {
        return parameters;
    }

    public synchronized void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public RangeType getFields() {
        return fields;
    }

    public void setFields(RangeType fields) {
        this.fields = fields;
    }

    public Set<TemporalGeometricPrimitive> getTemporalExtent() {
        return temporalExtent;
    }

    public void setTemporalExtent(Set<TemporalGeometricPrimitive> temporalExtent) {
        this.temporalExtent = temporalExtent;
    }

    public Set<Envelope> getVerticalExtent() {
        return verticalExtent;
    }

    public void setVerticalExtent(Set<Envelope> verticalExtent) {
        this.verticalExtent = verticalExtent;
    }

    /**
     * @return the temporalCRS
     */
    public CoordinateReferenceSystem getTemporalCRS() {
        return temporalCRS;
    }

    /**
     * @param temporalCRS the temporalCRS to set
     */
    public void setTemporalCRS(CoordinateReferenceSystem temporalCRS) {
        this.temporalCRS = temporalCRS;
    }

    /**
     * @return the verticalCRS
     */
    public CoordinateReferenceSystem getVerticalCRS() {
        return verticalCRS;
    }

    /**
     * @param verticalCRS the verticalCRS to set
     */
    public void setVerticalCRS(CoordinateReferenceSystem verticalCRS) {
        this.verticalCRS = verticalCRS;
    }
}

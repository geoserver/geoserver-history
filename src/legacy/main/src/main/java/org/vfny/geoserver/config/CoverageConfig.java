/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.measure.unit.Unit;
import javax.servlet.http.HttpServletRequest;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.MetadataLinkInfo;
import org.geoserver.data.util.CoverageStoreUtils;
import org.geoserver.data.util.CoverageUtils;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GeneralGridRange;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.GridRange2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.ServiceInfo;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.resources.XArray;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.metadata.Identifier;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.CoverageDimension;
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
    private InternationalString[] dimentionNames;

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
    private CoordinateReferenceSystem nativeCrs;

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

    Catalog catalog;
    
    /**
     * Package visible constructor for test cases
     */
    CoverageConfig() {
    }

    /**
     * Creating a coverage config from gridcoverages information
     *
     * @param formatId
     * @param format
     * @param gc
     * @throws ConfigurationException
     */
    public CoverageConfig(String formatId, Format format, AbstractGridCoverage2DReader reader,
        final HttpServletRequest request, Catalog catalog) throws ConfigurationException {


    	///////////////////////////////////////////////////////////////////////
    	//
    	// Initial checks
    	//
    	///////////////////////////////////////////////////////////////////////

        if ((formatId == null) || (formatId.length() == 0)) {
            throw new ConfigurationException("formatId is required for CoverageConfig");
        }
        if (format == null) {
            throw new ConfigurationException(new StringBuilder("Cannot handle format: ").append(
                    formatId).toString());
        }
        if(reader==null){
            throw new ConfigurationException(new StringBuilder("Cannot handle null reader for format: ").append(
                    formatId).toString());
        }
        
    	///////////////////////////////////////////////////////////////////////
    	//
    	// Get the coverage config as needed
    	//
    	///////////////////////////////////////////////////////////////////////
        this.formatId = formatId;
        final DataConfig dataConfig = getDataConfig(request);
        final CoverageStoreConfig cvConfig = dataConfig.getDataFormat(formatId);
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
        nativeCrs = reader.getCrs();
        userDefinedCrsIdentifier = (((nativeCrs != null) && !nativeCrs.getIdentifiers().isEmpty()) ? nativeCrs.getIdentifiers().toArray()[0].toString() : "UNKNOWN");
        //nativeCrsWKT = ((nativeCrs != null) ? nativeCrs.toWKT() : "UNKNOWN");
        envelope = reader.getOriginalEnvelope();
        final GeneralGridRange originalRange=reader.getOriginalGridRange();
        final MathTransform gridToWorldCorner =  reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        grid = new GridGeometry2D(originalRange,reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),nativeCrs);
        try {
            lonLatWGS84Envelope = CoverageStoreUtils.getWGS84LonLatEnvelope(envelope);
        } catch (IndexOutOfBoundsException e) {
            final ConfigurationException newEx = new ConfigurationException(new StringBuffer(
                        "Converting Envelope to Lat-Lon WGS84: ").append(e.getLocalizedMessage()).toString());
            newEx.initCause(e);
            throw newEx;
        } catch (FactoryException e) {
            final ConfigurationException newEx = new ConfigurationException(new StringBuffer(
                        "Converting Envelope to Lat-Lon WGS84: ").append(e.getLocalizedMessage()).toString());
            newEx.initCause(e);
            throw newEx;
        } catch (TransformException e) {
            final ConfigurationException newEx = new ConfigurationException(new StringBuffer(
                        "Converting Envelope to Lat-Lon WGS84: ").append(e.getLocalizedMessage()).toString());
            newEx.initCause(e);
            throw newEx;
        }

    	///////////////////////////////////////////////////////////////////////
    	//
    	// Now reading a fake small GridCoverage just to retrieve meta
		// information about bands:
    	//
        // - calculating a new envelope which is 1/20 of the original one
        // - reading the GridCoverage subset
        //
    	///////////////////////////////////////////////////////////////////////
        final GridCoverage2D gc;

        try {
            final ParameterValueGroup readParams = format.getReadParameters();
            final Map parameters = CoverageUtils.getParametersKVP(readParams);
            final int minX=originalRange.getLower(0);
            final int minY=originalRange.getLower(1);
            final int width=originalRange.getLength(0);
            final int height=originalRange.getLength(1);
            final int maxX=minX+(width<=5?width:5);
            final int maxY=minY+(height<=5?height:5);
            //we have to be sure that we are working against a valid grid range.
            final GridRange2D testRange= new GridRange2D(minX,minY,maxX,maxY);
            //build the corresponding envelope
            final GeneralEnvelope testEnvelope =CRS.transform(gridToWorldCorner,new GeneralEnvelope(testRange.getBounds()));
            testEnvelope.setCoordinateReferenceSystem(nativeCrs);
            parameters.put(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString(),
                new GridGeometry2D(testRange, testEnvelope));
            //try to read this coverage
            gc = (GridCoverage2D) reader.read(CoverageUtils.getParameters(readParams, parameters,
                        true));
            if(gc==null){
            	throw new ConfigurationException("Unable to acquire test coverage for format:"+formatId);
            }
            dimensions = parseCoverageDimesions(gc.getSampleDimensions(),catalog);
        } catch (UnsupportedEncodingException e) {
            final ConfigurationException newEx = new ConfigurationException(new StringBuffer(
                        "Coverage dimensions: ").append(e.getLocalizedMessage()).toString());
            newEx.initCause(e);
            throw newEx;
        } catch (IllegalArgumentException e) {
            final ConfigurationException newEx = new ConfigurationException(new StringBuffer(
                        "Coverage dimensions: ").append(e.getLocalizedMessage()).toString());
            newEx.initCause(e);
            throw newEx;
        } catch (IOException e) {
            final ConfigurationException newEx = new ConfigurationException(new StringBuffer(
                        "Coverage dimensions: ").append(e.getLocalizedMessage()).toString());
            newEx.initCause(e);
            throw newEx;
        } catch (TransformException e) {
        	 final ConfigurationException newEx = new ConfigurationException(new StringBuffer(
             			"Coverage dimensions: ").append(e.getLocalizedMessage()).toString());
        	 newEx.initCause(e);
        	 throw newEx;
        }
        dimentionNames = getDimensionNames(gc);

        final DataConfig config = ConfigRequests.getDataConfig(request);
        StringBuilder cvName =null;
        int count = 0;
        while (true) {
            final StringBuilder key = new StringBuilder(gc.getName().toString());
            if (count > 0) {
                key.append("_").append(count);
            }

            Map coverages = config.getCoverages();
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

        final ServiceInfo info = reader.getInfo();
        name = cvName.toString();
        wmsPath = "/";
        label = info.getTitle();
        if (null == label || "".equals(label)){
            label = new StringBuffer(name).append(" is a ").append(format.getDescription()).toString();
        }
        description = info.getDescription();
        if (null == description || "".equals(description)){
            description = new StringBuffer("Generated from ").append(formatId).toString();
        }

        MetadataLinkInfo ml = catalog.getFactory().createMetadataLink();
        ml.setAbout(format.getDocURL());
        ml.setMetadataType("other");
        metadataLink = new MetaDataLink(ml);
        //metadataLink.setAbout(format.getDocURL());
        //metadataLink.setMetadataType("other");
        keywords = new ArrayList(10);
        keywords.add("WCS");
        keywords.add(formatId);
        keywords.add(name);
        Set<String> infoKwds = info.getKeywords();
        if(infoKwds != null){
            keywords.addAll(infoKwds);
        }
        nativeFormat = format.getName();
        dirName = new StringBuffer(formatId).append("_").append(name).toString();
        requestCRSs = new ArrayList();
        if ((gc.getCoordinateReferenceSystem2D().getIdentifiers() != null)
                && !gc.getCoordinateReferenceSystem2D().getIdentifiers().isEmpty()) {
            requestCRSs.add(((Identifier) gc.getCoordinateReferenceSystem2D().getIdentifiers()
                                            .toArray()[0]).toString());
        }
        responseCRSs = new ArrayList();
        if ((gc.getCoordinateReferenceSystem2D().getIdentifiers() != null)
                && !gc.getCoordinateReferenceSystem2D().getIdentifiers().isEmpty()) {
            responseCRSs.add(((Identifier) gc.getCoordinateReferenceSystem2D().getIdentifiers()
                                             .toArray()[0]).toString());
        }
        supportedFormats = new ArrayList();
        final List formats = CoverageStoreUtils.listDataFormats();
        for (Iterator i = formats.iterator(); i.hasNext();) {
            final Format fTmp = (Format) i.next();
            final  String fName = fTmp.getName();

            if (fName.equalsIgnoreCase("WorldImage")) {
            	// TODO check if coverage can encode Format
                supportedFormats.add("GIF");
                supportedFormats.add("PNG");
                supportedFormats.add("JPEG");
                supportedFormats.add("TIFF");
            } else if (fName.toLowerCase().startsWith("geotiff")) {
                // TODO check if coverage can encode Format
                supportedFormats.add("GeoTIFF");
            } else {
                // TODO check if coverage can encode Format
                supportedFormats.add(fName);
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
        parameters = CoverageUtils.getParametersKVP(format.getReadParameters());
    }

    
    /**
     * Returns the dimension names for the specified coverage, either gathering them
     * from the coordinate system, or by
     * @param gc
     * @return
     */
    private InternationalString[] getDimensionNames(GridCoverage2D gc) {
        final InternationalString[] names;
        if (gc.getCoordinateReferenceSystem() != null) {
            final CoordinateSystem cs = gc.getCoordinateReferenceSystem().getCoordinateSystem();
            names = new InternationalString[cs.getDimension()];
            for (int i = 0; i < names.length; i++) {
                names[i] = new SimpleInternationalString(cs.getAxis(i).getName().getCode());
            }
        } else {
            names = XArray.resize(DIMENSION_NAMES, gc.getDimension());
            for (int i = DIMENSION_NAMES.length; i < names.length; i++) {
                names[i] = new SimpleInternationalString("dim" + (i + 1));
            }
        }
        return names;
    }

    /**
     * @param sampleDimensions
     * @return
     * @throws UnsupportedEncodingException
     */
    private CoverageDimension[] parseCoverageDimesions(GridSampleDimension[] sampleDimensions, Catalog catalog)
        throws UnsupportedEncodingException {
        final int length = sampleDimensions.length;
        CoverageDimension[] dims = new CoverageDimension[length];

        for (int i = 0; i < length; i++) {
            dims[i] = new CoverageDimension(catalog.getFactory().createCoverageDimension());
            dims[i].setName(sampleDimensions[i].getDescription().toString(Locale.getDefault()));

            StringBuffer label = new StringBuffer("GridSampleDimension".intern());
            final Unit uom = sampleDimensions[i].getUnits();

            if (uom != null) {
                label.append("(".intern());
                parseUom(label, uom);
                label.append(")".intern());
            }

            label.append("[".intern());
            label.append(sampleDimensions[i].getMinimumValue());
            label.append(",".intern());
            label.append(sampleDimensions[i].getMaximumValue());
            label.append("]".intern());
            dims[i].setDescription(label.toString());
            dims[i].setRange(sampleDimensions[i].getRange());

            final List<Category> categories = sampleDimensions[i].getCategories();
            if(categories!=null)
	            for (Category cat:categories) {
	
	                if ((cat != null) && cat.getName().toString().equalsIgnoreCase("no data")) {
	                    double min = cat.getRange().getMinimum();
	                    double max = cat.getRange().getMaximum();
	
	                    dims[i].setNullValues(((min == max) ? new Double[] { new Double(min) }
	                                                        : new Double[] {
	                            new Double(min), new Double(max)
	                        }));
	                }
	            }
        }

        return dims;
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
        userDefinedCrsIdentifier = dto.getUserDefinedCrsIdentifier();
       
        String nativeCrsWKT = dto.getNativeCrsWKT();
        
        try {
            nativeCrs = CRS.parseWKT(nativeCrsWKT);
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }

        envelope = dto.getEnvelope();
        lonLatWGS84Envelope = dto.getLonLatWGS84Envelope();
        grid = dto.getGrid();
        dimensions = dto.getDimensions();
        dimentionNames = dto.getDimensionNames();
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
        c.setUserDefinedCrsIdentifier(userDefinedCrsIdentifier);
        c.setNativeCrsWKT(nativeCrs.toWKT());
        c.setEnvelope(envelope);
        c.setLonLatWGS84Envelope(lonLatWGS84Envelope);
        c.setGrid(grid);
        c.setDimensions(dimensions);
        c.setDimensionNames(dimentionNames);
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

    /**
     * Access Catalog Configuration Model from the WebContainer.
     *
     * @param request
     *
     * @return Configuration model for Catalog information.
     */
    protected DataConfig getDataConfig(HttpServletRequest request) {
        return (DataConfig) request.getSession().getServletContext()
                                   .getAttribute(DataConfig.CONFIG_KEY);
    }

    public String getKey() {
        return getFormatId() + DataConfig.SEPARATOR + getName();
    }

    public String toString() {
        return "CoverageConfig[name: " + name + " dewcription: " + description + " srsName: "
        + userDefinedCrsIdentifier + "]";
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
     * @return the
     *
     * @uml.property name="srsName"
     */
    public String getUserDefinedCrsIdentifier() {
        return userDefinedCrsIdentifier;
    }

    /**
     * @param srsName
     *            The user defined CRS identifier
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
     * @return the native CRS
     * @uml.property name="crs"
     */
    public CoordinateReferenceSystem getNativeCrs() {
        return nativeCrs;
    }

    /**
     * @param crs the native CRS
     * @uml.property name="crs"
     */
    public void setNativeCrs(CoordinateReferenceSystem crs) {
        this.nativeCrs = crs;
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
     * @uml.property name="dimentionNames"
     */
    public InternationalString[] getDimentionNames() {
        return dimentionNames;
    }

    /**
     *
     * @uml.property name="dimentionNames"
     */
    public void setDimentionNames(InternationalString[] dimentionNames) {
        this.dimentionNames = dimentionNames;
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
}

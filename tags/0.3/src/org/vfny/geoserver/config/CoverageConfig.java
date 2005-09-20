/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.units.Unit;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.coverage.grid.GridFormatFinder;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.CoverageCategory;
import org.vfny.geoserver.global.CoverageDimension;
import org.vfny.geoserver.global.MetaDataLink;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;

import com.vividsolutions.jts.geom.Envelope;

/**
 * User interface FeatureType staging area.
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: FeatureTypeConfig.java,v 1.20 2004/03/09 10:59:56 jive Exp $
 */
public class CoverageConfig {

	/**
	 * 
	 * @uml.property name="formatId" multiplicity="(0 1)"
	 */
	private String formatId;

	/**
	 * 
	 * @uml.property name="name" multiplicity="(0 1)"
	 */
	private String name;

	/**
	 * 
	 * @uml.property name="label" multiplicity="(0 1)"
	 */
	private String label;

	/**
	 * 
	 * @uml.property name="description" multiplicity="(0 1)"
	 */
	private String description;

	/**
	 * 
	 * @uml.property name="metadataLink"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private MetaDataLink metadataLink;

	/**
	 * 
	 * @uml.property name="dirName" multiplicity="(0 1)"
	 */
	private String dirName;

	/**
	 * 
	 * @uml.property name="keywords"
	 * @uml.associationEnd elementType="java.lang.String" multiplicity="(0 -1)"
	 */
	private List keywords;

	/**
	 * 
	 * @uml.property name="envelope"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private Envelope envelope;

	/**
	 * 
	 * @uml.property name="grid"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private GridGeometry grid;

	/**
	 * 
	 * @uml.property name="dimensions"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 */
	private CoverageDimension[] dimensions;

	/**
	 * 
	 * @uml.property name="dimentionNames"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 */
	private InternationalString[] dimentionNames;

	/**
	 * 
	 * @uml.property name="requestCRSs"
	 * @uml.associationEnd elementType="java.lang.String" multiplicity="(0 -1)"
	 */
	private List requestCRSs;

	/**
	 * 
	 * @uml.property name="responseCRSs"
	 * @uml.associationEnd elementType="java.lang.String" multiplicity="(0 -1)"
	 */
	private List responseCRSs;

	/**
	 * 
	 * @uml.property name="nativeFormat" multiplicity="(0 1)"
	 */
	private String nativeFormat;

	/**
	 * 
	 * @uml.property name="supportedFormats"
	 * @uml.associationEnd elementType="java.lang.String" multiplicity="(0 -1)"
	 */
	private List supportedFormats;

	/**
	 * 
	 * @uml.property name="defaultInterpolationMethod" multiplicity="(0 1)"
	 */
	private String defaultInterpolationMethod;

	/**
	 * 
	 * @uml.property name="interpolationMethods"
	 * @uml.associationEnd elementType="java.lang.String" multiplicity="(0 -1)"
	 */
	private List interpolationMethods;

	/**
	 * 
	 * @uml.property name="srsName" multiplicity="(0 1)"
	 */
	private String srsName;

	/**
	 * 
	 * @uml.property name="crs"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private CoordinateReferenceSystem crs;


    /**
     * Package visible constructor for test cases
     */
    CoverageConfig() {
    }
    
    public CoverageConfig(String formatId, String formatType, GridCoverage2D gc, boolean generate) throws ConfigurationException {
        if ((formatId == null) || (formatId.length() == 0)) {
            throw new IllegalArgumentException(
                "formatId is required for CoverageConfig");
        }

        this.formatId = formatId;
        envelope = new Envelope();
        GeneralEnvelope gEnvelope=(GeneralEnvelope)gc.getEnvelope();
        if( generate ) {
        	envelope.init(gEnvelope.getLowerCorner().getOrdinate(0),
        			gEnvelope.getUpperCorner().getOrdinate(0),
        			gEnvelope.getLowerCorner().getOrdinate(1),
        			gEnvelope.getUpperCorner().getOrdinate(1)) ;
        }
        
		grid = gc.getGridGeometry();
		try {
			dimensions = parseCoverageDimesions(gc.getSampleDimensions());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dimentionNames = gc.getDimensionNames();
        crs = gc.getCoordinateReferenceSystem2D();
        srsName = (crs != null ? crs.getName().toString() : "WGS84");

        Format[] formats = GridFormatFinder.getFormatArray();
		Format format = null;
    	for( int i = 0; i < formats.length; i++ ) {
    		if( formats[i].getName().equals(formatType) ) {
    			format = formats[i];
    			break;
    		}
    	}
    	
        if (format == null) {
        	throw new ConfigurationException("Cannot handle format: " + formatId);
        }

        name = formatId + "_Coverage";
        label = format.getName() + "_Type";
        description = "Generated from " + formatId;
        metadataLink = new MetaDataLink();
        metadataLink.setAbout(format.getDocURL());
        metadataLink.setMetadataType("other");
        keywords = new LinkedList();
        keywords.add("WCS");
        keywords.add(formatId);
        keywords.add(name);
        nativeFormat = format.getName(); // ?
        dirName = formatId + "_" + name;
        requestCRSs = new LinkedList(); // ?
        responseCRSs = new LinkedList(); // ?
        supportedFormats = new LinkedList(); // ?
        defaultInterpolationMethod = "nearest neighbor"; // ?
        interpolationMethods = new LinkedList(); // ?
    }

    /**
	 * @param sampleDimensions
	 * @return
     * @throws UnsupportedEncodingException
	 */
	private CoverageDimension[] parseCoverageDimesions(GridSampleDimension[] sampleDimensions) throws UnsupportedEncodingException {
		final int length=sampleDimensions.length;
		CoverageDimension[] dims = new CoverageDimension[length];
		
		for(int i=0;i<length;i++) {
			dims[i] = new CoverageDimension();
			dims[i].setName("dim_".intern() + i);
			StringBuffer label=new StringBuffer("GridSampleDimension".intern());
			final Unit uom=sampleDimensions[i].getUnits();
			if(uom!=null)
			{
				
				label.append("(".intern());
				parseUom(label,uom);
				label.append(")".intern());
			}
			label.append("[".intern());
			label.append(sampleDimensions[i].getMinimumValue());
			label.append(",".intern());
			label.append(sampleDimensions[i].getMaximumValue());
			label.append("]".intern());	
			dims[i].setDescription(label.toString());
			final int numCategories=sampleDimensions[i].getCategories().size();
			Category[] cats = new Category[numCategories];
			CoverageCategory[] dimCats = new CoverageCategory[numCategories];
			int j=0;
			for(Iterator c_iT=sampleDimensions[i].getCategories().iterator();c_iT.hasNext();j++) {
				Category cat = (Category) c_iT.next();
				dimCats[j] = new CoverageCategory();
				dimCats[j].setName(cat.getName().toString());
				dimCats[j].setLabel(cat.toString());
				dimCats[j].setInterval(cat.getRange());
			}
			dims[i].setCategories(dimCats);
			double[] nTemp = sampleDimensions[i].getNoDataValues();
			if(nTemp != null) {
				final int ntLength=nTemp.length;
				Double[] nulls = new Double[ntLength];
				for(int nd=0;nd<ntLength;nd++) {
					nulls[nd] = new Double(nTemp[nd]);
				}
				dims[i].setNullValues(nulls);
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
		
		String uomString=uom.toString();
		uomString=uomString.replaceAll("²","^2");
		uomString=uomString.replaceAll("³","^3");
		uomString=uomString.replaceAll("Å","A");
		uomString=uomString.replaceAll("°","");
		label2.append(uomString);
		
	}

	public CoverageConfig(CoverageInfoDTO dto) {
        if (dto == null) {
            throw new NullPointerException(
                "Non null CoverageInfoDTO required");
        }

        formatId = dto.getFormatId();
        name = dto.getName();
        label = dto.getLabel();
        description = dto.getDescription();
        metadataLink = dto.getMetadataLink();
        keywords = dto.getKeywords();
        crs = dto.getCrs();
        srsName = dto.getSrsName();
        envelope = dto.getEnvelope();
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
    }

    public CoverageInfoDTO toDTO() {
    	CoverageInfoDTO c = new CoverageInfoDTO();
        c.setFormatId(formatId);
        c.setName(name);
        c.setLabel(label);
        c.setDescription(description);
        c.setMetadataLink(metadataLink);
        c.setKeywords(keywords);
        c.setCrs(crs);
        c.setSrsName(srsName);
        c.setEnvelope(envelope);
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

        return c;
    }


    public String getKey() {
        return getFormatId() + DataConfig.SEPARATOR + getName();
    }    

    public String toString() {
	return "CoverageConfig[name: " + name + " dewcription: " + description
	    + " srsName: " + srsName + "]";
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
	 * @param defaultInterpolationMethod The defaultInterpolationMethod to set.
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
	 * @param description The description to set.
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
	 * @param dirName The dirName to set.
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
	public Envelope getEnvelope() {
		return envelope;
	}

	/**
	 * @param envelope The envelope to set.
	 * 
	 * @uml.property name="envelope"
	 */
	public void setEnvelope(Envelope envelope) {
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
	 * @param formatId The formatId to set.
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
	 * @param interpolationMethods The interpolationMethods to set.
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
	 * @param keywords The keywords to set.
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
	 * @param label The label to set.
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
	 * @param metadataLink The metadataLink to set.
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
	 * @param name The name to set.
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
	 * @param nativeFormat The nativeFormat to set.
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
	 * @param requestCRSs The requestCRSs to set.
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
	 * @param responseCRSs The responseCRSs to set.
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
	 * @param srsName The srsName to set.
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
	 * @param supportedFormats The supportedFormats to set.
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
	 * @param dimensions The dimensions to set.
	 * 
	 * @uml.property name="dimensions"
	 */
	public void setDimensions(CoverageDimension[] dimensions) {
		this.dimensions = dimensions;
	}

}

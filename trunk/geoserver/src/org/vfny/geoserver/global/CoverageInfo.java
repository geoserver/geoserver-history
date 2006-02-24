/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.util.List;
import java.util.Map;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.styling.Style;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;
import org.vfny.geoserver.util.DataFormatUtils;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 */
public class CoverageInfo extends GlobalLayerSupertype {

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
	private GeneralEnvelope latLonEnvelope;

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
	private Data data;

	/**
	 * 
	 */
	private Map meta;

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
     * Default style used to render this Coverage with WMS
     */
    private String defaultStyle;
    
    public CoverageInfo(CoverageInfoDTO dto, Data data)
        throws ConfigurationException {
        this.data = data;

        formatId = dto.getFormatId();
        name = dto.getName();
        wmsPath = dto.getWmsPath();
        label = dto.getLabel();
        description = dto.getDescription();
        metadataLink = dto.getMetadataLink();
        dirName = dto.getDirName();
        keywords = dto.getKeywords();
        crs = dto.getCrs();
        srsName = dto.getSrsName(); 
        srsWKT = dto.getSrsWKT();
        envelope = dto.getEnvelope();
		grid = dto.getGrid();
		dimensions = dto.getDimensions();
		dimensionNames = dto.getDimensionNames();
        requestCRSs = dto.getRequestCRSs();
        responseCRSs = dto.getResponseCRSs();
        nativeFormat = dto.getNativeFormat();
        supportedFormats = dto.getSupportedFormats();
        defaultInterpolationMethod = dto.getDefaultInterpolationMethod();
        interpolationMethods = dto.getInterpolationMethods();
        defaultStyle = dto.getDefaultStyle();
    }

    Object toDTO() {
        CoverageInfoDTO dto = new CoverageInfoDTO();

        dto.setFormatId(formatId);
        dto.setName(name);
        dto.setWmsPath(wmsPath);
        dto.setLabel(label);
        dto.setDescription(description);
        dto.setMetadataLink(metadataLink);
        dto.setDirName(dirName);
        dto.setKeywords(keywords);
        dto.setCrs(crs);
        dto.setSrsName(srsName); 
        dto.setSrsWKT(srsWKT);
        dto.setEnvelope(envelope);
		dto.setGrid(grid);
		dto.setDimensions(dimensions);
		dto.setDimensionNames(dimensionNames);
        dto.setRequestCRSs(requestCRSs);
        dto.setResponseCRSs(responseCRSs);
        dto.setNativeFormat(nativeFormat);
        dto.setSupportedFormats(supportedFormats);
        dto.setDefaultInterpolationMethod(defaultInterpolationMethod);
        dto.setInterpolationMethods(interpolationMethods);
        dto.setDefaultStyle(defaultStyle);

        return dto;
    }

    public FormatInfo getFormatInfo() {
        return data.getFormatInfo(formatId);
    }

    public boolean isEnabled() {
        return (getFormatInfo() != null) && (getFormatInfo().isEnabled());
    }

    public FormatInfo getFormatMetaData() {
        return data.getFormatInfo(formatId);
    }

    public boolean containsMetaData(String key) {
        return meta.containsKey(key);
    }

    public void putMetaData(String key, Object value) {
        meta.put(key, value);
    }

    public Object getMetaData(String key) {
        return meta.get(key);
    }

	/**
	 * @return Returns the data.
	 */
	public Data getData() {
		return data;
	}

	/**
	 * @return Returns the defaultInterpolationMethod.
	 */
	public String getDefaultInterpolationMethod() {
		return defaultInterpolationMethod;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return Returns the dirName.
	 */
	public String getDirName() {
		return dirName;
	}

	/**
	 * @return Returns the envelope.
	 */
	public GeneralEnvelope getEnvelope() {
		return envelope;
	}

	/**
	 * @return Returns the formatId.
	 */
	public String getFormatId() {
		return formatId;
	}

	/**
	 * @return Returns the interpolationMethods.
	 */
	public List getInterpolationMethods() {
		return interpolationMethods;
	}

	/**
	 * @return Returns the keywords.
	 */
	public List getKeywords() {
		return keywords;
	}

	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return Returns the meta.
	 */
	public Map getMeta() {
		return meta;
	}

	/**
	 * @return Returns the metadataLink.
	 */
	public MetaDataLink getMetadataLink() {
		return metadataLink;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the nativeFormat.
	 */
	public String getNativeFormat() {
		return nativeFormat;
	}

	/**
	 * @return Returns the requestCRSs.
	 */
	public List getRequestCRSs() {
		return requestCRSs;
	}

	/**
	 * @return Returns the responseCRSs.
	 */
	public List getResponseCRSs() {
		return responseCRSs;
	}

	/**
	 * @return Returns the srsName.
	 */
	public String getSrsName() {
		return srsName;
	}

	/**
	 * @return Returns the supportedFormats.
	 */
	public List getSupportedFormats() {
		return supportedFormats;
	}

	
	/**
     * By now just return the default style to be able to declare it in
     * WMS capabilities, but all this stuff needs to be revisited since it seems
     * currently there is no way of retrieving all the styles declared for
     * a given Coverage.
     * 
     * @return the default Style for the Coverage
     */
    public Style getDefaultStyle(){
    	return data.getStyle(defaultStyle);
    }

	/**
	 * 
	 */
	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	/**
	 * 
	 */
	public GridGeometry getGrid() {
		return grid;
	}

	/**
	 * 
	 */
	public InternationalString[] getDimensionNames() {
		return dimensionNames;
	}

	/**
	 * @return Returns the dimensions.
	 */
	public CoverageDimension[] getDimensions() {
		return dimensions;
	}

	public String getSrsWKT() {
		return srsWKT;
	}
	public GeneralEnvelope getLatLonEnvelope() {
		if(latLonEnvelope == null) {
				try {
					latLonEnvelope = DataFormatUtils.getLatLonEnvelope(this.envelope);
				} catch (IndexOutOfBoundsException e) {
					return null;
				} catch (FactoryException e) {
					return null;
				} catch (TransformException e) {
					return null;
				}
		}
		
		return latLonEnvelope;
	}
	public String getWmsPath() {
		return wmsPath;
	}
	public void setWmsPath(String wmsPath) {
		this.wmsPath = wmsPath;
	}
}
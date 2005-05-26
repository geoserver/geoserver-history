package org.vfny.geoserver.global;

import java.util.List;
import java.util.Map;

import org.geotools.styling.Style;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;

import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class CoverageInfo extends GlobalLayerSupertype {
	private String formatId;

    private String name;

    private String label;

    private String description;

    private MetaDataLink metadataLink;

    private String dirName;

    private List keywords;
    
    private Envelope envelope;
    
    private List requestCRSs;
    
    private List responseCRSs;
    
    private String nativeFormat;
    
    private List supportedFormats;
    
    private String defaultInterpolationMethod;
    
    private List interpolationMethods;
    
    private Data data;

    private Map meta;

    private String srsName; 

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
        label = dto.getLabel();
        description = dto.getDescription();
        metadataLink = dto.getMetadataLink();
        dirName = dto.getDirName();
        keywords = dto.getKeywords();
        crs = dto.getCrs();
        srsName = dto.getSrsName(); 
        envelope = dto.getEnvelope();
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
        dto.setLabel(label);
        dto.setDescription(description);
        dto.setMetadataLink(metadataLink);
        dto.setDirName(dirName);
        dto.setKeywords(keywords);
        dto.setCrs(crs);
        dto.setSrsName(srsName); 
        dto.setEnvelope(envelope);
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

//    /**
//     * getFeatureSource purpose.
//     * 
//     * <p>
//     * Returns a real FeatureSource.
//     * </p>
//     *
//     * @return FeatureSource the feature source represented by this info class
//     *
//     * @throws IOException when an error occurs.
//     */
//    public FeatureSource getFeatureSource() throws IOException {
//        if (!isEnabled() || (getDataStoreInfo().getDataStore() == null)) {
//            throw new IOException("featureType: " + getName()
//                + " does not have a properly configured " + "datastore");
//        }
//
//        DataStore dataStore = data.getDataStoreInfo(dataStoreId).getDataStore();
//        FeatureSource realSource = dataStore.getFeatureSource(typeName);
//
//        if (((schema == null) || schema.isEmpty())) { // &&
//
//            //(ftc.getDefinitionQuery() == null || ftc.getDefinitionQuery().equals( Query.ALL ))){
//            return realSource;
//        } else {
//            return GeoServerFeatureLocking.create(realSource,
//                getFeatureType(realSource), getDefinitionQuery());
//        }
//    }
//
//    /**
//     * getBoundingBox purpose.
//     * 
//     * <p>
//     * The feature source bounds.
//     * </p>
//     *
//     * @return Envelope the feature source bounds.
//     *
//     * @throws IOException when an error occurs
//     */
//    public Envelope getBoundingBox() throws IOException {
//        DataStore dataStore = data.getDataStoreInfo(dataStoreId).getDataStore();
//        FeatureSource realSource = dataStore.getFeatureSource(typeName);
//
//        return realSource.getBounds();
//    }
//
//    /**
//     * Will return our delegate with all information filled out
//     * 
//     * <p>
//     * This is a hack because we cache our DTO delegate, this method combines
//     * or ftc delegate with possibly generated schema information for use by
//     * XMLConfigWriter among others.
//     * </p>
//     * 
//     * <p>
//     * Call this method to receive a complete featureTypeInfoDTO that incldues
//     * all schema information.
//     * </p>
//     *
//     * @return
//     *
//     * @throws IOException DOCUMENT ME!
//     */
//    private synchronized CoverageInfoDTO getGeneratedDTO()
//        throws IOException {
//        return DataTransferObjectFactory.create(formatId, getCoverage());
//    }

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
	public Envelope getEnvelope() {
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
     * a given FeatureType.
     * 
     * @return the default Style for the FeatureType
     */
    public Style getDefaultStyle(){
    	return data.getStyle(defaultStyle);
    }
	public CoordinateReferenceSystem getCrs() {
		return crs;
	}
}
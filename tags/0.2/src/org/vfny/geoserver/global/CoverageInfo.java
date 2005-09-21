package org.vfny.geoserver.global;

import java.util.List;
import java.util.Map;

import org.geotools.styling.Style;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;

import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class CoverageInfo extends GlobalLayerSupertype {

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
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private MetaDataLink metadataLink;

	/**
	 * 
	 * @uml.property name="dirName" multiplicity="(0 1)"
	 */
	private String dirName;

	/**
	 * 
	 * @uml.property name="keywords" multiplicity="(0 1)"
	 */
	private List keywords;

	/**
	 * 
	 * @uml.property name="envelope"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private Envelope envelope;

	/**
	 * 
	 * @uml.property name="grid"
	 * @uml.associationEnd multiplicity="(1 1)"
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
	 * @uml.property name="dimensionNames"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 */
	private InternationalString[] dimensionNames;

	/**
	 * 
	 * @uml.property name="requestCRSs" multiplicity="(0 1)"
	 */
	private List requestCRSs;

	/**
	 * 
	 * @uml.property name="responseCRSs" multiplicity="(0 1)"
	 */
	private List responseCRSs;

	/**
	 * 
	 * @uml.property name="nativeFormat" multiplicity="(0 1)"
	 */
	private String nativeFormat;

	/**
	 * 
	 * @uml.property name="supportedFormats" multiplicity="(0 1)"
	 */
	private List supportedFormats;

	/**
	 * 
	 * @uml.property name="defaultInterpolationMethod" multiplicity="(0 1)"
	 */
	private String defaultInterpolationMethod;

	/**
	 * 
	 * @uml.property name="interpolationMethods" multiplicity="(0 1)"
	 */
	private List interpolationMethods;

	/**
	 * 
	 * @uml.property name="data"
	 * @uml.associationEnd inverse="coverages:org.vfny.geoserver.global.Data" multiplicity=
	 * "(1 1)"
	 */
	private Data data;

	/**
	 * 
	 * @uml.property name="meta"
	 * @uml.associationEnd elementType="java.lang.Object" qualifier="key:java.lang.String
	 * java.lang.Object" multiplicity="(0 -1)" ordering="ordered"
	 */
	private Map meta;

	/**
	 * 
	 * @uml.property name="srsName" multiplicity="(0 1)"
	 */
	private String srsName;

	/**
	 * 
	 * @uml.property name="crs"
	 * @uml.associationEnd multiplicity="(1 1)"
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
        label = dto.getLabel();
        description = dto.getDescription();
        metadataLink = dto.getMetadataLink();
        dirName = dto.getDirName();
        keywords = dto.getKeywords();
        crs = dto.getCrs();
        srsName = dto.getSrsName(); 
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
        dto.setLabel(label);
        dto.setDescription(description);
        dto.setMetadataLink(metadataLink);
        dto.setDirName(dirName);
        dto.setKeywords(keywords);
        dto.setCrs(crs);
        dto.setSrsName(srsName); 
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
	 * 
	 * @uml.property name="data"
	 */
	public Data getData() {
		return data;
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
	 * @return Returns the description.
	 * 
	 * @uml.property name="description"
	 */
	public String getDescription() {
		return description;
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
	 * @return Returns the envelope.
	 * 
	 * @uml.property name="envelope"
	 */
	public Envelope getEnvelope() {
		return envelope;
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
	 * @return Returns the interpolationMethods.
	 * 
	 * @uml.property name="interpolationMethods"
	 */
	public List getInterpolationMethods() {
		return interpolationMethods;
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
	 * @return Returns the label.
	 * 
	 * @uml.property name="label"
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return Returns the meta.
	 * 
	 * @uml.property name="meta"
	 */
	public Map getMeta() {
		return meta;
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
	 * @return Returns the name.
	 * 
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
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
	 * @return Returns the requestCRSs.
	 * 
	 * @uml.property name="requestCRSs"
	 */
	public List getRequestCRSs() {
		return requestCRSs;
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
	 * @return Returns the srsName.
	 * 
	 * @uml.property name="srsName"
	 */
	public String getSrsName() {
		return srsName;
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

	/**
	 * 
	 * @uml.property name="crs"
	 */
	public CoordinateReferenceSystem getCrs() {
		return crs;
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
	 * @uml.property name="dimensionNames"
	 */
	public InternationalString[] getDimensionNames() {
		return dimensionNames;
	}

	/**
	 * @return Returns the dimensions.
	 * 
	 * @uml.property name="dimensions"
	 */
	public CoverageDimension[] getDimensions() {
		return dimensions;
	}

}
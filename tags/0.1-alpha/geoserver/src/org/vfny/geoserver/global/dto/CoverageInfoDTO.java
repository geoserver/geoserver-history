package org.vfny.geoserver.global.dto;

import java.util.LinkedList;
import java.util.List;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.global.MetaDataLink;

import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public final class CoverageInfoDTO implements DataTransferObject {

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

    private String srsName; 

    private CoordinateReferenceSystem crs;
    
    /**
     * Default style used to render this Coverage with WMS
     */
    private String defaultStyle;


    public CoverageInfoDTO() {
    }

    public CoverageInfoDTO(CoverageInfoDTO dto) {
        if (dto == null) {
            throw new NullPointerException(
                "Non null CoverageInfoDTO required");
        }

    	formatId = dto.getFormatId();
        name = dto.getName();
        label = dto.getLabel();
        description = dto.getDescription();
        metadataLink = dto.getMetadataLink();
        dirName = dto.getDirName();
        try {
            keywords = CloneLibrary.clone(dto.getKeywords()); //clone?
        } catch (Exception e) {
            keywords = new LinkedList();
        }
        crs = dto.getCrs();
        srsName = dto.getSrsName(); 
        envelope = CloneLibrary.clone(dto.getEnvelope());
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
        r = r && (srsName == f.getSrsName());
        r = r && (crs == f.getCrs());
        r = r && (name == f.getName());
        r = r && (description == f.getDescription());
        r = r && (label == f.getLabel());
        r = r && (metadataLink == f.getMetadataLink());
        if (keywords != null) {
            r = r && EqualsLibrary.equals(keywords, f.getKeywords());
        } else if (f.getKeywords() != null) {
            return false;
        }
        r = r && (defaultStyle == f.getDefaultStyle());
        r = r && (dirName == f.getDirName());
        r = r && (envelope == f.getEnvelope());

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

        if (srsName != null) {
            r *= srsName.hashCode();
        }

        if (crs != null) {
            r *= crs.hashCode();
        }

        return r;
    }

    /**
     * List of keywords (limitied to text).
     *
     * @return List of Keywords about this FeatureType
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
     */
    public void setDirName(String string) {
        dirName = string;
    }

    public String toString() {
        return "[CoverageInfoDTO: " + name + ", formatId: " + formatId
        + ", envelope: " + envelope + "\n  SRS: " + srsName + ", dirName: " + dirName
        + ", label: " + label + "\n  description: " + description;
    }
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the formatId.
	 */
	public String getFormatId() {
		return formatId;
	}
	/**
	 * @param formatId The formatId to set.
	 */
	public void setFormatId(String formatId) {
		this.formatId = formatId;
	}
	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return Returns the metadataLink.
	 */
	public MetaDataLink getMetadataLink() {
		return metadataLink;
	}
	/**
	 * @param metadataLink The metadataLink to set.
	 */
	public void setMetadataLink(MetaDataLink metadataLink) {
		this.metadataLink = metadataLink;
	}
	/**
	 * @return Returns the defaultInterpolationMethod.
	 */
	public String getDefaultInterpolationMethod() {
		return defaultInterpolationMethod;
	}
	/**
	 * @param defaultInterpolationMethod The defaultInterpolationMethod to set.
	 */
	public void setDefaultInterpolationMethod(String defaultInterpolationMethod) {
		this.defaultInterpolationMethod = defaultInterpolationMethod;
	}
	/**
	 * @return Returns the envelope.
	 */
	public Envelope getEnvelope() {
		return envelope;
	}
	/**
	 * @param envelope The envelope to set.
	 */
	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}
	/**
	 * @return Returns the interpolationMethods.
	 */
	public List getInterpolationMethods() {
		return interpolationMethods;
	}
	/**
	 * @param interpolationMethods The interpolationMethods to set.
	 */
	public void setInterpolationMethods(List interpolationMethods) {
		this.interpolationMethods = interpolationMethods;
	}
	/**
	 * @return Returns the nativeFormat.
	 */
	public String getNativeFormat() {
		return nativeFormat;
	}
	/**
	 * @param nativeFormat The nativeFormat to set.
	 */
	public void setNativeFormat(String nativeFormat) {
		this.nativeFormat = nativeFormat;
	}
	/**
	 * @return Returns the requestCRSs.
	 */
	public List getRequestCRSs() {
		return requestCRSs;
	}
	/**
	 * @param requestCRSs The requestCRSs to set.
	 */
	public void setRequestCRSs(List requestCRSs) {
		this.requestCRSs = requestCRSs;
	}
	/**
	 * @return Returns the responseCRSs.
	 */
	public List getResponseCRSs() {
		return responseCRSs;
	}
	/**
	 * @param responseCRSs The responseCRSs to set.
	 */
	public void setResponseCRSs(List responseCRSs) {
		this.responseCRSs = responseCRSs;
	}
	/**
	 * @return Returns the srsName.
	 */
	public String getSrsName() {
		return srsName;
	}
	/**
	 * @param srsName The srsName to set.
	 */
	public void setSrsName(String srsName) {
		this.srsName = srsName;
	}
	/**
	 * @return Returns the supportedFormats.
	 */
	public List getSupportedFormats() {
		return supportedFormats;
	}
	/**
	 * @param supportedFormats The supportedFormats to set.
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
     */
    public String getDefaultStyle() {
        //HACK: So our UI doesn't seem to allow the setting of styles or 
        //default styles or anything, despite the fact that shit chokes when none
        //is present.  This is making it so the beta release can not have any data
        //stores added to it.  This is a hacky ass way to get around it, just 
        //write out a normal style if it is null.  This can obviously be done 
        //better, and I have no idea why this default style shit is required - wfs
        //does not care about a style.  Should be able to seamlessly at least do
        //something for wms.
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
     */
    public void setDefaultStyle(String string) {
        defaultStyle = string;
    }
	public CoordinateReferenceSystem getCrs() {
		return crs;
	}
	public void setCrs(CoordinateReferenceSystem crs) {
		this.crs = crs;
	}
}
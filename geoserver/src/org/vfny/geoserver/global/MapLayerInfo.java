package org.vfny.geoserver.global;

import java.io.IOException;

import org.vfny.geoserver.global.dto.CoverageInfoDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;

import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class MapLayerInfo extends GlobalLayerSupertype {
	public static int TYPE_VECTOR = 0;
	public static int TYPE_RASTER = 1;
	
	private FeatureTypeInfo feature;
	private CoverageInfo coverage;
	private int type;
	
    private String name;

    private String label;

    private String description;

    private String dirName;
    
    public MapLayerInfo(CoverageInfoDTO dto, Data data)
        throws ConfigurationException {

        name = dto.getName();
        label = dto.getLabel();
        description = dto.getDescription();
        dirName = dto.getDirName();
        
        coverage = new CoverageInfo(dto, data);
        feature = null;
        type = TYPE_RASTER;
    }

    public MapLayerInfo(FeatureTypeInfoDTO dto, Data data)
	    throws ConfigurationException {
	
	    name = dto.getName();
	    label = dto.getTitle();
	    description = dto.getAbstract();
	    dirName = dto.getDirName();
	    
	    feature = new FeatureTypeInfo(dto, data);
	    coverage = null;
	    type = TYPE_VECTOR;
    }

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
	 */
	Object toDTO() {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * getBoundingBox purpose.
     * 
     * <p>
     * The feature source bounds.
     * </p>
     *
     * @return Envelope the feature source bounds.
     *
     * @throws IOException when an error occurs
     */
    public Envelope getBoundingBox() throws IOException {
    	if( this.type == TYPE_VECTOR ) {
            return feature.getBoundingBox();
    	} else {
    		return coverage.getEnvelope();
    	}
    }

	public CoverageInfo getCoverage() {
		return coverage;
	}
	public void setCoverage(CoverageInfo coverage) {

        name = coverage.getName();
        label = coverage.getLabel();
        description = coverage.getDescription();
        dirName = coverage.getDirName();
        
		this.coverage = coverage;
        feature = null;
        type = TYPE_RASTER;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDirName() {
		return dirName;
	}
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}
	public FeatureTypeInfo getFeature() {
		return feature;
	}
	public void setFeature(FeatureTypeInfo feature) {
		
	    name = feature.getName();
	    label = feature.getTitle();
	    description = feature.getAbstract();
	    dirName = feature.getDirName();
	    
		this.feature = feature;
		coverage = null;
	    type = TYPE_VECTOR;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
 

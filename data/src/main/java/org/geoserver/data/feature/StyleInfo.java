package org.geoserver.data.feature;

import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;

/**
 * Metadata object for GeoServer styles.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class StyleInfo {

	/**
	 * Style identifier.
	 */
	private String id;
	/**
	 * the underlying style
	 */
	private StyledLayerDescriptor sld;
	
	/**
	 * A unique identifier for the style.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	/**
	 * The styled layer descriptor.
	 * 
	 */
	public void setSLD( StyledLayerDescriptor sld ) {
		this.sld = sld;
	}
	
	public StyledLayerDescriptor getSLD() {
		return sld;
	}
	
}

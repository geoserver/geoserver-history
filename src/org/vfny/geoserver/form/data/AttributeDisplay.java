package org.vfny.geoserver.form.data;

import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.global.dto.AttributeTypeInfoDTO;

/**
 * Present Attribute information to user input.
 */
public class AttributeDisplay {
    
    private String name;    
    private boolean nillible;
    private String minOccurs;
    private String maxOccurs;    
    private String selectedType;
    private String fragment;
    
    public AttributeDisplay( AttributeTypeInfoConfig config ){
        name = config.getName();
        nillible = config.isNillable();
        minOccurs = String.valueOf( config.getMinOccurs() );
        maxOccurs = String.valueOf( config.getMaxOccurs() );
        selectedType = config.getType();
        fragment = config.getFragment();
    }
    public AttributeDisplay( AttributeTypeInfoDTO dto ){
        this( new AttributeTypeInfoConfig( dto ));        
    }     
	/**
	 * @return Returns the fragment.
	 */
	public String getFragment() {
		return fragment;
	}
	/**
	 * @return Returns the maxOccurs.
	 */
	public String getMaxOccurs() {
		return maxOccurs;
	}
	/**
	 * @return Returns the minOccurs.
	 */
	public String getMinOccurs() {
		return minOccurs;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return Returns the nillible.
	 */
	public boolean isNillible() {
		return nillible;
	}
	/**
	 * @return Returns the selectedType.
	 */
	public String getSelectedType() {
		return selectedType;
	}
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name+":"+selectedType;
    }
}
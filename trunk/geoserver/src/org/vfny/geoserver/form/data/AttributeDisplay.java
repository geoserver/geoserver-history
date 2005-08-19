package org.vfny.geoserver.form.data;

import org.vfny.geoserver.config.AttributeTypeInfoConfig;

/**
 * Present Attribute information to user input.
 */
public class AttributeDisplay {

	/**
	 * 
	 * @uml.property name="name" multiplicity="(0 1)"
	 */
	private String name;
    
    private boolean nillable;

	/**
	 * 
	 * @uml.property name="minOccurs" multiplicity="(0 1)"
	 */
	private String minOccurs;

	/**
	 * 
	 * @uml.property name="maxOccurs" multiplicity="(0 1)"
	 */
	private String maxOccurs;

	/**
	 * 
	 * @uml.property name="type" multiplicity="(0 1)"
	 */
	private String type;

	/**
	 * 
	 * @uml.property name="fragment" multiplicity="(0 1)"
	 */
	private String fragment;

    
    public AttributeDisplay( AttributeTypeInfoConfig config ){
        name = config.getName();
        nillable = config.isNillable();
        minOccurs = String.valueOf( config.getMinOccurs() );
        maxOccurs = String.valueOf( config.getMaxOccurs() );
        type = config.getType();
        fragment = config.getFragment();
    }

	/*public AttributeDisplay( AttributeTypeInfoDTO dto ){
	 this( new AttributeTypeInfoConfig( dto ));        
	 }   */
	/**
	 * @return Returns the fragment.
	 * 
	 * @uml.property name="fragment"
	 */
	public String getFragment() {
		return fragment;
	}

	/**
	 * @return Returns the maxOccurs.
	 * 
	 * @uml.property name="maxOccurs"
	 */
	public String getMaxOccurs() {
		return maxOccurs;
	}

	/**
	 * @return Returns the minOccurs.
	 * 
	 * @uml.property name="minOccurs"
	 */
	public String getMinOccurs() {
		return minOccurs;
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
	 * @return Returns the nillible.
	 */
	public boolean isNillable() {
		return nillable;
	}

	/**
	 * @return Returns the selectedType.
	 * 
	 * @uml.property name="type"
	 */
	public String getType() {
		return type;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name+":"+type;
    }
}
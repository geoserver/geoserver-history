package org.vfny.geoserver.form.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.feature.AttributeType;
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.global.dto.AttributeTypeInfoDTO;
import org.vfny.geoserver.global.dto.DataTransferObjectFactory;
import org.vfny.geoserver.global.xml.NameSpaceElement;

/**
 * Present Attribute information for user input.
 */
public class AttributeForm {

	/**
	 * 
	 * @uml.property name="name" multiplicity="(0 1)"
	 */
	private String name;

	/**
	 * 
	 * @uml.property name="nillable" multiplicity="(0 1)"
	 */
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

	/**
	 * 
	 * @uml.property name="attributeType"
	 * @uml.associationEnd elementType="org.vfny.geoserver.global.xml.NameSpaceElement"
	 * multiplicity="(0 -1)"
	 */
	private AttributeType attributeType;

    
    public AttributeForm( AttributeTypeInfoConfig config, AttributeType attribute ){
        name = config.getName();
        nillable = config.isNillable();
        
        minOccurs = String.valueOf( config.getMinOccurs() );
        maxOccurs = String.valueOf( config.getMaxOccurs() );
        type = config.getType();
        fragment = config.getFragment();
        
        attributeType = attribute;
    }   
    public AttributeTypeInfoDTO toDTO(){
        AttributeTypeInfoDTO dto = new AttributeTypeInfoDTO();
        dto.setName( name );
        dto.setNillable( nillable );
        dto.setMinOccurs( Integer.parseInt( minOccurs ) );
        dto.setMaxOccurs( Integer.parseInt( maxOccurs ) );
        
        if( AttributeTypeInfoConfig.TYPE_FRAGMENT.equals(type) ){
            dto.setComplex( true );
            dto.setType( fragment );
        }
        else {
            dto.setComplex( false );
            dto.setType( type );                        
        }        
        return dto;        
    }
    public AttributeTypeInfoConfig toConfig(){
        return new AttributeTypeInfoConfig( toDTO() );
    }

	/**
	 * @return Returns the fragment.
	 * 
	 * @uml.property name="fragment"
	 */
	public String getFragment() {
		return fragment;
	}

	/**
	 * @param fragment The fragment to set.
	 * 
	 * @uml.property name="fragment"
	 */
	public void setFragment(String fragment) {
		this.fragment = fragment;
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
	 * @param maxOccurs The maxOccurs to set.
	 * 
	 * @uml.property name="maxOccurs"
	 */
	public void setMaxOccurs(String maxOccurs) {
		this.maxOccurs = maxOccurs;
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
	 * @param minOccurs The minOccurs to set.
	 * 
	 * @uml.property name="minOccurs"
	 */
	public void setMinOccurs(String minOccurs) {
		this.minOccurs = minOccurs;
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
     * @return Returns the nillible.
     */
    public boolean isNillable() {
        return nillable;
    }

	/**
	 * @param nillible The nillible to set.
	 * 
	 * @uml.property name="nillable"
	 */
	public void setNillable(boolean nillible) {
		this.nillable = nillible;
	}

	/**
	 * @return Returns the selectedType.
	 * 
	 * @uml.property name="type"
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param selectedType The selectedType to set.
	 * 
	 * @uml.property name="type"
	 */
	public void setType(String selectedType) {
		this.type = selectedType;
	}

	/**
	 * AttributeType used to limit getType.
	 * 
	 * @return AttributeType
	 * 
	 * @uml.property name="attributeType"
	 */
	public AttributeType getAttributeType() {
		return attributeType;
	}

    /**
     * List of Types available for this attribtue.
     * <p>
     * The names are returned as references (like xs:string).
     * </p>
     */
    public List getTypes() {
    	List elements = DataTransferObjectFactory.getElements(name, attributeType.getType());
        List list = new ArrayList( elements.size() );
        for( Iterator i=elements.iterator(); i.hasNext(); ){
            NameSpaceElement element = (NameSpaceElement) i.next();

            if (!element.isAbstract()) {
            	list.add( element.getTypeDefName() );
            }
        }
        list.add( AttributeTypeInfoConfig.TYPE_FRAGMENT );
        return list;
    }
}

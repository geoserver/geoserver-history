package org.vfny.geoserver.form.data;

import org.vfny.geoserver.config.AttributeTypeInfoConfig;

/**
 * Present Attribute information for user input.
 */
class AttributeForm {
    
    private String name;    
    private boolean nillible;
    private String minOccurs;
    private String maxOccurs;    
    private String selectedType;
    private String fragment;
    
    public AttributeForm( AttributeTypeInfoConfig config ){
        name = config.getName();
        nillible = config.isNillable();
        minOccurs = String.valueOf( config.getMinOccurs() );
        maxOccurs = String.valueOf( config.getMaxOccurs() );
        selectedType = config.getType();
        fragment = config.getFragment();
    }
    /**
     * @return Returns the fragment.
     */
    public String getFragment() {
        return fragment;
    }
    /**
     * @param fragment The fragment to set.
     */
    public void setFragment(String fragment) {
        this.fragment = fragment;
    }
    /**
     * @return Returns the maxOccurs.
     */
    public String getMaxOccurs() {
        return maxOccurs;
    }
    /**
     * @param maxOccurs The maxOccurs to set.
     */
    public void setMaxOccurs(String maxOccurs) {
        this.maxOccurs = maxOccurs;
    }
    /**
     * @return Returns the minOccurs.
     */
    public String getMinOccurs() {
        return minOccurs;
    }
    /**
     * @param minOccurs The minOccurs to set.
     */
    public void setMinOccurs(String minOccurs) {
        this.minOccurs = minOccurs;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the nillible.
     */
    public boolean isNillible() {
        return nillible;
    }
    /**
     * @param nillible The nillible to set.
     */
    public void setNillible(boolean nillible) {
        this.nillible = nillible;
    }
    /**
     * @return Returns the selectedType.
     */
    public String getSelectedType() {
        return selectedType;
    }
    /**
     * @param selectedType The selectedType to set.
     */
    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }
}
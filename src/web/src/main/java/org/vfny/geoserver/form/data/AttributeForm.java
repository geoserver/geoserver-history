/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import org.opengis.feature.type.AttributeDescriptor;
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.global.dto.AttributeTypeInfoDTO;
import org.vfny.geoserver.global.dto.DataTransferObjectFactory;
import org.vfny.geoserver.global.xml.NameSpaceElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Present Attribute information for user input.
 */
public class AttributeForm {
    private String name;
    private boolean nillable;
    private String minOccurs;
    private String maxOccurs;
    private String type;
    private String fragment;
    private AttributeDescriptor attributeType;

    public AttributeForm(AttributeTypeInfoConfig config, AttributeDescriptor attribute) {
        name = config.getName();
        nillable = config.isNillable();

        minOccurs = String.valueOf(config.getMinOccurs());
        maxOccurs = String.valueOf(config.getMaxOccurs());
        type = config.getType();
        fragment = config.getFragment();

        attributeType = attribute;
    }

    public AttributeTypeInfoDTO toDTO() {
        AttributeTypeInfoDTO dto = new AttributeTypeInfoDTO();
        dto.setName(name);
        dto.setNillable(nillable);
        dto.setMinOccurs(Integer.parseInt(minOccurs));
        dto.setMaxOccurs(Integer.parseInt(maxOccurs));

        if (AttributeTypeInfoConfig.TYPE_FRAGMENT.equals(type)) {
            dto.setComplex(true);
            dto.setType(fragment);
        } else {
            dto.setComplex(false);
            dto.setType(type);
        }

        return dto;
    }

    public AttributeTypeInfoConfig toConfig() {
        return new AttributeTypeInfoConfig(toDTO());
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
    public boolean isNillable() {
        return nillable;
    }

    /**
     * @param nillible The nillible to set.
     */
    public void setNillable(boolean nillible) {
        this.nillable = nillible;
    }

    /**
     * @return Returns the selectedType.
     */
    public String getType() {
        return type;
    }

    /**
     * @param selectedType The selectedType to set.
     */
    public void setType(String selectedType) {
        this.type = selectedType;
    }

    /**
     * AttributeDescriptor used to limit getType.
     *
     * @return AttributeDescriptor
     */
    public AttributeDescriptor getAttributeType() {
        return attributeType;
    }

    /**
     * List of Types available for this attribtue.
     * <p>
     * The names are returned as references (like xs:string).
     * </p>
     */
    public List getTypes() {
        List elements = DataTransferObjectFactory.getElements(name, attributeType.getType().getBinding());
        List list = new ArrayList(elements.size());

        for (Iterator i = elements.iterator(); i.hasNext();) {
            NameSpaceElement element = (NameSpaceElement) i.next();

            if (!element.isAbstract()) {
                list.add(element.getTypeDefName());
            }
        }

        list.add(AttributeTypeInfoConfig.TYPE_FRAGMENT);

        return list;
    }
}

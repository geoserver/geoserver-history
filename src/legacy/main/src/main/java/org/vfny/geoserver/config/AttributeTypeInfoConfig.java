/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.opengis.feature.type.AttributeDescriptor;
import org.vfny.geoserver.global.dto.AttributeTypeInfoDTO;
import org.vfny.geoserver.global.xml.NameSpaceElement;
import org.vfny.geoserver.global.xml.NameSpaceTranslator;
import org.vfny.geoserver.global.xml.NameSpaceTranslatorFactory;


/**
 * Allows editing of AttributeTypeInfo.
 *
 * <p>
 * Represents most of a xs:element for an XMLSchema.
 * </p>
 *
 * <p>
 * we have three types of information to store, Schema defined types,
 * references and extentions on types. If the type represented is either  a
 * reference or a Schema defined type  then isRef should be true.
 * </p>
 *
 * <p>
 * Non-complex types are of the form:
 * </p>
 *
 * <ul>
 * <li>
 * <code>{element name='test' type='xs:string'/}</code>
 * </li>
 * <li>
 * <code>{element name='test' type='gml:PointType'/}</code>
 * </li>
 * </ul>
 *
 * <p>
 * These cases have their type name stored in this.type
 * </p>
 *
 * <p>
 * For complex types such as:<pre><code>
 * {element name='test'
 *   {xs:complexContent}
 *     {xs:extension base="gml:AbstractFeatureType"}
 *       {xs:sequence}
 *         {xs:element name="id"
 *                     type="xs:string"
 *                     minOccurs="0"/}
 *         {xs:element ref="gml:pointProperty"
 *                     minOccurs="0"/}
 *       {/xs:sequence}
 *     {/xs:extension}
 *  {/xs:complexContent}
 * {/element}
 * </code></pre>
 * The type will be equals to "(xml fragment)" and
 * fragment contains a similar to above.
 * </p>
 *
 * <p>
 * minOccurs, maxOccurs and nillable are all attributes for all cases. There is
 * more stuff in the XMLSchema spec but we don't care to parse it out right now.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id$
 */
public class AttributeTypeInfoConfig {
    /** Value of getType() used to indicate that fragement is in use */
    public static final String TYPE_FRAGMENT = "(xml fragment)";

    /**
     * XML Fragment used to define stuff.
     *
     * <p>
     * This property is only used with getType() is equals to "(xml fragment)".
     * </p>
     *
     * <p>
     * baseGMLTypes can only be used in your XML fragment.
     * </p>
     */
    private String fragment;

    /**
     * Maxmium number of occurances of this attribute in a feature.
     *
     * <p>
     * For Features based on the Simple Feature Specification this should be a
     * value of 1. If the attribute is optional it should still be 1, although
     * often optional is represented by allowing the Attribute to be
     * <code>nillable</code>.
     * </p>
     *
     * <p>
     * Common Min..Max Occurs values:
     * </p>
     *
     * <ul>
     * <li>
     * 0..<b>1</b>: attribute is optional
     * </li>
     * <li>
     * 1..<b>1</b>: attribute is required (usual for Simple Features)
     * </li>
     * <li>
     * 0..<b>N</b>: attribute forms a list that may be empty
     * </li>
     * </ul>
     *
     *
     * @see AttributeTypeInfoDTO.isNillable
     */
    private int maxOccurs;

    /** attribute min occurs */
    private int minOccurs;

    /** attribute name */
    private final String name;

    /**
     * Indicate if the attribute is allowed to be <code>null</code>.
     *
     * <p>
     * Nillable is often used to indicate that an attribute is optional. The
     * use of minOccurs and maxOccurs may be a more correct way to indicate
     * optional attribtues.
     * </p>
     *
     * @see AttributeTypeInfoDTO.minOccurs
     * @see AttributeTypeInfoDTO.maxOccurs
     */
    private boolean nillable;

    /**
     * Element type, a well-known gml or xs type or <code>TYPE_FRAGMENT</code>.
     *
     * <p>
     * If getType is equals to TYPE_FRAGMENT please consult getFragment() to
     * examin the actual user's definition.
     * </p>
     *
     * <p>
     * Other than that getType should be one of the constants defined by
     * GMLUtils.
     * </p>
     */
    public String type;

    /**
     * Set up AttributeTypeInfo based on attributeType.
     *
     * <p>
     * Set up is determined by the AttributeTypeInfoDTO( AttributeDescriptor )
     * constructor. This allows all Schema generation to be acomplished in the
     * same palce.
     * </p>
     *
     * @param attributeType GeoTools2 attributeType used for configuration
     */
    public AttributeTypeInfoConfig(AttributeDescriptor attributeType) {
        name = attributeType.getLocalName();
        minOccurs = 1;
        maxOccurs = 1;

        NameSpaceTranslatorFactory nsFactory = NameSpaceTranslatorFactory.getInstance();
        NameSpaceTranslator nst = nsFactory.getNameSpaceTranslator("xs");
        NameSpaceElement nse = nst.getElement(name);

        if (nse == null) {
            nse = nst.getDefaultElement(attributeType.getType().getBinding());
        }

        if (nse == null) {
            nst = nsFactory.getNameSpaceTranslator("gml");
            nse = nst.getElement(name);

            if (nse == null) {
                nse = nst.getDefaultElement(attributeType.getType().getBinding());
            }
        }

        //System.out.println("creating new atypininfig for: " + attributeType + 
        //		   ", nse = " + nse);
        //if (nse != null) System.out.println(", nse type = " + nse.getTypeDefName());
        fragment = "<!-- definition for " + attributeType.getType() + " -->";

        if (nse == null) {
            type = TYPE_FRAGMENT;
        } else {
            type = nse.getTypeDefName();
            fragment = "";
        }
    }

    /**
     * Set up AttributeTypeInfo based on Data Transfer Object.
     *
     * @param dto AttributeTypeInfoDTO used for configuration
     */
    public AttributeTypeInfoConfig(AttributeTypeInfoDTO dto) {
        name = dto.getName();

        if (dto.isComplex()) {
            type = TYPE_FRAGMENT;
            fragment = dto.getType();
        } else {
            type = dto.getType();
            fragment = "";
        }

        minOccurs = dto.getMinOccurs();
        maxOccurs = dto.getMaxOccurs();
        nillable = dto.isNillable();
    }

    /**
     * XML Fragment used to define stuff.
     *
     * <p>
     * This property is only used with getType() is equals to "(xml fragment)".
     * </p>
     *
     * <p>
     * baseGMLTypes can only be used in your XML fragment.
     * </p>
     *
     * @return Returns the fragment.
     */
    public String getFragment() {
        return fragment;
    }

    /**
     * getMaxOccurs purpose.
     *
     * <p>
     * The max number of occurences for this element.
     * </p>
     *
     * @return max number of occurences
     */
    public int getMaxOccurs() {
        return maxOccurs;
    }

    /**
     * getMinOccurs purpose.
     *
     * <p>
     * the min number of occurences for this element
     * </p>
     *
     * @return min number of occurences
     */
    public int getMinOccurs() {
        return minOccurs;
    }

    /**
     * getName purpose.
     *
     * <p>
     * returns the element name
     * </p>
     *
     * @return the element name
     */
    public String getName() {
        return name;
    }

    /**
     * Element type, a well-known gml or xs type or <code>TYPE_FRAGMENT</code>.
     *
     * <p>
     * If getType is equals to <code>TYPE_FRAGMENT</code> please consult
     * getFragment() to examine the actual user's definition.
     * </p>
     *
     * <p>
     * Other than that getType should be one of the constants defined by
     * GMLUtils.
     * </p>
     *
     * @return The element, or <code>TYPE_FRAGMENT</code>
     */
    public String getType() {
        return type;
    }

    /**
     * Indicate if the attribute is allowed to be <code>null</code>.
     *
     * <p>
     * Nillable is often used to indicate that an attribute is optional. The
     * use of minOccurs and maxOccurs may be a more correct way to indicate
     * optional attribtues.
     * </p>
     *
     * @return <code>true </code> to indicate attribute is alloed to be
     *         <code>null</code>
     *
     * @see AttributeTypeInfoDTO.setMinOccurs
     * @see AttributeTypeInfoDTO.setMaxOccurs
     */
    public boolean isNillable() {
        return nillable;
    }

    /**
     * XML Fragment used to define stuff.
     *
     * <p>
     * This property is only used with getType() is equals to "(xml fragment)".
     * </p>
     *
     * <p>
     * baseGMLTypes can only be used in your XML fragment.
     * </p>
     *
     * @param fragment The fragment to set.
     */
    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    /**
     * Maxmium number of occurances of this attribute in a feature.
     *
     * <p>
     * For Features based on the Simple Feature Specification this should be a
     * value of 1. If the attribute is optional it should still be 1, although
     * often optional is represented by allowing the Attribute to be
     * <code>nillable</code>.
     * </p>
     *
     * <p>
     * Common Min..Max Occurs values:
     * </p>
     *
     * <ul>
     * <li>
     * 0..<b>1</b>: attribute is optional
     * </li>
     * <li>
     * 1..<b>1</b>: attribute is required (usual for Simple Features)
     * </li>
     * <li>
     * 0..<b>N</b>: attribute forms a list that may be empty
     * </li>
     * </ul>
     *
     *
     * @param max The maximum number of occurances
     *
     * @see AttributeTypeInfoDTO.isNillable
     */
    public void setMaxOccurs(int max) {
        maxOccurs = max;
    }

    /**
     * Minimum number of occrances of this attribute in a feature.
     *
     * <p>
     * For Features based on the Simple Feture Specification this should be a
     * value of 1. If the attribute is optional is should be 0, although often
     * optional is represented by allowing the attribute to be nillable.
     * </p>
     * Common Min..Max Occurs values:
     *
     * <ul>
     * <li>
     * <b>0</b>..1: attribute is optional
     * </li>
     * <li>
     * <b>1</b>..1: attribute is required (usual for Simple Features)
     * </li>
     * <li>
     * <b>0</b>..N: attribute forms a list that may be empty
     * </li>
     * </ul>
     *
     *
     * @param min The minimum number of occurances
     *
     * @see AttributeTypeInfoDTO.isNillable
     */
    public void setMinOccurs(int min) {
        minOccurs = min;
    }

    /**
     * Indicate if the attribute is allowed to be <code>null</code>.
     *
     * <p>
     * Nillable is often used to indicate that an attribute is optional. The
     * use of minOccurs and maxOccurs may be a more correct way to indicate
     * optional attribtues.
     * </p>
     *
     * @param nillable <code>true </code> to indicate attribute is alloed to be
     *        <code>null</code>
     *
     * @see AttributeTypeInfoDTO.setMinOccurs
     * @see AttributeTypeInfoDTO.setMaxOccurs
     */
    public void setNillable(boolean nillable) {
        this.nillable = nillable;
    }

    /**
     * Element type, a well-known gml or xs type or <code>TYPE_FRAGMENT</code>.
     *
     * <p>
     * If getType is equals to <code>TYPE_FRAGMENT</code> please consult
     * getFragment() to examin the actual user's definition. <br>
     * Other than that getType should be one of the constants defined by
     * GMLUtils.
     * </p>
     *
     * @param type DOCUMENT ME!
     */
    public void setType(String type) {
        this.type = type;
    }

    public AttributeTypeInfoDTO toDTO() {
        AttributeTypeInfoDTO dto = new AttributeTypeInfoDTO();
        dto.setNillable(nillable);
        dto.setName(name);
        dto.setMaxOccurs(maxOccurs);
        dto.setMinOccurs(minOccurs);

        if (type != TYPE_FRAGMENT) {
            dto.setComplex(false);
            dto.setType(type);
        } else {
            dto.setComplex(true);
            dto.setType(fragment);
        }

        return dto;
    }
}

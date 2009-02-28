/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import com.vividsolutions.jts.geom.Envelope;

import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.FactoryException;
import org.vfny.geoserver.global.xml.NameSpaceElement;
import org.vfny.geoserver.global.xml.NameSpaceTranslator;
import org.vfny.geoserver.global.xml.NameSpaceTranslatorFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Generate Data Transfer Objects from "real" objects in the system.
 *
 * <p>
 * This class is used to isolate the DTO from the details of generating them.
 * This allows DTO objects to be safely used as a wire protocol with out
 * unrequired dependencies on such things as AttributeDescriptor and SimpleFeatureType.
 * </p>
 *
 * <p>
 * This class may choose to opperate as a facade on the services of global.xml?
 * </p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id$
 */
public class DataTransferObjectFactory {
    /**
     * Construct DTO based on provided AttributeDescriptor.
     *
     * <p>
     * GMLUtils is used to provide the mapping from
     * attributeType.getName/attributeType.getType() to an XML type/fragement.
     * </p>
     *
     * @param attributeType Real geotools2 AttributeDescriptor
     *
     * @return Data Transfer Object for provided attributeType
     */
    public static AttributeTypeInfoDTO create(String schemaBase, AttributeDescriptor attributeType) {
        AttributeTypeInfoDTO dto = new AttributeTypeInfoDTO();
        dto.setName(attributeType.getLocalName());

        if (isManditory(schemaBase, attributeType.getLocalName()) || (attributeType.getMinOccurs() > 0)) {
            dto.setMinOccurs(1);
        } else {
            dto.setMinOccurs(0);
        }

        dto.setMaxOccurs(1);
        dto.setNillable(attributeType.isNillable());

        NameSpaceTranslator xs = NameSpaceTranslatorFactory.getInstance()
                                                           .getNameSpaceTranslator("xs");
        NameSpaceTranslator gml = NameSpaceTranslatorFactory.getInstance()
                                                            .getNameSpaceTranslator("gml");
        NameSpaceElement element;

        element = xs.getElement(attributeType.getType().getBinding(), attributeType.getLocalName());

        if (element == null) {
            element = gml.getElement(attributeType.getType().getBinding(), attributeType.getLocalName());
        }

        if (element == null) {
            element = xs.getElement("string");
        }

        //		element = xs.getElement( attributeType.getName() );                
        //		if(element == null) element = gml.getElement( attributeType.getName() );
        //		if(element == null) element = xs.getElement( "string" );
        dto.setComplex(false);
        dto.setType(element.getTypeRefName());

        return dto;
    }

    /**
     * Construct any of the well-known GML attributeTypes.
     * <p>
     * SchemaBase is used to ensure that attribute required by the XMLSchema
     * have a minOccurs of 1.
     * </p>
     * <p>
     * This method uses NameSpaceTranslatorFactorys for xs and gml in order to
     * provide accurate type information describing the provided attribute
     * </p>
     * @param schemaBase used to determine manditory attributes
     * @param attributeName Name of attribute being described
     * @return DataTransferObject encapsulating attribute information.
     */
    public static AttributeTypeInfoDTO create(String schemaBase, String attributeName) {
        AttributeTypeInfoDTO dto = new AttributeTypeInfoDTO();
        dto.setName(attributeName);
        dto.setMinOccurs(isManditory(schemaBase, attributeName) ? 1 : 0);
        dto.setMaxOccurs(1);
        dto.setNillable(true); // nillable by default?

        NameSpaceTranslator xs = NameSpaceTranslatorFactory.getInstance()
                                                           .getNameSpaceTranslator("xs");
        NameSpaceTranslator gml = NameSpaceTranslatorFactory.getInstance()
                                                            .getNameSpaceTranslator("gml");
        NameSpaceElement element;

        element = xs.getElement(attributeName);

        if (element == null) {
            element = gml.getElement(attributeName);
        }

        if (element == null) {
            element = xs.getElement("string");
        }

        dto.setComplex(false);
        dto.setType(element.getTypeRefName());

        return dto;
    }

    /**
     * Construct DTO based on provided schema.
     *
     * <p>
     * GMLUtils is used to provide the mapping   to an XML type/fragement for
     * each attribute
     * </p>
     *
     * @param dataStoreId Used as a backpointer to locate dataStore
     * @param schema Real geotools2 SimpleFeatureType
     *
     * @return Data Transfer Object for provided schema
     */
    public static FeatureTypeInfoDTO create(String dataStoreId, SimpleFeatureType schema) {
        FeatureTypeInfoDTO dto = new FeatureTypeInfoDTO();
        dto.setAbstract(null);
        dto.setDataStoreId(dataStoreId);
        dto.setDefaultStyle("styles/normal.sld");
        dto.setDefinitionQuery(null); // no extra restrictions
        dto.setDirName(dataStoreId + "_" + schema.getTypeName());
        dto.setKeywords(Collections.EMPTY_LIST);
        dto.setLatLongBBox(new Envelope());
        dto.setNativeBBox(new Envelope());
        dto.setName(schema.getTypeName());
        dto.setNumDecimals(8);
        dto.setSchemaAttributes(generateAttributes(schema));

        NameSpaceTranslator gml = NameSpaceTranslatorFactory.getInstance()
                                                            .getNameSpaceTranslator("gml");
        String schemaBase = gml.getElement("AbstractFeatureType").getQualifiedTypeDefName();
        dto.setSchemaBase(schemaBase);

        dto.setSchemaName(dataStoreId.toUpperCase() + "_" + schema.getTypeName().toUpperCase()
            + "_TYPE");
        Integer epsgCode = null;
        try {
            CRS.lookupEpsgCode( schema.getCoordinateReferenceSystem(), true );
        }
        catch( FactoryException e ) {
            // log this?
        }
        if ( epsgCode != null ) {
            dto.setSRS(epsgCode.intValue());    
        }
        else {
            dto.setSRS(0);
        }
        dto.setTitle(schema.getName().getNamespaceURI() + " " + schema.getTypeName());

        return dto;
    }

    /**
     * List of attributes DTO information gernated from schema.
     *
     * @param schema
     * @return
     */
    public static List generateAttributes(SimpleFeatureType schema) {
        List attributes = schema.getAttributeDescriptors();
        
        List list = new ArrayList(attributes.size());

        for (int i = 0; i < attributes.size(); i++) {
            list.add(create("AbstractFeatureType", (AttributeDescriptor)attributes.get(i)));
        }

        return list;
    }

    /**
     * List of attribtue DTO information generated from schemaBase.
     * <p>
     * Please note this is currently only used for display by TypesForm,
     * TypeInfo simply makes use of getRequiredBaseAttributes to select
     * AttributeTypes from the SimpleFeatureType schema.
     * </p>
     * <p>
     * More specifically the values of isNillable, minOccurs and maxOccurs
     * provided by the DataStore may not agree with the results of this
     * function. TypeInfo opperatates on the assumption minOccurs=1, maxOccurs=1
     * and AttributeDescriptor.isNillable() is correct.
     * </p>
     * @param schemaBase SchemaBase
     * @return List of AttributeTypeInfoDTO representative of schemaBase required
     *         Attributes
     */
    public static List generateRequiredAttributes(String schemaBase) {
        String[] attributeNames = getRequiredBaseAttributes(schemaBase);

        List list = new ArrayList(attributeNames.length);

        for (int i = 0; i < attributeNames.length; i++) {
            list.add(create(schemaBase, attributeNames[i]));
        }

        return list;
    }

    /**
     * Test is attribute is a required attribtue of schemaBase.
     *
     * @return <code>True</code> if attribute is required for schemaBase
     */
    public static boolean isManditory(String schemaBase, String attribute) {
        String[] required = getRequiredBaseAttributes(schemaBase);

        for (int i = 0; i < required.length; i++) {
            if (attribute.equals(required[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Required Attributes for schemaBase.
     * <p>
     * This information is a hardcoded representation of what woudl be available
     * if we had actually parsed the GML XMLSchema.
     * </p>
     * @param schemaBase
     * @return
     */
    public static String[] getRequiredBaseAttributes(String schemaBase) {
        if (schemaBaseMap.containsKey(schemaBase)) {
            return (String[]) schemaBaseMap.get(schemaBase);
        }

        return new String[] {  };
    }

    public static Map schemaBaseMap = new HashMap();

    static {
        schemaBaseMap.put("gml:AbstractFeatureType", new String[] {  }); //"description","name","boundedBy"} );
                                                                         /*schemaBaseMap.put("AbstractFeatureCollectionBaseType",
        new String[] {"description","name","boundedBy"} );
        schemaBaseMap.put("GeometryPropertyType",
        new String[] {"geometry"} );
        schemaBaseMap.put("FeatureAssociationType",
        new String[] {"feature"} );
        schemaBaseMap.put("BoundingShapeType",
        new String[] {"box"} );
        schemaBaseMap.put("PointPropertyType",
        new String[] {"point"} );
        schemaBaseMap.put("PolygonPropertyType",
        new String[] {"polygon"} );
        schemaBaseMap.put("LineStringPropertyType",
        new String[] {"lineString"} );
        schemaBaseMap.put("MultiPointPropertyType",
        new String[] {"multiPoint"} );
        schemaBaseMap.put("MultiLineStringPropertyType",
        new String[] {"multiLineString"} );
        schemaBaseMap.put("MultiPolygonPropertyType",
        new String[] {"multiPolygonString"} );
        schemaBaseMap.put("MultiGeometryPropertyType",
        new String[] {"multiGeometry"} );
        schemaBaseMap.put("NullType", new String[] {} );*/
    }

    /**
     * Mappings for name and type, or null if not found.
     * <p>
     * List construction order:
     * <ul>
     * <li>Use of property types if name and exact type match one of the gml
     *     properties references.<br>
     *     For <code>name="pointProperty", type=com.vividsolutions.jts.geom.Point</code> maps to:
     *     <b>gml:PointPropertyType</b>
     *     </li>
     * <li>Search the schema for defined types are checked for an exact match
     *     based on type.<br>
     *     For <code>type=java.lang.String</code> maps to:
     *      <b>xs:string</b>
     *      </li>
     * <li>A linear seach of the defined types is made making use of
     *     isAssignable.<br>
     *     For <code>type=com.vividsolutions.jts.geom.Geometry</code> maps to:
     *     <b>gml:PointType gml:LineStringType gml:LinearRingType gml:BoxType gml:PolygonType gml:GeometryCollectionType gml:MultiPointType gml:MultiLineStringType, gml:MultiPolygonType</b>
     *     </li>
     * <li>As a wild assumption we assume <code>xs:string</code> can be used.<br>
     *     For <code>type=java.net.URL</code> maps to: <b>xs:string</b>
     *    </li>
     * </ul>
     * <p>
     * All mappings are consulted using using a linear search.
     * The list is returned in the order of most specific to least specific.
     * </p>
     * Complete Example:
     * <code>name="pointProperty", class=type=com.vividsolutions.jts.geom.Point</code>
     * <p>
     * Expected Mapping:
     * </p>
     * <ul>
     * <li>gml:PointPropertyType - pointProperty & Point.class match</li>
     * <li>gml:PointType - Point.class match</li>
     * <li>gml:AbstractGeometry - Point instance of Geometry match</li>
     * <li>xs:string - String assumption</li>
     * </ul>
     * @param name attribute name
     * @param type attribtue type
     * @return List of NameSpaceElements is returned in the order of most specific to least specific.
     */
    public static List getElements(String name, Class type) {
        NameSpaceTranslator xs = NameSpaceTranslatorFactory.getInstance()
                                                           .getNameSpaceTranslator("xs");
        NameSpaceTranslator gml = NameSpaceTranslatorFactory.getInstance()
                                                            .getNameSpaceTranslator("gml");
        List result = new LinkedList();

        if ((name == null) || (name == "")) {
            throw new NullPointerException("Element name must be defined.");
        }

        if (type == null) {
            throw new NullPointerException("Element type must be defined.");
        }

        Set s = xs.getAssociatedTypes(type);
        s.addAll(xs.getAssociatedTypes(name));
        s.addAll(gml.getAssociatedTypes(type));
        s.addAll(gml.getAssociatedTypes(name));

        Iterator i = s.iterator();

        while (i.hasNext()) {
            NameSpaceElement element = (NameSpaceElement) i.next();

            if (name.equals(element.getTypeDefName())) {
                if (!result.contains(element)) {
                    result.add(element);
                } else if (name.equals(element.getTypeRefName())) {
                    if (!result.contains(element)) {
                        result.add(element);
                    } else if (name.equals(element.getQualifiedTypeDefName())) {
                        if (!result.contains(element)) {
                            result.add(element);
                        } else if (name.equals(element.getQualifiedTypeRefName())) {
                            if (!result.contains(element)) {
                                result.add(element);
                            }
                        }
                    }
                }
            }
        }

        if (!Object.class.equals(type)) {
            Class cls = type;

            while (!Object.class.equals(cls)) {
                i = s.iterator();

                while (i.hasNext()) {
                    NameSpaceElement element = (NameSpaceElement) i.next();

                    // 	add the rest afterwards
                    if (element.getJavaClass().equals(cls) && !result.contains(element)) {
                        result.add(element);
                    }
                }

                cls = cls.getSuperclass();
            }
        }

        i = s.iterator();

        while (i.hasNext()) {
            NameSpaceElement element = (NameSpaceElement) i.next();

            // 	add the rest afterwards
            if (!result.contains(element)) {
                result.add(element);
            }
        }

        NameSpaceElement element = xs.getElement("string");

        if (!result.contains(element)) {
            result.add(element);
        }

        return result;
    }

    /**
     * Retrive best NameSpaceElement match for provided attribtue name and type.
     * <p>
     * Best match is determined by the search order defined by getElements.
     * </p>
     * @param name
     * @param type
     * @return Closest NameSapceElement
     */
    private static final NameSpaceElement getBestMatch(String name, Class type) {
        return (NameSpaceElement) getElements(name, type).get(0);
    }
}

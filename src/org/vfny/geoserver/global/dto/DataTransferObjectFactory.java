/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.global.xml.NameSpaceElement;
import org.vfny.geoserver.global.xml.NameSpaceTranslator;
import org.vfny.geoserver.global.xml.NameSpaceTranslatorFactory;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Generate Data Transfer Objects from "real" objects in the system.
 * 
 * <p>
 * This class is used to isolate the DTO from the details of generating them.
 * This allows DTO objects to be safely used as a wire protocol with out
 * unrequired dependencies on such things as AttributeType and FeatureType.
 * </p>
 * 
 * <p>
 * This class may choose to opperate as a facade on the services of global.xml?
 * </p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: DataTransferObjectFactory.java,v 1.10 2004/03/02 10:18:43 jive Exp $
 */
public class DataTransferObjectFactory {
    /**
     * Construct DTO based on provided attributeType.
     * 
     * <p>
     * GMLUtils is used to provide the mapping from
     * attributeType.getName/attributeType.getType() to an XML type/fragement.
     * </p>
     *
     * @param attributeType Real geotools2 AttributeType
     *
     * @return Data Transfer Object for provided attributeType
     */
    public static AttributeTypeInfoDTO create(AttributeType attributeType) {
        AttributeTypeInfoDTO dto = new AttributeTypeInfoDTO();

        dto.setName(attributeType.getName());        
        dto.setMinOccurs(0);
        dto.setMaxOccurs(1);
        dto.setNillable( attributeType.isNillable() );        
        NameSpaceTranslator nst1 = NameSpaceTranslatorFactory.getInstance().getNameSpaceTranslator("xs");
        NameSpaceTranslator nst2 = NameSpaceTranslatorFactory.getInstance().getNameSpaceTranslator("gml");
        NameSpaceElement nse = nst1.getElement(attributeType.getName());
        if(nse == null)
        	nse = nst2.getElement(attributeType.getName());

        if (nse == null) {
            dto.setComplex(false);
            dto.setType(nst1.getElement("string").getTypeRefName());
        } else {
            dto.setComplex(false);
            dto.setType(nse.getTypeRefName());
        }

        return dto;
    }

    /**
     * Construct any of the well-known GML attributeTypes.
     */
    public static AttributeTypeInfoDTO create( String schemaBase, String attributeName ){
        AttributeTypeInfoDTO dto = new AttributeTypeInfoDTO();

        dto.setName( attributeName );
        dto.setMinOccurs(0);
        dto.setMaxOccurs(1);
        NameSpaceTranslator nst1 = NameSpaceTranslatorFactory.getInstance().getNameSpaceTranslator("xs");
        NameSpaceTranslator nst2 = NameSpaceTranslatorFactory.getInstance().getNameSpaceTranslator("gml");
        NameSpaceElement nse = nst1.getElement( attributeName );
        
        if(nse == null)
            nse = nst2.getElement( attributeName );

        if (nse == null) {
            dto.setComplex(false);
            dto.setType( nst1.getElement("string").getTypeRefName() );
        } else {
            dto.setComplex(false);
            dto.setType( nse.getTypeRefName() );
        }
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
     * @param schema Real geotools2 FeatureType
     *
     * @return Data Transfer Object for provided schema
     */
    public static FeatureTypeInfoDTO create(String dataStoreId,
        FeatureType schema) {
        FeatureTypeInfoDTO dto = new FeatureTypeInfoDTO();
        dto.setAbstract(null);
        dto.setDataStoreId(dataStoreId);
        dto.setDefaultStyle("styles/normal.sld");
        dto.setDefinitionQuery(null); // no extra restrictions
        dto.setDirName(dataStoreId + "_" + schema.getTypeName());
        dto.setKeywords(Collections.EMPTY_LIST);
        dto.setLatLongBBox(new Envelope());
        dto.setName(schema.getTypeName());
        dto.setNumDecimals(8);
        dto.setSchemaAttributes(generateAttributes(schema));
        dto.setSchemaBase(NameSpaceTranslatorFactory.getInstance().getNameSpaceTranslator("gml").getElement("AbstractFeatureType").getTypeDefName());
        dto.setSchemaName(dataStoreId.toUpperCase() + "_"
            + schema.getTypeName().toUpperCase() + "_TYPE");
        dto.setSRS(schema.getDefaultGeometry().getGeometryFactory().getSRID());
        dto.setTitle(schema.getNamespace() + " " + schema.getTypeName());

        return dto;
    }

    /**
     * List of attribtues DTO information gernated from schema.
     * 
     * @param schema
     * @return
     */
    public static List generateAttributes(FeatureType schema) {
        AttributeType[] attributes = schema.getAttributeTypes();
        List list = new ArrayList(attributes.length);

        for (int i = 0; i < attributes.length; i++) {
            list.add(create(attributes[i]));
        }
        return list;
    }
    /**
     * List of attribtue DTO information generated from schemaBase.
     * 
     * @param schemaBase
     * @return
     */
    public static List generateAttribtues( String schemaBase ){
        String attributeNames[] = getRequiredBaseAttributes( schemaBase );
        
        List list = new ArrayList(attributeNames.length);
        for (int i = 0; i < attributeNames.length; i++) {
            list.add(create(schemaBase,attributeNames[i]));
        }
        return list;    	
    }
    
    public static String[] getRequiredBaseAttributes(String schemaBase){
        if("AbstractFeatureType".equals(schemaBase)){
            return new String[] {"description","name","boundedBy"};
        }
        if("AbstractFeatureCollectionBaseType".equals(schemaBase)){
            return new String[] {"description","name","boundedBy"};
        }
        if("GeometryPropertyType".equals(schemaBase)){
            return new String[] {"geometry"};
        }
        if("FeatureAssociationType".equals(schemaBase)){
            return new String[] {"feature"};
        }
        if("BoundingShapeType".equals(schemaBase)){
            return new String[] {"box"};
        }
        if("PointPropertyType".equals(schemaBase)){
            return new String[] {"point"};
        }
        if("PolygonPropertyType".equals(schemaBase)){
            return new String[] {"polygon"};
        }
        if("LineStringPropertyType".equals(schemaBase)){
            return new String[] {"lineString"};
        }
        if("MultiPointPropertyType".equals(schemaBase)){
            return new String[] {"multiPoint"};
        }
        if("MultiLineStringPropertyType".equals(schemaBase)){
            return new String[] {"multiLineString"};
        }
        if("MultiPolygonPropertyType".equals(schemaBase)){
            return new String[] {"multiPolygonString"};
        }
        if("MultiGeometryPropertyType".equals(schemaBase)){
            return new String[] {"multiGeometry"};
        }
        if("NullType".equals(schemaBase)){
            return new String[] {};
        }
        return new String[] {};
    }    
}

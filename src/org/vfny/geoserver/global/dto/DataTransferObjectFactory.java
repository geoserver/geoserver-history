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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: DataTransferObjectFactory.java,v 1.13 2004/03/02 22:29:43 dmzwiers Exp $
 */
public class DataTransferObjectFactory {
    /**
     * Construct DTO based on provided AttributeType.
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
    public static AttributeTypeInfoDTO create(String schemaBase, AttributeType attributeType) {            
        AttributeTypeInfoDTO dto = new AttributeTypeInfoDTO();
        dto.setName( attributeType.getName() );
        dto.setMinOccurs( isManditory(schemaBase, attributeType.getName() ) ? 1: 0 );
        dto.setMaxOccurs( 1 );
        dto.setNillable( attributeType.isNillable() );
        NameSpaceTranslator xs = NameSpaceTranslatorFactory.getInstance().getNameSpaceTranslator("xs");        
        NameSpaceTranslator gml = NameSpaceTranslatorFactory.getInstance().getNameSpaceTranslator("gml");
        NameSpaceElement element;
        
        element = xs.getElement( attributeType.getName() );                
        if(element == null) element = gml.getElement( attributeType.getName() );
        if(element == null) element = xs.getElement( "string" );
                
        dto.setComplex(false);
        dto.setType( element.getTypeRefName() );
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
     * @param schemaBase used to determine manditory attribtues
     * @param attributeName Name of attribute being described
     * @return DataTransferObject encapsulating attribute information. 
     */
    public static AttributeTypeInfoDTO create( String schemaBase, String attributeName ){
        AttributeTypeInfoDTO dto = new AttributeTypeInfoDTO();
        dto.setName( attributeName );
        dto.setMinOccurs( isManditory(schemaBase, attributeName ) ? 1: 0 );
        dto.setMaxOccurs( 1 );
        dto.setNillable( true ); // nillable by default?
        
        NameSpaceTranslator xs = NameSpaceTranslatorFactory.getInstance().getNameSpaceTranslator("xs");        
        NameSpaceTranslator gml = NameSpaceTranslatorFactory.getInstance().getNameSpaceTranslator("gml");
        NameSpaceElement element;
        
        element = xs.getElement( attributeName );                
        if(element == null) element = gml.getElement( attributeName );
        if(element == null) element = xs.getElement( "string" );
                
        dto.setComplex(false);
        dto.setType( element.getTypeRefName() );
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
            list.add(create("AbstractFeatureType",attributes[i]));
        }
        return list;
    }
    /**
     * List of attribtue DTO information generated from schemaBase.
     * <p>
     * Please note this is currently only used for display by TypesForm,
     * TypeInfo simply makes use of getRequiredBaseAttributes to select
     * AttributeTypes from the FeatureType schema.
     * </p>
     * <p>
     * More specifically the values of isNillable, minOccurs and maxOccurs
     * provided by the DataStore may not agree with the results of this
     * function. TypeInfo opperatates on the assumption minOccurs=1, maxOccurs=1
     * and AttributeType.isNillable() is correct.
     * </p>
     * @param schemaBase SchemaBase
     * @return List of AttributeTypeInfoDTO representative of schemaBase required
     *         Attribtues
     */
    public static List generateRequiredAttribtues( String schemaBase ){
        String attributeNames[] = getRequiredBaseAttributes( schemaBase );
        
        List list = new ArrayList(attributeNames.length);
        for (int i = 0; i < attributeNames.length; i++) {
            list.add(create(schemaBase,attributeNames[i]));
        }
        return list;    	
    }
    /**
     * Test is attribute is a required attribtue of schemaBase.
     * 
     * @return <code>True</code> if attribute is required for schemaBase
     */ 
    public static boolean isManditory( String schemaBase, String attribute ){
        String required[] = getRequiredBaseAttributes( schemaBase );
        for( int i=0; i<required.length; i++){
            if( attribute.equals( required[i]) ){
                return true;
            }
        }
        return false;
    }
    /**
     * Required Attribtues for schemaBase.
     * <p>
     * This information is a hardcoded representation of what woudl be available
     * if we had actually parsed the GML XMLSchema.
     * </p>
     * @param schemaBase
     * @return
     */
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
    /**
     * Mappings for name and type, or null if not found.
     * <p>
     * List construction order:
     * <ul>
     * <li>Use of property types if name and exact type match one of the gml properties references.
     *     For <code>name="pointProperty", type=com.vividsolutions.jts.geom.Point</code> maps to:
     *     <b>gml:PointPropertyType</b>
     *     </li>
     * <li>Search the schema for defined types are checked for an exact match based on type.
     *     For <code>type=java.lang.String</code> maps to:
     *      <b>xs:string</b>
     *      </li>
     * A linear seach of the defined types is made making use of isAssignable.
     * For type=com.vividsolutions.jts.geom.Geometry maps to: gml:PointType gml:LineStringType gml:LinearRingType gml:BoxType gml:PolygonType gml:GeometryCollectionType gml:MultiPointType gml:MultiLineStringType, gml:MultiPolygonType
     * All mappings are consulted using using a linear search.
     * As a wild assumption we assume xs:string can be used.
     * For type=java.net.URL maps to: xs:string

    This list is returned in the order of most specific to least specific.

    Complete Example:
    name="pointProperty", class=type=com.vividsolutions.jts.geom.Point

    Expected Mapping:

        * gml:PointPropertyType - pointProperty & Point.class match
        * gml:PointType - Point.class match
        * gml:AbstractGeometry - Point instance of Geometry match
        * xs:string - String assumption

    @param name - DOCUMENT ME!
    @param type - Type to look up schema for 
    @return List is returned in the order of most specific to least specific.

 
     */
    public List getElements(String name, Class type){
    	NameSpaceTranslator xs = NameSpaceTranslatorFactory.getInstance().getNameSpaceTranslator("xs");        
    	NameSpaceTranslator gml = NameSpaceTranslatorFactory.getInstance().getNameSpaceTranslator("gml");
    	List result = new LinkedList();
    	
    	if(name==null || name == "")
    		throw new NullPointerException("Element name must be defined.");
    	if(type==null)
    		throw new NullPointerException("Element type must be defined.");
    	
    	Set s = xs.getElements(type);s.addAll(gml.getElements(type)); 
    	Iterator i = s.iterator();
    	while(i.hasNext()){
        	NameSpaceElement element = (NameSpaceElement)i.next();
        	if(name.equals(element.getTypeDefName()))
        		if(!result.contains(element)) result.add(element);
            if(name.equals(element.getTypeRefName()))
        		if(!result.contains(element)) result.add(element);
            if(name.equals(element.getQualifiedTypeDefName()))
        		if(!result.contains(element)) result.add(element);
            if(name.equals(element.getQualifiedTypeRefName()))
        		if(!result.contains(element)) result.add(element);
    	}
    	
    	i = s.iterator();
    	while(i.hasNext()){
        	NameSpaceElement element = (NameSpaceElement)i.next();
        	// add the rest afterwards
        	if(!result.contains(element)) result.add(element);
    	}
    	
    	// order may not be exact here.
    	s = xs.getAssociatedTypes(type);s.addAll(gml.getAssociatedTypes(type));
    	i = s.iterator();
    	while(i.hasNext()){
        	NameSpaceElement element = (NameSpaceElement)i.next();
        	// add the rest afterwards
        	if(!result.contains(element)) result.add(element);
    	}
    	
    	NameSpaceElement element = xs.getElement("string");
    	if(!result.contains(element)) result.add(element);
    	
        return result; // fix me
    }    
}

/*
 * Created on Jan 12, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.global.xml;

/**
 * @author dzwiers
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GMLUtils {
	private static final String[] xmlSchemaTypes = {"decimal", "integer", "negativeInteger", "nonNegativeInteger", "positiveInteger", "long", "int", "short", "byte", "unsignedLong", "unsignedShort", "unsignedInt", "unsignedByte", "float", "double", "date", "dateTime", "duration", "gDay", "gMonth", "gMonthDay", "gYear", "gYearMonth", "time", "ID", "IDREF", "IDREFS", "ENTITY", "ENTITIES", "NMTOKEN", "NMTOKENS", "NOTATION", "string", "normalizedString", "token", "QName", "Name", "NCName"};
	private static final String[] gmlTypes = {"PointType", "LineStringType", "LinearRingType", "BoxType", "PolygonType", "GeometryCollectionType", "MultiPointType", "MultiLineStringType", "MultiPolygonType", "CoordType", "CoordinatesType", "PointPropertyType", "PolygonPropertyType", "LineStringPropertyType", "MultiPointPropertyType", "MultiLineStringPropertyType", "MultiPolygonPropertyType", "MultiGeometryPropertyType", "NullType"};
	private static final String[] baseGmlTypes = {"AbstractFeatureType", "AbstractFeatureCollectionBaseType", "AbstractFeatureCollectionType", "GeometryPropertyType", "FeatureAssociationType", "BoundingShapeType", "AbstractGeometryType", "AbstractGeometryCollectionBaseType", "AssociationAttributeGroup", "GeometryAssociationType", "PointMemberType", "LineStringMemberType", "PolygonMemberType", "LinearRingMemberType"};
	private GMLUtils(){}
	
	public static boolean isXMLSchemaElement(String s){
		return search(xmlSchemaTypes,s);
	}
	
	public static boolean isGMLSchemaElement(String s){
		return search(gmlTypes,s);
	}
	
	public static boolean isGMLAbstractSchemaElement(String s){
		return search(baseGmlTypes,s);
	}
	
	private static boolean search(String[] a, String s){
		// deal with namespace
		int t = s.lastIndexOf(":");
		if(t!=-1)
			s = s.substring(t);
		
		//find s in list 
		for(int i=0;i<a.length;i++)
			if(a[i]==s)
				return true;
		return false;
	}
	
	public static String[] getGmlAbstractTypes(boolean namespace){
		if(namespace)
			return addNS(baseGmlTypes,"gml");
		return baseGmlTypes;
	}
	
	public static String[] getGmlTypes(boolean namespace){
		if(namespace)
			return addNS(gmlTypes,"gml");
		return gmlTypes;
	}
	
	public static String[] getXmlSchemaTypes(boolean namespace){
		if(namespace)
			return addNS(xmlSchemaTypes,"xs");
		return xmlSchemaTypes;
	}
	
	private static String[] addNS(String[] a, String ns){
		String[] b = new String[a.length];
		for(int i=0;i<a.length;i++)
			b[i] = ns+":"+a[i];
		return b;
	}
}

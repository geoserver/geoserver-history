/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.xml;

import java.math.BigDecimal;
import java.util.Date;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Utility class defining GML constants, and utility functions.
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: GMLUtils.java,v 1.2 2004/01/14 09:59:51 jive Exp $
 */
public class GMLUtils {
    public static final Mapping DECIMAL = new Mapping("xs:decimal",BigDecimal.class );
    public static final Mapping INTEGER = new Mapping("xs:integer",Integer.class );    
    public static final Mapping NEGATIVEINTERGER = new Mapping("xs:negativeInteger",Integer.class );
    public static final Mapping POSTIVEINTEGER = new Mapping("xs:positiveInteger", Integer.class );
    public static final Mapping LONG = new Mapping("xs:long", Long.TYPE );
    public static final Mapping INT = new Mapping("xs:int", Integer.TYPE );    
    public static final Mapping SHORT = new Mapping("xs:short", Short.TYPE);
    public static final Mapping BYTE = new Mapping("xs:byte", Byte.TYPE );
    public static final Mapping UNSIGNEDLONG = new Mapping("xs:unsignedLong", Long.TYPE );
    public static final Mapping UNSIGNEDSHORT = new Mapping("xs:unsignedShort", Short.TYPE );    
    public static final Mapping UNSIGNEDINT = new Mapping("xs:unsignedInt", Integer.TYPE );
    public static final Mapping UNSIGNEDBYTE = new Mapping("xs:unsginedByte", Byte.TYPE );    
    public static final Mapping FLOAT = new Mapping("xs:float", Float.TYPE );    
    public static final Mapping DOUBLE = new Mapping("xs:double", Double.TYPE );
    public static final Mapping DATE = new Mapping("xs:date", Date.class );    
    public static final Mapping DATETIME = new Mapping("xs:dateTime" );
    public static final Mapping DURATION = new Mapping("xs:duration" );
    public static final Mapping GTYPE = new Mapping("xs:gDay" );
    public static final Mapping GMONTH = new Mapping("xs:gMonth" );
    public static final Mapping GMONTHDAY = new Mapping("xs:gMonthData" );
    public static final Mapping GYEAR = new Mapping("xs:gYear" );
    public static final Mapping GYEARMONTH = new Mapping("xs:gYearMonth" );
    public static final Mapping TIME = new Mapping("xs:time" );
    public static final Mapping ID = new Mapping("xs:ID" );
    public static final Mapping IDREF = new Mapping("xs:IDREF" );
    public static final Mapping ENTITY = new Mapping("xs:ENTITY" );
    public static final Mapping ENTITIES = new Mapping("xs:ENTITIES" );
    public static final Mapping NMTOKEN = new Mapping("xs:NMTOKENS" );
    public static final Mapping NOTATION = new Mapping("xs:NOTATION" );
    public static final Mapping STRING = new Mapping("xs:string" );
    public static final Mapping NORMALIZEDSTRING = new Mapping("xs:normalizedString" );
    public static final Mapping TOKEN = new Mapping("xs:token" );    
    public static final Mapping QNAME = new Mapping("xs:QName" );
    public static final Mapping NAME = new Mapping("xs:Name" );
    public static final Mapping NCNAME = new Mapping("xs:NCName" );
    
    private static final String[] xmlSchemaTypes = {
        "decimal","integer","negativeInteger","nonNegativeInteger",
        "positiveInteger", "long","int","short","byte","unsignedLong", 
        "unsignedShort", "unsignedInt","unsignedByte","float", 
        "double", "date","dateTime","duration", 
        "gDay", "gMonth", "gMonthDay", "gYear", "gYearMonth", "time",
        "ID", "IDREF", "IDREFS", "ENTITY", "ENTITIES", "NMTOKEN", "NMTOKENS",
        "NOTATION", "string", "normalizedString", "token", "QName", "Name",
        "NCName"
    };
    public static final Mapping TYPE = new Mapping("gml:type" );
    
    public static final Mapping POINTTYPE = new Mapping("gml:PointType", Point.class );
    public static final Mapping LINESTRINGTYPE = new Mapping("xs:LineStringType", LineString.class );
    public static final Mapping LINEARRINGTYPE = new Mapping("gml:LinearRingType", LinearRing.class );    
    public static final Mapping BOXTYPE = new Mapping("gml:BoxType", Envelope.class );
    public static final Mapping POLYGONTYPE = new Mapping("gml:PolygonType", Polygon.class );
    public static final Mapping GEOMETRYCOLLECTIONTYPE = new Mapping("gml:GeometryCollectionType", GeometryCollection.class );    
    public static final Mapping MULTIPOINTTYPE= new Mapping("gml:MultiPointType", MultiPoint.class );
    public static final Mapping MULTILINESTRINGTYPE = new Mapping("gml:MultiLineString", MultiLineString.class );
    public static final Mapping MULTIPOLYGONTYPE = new Mapping("gml:MultiPolygonType", MultiPolygon.class );
    public static final Mapping COORDTYPE = new Mapping("gml:CoordType", Coordinate.class );
    public static final Mapping COORDINATESTYPE = new Mapping("gml:CoordinatesType", CoordinateList.class );
    public static final Mapping POINTPROPERTYTYPE = new Mapping("gml:PointPropertyType", Point.class ); // named
    public static final Mapping POLYGONPROPERTYTYPE = new Mapping( "gml:PolygonPropertyType", Polygon.class ); // named
    public static final Mapping LINESTRINGPROPERTYTYPE = new Mapping("gml:LineStringPropertyType", LineString.class ); // named
    public static final Mapping MULTIPOINTPROPERTYTYPE = new Mapping("gml:MultiPointPropertyType", MultiPoint.class ); // named
    public static final Mapping MULTILINESTRING = new Mapping("gml:type" );
    
    
    
    
    private static final String[] gmlTypes = {
        "PointType", "LineStringType", "LinearRingType", "BoxType",
        "PolygonType", "GeometryCollectionType", "MultiPointType",
        "MultiLineStringType", "MultiPolygonType", "CoordType",
        "CoordinatesType", "PointPropertyType", "PolygonPropertyType",
        "LineStringPropertyType", "MultiPointPropertyType",
        "MultiLineStringPropertyType", "MultiPolygonPropertyType",
        "MultiGeometryPropertyType", "NullType"
    };
    private static final String[] baseGmlTypes = {
        "AbstractFeatureType", "AbstractFeatureCollectionBaseType",
        "AbstractFeatureCollectionType", "GeometryPropertyType",
        "FeatureAssociationType", "BoundingShapeType", "AbstractGeometryType",
        "AbstractGeometryCollectionBaseType", "AssociationAttributeGroup",
        "GeometryAssociationType", "PointMemberType", "LineStringMemberType",
        "PolygonMemberType", "LinearRingMemberType"
    };

    private GMLUtils() {
    }

    public static boolean isXMLSchemaElement(String s) {
        return search(xmlSchemaTypes, s);
    }

    public static boolean isGMLSchemaElement(String s) {
        return search(gmlTypes, s);
    }

    public static boolean isGMLAbstractSchemaElement(String s) {
        return search(baseGmlTypes, s);
    }

    private static boolean search(String[] a, String s) {
        // deal with namespace
        int t = s.lastIndexOf(":");

        if (t != -1) {
            s = s.substring(t);
        }

        //find s in list 
        for (int i = 0; i < a.length; i++)
            if (a[i] == s) {
                return true;
            }

        return false;
    }

    public static String[] getGmlAbstractTypes(boolean namespace) {
        if (namespace) {
            return addNS(baseGmlTypes, "gml");
        }

        return baseGmlTypes;
    }

    public static String[] getGmlTypes(boolean namespace) {
        if (namespace) {
            return addNS(gmlTypes, "gml");
        }

        return gmlTypes;
    }

    public static String[] getXmlSchemaTypes(boolean namespace) {
        if (namespace) {
            return addNS(xmlSchemaTypes, "xs");
        }

        return xmlSchemaTypes;
    }

    private static String[] addNS(String[] a, String ns) {
        String[] b = new String[a.length];

        for (int i = 0; i < a.length; i++)
            b[i] = ns + ":" + a[i];

        return b;
    }
    
    /**
     * Used to store Java/GML type mappings for use with
     * GMLUtils.
     * 
     * @author jgarnett, Refractions Research, Inc.
     * @author $Author: jive $ (last modification)
     * @version $Id: GMLUtils.java,v 1.2 2004/01/14 09:59:51 jive Exp $
     */
    public static class Mapping {
        public final String prefix;
        public final String schema;
        public final Class type;
        public Mapping( String xmlSchema ){
            this( xmlSchema, Object.class );           
        }
        public Mapping( String xmlSchema, Class type ){
            String split[] = xmlSchema.split(":");
            this.prefix = split[0];
            this.schema = split[1];
            this.type = type;
        }
    }
    
}
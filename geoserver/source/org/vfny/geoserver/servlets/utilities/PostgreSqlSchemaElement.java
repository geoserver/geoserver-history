/*
 * Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This servlet implements the OGC WFS getCapabilitiesResponse inteface.
 *
 */
package org.vfny.geoserver.servlets.utilities;

import java.io.*;
import java.util.*;
import java.sql.*;

/**
 * Encapsulates and abstracts a PostgreSQL-specific  'column' object for GML writing.
 * 
 * Passed a column and table name, this looks at a constructor generated hash and
 * matches up appropriate datatype to column schema definition.
 * 
 * THIS CLASS MUST GET ENHANCED ERROR CHECKING (DOES TABLE AND COL EXIST?).
 * SHOULD ABSTRACT HASH OUT OF CLASS TO GENERALIZE.
 * SHOULD ABSTRACT GML FORMATING OUT OF CLASS.
 *
 *@author Vision for New York
 *@author Rob Hranac
 *@version 0.9 alpha, 11/01/01
 *
 */
public class PostgreSqlSchemaElement {

		// Two primary hash tables for XML and GML schemas
		private static Hashtable GEOMETRY_SCHEMA = new Hashtable();
		private static Hashtable TYPE_SCHEMA = new Hashtable();

		// Basic class variables
		private String name = new String();
		private String databaseType = new String(); 
		private String nullable = new String();

		// Basic schema variables
		private String typeSchema = new String();
		private String schemaType = new String();
		private String sampleData = new String();
		private int minOccurs;
		private int maxOccurs = 1;

	 /**
		* Constructor method generates XML schema hash and GML schema hash for appropriate database objects.
		*
    * SHOULD BE ABSTRACTED TO TWO CLASSES.  DIFFICULTY LIES IN INTERNAL GEOMETRIC REPRESENTATION FOR GML
		* OBJECTS.
		* 
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/ 
		public PostgreSqlSchemaElement  () {

				// Set PostgreSQL PostGIS geometry types 
				GEOMETRY_SCHEMA.put("POINT", "gml:PointType");
				GEOMETRY_SCHEMA.put("LINESTRING", "gml:LineStringType");
				GEOMETRY_SCHEMA.put("POLYGON", "gml:PolygonType");
				GEOMETRY_SCHEMA.put("MULTIPOINT", "gml:MultiPointType");
				GEOMETRY_SCHEMA.put("MULTILINESTRING", "gml:MultiLineStringType");
				GEOMETRY_SCHEMA.put("MULTIPOLYGON", "gml:MultiPolygonType");
				GEOMETRY_SCHEMA.put("GEOMETRYCOLLECTION", "gml:GeometryCollectionType");

				// Set PostgreSQL integer types
				TYPE_SCHEMA.put("int2","<xs:simpleType><xs:restriction base='xs:integer'><xs:totalDigits value='5'/><xs:fractionDigits value='0'/><xs:maxInclusive value='32767'/><xs:minInclusive value='-32768'/></xs:restriction></xs:simpleType>");
				TYPE_SCHEMA.put("int4","<xs:simpleType><xs:restriction base='xs:integer'><xs:totalDigits value='10'/><xs:fractionDigits value='0'/><xs:maxInclusive value='214748364'/><xs:minInclusive value='-214748364'/></xs:restriction></xs:simpleType>");
				TYPE_SCHEMA.put("int8","<xs:simpleType><xs:restriction base='xs:integer'><xs:totalDigits value='19'/><xs:fractionDigits value='0'/></xs:restriction></xs:simpleType>");

				// Set PostgreSQL real types
				TYPE_SCHEMA.put("float8","<xs:simpleType><xs:restriction base='xs:decimal'><xs:fractionDigits value='6'/></xs:restriction></xs:simpleType>");
				TYPE_SCHEMA.put("float4","<xs:simpleType><xs:restriction base='xs:decimal'><xs:fractionDigits value='15'/></xs:restriction></xs:simpleType>");
				TYPE_SCHEMA.put("numeric","<xs:simpleType><xs:restriction base='xs:decimal'/></xs:simpleType>");

				// Set PostgreSQL character types
				TYPE_SCHEMA.put("bpchar","<xs:simpleType><xs:restriction base='xs:string'/></xs:simpleType>");
				TYPE_SCHEMA.put("varchar","<xs:simpleType><xs:restriction base='xs:string'/></xs:simpleType>");
				TYPE_SCHEMA.put("text","<xs:simpleType><xs:restriction base='xs:string'/></xs:simpleType>");

				// Set PostgreSQL boolean type
				TYPE_SCHEMA.put("boolean","<xs:simpleType><xs:restriction base='xs:boolean'/></xs:simpleType>");
				
				// Set PostgreSQL URI types
				TYPE_SCHEMA.put("inet","<xs:simpleType><xs:restriction base='xs:anyURI'/></xs:simpleType>");
				TYPE_SCHEMA.put("cidr","<xs:simpleType><xs:restriction base='xs:anyURI'/></xs:simpleType>");
				TYPE_SCHEMA.put("macaddr","<xs:simpleType><xs:restriction base='xs:string'/></xs:simpleType>");

				// Set PostgreSQL geometric types; pretty loose binding to schema types
				TYPE_SCHEMA.put("box","<xs:simpleType><xs:restriction base='xs:string'/></xs:simpleType>");
				TYPE_SCHEMA.put("circle","<xs:simpleType><xs:restriction base='xs:string'/></xs:simpleType>");
				TYPE_SCHEMA.put("line","<xs:simpleType><xs:restriction base='xs:string'/></xs:simpleType>");
				TYPE_SCHEMA.put("lseg","<xs:simpleType><xs:restriction base='xs:string'/></xs:simpleType>");
				TYPE_SCHEMA.put("point","<xs:simpleType><xs:restriction base='xs:string'/></xs:simpleType>");
				TYPE_SCHEMA.put("polygon","<xs:simpleType><xs:restriction base='xs:string'/></xs:simpleType>");

				// Several data names are claimed by PostgreSQL, but few are returned
				// Alternate representations of int4
				TYPE_SCHEMA.put("serial","<xs:simpleType><xs:restriction base='xs:integer'><xs:totalDigits value='10'/><xs:fractionDigits value='0'/><xs:maxInclusive value='214748364'/><xs:minInclusive value='-214748364'/></xs:restriction></xs:simpleType>");
				TYPE_SCHEMA.put("integer","<xs:simpleType><xs:restriction base='xs:integer'><xs:totalDigits value='10'/><xs:fractionDigits value='0'/><xs:maxInclusive value='214748364'/><xs:minInclusive value='-214748364'/></xs:restriction></xs:simpleType>");
				// Alternate representations of int8
				TYPE_SCHEMA.put("bigint","<xs:simpleType><xs:restriction base='xs:integer'><xs:totalDigits value='19'/><xs:fractionDigits value='0'/></xs:restriction></xs:simpleType>");
				// Alternate representations of pbchar
				TYPE_SCHEMA.put("char","<xs:simpleType><xs:restriction base='xs:string'/></xs:simpleType>");
				TYPE_SCHEMA.put("character","<xs:simpleType><xs:restriction base='xs:string'/></xs:simpleType>");

		}

	 /**
		* Setter method names the .
		*
		* @param columnName The column/XML tag name.
		* @param tableName The table/XML tag prefix name.
		*/ 
		public void setName ( String tableName, String columnName ) throws Exception {
				this.name = tableName + "." + columnName;
		}

	 /**
		* Setter method names the database type, the sample data from a column, and nullable information.
		*
		* @param databaseType Gives the database-specific type for XML hash.
		* @param sampleData Gives sample data for GML hash inspection.
		* @param nullable Specifies whether type may contain null values, from internal datbase representation.
		*/ 
		public void setType ( String databaseType, String sampleData, int nullable ) throws Exception {
				this.databaseType = databaseType;
				this.sampleData = sampleData;
				if ( nullable == 1 ) {
						this.nullable = "true";
						this.minOccurs = 1;
				}
				else {
						this.nullable = "false";
						this.minOccurs = 0;
				}
		}

	 /**
		* Inspects internal database type information and returns hashed schema information.
		*
		*/ 
		public void setXmlType () throws Exception {				

				StringTokenizer sampleTokenizer = new StringTokenizer( this.sampleData, ";(" );
				String geometryType = new String();
				
				if ( this.databaseType.equals( "geometry" ) ) {
						if( sampleTokenizer.countTokens() > 1 ) {
								geometryType = sampleTokenizer.nextToken();
								geometryType = sampleTokenizer.nextToken();
								if( GEOMETRY_SCHEMA.containsKey( geometryType ) ) {
										this.schemaType = "gml"; 
										this.typeSchema = ((String) GEOMETRY_SCHEMA.get( geometryType ));
								}
						}
				}
				else {
						this.schemaType = "xsd";
					  this.typeSchema = ((String) TYPE_SCHEMA.get( this.databaseType ));
				}
		}

	 /**
		* Inspects internal type information and returns XML representation.
		*
		*/ 
		public String xmlPrint () throws Exception {
				String xmlRepresentation = new String();

				this.setXmlType();
				xmlRepresentation = "\n\t\t\t\t<xs:element name='";
				xmlRepresentation = xmlRepresentation + this.name + "' ";
				if ( this.schemaType.equals("gml") )
						xmlRepresentation = xmlRepresentation + " type='" + this.typeSchema + "'";
				xmlRepresentation = xmlRepresentation + " nillable='" + this.nullable + "'";
				xmlRepresentation = xmlRepresentation + " minOccurs='" + this.minOccurs;
				xmlRepresentation = xmlRepresentation + "' maxOccurs='" + this.maxOccurs + "'>";
				if ( this.schemaType.equals("xsd") )				
						xmlRepresentation = xmlRepresentation + this.typeSchema;
				xmlRepresentation = xmlRepresentation + "</xs:element>";

				return xmlRepresentation;
		}
}

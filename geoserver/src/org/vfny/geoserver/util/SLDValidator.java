/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/*
 * Created on April 20, 2005
 *
 */
package org.vfny.geoserver.util;

import org.apache.xerces.parsers.SAXParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;



public class SLDValidator
{

	public SLDValidator()
	{
		
	}
	
	/**
	 *  validates against the "normal" location of the schema (ie. ".../capabilities/sld/StyleLayerDescriptor.xsd"
	 *  uses the geoserver_home patch 
	 * @param xml
	 * @param servContext servlet context
	 * @return
	 */
	public  List validateSLD(InputStream xml,ServletContext servContext)   
	{
        File schemaFile = new File( GeoserverDataDirectory.getGeoserverDataDirectory(servContext),"/data/capabilities/sld/StyledLayerDescriptor.xsd");
        try {       	
        	return validateSLD(xml,schemaFile.toURL().toString());
        }
        catch (Exception e)
		{
        	ArrayList al = new ArrayList();
        	al.add(new SAXException(e));
        	return al;
		}
	}
	
	/**
	 *  validate a .sld against the schema
	 *  
	 * @param xml  input stream representing the .sld file
	 * @param SchemaUrl location of the schemas. Normally use ".../capabilities/sld/StyleLayerDescriptor.xsd"
	 * @return list of SAXExceptions (0 if the file's okay)
	 */
	public  List validateSLD(InputStream xml, String SchemaUrl)   
	{
//		   String XmlDocumentUrl="C:/Documents and Settings/Owner/Desktop/blackpolygon.sld";
//		   try{
//		   xml = new FileInputStream(XmlDocumentUrl);
//		   }
//		   catch (Exception e)
//		   {
//		   	 e.printStackTrace();
//		   	 
//		   }

		 SAXParser parser = new SAXParser();     
		 try{
			 parser.setFeature("http://xml.org/sax/features/validation",true);
			 parser.setFeature("http://apache.org/xml/features/validation/schema",true);
			 parser.setFeature("http://apache.org/xml/features/validation/schema-full-checking",false);
			 
			 parser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",   SchemaUrl );
			 parser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation","http://www.opengis.net/sld "+SchemaUrl);
			 
			 Validator handler=new Validator();
			 parser.setErrorHandler(handler);  
			 parser.parse( new InputSource(xml)  );
			 
		     return handler.errors;
		 }
		 catch(java.io.IOException ioe)
		 {   
		     ArrayList al = new ArrayList();
		     al.add(new SAXParseException(ioe.getLocalizedMessage(),null) );
		     return al;
		 }
		 catch (SAXException e) 
		 { 
		     ArrayList al = new ArrayList();
		     al.add(new SAXParseException(e.getLocalizedMessage(), null) );
		     return al;
		 }     
	}

// errors in the document will be put in "errors".
// if errors.size() ==0  then there were no errors.
private class Validator extends DefaultHandler 
{   
  public ArrayList errors = new ArrayList();
  
  public void error(SAXParseException exception) throws SAXException	       
  {
  	errors.add(exception);
  }
  
  public void fatalError(SAXParseException exception) throws SAXException 
  {
  	errors.add(exception);	     
  }		    
  public void warning(SAXParseException exception) throws SAXException	       
  {
  	//do nothing
  }	
  
}   

}
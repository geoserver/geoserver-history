/*
 * Created on Jan 21, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.geotools.validation.xml;

import java.io.*;
import java.util.*;
import org.geotools.validation.dto.*;
/**
 * XMLWriter purpose.
 * <p>
 * Static class used to write DTO objects to XML.
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: XMLWriter.java,v 1.2 2004/01/21 21:51:45 dmzwiers Exp $
 */
public class XMLWriter {
	
	/**
	 * Does nothing.
	 */
	private XMLWriter(){}
	
	/**
	 * writeTest purpose.
	 * <p>
	 * Writes the DTO to XML using the current writer.
	 * </p>
	 * @param dto The DTO which contains the output data
	 * @param w non-null writer to write to
	 */
	public static void writePlugIn(PlugInDTO dto, Writer w) throws ValidationException{
		if(w==null || dto==null)
			throw new NullPointerException("Null parameter passed into XMLWriter:writeTest");
		WriterUtils cw = new WriterUtils(w);
		cw.writeln("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		Map m = new HashMap();
		m.put("xmlns","testSuiteSchema");
		m.put("xmlns:gml","http://www.opengis.net/gml");
		m.put("xmlns:ogc","http://www.opengis.net/ogc");
		m.put("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
		m.put("xsi:schemaLocation","testSuiteSchema /data/capabilities/validate/testSuiteSchema.xsd");
		cw.openTag("plugin",m);
		cw.textTag("name",dto.getName());
		if(dto.getDescription()!=null && dto.getDescription()!="")
			cw.textTag("description",dto.getDescription());
		cw.textTag("class",dto.getClassName());
		if(dto.getArgs()!=null){
			Iterator i = dto.getArgs().keySet().iterator();
			while(i.hasNext()){
				String name = (String)i.next();
				Object value = dto.getArgs().get(name);
				cw.openTag("argument");
				cw.textTag("name",name);
				cw.writeln(ArgHelper.getArgumentEncoding(value));
				cw.closeTag("argument");
			}
		}
		cw.closeTag("plugin");
	}
	
	/**
	 * writeTest purpose.
	 * <p>
	 * Writes the DTO to XML using the current writer.
	 * </p>
	 * @param dto The DTO which contains the output data
	 * @param w non-null writer to write to
	 */
	public static void writeTest(TestDTO dto, Writer w) throws ValidationException{
		if(w==null || dto==null)
			throw new NullPointerException("Null parameter passed into XMLWriter:writeTest");
		WriterUtils cw = new WriterUtils(w);
		cw.openTag("test");
		cw.textTag("name",dto.getName());
		if(dto.getDescription()!=null && dto.getDescription()!="")
			cw.textTag("description",dto.getDescription());
		cw.textTag("plugin",dto.getPlugIn().getName());
		if(dto.getArgs()!=null){
			Iterator i = dto.getArgs().keySet().iterator();
			while(i.hasNext()){
				String name = (String)i.next();
				Object value = dto.getArgs().get(name);
				cw.openTag("argument");
				cw.textTag("name",name);
				cw.writeln(ArgHelper.getArgumentEncoding(value));
				cw.closeTag("argument");
			}
		}
		cw.closeTag("test");
	}
	
	/**
	 * writeTestSuite purpose.
	 * <p>
	 * Writes the DTO to XML using the current writer.
	 * </p>
	 * @param dto The DTO which contains the output data
	 * @param w non-null writer to write to
	 */
	public static void writeTestSuite(TestSuiteDTO dto, Writer w) throws ValidationException{
		if(w==null || dto==null)
			throw new NullPointerException("Null parameter passed into XMLWriter:writeTestSuite");
		WriterUtils cw = new WriterUtils(w);
		cw.writeln("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		Map m = new HashMap();
		m.put("xmlns","testSuiteSchema");
		m.put("xmlns:gml","http://www.opengis.net/gml");
		m.put("xmlns:ogc","http://www.opengis.net/ogc");
		m.put("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
		m.put("xsi:schemaLocation","testSuiteSchema /data/capabilities/validate/testSuiteSchema.xsd");
		cw.openTag("suite",m);
		cw.textTag("name",dto.getName());
		if(dto.getDescription()!=null && dto.getDescription()!="")
			cw.textTag("description",dto.getDescription());
		Iterator i = dto.getTests().iterator();
		while(i.hasNext())
			writeTest((TestDTO)i.next(),w);
		cw.closeTag("suite");
		
	}
}

/*
 * Created on Jan 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.geotools.validation.xml;

import java.io.*;
import java.util.*;
import org.geotools.validation.dto.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
/**
 * XMLReader purpose.
 * <p>
 * Description of XMLReader ...
 * </p>
 * 
 * <p>
 * Capabilities:
 * </p>
 * <ul>
 * <li>
 * Feature: description
 * </li>
 * </ul>
 * <p>
 * Example Use:
 * </p>
 * <pre><code>
 * XMLReader x = new XMLReader(...);
 * </code></pre>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: XMLReader.java,v 1.3 2004/01/20 19:50:22 dmzwiers Exp $
 */
public class XMLReader {
	
	/**
	 * XMLReader constructor.
	 * <p>
	 * Should never be used.
	 * </p>
	 *
	 */
	private XMLReader(){}
	
	/**
	 * readPlugIn purpose.
	 * <p>
	 * This method is intended to read an XML PlugIn (pluginSchema.xsd) into a PlugInDTO object.
	 * </p>
	 * @param inputSource A reader which contains a copy of a valid PlugIn desciption.
	 * @return the resulting dto based on the input provided.
	 */
	public static PlugInDTO readPlugIn(Reader inputSource) throws ValidationException{
		PlugInDTO dto = new PlugInDTO();
		try{
			Element elem = null;
			try{
				elem= ReaderUtils.loadConfig(inputSource);
			}catch(ParserConfigurationException pce){
				throw new ValidationException("Cannot parse the inputSource: Cannot configure the parser.",pce);
			}catch(SAXException se){
				throw new ValidationException("Cannot parse the inputSource: Cannot configure the parser.",se);
			}
			try{
				dto.setName(ReaderUtils.getElementText(ReaderUtils.getChildElement(elem,"name",true),true));
			}catch(SAXException e){throw new ValidationException("Error parsing name for this plugin",e);}
			try{
				dto.setDescription(ReaderUtils.getElementText(ReaderUtils.getChildElement(elem,"description",true),true));
			}catch(SAXException e){throw new ValidationException("Error parsing description for the "+dto.getName()+" plugin",e);}
			try{
				dto.setClassName(ReaderUtils.getElementText(ReaderUtils.getChildElement(elem,"class",true),true));
			}catch(SAXException e){throw new ValidationException("Error parsing class for the "+dto.getName()+" plugin",e);}
			NodeList nl = elem.getElementsByTagName("argument");
			if(nl!=null){
				Map m = new HashMap();
				dto.setArgs(m);
				for(int i=0;i<nl.getLength();i++){
					elem = (Element)nl.item(i);
					String key = "";
					try{
						key = ReaderUtils.getChildText(elem,"name",true);
					}catch(SAXException e){
						throw new ValidationException("Error reading argument for "+dto.getName() + " :name required");
					}
					NodeList nl2 = elem.getElementsByTagName("*");
					if(nl2.getLength()!=2){
						throw new ValidationException("Invalid Argument \""+dto.getName()+"\" for argument \""+key+"\"");
					}
					if(((Element)nl2.item(0)).getTagName().equals("name")){
						elem = (Element)nl2.item(1);
					}else{
						elem = (Element)nl2.item(0);
					}
					// elem whould have the value now
					Object val = ArgHelper.getArgumentInstance(elem.getTagName().trim(),elem);
					m.put(key,val);
				}
			}
		}catch(IOException ioe){
			throw new ValidationException("Cannot parse the inputSource: Cannot configure the parser.",ioe);
		}
		return dto;
	}
	
	/**
	 * readTestDTO purpose.
	 * <p>
	 * This method is intended to read an XML Test (testSuiteSchema.xsd) into a TestDTO object.
	 * </p>
	 * @param inputSource A reader which contains a copy of a valid Test desciption.
	 * @param plugIns A name of plugin names to valid plugin DTOs
	 * @return the resulting dto based on the input provided.
	 */
	public static TestDTO readTestDTO(Reader inputSource, Map plugIns) throws ValidationException{
		TestDTO dto = null;
		try{

			Element elem = null;
			try{
				elem = ReaderUtils.loadConfig(inputSource);
			}catch(ParserConfigurationException e){
				throw new ValidationException("An error occured loading the XML parser.",e);
			}catch(SAXException e){
				throw new ValidationException("An error occured loading the XML parser.",e);
			}
			dto = loadTestDTO(elem,plugIns);
		}catch(IOException e){
			throw new ValidationException("Error reading a test",e);
		}
		return dto;
	}
	
	/**
	 * readTestSuiteDTO purpose.
	 * <p>
	 * This method is intended to read an XML Test (testSuiteSchema.xsd) into a TestSuiteDTO object.
	 * </p>
	 * @param inputSource A reader which contains a copy of a valid TestSuite desciption.
	 * @param plugIns A name of plugin names to valid plugin DTOs
	 * @return the resulting dto based on the input provided.
	 */
	public static TestSuiteDTO readTestSuiteDTO(Reader inputSource, Map plugIns) throws ValidationException{
		TestSuiteDTO dto = new TestSuiteDTO();
		try{
			Element elem = null;
			try{
				elem = ReaderUtils.loadConfig(inputSource);
			}catch(ParserConfigurationException e){
				throw new ValidationException("An error occured loading the XML parser.",e);
			}catch(SAXException e){
				throw new ValidationException("An error occured loading the XML parser.",e);
			}
			try{
				dto.setName(ReaderUtils.getChildText(elem,"name",true));
			}catch(SAXException e){throw new ValidationException("Error loading test suite name",e);}
			try{
				dto.setDescription(ReaderUtils.getChildText(elem,"description",true));
			}catch(SAXException e){throw new ValidationException("Error loading test suite description",e);}
			List l = new LinkedList();
			dto.setTests(l);
			NodeList nl = elem.getElementsByTagName("test");
			if(nl==null || nl.getLength()==0){
			  throw new ValidationException("The test suite loader has detected an error: no tests provided.");
			}else{
				for(int i=0;i<nl.getLength();i++){
					try{
						l.add(loadTestDTO((Element)nl.item(i),plugIns));
					}catch(ValidationException e){
						throw new ValidationException("An error occured loading a test in "+dto.getName()+" test suite.",e);
					}
				}
			}
		}catch(IOException e){
			throw new ValidationException("An error occured loading the "+ dto.getName() +"test suite",e);
		}
		return dto;
	}

	/**
	 * loadTestDTO purpose.
	 * <p>
	 * Helper method used by readTestDTO and readTestSuiteDTO
	 * </p>
	 * @param elem The head element of a test
	 * @return a TestDTO representing elem, null if elem is not corretly defined.
	 */
	private static TestDTO loadTestDTO(Element elem, Map plugIns) throws ValidationException{
		TestDTO dto = new TestDTO();
		try{
			dto.setName(ReaderUtils.getChildText(elem,"name",true));
		}catch(SAXException e){throw new ValidationException("Error reading the name for this test case.",e);}
		try{
			dto.setDescription(ReaderUtils.getChildText(elem,"description",false));
		}catch(SAXException e){throw new ValidationException("Error reading the description for the "+dto.getName()+" test case.",e);}
		try{
			String pluginName = ReaderUtils.getChildText(elem,"plugin",true);
			dto.setPlugIn((PlugInDTO)plugIns.get(pluginName));
		}catch(SAXException e){throw new ValidationException("Error reading the plugin for the "+dto.getName()+" test case.",e);}
		NodeList nl = elem.getElementsByTagName("argument");
		if(nl!=null){
			Map m = new HashMap();
			dto.setArgs(m);
			for(int i=0;i<nl.getLength();i++){
				elem = (Element)nl.item(i);
				String key = "";
				try{
					key = ReaderUtils.getChildText(elem,"name",true);
				}catch(SAXException e){
					throw new ValidationException("Error reading argument for "+dto.getName() + " :name required");
				}
				NodeList nl2 = elem.getChildNodes();
				Element name = null;
				Element value = null;
				for(int j=0;j<nl2.getLength();j++){
					if(nl2.item(j).getNodeType()==Node.ELEMENT_NODE){
						elem = (Element)nl2.item(j);
						if(elem.getTagName().trim().equals("name")){
							name = elem;
						}else{
							value = elem;
						}
					}
				}
				if(name==null || value ==null){
					throw new ValidationException("Invalid Argument \""+dto.getName()+"\" for argument \""+key+"\"");
				}
				// elem whould have the value now
				Object val = ArgHelper.getArgumentInstance(elem.getTagName().trim(),elem);
				m.put(key,val);
			}
		}
		return dto;
	}
}

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
 * @version $Id: XMLReader.java,v 1.1 2004/01/19 23:54:56 dmzwiers Exp $
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
			Element elem = ReaderUtils.loadConfig(inputSource);
			dto.setName(ReaderUtils.getElementText(ReaderUtils.getChildElement(elem,"name",true),true));
			dto.setDescription(ReaderUtils.getElementText(ReaderUtils.getChildElement(elem,"description",true),true));
			dto.setClassName(ReaderUtils.getElementText(ReaderUtils.getChildElement(elem,"class",true),true));
			Map m = new HashMap();
			dto.setArgs(m);
			NodeList nl = elem.getElementsByTagName("argument");
			if(nl!=null){
				for(int i=0;i<nl.getLength();i++){
					elem = (Element)nl.item(i);
					String key = ReaderUtils.getChildText(elem,"name",true);
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
					Object val = ArgHelper.getArgumentInstance(elem.getTagName(),elem);
					m.put(key,val);
				}
			}
		}catch(ParserConfigurationException pce){
			throw new ValidationException("Cannot parse the inputSource: Cannot configure the parser.",pce);
		}catch(SAXException se){
			throw new ValidationException("Cannot parse the inputSource: Cannot configure the parser.",se);
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
	 * @return the resulting dto based on the input provided.
	 */
	public static TestDTO readTestDTO(Reader inputSource){
		TestDTO dto = new TestDTO();
		// TODO fill this method body
		return dto;
	}
	
	/**
	 * readTestSuiteDTO purpose.
	 * <p>
	 * This method is intended to read an XML Test (testSuiteSchema.xsd) into a TestSuiteDTO object.
	 * </p>
	 * @param inputSource A reader which contains a copy of a valid TestSuite desciption.
	 * @return the resulting dto based on the input provided.
	 */
	public static TestSuiteDTO readTestSuiteDTO(Reader inputSource){
		TestSuiteDTO dto = new TestSuiteDTO();
		// TODO fill this method body
		return dto;
	}
}

/*
 * Created on Jan 20, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.geotools.validation.xml;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import javax.servlet.*;
import java.io.*;
import java.util.*;
import org.geotools.validation.*;
import org.geotools.validation.dto.*;
/**
 * ValidationPlugIn purpose.
 * <p>
 * Description of ValidationPlugIn ...
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
 * ValidationPlugIn x = new ValidationPlugIn(...);
 * </code></pre>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: ValidationPlugIn.java,v 1.2 2004/01/20 23:51:29 dmzwiers Exp $
 */
public class ValidationPlugIn implements PlugIn {

	/** The list of known plug-ins*/
	private Map plugIns;
	
	/** the list of known testSuites */
	private Map testSuites;
	
	/** the current processor */
	private ValidationProcessor processor;
	
	/** true after load */
	private boolean init;
	
	public static final String WEB_CONTAINER_KEY = "Validation.PlugIn.Key";
	
	/**
	 * Implementation of destroy.
	 * 
	 * @see org.apache.struts.action.PlugIn#destroy()
	 * 
	 * 
	 */
	public void destroy() {}

	/**
	 * Implementation of init.
	 * 
	 * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet, org.apache.struts.config.ModuleConfig)
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws javax.servlet.ServletException
	 */
	public void init(ActionServlet arg0, ModuleConfig arg1)
		throws ServletException {
		ServletContext sc = arg0.getServletContext();
		File rootDir = new File(sc.getRealPath("/"));
		File plugInDir = new File(rootDir,"data/plugIns");
		File validationDir = new File(rootDir,"data/validation");
		try{
			plugIns = loadPlugIns(plugInDir);
			testSuites = loadValidations(validationDir,plugIns);
			init = resetProcessor();
		}catch(ValidationException e){
			// LOG error
			init = false;
		}
		sc.setAttribute(WEB_CONTAINER_KEY,this);
	}

	/**
	 * Access plugIns property.
	 * 
	 * @return Returns the plugIns.
	 */
	public Map getPlugInDTOs() {
		if(init)
		return plugIns;
		return null;
	}

	/**
	 * Access testSuites property.
	 * 
	 * @return Returns the testSuites.
	 */
	public Map getTestSuiteDTOs() {
		if(init)
		return testSuites;
		return null;
	}
	
	/**
	 * Access processor property.
	 * 
	 * @return
	 */
	public ValidationProcessor getValidationProcessor(){
		if(init)
			return processor;
		return null;
	}
	
	/**
	 * resetProcessor purpose.
	 * <p>
	 * Will reset the processor based on the current DTO's being held in memory.
	 * </p>
	 * @return true when completed.
	 */
	public boolean resetProcessor() throws ValidationException{
		ValidationProcessor vp = new ValidationProcessor();
		// TODO this method body
		
		// step 1 make a list required plug-ins
		Set plugInNames = new HashSet();
		Iterator i = testSuites.keySet().iterator();
		while(i.hasNext()){
			TestSuiteDTO dto = (TestSuiteDTO)testSuites.get(i.next());
			Iterator j = dto.getTests().iterator();
			while(j.hasNext()){
				TestDTO tdto = (TestDTO)j.next();
				plugInNames.add(tdto.getPlugIn().getName());
			}
		}
		
		// step 2 configure plug-ins with defaults
		Map defaultPlugIns = new HashMap(plugInNames.size());
		i = plugInNames.iterator();
		while(i.hasNext()){
			String plugInName = (String)i.next();
			PlugInDTO dto = (PlugInDTO)plugIns.get(plugInName);
			Class plugInClass = null;
			try{
				plugInClass = Class.forName(dto.getClassName());
			}catch(ClassNotFoundException e){
				//Error, using default.
			}
			if(plugInClass == null)
				plugInClass = Validation.class;
			Map plugInArgs = dto.getArgs();
			if(plugInArgs == null)
				plugInArgs = new HashMap();
			org.geotools.validation.xml.PlugIn plugIn = new org.geotools.validation.xml.PlugIn(plugInName,plugInClass,dto.getDescription(),plugInArgs);
			defaultPlugIns.put(plugInName,plugIn);
		}
		
		// step 3 configure plug-ins with tests + add to processor
		i = testSuites.keySet().iterator();
		while(i.hasNext()){
			TestSuiteDTO tdto = (TestSuiteDTO)testSuites.get(i.next());
			Iterator j = tdto.getTests().iterator();
			while(j.hasNext()){
				TestDTO dto = (TestDTO)j.next();
				// deal with test
				Map testArgs = dto.getArgs();
				if(testArgs == null)
					testArgs = new HashMap();
				org.geotools.validation.xml.PlugIn plugIn = (org.geotools.validation.xml.PlugIn)defaultPlugIns.get(dto.getPlugIn().getName());
				Validation validation = plugIn.createValidation(dto.getName(), dto.getDescription(), testArgs );
				
				if( validation instanceof FeatureValidation ){
					processor.addValidation( (FeatureValidation) validation );
				}
				if( validation instanceof IntegrityValidation){
					processor.addValidation( (IntegrityValidation) validation );
				}
			}
		}
		
		processor = vp;
		return true;
	}
	
	/**
	 * loadPlugIns purpose.
	 * <p>
	 * Loads all the plugins in the directory
	 * </p>
	 * @param plugInDir
	 * @return
	 */
	private Map loadPlugIns(File plugInDir) throws ValidationException{
		Map r = null;
		try{
			plugInDir = ReaderUtils.initFile(plugInDir,true);
			File [] fileList = plugInDir.listFiles();
			r = new HashMap();
			for(int i=0;i<fileList.length;i++){
				if(fileList[i].canWrite() && fileList[i].isFile()){
					FileReader fr = new FileReader(fileList[i]);
					PlugInDTO dto = XMLReader.readPlugIn(fr);
					r.put(dto.getName(),dto);
				}
			}
		}catch(IOException e){
			throw new ValidationException("An io error occured while loading the plugin's",e);
		}
		return r;
	}

	/**
	 * loadValidations purpose.
	 * <p>
	 * Loads all the validations in the directory
	 * </p>
	 * @param validationDir
	 * @param plugInDTOs Already loaded list of plug-ins to link.
	 * @return
	 */
	private Map loadValidations(File validationDir, Map plugInDTOs) throws ValidationException{
		Map r = null;
		try{
			validationDir = ReaderUtils.initFile(validationDir,true);
			File [] fileList = validationDir.listFiles();
			r = new HashMap();
			for(int i=0;i<fileList.length;i++){
				if(fileList[i].canWrite() && fileList[i].isFile()){
					FileReader fr = new FileReader(fileList[i]);
					TestSuiteDTO dto = XMLReader.readTestSuiteDTO(fr,plugInDTOs);
					r.put(dto.getName(),dto);
				}
			}
		}catch(IOException e){
			throw new ValidationException("An io error occured while loading the plugin's",e);
		}
		return r;
		
	}
	
	/**
	 * load purpose.
	 * <p>
	 * replaces the current configuration of DTO's but does not affect the processor.
	 * </p>
	 * @param plugIns List of the plugInDTO objects
	 * @param testSuites List of the TestSuiteDTO objects
	 * @return boolean true if stored.
	 */
	public boolean load(Map plugIns, Map testSuites){
		return loadPlugIns(plugIns) && loadTestSuites(testSuites);
	}
	
	/**
	 * load purpose.
	 * <p>
	 * 
	 * replaces the current configuration of DTO's but does not affect the processor.
	 * </p>
	 * @param dtoList The list of DTO's o use.
	 * @param isPlugIns true if the list is of plugins, false for testSuites.
	 * @return true if stored.
	 */
	public boolean load(Map dtoList, boolean isPlugIns){
		if(isPlugIns)
			return loadPlugIns(dtoList);
		else
			return loadTestSuites(dtoList);
		
	}
	
	/**
	 * loadPlugIns purpose.
	 * <p>
	 * replaces the current configuration of plugInDTO's but does not affect the processor.
	 * </p>
	 * @param plugIns List of the plugInDTO objects
	 * @return true if stored.
	 */
	private boolean loadPlugIns(Map plugIns){
		if(plugIns ==null)
			return false;
		this.plugIns = plugIns;
		return true;
	}
	
	/**
	 * loadTestSuites purpose.
	 * <p>
	 * replaces the current configuration of testSuiteDTO's but does not affect the processor.
	 * </p>
	 * @param testSuites List of the testSuiteDTO objects
	 * @return true if stored.
	 */
	private boolean loadTestSuites(Map testSuites){
		if(testSuites ==null)
			return false;
		this.testSuites = testSuites;
		return true;
	}
}

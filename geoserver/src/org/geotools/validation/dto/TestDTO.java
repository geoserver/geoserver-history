/*
 * Created on Jan 14, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.geotools.validation.dto;


import java.util.*;
/**
 * TestDTO purpose.
 * <p>
 * Description of TestDTO ...
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
 * TestDTO x = new TestDTO(...);
 * </code></pre>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: TestDTO.java,v 1.1 2004/01/14 22:54:26 dmzwiers Exp $
 */
public class TestDTO {
	
	/** the test name */
	private String name;
	
	/** the test description */
	private String description;
	
	/** The plug-in which contains the class definition and default runtime values */
	private PlugInDTO plugIn;
	
	/** The set of runtime args for this particular test to override the defaults in the plug-in */
	private Map args;
	
	/**
	 * TestDTO constructor.
	 * <p>
	 * Does nothing
	 * </p>
	 */
	public TestDTO(){}
	
	/**
	 * TestDTO constructor.
	 * <p>
	 * Creates a copy from the TestDTO specified.
	 * </p>
	 * @param t the data to copy
	 */
	public TestDTO(TestDTO t){
		name = t.getName();
		description = t.getDescription();
		plugIn = new PlugInDTO(t.getPlugIn());
		args= new HashMap();
		if(plugIn!=null){
			Iterator i = t.getArgs().keySet().iterator();
			while(i.hasNext()){
				String key = (String)i.next();
				//TODO clone value.
				args.put(key,t.getArgs().get(key));
			}
		}
	}
	
	/**
	 * Implementation of clone.
	 * 
	 * @see java.lang.Object#clone()
	 * 
	 * @return A copy of this TestDTO
	 */
	public Object clone(){
		return new TestDTO(this);
	}
	
	/**
	 * Implementation of equals.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj
	 * @return true when they have the same data.
	 */
	public boolean equals(Object obj){
		if(obj == null || !(obj instanceof TestDTO))
			return false;
		TestDTO t = (TestDTO)obj;
		boolean r = true;
		
		r = r && (name == t.getName());
		r = r && (description == t.getDescription());
		
		if(plugIn == null){
			if(t.getPlugIn() != null){
				return false;
			}
		}else{
			if(t.getPlugIn() != null){
				r = r && plugIn.equals(t.getPlugIn());
			}else{
				return false;
			}
			
		}
		
		if(args == null){
			if(t.getArgs() != null){
				return false;
			}
		}else{
			if(t.getArgs() != null){
				r = r && args.equals(t.getArgs());
			}else{
				return false;
			}
			
		}
		
		return r;
	}
	
	/**
	 * Implementation of hashCode.
	 * 
	 * @see java.lang.Object#hashCode()
	 * 
	 * @return int hashcode
	 */
	public int hashCode(){
		int r = 1;
		if(name != null)
			r *= name.hashCode();
		if(description != null)
			r *= description.hashCode();
		if(plugIn!=null)
			r *= plugIn.hashCode();
		if(args!=null)
			r *= args.hashCode();
		return r;
	}
	/**
	 * Access args property.
	 * 
	 * @return Returns the args.
	 */
	public Map getArgs() {
		return args;
	}

	/**
	 * Set args to args.
	 *
	 * @param args The args to set.
	 */
	public void setArgs(Map args) {
		this.args = args;
	}

	/**
	 * Access description property.
	 * 
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set description to description.
	 *
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Access name property.
	 * 
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name to name.
	 *
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Access plugIn property.
	 * 
	 * @return Returns the plugIn.
	 */
	public PlugInDTO getPlugIn() {
		return plugIn;
	}

	/**
	 * Set plugIn to plugIn.
	 *
	 * @param plugIn The plugIn to set.
	 */
	public void setPlugIn(PlugInDTO plugIn) {
		this.plugIn = plugIn;
	}

}

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.geotools.referencing.crs.EPSGCRSAuthorityFactory;
import org.geotools.referencing.factory.epsg.FactoryFromWKT;
import org.vfny.geoserver.form.DemoForm;



public class SrsHelpAction extends Action {
	
	/**
	 *  This is a simple action - it reads in the GT2 epsg.properties file and sticks its contents in
	 *  the Form.
	 * 
	 *  We do a little monkey business to make sure that we are ordering things by id (its not sorted for 3 reasons -
	 *  (a) the properties is a hashtable (b) the file isnt in the correct order anyways (c) strings are hard to sort correct)
	 * 
	 * 
	 * TODO: once geosever support EPSG thats not in the properties file, this should
	 *       be a bit more abstract and get a list of all EPSG defs from the 
	 *       Factory (if possible).  Use toWKT() as its nicer to read.
	 * 
	 *   Form has two properies - ids  (list of String - the epsg #)
	 *                            defs (list of String - the epsg WKT definitions)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException 
		{
    	       ArrayList ids = new ArrayList();
    	       ArrayList defs = new ArrayList();
    	       
    	       Properties props = new Properties();
    	       try
    	       {    	          
    	          try{
    	          	props.load(FactoryFromWKT.class.getResourceAsStream( "epsg.properties"  ));
    	          }
    	          catch(Exception e) //saw this being loaded from two places in the GT2 code, lets try both
				  {
    	          	props=new Properties();
    	          	props.load(EPSGCRSAuthorityFactory.class.getResourceAsStream("epsg.properties"));
				  }
    	          
    	            // sort by integer!
    	          	  ArrayList intIds = new ArrayList(5000); // we dont know how many are in the file
                          // didier richard (2005-04-12) :
                          // as of release 1.5, 'enum' is a keyword, and may not be used as an identifier
                          // enum -> enumId
	    	          Enumeration enumId = props.propertyNames();
	    	          while(enumId.hasMoreElements())
	    	          {
	    	          	    String id = (String) enumId.nextElement();
	    	          	    intIds.add(new Integer(id)); 
	    	          }
    	              Integer[] intIdArray  = (Integer[]) intIds.toArray(new Integer[ intIds.size() ] );
    	              Arrays.sort(intIdArray);
    	              
    	          
    	          Pattern comma = Pattern.compile(",");   //see below - for replace "," with ", "
    	          
    	          for(int t=0;t<intIdArray.length; t++)
    	          {
    	          	    String id = (String) intIdArray[t].toString();
    	          	    String def = (String) props.getProperty(id);
    	          	    def = comma.matcher(def).replaceAll(", "); // replace "," with ", " for better html output
    	          	    ids.add(id);
    	          	    defs.add(def);
    	          }
    	       }
    	       catch( Exception e )
    	       {
    	         e.printStackTrace();
    	       }
    	       
    
    	
    	DynaActionForm  myForm = (DynaActionForm ) form;

        myForm.set("srsDefinitionList",defs.toArray(new String[1]) );
        myForm.set("srsIDList",ids.toArray(new String[1]) );

        // return back to the admin demo
        //
        return mapping.findForward("success");
    }
}

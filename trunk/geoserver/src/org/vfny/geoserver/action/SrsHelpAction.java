/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.geotools.referencing.CRS;
import org.geotools.referencing.FactoryFinder;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;



public class SrsHelpAction extends Action {
	
	/**
	 *  This is a simple action - it reads in the GT2 supported EPSG codes. 
	 * 
	 * DONE: once geosever support EPSG thats not in the properties file, this should
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
    	       ArrayList defs = new ArrayList();
    	       ArrayList ids_string = new ArrayList();
    	       
    	       Set codes = CRS.getSupportedCodes("EPSG");
    	       
    	         // make an array of each code (as an int)
    	       int[] ids = new int[codes.size()];
    	       int t=0;
    	       Iterator codeIt = codes.iterator();
    	       while (codeIt.hasNext())
    	       {
    	       		String code = (String) codeIt.next();
    	       		String id  = code.substring(code.indexOf(':')+1); //just the number
    	       		ids[t] = Integer.parseInt(id);
    	       		t++;
    	       }
    	       Arrays.sort(ids); //sort to get them in order
    	       
    	       for (t=0;t<ids.length;t++) //for each id (in sorted order)
    	       {
    	       	   try{  //get its definition
    	       	   		//CoordinateReferenceSystem crs = CRS.decode("EPSG:"+ids[t]);
        			CRSAuthorityFactory crsFactory = FactoryFinder.getCRSAuthorityFactory("EPSG", null);
        			CoordinateReferenceSystem crs=(CoordinateReferenceSystem) crsFactory.createCoordinateReferenceSystem("EPSG:"+ids[t]);

    	       	   		String def = crs.toWKT();
    	       	   		defs.add(def);
    	       	   		ids_string.add(""+ids[t]);
    	       	   }
    	       	   catch(Exception e)
				   {
    	       	   	   System.out.println("tried to parse projection "+"EPSG:"+ids[t]+" but couldnt!");
    	       	   	   //e.printStackTrace(); // dont really expect to get this, so we ignore that one code
				   }
    	       	   
    	       }
    	         	//send off to the .jsp
    	DynaActionForm  myForm = (DynaActionForm ) form;

        myForm.set("srsDefinitionList",defs.toArray(new String[1]) );
        myForm.set("srsIDList",ids_string.toArray(new String[1]) );

        // return back to the admin demo
        //
        return mapping.findForward("success");
    }
}

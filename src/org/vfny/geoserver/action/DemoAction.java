/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.form.DemoForm;
import org.vfny.geoserver.global.UserContainer;


/**
 * This Action handles all the buttons for the Demo.jsp page.
 * <p>
 * This one is more complicated then usual since not all the actions require
 * the form bean to be validated! I am going to have to hack a little bit
 * to make that happen, I may end up making the form bean validation differ
 * depending on the selected action.
 * </p>
 * <p>
 * Buttons that make this action go:
 * <ul>
 * <li>Submit: submit the request specified by url and post fields
 *     (Should be done using Javascript locally)
 *     </li>
 * <li>Change: select between the precanned demos
 *     </li>
 * </ul>
 * As usual we will have to uninternationlize the action name provided to us.
 * </p>
 * @author Richard Gould
 * @author Jody Garnett
 */
public class DemoAction extends GeoServerAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        
        DemoForm demoForm = (DemoForm) form;
        
        File dir = demoForm.getDir();
        String demo = demoForm.getDemo();
        System.out.println("Demo Exists:"+demo);
        if( demo.equals("")){
            demoForm.setUrl( org.vfny.geoserver.requests.Requests.getBaseUrl(request)+"wfs" );
            demoForm.setBody( "" );
        }
        String url = org.vfny.geoserver.requests.Requests.getBaseUrl(request)+"wfs";
        System.out.println("Demo update url:"+url);
        
        File file = new File( dir, demo );
        System.out.println("Demo reading:"+file );
        BufferedReader reader = new BufferedReader( new FileReader(file));
        StringBuffer buf = new StringBuffer();
        for( String line = reader.readLine(); line != null; line = reader.readLine() ){
            buf.append( line );
            System.out.println( "demo:"+line );
            buf.append("\n");
        }
        demoForm.setUrl( url );
        demoForm.setBody( buf.toString() );
        
        // return back to the admin demo
        //
        return mapping.findForward("admin.demo");     
    }
}
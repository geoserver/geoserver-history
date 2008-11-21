/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.geoserver.ows.util.RequestUtils;
import org.vfny.geoserver.form.DemoRequestForm;
import org.vfny.geoserver.util.Requests;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <b>DemoRequestAction</b><br>
 * Oct 7, 2005<br>
 *
 * <b>Purpose:</b><br>
 * This used to be DemoAction. We changed DemoAction to be a bunch of links, and leave
 * it as an action in case we want to do something fancier with it later.
 *
 * This Action handles all the buttons for the Demo.jsp page.
 *
 * <p>
 * This one is more complicated then usual since not all the actions require
 * the form bean to be validated! I am going to have to hack a little bit to
 * make that happen, I may end up making the form bean validation differ
 * depending on the selected action.
 * </p>
 *
 * <p>
 * Buttons that make this action go:
 *
 * <ul>
 * <li>
 * Submit: submit the request specified by url and post fields (Should be done
 * using Javascript locally)
 * </li>
 * <li>
 * Change: select between the precanned demos
 * </li>
 * </ul>
 *
 * As usual we will have to uninternationlize the action name provided to us.
 * </p>
 *
 * @author Richard Gould
 * @author Jody Garnett
 * @author Brent Owens (The Open Planning Project)
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 *
 * @version
 */
public class DemoRequestAction extends GeoServerAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        DemoRequestForm demoForm = (DemoRequestForm) form;

        File dir = demoForm.getDir();
        String demo = demoForm.getDemo();
        //String baseUrl = Requests.getBaseUrl(request, getGeoServer());

        String baseUrl = RequestUtils.baseURL(request);
        
        if (demo == null) {
            demo = "";
        }

        if (demo.equals("")) {
            demoForm.setUrl(baseUrl);
            demoForm.setBody("");
        }
        
        if(demo.indexOf('/') != -1 || demo.indexOf('\\') != -1)
            throw new IllegalArgumentException("Invalid path " + demo);

        String url = baseUrl + "ows";

        File file = new File(dir, demo);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        StringBuffer buf = new StringBuffer();

        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            buf.append(line);
            buf.append("\n");
        }

        if (demo.endsWith(".url")) {
            demoForm.setUrl(baseUrl + buf.toString());
            demoForm.setBody("");
        } else { //demo.endsWith(.xml), but not yet for backwards compatibility.
            demoForm.setUrl(url);
            demoForm.setBody(buf.toString());
        }

        // return back to the admin demo
        //
        return mapping.findForward("welcome.demoRequest");
    }
}

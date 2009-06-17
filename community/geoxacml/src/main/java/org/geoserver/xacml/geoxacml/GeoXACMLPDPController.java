/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.xacml.geoxacml;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.sun.xacml.PDP;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;



/**
 * Controller which acts as GeoXACML Policy Decision Point
 *
 * @author Christian Mueller
 *
 */
public class GeoXACMLPDPController extends AbstractController {

    /**
     * Creates the new PDP.
     *
     */
    public GeoXACMLPDPController() {
        setSupportedMethods(new String[]{METHOD_POST});
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        
        PDP pdp = GeoXACMLConfig.getPDP();
        RequestCtx request = RequestCtx.getInstance(req.getInputStream());        
        ResponseCtx response= pdp.evaluate(request);
        
        response.encode(resp.getOutputStream());                       
        return null;
    }

}

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */


package org.geoserver.xacml.geoxacml;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.sun.xacml.Indenter;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;

/**
 * Some utility methods
 * 
 * @author Mueller Christian
 *
 */
public class XACMLUtil {
    
    static public String asXMLString(RequestCtx ctx) {
        OutputStream out = new ByteArrayOutputStream();
        ctx.encode(out, new Indenter(2));
        return out.toString();        
    }
    
    static public String asXMLString(ResponseCtx ctx) {
        OutputStream out = new ByteArrayOutputStream();
        ctx.encode(out, new Indenter(2));
        return out.toString();        
    }


}

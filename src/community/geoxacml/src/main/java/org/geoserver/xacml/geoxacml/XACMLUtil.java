/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.geoxacml;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.xacml.geoxacml.attr.GML3Support;

import com.sun.xacml.Indenter;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

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

    public static int getDecisionFromRoleResponses(List<ResponseCtx> responses) {
        boolean hasPermit = false;
        for (ResponseCtx responseCtx : responses) {
            int decision = getDecisionFromResponseContext(responseCtx);
            if (decision == Result.DECISION_INDETERMINATE) // Error
                return decision;
            if (decision == Result.DECISION_PERMIT)
                hasPermit = true;
        }
        return hasPermit ? Result.DECISION_PERMIT : Result.DECISION_DENY;

        // int permitCount=0,denyCount=0, notApplicableCount=0;
        // for (ResponseCtx responseCtx: responses) {
        // int decision = getDecisionFromResponseContext(responseCtx);
        // if (decision==Result.DECISION_INDETERMINATE) // Error
        // return decision;
        // if (decision==Result.DECISION_DENY) denyCount++;
        // if (decision==Result.DECISION_PERMIT) permitCount++;
        // if (decision==Result.DECISION_NOT_APPLICABLE) notApplicableCount++;
        // }
        // if (permitCount > 0 && denyCount > 0) {
        // Logger log = getXACMLLogger();
        // log.severe("GeoXACML Error: having "+permitCount+ " permits and "+ denyCount +
        // " denies");
        // return Result.DECISION_INDETERMINATE;
        // }
        // if (permitCount > 0) return Result.DECISION_PERMIT;
        // if (denyCount > 0) return Result.DECISION_DENY;
        // return Result.DECISION_NOT_APPLICABLE;

    }

    public static int getDecisionFromResponseContext(ResponseCtx responseCtx) {
        Set<Result> results = responseCtx.getResults();
        if (results.size() != 1) {
            Logger log = getXACMLLogger();
            log.severe("GeoXACML Error: Response has more than one result");
            log.severe(XACMLUtil.asXMLString(responseCtx));
            return Result.DECISION_INDETERMINATE;
        }
        Result result = results.iterator().next();
        return result.getDecision();
    }

    public static Logger getXACMLLogger() {
        return Logger.getLogger("XACML");
    }
    
    public static  String getGMLTypeFor(Geometry g) {
        String gmlType=null;
        if (g instanceof Point) gmlType=GML3Support.GML_POINT;
        if (g instanceof LineString) gmlType=GML3Support.GML_LINESTRING;
        if (g instanceof Polygon) gmlType=GML3Support.GML_POLYGON;
        if (g instanceof MultiPoint) gmlType=GML3Support.GML_MULTIPOINT;
        if (g instanceof MultiLineString) gmlType=GML3Support.GML_MULTICURVE;
        if (g instanceof MultiPolygon) gmlType=GML3Support.GML_MULTISURFACE;
        
        if (gmlType==null) {
            throw new RuntimeException("No GML type for " + g.getClass().getName());
        }
        return gmlType;
    }


}

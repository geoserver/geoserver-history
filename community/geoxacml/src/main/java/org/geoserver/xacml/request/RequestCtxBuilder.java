/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.geoserver.ows.Dispatcher;
import org.geoserver.security.AccessMode;
import org.geoserver.xacml.geoxacml.XACMLConstants;
import org.geoserver.xacml.role.Role;
import org.geotools.data.shapefile.shp.JTSUtilities;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.xacml.geoxacml.attr.GML3Support;
import org.geotools.xacml.geoxacml.attr.GMLVersion;
import org.geotools.xacml.geoxacml.attr.GeometryAttribute;
import org.vfny.geoserver.Request;

import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.Subject;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Base class for geoxacml request context builders
 * The class inheritance structure is mirrored from {@link Request}
 * 
 * 
 * @author Christian Mueller
 *
 */
public abstract class RequestCtxBuilder extends Object {
    
                
    private Role role;
    private AccessMode mode;
    
    public Role getRole() {
        return role;
    }


    protected RequestCtxBuilder(Role role,AccessMode mode) {
        this.role=role;
        this.mode=mode;
    }
    
    
    protected void addRole(Set<Subject> subjects) {
        
            
        URI roleIdURI = null,roleURI=null;
        try {
            roleIdURI=new URI(XACMLConstants.RoleIdPrefix+role.getId());
            roleURI=new URI(role.getId());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        
        Set<Attribute> subjectAttributes = new HashSet<Attribute>(1+role.getAttributes().size());
        
        AttributeValue roleAttributeValue = new AnyURIAttribute(roleURI);
        Attribute roleAttribute= new Attribute(roleIdURI,null,null,roleAttributeValue);
        subjectAttributes.add(roleAttribute);
        
        for (Entry<String,Object> paramEntry: role.getAttributes().entrySet()) {
            URI paramURI = null;
            try {
                paramURI=new URI(XACMLConstants.RoleIdPrefix+role.getId()+XACMLConstants.RoleParamIdInfix+paramEntry.getKey());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            // TODO, not anything is a string
            AttributeValue paramValue = new StringAttribute(paramEntry.getValue().toString());
            Attribute paramAttribute = new Attribute(paramURI,null,null,paramValue);
            subjectAttributes.add(paramAttribute);
        }
                
        Subject subject = new Subject(subjectAttributes);
        subjects.add(subject);

    }
    
    protected void addAction(Set<Attribute> actions) {
        actions.add(new Attribute(XACMLConstants.ActionAttributeURI,null,null,
                new StringAttribute(mode.toString())));        
    }


    protected void addResource(Set<Attribute> resources, URI id, String resourceName) {
        resources.add(new Attribute(id,null,null,new StringAttribute(resourceName)));
    }

    protected void addGeoserverResource(Set<Attribute> resources) {
        resources.add(new Attribute(XACMLConstants.ResourceAttributeURI,null,null,new StringAttribute("GeoServer")));
    }
    
    protected void addOWSService(Set<Attribute> resources) {
        org.geoserver.ows.Request owsRequest = Dispatcher.REQUEST.get();
        if (owsRequest==null) return;
        resources.add(new Attribute(XACMLConstants.OWSRequestResourceURI,null,null,new StringAttribute(owsRequest.getRequest())));
        resources.add(new Attribute(XACMLConstants.OWSServiceResourceURI,null,null,new StringAttribute(owsRequest.getService())));        
    }
    

    protected void addGeometry(Set<Attribute> resources,  URI attributeURI, Geometry g, String srsName) {
        
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
        
        GeometryAttribute geomAttr = null;
        try {
            geomAttr = new GeometryAttribute(g,srsName,null,GMLVersion.Version3,gmlType);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }            
        resources.add(new Attribute(attributeURI,null,null,geomAttr));
    }
    
    protected void addBbox(Set<Attribute> resources) {
        org.geoserver.ows.Request owsRequest = Dispatcher.REQUEST.get();
        if (owsRequest==null) return;

        ReferencedEnvelope env = (ReferencedEnvelope) owsRequest.getKvp().get("BBOX");
        if (env == null) return;
        
        String srsName =  (String) owsRequest.getKvp().get("SRS");
        Geometry geom = JTS.toGeometry((Envelope)env);
        
        addGeometry(resources,XACMLConstants.BBoxResourceURI,geom,srsName);
                
    }
    
    abstract public RequestCtx createRequestCtx();
        

        
}

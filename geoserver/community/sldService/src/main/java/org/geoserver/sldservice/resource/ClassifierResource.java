package org.geoserver.sldservice.resource;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.fao.styling.RulesBuilder;
import org.fao.styling.impl.RedColorRamp;
import org.geotools.styling.Rule;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
/**
 * Create Classified sets of rules.
 * @author kappu
 *
 */

public class ClassifierResource extends BaseResource {

	private Integer firstRuleID=null;
	private Integer lastRuleID=null;
	private String featureTypeName=null;
	private String classMethod=null;
	private String property=null;
	private Integer classNum=null;
	private Rule[] rules;
	private Map attributes;

	public ClassifierResource(Context con, Request req, Response resp, Data data) {
		super(con, req, resp, data);
		
		this.attributes = req.getAttributes();
		if (attributes.containsKey("classMethod")) 
			classMethod = (String) attributes.get("classMethod");
		if (attributes.containsKey("property")) 
			property = (String) attributes.get("property");
		if (attributes.containsKey("classNum")) 
			classNum =  new Integer(attributes.get("classNum").toString());
		if (attributes.containsKey("featureType")) 
			featureTypeName = (String) attributes.get("featureType");
		
		
		
		//Get featureType or coverage at this time we are able to manage only featureType
		
		
		
		}

	public boolean allowGet() {
		return true;
	}

	public void handleGet() {
		RulesBuilder ruBuild;
		List<Rule> rulesL=null;
		FeatureTypeInfo ftInf=null;
		Object obj =this.findLayer(attributes);
		
		/*
		 * nota attento che per la unique value non hai bisogno della classe :-)
		 */
		if(obj!=null && this.classNum!=null && this.classMethod!=null && this.property!=null){
			
			if(obj instanceof FeatureTypeInfo){
			 ftInf=(FeatureTypeInfo)obj;
				ruBuild=new RulesBuilder();
				try {
					if(this.classMethod.equals("quantile"))	
							rulesL=ruBuild.quantileClassification(ftInf.getFeatureSource().getFeatures(), this.property, this.classNum);
					else if(this.classMethod.equals("equalInterval"))	
						rulesL=ruBuild.equalIntervalClassification(ftInf.getFeatureSource().getFeatures(), this.property, this.classNum);
					else if(this.classMethod.equals("uniqueValue"))	
						rulesL=ruBuild.uniqueIntervalClassification(ftInf.getFeatureSource().getFeatures(), this.property);
					Class geomT =ftInf.getFeatureType().getDefaultGeometry().getBinding();
					RedColorRamp ramp = new RedColorRamp();
					
					
					/*
					 * Line Symbolizer
					 */
					if (geomT == LineString.class || geomT == MultiLineString.class){
						ruBuild.lineStyle(rulesL, ramp);
					}
					
					/*
					 * Polygon Symbolyzer
					 */
					else if(geomT == MultiPolygon.class || geomT == Polygon.class || geomT == Point.class || geomT == MultiPoint.class){
						ruBuild.polygonStyle(rulesL, ramp);
					}
						
	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}
	if (rulesL != null) {
		this.rules=new Rule[rulesL.size()];
		this.rules=	rulesL.toArray(this.rules);
		this.firstRuleID=new Integer("0");
		this.lastRuleID=new Integer(this.rules.length);
		if (this.lastRuleID>50)this.lastRuleID=50;
		getResponse().setEntity(
					new StringRepresentation(this.fetchRules(),
							MediaType.TEXT_PLAIN));
		} else {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			getResponse().setEntity("Couldn't find requested resource",
					MediaType.TEXT_PLAIN);
		}
	}
	
	
	private String fetchRules() {
		String rulesSz;
		rulesSz = "[";
		if (this.firstRuleID != null && this.lastRuleID != null) {
			rulesSz += this.jsonRule(rules[this.firstRuleID.intValue()]);
			for (int i = 1; i < this.lastRuleID.intValue(); i++) {
				rulesSz += ",";
				rulesSz += this.jsonRule(rules[i]);
			}
			rulesSz += "]";
			return rulesSz;
		} else if (this.firstRuleID != null) {
			rulesSz += this.jsonRule(rules[this.firstRuleID.intValue()]);
			rulesSz += "]";
		} else {
			rulesSz += this.jsonRule(rules[0]);
			for (int i = 1; i < rules.length; i++) {
				rulesSz += ",";
				rulesSz += this.jsonRule(rules[i]);
			}
			rulesSz += "]";
		}
		return rulesSz;
	}
	private Object findLayer(Map attributes) {
		/* Looks in attribute map if there is the featureType param */
		if (attributes.containsKey("featureType")) {
			this.featureTypeName = (String) attributes.get("featureType");

			/* First try to find as a FeatureType */
			try {
				return this.dt.	getFeatureTypeInfo(this.featureTypeName);
			} catch (NoSuchElementException e) {
				/* Try to find as coverage */
				try {
					return this.dt.getCoverageInfo(this.featureTypeName);

				} catch (NoSuchElementException e1) {
					/* resurce not found return null */
					return null;
				}
			}
			/*attribute not found*/
		} else
			return null;
	}
	
}

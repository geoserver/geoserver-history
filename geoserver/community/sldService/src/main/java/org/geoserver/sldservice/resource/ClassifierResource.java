package org.geoserver.sldservice.resource;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.fao.styling.ColorRamp;
import org.fao.styling.RulesBuilder;
import org.fao.styling.impl.BlueColorRamp;
import org.fao.styling.impl.CustomColorRamp;
import org.fao.styling.impl.GrayColorRamp;
import org.fao.styling.impl.RandomColorRamp;
import org.fao.styling.impl.RedColorRamp;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Create Classified sets of rules. And save as style to current style
 * 
 * @author kappu
 * 
 */

public class ClassifierResource extends BaseResource {

	private String featureTypeName = null;
	private String userStyleId = null;
	private Rule[] rules;
	private Map attributes;

	public ClassifierResource(Context con, Request req, Response resp, Data data) {
		super(con, req, resp, data);

		this.attributes = req.getAttributes();
		if (attributes.containsKey("userStyleID"))
			userStyleId = (String) attributes.get("userStyleID");
		if (attributes.containsKey("featureType"))
			featureTypeName = (String) attributes.get("featureType");

	}

	public boolean allowPost() {

		return true;
	}

	public synchronized void handlePost() {
		String classMethod = null;
		String property = null;
		Integer classNum = null;
		String colorRamp =null;
		Color startColor = null;
		Color endColor = null;
		Color midColor = null;
		RulesBuilder ruBuild;

		/*
		 * get featureType dosn't manage coverage for at this moment
		 */
		FeatureTypeInfo ftInf = null;
		Object obj = this.findLayer(attributes);
		if (obj == null || !(obj instanceof FeatureTypeInfo)) {
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			getResponse().setEntity("Can't locate feaureType resource",
					MediaType.TEXT_PLAIN);
			return;
		}

		/*
		 * is feature Type info as expected
		 */
		ftInf = (FeatureTypeInfo) obj;

		/*
		 * Check userStyle exist and if so should be in featureType sld list
		 */
		if (this.dt.getStyle(this.userStyleId) == null
				|| (!ftInf.getStyles().contains(
						this.dt.getStyle(this.userStyleId)))
				&& !ftInf.getDefaultStyle().equals(
						this.dt.getStyle(this.userStyleId))) {
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			getResponse().setEntity(
					"Can't locate UserStyle resource for this feature type",
					MediaType.TEXT_PLAIN);
			return;
		}

		/*
		 * Retrive and check mandatory post pram: classMethod property
		 */
		Request req = getRequest();
		Form params = req.getEntityAsForm();

		if (params.getFirst("classMethod") == null
				|| params.getFirst("classMethod").getValue() == null
				|| (!params.getFirst("classMethod").getValue()
						.equalsIgnoreCase("unique")
						&& !params.getFirst("classMethod").getValue()
								.equalsIgnoreCase("quantile") && !params
						.getFirst("classMethod").getValue().equalsIgnoreCase(
								"equalInterval"))) {
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			getResponse().setEntity("Bad classMethod prameter value",
					MediaType.TEXT_PLAIN);
			return;
		} else
			classMethod = params.getFirst("classMethod").getValue();
		if (params.getFirst("property") == null
				|| params.getFirst("property").getValue() == null
				|| !(ftInf.getAttributeNames().contains(params.getFirst(
						"property").getValue()))) {
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			getResponse().setEntity("Missing requested property",
					MediaType.TEXT_PLAIN);
			return;
		} else
			property = params.getFirst("property").getValue();

		/*
		 * Retriving or setting default value for optional param
		 * ClassNum,startColor, endColor, midColor,colorRamp
		 */
		if (params.getFirst("classNum") == null
				|| params.getFirst("classNum").getValue() == null) {
			classNum = 4;
		} else
			classNum = Integer.parseInt(params.getFirst("classNum").getValue());
		classNum = (classNum < 2) ? 4 : classNum;
		/*
		 * Looks for color ramp type [red, blue, gray, random , custom]
		 */
		if (params.getFirst("colorRamp") != null
				&& params.getFirst("colorRamp").getValue() != null) {

			colorRamp = params.getFirst("colorRamp").getValue();	
			
		}
		
		/*
		 * if is custom lokking for custom color
		 */
		if(colorRamp.equalsIgnoreCase("custom")){
		
		if (params.getFirst("startColor") != null
				&& params.getFirst("startColor").getValue() != null) {
			
				try {
					startColor = Color.decode(params.getFirst("startColor").getValue());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			
		}
		if (params.getFirst("endColor") != null
				&& params.getFirst("endColor").getValue() != null) {
			try {
				endColor = Color.decode(params.getFirst("endColor").getValue());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if (params.getFirst("midColor") != null
				&& params.getFirst("midColor").getValue() != null) {
			try {
				midColor= Color.decode(params.getFirst("midColor").getValue());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		}

		/*
		 * Now we can start to create classification
		 */
		ruBuild = new RulesBuilder();
		List<Rule> rulesL = null;
		try {
			if (classMethod.equals("quantile"))
				rulesL = ruBuild.quantileClassification(ftInf
						.getFeatureSource().getFeatures(), property, classNum);
			else if (classMethod.equals("equalInterval"))
				rulesL = ruBuild.equalIntervalClassification(ftInf
						.getFeatureSource().getFeatures(), property, classNum);
			else if (classMethod.equals("unique"))
				rulesL = ruBuild.uniqueIntervalClassification(ftInf
						.getFeatureSource().getFeatures(), property);
			Class geomT = ftInf.getFeatureType().getDefaultGeometry()
					.getBinding();

			/*
			 * Check the number of class if more then 100 refuse to produce sld
			 * 
			 */
			if (rulesL.size() > 100) {
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
				getResponse()
						.setEntity(
								"This classification produce more then 100 class, change method or attribute",
								MediaType.TEXT_PLAIN);
				return;
			}
			
			/*
			 * now we have to create symbolizer choose the correct color ramp
			 */
			ColorRamp ramp=null;
			if (colorRamp.equalsIgnoreCase("random"))
				ramp= (ColorRamp) new RandomColorRamp();
			else if(colorRamp.equalsIgnoreCase("red"))
				ramp= (ColorRamp) new RedColorRamp();
			else if(colorRamp.equalsIgnoreCase("blue"))
				ramp= (ColorRamp) new BlueColorRamp();
			else if(colorRamp.equalsIgnoreCase("custom")){
				if (startColor != null && endColor != null) {
				CustomColorRamp tramp = new CustomColorRamp();
				tramp.setStartColor(startColor);
				tramp.setEndColor(endColor);
				if (midColor != null)
					tramp.setMid(midColor);
				ramp =(ColorRamp)tramp;
			
				} 
			}else if(ramp ==null) 
				ramp = (ColorRamp) new GrayColorRamp();
			
			/*
			 * Line Symbolizer
			 */

			if (geomT == LineString.class || geomT == MultiLineString.class) {
				ruBuild.lineStyle(rulesL, ramp);
			}

			/*
			 * Polygon Symbolyzer
			 */
			else if (geomT == MultiPolygon.class || geomT == Polygon.class
					|| geomT == Point.class || geomT == MultiPoint.class) {
				ruBuild.polygonStyle(rulesL, ramp);
			}
		} catch (IOException e) {

			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
			getResponse().setEntity(e.toString(), MediaType.TEXT_PLAIN);
			return;

		}

		/*
		 * Now we have to put new rules in UserStyle
		 */
		if (rulesL != null) {
			this.rules = new Rule[rulesL.size()];
			this.rules = rulesL.toArray(this.rules);
			Style style = this.dt.getStyle(this.userStyleId);
			style.getFeatureTypeStyles()[0].setRules(rules);

			/*
			 * creation ok
			 */
			getResponse().setStatus(Status.SUCCESS_CREATED);
			getResponse().setEntity("Classification succesfully created",
					MediaType.TEXT_PLAIN);
		} else {
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
			getResponse().setEntity("Unable to complete classfication",
					MediaType.TEXT_PLAIN);
		}

	}

	public boolean allowGet() {
		return false;
	}

	private Object findLayer(Map attributes) {
		/* Looks in attribute map if there is the featureType param */
		if (attributes.containsKey("featureType")) {
			this.featureTypeName = (String) attributes.get("featureType");

			/* First try to find as a FeatureType */
			try {
				return this.dt.getFeatureTypeInfo(this.featureTypeName);
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

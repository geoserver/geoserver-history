/**
 * 
 */
package org.geoserver.sldservice.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.transform.TransformerException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.fao.styling.RulesBuilder;
import org.geotools.feature.FeatureCollection;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDTransformer;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.global.Data;

/**
 * @author Alessio
 *
 */
public class EqualIntervalGenerator extends Restlet {
	final private RulesBuilder builder = new RulesBuilder();
	private Data dt;
	
	public EqualIntervalGenerator(Data dt) {
		this.dt = dt;
	}
	
	public void handle(Request request, Response response) {
		Map attributes = request.getAttributes();
		/*try to get the featureType or coverage object*/
		List<Rule> rules = this.generateEqualInterval(attributes);
		JSONArray json = null;
		
		if(rules != null) 
			json = this.jsonRulesList(request, rules);
		
		if(json!=null){
				String message = json.toString();
				response.setEntity(new StringRepresentation(message,
					MediaType.TEXT_PLAIN));
		}else{
			response.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			response.setEntity("404 - Couldn't find requested resource", MediaType.TEXT_PLAIN);
		}
	}
	
	private JSONArray jsonRulesList(Request req, List<Rule> rules) {

		Reference ref = req.getResourceRef();
		JSONArray json=null;
		
		List out = new ArrayList();
		for (Rule rule : rules) {
			out.add(jsonRule(rule));
		}
		
		json = JSONArray.fromObject(out);
		
		return json;
	}
	
	/**
	 * 
	 * @param rule
	 * @return a string with json Rule representation
	 */
	private String jsonRule(Rule rule) {

		String ruleSz=null;
		String xmlRule;
		XMLSerializer xmlS = new XMLSerializer();
				
		SLDTransformer transform = new SLDTransformer();
		transform.setIndentation(2);
		try {
			xmlRule = transform.transform( rule );
			xmlS.setRemoveNamespacePrefixFromElements(true);
			xmlS.setSkipNamespaces(true);
			JSONObject json = (JSONObject) xmlS.read( xmlRule );
			ruleSz = json.toString();
			
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		
		return ruleSz;

	}
	
	private List<Rule> generateEqualInterval(Map attributes) {
		/* Looks in attribute map if there is the featureType param */
		if (attributes.containsKey("featureType") && attributes.containsKey("property") && attributes.containsKey("classNum")) {
			final String featureTypeName = (String) attributes.get("featureType");
			final String property = (String) attributes.get("property");
			final int classNum = Integer.valueOf((String) attributes.get("classNum"));

			/* First try to find as a FeatureType */
			try {
				FeatureCollection ftCollection = this.dt.getFeatureTypeInfo(featureTypeName).getFeatureSource(true).getFeatures();
				return builder.equalIntervalClassification(ftCollection, property, classNum, true);
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else
			return null;
	}
}

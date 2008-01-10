package org.geoserver.sldservice;

/* utils class that list attributs for a featuretype
 * this will be deprecated when available in restconfig
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.geotools.styling.Style;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.AttributeTypeInfo;
import com.noelios.restlet.ext.servlet.ServletCall;
import com.noelios.restlet.http.HttpCall;
import com.noelios.restlet.http.HttpRequest;

/**
 * @author kappu
 *
 * Should get all Attributes related to a featureType
 * we have internal Style abd external sld
 *
 */
public class ListAttributes extends Restlet {
	private Data dt;
	private String featureTypeName;

	public ListAttributes(Context context, Data dt) {
		super(context);
		this.dt = dt;
	}

	public void handle(Request request, Response response) {

		Map attributes = request.getAttributes();
		/*try to get the featureType or coverage object*/
		Object obj =this.findLayer(attributes);
		JSONArray json = null;
		
		if(obj!=null) 
			json =  this.jsonAttributesList(request,obj);
		
		if(json!=null){
				String message = json.toString();
				response.setEntity(new StringRepresentation(message,
					MediaType.TEXT_PLAIN));
		}else{
			response.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			response.setEntity("404 - Couldn't find requested resource", MediaType.TEXT_PLAIN);
		}
		
		
	}

	private JSONArray jsonAttributesList(Request req, Object obj) {

		Reference ref = req.getResourceRef();
		JSONArray json=null;
		
		List<AttributeTypeInfo> attributes = null;
		/* Check if it's feature type or coverage */
		if (obj instanceof FeatureTypeInfo) {
			FeatureTypeInfo fTpInfo;
			fTpInfo = (FeatureTypeInfo) obj;
			attributes = fTpInfo.getAttributes();
			List out = new ArrayList();
			Map attributesOut;
			for (AttributeTypeInfo attr : attributes) {
				attributesOut= new HashMap();
				attributesOut.put("name",attr.getName() );
				attributesOut.put("type", attr.getType());
				out.add(attributesOut);
			}
			json = JSONArray.fromObject(out);
		}
	

		return json;
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

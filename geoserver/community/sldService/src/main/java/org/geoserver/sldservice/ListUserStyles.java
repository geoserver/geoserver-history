package org.geoserver.sldservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

/**
 * @author kappu
 *
 * Should get all UserStyles related to a featureType
 * we have internal Style abd external sld
 *
 */
public class ListUserStyles extends Restlet {
	private Data dt;
	private String featureTypeName;

	public ListUserStyles(Context context, Data dt) {
		super(context);
		this.dt = dt;
	}

	public void handle(Request request, Response response) {

		Map attributes = request.getAttributes();
		/*try to get the featureType or coverage object*/
		Object obj =this.findLayer(attributes);
		
		if(obj!=null){
			String message = this.jsonUserStyleList(request,obj).toString();
			response.setEntity(new StringRepresentation(message,
					MediaType.TEXT_PLAIN));
		}else{
			response.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			response.setEntity("404 - Couldn't find requested resource", MediaType.TEXT_PLAIN);
		}
		
		
		
	}

	private JSONArray jsonUserStyleList(Request req, Object obj) {

		Reference ref = req.getResourceRef();
		String bUrl = ref.toString();
		if (bUrl.charAt(bUrl.length() - 1) == '/')
			bUrl = bUrl.substring(0, bUrl.length() - 1);

		Style defStyle = null;
		List styles = null;
		/* Check if it's feature type or coverage */
		if (obj instanceof FeatureTypeInfo) {
			FeatureTypeInfo fTpInfo;
			fTpInfo = (FeatureTypeInfo) obj;
			defStyle = fTpInfo.getDefaultStyle();
			styles = fTpInfo.getStyles();
		} else {
			CoverageInfo cInfo;
			cInfo = (CoverageInfo) obj;
			defStyle = cInfo.getDefaultStyle();
			styles = cInfo.getStyles();
		}

		List out = new ArrayList();
		Map styleOut = new HashMap();

		String name;
		/* set the default style */
		name = defStyle.getName();
		styleOut.put("default", "true");
		styleOut.put("link", bUrl + "/" + name);
		styleOut.put("name", name);
		
		
		out.add(JSONObject.fromObject(styleOut).toString());

		
		if (styles != null) {
			Iterator it = styles.iterator();
			Style style;
			while (it.hasNext()) {
				style = (Style) it.next();
				name = style.getName();
				styleOut = new HashMap();
				styleOut.put("link", bUrl + "/" + name);
				styleOut.put("name", name);
				out.add(JSONObject.fromObject(styleOut).toString());

			}
		}
		
		Collections.sort(out);
		JSONArray json = JSONArray.fromObject(out);
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

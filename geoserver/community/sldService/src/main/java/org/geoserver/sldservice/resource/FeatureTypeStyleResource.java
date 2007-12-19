package org.geoserver.sldservice.resource;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.util.PropertyFilter;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.opengis.filter.FilterFactory2;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.global.Data;

import com.noelios.restlet.ext.servlet.ServletCall;
import com.noelios.restlet.http.HttpCall;
import com.noelios.restlet.http.HttpRequest;

public class FeatureTypeStyleResource extends BaseResource {
	private int featureTypeStyleID;
	private String userStyleID;
	private Style style;
	private FeatureTypeStyle fTStyle;
	
	public FeatureTypeStyleResource(Context con, Request req, Response resp, Data data) {
		super(con, req, resp,data);
		this.userStyleID = req.getAttributes().get("userStyleID").toString();
		this.featureTypeStyleID = Integer.parseInt(req.getAttributes().get("featureTypeID").toString().trim());
	}

	public boolean allowGet() {
		return true;
	}

	public void handleGet() {
		style = this.dt.getStyle(this.userStyleID);
		fTStyle = style.getFeatureTypeStyles()[this.featureTypeStyleID];
		if(fTStyle!=null){
			getResponse().setEntity(
					new StringRepresentation(this.fetchFeatureTypeStyle(),
							MediaType.TEXT_PLAIN));
		} else {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			getResponse().setEntity("Couldn't find requested resource",
					MediaType.TEXT_PLAIN);
		}
	}

	private String fetchFeatureTypeStyle() {
		String featureTypeStyleSz;
	
		featureTypeStyleSz = "{'name':'" + fTStyle.getName() + "','title':'"
				+ fTStyle.getTitle() + "','abstract':'" + fTStyle.getAbstract()
				+ "','featureTypeName':'"+fTStyle.getFeatureTypeName()+"',ruleslength:'"+fTStyle.getRules().length+"'}";
		
		return featureTypeStyleSz;
	

	}

}

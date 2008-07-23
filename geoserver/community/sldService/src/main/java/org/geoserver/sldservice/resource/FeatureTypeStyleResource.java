package org.geoserver.sldservice.resource;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.global.Data;

public class FeatureTypeStyleResource extends BaseResource {
	private int featureTypeStyleID;
	private String userStyleID;
	private Style style;
	private FeatureTypeStyle fTStyle;
	
	public FeatureTypeStyleResource(Data data) {
		super(data);
	}

	public boolean allowGet() {
		return true;
	}

	public void handleGet() {
        this.userStyleID = getRequest().getAttributes().get("userStyleID").toString();
	    this.featureTypeStyleID = Integer.parseInt(getRequest().getAttributes().get("featureTypeID").toString().trim());

	    
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

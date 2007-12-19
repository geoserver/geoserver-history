package org.geoserver.sldservice.resource;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.global.Data;

public class UserStyleResource extends BaseResource {
	private String userStyleID;
	private Style style;

	public UserStyleResource(Context con, Request req, Response resp, Data data) {
		super(con, req, resp, data);
		this.userStyleID = req.getAttributes().get("userStyleID").toString();
	}

	public boolean allowGet() {
		return true;
	}

	public void handleGet() {
		style = this.dt.getStyle(this.userStyleID);
		try {
			getResponse().setEntity(
					new StringRepresentation(this.fetchUserStyle(),
							MediaType.TEXT_PLAIN));
		} catch (Exception e) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			getResponse().setEntity("Couldn't find requested resource",
					MediaType.TEXT_PLAIN);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * prepare json string off UserStyle
	 * 
	 * @return
	 */
	private String fetchUserStyle() {
		String userStyleSz;

		userStyleSz = "{'name':'" + style.getName() + "','title':'"
				+ style.getTitle() + "','abstract':'" + style.getAbstract()
				+ "','featureTypeStyles':[";
		FeatureTypeStyle[] ftStyleA = style.getFeatureTypeStyles();

		String fTypeStyleSz;
		FeatureTypeStyle fTypeStyle = ftStyleA[0];
		fTypeStyleSz = "{'name':'" + fTypeStyle.getName() + "','link':'"
				+ this.baseUrl + "/0','id':'0'}";
		userStyleSz += fTypeStyleSz;

		for (int i = 1; i < ftStyleA.length; i++) {
			fTypeStyle = ftStyleA[i];
			fTypeStyleSz = "{'name':'" + fTypeStyle.getName() + "','link':'"
					+ this.baseUrl + "/" + i + "','id':,'" + i + "'}";
			userStyleSz += "," + fTypeStyleSz;
		}
		userStyleSz += "]}";
		return userStyleSz;
	}

}

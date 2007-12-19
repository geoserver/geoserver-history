package org.geoserver.sldservice.finder;

import org.geoserver.sldservice.resource.ClassifierResource;
import org.geoserver.sldservice.resource.FeatureTypeStyleResource;
import org.geoserver.sldservice.resource.RulesResource;
import org.geoserver.sldservice.resource.UserStyleResource;
import org.restlet.Context;
import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;
import org.vfny.geoserver.global.Data;

public class ResourceFinder extends Finder {
	public final static int RESOURCE_USERSTYLE = 1;
	public final static int RESOURCE_FEATURETYPESTYLE = 2;
	public final static int RESOURCE_RULESRESOURCE = 3;
	public final static int RESOURCE_CLASSIFIER = 4;
	private Data myData;
	private int myType = 0;
	
	public ResourceFinder(int type, Context context, Data dc){
		super(context);
		myData = dc;
		myType = type;
	}
	
	public Resource findTarget(Request request, Response response) {
		switch(myType) {
			case RESOURCE_USERSTYLE:
				return new UserStyleResource(getContext(), request, response, myData);
			case RESOURCE_FEATURETYPESTYLE:
				return new FeatureTypeStyleResource(getContext(), request, response, myData);		
			case  RESOURCE_RULESRESOURCE:
			     return new RulesResource(getContext(), request, response, myData);
			case  RESOURCE_CLASSIFIER:
			     return new ClassifierResource(getContext(), request, response, myData);
			default:
				return null;
		}		
	}
}

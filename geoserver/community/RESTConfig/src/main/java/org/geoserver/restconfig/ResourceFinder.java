package org.geoserver.restconfig;

import org.restlet.Context;
import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

import org.vfny.geoserver.config.DataConfig;

public class ResourceFinder extends Finder {
	public final static int RESOURCE_DATASTORE = 1;
	public final static int RESOURCE_FEATURETYPE = 2;
	public final static int RESOURCE_COVERAGE = 3;
	public final static int RESOURCE_STYLE = 4;
	public final static int RESOURCE_PROJECTION = 5;
	public final static int RESOURCE_LAYERGROUP = 6;
	
	private DataConfig myDataConfig;
	private int myType = 0;
	
	public ResourceFinder(int type, Context context, DataConfig dc){
		super(context);
		myDataConfig = dc;
		myType = type;
	}
	
	public Resource findTarget(Request request, Response response) {
		switch(myType) {
			case RESOURCE_DATASTORE:
				return new DataStoreResource(getContext(), request, response, myDataConfig);
			case RESOURCE_FEATURETYPE:
				return new FeatureTypeResource(getContext(), request, response, myDataConfig);			
			default:
				return null;
		}
		
	}
}

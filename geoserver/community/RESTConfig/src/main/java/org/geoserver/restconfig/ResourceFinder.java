package org.geoserver.restconfig;

import org.restlet.Context;
import org.restlet.Finder;
import org.restlet.Router;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;
import org.springframework.context.ApplicationContext;

import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.WMSConfig;

public class ResourceFinder extends Finder {
    public final static int RESOURCE_DATASTORE = 1;
    public final static int RESOURCE_FEATURETYPE = 2;
    public final static int RESOURCE_COVERAGE = 3;
    public final static int RESOURCE_STYLE = 4;
    public final static int RESOURCE_PROJECTION = 5;
    public final static int RESOURCE_LAYERGROUP = 6;
    public final static int RESOURCE_INDEX = 7;
    public final static int RESOURCE_COVERAGESTORE = 8;

    private ApplicationContext myAppCon;
    private Router myRouter;
    private int myType = 0;

    public ResourceFinder(int type, Context context, ApplicationContext appCon, Router router){
        super(context);
        myAppCon = appCon;
        myRouter = router;
        myType = type;
    }

    public Resource findTarget(Request request, Response response) {
        DataConfig myDataConfig = (DataConfig)myAppCon.getBean("dataConfig");
        WMSConfig myWMSConfig = (WMSConfig)myAppCon.getBean("wmsConfig");

	switch(myType) {
        case RESOURCE_DATASTORE:
            return (request.getAttributes().containsKey("datastore") ? 
                    new DataStoreResource(getContext(), request, response, myDataConfig) :
                    new DataStoreListResource(getContext(), request, response, myDataConfig));
        case RESOURCE_FEATURETYPE:
            return new FeatureTypeResource(getContext(), request, response, myDataConfig);		
        case RESOURCE_COVERAGE:
            return (request.getAttributes().containsKey("coverage") ? 
                    new CoverageResource(getContext(), request, response, myAppCon) : 
                    new CoverageListResource(getContext(), request, response, myDataConfig));
        case RESOURCE_COVERAGESTORE:
            return (request.getAttributes().containsKey("coveragestore") ? 
                    new CoverageStoreResource(getContext(), request, response, myDataConfig) : 
                    new CoverageStoreListResource(getContext(), request, response, myDataConfig));
        case RESOURCE_STYLE:
            return (request.getAttributes().containsKey("style") ? 
                    new StyleResource(getContext(), request, response, myDataConfig) : 
                    new StyleListResource(getContext(), request, response, myDataConfig));
        case RESOURCE_LAYERGROUP:
            return new LayerGroupResource(getContext(), request, response, myWMSConfig);
	    case RESOURCE_INDEX:
		return new IndexResource(getContext(), request, response, myRouter);
	    default:
		return null;
	}		
    }
}

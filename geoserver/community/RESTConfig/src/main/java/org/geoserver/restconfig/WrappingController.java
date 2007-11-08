package org.geoserver.restconfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.Restlet;
import org.restlet.Router;
import org.springframework.beans.BeansException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.noelios.restlet.ext.servlet.ServletConverter;

import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.WMSConfig;

/**
  * Simple AbstractController implementation that does the translation between
  * Spring requests and Restlet requests.
  */
public class WrappingController extends AbstractController {

	public static String METHOD_PUT = "PUT";
	public static String METHOD_DELETE = "DELETE";

	ServletConverter myConverter;

        public WrappingController(){
		super();
		setSupportedMethods(new String[]{
				METHOD_GET,
				METHOD_POST,
				METHOD_PUT,
				METHOD_DELETE,
				METHOD_HEAD}
		);
	}
	
	protected void initApplicationContext() throws BeansException {
		super.initApplicationContext();

		myConverter = new ServletConverter(getServletContext());
		myConverter.setTarget(createRoot());
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		myConverter.service(req, resp);

		return null;
	}

	public Restlet createRoot() {
		Router router = new Router();

		DataConfig dc = (DataConfig) getApplicationContext().getBean("dataConfig");
		WMSConfig wmsc = (WMSConfig) getApplicationContext().getBean("wmsConfig");
		router.attach("/datastores", new ResourceFinder(ResourceFinder.RESOURCE_DATASTORE, router.getContext(), dc, wmsc));
		router.attach("/datastores/{datastore}", new ResourceFinder(ResourceFinder.RESOURCE_DATASTORE, router.getContext(), dc, wmsc));
		// This rule messes everything up: router.attach("/datastores/{datastore}/featuretypes", new ResourceFinder(ResourceFinder.RESOURCE_DATASTORE, router.getContext(), dc));
		router.attach("/datastores/{datastore}/featuretypes/{featuretype}", new ResourceFinder(ResourceFinder.RESOURCE_FEATURETYPE, router.getContext(), dc, wmsc));
                router.attach("/styles/{style}", new ResourceFinder(ResourceFinder.RESOURCE_STYLE, router.getContext(), dc, wmsc));
                router.attach("/styles", new ResourceFinder(ResourceFinder.RESOURCE_STYLE, router.getContext(), dc, wmsc));
                router.attach("/coverages", new ResourceFinder(ResourceFinder.RESOURCE_COVERAGE, router.getContext(), dc, wmsc));
                router.attach("/coverages/{coverage}", new ResourceFinder(ResourceFinder.RESOURCE_COVERAGE, router.getContext(), dc, wmsc));
                router.attach("/dummy/{name}", new DummyRestlet(getApplicationContext()));
		router.attach("/layergroups", new ResourceFinder(ResourceFinder.RESOURCE_LAYERGROUP, router.getContext(), dc, wmsc));
		router.attach("/layergroups/{group}", new ResourceFinder(ResourceFinder.RESOURCE_LAYERGROUP, router.getContext(), dc, wmsc));
		router.attach("/projections/", new DummyRestlet(getApplicationContext()));
		router.attach("/projections/{projection}", new DummyRestlet(getApplicationContext()));
		
		return router;
	}

}

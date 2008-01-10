package org.geoserver.sldservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.sldservice.finder.ResourceFinder;
import org.restlet.Restlet;
import org.restlet.Router;
import org.springframework.beans.BeansException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.vfny.geoserver.global.Data;

import com.noelios.restlet.ext.servlet.ServletConverter;

/**
  * Simple AbstractController implementation that does the translation between
  * Spring requests and Restlet requests.
  * kappu
  */
public class WrappingController extends AbstractController {
    
	public static String METHOD_PUT = "PUT";
	public static String METHOD_DELETE = "DELETE";
	private Data dt;
	ServletConverter myConverter;

	public WrappingController(Data data){
		super();
		this.dt=data;
		setSupportedMethods(new String[]{
				METHOD_GET,
				METHOD_POST,
				METHOD_PUT,
				METHOD_DELETE}
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
		
		/*attach finder*/
		router.attach("/attributeInfo/{featureType}", new ListAttributes(router.getContext(), this.dt));
		router.attach("/classify/{featureType}/{userStyleID}",  new ResourceFinder(ResourceFinder.RESOURCE_CLASSIFIER,router.getContext(),this.dt));
		router.attach("/{featureType}", new ListUserStyles(router.getContext(), this.dt));
		router.attach("/{featureType}/{userStyleID}",  new ResourceFinder(ResourceFinder.RESOURCE_USERSTYLE,router.getContext(),this.dt));
		router.attach("/{featureType}/{userStyleID}/{featureTypeID}",  new ResourceFinder(ResourceFinder.RESOURCE_FEATURETYPESTYLE,router.getContext(),this.dt));
		router.attach("/{featureType}/{userStyleID}/{featureTypeID}/rules",  new ResourceFinder(ResourceFinder.RESOURCE_RULESRESOURCE,router.getContext(),this.dt));
		router.attach("/{featureType}/{userStyleID}/{featureTypeID}/rules/{firstRuleID}",  new ResourceFinder(ResourceFinder.RESOURCE_RULESRESOURCE,router.getContext(),this.dt));
		router.attach("/{featureType}/{userStyleID}/{featureTypeID}/rules/{firstRuleID}/{lastRuleID}",  new ResourceFinder(ResourceFinder.RESOURCE_RULESRESOURCE,router.getContext(),this.dt));
		
		return router;
	}

}

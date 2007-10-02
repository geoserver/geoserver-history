package org.geoserver.usermanagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.Restlet;
import org.restlet.Router;
import org.springframework.beans.BeansException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.noelios.restlet.ext.servlet.ServletConverter;

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

		EditableUserDAO eud = 
			(EditableUserDAO)getApplicationContext()
				.getBean("userDetailsService");
		// router.setRequiredScore(0);
		// router.attach("/roles", new UserRestlet("Role Management Page"));
		router.attach("/user/{name}", new UserFinder(router.getContext(), eud));
		router.attach("/dummy/{name}", new DummyRestlet(getApplicationContext()));
		// router.attach("/geoserver/users/{user}/roles", new
		// UserRestlet("getting role information"));
		return router;
	}

}

package org.geoserver.usermanagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.security.EditableUserDAO;
import org.geoserver.security.GeoserverUserDao;
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

	ServletConverter myConverter;

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

		// router.setRequiredScore(0);
		// router.attach("/roles", new UserRestlet("Role Management Page"));
		router.attach("/user/{name}", 
				new UserRestlet((EditableUserDAO)getApplicationContext().getBean("userDetailsService")));
		router.attach("/dummy/{name}", new DummyRestlet(getApplicationContext()));
		// router.attach("/geoserver/users/{user}/roles", new
		// UserRestlet("getting role information"));
		return router;
	}

}

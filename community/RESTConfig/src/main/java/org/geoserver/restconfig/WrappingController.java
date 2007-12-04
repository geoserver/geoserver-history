package org.geoserver.restconfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.Restlet;
import org.restlet.Router;
import org.springframework.beans.BeansException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.context.ApplicationContext;

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
        ApplicationContext context = getApplicationContext();

        router.attach("", new ResourceFinder(ResourceFinder.RESOURCE_INDEX, router.getContext(), context, router));
        router.attach("/datastores.{type}", new ResourceFinder(ResourceFinder.RESOURCE_DATASTORE, router.getContext(), context, router));
        router.attach("/datastores", new ResourceFinder(ResourceFinder.RESOURCE_DATASTORE, router.getContext(), context, router));
        router.attach("/datastores/{datastore}.{type}", new ResourceFinder(ResourceFinder.RESOURCE_DATASTORE, router.getContext(), context, router));
        router.attach("/datastores/{datastore}", new ResourceFinder(ResourceFinder.RESOURCE_DATASTORE, router.getContext(), context, router));
        // This rule messes everything up: router.attach("/datastores/{datastore}/featuretypes", new ResourceFinder(ResourceFinder.RESOURCE_DATASTORE, router.getContext(), dc));
        router.attach("/datastores/{datastore}/featuretypes/{featuretype}.{type}", new ResourceFinder(ResourceFinder.RESOURCE_FEATURETYPE, router.getContext(), context, router));
        router.attach("/datastores/{datastore}/featuretypes/{featuretype}", new ResourceFinder(ResourceFinder.RESOURCE_FEATURETYPE, router.getContext(), context, router));
        router.attach("/styles/{style}.{type}", new ResourceFinder(ResourceFinder.RESOURCE_STYLE, router.getContext(), context, router));
        router.attach("/styles/{style}", new ResourceFinder(ResourceFinder.RESOURCE_STYLE, router.getContext(), context, router));
        router.attach("/styles.{type}", new ResourceFinder(ResourceFinder.RESOURCE_STYLE, router.getContext(), context, router));
        router.attach("/styles", new ResourceFinder(ResourceFinder.RESOURCE_STYLE, router.getContext(), context, router));
        router.attach("/coveragestores.{type}", new ResourceFinder(ResourceFinder.RESOURCE_COVERAGESTORE, router.getContext(), context, router));
        router.attach("/coveragestores", new ResourceFinder(ResourceFinder.RESOURCE_COVERAGESTORE, router.getContext(), context, router));
        router.attach("/coveragestores/{coveragestore}.{type}", new ResourceFinder(ResourceFinder.RESOURCE_COVERAGESTORE, router.getContext(), context, router));
        router.attach("/coveragestores/{coveragestore}", new ResourceFinder(ResourceFinder.RESOURCE_COVERAGESTORE, router.getContext(), context, router));
        router.attach("/coveragestores/{coveragestore}/coverages/{coverage}.{type}", new ResourceFinder(ResourceFinder.RESOURCE_COVERAGE, router.getContext(), context, router));
        router.attach("/coveragestores/{coveragestore}/coverages/{coverage}", new ResourceFinder(ResourceFinder.RESOURCE_COVERAGE, router.getContext(), context, router));
        router.attach("/dummy/{name}", new DummyRestlet(getApplicationContext()));
        router.attach("/layergroups.{type}", new ResourceFinder(ResourceFinder.RESOURCE_LAYERGROUP, router.getContext(), context, router));
        router.attach("/layergroups", new ResourceFinder(ResourceFinder.RESOURCE_LAYERGROUP, router.getContext(), context, router));
        router.attach("/projections", new DummyRestlet(getApplicationContext()));
        router.attach("/projections/{projection}", new DummyRestlet(getApplicationContext()));

        return router;
    }

}

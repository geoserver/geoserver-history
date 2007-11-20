package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.FileRepresentation;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;

/**
 * Restlet for Style resources
 * 
 * @author David Winslow <dwinslow@openplans.org> , The Open Planning Project
 */
public class CoverageResource extends Resource {
    private DataConfig myDC;

    public CoverageResource(Context context, Request request, Response response,
	                    DataConfig myDataConfig) {
	super(context, request, response);
	myDC = myDataConfig;
    }

    private DataStoreConfig findMyDataStore() {
	Map attributes = getRequest().getAttributes();
	if(attributes.containsKey("datastore")) {
	    String dsid = (String) attributes.get("datastore");
	    return myDC.getDataStore(dsid);
	}
	return null;
    }

    public boolean allowGet() {
	return true;
    }

    public void handleGet() {
	MediaType mt = null;
	Request req = getRequest();

	// Determine desired output format
	if (req.getResourceRef().getQueryAsForm().contains("format")) {
	    mt = MediaType.valueOf(req.getResourceRef().getQueryAsForm()
		    .getFirstValue("format"));
	} else {
	    mt = MediaType.TEXT_HTML;
	}

	String coverageName = (String)req.getAttributes().get("coverage");
	if (coverageName == null){
	    Map coverages = myDC.getCoverages();
	    Map templateContext = new HashMap();
	    List coverageList = new ArrayList();

	    Iterator it = coverages.entrySet().iterator();
	    while (it.hasNext()){
		coverageList.add(((Map.Entry)it.next()).getKey().toString());
	    }

	    templateContext.put("coverages", coverageList);

	    templateContext.put("requestURL", req.getResourceRef().getBaseRef());

	    getResponse().setEntity(
		    HTMLTemplate.getHtmlRepresentation("HTMLTemplates/coverages.ftl", templateContext)); 
	} else {
	    CoverageConfig cc =(CoverageConfig) myDC.getCoverages().get(coverageName);
	    if (cc != null){
		getResponse().setEntity(HTMLTemplate.getHtmlRepresentation("HTMLTemplates/coverage.ftl", getCoverageConfigMap(cc)));
		//      getResponse().setEntity(new StringRepresentation("Found the coverage, but I'm not telling you anything about it!", MediaType.TEXT_PLAIN));
	    } else {
		getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		getResponse().setEntity(new StringRepresentation("Error - Couldn't find the requested resource", MediaType.TEXT_PLAIN));
	    }
	} 
    }

    private TemplateRepresentation getIndexXML(HashMap map) {
	return XMLTemplate.getXmlRepresentation("XMLTemplates/datastores.ftl", map);
    }

    private TemplateRepresentation getIndexJSON(HashMap map) {
	// ToDo
	return null;
    }

    private TemplateRepresentation getIndexHTML(HashMap map) {
	return HTMLTemplate.getHtmlRepresentation("HTMLTemplates/coverage.ftl", map);
    }

    private Map getCoverageConfigMap(CoverageConfig cc){
	Map m = new HashMap();
	m.put("coverageName", cc.getName());
	m.put("CRSDescription", cc.getCrs().getName());
	m.put("CRSFull", cc.getCrs().toString());
	m.put("DefaultStyle", cc.getDefaultStyle());
	m.put("Label", cc.getLabel());
	m.put("Description", cc.getDescription());
	m.put("Keywords", cc.getKeywords());
	return m;
    }

    /**
     * Sorts the FeatureTypeConfigs according to the datastore to which they are associated.
     * 
     * @return HashMap where datastore id is key and value is list of associated feature types
     */
    private HashMap getSortedFeatureTypes() {
	HashMap map = new HashMap();
	for (Iterator it = myDC.getFeaturesTypes().values().iterator(); it.hasNext();) {
	    FeatureTypeConfig ftc = (FeatureTypeConfig) it.next();
	    List ftcs = (ArrayList) map.get(ftc.getDataStoreId());
	    if(ftcs == null)
		ftcs = new ArrayList();
	    ftcs.add(ftc);
	    map.put(ftc.getDataStoreId(), ftcs);
	}
	return map;
    }

    /**
     * Get the FeatureTypeConfigs for given DataStoreId
     * 
     * @return list of FeatureTypeConfigs in given DataStore
     */
    private List getFeatureTypes(String dsId) {
	List ftcs = new ArrayList();
	for (Iterator it = myDC.getFeaturesTypes().values().iterator(); it.hasNext();) {
	    FeatureTypeConfig ftc = (FeatureTypeConfig) it.next();

	    if(ftc.getDataStoreId().equals(dsId))
		ftcs.add(ftc);
	}
	return ftcs;
    }

    /**
     * Extracts information from FeatureTypeConfigs and creates a Map
     * suitable for use with FreeMarker templates.
     * 
     * @param featureTypeConfigs list with FeatureTypeConfigs
     * @return HashMap for use with FreeMarker templates
     */
    private HashMap makeFeatureTypeMap(List featureTypeConfigs) {
	HashMap map = new HashMap();
	List fts = new ArrayList();
	for (Iterator it = featureTypeConfigs.iterator(); it.hasNext();) {
	    HashMap am = new HashMap();
	    FeatureTypeConfig ftc = (FeatureTypeConfig) it.next();
	    am.put("name", ftc.getName());
	    am.put("srs", Integer.valueOf(ftc.getSRS()));
	    fts.add(am);
	}
	map.put("featuretypes", fts);
	return map;
    }

    public boolean allowPut() {
	return false;
    }

    public void handlePut(){

    }

    public boolean allowDelete() {
	return false;    
    }

    public void handleDelete(){

    }   
}

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
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;

/**
 * Restlet for DataStore resources
 * 
 * @author Arne Kepp <ak@openplans.org> , The Open Planning Project
 */
public class DataStoreResource extends MapResource {
    private DataConfig myDC;
    private DataStoreConfig myDSC;

    public DataStoreResource(Context context,
	    Request request,
	    Response response,
	    DataConfig dc){
	super(context, request, response);
	myDC  = dc;
	myDSC = findMyDataStore();
    }

    private DataStoreConfig findMyDataStore() {
	Map attributes = getRequest().getAttributes();
	if(attributes.containsKey("datastore")) {
	    String dsid = (String) attributes.get("datastore");
	    return myDC.getDataStore(dsid);
	}
	return null;
    }

    public Map getSupportedFormats(){
	Map m = new HashMap();
	m.put("html", new HTMLFormat("HTMLTemplates/datastore.ftl"));
	m.put("json", new JSONFormat());
	m.put(null, m.get("html"));
	return m;
    }

    public Map getMap(){
	List ftcList = getFeatureTypes(myDSC.getId());
	Map map = makeFeatureTypeMap(ftcList);
	map.put("datastoreid", myDSC.getId());
	return map; 
    }

    /**
     * Create an index showing all the datastores
     * @param mt the media type (HTML, JSON, XML, ...)
     */
    public void getIndex(MediaType mt) {		
	//Get the data
	HashMap map = makeDataStoreMap();
	map.put("currentURL", getRequest().getResourceRef().getBaseRef());
	//Do the output formatting
	if(mt.equals(MediaType.APPLICATION_XML)) {
	    getResponse().setEntity(getIndexXML(map));
	} else if(mt.equals(MediaType.APPLICATION_JSON)) {
	    getResponse().setEntity(getIndexJSON(map));
	} else {
	    //implying mt.equals(MediaType.TEXT_HTML))
	    getResponse().setEntity(getIndexHTML(map));
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
	return HTMLTemplate.getHtmlRepresentation("HTMLTemplates/datastores.ftl", map);
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
	    ftcs.add(ftc.getName());
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

    /**
     * Finds information about all datastore and the associated 
     * FeatureTypeConfigs, then creates map suitable for use with 
     * FreeMarker templates.
     * 
     * @return HashMap for use with FreeMarker templates
     */
    private HashMap makeDataStoreMap() {
	// Get all the featuretypes, sorted according to datastore
	Map sortedFeatureTypes = getSortedFeatureTypes();

	HashMap map = new HashMap();
	List datastores = new ArrayList();
	for (Iterator it = myDC.getDataStores().values().iterator(); it.hasNext();) {
	    HashMap am = new HashMap();
	    DataStoreConfig dsc = (DataStoreConfig) it.next();
	    am.put("id", dsc.getId());
	    am.put("type", dsc.getFactory().getDisplayName());
	    if(sortedFeatureTypes.containsKey(dsc.getId()))
		am.put("featuretypes", sortedFeatureTypes.get(dsc.getId()));
	    datastores.add(am);
	}
	map.put("datastores", datastores);
	return map;
    }

    /**
     * Create a page with information about a given datastore,
     * determined from the request attribute "name", in the given
     * data format.
     * 
     * @param mt the media type (HTML, JSON, XML, ...)
    private void getDataStore(MediaType mt) {
	// We can end up where with an invalid datastore name
	if(myDSC == null) {
	    //Return a 404
	    getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
	    getResponse().setEntity("404 - Couldn't find requested resource", MediaType.TEXT_PLAIN);

	} else {
	    List ftcs = getFeatureTypes(myDSC.getId());
	    HashMap map = makeFeatureTypeMap(ftcs);
	    map.put("datastoreid", myDSC.getId());
	    map.put("currentURL", getRequest().getResourceRef().getBaseRef());

	    //Do the output formatting
	    if(mt.equals(MediaType.APPLICATION_XML)) {
		getResponse().setEntity(getDataStoreXML(map));
	    } else if(mt.equals(MediaType.APPLICATION_JSON)) {
		getResponse().setEntity(getDataStoreJSON(map));
	    } else {
		//implying mt.equals(MediaType.TEXT_HTML))
		getResponse().setEntity(getDataStoreHTML(map));
	    }
	}
    } */
}

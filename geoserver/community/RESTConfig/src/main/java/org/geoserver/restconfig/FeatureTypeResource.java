/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.resource.Resource;
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Restlet for DataStore resources
 *
 * @author Arne Kepp <ak@openplans.org> , The Open Planning Project
 */
public class FeatureTypeResource extends MapResource {
    private DataConfig myDC;
    private DataStoreConfig myDSC = null;
    private FeatureTypeConfig myFTC = null;

    public FeatureTypeResource(){
        super();
    }

    public FeatureTypeResource(Context context, Request request, Response response, DataConfig dc) {
        super(context, request, response);
        myDC = dc;
        myFTC = findMyFeatureTypeConfig();
    }

    public void setDataConfig(DataConfig dc){
        myDC = dc;
    }

    public DataConfig getDataConfig(){
        return myDC;
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();

        m.put("html", new HTMLFormat("HTMLTemplates/featuretype.ftl"));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("FeatureType"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap() {
        myFTC = findMyFeatureTypeConfig();
        Map m = new HashMap();

        m.put("Style", myFTC.getDefaultStyle());
        m.put("AdditionalStyles", myFTC.getStyles());
        m.put("SRS", myFTC.getSRS());
        m.put("SRSHandling", getSRSHandling());
        m.put("Title", myFTC.getTitle());
        m.put("BBox", getBoundingBox()); 
        m.put("Keywords", getKeywords());
        m.put("Abstract", myFTC.getAbstract());
        m.put("WMSPath", myFTC.getWmsPath());
        m.put("MetadataLinks", getMetadataLinks());
        m.put("CachingEnabled", myFTC.isCachingEnabled());
        m.put("CacheTime", Integer.valueOf(myFTC.getCacheMaxAge()));
        m.put("SchemaBase", myFTC.getSchemaBase());

        return m;
    }

    protected void putMap(Map m) throws Exception{
        System.out.println(m);
    }

    private String getSRSHandling(){
        try{
            return (new String[]{"Force","Reproject","Ignore"})[myFTC.getSRSHandling()];
        } catch (Exception e){
            return "Ignore";
        }
    }

    private List getBoundingBox(){
        List l = new ArrayList();
        Envelope e = myFTC.getLatLongBBox();
        l.add(e.getMinX());
        l.add(e.getMaxX());
        l.add(e.getMinY());
        l.add(e.getMaxY());
        return l;
    }

    private List getKeywords(){
        List l = new ArrayList();
        l.addAll(myFTC.getKeywords());
        return l;
    }

    private List getMetadataLinks(){
        List l = new ArrayList();
        l.addAll(myFTC.getMetadataLinks());
        return l;
    }

    private FeatureTypeConfig findMyFeatureTypeConfig() {
        Map attributes = getRequest().getAttributes();
        String dsid = null;

        //The key for the featureTypeConfig depends on the datastores name
        if (attributes.containsKey("datastore")) {
            dsid = (String) attributes.get("datastore");
            myDSC = myDC.getDataStore(dsid);
        }

        if ((myDSC != null) && attributes.containsKey("featuretype")) {
            String ftid = (String) attributes.get("featuretype");

            // Append the datastore prefix
            return myDC.getFeatureTypeConfig(dsid + ":" + ftid);
        }

        return null;
    }

    public boolean allowGet() {
        return true;
    }

    public void donthandleGet() {
        MediaType mt = null;
        Request req = getRequest();

        // Determine desired output format
        if (req.getResourceRef().getQueryAsForm().contains("format")) {
            mt = MediaType.valueOf(req.getResourceRef().getQueryAsForm().getFirstValue("format"));
        } else {
            mt = MediaType.TEXT_HTML;
        }

        // Determine what resource is requested, get it
        if (myFTC != null) {
            // This is a request for a particular datastore
            getFeatureType(mt);
        } else {
            //Return a 404
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            getResponse().setEntity("404 - Couldn't find requested resource", MediaType.TEXT_PLAIN);
        }
    }

    /**
     * Extracts information from FeatureTypeConfig and creates a Map
     * suitable for use with FreeMarker templates.
     *
     * @return HashMap for use with FreeMarker templates
     */
    private HashMap makeFeatureTypeMap() {
        if (myFTC == null) {
            return null;
        }

        HashMap map = new HashMap();
        map.put("name", myFTC.getName());
        map.put("datastoreid", myFTC.getDataStoreId());
        map.put("srs", Integer.valueOf(myFTC.getSRS()));
        map.put("namespace", myDSC.getNameSpaceId());
        map.put("defaultstyle", myFTC.getDefaultStyle());

        ArrayList schemAttribs = new ArrayList();

        for (Iterator it = myFTC.getSchemaAttributes().iterator(); it.hasNext();) {
            AttributeTypeInfoConfig atic = (AttributeTypeInfoConfig) it.next();
            schemAttribs.add(atic.getName() + ": " + atic.getType());
        }

        map.put("schemaattributes", schemAttribs);

        //for (Iterator it = this.myFTC..iterator(); it.hasNext();) {
        //	HashMap am = new HashMap();
        //	FeatureTypeConfig ftc = (FeatureTypeConfig) it.next();
        //	am.put("name", ftc.getName());
        //	am.put("srs", Integer.valueOf(ftc.getSRS()));
        //	fts.add(am);
        //}
        //map.put("featuretypes", fts);
        return map;
    }

    /**
     * Create a page with information about a given datastore,
     * determined from the request attribute "name", in the given
     * data format.
     *
     * It is assumed that if we get here myFTC has been set
     *
     * @param mt the media type (HTML, JSON, XML, ...)
     */
    private void getFeatureType(MediaType mt) {
        HashMap map = makeFeatureTypeMap();

        //Do the output formatting
        if (mt.equals(MediaType.APPLICATION_XML)) {
            getResponse().setEntity(getFeatureTypeXML(map));
        } else if (mt.equals(MediaType.APPLICATION_JSON)) {
            getResponse().setEntity(getFeatureTypeJSON(map));
        } else {
            //implying mt.equals(MediaType.TEXT_HTML))
            getResponse().setEntity(getFeatureTypeHTML(map));
        }
    }

    public TemplateRepresentation getFeatureTypeXML(HashMap map) {
        return XMLTemplate.getXmlRepresentation("XMLTemplates/featuretype.ftl", map);
    }

    public TemplateRepresentation getFeatureTypeJSON(HashMap map) {
        // ToDo
        return null;
    }

    public TemplateRepresentation getFeatureTypeHTML(HashMap map) {
        return HTMLTemplate.getHtmlRepresentation("HTMLTemplates/featuretype.ftl", map);
    }

    public boolean allowPut() {
        return true;
    }

    public boolean allowDelete() {
        return true;
    }

    public void handleDelete() {
    }
}

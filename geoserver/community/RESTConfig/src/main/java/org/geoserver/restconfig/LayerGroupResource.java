/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WCSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;
import org.vfny.geoserver.global.xml.XMLConfigWriter;
import org.geoserver.wfs.WFS;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoserverDataDirectory;



import com.vividsolutions.jts.geom.Envelope;


/**
 * Restlet for Style resources
 *
 * @author David Winslow <dwinslow@openplans.org> , The Open Planning Project
 */
class LayerGroupResource extends MapResource {
    private WMSConfig myWMSConfig;
    private WMS myWMS;
    private WFS myWFS;
    private WCS myWCS;
    private GeoServer myGeoserver;

    public LayerGroupResource(){
        super();
    }

    public LayerGroupResource(Context context, Request request, Response response,
        WMSConfig wmsConfig) {
        super(context, request, response);
        myWMSConfig = wmsConfig;
    }

    public void setWMSConfig(WMSConfig c){
        myWMSConfig = c;
    }

    public WMSConfig getWMSConfig(){
        return myWMSConfig;
    }

    public void setWMS(WMS wms){
        myWMS = wms;
    }

    public WMS getWMS(){
        return myWMS;
    }

    public void setWCS(WCS wcs){
        myWCS = wcs;
    }

    public WCS getWCS(){
        return myWCS;
    }

    public void setWFS(WFS wfs){
        myWFS = wfs;
    }

    public WFS getWFS(){
        return myWFS;
    }

    public void setGeoserver(GeoServer geoserver){
        myGeoserver = geoserver;
    }

    public GeoServer getGeoserver(){
        return myGeoserver;
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();
        m.put("html", new HTMLFormat("HTMLTemplates/layergroups.ftl"));
        m.put("json", new JSONFormat());
        m.put("xml",  new AutoXMLFormat("layergroups"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap() {
        String group = (String)getRequest().getAttributes().get("group");
        Map context = new HashMap();
        Map layerGroups = myWMSConfig.getBaseMapLayers();
        List members = null;
        if (layerGroups != null && 
            layerGroups.containsKey(group)){
            members = Arrays.asList(layerGroups.get(group).toString().split(","));
        }

        List styles = null;
        if (myWMSConfig.getBaseMapStyles() != null &&
            (myWMSConfig.getBaseMapStyles().get(group) != null)){
            styles = Arrays.asList(myWMSConfig.getBaseMapStyles().get(group).toString().split(","));
        }

        context.put("Members", members);
        context.put("Styles", styles);
        
        GeneralEnvelope env = null;
        if (myWMSConfig.getBaseMapEnvelopes() != null && 
        	myWMSConfig.getBaseMapEnvelopes().get(group) != null){
        	env = (GeneralEnvelope)myWMSConfig.getBaseMapEnvelopes().get(group);
        }
        
        context.put("SRS", env == null ?
        		null :
        		env.getCoordinateReferenceSystem().getIdentifiers().toArray()[0].toString());
        context.put("Envelope", (env == null ? null : envelopeAsList(env)));

        return context;
    }
    
    private List envelopeAsList(GeneralEnvelope e){
        List l = new ArrayList();
        
        l.add(Double.valueOf(e.getMinimum(0)));
        l.add(Double.valueOf(e.getMaximum(0)));
        l.add(Double.valueOf(e.getMinimum(1)));
        l.add(Double.valueOf(e.getMaximum(1)));
        
        return l;
    }
    
    private GeneralEnvelope listAsEnvelope(List coords, String srsName) throws Exception{
        GeneralEnvelope genv = new GeneralEnvelope(CRS.decode(srsName));
        genv.setRange(0,
                Double.parseDouble((String)coords.get(0)),
                Double.parseDouble((String)coords.get(1))
                );
        genv.setRange(1, 
                Double.parseDouble((String)coords.get(2)),
                Double.parseDouble((String)coords.get(3))
                );
        return genv;
    }

    private String listAsDelimitedString(List values, String delimiter){
        if (values == null) return "";
        StringBuffer buff = new StringBuffer();
        Iterator it = values.iterator();

        while (it.hasNext()){
            Object obj = it.next();
            
            buff.append(obj == null ? "" : obj.toString());
            if (it.hasNext()) buff.append(delimiter);
        }

        return buff.toString();
    }

    public boolean allowPut(){
        return true;
    }


    protected void putMap(Map details) throws Exception {
    	String group = (String)getRequest().getAttributes().get("group");
    	
    	List layers = (List)details.get("Members");
    	List styles = (List)details.get("Styles");
        String srsName = (String)details.get("SRS");
        List coords = (List)details.get("Envelope");
        GeneralEnvelope env = null;
        try{
        env = listAsEnvelope(coords, srsName);
        } catch(Exception e){
            getResponse().setEntity(
                    new StringRepresentation("Failed to parse Envelope!" + e,
                        MediaType.TEXT_PLAIN)
                    );
            e.printStackTrace();
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }

        LOG.fine("Parsed layergroup details: " + details);
        LOG.fine("Interpreted as: layers=[" + listAsDelimitedString(layers, ", ") + "]; styles=[" + listAsDelimitedString(styles, ", ") + "]; Envelope=" + env);
        
        Map layerGroups = myWMSConfig.getBaseMapLayers();
        if (layerGroups == null){
        	layerGroups = new HashMap();
        }
        
        Map layerStyles = myWMSConfig.getBaseMapStyles();
        if (layerStyles == null){
        	layerStyles = new HashMap();
        }
        
        Map layerEnvelopes = myWMSConfig.getBaseMapEnvelopes();
        if (layerEnvelopes == null){
        	layerStyles = new HashMap();
        }        

        layerGroups.put(group, listAsDelimitedString(layers, ","));
        if (styles == null) {
            layerStyles.put(group, styles);
        } else {
            layerStyles.put(group, listAsDelimitedString(styles, ","));
        }
        layerEnvelopes.put(group, env);

        try{
            saveConfiguration();
        } catch (Exception e){
            getResponse().setEntity(
                    new StringRepresentation("Failed to load configuration for " + group + " due to " + e,
                        MediaType.TEXT_PLAIN)
                    );
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        }

        getResponse().setEntity(
                new StringRepresentation("Successfully PUTted " + group,
                    MediaType.TEXT_PLAIN)
                );
        getResponse().setStatus(Status.SUCCESS_OK);
    }

    public boolean allowDelete(){
        return true;
    }

    public void handleDelete(){
        String group = (String)getRequest().getAttributes().get("group");

        Map layerGroups = myWMSConfig.getBaseMapLayers();
        if (layerGroups == null || !layerGroups.containsKey(group)){
            getResponse().setEntity(
                    new StringRepresentation("Group " + group + " does not exist.",
                        MediaType.TEXT_PLAIN)
                    );
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return;
        }

        layerGroups.remove(group);
        myWMSConfig.getBaseMapStyles().remove(group);
        myWMSConfig.getBaseMapEnvelopes().remove(group);

        try{
            saveConfiguration();
        } catch (ConfigurationException ce){
            getResponse().setEntity(
                    new StringRepresentation("Failed to save configuration due to " + ce,
                        MediaType.TEXT_PLAIN)
                    );
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }

        getResponse().setEntity(
                new StringRepresentation("Deleted layergroup " + group + ".",
                    MediaType.TEXT_PLAIN
                    )
                );
        getResponse().setStatus(Status.SUCCESS_OK);
    }


    private void saveConfiguration() throws ConfigurationException{
        getWMS().load(getWMSConfig().toDTO());
        XMLConfigWriter.store(
                (WCSDTO)getWCS().toDTO(),
                (WMSDTO)getWMS().toDTO(),
                (WFSDTO)getWFS().toDTO(),
                (GeoServerDTO)getGeoserver().toDTO(),
                GeoserverDataDirectory.getGeoserverDataDirectory()
                );
    }
}

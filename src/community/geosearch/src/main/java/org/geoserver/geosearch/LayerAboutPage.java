package org.geoserver.geosearch;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.restlet.Restlet;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Method;
import org.restlet.data.MediaType;
import org.restlet.data.Status;

import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.geoserver.geosearch.GeoServerProxyAwareRestlet;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.rest.DataFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.RESTUtils;
import org.geoserver.rest.RestletException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

import com.vividsolutions.jts.geom.Envelope;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import freemarker.template.SimpleHash;

public class LayerAboutPage extends GeoServerProxyAwareRestlet {
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");	
	
    private final DataFormat format =
        new FreemarkerFormat("layerpage.ftl", getClass(), MediaType.TEXT_HTML);

    private Data catalog;

    public void setCatalog(Data cat){
        catalog = cat;
    }

    public Data getCatalog(){
        return catalog;
    }

    public void handle(Request request, Response response){
        if (request.getMethod().equals(Method.GET)) doGet(request, response);
        else response.setStatus(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    }

    public void doGet(Request request, Response response){
        String namespace = (String)request.getAttributes().get("namespace");
        String layer = (String)request.getAttributes().get("layer");

        response.setEntity(format.makeRepresentation(getContext(namespace, layer, request)));
    }
    
    SimpleHash getContext(String namespace, String layer, Request request){
    	FeatureTypeInfo info = lookupType(namespace, layer);
    	
    	SimpleHash map = new SimpleHash();
    	
    	//basic
    	map.put("title", info.getTitle());
    	map.put("abstract", info.getAbstract());
    	
    	//Metadata
    	map.put("keywords", info.getKeywords());
		map.put("declaredCRS", info.getDeclaredCRS());	    	
		map.put("metadataLinks", info.getMetadataLinks());
		try{
			map.put("nativeCRS", info.getNativeCRS());
		}catch(Exception e){
            LOGGER.log(Level.WARNING, "Error trying to get nativeCRS from " + info.getName() + "FeatureTypeInfo", e);
		}
    	
		String baseUrl = RESTUtils.getBaseURL(request);
		map.put("base", baseUrl);    			
		
		//general parameters for data requests
    	map.put("name", info.getName());
    	map.put("srs", "EPSG:" + info.getSRS());
    	
    	ReferencedEnvelope bbox = getBBOX(info);
    	
    	String bboxString = bbox.getMinX() + "," + bbox.getMinY() + "," + bbox.getMaxX() + ","
        + bbox.getMaxY();    	
    	map.put("bbox", bboxString);
    	
    	map.put("tilesOrigin", bbox.getMinX()+","+bbox.getMinY());    	
    	
    	int[] imageBox = getMapWidthHeight(bbox);
        map.put("width", String.valueOf(imageBox[0]));
        map.put("height", String.valueOf(imageBox[1]));
    	
        map.put("maxResolution", getMaxResolution(bbox));
        
    	try{        	
        	map.put("boundingBox", info.getBoundingBox());
        	map.put("lonLatBoundingBox", info.getLatLongBoundingBox());
    	}catch(IOException e){
            LOGGER.log(Level.WARNING, "Error trying to access bounding box or lonLatBoundingBox for " + info.getName() + "FeatureTypeInfo", e);
    	}	
    	
    	//Fields of Access
    	map.put("gwc", isGWCAround() + "");
    	
    	String gwcLink = baseUrl.substring(0,baseUrl.length()-4) + "gwc/";
    	map.put("gwcLink", gwcLink);
    	
    	map.put("attributes", info.getAttributes());
    	
    	return map;
    }

    private FeatureTypeInfo lookupType(String namespace, String layer){

        NameSpaceInfo ns = catalog.getNameSpace(namespace);
        if ( ns == null ) {
            throw new RestletException(
                    "No such namespace: " + namespace, Status.CLIENT_ERROR_NOT_FOUND 
                    );
        }

        FeatureTypeInfo featureType = null;
        try {
            featureType = catalog.getFeatureTypeInfo(layer,ns.getUri());
        } catch( Exception e ) {
            throw new RestletException(
                    "No such featuretype: " + namespace + ":" + layer,
                     Status.CLIENT_ERROR_NOT_FOUND 
                    );
        }
        return featureType;
    }
    
    
    private ReferencedEnvelope getBBOX(FeatureTypeInfo layer){
    	
        String bboxList;
    	
        // We need to create a 4326 CRS for comparison to layer's crs
        CoordinateReferenceSystem latLonCrs = null;

        try { // get the CRS object for lat/lon 4326
            latLonCrs = CRS.decode("EPSG:" + 4326);
        } catch (NoSuchAuthorityCodeException e) {
            String msg = "Error looking up SRS for EPSG: " + 4326 + ":" + e.getLocalizedMessage();
            //currently does nothing with this string
        } catch (FactoryException e) {
            String msg = "Error looking up SRS for EPSG: " + 4326 + ":" + e.getLocalizedMessage();
            //currently does nothing with this string                        
        }
    	
    	//yoinked from MapPreviewAction
        try {

            CoordinateReferenceSystem layerCrs = layer.getDeclaredCRS();

            /* A quick and efficient way to grab the bounding box is to get it
             * from the featuretype info where the lat/lon bbox is loaded
             * from the DTO. We do this with layer.getLatLongBoundingBox().
             * We need to reproject the bounding box from lat/lon to the layer crs
             * for display
             */
            Envelope orig_bbox = layer.getLatLongBoundingBox();

            if ((orig_bbox.getWidth() == 0) || (orig_bbox.getHeight() == 0)) {
                orig_bbox.expandBy(0.1);
            }

            ReferencedEnvelope bbox = new ReferencedEnvelope(orig_bbox, latLonCrs);

            if (!CRS.equalsIgnoreMetadata(layerCrs, latLonCrs)) {
                // first check if we have a native bbox
                bbox = layer.getBoundingBox();
            }

            // we now have a bounding box in the same CRS as the layer
            if ((bbox.getWidth() == 0) || (bbox.getHeight() == 0)) {
                bbox.expandBy(0.1);
            }
            
            if (layer.isEnabled()) {
                // expand bbox by 5% to allow large symbolizers to fit the map
                bbox.expandBy(bbox.getWidth() / 20, bbox.getHeight() / 20);
                return bbox;
            }
        } catch(Exception e) {

        }
        
        return null;
    }
    
    //yoinked from MapPreviewAction
    private int[] getMapWidthHeight(Envelope bbox) {
        int width;
        int height;
        double ratio = bbox.getHeight() / bbox.getWidth();

        if (ratio < 1) {
            width = 750;
            height = (int) Math.round(750 * ratio);
        } else {
            width = (int) Math.round(550 / ratio);
            height = 550;
        }

        // make sure we reach some minimal dimensions (300 pixels is more or less 
        // the height of the zoom bar)
        if (width < 300) {
            width = 300;
        }

        if (height < 300) {
            height = 300;
        }

        // add 50 pixels horizontally to account for the zoom bar
        return new int[] { width + 50, height };
    }
    

	private double getMaxResolution(ReferencedEnvelope areaOfInterest) {
		double w = areaOfInterest.getWidth();
		double h = areaOfInterest.getHeight();

		return ((w > h) ? w : h) / 256;
	}

    //returns true if this GeoServer instance uses GWC, false otherwise.
    private boolean isGWCAround(){
    	boolean GWCisAround = false;

    	try {
    	  Class.forName("org.geowebcache.GeoWebCacheDispatcher");
    	  GWCisAround = true;
    	} catch (ClassNotFoundException cnfe) {
    	  // guess it's not there.
    	}
    	    	
    	return GWCisAround;
    }
}

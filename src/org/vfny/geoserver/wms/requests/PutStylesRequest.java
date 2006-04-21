package org.vfny.geoserver.wms.requests;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.styling.SLDParser;
import org.geotools.styling.StyledLayerDescriptor;
import org.vfny.geoserver.util.SLDValidator;
import org.vfny.geoserver.wms.WmsException;

public class PutStylesRequest extends WMSRequest {
	
	public static final String INSERT_AND_REPLACE = "InsertAndReplace";
	public static final String REPLACE_ALL = "ReplaceAll";
	
	/** mode of request, either "InsertAndReplace" or "ReplaceAll" */
	String mode = null;
	
	/** parsed sld of the request */
	StyledLayerDescriptor sld;
	
	/**
	 * Creates a new PutStyle object.
     */
    public PutStylesRequest() {
        super();
        setRequest("PutStyle");
    }
    
    public void setMode(String mode) {
    	this.mode = mode;
    }
    
    public String getMode() {
    	return mode;
    }
    
    public void setStyledLayerDescriptor(StyledLayerDescriptor sld) {
    	this.sld = sld;
    }
    
    public StyledLayerDescriptor getStyledLayerDescriptor() {
    	return sld;
    }
}

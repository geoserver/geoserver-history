package org.vfny.geoserver.wms.responses;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;

import org.apache.struts.taglib.tiles.GetAttributeTag;
import org.geotools.feature.FeatureType;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.config.GlobalConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.config.WFSConfig;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.global.ApplicationState;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.TemporaryFeatureTypeInfo;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;
import org.vfny.geoserver.global.xml.XMLConfigWriter;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapKvpReader;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.requests.PutStylesRequest;

public class PutStylesResponse implements Response {

	ServletContext context;
	
	public PutStylesResponse(ServletContext context) {
		this.context = context;
	}
	
	private boolean empty(String s) {
		return s == null || s.trim().equals("");
	}
	
	public void execute(Request req) throws ServiceException {
		PutStylesRequest request = (PutStylesRequest) req;
		
		String mode = request.getMode();
		StyledLayerDescriptor sld = request.getStyledLayerDescriptor();
		String name = sld.getName();
		
		if (name == null) {
			throw new WmsException("<StyledLayerDescriptor> must contain a <Name> element");
		}
		
		//create the style
		File dd = GeoserverDataDirectory.getGeoserverDataDirectory(context);
		File styleDir = null;
		try {
			styleDir = GeoserverDataDirectory.findConfigDir(dd, "styles");
		} 
		catch (ConfigurationException e1) {
			String msg = "Could not locate style directory under " + dd.getAbsolutePath();
			throw new ServiceException(msg,e1);
		}
		
		File sldFile = new File(
			styleDir.getAbsolutePath() + File.separator,name + ".sld"
		);
		
		try {
			Writer writer = new BufferedWriter(new FileWriter(sldFile));
			SLDTransformer transformer = new SLDTransformer();
			
			transformer.transform(sld,writer);
			writer.flush();
			writer.close();
		} 
		catch (Exception e) {
			String msg = "Error writing sld " + sldFile.getAbsolutePath();
			throw new ServiceException(msg,e);
		} 
		
		DataConfig config = 
			(DataConfig)context.getAttribute(DataConfig.CONFIG_KEY);
		
		StyleConfig styleConfig = config.getStyle(name);
		if (styleConfig == null) {
			//create a new style config
			styleConfig = new StyleConfig();
			styleConfig.setId(name);
			styleConfig.setFilename(sldFile);
		
			config.addStyle(name,styleConfig);
		}
		
		//update the feature type configs from the style
		for (int i = 0; i < sld.getStyledLayers().length; i++) {
			StyledLayer sl = (StyledLayer) sld.getStyledLayers()[i];
			if (sl instanceof UserLayer) {
				UserLayer ul = (UserLayer) sl;
				Style[] styles = ul.getUserStyles();
				if (styles == null) 
					continue;
				
				for (int j = 0; j < styles.length; j++) {
					Style style = styles[j];
					FeatureTypeStyle[] ftStyles = style.getFeatureTypeStyles();
					if (ftStyles == null)
						continue;
					
					for (int k = 0; k < ftStyles.length; k++) {
						FeatureTypeStyle ftStyle = (FeatureTypeStyle) ftStyles[k];
						String ftName = ftStyle.getFeatureTypeName();
						if (ftName == null)
							continue;
						
						//check for namespace prefix
						if (ftName.indexOf(":") != -1) {
							ftName = ftName.split(":")[1];
						}
						
						for (Iterator itr = config.getFeaturesTypes().values().iterator(); itr.hasNext();) {
							FeatureTypeConfig ftConfig = (FeatureTypeConfig) itr.next();
							if (ftName.equals(ftConfig.getName()))
								ftConfig.addStyle(name);
							
							
						}
					}
				}
			}
		}
		
		ApplicationState state = 
			(ApplicationState) context.getAttribute(ApplicationState.WEB_CONTAINER_KEY);
		saveConfig(request.getHttpServletRequest(), state);
		
		
	}

	private void saveConfig(HttpServletRequest request, ApplicationState state)
		throws ServiceException {
		
		File dd = GeoserverDataDirectory.getGeoserverDataDirectory(context);
		
		state.notifyConfigChanged();
		
		//first to the update
		try {
			
			WMSDTO wmsDTO = ((WMSConfig) context.getAttribute(WMSConfig.CONFIG_KEY)).toDTO(); 
            WFSDTO wfsDTO = ((WFSConfig) context.getAttribute(WFSConfig.CONFIG_KEY)).toDTO();
            
            GeoServerDTO geoserverDTO = ((GlobalConfig) context.getAttribute(GlobalConfig.CONFIG_KEY)).toDTO();
            DataDTO dataDTO = ((DataConfig) context.getAttribute(DataConfig.CONFIG_KEY)).toDTO();

            Requests.getWFS(request).load(wfsDTO);
            Requests.getWMS(request).load(wmsDTO);
            Requests.getWFS(request).getGeoServer().load(geoserverDTO,context);
            Requests.getWFS(request).getData().load(dataDTO);

            state.notifyToGeoServer();
        } 
		catch (ConfigurationException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
		
		//then the save
		 try {
           XMLConfigWriter.store(
       		(WMSDTO) Requests.getWMS(request).toDTO(),
               (WFSDTO) Requests.getWFS(request).toDTO(),
               (GeoServerDTO)  Requests.getWFS(request).getGeoServer().toDTO(),
               (DataDTO)  Requests.getWFS(request).getData().toDTO(), dd);
           
           state.notifiySaveXML();
		 } 
		 catch (ConfigurationException e) {
           e.printStackTrace();
           throw new ServiceException(e);
       }
	}
	
	public FeatureTypeInfo findLayer(PutStylesRequest request, String layerName)
	    throws WmsException {
	    Data catalog = request.getWMS().getData();
	    FeatureTypeInfo ftype = null;
	
	    try {
	        ftype = catalog.getFeatureTypeInfo(layerName);
	    } catch (NoSuchElementException ex) {
	    	WmsException e = new WmsException(ex,
	            layerName + ": no such layer on this server", "LayerNotDefined");
	    	e.setCode("LayerNotDefined"); //DJB: added this for cite tests
	    	throw e;
	    }
	
	    return ftype;
	}
	
	public String getContentType(GeoServer gs) throws IllegalStateException {
		return "application/vnd.ogc.success+xml";
	}

	public String getContentEncoding() {
//		return "application/vnd.ogc.success+xml"
		return null;
	}

	public void writeTo(OutputStream out) throws ServiceException, IOException {
		
	}

	public void abort(Service gs) {
		// TODO Auto-generated method stub

	}

}

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.action.data;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.coverage.grid.AbstractGridFormat;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.FactoryFinder;
import org.geotools.referencing.factory.epsg.DefaultFactory;
//import org.geotools.referencing.crs.EPSGCRSAuthorityFactory;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataFormatConfig;
import org.vfny.geoserver.form.data.DataFormatsEditorForm;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.util.CoverageUtils;
import org.vfny.geoserver.util.DataFormatUtils;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 */
public class DataFormatsEditorAction extends ConfigAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			UserContainer user, HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		DataFormatsEditorForm dataFormatsForm = (DataFormatsEditorForm) form;
		
		String dataFormatID = dataFormatsForm.getDataFormatId();
        String namespace = dataFormatsForm.getNamespaceId();
        String type = dataFormatsForm.getType();
		String url = dataFormatsForm.getUrl();
		String description = dataFormatsForm.getDescription();
		
		DataConfig dataConfig = (DataConfig) getDataConfig();
		DataFormatConfig config = null;
		
		config = (DataFormatConfig) dataConfig.getDataFormat(dataFormatID);
		
		if( config == null ) {
			// we are creating a new one.
			dataConfig.addDataFormat(getUserContainer(request).getDataFormatConfig());
			config = (DataFormatConfig) dataConfig.getDataFormat(dataFormatID);            
		}
		
		// After extracting params into a map
		Map parameters = new HashMap(); // values used for connection
		Map paramTexts = new HashMap(); // values as stored
		
		Map params = dataFormatsForm.getParams();
		
		Format factory = config.getFactory();
		ParameterValueGroup info = factory.getReadParameters();
		
		// Convert Params into the kind of Map we actually need
		//
		for (Iterator i = params.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			
			ParameterValue param = DataFormatUtils.find(info, key);
			
			if (param == null) {
				
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR,
						new ActionError("error.cannotProcessConnectionParams"));
				saveErrors(request, errors);
				
				return mapping.findForward("config.data.format.editor");
			}
			
			Object value = CoverageUtils.getCvParamValue(key, param, params);
			
			if (value != null) {
				parameters.put(key, value);
				if( key.equalsIgnoreCase("values_palette") ) {
					paramTexts.put(key, value);
				} else {
					String text = value.toString();
					paramTexts.put(key, text);
				}
			}
		}
		
 	try {
			ServletContext sc = request.getSession().getServletContext();
			Map niceParams = DataFormatUtils.getParams(parameters, sc);
			Format victim = factory;
			String[] typeNames = null;
			ParameterValueGroup vicParams = victim.getReadParameters();
			if( niceParams != null && !niceParams.isEmpty() ) {
				typeNames = new String[victim.getReadParameters().values().size()];
				int cnt = 0;
				for( Iterator iT = niceParams.keySet().iterator(); iT.hasNext(); cnt++ ) {
					String key = (String) iT.next();
					Object value = niceParams.get(key);
					vicParams.parameter(key).setValue(value);
					typeNames[cnt] = key;
				}
			}
			
			URL victimUrl = null;
			
//			if(url != null) {
				victimUrl = CoverageUtils.getResource(url, sc.getRealPath("/"));
//			} else {
//		        File rootDir = GeoserverDataDirectory.getGeoserverDataDirectory(getServlet().getServletContext());
//
//		        File coverageDir = new File(rootDir, "data/coverages/" + dataFormatID);
//
//				if (LOGGER.isLoggable(Level.FINE)) {
//					LOGGER.fine(new StringBuffer("new format dir is: ").append(coverageDir.getName()).append(", exists: ").append(coverageDir.exists()).toString());
//				}
//		        
//		        boolean dirAvailable = false;
//		        if(!coverageDir.exists()) {
//		        	if(coverageDir.mkdir()) {
//		        		dirAvailable = true;
//		        	} else {
//						if (LOGGER.isLoggable(Level.FINE)) {
//							LOGGER.fine(new StringBuffer("Cannot create Coverage Format Dir.").toString());
//						}
//						ActionErrors errors = new ActionErrors();
//						errors.add(ActionErrors.GLOBAL_ERROR,
//								new ActionError("error.coveragedir.create", coverageDir.getPath()));
//						saveErrors(request, errors);
//						
//						return mapping.findForward("config.data.format.editor");
//		        	}
//		        } else if(coverageDir.isDirectory()) {
//		        	dirAvailable = true;
//		        } else {
//					if (LOGGER.isLoggable(Level.FINE)) {
//						LOGGER.fine(new StringBuffer("Cannot write into Coverage Format Dir.").toString());
//					}
//					ActionErrors errors = new ActionErrors();
//					errors.add(ActionErrors.GLOBAL_ERROR,
//							new ActionError("error.coveragedir.write", coverageDir.getPath()));
//					saveErrors(request, errors);
//					
//					return mapping.findForward("config.data.format.editor");
//		        }
//		        
//		        if(dirAvailable) {
//		        	formatFile.getFileName();
//		        }
//			}
			
		
			GridCoverageReader reader = ((AbstractGridFormat) victim).getReader(victimUrl);
			
			GridCoverage gc = /*(GridCoverage2D) reader.read(
					vicParams != null ?
					(GeneralParameterValue[]) vicParams.values().toArray(new GeneralParameterValue[vicParams.values().size()])
					: null
					);*/
				reader.read(
						vicParams != null ?
								(GeneralParameterValue[]) vicParams.values().toArray(new GeneralParameterValue[vicParams.values().size()])
								: null
				);
			
			if (gc == null) {
				// We *really* could not use these params!
				//
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR,
						new ActionError("error.invalidConnectionParams"));
				saveErrors(request, errors);
				
				return mapping.findForward("config.data.format.editor");
			}
			dump( "typeNames", typeNames );
		} catch (Throwable throwable) {
			
			ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_ERROR,
					new ActionError("error.exception", throwable.getMessage()));
			
			saveErrors(request, errors);
			
			return mapping.findForward("config.data.format.editor");
		}
		
		boolean enabled = dataFormatsForm.isEnabled();
		
		if (dataFormatsForm.isEnabledChecked() == false) {
			enabled = false;
		}
		
		config.setEnabled(enabled);
		config.setNameSpaceId(namespace);
		config.setType(type);
		config.setUrl(url);
		config.setAbstract(description);
		config.setParameters(paramTexts);
		
		dataConfig.addDataFormat(config);
		
		getUserContainer(request).setDataFormatConfig(null);
		getApplicationState().notifyConfigChanged();
		
		return mapping.findForward("config.data.format");
	}
	/** Used to debug connection parameters */
	public void dump( String msg, Map params ){
		if( msg != null ){
			System.out.print( msg + " ");
		}
		System.out.print( ": { " );
		for( Iterator i=params.entrySet().iterator(); i.hasNext();){
			Map.Entry entry = (Map.Entry) i.next();
			System.out.print( entry.getKey() );
			System.out.print("=");
			dump( entry.getValue() );
			if( i.hasNext() ){
				System.out.print( ", " );
			}	
		}
		System.out.println( "}" );
	}
	public void dump( Object obj ){
		if( obj==null){
			System.out.print("null");
		}
		else if ( obj instanceof String){
			System.out.print("\"");
			System.out.print( obj );
			System.out.print("\"");
		}
		else {
			System.out.print( obj );
		}
		
	}
	public void dump( String msg, Object array[] ){
		if( msg != null ){
			System.out.print( msg + " ");
		}
		System.out.print( ": " );
		if( array == null){
			System.out.print( "null" );
			return;
		}
		System.out.print( "(" );
		for( int i=0; i<array.length; i++){
			dump( array[i] );    	
			if( i<array.length-1 ){
				System.out.print( ", " );
			}
		}	
		System.out.println( ")" );
	}
}

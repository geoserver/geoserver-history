/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.action.data;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.coverage.grid.stream.StreamGridCoverageExchange;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.FactoryFinder;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageExchange;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataFormatConfig;
import org.vfny.geoserver.form.data.DataFormatsEditorForm;
import org.vfny.geoserver.global.UserContainer;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class DataFormatsEditorAction extends ConfigAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			UserContainer user, HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		DataFormatsEditorForm dataFormatsForm = (DataFormatsEditorForm) form;
		
		String dataFormatID = dataFormatsForm.getDataFormatId();
		//String namespace = dataFormatsForm.getNamespaceId();
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
			
			Object value = null;
			
			try {
				if( key.equalsIgnoreCase("crs") ) {
					if( params.get(key) != null && ((String) params.get(key)).length() > 0 ) {
						CRSFactory crsFactory = FactoryFinder.getCRSFactory();
						CoordinateReferenceSystem crs = crsFactory.createFromWKT((String) params.get(key));
						value = crs;
					} else {
						CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
						value = crs;
					}
				} else if( key.equalsIgnoreCase("envelope") ) {
					if( params.get(key) != null && ((String) params.get(key)).length() > 0 ) {
						String tmp = (String) params.get(key);
						if( tmp.indexOf("[") > 0 && tmp.indexOf("]") > tmp.indexOf("[") ) {
							tmp = tmp.substring(tmp.indexOf("[") + 1, tmp.indexOf("]")).trim();
							tmp = tmp.replaceAll(",","");
							String[] strCoords = tmp.split(" ");
							double[] coords = new double[strCoords.length];
							if( strCoords.length == 4 ) {
								for( int iT=0; iT<4; iT++) {
									coords[iT] = Double.parseDouble(strCoords[iT].trim());
								}
								
								value = (org.opengis.spatialschema.geometry.Envelope) 
										new GeneralEnvelope(
											new double[] {coords[0], coords[1]},
											new double[] {coords[2], coords[3]}
										);
							}
						}
					}
				} else {
					Class[] clArray = {String.class};
					Object[] inArray = {params.get(key)};
					value = param.getValue().getClass().getConstructor(clArray).newInstance(inArray);
				}
			} catch (Exception e) {
				value = param.getValue();
			}
			
			if (value != null) {
				parameters.put(key, value);
				
				String text = value.toString();
				paramTexts.put(key, text);
			}
		}
		
		// put magic namespace into the mix
		// not sure if we want to do this, as we want the full namespace, not
		//the id.  But getParams in DataStore may override this - ch
		//        parameters.put("namespace", dataFormatsForm.getNamespaceId());
		//        paramTexts.put("namespace", dataFormatsForm.getNamespaceId());
		
		//        //dump("editor", connectionParams );
		//        //dump("texts ",paramTexts );        
		//        if (!factory.canProcess(parameters)) {
		//            // We could not use these params!
		//            //
		//            ActionErrors errors = new ActionErrors();
		//            errors.add(ActionErrors.GLOBAL_ERROR,
		//                new ActionError("error.cannotProcessConnectionParams"));
		//            saveErrors(request, errors);
		//
		//            return mapping.findForward("config.data.store.editor");
		//        }
		
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
			
			URL victimUrl = getResource(url, sc.getRealPath("/"));
			GridCoverageExchange gce = new StreamGridCoverageExchange();
			GridCoverageReader reader = gce.getReader(victimUrl);
			
			GridCoverage2D gc = (GridCoverage2D) reader.read(
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
			throwable.printStackTrace();
			
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
		//config.setNameSpaceId(namespace);
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
	
	private URL getResource(String path, String baseDir) throws MalformedURLException{
		URL url = null;
		if (path.startsWith("file:data/")) {
			path = path.substring(5); // remove 'file:' prefix
			
			File file = new File(baseDir, path);
			url = file.toURL();
		} else {
			url = new URL(path);
		}
		
		return url;
	}
	
}

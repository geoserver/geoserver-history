/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.data;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.coverage.grid.stream.StreamGridCoverageExchange;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.FactoryFinder;
import org.geotools.referencing.crs.GeographicCRS;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageExchange;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.action.HTMLEncoder;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataFormatConfig;
import org.vfny.geoserver.form.data.CoveragesEditorForm;
import org.vfny.geoserver.global.MetaDataLink;
import org.vfny.geoserver.global.UserContainer;

import com.vividsolutions.jts.geom.Envelope;


/**
 * These Action handles all the buttons for the Coverage Editor.
 * 
 * <p>
 * This one is more complicated then usual since not all the actions require
 * the form bean to be validated! I am going to have to hack a little bit to
 * make that happen, I may end up making the form bean validation differ
 * depending on the selected action.
 * </p>
 * 
 * <p>
 * Buttons that make this action go:
 * 
 * <ul>
 * <li>
 * Submit: update the CoverageConfig held by the user, punt it back into
 * DataConfig and return to the CoverageSelect screen.
 * </li>
 * <li>
 * Up and Down (for each attribute): not quite sure how to make these work yet
 * - I hope I dont have to give them different names.
 * </li>
 * </ul>
 * 
 * As usual we will have to uninternationlize the action name provided to us.
 * </p>
 *
 * @author Richard Gould
 * @author Jody Garnett
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class CoveragesEditorAction extends ConfigAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			UserContainer user, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		LOGGER.finer("form bean:" + form.getClass().getName());
		
		CoveragesEditorForm coverageForm = (CoveragesEditorForm) form;
		
		String action = coverageForm.getAction();
		LOGGER.finer("CoveragesEditorAction is " + action);
		
		String newCoverage = coverageForm.getNewCoverage();
		LOGGER.finer("CoveragesEditorNew is " + newCoverage);
		
		Locale locale = (Locale) request.getLocale();
		MessageResources messages = servlet.getResources();
		final String SUBMIT = HTMLEncoder.decode(messages.getMessage(locale,
		"label.submit"));
		final String ADD = HTMLEncoder.decode(messages.getMessage(locale,
		"label.add"));
		final String ENVELOPE = HTMLEncoder.decode(messages.getMessage(locale,
		"config.data.calculateBoundingBox.label"));
		LOGGER.finer("ENVELOPE: " + ENVELOPE);
		
		if (action.equals(SUBMIT)) {
			return executeSubmit(mapping, coverageForm, user, request);
		}

		if( newCoverage != null && newCoverage.equals("true") ) {
			LOGGER.finer("NEW COVERAGE: " + newCoverage);
			request.setAttribute(DataCoveragesNewAction.NEW_COVERAGE_KEY, "true");
		}
		
		if (action.equals(ENVELOPE)) {
			return executeEnvelope(mapping, coverageForm, user, request);
		}
		
		// Update, Up, Down, Add, Remove need to resync
		sync(coverageForm, user.getCoverageConfig(), request);
		form.reset(mapping, request);

		return mapping.findForward("config.data.coverage.editor");
	}
	
	/**
	 * Populate the bounding box fields from the source and pass control back
	 * to the UI
	 *
	 * @param mapping DOCUMENT ME!
	 * @param coverageForm DOCUMENT ME!
	 * @param user DOCUMENT ME!
	 * @param request DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws IOException DOCUMENT ME!
	 * @throws ServletException DOCUMENT ME!
	 */
	private ActionForward executeEnvelope(ActionMapping mapping,
			CoveragesEditorForm coverageForm, UserContainer user, HttpServletRequest request)
	throws IOException, ServletException {
		DataConfig dataConfig = getDataConfig();
		DataFormatConfig dfConfig = dataConfig.getDataFormat(coverageForm.getFormatId());
		
		GridCoverage2D gc = null;
		
		try {
			ServletContext sc = getServlet().getServletContext();
			URL url = getResource(dfConfig.getUrl(), sc.getRealPath("/"));
			GridCoverageExchange gce = new StreamGridCoverageExchange();
			GridCoverageReader reader = gce.getReader(url);
			Format format = reader.getFormat();
			ParameterValueGroup params = format.getReadParameters();
			
			if( params != null ) {
				List list=params.values();
				Iterator it=list.iterator();
				while(it.hasNext())
				{
					ParameterValue param=((ParameterValue)it.next());
					ParameterDescriptor descr=(ParameterDescriptor)param.getDescriptor();
					
					Object value = null;
					String key = descr.getName().toString();
					
					try {
						if( key.equalsIgnoreCase("crs") ) {
							if( dfConfig.getParameters().get(key) != null && ((String) dfConfig.getParameters().get(key)).length() > 0 ) {
								CRSFactory crsFactory = FactoryFinder.getCRSFactory();
								CoordinateReferenceSystem crs = crsFactory.createFromWKT((String) dfConfig.getParameters().get(key));
								value = crs;
							} else {
								CoordinateReferenceSystem crs = GeographicCRS.WGS84;
								value = crs;
							}
						} else if( key.equalsIgnoreCase("envelope") ) {
							
						} else {
							Class[] clArray = {String.class};
							Object[] inArray = {dfConfig.getParameters().get(key)};
							value = param.getValue().getClass().getConstructor(clArray).newInstance(inArray);
						}
					} catch (Exception e) {
						value = null;
					}
					
					if( value != null )
						params.parameter(key).setValue(value);
				}
			}
			
			gc = (GridCoverage2D) reader.read(
					params != null ?
					(GeneralParameterValue[]) params.values().toArray(new GeneralParameterValue[params.values().size()])
					: null
					);
		} catch (InvalidParameterValueException e) {
			throw new ServletException(e);
		} catch (ParameterNotFoundException e) {
			throw new ServletException(e);
		} catch (MalformedURLException e) {
			throw new ServletException(e);
		} catch (IllegalArgumentException e) {
			throw new ServletException(e);
		} catch (SecurityException e) {
			throw new ServletException(e);
		} catch (IOException e) {
			throw new ServletException(e);
		}
		
		Envelope envelope = new Envelope();
		GeneralEnvelope gEnvelope=(GeneralEnvelope)gc.getEnvelope();
		envelope.init(gEnvelope.getLowerCorner().getOrdinate(0),
				gEnvelope.getUpperCorner().getOrdinate(0),
				gEnvelope.getLowerCorner().getOrdinate(1),
				gEnvelope.getUpperCorner().getOrdinate(1));
		
		coverageForm.setMinX(Double.toString(envelope.getMinX()));
		coverageForm.setMaxX(Double.toString(envelope.getMaxX()));
		coverageForm.setMinY(Double.toString(envelope.getMinY()));
		coverageForm.setMaxY(Double.toString(envelope.getMaxY()));
		
		return mapping.findForward("config.data.coverage.editor");
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
	
	/**
	 * Sync generated attributes with schemaBase.
	 *
	 * @param form
	 * @param config
	 */
	private void sync(CoveragesEditorForm form, CoverageConfig config, 
			HttpServletRequest request) {
		config.setDefaultInterpolationMethod(form.getDefaultInterpolationMethod());
		config.setDescription(form.getDescription());
		config.setEnvelope(getEnvelope(form));
		config.setInterpolationMethods(interpolationMethods(form));
		config.setKeywords(keyWords(form));
		config.setLabel(form.getLabel());
		config.setMetadataLink(metadataLink(form));
		config.setNativeFormat(form.getNativeFormat());
		config.setRequestCRSs(requestCRSs(form));
		config.setResponseCRSs(responseCRSs(form));
		//*A* config.setCrs(form.get);
		config.setSrsName(form.getSrsName());
		config.setSupportedFormats(supportedFormats(form));

		config.setName(form.getName());
		config.setDirName(config.getFormatId() + "_" + form.getName());
	}
	
	/**
	 * Execute Submit Action.
	 *
	 * @param mapping
	 * @param form
	 * @param user
	 * @param request
	 *
	 * @return
	 */
		private ActionForward executeSubmit(ActionMapping mapping,
				CoveragesEditorForm form, UserContainer user, HttpServletRequest request) {
			CoverageConfig config = user.getCoverageConfig();
			sync(form, config, request);
			
			DataConfig dataConfig = (DataConfig) getDataConfig();
			dataConfig.addCoverage(config.getFormatId() + ":"
					+ config.getName(), config);
			
			// Don't think reset is needed (as me have moved on to new page)
			// form.reset(mapping, request);
			getApplicationState().notifyConfigChanged();
			
			// Coverage no longer selected
			user.setCoverageConfig(null);
			
			return mapping.findForward("config.data.coverage");
		}
	
	/**
	 * DOCUMENT ME!
	 *
	 * @param coverageForm
	 *
	 * @return Bounding box in lat long
	 */
	private Envelope getEnvelope(CoveragesEditorForm coverageForm) {
		return new Envelope(
				Double.parseDouble(coverageForm.getMinX()),
				Double.parseDouble(coverageForm.getMaxX()),
				Double.parseDouble(coverageForm.getMinY()),
				Double.parseDouble(coverageForm.getMaxY())
		);
	}
	
	private MetaDataLink metadataLink(CoveragesEditorForm coverageForm) {
		MetaDataLink ml = new MetaDataLink();
		
		if( coverageForm.getMetadataLink() != null && coverageForm.getMetadataLink().length() > 0 ) {
			ml.setAbout(coverageForm.getMetadataLink());
			ml.setMetadataType("other");
		} else {
			ml = null;
		}
		
		return ml;
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @param coverageForm
	 *
	 * @return Set of keywords
	 */
	private List keyWords(CoveragesEditorForm coverageForm) {
		LinkedList keywords = new LinkedList();
		String[] array = (coverageForm.getKeywords() != null)
		? coverageForm.getKeywords().split(" ")
				: new String[0];
		
		for (int i = 0; i < array.length; i++) {
			keywords.add(array[i]);
		}
		
		return keywords;
	}
	
	private List interpolationMethods(CoveragesEditorForm coverageForm) {
		LinkedList interpolationMethods = new LinkedList();
		String[] array = (coverageForm.getInterpolationMethods() != null)
		? coverageForm.getInterpolationMethods().split(",")
				: new String[0];
		
		for (int i = 0; i < array.length; i++) {
			interpolationMethods.add(array[i]);
		}
		
		return interpolationMethods;
	}
	
	private List requestCRSs(CoveragesEditorForm coverageForm) {
		LinkedList requestCRSs = new LinkedList();
		String[] array = (coverageForm.getRequestCRSs() != null)
		? coverageForm.getRequestCRSs().split(",")
				: new String[0];
		
		for (int i = 0; i < array.length; i++) {
			requestCRSs.add(array[i]);
		}
		
		return requestCRSs;
	}
	
	private List responseCRSs(CoveragesEditorForm coverageForm) {
		LinkedList responseCRSs = new LinkedList();
		String[] array = (coverageForm.getResponseCRSs() != null)
		? coverageForm.getResponseCRSs().split(",")
				: new String[0];
		
		for (int i = 0; i < array.length; i++) {
			responseCRSs.add(array[i]);
		}
		
		return responseCRSs;
	}
	
	private List supportedFormats(CoveragesEditorForm coverageForm) {
		LinkedList supportedFormats = new LinkedList();
		String[] array = (coverageForm.getSupportedFormats() != null)
		? coverageForm.getSupportedFormats().split(",")
				: new String[0];
		
		for (int i = 0; i < array.length; i++) {
			supportedFormats.add(array[i]);
		}
		
		return supportedFormats;
	}
}

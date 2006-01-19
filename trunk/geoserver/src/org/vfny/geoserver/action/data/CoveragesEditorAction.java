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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.coverage.grid.AbstractGridFormat;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.JTS;
import org.geotools.referencing.FactoryFinder;
import org.geotools.referencing.factory.epsg.DefaultFactory;
import org.geotools.resources.CRSUtilities;
//import org.geotools.referencing.crs.EPSGCRSAuthorityFactory;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.OperationNotFoundException;
import org.opengis.referencing.operation.TransformException;
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
 * Buttons that make this action go:
 * 
 * <ul>
 * <li>
 * Submit: update the CoverageConfig held by the user, punt it back into
 * DataConfig and return to the CoverageSelect screen.
 * </li>
 * </ul>
 * 
 * As usual we will have to uninternationlize the action name provided to us.
 * </p>
 *
 * @author Richard Gould
 * @author Jody Garnett
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
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
			final ServletContext sc = getServlet().getServletContext();
			final URL url = getResource(dfConfig.getUrl(), sc.getRealPath("/"));
			
//			GridCoverageExchange gce = new StreamGridCoverageExchange();
//			GridCoverageReader reader = gce.getReader(url);
//			Format format = reader.getFormat();

			final Format format = dfConfig.getFactory();
			final GridCoverageReader reader = ((AbstractGridFormat) format).getReader(url);

			final ParameterValueGroup params = format.getReadParameters();
			
			if( params != null ) {
				final List list=params.values();
				final Iterator it=list.iterator();
				while(it.hasNext())
				{
					final ParameterValue param=((ParameterValue)it.next());
					final ParameterDescriptor descr=(ParameterDescriptor)param.getDescriptor();
					
					Object value = null;
					final String key = descr.getName().toString();
					
					try {
						if( key.equalsIgnoreCase("crs") ) {
							if( dfConfig.getParameters().get(key) != null && ((String) dfConfig.getParameters().get(key)).length() > 0 ) {
								//CRSFactory crsFactory = FactoryFinder.getCRSFactory(new Hints(Hints.CRS_AUTHORITY_FACTORY,EPSGCRSAuthorityFactory.class));
								CRSFactory crsFactory = FactoryFinder.getCRSFactory(new Hints(Hints.CRS_AUTHORITY_FACTORY,CRSAuthorityFactory.class));
								CoordinateReferenceSystem crs = crsFactory.createFromWKT((String) dfConfig.getParameters().get(key));
								value = crs;
							} else {
								//CRSAuthorityFactory crsFactory=FactoryFinder.getCRSAuthorityFactory("EPSG",new Hints(Hints.CRS_AUTHORITY_FACTORY,EPSGCRSAuthorityFactory.class));
								CRSAuthorityFactory crsFactory=FactoryFinder.getCRSAuthorityFactory("EPSG", new Hints(Hints.CRS_AUTHORITY_FACTORY, CRSAuthorityFactory.class));
								CoordinateReferenceSystem crs=(CoordinateReferenceSystem) crsFactory.createCoordinateReferenceSystem("EPSG:4326");
								value = crs;
							}
						} else if( key.equalsIgnoreCase("envelope") ) {
							if( dfConfig.getParameters().get(key) != null && ((String) dfConfig.getParameters().get(key)).length() > 0 ) {
								String tmp = (String) dfConfig.getParameters().get(key);
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
						} else if( key.equalsIgnoreCase("values_palette") ) {
							if( dfConfig.getParameters().get(key) != null && ((String) dfConfig.getParameters().get(key)).length() > 0 ) {
								String tmp = (String) dfConfig.getParameters().get(key);
								String[] strColors = tmp.split(";");
								Vector colors = new Vector();
								for( int i=0; i<strColors.length; i++) {
									if(Color.decode(strColors[i]) != null) {
										colors.add(Color.decode(strColors[i]));
									}
								}
								
								value = colors.toArray(new Color[colors.size()]);
							} else {
								value = "#000000;#3C3C3C;#FFFFFF";
							}
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

		try {
			final CRSAuthorityFactory crsFactory = FactoryFinder.getCRSAuthorityFactory("EPSG", new Hints(Hints.CRS_AUTHORITY_FACTORY, CRSAuthorityFactory.class));
			final CoordinateOperationFactory opFactory = FactoryFinder.getCoordinateOperationFactory(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));
			final CoordinateReferenceSystem targetCRS = crsFactory.createCoordinateReferenceSystem("EPSG:4326");
			final CoordinateReferenceSystem sourceCRS = gc.getEnvelope2D().getCoordinateReferenceSystem();
			final CoordinateOperation operation = opFactory.createOperation(sourceCRS, targetCRS);
			MathTransform mathTransform = (MathTransform) operation.getMathTransform();
			GeneralEnvelope gEnvelope=(GeneralEnvelope)gc.getEnvelope();
			GeneralEnvelope targetEnvelope = null;
			if( !mathTransform.isIdentity() )
				targetEnvelope = CRSUtilities.transform(mathTransform, gEnvelope);
			else
				targetEnvelope = gEnvelope;

	    	final CoordinateSystem cs = targetCRS.getCoordinateSystem();
	    	boolean lonFirst = true;
	    	if (cs.getAxis(0).getDirection().absolute().equals(AxisDirection.NORTH)) {
	    		lonFirst = false;
	    	}
	    	boolean swapXY = !lonFirst;

	    	// latitude index
	        final int latIndex = lonFirst ? 1 : 0;

	        final AxisDirection latitude = cs.getAxis(latIndex).getDirection();
	        final AxisDirection longitude = cs.getAxis((latIndex + 1) % 2).getDirection();
	        final boolean[] reverse = new boolean[] {
	        		lonFirst ? !longitude.equals(AxisDirection.EAST) : !latitude.equals(AxisDirection.NORTH), 
	        		lonFirst ? !latitude.equals(AxisDirection.NORTH) : !longitude.equals(AxisDirection.EAST)
			};

		    Envelope envelope = new Envelope();
			envelope.init(
					reverse[(latIndex + 1) % 2] ? targetEnvelope.getUpperCorner().getOrdinate(swapXY ? 1 : 0) : targetEnvelope.getLowerCorner().getOrdinate(swapXY ? 1 : 0),
					reverse[(latIndex + 1) % 2] ? targetEnvelope.getLowerCorner().getOrdinate(swapXY ? 1 : 0) : targetEnvelope.getUpperCorner().getOrdinate(swapXY ? 1 : 0),
					reverse[latIndex] ? targetEnvelope.getUpperCorner().getOrdinate(swapXY ? 0 : 1) : targetEnvelope.getLowerCorner().getOrdinate(swapXY ? 0 : 1),
					reverse[latIndex] ? targetEnvelope.getLowerCorner().getOrdinate(swapXY ? 0 : 1) : targetEnvelope.getUpperCorner().getOrdinate(swapXY ? 0 : 1));

			if(!targetCRS.getIdentifiers().isEmpty()) {
				coverageForm.setSrsName(targetCRS.getIdentifiers().toArray()[0].toString());				
			} else {
				coverageForm.setSrsName(targetCRS.getName().toString());
			}
			coverageForm.setMinX(Double.toString(envelope.getMinX()));
			coverageForm.setMaxX(Double.toString(envelope.getMaxX()));
			coverageForm.setMinY(Double.toString(envelope.getMinY()));
			coverageForm.setMaxY(Double.toString(envelope.getMaxY()));
		} catch (FactoryRegistryException e) {
			throw new ServletException(e);
		} catch (OperationNotFoundException e) {
			throw new ServletException(e);
		} catch (FactoryException e) {
			throw new ServletException(e);
		} catch (TransformException e) {
			throw new ServletException(e);
		}

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

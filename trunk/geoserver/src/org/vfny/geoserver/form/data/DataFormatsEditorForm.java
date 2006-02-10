/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ControllerConfig;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.FactoryFinder;
import org.geotools.referencing.factory.epsg.DefaultFactory;
// import org.geotools.referencing.crs.EPSGCRSAuthorityFactory;
import org.opengis.coverage.grid.Format;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataFormatConfig;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.util.CoverageUtils;
import org.vfny.geoserver.util.DataFormatUtils;
import org.vfny.geoserver.util.Requests;

/**
 * Represents the information required for editing a DataFormat.
 * 
 * <p>
 * The parameters required by a DataFormat are dynamically generated from the
 * DataFormatFactorySPI. Most use of DataFormatFactorySPI has been hidden behind
 * the DataStoreUtil class.
 * </p>
 * 
 * @author Richard Gould, Refractions Research
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public class DataFormatsEditorForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8469919940722502675L;

	/**
	 * Help text for Params if available
	 */
	private ArrayList paramHelp;

	/**
	 * Used to identify the Format being edited. Maybe we should grab this from
	 * session?
	 */
	private String dataFormatId;

	/**
	 * Enabled status of Format
	 */
	private boolean enabled;

	/**
	 * 
	 */
	private String type;

	/**
	 * 
	 */
	private String url;

	/**
	 * 
	 */
	private FormFile urlFile = null;

	/**
	 * 
	 */
	/* Description of Format (abstract?) */
	private String description;

	// These are not stored in a single map so we can access them
	// easily from JSP page
	//

	/**
	 * String representation of connection parameter keys
	 */
	private List paramKeys;

	/**
	 * String representation of connection parameter values
	 */
	private List paramValues;

	/**
	 * Because of the way that STRUTS works, if the user does not check the
	 * enabled box, or unchecks it, setEnabled() is never called, thus we must
	 * monitor setEnabled() to see if it doesn't get called. This must be
	 * accessible, as ActionForms need to know about it -- there is no way we
	 * can tell whether we are about to be passed to an ActionForm or not.
	 * Probably a better way to do this, but I can't think of one. -rgould
	 */
	private boolean enabledChecked = false;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);

		enabledChecked = false;
		DataFormatConfig dfConfig = Requests.getUserContainer(request)
				.getDataFormatConfig();

		if (dfConfig == null) {
			// something is horribly wrong no FormatID selected!
			// The JSP needs to not include us if there is no
			// selected Format
			//
			throw new RuntimeException(
					"selectedDataFormatId required in Session");
		}

		dataFormatId = dfConfig.getId();
		description = dfConfig.getAbstract();
		enabled = dfConfig.isEnabled();
		url = dfConfig.getUrl();

		// Retrieve connection params
		Format factory = dfConfig.getFactory();
		type = (dfConfig.getType() != null && dfConfig.getType().length() > 0 ? dfConfig
				.getType()
				: factory.getName());
		ParameterValueGroup params = factory.getReadParameters();

		if (params != null && params.values().size() > 0) {
			paramKeys = new ArrayList(params.values().size());
			paramValues = new ArrayList(params.values().size());
			paramHelp = new ArrayList(params.values().size());

			List list = params.values();
			Iterator it = list.iterator();
			ParameterDescriptor descr = null;
			ParameterValue val = null;
			while (it.hasNext()) {
				val = (ParameterValue) it.next();
				if (val != null) {
					descr = (ParameterDescriptor) val.getDescriptor();
					String key = descr.getName().toString();

					if ("namespace".equals(key)) {
						// skip namespace as it is *magic* and
						// appears to be an entry used in all dataformats?
						//
						continue;
					}

					Object value = dfConfig.getParameters().get(key);
					String text = "";

					if ("values_palette".equals(key)) {
						Object palVal = value;
						if (palVal instanceof Color[]) {
							for (int i = 0; i < ((Color[]) palVal).length; i++) {
								String colString = "#"
										+ (Integer.toHexString(
												((Color) ((Color[]) palVal)[i])
														.getRed()).length() > 1 ? Integer
												.toHexString(((Color) ((Color[]) palVal)[i])
														.getRed())
												: "0"
														+ Integer
																.toHexString(((Color) ((Color[]) palVal)[i])
																		.getRed()))
										+ (Integer.toHexString(
												((Color) ((Color[]) palVal)[i])
														.getGreen()).length() > 1 ? Integer
												.toHexString(((Color) ((Color[]) palVal)[i])
														.getGreen())
												: "0"
														+ Integer
																.toHexString(((Color) ((Color[]) palVal)[i])
																		.getGreen()))
										+ (Integer.toHexString(
												((Color) ((Color[]) palVal)[i])
														.getBlue()).length() > 1 ? Integer
												.toHexString(((Color) ((Color[]) palVal)[i])
														.getBlue())
												: "0"
														+ Integer
																.toHexString(((Color) ((Color[]) palVal)[i])
																		.getBlue()));
								text += (i > 0 ? ";" : "") + colString;
							}
						} else if (palVal instanceof String) {
							text = (String) palVal;
						}
					} else {
						if (value == null) {
							text = null;
						} else if (value instanceof String) {
							text = (String) value;
						} else {
							text = value.toString();
						}
					}

					paramKeys.add(key);
					paramValues.add((text != null) ? text : "");
					paramHelp.add(key);
				}
			}
		}
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// if (this.getUrlFile().getFileSize()==0) {// filename not filed or
		// file does not exist
		// errors.add("Format", new ActionError("error.file.required")) ;
		// return errors;
		// }
		// Requests.getApplicationState(request);

		// Selected DataFormatConfig is in session
		//
		UserContainer user = Requests.getUserContainer(request);
		DataFormatConfig dfConfig = user.getDataFormatConfig();
		//
		// dsConfig is the only way to get a factory
		Format factory = dfConfig.getFactory();
		ParameterValueGroup info = factory.getReadParameters();

		Map connectionParams = new HashMap();

		// Convert Params into the kind of Map we actually need
		//
		if (paramKeys != null) {
			for (int i = 0; i < paramKeys.size(); i++) {
				String key = (String) getParamKey(i);

				ParameterValue param = DataFormatUtils.find(info, key);

				if (param == null) {
					errors.add("paramValue[" + i + "]", new ActionError(
							"error.dataFormatEditor.param.missing", key,
							factory.getDescription()));

					continue;
				}

				Boolean maxSize = (Boolean) request
						.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
				if ((maxSize != null) && (maxSize.booleanValue())) {
					String size = null;
					ControllerConfig cc = mapping.getModuleConfig()
							.getControllerConfig();
					if (cc == null) {
						size = Long
								.toString(CommonsMultipartRequestHandler.DEFAULT_SIZE_MAX);
					} else {
						size = cc.getMaxFileSize();// struts-config :
													// <controller
													// maxFileSize="nK" />
					}
					errors.add("styleID", new ActionError(
							"error.file.maxLengthExceeded", size));
					return errors;
				}

				Object value = CoverageUtils.getCvParamValue(key, param,
						paramValues, i);

				// if ((value == null) && param.required) {
				// errors.add("paramValue[" + i + "]",
				// new ActionError("error.dataStoreEditor.param.required",
				// key));
				//
				// continue;
				// }

				if (value != null) {
					connectionParams.put(key, value);
				}
			}
		}

		dump("form", connectionParams);
		// Factory will provide even more stringent checking
		//
		// if (!factory.canProcess( connectionParams )) {
		// errors.add("paramValue",
		// new ActionError("error.datastoreEditor.validation"));
		// }

		return errors;
	}

	/** Used to debug connection parameters */
	public void dump(String msg, Map params) {
		if (msg != null) {
			System.out.print(msg + " ");
		}
		System.out.print(" connection params { ");
		for (Iterator i = params.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			System.out.print(entry.getKey());
			System.out.print("=");
			if (entry.getValue() == null) {
				System.out.print("null");
			} else if (entry.getValue() instanceof String) {
				System.out.print("\"");
				System.out.print(entry.getValue());
				System.out.print("\"");
			} else {
				System.out.print(entry.getValue());
			}
			if (i.hasNext()) {
				System.out.print(", ");
			}
		}
		System.out.println("}");
	}

	public Map getParams() {
		Map map = new HashMap();

		if (paramKeys != null) {
			for (int i = 0; i < paramKeys.size(); i++) {
				map.put(paramKeys.get(i), paramValues.get(i));

			}
		}

		return map;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return
	 */
	public List getParamKeys() {
		return paramKeys;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param index
	 *            DOCUMENT ME!
	 * 
	 * @return
	 */
	public String getParamKey(int index) {
		return (String) paramKeys.get(index);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param index
	 *            DOCUMENT ME!
	 * 
	 * @return
	 */
	public String getParamValue(int index) {
		return (String) paramValues.get(index);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param index
	 * @param value
	 *            DOCUMENT ME!
	 */
	public void setParamValues(int index, String value) {
		paramValues.set(index, value);
	}

	/**
	 * getDataStoreId purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 */
	public String getDataFormatId() {
		return dataFormatId;
	}

	/**
	 * getDescription purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * isEnabled purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * getParamValues purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 */
	public List getParamValues() {
		return paramValues;
	}

	/**
	 * setDescription purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * setEnabled purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param b
	 */
	public void setEnabled(boolean b) {
		setEnabledChecked(true);
		enabled = b;
	}

	/**
	 * setParamKeys purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param list
	 */
	public void setParamKeys(List list) {
		paramKeys = list;
	}

	/**
	 * setParamValues purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param list
	 */
	public void setParamValues(List list) {
		paramValues = list;
	}

	/**
	 * enabledChecked property
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isEnabledChecked() {
		return enabledChecked;
	}

	/**
	 * enabledChecked property
	 * 
	 * @param b
	 *            DOCUMENT ME!
	 */
	public void setEnabledChecked(boolean b) {
		enabledChecked = b;
	}

	/**
	 * Index property paramHelp
	 * 
	 * @return DOCUMENT ME!
	 */
	public String[] getParamHelp() {
		return (String[]) paramHelp.toArray(new String[paramHelp.size()]);
	}

	/**
	 * Index property paramHelp
	 * 
	 * @param index
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getParamHelp(int index) {
		return (String) paramHelp.get(index);
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 */
	public FormFile getUrlFile() {
		return this.urlFile;
	}

	/**
	 * 
	 */
	public void setUrlFile(FormFile filename) {
		this.urlFile = filename;
	}

}
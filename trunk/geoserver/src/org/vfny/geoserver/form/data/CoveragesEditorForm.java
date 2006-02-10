/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.form.data;

import java.util.Iterator;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.vfny.geoserver.action.HTMLEncoder;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.util.Requests;

import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public class CoveragesEditorForm extends ActionForm {

	/**
	 * 
	 */
	private String formatId;

	/**
	 * Identify Style used to render this feature type
	 */
	private String styleId;

	/**
	 * 
	 */
	private String name;

	/**
	 * 
	 */
	private String label;

	/**
	 * 
	 */
	private String description;

	/**
	 * 
	 */
	private String metadataLink;

	private String latLonBoundingBoxMinX;

	private String latLonBoundingBoxMinY;

	private String latLonBoundingBoxMaxX;

	private String latLonBoundingBoxMaxY;

	/**
	 * 
	 */
	private String keywords;

	/**
	 * Sorted Set of available styles
	 */
	private SortedSet styles;

	/**
	 * 
	 */
	private String srsName;

	/**
	 * 
	 */
	private String WKTString;

	/**
	 * 
	 */
	private String requestCRSs;

	/**
	 * 
	 */
	private String responseCRSs;

	/**
	 * 
	 */
	private String nativeFormat;

	/**
	 * 
	 */
	private String supportedFormats;

	/**
	 * 
	 */
	private String defaultInterpolationMethod;

	/**
	 * 
	 */
	private String interpolationMethods;

	/**
	 * Action requested by user
	 */
	private String action;

	/**
	 * 
	 */
	private String newCoverage;

	/**
	 * Set up CoverageEditor from from Web Container.
	 * 
	 * <p>
	 * The key DataConfig.SELECTED_COVERAGE is used to look up the selected from
	 * the web container.
	 * </p>
	 * 
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);

		action = "";
		newCoverage = "";
		final DataConfig config = ConfigRequests.getDataConfig(request);
		final UserContainer user = Requests.getUserContainer(request);
		final CoverageConfig type = user.getCoverageConfig();

		if (type == null) {

			// TODO Not sure what to do, user must have bookmarked?
			return; // Action should redirect to Select screen?
		}

		this.formatId = type.getFormatId();
		this.styleId = type.getDefaultStyle();

		final CoverageConfig cvConfig= (CoverageConfig) request.getSession().getAttribute(
				DataConfig.SELECTED_COVERAGE);
		final Envelope bounds = cvConfig.getEnvelope();
		srsName = cvConfig.getSrsName();
		WKTString = cvConfig.getSrsWKT();

		if (bounds.isNull()) {
			latLonBoundingBoxMinX = "";
		} else {
			latLonBoundingBoxMinX = Double.toString(bounds.getMinX());
			latLonBoundingBoxMinY = Double.toString(bounds.getMinY());
			latLonBoundingBoxMaxX = Double.toString(bounds.getMaxX());
			latLonBoundingBoxMaxY = Double.toString(bounds.getMaxY());
		}

		name = cvConfig.getName();
		label = cvConfig.getLabel();
		description = cvConfig.getDescription();
		metadataLink = (cvConfig.getMetadataLink() != null ? cvConfig
				.getMetadataLink().getAbout() : null);
		nativeFormat = cvConfig.getNativeFormat();
		defaultInterpolationMethod = cvConfig.getDefaultInterpolationMethod();

		StringBuffer buf = new StringBuffer();
		// Keywords
		if (cvConfig.getKeywords() != null) {
			for (Iterator i = cvConfig.getKeywords().iterator(); i.hasNext();) {
				String keyword = (String) i.next();
				buf.append(keyword);

				if (i.hasNext()) {
					buf.append(" ");
				}
			}

			this.keywords = buf.toString();
		}

		if (cvConfig.getRequestCRSs() != null) {
			buf = new StringBuffer();
			// RequestCRSs
			for (Iterator i = cvConfig.getRequestCRSs().iterator(); i.hasNext();) {
				String CRS = (String) i.next();
				buf.append(CRS);

				if (i.hasNext()) {
					buf.append(",");
				}
			}

			this.requestCRSs = buf.toString();
		}

		if (cvConfig.getResponseCRSs() != null) {
			buf = new StringBuffer();
			// ResponseCRSs
			for (Iterator i = cvConfig.getResponseCRSs().iterator(); i
					.hasNext();) {
				String CRS = (String) i.next();
				buf.append(CRS);

				if (i.hasNext()) {
					buf.append(",");
				}
			}

			this.responseCRSs = buf.toString();
		}

		if (cvConfig.getSupportedFormats() != null) {
			buf = new StringBuffer();
			// SupportedFormats
			for (Iterator i = cvConfig.getSupportedFormats().iterator(); i
					.hasNext();) {
				String format = (String) i.next();
				buf.append(format);

				if (i.hasNext()) {
					buf.append(",");
				}
			}

			this.supportedFormats = buf.toString();
		}

		if (cvConfig.getInterpolationMethods() != null) {
			buf = new StringBuffer();
			// InterpolationMethods
			for (Iterator i = cvConfig.getInterpolationMethods().iterator(); i
					.hasNext();) {
				String intMethod = (String) i.next();
				buf.append(intMethod);

				if (i.hasNext()) {
					buf.append(",");
				}
			}

			this.interpolationMethods = buf.toString();
		}

		styles = new TreeSet();
		for (Iterator i = config.getStyles().values().iterator(); i.hasNext();) {
			StyleConfig sc = (StyleConfig) i.next();
			styles.add(sc.getId());

			if (sc.isDefault()) {
				if ((styleId == null) || styleId.equals("")) {
					styleId.equals(sc.getId());
				}
			}
		}

		Object attribute = styles;

		if (attribute instanceof org.vfny.geoserver.form.data.AttributeDisplay) {
			// TODO why I am here?
		}
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		Locale locale = (Locale) request.getLocale();
		MessageResources messages = servlet.getResources();
		final String ENVELOPE = HTMLEncoder.decode(messages.getMessage(locale,
				"config.data.calculateBoundingBox.label"));

		// Pass Attribute Management Actions through without
		// much validation.
		if (action.startsWith("Up") || action.startsWith("Down")
				|| action.startsWith("Remove") || action.equals(ENVELOPE)) {
			return errors;
		}

		DataConfig data = ConfigRequests.getDataConfig(request);
		// Check selected style exists

		if (!(data.getStyles().containsKey(styleId) || "".equals(styleId))) {
			errors.add("styleId", new ActionError("error.styleId.notFound",
					styleId));
		}

		// check name exists in current DataStore?
		if ("".equals(latLonBoundingBoxMinX)
				|| "".equals(latLonBoundingBoxMinY)
				|| "".equals(latLonBoundingBoxMaxX)
				|| "".equals(latLonBoundingBoxMaxY)) {

			errors.add("envelope", new ActionError("error.envelope.required"));
		} else {
			try {
				double minX = Double.parseDouble(latLonBoundingBoxMinX);
				double minY = Double.parseDouble(latLonBoundingBoxMinY);
				double maxX = Double.parseDouble(latLonBoundingBoxMaxX);
				double maxY = Double.parseDouble(latLonBoundingBoxMaxY);
			} catch (NumberFormatException badNumber) {
				errors.add("envelope", new ActionError(
						"error.envelope.invalid", badNumber));
			}
		}

		if ("".equals(name)) {
			errors.add("name", new ActionError("error.coverage.name.required"));
		} else if (name.indexOf(" ") > 0) {
			errors.add("name", new ActionError("error.coverage.name.invalid"));
		}

		return errors;
	}

	/**
	 * @return Returns the defaultInterpolationMethod.
	 */
	public String getDefaultInterpolationMethod() {
		return defaultInterpolationMethod;
	}

	/**
	 * @param defaultInterpolationMethod
	 *            The defaultInterpolationMethod to set.
	 */
	public void setDefaultInterpolationMethod(String defaultInterpolationMethod) {
		this.defaultInterpolationMethod = defaultInterpolationMethod;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the keywords.
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords
	 *            The keywords to set.
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return Returns the latLonBoundingBoxMaxX.
	 */
	public String getMaxX() {
		return latLonBoundingBoxMaxX;
	}

	/**
	 * @param latLonBoundingBoxMaxX
	 *            The latLonBoundingBoxMaxX to set.
	 */
	public void setMaxX(String latLonBoundingBoxMaxX) {
		this.latLonBoundingBoxMaxX = latLonBoundingBoxMaxX;
	}

	/**
	 * @return Returns the latLonBoundingBoxMaxY.
	 */
	public String getMaxY() {
		return latLonBoundingBoxMaxY;
	}

	/**
	 * @param latLonBoundingBoxMaxY
	 *            The latLonBoundingBoxMaxY to set.
	 */
	public void setMaxY(String latLonBoundingBoxMaxY) {
		this.latLonBoundingBoxMaxY = latLonBoundingBoxMaxY;
	}

	/**
	 * @return Returns the latLonBoundingBoxMinX.
	 */
	public String getMinX() {
		return latLonBoundingBoxMinX;
	}

	/**
	 * @param latLonBoundingBoxMinX
	 *            The latLonBoundingBoxMinX to set.
	 */
	public void setMinX(String latLonBoundingBoxMinX) {
		this.latLonBoundingBoxMinX = latLonBoundingBoxMinX;
	}

	/**
	 * @return Returns the latLonBoundingBoxMinY.
	 */
	public String getMinY() {
		return latLonBoundingBoxMinY;
	}

	/**
	 * @param latLonBoundingBoxMinY
	 *            The latLonBoundingBoxMinY to set.
	 */
	public void setMinY(String latLonBoundingBoxMinY) {
		this.latLonBoundingBoxMinY = latLonBoundingBoxMinY;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the nativeFormat.
	 */
	public String getNativeFormat() {
		return nativeFormat;
	}

	/**
	 * @param nativeFormat
	 *            The nativeFormat to set.
	 */
	public void setNativeFormat(String nativeFormat) {
		this.nativeFormat = nativeFormat;
	}

	/**
	 * @return Returns the srsName.
	 */
	public String getSrsName() {
		return srsName;
	}

	/**
	 * @param srsName
	 *            The srsName to set.
	 */
	public void setSrsName(String srsName) {
		this.srsName = srsName;
	}

	/**
	 * @return Returns the metadataLink.
	 */
	public String getMetadataLink() {
		return metadataLink;
	}

	/**
	 * @param metadataLink
	 *            The metadataLink to set.
	 */
	public void setMetadataLink(String metadataLink) {
		this.metadataLink = metadataLink;
	}

	/**
	 * @return Returns the interpolationMethods.
	 */
	public String getInterpolationMethods() {
		return interpolationMethods;
	}

	/**
	 * @param interpolationMethods
	 *            The interpolationMethods to set.
	 */
	public void setInterpolationMethods(String interpolationMethods) {
		this.interpolationMethods = interpolationMethods;
	}

	/**
	 * @return Returns the requestCRSs.
	 */
	public String getRequestCRSs() {
		return requestCRSs;
	}

	/**
	 * @param requestCRSs
	 *            The requestCRSs to set.
	 */
	public void setRequestCRSs(String requestCRSs) {
		this.requestCRSs = requestCRSs;
	}

	/**
	 * @return Returns the responseCRSs.
	 */
	public String getResponseCRSs() {
		return responseCRSs;
	}

	/**
	 * @param responseCRSs
	 *            The responseCRSs to set.
	 */
	public void setResponseCRSs(String responseCRSs) {
		this.responseCRSs = responseCRSs;
	}

	/**
	 * @return Returns the supportedFormats.
	 */
	public String getSupportedFormats() {
		return supportedFormats;
	}

	/**
	 * @param supportedFormats
	 *            The supportedFormats to set.
	 */
	public void setSupportedFormats(String supportedFormats) {
		this.supportedFormats = supportedFormats;
	}

	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return Returns the formatId.
	 */
	public String getFormatId() {
		return formatId;
	}

	/**
	 * @param formatId
	 *            The formatId to set.
	 */
	public void setFormatId(String formatId) {
		this.formatId = formatId;
	}

	/**
	 * @return Returns the newCoverage.
	 */
	public String getNewCoverage() {
		return newCoverage;
	}

	/**
	 * @param newCoverage
	 *            The newCoverage to set.
	 */
	public void setNewCoverage(String newCoverage) {
		this.newCoverage = newCoverage;
	}

	public SortedSet getStyles() {
		return styles;
	}

	public void setStyles(SortedSet styles) {
		this.styles = styles;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	public String getWKTString() {
		return WKTString;
	}

	public void setWKTString(String string) {
		WKTString = string;
	}
}

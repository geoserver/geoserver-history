/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.data;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataFeatureTypesEditorForm extends ActionForm {
	
	private String name;
	private String SRS;
	private String title;
	private String latlonBoundingBox;
	private String keywords;
	private String _abstract;
	
	public void reset(ActionMapping arg0, HttpServletRequest request) {
		super.reset(arg0, request);

		ServletContext context = getServlet().getServletContext();
		DataConfig config =
			(DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);

		String featureTypeName = (String) request.getSession().getAttribute("selectedFeatureType");
		System.out.println("Editor form: "+featureTypeName);
		int index = featureTypeName.indexOf(DataConfig.SEPARATOR);
		
		featureTypeName = featureTypeName.substring(index+DataConfig.SEPARATOR.length());
		System.out.println("Editor form: "+featureTypeName);
		FeatureTypeConfig ftConfig = config.getFeatureTypeConfig(featureTypeName);		
		if (ftConfig == null)
			System.out.println("NULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULL");
		_abstract = ftConfig.getAbstract();
		latlonBoundingBox = ftConfig.getLatLongBBox().toString();
		name = ftConfig.getName();
		SRS = Integer.toString(ftConfig.getSRS());
		title = ftConfig.getTitle();
		
		String out = "";
		for (int i = 0; i < ftConfig.getKeywords().size(); i++) {
			out = out + ftConfig.getKeywords().get(i);// + System.getProperty("line.separator");
		}

		this.keywords = out;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		return errors;
	}
	/**
	 * @return
	 */
	public String get_abstract() {
		return _abstract;
	}

	/**
	 * @return
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @return
	 */
	public String getLatlonBoundingBox() {
		return latlonBoundingBox;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getSRS() {
		return SRS;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param string
	 */
	public void set_abstract(String string) {
		_abstract = string;
	}

	/**
	 * @param string
	 */
	public void setKeywords(String string) {
		keywords = string;
	}

	/**
	 * @param string
	 */
	public void setLatlonBoundingBox(String string) {
		latlonBoundingBox = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setSRS(String string) {
		SRS = string;
	}

	/**
	 * @param string
	 */
	public void setTitle(String string) {
		title = string;
	}

}

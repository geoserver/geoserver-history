/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.data;

import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.requests.Requests;

import com.vividsolutions.jts.geom.Envelope;

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
    private String bBox;
	private String keywords;
	private String _abstract;
    private boolean _default;
	private boolean defaultChecked;
	
    /**
     * Set up FeatureTypeEditor from from Web Container.
     * <p>
     * The key DataConfig.SELECTED_FEATURE_TYPE is used to look up the
     * selected from the web container.
     * </p>
     * @param mapping
     * @param request
     */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset( mapping, request);
        
		ServletContext context = getServlet().getServletContext();
        DataConfig config = ConfigRequests.getDataConfig(request);

        
		UserContainer user = Requests.getUserContainer(request);
        
		// Richard can we please use this to store stuff?
        FeatureTypeConfig ftConfig; //= user.getFeatureTypeConfig();
        
        ftConfig = (FeatureTypeConfig) request.getSession().getAttribute(DataConfig.SELECTED_FEATURE_TYPE);
        
		_abstract = ftConfig.getAbstract();
        Envelope bounds = ftConfig.getLatLongBBox();
        if( bounds.isNull()){
            bBox = "";
        }
        else {
            bBox =  bounds.getMinX()+" "+bounds.getMinY()+" "+
                    bounds.getMaxX()+" "+bounds.getMaxY();
        }		
		name = ftConfig.getName();
		SRS = Integer.toString(ftConfig.getSRS());
		title = ftConfig.getTitle();
        _default = (ftConfig.getSchemaAttributes() == null) || (ftConfig.getSchemaAttributes().isEmpty());
        defaultChecked = false;
		
        StringBuffer buf = new StringBuffer();
        for( Iterator i=ftConfig.getKeywords().iterator(); i.hasNext();){
            String keyword = (String) i.next();
            buf.append( keyword );
            if( i.hasNext() ){
                buf.append( " " );    
            }
		}
		this.keywords = buf.toString();
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		DataConfig data = ConfigRequests.getDataConfig(request);
        
		// check name exists in current DataStore?
        
        if( "".equals( bBox ) ){
            errors.add( "latlongBoundingBox",
                    new ActionError("Lat Long Bounding box required")
            );
        }
        else {
            String parse[] = bBox.trim().split("\\w");
            if( parse.length != 4){
                errors.add( "latlongBoundingBox",
                    new ActionError("Invalid Lat Long Bounding required format: minx miny maxx maxy")
                );
            }
            else {
                try {
                    double minX = Double.parseDouble( parse[0] );
                    double minY = Double.parseDouble( parse[1] );
                    double maxX = Double.parseDouble( parse[2] );
                    double maxY = Double.parseDouble( parse[3] );
                }
                catch( NumberFormatException badNumber){
                    errors.add( "latlongBoundingBox",
                            new ActionError("Invliad Lat Long Bounding box", badNumber)
                    );                    
                }
            }
        }        
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
        return bBox;       
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
	public void setLatlonBoundingBox(String text) {
        bBox = text;        
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

	/**
	 * Access _default property.
	 * 
	 * @return Returns the _default.
	 */
	public boolean is_default() {
		return _default;
	}

	/**
	 * Set _default to _default.
	 *
	 * @param _default The _default to set.
	 */
	public void set_default(boolean _default) {
        defaultChecked = true;
		this._default = _default;
	}

	/**
	 * Access defaultChecked property.
	 * 
	 * @return Returns the defaultChecked.
	 */
	public boolean isDefaultChecked() {
		return defaultChecked;
	}
}

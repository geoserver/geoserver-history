/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.form.data;

import com.vividsolutions.jts.geom.Envelope;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.requests.Requests;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Form used to work with FeatureType information.
 * 
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: TypesEditorForm.java,v 1.1 2004/03/02 02:36:00 jive Exp $
 */
public class TypesEditorForm extends ActionForm {
    private String name;
    private String SRS;
    private String title;
    private String latLonBoundingBoxMinX;
    private String latLonBoundingBoxMinY;
    private String latLonBoundingBoxMaxX;
    private String latLonBoundingBoxMaxY;    
    private String keywords;
    /** FeatureType abstract */
    private String description;
    /**
     * List of AttributesEditorForm.
     * <p>
     * Empty denotes use of defaults.
     * </p>
     */
    private List attributes;

    /**
     * Set up FeatureTypeEditor from from Web Container.
     * 
     * <p>
     * The key DataConfig.SELECTED_FEATURE_TYPE is used to look up the selected
     * from the web container.
     * </p>
     *
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);

        ServletContext context = getServlet().getServletContext();
        DataConfig config = ConfigRequests.getDataConfig(request);

        UserContainer user = Requests.getUserContainer(request);

        FeatureTypeConfig type = user.getFeatureTypeConfig();
        if( type == null ){
            // Not sure what to do, user must of bookmarked?
            return; // Action should redirect to Select screen?
        }
        description = type.getAbstract();
        
        Envelope bounds = type.getLatLongBBox();
        if (bounds == null || bounds.isNull()) {
            latLonBoundingBoxMinX = "";
            latLonBoundingBoxMinY = "";
            latLonBoundingBoxMaxX = "";
            latLonBoundingBoxMaxY = "";
        } else {
            latLonBoundingBoxMinX = Double.toString(bounds.getMinX());
            latLonBoundingBoxMinY = Double.toString(bounds.getMinY());
            latLonBoundingBoxMaxX = Double.toString(bounds.getMaxX());
            latLonBoundingBoxMaxY = Double.toString(bounds.getMaxY());
        }
        name = type.getName();
        SRS = Integer.toString(type.getSRS());
        title = type.getTitle();
        
        type.getSchemaAttributes(); 
        _default = (ftConfig.getSchemaAttributes() == null)
            || (ftConfig.getSchemaAttributes().isEmpty());
        defaultChecked = false;

        StringBuffer buf = new StringBuffer();

        for (Iterator i = ftConfig.getKeywords().iterator(); i.hasNext();) {
            String keyword = (String) i.next();
            buf.append(keyword);

            if (i.hasNext()) {
                buf.append(" ");
            }
        }

        this.keywords = buf.toString();
    }

    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        DataConfig data = ConfigRequests.getDataConfig(request);

        // check name exists in current DataStore?
        if ("".equals(latLonBoundingBoxMinX)
         || "".equals(latLonBoundingBoxMinY)
         || "".equals(latLonBoundingBoxMaxX)
         || "".equals(latLonBoundingBoxMaxY)) {
           
            errors.add("latlongBoundingBox",
                new ActionError("error.latLonBoundingBox.required"));
        } else {
            try {
                double minX = Double.parseDouble(latLonBoundingBoxMinX);
                double minY = Double.parseDouble(latLonBoundingBoxMinY);
                double maxX = Double.parseDouble(latLonBoundingBoxMaxX);
                double maxY = Double.parseDouble(latLonBoundingBoxMaxY);
            } catch (NumberFormatException badNumber) {
                errors.add("latlongBoundingBox",
                    new ActionError("error.latLonBoundingBox.invalid",
                        badNumber));
            }
        }

        return errors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String get_abstract() {
        return _abstract;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getLatLonBoundingBoxMinX() {
        return latLonBoundingBoxMinX;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getSRS() {
        return SRS;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void set_abstract(String string) {
        _abstract = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setKeywords(String string) {
        keywords = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text
     */
    public void setLatLonBoundingBoxMinX(String text) {
        latLonBoundingBoxMinX = text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setSRS(String string) {
        SRS = string;
    }

    /**
     * DOCUMENT ME!
     *
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
	/**
	 * Access latLonBoundingBox2 property.
	 * 
	 * @return Returns the latLonBoundingBox2.
	 */
	public String getLatLonBoundingBoxMinY() {
		return latLonBoundingBoxMinY;
	}

	/**
	 * Set latLonBoundingBox2 to latLonBoundingBox2.
	 *
	 * @param latLonBoundingBox2 The latLonBoundingBox2 to set.
	 */
	public void setLatLonBoundingBoxMinY(String latLonBoundingBox2) {
		this.latLonBoundingBoxMinY = latLonBoundingBox2;
	}

	/**
	 * Access latLonBoundingBox3 property.
	 * 
	 * @return Returns the latLonBoundingBox3.
	 */
	public String getLatLonBoundingBoxMaxX() {
		return latLonBoundingBoxMaxX;
	}

	/**
	 * Set latLonBoundingBox3 to latLonBoundingBox3.
	 *
	 * @param latLonBoundingBox3 The latLonBoundingBox3 to set.
	 */
	public void setLatLonBoundingBoxMaxX(String latLonBoundingBox3) {
		this.latLonBoundingBoxMaxX = latLonBoundingBox3;
	}

	/**
	 * Access latLonBoundingBox4 property.
	 * 
	 * @return Returns the latLonBoundingBox4.
	 */
	public String getLatLonBoundingBoxMaxY() {
		return latLonBoundingBoxMaxY;
	}

	/**
	 * Set latLonBoundingBox4 to latLonBoundingBox4.
	 *
	 * @param latLonBoundingBox4 The latLonBoundingBox4 to set.
	 */
	public void setLatLonBoundingBoxMaxY(String latLonBoundingBox4) {
		this.latLonBoundingBoxMaxY = latLonBoundingBox4;
	}

}

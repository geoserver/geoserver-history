/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.form.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.DataStore;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.global.dto.DataTransferObjectFactory;
import org.vfny.geoserver.requests.Requests;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Form used to work with FeatureType information.
 * 
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: TypesEditorForm.java,v 1.2 2004/03/02 10:06:42 jive Exp $
 */
public class TypesEditorForm extends ActionForm {

    /** Identiy DataStore responsible for this FeatureType */
    private String dataStoreId;
    
    /**
     * Name of featureType.
     * <p>
     * Usually an exact match for typeName provided by a DataStore.
     * </p> 
     */
    private String name;
    /**
     * Representation of the Spatial Reference System.
     * <p>
     * Empty represents unknown, usually assumed to be Cartisian Coordinates.
     * </p>
     */
    private String SRS;
    /** Title of this FeatureType */
    private String title;
    /** Representation of bounds info as parseable by Double */
    private String latLonBoundingBoxMinX;
    /** Representation of bounds info as parseable by Double */
    private String latLonBoundingBoxMinY;
    /** Representation of bounds info as parseable by Double */
    private String latLonBoundingBoxMaxX;
    /** Representation of bounds info as parseable by Double */
    private String latLonBoundingBoxMaxY;
    /** List of keywords, often grouped with brackets */
    private String keywords;
    
    /** FeatureType abstract */
    private String description;
    
    /**
     * One of a select list - simplest is AbstractBaseClass.
     * <p>
     * The value "--" will be used to indicate default schema completly
     * generated from FeatureType information at runtime.
     * </p>
     * <p>
     * When generated the schema will make use a schemaBase of
     * "AbstractFeatureType".
     * </p> 
     */
    private String schemaBase;
    
    /**
     * List of AttributesEditorForm.
     */
    private List attributes;

    /**
     * Set up FeatureTypeEditor from from Web Container.
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
        this.dataStoreId = type.getDataStoreId()      ;  
        
        
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
        
        if( type.getSchemaBase().equals("AbstractFeatureType") &&
            type.getSchemaAttributes() == null ){ 
            this.schemaBase = "--";
            this.attributes = new ArrayList();
            
            // Generate ReadOnly list of Attribtues
            //
            DataStoreConfig dataStoreConfig = config.getDataStore( dataStoreId );
            try {
				DataStore dataStore = dataStoreConfig.findDataStore();
                FeatureType featureType = dataStore.getSchema( name );
                List generated = DataTransferObjectFactory.generateAttributes( featureType );                        
                for( Iterator i=type.getSchemaAttributes().iterator(); i.hasNext();){
                    AttributeTypeInfoConfig attribute = (AttributeTypeInfoConfig) i.next();
                    this.attributes.add( new AttributeDisplay( attribute ) );
                }
			} catch (IOException e) {
				// DataStore unavailable!
			}
        }
        else {
        	this.schemaBase = type.getSchemaBase();
            this.attributes = new ArrayList();
            //
            // Need to add read only AttributeDisplay for each attribute
            // defined by schemaBase
            //
            for( Iterator i=type.getSchemaAttributes().iterator(); i.hasNext();){
                AttributeTypeInfoConfig attribute = (AttributeTypeInfoConfig) i.next();
                this.attributes.add( new AttributeForm( attribute ) );
            }
        }
        StringBuffer buf = new StringBuffer();
        for (Iterator i = type.getKeywords().iterator(); i.hasNext();) {
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

}
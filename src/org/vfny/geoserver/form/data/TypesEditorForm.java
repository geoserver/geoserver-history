/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.form.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import org.vfny.geoserver.global.dto.AttributeTypeInfoDTO;
import org.vfny.geoserver.global.dto.DataTransferObjectFactory;
import org.vfny.geoserver.requests.Requests;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Form used to work with FeatureType information.
 * 
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: TypesEditorForm.java,v 1.6 2004/03/09 05:38:18 jive Exp $
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
            System.out.println("Type is not there");
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
        
        if( ( type.getSchemaBase() == null ||
              type.getSchemaBase().equals("AbstractFeatureType") ) &&
            type.getSchemaAttributes() == null ){
            this.schemaBase = "--";
            this.attributes = new ArrayList();
            
            // Generate ReadOnly list of Attribtues
            //
            DataStoreConfig dataStoreConfig = config.getDataStore( dataStoreId );
            try {
				DataStore dataStore = dataStoreConfig.findDataStore(getServlet().getServletContext());
                FeatureType featureType = dataStore.getSchema( name );                
                List generated = DataTransferObjectFactory.generateAttributes( featureType );
                this.attributes = attribtuesDisplayList( generated );
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
            List schemaAttribtues = DataTransferObjectFactory.generateRequiredAttribtues(schemaBase);
            attributes.addAll( attribtuesDisplayList( schemaAttribtues ));
            attributes.addAll( attribtuesFormList( type.getSchemaAttributes() ));
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
    /**
     * Create a List of AttributeDisplay based on AttributeTypeInfoDTO.
     * 
     * @param list
     * @return
     */
    private List attribtuesDisplayList( List dtoList ){
        List list = new ArrayList();
        int index=0;
        for( Iterator i=dtoList.iterator(); i.hasNext(); index++){
            Object next = i.next();
            System.out.println( index+" attribute: "+next);
            list.add( new AttributeDisplay( (AttributeTypeInfoDTO) next ) );
        }
        return list;
    }
    /**
     * Create a List of AttributeForm based on AttributeTypeInfoDTO.
     * 
     * @param list
     * @return
     */
    private List attribtuesFormList( List dtoList ){
        List list = new ArrayList();
        for( Iterator i=dtoList.iterator(); i.hasNext();){            
            list.add( new AttributeDisplay( (AttributeTypeInfoConfig) i.next() ) );
        }
        return list;
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
     * Access attributes property.
     * 
     * @return Returns the attributes.
     */
    public List getAttributes() {
        return attributes;
    }
    /**
     * Set attributes to attributes.
     *
     * @param attributes The attributes to set.
     */
    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }
    /**
     * Access dataStoreId property.
     * 
     * @return Returns the dataStoreId.
     */
    public String getDataStoreId() {
        return dataStoreId;
    }
    /**
     * Set dataStoreId to dataStoreId.
     *
     * @param dataStoreId The dataStoreId to set.
     */
    public void setDataStoreId(String dataStoreId) {
        this.dataStoreId = dataStoreId;
    }
    /**
     * Access abstact (or description) property.
     * 
     * @return Returns the description.
     */
    public String getAbstract() {
        return description;
    }
    /**
     * Set abstact (or description) to description.
     *
     * @param description The description to set.
     */
    public void setAbstract(String description) {
        this.description = description;
    }
    /**
     * Access keywords property.
     * 
     * @return Returns the keywords.
     */
    public String getKeywords() {
        return keywords;
    }
    /**
     * Set keywords to keywords.
     *
     * @param keywords The keywords to set.
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    /**
     * Access latLonBoundingBoxMaxX property.
     * 
     * @return Returns the latLonBoundingBoxMaxX.
     */
    public String getLatLonBoundingBoxMaxX() {
        return latLonBoundingBoxMaxX;
    }
    /**
     * Set latLonBoundingBoxMaxX to latLonBoundingBoxMaxX.
     *
     * @param latLonBoundingBoxMaxX The latLonBoundingBoxMaxX to set.
     */
    public void setLatLonBoundingBoxMaxX(String latLonBoundingBoxMaxX) {
        this.latLonBoundingBoxMaxX = latLonBoundingBoxMaxX;
    }
    /**
     * Access latLonBoundingBoxMaxY property.
     * 
     * @return Returns the latLonBoundingBoxMaxY.
     */
    public String getLatLonBoundingBoxMaxY() {
        return latLonBoundingBoxMaxY;
    }
    /**
     * Set latLonBoundingBoxMaxY to latLonBoundingBoxMaxY.
     *
     * @param latLonBoundingBoxMaxY The latLonBoundingBoxMaxY to set.
     */
    public void setLatLonBoundingBoxMaxY(String latLonBoundingBoxMaxY) {
        this.latLonBoundingBoxMaxY = latLonBoundingBoxMaxY;
    }
    /**
     * Access latLonBoundingBoxMinX property.
     * 
     * @return Returns the latLonBoundingBoxMinX.
     */
    public String getLatLonBoundingBoxMinX() {
        return latLonBoundingBoxMinX;
    }
    /**
     * Set latLonBoundingBoxMinX to latLonBoundingBoxMinX.
     *
     * @param latLonBoundingBoxMinX The latLonBoundingBoxMinX to set.
     */
    public void setLatLonBoundingBoxMinX(String latLonBoundingBoxMinX) {
        this.latLonBoundingBoxMinX = latLonBoundingBoxMinX;
    }
    /**
     * Access latLonBoundingBoxMinY property.
     * 
     * @return Returns the latLonBoundingBoxMinY.
     */
    public String getLatLonBoundingBoxMinY() {
        return latLonBoundingBoxMinY;
    }
    /**
     * Set latLonBoundingBoxMinY to latLonBoundingBoxMinY.
     *
     * @param latLonBoundingBoxMinY The latLonBoundingBoxMinY to set.
     */
    public void setLatLonBoundingBoxMinY(String latLonBoundingBoxMinY) {
        this.latLonBoundingBoxMinY = latLonBoundingBoxMinY;
    }
    /**
     * Access name property.
     * 
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * Set name to name.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Access schemaBase property.
     * 
     * @return Returns the schemaBase.
     */
    public String getSchemaBase() {
        return schemaBase;
    }
    /**
     * Set schemaBase to schemaBase.
     *
     * @param schemaBase The schemaBase to set.
     */
    public void setSchemaBase(String schemaBase) {
        this.schemaBase = schemaBase;
    }
    /**
     * Access sRS property.
     * 
     * @return Returns the sRS.
     */
    public String getSRS() {
        return SRS;
    }
    /**
     * Set sRS to srs.
     *
     * @param srs The sRS to set.
     */
    public void setSRS(String srs) {
        SRS = srs;
    }
    /**
     * Access title property.
     * 
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }
    /**
     * Set title to title.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    static final List schemaBases;
    static {
        List bases = new ArrayList();
        bases.add( "--" );
        bases.addAll( DataTransferObjectFactory.schemaBaseMap.keySet() );
        schemaBases = Collections.unmodifiableList( bases );
    }
    /**
     * Are belong to us.
     * <p>
     * What can I say it is near a deadline!
     * Easy access for <code>Editor.jsp</code>.
     * </p>
     * @return Possible schemaBase options
     */
    public List getAllYourBase(){        
        return schemaBases;                
    }
}
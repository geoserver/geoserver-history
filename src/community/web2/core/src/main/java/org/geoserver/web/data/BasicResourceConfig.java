/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.ProjectionPolicy;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.web.wicket.CRSPanel;
import org.geoserver.web.wicket.EnvelopePanel;
import org.geoserver.web.wicket.KeywordsEditor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;

/**
 * A generic configuration panel for all basic ResourceInfo properties
 */
@SuppressWarnings("serial")
public class BasicResourceConfig extends ResourceConfigurationPanel {
	
	public BasicResourceConfig(String id, IModel model) {
		super(id, model);

		add(new TextField("title"));
		add(new TextArea("abstract"));
		add(new KeywordsEditor("keywords", new PropertyModel(model, "keywords")));
        add(new MetadataLinkEditor("metadataLinks", model));
        
        final Form bboxForm = new Form("boxesForm");
        add(bboxForm);
        
        // native bbox
		PropertyModel nativeBBoxModel = new PropertyModel(model, "nativeBoundingBox");
        final EnvelopePanel nativeBBox = new EnvelopePanel("nativeBoundingBox", nativeBBoxModel);
    	nativeBBox.setOutputMarkupId(true);
    	bboxForm.add(nativeBBox);
    	bboxForm.add(new AjaxLink("computeNative", nativeBBoxModel) {

            @Override
            public void onClick(final AjaxRequestTarget target) {
                ResourceInfo resource = (ResourceInfo) BasicResourceConfig.this.getModelObject();
                ReferencedEnvelope re = null;
                try {
                    if(resource instanceof FeatureTypeInfo) {
                        re = ((FeatureTypeInfo) resource).getFeatureSource(null, null).getBounds();
                    } else if(resource instanceof CoverageInfo) {
                        // for coverages that should be always available.. shoulnd't it?
                        re = ((CoverageInfo) resource).getNativeBoundingBox();
                    }
                } catch(IOException e) {
                    LOGGER.log(Level.SEVERE, "Error computing the native BBOX", e);
                    error("Error computing the native BBOX:\n" + e.getMessage());
                }
                nativeBBox.setModelObject(re);
                target.addComponent(nativeBBox);
            }
            
        });
        
        // lat/lon bbox
        final EnvelopePanel latLonPanel = new EnvelopePanel("latLonBoundingBox", new PropertyModel(model, "latLonBoundingBox"));
        latLonPanel.setOutputMarkupId(true);
        bboxForm.add(latLonPanel);
        bboxForm.add(new AjaxSubmitLink("computeLatLon", bboxForm) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                form.process();
                ReferencedEnvelope env = (ReferencedEnvelope) nativeBBox.getModelObject();
                try {
                    if(env != null) {
                        latLonPanel.setModelObject(env.transform(DefaultGeographicCRS.WGS84, true));
                    }
                } catch(Exception e) {
                    
                }
                target.addComponent(latLonPanel);
            }
            
        });
        
        // native srs , declared srs, and srs handling dropdown
        CRSPanel nativeCRS = new CRSPanel("nativeSRS", new PropertyModel(model, "nativeCRS"));
        nativeCRS.setReadOnly(true);
        add(nativeCRS);
        CRSPanel declaredCRS = new CRSPanel("declaredSRS", new PropertyModel(model, "cRS"));
        add(declaredCRS);
        DropDownChoice projectionPolicy = new DropDownChoice("srsHandling", new PropertyModel(model, "projectionPolicy"), Arrays.asList(ProjectionPolicy.values()), new ProjectionPolicyRenderer());
        ResourceInfo ri = (ResourceInfo) model.getObject();
        if(((ResourceInfo) model.getObject()).getCRS() == null) {
            // no native, the only meaningful policy is to force
            ri.setProjectionPolicy(ProjectionPolicy.FORCE_DECLARED);
        } if(model.getObject() instanceof CoverageInfo) {
            // coverages projection policy choice is not allowed, if there is a native a reprojection will occurr
            CoverageInfo ci = (CoverageInfo) model.getObject();
            ci.setProjectionPolicy(ProjectionPolicy.REPROJECT_TO_DECLARED);
        } else 
        add(projectionPolicy);
	}
	
	class ProjectionPolicyRenderer implements IChoiceRenderer {

        public Object getDisplayValue(Object object) {
            return new StringResourceModel(((ProjectionPolicy) object).name(), 
                    BasicResourceConfig.this, null).getString();
        }

        public String getIdValue(Object object, int index) {
            return ((ProjectionPolicy) object).name();
        }
	    
	}
}

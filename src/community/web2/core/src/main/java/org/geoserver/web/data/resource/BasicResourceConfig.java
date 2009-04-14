/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import org.apache.wicket.ajax.AjaxRequestTarget;
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
import org.geoserver.web.wicket.SRSToCRSModel;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;

/**
 * A generic configuration panel for all basic ResourceInfo properties
 */
@SuppressWarnings("serial")
public class BasicResourceConfig extends ResourceConfigurationPanel {
	
	DropDownChoice projectionPolicy;
	CRSPanel declaredCRS;

    public BasicResourceConfig(String id, IModel model) {
		super(id, model);

		add(new TextField("name"));
		add(new TextField("title"));
		add(new TextArea("abstract"));
		add(new KeywordsEditor("keywords", new PropertyModel(model, "keywords")));
        add(new MetadataLinkEditor("metadataLinks", model));
        
        final Form refForm = new Form("referencingForm");
        add(refForm);
        
        // native bbox
		PropertyModel nativeBBoxModel = new PropertyModel(model, "nativeBoundingBox");
        final EnvelopePanel nativeBBox = new EnvelopePanel("nativeBoundingBox", nativeBBoxModel);
    	nativeBBox.setOutputMarkupId(true);
    	refForm.add(nativeBBox);
    	refForm.add(new AjaxSubmitLink("computeNative", refForm) {

            @Override
            public void onSubmit(final AjaxRequestTarget target, Form form) {
                form.process();
                ResourceInfo resource = (ResourceInfo) BasicResourceConfig.this.getModelObject();
                computeNative(nativeBBox, resource);
                target.addComponent(nativeBBox);
            }
            
        });
        
        // lat/lon bbox
        final EnvelopePanel latLonPanel = new EnvelopePanel("latLonBoundingBox", new PropertyModel(model, "latLonBoundingBox"));
        latLonPanel.setOutputMarkupId(true);
        latLonPanel.setRequired(true);
        refForm.add(latLonPanel);
        refForm.add(new AjaxSubmitLink("computeLatLon", refForm) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                
                computeLatLon(nativeBBox, latLonPanel);
                target.addComponent(latLonPanel);
            }
        });
        
        // native srs , declared srs, and srs handling dropdown
        CRSPanel nativeCRS = new CRSPanel("nativeSRS", new PropertyModel(model, "nativeCRS"));
        nativeCRS.setReadOnly(true);
        refForm.add(nativeCRS);
        declaredCRS = new CRSPanel("declaredSRS", new SRSToCRSModel(new PropertyModel(model, "sRS")));
        declaredCRS.setRequired(true);
        refForm.add(declaredCRS);
        
        projectionPolicy = new DropDownChoice("srsHandling", new PropertyModel(model, "projectionPolicy"), Arrays.asList(ProjectionPolicy.values()), new ProjectionPolicyRenderer());
        ResourceInfo ri = (ResourceInfo) model.getObject();
        if(((ResourceInfo) model.getObject()).getCRS() == null) {
            // no native, the only meaningful policy is to force
            ri.setProjectionPolicy(ProjectionPolicy.FORCE_DECLARED);
        } if(model.getObject() instanceof CoverageInfo) {
            // coverages projection policy choice is not allowed, if there is a native a reprojection will occurr
            CoverageInfo ci = (CoverageInfo) model.getObject();
            ci.setProjectionPolicy(ProjectionPolicy.REPROJECT_TO_DECLARED);
        } 
        refForm.add(projectionPolicy);
	}
    
    void computeLatLon(final EnvelopePanel nativeBBox,
            final EnvelopePanel latLonPanel) {
        ReferencedEnvelope env = (ReferencedEnvelope) nativeBBox.getModelObject();
        // handle the declared srs if necessary
        if(projectionPolicy.getModelObject() == ProjectionPolicy.FORCE_DECLARED) {
            env = new ReferencedEnvelope(env, declaredCRS.getCRS());
        }
        // reproject to WGS84
        try {
            if(env != null) {
                latLonPanel.setModelObject(env.transform(DefaultGeographicCRS.WGS84, true));
            }
        } catch(Exception e) {
            LOGGER.log(Level.FINE, "Envelope reprojection error", e);
            error("Could not reproject to WGS84: " + e.getMessage());
        }
    }
    
    private void computeNative(final EnvelopePanel nativeBBox,
            ResourceInfo resource) {
        ReferencedEnvelope re = null;
        try {
            if(resource instanceof FeatureTypeInfo) {
                re = ((FeatureTypeInfo) resource).getFeatureSource(null, null).getBounds();
            } else if(resource instanceof CoverageInfo) {
                // for coverages that should be always available.. shoulnd't it?
                re = ((CoverageInfo) resource).getNativeBoundingBox();
            }
            if(projectionPolicy.getModelObject() == ProjectionPolicy.REPROJECT_TO_DECLARED) {
                re = re.transform(declaredCRS.getCRS(), true);
            }
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, "Error computing the native BBOX", e);
            error("Error computing the native BBOX:" + e.getMessage());
        } catch(Exception ex) {
            LOGGER.log(Level.SEVERE, "Error transforming the native BBOX to the declared projection", ex);
            error("Error transforming the native BBOX to the declared projection " + ex.getMessage());
        }
        nativeBBox.setModelObject(re);
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

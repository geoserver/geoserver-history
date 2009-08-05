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
import org.apache.wicket.validation.validator.PatternValidator;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.ProjectionPolicy;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.wicket.CRSPanel;
import org.geoserver.web.wicket.EnvelopePanel;
import org.geoserver.web.wicket.GeoServerAjaxFormLink;
import org.geoserver.web.wicket.KeywordsEditor;
import org.geoserver.web.wicket.LiveCollectionModel;
import org.geoserver.web.wicket.SRSToCRSModel;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * A generic configuration panel for all basic ResourceInfo properties
 */
@SuppressWarnings("serial")
public class BasicResourceConfig extends ResourceConfigurationPanel {

    DropDownChoice projectionPolicy;

    CRSPanel declaredCRS;

    public BasicResourceConfig(String id, IModel model) {
        super(id, model);

        TextField name = new TextField("name");
        name.add(new PatternValidator("[\\w_]\\w*"));
        add(name);
        add(new TextField("title"));
        add(new TextArea("abstract"));
        add(new KeywordsEditor("keywords", LiveCollectionModel.list(new PropertyModel(model, "keywords"))));
        add(new MetadataLinkEditor("metadataLinks", model));

        final Form refForm = new Form("referencingForm");
        add(refForm);

        // native bbox
        PropertyModel nativeBBoxModel = new PropertyModel(model, "nativeBoundingBox");
        final EnvelopePanel nativeBBox = new EnvelopePanel("nativeBoundingBox", nativeBBoxModel);
        nativeBBox.setOutputMarkupId(true);
        refForm.add(nativeBBox);
        refForm.add(computeNativeBoundsLink(refForm, nativeBBox));

        // lat/lon bbox
        final EnvelopePanel latLonPanel = new EnvelopePanel("latLonBoundingBox", new PropertyModel(
                model, "latLonBoundingBox"));
        latLonPanel.setOutputMarkupId(true);
        latLonPanel.setRequired(true);
        refForm.add(latLonPanel);
        refForm.add(computeLatLonBoundsLink(refForm, nativeBBox, latLonPanel));

        // native srs , declared srs, and srs handling dropdown
        CRSPanel nativeCRS = new CRSPanel("nativeSRS", new PropertyModel(model, "nativeCRS"));
        nativeCRS.setReadOnly(true);
        refForm.add(nativeCRS);
        declaredCRS = new CRSPanel("declaredSRS",
                new SRSToCRSModel(new PropertyModel(model, "sRS")));
        declaredCRS.setRequired(true);
        refForm.add(declaredCRS);

        projectionPolicy = new DropDownChoice("srsHandling", new PropertyModel(model,
                "projectionPolicy"), Arrays.asList(ProjectionPolicy.values()),
                new ProjectionPolicyRenderer());
        ResourceInfo ri = (ResourceInfo) model.getObject();
        if (((ResourceInfo) model.getObject()).getCRS() == null) {
            // no native, the only meaningful policy is to force
            ri.setProjectionPolicy(ProjectionPolicy.FORCE_DECLARED);
        }
        if (model.getObject() instanceof CoverageInfo) {
            // coverages projection policy choice is not allowed, if there is a native a
            // reprojection will occurr
            CoverageInfo ci = (CoverageInfo) model.getObject();
            ci.setProjectionPolicy(ProjectionPolicy.REPROJECT_TO_DECLARED);
        }
        refForm.add(projectionPolicy);
    }

    AjaxSubmitLink computeNativeBoundsLink(final Form refForm,
            final EnvelopePanel nativeBBox) {
        return new AjaxSubmitLink("computeNative", refForm) {

            @Override
            public void onSubmit(final AjaxRequestTarget target, Form form) {
                // perform manual processing otherwise the component contents won't be updated
                form.process();
                ResourceInfo resource = (ResourceInfo) BasicResourceConfig.this.getModelObject();
                try {
                    CatalogBuilder cb = new CatalogBuilder(GeoServerApplication.get().getCatalog());
                    ReferencedEnvelope bounds = cb.getNativeBounds(resource);
                    resource.setNativeBoundingBox(bounds);
                    nativeBBox.setModelObject(bounds);
                } catch(IOException e) {
                    LOGGER.log(Level.SEVERE, "Error computing the native BBOX", e);
                    error("Error computing the native BBOX:" + e.getMessage());
                }
                target.addComponent(nativeBBox);
            }
            
            public boolean getDefaultFormProcessing() {
                // disable the default processing or the link won't trigger
                // when any validation fails
                return false;
            }

        };
    }

    GeoServerAjaxFormLink computeLatLonBoundsLink(final Form refForm,
            final EnvelopePanel nativeBBox, final EnvelopePanel latLonPanel) {
        return new GeoServerAjaxFormLink("computeLatLon", refForm) {

            @Override
            protected void onClick(AjaxRequestTarget target, Form form) {
                // perform manual processing of the required fields
                nativeBBox.processInput();
                declaredCRS.processInput();
                
                ReferencedEnvelope nativeBounds = (ReferencedEnvelope) nativeBBox.getModelObject();
                try {
                    CatalogBuilder cb = new CatalogBuilder(GeoServerApplication.get().getCatalog());
                    latLonPanel.setModelObject(cb.getLatLonBounds(nativeBounds, declaredCRS.getCRS()));
                } catch(IOException e) {
                    LOGGER.log(Level.SEVERE, "Error computing the geographic BBOX", e);
                    error("Error computing the geographic bounds:" + e.getMessage());
                }
                target.addComponent(latLonPanel);
            }
        };
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
    
    /**
     * Checks a resource name is actually a valid one (WFS/WMS wise),
     * in particular, only word chars
     */
    static class ResourceNameValidator extends PatternValidator {
        public ResourceNameValidator() {
            super("[\\w][\\w.-]*");
        }
    }
}

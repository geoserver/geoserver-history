/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.services;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.UrlValidator;
import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.web.GeoServerHomePage;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.wicket.KeywordsEditor;
import org.geoserver.web.wicket.LiveCollectionModel;

/**
 * Base page for service administration pages.
 * <p>
 * Subclasses of this page should contribute form components in the {@link #build(ServiceInfo, Form)}
 * method. Each component that is added to the form should have a corresponding
 * markup entry of the following form:
 * <pre>
 * <wicket:extend>
 *   &lt;li>
 *       &lt;span>
 *         &lt;label><wicket:message key="maxFeatures.title">Maximum Features</wicket:message></label>
 *         &lt;input wicket:id="maxFeatures" class="field text" type="text"></input>
 *       &lt;/span>
 *       &lt;p class="instruct">
 *       &lt;/p>
 *     &lt;/li>
 *   
 * </wicket:extend>
 *   </pre>
 * </p>
 * 
 *@author Justin Deoliveira, The Open Planning Project
 *
 */
public abstract class BaseServiceAdminPage<T extends ServiceInfo> extends GeoServerSecuredPage {

    public BaseServiceAdminPage() {
        final IModel infoModel = new LoadableDetachableModel() {
            public Object load() {
                return getGeoServer().getService(getServiceClass());
            }
        };
        
        Form form = new Form( "form", new CompoundPropertyModel(infoModel));
        add(form);
        
        form.add(new Label("service.enabled", new StringResourceModel("service.enabled", this, null, new Object[]{
            getServiceName()
        })));
        form.add(new TextField("maintainer"));
        TextField onlineResource = new TextField("onlineResource");
        onlineResource.add(new UrlValidator());
        form.add(onlineResource);
        form.add(new CheckBox("enabled"));
        form.add(new CheckBox("citeCompliant"));
        form.add(new TextField("title"));
        form.add(new TextArea("abstract"));
        form.add(new KeywordsEditor("keywords", LiveCollectionModel.list(new PropertyModel(infoModel, "keywords"))));
        form.add(new TextField("fees"));
        form.add(new TextField("accessConstraints"));
        
        build(infoModel, form);
        
        SubmitLink submit = new SubmitLink("submit",new StringResourceModel( "save", (Component)null, null) ) {
            @Override
            public void onSubmit() {
                handleSubmit((T)infoModel.getObject());
                setResponsePage(GeoServerHomePage.class);
            }
        };
        form.add(submit);
        
        Button cancel = new Button( "cancel", new StringResourceModel( "cancel", (Component)null, null) ) {
            public void onSubmit() {
                setResponsePage(GeoServerHomePage.class);
            }
        };
        form.add( cancel );
        //cancel.setDefaultFormProcessing( false );
    }
    
    /**
     * The class of the service.
     * <p>
     * This value is used to obtain a reference to the service info 
     * object via {@link GeoServer#getService(Class)}. 
     * </p>
     */
    protected abstract Class<T> getServiceClass();
    
    /**
     * Builds the form for the page.
     * <p>
     * The form uses a {@link CompoundPropertyModel} so in the normal
     * case components do not need a model as its inherited from the parent.
     * This means that component id's should match the info bean property 
     * they correspond to.
     * </p>
     * @param info The service info object.
     * @param form The page form.
     */
    protected abstract void build(IModel info, Form form ); // {
        
    // }
    
    /**
     * Callback for submit.
     * <p>
     * This implementation simply saves the service. Subclasses may 
     * extend / override if need be.
     * </p>
     * @param info
     */
    protected void handleSubmit( T info ) {
        getGeoServer().save( info );
    }

    /**
     * The string to use when representing this service to users.
     * Subclasses must override.
     */
    protected abstract String getServiceName();
}

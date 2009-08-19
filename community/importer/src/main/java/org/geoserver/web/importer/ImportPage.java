/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.importer;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.importer.FeatureTypeImporter;
import org.geoserver.importer.ImporterThreadManager;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.wicket.GeoServerDialog;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.web.wicket.browser.ExtensionFileFilter;
import org.geoserver.web.wicket.browser.GeoServerFileChooser;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.directory.DirectoryDataStoreFactory;

/**
 * Sets up the import process and starts it up delegating the progress to {@link ImportProgressPage}
 */
@SuppressWarnings("serial")
public class ImportPage extends GeoServerSecuredPage {
    String directory = "";

    String project = "";
    
    GeoServerDialog dialog;

    private TextField dirField;
    
   
    public ImportPage(PageParameters params) {
        if("TRUE".equalsIgnoreCase((String) params.getString("afterCleanup")))
            info(new ParamResourceModel("rollbackSuccessful", this).getString());
        add(dialog = new GeoServerDialog("dialog"));
        
        Form form = new Form("form", new CompoundPropertyModel(this));
        add(form);

        dirField = new TextField("directory");
        dirField.add(new DirectoryValidator());
        dirField.setRequired(true);
        dirField.setOutputMarkupId(true);
        // dirField.setEnabled(false);
        form.add(dirField);
        form.add(chooserButton(form));
        TextField projectField = new TextField("project");
        projectField.setRequired(true);
        projectField.add(new ProjectValidator());
        projectField.add(new PatternValidator("\\w+"));
        projectField.add(new StringValidator.MaximumLengthValidator(6));
        form.add(projectField);

        SubmitLink submitLink = submitLink();
        form.add(submitLink);
        
        form.setDefaultButton(submitLink);
    }

    private Component chooserButton(Form form) {
        AjaxSubmitLink link =  new AjaxSubmitLink("chooser") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                dialog.setTitle(new ParamResourceModel("chooseDirectory", this));
                dialog.showOkCancel(target, new GeoServerDialog.DialogDelegate() {

                    @Override
                    protected Component getContents(String id) {
                        // use what the user currently typed
                    	File file = null;
                    	if(!dirField.getInput().trim().equals("")) {
                    		file = new File(dirField.getInput());
                    		if(!file.exists())
                    		    file = null;
                    	}
                    		
                        GeoServerFileChooser chooser = new GeoServerFileChooser(id, new Model(file));
                        chooser.setFilter(new Model(new ExtensionFileFilter(".shp")));
                        return chooser;
                    }

                    @Override
                    protected boolean onSubmit(AjaxRequestTarget target, Component contents) {
                        GeoServerFileChooser chooser = (GeoServerFileChooser) contents;
                        directory = ((File) chooser.getModelObject()).getAbsolutePath();
                        // clear the raw input of the field won't show the new model value
                        dirField.clearInput();
                        target.addComponent(dirField);
                        return true;
                    }
                    
                    @Override
                    public void onClose(AjaxRequestTarget target) {
                        // update the field with the user chosen value
                        target.addComponent(dirField);
                    }
                    
                });
                
            }
            
        };
        // otherwise the link won't trigger when the form contents are not valid
        link.setDefaultFormProcessing(false);
        return link;
    }

    SubmitLink submitLink() {
        return new SubmitLink("import") {

            @Override
            public void onSubmit() {
                try {
                    // build the datastore namespace URI
                    String ns = buildDatastoreNamespace();
                    
                    // build the workspace
                    WorkspaceInfo ws = getCatalog().getWorkspaceByName(project);
                    boolean workspaceNew = false;
                    if (ws == null) {
                        workspaceNew = true;
                        ws = getCatalog().getFactory().createWorkspace();
                        ws.setName(project);
                        NamespaceInfo nsi = getCatalog().getFactory().createNamespace();
                        nsi.setPrefix(project);
                        nsi.setURI(ns);
                        getCatalog().add(ws);
                        getCatalog().add(nsi);
                    }
                    
                    // build/reuse the store
                    String storeType = new DirectoryDataStoreFactory().getDisplayName();
                    Map<String, Serializable> params = new HashMap<String, Serializable>();
                    params.put(DirectoryDataStoreFactory.URLP.key, new File(directory).toURI().toURL().toString());
                    params.put(DirectoryDataStoreFactory.NAMESPACE.key, new URI(ns).toString());
                    
                    DataStoreInfo si;
                    StoreInfo preExisting = getCatalog().getStoreByName(ws, project, StoreInfo.class);
                    boolean storeNew = false;
                    if(preExisting != null) {
                        if(!(preExisting instanceof DataStoreInfo)) {
                            error(new ParamResourceModel("storeExistsNotVector", this, project));
                            return;
                        } 
                        si = (DataStoreInfo) preExisting;
                        if(!si.getType().equals(storeType) || !si.getConnectionParameters().equals(params)) {
                            error(new ParamResourceModel("storeExistsNotSame", this, project));
                            return;
                        } 
                        // make sure it's enabled, we just verified the directory exists
                        si.setEnabled(true);
                    } else {
                        storeNew = true;
                        CatalogBuilder builder = new CatalogBuilder(getCatalog());
                        builder.setWorkspace(ws);
                        si = builder.buildDataStore(project);
                        si.getConnectionParameters().putAll(params);
                        si.setEnabled(true);
                        si.setType(storeType);
                        
                        getCatalog().add(si);
                    }
                    
                    // build and run the importer
                    FeatureTypeImporter importer = new FeatureTypeImporter(si, null, getCatalog(), workspaceNew, storeNew);
                    ImporterThreadManager manager = (ImporterThreadManager) getGeoServerApplication().getBean("importerPool");
                    String importerKey = manager.startImporter(importer);
                    
                    setResponsePage(new ImportProgressPage(importerKey));
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error while setting up mass import", e);
                }

            }
        };
    }
    
    String buildDatastoreNamespace() {
        return "http://www.geoserver.org/" + project;
    }

    static class DirectoryValidator extends AbstractValidator {

        @Override
        protected void onValidate(IValidatable validatable) {
            String directory = (String) validatable.getValue();
            
            DataStore store = null;
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            try {
                // check the store can be built (we need to provide the namespace as well
                params.put(DirectoryDataStoreFactory.URLP.key, new File(directory).toURI().toURL());
                params.put(DirectoryDataStoreFactory.NAMESPACE.key, new URI("http://www.geoserver.org"));
                store = DataStoreFinder.getDataStore(params);
                if (store == null) {
                    error(validatable, "ImportPage.invalidPath");
                } else if (store.getTypeNames().length == 0) {
                    error(validatable, "ImportPage.noData");
                }
            } catch(Exception e) {
                error(validatable, "ImportPage.noData");
            }
        }
    }
    
    class ProjectValidator extends AbstractValidator {

        @Override
        protected void onValidate(IValidatable validatable) {
            String project = (String) validatable.getValue();
            
            // new workspace? if so, good
            WorkspaceInfo ws = getCatalog().getWorkspaceByName(project);
            if(ws == null)
                return;
            
            // new store too?
            StoreInfo store = getCatalog().getStoreByName(ws, project, StoreInfo.class);
            if(store != null)
                error(validatable, "ImportPage.duplicateStore", 
                        Collections.singletonMap("project", project));
        }
        
    }
    
    
}

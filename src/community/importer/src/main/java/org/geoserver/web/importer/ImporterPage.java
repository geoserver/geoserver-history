package org.geoserver.web.importer;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
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
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.directory.DirectoryDataStoreFactory;

@SuppressWarnings("serial")
public class ImporterPage extends GeoServerSecuredPage {
    String directory = "";

    String project = "";
    
    public ImporterPage() {
        Form form = new Form("form", new CompoundPropertyModel(this));
        add(form);

        TextField directory = new TextField("directory");
        directory.add(new DirectoryValidator());
        directory.setRequired(true);
        form.add(directory);
        TextField projectField = new TextField("project");
        projectField.setRequired(true);
        projectField.add(new ProjectValidator());
        projectField.add(new PatternValidator("\\w+"));
        projectField.add(new StringValidator.MaximumLengthValidator(10));
        form.add(projectField);

        SubmitLink submitLink = submitLink();
        form.add(submitLink);
        
        form.setDefaultButton(submitLink);
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
                    if (ws == null) {
                        ws = getCatalog().getFactory().createWorkspace();
                        ws.setName(project);
                        NamespaceInfo nsi = getCatalog().getFactory().createNamespace();
                        nsi.setPrefix(project);
                        nsi.setURI(ns);
                        getCatalog().add(ws);
                        getCatalog().add(nsi);
                    }

                    // build the store info and save it (TODO: check the store is not already there)
                    CatalogBuilder builder = new CatalogBuilder(getCatalog());
                    builder.setWorkspace(ws);
                    DataStoreInfo si = builder.buildDataStore(project);
                    Map<String, Serializable> params = si.getConnectionParameters();
                    params.put(DirectoryDataStoreFactory.URLP.key, new File(directory).toURL());
                    params.put(DirectoryDataStoreFactory.NAMESPACE.key, new URI(ns));
                    si.setEnabled(true);
                    si.setType(new DirectoryDataStoreFactory().getDisplayName());
                    getCatalog().add(si);
                    
                    // build and run the importer
                    FeatureTypeImporter importer = new FeatureTypeImporter(si, null, getCatalog());
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
                params.put(DirectoryDataStoreFactory.URLP.key, new File(directory).toURL());
                params.put(DirectoryDataStoreFactory.NAMESPACE.key, new URI("http://www.geoserver.org"));
                store = DataStoreFinder.getDataStore(params);
                if (store == null) {
                    error(validatable, "ImporterPage.invalidPath");
                } else if (store.getTypeNames().length == 0) {
                    error(validatable, "ImporterPage.noData");
                }
            } catch(Exception e) {
                error(validatable, "ImporterPage.noData");
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
                error(validatable, "ImporterPage.duplicateStore", 
                        Collections.singletonMap("project", project));
        }
        
    }
}

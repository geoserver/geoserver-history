package org.geoserver.web.data.workspace;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerSecuredPage;

public class WorkspaceNewPage extends GeoServerSecuredPage {

    Form form;
    TextField nsUriTextField;
    
    public WorkspaceNewPage() {
        WorkspaceInfo ws = getCatalog().getFactory().createWorkspace();
        
        form = new Form( "form", new CompoundPropertyModel( ws ) ) {
            @Override
            protected void onSubmit() {
                Catalog catalog = getCatalog();
                
                WorkspaceInfo ws = (WorkspaceInfo) form.getModelObject();
                String nsURI = nsUriTextField.getModelObjectAsString();
                if ( nsURI == null || "".equals( nsURI ) ) {
                    nsURI = "http://" + ws.getName();
                }
                
                NamespaceInfo ns = catalog.getFactory().createNamespace();
                ns.setPrefix ( ws.getName() );
                ns.setURI( nsURI );
                
                catalog.add( ws );
                catalog.add( ns );
                
                //TODO: set the response page to be the ediut 
                setResponsePage(WorkspacePage.class );
            }
        };
        add(form);
        
        TextField nameTextField = new TextField( "name", String.class );
        form.add( nameTextField.setRequired(true) );
        
        nsUriTextField = new TextField( "uri", new Model() );
        form.add( nsUriTextField );
        
        SubmitLink submitLink = new SubmitLink( "submit", form );
        form.add( submitLink );
        
        AjaxLink cancelLink = new AjaxLink( "cancel" ) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(WorkspacePage.class);
            }
        };
        form.add( cancelLink );
        
    }
}

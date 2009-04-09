/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.layergroup;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.web.GeoServerSecuredPage;

/**
 * Allows creation of a new layer group
 */
@SuppressWarnings("serial")
public class LayerGroupNewPage extends GeoServerSecuredPage {

    public LayerGroupNewPage() {
        LayerGroupInfo lg = getCatalog().getFactory().createLayerGroup();
        Form form = new Form( "form", new CompoundPropertyModel( lg ) );
        add(form);
        
        form.add( new TextField( "name" ) );
        form.add( new SubmitLink( "submit", form ) {
            @Override
            public void onSubmit() {
                LayerGroupInfo lg = (LayerGroupInfo) getForm().getModelObject();
                
                Catalog catalog = getCatalog();
                try {
                    catalog.add( lg );
                    
                    lg = catalog.getLayerGroup( lg.getId() );
                    setResponsePage(new LayerGroupEditPage( lg ) );
                }
                catch( Exception e ) {
                    error( e );
                }
            }
            
        });
        form.add( new SubmitLink( "cancel", form ) {
            @Override
            public void onSubmit() {
                setResponsePage(LayerGroupPage.class);
            }
        });
    }
}

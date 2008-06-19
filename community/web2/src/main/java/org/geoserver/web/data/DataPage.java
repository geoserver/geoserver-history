package org.geoserver.web.data;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.data.tree.DataTreeTable;

public class DataPage extends GeoServerBasePage {

    public DataPage( ) {
    	final DataTreeTable tree = new DataTreeTable( "dataTree", this );
    	add( tree );
    	
    	Form form = new Form("controlForm");
        form.add(new AjaxButton("collapseAll") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                tree.getTreeState().collapseAll();
                target.addComponent(tree);
            }
        });
        add(form);
    }
}

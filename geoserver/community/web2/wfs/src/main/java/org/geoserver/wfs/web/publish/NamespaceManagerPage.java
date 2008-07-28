package org.geoserver.wfs.web.publish;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.Item;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.web.GeoServerBasePage;

public class NamespaceManagerPage extends GeoServerBasePage {

    private String newPrefix;
    private String newURI;

	@SuppressWarnings("serial")
	public NamespaceManagerPage(){
        newPrefix = "prefix";
        newURI = "URI";

	    add(new RefreshingView("namespaces"){
		    @Override
		    protected void populateItem(final Item item) {
                item.add(
                    new SimpleAttributeModifier(
                        "class",
                        item.getIndex() % 2 == 0 ? "even" : "odd"
                        ) 
                    );
                         
		    	final NamespaceInfo info = (NamespaceInfo)item.getModelObject();
		    	item.add(new AjaxEditableLabel("prefix", new PropertyModel(info, "prefix")));
		    	item.add(new AjaxEditableLabel("URI", new PropertyModel(info, "URI")));
		    	item.add(new Link("delete", new PropertyModel(info, "delete")){
		    		@Override
		    		public void onClick() {
		    			getCatalog().remove(info);
		    		}
		    	});
		    }

            @Override
            protected Iterator getItemModels(){
                List<IModel> models = new ArrayList<IModel>();
                for (NamespaceInfo info : getCatalog().getNamespaces())
                    models.add(new CompoundPropertyModel(info));
                return models.iterator();
            }
	    }
	    );
        WebMarkupContainer container = new WebMarkupContainer("input");
        container.add(
            new SimpleAttributeModifier(
                "class",
                getCatalog().getStyles().size() % 2 == 0 ? "even" : "odd"
                ) 
            );
        add(container);
        container.add(new AjaxEditableLabel("newPrefix", new PropertyModel(this, "newPrefix")));
        container.add(new AjaxEditableLabel("newURI", new PropertyModel(this, "newURI")));
        container.add(new Link("add"){
            public void onClick() {
                NamespaceInfo info = getCatalog().getFactory().createNamespace();
                info.setURI(newURI);
                info.setPrefix(newPrefix);
                getCatalog().add(info);
            }
        });
	}
}

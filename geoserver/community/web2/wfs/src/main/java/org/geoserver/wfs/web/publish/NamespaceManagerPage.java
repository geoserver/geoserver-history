package org.geoserver.wfs.web.publish;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
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
                         
		    	item.add(new RevertingAjaxEditableLabel("prefix", new PropertyModel(item.getModel(), "prefix")));
		    	item.add(new RevertingAjaxEditableLabel("URI", new PropertyModel(item.getModel(), "URI")));
		    	item.add(new Link("delete", new PropertyModel(item.getModel(), "delete")){
		    		@Override
		    		public void onClick() {
		    			getCatalog().remove((NamespaceInfo)item.getModel().getObject());
		    		}
		    	});
		    }

            @Override
            protected Iterator<?> getItemModels(){
                List<IModel> models = new ArrayList<IModel>();
                for (NamespaceInfo info : getCatalog().getNamespaces()){
                    final String prefix = info.getPrefix();
                    models.add(new CompoundPropertyModel(new LoadableDetachableModel(){
                        public Object load(){
                            return getCatalog().getNamespaceByPrefix(prefix);
                        }
                    }
                    ));
                }
                return models.iterator();
            }
	    }
	    );

        Form form  = new Form("input");
        form.add(
            new SimpleAttributeModifier(
                "class",
                getCatalog().getStyles().size() % 2 == 0 ? "even" : "odd"
                ) 
            );
        add(form);
        form.add(new TextField("newPrefix", new PropertyModel(this, "newPrefix")));
        form.add(new TextField("newURI", new PropertyModel(this, "newURI")));
        form.add(new Button("add"));
	}

    private static class RevertingAjaxEditableLabel extends AjaxEditableLabel {
        private String placeholder;
        public RevertingAjaxEditableLabel(String id, IModel model){
            super(id, model);
        }

        protected void onEdit(AjaxRequestTarget target){
            super.onEdit(target);
            placeholder = (String)getModel().getObject();
        }

        protected void onCancel(AjaxRequestTarget target){
            super.onCancel(target);
            getModel().setObject(placeholder);
        }
    }
}

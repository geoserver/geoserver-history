package org.geoserver.wms.web.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.web.GeoServerBasePage;

@SuppressWarnings("serial")
public class StylesPage extends GeoServerBasePage {
    String name;

    public StylesPage(){
        name = "New Style";
        add(new RefreshingView("styles"){
            @Override
            protected Iterator<?> getItemModels(){
                List<IModel> styles = new ArrayList<IModel>();
                for (StyleInfo info : getCatalog().getStyles()){
                    final String id = info.getId();
                    styles.add(new CompoundPropertyModel(new LoadableDetachableModel(){
                        public Object load() {
                            return getCatalog().getStyle(id);
                        }
                    }));
                }
                return styles.iterator();
            }

            @Override
            protected void populateItem(final Item item){
                item.add(
                    new SimpleAttributeModifier(
                        "class", 
                        item.getIndex() % 2 == 0 ? "even" : "odd"
                        )
                    );
                item.add(new AjaxEditableLabel("name"));
                item.add(new Link("edit"){
                        @Override
                        public void onClick(){
                            setResponsePage(new StyleEditorPage((StyleInfo)item.getModelObject()));
                        }
                    });
                item.add(new Link("delete"){
                        @Override
                        public void onClick(){
                            getCatalog().remove((StyleInfo)item.getModelObject());
                        }
                    });
            }
        });
        
        WebMarkupContainer container = new WebMarkupContainer("input");
        container.add(
                new SimpleAttributeModifier(
                    "class",
                    getCatalog().getStyles().size() % 2 == 0 ? "even" : "odd"
                    )
                );
        add(container);
        container.add(new AjaxEditableLabel("name", new PropertyModel(this, "name")));
        container.add(new Link("add"){
            @Override
            public void onClick(){
            }
        });
    }
}

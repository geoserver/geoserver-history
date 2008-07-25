package org.geoserver.wms.web.data;

import org.geoserver.web.GeoServerBasePage;
import org.geoserver.catalog.StyleInfo;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

public class StylesPage extends GeoServerBasePage {

    private String name;

    public StylesPage(){
        name = "New Style";
        add(new RefreshingView("styles"){
            @Override
            protected Iterator getItemModels(){
                List<IModel> styles = new ArrayList<IModel>();
                for (StyleInfo info : getCatalog().getStyles())
                    styles.add(new CompoundPropertyModel(info));;
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
                final StyleInfo info = (StyleInfo)item.getModelObject();
                item.add(new AjaxEditableLabel("name"));
                item.add(new Link("edit"){
                        @Override
                        public void onClick(){
                            setResponsePage(new StyleEditorPage(info));
                        }
                    });
                item.add(new Link("delete"){
                        @Override
                        public void onClick(){
                            getCatalog().remove(info);
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

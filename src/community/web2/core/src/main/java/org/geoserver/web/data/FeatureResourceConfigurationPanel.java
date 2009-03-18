package org.geoserver.web.data;

import java.beans.PropertyDescriptor;
import java.io.IOException;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.geoserver.catalog.AttributeTypeInfo;
import org.geoserver.catalog.FeatureTypeInfo;

@SuppressWarnings("serial")
public class FeatureResourceConfigurationPanel extends ResourceConfigurationPanel {

    public FeatureResourceConfigurationPanel(String id, IModel model) {
        super(id, model);
        
        // just use the direct attributes, this is not editable atm
        final FeatureTypeInfo typeInfo = (FeatureTypeInfo) model.getObject();
        ListView attributes = new ListView("attributes", typeInfo.getAttributes()) {

            @Override
            protected void populateItem(ListItem item) {
                
                // odd/even style
                item.add(new SimpleAttributeModifier("class",
                        item.getIndex() % 2 == 0 ? "even" : "odd"));

                // dump the attribute information we have
                AttributeTypeInfo attribute = (AttributeTypeInfo) item.getModelObject();
                item.add(new Label("name", attribute.getName()));
                item.add(new Label("minmax", attribute.getMinOccurs() + "/" + attribute.getMaxOccurs()));
                try {
                    org.opengis.feature.type.PropertyDescriptor pd = typeInfo.getFeatureType().getDescriptor(attribute.getName());
                    String typeName = pd.getType().getBinding().getSimpleName();
//                    String typeName = attribute.getAttribute().getType().getBinding().getSimpleName();
                    item.add(new Label("type", typeName));
                    item.add(new Label("nillable", pd.isNillable() + ""));
                } catch(IOException e) {
                    item.add(new Label("type", "?"));
                    item.add(new Label("nillable", "?"));
                }
            }
            
        };
        add(attributes);
    }
}

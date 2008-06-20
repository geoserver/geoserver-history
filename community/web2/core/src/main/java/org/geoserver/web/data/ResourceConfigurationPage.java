package org.geoserver.web.data;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.publish.LayerConfigurationPanel;
import org.geoserver.web.publish.LayerConfigurationPanelInfo;

public class ResourceConfigurationPage extends GeoServerBasePage {

    private ResourceInfo myResourceInfo;
    private IModel myResourceModel;
    private IModel myLayerModel;

    public ResourceConfigurationPage(ResourceInfo info){
        myResourceInfo = info;
        myResourceModel = new CompoundPropertyModel(myResourceInfo);
        myLayerModel = new CompoundPropertyModel(info.getCatalog().getLayers(info));
    }

    public ResourceConfigurationPage(LayerInfo info) {
        myResourceInfo = info.getResource();
        myResourceModel = new CompoundPropertyModel(myResourceInfo);
        myLayerModel = new CompoundPropertyModel(info);

        add(new Label("resourcename", myResourceInfo.getId()));
        Form theForm = new Form("resource", myResourceModel);
        add(theForm);
        theForm.add(new ResourceConfigurationSectionListView("resources"));
        theForm.add(new LayerConfigurationSectionListView("layers"));
    }

    private class ResourceConfigurationSectionListView extends ListView {
        private static final long serialVersionUID = -6575960326680386479L;

        public ResourceConfigurationSectionListView(String id) {
            super(id, 
                    ((GeoServerApplication)getGeoServerApplication())
                    .getBeansOfType(ResourceConfigurationPanelInfo.class)
                 );
        }

        @Override
            protected void populateItem(ListItem item) {
                ResourceConfigurationPanelInfo panelInfo = (ResourceConfigurationPanelInfo) item
                    .getModelObject();
                try {
                    ResourceConfigurationPanel panel = panelInfo
                        .getComponentClass()
                        .getConstructor(String.class, IModel.class)
                        .newInstance("content", myResourceModel);
                    item.add((Component) panel);
                } catch (Exception e) {
                    throw new WicketRuntimeException("Failed to add pluggable resource configuration panels", e);
                }
            }
    }
    private class LayerConfigurationSectionListView extends ListView {
        private static final long serialVersionUID = -6575960326680386479L;

        public LayerConfigurationSectionListView(String id) {
            super(id, 
                    ((GeoServerApplication)getGeoServerApplication())
                    .getBeansOfType(LayerConfigurationPanelInfo.class)
                 );
        }

        @Override
            protected void populateItem(ListItem item) {
                LayerConfigurationPanelInfo panelInfo = (LayerConfigurationPanelInfo) item
                    .getModelObject();
                try {
                    LayerConfigurationPanel panel = panelInfo
                        .getComponentClass()
                        .getConstructor(String.class, IModel.class)
                        .newInstance("content", myLayerModel);
                    item.add((Component) panel);
                } catch (Exception e) {
                    throw new WicketRuntimeException("Failed to add pluggable layer configuration panels", e);
                }
            }
    }
}

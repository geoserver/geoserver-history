package org.geoserver.web.data;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Button;
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
    private LayerInfo myLayerInfo;
    private IModel myResourceModel;
    private IModel myLayerModel;

    private boolean isNew;

    public ResourceConfigurationPage(ResourceInfo info, boolean isNew){
        myResourceInfo = info;
        myResourceModel = new CompoundPropertyModel(myResourceInfo);
        myLayerInfo  = info.getCatalog().getLayers(info).get(0);
        myLayerModel = new CompoundPropertyModel(myLayerInfo);
        this.isNew = isNew;

        initComponents();
    }

    public ResourceConfigurationPage(LayerInfo info, boolean isNew) {
        myResourceInfo = info.getResource();
        myResourceModel = new CompoundPropertyModel(myResourceInfo);
        myLayerInfo = info;
        myLayerModel = new CompoundPropertyModel(info);
        this.isNew = isNew;

        initComponents();
    }

    private void initComponents(){
        add(new Label("resourcename", myResourceInfo.getId()));
        Form theForm = new Form("resource", myResourceModel);
        add(theForm);
        theForm.add(new ResourceConfigurationSectionListView("resources"));
        theForm.add(new LayerConfigurationSectionListView("layers"));
        theForm.add(new Button("saveButton"){
            public void onSubmit(){
                if (isNew){
                    myResourceInfo.getCatalog().add(myResourceInfo);
                    myResourceInfo.getCatalog().add(myLayerInfo);
                } else {
                    myResourceInfo.getCatalog().save(myResourceInfo);
                    myResourceInfo.getCatalog().save(myLayerInfo);
                }
            }
        });
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

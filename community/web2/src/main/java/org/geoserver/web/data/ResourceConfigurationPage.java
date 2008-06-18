package org.geoserver.web.data;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.GeoServerApplication;

public class ResourceConfigurationPage extends GeoServerBasePage {

	private List<ResourceConfigurationInfo> myConfigurators;
	private ResourceInfo myResourceInfo;
	
	public ResourceConfigurationPage(ResourceInfo info) {
		myResourceInfo = info;
		
		add(new Label("resourcename", myResourceInfo.getId()));
        Form theForm = new Form("resource", new CompoundPropertyModel(myResourceInfo));
        add(theForm);
        theForm.add(new ConfigurationSectionListView("configuration"));
	}

	private class ConfigurationSectionListView extends ListView {

		public ConfigurationSectionListView(String id) {
			super(id, ((GeoServerApplication)getGeoServerApplication()).getBeansOfType(ResourceConfigurationInfo.class));
		}

		@Override
		protected void populateItem(ListItem item) {
			ResourceConfigurationInfo panelInfo = (ResourceConfigurationInfo) item
					.getModelObject();
			try {
				ResourceConfigurationPanel panel = panelInfo
						.getComponentClass().getConstructor(String.class,
								ResourceInfo.class).newInstance("content",
								myResourceInfo);
				item.add((Component) panel);
			} catch (Exception e) {
                e.printStackTrace();
			}
		}
	}
}

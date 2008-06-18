package org.geoserver.web.data;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.GeoServerBasePage;

public class ResourceConfigurationPage extends GeoServerBasePage {

	private ResourceInfo myResourceInfo;
	
	public ResourceConfigurationPage(ResourceInfo info) {
		myResourceInfo = info;
		
		add(new Label("resourcename", myResourceInfo.getId()));
        Form theForm = new Form("resource", new CompoundPropertyModel(myResourceInfo));
        add(theForm);
        theForm.add(new ConfigurationSectionListView("configuration"));
	}

	private class ConfigurationSectionListView extends ListView {

		private static final long serialVersionUID = -6575960326680386479L;

		public ConfigurationSectionListView(String id) {
			super(id, ((GeoServerApplication)getGeoServerApplication()).getBeansOfType(ResourceConfigurationPanelInfo.class));
		}

		@Override
		protected void populateItem(ListItem item) {
			ResourceConfigurationPanelInfo panelInfo = (ResourceConfigurationPanelInfo) item
					.getModelObject();
			try {
				ResourceConfigurationPanel panel = panelInfo
						.getComponentClass().getConstructor(String.class).newInstance("content");
				item.add((Component) panel);
			} catch (Exception e) {
                e.printStackTrace();
			}
		}
	}
}

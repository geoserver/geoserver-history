package org.vfny.geoserver.ui;

import wicket.Component;
import wicket.extensions.markup.html.repeater.data.DataView;
import wicket.extensions.markup.html.repeater.data.IDataProvider;
import wicket.extensions.markup.html.repeater.refreshing.Item;

/**
 * Can be used to populate a DataView with a list of web components. Used for
 * links, but it's more general than that.
 * 
 * @author wolf
 * 
 */
public class ComponentDataView extends DataView {

	public ComponentDataView(String id, IDataProvider provider) {
		super(id, provider);
	}

	protected void populateItem(Item item) {
		Component c = (Component) item.getModelObject();
		item.add(c);
	}

}

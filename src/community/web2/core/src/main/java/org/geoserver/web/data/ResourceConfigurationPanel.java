/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data;

import java.util.logging.Logger;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.geoserver.catalog.ResourceInfo;
import org.geotools.util.logging.Logging;

@SuppressWarnings("serial")
public class ResourceConfigurationPanel extends Panel {
    protected static Logger LOGGER = Logging.getLogger(ResourceConfigurationPanel.class);

	public ResourceConfigurationPanel(String id, IModel model){
		super(id, model);
	}
	
	public ResourceInfo getResourceInfo(){
		return (ResourceInfo)getModelObject();
	}
}

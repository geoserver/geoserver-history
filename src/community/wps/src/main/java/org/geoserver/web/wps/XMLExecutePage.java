/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wps;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

/**
 * Shows the Execute xml
 */
public class XMLExecutePage extends WebPage {
	String xml;

	public XMLExecutePage(final ModalWindow container, final ModalWindow responseWindow, String initialXml) {
		this.xml = initialXml;
		
		add(new Label("xml", new PropertyModel(this, "xml")));
	}
	
	
}

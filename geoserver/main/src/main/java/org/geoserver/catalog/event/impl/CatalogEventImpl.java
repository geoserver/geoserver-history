package org.geoserver.catalog.event.impl;

import org.geoserver.catalog.event.CatalogEvent;

public class CatalogEventImpl implements CatalogEvent {

	Object source;
	
	
	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}
}

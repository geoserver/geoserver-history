package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.LatLonBoundingBox;

@XmlRootElement
public class LatLonBoundingBoxJSONHolder  {

	private LatLonBoundingBox latLongBoundingBox;

	public LatLonBoundingBoxJSONHolder() {
	}

	public LatLonBoundingBoxJSONHolder(final LatLonBoundingBox latLongBoundingBox) {
		this.latLongBoundingBox = latLongBoundingBox;
	}

	public final LatLonBoundingBox getLatLongBoundingBox() {
		return this.latLongBoundingBox;
	}

	public final void setLatLongBoundingBox(final LatLonBoundingBox latLongBoundingBox) {
		this.latLongBoundingBox = latLongBoundingBox;
	}
}

package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.NativeBoundingBox;

@XmlRootElement
public class NativeBoundingBoxJSONHolder {

	private NativeBoundingBox nativeBoundingBox;

	public NativeBoundingBoxJSONHolder() {
	}

	public NativeBoundingBoxJSONHolder(final NativeBoundingBox nativeBoundingBox) {
		this.nativeBoundingBox = nativeBoundingBox;
	}

	public final NativeBoundingBox getNativeBoundingBox() {
		return this.nativeBoundingBox;
	}

	public final void setNativeBoundingBox(final NativeBoundingBox nativeBoundingBox) {
		this.nativeBoundingBox = nativeBoundingBox;
	}
}

package org.openplans.geoserver.gt.binding;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import org.openplans.geoserver.binding.KvpBinding;

public class DimensionBinding extends KvpBinding {

	public DimensionBinding(List keys, String key) {
		super(keys, key);
	}

	public Object bind(Map kvp) {
		return new Dimension(
			Integer.parseInt((String) kvp.get("width")),
			Integer.parseInt((String) kvp.get("height"))
		);
	}

}

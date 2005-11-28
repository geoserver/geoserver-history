package org.openplans.geoserver.gt.binding;

import org.openplans.geoserver.binding.KvpBinding;
import org.openplans.geoserver.binding.KvpRequestReader;

public class DimensionBinding extends KvpRequestReader implements KvpBinding {

	public boolean canBind(String key, String value) {
		return "width".equalsIgnoreCase(key) || 
			"height".equalsIgnoreCase(key);
	}

	public Object bind(String key, String value) {
		return Integer.valueOf(value);
	}

}

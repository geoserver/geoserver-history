package org.openplans.geoserver.binding;

import java.util.List;
import java.util.Map;

public abstract class KvpBinding {

	private List keys;
	private String key;
	
	public KvpBinding(List keys, String key) {
		this.keys = keys;
		this.key = key;
	}
	
	public List getKeys() {
		return keys;
	}
	
	public String getKey() {
		return key;
	}
	
	public abstract Object bind(Map kvp);
}

package org.openplans.geoserver.binding;

public interface KvpBinding {

	boolean canBind(String key, String value);
	
	Object bind(String key, String value);
}

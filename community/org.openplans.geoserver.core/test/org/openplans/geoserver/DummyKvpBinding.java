package org.openplans.geoserver;

import java.util.List;
import java.util.Map;

import org.openplans.geoserver.binding.KvpBinding;

public class DummyKvpBinding extends KvpBinding {

	public DummyKvpBinding(List keys, String key) {
		super(keys, key);
	}

	public Object bind(Map kvp) {
		return kvp.get("dummy");
	}

}

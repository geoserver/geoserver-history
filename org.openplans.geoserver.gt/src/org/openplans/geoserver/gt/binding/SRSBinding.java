package org.openplans.geoserver.gt.binding;

import java.util.List;
import java.util.Map;

import org.geotools.referencing.CRS;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.openplans.geoserver.binding.KvpBinding;

public class SRSBinding extends KvpBinding {

	public SRSBinding(List keys, String key) {
		super(keys, key);
	}

	public Object bind(Map kvp) {
		try {
			return CRS.decode((String)kvp.get("srs"));
		} 
		catch (NoSuchAuthorityCodeException e) {
			new IllegalArgumentException((String)kvp.get("srs")).initCause(e);
		}
		
		return null;
	}

}

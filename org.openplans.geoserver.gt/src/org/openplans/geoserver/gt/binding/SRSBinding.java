package org.openplans.geoserver.gt.binding;

import org.geotools.referencing.CRS;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.openplans.geoserver.binding.KvpBinding;
import org.openplans.geoserver.binding.KvpRequestReader;

public class SRSBinding extends KvpRequestReader implements KvpBinding {

	public boolean canBind(String key, String value) {
		return "srs".equalsIgnoreCase(key);
	}

	public Object bind(String key, String value) {
		try {
			return CRS.decode(value);
		} 
		catch (NoSuchAuthorityCodeException e) {
			new IllegalArgumentException(value).initCause(e);
		}
		
		return null;
	}

}

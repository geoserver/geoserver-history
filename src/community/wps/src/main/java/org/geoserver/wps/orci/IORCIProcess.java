package org.geoserver.wps.orci;

import java.util.Map;

import org.geotools.data.Parameter;
import org.opengis.util.InternationalString;

public interface IORCIProcess {
	
	public InternationalString getDescription();
	
	public InternationalString getTitle();
	
	Map<String, Parameter<?>> getParameterInfo();
	
	Map<String, Parameter<?>> getResultInfo(Map<String, Object> inputs);

}

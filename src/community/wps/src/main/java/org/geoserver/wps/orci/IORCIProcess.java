/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
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

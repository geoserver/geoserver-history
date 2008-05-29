/**
 * @author lreed@refractions.net
 */

package org.geoserver.wps;

import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.util.LegacyServicesReader;
import org.geoserver.config.util.ServiceLoader;

public class WPSLoader extends ServiceLoader
{
	public ServiceInfo load(LegacyServicesReader reader, GeoServer geoServer) throws Exception
	{
		WPSInfo wps = new WPSInfoImpl();
		
		return wps;
	}
}
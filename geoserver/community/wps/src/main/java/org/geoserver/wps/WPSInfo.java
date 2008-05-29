/**
 * @author lreed@refractions.net
 */

package org.geoserver.wps;

import org.geoserver.config.ServiceInfo;

public interface WPSInfo extends ServiceInfo
{
	static enum Version
	{
		V_10;
	};
	
	static enum Operations
	{
		GETCAPABILITIES
		{
			public int getCode()
			{
                return 0;
            }
		},
		DESCRIBEPROCESS
		{
			public int getCode()
			{
                return 1;
            }
		},
		EXECUTE
		{
			public int getCode()
			{
                return 2;
            }
		}
	};
}
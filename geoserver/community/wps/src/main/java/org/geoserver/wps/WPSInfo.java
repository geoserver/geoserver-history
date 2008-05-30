/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

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
package org.geoserver.printing;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import org.springframework.web.servlet.mvc.ServletWrappingController;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * Wrapper for Spring's ServletWrappingController to allow use of GeoServer's config dir.
 * 
 * @author Alan Gerber, The Open Planning Project
 *
 */
public class PrintingServletWrappingController extends
		ServletWrappingController {
	
    private Logger LOG = org.geotools.util.logging.Logging.getLogger("org.geoserver.printing");
	
	public void setInitParameters(Properties initParameters)
	{
		/*find the config parameter and update it so it points to $GEOSERVER_DATA_DIR/printing/$CONFIG */
		String configProp = initParameters.getProperty("config");		
		try{
			File dir = GeoserverDataDirectory.findCreateConfigDir("printing");
			File qualifiedConfig = new File(dir, configProp);
			initParameters.setProperty("config", qualifiedConfig.getCanonicalPath());			
		}
		catch(org.vfny.geoserver.global.ConfigurationException e){
			LOG.warning("Explosion while attempting to access/create config directory for MapFish " +
					"printing module.  Module will fail when run. Config exception is: " + e);
		}
		catch(java.io.IOException e){
			LOG.warning("Explosion while calculating canonical path for MapFish printing servlet. " +
					"Module will fail when run.  IO Exception is: " + e);
		}
		super.setInitParameters(initParameters);
	}

}

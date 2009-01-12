package org.geoserver.security.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.security.model.LayerSecurityModel;
import org.geoserver.security.model.configuration.ConfiguratorLayers;
import org.geoserver.security.model.configuration.ConfigureChainOfResponsibility;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * DAOConfigurationProperties for loading the security properties files.
 * 
 * @author Francesco Izzi (geoSDI)
 */

public class DAOConfigurationProperties implements IDAOConfiguration {

	static final Logger LOGGER = Logging
			.getLogger(DAOConfigurationProperties.class);

	public ConfigureChainOfResponsibility loadConfiguration()
			throws DAOException {
		ConfigureChainOfResponsibility chain = null;
		try {
			File security = GeoserverDataDirectory.findConfigDir(
					GeoserverDataDirectory.getGeoserverDataDirectory(),
					"security");
			Properties properties = loadProperties(security.getAbsolutePath()
					+ "/" + "layers.properties");

			ConfiguratorLayers confLayers = new ConfiguratorLayers();
			for (Map.Entry entry : properties.entrySet()) {
				final String ruleKey = (String) entry.getKey();
				final String ruleValue = (String) entry.getValue();
				final String rule = ruleKey + "=" + ruleValue;
				String[] elements = parseElements(ruleKey);
	            final String workspace = elements[0];
	            final String layerName = elements[1];
	            final String modeAlias = elements[2];				
				LayerSecurityModel row = new LayerSecurityModel(workspace,layerName,modeAlias,ruleValue);
				confLayers.addLayerSecurityModel(row);
			}

			chain = new ConfigureChainOfResponsibility();
			chain.addConfiguratore(confLayers);

		} catch (IOException e) {
			throw new DAOException("Error while loading configuration\n"
					+ e.getMessage());
		} catch (ConfigurationException e) {
			throw new DAOException("Error while loading configuration\n"
					+ e.getMessage());
		}

		return chain;
	}

	private Properties loadProperties(String fileName) throws IOException {
		Properties properties = new Properties();
		InputStream stream = null;
		try {
			stream = new FileInputStream(fileName);
			properties.load(stream);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		return properties;
	}

	private String[] parseElements(String path) {
		// regexp: ignore extra spaces
		return path.split("\\s*\\.\\s*");
	}

}

package org.geoserver.security.model.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * ConfigureChainOfResponsibility
 * 
 * @author Francesco Izzi (geoSDI)
 */

public class ConfigureChainOfResponsibility {

	private List<IConfigurator> chains = new ArrayList<IConfigurator>();

	public void addConfiguratore(IConfigurator configuratore) {

		this.chains.add(configuratore);

	}

	public void run(ConfigurationSingleton configurator) {

		for (IConfigurator configuratore : chains) {

			configuratore.configure(configurator);

		}

	}
}

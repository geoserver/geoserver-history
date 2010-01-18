package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataStore {

	protected ConnectionParameters connectionParameters;

	protected boolean enabled;

	protected String featureTypes;

	protected String name;

	protected Workspace workspace;

	/**
	 * @return the connectionParameters
	 */
	public ConnectionParameters getConnectionParameters() {
		return this.connectionParameters;
	}

	/**
	 * @return the featureTypes
	 */
	public String getFeatureTypes() {
		return this.featureTypes;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the workspace
	 */
	public Workspace getWorkspace() {
		return this.workspace;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * @param connectionParameters the connectionParameters to set
	 */
	public void setConnectionParameters(final ConnectionParameters connectionParameters) {
		this.connectionParameters = connectionParameters;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param featureTypes the featureTypes to set
	 */
	public void setFeatureTypes(final String featureTypes) {
		this.featureTypes = featureTypes;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param workspace the workspace to set
	 */
	public void setWorkspace(final Workspace workspace) {
		this.workspace = workspace;
	}
}

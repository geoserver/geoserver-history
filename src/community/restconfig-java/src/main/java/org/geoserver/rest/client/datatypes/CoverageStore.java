package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CoverageStore {

	protected String url;

	protected boolean enabled;

	protected String href;

	protected String name;

	protected String nativeFormat;

	protected CoverageStoreType type;

	protected Workspace workspace;

	public CoverageStore() {
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return this.href;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the type
	 */
	public CoverageStoreType getType() {
		return this.type;
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
	 * @param url the url to set
	 */
	public void setUrl(final String url) {
		this.url = url;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param href the href to set
	 */
	public void setHref(final String href) {
		this.href = href;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(final CoverageStoreType type) {
		this.type = type;
	}

	/**
	 * @param workspace the workspace to set
	 */
	public void setWorkspace(final Workspace workspace) {
		this.workspace = workspace;
	}
}

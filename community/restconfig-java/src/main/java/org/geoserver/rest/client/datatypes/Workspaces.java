package org.geoserver.rest.client.datatypes;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Workspaces {

	protected String href;

	@XmlElement(name="workspace", type=Workspace.class)
	protected final List<Workspace> workspace;

	public Workspaces() {
		this.workspace = new LinkedList<Workspace>();
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return this.href;
	}

	/**
	 * @return the workspace
	 */
	public List<Workspace> getWorkspace() {
		return this.workspace;
	}

	/**
	 * @param href the href to set
	 */
	public void setHref(final String href) {
		this.href = href;
	}
}

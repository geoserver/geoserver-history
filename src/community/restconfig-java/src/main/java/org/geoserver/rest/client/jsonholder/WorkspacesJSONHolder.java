package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.Workspaces;

@XmlRootElement
public class WorkspacesJSONHolder {

	private Workspaces workspaces;

	public WorkspacesJSONHolder() {
		this.workspaces = new Workspaces();
	}

	public WorkspacesJSONHolder(final Workspaces workspaces) {
		this.workspaces = workspaces;
	}

	public final Workspaces getWorkspaces() {
		return this.workspaces;
	}

	public final void setWorkspaces(final Workspaces workspaces) {
		this.workspaces = workspaces;
	}
}

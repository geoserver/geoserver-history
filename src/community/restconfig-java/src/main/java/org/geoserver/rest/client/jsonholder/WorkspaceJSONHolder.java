package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.Workspace;

@XmlRootElement
public class WorkspaceJSONHolder {

	private Workspace workspace;

	public WorkspaceJSONHolder() {
	}

	public WorkspaceJSONHolder(final Workspace workspace) {
		this.workspace = workspace;
	}

	public final Workspace getWorkspace() {
		return this.workspace;
	}

	public final void setWorkspace(final Workspace workspace) {
		this.workspace = workspace;
	}
}

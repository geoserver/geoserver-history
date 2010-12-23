package org.geoserver.rest.client;

import java.io.IOException;
import java.rmi.RemoteException;

import org.geoserver.rest.client.datatypes.CoverageStore;
import org.geoserver.rest.client.datatypes.Workspace;
import org.geoserver.rest.client.datatypes.Workspaces;
import org.geoserver.rest.client.jsonholder.WorkspaceJSONHolder;
import org.geoserver.rest.client.jsonholder.WorkspacesJSONHolder;

import com.sun.jersey.api.client.ClientResponse;

/**
 * The Geoserver Workspace Configuration client object that allows users to manipulate {@link Workspace}s on
 * the connected Geoserver instance. Workspaces are used to manage holdings of every other kind of Geoserver Resource.
 *
 * @author Ronak Patel
 */
public class WorkspaceConfigClient extends ConfigClient {

	/**
	 * The URI for manipulating a single {@link Workspace}
	 */
	private static final String SPACE_URI = "/rest/workspaces/{wksp}";	

	/**
	 * The URI for manipulating a collection of {@link CoverageStore}s
	 */
	private static final String URI = "/rest/workspaces";

	/**
	 * Creates a Geoserver interaction object
	 * 
	 * @param geoserverClient
	 *            the Geoserver Client Configuration
	 */
	public WorkspaceConfigClient(final GeoserverConfigClient geoserverClient) {
		super(geoserverClient);
	}

	/**
	 * Creates a {@link Workspace} on the server that serves as a container for
	 * every other kind of Geoserver resource.
	 * 
	 * @param workspace
	 *            a {@link Workspace}
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public Workspace createWorkspace(final Workspace workspace) throws IOException {

		//remove all spaces and commas and convert to lower case
		this.transformWorkspace(workspace);
		
		final String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + WorkspaceConfigClient.URI;

		this.createEntity(uri, new WorkspaceJSONHolder(workspace));

		return workspace;
	}

	/**
	 * Transforms the name of the {@link Workspace} to one that is safe to be
	 * used inside of Geoserver.
	 * 
	 * @param workspace
	 *            a Geoserver {@link Workspace}
	 * @return the modified {@link Workspace}
	 */
	private Workspace transformWorkspace(final Workspace workspace) {
		
		//update the workspace name by the transform rules
		workspace.setName(this.transformWorkspaceName(workspace.getName()));
		
		return workspace;
	}
	
	/**
	 * Transforms a name of a {@link Workspace} to one that is safe to be used
	 * inside of Geoserver.
	 * 
	 * @param name
	 *            the name of a Geoserver {@link Workspace}
	 * @return the modified {@link Workspace} name
	 */
	private String transformWorkspaceName(final String name) {
		
		String transformedName = name.replace(" ", "");
		transformedName = name.replace(",", "");
		transformedName = name.toLowerCase().trim();
		
		return transformedName;
	}

	/**
	 * Removes a {@link Workspace} from the server
	 * 
	 * @param workspace
	 *            the {@link Workspace} to remove
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public void deleteWorkspace(final Workspace workspace) throws RemoteException, IOException {

		//remove all spaces and commas and convert to lower case
		this.transformWorkspace(workspace);
		
		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + WorkspaceConfigClient.SPACE_URI;
		uri = uri.replace("{wksp}", workspace.getName());

		this.deleteEntity(uri);
	}

	/**
	 * Retrieves the {@link Workspace} given by name <code>name</code>
	 * @param name
	 * @return
	 * @throws RemoteException
	 * @throws IOException
	 */
	public Workspace getWorkspace(final String name) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + WorkspaceConfigClient.SPACE_URI;
		uri = uri.replace("{wksp}", this.transformWorkspaceName(name));

		final ClientResponse response = this.getEntity(uri);
		final WorkspaceJSONHolder holder = response.getEntity(WorkspaceJSONHolder.class);

		return holder.getWorkspace();
	}

	public Workspaces getWorkspaces() throws RemoteException, IOException {

		final String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + WorkspaceConfigClient.URI;

		final ClientResponse response = this.getEntity(uri);
		final WorkspacesJSONHolder holder = response.getEntity(WorkspacesJSONHolder.class);

		return holder.getWorkspaces();
	}
}

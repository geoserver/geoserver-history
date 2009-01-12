package org.geoserver.security.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.PropertyTreeColumn;
import org.apache.wicket.extensions.markup.html.tree.table.TreeTable;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Alignment;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Unit;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.security.dao.DAOConfigurationProperties;
import org.geoserver.security.dao.DAOException;
import org.geoserver.security.dao.IDAOConfiguration;
import org.geoserver.security.model.LayerSecurityModel;
import org.geoserver.security.model.PropertyEditableColumn;
import org.geoserver.security.model.configuration.ConfigurationSingleton;
import org.geoserver.security.model.configuration.ConfigureChainOfResponsibility;
import org.geoserver.web.admin.ServerAdminPage;
import org.geotools.util.logging.Logging;

/**
 * A panel for Security Page
 * 
 * @author Francesco Izzi (geoSDI)
 */

public class SecurityPage extends ServerAdminPage {

	private TreeTable tree;

	private Catalog catalog;

	public SecurityPage() {
		
		IDAOConfiguration dao = new DAOConfigurationProperties();
		
		try {
			ConfigureChainOfResponsibility configuration = dao.loadConfiguration();
			configuration.run(ConfigurationSingleton.getInstance());
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		add(new Label("label",
				"Per layer security sub system, protect your data!"));

		IColumn columns[] = new IColumn[] {
				new PropertyTreeColumn(new ColumnLocation(Alignment.LEFT, 18,
						Unit.EM), "NAME_SPACE", "userObject.namespace"),
				new PropertyEditableColumn(new ColumnLocation(Alignment.LEFT,
						12, Unit.EM), "LAYER", "userObject.layer"),
				new PropertyEditableColumn(new ColumnLocation(Alignment.LEFT,
						12, Unit.EM), "ACCESS", "userObject.access"),
				new PropertyEditableColumn(new ColumnLocation(Alignment.LEFT,
						12, Unit.EM), "ROLE", "userObject.role"),

		};

		Form form = new Form("form");
		add(form);

		tree = new TreeTable("treeTable", createTreeModel(), columns);
		form.add(tree);
		tree.getTreeState().expandAll();

		IModel resourceListModel = new LoadableDetachableModel() {
			public Object load() {
				
				IDAOConfiguration dao = new DAOConfigurationProperties();
				
				try {
					ConfigureChainOfResponsibility configuration = dao.loadConfiguration();
					configuration.run(ConfigurationSingleton.getInstance());
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<String> result = new ArrayList<String>();

				// gather layer and group names
				List<LayerInfo> layers = getCatalog().getLayers();
				for (LayerInfo layer : layers) {
					ResourceInfo resource = layer.getResource();
					if (layer.isEnabled() && resource.isEnabled()) {
						result.add(resource.getPrefixedName());
					}
				}
				List<LayerGroupInfo> groups = getCatalog().getLayerGroups();
				for (LayerGroupInfo group : groups) {
					result.add(group.getName());
				}

				// alphabetical sort
				Collections.sort(result);

				return result;
			}
		};

//		ListView listview = new ListView("listview", resourceListModel) {
//			protected void populateItem(ListItem item) {
//				final Object itemModel = (Object) item.getModelObject();
//				item.add(new Label("label-test", itemModel.toString()));
//				item.add(new Link("delete") {
//
//					@Override
//					public void onClick() {
//						System.out.println("Delete " + itemModel);
//						try {
//							SecureCatalogImpl catalogo = new SecureCatalogImpl(
//									getCatalog());
//							System.out.println(catalogo.getNamespaces());
//							System.out.println(catalogo.getLayers());
//						
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				});
//			}
//		};
//
//		add(listview);

	}

	protected TreeModel createTreeModel() {
		List<Object> l1 = new ArrayList<Object>();

		
		
		
		
		return convertToTreeModel(l1);
	}

	private TreeModel convertToTreeModel(List<Object> list) {
		TreeModel model = null;
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(
				new LayerSecurityModel("topp", "*", "w", "ROLE_ADMINISTRATOR"));
		add(rootNode, list);
		model = new DefaultTreeModel(rootNode);
		return model;
	}

	private void add(DefaultMutableTreeNode parent, List<Object> sub) {
		for (Iterator<Object> i = sub.iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof List) {
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(
						new LayerSecurityModel("topp", "*", "w",
								"ROLE_ADMINISTRATOR"));
				parent.add(child);
				add(child, (List<Object>) o);
			} else {
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(
						new LayerSecurityModel(o.toString(), o.toString(), o
								.toString(), o.toString()));
				parent.add(child);
			}
		}
	}

}

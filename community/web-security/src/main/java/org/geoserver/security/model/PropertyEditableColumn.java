package org.geoserver.security.model;

import java.util.List;

import javax.swing.tree.TreeNode;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IRenderable;
import org.apache.wicket.extensions.markup.html.tree.table.PropertyRenderableColumn;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.security.web.EditablePanel;

/**
 * 
 * @author Francesco Izzi (geoSDI)
 */

public class PropertyEditableColumn extends PropertyRenderableColumn {

	private List layers;
	
	public PropertyEditableColumn(ColumnLocation location, String header,
			String propertyExpression, List layers) {
		super(location, header, propertyExpression);
		this.layers=layers;
	}

	public Component newCell(MarkupContainer parent, String id, TreeNode node,
			int level) {
		return new EditablePanel(id, new PropertyModel(node,
				getPropertyExpression()), this.layers);
	}

	public IRenderable newCell(TreeNode node, int level) {
		if (getTreeTable().getTreeState().isNodeSelected(node))
			return null;
		else
			return super.newCell(node, level);
	}
}

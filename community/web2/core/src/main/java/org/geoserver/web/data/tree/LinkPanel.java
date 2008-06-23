/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.tree;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public abstract class LinkPanel extends AbstractTreePanel {

    Label label;

    public LinkPanel(String id, DataTreeTable tree, MarkupContainer parent,
            CatalogNode node, int level) {
        super(id, tree, parent, node, level);
        AjaxLink link = new AjaxLink("ajaxLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                LinkPanel.this.onClick(target);

            }

        };
        label = new org.apache.wicket.markup.html.basic.Label("label", new Model(node));
        link.add(label);
        add(link);
    }

    protected abstract void onClick(AjaxRequestTarget target);

}

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.tree;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.geoserver.web.GeoServerApplication;

/**
 * A simple component that can be used to edit/remove items from the tree
 * 
 * @author aaime
 * 
 */
public class AddConfigPanel extends Panel {

    private final AbstractCatalogNode node;

    public AddConfigPanel(String id, AbstractCatalogNode node) {
        super(id);
        this.node = node;

        AjaxLink link = new AjaxLink("config") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                onConfigClick(target);
            }
        };
        Image icon = new Image("configIcon", new ResourceReference(
                GeoServerApplication.class, "img/icons/silk/pencil_add.png"));
        icon.add(new AttributeModifier("title", true, new Model(
                "Add and configure " + node.getNodeLabel())));
        link.add(icon);
        add(link);

        link = new AjaxLink("add") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                addAddClick(target);
            }
        };
        icon = new Image("addIcon", new ResourceReference(
                GeoServerApplication.class, "img/icons/silk/add.png"));
        icon.add(new AttributeModifier("title", true, new Model("Add "
                + node.getNodeLabel() + " (auto config)")));
        link.add(icon);
        add(link);
    }

    protected void addAddClick(AjaxRequestTarget target) {
        System.out.println("Add clicked!");
    }

    protected void onConfigClick(AjaxRequestTarget target) {
        System.out.println("Config clicked!");
    }

}

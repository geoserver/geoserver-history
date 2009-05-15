/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data;

import static org.geoserver.catalog.CascadeRemovalReporter.ModificationType.DELETE;
import static org.geoserver.catalog.CascadeRemovalReporter.ModificationType.EXTRA_STYLE_REMOVED;
import static org.geoserver.catalog.CascadeRemovalReporter.ModificationType.GROUP_CHANGED;
import static org.geoserver.catalog.CascadeRemovalReporter.ModificationType.STYLE_RESET;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.CascadeRemovalReporter;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerApplication;

@SuppressWarnings("serial")
public class ConfirmRemovalPanel extends Panel {
    List<? extends CatalogInfo> roots;
    
    public ConfirmRemovalPanel(String id, CatalogInfo... roots) {
        this(id, Arrays.asList(roots));
    }

    public ConfirmRemovalPanel(String id, List<? extends CatalogInfo> roots) {
        super(id);
        this.roots = roots;
        
        // collect the objects that will be removed (besides the roots)
        Catalog catalog = GeoServerApplication.get().getCatalog();
        CascadeRemovalReporter visitor = new CascadeRemovalReporter(catalog);
        for (CatalogInfo root : roots) {
            root.accept(visitor);
        }
        visitor.removeAll(roots);
        
        // add roots
        add(new Label("rootObjects", names(roots)));
        
        // removed objects root (we show it if any removed object is on the list)
        WebMarkupContainer removed = new WebMarkupContainer("removedObjects");
        removed.setVisible(visitor.getObjects(null, DELETE).size() > 0);
        add(removed);
        
        // removed workspaces
        WebMarkupContainer wsr = new WebMarkupContainer("workspacesRemoved");
        removed.add(wsr);
        List<WorkspaceInfo> workspaces = visitor.getObjects(WorkspaceInfo.class);
        if(workspaces.size() == 0)
            wsr.setVisible(false);
        wsr.add(new Label("workspaces", names(workspaces)));
        
        // removed stores
        WebMarkupContainer str = new WebMarkupContainer("storesRemoved");
        removed.add(str);
        List<StoreInfo> stores = visitor.getObjects(StoreInfo.class);
        if(stores.size() == 0)
            str.setVisible(false);
        str.add(new Label("stores", names(stores)));
        
        // removed layers
        WebMarkupContainer lar = new WebMarkupContainer("layersRemoved");
        removed.add(lar);
        List<LayerInfo> layers = visitor.getObjects(LayerInfo.class, DELETE);
        if(layers.size() == 0)
            lar.setVisible(false);
        lar.add(new Label("layers", names(layers)));
        
        // removed groups
        WebMarkupContainer grr = new WebMarkupContainer("groupsRemoved");
        removed.add(grr);
        List<LayerGroupInfo> groups = visitor.getObjects(LayerGroupInfo.class, DELETE);
        if(groups.size() == 0)
            grr.setVisible(false);
        grr.add(new Label("groups", names(groups)));
        
        // modified objects root (we show it if any modified object is on the list)
        WebMarkupContainer modified = new WebMarkupContainer("modifiedObjects");
        modified.setVisible(visitor.getObjects(null, EXTRA_STYLE_REMOVED, GROUP_CHANGED, STYLE_RESET).size() > 0);
        add(modified);
        
        // layers modified
        WebMarkupContainer lam = new WebMarkupContainer("layersModified");
        modified.add(lam);
        layers = visitor.getObjects(LayerInfo.class, 
                STYLE_RESET, EXTRA_STYLE_REMOVED);
        if(layers.size() == 0)
            lam.setVisible(false);
        lam.add(new Label("layers", names(layers)));
        
        // groups modified
        WebMarkupContainer grm = new WebMarkupContainer("groupsModified");
        modified.add(grm);
        groups = visitor.getObjects(LayerGroupInfo.class, GROUP_CHANGED);
        if(groups.size() == 0)
            grm.setVisible(false);
        grm.add(new Label("groups", names(groups)));
    }
    
    public List<? extends CatalogInfo> getRoots() {
        return roots;
    }
    
    String names(List objects) {
        try {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < objects.size(); i++) {
                sb.append((String) BeanUtils.getProperty(objects.get(i), "name"));
                if(i < (objects.size() - 1))
                    sb.append(", ");
            }
            return sb.toString();
        } catch(Exception e) {
            throw new RuntimeException("A catalog object that does not have " +
            		"a 'name' property has been used, this is unexpected", e);
        }
    }

//    String workspaceNames(List<WorkspaceInfo> workspaces) {
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < workspaces.size(); i++) {
//            sb.append(workspaces.get(i).getName());
//            if(i < (workspaces.size() - 1))
//                sb.append(", ");
//        }
//        return sb.toString();
//    }
//    
//    String storeNames(List<StoreInfo> stores) {
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < stores.size(); i++) {
//            sb.append(stores.get(i).getName());
//            if(i < (stores.size() - 1))
//                sb.append(", ");
//        }
//        return sb.toString();
//    }
//    
//    String layerNames(List<LayerInfo> layers) {
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < layers.size(); i++) {
//            sb.append(layers.get(i).getName());
//            if(i < (layers.size() - 1))
//                sb.append(", ");
//        }
//        return sb.toString();
//    }
//    
//    String groupNames(List<LayerGroupInfo> groups) {
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < groups.size(); i++) {
//            sb.append(groups.get(i).getName());
//            if(i < (groups.size() - 1))
//                sb.append(", ");
//        }
//        return sb.toString();
//    }
}

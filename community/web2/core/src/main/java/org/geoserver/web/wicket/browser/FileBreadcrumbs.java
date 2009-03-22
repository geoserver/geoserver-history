/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket.browser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * A panel showing the path between the root directory and the current directory as a set
 * of links separated by "/", much like breadcrumbs in a web site. 
 * @author Andrea Aime - OpenGeo
 *
 */
public abstract class FileBreadcrumbs extends Panel {
    IModel rootFile;

    public FileBreadcrumbs(String id, IModel rootFile, IModel currentFile) {
        super(id, currentFile);

        add(new ListView("path", new BreadcrumbModel(rootFile, currentFile)) {

            @Override
            protected void populateItem(ListItem item) {
                File file = (File) item.getModelObject();
                boolean last = item.getIndex() == getList().size() - 1;

                // the link to the current path item
                Label name = new Label("pathItem", file.getName());
                Link link = new AjaxFallbackLink("pathItemLink", item
                        .getModel()) {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        pathItemClicked((File) getModelObject(), target);
                    }

                };
                link.add(name);
                item.add(link);
                link.setEnabled(!last);

                // the separator
                String separator;
                if (!last)
                    separator = "/";
                else
                    separator = "";
                item.add(new Label("separator", separator));
            }

        });
    }

    protected abstract void pathItemClicked(File file,
            AjaxRequestTarget target);

    static class BreadcrumbModel extends LoadableDetachableModel {
        IModel rootFileModel;

        IModel currentFileModel;

        public BreadcrumbModel(IModel rootFileModel, IModel currentFileModel) {
            this.rootFileModel = rootFileModel;
            this.currentFileModel = currentFileModel;
        }

        @Override
        protected Object load() {
            File root = (File) rootFileModel.getObject();
            File current = (File) currentFileModel.getObject();

            // get all directories between current and root
            List<File> files = new ArrayList<File>();
            while (current != null && !current.equals(root)) {
                files.add(current);
                current = current.getParentFile();
            }
            if(current != null && current.equals(root))
                files.add(root);
            // reverse the order, we want them ordered from root
            // to current
            Collections.reverse(files);

            return files;
        }

    }

//    public static void main(String[] args) {
//        WicketTestApplication.start(new IComponentFactory() {
//
//            public Component createComponent(String id) {
//                Model root = new Model(new File("file:/c:"));
//                Model curr = new Model(new File("file:/c:/progetti/gisData/bc_shapefiles"));
//                return new FileBreadcrumbs(id, root, curr) {
//
//                    @Override
//                    protected void pathItemClicked(File file,
//                            AjaxRequestTarget target) {
//                        System.out.println(file);
//                    }
//                    
//                };
//            }
//        }, "/test", 9090);
//    }
}

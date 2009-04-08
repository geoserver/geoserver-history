package org.geoserver.web;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Small utility panel showed only in dev mode that allows developers to control
 * some Wicket behavior
 */
@SuppressWarnings("serial")
public class DeveloperToolbar extends Panel {

    public DeveloperToolbar(String id) {
        super(id);

        // Clears the resource caches
        add(new AjaxFallbackLink("clearCache") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                GeoServerApplication.get().clearWicketCaches();
            }
        });

        IModel gsApp = new GeoServerApplicationModel();

        // controls whether wicket paths are being generated
        final AjaxCheckBox wicketPaths = new AjaxCheckBox("wicketPaths",
                new PropertyModel(gsApp, "debugSettings.outputComponentPath")) {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {

            }

        };
        wicketPaths.setOutputMarkupId(true);
        add(wicketPaths);

        // controls whether wicket ids are being generated
        add(new AjaxCheckBox("wicketIds", new PropertyModel(gsApp,
                "markupSettings.stripWicketTags")) {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                wicketPaths.setModelObject(Boolean.FALSE);
                target.addComponent(wicketPaths);
            }

        });

    }

    static class GeoServerApplicationModel extends LoadableDetachableModel {

        GeoServerApplicationModel() {
            super(GeoServerApplication.get());
        }

        @Override
        protected Object load() {
            return GeoServerApplication.get();
        }

    }

}

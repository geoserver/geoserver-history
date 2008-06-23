/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.coverage;

import java.io.IOException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.data.util.CoverageStoreUtils;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.data.tree.DataPage;
import org.opengis.coverage.grid.Format;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterValueGroup;

/**
 * 
 * <p>
 * TODO: implement coverage resource configuration
 * </p>
 * 
 * @author Gabriel Roldan
 */
public class RasterCoverageConfiguration extends GeoServerBasePage {

    private final String workspaceId;

    /**
     * 
     * @param workspaceId
     *            the {@link WorkspaceInfo#getId() id} of the workspace to
     *            attach the new coverage to
     * @param coverageFactoryName
     *            the {@link Format#getName() name} of the format to create a
     *            new raster coverage for
     */
    public RasterCoverageConfiguration(final String workspaceId, final String coverageFactoryName) {
        this.workspaceId = workspaceId;

        Format format;
        try {
            format = CoverageStoreUtils.acquireFormat(coverageFactoryName);
        } catch (IOException e) {
            throw new RuntimeException("Can't acquire raster format " + coverageFactoryName + ": "
                    + e.getMessage(), e);
        }

        final WorkspaceInfo workspace = getCatalog().getWorkspace(workspaceId);

        add(new Label("workspaceName", workspace.getName()));

        final Form paramsForm = new Form("rasterStoreForm");
        add(paramsForm);

        paramsForm.add(new BookmarkablePageLink("cancel", DataPage.class));

        paramsForm.add(new Button("submit") {
            @Override
            public void onSubmit() {
                // TODO: save
            }
        });

        paramsForm.add(new FeedbackPanel("feedback"));

        // ParameterValueGroup readParameters = format.getReadParameters();
        // ParameterDescriptorGroup descriptor = readParameters.getDescriptor();
    }

}

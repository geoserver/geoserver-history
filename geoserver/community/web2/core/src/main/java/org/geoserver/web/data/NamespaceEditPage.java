/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogFactory;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.data.tree.DataPage;
import org.geoserver.web.util.MapModel;

/**
 * 
 * @author Gabriel Roldan
 */
public class NamespaceEditPage extends GeoServerBasePage {

    private static final String PREFIX_KEY = "prefix";

    private static final String URI_KEY = "uri";

    /**
     * Id of the namespace being edited, or {@code null} if about to create a
     * new namespace.
     */
    private String namespaceId;

    /**
     * Creates a page to add a new namespace to the catalog
     */
    public NamespaceEditPage() {
        namespaceId = null;
        Catalog catalog = getCatalog();
        CatalogFactory factory = catalog.getFactory();
        NamespaceInfo newNamespace = factory.createNamespace();
        init(newNamespace);
    }

    /**
     * Creates a page to edit the given namespace
     * 
     * @param namespace
     *            the namespaceInfo to edit
     */
    public NamespaceEditPage(final String namespaceId) {
        final NamespaceInfo nsInfo = getCatalog().getNamespace(namespaceId);
        if (nsInfo == null) {
            throw new IllegalArgumentException("Namespace with id " + namespaceId
                    + " does not exist");
        }
        this.namespaceId = namespaceId;
        init(nsInfo);
    }

    /**
     * Initializes the edit page for the given {@link NamespaceInfo}
     * 
     * @param nsInfo
     */
    private void init(final NamespaceInfo nsInfo) {

        final Map<String, String> model = new HashMap<String, String>();
        model.put(PREFIX_KEY, nsInfo.getPrefix());
        model.put(URI_KEY, nsInfo.getURI());

        String pageTitle = (namespaceId == null ? "Add" : "Edit") + " Workspace";
        add(new Label("pageTitle", pageTitle));

        final Form nsForm = new Form("nsForm");
        add(nsForm);

        TextField prefixField = new TextField("prefixValue", new MapModel(model, PREFIX_KEY));
        prefixField.setRequired(true);
        prefixField.add(new IValidator() {
            private static final long serialVersionUID = 1L;

            public void validate(final IValidatable validatable) {
                String prefix = (String) validatable.getValue();
                Catalog catalog = getCatalog();
                NamespaceInfo existing = catalog.getNamespaceByPrefix(prefix);

                final boolean addingAndExists = namespaceId == null && existing != null;
                final boolean editingAndExistsOther = namespaceId != null && existing != null
                        && namespaceId != existing.getId();

                if (addingAndExists || editingAndExistsOther) {
                    String error = "A namespace with prefix " + prefix
                            + " already exists in the catalog";
                    validatable.error(new ValidationError().setMessage(error));
                }
            }
        });

        TextField uriField = new TextField("uriValue", new MapModel(model, URI_KEY));
        uriField.setRequired(true);
        uriField.add(new IValidator() {
            private static final long serialVersionUID = 1L;

            public void validate(IValidatable validatable) {
                String value = (String) validatable.getValue();
                try {
                    new URI(value);
                } catch (URISyntaxException e) {
                    validatable.error(new ValidationError()
                            .setMessage("The namespace shall be a valid URI"));
                }
            }
        });

        nsForm.add(new Label(PREFIX_KEY, "Prefix"));
        nsForm.add(prefixField);

        nsForm.add(new Label(URI_KEY, "Namespace URI"));
        nsForm.add(uriField);

        nsForm.add(new FeedbackPanel("feedback"));

        Link link = new Link("cancel") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(DataPage.class);
            }
        };
        link.add(new Label("label", "Cancel"));
        nsForm.add(link);

        SubmitLink saveLink = new SubmitLink("save", nsForm) {
            private static final long serialVersionUID = 1L;

            public void onSubmit() {
                try {
                    final String oldPrefix = nsInfo.getPrefix();

                    final String prefix = model.get(PREFIX_KEY);
                    final String uri = model.get(URI_KEY);
                    nsInfo.setPrefix(prefix);
                    nsInfo.setURI(uri);
                    Catalog catalog = getCatalog();
                    if (namespaceId == null) {
                        catalog.add(nsInfo);
                    } else {
                        catalog.save(nsInfo);
                        WorkspaceInfo workspace = catalog.getWorkspaceByName(oldPrefix);
                        workspace.setName(prefix);
                        catalog.save(workspace);
                    }
                    setResponsePage(DataPage.class);
                } catch (Exception e) {
                    nsForm.error("Unexpected error: " + e.getMessage());
                }
            }
        };
        saveLink.add(new Label("label", "Save"));
        nsForm.add(saveLink);
    }

}

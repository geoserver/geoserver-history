/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.web.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;
import org.geoserver.catalog.ResourcePool;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.wms.web.publish.StylesModel;

/**
 * Base page for creating/editing styles
 */
@SuppressWarnings("serial")
public abstract class AbstractStylePage extends GeoServerSecuredPage {

    protected TextField nameTextField;

    protected SLDEditorPanel sldEditorPanel;

    protected FileUploadField fileUploadField;

    protected DropDownChoice styles;

    protected AjaxSubmitLink copyLink;

    protected Form uploadForm;

    protected Form styleForm;

    public AbstractStylePage() {
        this(null);
    }

    public AbstractStylePage(StyleInfo style) {
        styleForm = new Form("form", new CompoundPropertyModel(style != null ? style : getCatalog().getFactory().createStyle())) {
            @Override
            protected void onSubmit() {
                onStyleFormSubmit();
            }
        };
        styleForm.setMarkupId("mainForm");
        add(styleForm);

        styleForm.add(nameTextField = new TextField("name"));
        nameTextField.setRequired(true);
        styleForm.add(sldEditorPanel = new SLDEditorPanel("sld", new Model()));
        sldEditorPanel.setOutputMarkupId(true);

        if (style != null) {
            try {
                sldEditorPanel.setRawSLD(readFile(style));
            } catch (IOException e) {
                throw new WicketRuntimeException(e);
            }
        }

        // style copy functionality
        styles = new DropDownChoice("existingStyles", new Model(), new StylesModel());
        styles.setOutputMarkupId(true);
        styles.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                styles.validate();
                copyLink.setEnabled(styles.getConvertedInput() != null);
                target.addComponent(copyLink);
            }
        });
        styleForm.add(styles);
        copyLink = copyLink();
        copyLink.setEnabled(false);
        styleForm.add(copyLink);

        uploadForm = uploadForm(styleForm);
        uploadForm.setMultiPart(true);
        uploadForm.setMaxSize(Bytes.megabytes(1));
        uploadForm.setMarkupId("uploadForm");
        add(uploadForm);

        uploadForm.add(fileUploadField = new FileUploadField("filename"));

        Link cancelLink = new Link("cancel") {
            @Override
            public void onClick() {
                setResponsePage(StylePage.class);
            }
        };
        add(cancelLink);
    }

    Form uploadForm(final Form form) {
        return new Form("uploadForm") {
            @Override
            protected void onSubmit() {
                FileUpload upload = fileUploadField.getFileUpload();
                if (upload == null) {
                    warn("No file selected.");
                    return;
                }
                ByteArrayOutputStream bout = new ByteArrayOutputStream();

                try {
                    IOUtils.copy(upload.getInputStream(), bout);
                    sldEditorPanel.setRawSLD(new ByteArrayInputStream(bout.toByteArray()));
                    sldEditorPanel.updateModel();
                } catch (IOException e) {
                    throw new WicketRuntimeException(e);
                }

                // update the style object
                StyleInfo s = (StyleInfo) form.getModelObject();
                s.setFilename(upload.getClientFileName());

                if (s.getName() == null || "".equals(s.getName().trim())) {
                    // set it
                    nameTextField.setModelValue(ResponseUtils.stripExtension(upload
                            .getClientFileName()));
                    nameTextField.modelChanged();
                }
            }
        };
    }

    AjaxSubmitLink copyLink() {
        return new AjaxSubmitLink("copy") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                // we need to force validation or the value won't be converted
                styles.processInput();
                StyleInfo style = (StyleInfo) styles.getConvertedInput();

                if (style != null) {
                    try {
                        // same here, force validation or the field won't be udpated
                        sldEditorPanel.getTextArea().validate();
                        sldEditorPanel.getTextArea().clearInput();
                        sldEditorPanel.setRawSLD(readFile(style));
                    } catch (Exception e) {
                        error("Errors occurred loading the '" + style.getName() + "' style");
                    }
                    target.addComponent(sldEditorPanel);
                }
            }

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                return new AjaxPreprocessingCallDecorator(super.getAjaxCallDecorator()) {

                    @Override
                    public CharSequence preDecorateScript(CharSequence script) {
                        return "if(editAreaLoader.getValue('"
                                + sldEditorPanel.getTextArea().getMarkupId()
                                + "') != '' &&"
                                + "!confirm('"
                                + new ParamResourceModel("confirmOverwrite", AbstractStylePage.this)
                                        .getString() + "')) return false;" + script;
                    }
                };
            }

            @Override
            public boolean getDefaultFormProcessing() {
                return false;
            }

        };
    }

    Reader readFile(StyleInfo style) throws IOException {
        ResourcePool pool = getCatalog().getResourcePool();
        return pool.readStyle(style);
    }

    /**
     * Subclasses must implement to define the submit behavior
     */
    protected abstract void onStyleFormSubmit();
}

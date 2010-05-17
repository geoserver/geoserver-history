/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.wicket.EditAreaBehavior;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * 
 * @author Gabriel Roldan
 * @since 1.8.x
 * @version $Id$
 */
@SuppressWarnings("serial")
public class DemoRequestsPage extends GeoServerBasePage {

    private static final Logger LOGGER = Logging.getLogger("org.geoserver.web.demo");

    /**
     * Holds on the properties used as arguments for the TestWfsPost servlet
     * 
     * @author Gabriel Roldan (TOPP)
     * @version $Id$
     * @since 2.0.x
     */
    static class DemoRequest implements Serializable {
        /**
         * The directory containing the demo files
         */
        private final File demoDir;

        private String requestFileName;

        private String requestUrl;

        private String requestBody;

        private String userName;

        private String password;

        DemoRequest(final File demoDir) {
            this.demoDir = demoDir;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRequestFileName() {
            return requestFileName;
        }

        public void setRequestFileName(String requestFileName) {
            this.requestFileName = requestFileName;
        }

        public String getRequestUrl() {
            return requestUrl;
        }

        public void setRequestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
        }

        public String getRequestBody() {
            return requestBody;
        }

        public void setRequestBody(String requestBody) {
            this.requestBody = requestBody;
        }

        public File getDemoDir() {
            return demoDir;
        }
    }

    private final File demoDir;

    private TextField urlTextField;

    private TextArea body;

    private TextField username;

    private PasswordTextField password;

    public DemoRequestsPage() {
        try {
            demoDir = GeoserverDataDirectory.findCreateConfigDir("demo/");
        } catch (ConfigurationException e) {
            throw new WicketRuntimeException("Can't access demo requests directory: "
                    + e.getMessage());
        }
        DemoRequest model = new DemoRequest(demoDir);
        setModel(new Model(model));

        setUpDemoRequestsForm(demoDir);
    }

    /**
     * Package visible constructor aimed to help in setting up unit tests for this class
     * 
     * @param demoDir
     */
    DemoRequestsPage(final File demoDir) {
        this.demoDir = demoDir;
        DemoRequest model = new DemoRequest(demoDir);
        setModel(new Model(model));
        setUpDemoRequestsForm(demoDir);
    }

    /**
     * Loads the contents of the demo request file named {@code reqFileName} and located in the
     * {@link #getDemoDir() demo directory}.
     * 
     * @param reqFileName
     *            the file name to load the contents for
     * @return the file contents
     * @throws IOException
     *             if an io exception occurs opening or loading the file
     */
    private String getFileContents(final String reqFileName) throws IOException {
        final File file = new File(demoDir, reqFileName);
        final StringBuilder sb = new StringBuilder();
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } finally {
            reader.close();
        }
        return sb.toString();
    }

    private void setUpDemoRequestsForm(final File demoDir) {
        final IModel requestModel = getModel();

        final Form demoRequestsForm;
        demoRequestsForm = new Form("demoRequestsForm");
        demoRequestsForm.setOutputMarkupId(true);
        demoRequestsForm.setModel(requestModel);
        add(demoRequestsForm);

        final List<String> demoList = getDemoList(demoDir);
        final DropDownChoice demoRequestsList;
        final IModel reqFileNameModel = new PropertyModel(requestModel, "requestFileName");
        demoRequestsList = new DropDownChoice("demoRequestsList", reqFileNameModel, demoList,
                new IChoiceRenderer() {
                    public String getIdValue(Object obj, int index) {
                        return String.valueOf(obj);
                    }

                    public Object getDisplayValue(Object obj) {
                        return obj;
                    }
                });
        demoRequestsForm.add(demoRequestsList);

        /*
         * Wanted to use a simpler OnChangeAjaxBehavior but target.addComponent(body) does not make
         * the EditAreaBehavior to update the body contents inside it, but instead puts the plain
         * TextArea contents above the empty xml editor
         */
        demoRequestsList.add(new AjaxFormSubmitBehavior(demoRequestsForm, "onchange") {

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                final String reqFileName = demoRequestsList.getModelValue();
                final String contents;

                final String baseUrl;
                {
                    WebRequest request = (WebRequest) DemoRequestsPage.this.getRequest();
                    HttpServletRequest httpServletRequest;
                    httpServletRequest = ((WebRequest) request).getHttpServletRequest();
                    baseUrl = ResponseUtils.baseURL(httpServletRequest);
                }
                try {
                    contents = getFileContents(reqFileName);
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Can't load demo file " + reqFileName, e);
                    throw new WicketRuntimeException("Can't load demo file " + reqFileName, e);
                }

                boolean demoRequestIsHttpGet = reqFileName.endsWith(".url");
                final String service = reqFileName.substring(0, reqFileName.indexOf('_'))
                        .toLowerCase();
                final String serviceUrl = baseUrl + service;
                if (demoRequestIsHttpGet) {
                    String url = baseUrl + contents;
                    urlTextField.setModelObject(url);
                    body.setModelObject("");
                } else {
                    urlTextField.setModelObject(serviceUrl);
                    body.setModelObject(contents);
                }

                // target.addComponent(urlTextField);
                // target.addComponent(body);
                /*
                 * Need to setResponsePage, addComponent causes the EditAreaBehavior to sometimes
                 * not updating properly
                 */
                setResponsePage(DemoRequestsPage.this);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                // nothing to do
            }
        });

        urlTextField = new TextField("url", new PropertyModel(requestModel, "requestUrl"));
        urlTextField.setMarkupId("requestUrl");
        urlTextField.setOutputMarkupId(true);
        demoRequestsForm.add(urlTextField);

        body = new TextArea("body", new PropertyModel(requestModel, "requestBody"));
        // force the id otherwise this blasted thing won't be usable from other forms
        body.setMarkupId("requestBody");
        body.setOutputMarkupId(true);
        body.add(new EditAreaBehavior());
        demoRequestsForm.add(body);

        username = new TextField("username", new PropertyModel(requestModel, "userName"));
        demoRequestsForm.add(username);

        password = new PasswordTextField("password", new PropertyModel(requestModel, "password"));
        password.setRequired(false);
        demoRequestsForm.add(password);

        final ModalWindow responseWindow;

        responseWindow = new ModalWindow("responseWindow");
        add(responseWindow);
        responseWindow.setPageMapName("demoResponse");
        responseWindow.setCookieName("demoResponse");

        responseWindow.setPageCreator(new ModalWindow.PageCreator() {

            public Page createPage() {
                return new DemoRequestResponse(requestModel);
            }
        });

        demoRequestsForm.add(new AjaxSubmitLink("submit", demoRequestsForm) {
            @Override
            public void onSubmit(AjaxRequestTarget target, Form testWfsPostForm) {
                responseWindow.show(target);
            }

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                // we need to force EditArea to update the textarea contents (which it hides)
                // before submitting the form, otherwise the contents won't be the ones the user
                // edited
                return new AjaxCallDecorator() {
                    @Override
                    public CharSequence decorateScript(CharSequence script) {
                        return "document.getElementById('requestBody').value = editAreaLoader.getValue('requestBody');"
                                + script;
                    }
                };
            }

        });
    }

    private List<String> getDemoList(final File demoDir) {
        final List<String> demoList = new ArrayList<String>();
        for (File file : demoDir.listFiles()) {
            if (!file.isDirectory()) {
                final String name = file.getName();
                if (name.endsWith(".url") || name.endsWith(".xml")) {
                    demoList.add(name);
                } else {
                    LOGGER.warning("Ignoring file " + name
                            + " in demo requests directory, only .url and .xml files allowed");
                }
            }
        }
        Collections.sort(demoList);
        return demoList;
    }
}

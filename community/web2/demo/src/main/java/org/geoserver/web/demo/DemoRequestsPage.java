/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */package org.geoserver.web.demo;

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

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.web.GeoServerBasePage;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * 
 * @author Gabriel Roldan
 * @since 1.8.x
 * @version $Id$
 */
public class DemoRequestsPage extends GeoServerBasePage {

    private static final Logger LOGGER = Logging.getLogger("org.geoserver.web.demo");

    private static class DemoRequestsModel implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * The directory containing the demo files
         */
        private final File demoDir;

        private String requestFileName;

        private String requestUrl;

        private String requestBody;

        private String userName;

        private String password;

        public DemoRequestsModel(final File demoDir) {
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

        public String getFileContents(final String reqFileName) throws IOException {
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
    }

    private final DemoRequestsModel model;

    public DemoRequestsPage() {
        File demoDir;
        try {
            demoDir = GeoserverDataDirectory.findCreateConfigDir("demo/");
        } catch (ConfigurationException e) {
            throw new WicketRuntimeException("Can't access demo requests directory: "
                    + e.getMessage());
        }
        model = new DemoRequestsModel(demoDir);

        setUpRequestSelectionForm();
        setUpRequestSumbitionForm();
    }

    private void setUpRequestSumbitionForm() {
        final Form testWfsPostForm = new Form("TestWfsPostForm");
        add(testWfsPostForm);

        TextField urlTextField = new TextField("url", new PropertyModel(model, "requestUrl"));
        testWfsPostForm.add(urlTextField);

        TextArea body = new TextArea("body", new PropertyModel(model, "requestBody"));
        testWfsPostForm.add(body);

        TextField username = new TextField("username", new PropertyModel(model, "username"));
        testWfsPostForm.add(username);

        PasswordTextField password = new PasswordTextField("password", new PropertyModel(model,
                "password"));
        testWfsPostForm.add(password);

        testWfsPostForm.add(new Button("submit") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                submitOnIFrame();
            }

        });
        testWfsPostForm.add(new Button("submitNew") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                submitOnNewWindow();
            }
        });
    }

    private void setUpRequestSelectionForm() {
        final Form selectForm = new Form("selectForm");
        final DropDownChoice demoRequestsList;

        add(selectForm);

        final List<String> demoList = new ArrayList<String>();
        for (File file : model.getDemoDir().listFiles()) {
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
        demoRequestsList = new DemoRequestsDropDown("demoRequestsList", model, demoList);
        selectForm.add(demoRequestsList);
    }

    private void submitOnIFrame() {
        
    }

    private void submitOnNewWindow() {
        // TODO Auto-generated method stub

    }

    /**
     * A drop down option component to select a demo request, updates the
     * model's {@link DemoRequestsModel#getRequestUrl() requestUrl} and
     * {@link DemoRequestsModel#getRequestBody() requestBody} properties so the
     * appropriate form input fields update their values automatically.
     * 
     * @author Gabriel Roldan
     */
    private static class DemoRequestsDropDown extends DropDownChoice {

        private static final long serialVersionUID = 1L;

        private DemoRequestsModel demosModel;

        public DemoRequestsDropDown(final String id, DemoRequestsModel model, List<String> demoList) {
            super(id, new Model(), demoList);
            this.demosModel = model;
        }

        @Override
        protected boolean wantOnSelectionChangedNotifications() {
            return true;
        }

        /**
         * Updates the {@link DemoRequestsModel} so the form inputs get updated
         */
        @Override
        protected void onSelectionChanged(final Object newSelection) {
            final String reqFileName = (String) newSelection;
            final String contents;
            try {
                contents = demosModel.getFileContents(reqFileName);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Can't load demo file " + reqFileName, e);
                throw new WicketRuntimeException("Can't load demo file");
            }

            boolean isUrl = reqFileName.endsWith(".url");
            if (isUrl) {
                demosModel.setRequestUrl(contents);
                demosModel.setRequestBody(null);
            } else {
                demosModel.setRequestUrl(null);
                demosModel.setRequestBody(contents);
            }
        }
    }
}

/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.jai.JAIInfo;
import org.geoserver.web.GeoServerBasePage;

/** 
 * 
 * @author Arne Kepp, The Open Planning Project
 */
@SuppressWarnings("serial")
public class ServerAdminPage extends GeoServerBasePage {
    private static final long serialVersionUID = 4712657652337914993L;
    
    public ServerAdminPage() {
        setModel(new Model("tabpanel"));

        // create a list of ITab objects used to feed the tabbed panel
        List<AbstractTab> tabs = new ArrayList<AbstractTab>();

        final IModel geoServerModel = new LoadableDetachableModel(){
            public Object load() {
                return getGeoServerApplication().getGeoServer();
            }
        };

        final IModel globalInfoModel = new LoadableDetachableModel(){
            public Object load() {
                return getGeoServerApplication().getGeoServer().getGlobal();
            }
        };

        final IModel globalConfigModel = new LoadableDetachableModel(){
            public Object load() {
                return getGeoServerApplication().getApplicationContext().getBean("globalConfig");
            }
        };

        final IModel jaiModel = new LoadableDetachableModel(){
            public Object load() {
                return getGeoServerApplication()
                       .getGeoServer()
                       .getGlobal()
                       .getMetadata()
                       .get(JAIInfo.KEY);
            }
        };

        final IModel contactModel = new LoadableDetachableModel(){
            public Object load() {
                return getGeoServerApplication()
                       .getGeoServer()
                       .getGlobal()
                       .getContact();
            }
        };

        // General server settings, logging and Java
        tabs.add(new AbstractTab(new Model("Settings")) {
            public Panel getPanel(String panelId) {
                return new TabPanelSettings(panelId, geoServerModel, globalInfoModel, globalConfigModel);
            }
        });

        // Settings related to Java Advanced Imaging (JAI)
        tabs.add(new AbstractTab(new Model("Java Advanced Imaging")) {
            public Panel getPanel(String panelId) {
                return new TabPanelJAI(panelId, geoServerModel, globalInfoModel, jaiModel);
            }
        });

        // Settings for credits and contact information
        tabs.add(new AbstractTab(new Model("Contact Information")) {
            public Panel getPanel(String panelId) {
                return new TabPanelContact(panelId, geoServerModel, contactModel);
            }
        });

        // add the new tabbed panel
        add(new TabbedPanel("tabs", tabs));
    }
    
    protected void handleSubmit(Object obj) {
        getGeoServer().save((GeoServerInfo) obj);
    }
    
    
    private static class TabPanelSettings extends Panel {
        private static final long serialVersionUID = 4716657682337915996L;

        public TabPanelSettings(String id, final IModel geoServerModel, final IModel globalInfoModel, final IModel globalConfigModel) {
            super(id);
                        
            Form form = new Form("form", new CompoundPropertyModel(globalInfoModel)) {
                protected void onSubmit() {
                    ((GeoServer)geoServerModel.getObject())
                        .save((GeoServerInfo)globalInfoModel.getObject());
                }
            };

            add( form );
            
            form.add( new TextField( "maxFeatures", new PropertyModel(globalConfigModel,"maxFeatures") ) );
            form.add( new CheckBox( "verbose" ) );
            form.add( new CheckBox( "verboseExceptions" ) );
            form.add( new TextField( "numDecimals" ) );
            form.add( new TextField( "charset" ) );
            form.add( new TextField( "proxyBaseUrl" ) );
            logLevelsAppend(form, globalConfigModel);
            form.add( new CheckBox( "stdOutLogging" ) );
            form.add( new TextField("loggingLocation") );
            
            Button submit = new Button("submit",new StringResourceModel( "submit", this, null) );
            form.add(submit);
        }
        
        private void logLevelsAppend(Form form, IModel globalConfigModel) {
            List<String> logProfiles = Arrays.asList(
                "DEFAULT_LOGGING.properties",
                "VERBOSE_LOGGING.properties",
                "PRODUCTION_LOGGING.properties",
                "GEOTOOLS_DEVELOPER_LOGGING.properties",
                "GEOSERVER_DEVELOPER_LOGGING.properties");
            
            form.add(new ListChoice("log4jConfigFile",
                    new PropertyModel(globalConfigModel, "log4jConfigFile"), logProfiles ));
        }
    };
    
    private static class TabPanelJAI extends Panel
    {
        private static final long serialVersionUID = -1184717232184497578L;
        
        public TabPanelJAI(String id, final IModel geoServerModel, final IModel globalInfoModel, final IModel jaiModel)
        {
            super(id);
            
            Form form = new Form("form", new CompoundPropertyModel(jaiModel)) {
                protected void onSubmit() {
                    ((GeoServer)geoServerModel.getObject())
                        .getGlobal()
                        .getMetadata()
                        .put(
                                JAIInfo.KEY,
                                (JAIInfo)jaiModel.getObject()
                            );
                }
            };
            
            add( form );
            
            form.add(new TextField("memoryCapacity"));
            form.add(new TextField("memoryThreshold"));
            form.add(new TextField("tileThreads"));
            form.add(new TextField("tilePriority"));
            form.add(new CheckBox("recycling"));
            form.add(new CheckBox("imageIOCache"));
            form.add(new CheckBox("jpegAcceleration"));
            form.add(new CheckBox("pngAcceleration"));
            
            Button submit = new Button("submit", new StringResourceModel("submit", this, null));
            form.add(submit);
        }
    }
    
    private static class TabPanelContact extends Panel {
        private static final long serialVersionUID = 348888410971935237L;

        public TabPanelContact(String id, final IModel geoServerModel, final IModel contactModel) {
            super(id);
            
            Form form = new Form("form", new CompoundPropertyModel(contactModel)) {
                protected void onSubmit() {
                    ((GeoServer)geoServerModel.getObject())
                        .getGlobal().setContact((ContactInfo)contactModel.getObject());
                }
            };
            
            add(form);
            
            form.add(new TextField("contactPerson"));
            form.add(new TextField("contactOrganization"));
            form.add(new TextField("contactPosition"));
            form.add(new TextField("addressType"));
            form.add(new TextField("address"));
            form.add(new TextField("addressCity"));
            form.add(new TextField("addressState"));
            form.add(new TextField("addressPostalCode"));
            form.add(new TextField("addressCountry"));
            form.add(new TextField("contactVoice"));
            form.add(new TextField("contactFacsimile"));
            form.add(new TextField("contactEmail"));
            
            Button submit = new Button("submit",new StringResourceModel( "submit", this, null) );
            form.add(submit);
        }

    }
}

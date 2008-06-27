/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.jai.JAIInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.GeoServerHomePage;
import org.geoserver.web.publish.LayerConfigurationPanel;
import org.geoserver.web.publish.LayerConfigurationPanelInfo;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.config.GlobalConfig;

/** 
 * 
 * @author Arne Kepp, The Open Planning Project
 */
public class ServerAdminPage extends GeoServerBasePage {
    private static final long serialVersionUID = 4712657652337914993L;
    
    public ServerAdminPage() {
        setModel(new Model("tabpanel"));

        // create a list of ITab objects used to feed the tabbed panel
        List<AbstractTab> tabs = new ArrayList<AbstractTab>();
        
        // General server settings, logging and Java
        tabs.add(new AbstractTab(new Model("Settings")) {
            public Panel getPanel(String panelId) {
                return new TabPanelSettings(panelId, getGeoServerApplication());
            }
        });

        // Settings related to Java Advanced Imaging (JAI)
        tabs.add(new AbstractTab(new Model("Java Advanced Imaging")) {
            public Panel getPanel(String panelId) {
                return new TabPanelJAI(panelId, getGeoServer());
            }
        });

        // Settings for credits and contact information
        tabs.add(new AbstractTab(new Model("Contact Information")) {
            public Panel getPanel(String panelId) {
                return new TabPanelContact(panelId, getGeoServer());
            }
        });

        // add the new tabbed panel
        add(new TabbedPanel("tabs", tabs));
    }
    
    protected void handleSubmit(Object obj) {
        getGeoServer().save((GeoServerInfo) obj);
    }
    
    
    private static class TabPanelSettings extends Panel
    {
        private static final long serialVersionUID = 4716657682337915996L;

        private GeoServer gs;
        private GeoServerInfo gsi;
        private GlobalConfig globalConfig;
        
        public TabPanelSettings(String id, GeoServerApplication gsa)
        {
            super(id);
            this.gs = gsa.getGeoServer();
            this.gsi = gs.getGlobal();
            this.globalConfig = (GlobalConfig) gsa.getApplicationContext().getBean("globalConfig");
                        
            Form form = new Form( "form", new CompoundPropertyModel( gsi ) ) {
                protected void onSubmit() {
                    gs.save(gsi);
                }
            };
            add( form );
            
            form.add( new TextField( "maxFeatures", new PropertyModel(globalConfig,"maxFeatures") ) );
            form.add( new CheckBox( "verbose" ) );
            form.add( new CheckBox( "verboseExceptions" ) );
            form.add( new TextField( "numDecimals" ) );
            form.add( new TextField( "charset" ) );
            form.add( new TextField( "proxyBaseUrl" ) );
            logLevelsAppend(form);
            form.add( new CheckBox( "stdOutLogging" ) );
            form.add( new TextField("loggingLocation") );
            
            Button submit = new Button("submit",new StringResourceModel( "submit", this, null) );
            form.add(submit);
        }
        
        private void logLevelsAppend(Form form) {
            List<String> logProfiles = Arrays.asList(
                "DEFAULT_LOGGING.properties",
                "VERBOSE_LOGGING.properties",
                "PRODUCTION_LOGGING.properties",
                "GEOTOOLS_DEVELOPER_LOGGING.properties",
                "GEOSERVER_DEVELOPER_LOGGING.properties");
            
            form.add(new ListChoice("log4jConfigFile",
                    new PropertyModel(this.globalConfig, "log4jConfigFile"), logProfiles ));
            
        }
    };
    
    private static class TabPanelJAI extends Panel
    {
        private static final long serialVersionUID = -1184717232184497578L;
        
        private GeoServer gs;
        private JAIInfo jai;
        
        public TabPanelJAI(String id, GeoServer tempGs)
        {
            super(id);
            this.gs = tempGs;
            this.jai = (JAIInfo) tempGs.getGlobal().getMetadata().get(JAIInfo.KEY);
            
            Form form = new Form( "form", new CompoundPropertyModel( jai ) ) {
                protected void onSubmit() {
                    gs.getGlobal().getMetadata().put(JAIInfo.KEY,jai);
                }
            };
            
            add( form );
            
            form.add( new TextField( "memoryCapacity" ) );
            form.add( new TextField( "memoryThreshold" ) );
            form.add( new TextField( "tileThreads" ) );
            form.add( new TextField( "tilePriority") );
            form.add( new CheckBox( "recycling" ) );
            form.add( new CheckBox( "imageIOCache" ) );
            form.add( new CheckBox( "jpegAcceleration" ) );
            form.add( new CheckBox( "pngAcceleration" ) );
            
            Button submit = new Button("submit",new StringResourceModel( "submit", this, null) );
            form.add(submit);
        }
    }
    
    private static class TabPanelContact extends Panel
    {
        private static final long serialVersionUID = 348888410971935237L;

        private GeoServer gs;
        private ContactInfo contact;
        public TabPanelContact(String id, GeoServer tempGs)
        {
            super(id);
            this.gs = tempGs;
            this.contact = gs.getGlobal().getContact();
            
            Form form = new Form( "form", new CompoundPropertyModel( contact ) ) {
                protected void onSubmit() {
                   gs.getGlobal().setContact(contact);
                }
            };
            
            add( form );
            
            form.add( new TextField( "contactPerson" ) );
            form.add( new TextField( "contactOrganization" ) );
            form.add( new TextField( "contactPosition" ) );
            form.add( new TextField( "addressType") );
            form.add( new TextField( "address" ) );
            form.add( new TextField( "addressCity" ) );
            form.add( new TextField( "addressState" ) );
            form.add( new TextField( "addressPostalCode") );
            form.add( new TextField( "addressCountry") );
            form.add( new TextField( "contactVoice" ) );
            form.add( new TextField( "contactFacsimile") );
            form.add( new TextField( "contactEmail") );
            
            Button submit = new Button("submit",new StringResourceModel( "submit", this, null) );
            form.add(submit);
            
        }

    }
}

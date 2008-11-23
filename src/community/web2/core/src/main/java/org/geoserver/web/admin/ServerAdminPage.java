/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.media.jai.JAI;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.jai.JAIInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geotools.data.DataStore;
import org.geotools.data.LockingManager;

import com.sun.media.jai.util.SunTileCache;
/** 
 * 
 * @author Arne Kepp, The Open Planning Project
 */
@SuppressWarnings("serial")
public abstract class ServerAdminPage extends GeoServerSecuredPage {
    private static final long serialVersionUID = 4712657652337914993L;

    public IModel getGeoServerModel(){
        return new LoadableDetachableModel(){
            public Object load() {
                return getGeoServerApplication().getGeoServer();
            }
        };
    }

    public IModel getGlobalInfoModel(){
        return new LoadableDetachableModel(){
            public Object load() {
                return getGeoServerApplication().getGeoServer().getGlobal();
            }
        };
    }

    public IModel getGlobalConfigModel(){
        return new LoadableDetachableModel(){
            public Object load() {
                return getGeoServerApplication().getApplicationContext().getBean("globalConfig");
            }
        };
    }

    public IModel getJAIModel(){
        return new LoadableDetachableModel(){
            public Object load() {
                return getGeoServerApplication()
                    .getGeoServer()
                    .getGlobal()
                    .getMetadata()
                    .get(JAIInfo.KEY);
            }
        };
    }

    public IModel getContactInfoModel(){
        return new LoadableDetachableModel(){
            public Object load() {
                return getGeoServerApplication()
                    .getGeoServer()
                    .getGlobal()
                    .getContact();
            }
        };
    }

    private synchronized int getLockCount(){
        int count = 0;

        for (Iterator i = getDataStores().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                // Don't count locks from disabled datastores.
                continue;
            }

            try {
                DataStore store = meta.getDataStore(null);
                LockingManager lockingManager = store.getLockingManager();
                if (lockingManager != null){
                    // we can't actually *count* locks right now?
                    // count += lockingManager.getLockSet().size();
                }
            } catch (IllegalStateException notAvailable) {
                continue; 
            } catch (Throwable huh) {
                continue;
            }
        }

        return count;
    }

    private synchronized int getConnectionCount() {
        int count = 0;

        for (Iterator i = getDataStores().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                // Don't count connections from disabled datastores.
                continue; 
            }

            try {
                DataStore dataStore = meta.getDataStore(null);
            } catch (Throwable notAvailable) {
                //TODO: Logging.
                continue; 
            }

            count += 1;
        }

        return count;
    }

    private List<DataStoreInfo> getDataStores(){
        return getGeoServerApplication().getGeoServer().getCatalog().getDataStores();
    }
}


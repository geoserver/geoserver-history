/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.action.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.form.data.DataFeatureTypesEditorForm;
import org.vfny.geoserver.global.UserContainer;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataFeatureTypesEditorAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        UserContainer user, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        DataFeatureTypesEditorForm featureTypesForm = (DataFeatureTypesEditorForm) form;

        String name = featureTypesForm.getName();
        String SRS = featureTypesForm.getSRS();
        String title = featureTypesForm.getTitle();
        String latLonBoundingBox = featureTypesForm.getLatlonBoundingBox();
        String keywords = featureTypesForm.getKeywords();
        String _abstract = featureTypesForm.get_abstract();
        boolean _default = featureTypesForm.is_default();

        if (featureTypesForm.isDefaultChecked() == false) {
            _default = false;
        }

        DataConfig dataConfig = (DataConfig) getDataConfig();
        FeatureTypeConfig config = dataConfig.getFeatureTypeConfig(name); //TODO - RETRIEVE featuretype config		

        config.setAbstract(_abstract);
        config.setName(name);
        config.setSRS(Integer.parseInt(SRS));
        config.setTitle(title);

        
        List list = new ArrayList();
        String[] array = (keywords != null)
            ? keywords.split(System.getProperty("line.separator")) : new String[0];

        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }

        config.setKeywords(list);

        if (_default) {
            config.setSchemaAttributes(null);
        }

        dataConfig.addFeatureType(name, config);

        featureTypesForm.reset(mapping, request);
        getApplicationState().notifyConfigChanged();

        return mapping.findForward("dataConfigFeatureTypes");
    }

    DataStore aquireDataStore(String dataStoreID) throws IOException {
        DataConfig dataConfig = getDataConfig();
        DataStoreConfig dataStoreConfig = dataConfig.getDataStore(dataStoreID);

        Map params = dataStoreConfig.getConnectionParams();

        return DataStoreFinder.getDataStore(params);
    }

    FeatureType getSchema(String dataStoreID, String typeName)
        throws IOException {
        DataStore dataStore = aquireDataStore(dataStoreID);
        FeatureType type;

        return dataStore.getSchema(typeName);
    }
}

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/*
 * Created on Jan 13, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.action.data.DataStoreUtils;
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.requests.Requests;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * DOCUMENT ME!
 *
 * @author User To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DataAttributeTypesNewForm extends ActionForm {
    String selectedNewAttributeType;

    //we must save the request so getNewAttributeTypes can function
    HttpServletRequest request;

    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);

        System.out.println("@@@@@@@@@@@@@@@@@@@@atNewForm reset");

        this.request = request;
        selectedNewAttributeType = "";
    }

    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the selectedNewFeatureType.
     */
    public String getSelectedNewAttributeType() {
        return selectedNewAttributeType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param selectedNewFeatureType The selectedNewFeatureType to set.
     */
    public void setSelectedNewAttributeType(String selectedNewFeatureType) {
        this.selectedNewAttributeType = selectedNewFeatureType;
    }

    public SortedSet getNewAttributeTypes() throws IOException {
        ServletContext context = getServlet().getServletContext();
        DataConfig config = (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);
        FeatureTypeConfig ftConfig = (FeatureTypeConfig) request.getSession()
                                                                .getAttribute(DataConfig.SELECTED_FEATURE_TYPE);

        UserContainer user = Requests.getUserContainer(request);

        user.getFeatureTypeConfig();

        String dataStoreID = ftConfig.getDataStoreId();
        DataStoreConfig dsConfig = config.getDataStore(dataStoreID);
        DataStoreFactorySpi dsFactory = dsConfig.getFactory();
        Map params = DataStoreUtils.toConnectionParams(dsFactory,
                dsConfig.getConnectionParams());

        DataStore dataStore = null;
        dataStore = DataStoreUtils.aquireDataStore(params);

        FeatureType featureType = dataStore.getSchema(ftConfig.getName());
        System.out.println("STRAFBAR");

        SortedSet set = new TreeSet();
        List list = Arrays.asList(featureType.getAttributeTypes());

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            AttributeType element = (AttributeType) iter.next();
            System.out.println("STRAFBAR ZWEI: " + element.getName());
            set.add(element.getName());
        }

        //Create list to diff against.
        List alternateList = ftConfig.getSchemaAttributes();
        SortedSet alternateSet = new TreeSet();

        for (Iterator iter = alternateList.iterator(); iter.hasNext();) {
            AttributeTypeInfoConfig element = (AttributeTypeInfoConfig) iter
                .next();
            alternateSet.add(element.getName());
        }

        set.removeAll(alternateSet);

        return Collections.unmodifiableSortedSet(set);
    }
}

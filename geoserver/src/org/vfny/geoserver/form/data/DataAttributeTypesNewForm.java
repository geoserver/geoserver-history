/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
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
        System.out.println("Run Level -7");
        DataConfig config = (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);
        System.out.println("Run Level -6");
        FeatureTypeConfig ftConfig = (FeatureTypeConfig) request.getSession()
                                                                .getAttribute(DataConfig.SELECTED_FEATURE_TYPE);

        UserContainer user = Requests.getUserContainer(request);
        System.out.println("Run Level -5");
        String dataStoreID = ftConfig.getDataStoreId();
        System.out.println("Run Level -4");
        DataStoreConfig dsConfig = config.getDataStore(dataStoreID);
        System.out.println("Run Level -3");
        DataStoreFactorySpi dsFactory = dsConfig.getFactory();
        System.out.println("Run Level -2");
        Map params = DataStoreUtils.toConnectionParams(dsFactory,
                dsConfig.getConnectionParams());

        DataStore dataStore = null;
        System.out.println("Run Level -1");
        dataStore = DataStoreUtils.aquireDataStore(params);
        System.out.println("Run Level 0");
        FeatureType featureType = dataStore.getSchema(ftConfig.getName());
        System.out.println("Run Level 1");
        SortedSet set = new TreeSet();
        List list = Arrays.asList(featureType.getAttributeTypes());
        System.out.println("Run Level 2");
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            System.out.println("Run Level 2.5");
            AttributeType element = (AttributeType) iter.next();
            set.add(element.getName());
        }System.out.println("Run Level 3");

        //Create list to diff against.
        List alternateList = ftConfig.getSchemaAttributes();
        System.out.println("Run Level 4");
        SortedSet alternateSet = new TreeSet();
        System.out.println("Run Level 5");
        for (Iterator iter = alternateList.iterator(); iter.hasNext();) {
            System.out.println("Run Level 5.1");
            AttributeTypeInfoConfig element = (AttributeTypeInfoConfig) iter
                .next();
            System.out.println("Run Level 5.2");
            alternateSet.add(element.getName());
        }
        System.out.println("Run Level 6");
        set.removeAll(alternateSet);

        return Collections.unmodifiableSortedSet(set);
    }
}

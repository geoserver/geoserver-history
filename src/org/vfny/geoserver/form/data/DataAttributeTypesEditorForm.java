/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.form.data;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.DataStore;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.global.xml.NameSpaceElement;
import org.vfny.geoserver.global.xml.NameSpaceTranslator;
import org.vfny.geoserver.global.xml.NameSpaceTranslatorFactory;


/**
 * DOCUMENT ME!
 *
 * @author User To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DataAttributeTypesEditorForm extends ActionForm {
    private boolean nillible;
    private String minOccurs;
    private String maxOccurs;
    private String name;
    private String selectedType;
    private String fragment;
    HttpServletRequest request;

    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);
        this.request = request;
        FeatureTypeConfig ftConfig = (FeatureTypeConfig) request.getSession()
                                                                .getAttribute(DataConfig.SELECTED_FEATURE_TYPE);

        AttributeTypeInfoConfig config = (AttributeTypeInfoConfig) request.getSession()
                                                                          .getAttribute(DataConfig.SELECTED_ATTRIBUTE_TYPE);

        nillible = config.isNillable();
        minOccurs = Integer.toString(config.getMinOccurs());
        maxOccurs = Integer.toString(config.getMaxOccurs());
        name = config.getName();
        selectedType = config.getType();
        fragment = config.getFragment();
    }

    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the fragment.
     */
    public String getFragment() {
        return fragment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fragment The fragment to set.
     */
    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the maxOccurs.
     */
    public String getMaxOccurs() {
        return maxOccurs;
    }

    /**
     * DOCUMENT ME!
     *
     * @param maxOccurs The maxOccurs to set.
     */
    public void setMaxOccurs(String maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the minOccurs.
     */
    public String getMinOccurs() {
        return minOccurs;
    }

    /**
     * DOCUMENT ME!
     *
     * @param minOccurs The minOccurs to set.
     */
    public void setMinOccurs(String minOccurs) {
        this.minOccurs = minOccurs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the nillible.
     */
    public boolean isNillible() {
        return nillible;
    }

    /**
     * DOCUMENT ME!
     *
     * @param nillible The nillible to set.
     */
    public void setNillible(boolean nillible) {
        this.nillible = nillible;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the selectedType.
     */
    public String getSelectedType() {
        return selectedType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param selectedType The selectedType to set.
     */
    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public SortedSet getAttributeTypes() throws IOException {
        TreeSet set = new TreeSet();
        ServletContext context = getServlet().getServletContext();
        DataConfig dataConfig = (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);
        FeatureTypeConfig ftConfig = (FeatureTypeConfig) request.getSession()
                                                                .getAttribute(DataConfig.SELECTED_FEATURE_TYPE);

        DataStore dataStore = dataConfig.getDataStore(ftConfig.getDataStoreId())
                                        .findDataStore();
        FeatureType featureType = dataStore.getSchema(ftConfig.getName());
        AttributeType[] types = featureType.getAttributeTypes();
        AttributeTypeInfoConfig atiConfig = (AttributeTypeInfoConfig) request.getSession()
                                                                             .getAttribute(DataConfig.SELECTED_ATTRIBUTE_TYPE);

        NameSpaceTranslator nst = null; 
        for (int i = 0; i < types.length; i++) {
            if (atiConfig.getName().equals(types[i].getName())) {
            	Set s = new HashSet();
            	nst = NameSpaceTranslatorFactory.getInstance().getNameSpaceTranslator("xs");
            	Set s1 = nst.getAssociatedTypes(types[i].getType());
            	Set s2 = nst.getAssociatedTypes(types[i].getName());
            	s.addAll(s1);
            	s.addAll(s2);
            	nst = NameSpaceTranslatorFactory.getInstance().getNameSpaceTranslator("gml");
            	s1 = nst.getAssociatedTypes(types[i].getType());
            	s2 = nst.getAssociatedTypes(types[i].getName());
            	s.addAll(s1);
            	s.addAll(s2);

                for (Iterator iter = s.iterator(); iter.hasNext();) {
                	NameSpaceElement element = (NameSpaceElement) iter.next();
                    set.add(element.getQualifiedTypeRefName());
                }
            }
        }

        return set;
    }
}

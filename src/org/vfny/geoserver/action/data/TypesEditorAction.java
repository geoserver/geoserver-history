/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.data;

import com.vividsolutions.jts.geom.Envelope;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.action.HTMLEncoder;
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.form.data.AttributeForm;
import org.vfny.geoserver.form.data.TypesEditorForm;
import org.vfny.geoserver.global.UserContainer;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * These Action handles all the buttons for the FeatureType Editor.
 * 
 * <p>
 * This one is more complicated then usual since not all the actions require
 * the form bean to be validated! I am going to have to hack a little bit to
 * make that happen, I may end up making the form bean validation differ
 * depending on the selected action.
 * </p>
 * 
 * <p>
 * Buttons that make this action go:
 * 
 * <ul>
 * <li>
 * Submit: update the FeatureTypeConfig held by the user, punt it back into
 * DataConfig and return to the FeatureTypeSelect screen.
 * </li>
 * <li>
 * Up and Down (for each attribute): not quite sure how to make these work yet
 * - I hope I dont have to give them different names.
 * </li>
 * </ul>
 * 
 * As usual we will have to uninternationlize the action name provided to us.
 * </p>
 *
 * @author Richard Gould
 * @author Jody Garnett
 */
public class TypesEditorAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        UserContainer user, HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {
        LOGGER.finer("form bean:" + form.getClass().getName());

        TypesEditorForm typeForm = (TypesEditorForm) form;

        String action = typeForm.getAction();
        LOGGER.finer("TypesEditorAction is " + action);

        Locale locale = (Locale) request.getLocale();
        MessageResources messages = servlet.getResources();
        final String SUBMIT = HTMLEncoder.decode(messages.getMessage(locale,
                    "label.submit"));
        final String ADD = HTMLEncoder.decode(messages.getMessage(locale,
                    "label.add"));
        final String BBOX = HTMLEncoder.decode(messages.getMessage(locale,
                    "config.data.calculateBoundingBox.label"));
        LOGGER.finer("BBOX: " + BBOX);

        if (action.equals(SUBMIT)) {
            return executeSubmit(mapping, typeForm, user, request);
        }

        if (action.equals(BBOX)) {
            return executeBBox(mapping, typeForm, user, request);
        }

        List attributes = typeForm.getAttributes();

        if (action.startsWith("up_")) {
            int index = Integer.parseInt(action.substring(3));
            Object attribute = attributes.remove(index);
            attributes.add(index - 1, attribute);
        } else if (action.startsWith("down_")) {
            int index = Integer.parseInt(action.substring(5));
            Object attribute = attributes.remove(index);
            attributes.add(index + 1, attribute);
        } else if (action.startsWith("delete_")) {
            int index = Integer.parseInt(action.substring(7));
            attributes.remove(index);
        } else if (action.equals(ADD)) {
            executeAdd(mapping, typeForm, user, request);
        }

        // Update, Up, Down, Add, Remove need to resync
        sync(typeForm, user.getFeatureTypeConfig());
        form.reset(mapping, request);

        return mapping.findForward("config.data.type.editor");
    }

    /**
     * Populate the bounding box fields from the source and pass control back
     * to the UI
     *
     * @param mapping DOCUMENT ME!
     * @param typeForm DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     */
    private ActionForward executeBBox(ActionMapping mapping,
        TypesEditorForm typeForm, UserContainer user, HttpServletRequest request)
        throws IOException, ServletException {
        DataConfig dataConfig = getDataConfig();
        DataStoreConfig dsConfig = dataConfig.getDataStore(typeForm
                .getDataStoreId());
        DataStore dataStore = dsConfig.findDataStore(request.getSession()
                                                            .getServletContext());
        FeatureType featureType = dataStore.getSchema(typeForm.getTypeName());
        FeatureSource fs = dataStore.getFeatureSource(featureType.getTypeName());

        Envelope envelope = DataStoreUtils.getBoundingBoxEnvelope(fs);

        typeForm.setMinX(Double.toString(envelope.getMinX()));
        typeForm.setMaxX(Double.toString(envelope.getMaxX()));
        typeForm.setMinY(Double.toString(envelope.getMinY()));
        typeForm.setMaxY(Double.toString(envelope.getMaxY()));

        return mapping.findForward("config.data.type.editor");
    }

    /**
     * Sync generated attributes with schemaBase.
     *
     * @param form
     * @param config
     */
    private void sync(TypesEditorForm form, FeatureTypeConfig config) {
        config.setName(form.getTypeName());
        config.setAbstract(form.getAbstract());
        config.setDefaultStyle(form.getStyleId());
        config.setSRS(Integer.parseInt(form.getSRS()));
        config.setTitle(form.getTitle());
        config.setLatLongBBox(getBoundingBox(form));
        config.setKeywords(keyWords(form));

        String schemaBase = form.getSchemaBase();

        if ((schemaBase == null) || schemaBase.equals("")
                || schemaBase.equals("--")) {
            config.setSchemaBase(null);
            config.setSchemaName(null);
            config.setSchemaAttributes(null);
        } else {
            config.setSchemaBase(schemaBase);

            String schemaName = form.getSchemaName();

            if ((schemaName == null) || (schemaName.trim().length() == 0)) {
                schemaName = form.getTypeName() + "_Type";
            }

            config.setSchemaName(schemaName);
            config.setSchemaAttributes(form.toSchemaAttributes());
        }

        config.setSchemaAttributes(form.toSchemaAttributes());
    }

    private void executeAdd(ActionMapping mapping, TypesEditorForm form,
        UserContainer user, HttpServletRequest request) {
        String attributeName = form.getNewAttribute();
        FeatureType featureType = null;

        try {
            DataConfig config = ConfigRequests.getDataConfig(request);
            DataStoreConfig dataStoreConfig = config.getDataStore(form
                    .getDataStoreId());
            DataStore dataStore = dataStoreConfig.findDataStore(getServlet()
                                                                    .getServletContext());
            featureType = dataStore.getSchema(form.getTypeName());
        } catch (IOException e) {
            // DataStore unavailable!
        }

        AttributeType attributeType = featureType.getAttributeType(attributeName);
        AttributeTypeInfoConfig attributeConfig = new AttributeTypeInfoConfig(attributeType);
        AttributeForm newAttribute = new AttributeForm(attributeConfig,
                attributeType);
        form.getAttributes().add(newAttribute);
    }

    /**
     * Execute Submit Action.
     *
     * @param mapping
     * @param form
     * @param user
     * @param request
     *
     * @return
     */
    private ActionForward executeSubmit(ActionMapping mapping,
        TypesEditorForm form, UserContainer user, HttpServletRequest request) {
        FeatureTypeConfig config = user.getFeatureTypeConfig();
        sync(form, config);

        DataConfig dataConfig = (DataConfig) getDataConfig();
        dataConfig.addFeatureType(config.getDataStoreId() + ":"
            + config.getName(), config);

        // Don't think reset is needed (as me have moved on to new page)
        // form.reset(mapping, request);
        getApplicationState().notifyConfigChanged();

        // Feature no longer selected
        user.setFeatureTypeConfig(null);

        return mapping.findForward("config.data.type");
    }

    /**
     * DOCUMENT ME!
     *
     * @param typeForm
     *
     * @return Bounding box in lat long
     */
    private Envelope getBoundingBox(TypesEditorForm typeForm) {
        return new Envelope(Double.parseDouble(typeForm.getMinX()),
            Double.parseDouble(typeForm.getMaxX()),
            Double.parseDouble(typeForm.getMinY()),
            Double.parseDouble(typeForm.getMaxY()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param typeForm
     *
     * @return Set of keywords
     */
    private Set keyWords(TypesEditorForm typeForm) {
        HashSet keywords = new HashSet();
        String[] array = (typeForm.getKeywords() != null)
            ? typeForm.getKeywords().split(System.getProperty("line.separator"))
            : new String[0];

        for (int i = 0; i < array.length; i++) {
            keywords.add(array[i]);
        }

        return keywords;
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

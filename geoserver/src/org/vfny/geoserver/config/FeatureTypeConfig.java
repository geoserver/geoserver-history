/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import com.vividsolutions.jts.geom.*;
import org.geotools.data.*;
import org.geotools.factory.*;
import org.geotools.feature.*;
import org.geotools.filter.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Rold�n
 * @version 0.1
 */
public class FeatureTypeConfig extends BasicConfig {
    /** DOCUMENT ME! */
    private DataStoreConfig dataStore;

    /** DOCUMENT ME! */
    private Envelope bbox;

    /** DOCUMENT ME! */
    private Envelope latLongBBox;

    /** DOCUMENT ME! */
    private String SRS;

    /** DOCUMENT ME! */
    private Filter definitionQuery;

    /** DOCUMENT ME! */
    private FeatureType schema;

    /** DOCUMENT ME! */
    private Map styles;

    /** DOCUMENT ME! */
    private String defaultStyle;

    /**
     * Creates a new FeatureTypeConfig object.
     *
     * @param catalog DOCUMENT ME!
     * @param fTypeRoot DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    public FeatureTypeConfig(CatalogConfig catalog, Element fTypeRoot)
        throws ConfigurationException {
        super(fTypeRoot);

        String msg = null;
        String dataStoreNS = getAttribute(fTypeRoot, "datastore", true);
        NameSpace dsNs = catalog.getNameSpace(dataStoreNS);

        if (dsNs == null) {
            msg = "a feature type named " + getName()
                + " has been configured for datastore " + dataStoreNS
                + " for wich there are no configured namespace";
            throw new ConfigurationException(msg);
        }

        this.dataStore = catalog.getDataStore(dsNs);

        if (dataStore == null) {
            msg = "FeatureType " + getName()
                + " is congfigured from a datastore named " + dsNs.getPrefix()
                + " wich was not found. Check your config files.";
            throw new ConfigurationException(msg);
        }

        this.SRS = getChildText(fTypeRoot, "SRS", true);

        try {
            this.schema = getSchema(getChildElement(fTypeRoot, "attributes"));
        } catch (IOException ex) {
            throw new ConfigurationException("Error obtaining schema for "
                + getName() + ": " + ex.getMessage(), ex);
        }

        loadStyles(getChildElement(fTypeRoot, "styles"));
        loadLatLongBBox(getChildElement(fTypeRoot, "latLonBoundingBox"));
    }

    public FeatureType getSchema() {
        return schema;
    }

    public DataStoreConfig getDataStore() {
        return this.dataStore;
    }

    /**
     * overrides getName to return full type name with namespace prefix
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return new StringBuffer(getDataStore().getNameSpace().getPrefix()).append(NameSpace.PREFIX_DELIMITER)
                                                                          .append(super
            .getName()).toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public FeatureSource getFeatureSource() throws IOException {
        return dataStore.getDataStore().getFeatureSource(super.getName());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public Envelope getBoundingBox() throws IOException {
        if (bbox == null) {
            loadBoundingBoxes();
        }

        return bbox;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public Envelope getLatLongBoundingBox() throws IOException {
        if (latLongBBox == null) {
            loadBoundingBoxes();
        }

        return latLongBBox;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSRS() {
        return SRS;
    }

    /**
     **/
    public String getOperations() {
        //get this from the datasource?
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Map getStyles() {
        return this.styles;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDefaultStyle() {
        return this.defaultStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void loadBoundingBoxes() throws IOException {
        FeatureSource source = getFeatureSource();
        this.bbox = source.getBounds();

        if (this.latLongBBox == null) {
            this.latLongBBox = getLatLongBBox(getSRS(), bbox);
        }
    }

    /**
     * creates a FeatureType schema from the list of defined exposed
     * attributes, or the full schema if no exposed attributes were defined
     *
     * @param attsElem DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     *
     * @task TODO: if the default geometry attribute was not declared as
     *       exposed should we expose it anyway? I think yes.
     */
    private FeatureType getSchema(Element attsElem)
        throws ConfigurationException, IOException {
        NodeList exposedAttributes = null;
        FeatureType schema = getFeatureSource().getSchema();
        FeatureType filteredSchema = null;

        if (attsElem != null) {
            exposedAttributes = attsElem.getElementsByTagName("attribute");
        }

        if ((exposedAttributes == null) || (exposedAttributes.getLength() == 0)) {
            return schema;
        }

        int attCount = exposedAttributes.getLength();
        AttributeType[] attributes = new AttributeType[attCount];
        Element attElem;
        String attName;

        for (int i = 0; i < attCount; i++) {
            attElem = (Element) exposedAttributes.item(i);
            attName = getAttribute(attElem, "name", true);
            attributes[i] = schema.getAttributeType(attName);

            if (attributes[i] == null) {
                throw new ConfigurationException("the FeatureType " + getName()
                    + " does not contains the configured attribute " + attName
                    + ". Check your catalog configuration");
            }
        }

        try {
            filteredSchema = FeatureTypeFactory.newFeatureType(attributes,
                    getName());
        } catch (SchemaException ex) {
        } catch (FactoryConfigurationError ex) {
        }

        return filteredSchema;
    }

    /**
     * here we must make the transformation. Crhis: do you know how to do it?
     *
     * @param fromSrId DOCUMENT ME!
     * @param bbox DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Envelope getLatLongBBox(String fromSrId, Envelope bbox) {
        //Envelope latLongBBox = null;
        //return latLongBBox;
        return bbox;
    }

    private void loadLatLongBBox(Element bboxElem)
        throws ConfigurationException {
        boolean dynamic = getBooleanAttribute(bboxElem, "dynamic", false);

        if (!dynamic) {
            double minx = getDoubleAttribute(bboxElem, "minx", true);
            double miny = getDoubleAttribute(bboxElem, "minx", true);
            double maxx = getDoubleAttribute(bboxElem, "minx", true);
            double maxy = getDoubleAttribute(bboxElem, "minx", true);
            this.latLongBBox = new Envelope(minx, miny, maxx, maxy);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param styles DOCUMENT ME!
     */
    private void loadStyles(Element styles) {
        NodeList stylesList = null;
        int numStyles = 0;

        if (styles != null) {
            stylesList = styles.getElementsByTagName("style");
            numStyles = stylesList.getLength();
        }

        if (numStyles == 0) {
            this.styles = new HashMap(numStyles + 1);
        }
    }
}

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.w3c.dom.*;
import java.nio.charset.*;
import java.util.logging.*;


/**
 * Global server configuration parameters
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public class GlobalConfig extends AbstractConfig {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");
    private static GlobalConfig globalConfig = null;

    /** DOCUMENT ME! */
    private Level loggingLevel = Logger.getLogger("org.vfny.geoserver")
                                       .getLevel();

    /** Sets the max number of Features returned by GetFeature */
    private int maxFeatures = 20000;

    /**
     * Whether newlines and indents should be returned in XML responses.
     * Default is false
     */
    private boolean verbose = true;

    /**
     * Sets the max number of decimal places past the zero returned in a
     * GetFeature response.  Default is 4 should it be moved to FeatureType
     * level?
     */
    private int numDecimals = 8;

    /**
     * Sets the global character set.  This could use some more testing from
     * international users, but what it does is sets the encoding globally for
     * all postgis database connections (the charset tag in FeatureType), as
     * well as specifying the encoding in the return xml header and mime type.
     * The default is UTF-8.  Also be warned that GeoServer does not check if
     * the CharSet is valid before attempting to use it, so it will fail
     * miserably if a bad charset is used.
     */
    private Charset charSet;
    private String baseUrl;
    private String schemaBaseUrl;
    private ContactConfig contactConfig;

    /**
     * Creates a new GlobalConfig object.
     *
     * @param globalConfigElem DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    public GlobalConfig(Element globalConfigElem) throws ConfigurationException {
        LOGGER.fine("parsing global configuration parameters");

        Element elem = null;

        elem = getChildElement(globalConfigElem, "ContactInformation");
        this.contactConfig = new ContactConfig(elem);

        Level loggingLevel = getLoggingLevel(globalConfigElem);

        //init this now so the rest of the config has correct log levels.
        Log4JFormatter.init("org.geotools", loggingLevel);
        Log4JFormatter.init("org.vfny.geoserver", loggingLevel);
        LOGGER.config("logging level is " + loggingLevel);

        elem = getChildElement(globalConfigElem, "verbose", false);

        if (elem != null) {
            this.verbose = getBooleanAttribute(elem, "value", false);
        }

        elem = getChildElement(globalConfigElem, "maxFeatures");

        if (elem != null) {
            //if the element is pressent, it's "value" attribute is mandatory
            this.maxFeatures = getIntAttribute(elem, "value", true, maxFeatures);
        }

        LOGGER.config("maxFeatures is " + maxFeatures);

        elem = getChildElement(globalConfigElem, "numDecimals");

        if (elem != null) {
            this.numDecimals = getIntAttribute(elem, "value", true, numDecimals);
        }

        LOGGER.config("numDecimals returning is " + numDecimals);

        elem = getChildElement(globalConfigElem, "charSet");
        charSet = Charset.forName("ISO-8859-1");

        if (elem != null) {
            String chSet = getAttribute(elem, "value", true);

            try {
                Charset cs = Charset.forName(chSet);
                this.charSet = cs;
                LOGGER.finer("charSet: " + cs.displayName());
            } catch (Exception ex) {
                LOGGER.info(ex.getMessage());
            }
        }

        LOGGER.config("charSet is " + charSet);

        //TODO: better checking.
        this.baseUrl = getChildText(globalConfigElem, "URL", true);

        String schemaBaseUrl = getChildText(globalConfigElem, "SchemaBaseUrl");

        if (schemaBaseUrl != null) {
            this.schemaBaseUrl = schemaBaseUrl;
        } else {
            this.schemaBaseUrl = baseUrl + "/data/capabilities/";
        }

        globalConfig = this;
    }

    public static GlobalConfig getInstance() {
        if (globalConfig == null) {
            String mesg = "ServerConfig must be initialized before calling "
                + " getInstance on globalConfig";
            throw new java.lang.IllegalStateException(mesg);
        }

        return globalConfig;
    }

    public ContactConfig getContactInformation() {
        return contactConfig;
    }

    /**
     * DOCUMENT ME!
     *
     * @param globalConfigElem DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Level getLoggingLevel(Element globalConfigElem) {
        Level level = this.loggingLevel;
        Element levelElem = getChildElement(globalConfigElem, "loggingLevel");

        if (levelElem != null) {
            String levelName = levelElem.getFirstChild().getNodeValue();

            try {
                level = Level.parse(levelName);
            } catch (IllegalArgumentException ex) {
                LOGGER.warning("illegal loggingLevel name: " + levelName);
            }
        } else {
            LOGGER.config("No loggingLevel found, using default "
                + "logging.properties setting");
        }

        return level;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Charset getCharSet() {
        return charSet;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Level getLoggingLevel() {
        return loggingLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaxFeatures() {
        return maxFeatures;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumDecimals() {
        return numDecimals;
    }

    /**
     * wether xml documents should be pretty formatted
     *
     * @return DOCUMENT ME!
     */
    public boolean isVerbose() {
        return verbose;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getSchemaBaseUrl() {
        return schemaBaseUrl;
    }

    public String getMimeType() {
        return "text/xml; charset=" + getCharSet().displayName();
    }
}

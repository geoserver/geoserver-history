/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vfny.geoserver.config.*;

/**
 * GlobalData server configuration parameters
 *
 * @author Gabriel Roldán
 * @version $Id: GlobalData.java,v 1.1.2.2 2004/01/03 00:20:15 dmzwiers Exp $
 */
public class GlobalData extends GlobalAbstract {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");

    /** DOCUMENT ME! */
    private Level loggingLevel = Logger.getLogger("org.vfny.geoserver")
                                       .getLevel();

   
    private GlobalConfig global;

       
    public GlobalData(GlobalConfig config){
    	global = config;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws java.lang.IllegalStateException DOCUMENT ME!
     */
    /*public static GlobalData getInstance() {
        if (globalConfig == null) {
            String mesg = "GlobalServer must be initialized before calling "
                + " getInstance on globalConfig";
            throw new java.lang.IllegalStateException(mesg);
        }

        return globalConfig;
    }*/

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    /*public ContactConfig getContactInformation() {
        return contactConfig;
    }*/

    /**
     * DOCUMENT ME!
     *
     * @param globalConfigElem DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
   /* private Level getLoggingLevel(Element globalConfigElem) {
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
    }*/

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Charset getCharSet() {
        return global.getCharSet();
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
        return global.getMaxFeatures();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumDecimals() {
        return global.getNumDecimals();
    }

    /**
     * wether xml documents should be pretty formatted
     *
     * @return DOCUMENT ME!
     */
    public boolean isVerbose() {
        return global.isVerbose();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBaseUrl() {
        return global.getBaseUrl();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSchemaBaseUrl() {
        return global.getSchemaBaseUrl();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMimeType() {
        return "text/xml; charset=" + getCharSet().displayName();
    }

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getAddress() {
		return notNull(global.getContact().getAddress());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getAddressCity() {
		return notNull(global.getContact().getAddressCity());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getAddressCountry() {
		return notNull(global.getContact().getAddressCountry());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getAddressPostalCode() {
		return notNull(global.getContact().getAddressPostalCode());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getAddressState() {
		return notNull(global.getContact().getAddressState());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getAddressType() {
		return notNull(global.getContact().getAddressType());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContactEmail() {
		return notNull(global.getContact().getContactEmail());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContactFacsimile() {
		return notNull(global.getContact().getContactFacsimile());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContactOrganization() {
		return notNull(global.getContact().getContactOrganization());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContactPerson() {
		return notNull(global.getContact().getContactPerson());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContactPosition() {
		return notNull(global.getContact().getContactPosition());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContactVoice() {
		return notNull(global.getContact().getContactVoice());
	}
}

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.w3c.dom.*;
import java.util.*;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Rold�n
 * @version 0.1
 */
public class BasicConfig extends AbstractConfig {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private String title;

    /** DOCUMENT ME! */
    private String _abstract;

    /** DOCUMENT ME! */
    private List keywords;

    /** DOCUMENT ME! */
    private String fees;

    /** DOCUMENT ME! */
    private String accessConstraints = "NONE";

    /** DOCUMENT ME! */
    private String maintainer;

    /**
     * Creates a new BasicServiceConfig object.
     *
     * @param serviceRoot DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    public BasicConfig(Element serviceRoot) throws ConfigurationException {
        this.name = getChildText(serviceRoot, "name", true);
        this.title = getChildText(serviceRoot, "title", true);
        this._abstract = getChildText(serviceRoot, "abstract");
        this.keywords = getKeyWords(getChildElement(serviceRoot, "keywords"));
        this.fees = getChildText(serviceRoot, "fees");
        this.accessConstraints = getChildText(serviceRoot, "accessConstraints");
        this.maintainer = getChildText(serviceRoot, "maintainer");
    }

    /**
     * DOCUMENT ME!
     *
     * @param keywordsElem DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private List getKeyWords(Element keywordsElem) {
        NodeList klist = keywordsElem.getElementsByTagName("keyword");
        int kCount = klist.getLength();
        List keywords = new ArrayList(kCount);
        String kword;
        Element kelem;

        for (int i = 0; i < kCount; i++) {
            kelem = (Element) klist.item(i);
            kword = getElementText(kelem);

            if (kword != null) {
                keywords.add(kword);
            }
        }

        return keywords;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAccessConstraints() {
        return accessConstraints;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFees() {
        return fees;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List getKeywords() {
        return keywords;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMaintainer() {
        return maintainer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return title;
    }
}

/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.w3c.dom.*;
import java.util.logging.Logger;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public abstract class AbstractConfig {
    /** DOCUMENT ME! */
    protected static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");

    /**
     * DOCUMENT ME!
     *
     * @param elem DOCUMENT ME!
     * @param attName DOCUMENT ME!
     * @param mandatory DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    protected String getAttribute(Element elem, String attName,
        boolean mandatory) throws ConfigurationException {
        Attr att = elem.getAttributeNode(attName);
        String value = null;

        if (att != null) {
            value = att.getValue();
        }

        if (mandatory) {
            if (att == null) {
                throw new ConfigurationException("element "
                    + elem.getNodeName()
                    + " does not contains an attribute named " + attName);
            } else if ("".equals(value)) {
                throw new ConfigurationException("attribute " + attName
                    + "in element " + elem.getNodeName() + " is empty");
            }
        }

        return value;
    }

    /**
     * checks <code>s</code> for nullity and if so, returns an empty String,
     * else just returns <code>s</code>
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String notNull(String s) {
        return (s == null) ? "" : s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param elem DOCUMENT ME!
     * @param attName DOCUMENT ME!
     * @param mandatory DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    protected boolean getBooleanAttribute(Element elem, String attName,
        boolean mandatory) throws ConfigurationException {
        String value = getAttribute(elem, attName, mandatory);

        return Boolean.valueOf(value).booleanValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param elem DOCUMENT ME!
     * @param attName DOCUMENT ME!
     * @param mandatory DOCUMENT ME!
     *
     * @return the value of the attribute attName parsed as a double, or
     *         Double.NaN if there was no such attribute and <code>mandatory
     *         == false</code>
     *
     * @throws ConfigurationException if <code>mandatory == true</code> and the
     *         attribute named <code>attName</code> was not found in
     *         <code>elem</code>, or if the attribute exists but cannot be
     *         parsed as a <code>double</code>
     */
    protected double getDoubleAttribute(Element elem, String attName,
        boolean mandatory) throws ConfigurationException {
        String value = getAttribute(elem, attName, mandatory);
        double d = Double.NaN;

        if (value != null) {
            try {
                d = Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                throw new ConfigurationException("Illegal attribute value for "
                    + attName + " in element " + elem.getNodeName()
                    + ". Expected double, but was " + value);
            }
        }

        return d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param elem DOCUMENT ME!
     * @param attName DOCUMENT ME!
     * @param mandatory DOCUMENT ME!
     * @param defaultValue DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    protected int getIntAttribute(Element elem, String attName,
        boolean mandatory, int defaultValue) throws ConfigurationException {
        String attValue = getAttribute(elem, attName, mandatory);

        if (!mandatory && (attValue == null)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(attValue);
        } catch (Exception ex) {
            if (mandatory) {
                throw new ConfigurationException(attName
                    + " attribute of element " + elem.getNodeName()
                    + " must be an integer, but it's '" + attValue + "'");
            } else {
                return defaultValue;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param root DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Element getChildElement(Element root, String name) {
        try {
            return getChildElement(root, name, false);
        } catch (ConfigurationException ex) {
            return null;
        }
    }

    /**
     * returns the first Element that is a direct child of <code>root</code>
     * named <code>name</code>
     *
     * @param root DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param mandatory DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    protected Element getChildElement(Element root, String name,
        boolean mandatory) throws ConfigurationException {
        Node child = root.getFirstChild();

        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (name.equals(child.getNodeName())) {
                    return (Element) child;
                }
            }

            child = child.getNextSibling();
        }

        if (mandatory && (child == null)) {
            throw new ConfigurationException(root.getNodeName()
                + " does not contains a child element named " + name);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param root DOCUMENT ME!
     * @param childName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getChildText(Element root, String childName) {
        try {
            return getChildText(root, childName, false);
        } catch (ConfigurationException ex) {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param root DOCUMENT ME!
     * @param childName DOCUMENT ME!
     * @param mandatory DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    protected String getChildText(Element root, String childName,
        boolean mandatory) throws ConfigurationException {
        Element elem = getChildElement(root, childName, mandatory);

        if (elem != null) {
            return getElementText(elem, mandatory);
        } else {
            if (mandatory) {
                String msg = "Mandatory child " + childName + "not found in "
                    + " element: " + root;
                throw new ConfigurationException(msg);
            }

            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param elem DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getElementText(Element elem) {
        try {
            return getElementText(elem, false);
        } catch (ConfigurationException ex) {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param elem DOCUMENT ME!
     * @param mandatory DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    protected String getElementText(Element elem, boolean mandatory)
        throws ConfigurationException {
        String value = null;
        LOGGER.finer("getting element text for " + elem);

        if (elem != null) {
            Node child;
            NodeList childs = elem.getChildNodes();
            int nChilds = childs.getLength();

            for (int i = 0; i < nChilds; i++) {
                child = childs.item(i);

                if (child.getNodeType() == Node.TEXT_NODE) {
                    value = child.getNodeValue();

                    if (mandatory && "".equals(value.trim())) {
                        throw new ConfigurationException(elem.getNodeName()
                            + " text is empty");
                    }

                    break;
                }
            }

            if (mandatory && (value == null)) {
                throw new ConfigurationException(elem.getNodeName()
                    + " element does not contains text");
            }
        } else {
            throw new ConfigurationException("Argument element can't be null");
        }

        return value;
    }
}

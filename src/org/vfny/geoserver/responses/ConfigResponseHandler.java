/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import org.vfny.geoserver.config.*;
import org.xml.sax.*;
import java.util.*;


/**
 * Streams out a BasicConfig element.  Also serves as a super class for others
 * printing out the basic configuration elements.
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: ConfigResponseHandler.java,v 1.3 2004/01/02 23:37:49 cholmesny Exp $
 */
public abstract class ConfigResponseHandler extends XmlResponseHandler {
    /**
     * Creates a new ConfigResponseHandler object.
     *
     * @param contentHandler The handler to send content to.
     */
    public ConfigResponseHandler(ContentHandler contentHandler) {
        super(contentHandler);
    }

    /**
     * Handles a config element, sends content for all the common information
     * of configs.
     *
     * @param config A configuration to have its name, title, abstract and
     *        keywords printed for.
     *
     * @throws SAXException For any errors.
     */
    public void handleConfig(BasicConfig config) throws SAXException {
        indent();
        handleSingleElem("Name", config.getName());
        cReturn();
        handleSingleElem("Title", config.getTitle());
        cReturn();
        handleSingleElem("Abstract", config.getAbstract());
        cReturn();
        handleKeywords(config.getKeywords());
        cReturn();
        unIndent();
    }

    /**
     * Handles a keyword list.  This implementation should generally be
     * overriden, but some subclasses may be able to use it.
     *
     * @param kwords The list of key words.
     *
     * @throws SAXException If anything goes wrong.
     */
    protected void handleKeywords(List kwords) throws SAXException {
        if ((kwords != null) && (kwords.size() > 0)) {
            startElement("Keywords");

            for (Iterator it = kwords.iterator(); it.hasNext();) {
                characters(it.next().toString());

                if (it.hasNext()) {
                    characters(", ");
                }
            }

            endElement("Keywords");
        }
    }
}

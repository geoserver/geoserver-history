/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import org.vfny.geoserver.config.*;
import org.xml.sax.*;
import java.util.*;


/**
 * Streams out a BasicConfig element
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public abstract class ConfigResponseHandler extends XmlResponseHandler {
    /**
     * Creates a new ConfigResponseHandler object.
     *
     * @param contentHandler DOCUMENT ME!
     */
    public ConfigResponseHandler(ContentHandler contentHandler) {
        super(contentHandler);
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
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

        String fees = config.getFees();

        if ((fees == null) || "".equals(fees)) {
            fees = "NONE";
        }

        handleSingleElem("Fees", fees);
        cReturn();

        String accessConstraints = config.getAccessConstraints();

        if ((accessConstraints == null) || "".equals(accessConstraints)) {
            accessConstraints = "NONE";
        }

        handleSingleElem("AccessConstraints", accessConstraints);
        unIndent();
    }

    /**
     * DOCUMENT ME!
     *
     * @param kwords DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void handleKeywords(List kwords) throws SAXException {
        startElement("Keyword");

        if (kwords != null) {
            for (Iterator it = kwords.iterator(); it.hasNext();) {
                characters(it.next().toString());

                if (it.hasNext()) {
                    characters(". ");
                }
            }
        }

        endElement("Keyword");
    }
}

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
 * @version $Id: ConfigResponseHandler.java,v 1.2 2003/12/16 18:46:09 cholmesny Exp $
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
        unIndent();
    }

    /**
     * Handles a keyword list.
     *
     * @param kwords The list of key words.
     *
     * @throws SAXException DOCUMENT ME!
     *
     * @task REVISIT: I don't think this is currently right for wms or wfs
     *       service elements.  I'm just subclassing for WfsCapabilities
     *       response. It should be Keywords instead of Keyword.  For WMS I
     *       think it should be KeywordList or something to that effect, with
     *       individual keywords delimited by keyword elements.  So I'm not
     *       sure what should go here by default, perhaps should just remain
     *       abstract.
     */
    protected void handleKeywords(List kwords) throws SAXException {
        startElement("Keywords");

        if (kwords != null) {
            for (Iterator it = kwords.iterator(); it.hasNext();) {
                characters(it.next().toString());

                if (it.hasNext()) {
                    characters(", ");
                }
            }
        }

        endElement("Keywords");
    }
}

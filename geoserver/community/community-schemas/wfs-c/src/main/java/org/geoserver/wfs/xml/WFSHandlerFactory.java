/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml;

import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.geotools.xml.impl.AttributeHandler;
import org.geotools.xml.impl.DocumentHandler;
import org.geotools.xml.impl.ElementHandler;
import org.geotools.xml.impl.ElementHandlerImpl;
import org.geotools.xml.impl.Handler;
import org.geotools.xml.impl.HandlerFactory;
import org.geotools.xml.impl.ParserHandler;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;


/**
 * Special handler factory which creates handlers for elements which are
 * defined as wfs feature types.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WFSHandlerFactory implements HandlerFactory {
    static Logger logger = org.geotools.util.logging.Logging.getLogger("org.geoserver.wfs");

    /**
     * Catalog reference
     */
    Data catalog;

    /**
     * Schema Builder
     */
    FeatureTypeSchemaBuilder schemaBuilder;

    public WFSHandlerFactory(Data catalog, FeatureTypeSchemaBuilder schemaBuilder) {
        this.catalog = catalog;
        this.schemaBuilder = schemaBuilder;
    }

    public DocumentHandler createDocumentHandler(ParserHandler parser) {
        return null;
    }

    public ElementHandler createElementHandler(QName name, Handler parent, ParserHandler parser) {
        String namespaceURI = name.getNamespaceURI();

        if (namespaceURI == null) {
            //assume default
            namespaceURI = catalog.getDefaultNameSpace().getURI();
        }

        try {
            //look for a FeatureType
            FeatureTypeInfo meta = catalog.getFeatureTypeInfo(name.getLocalPart(), namespaceURI);

            if (meta != null) {
                //found it
                XSDSchema schema = schemaBuilder.build(meta, null);

                for (Iterator e = schema.getElementDeclarations().iterator(); e.hasNext();) {
                    XSDElementDeclaration element = (XSDElementDeclaration) e.next();

                    if (name.getLocalPart().equals(element.getName())) {
                        return new ElementHandlerImpl(element, parent, parser);
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error building schema", e);
        }

        return null;
    }

    public ElementHandler createElementHandler(XSDElementDeclaration e, Handler parent,
        ParserHandler parser) {
        return null;
    }

    public AttributeHandler createAttributeHandler(XSDAttributeDeclaration a, Handler parent,
        ParserHandler parser) {
        return null;
    }
}

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionType;

import org.eclipse.emf.ecore.EObject;

/**
 * Transaction elements are an open ended set, both thanks to the Native element
 * type, and to the XSD sustitution group concept (xsd inheritance). Element
 * handlers know how to process a certain element in a wfs transaction request.
 * 
 * TODO: add/alter method calls to support validation and other general transaction plugins 
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public interface TransactionElementHandler {
    /**
     * Returns the element class this handler can proces
     */
    Class getElementClass();

    /**
     * Checks the element content is valid, throws an exception otherwise
     */
    void checkValidity(EObject element, Map featureTypeInfos) throws WFSTransactionException;

    /**
     * Returns a list of feature type names needed to handle this element
     */
    QName[] getTypeNames(EObject element) throws WFSTransactionException;

    /**
     * Executes the element against the provided feature sources
     * TODO: return a summary, such as a dirty envelope two feature collections,
     * one for inserted, one for updated elements (with the udpated state
     * Alternatively, make this stateful, and allow to access these by getters
     */
    void execute(EObject element, TransactionType request, Map featureStores,
            TransactionResponseType response) throws WFSTransactionException;
}

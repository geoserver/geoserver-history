/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Creates an encoder for a specific getfeature results output format
 * 
 * <p>
 * this opens the doors for future additions of new output formats (BXML?), by
 * decoupling the execution from the response generation of a getfeature
 * request
 * </p>
 *
 * @author Gabriel Roldán
 * @version $Id: FeatureResponseDelegateFactory.java,v 1.2 2004/03/12 10:19:44 cholmesny Exp $
 */
public class FeatureResponseDelegateFactory {
    /** DOCUMENT ME! */
    private static final List encoders = new LinkedList();

    static {
        encoders.add(new GML2FeatureResponseDelegate());
        encoders.add(new GML2AppSchemaFeatureResponseDelegate());
    }

    private FeatureResponseDelegateFactory() {
    }

    /**
     * Creates an encoder for a specific getfeature results output format
     *
     * @param outputFormat DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public static FeatureResponseDelegate encoderFor(String outputFormat)
        throws NoSuchElementException {
        FeatureResponseDelegate encoder = null;

        for (Iterator it = encoders.iterator(); it.hasNext();) {
            encoder = (FeatureResponseDelegate) it.next();

            if (encoder.canProduce(outputFormat)) {
                try {
                    if (encoder != null) {
                        return (FeatureResponseDelegate) encoder.getClass()
                                                                .newInstance();
                    }
                } catch (Exception ex) {
                    throw new NoSuchElementException(
                        "Can't create the encoder "
                        + encoder.getClass().getName());
                }
            }
        }

        throw new NoSuchElementException("Can't find an encoder for "
            + outputFormat + " output format");
    }
}

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.vfny.geoserver.wcs.responses.coverage.AscCoverageResponseDelegate;
import org.vfny.geoserver.wcs.responses.coverage.IMGCoverageResponseDelegate;


/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class CoverageResponseDelegateFactory {
    /** DOCUMENT ME! */
    private static final List encoders = new LinkedList();

    static {
        encoders.add(new AscCoverageResponseDelegate());
        encoders.add(new IMGCoverageResponseDelegate());
    }

    private CoverageResponseDelegateFactory() {
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
    public static CoverageResponseDelegate encoderFor(String outputFormat)
        throws NoSuchElementException {
        CoverageResponseDelegate encoder = null;

        for (Iterator it = encoders.iterator(); it.hasNext();) {
            encoder = (CoverageResponseDelegate) it.next();

            if (encoder.canProduce(outputFormat)) {
                try {
                    if (encoder != null) {
                        return (CoverageResponseDelegate) encoder.getClass()
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

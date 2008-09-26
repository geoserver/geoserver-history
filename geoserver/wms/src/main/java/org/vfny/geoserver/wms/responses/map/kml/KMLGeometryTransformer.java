/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import org.geotools.gml.producer.GeometryTransformer;
import org.geotools.xml.transform.Translator;
import org.xml.sax.ContentHandler;


/**
 * Geometry transformer for KML geometries.
 * <p>
 * This class does nothing beyond the normal gml2 geometry transformer
 * besides ensure no gml prefix is used.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class KMLGeometryTransformer extends GeometryTransformer {
    public Translator createTranslator(ContentHandler handler) {
        return new KMLGeometryTranslator(handler, numDecimals, useDummyZ);
    }

    /**
     * Subclass which sets prefix and nsuri to null.
     */
    static class KMLGeometryTranslator extends GeometryTranslator {
        public KMLGeometryTranslator(ContentHandler handler, int numDecimals, boolean useDummyZ) {
            //super(handler, "kml", "http://earth.google.com/kml/2.0" );
            super(handler, null, null, numDecimals, useDummyZ);
            coordWriter = new KMLCoordinateWriter(numDecimals, useDummyZ);
        }
    }
}

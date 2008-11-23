/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import javax.xml.namespace.QName;

import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.Encoder;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.NamespaceSupport;

import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


/**
 * This is a GML3 geometry encoder for KML - OWS5 branch. It has been an experiment
 * that people decided to discard.
 * </p>
 * @author Andrea Aime, TOPP
 *
 */
public class OWS5GeometryTransformer extends TransformerBase {
    
    public Translator createTranslator(ContentHandler handler) {
        return new KML3GeometryTranslator(handler);
    }

    /**
     * Subclass which sets prefix and nsuri to null.
     */
    static class KML3GeometryTranslator implements Translator {
        private Encoder encoder;
        private ContentHandler handler;

        public KML3GeometryTranslator(ContentHandler handler) {
            this.handler = handler;
            org.geotools.xml.Configuration configuration = new GMLConfiguration();
            this.encoder = new Encoder(configuration);
            this.encoder.setNamespaceAware(false);
        }

        public void abort() {
            // cannot do that
        }

        public void encode(Object o) throws IllegalArgumentException {
            if(o == null)
                return;
            
            QName name = null;
            if(o instanceof GeometryCollection) {
                if(o instanceof MultiPoint)
                    name = org.geotools.gml3.GML.MultiPoint;
                else if(o instanceof MultiLineString)
                    name = org.geotools.gml3.GML.MultiLineString;
                else if(o instanceof MultiPolygon)
                    name = org.geotools.gml3.GML.MultiSurface;
                else
                    name = org.geotools.gml3.GML.MultiGeometry;
            } else if(o instanceof Point)
                name = org.geotools.gml3.GML.Point;
            else if(o instanceof LineString)
                name = org.geotools.gml3.GML.LineString;
            else if(o instanceof Polygon)
                name = org.geotools.gml3.GML.Polygon;
            else
                throw new IllegalArgumentException("Cannot handle geometries of type " + o.getClass());
            
            try {
                encoder.encode(o, name, handler);
            } catch(IllegalArgumentException e) {
                throw e;
            } catch(Exception e) {
                throw new RuntimeException("Error occurred during gml3 encoding", e);
            }
        }

        public String getDefaultNamespace() {
            return encoder.getSchema().getTargetNamespace();
        }

        public String getDefaultPrefix() {
            return "gml";
        }

        public NamespaceSupport getNamespaceSupport() {
            return null;
        }

        public SchemaLocationSupport getSchemaLocationSupport() {
            return null;
        }
    }
}

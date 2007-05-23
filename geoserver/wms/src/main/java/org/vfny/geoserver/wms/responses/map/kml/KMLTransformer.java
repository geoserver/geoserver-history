/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;

import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.filter.BBoxExpression;
import org.geotools.filter.Expression;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.xml.sax.ContentHandler;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;


public class KMLTransformer extends TransformerBase {
    /**
     * Factory used to create filter objects
     */
    FilterFactory filterFactory = (FilterFactory) FilterFactoryFinder.createFilterFactory();

    /**
     * Flag controlling wether kmz was requested.
     */
    boolean kmz = false;

    public KMLTransformer() {
        setNamespaceDeclarationEnabled(false);
    }

    public Translator createTranslator(ContentHandler handler) {
        return new KMLTranslator(handler);
    }

    public void setFilterFactory(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    public void setKmz(boolean kmz) {
        this.kmz = kmz;
    }

    class KMLTranslator extends TranslatorSupport {
        public KMLTranslator(ContentHandler handler) {
            super(handler, null, null);
        }

        public void encode(Object o) throws IllegalArgumentException {
            start("kml");

            WMSMapContext mapContext = (WMSMapContext) o;
            GetMapRequest request = mapContext.getRequest();
            MapLayer[] layers = mapContext.getLayers();

            //if we have more than one layer ( or a legend was requested ),
            //use the name "GeoServer" to group them
            boolean group = layers.length > 1 || request.getLegend();
            if ( group ) {
                start("Document");
                element("name", "GeoServer");
            }

            //for every layer specified in the request
            for (int i = 0; i < layers.length; i++) {
                //layer and info
                MapLayer layer = layers[i];
                MapLayerInfo layerInfo = mapContext.getRequest().getLayers()[i];

                //was a super overlay requested?
                if ( mapContext.getRequest().getSuperOverlay() ) {
                    //encode as super overlay
                    encodeSuperOverlayLayer(mapContext, layer);    
                }
                else {
                    //figure out which type of layer this is, raster or vector
                    if (layerInfo.getType() == MapLayerInfo.TYPE_VECTOR) {
                        //vector 
                        encodeVectorLayer(mapContext, layer);
                    } 
                    else {
                        //encode as normal ground overlay
                        encodeRasterLayer(mapContext, layer);    
                    }        
                }
            }

            //legend suppoer
            if ( request.getLegend() ) {
                //for every layer specified in the request
                for (int i = 0; i < layers.length; i++) {
                    //layer and info
                    MapLayer layer = layers[i];
                    encodeLegend(mapContext,layer);
                    
                }
            }
            
            if (group) {
                end("Document");
            }

            end("kml");
        }

        /**
         * Encodes a vector layer as kml.
         */
        protected void encodeVectorLayer(WMSMapContext mapContext, MapLayer layer) {
            //get the data
            FeatureSource featureSource = layer.getFeatureSource();
            FeatureCollection features = null;

            try {
                features = loadFeatureCollection(featureSource, layer, mapContext);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //was kmz requested?
            if (kmz) {
                //calculate kmscore to determine if we shoud write as vectors
                // or pre-render
                int kmscore = mapContext.getRequest().getKMScore();
                boolean useVector = useVectorOutput(kmscore, features.size());

                if (useVector) {
                    //encode
                    KMLVectorTransformer tx = new KMLVectorTransformer(mapContext, layer);
                    tx.createTranslator(contentHandler).encode(features);
                } else {
                    KMLRasterTransformer tx = new KMLRasterTransformer(mapContext);

                    //set inline to true to have the transformer reference images
                    // inline in the zip file
                    tx.setInline(true);

                    tx.createTranslator(contentHandler).encode(layer);
                }
            } else {
                //kmz not selected, just do straight vector
                KMLVectorTransformer tx = new KMLVectorTransformer(mapContext, layer);
                tx.createTranslator(contentHandler).encode(features);
            }
        }

        /**
         * Encodes a raster layer as kml.
         */
        protected void encodeRasterLayer(WMSMapContext mapContext, MapLayer layer) {
            KMLRasterTransformer tx = new KMLRasterTransformer(mapContext);
            tx.setInline(kmz);

            tx.createTranslator(contentHandler).encode(layer);
        }

        /**
         * Encodes a layer as a super overlay.
         */
        protected void encodeSuperOverlayLayer( WMSMapContext mapContext, MapLayer layer ) {
            KMLSuperOverlayTransformer tx = new KMLSuperOverlayTransformer( mapContext );
            tx.createTranslator( contentHandler ).encode( layer );
        }
        
        /**
         * Encodes the legend for a maper layer as a scree overlay.
         */
        protected void encodeLegend( WMSMapContext mapContext, MapLayer layer  ) {
            KMLLegendTransformer tx = new KMLLegendTransformer( mapContext );
            tx.createTranslator( contentHandler ).encode( layer );
        }
        
        double computeScaleDenominator(MapLayer layer, WMSMapContext mapContext) {
            Rectangle paintArea = new Rectangle(mapContext.getMapWidth(), mapContext.getMapHeight());
            AffineTransform worldToScreen = RendererUtilities.worldToScreenTransform(mapContext
                    .getAreaOfInterest(), paintArea);

            try {
                //90 = OGC standard DPI (see SLD spec page 37)
                return RendererUtilities.calculateScale(mapContext.getAreaOfInterest(),
                    mapContext.getCoordinateReferenceSystem(), paintArea.width, paintArea.height, 90);
            } catch (Exception e) {
                //probably either (1) no CRS (2) error xforming, revert to
                // old method - the best we can do (DJB)
                return 1 / worldToScreen.getScaleX();
            }
        }

        /**
         * Determines whether to return a vector (KML) result of the data or to
         * return an image instead.
         * If the kmscore is 100, then the output should always be vector. If
         * the kmscore is 0, it should always be raster. In between, the number of
         * features is weighed against the kmscore value.
         * kmscore determines whether to return the features as vectors, or as one
         * raster image. It is the point, determined by the user, where X number of
         * features is "too many" and the result should be returned as an image instead.
         *
         * kmscore is logarithmic. The higher the value, the more features it takes
         * to make the algorithm return an image. The lower the kmscore, the fewer
         * features it takes to force an image to be returned.
         * (in use, the formula is exponential: as you increase the KMScore value,
         * the number of features required increases exponentially).
         *
         * @param kmscore the score, between 0 and 100, use to determine what output to use
         * @param numFeatures how many features are being rendered
         * @return true: use just kml vectors, false: use raster result
         */
        boolean useVectorOutput(int kmscore, int numFeatures) {
            if (kmscore == 100) {
                return true; // vector KML
            }

            if (kmscore == 0) {
                return false; // raster KMZ
            }

            // For numbers in between, determine exponentionally based on kmscore value:
            // 10^(kmscore/15)
            // This results in exponential growth.
            // The lowest bound is 1 feature and the highest bound is 3.98 million features
            // The most useful kmscore values are between 20 and 70 (21 and 46000 features respectively)
            // A good default kmscore value is around 40 (464 features)
            double magic = Math.pow(10, kmscore / 15);

            if (numFeatures > magic) {
                return false; // return raster
            } else {
                return true; // return vector
            }
        }

        FeatureCollection loadFeatureCollection(FeatureSource featureSource, MapLayer layer,
            WMSMapContext mapContext) throws Exception {
            FeatureType schema = featureSource.getSchema();

            Envelope envelope = mapContext.getAreaOfInterest();
            ReferencedEnvelope aoi = new ReferencedEnvelope(envelope,
                    mapContext.getCoordinateReferenceSystem());
            CoordinateReferenceSystem sourceCrs = schema.getDefaultGeometry().getCoordinateSystem();

            if ((sourceCrs != null)
                    && !CRS.equalsIgnoreMetadata(aoi.getCoordinateReferenceSystem(), sourceCrs)) {
                aoi = aoi.transform(sourceCrs, true);
            }

            BBoxExpression rightBBox = filterFactory.createBBoxExpression(aoi);
            Filter filter = createBBoxFilter(schema, rightBBox);

            // now build the query using only the attributes and the bounding
            // box needed
            DefaultQuery q = new DefaultQuery(schema.getTypeName());
            q.setFilter(filter);

            // now, if a definition query has been established for this layer, be
            // sure to respect it by combining it with the bounding box one.
            Query definitionQuery = layer.getQuery();

            if (definitionQuery != Query.ALL) {
                if (q == Query.ALL) {
                    q = (DefaultQuery) definitionQuery;
                } else {
                    q = (DefaultQuery) DataUtilities.mixQueries(definitionQuery, q, "KMLEncoder");
                }
            }

            q.setCoordinateSystem(layer.getFeatureSource().getSchema().getDefaultGeometry()
                                       .getCoordinateSystem());

            return featureSource.getFeatures(q);
        }

        /** Creates the bounding box filters (one for each geometric attribute) needed to query a
         * <code>MapLayer</code>'s feature source to return just the features for the target
         * rendering extent
         *
         * @param schema the layer's feature source schema
         * @param bbox the expression holding the target rendering bounding box
         * @return an or'ed list of bbox filters, one for each geometric attribute in
         *         <code>attributes</code>. If there are just one geometric attribute, just returns
         *         its corresponding <code>GeometryFilter</code>.
         * @throws IllegalFilterException if something goes wrong creating the filter
         */
        Filter createBBoxFilter(FeatureType schema, BBoxExpression bbox)
            throws IllegalFilterException {
            Filter filter = null;

            for (int j = 0; j < schema.getAttributeCount(); j++) {
                AttributeType attType = schema.getAttributeType(j);

                if (attType instanceof GeometryAttributeType) {
                    GeometryFilter gfilter = filterFactory.createGeometryFilter(Filter.GEOMETRY_BBOX);

                    // TODO: how do I get the full xpath of an attribute should
                    // feature composition be used?
                    Expression left = filterFactory.createAttributeExpression(schema,
                            attType.getName());
                    gfilter.addLeftGeometry(left);
                    gfilter.addRightGeometry(bbox);

                    if (filter == null) {
                        filter = gfilter;
                    } else {
                        filter = filter.or(gfilter);
                    }
                }
            }

            return filter;
        }
    }
}

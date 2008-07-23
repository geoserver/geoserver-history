/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.platform.GeoServerExtensions;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.crs.ReprojectFeatureResults;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureTypes;
import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.xml.sax.ContentHandler;

import com.vividsolutions.jts.geom.Envelope;


public class KMLTransformer extends TransformerBase {
    /**
     * logger
     */
    static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.kml");
        
    /**
     * Factory used to create filter objects
     */
    FilterFactory filterFactory = (FilterFactory) CommonFactoryFinder.getFilterFactory(null);
    
    private static final CoordinateReferenceSystem WGS84;
    
    static {
        try {
            WGS84 = CRS.decode("EPSG:4326");
        } catch(Exception e) {
            throw new RuntimeException("Cannot decode EPSG:4326, the CRS subsystem must be badly broken...");
        }
    }

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

    protected class KMLTranslator extends TranslatorSupport {
    	/**
         * Tolerance used to compare doubles for equality
         */
        static final double TOLERANCE = 1e-6;
        
        static final int RULES = 0;
        static final int ELSE_RULES = 1;
    	
        private double scaleDenominator;

		public KMLTranslator(ContentHandler handler) {
            super(handler, null, null);
        }

        public void encode(Object o) throws IllegalArgumentException {
            start("kml");

            WMSMapContext mapContext = (WMSMapContext) o;
            GetMapRequest request = mapContext.getRequest();
            MapLayer[] layers = mapContext.getLayers();
            
            //calculate scale denominator
            scaleDenominator = 1; 
            try {
               scaleDenominator = 
                       RendererUtilities.calculateScale(mapContext.getAreaOfInterest(), mapContext.getMapWidth(), mapContext.getMapHeight(), null);
            } 
            catch( Exception e ) {
               LOGGER.log( Level.WARNING, "Error calculating scale denominator", e );
            }
            LOGGER.fine( "scale denominator = " + scaleDenominator );

            //if we have more than one layer ( or a legend was requested ),
            //use the name "GeoServer" to group them
            boolean group = (layers.length > 1) || request.getLegend();

            if (group) {
                StringBuffer sb = new StringBuffer();
                for ( int i = 0; i < layers.length; i++ ) {
                    sb.append( layers[i].getTitle() + "," );
                }
                sb.setLength(sb.length()-1);
               
                start("Document");
                element("name", sb.toString() );
            }

            //for every layer specified in the request
            for (int i = 0; i < layers.length; i++) {
                //layer and info
                MapLayer layer = layers[i];
                MapLayerInfo layerInfo = mapContext.getRequest().getLayers()[i];

                //was a super overlay requested?
                if (mapContext.getRequest().getSuperOverlay()) {
                    //encode as super overlay
                    encodeSuperOverlayLayer(mapContext, layer);
                } else {
                    //figure out which type of layer this is, raster or vector
                    if (layerInfo.getType() == MapLayerInfo.TYPE_VECTOR || layerInfo.getType() == MapLayerInfo.TYPE_REMOTE_VECTOR) {
                        //vector 
                        encodeVectorLayer(mapContext, layer);
                    } else {
                        //encode as normal ground overlay
                        encodeRasterLayer(mapContext, layer);
                    }
                }
            }

            //legend suppoer
            if (request.getLegend()) {
                //for every layer specified in the request
                for (int i = 0; i < layers.length; i++) {
                    //layer and info
                    MapLayer layer = layers[i];
                    encodeLegend(mapContext, layer);
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
        @SuppressWarnings("unchecked")
        protected void encodeVectorLayer(WMSMapContext mapContext, MapLayer layer) {
            //get the data
            FeatureSource <SimpleFeatureType, SimpleFeature> featureSource;
            featureSource = (FeatureSource<SimpleFeatureType, SimpleFeature>) layer.getFeatureSource();
            FeatureCollection<SimpleFeatureType, SimpleFeature> features = null;

            try {
                features = loadFeatureCollection(featureSource, layer, mapContext);
                if(features == null)
                	return;
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
                    KMLVectorTransformer tx = createVectorTransformer(mapContext, layer);
                    initTransformer(tx);
                    tx.setScaleDenominator(scaleDenominator);
                    tx.createTranslator(contentHandler).encode(features);
                } else {
                    KMLRasterTransformer tx = createRasterTransfomer(mapContext);
                    initTransformer(tx);
                    
                    //set inline to true to have the transformer reference images
                    // inline in the zip file
                    tx.setInline(true);
                    tx.createTranslator(contentHandler).encode(layer);
                }
            } else {
                //kmz not selected, just do straight vector
                KMLVectorTransformer tx = createVectorTransformer(mapContext, layer);
                initTransformer(tx);
                tx.setScaleDenominator(scaleDenominator);
                tx.createTranslator(contentHandler).encode(features);
            }
        }

        /**
         * Factory method, allows subclasses to inject their own version of the raster transfomer
         * @param mapContext
         * @return
         */
        protected KMLRasterTransformer createRasterTransfomer(WMSMapContext mapContext) {
            return new KMLRasterTransformer(mapContext);
        }

        /**
         * Factory method, allows subclasses to inject their own version of the vector transfomer
         * @param mapContext
         * @return
         */
        protected KMLVectorTransformer createVectorTransformer(WMSMapContext mapContext,
                MapLayer layer) {
            return new KMLVectorTransformer(mapContext, layer);
        }

        /**
         * Encodes a raster layer as kml.
         */
        protected void encodeRasterLayer(WMSMapContext mapContext, MapLayer layer) {
            KMLRasterTransformer tx = createRasterTransfomer(mapContext);
            initTransformer(tx);
            
            tx.setInline(kmz);
            tx.createTranslator(contentHandler).encode(layer);
        }

        /**
         * Encodes a layer as a super overlay.
         */
        protected void encodeSuperOverlayLayer(WMSMapContext mapContext, MapLayer layer) {
            KMLSuperOverlayTransformer tx = new KMLSuperOverlayTransformer(mapContext);
            initTransformer(tx);
            tx.createTranslator(contentHandler).encode(layer);
        }

        /**
         * Encodes the legend for a maper layer as a scree overlay.
         */
        protected void encodeLegend(WMSMapContext mapContext, MapLayer layer) {
            KMLLegendTransformer tx = new KMLLegendTransformer(mapContext);
            initTransformer(tx);
            tx.createTranslator(contentHandler).encode(layer);
        }
        
        protected void initTransformer(KMLTransformerBase delegate) {
            delegate.setIndentation( getIndentation() );
            delegate.setEncoding(getEncoding());
            delegate.setStandAlone(false);
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

        FeatureCollection<SimpleFeatureType, SimpleFeature> loadFeatureCollection(
                FeatureSource <SimpleFeatureType, SimpleFeature> featureSource, MapLayer layer,
            WMSMapContext mapContext) throws Exception {
            SimpleFeatureType schema = featureSource.getSchema();

            Envelope envelope = mapContext.getAreaOfInterest();
            ReferencedEnvelope aoi = new ReferencedEnvelope(envelope,
                    mapContext.getCoordinateReferenceSystem());
            CoordinateReferenceSystem sourceCrs = schema.getCoordinateReferenceSystem();

            boolean reprojectBBox = (sourceCrs != null)
                && !CRS.equalsIgnoreMetadata(aoi.getCoordinateReferenceSystem(), sourceCrs); 
            if (reprojectBBox) {
                aoi = aoi.transform(sourceCrs, true);
            }

            Filter filter = createBBoxFilter(schema, aoi);

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

            //handle startIndex requested by client query
            q.setStartIndex(definitionQuery.getStartIndex());
            
            // check the regionating strategy
            RegionatingStrategy regionatingStrategy = null;
            String stratname = (String)mapContext.getRequest().getFormatOptions().get("regionateBy");
            if (("auto").equals(stratname)) {
                Data catalog = mapContext.getRequest().getWMS().getData();
                Name name = layer.getFeatureSource().getName();
                stratname = catalog.getFeatureTypeInfo(name).getRegionateStrategy();
                if(stratname == null)
                    throw new WmsException("No default regionating strategy has been configured in " + name);
            } 
            if(stratname != null) {
                List<RegionatingStrategyFactory> factories = GeoServerExtensions.extensions(RegionatingStrategyFactory.class);
                Iterator<RegionatingStrategyFactory> it = factories.iterator();
                while (it.hasNext()){
                    RegionatingStrategyFactory factory = it.next();
                    if (factory.canHandle(stratname)){
                        regionatingStrategy = factory.createStrategy();
                        break;
                    }
                }
                // if a strategy was specified but we did not find it, let the user know
                if(regionatingStrategy == null)
                    throw new WmsException("Unknown regionating strategy " + stratname);
            } 

            // try to load less features by leveraging regionating strategy and the SLD
            Filter regionatingFilter = Filter.INCLUDE;
            if(regionatingStrategy != null)
                regionatingFilter = regionatingStrategy.getFilter(mapContext, layer);
            Filter ruleFilter = summarizeRuleFilters(getLayerRules(featureSource.getSchema(), layer.getStyle()));
            Filter finalFilter = joinFilters(q.getFilter(), joinFilters(ruleFilter, regionatingFilter));
          	q.setFilter(finalFilter);
          	
          	// make sure we output in 4326 since that's what KML mandates
            if (sourceCrs != null && !CRS.equalsIgnoreMetadata(WGS84, sourceCrs)) {
                return new ReprojectFeatureResults( featureSource.getFeatures(q), WGS84 );
            } else {
                return featureSource.getFeatures(q);
            }
        }
        
        private List[] getLayerRules(SimpleFeatureType ftype, Style style) {
    		List[] result = new List[] {new ArrayList(), new ArrayList()};

    		final String typeName = ftype.getTypeName();
    		FeatureTypeStyle[] featureStyles = style.getFeatureTypeStyles();
			final int length = featureStyles.length;
    		for (int i = 0; i < length; i++)
    		{
    			// getting feature styles
    			FeatureTypeStyle fts = featureStyles[i];

    			// check if this FTS is compatible with this FT.
    			if ((typeName != null)
    					&& FeatureTypes.isDecendedFrom(ftype, null, fts.getFeatureTypeName())) {

    				// get applicable rules at the current scale
    				Rule[] ftsRules = fts.getRules();
    				for (int j = 0; j < ftsRules.length; j++) {
    					// getting rule
    					Rule r = ftsRules[j];

    					if (isWithInScale(r)) {
    						if (r.hasElseFilter()) {
    							result[ELSE_RULES].add(r);
    						} else {
    							result[RULES].add(r);
    						}
    					}
    				}
    			}
    		}

    		return result;
        }
        
        private Filter joinFilters(Filter first, Filter second) {
            if(Filter.EXCLUDE.equals(first) || Filter.EXCLUDE.equals(second))
                return Filter.EXCLUDE;
            
            if(first == null || Filter.INCLUDE.equals(first))
                return second;
            
            if(second == null || Filter.INCLUDE.equals(second))
                return first;
            
            FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
      		return ff.and(first, second);
        }

        /**
         * Summarizes, when possible, the rule filters into one. 
         * @param rules
         * @param originalFiter
         * @return
         */
       private Filter summarizeRuleFilters(List[] rules) {
           if(rules[RULES].size() == 0 || rules[ELSE_RULES].size() > 0)
               return Filter.INCLUDE;
           
            List filters = new ArrayList();
            for (Iterator it = rules[RULES].iterator(); it.hasNext();) {
                Rule rule = (Rule) it.next();
                // if there is a single rule asking for all filters, we have to 
                // return everything that the original filter returned already
                if(rule.getFilter() == null || Filter.INCLUDE.equals(rule.getFilter()))
                    return Filter.INCLUDE;
                else
                    filters.add(rule.getFilter());
            }
            
            FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
            return ff.or(filters);
        }
        
        /**
         * Checks if a rule can be triggered at the current scale level
         * 
         * @param r
         *            The rule
         * @return true if the scale is compatible with the rule settings
         */
         boolean isWithInScale(Rule r) {
                return ((r.getMinScaleDenominator() - TOLERANCE) <= scaleDenominator)
                    && ((r.getMaxScaleDenominator() + TOLERANCE) > scaleDenominator);
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
        Filter createBBoxFilter(SimpleFeatureType schema, Envelope bbox)
            throws IllegalFilterException {
            List filters = new ArrayList();
            for (int j = 0; j < schema.getAttributeCount(); j++) {
                AttributeDescriptor attType = schema.getDescriptor(j);

                if (attType instanceof GeometryDescriptor) {
                    Filter gfilter = filterFactory.bbox(attType.getLocalName(), bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY(), null);
                    filters.add(gfilter);
                }
            }

            if(filters.size() == 0)
                return Filter.INCLUDE;
            else if(filters.size() == 1)
                return (Filter) filters.get(0);
            else
                return filterFactory.or(filters);
        }
    }
}

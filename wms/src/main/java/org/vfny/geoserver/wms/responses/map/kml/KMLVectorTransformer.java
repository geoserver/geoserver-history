/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.geoserver.ows.util.RequestUtils;
import org.geoserver.template.FeatureWrapper;
import org.geoserver.template.GeoServerTemplateLoader;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureType;
import org.geotools.geometry.jts.JTS;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.style.LineStyle2D;
import org.geotools.renderer.style.MarkStyle2D;
import org.geotools.renderer.style.PolygonStyle2D;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.renderer.style.TextStyle2D;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.NumberRange;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.map.kml.KMLGeometryTransformer.KMLGeometryTranslator;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Transforms a feature collection to a kml "Document" consisting of nested
 * "Document" elements for each feature.
 * <p>
 * Usage:
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class KMLVectorTransformer extends TransformerBase {
    /**
     * logger
     */
    static Logger LOGGER = Logger.getLogger("org.geoserver.kml");

    /**
     * The template configuration used for placemark descriptions
     */
    static Configuration templateConfig;

    static {
        //initialize the template engine, this is static to maintain a cache 
        // over instantiations of kml writer
        templateConfig = new Configuration();
        templateConfig.setObjectWrapper(new FeatureWrapper());
    }

    /**
     * Tolerance used to compare doubles for equality
     */
    static final double TOLERANCE = 1e-6;

    /**
     * The map context
     */
    final WMSMapContext mapContext;

    /**
     * The map layer being transformed
     */
    final MapLayer mapLayer;

    /**
     * The scale denominator.
     *
     * TODO: calcuate a real value based on image size to bbox ratio, as image
     * size has no meanining for KML yet this is a fudge.
     */
    double scaleDenominator = 1;
    NumberRange scaleRange = new NumberRange(scaleDenominator, scaleDenominator);

    /**
     * used to create 2d style objects for features
     */
    SLDStyleFactory styleFactory = new SLDStyleFactory();

    /**
     * Source coordinate reference system
     */
    CoordinateReferenceSystem sourceCrs = DefaultGeographicCRS.WGS84;

    public KMLVectorTransformer(WMSMapContext mapContext, MapLayer mapLayer) {
        this.mapContext = mapContext;
        this.mapLayer = mapLayer;

        setNamespaceDeclarationEnabled(false);
    }

    /**
     * Sets the source coordinate reference system.
     *
     */
    public void setSourceCrs(CoordinateReferenceSystem sourceCrs) {
        this.sourceCrs = sourceCrs;
    }

    /**
     * Sets the scale denominator.
     */
    public void setScaleDenominator(double scaleDenominator) {
        this.scaleDenominator = scaleDenominator;
    }

    public Translator createTranslator(ContentHandler handler) {
        return new KMLTranslator(handler);
    }

    class KMLTranslator extends TranslatorSupport {
        /**
         * Geometry transformer
         */
        KMLGeometryTransformer.KMLGeometryTranslator geometryTranslator;

        public KMLTranslator(ContentHandler contentHandler) {
            //super(contentHandler, "kml", "http://earth.google.com/kml/2.0" );
            super(contentHandler, null, null);

            KMLGeometryTransformer geometryTransformer = new KMLGeometryTransformer();
            //geometryTransformer.setUseDummyZ( true );
            geometryTransformer.setOmitXMLDeclaration(true);
            geometryTransformer.setNamespaceDeclarationEnabled(true);

            GeoServer config = mapContext.getRequest().getGeoServer();
            geometryTransformer.setNumDecimals(config.getNumDecimals());

            geometryTranslator = (KMLGeometryTranslator) geometryTransformer.createTranslator(contentHandler);
        }

        public void encode(Object o) throws IllegalArgumentException {
            FeatureCollection features = (FeatureCollection) o;
            FeatureType featureType = features.getSchema();

            //start the root document, name it the name of the layer
            start("Document");
            element("name", mapLayer.getTitle());

            //get the styles for hte layer
            FeatureTypeStyle[] featureTypeStyles = filterFeatureTypeStyles(mapLayer.getStyle(),
                    featureType);

            for (int i = 0; i < featureTypeStyles.length; i++) {
                encode(features, featureTypeStyles[i]);
            }

            //encode the legend
            //encodeLegendScreenOverlay();
            
            end("Document");
        }

        protected void encode(FeatureCollection features, FeatureTypeStyle style) {
            //if no rules bail out early
            if ((style.getRules() == null) || (style.getRules().length == 0)) {
                return;
            }

            //grab a feader and process
            FeatureIterator reader = features.features();

            try {
                while (reader.hasNext()) {
                    Feature feature = (Feature) reader.next();

                    try {
                        encode(feature, style);
                    } catch (Throwable t) {
                        //TODO: perhaps rethrow hte exception
                        String msg = "Failure tranforming feature to KML:" + feature.getID();
                        LOGGER.log(Level.WARNING, msg, t);
                    }
                }
            } finally {
                //make sure we always close
                features.close(reader);
            }
        }

        protected void encode(Feature feature, FeatureTypeStyle style) {
            //get the feature id
            String featureId = featureId(feature);

            //start the document
            start("Document");

            element("name", featureId);
            element("title", mapLayer.getTitle());

            //encode the styles, keep track of any labels provided by the 
            // styles
            StringBuffer featureLabel = new StringBuffer();
            encodeStyle(feature, style, featureLabel);

            encodePlacemark(feature, featureLabel.toString());

            end("Document");
        }

        /**
         * Encodes the provided set of rules as KML styles.
         */
        protected void encodeStyle(Feature feature, FeatureTypeStyle style, StringBuffer label) {
            //start the style
            start("Style", KMLUtils.attributes(new String[] { "id", "GeoServerStyle" + feature.getID() }));

            Rule[] rules = filterRules(style, feature);

            for (int i = 0; i < rules.length; i++) {
                encodeStyle(feature, rules[i].getSymbolizers(), label);
            }

            //end the style
            end("Style");
        }

        /**
         * Encodes the provided set of symbolizers as KML styles.
         */
        protected void encodeStyle(Feature feature, Symbolizer[] symbolizers, StringBuffer label) {
            //encode the style for the icon
            //start IconStyle
            start("IconStyle");

            //make transparent if they didn't ask for attributes
            if (!mapContext.getRequest().getKMattr()) {
                encodeColor( "00ffffff" );
            }

            //start Icon
            start("Icon");
            
            if ( feature.getDefaultGeometry() != null && 
                feature.getDefaultGeometry() instanceof Point || 
                feature.getDefaultGeometry() instanceof MultiPoint  ) {
               
                //do nothing, this is handled by encodePointStyle
            }
            else if ( feature.getDefaultGeometry() != null && 
                 (   feature.getDefaultGeometry() instanceof LineString || 
                     feature.getDefaultGeometry() instanceof MultiLineString ) ) {
                //line
                element("href", "root://icons/palette-3.png");
                element("x", "224");
                element("y", "32");
                element("w", "32");
                element("h", "32");
            }
            else if ( feature.getDefaultGeometry() != null && 
                    feature.getDefaultGeometry() instanceof Polygon || 
                    feature.getDefaultGeometry() instanceof MultiPolygon   ) {
                //polygon
                element("href", "root://icons/palette-3.png");
                element("x", "224");
                element("y", "32");
                element("w", "32");
                element("h", "32");
            }
            else {
                //default
            }
            
            end("Icon");

            //end IconStyle
            end("IconStyle");
           
            for (int i = 0; i < symbolizers.length; i++) {
                Symbolizer symbolizer = symbolizers[i];
                LOGGER.finer(new StringBuffer("Applying symbolizer ").append(symbolizer).toString());

                //create a 2-D style
                Style2D style = styleFactory.createStyle(feature, symbolizer, scaleRange);

                //split out each type of symbolizer
                if (symbolizer instanceof TextSymbolizer) {
                    encodeTextStyle((TextStyle2D) style, (TextSymbolizer) symbolizer);

                    //figure out the label
                    Expression e = SLD.textLabel((TextSymbolizer) symbolizer);
                    String value = (String) e.evaluate(feature, String.class);

                    if ((value != null) && !"".equals(value.trim())) {
                        label.append(value);
                    }
                }

                if (symbolizer instanceof PolygonSymbolizer) {
                    encodePolygonStyle((PolygonStyle2D) style, (PolygonSymbolizer) symbolizer);
                }

                if (symbolizer instanceof LineSymbolizer) {
                    encodeLineStyle((LineStyle2D) style, (LineSymbolizer) symbolizer);
                }
                
                if (symbolizer instanceof PointSymbolizer) {
                    encodePointStyle( style, (PointSymbolizer) symbolizer);
                }
            }
        }

        /**
         * Encodes a KML IconStyle + PolyStyle from a polygon style and symbolizer.
         */
        protected void encodePolygonStyle(PolygonStyle2D style, PolygonSymbolizer symbolizer) {
            //star the polygon style
            start("PolyStyle");

            //fill
            if (symbolizer.getFill() != null) {
                //get opacity
                double opacity = SLD.opacity(symbolizer.getFill());

                if (Double.isNaN(opacity)) {
                    //none specified, default to full opacity
                    opacity = 1.0;
                }

                encodeColor( (Color) style.getFill(), opacity );
            } else {
                //make it transparent
                encodeColor( "00aaaaaa" );
            }

            //outline
            if (symbolizer.getStroke() != null) {
                element("outline", "1");
            } else {
                element("outline", "0");
            }

            end("PolyStyle");

            //if stroke specified add line style as well
            if (symbolizer.getStroke() != null) {
                start("LineStyle");

                //opacity
                double opacity = SLD.opacity(symbolizer.getStroke());

                if (Double.isNaN(opacity)) {
                    //none specified, default to full opacity
                    opacity = 1.0;
                }

                encodeColor( colorToHex((Color) style.getContour(), opacity) );
                
                //width
                int width = SLD.width(symbolizer.getStroke());

                if (width != SLD.NOTFOUND) {
                    element("width", Integer.toString(width));
                }

                end("LineStyle");
            }
        }

        /**
         * Encodes a KML IconStyle + LineStyle from a polygon style and symbolizer.
         */
        protected void encodeLineStyle(LineStyle2D style, LineSymbolizer symbolizer) {
            start("LineStyle");

            //stroke
            if (symbolizer.getStroke() != null) {
                //opacity
                double opacity = SLD.opacity(symbolizer.getStroke());

                if (Double.isNaN(opacity)) {
                    //default to full opacity
                    opacity = 1.0;
                }

                encodeColor( (Color) style.getContour(), opacity);
                
                //width
                int width = SLD.width(symbolizer.getStroke());

                if (width != SLD.NOTFOUND) {
                    element("width", Integer.toString(width));
                }
            } else {
                //default
                encodeColor( "ffaaaaaa" );
                element("width", "1");
            }

            end("LineStyle");
        }

        /**
         * Encodes a KML IconStyle from a point style and symbolizer.
         */
        protected void encodePointStyle(Style2D style, PointSymbolizer symbolizer) {
            start("IconStyle");

            if ( style instanceof MarkStyle2D ) {
                Mark mark = SLD.mark(symbolizer);    
                if (mark != null) {
                    double opacity = SLD.opacity(mark.getFill());

                    if (Double.isNaN(opacity)) {
                        //default to full opacity
                        opacity = 1.0;
                    }

                    encodeColor( SLD.color( mark.getFill() ), opacity );
                } else {
                    //default
                    encodeColor( "ffaaaaaa" );
                }
            }
            else {
                //default
                encodeColor( "ffaaaaaa" );
            }
            
            element("colorMode", "normal");

            // placemark icon
            
            String iconHref = null;
            
            //if the point symbolizer uses an external graphic use it
            if ( symbolizer.getGraphic() != null && 
                    symbolizer.getGraphic().getExternalGraphics() != null &&  
                    symbolizer.getGraphic().getExternalGraphics().length > 0 ) {
                
                ExternalGraphic graphic = 
                    symbolizer.getGraphic().getExternalGraphics()[0];
                try {
                    if ( "file".equals(  graphic.getLocation().getProtocol() ) ) {
                        //it is a local file, reference locally from "styles" directory
                        File file = new File( graphic.getLocation().getFile() );
                        iconHref = RequestUtils.baseURL( mapContext.getRequest().getHttpServletRequest() ) 
                            + "styles/" + file.getName();
                    }    
                    else {
                        //TODO: should we check for http:// and use it directly?
                    }
                }
                catch( Exception e ) {
                    LOGGER.log( Level.WARNING, "Error processing external graphic:" + graphic , e );
                }
                
            }
            
            if ( iconHref == null ) {
                iconHref = "root://icons/palette-4.png";
            }
            
            start("Icon");
            
            element("href", iconHref );    
            element("x", "32");
            element("y", "128");
            element("w", "32");
            element("h", "32");

            end("Icon");

            end("IconStyle");
        }

        /**
         * Encodes a KML LabelStyle from a text style and symbolizer.
         */
        protected void encodeTextStyle(TextStyle2D style, TextSymbolizer symbolizer) {
            start("LabelStyle");

            if (symbolizer.getFill() != null) {
                double opacity = SLD.opacity(symbolizer.getFill());

                if (Double.isNaN(opacity)) {
                    //default to full opacity
                    opacity = 1.0;
                }

                encodeColor( (Color) style.getFill(), opacity);
            } else {
                //default
                encodeColor("ffaaaaaa");
            }

            end("LabelStyle");
        }

        /**
         * Encodes a KML Placemark from a feature and optional name.
         */
        protected void encodePlacemark(Feature feature, String name) {
            Geometry geometry = featureGeometry(feature);
            Coordinate centroid = geometryCentroid(geometry);

            start("Placemark");

            //name
            if ((name == null) || "".equals(name.trim())) {
                name = feature.getID();
            }

            //element( "name", "<![CDATA[" + name + "]]>" );
            start("name");
            cdata(name);
            end("name");

            //description
            try {
                encodePlacemarkDescription(feature);
            } catch (IOException e) {
                //TODO: should we just log an error, since templates are fiddled
                // with by users we may not want to fail hard
                throw new RuntimeException(e);
            }

            //look at
            encodePlacemarkLookAt(centroid);

            //style reference
            element("styleUrl", "#GeoServerStyle" + feature.getID());

            //geometry
            encodePlacemarkGeometry(geometry, centroid);

            end("Placemark");
        }

        /**
         * Encodes a KML Placemark description from a feature by processing a
         * template.
         */
        protected void encodePlacemarkDescription(Feature feature)
            throws IOException {
            String description = null;

            if (mapContext.getRequest().getKMattr()) {
                //descriptions are "templatable" by users, so see if there is a 
                // template available for use
                FeatureType schema = feature.getFeatureType();
                GeoServerTemplateLoader templateLoader = new GeoServerTemplateLoader(getClass());
                templateLoader.setFeatureType(schema.getTypeName());

                Template template = null;

                //Configuration is not thread safe
                synchronized (templateConfig) {
                    templateConfig.setTemplateLoader(templateLoader);
                    template = templateConfig.getTemplate("kmlPlacemarkDescription.ftl");
                }

                StringWriter writer = new StringWriter();

                try {
                    template.process(feature, writer);
                } catch (TemplateException e) {
                    String msg = "Error occured processing template.";
                    throw (IOException) new IOException(msg).initCause(e);
                }

                description = writer.toString();
            }

            if ( description != null ) {
                start("description");
                cdata(description);   
                end("description");
            }
            
        }

        /**
         * Encods a KML Placemark LookAt from a geometry + centroid.
         */
        protected void encodePlacemarkLookAt(Coordinate centroid) {
            start("LookAt");

            element("longitude", Double.toString(centroid.x));
            element("latitude", Double.toString(centroid.y));
            element("range", "700");
            element("tilt", "10.0");
            element("heading", "10.0");

            end("LookAt");
        }

        /**
         * Encodes a KML Placemark geometry from a geometry + centroid.
         */
        protected void encodePlacemarkGeometry(Geometry geometry, Coordinate centroid) {
            start("MultiGeometry");

            //the centroid
            start("Point");
            if ( !Double.isNaN( centroid.z ) ) {
                element("coordinates", centroid.x + "," + centroid.y + "," + centroid.z);    
            }
            else {
                element("coordinates", centroid.x + "," + centroid.y );   
            }
            
            end("Point");

            //the actual geometry
            encodeGeometry(geometry);

            end("MultiGeometry");
        }

        /**
         * Encodes a KML geometry.
         */
        protected void encodeGeometry(Geometry geometry) {
            if (geometry instanceof GeometryCollection) {
                //unwrap the collection
                GeometryCollection collection = (GeometryCollection) geometry;

                for (int i = 0; i < collection.getNumGeometries(); i++) {
                    encodeGeometry(collection.getGeometryN(i));
                }
            } else {
                geometryTranslator.encode(geometry);
            }
        }

        /**
         * Encodes a color element from its color + opacity representation.
         * 
         * @param color The color to encode.
         * @param opacity The opacity ( alpha ) of the color.
         */
        void encodeColor( Color color, double opacity ) {
            encodeColor( colorToHex(color, opacity) );
        }
        
        /**
         * Encodes a color element from its hex representation.
         * 
         * @param hex The hex value ( with alpha ) of the color.
         * 
         */
        void encodeColor( String hex ) {
            element( "color", hex );
        }
        
        /**
         * Filters a set of rules by the current scale.
         *
         * @param rules The original rule set.
         *
         * @return The filtered rule set.
         */
        Rule[] filterRulesByScale(Rule[] rules) {
            List filtered = new ArrayList(rules.length);

            for (int j = 0; j < rules.length; j++) {
                Rule r = rules[j];
                double min = r.getMinScaleDenominator();
                double max = r.getMaxScaleDenominator();

                if (((min - TOLERANCE) <= scaleDenominator)
                        && ((max + TOLERANCE) >= scaleDenominator)) {
                    filtered.add(r);
                }
            }

            return (Rule[]) filtered.toArray(new Rule[filtered.size()]);
        }

        /**
         * Returns the id of the feature removing special characters like
         * '&','>','<','%'.
         */
        String featureId(Feature feature) {
            String id = feature.getID();
            id = id.replaceAll("&", "");
            id = id.replaceAll(">", "");
            id = id.replaceAll("<", "");
            id = id.replaceAll("%", "");

            return id;
        }

        /**
         * Rreturns the geometry for the feature reprojecting if necessary.
         */
        Geometry featureGeometry(Feature f) {
            // get the geometry
            Geometry geom = f.getDefaultGeometry();

            if (!CRS.equalsIgnoreMetadata(sourceCrs, mapContext.getCoordinateReferenceSystem())) {
                try {
                    MathTransform transform = CRS.findMathTransform(sourceCrs,
                            mapContext.getCoordinateReferenceSystem(), true);
                    geom = JTS.transform(geom, transform);
                } catch (MismatchedDimensionException e) {
                    LOGGER.severe(e.getLocalizedMessage());
                } catch (TransformException e) {
                    LOGGER.severe(e.getLocalizedMessage());
                } catch (FactoryException e) {
                    LOGGER.severe(e.getLocalizedMessage());
                }
            }

            return geom;
        }

        /**
         * Returns the centroid of the geometry, handling  a geometry collection.
         * <p>
         * In the case of a collection a multi point containing the centroid of
         * each geometry in the collection is calculated. The first point in
         * the multi point is returned as the cetnroid.
         * </p>
         */
        Coordinate geometryCentroid(Geometry g) {
            //TODO: should the collecftion case return the centroid of hte 
            // multi point?
            if (g instanceof GeometryCollection) {
                GeometryCollection gc = (GeometryCollection) g;
                Coordinate[] pts = new Coordinate[gc.getNumGeometries()];

                for (int t = 0; t < gc.getNumGeometries(); t++) {
                    pts[t] = gc.getGeometryN(t).getCentroid().getCoordinate();
                }

                return g.getFactory().createMultiPoint(pts).getCoordinates()[0];
            } else {
                return g.getCentroid().getCoordinate();
            }
        }

        /**
         * Utility method to convert an int into hex, padded to two characters.
         * handy for generating colour strings.
         *
         * @param i Int to convert
         * @return String a two character hex representation of i
         * NOTE: this is a utility method and should be put somewhere more useful.
         */
        String intToHex(int i) {
            String prelim = Integer.toHexString(i);

            if (prelim.length() < 2) {
                prelim = "0" + prelim;
            }

            return prelim;
        }

        /**
         * Utility method to convert a Color and opacity (0,1.0) into a KML
         * color ref.
         *
         * @param c The color to convert.
         * @param opacity Opacity / alpha, double from 0 to 1.0.
         *
         * @return A String of the form "#AABBGGRR".
         */
        String colorToHex(Color c, double opacity) {
            return new StringBuffer().append(intToHex(new Float(255 * opacity).intValue()))
                .append(intToHex(c.getBlue())).append(intToHex(c.getGreen()))
                .append(intToHex(c.getRed())).toString();
        }

        /**
         * Filters the feature type styles of <code>style</code> returning only
         * those that apply to <code>featureType</code>
         * <p>
         * This methods returns feature types for which
         * <code>featureTypeStyle.getFeatureTypeName()</code> matches the name
         * of the feature type of <code>featureType</code>, or matches the name of
         * any parent type of the feature type of <code>featureType</code>. This
         * method returns an empty array in the case of which no rules match.
         * </p>
         * @param style The style containing the feature type styles.
         * @param featureType The feature type being filtered against.
         *
         */
        protected FeatureTypeStyle[] filterFeatureTypeStyles(Style style, FeatureType featureType) {
            FeatureTypeStyle[] featureTypeStyles = style.getFeatureTypeStyles();

            if ((featureTypeStyles == null) || (featureTypeStyles.length == 0)) {
                return new FeatureTypeStyle[0];
            }

            ArrayList filtered = new ArrayList(featureTypeStyles.length);

            for (int i = 0; i < featureTypeStyles.length; i++) {
                FeatureTypeStyle featureTypeStyle = featureTypeStyles[i];
                String featureTypeName = featureTypeStyle.getFeatureTypeName();

                //does this style apply to the feature collection
                if (featureType.getTypeName().equalsIgnoreCase(featureTypeName)
                        || featureType.isDescendedFrom(null, featureTypeName)) {
                    filtered.add(featureTypeStyle);
                }
            }

            return (FeatureTypeStyle[]) filtered.toArray(new FeatureTypeStyle[filtered.size()]);
        }

        /**
         * Filters the rules of <code>featureTypeStyle</code> returnting only
         * those that apply to <code>feature</code>.
         * <p>
         * This method returns rules for which:
         * <ol>
         *  <li><code>rule.getFilter()</code> matches <code>feature</code>, or:
         *  <li>the rule defines an "ElseFilter", and the feature matches no
         *  other rules.
         * </ol>
         * This method returns an empty array in the case of which no rules
         * match.
         * </p>
         * @param featureTypeStyle The feature type style containing the rules.
         * @param feature The feature being filtered against.
         *
         */
        Rule[] filterRules(FeatureTypeStyle featureTypeStyle, Feature feature) {
            Rule[] rules = featureTypeStyle.getRules();

            if ((rules == null) || (rules.length == 0)) {
                return new Rule[0];
            }

            ArrayList filtered = new ArrayList(rules.length);

            //process the rules, keep track of the need to apply an else filters
            boolean match = false;
            boolean hasElseFilter = false;

            for (int i = 0; i < rules.length; i++) {
                Rule rule = rules[i];
                LOGGER.finer(new StringBuffer("Applying rule: ").append(rule.toString()).toString());

                //does this rule have an else filter
                if (rule.hasElseFilter()) {
                    hasElseFilter = true;

                    continue;
                }

                //does this rule have a filter which applies to the feature
                Filter filter = rule.getFilter();

                if ((filter == null) || filter.evaluate(feature)) {
                    match = true;

                    filtered.add(rule);
                }
            }

            //if no rules mached the feautre, re-run through the rules applying
            // any else filters
            if (!match && hasElseFilter) {
                //loop through again and apply all the else rules
                for (int i = 0; i < rules.length; i++) {
                    Rule rule = rules[i];

                    if (rule.hasElseFilter()) {
                        filtered.add(rule);
                    }
                }
            }

            return (Rule[]) filtered.toArray(new Rule[filtered.size()]);
        }

       
    }
}

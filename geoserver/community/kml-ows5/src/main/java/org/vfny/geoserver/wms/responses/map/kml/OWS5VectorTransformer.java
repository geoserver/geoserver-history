package org.vfny.geoserver.wms.responses.map.kml;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.geotools.feature.FeatureCollection;
import org.geotools.map.MapLayer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.OtherText;
import org.geotools.styling.Rule;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.geotools.xml.impl.DatatypeConverterImpl;
import org.geotools.xml.impl.DatatypeConverterInterface;
import org.geotools.xml.transform.Translator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.expression.Expression;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

public class OWS5VectorTransformer extends KMLVectorTransformer {
    
    private static DatatypeConverterInterface dataTypeConverter = DatatypeConverterImpl.getInstance(); 
    private boolean extendedDataModule;
    private boolean styleModule;

    public OWS5VectorTransformer(WMSMapContext mapContext, MapLayer mapLayer, 
            boolean extendedDataModule, boolean styleModule) {
        super(mapContext, mapLayer);
        this.extendedDataModule = extendedDataModule;
        this.styleModule = styleModule;
    }

    public Translator createTranslator(ContentHandler handler) {
        return new KML3Translator(handler);
    }

    protected class KML3Translator extends KMLTranslator {
        private boolean kml22DataStyle = false;
        protected String schemaId;

        public KML3Translator(ContentHandler contentHandler) {
            super(contentHandler);
            
            KMLGeometryTransformer geometryTransformer = new KMLGeometryTransformer();
            //geometryTransformer.setUseDummyZ( true );
            geometryTransformer.setOmitXMLDeclaration(true);
            geometryTransformer.setNamespaceDeclarationEnabled(true);

            GeoServer config = mapContext.getRequest().getGeoServer();
            geometryTransformer.setNumDecimals(config.getNumDecimals());

            geometryTranslator = geometryTransformer.createTranslator(contentHandler);
            
//            GML3 outputting transformer
//            OWS5GeometryTransformer geometryTransformer = new OWS5GeometryTransformer();
//            geometryTranslator = (KML3GeometryTranslator) geometryTransformer.createTranslator(contentHandler);

            // we need to make sure the data type converter is registered properly

            // We need to create a unique ID for this schema. Given the
            // restrictions of the XML ID
            // type and the fact the same feature type may appear multiple times
            // in the same
            // MapContext, we need to create an artificial ID that's guaranteed
            // to be unique across
            // the encoded KML document. We'll use the layer position as the ID.
            final MapLayer[] layers = OWS5VectorTransformer.this.mapContext.getLayers();
            for (int i = 0; i < layers.length; i++) {
                if (layers[i] == OWS5VectorTransformer.this.mapLayer) {
                    schemaId = "Schema" + (i + 1);
                }
            }
            if (schemaId == null)
                throw new IllegalStateException("Wrapping KMLVectorTransformer holds "
                        + "a layer reference that's not among the MapLayer ones");
        }

        public void encodeSchemas(FeatureCollection collection) {
            // if the extended data module is not active, don't encode schema and extended data
            if(!extendedDataModule)
                return;
            
            // TODO: consider turning this into a Freemarker template
            final SimpleFeatureType schema = (SimpleFeatureType) collection.getSchema();
            final String[] atts = new String[] { "name", schema.getTypeName(), "id", schemaId };
            start("Schema", KMLUtils.attributes(atts));

            // output all non geometric attribute types
            // TODO: or shall we encode only the types that are well known in SchemaData
            // and jump over the rest? Besides, how does one limit the number of attributes
            // displayed? a PROPERTY=x,y,z like in GetFeature would be beneficial 
            // to GetFeatureInfo as well
            for (int i = 0; i < schema.getAttributeCount(); i++) {
                AttributeDescriptor at = schema.getDescriptor(i);
                if (at instanceof GeometryDescriptor)
                    continue;

                final String[] atAttributes = new String[] { "type", getType(at), "name",
                        at.getLocalName() };
                start("SimpleField", KMLUtils.attributes(atAttributes));
                element("displayName", at.getLocalName());
                end("SimpleField");
            }

            end("Schema");
        }

        protected String getType(AttributeDescriptor at) {
            // see
            // http://code.google.com/apis/kml/documentation/kml_tags_beta1.html#simplefield
            // Eventually see if we need to support uint/ushort as well (do we
            // have any standard filter for positive numbers?) and clarify
            // what's the range of int and short
            if (Short.class.equals(at.getType().getBinding()))
                return "short";
            else if (Integer.class.equals(at.getType().getBinding()))
                return "int";
            else if (Float.class.equals(at.getType().getBinding()))
                return "float";
            else if (Double.class.equals(at.getType().getBinding()))
                return "double";
            else if (Boolean.class.equals(at.getType().getBinding()))
                return "bool";
            else
                return "string";
        }

        protected void encodeExtendedData(SimpleFeature feature) {
            // if the extended data module is not active, don't encode schema and extended data
            if(!extendedDataModule)
                return;
            
            if(kml22DataStyle)
                encodeKML22ExtendedData(feature);
            else 
                encodeKMLOWS5ExtendedData(feature);
        }

        private void encodeKML22ExtendedData(SimpleFeature feature) {
            // TODO: consider turning this into a Freemarker template
            start("ExtendedData");
            start("SchemaData", KMLUtils.attributes(new String[] { "schemaUrl", "#" + schemaId }));

            final int count = feature.getAttributeCount();
            final SimpleFeatureType schema = feature.getFeatureType();
            for (int i = 0; i < count; i++) {
                final AttributeDescriptor at = schema.getDescriptor(i);
                if(at instanceof GeometryDescriptor)
                    continue;
                
                final Attributes atts = KMLUtils.attributes(new String[] { "name",
                        at.getLocalName() });
                element("SimpleData", encodeValue(feature.getAttribute(i)), atts);
            }

            end("SchemaData");
            end("ExtendedData");
        }
        
        private void encodeKMLOWS5ExtendedData(SimpleFeature feature) {
            // TODO: consider turning this into a Freemarker template
            start("ExtendedData", KMLUtils.attributes(new String[] { "schemaUrl", "#" + schemaId }));

            final int count = feature.getAttributeCount();
            final SimpleFeatureType schema = feature.getFeatureType();
            for (int i = 0; i < count; i++) {
                final AttributeDescriptor at = schema.getDescriptor(i);
                if(at instanceof GeometryDescriptor)
                    continue;
                
                final Attributes atts = KMLUtils.attributes(new String[] { "name",
                        at.getLocalName() });
                element("Data", encodeValue(feature.getAttribute(i)), atts);
            }

            end("ExtendedData");
        }

        protected String encodeValue(Object o) {
            if (o == null) {
                return "";
            } else if (o instanceof Number) {
                if (o instanceof Byte)
                    return dataTypeConverter.printByte(((Byte) o).byteValue());
                else if (o instanceof Short)
                    return dataTypeConverter.printShort(((Short) o).shortValue());
                else if (o instanceof Integer)
                    return dataTypeConverter.printInt((((Integer) o).intValue()));
                else if (o instanceof Long)
                    return dataTypeConverter.printLong(((Long) o).intValue());
                else if (o instanceof Float)
                    return dataTypeConverter.printInt((((Float) o).intValue()));
                else if (o instanceof Double)
                    return dataTypeConverter.printInt(((Double) o).intValue());
                else if (o instanceof BigDecimal)
                    return dataTypeConverter.printDecimal((BigDecimal) o);
                else
                    return dataTypeConverter.printString(o.toString());
            } else if (o instanceof Boolean) {
                return dataTypeConverter.printBoolean((((Boolean) o).booleanValue()));
            } else if (o instanceof Date) {
                final Date d = (Date) o;
                final Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                if (d instanceof java.sql.Date)
                    return dataTypeConverter.printDate(cal);
                else if (d instanceof java.sql.Time)
                    return dataTypeConverter.printTime(cal);
                else
                    return dataTypeConverter.printDateTime(cal);
            } else {
                return dataTypeConverter.printString(o.toString());
            }

        }
        
//        /**
//         * Encodes a KML Placemark geometry from a geometry + centroid.
//         */
//        protected void encodePlacemarkGeometry(Geometry geometry, Coordinate centroid, FeatureTypeStyle[] styles) {
//            // if point, just encode a single point, otherwise encode the geometry + centroid
//            if ( geometry instanceof Point || 
//                    (geometry instanceof MultiPoint) && ((MultiPoint)geometry).getNumPoints() == 1 ) {
//                encodeGeometry( geometry, styles);
//            }
//            else {
//                start("MultiGeometry");
//
//                //the centroid
//                start("Point");
//
//                if (!Double.isNaN(centroid.z)) {
//                    element("pos", centroid.x + " " + centroid.y + " " + centroid.z);
//                } else {
//                    element("pos", centroid.x + " " + centroid.y);
//                }
//
//                end("Point");
//
//                //the actual geometry
//                encodeGeometry(geometry, styles);
//
//                end("MultiGeometry");
//            }
//            
//        }
        
        @Override
        protected void encodePlacemarkDescription(SimpleFeature feature, FeatureTypeStyle[] styles)
                throws IOException {
            // look for a kml text style with the description attribute
            List<TextSymbolizer2> textSymbolizers = getTextSymbolizers2(feature, styles);
            Expression description = null;
            for (TextSymbolizer2 ts : textSymbolizers) {
                if(ts.getFeatureDescription() != null)
                    description = ts.getFeatureDescription();
            }
            if(description == null) {
                // use the freemarker template as a fallback
                super.encodePlacemarkDescription(feature, styles);
                return;
            }
            
            start("description");
            cdata(description.evaluate(feature, String.class));
            end("description");
        }
        
        @Override
        protected void encodePlacemarkSnippet(SimpleFeature feature, FeatureTypeStyle[] styles) {
            // look for a kml text style with the abstract attribute
            List<TextSymbolizer2> textSymbolizers = getTextSymbolizers2(feature, styles);
            Expression abxtract = null;
            for (TextSymbolizer2 ts : textSymbolizers) {
                if(ts.getSnippet() != null)
                    abxtract = ts.getSnippet();
            }
            if(abxtract == null) {
                // no snippet then...
                return;
            }
            start("Snippet");
            cdata(abxtract.evaluate(feature, String.class));
            end("Snippet");
        }
        
        @Override
        protected void encodePlacemarkTime(SimpleFeature feature, FeatureTypeStyle[] styles)
                throws IOException {
            // look for a kml text style with the time/startTime/endTime otherText attributes
            List<TextSymbolizer2> textSymbolizers = getTextSymbolizers2(feature, styles);
            Expression abxtract = null;
            Date fromDate = null;
            Date toDate = null;
            Date timestamp = null;
            for (TextSymbolizer2 ts : textSymbolizers) {
                final OtherText ot = ts.getOtherText();
                if(ot != null && ot.getTarget() != null && ot.getText() != null) {
                    if(ot.getTarget().toLowerCase().equals("kml:fromdate")) 
                        fromDate = ot.getText().evaluate(feature, Date.class);
                    else if(ot.getTarget().toLowerCase().equals("kml:todate"))
                        toDate = ot.getText().evaluate(feature, Date.class);
                    else if(ot.getTarget().toLowerCase().equals("kml:timestamp"))
                        timestamp = ot.getText().evaluate(feature, Date.class);
                }
            }
            try {
                if(fromDate != null || toDate != null)
                    encodeKmlTimeSpan(fromDate, toDate);
                else if(timestamp != null)
                    encodeKmlTimeStamp(timestamp);
                else
                    super.encodePlacemarkTime(feature, styles);
            } catch(Exception e) {
                throw (IOException) new IOException().initCause(e);
            }
        }

        /**
         * Extracts all of the TextSymbolizer2 from the active rules, in the order
         * they are declared.
         * @param feature
         * @param styles
         * @return
         */
        private List<TextSymbolizer2> getTextSymbolizers2(SimpleFeature feature,
                FeatureTypeStyle[] styles) {
            List<TextSymbolizer2> textSymbolizers = new ArrayList<TextSymbolizer2>();
            for (int i = 0; i < styles.length; i++) {
                Rule[] rules = filterRules(styles[i], feature );
                for ( int j = 0; j < rules.length; j++ ) {
                    Symbolizer[] syms = rules[j].getSymbolizers();
                    for ( int k = 0; k < syms.length; k++) {
                        if ( syms[k] instanceof TextSymbolizer2) {
                            textSymbolizers.add((TextSymbolizer2) syms[k]);
                        }
                    }
                }
            }
            return textSymbolizers;
        }
        
        
        /**
         * Encodes the provided set of rules as KML styles. Override that handles
         * the style definition and encodes it only if the style module is enabled
         */
        protected boolean encodeStyle(SimpleFeature feature, FeatureTypeStyle[] styles) {
           
            //encode hte Line/Poly styles
            List symbolizerList = new ArrayList();
            for ( int j = 0; j < styles.length ; j++ ) {
               Rule[] rules = filterRules(styles[j], feature);
                
                for (int i = 0; i < rules.length; i++) {
                    symbolizerList.addAll(Arrays.asList(rules[i].getSymbolizers()));
                }
            }
            
            if ( !symbolizerList.isEmpty() ) {
                if(styleModule) {
                    //start the style
                    start("Style",
                        KMLUtils.attributes(new String[] { "id", "GeoServerStyle" + feature.getID() }));
    
                    //encode the icon
                    encodeIconStyle(feature, styles);
    
                    Symbolizer[] symbolizers = (Symbolizer[]) symbolizerList.toArray(new Symbolizer[symbolizerList.size()]);
                    encodeStyle(feature, symbolizers);
                    
                    //end the style
                    end("Style");
                }
                
                // we return true anyways because otherwise the geometry won't be encoded
                return true;
            }
            else {
                //dont encode
                return false;
            }

        }


    }

}

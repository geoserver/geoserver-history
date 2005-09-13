/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import java.awt.Color;
import java.awt.Paint;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.util.Range;
import javax.xml.transform.TransformerFactory;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureType;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.renderer.style.PolygonStyle2D;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;
import org.geotools.util.NumberRange;
import org.vfny.geoserver.wms.WMSMapContext;
import com.vividsolutions.jts.geom.Envelope;
import java.util.Iterator;
import javax.xml.transform.TransformerException;
import org.geotools.filter.Filter;
import org.geotools.gml.producer.GeometryTransformer;

import org.geotools.renderer.style.Style2D;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.TextSymbolizer;
import org.opengis.referencing.operation.MathTransform2D;


/**
 * Writer for KML (Keyhole Markup Language) files.
 * Normaly controled by an EncodeKML instance, this class handles the styling information
 * and ensures that the geometries produced match the psuo GML expected by GE.
 *
 * @REVISIT: Once this is fully working, revisit as an extention to TransformerBase
 * @author James Macgill
 */
public class KMLWriter extends OutputStreamWriter {
    private static final Logger LOGGER = Logger.getLogger(KMLWriter.class.getPackage()
    .getName());
    
    /**
     * a number formatter set up to write KML legible numbers
     */
    private static DecimalFormat formatter;
    
    /**
     * Resolves the FeatureTypeStyle info per feature into a Style2D object.
     */
    private SLDStyleFactory styleFactory = new SLDStyleFactory();
    
    //TODO: calcuate a real value based on image size to bbox ratio, as image size has no meanining for KML yet this is a fudge.
    private double scaleDenominator = 1;
    
    /**
     * Handles the outputing of geometries as GML
     **/
    private GeometryTransformer transformer; 
    
    static {
        Locale locale = new Locale("en", "US");
        
        DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols(locale);
        decimalSymbols.setDecimalSeparator('.');
        formatter = new DecimalFormat();
        formatter.setDecimalFormatSymbols(decimalSymbols);
        
        //do not group
        formatter.setGroupingSize(0);
        
        //do not show decimal separator if it is not needed
        formatter.setDecimalSeparatorAlwaysShown(false);
        formatter.setDecimalFormatSymbols(null);
        
        //set default number of fraction digits
        formatter.setMaximumFractionDigits(5);
        
        //minimun fraction digits to 0 so they get not rendered if not needed
        formatter.setMinimumFractionDigits(0);
    }
    
    
    /** Holds the map layer set, styling info and area of interest bounds */
    private WMSMapContext mapContext;
    
    /**
     * Creates a new KMLWriter object.
     *
     * @param out OutputStream to write the KML into
     * @param config WMSMapContext describing the map to be generated.
     */
    public KMLWriter(OutputStream out, WMSMapContext mapContext) {
        super(out);
        this.mapContext = mapContext;
        
        transformer = new GeometryTransformer();
        //transformer.setUseDummyZ(true);
        transformer.setOmitXMLDeclaration(true);
        transformer.setNamespaceDeclarationEnabled(true);
    }
    
    /**
     * Sets the maximum number of digits allowed in the fraction portion of a
     * number.
     *
     * @param numDigits
     * @see NumberFormat#setMaximumFractionDigits
     */
    public void setMaximunFractionDigits(int numDigits) {
        formatter.setMaximumFractionDigits(numDigits);
    }
    
    /**
     * Gets the maximum number of digits allowed in the fraction portion of a
     * number.
     *
     * @return int numDigits
     * @see NumberFormat#getMaximumFractionDigits
     */
    public int getMaximunFractionDigits() {
        return formatter.getMaximumFractionDigits();
    }
    
    /**
     * Sets the minimum number of digits allowed in the fraction portion of a
     * number.
     *
     * @param numDigits
     * @see NumberFormat#setMinimumFractionDigits
     */
    public void setMinimunFractionDigits(int numDigits) {
        formatter.setMinimumFractionDigits(numDigits);
    }
    
    /* Sets the minimum number of digits allowed in the fraction portion of a
     * number.
     *
     * @param numDigits
     * @see NumberFormat#getMinimumFractionDigits
     */
    public int getMinimunFractionDigits() {
        return formatter.getMinimumFractionDigits();
    }
    
    /**
     * Formated version of standard write double
     *
     * @param d The double to format and write out.
     *
     * @throws IOException
     */
    public void write(double d) throws IOException {
        write(formatter.format(d));
    }
    
    /**
     * Convinience method to add a newline char to the output
     *
     * @throws IOException
     */
    public void newline() throws IOException {
        super.write('\n');
    }
    
    /**
     * Write all the features in a collection which pass the rules in the provided
     * Style object.  
     *
     * @TODO: support Name and Description information
     */
    public void writeFeatures(FeatureCollection features, Style style)
    throws IOException, AbortedException {
        Feature ft;
        
        try {
            FeatureType featureType = features.getSchema();
            Class gtype = featureType.getDefaultGeometry().getType();
            
            setUpWriterHandler(featureType);
            startFolder(null, null);
            FeatureTypeStyle[] fts = style.getFeatureTypeStyles();
            processStylers(features, fts);
            endFolder();
            
            LOGGER.fine("encoded " + featureType.getTypeName());
        } catch (NoSuchElementException ex) {
            throw new DataSourceException(ex.getMessage(), ex);
        } catch (IllegalAttributeException ex) {
            throw new DataSourceException(ex.getMessage(), ex);
        }
    }
    
    /**
     * Start a new KML folder.
     * From the spec 2.0: A top-level, optional tag used to structure hierarchical
     * arrangement of other folders,  placemarks, ground overlays, and screen
     * overlays. Use this tag to structure and organize your information in the
     * Google Earth client.
     *
     * In this context we should be using a Folder per map layer.
     *
     * @param name A String to label this folder with, if null the name tag will be ommited
     * @param description  Supplies descriptive information.
     *         This description appears in the Places window when the user
     *         clicks on the folder or ground overlay, and in a pop-up window
     *         when the user clicks on either the Placemark name in the
     *         Places window, or the placemark icon.
     *         The description element supports plain text as well as HTML
     *         formatting. A valid URL string for the World Wide Web is
     *         automatically converted to a hyperlink to that URL
     *         (e.g. http://www.google.com).
     */
    
    private void startFolder(String name, String description) throws IOException {
        write("<Folder>");
        if(name != null){
            write("<Name>"+name+"</Name>");
        }
        if(description != null){
            write("<Description>"+description+"</Description>");
        }
    }
    
    private void endFolder() throws IOException {
        write("</Folder>");
    }
    
    /**
     * Gather any information needed to write the KML document.
     *
     * @TODO: support writing of 'Schema' tags based on featureType
     */
    private void setUpWriterHandler(FeatureType featureType)
    throws IOException {
        String typeName = featureType.getTypeName();
            /*
             * REVISIT: To use attributes properly we need to be using the 'schema' part of
             * KML to contain custom data..
             */
        List atts = new ArrayList(0);// config.getAttributes(typeName);
    }
    
    
    /**
     * Write out the geometry.
     * Contains workaround for the fact that KML2.0 does not support multipart geometries in the same way that
     * GML does.
     *
     * @param geom The Geometry to be encoded, multi part geometries will be written as a sequence.
     * @param trans A GeometryTransformer to produce the gml output, its output is post processed to remove gml namespace prefixes.
     */
    protected void writeGeometry(Geometry geom, GeometryTransformer trans) throws IOException, TransformerException{
        Class geomClass = geom.getClass();
        if (isMultiPart(geom)) {
            for(int i=0; i<geom.getNumGeometries(); i++){
                writeGeometry(geom.getGeometryN(i), trans);
            }
        } else{
            //remove gml prefixing as KML does not accept them
            StringWriter tempWriter = new StringWriter();
            trans.transform(geom, tempWriter);
            String tempBuffer = tempWriter.toString();
            //@REVISIT: should check which prefix is being used, this will only work for the default (99.9%) of cases.
            write(tempBuffer.replaceAll("gml:", ""));
        }
    }
    
    /**
     * Test to see if the geometry is a Multi geometry
     *
     * @return true if geom instance of MultiPolygon, MultiPoint or MultiLineString
     */
    protected boolean isMultiPart(Geometry geom){
        Class geomClass = geom.getClass();
        return (geomClass.equals(MultiPolygon.class) || geomClass.equals(MultiPoint.class) || geomClass.equals(MultiLineString.class));
    }
    
    /**
     * Applies each feature type styler in turn to all of the features.
     *
     * @param features A FeatureCollection contatining the features to be rendered
     * @param featureStylers An array of feature stylers to be applied
     * @throws IOException
     * @throws IllegalAttributeException
     * @TODO: multiple features types result in muliple data passes, could be split into separate tempory files then joined.
     */
    private void processStylers(final FeatureCollection features, final FeatureTypeStyle[] featureStylers )
    throws IOException,  IllegalAttributeException {
        
        for( int i = 0; i < featureStylers.length; i++ ) {
            FeatureTypeStyle fts = featureStylers[i];
            String typeName = features.getSchema().getTypeName();
            
            if ((typeName != null)
            && (features.getSchema().isDescendedFrom(null,
                    fts.getFeatureTypeName()) || typeName.equalsIgnoreCase(fts
                    .getFeatureTypeName()))) {
                
                // get applicable rules at the current scale
                Rule[] rules = fts.getRules();
                List ruleList = new ArrayList();
                List elseRuleList = new ArrayList();
                
                for( int j = 0; j < rules.length; j++ ) {
                    
                    Rule r = rules[j];
                    
                    if (isWithinScale(r)) {
                        if (r.hasElseFilter()) {
                            elseRuleList.add(r);
                        } else {
                            ruleList.add(r);
                        }
                    }
                }
                
                if ( (ruleList.size() == 0) && (elseRuleList.size()==0) ){
                    return;
                }
                //REVISIT: once scaleDemominator can actualy be determined re-evaluate sensible ranges for GE
                NumberRange scaleRange = new NumberRange(scaleDenominator, scaleDenominator);
                FeatureIterator reader = features.features();
                
                while( true ) {
                    try {
                        
                        /*if (renderingStopRequested) {
                            break;
                        }*/
                        
                        if (!reader.hasNext()) {
                            break;
                        }
                        
                        boolean doElse = true;
                        Feature feature = reader.next();
                        
                        
                        // applicable rules
                        for( Iterator it = ruleList.iterator(); it.hasNext(); ) {
                            Rule r = (Rule) it.next();
                            LOGGER.finer("applying rule: " + r.toString());
                            Filter filter = r.getFilter();
                            if ((filter == null) || filter.contains(feature)) {
                                doElse = false;
                                LOGGER.finer("processing Symobolizer ...");
                                Symbolizer[] symbolizers = r.getSymbolizers();
                                processSymbolizers(feature, symbolizers, scaleRange);
                            }
                        }
                        if (doElse) {
                            // rules with an else filter
                            LOGGER.finer("rules with an else filter");
                            
                            for( Iterator it = elseRuleList.iterator(); it.hasNext(); ) {
                                Rule r = (Rule) it.next();
                                Symbolizer[] symbolizers = r.getSymbolizers();
                                LOGGER.finer("processing Symobolizer ...");
                                processSymbolizers( feature, symbolizers, scaleRange);
                            }
                        }
                    } catch (Exception e) {
                        // that feature failed but others may still work
                        //REVISIT: don't like eating exceptions, even with a log.
                        LOGGER.warning("KML transform for feature failed "+e.getMessage());
                    }
                }
                //FeatureIterators may be backed by a stream so this tidies things up.
                features.close(reader);
            }
        }
    }
    
    /**
     * Applies each of a set of symbolizers in turn to a given feature.
     * <p>
     * This is an internal method and should only be called by processStylers.
     * </p>
     *
     * @param feature The feature to be rendered
     * @param symbolizers An array of symbolizers which actually perform the rendering.
     * @param scaleRange The scale range we are working on... provided in order to make the style
     *        factory happy
     */
    private void processSymbolizers( final Feature feature,
            final Symbolizer[] symbolizers, Range scaleRange ) throws IOException, TransformerException {
        
        String title=null;
        for( int m = 0; m < symbolizers.length; m++ ) {
            LOGGER.finer("applying symbolizer " + symbolizers[m]);
            
            if (symbolizers[m] instanceof RasterSymbolizer) {
                //for now we are out of luck.
            } else{
                Geometry g = findGeometry(feature, symbolizers[m]);
                //TODO: come back and sort out crs transformation
                //  CoordinateReferenceSystem crs = findGeometryCS(feature, symbolizers[m]);              
                if( symbolizers[m] instanceof TextSymbolizer ){
                    title = (String)((TextSymbolizer) symbolizers[m]).getLabel().getValue(feature);
                } else{
                    Style2D style = styleFactory.createStyle(feature, symbolizers[m], scaleRange);
                    write("<Placemark>");
                    if(title != null){
                        write("<Name>"+title+"</Name>");
                    }
                    writeStyle(style);
   
                    write("<GeometryCollection>");
                    writeGeometry(g,transformer);
                    write("</GeometryCollection>");
                    write("</Placemark>");
                    newline();
                }
            }
        }
    }
    
    private void writeStyle(Style2D style) throws IOException {
        if(style instanceof PolygonStyle2D){
            
            write("<Style>");
            write("<geomColor>");
            Paint p = ((PolygonStyle2D)style).getFill();
            if(p instanceof Color){
                write("aa"+colorToHex((Color)p));//transparancy needs to come from the opacity value.
            } else{
                write("ffaaaaaa");//should not occure in normal parsing
            }
            write("</geomColor>");
            write("</Style>");
        }
    }
    
    private boolean isWithinScale(Rule r){
        return true;
    }
    
    /**
     * Finds the geometric attribute requested by the symbolizer
     *
     * @param f The feature
     * @param s The symbolizer
     * @return The geometry requested in the symbolizer, or the default geometry if none is
     *         specified
     */
    private com.vividsolutions.jts.geom.Geometry findGeometry( Feature f, Symbolizer s ) {
        String geomName = getGeometryPropertyName(s);
        
        // get the geometry
        Geometry geom;
        
        if (geomName == null) {
            geom = f.getDefaultGeometry();
        } else {
            geom = (com.vividsolutions.jts.geom.Geometry) f.getAttribute(geomName);
        }
        
        // if the symbolizer is a point symbolizer generate a suitable location to place the
        // point in order to avoid recomputing that location at each rendering step
        if (s instanceof PointSymbolizer)
            geom = getCentroid(geom); // djb: major simpificatioN
        
        return geom;
    }
    
    /**
     *  Finds the centroid of the input geometry
     *    if input = point, line, polygon  --> return a point that represents the centroid of that geom
     *    if input = geometry collection --> return a multipoint that represents the centoid of each sub-geom
     * @param g
     * @return
     */
    public Geometry getCentroid(Geometry g) {
        if (g instanceof GeometryCollection) {
            GeometryCollection gc = (GeometryCollection) g;
            Coordinate[] pts = new Coordinate[gc.getNumGeometries()];
            for (int t=0;t<gc.getNumGeometries();t++) {
                pts[t] = gc.getGeometryN(t).getCentroid().getCoordinate();
            }
            return g.getFactory().createMultiPoint(pts);
        } else {
            return g.getCentroid();
        }
    }
    
    /**
     * Finds the geometric attribute coordinate reference system
     *
     * @param f The feature
     * @param s The symbolizer
     * @return The geometry requested in the symbolizer, or the default geometry if none is
     *         specified
     */
    private org.opengis.referencing.crs.CoordinateReferenceSystem findGeometryCS( Feature f,
            Symbolizer s ) {
        String geomName = getGeometryPropertyName(s);
        
        if (geomName != null) {
            return ((GeometryAttributeType) f.getFeatureType().getAttributeType(geomName))
            .getCoordinateSystem();
        } else {
            return ((GeometryAttributeType) f.getFeatureType().getDefaultGeometry())
            .getCoordinateSystem();
        }
    }
    
    /**
     * Utility method to find which geometry property is referenced by a given
     * symbolizer.
     *
     * @param s The symbolizer
     * @TODO: this is c&p from lite renderer code as the method was private
     *        consider moving to a public unility class.
     */
    private String getGeometryPropertyName( Symbolizer s ) {
        String geomName = null;
        
        // TODO: fix the styles, the getGeometryPropertyName should probably be moved into an
        // interface...
        if (s instanceof PolygonSymbolizer) {
            geomName = ((PolygonSymbolizer) s).getGeometryPropertyName();
        } else if (s instanceof PointSymbolizer) {
            geomName = ((PointSymbolizer) s).getGeometryPropertyName();
        } else if (s instanceof LineSymbolizer) {
            geomName = ((LineSymbolizer) s).getGeometryPropertyName();
        } else if (s instanceof TextSymbolizer) {
            geomName = ((TextSymbolizer) s).getGeometryPropertyName();
        }
        
        return geomName;
    }
    
    /**
     * Utility method to convert an int into kex, padded to two characters.
     * handy for generating colour strings.
     *
     * @param i Int to convert
     * @return String a two character hex representation of i
     */
    private String intToHex(int i){
        String prelim = Integer.toHexString(i);
        if (prelim.length() < 2){
            prelim = "0" + prelim;
        }
        return prelim;
    }
    
    /**
     * Utility method to convert a Color into a KML color ref
     * @param c The color to convert
     * @return A string in BBGGRR format - note alpha must be prefixed seperatly before use.
     */
    private String colorToHex(Color c){
        return intToHex(c.getBlue()) + intToHex(c.getGreen()) + intToHex(c.getRed());
    }
}

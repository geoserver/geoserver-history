/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import java.awt.Color;
import java.awt.Paint;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import javax.media.jai.util.Range;
import javax.xml.transform.TransformerException;

//import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureType;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.Expression;
import org.geotools.filter.Filter;
import org.geotools.gml.producer.GeometryTransformer;
import org.geotools.map.MapLayer;
import org.geotools.renderer.style.LineStyle2D;
import org.geotools.renderer.style.PolygonStyle2D;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.renderer.style.TextStyle2D;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.NumberRange;
import org.vfny.geoserver.wms.WMSMapContext;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;


/**
 * Writer for KML/KMZ (Keyhole Markup Language) files.
 * Normaly controled by an EncodeKML instance, this class handles the styling information
 * and ensures that the geometries produced match the pseudo GML expected by GE.
 *
 * @REVISIT: Once this is fully working, revisit as an extention to TransformerBase
 * @author James Macgill
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $
 * @author Brent Owens
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
    public void writeFeatures(final FeatureCollection features, final MapLayer layer,
    		final int order, final boolean kmz, final boolean vectorResult)
    throws IOException, AbortedException {
        Style style = layer.getStyle();
		
        try {
            FeatureType featureType = features.getSchema();
            
            setUpWriterHandler(featureType);
            FeatureTypeStyle[] fts = style.getFeatureTypeStyles();
            if (!kmz)
            	processStylers(features, fts, layer, order);
            else
            	processStylersKMZ(features, fts, layer, order, vectorResult);


           	LOGGER.fine(new StringBuffer("encoded ").append(featureType.getTypeName()).toString());
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
     *         if null the description tag will be ommited
     */
    
    public void startFolder(String name, String description) throws IOException {
        write("<Folder>");
        if(name != null){
            write("<name>"+name+"</name>");
        }
        if(description != null){
            write("<description>"+description+"</description>");
        }
    }

    public void startDocument(String name, String description) throws IOException {
        write("<Document>");
        if(name != null){
            write("<name>"+name+"</name>");
        }
        if(description != null){
            write("<description>"+description+"</description>");
        }
    }

    public void endFolder() throws IOException {
        write("</Folder>");
    }

    public void endDocument() throws IOException {
        write("</Document>");
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

    protected void writeLookAt(Geometry geom, GeometryTransformer trans) throws IOException, TransformerException{
    	final Coordinate[] coordinates = getCentroid(geom).getCoordinates();
    	write("<LookAt>");
        write("<longitude>"+coordinates[0].x+"</longitude>");
        write("<latitude>"+coordinates[0].y+"</latitude>");
        write("<range>700</range>");
        write("<tilt>10.0</tilt>");
        write("<heading>10.0</heading>");
        write("</LookAt>");
    }
    
    protected void writePlaceMarkPoint(Geometry geom, GeometryTransformer trans) throws IOException, TransformerException {
    	final Coordinate[] coordinates = getCentroid(geom).getCoordinates();
    	write("<Point><coordinates>"+coordinates[0].x+","+coordinates[0].y+","+coordinates[0].z+"</coordinates></Point>");
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
    private void processStylers(final FeatureCollection features, final FeatureTypeStyle[] featureStylers,
    		final MapLayer layer, final int order)
    throws IOException,  IllegalAttributeException {

    	final int ftsLength = featureStylers.length;
        for( int i = 0; i < ftsLength; i++ ) {
            FeatureTypeStyle fts = featureStylers[i];
            final String typeName = features.getSchema().getTypeName();
            
            if ((typeName != null)
            		&& (features.getSchema().isDescendedFrom(null,fts.getFeatureTypeName()) 
            		|| typeName.equalsIgnoreCase(fts.getFeatureTypeName()))) {
                
                // get applicable rules at the current scale
                Rule[] rules = fts.getRules();
                List ruleList = new ArrayList();
                List elseRuleList = new ArrayList();
                
                // gather all the rules that will be used
                // each feature will run itself through each of these rules
                final int rulesLength = rules.length;
                for( int j = 0; j < rulesLength; j++ ) {
                    
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
                    return; // bail out early if no rules made it (because of scale denominators)
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
                        boolean raster = false;
                        Feature feature = reader.next();
                        StringBuffer title = new StringBuffer("");	// this gets filled in if there is a textsymbolizer
                        
                        startDocument(feature.getID(), layer.getTitle());
                        
                        // start writing out the styles
                        write("<Style id=\"GeoServerStyle" + feature.getID() +"\">");
                        
                        // applicable rules
                        for( Iterator it = ruleList.iterator(); it.hasNext(); ) {
                            Rule r = (Rule) it.next();
                            LOGGER.finer(new StringBuffer("applying rule: ").append(r.toString()).toString());
                            Filter filter = r.getFilter();
                            // if there is no filter or the filter says to do the feature anyways, render it
                            if ((filter == null) || filter.contains(feature)) {
                                doElse = false;
                                LOGGER.finer("processing Symobolizer ...");
                                Symbolizer[] symbolizers = r.getSymbolizers();
                                raster = processSymbolizers(features, feature, symbolizers, scaleRange, layer, order, -1, title, true);
                            }
                        }
                        if (doElse) {
                            // rules with an else filter
                            LOGGER.finer("rules with an else filter");
                            for( Iterator it = elseRuleList.iterator(); it.hasNext(); ) {
                                Rule r = (Rule) it.next();
                                Symbolizer[] symbolizers = r.getSymbolizers();
                                LOGGER.finer("processing Symobolizer ...");
                                raster = processSymbolizers(features, feature, symbolizers, scaleRange, layer, order, -1, title, true);
                            }
                        }
                        
                        write("</Style>"); // close off styles
                        
                        if (!raster) {
                        	String fTitle = title.toString();
                        	if (fTitle.equals(""))
                        		fTitle = feature.getID();
                        	
                        	write("<Placemark>");
                        	write("<name><![CDATA["+title+"]]></name>");// CDATA needed for ampersands
                        	final FeatureType schema = features.getSchema();
                        	final StringBuffer description = new StringBuffer();
                        	// if there are supposed to be detailed descriptions, write them out
                        	makeDescription(feature, schema, description);
                        	write("<description><![CDATA["+description.toString()+"]]></description>");
                        	writeLookAt(findGeometry(feature),transformer);
                        	write("<styleUrl>#GeoServerStyle"+feature.getID()+"</styleUrl>");
                        	write("<MultiGeometry>");
                        	writePlaceMarkPoint(findGeometry(feature),transformer);
                        	writeGeometry(findGeometry(feature),transformer);
                        	write("</MultiGeometry>");
                        	write("</Placemark>");
                        	newline();
                        }
                        endDocument();
                    } catch (Exception e) {
                        // that feature failed but others may still work
                        //REVISIT: don't like eating exceptions, even with a log.
                    	LOGGER.warning(new StringBuffer("KML transform for feature failed ").append(e.getMessage()).toString());
                    }
                }
                //FeatureIterators may be backed by a stream so this tidies things up.
                features.close(reader);
            }
        }
    }
    
    /**
     * 
     * @param features
     * @param featureStylers
     * @param layer
     * @param order 
     * @param vectorResult is false when the result is forced to be a raster image (kmscore)
     * @throws IOException
     * @throws IllegalAttributeException
     */
    private void processStylersKMZ(final FeatureCollection features, final FeatureTypeStyle[] featureStylers,
    		final MapLayer layer, final int order, final boolean vectorResult)
    throws IOException,  IllegalAttributeException {
        
    	startFolder("layer_"+order, layer.getTitle());
    	int layerCounter = order; // wcs stuff
    	
    	final int ftStylesLength = featureStylers.length;
        for( int i = 0; i < ftStylesLength; i++ ) {// for each style
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

                final int rulesLength = rules.length;
                for( int j = 0; j < rulesLength; j++ ) {
                    
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
                
                while( true ) { // for every feature
                    try {
                        
                        /*if (renderingStopRequested) {
                            break;
                        }*/
                        
                        if (!reader.hasNext()) {
                            break;
                        }
                        
                        boolean doElse = true;
                        boolean raster = false;
                        Feature feature = reader.next();
                        StringBuffer title = new StringBuffer("");
                        
                        // applicable rules
                        for( Iterator it = ruleList.iterator(); it.hasNext(); ) {
                            Rule r = (Rule) it.next();
                            LOGGER.finer(new StringBuffer("applying rule: ").append(r.toString()).toString());
                            Filter filter = r.getFilter();
                            if ((filter == null) || filter.contains(feature)) {
                                doElse = false;
                                LOGGER.finer("processing Symobolizer ...");
                                Symbolizer[] symbolizers = r.getSymbolizers();
                                // title gets filled in if there is a textsymbolizer
                                raster = processSymbolizers(features, feature, symbolizers, scaleRange, layer, order, layerCounter, title, vectorResult);
                                layerCounter++;
                            }
                        }
                        if (doElse) {
                            // rules with an else filter
                        	LOGGER.finer("rules with an else filter");
                            for( Iterator it = elseRuleList.iterator(); it.hasNext(); ) {
                                Rule r = (Rule) it.next();
                                Symbolizer[] symbolizers = r.getSymbolizers();
                                LOGGER.finer("processing Symobolizer ...");
                                // title gets filled in if there is a textsymbolizer
                                raster = processSymbolizers(features, feature, symbolizers, scaleRange, layer, order, layerCounter, title, vectorResult);
                                layerCounter++;
                            }
                        }
                        
                        // temporarily removed (bao)
                        // Is this ideal? If we are producing KMZ raster images, it is
                        // because there are too many features for the KMScore value.
                        // If there are placemarks for every feature, this is slow! 
                        /*if (!raster) {
                        	write("<Placemark>");
                        	write("<name>"+feature.getID()+"</name>");
                        	final FeatureType schema = features.getSchema();
                        	
                        	final StringBuffer description = new StringBuffer();
                        	// if there are supposed to be detailed descriptions, write them out
                        	makeDescription(feature, schema, description);
                        	
                        	write("<description><![CDATA["+description.toString()+"]]></description>");
                        	writeLookAt(findGeometry(feature),transformer);
                        	write("<styleUrl>#GeoServerStyle"+feature.getID()+"</styleUrl>");
                        	writePlaceMarkPoint(findGeometry(feature),transformer);
                        	write("</Placemark>");
                        	newline();
                        }*/
                    } catch (Exception e) {
                        // that feature failed but others may still work
                        //REVISIT: don't like eating exceptions, even with a log.
                    	LOGGER.warning(new StringBuffer("KML transform for feature failed ").append(e.getMessage()).toString());
                    }
                }
                //FeatureIterators may be backed by a stream so this tidies things up.
                features.close(reader);
            }
        }
        endFolder();
    }

	private void makeDescription(Feature feature, final FeatureType schema, final StringBuffer description) {
		if (mapContext.getRequest().getKMattr())
		{
			description.append("<table border='1'>");
			description.append("<tr><th colspan=").append(schema.getAttributeCount()).
					append(" scope='col'>").append(schema.getTypeName()).append(" </th></tr>");
			description.append("<tr>");

			final int attrCount = schema.getAttributeCount();
			for (int j = 0; j < attrCount; j++) {
				description.append("<td>").
						append(schema.getAttributeType(j).getName()).append("</td>");
			}
			
			description.append("</tr>");
			AttributeType[] types = schema.getAttributeTypes();
			description.append("<tr>");

			final int typesLength = types.length;
			for (int j = 0; j < typesLength; j++) {
				if (Geometry.class.isAssignableFrom(types[j].getType())) {
					description.append("<td>");
					description.append("[GEOMETRY]");
					description.append("</td>");
				} else {
					description.append("<td>");
					description.append(feature.getAttribute(types[j].getName()));
					description.append("</td>");
				}
			}
			
			description.append("</tr>");
			description.append("</table>");
		}
	}
    
    /**
     * Applies each of a set of symbolizers in turn to a given feature.
     * <p>
     * This is an internal method and should only be called by processStylers.
     * </p>
     * 
     * The KML color tag:
     * The order of expression is alpha, blue, green, red (ABGR). 
     * The range of values for any one color is 0 to 255 (00 to ff). 
     * For opacity, 00 is fully transparent and ff is fully opaque.
     *
     * @param feature The feature to be rendered
     * @param symbolizers An array of symbolizers which actually perform the rendering.
     * @param scaleRange The scale range we are working on... provided in order to make the style
     *        factory happy
     */
    private boolean processSymbolizers( final FeatureCollection features, final Feature feature,
            final Symbolizer[] symbolizers, Range scaleRange,
			final MapLayer layer, final int order, final int layerCounter, StringBuffer title, boolean vectorResult) throws IOException, TransformerException {
        
    	boolean res = false;
        //String title=null;
        final int length = symbolizers.length;
        
        
        // for each Symbolizer (text, polygon, line etc...)
        for( int m = 0; m < length; m++ ) {
        	LOGGER.finer(new StringBuffer("applying symbolizer ").append(symbolizers[m]).toString());
            
            if (symbolizers[m] instanceof RasterSymbolizer) {
            	//LOGGER.info("Removed by bao for testing");
            	/*final GridCoverage gc = (GridCoverage) feature.getAttribute("grid");
				final HttpServletRequest request = this.mapContext.getRequest().getHttpServletRequest();
				final String baseURL = org.vfny.geoserver.util.Requests.getBaseUrl(request);
            	com.vividsolutions.jts.geom.Envelope envelope = this.mapContext.getRequest().getBbox();
            	*/
            	/**
            	 * EXAMPLE OUTPUT:
            	 	<GroundOverlay>
					  <name>Google Earth - New Image Overlay</name>
					  <Icon>
					    <href>http://localhost:8081/geoserver/wms?bbox=-130,24,-66,50&amp;styles=raster&amp;Format=image/tiff&amp;request=GetMap&amp;layers=nurc:Img_Sample&amp;width=550&amp;height=250&amp;srs=EPSG:4326&amp;</href>
					    <viewRefreshMode>never</viewRefreshMode>
					    <viewBoundScale>0.75</viewBoundScale>
					  </Icon>
					  <LatLonBox>
					    <north>50.0</north>
					    <south>24.0</south>
					    <east>-66.0</east>
					    <west>-130.0</west>
					  </LatLonBox>
					</GroundOverlay> 
            	 */
            	/*
                write(new StringBuffer("<GroundOverlay>").
                	append("<name>").append(((GridCoverage2D)gc).getName()).append("</name>").
                	append("<drawOrder>").append(order).append("</drawOrder>").
					append("<Icon>").toString());
				final double[] BBOX = new double[] {
						envelope.getMinX(),
						envelope.getMinY(),
						envelope.getMaxX(),
						envelope.getMaxY()
						};
				if (layerCounter<0) {
					final StringBuffer getMapRequest = new StringBuffer(baseURL).append("wms?bbox=").append(BBOX[0]).append(",").
					append(BBOX[1]).append(",").append(BBOX[2]).append(",").append(BBOX[3]).append("&amp;styles=").
					append(layer.getStyle().getName()).append("&amp;Format=image/png&amp;request=GetMap&amp;layers=").
					append(layer.getTitle()).append("&amp;width="+this.mapContext.getMapWidth()+"&amp;height="+this.mapContext.getMapHeight()+"&amp;srs=EPSG:4326&amp;");
					write("<href>"+getMapRequest.toString()+"</href>");
				} else {
					write("<href>layer_"+order+".png</href>");
				}
				write(new StringBuffer("<viewRefreshMode>never</viewRefreshMode>").
					append("<viewBoundScale>0.75</viewBoundScale>").
					append("</Icon>").
					append("<LatLonBox>").
					append("<north>").append(BBOX[3]).append("</north>").
					append("<south>").append(BBOX[1]).append("</south>").
					append("<east>").append(BBOX[2]).append("</east>").
					append("<west>").append(BBOX[0]).append("</west>").
					append("</LatLonBox>").
					append("</GroundOverlay>").toString());
                //Geometry g = findGeometry(feature, symbolizers[m]);
                //writeRasterStyle(getMapRequest.toString(), feature.getID());
				*/
				res = true;
            } else if(vectorResult){
                //TODO: come back and sort out crs transformation
                //CoordinateReferenceSystem crs = findGeometryCS(feature, symbolizers[m]);              
                if( symbolizers[m] instanceof TextSymbolizer ){
                	TextSymbolizer ts = (TextSymbolizer) symbolizers[m];
                	Expression ex = ts.getLabel();
                	String value = (String)ex.getValue(feature);
                	title.append(value);
                	Style2D style = styleFactory.createStyle(feature, symbolizers[m], scaleRange);
                	writeStyle(style, feature.getID(), symbolizers[m]);
                } else {
                    Style2D style = styleFactory.createStyle(feature, symbolizers[m], scaleRange);
                    writeStyle(style, feature.getID(), symbolizers[m]);
                }
            } else if(!vectorResult) {
            	com.vividsolutions.jts.geom.Envelope envelope = this.mapContext.getRequest().getBbox();
                write(new StringBuffer("<GroundOverlay>").
                    	append("<name>").append(feature.getID()).append("</name>").
                    	append("<drawOrder>").append(order).append("</drawOrder>").
    					append("<Icon>").toString());
				final double[] BBOX = new double[] {
						envelope.getMinX(),
						envelope.getMinY(),
						envelope.getMaxX(),
						envelope.getMaxY()
						};
				write(new StringBuffer("<href>layer_").append(order).append(".png</href>").
						append("<viewRefreshMode>never</viewRefreshMode>").
						append("<viewBoundScale>0.75</viewBoundScale>").
						append("</Icon>").
						append("<LatLonBox>").
						append("<north>").append(BBOX[3]).append("</north>").
						append("<south>").append(BBOX[1]).append("</south>").
						append("<east>").append(BBOX[2]).append("</east>").
						append("<west>").append(BBOX[0]).append("</west>").
						append("</LatLonBox>").
						append("</GroundOverlay>").toString());
            }
            else
            	LOGGER.info("KMZ processSymbolizerz unknown case. Please report error.");
        }
        	
        
        return res;
    }
    
    /**
     * Adds the <style> tag to the KML document.
     * @param style
     * @param id
     * @throws IOException
     */
    private void writeStyle(final Style2D style, final String id, Symbolizer sym) throws IOException {
        if(style instanceof PolygonStyle2D){
        	
        	if ( ((PolygonStyle2D)style).getFill() == null && 
        		((PolygonStyle2D)style).getStroke() == null)
        		LOGGER.info("Empty PolygonSymbolizer, using default fill and stroke.");
        	
        	final StringBuffer styleString = new StringBuffer();
        	
        	styleString.append("<IconStyle>");
        	if (!mapContext.getRequest().getKMattr()) // if they don't want attributes
        		styleString.append("<color>#00ffffff</color>");// fully transparent
        	styleString.append("<Icon><href>root://icons/palette-3.png</href><x>224</x><w>32</w><h>32</h></Icon>");
        	styleString.append("</IconStyle>");
        	styleString.append("<PolyStyle><color>");
        	float op = getOpacity(sym);
        	int opacity = (new Float(255*op)).intValue();
        	
            Paint p = ((PolygonStyle2D)style).getFill();
            if(p instanceof Color){
            	styleString.append("#").append(intToHex(opacity)).append(colorToHex((Color)p));//transparancy needs to come from the opacity value.
            } else{
            	styleString.append("#ffaaaaaa");//should not occure in normal parsing
            }
            styleString.append("</color></PolyStyle>");
            //styleString.append("</Style>");

            write(styleString.toString());
        } else if(style instanceof LineStyle2D){
        	
        	if ( ((LineStyle2D)style).getStroke() == null)
            		LOGGER.info("Empty LineSymbolizer, using default stroke.");
        	
        	final StringBuffer styleString = new StringBuffer();
        	//styleString.append("<Style id=\"GeoServerStyle").append(id).append("\">");
        	styleString.append("<IconStyle>");
        	if (!mapContext.getRequest().getKMattr()) // if they don't want attributes
        		styleString.append("<color>#00ffffff</color>");// fully transparent
        	styleString.append("</IconStyle>");
        	styleString.append("<LineStyle><color>");
        	float op = getOpacity(sym);
        	int opacity = (new Float(255*op)).intValue();
        	
            Paint p = ((LineStyle2D)style).getContour();
            if(p instanceof Color){
            	styleString.append("#").append(intToHex(opacity)).append(colorToHex((Color)p));//transparancy needs to come from the opacity value.
            } else{
            	styleString.append("#ffaaaaaa");//should not occure in normal parsing
            }
            styleString.append("</color><width>2</width></LineStyle>");
            //styleString.append("</Style>");

            write(styleString.toString());
        }
	    else if(style instanceof TextStyle2D){
	    	final StringBuffer styleString = new StringBuffer();
        	//styleString.append("<Style id=\"GeoServerStyle").append(id).append("\">");
        	styleString.append("<LabelStyle><color>");
        	float op = getOpacity(sym);
        	int opacity = (new Float(255*op)).intValue();
        	Paint p = ((TextStyle2D)style).getFill();
        	
        	if(p instanceof Color){
            	styleString.append("#").append(intToHex(opacity)).append(colorToHex((Color)p));//transparancy needs to come from the opacity value.
            } else{
            	styleString.append("#ffaaaaaa");//should not occure in normal parsing
            }
        	styleString.append("</color></LabelStyle>");
            //styleString.append("</Style>");
            
            write(styleString.toString());
	    }
    }
    
    /**
     * @param href
     * @param id
     * @throws IOException
     */
    private void writeRasterStyle(final String href, final String id) throws IOException {
    	final StringBuffer styleString = new StringBuffer();
    	styleString.append("<Style id=\"GeoServerStyle").append(id).append("\">");
    	styleString.append("<IconStyle><Icon><href>").
			append(href).append("</href><viewRefreshMode>never</viewRefreshMode>").
        	append("<viewBoundScale>0.75</viewBoundScale><w>").//default 0.75 for overlays
			append(this.mapContext.getMapWidth()).append("</w><h>").
			append(this.mapContext.getMapHeight()).
			append("</h></Icon></IconStyle>");
    	styleString.append("<PolyStyle><fill>0</fill><outline>0</outline></PolyStyle>");
    	styleString.append("</Style>");
    	
    	write(styleString.toString());
    }
    
    private boolean isWithinScale(Rule r){
    	double min = r.getMinScaleDenominator();
    	double max = r.getMaxScaleDenominator();
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
     * Returns the default geometry in the feature.
     * @param f feature to find the geometry in
     * @return
     */
    private com.vividsolutions.jts.geom.Geometry findGeometry( Feature f ) {
        // get the geometry
        return f.getDefaultGeometry();
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
    
    /**
     * Borrowed from StreamingRenderer
     * 
     * @param sym
     * @return
     */
    private float getOpacity(final Symbolizer sym) {
        float alpha = 1.0f;
        Expression exp = null;
        if (sym instanceof PolygonSymbolizer)
        	exp = ((PolygonSymbolizer)sym).getFill().getOpacity();
        else if (sym instanceof LineSymbolizer)
        	exp = ((LineSymbolizer)sym).getStroke().getOpacity();
        else if (sym instanceof PointSymbolizer)
        	exp = ((PointSymbolizer)sym).getGraphic().getOpacity();
        else if (sym instanceof TextSymbolizer)
        	exp = ((TextSymbolizer)sym).getFill().getOpacity();
        else
        	LOGGER.info("Symbolizer not matched; was of class: "+sym);
        if (exp == null)
        {
        	LOGGER.info("Could not determine proper symbolizer opacity.");
        	return alpha;
        }
        Object obj = exp.getValue(null);
        if(obj == null) return alpha;
        Number num = null;
        if(obj instanceof Number) num = (Number)obj;
        if(num == null) return alpha;
        return num.floatValue();
    }
    
}

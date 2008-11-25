/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.vfny.geoserver.wms.responses;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.renderer.lite.LabelCache;
import org.geotools.renderer.lite.LabelCacheItem;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.TextStyle2D;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.NumberRange;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import com.vividsolutions.jts.operation.linemerge.LineMerger;
import com.vividsolutions.jts.precision.EnhancedPrecisionOp;

/**
 * Default LabelCache Implementation
 *
 * DJB (major changes on May 11th, 2005): 1.The old version of the labeler, if
 * given a *set* of points, lines, or polygons justed labels the first item in
 * the set. The sets are formed when you want to only put a single "Main St" on
 * the map even if you have a bunch of small "Main St" segments.
 *
 * I changed this to be much much wiser.
 *
 * Basically, the new way looks at the set of geometries that its going to put a
 * label on and find the "best" one that represents it. That geometry is then
 * labeled (see below for details on where that label is placed).
 *
 * 2. I changed the actual drawing routines;
 *
 * 1. get the "representative geometry" 2. for points, label as before 3. for
 * lines, find the middle point on the line (old version just averaged start and
 * end points) and centre label on that point (rotated) 4. for polygon, put the
 * label in the middle
 *
 * 3.
 *
 * ie. for lines, try the label at the 1/3, 1/2, and 2/3 location. Metric is how
 * close the label bounding box is to the line.
 *
 * ie. for polygons, bisect the polygon (about the centroid) in to North, South,
 * East and West polygons. Use the location that has the label best inside the
 * polygon.
 *
 * After this is done, you can start doing constraint relaxation...
 *
 * 4. TODO: deal with labels going off the edge of the screen (much reduced
 * now). 5. TODO: add a "minimum quality" parameter (ie. if you're labeling a
 * tiny polygon with a tiny label, dont bother). Metrics are descibed in #3. 6.
 * TODO: add ability for SLD to tweak parameters (ie. "always label").
 *
 *
 * ------------------------------------------------------------------------------------------
 * I've added extra functionality; a) priority -- if you set the <Priority> in a
 * TextSymbolizer, then you can control the order of labelling ** see mailing
 * list for more details b) <VendorOption name="group">no</VendorOption> ---
 * turns off grouping for this symbolizer c) <VendorOption name="spaceAround">5</VendorOption> --
 * do not put labels within 5 pixels of this label.
 *
 * @author jeichar
 * @author dblasby
 * @source $URL$
 */
public final class GSLabelCache implements LabelCache {

    /**
	 * labels that aren't this good will not be shown
	 */
	public double MIN_GOODNESS_FIT = 0.7;

	public double DEFAULT_PRIORITY = 1000.0;

	/** Map<label, LabelCacheItem> the label cache */
	protected Map labelCache = new HashMap();

	/** non-grouped labels get thrown in here* */
	protected ArrayList labelCacheNonGrouped = new ArrayList();

	// what to do if there's no grouping option
	public boolean DEFAULT_GROUP = false; 

	// by default, don't add space around labels
	public int DEFAULT_SPACE_AROUND = 0;
	
	// default max displacement when labeling lines
	public int DEFAULT_MAX_DISPLACEMENT = 50;
	
	// default min distance between labels in the same group (-1 means no min distance)
    public int DEFAULT_MIN_GROUP_DISTANCE = -1;
	
	// default repetition distance for labels (<= 0 -> no repetition)
	public int DEFAULT_LABEL_REPEAT = 0;
	
	// if in case of grouping all resulting lines have to be labelled
	public boolean DEFAULT_LABEL_ALL_GROUP = false;
	
	// we allow labels that are longer than the line to appear by default
    public boolean DEFAULT_ALLOW_OVERRUNS = true;
	
	// if, in case of grouping, self overlaps have to be taken into account and removed (expensive!)
	public boolean DEFAULT_REMOVE_OVERLAPS = false;
	
	// if labels with a line placement should follow the line shape or be just tangent
    public boolean DEFAULT_FOLLOW_LINE = false;
    
    // when label follows line, the max angle change between two subsequent characters, keeping
    // it low avoids chars overlaps. When the angle is exceeded the label placement will fail.
    public double DEFAULT_MAX_ANGLE_DELTA = 22.5;
    
    // The angle delta at which we switch from curved rendering to straight rendering  
    static final double MIN_CURVED_DELTA = Math.PI / 60;
    
    // Auto wrapping long labels default  
    static final int DEFAULT_AUTO_WRAP = Integer.MAX_VALUE;
	
	/**
	 * When true, the text is rendered as its GlyphVector outline (as a geometry) instead of using
	 * drawGlypVector. Pro: labels and halos are perfectly centered, some people prefer the 
	 * extra antialiasing obtained. Cons: possibly slower, some people do not like the 
	 * extra antialiasing :) 
	 */
	protected boolean outlineRenderingEnabled = false;
	
	protected SLDStyleFactory styleFactory=new SLDStyleFactory();
	boolean stop=false;
	Set enabledLayers=new HashSet();
	Set activeLayers=new HashSet();
	
	LineLengthComparator lineLengthComparator = new LineLengthComparator ();
	
	GeometryFactory gf = new GeometryFactory();

	private boolean needsOrdering=false;
	
	public void stop() {
		stop = true;
		activeLayers.clear();
	}

	/**
	 * @see org.geotools.renderer.lite.LabelCache#start()
	 */
	public void start() {
		stop = false;
	}
	
	public void clear() {
		if( !activeLayers.isEmpty() ){
			throw new IllegalStateException( activeLayers+" are layers that started rendering but have not completed," +
					" stop() or endLayer() must be called before clear is called" );
		}
		needsOrdering=true;
        labelCache.clear();
        labelCacheNonGrouped.clear();
		enabledLayers.clear();
	}

	public void clear(String layerId){
		if( activeLayers.contains(layerId) ){
			throw new IllegalStateException( layerId+" is still rendering, end the layer before calling clear." );
		}
		needsOrdering=true;

		for (Iterator iter = labelCache.values().iterator(); iter.hasNext();) {
			LabelCacheItem item = (LabelCacheItem) iter.next();
			if( item.getLayerIds().contains(layerId) )
				iter.remove();
		}
		for (Iterator iter = labelCacheNonGrouped
				.iterator(); iter.hasNext();) {
			LabelCacheItem item = (LabelCacheItem) iter.next();
			if( item.getLayerIds().contains(layerId) )
				iter.remove();
		}
		
		enabledLayers.remove(layerId);
		
	}
	
	public void disableLayer(String layerId) {
		needsOrdering=true;
		enabledLayers.remove(layerId);
	}
	/**
	 * @see org.geotools.renderer.lite.LabelCache#startLayer()
	 */
	public void startLayer(String layerId) {
		enabledLayers.add(layerId);
		activeLayers.add(layerId);
	}

	/**
	 * get the priority from the symbolizer its an expression, so it will try to
	 * evaluate it: 1. if its missing --> DEFAULT_PRIORITY 2. if its a number,
	 * return that number 3. if its not a number, convert to string and try to
	 * parse the number; return the number 4. otherwise, return DEFAULT_PRIORITY
	 *
	 * @param symbolizer
	 * @param feature
	 */
	public double getPriority(TextSymbolizer symbolizer, SimpleFeature feature) {
		if (symbolizer.getPriority() == null)
			return DEFAULT_PRIORITY;

		// evaluate
        try {
            Double number = (Double) symbolizer.getPriority().evaluate( feature, Double.class );
            return number.doubleValue();
        } catch (Exception e) {
			return DEFAULT_PRIORITY;
		}
	}

	/**
	 * @see org.geotools.renderer.lite.LabelCache#put(org.geotools.renderer.style.TextStyle2D,
	 *      org.geotools.renderer.lite.LiteShape)
	 */
	public void put(String layerId, TextSymbolizer symbolizer, SimpleFeature feature, LiteShape2 shape, NumberRange scaleRange) 
	{
		needsOrdering=true;
		try{
			//get label and geometry				
		    String label = (String) symbolizer.getLabel().evaluate(feature, String.class);
		    
			if (label == null) return;
            
		    label = label.trim();
		    if (label.length() ==0){
		    	return; // dont label something with nothing!
            }
		    double priorityValue = getPriority(symbolizer,feature);
		    boolean group = getBooleanOption(symbolizer, "group", false);
		    if (!(group))
		    {
		    	GSLabelCacheItem item = buildLabelCacheItem(layerId, symbolizer, feature, shape,
                        scaleRange, label, priorityValue);
				labelCacheNonGrouped.add(item);
			} else { // / --------- grouping case ----------------

				// equals and hashcode of LabelCacheItem is the hashcode of
				// label and the
				// equals of the 2 labels so label can be used to find the
				// entry.

				// DJB: this is where the "grouping" of 'same label' features
				// occurs
				LabelCacheItem lci = (LabelCacheItem) labelCache.get(label);
				if (lci == null) // nothing in there yet!
				{
					lci = buildLabelCacheItem(layerId, symbolizer, feature, shape, scaleRange, label, priorityValue);
					labelCache.put(label, lci);
				} else {
					// add only in the non-default case or non-literal. Ie.
					// area()
					if ((symbolizer.getPriority() != null)
							&& (!(symbolizer.getPriority() instanceof Literal)))
						lci.setPriority(lci.getPriority() + priorityValue); // djb--
					// changed
					// because
					// you
					// do
					// not
					// always
					// want
					// to
					// add!

					lci.getGeoms().add(shape.getGeometry());
				}
			}
		} catch (Exception e) // DJB: protection if there's a problem with the
		// decimation (getGeometry() can be null)
		{
			// do nothing
		}
	}

    private GSLabelCacheItem buildLabelCacheItem(String layerId, TextSymbolizer symbolizer,
            SimpleFeature feature, LiteShape2 shape, NumberRange scaleRange, String label,
            double priorityValue) {
        TextStyle2D textStyle=(TextStyle2D) styleFactory.createStyle(feature, symbolizer, scaleRange);
        
        GSLabelCacheItem item = new GSLabelCacheItem(layerId, textStyle, shape,label);
        item.setPriority(priorityValue);
        item.setSpaceAround(getIntOption(symbolizer, "spaceAround", DEFAULT_SPACE_AROUND));
        item.setMaxDisplacement(getIntOption(symbolizer, "maxDisplacement", DEFAULT_MAX_DISPLACEMENT));
        item.setMinGroupDistance(getIntOption(symbolizer, "minGroupDistance", DEFAULT_MIN_GROUP_DISTANCE));
        item.setRepeat(getIntOption(symbolizer, "repeat", DEFAULT_LABEL_REPEAT));
        item.setLabelAllGroup(getBooleanOption(symbolizer, "labelAllGroup", DEFAULT_LABEL_ALL_GROUP));
        item.setRemoveGroupOverlaps(getBooleanOption(symbolizer, "removeOverlaps", DEFAULT_REMOVE_OVERLAPS));
        item.setAllowOverruns(getBooleanOption(symbolizer, "allowOverruns", DEFAULT_ALLOW_OVERRUNS));
        item.setFollowLineEnabled(getBooleanOption(symbolizer, "followLine", DEFAULT_FOLLOW_LINE));
        double maxAngleDelta = getDoubleOption(symbolizer, "maxAngleDelta", DEFAULT_MAX_ANGLE_DELTA);
        item.setMaxAngleDelta(Math.toRadians(maxAngleDelta));
        item.setAutoWrap(getIntOption(symbolizer, "autoWrap", DEFAULT_AUTO_WRAP));
        return item;
    }

	private boolean getLabelAllGroup(TextSymbolizer symbolizer) {
        return false;
    }

    private int getIntOption(TextSymbolizer symbolizer, String optionName, int defaultValue) {
        String value = symbolizer.getOption(optionName);
        if (value == null)
            return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    private double getDoubleOption(TextSymbolizer symbolizer, String optionName, double defaultValue) {
        String value = symbolizer.getOption(optionName);
        if (value == null)
            return defaultValue;
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    /**
     * look at the options in the symbolizer for "group". return its value if
     * not present, return "DEFAULT_GROUP"
     *
     * @param symbolizer
     */
    private boolean getBooleanOption(TextSymbolizer symbolizer, String optionName, boolean defaultValue) {
        String value = symbolizer.getOption(optionName);
        if (value == null)
            return defaultValue;
        return value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true")
                || value.equalsIgnoreCase("1");
    }

	/**
	 * @see org.geotools.renderer.lite.LabelCache#endLayer(java.awt.Graphics2D,
	 *      java.awt.Rectangle)
	 */
	public void endLayer(String layerId, Graphics2D graphics, Rectangle displayArea) 
	{
		activeLayers.remove(layerId);
	}

	/**
	 * return a list with all the values in priority order. Both grouped and
	 * non-grouped
	 *
	 *
	 */
	public List orderedLabels() {
		ArrayList al = getActiveLabels();
		
		Collections.sort(al);
		Collections.reverse(al);
		return al;		
	}
	private ArrayList getActiveLabels() {
		Collection c = labelCache.values();
		ArrayList al = new ArrayList(); // modifiable (ie. sortable)
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			LabelCacheItem item = (LabelCacheItem) iter.next();
			if( isActive(item.getLayerIds()) )
					al.add(item);
		}

		for (Iterator iter = labelCacheNonGrouped.iterator(); iter.hasNext();) {
			LabelCacheItem item = (LabelCacheItem) iter.next();
			if( isActive(item.getLayerIds()) )
					al.add(item);
		}
		return al;
	}
	private boolean isActive(Set layerIds) {
		for (Iterator iter = layerIds.iterator(); iter.hasNext();) {
			String string = (String) iter.next();
			if( enabledLayers.contains(string) )
				return true;
			
		}
		return false;
	}
	/**
	 * @see org.geotools.renderer.lite.LabelCache#end(java.awt.Graphics2D,
	 *      java.awt.Rectangle)
	 */
	public void end(Graphics2D graphics, Rectangle displayArea) 
	{
	    final Object antialiasing = graphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
	    final Object textAntialiasing = graphics.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        try {
            // if we are asked to antialias only text but we're drawing using the outline
            // method, we need to re-enable graphics antialiasing during label painting
            if (outlineRenderingEnabled
                    && antialiasing == RenderingHints.VALUE_ANTIALIAS_OFF
                    && textAntialiasing == RenderingHints.VALUE_TEXT_ANTIALIAS_ON) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
            }
            paintLabels(graphics, displayArea);
        } finally {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasing);
        }
    }

    void paintLabels(Graphics2D graphics, Rectangle displayArea) {
        if( !activeLayers.isEmpty() ){
			throw new IllegalStateException( activeLayers+" are layers that started rendering but have not completed," +
					" stop() or endLayer() must be called before end() is called" );
		}
		LabelIndex glyphs= new LabelIndex();
		
        // Hack: let's reduce the display area width and height by one pixel.
        // If the rendered image is 256x256, proper rendering of polygons and
        // lines occurr only if the display area is [0,0; 256,256], yet if you
        // try to render anything at [x,256] or [256,y] it won't show.
        // So, to avoid labels that happen to touch the border being cut
        // by one pixel, we reduce the display area. 
        // Feels hackish, don't have a better solution at the moment thought
        displayArea = new Rectangle(displayArea);
        displayArea.width -= 1;
        displayArea.height -= 1;

		List items; // both grouped and non-grouped
		if ( needsOrdering ){
			items = orderedLabels();
		} else {
			items = getActiveLabels();
		}
		GSLabelPainter painter = new GSLabelPainter(graphics, outlineRenderingEnabled);
		for (Iterator labelIter = items.iterator(); labelIter.hasNext();) {
			if (stop)
				return;
			
			GSLabelCacheItem labelItem = (GSLabelCacheItem) labelIter.next();
            painter.setLabel(labelItem);
			try {
				// LabelCacheItem labelItem = (LabelCacheItem)
				// labelCache.get(labelIter.next());
			    
				// DJB: simplified this. Just send off to the point,line,or
				// polygon routine
				// NOTE: labelItem.getGeometry() returns the FIRST geometry, so
				// we're assuming that lines & points arent mixed
				// If they are, then the FIRST geometry determines how its
				// rendered (which is probably bad since it should be in
				// area,line,point order
				// TOD: as in NOTE above

				

				/*
				* Just use identity for tempTransform because display area is 0,0,width,height
				* and oldTransform may have a different origin. OldTransform will be used later
				* for drawing.
				* -rg & je
				*/
 				AffineTransform tempTransform = new AffineTransform();

 				Geometry geom = labelItem.getGeometry();
 				boolean labelled = false;
				if ((geom instanceof Point) || (geom instanceof MultiPoint))
					labelled = paintPointLabel(painter, tempTransform, displayArea, glyphs);
				else if (((geom instanceof LineString) && !(geom instanceof LinearRing))
						|| (geom instanceof MultiLineString))
					labelled = paintLineLabels(painter, tempTransform, displayArea, glyphs);
				else if (geom instanceof Polygon
						|| geom instanceof MultiPolygon
						|| geom instanceof LinearRing)
					labelled = paintPolygonLabel(painter, tempTransform, displayArea, glyphs);
				
//				if(labelled)
//				    System.out.println("Painted " + labelItem.getLabel());
//				else
//				    System.out.println("Lost " + labelItem.getLabel());
			} catch (Exception e) {
			    System.out.println("Issues painting " + labelItem.getLabel());
                // the decimation can cause problems - we try to minimize it
			    // do nothing
			    e.printStackTrace();
			}
    	}
    }
    
    

    private Envelope toEnvelope(Rectangle2D bounds) {
        return new Envelope(bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(), bounds.getMaxY());
    }

	/**
	 * how well does the label "fit" with the geometry. 1. points ALWAYS RETURNS
	 * 1.0 2. lines ALWAYS RETURNS 1.0 (modify polygon method to handle rotated
	 * labels) 3. polygon + assume: polylabels are unrotated + assume: polygon
	 * could be invalid + dont worry about holes
	 *
	 * like to RETURN area of intersection between polygon and label bounds, but
	 * thats expensive and likely to give us problems due to invalid polygons
	 * SO, use a sample method - make a few points inside the label and see if
	 * they're "close to" the polygon The method sucks, but works well...
	 *
	 * @param glyphVector
	 * @param representativeGeom
	 */
	/**
	 * @param glyphBounds
	 * @param representativeGeom
	 * @return
	 */
	private double goodnessOfFit(GSLabelPainter painter, Rectangle2D glyphBounds, Geometry representativeGeom) {
		if (representativeGeom instanceof Point) {
			return 1.0;
		}
		if (representativeGeom instanceof LineString) {
			return 1.0;
		}
		if (representativeGeom instanceof Polygon) {
			try {
			    // do a sampling, how many points sitting on the labels are also within a certain distance of the polygon?
				Polygon p = simplifyPoly((Polygon) representativeGeom);
				int count = 0;
				int n = 10;
				double mindistance = painter.getLineHeight();
				Coordinate c = new Coordinate();
				Point pp = gf.createPoint(c);
				for(int i = 1; i < (painter.getLineCount() + 1); i++) {
				    double y = glyphBounds.getY() + ((double) glyphBounds.getHeight()) * (((double) i) / (painter.getLineCount() + 1));
    				for (int j = 1; j < (n + 1); j++) {
    				    c.x = glyphBounds.getX() + ((double) glyphBounds.getWidth()) * (((double) j) / (n + 1));
    				    c.y = y;
    				    pp.geometryChanged();
    					if (p.distance(pp) < mindistance)
    						count++;
    				}
				}
				return ((double) count) / (n * painter.getLineCount());
			} catch (Exception e) {
				representativeGeom.geometryChanged(); // djb -- jessie should
				// do this during
				// generalization
				Envelope ePoly = representativeGeom.getEnvelopeInternal();
				Envelope eglyph = toEnvelope(glyphBounds);
				Envelope inter = intersection(ePoly, eglyph);
				if (inter != null)
					return (inter.getWidth() * inter.getHeight())
							/ (eglyph.getWidth() * eglyph.getHeight());
				return 0.0;
			}
		}
		return 0.0;
	}

	/**
	 * Remove holes from a polygon
	 *
	 * @param polygon
	 */
	private Polygon simplifyPoly(Polygon polygon) {
	    if(polygon.getNumInteriorRing() == 0)
	        return polygon;
	    
		LineString outer = polygon.getExteriorRing();
		if (outer.getStartPoint().distance(outer.getEndPoint()) != 0) {
			List clist = new ArrayList(Arrays.asList(outer.getCoordinates()));
			clist.add(outer.getStartPoint().getCoordinate());
			outer = outer.getFactory().createLinearRing(
					(Coordinate[]) clist.toArray(new Coordinate[clist.size()]));
		}
		LinearRing r = (LinearRing) outer;

		return outer.getFactory().createPolygon(r, null);
	}

	private boolean paintLineLabels(GSLabelPainter painter, AffineTransform originalTransform,
			Rectangle displayArea, LabelIndex paintedBounds) throws Exception {
	    final GSLabelCacheItem labelItem = painter.getLabel();
        List<LineString> lines = (List<LineString>) getLineSetRepresentativeLocation(
                labelItem.getGeoms(), displayArea, labelItem.removeGroupOverlaps());

        if (lines == null || lines.size() == 0)
            return false;
        
        // if we just want to label the longest line, remove the others
        if(!labelItem.labelAllGroup() && lines.size() > 1) {
            lines = Collections.singletonList(lines.get(0));
        }
        
        // pre compute some labelling params
        final Rectangle2D textBounds  = painter.getFullLabelBounds();
        final double step = painter.getLineHeight();
        int space = labelItem.getSpaceAround();
        int haloRadius = Math.round(labelItem.getTextStyle().getHaloFill() != null ? 
                labelItem.getTextStyle().getHaloRadius() : 0);
        int extraSpace = space + haloRadius;
        // repetition distance, if any
        int labelDistance = labelItem.getRepeat();
        // min distance, if any
        int minDistance = labelItem.getMinGroupDistance();
        LabelIndex groupLabels = new LabelIndex();
        // Max displacement for the current label
        double labelOffset = labelItem.getMaxDisplacement();
        boolean allowOverruns = labelItem.allowOverruns();
        double maxAngleDelta = labelItem.getMaxAngleDelta();
        
        int labelCount = 0;
        for (LineString line : lines) {
            // if we are following lines, use a simplified version of the line, 
            // we don't want very small segments to influence the character orientation
            if(labelItem.isFollowLineEnabled())
                line = decimateLineString(line, step / 2);
            
    	    // max distance between candidate label points, if any
    	    final double lineStringLength = line.getLength();
    	    
    	    // if the line is too small compared to the label, don't label it
    	    // and exit right away, since the lines are sorted from longest to shortest
    	    if((!allowOverruns || labelItem.isFollowLineEnabled()) && line.getLength() < textBounds.getWidth())
    	        return labelCount > 0;
    	    
    	    // create the candidate positions for the labels over the line. If we can place just one
    	    // label or we're not supposed to replicate them, create the mid position, otherwise 
    	    // create mid and then create the sequence of before and after labels
    	    double[] labelPositions;
    	    if(labelDistance > 0 && labelDistance < lineStringLength / 2) {
    	        labelPositions = new double[(int) (lineStringLength / labelDistance)];
    	        labelPositions[0] = lineStringLength / 2;
    	        double offset = labelDistance;
    	        for (int i = 1; i < labelPositions.length; i++) {
                    labelPositions[i] = labelPositions[i - 1] + offset;
                    // this will generate a sequence like s, -2s, 3s, -4s, ...
                    // which will make the cursor alternate on mid + s, mid - s, mid + 2s, mid - 2s, mid + 3s, ...
                    double signum = Math.signum(offset);
                    offset = -1 * signum * (Math.abs(offset) + labelDistance);
                }
    	    } else {
    	        labelPositions = new double[1];
    	        labelPositions[0] = lineStringLength / 2;
    	    }
    	    
    	    // Ok, now we try to paint each of the labels in each position, and we take into
    	    // account that we might have to displace the labels
            LineStringCursor cursor = new LineStringCursor(line);
            AffineTransform tx = new AffineTransform();
            for (int i = 0; i < labelPositions.length; i++) {
                cursor.moveTo(labelPositions[i]);
                Coordinate centroid = cursor.getCurrentPosition();
        		double currOffset = 0;
        		
        		// label displacement loop
        		boolean painted = false;
        		while(Math.abs(currOffset) < labelOffset * 2 && !painted) {
        		    // reset transform and other computation parameters
        		    tx.setToIdentity();
        		    Rectangle2D labelEnvelope;
        		    double maxAngleChange = 0;
        		    
        		    // the line ordinates where we presume the label will start and end (using full bounds, 
        		    // thus taking into account shield and halo)
        		    double startOrdinate = cursor.getCurrentOrdinate() - textBounds.getWidth() / 2;
                    double endOrdinate = cursor.getCurrentOrdinate() + textBounds.getWidth() / 2;
                    
                    // compute label bounds
        		    if(labelItem.followLineEnabled) {
        		        // curved label, but we might end up drawing a straight one as an optimization
        		        maxAngleChange = cursor.getMaxAngleChange(startOrdinate, endOrdinate);
        		        if(maxAngleChange < MIN_CURVED_DELTA) {
        		            // if label will be painted as straight, use the straight bounds
        		            setupLineTransform(painter, cursor, centroid, tx);
        		            labelEnvelope = tx.createTransformedShape(textBounds).getBounds2D();
        		        } else {
        		            // otherwise use curved bounds, more expensive to compute
        		            labelEnvelope = getCurvedLabelBounds(cursor, startOrdinate, endOrdinate, textBounds.getHeight() / 2);
        		        }
        		    } else {
        		        setupLineTransform(painter, cursor, centroid, tx);
        		        labelEnvelope = tx.createTransformedShape(textBounds).getBounds2D();
        		    }
        		    
        		    // try to paint the label, the condition under which this happens are complex
        		    if(displayArea.contains(labelEnvelope) 
        		            && !paintedBounds.labelsWithinDistance(labelEnvelope, extraSpace)
        		            && !groupLabels.labelsWithinDistance(labelEnvelope, minDistance)) {
        		        if(labelItem.isFollowLineEnabled()) {
        		            // for curved labels we never paint in case of overrun
        		            if((startOrdinate > 0 && endOrdinate <= cursor.getLineStringLength())) {
                                if(maxAngleChange < maxAngleDelta) {
                                    // if the max angle is very small, draw it like a straight line
                                    if(maxAngleChange < MIN_CURVED_DELTA)
                                        painter.paintStraightLabel(tx);
                                    else {
                                        painter.paintCurvedLabel(cursor);
                                    }
            		                painted = true;
            		            }
        		            }
        		        } else {
        		            // for straight labels, check overrun only if required
        		            if((allowOverruns || (startOrdinate > 0 && endOrdinate <= cursor.getLineStringLength()))) {
        		                painter.paintStraightLabel(tx);
        		                painted = true;
        		            }
        		            
        		        }
        		    }
        		    
        		    // if we actually painted the label, add the envelope to the indexes and break out of the loop,
        		    // otherwise move to the next candidate position in the displacement sequence
        		    if(painted) {
                        labelCount++;
                        groupLabels.addLabel(labelItem, labelEnvelope);
                        paintedBounds.addLabel(labelItem, labelEnvelope);
                    } else { 
            	        // this will generate a sequence like s, -2s, 3s, -4s, ...
            	        // which will make the cursor alternate on mid + s, mid - s, mid + 2s, mid - 2s, mid + 3s, ... 
            	        double signum = Math.signum(currOffset);
            	        if(signum == 0) {
            	            currOffset = step;
            	        } else {
            	            currOffset = -1 * signum * (Math.abs(currOffset) + step);
            	        }
            		    cursor.moveRelative(currOffset);
            		    cursor.getCurrentPosition(centroid);
                    }
        		}
            }
        }
		
		return labelCount > 0;
	}

    private Rectangle2D getCurvedLabelBounds(LineStringCursor cursor, double startOrdinate,
            double endOrdinate, double bufferSize) {
        LineString cut = cursor.getSubLineString(startOrdinate, endOrdinate);
        Envelope e = cut.getEnvelopeInternal();
        e.expandBy(bufferSize);
        return new Rectangle2D.Double(e.getMinX(), e.getMinY(), e.getWidth(), e.getHeight());
    }

    private LineString decimateLineString(LineString line, double step) {
        Coordinate[] inputCoordinates = line.getCoordinates();
        List<Coordinate> simplified = new ArrayList<Coordinate>();
        Coordinate prev = inputCoordinates[0];
        simplified.add(prev);
        for (int i = 1; i < inputCoordinates.length; i++) {
            Coordinate curr = inputCoordinates[i];
            // see if this one should be added
            if ((Math.abs(curr.x - prev.x) > step) || (Math.abs(curr.y - prev.y)) > step) {
                simplified.add(curr);
                prev = curr;
            }
        }
        if(simplified.size() == 1)
            simplified.add(inputCoordinates[inputCoordinates.length - 1]);
        Coordinate[] newCoords = (Coordinate[]) simplified.toArray(new Coordinate[simplified.size()]);
        return line.getFactory().createLineString(newCoords);
    }

    private void setupLineTransform(GSLabelPainter painter, LineStringCursor cursor, 
            Coordinate centroid, AffineTransform tempTransform) {
        tempTransform.translate(centroid.x, centroid.y);
		double displacementX = 0;
		double displacementY = 0;

		TextStyle2D textStyle = painter.getLabel().getTextStyle();
		double anchorX = textStyle.getAnchorX();
		double anchorY = textStyle.getAnchorY();

		// undo the above if its point placement!
		double rotation;
		if (textStyle.isPointPlacement()) {
		    // use the one the user supplied!
			rotation = textStyle.getRotation(); 
		} else { // lineplacement
		    rotation = cursor.getLabelOrientation();
		    // move it off the line
			displacementY -= textStyle.getPerpendicularOffset(); 
			anchorX = 0.5; // centered
			anchorY = painter.getLinePlacementYAnchor();
		}

		Rectangle2D textBounds = painter.getLabelBounds();
		displacementX = (anchorX * (-textBounds.getWidth()))
				+ textStyle.getDisplacementX();
		displacementY += (anchorY * (textBounds.getHeight()))
				- textStyle.getDisplacementY();

		if (Double.isNaN(rotation) || Double.isInfinite(rotation))
			rotation = 0.0;
		tempTransform.rotate(rotation);
		tempTransform.translate(displacementX, displacementY);
    }

	/**
	 * Simple to paint a point (or set of points) Just choose the first one and
	 * paint it!
	 *
	 */
	private boolean paintPointLabel(GSLabelPainter painter, AffineTransform tempTransform,
			Rectangle displayArea, LabelIndex glyphs) throws Exception {
	    GSLabelCacheItem labelItem = painter.getLabel();
		// get the point onto the shape has to be painted
		Point point = getPointSetRepresentativeLocation(labelItem.getGeoms(),
				displayArea);
		if (point == null)
			return false;

		TextStyle2D textStyle = labelItem.getTextStyle();
		Rectangle2D textBounds = painter.getLabelBounds();
		tempTransform.translate(point.getX(), point.getY());
		double displacementX = 0;
		double displacementY = 0;

		// DJB: this probably isnt doing what you think its doing - see others
		displacementX = (textStyle.getAnchorX() * (-textBounds.getWidth()))
				+ textStyle.getDisplacementX();
		displacementY = (textStyle.getAnchorY() * (textBounds.getHeight()))
				- textStyle.getDisplacementY();

		if (!textStyle.isPointPlacement()) {
			// lineplacement. We're cheating here, since we cannot line label a
			// point
	        // just move it up (yes, its cheating)
			displacementY -= textStyle.getPerpendicularOffset(); 
		}

		double rotation = textStyle.getRotation();
		if (rotation != rotation) // IEEE def'n x=x for all x except when x is
			// NaN
			rotation = 0.0;
		if (Double.isInfinite(rotation))
			rotation = 0; // weird number

		tempTransform.rotate(rotation);
		tempTransform.translate(displacementX, displacementY);
		
		// check for overlaps and paint
		Rectangle2D transformed = tempTransform.createTransformedShape(painter.getFullLabelBounds()).getBounds2D();
		if(!displayArea.contains(transformed) 
		        || glyphs.labelsWithinDistance(transformed, labelItem.getSpaceAround())) {
		    return false;
		} else {
		    painter.paintStraightLabel(tempTransform);
		    glyphs.addLabel(labelItem, transformed);
		    return true;
		}
	}

	/**
	 * returns the representative geometry (for further processing)
	 *
	 * TODO: handle lineplacement for a polygon (perhaps we're supposed to grab
	 * the outside line and label it, but spec is unclear)
	 */
	private boolean paintPolygonLabel(GSLabelPainter painter, AffineTransform tempTransform,
			Rectangle displayArea, LabelIndex glyphs) throws Exception {
	    GSLabelCacheItem labelItem = painter.getLabel();
		Polygon geom = getPolySetRepresentativeLocation(labelItem.getGeoms(),
				displayArea);
		if (geom == null)
			return false;
		
		Point centroid;

		try {
			centroid = geom.getCentroid();
		} catch (Exception e) // generalized polygons causes problems - this
		// tries to hid them.
		{
			try {
				centroid = geom.getExteriorRing().getCentroid();
			} catch (Exception ee) {
				try {
					centroid = geom.getFactory().createPoint(
							geom.getCoordinate());
				} catch (Exception eee) {
					return false; // we're hooped
				}
			}
		}

		TextStyle2D textStyle = labelItem.getTextStyle();
		Rectangle2D textBounds = painter.getFullLabelBounds();
		System.out.println(textBounds);
		double displacementX = 0;
		double displacementY = 0;

		// DJB: this now does "centering"
		displacementX = (textStyle.getAnchorX() * (-textBounds.getWidth()))
				+ textStyle.getDisplacementX();
		displacementY = (textStyle.getAnchorY() * (-textBounds.getHeight()))
				- textStyle.getDisplacementY();
		tempTransform.translate(centroid.getX() + displacementX - textBounds.getMinX(), centroid.getY() + displacementY - textBounds.getMinY());

		if (!textStyle.isPointPlacement()) {
			// lineplacement. We're cheating here, since we've reduced the
			// polygon to a point, when we should be trying to do something
			// a little smarter (like find its median axis!)
		    // just move it up (yes, its cheating)
			displacementY -= textStyle.getPerpendicularOffset(); 
		}

		double rotation = textStyle.getRotation();
		if (rotation != rotation) // IEEE def'n x=x for all x except when x is
			// NaN
			rotation = 0.0;
		if (Double.isInfinite(rotation))
			rotation = 0; // weird number

		tempTransform.rotate(rotation);
		
		Rectangle2D transformed = tempTransform.createTransformedShape(painter.getFullLabelBounds()).getBounds2D();
		if (!displayArea.contains(transformed) 
		        || glyphs.labelsWithinDistance(transformed, labelItem.getSpaceAround()) 
		        || goodnessOfFit(painter, transformed, geom) < MIN_GOODNESS_FIT)
            return false;
		
//		painter.graphics.setStroke(new BasicStroke(2));
//        painter.graphics.setColor(Color.BLACK);
//        painter.graphics.draw(tempTransform.createTransformedShape(painter.getFullLabelBounds()));
//        painter.graphics.setColor(Color.WHITE);
//        painter.graphics.draw(new Line2D.Double(centroid.getX(), centroid.getY(), centroid.getX(), centroid.getY()));
		painter.paintStraightLabel(tempTransform);
		glyphs.addLabel(labelItem, transformed);
		return true;
	}

	/**
	 *
	 * 1. get a list of points from the input geometries that are inside the
	 * displayGeom NOTE: lines and polygons are reduced to their centroids (you
	 * shouldnt really calling this with lines and polys) 2. choose the most
	 * "central" of the points METRIC - choose anyone TODO: change metric to be
	 * "closest to the centoid of the possible points"
	 *
	 * @param geoms
	 *            list of Point or MultiPoint (any other geometry types are
	 *            rejected
	 * @param displayGeometry
	 * @return a point or null (if there's nothing to draw)
	 */
	Point getPointSetRepresentativeLocation(List geoms, Rectangle displayArea) {
		ArrayList pts = new ArrayList(); // points that are inside the
		// displayGeometry

		Iterator it = geoms.iterator();
		Geometry g;
		while (it.hasNext()) {
			g = (Geometry) it.next();
			if (!((g instanceof Point) || (g instanceof MultiPoint))) // handle
				// lines,polys, gc, etc..
				g = g.getCentroid(); // will be point
			if (g instanceof Point) {
			    Point point = (Point) g;
				if (displayArea.contains(point.getX(), point.getY())) // this is robust!
					pts.add(g); // possible label location
			} else if (g instanceof MultiPoint) {
				for (int t = 0; t < g.getNumGeometries(); t++) {
					Point gg = (Point) g.getGeometryN(t);
					if (displayArea.contains(gg.getX(), gg.getY()))
						pts.add(gg); // possible label location
				}
			}
		}
		if (pts.size() == 0)
			return null;

		// do better metric than this:
		return (Point) pts.get(0);
	}

	/**
     * 1. make a list of all the geoms (not clipped) NOTE: reject points,
     * convert polygons to their exterior ring (you shouldnt be calling this
     * function with points and polys) 2. join the lines together 3. clip
     * resulting lines to display geometry 4. return longest line
     * 
     * NOTE: the joining has multiple solution. For example, consider a Y (3
     * lines): * * 1 2 * * * 3 * solutions are: 1->2 and 3 1->3 and 2 2->3 and 1
     * 
     * (see mergeLines() below for detail of the algorithm; its basically a
     * greedy algorithm that should form the 'longest' possible route through
     * the linework)
     * 
     * NOTE: we clip after joining because there could be connections "going on"
     * outside the display bbox
     * 
     * 
     * @param geoms
	 * @param removeOverlaps 
     * @param displayGeometry
     *            must be poly
     */
    List<LineString> getLineSetRepresentativeLocation(List<Geometry> geoms, Rectangle displayArea, boolean removeOverlaps) {
        Geometry displayGeom = gf.toGeometry(toEnvelope(displayArea));
        Envelope displayGeomEnv = displayGeom.getEnvelopeInternal();

        // go through each geometry in the set.
        // if its a polygon or multipolygon, get the boundary (reduce to a line)
        // if its a line, add it to "lines"
        // if its a multiline, add each component line to "lines"
        List<LineString> lines = new ArrayList<LineString>();
        for (Geometry g : geoms) {
            accumulateLineStrings(g, lines);
        }
        if (lines.size() == 0)
            return null;
        
        // clip all the lines to the current bounds
        List<LineString> clippedLines = new ArrayList<LineString>();
        for (LineString ls : lines) {
            // more robust clipper -- see its dox
            MultiLineString ll = clipLineString(ls, (Polygon) displayGeom, displayGeomEnv);
            if ((ll != null) && (!(ll.isEmpty()))) {
                for (int t = 0; t < ll.getNumGeometries(); t++)
                    clippedLines.add((LineString) ll.getGeometryN(t));
            }
        }
        
        if(removeOverlaps) {
            List<LineString> cleanedLines = new ArrayList<LineString>();
            List<Geometry> bufferCache = new ArrayList<Geometry>();
            for(LineString ls : clippedLines) {
                Geometry g = ls;
                for (int i = 0; i < cleanedLines.size(); i++) {
                    LineString cleaned = cleanedLines.get(i);
                    if(g.getEnvelopeInternal().intersects(cleaned.getEnvelopeInternal())) {
                        Geometry buffer = bufferCache.get(i);
                        if(buffer == null) {
                            buffer =  cleaned.buffer(2);
                            bufferCache.set(i, buffer);
                        }
                        g = g.difference(buffer);
                    }
                }
                int added = accumulateLineStrings(g, cleanedLines);
                for (int i = 0; i < added; i++) {
                    bufferCache.add(null);
                }
            }
            clippedLines = cleanedLines;
        }
        
        if(clippedLines == null || clippedLines.size() == 0)
            return null;
        
        // at this point "lines" now is a list of linestring
        // join this algo doesnt always do what you want it to do, but its
        // pretty good
//        LineMerger merger = new LineMerger();
//        merger.add(clippedLines);
//        List merged = new ArrayList(merger.getMergedLineStrings());
        List merged = mergeLines(clippedLines);
//        List merged = cleanedLines;

        // clippedLines is a list of LineString, all cliped (hopefully) to the
        // display geometry. we choose longest one
        if (merged.size() == 0)
            return null;
//        double maxLen = -1;
//        LineString maxLine = null;
//        LineString cline;
//        for (int t = 0; t < merged.size(); t++) {
//            cline = (LineString) merged.get(t);
//            if (cline.getLength() > maxLen) {
//                maxLine = cline;
//                maxLen = cline.getLength();
//            }
//        }
//        return maxLine; // longest resulting line
        
        // sort and reverse to have the longest lines first
        Collections.sort(merged, new LineLengthComparator());
//        Collections.reverse(merged);
        return merged;
    }

	private List<LineString> snapLines(List<LineString> lines) {
        List<LineString> result = new ArrayList<LineString>();
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(0.01));
        for (LineString line : lines) {
            result.add(gf.createLineString(line.getCoordinateSequence()));
        }
        return result;
    }

    private void snapVertices(List<LineString> clippedLines) {
	    for (LineString ls : clippedLines) {
            CoordinateSequence cs = ls.getCoordinateSequence();
            for (int i = 0; i < cs.size(); i++) {
                long x = (long) Math.floor(cs.getOrdinate(i, 0));
                long y = (long) Math.floor(cs.getOrdinate(i, 1));
                cs.setOrdinate(i, 0, x);
                cs.setOrdinate(i, 1, y);
            }
        }
    }

    private int accumulateLineStrings(Geometry g, List<LineString> lines) {
	    if (!((g instanceof LineString) || (g instanceof MultiLineString)
                || (g instanceof Polygon) || (g instanceof MultiPolygon)))
            return 0;

	    // reduce polygons to their boundaries
        if ((g instanceof Polygon) || (g instanceof MultiPolygon)) {
            g = g.getBoundary(); // line or multiline m
            // TODO: boundary included the inside rings, might want to
            // replace this with getExteriorRing()
            if (!((g instanceof LineString) || (g instanceof MultiLineString)))
                return 0;
        }
            
        // deal with line and multi line string, and finally with geom collection
        if (g instanceof LineString) {
            if (g.getLength() != 0) {
                lines.add((LineString) g);
                return 1;
            } else {
                return 0;
            }
        } else if(g instanceof MultiLineString){// multiline
            for (int t = 0; t < g.getNumGeometries(); t++) {
                LineString gg = (LineString) g.getGeometryN(t);
                lines.add(gg);
            }
            return g.getNumGeometries();
        } else {
            int count = 0;
            for (int t = 0; t < g.getNumGeometries(); t++) {
                count += accumulateLineStrings(g.getGeometryN(t), lines);
            }
            return count;
        }
    }

    /**
	 * try to be more robust dont bother returning points
	 *
	 * This will try to solve robustness problems, but read code as to what it
	 * does. It might return the unclipped line if there's a problem!
	 *
	 * @param line
	 * @param bbox
	 *            MUST BE A BOUNDING BOX
	 */
	public MultiLineString clipLineString(LineString line, Polygon bbox,
			Envelope displayGeomEnv) {
		
		Geometry clip = line;
		line.geometryChanged();// djb -- jessie should do this during
		// generalization
		if (displayGeomEnv.contains(line.getEnvelopeInternal())) {
			// shortcut -- entirely inside the display rectangle -- no clipping
			// required!
			LineString[] lns = new LineString[1];
			lns[0] = (LineString) clip;
			return line.getFactory().createMultiLineString(lns);
		}
		try {
		        // the representative geometry does not need to be accurate, let's
                        // simplify it further before doing the overlay to reduce the overlay cost
//                        Decimator d = new Decimator(10, 10);
//                        d.decimate(line);
//                        line.geometryChanged();
//			clip = EnhancedPrecisionOp.intersection(line, bbox);
		    return clipLineToEnvelope(line, bbox, displayGeomEnv);
		} catch (Exception e) {
			// TODO: should try to expand the bounding box and re-do the
			// intersection, but line-bounding box
			// problems are quite rare.
		    return line.getFactory().createMultiLineString(new LineString[] {line});
		}
//		if (clip instanceof MultiLineString) {
//		    // if the original line is self touching the intersection will
//		    // give us back spaghetti, many consequent but separate linestring objects, with
//		    // lots of duplicatest that make it impossible to perform a merge (think the
//		    // case of a bus line going forwards and backwards, most of the time sitting on
//		    // the same path
//		    return simplifyOverlaps((MultiLineString) clip);
//		}
//		if (clip instanceof LineString) {
//			LineString[] lns = new LineString[1];
//			lns[0] = (LineString) clip;
//			return line.getFactory().createMultiLineString(lns);
//		}
//		// otherwise we've got a point or line&point or empty
//		if (clip instanceof Point)
//			return null;
//		if (clip instanceof MultiPoint)
//			return null;
//
//		// its a GC (Line intersection Poly cannot be a polygon/multipoly)
//		GeometryCollection gc = (GeometryCollection) clip;
//		ArrayList lns = new ArrayList();
//		Geometry g;
//		for (int t = 0; t < gc.getNumGeometries(); t++) {
//			g = gc.getGeometryN(t);
//			if (g instanceof LineString)
//				lns.add(g);
//			// dont think multilinestring is possible, but not sure
//		}
//
//		// convert to multilinestring
//		if (lns.size() == 0)
//			return null;
//
//		return line.getFactory().createMultiLineString(
//				(LineString[]) lns.toArray(new LineString[1]));

	}
	
	private MultiLineString simplifyOverlaps(MultiLineString mls) {
	    Quadtree index = new Quadtree();
	    for (int i = 0; i < mls.getNumGeometries(); i++) {
          final Geometry g = mls.getGeometryN(i);
          index.insert(g.getEnvelopeInternal(), g);
	    }
	    for (int i = 0; i < mls.getNumGeometries(); i++) {
	        final LineString ls = (LineString) mls.getGeometryN(i);
	        List others = index.query(ls.getEnvelopeInternal());
	        for (Iterator it = others.iterator(); it.hasNext();) {
                LineString other = (LineString) it.next();
//                System.out.println(ls);
//                System.out.println(other);
                if(other != ls && ls.getEnvelopeInternal().equals(other.getEnvelopeInternal())) {
                    index.remove(ls.getEnvelopeInternal(), ls);
                    
                }
            }
	    }
	    
	    List remaining = new ArrayList(index.queryAll());
	    Collection merged = mergeLines(remaining);
	    return gf.createMultiLineString((LineString[]) merged.toArray(new LineString[merged.size()]));
    }

    private MultiLineString clipLineToEnvelope(LineString line, Polygon bbox, Envelope displayGeomEnv) {
	    Coordinate[] coords = line.getCoordinates();
	    List clipped = new ArrayList();
	    List coordinates = new ArrayList();
	    boolean prevInside = displayGeomEnv.contains(coords[0]);
	    if(prevInside)
	        coordinates.add(prevInside);
	    for (int i = 1; i <  coords.length; i++) {
            boolean inside = displayGeomEnv.contains(coords[i]);
            if(inside == prevInside) {
                if(inside) {
                    // both segments were inside, not need for cutting
                    coordinates.add(coords[i]);
                } else {
                    // both were outside, this might still be caused by a line
                    // crossing the envelope but whose endpoints lie outside
                    LineString segment = gf.createLineString(new Coordinate[] {coords[i-1], coords[i]});
                    Geometry g = segment.intersection(bbox);
                    // in case of null intersection we'll get an empty geometry collection
                    if(g instanceof LineString)
                        clipped.add((LineString) g);
                }
            } else {
                LineString segment = gf.createLineString(new Coordinate[] {coords[i-1], coords[i]});
                LineString ls = (LineString) segment.intersection(bbox);
                if(prevInside) {
                    coordinates.add(ls.getCoordinateN(1));
                } else {
                    coordinates.add(ls.getCoordinateN(0));
                    coordinates.add(coords[i]);
                }
                if(prevInside) {
                    clipped.add(gf.createLineString((Coordinate[]) coordinates.toArray(new Coordinate[coordinates.size()])));
                    coordinates.clear();
                } 
            }
            prevInside = inside;
        }
	    if(coordinates.size() > 0)
	        clipped.add(gf.createLineString((Coordinate[]) coordinates.toArray(new Coordinate[coordinates.size()])));
	    return gf.createMultiLineString((LineString[]) clipped.toArray(new LineString[clipped.size()]));
	}

	/**
	 * 1. make a list of all the polygons clipped to the displayGeometry NOTE:
	 * reject any points or lines 2. choose the largest of the clipped
	 * geometries
	 *
	 * @param geoms
	 * @param displayGeometry
	 */
	Polygon getPolySetRepresentativeLocation(List geoms,
			Rectangle displayArea) {
		ArrayList polys = new ArrayList(); // points that are inside the
		Geometry displayGeometry = gf.toGeometry(toEnvelope(displayArea));
		
		Iterator it = geoms.iterator();
		Geometry g;
		// go through each geometry in the input set
		// if its not a polygon or multipolygon ignore it
		// if its a polygon, add it to "polys"
		// if its a multipolgon, add each component to "polys"
		while (it.hasNext()) {
			g = (Geometry) it.next();
			if (!((g instanceof Polygon) || (g instanceof MultiPolygon)))
				continue;

			if (g instanceof Polygon) {
				polys.add(g);
			} else // multipoly
			{
				for (int t = 0; t < g.getNumGeometries(); t++) {
					Polygon gg = (Polygon) g.getGeometryN(t);
					polys.add(gg);
				}
			}
		}
		if (polys.size() == 0)
			return null;

		// at this point "polys" is a list of polygons

		// clip
		ArrayList clippedPolys = new ArrayList();
		it = polys.iterator();
		Polygon p;
		MultiPolygon pp;
		Envelope displayGeomEnv = displayGeometry.getEnvelopeInternal();
		while (it.hasNext()) {
			p = (Polygon) it.next();
			pp = clipPolygon(p, (Polygon) displayGeometry, displayGeomEnv);
			if ((pp != null) && (!(pp.isEmpty()))) {
				for (int t = 0; t < pp.getNumGeometries(); t++)
					clippedPolys.add(pp.getGeometryN(t)); // more robust
				// version -- see
				// dox
			}
		}
		// clippedPolys is a list of Polygon, all cliped (hopefully) to the
		// display geometry. we choose largest one
		if (clippedPolys.size() == 0)
			return null;
		if (clippedPolys.size() == 1)
			return (Polygon) clippedPolys.get(0);
		double maxSize = -1;
		Polygon maxPoly = null;
		Polygon cpoly;
		for (int t = 0; t < clippedPolys.size(); t++) {
			cpoly = (Polygon) clippedPolys.get(t);
			if (cpoly.getArea() > maxSize) {
				maxPoly = cpoly;
				maxSize = cpoly.getArea();
			}
		}
		return maxPoly;
	}

	/**
	 * try to do a more robust way of clipping a polygon to a bounding box. This
	 * might return the orginal polygon if it cannot clip TODO: this is a bit
	 * simplistic, there's lots more to do.
	 *
	 * @param poly
	 * @param bbox
	 * @param displayGeomEnv
	 *
	 * @return a MutliPolygon
	 */
	public MultiPolygon clipPolygon(Polygon poly, Polygon bbox,
			Envelope displayGeomEnv) {

		Geometry clip = poly;
		poly.geometryChanged();// djb -- jessie should do this during
		// generalization
		if (displayGeomEnv.contains(poly.getEnvelopeInternal())) {
			// shortcut -- entirely inside the display rectangle -- no clipping
			// required!
			Polygon[] polys = new Polygon[1];
			polys[0] = (Polygon) clip;
			return poly.getFactory().createMultiPolygon(polys);
		}

		try {
		        // the representative geometry does not need to be accurate, let's
	                // simplify it further before doing the overlay to reduce the overlay cost
	                Decimator d = new Decimator(10, 10);
	                d.decimate(poly);
	                poly.geometryChanged();
			clip = EnhancedPrecisionOp.intersection(poly, bbox);
		} catch (Exception e) {
			// TODO: should try to expand the bounding box and re-do the
			// intersection.
			// TODO: also, try removing the interior rings of the polygon

			clip = poly;// just return the unclipped version
		}
		if (clip instanceof MultiPolygon)
			return (MultiPolygon) clip;
		if (clip instanceof Polygon) {
			Polygon[] polys = new Polygon[1];
			polys[0] = (Polygon) clip;
			return poly.getFactory().createMultiPolygon(polys);
		}
		// otherwise we've got a point or line&point or empty
		if (clip instanceof Point)
			return null;
		if (clip instanceof MultiPoint)
			return null;
		if (clip instanceof LineString)
			return null;
		if (clip instanceof MultiLineString)
			return null;

		// its a GC
		GeometryCollection gc = (GeometryCollection) clip;
		ArrayList plys = new ArrayList();
		Geometry g;
		for (int t = 0; t < gc.getNumGeometries(); t++) {
			g = gc.getGeometryN(t);
			if (g instanceof Polygon)
				plys.add(g);
			// dont think multiPolygon is possible, but not sure
		}

		// convert to multipoly
		if (plys.size() == 0)
			return null;

		return poly.getFactory().createMultiPolygon(
				(Polygon[]) plys.toArray(new Polygon[1]));
	}



	List mergeLines(Collection lines) {
		LineMerger lm = new LineMerger();
		lm.add(lines);
		List merged = new ArrayList(lm.getMergedLineStrings()); // merged lines

		// Collection merged = lines;

		if (merged.size() == 0) {
			return null; // shouldnt happen
		}
		if (merged.size() == 1) // simple case - no need to continue merging
		{
			return merged;
		}

		Hashtable nodes = new Hashtable(merged.size() * 2); // coordinate ->
		// list of lines
		Iterator it = merged.iterator();
		while (it.hasNext()) {
			LineString ls = (LineString) it.next();
			putInNodeHash(ls, nodes);
		}

		ArrayList result = new ArrayList();
		ArrayList merged_list = new ArrayList(merged);

		// SORT -- sorting is important because order does matter.
		Collections.sort(merged_list, lineLengthComparator); // sorted
		// long->short
		processNodes(merged_list, nodes, result);
		// this looks for differences between the two methods.
		// Collection a = mergeLines2(lines);
		// if (a.size() != result.size())
		// {
		// System.out.println("bad");
		// boolean bb= false;
		// if (bb)
		// {
		// Collection b = mergeLines(lines);
		// }
		// }
		return result;
	}

	/**
	 * pull a line from the list, and: 1. if nothing connects to it (its
	 * issolated), add it to "result" 2. otherwise, merge it at the start/end
	 * with the LONGEST line there. 3. remove the original line, and the lines
	 * it merged with from the hashtables 4. go again, with the merged line
	 *
	 * @param edges
	 * @param nodes
	 * @param result
	 *
	 */
	public void processNodes(List edges, Hashtable nodes, ArrayList result) {
		int index = 0; // index into edges
		while (index < edges.size()) // still more to do
		{
			// 1. get a line and remove it from the graph
			LineString ls = (LineString) edges.get(index);
			Coordinate key = ls.getCoordinateN(0);
			ArrayList nodeList = (ArrayList) nodes.get(key);
			if (nodeList == null) // this was removed in an earlier iteration
			{
				index++;
				continue;
			}
			if (!nodeList.contains(ls)) {
				index++;
				continue; // already processed
			}
			removeFromHash(nodes, ls); // we're removing this from the network

			Coordinate key2 = ls.getCoordinateN(ls.getNumPoints() - 1);
			ArrayList nodeList2 = (ArrayList) nodes.get(key2);

			// case 1 -- this line is independent
			if ((nodeList.size() == 0) && (nodeList2.size() == 0)) {
				result.add(ls);
				index++; // move to next line
				continue;
			}

			if (nodeList.size() > 0) // touches something at the start
			{
				LineString ls2 = getLongest(nodeList); // merge with this one
				ls = merge(ls, ls2);
				removeFromHash(nodes, ls2);
			}
			if (nodeList2.size() > 0) // touches something at the start
			{
				LineString ls2 = getLongest(nodeList2); // merge with this one
				ls = merge(ls, ls2);
				removeFromHash(nodes, ls2);
			}
			// need for further processing
			edges.set(index, ls); // redo this one.
			putInNodeHash(ls, nodes); // put in network
		}
	}

	public void removeFromHash(Hashtable nodes, LineString ls) {
		Coordinate key = ls.getCoordinateN(0);
		ArrayList nodeList = (ArrayList) nodes.get(key);
		if (nodeList != null) {
			nodeList.remove(ls);
		}
		key = ls.getCoordinateN(ls.getNumPoints() - 1);
		nodeList = (ArrayList) nodes.get(key);
		if (nodeList != null) {
			nodeList.remove(ls);
		}
	}

	public LineString getLongest(ArrayList al) {
		if (al.size() == 1)
			return (LineString) (al.get(0));
		double maxLength = -1;
		LineString result = null;
		final int size = al.size();
		LineString l;
		for (int t = 0; t < size; t++) {
			l = (LineString) al.get(t);
			if (l.getLength() > maxLength) {
				result = l;
				maxLength = l.getLength();
			}
		}
		return result;
	}

	public void putInNodeHash(LineString ls, Hashtable nodes) {
		Coordinate key = ls.getCoordinateN(0);
		ArrayList nodeList = (ArrayList) nodes.get(key);
		if (nodeList == null) {
			nodeList = new ArrayList();
			nodeList.add(ls);
			nodes.put(key, nodeList);
		} else
			nodeList.add(ls);
		key = ls.getCoordinateN(ls.getNumPoints() - 1);
		nodeList = (ArrayList) nodes.get(key);
		if (nodeList == null) {
			nodeList = new ArrayList();
			nodeList.add(ls);
			nodes.put(key, nodeList);
		} else
			nodeList.add(ls);
	}

	/**
	 * merges a set of lines together into a (usually) smaller set. This one's
	 * pretty dumb, we use the JTS method (which doesnt merge on degree 3 nodes)
	 * and try to construct less lines.
	 *
	 * There's multiple solutions, but we do this the easy way. Usually you will
	 * not be given more than 3 lines (especially after jts is finished with).
	 *
	 * Find a line, find a lines that it "connects" to and add it. Keep going.
	 *
	 * DONE: be smarter - use length so the algorithm becomes greedy.
	 *
	 * This isnt 100% correct, but usually it does the right thing.
	 *
	 * NOTE: this is O(N^2), but N tends to be <10
	 *
	 * @param lines
	 */
	Collection mergeLines2(Collection lines) {
		LineMerger lm = new LineMerger();
		lm.add(lines);
		Collection merged = lm.getMergedLineStrings(); // merged lines
		// Collection merged = lines;

		if (merged.size() == 0) {
			return null; // shouldnt happen
		}
		if (merged.size() == 1) // simple case - no need to continue merging
		{
			return merged;
		}

		// key to this algorithm is the sorting by line length!

		// basic method:
		// 1. grab the first line in the list of lines to be merged
		// 2. search through the rest of lines (longer ones = first checked) for
		// a line that can be merged
		// 3. if you find one, great, merge it and do 2 things - a) update the
		// search geometry with the merged geometry and b) delete the other
		// geometry
		// if not, keep looking
		// 4. go back to step #1, but use the next longest line
		// 5. keep going until you've completely gone through the list and no
		// merging's taken place

		ArrayList mylines = new ArrayList(merged);

		boolean keep_going = true;
		while (keep_going) {
			keep_going = false; // no news is bad news
			Collections.sort(mylines, lineLengthComparator); // sorted
			final int size = mylines.size(); // long->short
			LineString major, minor, merge;
			for (int t = 0; t < size; t++) // for each line
			{
				major = (LineString) mylines.get(t); // this is the
				// search
				// geometry
				// (step #1)
				if (major != null) {
					for (int i = t + 1; i < mylines.size(); i++) // search
					// forward
					// for a
					// joining
					// thing
					{
						minor = (LineString) mylines.get(i); // forward
						// scan
						if (minor != null) // protection because we remove an
						// already match line!
						{
							merge = merge(major, minor); // step 3
							// (null =
							// not
							// mergeable)
							if (merge != null) {
								// step 3a
								keep_going = true;
								mylines.set(i, null);
								mylines.set(t, merge);
								major = merge;
							}
						}
					}
				}
			}
			// remove any null items in the list (see step 3a)

			mylines = (ArrayList) removeNulls(mylines);

		}

		// return result
		return removeNulls(mylines);

	}

	/**
	 * given a list, return a new list thats the same as the first, but has no
	 * null values in it.
	 *
	 * @param l
	 */
	ArrayList removeNulls(List l) {
		ArrayList al = new ArrayList();
		Iterator it = l.iterator();
		Object o;
		while (it.hasNext()) {
			o = it.next();
			if (o != null) {
				al.add(o);
			}
		}
		return al;
	}

	/**
	 * reverse direction of points in a line
	 */
	LineString reverse(LineString l) {
		List clist = Arrays.asList(l.getCoordinates());
		Collections.reverse(clist);
		return l.getFactory().createLineString(
				(Coordinate[]) clist.toArray(new Coordinate[1]));
	}

	/**
	 * if possible, merge the two lines together (ie. their start/end points are
	 * equal) returns null if not possible
	 *
	 * @param major
	 * @param minor
	 */
	LineString merge(LineString major, LineString minor) {
		Coordinate major_s = major.getCoordinateN(0);
		Coordinate major_e = major.getCoordinateN(major.getNumPoints() - 1);
		Coordinate minor_s = minor.getCoordinateN(0);
		Coordinate minor_e = minor.getCoordinateN(minor.getNumPoints() - 1);

		if (major_s.equals2D(minor_s)) {
			// reverse minor -> major
			return mergeSimple(reverse(minor), major);

		} else if (major_s.equals2D(minor_e)) {
			// minor -> major
			return mergeSimple(minor, major);
		} else if (major_e.equals2D(minor_s)) {
			// major -> minor
			return mergeSimple(major, minor);
		} else if (major_e.equals2D(minor_e)) {
			// major -> reverse(minor)
			return mergeSimple(major, reverse(minor));
		}
		return null; // no merge
	}

	/**
	 * simple linestring merge - l1 points then l2 points
	 */
	private LineString mergeSimple(LineString l1, LineString l2) {
		ArrayList clist = new ArrayList(Arrays.asList(l1.getCoordinates()));
		clist.addAll(Arrays.asList(l2.getCoordinates()));

		return l1.getFactory().createLineString(
				(Coordinate[]) clist.toArray(new Coordinate[1]));
	}

	/**
	 * sorts a list of LineStrings by length (long=1st)
	 *
	 */
	private final class LineLengthComparator implements java.util.Comparator {
		public int compare(Object o1, Object o2) // note order - this sort
		// big->small
		{
			return Double.compare(((LineString) o2).getLength(),
					((LineString) o1).getLength());
		}
	}

	// djb: replaced because old one was from sun's Rectangle class
	private Envelope intersection(Envelope e1, Envelope e2) {
		Envelope r = e1.intersection(e2);
		if (r.getWidth() < 0)
			return null;
		if (r.getHeight() < 0)
			return null;
		return r;
	}

	public void enableLayer(String layerId) {
		needsOrdering=true;
		enabledLayers.add(layerId);
	}

    public boolean isOutlineRenderingEnabled() {
        return outlineRenderingEnabled;
    }

    /**
     * Sets the text rendering mode. 
     * When true, the text is rendered as its GlyphVector outline (as a geometry) instead of using
     * drawGlypVector. Pro: labels and halos are perfectly centered, some people prefer the 
     * extra antialiasing obtained. Cons: possibly slower, some people do not like the 
     * extra antialiasing :) 
     */
    public void setOutlineRenderingEnabled(boolean outlineRenderingEnabled) {
        this.outlineRenderingEnabled = outlineRenderingEnabled;
    }
    
}

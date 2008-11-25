package org.vfny.geoserver.wms.responses;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.Bidi;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.geometry.jts.LiteShape2;
import org.geotools.renderer.lite.StyledShapePainter;
import org.geotools.renderer.style.TextStyle2D;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class GSLabelPainter {

    GSLabelCacheItem labelItem;

    List<LineInfo> lines;

    Graphics2D graphics;

    boolean outlineRenderingEnabled;

    GeometryFactory gf = new GeometryFactory();
    
    Rectangle2D labelBounds;

    public GSLabelPainter(Graphics2D graphics, boolean outlineRenderingEnabled) {
        this.graphics = graphics;
        this.outlineRenderingEnabled = outlineRenderingEnabled;
    }

    public void setLabel(GSLabelCacheItem labelItem) {
        this.labelItem = labelItem;
        labelItem.getTextStyle().setLabel(labelItem.getLabel());
        
        // reset previous caches
        labelBounds = null;
        lines = null;
        
        // split the label into lines
        String text = labelItem.getLabel();
        if(!text.contains("\n") && (labelItem.getAutoWrap() == Integer.MAX_VALUE || labelItem.getAutoWrap() > 0)) {
            // no layout needed
            LineInfo line = new LineInfo(text, layoutSentence(text, labelItem));
            labelBounds = line.gv.getVisualBounds();
            lines = Collections.singletonList(line);
        } else {
            // first split along the newlines
            String[] splitted = text.split("\\n");
            
            // then perform an auto-wrap using the java2d facilities. This
            // is done using a LineBreakMeasurer, but first we need to create
            // some extra objects
            
            // setup the attributes
            Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
            map.put(TextAttribute.FONT, labelItem.getTextStyle().getFont());
            
            // accumulate the lines and compute the total bounds
            lines = new ArrayList<LineInfo>();
            for (int i = 0; i < splitted.length; i++) {
                String line = splitted[i];

                // build the line break iterator
                AttributedString attributed = new AttributedString(line, map);
                AttributedCharacterIterator iter = attributed.getIterator();
                LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(iter, BreakIterator.getWordInstance(),
                        graphics.getFontRenderContext());

                // setup iteration and start splitting at word boundaries
                int prevPosition = 0;
                double minY = 0;
                while (lineMeasurer.getPosition() < iter.getEndIndex()) {
                    // grab the next portion of text within the wrapping limits
                    TextLayout layout = lineMeasurer.nextLayout(labelItem.getAutoWrap());
                    int newPosition = lineMeasurer.getPosition();
                    
                    // extract the text, and trim it since leading and trailing and ... spaces can affect
                    // label alignment in an unpleasant way (improper left or right alignment, or bad centering)
                    String extracted = line.substring(prevPosition, newPosition).trim();
                    prevPosition = newPosition;
                    
                    LineInfo info = new LineInfo(extracted, layoutSentence(extracted, labelItem), layout);
                    lines.add(info);
                    
                    Rectangle2D currBounds = info.gv.getVisualBounds();
                    
                    // the position at which we start to draw, x and y
                    // for x we have to take into consideration alignment as well since that affects
                    // the horizontal size of the bounds, for y we don't care right now as we're computing
                    // only the total bounds for a text located in the origin 
                    double minX = (labelItem.getAutoWrap() - layout.getAdvance()) * labelItem.getTextStyle().getAnchorX();
                    info.minX = minX;
                                       
                    if(labelBounds == null) {
                        labelBounds = currBounds;
                        minY = currBounds.getMinY() + layout.getAscent() + layout.getDescent() + layout.getLeading();
                    } else {
                        Rectangle2D translated = new Rectangle2D.Double(minX, minY, 
                                currBounds.getWidth(), currBounds.getHeight());
                        minY += layout.getAscent() + layout.getDescent() + layout.getLeading();
                        labelBounds = labelBounds.createUnion(translated);
                    }
                        
                }
            }
        }
    }
    
    GlyphVector layoutSentence(String label, GSLabelCacheItem item) {
        final Font font = item.getTextStyle().getFont(); 
        final char[] chars = label.toCharArray();
        final int length = label.length();
        if(Bidi.requiresBidi(chars, 0, length) && 
                new Bidi(label, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).isRightToLeft())
            return font.layoutGlyphVector(graphics.getFontRenderContext(), chars, 
                    0, length, Font.LAYOUT_RIGHT_TO_LEFT);
        else
            return font.createGlyphVector(graphics.getFontRenderContext(), chars);
    }

    public GSLabelCacheItem getLabel() {
        return labelItem;
    }

    /**
     * Returns the line height for this label in pixels
     * 
     * @return
     */
    public int getLineHeight() {
        if(getLineCount() == 1)
            return (int) Math.round(getLabelBounds().getHeight());
        else
            return (int) Math.round(lines.get(0).gv.getVisualBounds().getHeight());
    }

    /**
     * Returns the width of the label, as painted in straight form (
     * 
     * @return
     */
    public int getStraightLabelWidth() {
        return (int) Math.round(getLabelBounds().getWidth());
    }
    
    /**
     * Number of lines for this label (more than 1 if the label has embedded newlines or if we're auto-wrapping it)
     * @return
     */
    public int getLineCount() {
        return lines.size();
    }

    /**
     * Get the straight label bounds, taking into account halo, shield and line
     * wrapping
     * 
     * @return
     */
    public Rectangle2D getFullLabelBounds() {
        // base bounds
        Rectangle2D bounds = getLabelBounds();

        // take into account halo
        int haloRadius = Math.round(labelItem.getTextStyle().getHaloFill() != null ? labelItem
                .getTextStyle().getHaloRadius() : 0);
        bounds.add(bounds.getMinX() - haloRadius, bounds.getMinY() - haloRadius);
        bounds.add(bounds.getMaxX() + haloRadius, bounds.getMaxY() + haloRadius);

        // if there is a shield, expand the bounds to account for it as well
        if (labelItem.getTextStyle().getGraphic() != null) {
            Rectangle2D area = labelItem.getTextStyle().getGraphicDimensions();
            // center the graphics on the labels back
            Rectangle2D shieldBounds = new Rectangle2D.Double(-area.getWidth() / 2
                    + bounds.getMinX() - bounds.getWidth() / 2, -area.getHeight() / 2
                    + bounds.getMinY() - bounds.getHeight() / 2, area.getWidth(), area.getHeight());
            bounds.createUnion(shieldBounds);
        }

        return bounds;
    }

    /**
     * Get the straight label bounds, without taking into account halo and
     * shield
     * 
     * @return
     */
    public Rectangle2D getLabelBounds() {
        return labelBounds;
    }

    public void paintStraightLabel(AffineTransform transform) throws Exception {
        Rectangle2D glyphBounds = getLabelBounds();
        glyphBounds = transform.createTransformedShape(glyphBounds).getBounds();

        AffineTransform oldTransform = graphics.getTransform();
        try {
            AffineTransform newTransform = new AffineTransform(oldTransform);
            newTransform.concatenate(transform);
            graphics.setTransform(newTransform);

            if (labelItem.getTextStyle().getGraphic() != null) {

                // draw the label shield first, underneath the halo
                LiteShape2 tempShape = new LiteShape2(gf.createPoint(new Coordinate(glyphBounds
                        .getWidth() / 2.0, -1.0 * glyphBounds.getHeight() / 2.0)), null, null,
                        false, false);

                // labels should always draw, so we'll just force this
                // one to draw by setting it's min/max scale to 0<10 and
                // then
                // drawing at scale 5.0 on the next line
                labelItem.getTextStyle().getGraphic().setMinMaxScale(0.0, 10.0);
                new StyledShapePainter(null).paint(graphics, tempShape, labelItem.getTextStyle()
                        .getGraphic(), 5.0);
                graphics.setTransform(transform);
            }

            if(lines.size() == 1) {
                drawGlyphVector(lines.get(0).gv);   
            } else {
                double yOffset = 0;
                AffineTransform lineTx = new AffineTransform(transform);
                for (LineInfo line : lines) {
                    lineTx.setTransform(transform);
                    lineTx.translate(line.minX, yOffset);
                    graphics.setTransform(lineTx);
                    yOffset +=  line.layout.getAscent() + line.layout.getDescent() + line.layout.getLeading();
                    drawGlyphVector(line.gv);   
                }
            }
        } finally {
            graphics.setTransform(oldTransform);
        }
    }

    private void drawGlyphVector(GlyphVector gv ) {
        java.awt.Shape outline = gv.getOutline();
        if (labelItem.getTextStyle().getHaloFill() != null) {
            configureHalo();
            graphics.draw(outline);
        }
        configureLabelStyle();
        if (outlineRenderingEnabled)
            graphics.fill(outline);
        else
            graphics.drawGlyphVector(gv, 0, 0);
    }

    private void configureHalo() {
        graphics.setPaint(labelItem.getTextStyle().getHaloFill());
        graphics.setComposite(labelItem.getTextStyle().getHaloComposite());
        int haloRadius = Math.round(labelItem.getTextStyle().getHaloFill() != null ? labelItem
                .getTextStyle().getHaloRadius() : 0);
        graphics.setStroke(new BasicStroke(2f * haloRadius, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
    }

    private void configureLabelStyle() {
        // DJB: added this because several people were using
        // "font-color" instead of fill
        // It legal to have a label w/o fill (which means dont
        // render it)
        // This causes people no end of trouble.
        // If they dont want to colour it, then they should use a
        // filter
        // DEFAULT (no <Fill>) --> BLACK
        // NOTE: re-reading the spec says this is the correct
        // assumption.
        Paint fill = labelItem.getTextStyle().getFill();
        Composite comp = labelItem.getTextStyle().getComposite();
        if (fill == null) {
            fill = Color.BLACK;
            comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f); // 100%
            // opaque
        }
        graphics.setPaint(fill);
        graphics.setComposite(comp);
    }

    /**
     * Paints a label that follows the line, centered in the current cursor
     * position
     * 
     * @param cursor
     */
    public void paintCurvedLabel(LineStringCursor cursor) {
        GlyphVector glyphVector = lines.get(0).gv;
        AffineTransform oldTransform = graphics.getTransform();
        try {
            // first off, check if we are walking the line so that the label is
            // looking up
            if (!isLabelUpwards(cursor)) {
                LineStringCursor reverse = cursor.reverse();
                reverse.moveTo(cursor.getLineStringLength() - cursor.getCurrentOrdinate());
                cursor = reverse;
            }

            // find out the true centering position
            double anchorY = getLinePlacementYAnchor();

            // init, move to the starting position
            double mid = cursor.getCurrentOrdinate();
            Coordinate c = new Coordinate();
            c = cursor.getCurrentPosition(c);
            graphics.setPaint(Color.BLACK);

            double startOrdinate = mid - getStraightLabelWidth() / 2;
            if (startOrdinate < 0)
                startOrdinate = 0;
            cursor.moveTo(startOrdinate);
            final int numGlyphs = glyphVector.getNumGlyphs();
            float nextAdvance = glyphVector.getGlyphMetrics(0).getAdvance() * 0.5f;
            Shape[] outlines = new Shape[numGlyphs];
            AffineTransform[] transforms = new AffineTransform[numGlyphs];
            for (int i = 0; i < numGlyphs; i++) {
                outlines[i] = glyphVector.getGlyphOutline(i);
                Point2D p = glyphVector.getGlyphPosition(i);
                float advance = nextAdvance;
                nextAdvance = i < numGlyphs - 1 ? glyphVector.getGlyphMetrics(i + 1).getAdvance() * 0.5f
                        : 0;

                c = cursor.getCurrentPosition(c);
                AffineTransform t = new AffineTransform();
                t.setToTranslation(c.x, c.y);
                t.rotate(cursor.getCurrentAngle());
                t.translate(-p.getX() - advance, -p.getY() + getLineHeight() * anchorY);
                transforms[i] = t;

                cursor.moveTo(cursor.getCurrentOrdinate() + advance + nextAdvance);
                // System.out.println(cursor.getCurrentOrdinate());
            }

            // draw halo and label
            if (labelItem.getTextStyle().getHaloFill() != null) {
                configureHalo();
                for (int i = 0; i < numGlyphs; i++) {
                    graphics.setTransform(transforms[i]);
                    graphics.draw(outlines[i]);
                }
            }
            configureLabelStyle();
            for (int i = 0; i < numGlyphs; i++) {
                graphics.setTransform(transforms[i]);
                graphics.fill(outlines[i]);
            }
        } finally {
            graphics.setTransform(oldTransform);
        }
    }

    /**
     * Vertical centering is not trivial, because visually we want centering on
     * characters such as a,m,e, and not centering on d,g whose center is
     * affected by the full ascent or the full descent. This method tries to
     * computes the y anchor taking into account those.
     */
    public double getLinePlacementYAnchor() {
        TextStyle2D textStyle = getLabel().getTextStyle();
        LineMetrics lm = textStyle.getFont().getLineMetrics(textStyle.getLabel(), graphics.getFontRenderContext());
        return (Math.abs(lm.getStrikethroughOffset()) + lm.getDescent() + lm.getLeading() / 2)
                / lm.getHeight();
    }

    /**
     * Returns true if a label placed in the current cursor position would look
     * upwards or not, defining upwards a label whose bottom to top direction is
     * greater than zero, and less or equal to 180 degrees.
     * 
     * @param cursor
     * @return
     */
    boolean isLabelUpwards(LineStringCursor cursor) {
        // label angle is orthogonal to the line direction
        double labelAngle = cursor.getCurrentAngle() + Math.PI / 2;
        // normalize the angle so that it's comprised between 0 and 360°
        labelAngle = labelAngle % (Math.PI * 2);
        return labelAngle >= 0 && labelAngle < Math.PI;
    }
    
    /**
     * Core information needed to draw out a line of text
     */
    private static class LineInfo {
        public double minX;
        String text;
        GlyphVector gv;
        TextLayout layout;
        
        public LineInfo(String text, GlyphVector gv, TextLayout layout) {
            super();
            this.text = text;
            this.gv = gv;
            this.layout = layout;
        }

        public LineInfo(String text, GlyphVector gv) {
            super();
            this.text = text;
            this.gv = gv;
        }
        
        
        
    }
}

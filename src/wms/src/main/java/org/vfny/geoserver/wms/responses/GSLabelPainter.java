package org.vfny.geoserver.wms.responses;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.geotools.geometry.jts.LiteShape2;
import org.geotools.renderer.lite.StyledShapePainter;
import org.geotools.renderer.style.TextStyle2D;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class GSLabelPainter {

    GSLabelCacheItem labelItem;

    GlyphVector glyphVector;

    Graphics2D graphics;

    boolean outlineRenderingEnabled;

    GeometryFactory gf = new GeometryFactory();

    FontRenderContext fontContext = new FontRenderContext(new AffineTransform(), true, false);

    public GSLabelPainter(Graphics2D graphics, boolean outlineRenderingEnabled) {
        this.graphics = graphics;
        this.outlineRenderingEnabled = outlineRenderingEnabled;
    }

    public void setLabel(GSLabelCacheItem label) {
        this.labelItem = label;
        label.getTextStyle().setLabel(label.getLabel());
        glyphVector = label.getTextStyle().getTextGlyphVector(graphics);
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
        return (int) Math.round(glyphVector.getVisualBounds().getHeight());
    }

    /**
     * Returns the width of the label, as painted in straight form (
     * 
     * @return
     */
    public int getStraightLabelWidth() {
        return (int) Math.round(glyphVector.getVisualBounds().getWidth());
    }

    /**
     * Get the straight label bounds, taking into account halo, shield and line
     * wrapping
     * 
     * @return
     */
    public Rectangle2D getFullLabelBounds() {
        // base bounds
        Rectangle2D bounds = glyphVector.getVisualBounds();

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
        return glyphVector.getVisualBounds();
    }

    public void paintStraightLabel(AffineTransform transform) throws Exception {
        Rectangle2D glyphBounds = glyphVector.getVisualBounds();
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

            java.awt.Shape outline = glyphVector.getOutline();
            if (labelItem.getTextStyle().getHaloFill() != null) {
                configureHalo();
                graphics.draw(outline);
            }
            configureLabelStyle();
            if (outlineRenderingEnabled)
                graphics.fill(outline);
            else
                graphics.drawGlyphVector(glyphVector, 0, 0);
        } finally {
            graphics.setTransform(oldTransform);
        }
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
        LineMetrics lm = textStyle.getFont().getLineMetrics(textStyle.getLabel(), fontContext);
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
}

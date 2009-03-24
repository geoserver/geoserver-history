/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.decorations;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.responses.Decoration;
import org.vfny.geoserver.wms.WMSMapContext;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.imageio.ImageIO;

public class LegendDecoration implements Decoration {
    /** A logger for this class. */
    private static final Logger LOGGER = 
        org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.wms.responses");

    private static final int TITLE_INDENT = 4;

    public void loadOptions(Map<String, String> options){
    }

    public Dimension findOptimalSize(Graphics2D g2d, WMSMapContext mapContext){
        int x = 0, y = 0;
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont().deriveFont(Font.BOLD));
        try {
            for (MapLayerInfo layer : mapContext.getRequest().getLayers()){
                try {
                    BufferedImage legend = getLegend(layer);
                    x = Math.max(x, (int)legend.getWidth());
                    x = Math.max(x, TITLE_INDENT + metrics.stringWidth(layer.getLabel()));
                    y += legend.getHeight() + metrics.getHeight(); 
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error getting legend for " + layer);
                    continue;
                }
            }
            x += metrics.getDescent();
            return new Dimension(x, y);
        } catch (Exception e) {
            return new Dimension(50,50);
        }
    }

    public void paint(Graphics2D g2d, Rectangle paintArea, WMSMapContext mapContext) 
    throws Exception {
        Dimension d = findOptimalSize(g2d, mapContext);
        Rectangle bgRect = new Rectangle(0, 0, d.width, d.height);

        Color oldColor = g2d.getColor();
        AffineTransform oldTransform = (AffineTransform)g2d.getTransform().clone();
        Font oldFont = g2d.getFont();
        g2d.setFont(oldFont.deriveFont(Font.BOLD));
        g2d.translate(paintArea.getX(), paintArea.getY());
        final WMS wms = mapContext.getRequest().getWMS();

        AffineTransform tx = new AffineTransform(); 

        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());

        double scaleFactor = (paintArea.getWidth() / d.getWidth());
        scaleFactor = Math.min(scaleFactor, paintArea.getHeight() / d.getHeight());

        if (scaleFactor < 1.0) {
            g2d.scale(scaleFactor, scaleFactor);
        }
        AffineTransform bgTransform = g2d.getTransform();
        g2d.setColor(Color.WHITE);
        g2d.fill(bgRect);
        g2d.setColor(Color.BLACK);

        for (MapLayerInfo layer : mapContext.getRequest().getLayers()){
            BufferedImage legend = getLegend(layer);
            g2d.translate(0, metrics.getHeight());
            g2d.drawString(layer.getLabel(), TITLE_INDENT, 0 - metrics.getDescent());
            g2d.drawImage(legend, tx, null);
            g2d.translate(0, legend.getHeight());
        }

        g2d.setTransform(bgTransform);
        g2d.draw(new Rectangle(bgRect.x, bgRect.y, bgRect.width -1, bgRect.height - 1));

        g2d.setTransform(oldTransform);
        g2d.setFont(oldFont);
        g2d.setColor(oldColor);
    }

    protected BufferedImage getLegend(MapLayerInfo layer) 
    throws MalformedURLException, IOException {
        // TODO: Do this via direct GetLegendGraphic instantiation
        URL imageURL = new URL("http://localhost:8080/geoserver/wms?request=getLegendGraphic&format=image/png&layer=" + layer.getName());

        return ImageIO.read(imageURL);
    }
}

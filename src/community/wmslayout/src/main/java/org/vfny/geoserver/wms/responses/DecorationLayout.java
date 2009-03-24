/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.responses.decorations.LegendDecoration;
import org.vfny.geoserver.wms.responses.decorations.ScaleRatioDecoration;
import org.vfny.geoserver.wms.responses.decorations.WatermarkDecoration;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * The DecorationLayout class describes a set of overlays to be used to enhance a WMS response.
 * It maintains a collection of Decoration objects and the configuration associated with each, and
 * delegates the actual rendering operations to the decorations.
 *
 * @author David Winslow <dwinslow@opengeo.org> 
 */
public class DecorationLayout {
    private static Logger LOGGER = 
        org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.wms.responses");

    private static class Block {
        private static enum Mode {OVERLAY, APPEND};

        public static enum Position {
            UL, UC, UR, CL, CC, CR, LL, LC, LR;

            public static Rectangle findBounds(
                Position p,
                Rectangle container,
                Dimension dim,
                Point o,
                Mode m) {

                int x = 0, y = 0;
                int height = dim.height, width = dim.width;
                

                if (m == Mode.APPEND) {
                    switch (p) {
                        case UC:
                            return new Rectangle(
                                (int)container.getMinX(), (int)(container.getMinY() - dim.height),
                                (int)container.getWidth(), dim.height
                            );
                        case LC:
                            return new Rectangle(
                                (int)container.getMinX(), (int) container.getMaxY(),
                                (int)container.getWidth(), dim.height
                            );
                        case CR:
                            return new Rectangle(
                                (int)container.getMaxX(), (int) container.getMinY(),
                                dim.width, (int) container.getHeight()
                            );
                        case CL: 
                            return new Rectangle(
                                (int) (container.getMinX() - dim.width), (int) container.getMinY(),
                                dim.width, (int) container.getHeight()
                            );
                        default:
                            System.out.println("Returning null for Position: " + p);
                            return null;
                    }
                } else {
                    // adjust Y coord
                    switch (p) {
                        case UC:
                        case UR:
                        case UL:
                            y = (int) (container.getMinY() + o.y);
                            break;

                        case CL:
                        case CC:
                        case CR:
                            y = (int) (container.getMinY() + container.getMaxY() - dim.height) / 2;
                            // ignore vertical offset when vertically centered
                            break;

                        case LL:
                        case LC:
                        case LR:
                            y = (int) (container.getMaxY() - o.y - dim.height);
                    }

                    // adjust X coord
                    switch(p){
                        case UL:
                        case CL:
                        case LL:
                            x = (int) (container.getMinX() + o.x);
                            break;

                        case UC:
                        case CC:
                        case LC:
                            x = (int) (container.getMinX() + container.getMaxX()) / 2;
                            // ignore horizontal offset when horizontally centered
                            break;

                        case UR:
                        case CR:
                        case LR:
                            x = (int) (container.getMaxX() - o.x - dim.width);
                    }

                    // in the event that this block is the same size as the container, 
                    // ignore the offset so it will fit
                    if ((dim.width + (2 * o.x)) > container.width) {
                        x = (int) container.getMinX() + o.x;
                        width = container.width - (2 * o.x);
                    }

                    if ((dim.height + (2 * o.y)) > container.height) {
                        y = (int) container.getMinY() + o.y;
                        height = container.height - (2 * o.y);
                    }

                    return new Rectangle(x, y, width, height);
                }
            }
        }

        final Decoration decoration;
        final Position position;
        final Dimension dimension;
        final Point offset;
        final Mode mode;

        public Block(Decoration d, Position p, Dimension dim, Point o, Mode m) {
            decoration = d;
            position = p;
            dimension = dim;
            offset = o;
            mode = m;
        }

        public Dimension findOptimalSize(WMSMapContext mapContext){
            return (dimension != null) 
                ? dimension 
                : decoration.findOptimalSize(mapContext);
        }

        public void paint(Graphics2D g2d, Rectangle rect, WMSMapContext mapContext) 
        throws Exception {
            Dimension desiredSize = findOptimalSize(mapContext);

            Rectangle box = Position.findBounds(position, rect, desiredSize, offset, mode);
            Shape oldClip = g2d.getClip();
            g2d.setClip(box);
            decoration.paint(g2d, box, mapContext);
            g2d.setClip(oldClip);
        }
    }

    private List<Block> blocks;

    public DecorationLayout(){
        this.blocks = new ArrayList<Block>();
    }

    public static DecorationLayout fromFile(File f) throws Exception {
        //TODO: Actually try to read something
        DecorationLayout dl = new DecorationLayout();
        
        Decoration d = new WatermarkDecoration();
        Map<String, String> m = new HashMap<String, String>();
        m.put("url", "/home/dwins/public_html/alachua/theme/img/north_arrow.png");
        d.loadOptions(m);

        dl.addBlock(new Block(
            d,
            Block.Position.LR, 
            new Dimension(50, 50),
            new Point(6, 6),
            Block.Mode.OVERLAY
        ));

        dl.addBlock(new Block(
            new ScaleRatioDecoration(),
            Block.Position.LR, 
            new Dimension(100, 30),
            new Point(62, 16),
            Block.Mode.OVERLAY
        ));
        
        dl.addBlock(new Block(
            new LegendDecoration(),
            Block.Position.UL, 
            null,
            new Point(6, 6),
            Block.Mode.OVERLAY
        ));

        return dl;
    }

    public Rectangle findImageBounds(WMSMapContext mapContext) {
        int x = mapContext.getRequest().getWidth(); 
        int y = mapContext.getRequest().getHeight();

//        for (Block b : blocks){
//            Dimension d = b.findOptimalSize(mapContext);
//            x = Math.max(x, d.width + b.offset.x * 2);
//            y = Math.max(y, d.height + b.offset.y * 2);
//        }

        return new Rectangle(0, 0, x, y);
    }

    public Rectangle findMapBounds(WMSMapContext mapContext) {
        return findImageBounds(mapContext);
//        Rectangle image = findImageBounds(mapContext);
//
//        int dWidth = (int) (image.getWidth() - mapContext.getRequest().getWidth());
//        int dHeight = (int) (image.getHeight() - mapContext.getRequest().getHeight());
//
//        return new Rectangle(
//            dWidth / 2, 
//            dHeight / 2, 
//            mapContext.getRequest().getWidth(), 
//            mapContext.getRequest().getHeight()
//        );
    }

    private void addBlock(Block b){
        blocks.add(b);
    }

    public void paint(Graphics2D g2d, Rectangle paintArea, WMSMapContext mapContext){ 
        for (Block b : blocks) {
            try{
                b.paint(g2d, paintArea, mapContext);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "couldn't paint due to: ", e);
            }
        }
    }
}

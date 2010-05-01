/* Copyright (c) 2001 - 2010 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.crs;

import java.awt.Graphics2D;
import java.util.logging.Logger;

import org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A wicket resource that points back to a dynamically generated image that represents the area of
 * validity of a given {@link CoordinateReferenceSystem CRS}.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @see CRSAreaOfValidityMapBuilder
 */
public class DynamicCrsMapResource extends RenderedDynamicImageResource {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logging.getLogger("org.geoserver.web.crs");

    private final String crsCode;

    public DynamicCrsMapResource(final String crsCode, final int width, final int height) {
        super(width, height);
        this.crsCode = crsCode;
    }

    @Override
    protected boolean render(Graphics2D graphics) {
//        CoordinateReferenceSystem crs;
//        try {
//            crs = CRS.decode(crsCode);
//        } catch (Exception e) {
//            LOGGER.log(Level.INFO, "Error getting CRS " + crsCode, e);
//            throw new RuntimeException(e);
//        }
//        final int width = super.getWidth();
//        final int height = super.getHeight();
//        CRSAreaOfValidityMapBuilder mapBuilder = new CRSAreaOfValidityMapBuilder(width, height);
//        try {
//            mapBuilder.createMapFor(crs, graphics);
//        } catch (IOException e) {
//            LOGGER.log(Level.INFO, "Error creating map for " + crsCode, e);
//            throw new RuntimeException(e);
//        }
        return true;
    }
}

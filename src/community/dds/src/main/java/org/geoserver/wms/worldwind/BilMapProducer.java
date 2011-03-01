/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.worldwind;

import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.Interpolation;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.media.jai.operator.FormatDescriptor;

import org.geoserver.data.util.CoverageUtils;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetMapRequest;
import org.geoserver.wms.MapLayerInfo;
import org.geoserver.wms.WMS;
import org.geoserver.wms.map.AbstractMapResponse;
import org.geoserver.wms.map.RawMap;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.util.Assert;
import org.vfny.geoserver.util.WCSUtils;
import org.vfny.geoserver.wcs.WcsException;

import com.sun.media.imageioimpl.plugins.raw.RawImageWriterSpi;
import com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi;

/**
 * Map producer for producing Raw bil images out of an elevation model.
 * 
 * @author Tishampati Dhar
 * @since 2.0.x
 * 
 */
public final class BilMapProducer extends AbstractMapResponse {
	/** A logger for this class. */
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(BilMapProducer.class);

	/** the only MIME type this map producer supports */
    static final String MIME_TYPE = "image/bil";

    private static final String[] OUTPUT_FORMATS = {MIME_TYPE,"application/bil",
    	"application/bil8","application/bil16", "application/bil32" };
    
    private WMS wmsConfig;
    
    /**
     * Constructor for a {@link BilMapProducer}.
     *
     * @param wms
     *            that is asking us to encode the image.
     */
    public BilMapProducer(WMS wms) {
        super(BilMap.class, OUTPUT_FORMATS);
        this.wmsConfig = wms;
    }
    
	public void write(Object value,OutputStream output,Operation operation) throws ServiceException, IOException {
		Assert.isInstanceOf(BilMap.class, value);
        BilMap map = (BilMap) value;
        try {
            map.writeTo(output);
            output.flush();
        } finally {
            map.dispose();
        }
	}
}

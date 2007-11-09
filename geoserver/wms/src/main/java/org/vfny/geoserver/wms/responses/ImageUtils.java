/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.TiledImage;

import org.geotools.image.ImageWorker;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.palette.CustomPaletteBuilder;
import org.vfny.geoserver.wms.responses.palette.InverseColorMapOp;

/**
 * Provides utility methods for the shared handling of images by the raster map
 * and legend producers.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
public class ImageUtils {
    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.responses.wms.map");

    /**
     * Forces the use of the class as a pure utility methods one by declaring a
     * private default constructor.
     */
    private ImageUtils() {
        // do nothing
    }

    /**
     * Sets up a {@link BufferedImage#TYPE_4BYTE_ABGR} if the paletteInverter is
     * not provided, or a indexed image otherwise. Subclasses may override this
     * method should they need a special kind of image
     * 
     * @param width
     *            the width of the image to create.
     * @param height
     *            the height of the image to create.
     * @param paletteInverter
     *            an {@link IndexColorModel} if the image is to be indexed, or
     *            <code>null</code> otherwise.
     * @return an image of size <code>width x height</code> appropriate for
     *         the given color model, if any, and to be used as a transparent
     *         image or not depending on the <code>transparent</code>
     *         parameter.
     */
    public static BufferedImage createImage(final int width, final int height,
            final IndexColorModel palette, final boolean transparent) {
        if (palette != null) {
            // unfortunately we can't use packed rasters because line rendering
            // gets completely
            // broken, see GEOS-1312 (http://jira.codehaus.org/browse/GEOS-1312)
            // final WritableRaster raster =
            // palette.createCompatibleWritableRaster(width, height);
            final WritableRaster raster = Raster.createInterleavedRaster(palette.getTransferType(),
                    width, height, 1, null);
            return new BufferedImage(palette, raster, false, null);
        }

        if (transparent) {
            return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        }
        // don't use alpha channel if the image is not transparent
        return new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

    }

    /**
     * Sets up and returns a {@link Graphics2D} for the given
     * <code>preparedImage</code>, which is already prepared with a
     * transparent background or the given background color.
     * 
     * @param transparent
     *            whether the graphics is transparent or not.
     * @param bgColor
     *            the background color to fill the graphics with if its not
     *            transparent.
     * @param preparedImage
     *            the image for which to create the graphics.
     * @param extraHints
     *            an optional map of extra rendering hints to apply to the
     *            {@link Graphics2D}, other than
     *            {@link RenderingHints#KEY_ANTIALIASING}.
     * @return a {@link Graphics2D} for <code>preparedImage</code> with
     *         transparent background if <code>transparent == true</code> or
     *         with the background painted with <code>bgColor</code>
     *         otherwise.
     */
    public static Graphics2D prepareTransparency(final boolean transparent, final Color bgColor,
            final RenderedImage preparedImage, final Map extraHints) {
        final Graphics2D graphic;

        if (preparedImage instanceof BufferedImage) {
            graphic = ((BufferedImage) preparedImage).createGraphics();
        } else if (preparedImage instanceof TiledImage) {
            graphic = ((TiledImage) preparedImage).createGraphics();
        } else if (preparedImage instanceof VolatileImage) {
            graphic = ((VolatileImage) preparedImage).createGraphics();
        } else {
            throw new WmsException("Unrecognized back-end image type");
        }

        // fill the background with no antialiasing
        Map hintsMap;
        if (extraHints == null) {
            hintsMap = new HashMap();
        } else {
            hintsMap = new HashMap(extraHints);
        }
        hintsMap.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphic.setRenderingHints(hintsMap);
        if (transparent) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("setting to transparent");
            }

            int type = AlphaComposite.SRC;
            graphic.setComposite(AlphaComposite.getInstance(type));

            Color c = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 0);
            graphic.setBackground(bgColor);
            graphic.setColor(c);
            graphic.fillRect(0, 0, preparedImage.getWidth(), preparedImage.getHeight());
            type = AlphaComposite.SRC_OVER;
            graphic.setComposite(AlphaComposite.getInstance(type));
        } else {
            graphic.setColor(bgColor);
            graphic.fillRect(0, 0, preparedImage.getWidth(), preparedImage.getHeight());
        }
        return graphic;
    }
    
    
    /**
     * @param originalImage
     * @return
     */
    public static RenderedImage forceIndexed8Bitmask(RenderedImage originalImage, final InverseColorMapOp invColorMap) {
        // /////////////////////////////////////////////////////////////////
        //
        // Check what we need to do depending on the color model of the image we
        // are working on.
        //
        // /////////////////////////////////////////////////////////////////
        final ColorModel cm = originalImage.getColorModel();
        final boolean dataTypeByte = originalImage.getSampleModel()
                .getDataType() == DataBuffer.TYPE_BYTE;
        RenderedImage image;

        // /////////////////////////////////////////////////////////////////
        //
        // IndexColorModel and DataBuffer.TYPE_BYTE
        //
        // ///
        //
        // If we got an image whose color model is already indexed on 8 bits
        // we have to check if it is bitmask or not.
        //
        // /////////////////////////////////////////////////////////////////
        if ((cm instanceof IndexColorModel) && dataTypeByte) {
            final IndexColorModel icm = (IndexColorModel) cm;

            if (icm.getTransparency() != Transparency.TRANSLUCENT) {
                // //
                //
                // The image is indexed on 8 bits and the color model is either
                // opaque or bitmask. WE do not have to do anything.
                //
                // //
                image = originalImage;
            } else {
                // //
                //
                // The image is indexed on 8 bits and the color model is
                // Translucent, we have to perform some color operations in
                // order to convert it to bitmask.
                //
                // //
                image = new ImageWorker(originalImage)
                        .forceBitmaskIndexColorModel().getRenderedImage();
            }
        } else {
            // /////////////////////////////////////////////////////////////////
            //
            // NOT IndexColorModel and DataBuffer.TYPE_BYTE
            //
            // ///
            //
            // We got an image that needs to be converted.
            //
            // /////////////////////////////////////////////////////////////////

            if (invColorMap != null) {

                // make me parametric which means make me work with other image
                // types
                image = invColorMap.filterRenderedImage(originalImage);
            } else {
                // //
                //
                // We do not have a paletteInverter, let's create a palette that
                // is as good as possible.
                //
                // //
                // make sure we start from a componentcolormodel.
                image = new ImageWorker(originalImage)
                        .forceComponentColorModel().getRenderedImage();

//              if (originalImage.getColorModel().hasAlpha()) {
//                  // //
//                  //
//                  // We want to use the CustomPaletteBuilder but to do so we
//                  // have first to reduce the image to either opaque or
//                  // bitmask because otherwise the CustomPaletteBuilder will
//                  // fail to address transparency.
//                  //
//                  // //
//                  // I am exploiting the clamping property of the JAI
//                  // MultiplyCOnst operation.
//                  // TODO make this code parametric since people might want to
//                  // use a different transparency threshold. Right now we are
//                  // thresholding the transparency band using a fixed
//                  // threshold of 255, which means that anything that was not
//                  // transparent will become opaque.
//                  //
//                  ////
//                  final RenderedImage alpha = new ImageWorker(originalImage)
//                          .retainLastBand().multiplyConst(
//                                  new double[] { 255.0 }).retainFirstBand()
//                          .getRenderedImage();
//
//                  final int numBands = originalImage.getSampleModel()
//                          .getNumBands();
//                  originalImage = new ImageWorker(originalImage).retainBands(
//                          numBands - 1).getRenderedImage();
//
//                  final ImageLayout layout = new ImageLayout();
//
//                  if (numBands == 4) {
//                      layout.setColorModel(new ComponentColorModel(ColorSpace
//                              .getInstance(ColorSpace.CS_sRGB), true, false,
//                              Transparency.BITMASK, DataBuffer.TYPE_BYTE));
//                  } else {
//                      layout.setColorModel(new ComponentColorModel(ColorSpace
//                              .getInstance(ColorSpace.CS_GRAY), true, false,
//                              Transparency.BITMASK, DataBuffer.TYPE_BYTE));
//                  }
//
//                  image = BandMergeDescriptor.create(originalImage, alpha,
//                          new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout))
//                          .getNewRendering();
//              } else {
//                  // //
//                  //
//                  // Everything is fine
//                  //
//                  // //
//                  image = originalImage;
//              }

                // //
                //
                // Build the CustomPaletteBuilder doing some good subsampling.
                //
                // //
                final int subsx = (int) Math.pow(2, image.getWidth() / 256);
                final int subsy = (int) Math.pow(2, image.getHeight() / 256);
                image = new CustomPaletteBuilder(image, 256, subsx, subsy, 1)
                        .buildPalette().getIndexedImage();
            }
        }

        return image;
    }
}

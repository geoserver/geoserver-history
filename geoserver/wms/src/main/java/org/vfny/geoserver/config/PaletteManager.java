/* Copyright (c) 2001, 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.geotools.util.SoftValueHashMap;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;


/**
 * Allows access to palettes (implemented as {@link IndexColorModel} classes)
 *
 * @author Andrea Aime - TOPP
 *
 */
public class PaletteManager {
    private static final Logger LOG = Logger.getLogger("PaletteManager");

    /**
     * Safe palette, a 6x6x6 color cube, followed by a 39 elements gray scale,
     * and a final transparent element. See the internet safe color palette for
     * a reference <a href="http://www.intuitive.com/coolweb/colors.html">
     */
    public static final String SAFE = "SAFE";
    public static final IndexColorModel safePalette = buildDefaultPalette();
    static SoftValueHashMap paletteCache = new SoftValueHashMap();

    /**
     * TODO: we should probably provide the data directory as a constructor
     * parameter here
     */
    private PaletteManager() {
    }

    /**
     * Loads a PaletteManager
     *
     * @param name
     * @return
     * @throws Exception
     */
    public static IndexColorModel getPalette(String name)
        throws Exception {
        // check for safe palette
        if ("SAFE".equals(name.toUpperCase())) {
            return safePalette;
        }

        // check for cached one, making sure it's not stale
        PaletteCacheEntry entry = (PaletteCacheEntry) paletteCache.get(name);

        if (entry != null) {
            if (entry.isStale()) {
                paletteCache.remove(name);
            } else {
                return entry.model;
            }
        }

        // ok, load it. for the moment we load palettes from .png and .gif
        // files, but we may want to extend this ability to other file formats
        // (Gimp palettes for example), in this case we'll adopt the classic
        // plugin approach using either the Spring context of the SPI

        // hum... loading the paletteDir could be done once, but then if the
        // users
        // adds the palette dir with a running Geoserver, we won't find it
        // anymore...
        File root = GeoserverDataDirectory.getGeoserverDataDirectory();
        File paletteDir = GeoserverDataDirectory.findConfigDir(root, "palettes");

        final String[] names = new String[] { name + ".gif", name + ".png" };
        File[] paletteFiles = paletteDir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        for (int i = 0; i < names.length; i++) {
                            if (name.toLowerCase().equals(names[i])) {
                                return true;
                            }
                        }

                        return false;
                    }
                });

        // scan the files found (we may have multiple files with different
        // extensions and return the first palette you find
        for (int i = 0; i < paletteFiles.length; i++) {
            File file = paletteFiles[i];

            final Iterator it = ImageIO.getImageReaders(file);

            if (it.hasNext()) {
                final ImageReader reader = (ImageReader) it.next();
                final ColorModel cm = ((ImageTypeSpecifier) reader.getImageTypes(0).next())
                    .getColorModel();

                if (cm instanceof IndexColorModel) {
                    final IndexColorModel icm = (IndexColorModel) cm;
                    paletteCache.put(name, new PaletteCacheEntry(file, icm));

                    return icm;
                }
            }

            LOG.warning("Skipping palette file " + file.getName()
                + " since color model is not indexed (no 256 colors palette)");
        }

        return null;
    }

    /**
     * Builds a buffered image with the specified indexed color model, width and
     * height
     *
     * @param model
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage buildIndexedImage(IndexColorModel model, int width, int height) {
        IndexColorModel colorModel = buildDefaultPalette();
        WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height,
                1, null);

        return new BufferedImage(colorModel, raster, false, null);
    }

    /**
     * Builds the internet safe palette
     */
    static IndexColorModel buildDefaultPalette() {
        int[] cmap = new int[256];

        // Create the standard 6x6x6 color cube (all elements do cycle
        // between 00, 33, 66, 99, CC and FF, the decimal difference is 51)
        // The color is made of alpha, red, green and blue, in this order, from
        // the most significant bit onwards.
        int i = 0;
        int opaqueAlpha = 255 << 24;

        for (int r = 0; r < 256; r += 51) {
            for (int g = 0; g < 256; g += 51) {
                for (int b = 0; b < 256; b += 51) {
                    cmap[i] = opaqueAlpha | (r << 16) | (g << 8) | b;
                    i++;
                }
            }
        }

        // The gray scale. Make sure we end up with gray == 255
        int grayIncr = 256 / (255 - i);
        int gray = 255 - ((255 - i - 1) * grayIncr);

        for (; i < 255; i++) {
            cmap[i] = opaqueAlpha | (gray << 16) | (gray << 8) | gray;
            gray += grayIncr;
        }

        // setup the transparent color (alpha == 0)
        cmap[255] = (255 << 16) | (255 << 8) | 255;

        // create the color model
        return new IndexColorModel(8, 256, cmap, 0, true, 255, DataBuffer.TYPE_BYTE);
    }

    /**
     * An entry in the palette cache. Can determine wheter it's stale or not,
     * too
     */
    private static class PaletteCacheEntry {
        File file;
        long lastModified;
        IndexColorModel model;

        public PaletteCacheEntry(File file, IndexColorModel model) {
            this.file = file;
            this.model = model;
            this.lastModified = file.lastModified();
        }

        /**
         * Returns true if the backing file does not exist any more, or has been
         * modified
         *
         * @return
         */
        public boolean isStale() {
            return !file.exists() || (file.lastModified() != lastModified);
        }
    }
}

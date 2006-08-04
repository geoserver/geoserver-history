package org.vfny.geoserver.wms.responses.map.jpeg;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.media.jai.PlanarImage;

import org.geotools.image.ImageWorker;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;

public class JPEGMapProducer extends DefaultRasterMapProducer {

    /**
     * This class overrides the setCompressionQuality() method to workaround a problem in
     * compressing JPEG images using the javax.imageio package.
     */
    public static class MyImageWriteParam extends JPEGImageWriteParam {
        public MyImageWriteParam() {
            super(Locale.getDefault());
        }

        /**
         * This method accepts quality levels between 0 (lowest) and 1 (highest) and simply converts
         * it to a range between 0 and 256; this is not a correct conversion algorithm. However, a
         * proper alternative is a lot more complicated. This should do until the bug is fixed.
         */
        public void setCompressionQuality( float quality ) {
            if (quality < 0.0F || quality > 1.0F) {
                throw new IllegalArgumentException("Quality out-of-bounds!");
            }
            this.compressionQuality = 256 - (quality * 256);
        }
    }

    public JPEGMapProducer( String outputFormat ) {
        super(outputFormat);
    }

    protected void formatImageOutputStream( String format, BufferedImage image,
            OutputStream outStream ) throws IOException {
        if (format.equalsIgnoreCase(JPEGMapProducerFactory.MIME_TYPE))
            throw new IllegalArgumentException("The provided format " + format
                    + " is not the same as expected: " + JPEGMapProducerFactory.MIME_TYPE);

        // /////////////////////////////////////////////////////////////////
        //
        // Reformatting this image for png
        //
        // /////////////////////////////////////////////////////////////////
        final ColorModel cm = image.getColorModel();
        final boolean indexColorModel = image.getColorModel() instanceof IndexColorModel;
        final int numBands = image.getSampleModel().getNumBands();
        final boolean hasAlpha = cm.hasAlpha();
        PlanarImage encodedImage = PlanarImage.wrapRenderedImage(image);
        if (indexColorModel || hasAlpha) {
            final ImageWorker worker = new ImageWorker();
            worker.setImage(image);
            if (indexColorModel)
                worker.forceComponentColorModel();
            if (hasAlpha)
                worker.retainBands(numBands - 1);
            encodedImage = worker.getPlanarImage();

        }

        // /////////////////////////////////////////////////////////////////
        //
        // Getting a writer
        //
        // /////////////////////////////////////////////////////////////////
        final Iterator it = ImageIO.getImageWritersByMIMEType(format);
        ImageWriter writer = null;
        if (!it.hasNext()) {
            throw new IllegalStateException("No PNG ImageWriter found");
        } else
            writer = (ImageWriter) it.next();

        // /////////////////////////////////////////////////////////////////
        //
        // Compression is available only on native lib
        //
        // /////////////////////////////////////////////////////////////////
        final ImageWriteParam iwp;
        boolean nativeW = false;
        if (writer.getClass().getName().equals(
                "com.sun.media.imageioimpl.plugins.png.CLibJPEGImageWriter")) {
            iwp= writer.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionType("JPEG");
            iwp.setCompressionQuality(0.75f);// we can control quality here
            nativeW = true;
            writer.setOutput(outStream);
        } else {
            iwp=new MyImageWriteParam();
            final MemoryCacheImageOutputStream memOutStream = new MemoryCacheImageOutputStream(
                    outStream);
            writer.setOutput(memOutStream);
        }

        writer.write(null, new IIOImage(encodedImage, null, null), iwp);

        if (nativeW)
            ((ImageOutputStream) writer.getOutput()).close();
        else
            outStream.close();

        writer.dispose();
    }
}

package org.geoserver.rest.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 * FileStorage implementation for storing uploaded images in multiple sizes.
 *
 * @author David Winslow <dwinslow@opengeo.org>
 */
public class ThumbnailStorage implements FileStorage {
    private static final Dimension THUMB_SIZE = new Dimension( 100,  100);
    private static final Dimension FULL_SIZE  = new Dimension(1000, 1000);
//    private Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.rest.upload");

    public List handleUpload(
        String contentType,
        File content, 
        UniqueIDGenerator namer, 
        File uploadDirectory
    ) throws IOException {
        String originalName = null;
        ImageWriter format = null;
        BufferedImage image = null;

        originalName = "";
        format = findWriter(contentType);
        image = ImageIO.read(content);

        double thumbScale = getDesiredSize(image.getWidth(), image.getHeight(), THUMB_SIZE);
        double fullScale  = getDesiredSize(image.getWidth(), image.getHeight(), FULL_SIZE);

        String thumbName = namer.generate("thumb_" + originalName); 
        String fullName  = namer.generate("full_" + originalName);

        writeResized(image, thumbScale, format, thumbName, uploadDirectory);
        writeResized(image, fullScale,  format, fullName, uploadDirectory);

        List l = new ArrayList();
        l.add(thumbName);
        l.add(fullName);
        return l;
    }

    private double getDesiredSize(int width, int height, Dimension max) {
        return getDesiredSize(new Dimension(width, height), max);
    }

    private double getDesiredSize(Dimension original, Dimension max) {
        if (original.getWidth() <= max.getWidth() &&
            original.getHeight() <= max.getHeight()) {

            return 1.0;
        }

        double newWidth = original.getWidth() * (max.getHeight() / original.getHeight());

        if (newWidth > max.getWidth()){
            return max.getHeight() / original.getHeight();
        } else {
            return max.getWidth() / original.getWidth();
        }
    }

    private void writeResized(BufferedImage image, double scale, ImageWriter format, String name, File root) throws IOException {
        BufferedImage scaledImage = null;
        if (Math.abs(scale - 1.0) > 0.001) {
            AffineTransform xform = AffineTransform.getScaleInstance(scale, scale);
            AffineTransformOp op  = new AffineTransformOp(xform, AffineTransformOp.TYPE_BILINEAR);
            Rectangle2D dim = op.getBounds2D(image);
            scaledImage = new BufferedImage(
                (int)dim.getMaxX(), 
                (int)dim.getMaxY(), 
                image.getType()
            );
            scaledImage = op.filter(image, scaledImage);
        } else {
            scaledImage = image;
        }

        File f = new File(root, name);

        ImageOutputStream out = new FileImageOutputStream(f);

        format.setOutput(out);
        format.write(scaledImage);
        out.flush();
        out.close();
    }

    private ImageWriter findWriter(String contentType) {
        Iterator<ImageWriter> it = ImageIO.getImageWritersByMIMEType(contentType);
        if (it.hasNext()) return it.next();
        return null;
    }
}

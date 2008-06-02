package org.geoserver.rest.upload;

import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.apache.commons.fileupload.FileItem;

/**
 * Filter for resizing uploaded images.
 *
 * @author David Winslow
 */
public class ThumbnailFilter {
    private static final Dimension THUMB_SIZE = new Dimension( 100,  100);
    private static final Dimension FULL_SIZE  = new Dimension(1000, 1000);
//    private Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.rest.upload");

    public List handleUpload(FileItem f, UniqueIDGenerator namer, File uploadDirectory) throws IOException {
        String originalName  = (new File(f.getName())).getName();
        String typeName      = getSuffix(originalName);
        BufferedImage image  = ImageIO.read(f.getInputStream());
        double thumbScale = getDesiredSize(image.getWidth(), image.getHeight(), THUMB_SIZE);
        double fullScale  = getDesiredSize(image.getWidth(), image.getHeight(), FULL_SIZE);

        String thumbName = namer.generate("thumb_" + originalName); 
        String fullName  = namer.generate("full_" + originalName);

        writeResized(image, thumbScale, typeName, thumbName, uploadDirectory);
        writeResized(image, fullScale,  typeName, thumbName, uploadDirectory);

        List l = new ArrayList();
        l.add(thumbName);
        l.add(fullName);
        return l;
    }

    private double getDesiredSize(int width, int height, Dimension max){
        return getDesiredSize(new Dimension(width, height), max);
    }

    private double getDesiredSize(Dimension original, Dimension max){
        double newWidth = original.getWidth() * (max.getHeight() / original.getHeight());
        double newHeight = original.getHeight() * (max.getWidth() / original.getWidth());

        if (newWidth > max.getWidth()){
            return max.getHeight() / original.getHeight();
        } else {
            return max.getWidth() / original.getWidth();
        }
    }

    private void writeResized(BufferedImage image, double scale, String type, String name, File root) throws IOException {
        AffineTransform xform = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp op  = new AffineTransformOp(xform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage scaledImage = op.filter(image, null);
        File f = new File(root, name);
        ImageIO.write(scaledImage, type, f);
    }

    /**
     * Find the filename suffix for the filename provided.
     *
     * @todo We should probably do something nicer than just return the entire name when we can't find a suffix
     * 
     * @param originalName a file name
     * @return everything after the last . in the filename, or the entire filename if no .'s are present.
     */
    private String getSuffix(String originalName){
        int index = originalName.lastIndexOf('.');
        return originalName.substring(index);
    }
}

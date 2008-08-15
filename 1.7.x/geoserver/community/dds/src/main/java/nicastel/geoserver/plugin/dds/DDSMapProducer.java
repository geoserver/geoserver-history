package nicastel.geoserver.plugin.dds;

import gov.nasa.worldwind.formats.dds.DDSConverter;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.WWIO;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;

public class DDSMapProducer extends DefaultRasterMapProducer {

	public DDSMapProducer(String mimeType, WMS wms) {
		super(mimeType, wms);		
	}

	public void formatImageOutputStream(RenderedImage img, OutputStream os)
			throws WmsException, IOException {
		if (img instanceof BufferedImage) {
			BufferedImage bimg = (BufferedImage) img;
			ByteBuffer bb = DDSConverter.convertToDxt3(bimg);
			saveBuffer(bb,os);
		}		
	}

	
	public static boolean saveBuffer(ByteBuffer buffer, OutputStream os) throws IOException
    {
        if (buffer == null)
        {
            String message = "nullValue.BufferNull";
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (os == null)
        {
            String message = "nullValue.FileIsNull";
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }        
      
        int numBytesRead = 0;
        //WWIO.saveBuffer(buffer, new File("C:\\testdds\\image.dds"));
        os.write(buffer.array());
        return true;        
    }	    
}

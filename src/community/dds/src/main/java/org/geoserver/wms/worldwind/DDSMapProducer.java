package org.geoserver.wms.worldwind;

import gov.nasa.worldwind.formats.dds.DDSConverter;
import gov.nasa.worldwind.util.Logging;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Set;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.WMS;
import org.geoserver.wms.WMSMapContext;
import org.geoserver.wms.map.RenderedImageMapResponse;

public class DDSMapProducer extends RenderedImageMapResponse {

	/** the only MIME type this map producer supports */
	static final String MIME_TYPE = "image/dds";

	/**
	 * convenient singleton Set to expose the output format this producer
	 * supports
	 */
	private static final Set SUPPORTED_FORMATS = Collections
			.singleton(MIME_TYPE);
	
	public DDSMapProducer(WMS wms) {
		super(MIME_TYPE, wms);		
	}

	public void formatImageOutputStream(RenderedImage img, OutputStream os, 
			WMSMapContext mapContext)
			throws ServiceException, IOException {
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

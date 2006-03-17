package org.vfny.geoserver.wms.responses.map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducerTest;

public class PNGMapProducerTest extends DefaultRasterMapProducerTest {

	public PNGMapProducerTest() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected DefaultRasterMapProducer getProducerInstance() {
		return new PNGMapProducer("image/png"); // DJB: set content enconding
		// correctly
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param testName
	 *            DOCUMENT ME!
	 * @param producer
	 *            DOCUMENT ME!
	 */
	protected void assertNotBlank(String testName,
			DefaultRasterMapProducer producer) {

		try {
			final File tmpPNG = File
					.createTempFile("temporaryPNGFile", ".png");

			// tmpGif.deleteOnExit();
			OutputStream out = new FileOutputStream(tmpPNG);
			producer.writeTo(out);
			final long iniT = System.currentTimeMillis();
			out.flush();
			out.close();
			System.out.println("******** "
					+ (System.currentTimeMillis() - iniT) + "  ********");
			showImage(tmpPNG.getAbsolutePath(), ImageIO.read(tmpPNG));

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
}

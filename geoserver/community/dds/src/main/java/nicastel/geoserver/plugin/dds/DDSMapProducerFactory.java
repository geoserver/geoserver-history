package nicastel.geoserver.plugin.dds;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;
import org.vfny.geoserver.wms.responses.map.gif.GIFMapProducer;

public class DDSMapProducerFactory implements GetMapProducerFactorySpi {
	/** the only MIME type this map producer supports */
	static final String MIME_TYPE = "image/dds";

	/**
	 * convenient singleton Set to expose the output format this producer
	 * supports
	 */
	private static final Set SUPPORTED_FORMATS = Collections
			.singleton(MIME_TYPE);

	public boolean canProduce(String mapFormat) {
		return MIME_TYPE.equals(mapFormat);
	}

	public GetMapProducer createMapProducer(String mapFormat, WMS wms)
			throws IllegalArgumentException {
		if (!canProduce(mapFormat)) {
			throw new IllegalArgumentException(mapFormat
					+ " not supported by this map producer");
		}

		return new DDSMapProducer(MIME_TYPE, wms);		
	}

	public String getName() {
		return "Direct Draw Surface (DDS) map producer";
	}

	public Set getSupportedFormats() {
		// TODO Auto-generated method stub
		return SUPPORTED_FORMATS;
	}

	public boolean isAvailable() {
		return true;
	}

    public Map getImplementationHints() {
        return java.util.Collections.EMPTY_MAP;
    }

}

package org.vfny.geoserver.wms.responses.map.worldwind;

import static org.geoserver.data.test.MockData.STREAMS;

import java.awt.Color;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import junit.framework.Test;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.data.test.MockData;
import org.geoserver.security.decorators.DecoratingFeatureSource;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureSourceMapLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import org.vfny.geoserver.wms.responses.RenderExceptionStrategy;
import org.vfny.geoserver.wms.responses.map.DefaultRasterMapProducerTest;

import com.vividsolutions.jts.geom.Envelope;

public class DDSMapProducerTest extends DefaultRasterMapProducerTest {

	/** DOCUMENT ME! */
	private static final Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger(DDSMapProducerTest.class.getPackage().getName());

	private DefaultRasterMapProducer rasterMapProducer;
	
	/** DOCUMENT ME! */
    private static final Color BG_COLOR = Color.white;
    
    /** DOCUMENT ME! */
    private String mapFormat = "image/dds";

	/**
	 * This is a READ ONLY TEST so we can use one time setup
	 */
	public static Test suite() {
		return new OneTimeTestSetup(new DDSMapProducerTest());
	}

	protected DefaultRasterMapProducer getProducerInstance() {
		return new DDSMapProducer(getWMS());
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void setUpInternal() throws Exception {
	    super.setUpInternal();
	    this.rasterMapProducer = getProducerInstance();
	}
	
	public String getMapFormat()
	{
		return this.mapFormat;
	}
}

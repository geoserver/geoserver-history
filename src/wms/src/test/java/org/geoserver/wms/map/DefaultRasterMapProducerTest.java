package org.geoserver.wms.map;

import static org.geoserver.data.test.MockData.STREAMS;

import java.awt.Color;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
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
import org.geoserver.wms.WMS;
import org.geoserver.wms.WMSMapContext;
import org.geoserver.wms.WMSTestSupport;
import org.geoserver.wms.request.GetMapRequest;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureSourceMapLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.vfny.geoserver.wms.WmsException;

import com.vividsolutions.jts.geom.Envelope;

public class DefaultRasterMapProducerTest extends WMSTestSupport {

    /** DOCUMENT ME! */
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(DefaultRasterMapProducerTest.class.getPackage().getName());

    /** DOCUMENT ME! */
    private DefaultRasterMapProducer rasterMapProducer;
    
    /** DOCUMENT ME! */
    private String mapFormat = "image/gif";

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new DefaultRasterMapProducerTest());
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

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected DefaultRasterMapProducer getProducerInstance() {
        return new DummyRasterMapProducer(getWMS());
    }

    /**
     * DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void tearDownInternal() throws Exception {
        this.rasterMapProducer = null;
        super.tearDownInternal();
    }
    
    public String getMapFormat()
    {
    	return this.mapFormat;
    }

    /**
     * DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testSimpleGetMapQuery() throws Exception {

        
        Catalog catalog = getCatalog();
        final FeatureSource fs = catalog.getFeatureTypeByName(MockData.BASIC_POLYGONS.getPrefix(),
                MockData.BASIC_POLYGONS.getLocalPart()).getFeatureSource(null, null);
        
        final Envelope env = fs.getBounds();

        LOGGER.info("about to create map ctx for BasicPolygons with bounds " + env);

        final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(new ReferencedEnvelope(env, DefaultGeographicCRS.WGS84));
        map.setMapWidth(300);
        map.setMapHeight(300);
        map.setBgColor(Color.red);
        map.setTransparent(false);
        map.setRequest(new GetMapRequest(getWMS()));

        StyleInfo styleByName = catalog.getStyleByName("Default");
        Style basicStyle = styleByName.getStyle();
        map.addLayer(fs, basicStyle);

        this.rasterMapProducer.setOutputFormat(getMapFormat());
        this.rasterMapProducer.setMapContext(map);
        this.rasterMapProducer.produceMap();

        assertNotBlank("testSimpleGetMapQuery", this.rasterMapProducer);
    }

    /**
     * DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testDefaultStyle() throws Exception {
        List<org.geoserver.catalog.FeatureTypeInfo> typeInfos = getCatalog().getFeatureTypes();

        for (org.geoserver.catalog.FeatureTypeInfo info : typeInfos) {
            if (info.getQualifiedName().getNamespaceURI().equals(MockData.CITE_URI)
                    && info.getFeatureType().getGeometryDescriptor() != null)
                testDefaultStyle(info.getFeatureSource(null, null));
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @throws IOException
     *             DOCUMENT ME!
     * @throws IllegalFilterException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void testBlueLake() throws IOException, IllegalFilterException, Exception {
        final Catalog catalog = getCatalog();
        org.geoserver.catalog.FeatureTypeInfo typeInfo = catalog.getFeatureTypeByName(MockData.LAKES.getNamespaceURI(), MockData.LAKES.getLocalPart());
        Envelope env = typeInfo.getFeatureSource(null, null).getBounds();
        double shift = env.getWidth() / 6;

        env = new Envelope(env.getMinX() - shift, env.getMaxX() + shift, env.getMinY() - shift, env
                .getMaxY()
                + shift);

        final WMSMapContext map = new WMSMapContext();
        int w = 400;
        int h = (int) Math.round((env.getHeight() * w) / env.getWidth());
        map.setMapWidth(w);
        map.setMapHeight(h);
        map.setBgColor(BG_COLOR);
        map.setTransparent(true);
        map.setRequest(new GetMapRequest(getWMS()));

        addToMap(map, MockData.FORESTS);
        addToMap(map, MockData.LAKES);
        addToMap(map, MockData.STREAMS);
        addToMap(map, MockData.NAMED_PLACES);
        addToMap(map, MockData.ROAD_SEGMENTS);
        addToMap(map, MockData.PONDS);
        addToMap(map, MockData.BUILDINGS);
        addToMap(map, MockData.DIVIDED_ROUTES);
        addToMap(map, MockData.BRIDGES);
        addToMap(map, MockData.MAP_NEATLINE);

        map.setAreaOfInterest(new ReferencedEnvelope(env, DefaultGeographicCRS.WGS84));

        this.rasterMapProducer.setOutputFormat(getMapFormat());
        this.rasterMapProducer.setMapContext(map);
        this.rasterMapProducer.produceMap();

        assertNotBlank("testBlueLake", this.rasterMapProducer);
    }

    private void addToMap(final WMSMapContext map, final QName typeName) throws IOException {
        final FeatureTypeInfo ftInfo = getCatalog().getFeatureTypeByName(typeName.getNamespaceURI(), typeName.getLocalPart());
        
        List<LayerInfo> layers = getCatalog().getLayers(ftInfo);
        StyleInfo defaultStyle = layers.get(0).getDefaultStyle();
        Style style = defaultStyle.getStyle();
        
        map.addLayer(new FeatureSourceMapLayer(ftInfo.getFeatureSource(null, null), style));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param fSource
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    private void testDefaultStyle(FeatureSource fSource) throws Exception {
        Catalog catalog = getCatalog();
        Style style = catalog.getStyleByName("Default").getStyle();

        FeatureTypeInfo typeInfo = catalog.getFeatureTypeByName(MockData.LAKES.getNamespaceURI(), MockData.LAKES.getLocalPart());
        Envelope env = typeInfo.getFeatureSource(null, null).getBounds();
        env.expandToInclude(fSource.getBounds());

        int w = 400;
        int h = (int) Math.round((env.getHeight() * w) / env.getWidth());

        double shift = env.getWidth() / 6;

        env = new Envelope(env.getMinX() - shift, env.getMaxX() + shift, env.getMinY() - shift, env
                .getMaxY()
                + shift);

        WMSMapContext map = new WMSMapContext();
        map.setRequest(new GetMapRequest(getWMS()));
        map.addLayer(fSource, style);
        map.setAreaOfInterest(new ReferencedEnvelope(env, DefaultGeographicCRS.WGS84));
        map.setMapWidth(w);
        map.setMapHeight(h);
        map.setBgColor(BG_COLOR);
        map.setTransparent(false);

        this.rasterMapProducer.setOutputFormat(getMapFormat());
        this.rasterMapProducer.setMapContext(map);
        this.rasterMapProducer.produceMap();

        RenderedImage image = this.rasterMapProducer.getImage();

        String typeName = fSource.getSchema().getName().getLocalPart();
        assertNotBlank("testDefaultStyle " + typeName, this.rasterMapProducer);
    }
    
    /**
     * Checks {@link DefaultRasterMapProducer} makes good use of {@link RenderExceptionStrategy}
     */
    @SuppressWarnings("deprecation")
    public void testRenderingErrorsHandling() throws Exception {

        //the ones that are ignorable by the renderer
        assertNotNull(forceRenderingError(new TransformException("fake transform exception")));
        assertNotNull(forceRenderingError(new NoninvertibleTransformException("fake non invertible exception")));
        assertNotNull(forceRenderingError(new IllegalAttributeException("non illegal attribute exception")));
        assertNotNull(forceRenderingError(new FactoryException("fake factory exception")));

        //any other one should make the map producer fail
        try{
            forceRenderingError(new RuntimeException("fake runtime exception"));
            fail("Expected WMSException");
        }catch(WmsException e){
            assertTrue(true);
        }

        try{
            forceRenderingError(new IOException("fake IO exception"));
            fail("Expected WMSException");
        }catch(WmsException e){
            assertTrue(true);
        }
        
        try{
            forceRenderingError(new IllegalArgumentException("fake IAE exception"));
            fail("Expected WMSException");
        }catch(WmsException e){
            assertTrue(true);
        }
    }
    
    /**
     * Sets up a rendering loop and throws {@code renderExceptionToThrow} wrapped to a
     * RuntimeException when the renderer tries to get a Feature to render.
     * <p>
     * If the rendering succeeded returns the image, which is going to be a blank one but means the
     * renderer didn't complain about the exception caught. Otherwise throws back the exception
     * thrown by {@link DefaultRasterMapProducer#produceMap()}
     * </p>
     */
    @SuppressWarnings("unchecked")
    private RenderedImage forceRenderingError(final Exception renderExceptionToThrow) throws Exception {

        final WMSMapContext map = new WMSMapContext();
        map.setMapWidth(100);
        map.setMapHeight(100);
        map.setRequest(new GetMapRequest(getWMS()));
        final ReferencedEnvelope bounds = new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84);
        map.setAreaOfInterest(bounds);

        final FeatureTypeInfo ftInfo = getCatalog().getFeatureTypeByName(STREAMS.getNamespaceURI(), STREAMS.getLocalPart());

        final SimpleFeatureSource featureSource = (SimpleFeatureSource) ftInfo.getFeatureSource(null, null);
        
        DecoratingFeatureSource source;
        // This source should make the renderer fail when asking for the features
        source = new DecoratingFeatureSource(featureSource) {
            @Override
            public SimpleFeatureCollection getFeatures(Query query)
                    throws IOException {
                throw new RuntimeException(renderExceptionToThrow);
                // return delegate.getFeatures(query);
            }
        };
        
        StyleInfo someStyle = getCatalog().getStyles().get(0);
        map.addLayer(source, someStyle.getStyle());
        this.rasterMapProducer.setOutputFormat(getMapFormat());
        this.rasterMapProducer.setMapContext(map);
        this.rasterMapProducer.produceMap();

        return this.rasterMapProducer.getImage();
    }


    /**
     * This dummy producer adds no functionality to DefaultRasterMapProducer,
     * just implements a void formatImageOutputStream to have a concrete class
     * over which test that DefaultRasterMapProducer correctly generates the
     * BufferedImage.
     * 
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id: DefaultRasterMapProducerTest.java 6797 2007-05-16 10:23:50Z
     *          aaime $
     */
    private static class DummyRasterMapProducer extends DefaultRasterMapProducer {

        public DummyRasterMapProducer(WMS wms) {
            super("image/gif", new String[] { "image/gif" }, wms);
        }

        public void formatImageOutputStream(RenderedImage image, OutputStream outStream)
                throws WmsException, IOException {
            /*
             * Intentionally left blank, since this class is used just to ensure
             * the abstract raster producer correctly generates a BufferedImage.
             */
        }

        public String getContentDisposition() {
            // can be null
            return null;
        }
    }

}
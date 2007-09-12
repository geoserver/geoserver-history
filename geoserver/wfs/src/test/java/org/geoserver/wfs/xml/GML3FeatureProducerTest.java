package org.geoserver.wfs.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.WfsFactory;

import org.geoserver.data.test.MockData;
import org.geoserver.platform.Operation;
import org.geoserver.platform.Service;
import org.geoserver.wfs.WFSTestSupport;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.vfny.geoserver.global.DataStoreInfo;
import org.w3c.dom.Document;

public class GML3FeatureProducerTest extends WFSTestSupport {

    GML3OutputFormat producer() {
        WFSConfiguration configuration = new WFSConfiguration(getCatalog(),
                new FeatureTypeSchemaBuilder.GML3(getWFS(), getCatalog(),
                        getResourceLoader()));
        return new GML3OutputFormat(getWFS(), getCatalog(), configuration);
    }
    
    Operation request() {
        Service service = new Service("wfs", null, null);
        GetFeatureType type = WfsFactory.eINSTANCE.createGetFeatureType();
        type.setBaseUrl("http://localhost:8080/geoserver");
        
        Operation request = new Operation("wfs", service, null, new Object[] { type });
        return request;
    }

    public void testSingle() throws Exception {
        DataStoreInfo dataStore = getCatalog().getDataStoreInfo(MockData.CDF_PREFIX);
        FeatureSource source = dataStore.getDataStore().getFeatureSource(
                MockData.SEVEN.getLocalPart());
        FeatureCollection features = source.getFeatures();

        FeatureCollectionType fcType = WfsFactory.eINSTANCE
                .createFeatureCollectionType();

        fcType.getFeature().add(features);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        producer().write(fcType, output, request() );

        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        Document document = docBuilder.parse(new ByteArrayInputStream(output
                .toByteArray()));
        assertEquals(7, document.getElementsByTagName("cdf:Seven").getLength());

    }

    public void testMultipleSameNamespace() throws Exception {
        DataStoreInfo dataStore = getCatalog()
                .getDataStoreInfo(MockData.CDF_PREFIX);

        FeatureCollectionType fcType = WfsFactory.eINSTANCE
                .createFeatureCollectionType();
        fcType.getFeature().add(
                dataStore.getDataStore().getFeatureSource(
                        MockData.SEVEN.getLocalPart())
                        .getFeatures());
        fcType.getFeature().add(
                dataStore.getDataStore().getFeatureSource(
                        MockData.FIFTEEN.getLocalPart())
                        .getFeatures());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        producer().write(fcType, output, request());

        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        Document document = docBuilder.parse(new ByteArrayInputStream(output
                .toByteArray()));
        assertEquals(7 + 15, document.getElementsByTagName("cdf:Seven")
                .getLength()
                + document.getElementsByTagName("cdf:Fifteen").getLength());
    }

    public void testMultipleDifferentNamespace() throws Exception {
        DataStoreInfo seven = getCatalog()
                .getDataStoreInfo(MockData.CDF_PREFIX);
        DataStoreInfo polys = getCatalog()
                .getDataStoreInfo(MockData.CGF_PREFIX);

        FeatureCollectionType fcType = WfsFactory.eINSTANCE
                .createFeatureCollectionType();
        fcType.getFeature().add(
                seven.getDataStore().getFeatureSource(
                        MockData.SEVEN.getLocalPart())
                        .getFeatures());
        fcType.getFeature().add(
                polys.getDataStore().getFeatureSource(
                        MockData.POLYGONS.getLocalPart())
                        .getFeatures());
        int npolys = polys.getDataStore().getFeatureSource(
                MockData.POLYGONS.getLocalPart())
                .getFeatures().size();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        producer().write(fcType, output, request());

        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        Document document = docBuilder.parse(new ByteArrayInputStream(output
                .toByteArray()));
        assertEquals(7 + npolys, document.getElementsByTagName("cdf:Seven")
                .getLength()
                + document.getElementsByTagName("cgf:Polygons").getLength());
    }

}

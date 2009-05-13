package org.geoserver.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.geoserver.data.test.TestData;
import org.w3c.dom.Document;

/**
 * WFS Test for DataAccessIntegrationTest in Geotools/app-schema.
 * {@link org.geotools.data.complex.DataAccessIntegrationTest} Adapted from FeatureChainingWfsTest.
 * 
 * @author Rini Angreani, Curtin University of Technology
 * 
 */
public class DataAccessIntegrationWfsTest extends GeoServerAbstractTestSupport {
    /**
     * Read-only test so can use one-time setup.
     * 
     * @return
     */
    public static Test suite() {
        return new OneTimeTestSetup(new DataAccessIntegrationWfsTest());
    }

    @Override
    protected TestData buildTestData() throws Exception {
        return new DataAccessIntegrationMockData();
    }
    
    @Override
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
        // Setup XMLUnit namespaces
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wfs", "http://www.opengis.net/wfs");
        namespaces.put("ows", "http://www.opengis.net/ows");
        namespaces.put("ogc", "http://www.opengis.net/ogc");
        namespaces.put("xs", "http://www.w3.org/2001/XMLSchema");
        namespaces.put("xsd", "http://www.w3.org/2001/XMLSchema");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put(FeatureChainingMockData.GSML_NAMESPACE_PREFIX,
                FeatureChainingMockData.GSML_NAMESPACE_URI);
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }

    /**
     * Test whether GetCapabilities returns wfs:WFS_Capabilities.
     * 
     * @throws Exception
     */
    public void testDescribeFeatureType() throws Exception {
        Document doc = getAsDOM("wfs?request=DescribeFeatureType&typename=gsml:GeologicUnit");
        LOGGER.info("WFS DescribeFeatureType response:\n"
                + FeatureChainingWfsTest.prettyString(doc));
        assertEquals("xsd:schema", doc.getDocumentElement().getNodeName());

    }

    /**
     * Test whether DescribeFeatureType returns xsd:schema.
     * 
     * @throws Exception
     */
    public void testGetCapabilities() throws Exception {
        Document doc = getAsDOM("wfs?request=GetCapabilities");
        LOGGER.info("WFS GetCapabilities response:\n" + FeatureChainingWfsTest.prettyString(doc));
        assertEquals("wfs:WFS_Capabilities", doc.getDocumentElement().getNodeName());
    }

    /**
     * Test whether GetFeature returns wfs:FeatureCollection.
     * 
     * @throws Exception
     */
    public void testGetFeature() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=gsml:GeologicUnit");
        LOGGER.info("WFS GetFeature response:\n" + FeatureChainingWfsTest.prettyString(doc));
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement().getNodeName());
    }

    /**
     * Test content of GetFeature response.
     * 
     * @throws Exception
     */
    public void testGetFeatureContent() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=gsml:GeologicUnit");
        FeatureChainingWfsTest.assertXpathCount(3, "//gsml:GeologicUnit", doc);

        // GU:25699
        XMLAssert.assertXpathEvaluatesTo("er.25699",
                "//gsml:GeologicUnit[@gml:id='25699']/gml:name", doc);
        XMLAssert.assertXpathEvaluatesTo("gu.25699",
                "//gsml:GeologicUnit[@gml:id='25699']/gml:name[2]", doc);
        FeatureChainingWfsTest.assertXpathCount(1,
                "//gsml:GeologicUnit[@gml:id='25699']/gsml:occurence/gsml:MappedFeature", doc);
        // mf1
        XMLAssert.assertXpathEvaluatesTo("mf1",
                "//gsml:GeologicUnit[@gml:id='25699']/gsml:occurence/gsml:MappedFeature/@gml:id",
                doc);
        XMLAssert.assertXpathEvaluatesTo("GUNTHORPE FORMATION",
                "//gsml:GeologicUnit[@gml:id='25699']/gsml:occurence/gsml:MappedFeature/gml:name",
                doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "mf1",
                        "//gsml:GeologicUnit[@gml:id='25699']/gsml:occurence/gsml:MappedFeature/gml:name[2]",
                        doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "-1.2 52.5 -1.2 52.6 -1.1 52.6 -1.1 52.5 -1.2 52.5",
                        "//gsml:GeologicUnit[@gml:id='25699']/gsml:occurence/gsml:MappedFeature/gsml:shape//gml:posList",
                        doc);

        XMLAssert
                .assertXpathEvaluatesTo(
                        "strataform",
                        "//gsml:GeologicUnit[@gml:id='25699']/gsml:bodyMorphology/gsml:CGI_TermValue/gsml:value",
                        doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "urn:cgi:classifierScheme:GSV:GeologicalUnitType",
                        "//gsml:GeologicUnit[@gml:id='25699']/gsml:classifier/gsml:ControlledConcept/gsml:preferredName",
                        doc);
        FeatureChainingWfsTest.assertXpathCount(3,
                "//gsml:GeologicUnit[@gml:id='25699']/gsml:composition", doc);
        // cp.167775491936278844
        XMLAssert.assertXpathEvaluatesTo("significant",
                "//gsml:GeologicUnit[@gml:id='25699']/gsml:composition/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "interbedded component",
                        "//gsml:GeologicUnit[@gml:id='25699']/gsml:composition/gsml:CompositionPart/gsml:role",
                        doc);
        // cp.167775491936278812
        XMLAssert.assertXpathEvaluatesTo("significant",
                "//gsml:GeologicUnit[@gml:id='25699']/gsml:composition[2]/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "interbedded component",
                        "//gsml:GeologicUnit[@gml:id='25699']/gsml:composition[2]/gsml:CompositionPart/gsml:role",
                        doc);
        XMLAssert.assertXpathEvaluatesTo("name_a",
                "//gsml:GeologicUnit[@gml:id='25699']/gsml:composition[2]/gsml:CompositionPart/gsml:lithology"
                        + "/gsml:ControlledConcept/gml:name", doc);
        XMLAssert.assertXpathEvaluatesTo("name_b",
                "//gsml:GeologicUnit[@gml:id='25699']/gsml:composition[2]/gsml:CompositionPart/gsml:lithology"
                        + "/gsml:ControlledConcept/gml:name[2]", doc);
        XMLAssert.assertXpathEvaluatesTo("name_c",
                "//gsml:GeologicUnit[@gml:id='25699']/gsml:composition[2]/gsml:CompositionPart/gsml:lithology"
                        + "/gsml:ControlledConcept/gml:name[3]", doc);
        XMLAssert.assertXpathEvaluatesTo("cp.167775491936278812",
                "//gsml:GeologicUnit[@gml:id='25699']/gsml:composition[2]/gsml:CompositionPart/gsml:lithology"
                        + "/gsml:ControlledConcept/gml:name[4]", doc);
        XMLAssert.assertXpathEvaluatesTo("name_2",
                "//gsml:GeologicUnit[@gml:id='25699']/gsml:composition[2]/gsml:CompositionPart/gsml:lithology[2]"
                        + "/gsml:ControlledConcept/gml:name", doc);
        XMLAssert.assertXpathEvaluatesTo("cp.167775491936278812",
                "//gsml:GeologicUnit[@gml:id='25699']/gsml:composition[2]/gsml:CompositionPart/gsml:lithology[2]"
                        + "/gsml:ControlledConcept/gml:name[2]", doc);
        // cp.167775491936278856
        XMLAssert.assertXpathEvaluatesTo("minor",
                "//gsml:GeologicUnit[@gml:id='25699']/gsml:composition[3]/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "interbedded component",
                        "//gsml:GeologicUnit[@gml:id='25699']/gsml:composition[3]/gsml:CompositionPart/gsml:role",
                        doc);

        // GU:25678
        XMLAssert.assertXpathEvaluatesTo("er.25678",
                "//gsml:GeologicUnit[@gml:id='25678']/gml:name", doc);
        XMLAssert.assertXpathEvaluatesTo("gu.25678",
                "//gsml:GeologicUnit[@gml:id='25678']/gml:name[2]", doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "vein",
                        "//gsml:GeologicUnit[@gml:id='25678']/gsml:bodyMorphology/gsml:CGI_TermValue/gsml:value",
                        doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "urn:cgi:classifierScheme:GSV:GeologicalUnitType",
                        "//gsml:GeologicUnit[@gml:id='25678']/gsml:classifier/gsml:ControlledConcept/gsml:preferredName",
                        doc);
        FeatureChainingWfsTest.assertXpathCount(2,
                "//gsml:GeologicUnit[@gml:id='25678']/gsml:occurence/gsml:MappedFeature", doc);
        // mf2
        XMLAssert.assertXpathEvaluatesTo("mf2",
                "//gsml:GeologicUnit[@gml:id='25678']/gsml:occurence"
                        + "/gsml:MappedFeature/@gml:id", doc);
        XMLAssert.assertXpathEvaluatesTo("MERCIA MUDSTONE GROUP",
                "//gsml:GeologicUnit[@gml:id='25678']/gsml:occurence/gsml:MappedFeature/gml:name",
                doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "mf2",
                        "//gsml:GeologicUnit[@gml:id='25678']/gsml:occurence/gsml:MappedFeature/gml:name[2]",
                        doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "-1.3 52.5 -1.3 52.6 -1.2 52.6 -1.2 52.5 -1.3 52.5",
                        "//gsml:GeologicUnit[@gml:id='25678']/gsml:occurence/gsml:MappedFeature/gsml:shape//gml:posList",
                        doc);
        // mf3
        XMLAssert
                .assertXpathEvaluatesTo(
                        "mf3",
                        "//gsml:GeologicUnit[@gml:id='25678']/gsml:occurence[2]/gsml:MappedFeature/@gml:id",
                        doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "CLIFTON FORMATION",
                        "//gsml:GeologicUnit[@gml:id='25678']/gsml:occurence[2]/gsml:MappedFeature/gml:name",
                        doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "mf3",
                        "//gsml:GeologicUnit[@gml:id='25678']/gsml:occurence[2]/gsml:MappedFeature/gml:name[2]",
                        doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "-1.2 52.5 -1.2 52.6 -1.1 52.6 -1.1 52.5 -1.2 52.5",
                        "//gsml:GeologicUnit[@gml:id='25678']/gsml:occurence[2]/gsml:MappedFeature/gsml:shape//gml:posList",
                        doc);

        FeatureChainingWfsTest.assertXpathCount(1, "//gsml:GeologicUnit[@gml:id='25678']"
                + "/gsml:composition", doc);
        // cp.167775491936278856
        XMLAssert.assertXpathEvaluatesTo("minor",
                "//gsml:GeologicUnit[@gml:id='25678']/gsml:composition/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "interbedded component",
                        "//gsml:GeologicUnit[@gml:id='25678']/gsml:composition/gsml:CompositionPart/gsml:role",
                        doc);

        // GU:25682
        XMLAssert.assertXpathEvaluatesTo("er.25682",
                "//gsml:GeologicUnit[@gml:id='25682']/gml:name", doc);
        XMLAssert.assertXpathEvaluatesTo("gu.25682",
                "//gsml:GeologicUnit[@gml:id='25682']/gml:name[2]", doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "cross-cutting",
                        "//gsml:GeologicUnit[@gml:id='25682']/gsml:bodyMorphology/gsml:CGI_TermValue/gsml:value",
                        doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "urn:cgi:classifierScheme:GSV:GeologicalUnitType",
                        "//gsml:GeologicUnit[@gml:id='25682']/gsml:classifier/gsml:ControlledConcept/gsml:preferredName",
                        doc);
        FeatureChainingWfsTest.assertXpathCount(1,
                "//gsml:GeologicUnit[@gml:id='25682']/gsml:occurence/gsml:MappedFeature", doc);
        // mf4
        XMLAssert.assertXpathEvaluatesTo("mf4",
                "//gsml:GeologicUnit[@gml:id='25682']/gsml:occurence/gsml:MappedFeature/@gml:id",
                doc);
        XMLAssert.assertXpathEvaluatesTo("MURRADUC BASALT",
                "//gsml:GeologicUnit[@gml:id='25682']/gsml:occurence/gsml:MappedFeature/gml:name",
                doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "mf4",
                        "//gsml:GeologicUnit[@gml:id='25682']/gsml:occurence/gsml:MappedFeature/gml:name[2]",
                        doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "-1.3 52.5 -1.3 52.6 -1.2 52.6 -1.2 52.5 -1.3 52.5",
                        "//gsml:GeologicUnit[@gml:id='25682']/gsml:occurence/gsml:MappedFeature/gsml:shape//gml:posList",
                        doc);

        FeatureChainingWfsTest.assertXpathCount(1, "//gsml:GeologicUnit[@gml:id='25682']"
                + "/gsml:composition", doc);
        // cp.167775491936278812
        XMLAssert.assertXpathEvaluatesTo("significant",
                "//gsml:GeologicUnit[@gml:id='25682']/gsml:composition/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert
                .assertXpathEvaluatesTo(
                        "interbedded component",
                        "//gsml:GeologicUnit[@gml:id='25682']/gsml:composition/gsml:CompositionPart/gsml:role",
                        doc);
        XMLAssert.assertXpathEvaluatesTo("name_a",
                "//gsml:GeologicUnit[@gml:id='25682']/gsml:composition/gsml:CompositionPart/gsml:lithology"
                        + "/gsml:ControlledConcept/gml:name", doc);
        XMLAssert.assertXpathEvaluatesTo("name_b",
                "//gsml:GeologicUnit[@gml:id='25682']/gsml:composition/gsml:CompositionPart/gsml:lithology"
                        + "/gsml:ControlledConcept/gml:name[2]", doc);
        XMLAssert.assertXpathEvaluatesTo("name_c",
                "//gsml:GeologicUnit[@gml:id='25682']/gsml:composition/gsml:CompositionPart/gsml:lithology"
                        + "/gsml:ControlledConcept/gml:name[3]", doc);
        XMLAssert.assertXpathEvaluatesTo("cp.167775491936278812",
                "//gsml:GeologicUnit[@gml:id='25682']/gsml:composition/gsml:CompositionPart/gsml:lithology"
                        + "/gsml:ControlledConcept/gml:name[4]", doc);
        XMLAssert.assertXpathEvaluatesTo("name_2",
                "//gsml:GeologicUnit[@gml:id='25682']/gsml:composition/gsml:CompositionPart/gsml:lithology[2]"
                        + "/gsml:ControlledConcept/gml:name", doc);
        XMLAssert.assertXpathEvaluatesTo("cp.167775491936278812",
                "//gsml:GeologicUnit[@gml:id='25682']/gsml:composition/gsml:CompositionPart/gsml:lithology[2]"
                        + "/gsml:ControlledConcept/gml:name[2]", doc);
    }
}

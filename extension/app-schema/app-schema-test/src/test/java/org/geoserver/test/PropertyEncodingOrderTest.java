package org.geoserver.test;

import org.geoserver.wfs.WFSInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Xiangtan Lin, CSIRO Information Management and Technology
 * 
 */
public class PropertyEncodingOrderTest extends AbstractAppSchemaWfsTestSupport {

    @Override
    protected NamespaceTestData buildTestData() {
        return new PropertyEncodingOrderMockData();
    }

    /**
     * Test the gmsl:PlanarOrientation is encoded in the order of aziumth, convention, dip, polarity
     * according to the schema CGI_Value.xsd
     * 
     * @throws Exception
     */
    public void testPropertyEncodingOrder_PlanarOrientation() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=er:MineralOccurrence");
        LOGGER.info("WFS GetFeature&er:MineralOccurrence:\n" + prettyString(doc));
        assertXpathCount(1, "//er:MineralOccurrence[@gml:id='er.mineraloccurrence.S0032895']", doc);

        Node feature = doc.getElementsByTagName("er:MineralOccurrence").item(0);
        assertEquals("er:MineralOccurrence", feature.getNodeName());

        // check for gml:id
        assertXpathEvaluatesTo("er.mineraloccurrence.S0032895", "//er:MineralOccurrence/@gml:id",
                doc);

        Node name = feature.getFirstChild();
        assertEquals("gml:name", name.getNodeName());
        assertXpathEvaluatesTo("Robinson Range - Deposit D",
                "//er:MineralOccurrence[@gml:id='er.mineraloccurrence.S0032895']/gml:name", doc);

        // er:planarOrientation
        Node planarOrientation = name.getNextSibling();
        assertEquals("er:planarOrientation", planarOrientation.getNodeName());

        // gsml:CGI_PlanarOrientation
        Node gsml_planarOrientation = planarOrientation.getFirstChild();
        assertEquals("gsml:CGI_PlanarOrientation", gsml_planarOrientation.getNodeName());

        // convention
        Node convention = gsml_planarOrientation.getFirstChild();
        assertEquals("gsml:convention", convention.getNodeName());
        assertXpathEvaluatesTo(
                "strike dip right hand rule",
                "//er:MineralOccurrence[@gml:id='er.mineraloccurrence.S0032895']/er:planarOrientation/gsml:CGI_PlanarOrientation/gsml:convention",
                doc);

        // azimuth
        Node azimuth = convention.getNextSibling();
        assertEquals("gsml:azimuth", azimuth.getNodeName());
        assertXpathEvaluatesTo(
                "50.0",
                "//er:MineralOccurrence[@gml:id='er.mineraloccurrence.S0032895']/er:planarOrientation/gsml:CGI_PlanarOrientation/gsml:azimuth/gsml:CGI_NumericValue/gsml:principalValue",
                doc);

        // dip
        Node dip = azimuth.getNextSibling();
        assertEquals("gsml:dip", dip.getNodeName());
        assertXpathEvaluatesTo(
                "60-80",
                "//er:MineralOccurrence[@gml:id='er.mineraloccurrence.S0032895']/er:planarOrientation/gsml:CGI_PlanarOrientation/gsml:dip/gsml:CGI_TermValue/gsml:value",
                doc);

        // polarity
        Node polarity = dip.getNextSibling();
        assertEquals("gsml:polarity", polarity.getNodeName());
        assertXpathEvaluatesTo(
                "not applicable",
                "//er:MineralOccurrence[@gml:id='er.mineraloccurrence.S0032895']/er:planarOrientation/gsml:CGI_PlanarOrientation/gsml:polarity",
                doc);
    }

    /**
     * 
     * Test elements are encoded in the order as defined in the schema GeologicUnit is tested here
     * 
     * @throws Exception
     */

    public void testPropertyEncodingOrder_GeologicUnit() throws Exception {
        WFSInfo wfs = getGeoServer().getService(WFSInfo.class);
        wfs.setEncodeFeatureMember(true);
        getGeoServer().save(wfs);
        Document doc = getAsDOM("wfs?request=GetFeature&typename=gsml:GeologicUnit&featureid=gu.25699");
        LOGGER.info("WFS GetFeature&typename=gsml:GeologicUnit&featureid=gu.25699:\n"
                + prettyString(doc));

        assertEquals(1, doc.getElementsByTagName("gml:featureMember").getLength());
        assertXpathCount(1, "//gsml:GeologicUnit[@gml:id='gu.25699']", doc);

        // GeologicUnit
        Node feature = doc.getElementsByTagName("gsml:GeologicUnit").item(0);
        assertEquals("gsml:GeologicUnit", feature.getNodeName());

        // description
        Node description = feature.getFirstChild();
        assertEquals("gml:description", description.getNodeName());
        assertXpathEvaluatesTo("Olivine basalt, tuff, microgabbro, minor sedimentary rocks",
                "//gsml:GeologicUnit[@gml:id='gu.25699']/gml:description", doc);

        // name1
        Node name1 = description.getNextSibling();
        assertEquals("gml:name", name1.getNodeName());
        assertXpathEvaluatesTo("Yaugher Volcanic Group",
                "//gsml:GeologicUnit[@gml:id='gu.25699']/gml:name[1]", doc);

        // name2
        Node name2 = name1.getNextSibling();
        assertEquals("gml:name", name2.getNodeName());
        assertXpathEvaluatesTo("-Py", "//gsml:GeologicUnit[@gml:id='gu.25699']/gml:name[2]", doc);

        // occurrence
        Node occurrence = name2.getNextSibling();
        assertEquals("gsml:occurrence", occurrence.getNodeName());
        assertXpathCount(
                1,
                "//gsml:GeologicUnit[@gml:id='gu.25699']/gsml:occurrence[@xlink:href='urn:cgi:feature:MappedFeature:mf1']",
                doc);

        // exposureColor
        Node exposureColor = occurrence.getNextSibling();
        assertEquals("gsml:exposureColor", exposureColor.getNodeName());
        assertXpathEvaluatesTo(
                "Blue",
                "//gsml:GeologicUnit[@gml:id='gu.25699']/gsml:exposureColor/gsml:CGI_TermValue/gsml:value",
                doc);

        // outcropCharacter
        Node outcropCharacter = exposureColor.getNextSibling();
        assertEquals("gsml:outcropCharacter", outcropCharacter.getNodeName());
        assertXpathEvaluatesTo(
                "x",
                "//gsml:GeologicUnit[@gml:id='gu.25699']/gsml:outcropCharacter/gsml:CGI_TermValue/gsml:value",
                doc);

        // composition
        Node composition = outcropCharacter.getNextSibling();
        assertEquals("gsml:composition", composition.getNodeName());

        Node compositionPart = doc.getElementsByTagName("gsml:CompositionPart").item(0);
        assertEquals("gsml:CompositionPart", compositionPart.getNodeName());

        // role
        Node role = compositionPart.getFirstChild();
        assertEquals("gsml:role", role.getNodeName());
        assertXpathEvaluatesTo("significant",
                "//gsml:GeologicUnit[@gml:id='gu.25699']/gsml:composition/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);

        // proportion
        Node proportion = role.getNextSibling();
        assertEquals("gsml:proportion", proportion.getNodeName());
        assertXpathEvaluatesTo(
                "interbedded component",
                "//gsml:GeologicUnit[@gml:id='gu.25699']/gsml:composition/gsml:CompositionPart/gsml:role",
                doc);

    }
}

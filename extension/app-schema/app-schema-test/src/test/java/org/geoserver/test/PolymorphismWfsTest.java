/*
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import junit.framework.Test;
import org.w3c.dom.Document;

/**
 * WFS GetFeature to test polymorphism in Geoserver app-schema.
 * 
 * @author Rini Angreani, CSIRO Earth Science and Resource Engineering
 */
public class PolymorphismWfsTest extends AbstractAppSchemaWfsTestSupport {

    private static Document doc;

    /**
     * Read-only test so can use one-time setup.
     * 
     * @return
     */
    public static Test suite() {
        PolymorphismWfsTest test = new PolymorphismWfsTest();
        Test suite = new OneTimeTestSetup(test);
        return suite;
    }

    @Override
    protected NamespaceTestData buildTestData() {
        return new PolymorphismMockData();
    }

    public void testPolymorphism() {
        doc = getAsDOM("wfs?request=GetFeature&typename=ex:PolymorphicFeature");
        LOGGER
                .info("WFS GetFeature&typename=ex:PolymorphicFeature response:\n"
                        + prettyString(doc));
        assertXpathCount(6, "//ex:PolymorphicFeature", doc);
        // check contents per attribute (each attribute has a different use case)
        checkPolymorphicFeatureChaining();
        checkPolymorphismOnly();
        checkFeatureChainingOnly();
        checkNullValues();
        checkAnyType();
    }

    /**
     * This is to test that polymorphism with feature chaining works.
     */
    private void checkPolymorphicFeatureChaining() {
        // f1: make sure only 1 gsml:CGI_NumericValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f1']/ex:firstValue", doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:firstValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:firstValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f1']/ex:firstValue/ex:TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f1']/ex:firstValue/ex:NullValue", doc);
        assertXpathEvaluatesTo(
                "1.0",
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:firstValue/gsml:CGI_NumericValue/gsml:principalValue",
                doc);
        assertXpathEvaluatesTo(
                "m",
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:firstValue/gsml:CGI_NumericValue/gsml:principalValue/@uom",
                doc);

        // f2: make sure only 1 gsml:CGI_TermValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f2']/ex:firstValue", doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:firstValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:firstValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f2']/ex:firstValue/ex:TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f2']/ex:firstValue/ex:NullValue", doc);
        assertXpathEvaluatesTo(
                "x",
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:firstValue/gsml:CGI_TermValue/gsml:value",
                doc);

        // f3: make sure firstValue is null
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f3']/ex:firstValue", doc);

        // f4: make sure firstValue is null
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f4']/ex:firstValue", doc);

        // f5: make sure only 1 gsml:CGI_TermValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f5']/ex:firstValue", doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:firstValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:firstValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f5']/ex:firstValue/ex:TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f5']/ex:firstValue/ex:NullValue", doc);
        assertXpathEvaluatesTo(
                "y",
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:firstValue/gsml:CGI_TermValue/gsml:value",
                doc);

        // f6: make sure firstValue is null
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f6']/ex:firstValue", doc);
    }

    /**
     * This is to test that polymorphism with no feature chaining works.
     */
    private void checkPolymorphismOnly() {

        // f1: make sure only 1 gsml:CGI_NumericValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f1']/ex:secondValue", doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:secondValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:secondValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f1']/ex:secondValue/ex:TermValue",
                doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f1']/ex:secondValue/ex:NullValue",
                doc);
        assertXpathEvaluatesTo(
                "1.0",
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:secondValue/gsml:CGI_NumericValue/gsml:principalValue",
                doc);
        assertXpathEvaluatesTo(
                "m",
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:secondValue/gsml:CGI_NumericValue/gsml:principalValue/@uom",
                doc);

        // f2: make sure only 1 ex:TermValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f2']/ex:secondValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:secondValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:secondValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f2']/ex:secondValue/ex:TermValue",
                doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f2']/ex:secondValue/ex:NullValue",
                doc);
        assertXpathEvaluatesTo("0",
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:secondValue/ex:TermValue/gsml:value", doc);

        // f3: make sure only 1 gsml:CGI_NumericValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f3']/ex:secondValue", doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f3']/ex:secondValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f3']/ex:secondValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f3']/ex:secondValue/ex:TermValue",
                doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f3']/ex:secondValue/ex:NullValue",
                doc);
        assertXpathEvaluatesTo(
                "0.0",
                "//ex:PolymorphicFeature[@gml:id='f3']/ex:secondValue/gsml:CGI_NumericValue/gsml:principalValue",
                doc);
        assertXpathEvaluatesTo(
                "m",
                "//ex:PolymorphicFeature[@gml:id='f3']/ex:secondValue/gsml:CGI_NumericValue/gsml:principalValue/@uom",
                doc);

        // f4: make sure only 1 ex:TermValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f4']/ex:secondValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f4']/ex:secondValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f4']/ex:secondValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f4']/ex:secondValue/ex:TermValue",
                doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f4']/ex:secondValue/ex:NullValue",
                doc);
        assertXpathEvaluatesTo("1",
                "//ex:PolymorphicFeature[@gml:id='f4']/ex:secondValue/ex:TermValue/gsml:value", doc);

        // f5: make sure only 1 ex:TermValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f5']/ex:secondValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:secondValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:secondValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f5']/ex:secondValue/ex:TermValue",
                doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f5']/ex:secondValue/ex:NullValue",
                doc);
        assertXpathEvaluatesTo("0",
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:secondValue/ex:TermValue/gsml:value", doc);

        // f6: make sure only 1 ex:TermValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f6']/ex:secondValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f6']/ex:secondValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f6']/ex:secondValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f6']/ex:secondValue/ex:TermValue",
                doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f6']/ex:secondValue/ex:NullValue",
                doc);
        assertXpathEvaluatesTo("1000",
                "//ex:PolymorphicFeature[@gml:id='f6']/ex:secondValue/ex:TermValue/gsml:value", doc);
    }

    /**
     * This is to test that polymorphism can be achieved with feature chaining alone.
     */
    private void checkFeatureChainingOnly() {

        // f1: make sure only 1 gsml:CGI_NumericValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f1']/ex:thirdValue", doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:thirdValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:thirdValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f1']/ex:thirdValue/ex:TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f1']/ex:thirdValue/ex:NullValue", doc);
        assertXpathEvaluatesTo(
                "1.0",
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:thirdValue/gsml:CGI_NumericValue/gsml:principalValue",
                doc);
        assertXpathEvaluatesTo(
                "m",
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:thirdValue/gsml:CGI_NumericValue/gsml:principalValue/@uom",
                doc);

        // f2: make sure only 1 gsml:CGI_TermValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f2']/ex:thirdValue", doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:thirdValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:thirdValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f2']/ex:thirdValue/ex:TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f2']/ex:thirdValue/ex:NullValue", doc);
        assertXpathEvaluatesTo(
                "x",
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:thirdValue/gsml:CGI_TermValue/gsml:value",
                doc);

        // f3: make sure only 1 gsml:CGI_TermValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f3']/ex:thirdValue", doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f3']/ex:thirdValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f3']/ex:thirdValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f3']/ex:thirdValue/ex:TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f3']/ex:thirdValue/ex:NullValue", doc);
        assertXpathEvaluatesTo(
                "y",
                "//ex:PolymorphicFeature[@gml:id='f3']/ex:thirdValue/gsml:CGI_TermValue/gsml:value",
                doc);

        // f4: make sure only 1 gsml:CGI_NumericValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f4']/ex:thirdValue", doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f4']/ex:thirdValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f4']/ex:thirdValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f4']/ex:thirdValue/ex:TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f4']/ex:thirdValue/ex:NullValue", doc);
        assertXpathEvaluatesTo(
                "1.0",
                "//ex:PolymorphicFeature[@gml:id='f4']/ex:thirdValue/gsml:CGI_NumericValue/gsml:principalValue",
                doc);
        assertXpathEvaluatesTo(
                "m",
                "//ex:PolymorphicFeature[@gml:id='f4']/ex:thirdValue/gsml:CGI_NumericValue/gsml:principalValue/@uom",
                doc);

        // f5: make sure only 1 gsml:CGI_TermValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f5']/ex:thirdValue", doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:thirdValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:thirdValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f5']/ex:thirdValue/ex:TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f5']/ex:thirdValue/ex:NullValue", doc);
        assertXpathEvaluatesTo(
                "y",
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:thirdValue/gsml:CGI_TermValue/gsml:value",
                doc);

        // f6: make sure only 1 gsml:CGI_NumericValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f6']/ex:thirdValue", doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f6']/ex:thirdValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f6']/ex:thirdValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f6']/ex:thirdValue/ex:TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f6']/ex:thirdValue/ex:NullValue", doc);
        assertXpathEvaluatesTo(
                "1000.0",
                "//ex:PolymorphicFeature[@gml:id='f6']/ex:thirdValue/gsml:CGI_NumericValue/gsml:principalValue",
                doc);
        assertXpathEvaluatesTo(
                "m",
                "//ex:PolymorphicFeature[@gml:id='f6']/ex:thirdValue/gsml:CGI_NumericValue/gsml:principalValue/@uom",
                doc);
    }

    /**
     * This is to test that conditional polymorphism can be used to encode null values.
     */
    private void checkNullValues() {

        // f1: make sure only 1 ex:NullValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f1']/ex:fourthValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:fourthValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:fourthValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f1']/ex:fourthValue/ex:TermValue",
                doc);
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f1']/ex:fourthValue/ex:NullValue",
                doc);
        assertXpathEvaluatesTo("urn:ogc:def:nil:OGC::missing",
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:fourthValue/ex:NullValue/gsml:value", doc);

        // f2: make sure only 1 ex:NullValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f2']/ex:fourthValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:fourthValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:fourthValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f2']/ex:fourthValue/ex:TermValue",
                doc);
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f2']/ex:fourthValue/ex:NullValue",
                doc);
        assertXpathEvaluatesTo("urn:ogc:def:nil:OGC::missing",
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:fourthValue/ex:NullValue/gsml:value", doc);

        // f3: make sure only 1 ex:NullValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f3']/ex:fourthValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f3']/ex:fourthValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f3']/ex:fourthValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f3']/ex:fourthValue/ex:TermValue",
                doc);
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f3']/ex:fourthValue/ex:NullValue",
                doc);
        assertXpathEvaluatesTo("urn:ogc:def:nil:OGC::missing",
                "//ex:PolymorphicFeature[@gml:id='f3']/ex:fourthValue/ex:NullValue/gsml:value", doc);

        // f4: make sure only 1 ex:NullValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f4']/ex:fourthValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f4']/ex:fourthValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f4']/ex:fourthValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f4']/ex:fourthValue/ex:TermValue",
                doc);
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f4']/ex:fourthValue/ex:NullValue",
                doc);
        assertXpathEvaluatesTo("urn:ogc:def:nil:OGC::missing",
                "//ex:PolymorphicFeature[@gml:id='f4']/ex:fourthValue/ex:NullValue/gsml:value", doc);

        // f5: make sure only 1 ex:NullValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f5']/ex:fourthValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:fourthValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:fourthValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f5']/ex:fourthValue/ex:TermValue",
                doc);
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f5']/ex:fourthValue/ex:NullValue",
                doc);
        assertXpathEvaluatesTo("urn:ogc:def:nil:OGC::missing",
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:fourthValue/ex:NullValue/gsml:value", doc);

        // f6: make sure only 1 gsml:CGI_NumericValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f6']/ex:fourthValue", doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f6']/ex:fourthValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f6']/ex:fourthValue/gsml:CGI_TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f6']/ex:fourthValue/ex:TermValue",
                doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f6']/ex:fourthValue/ex:NullValue",
                doc);
        assertXpathEvaluatesTo(
                "1000.0",
                "//ex:PolymorphicFeature[@gml:id='f6']/ex:fourthValue/gsml:CGI_NumericValue/gsml:principalValue",
                doc);
        assertXpathEvaluatesTo(
                "m",
                "//ex:PolymorphicFeature[@gml:id='f6']/ex:fourthValue/gsml:CGI_NumericValue/gsml:principalValue/@uom",
                doc);
    }

    /**
     * This is to test that conditional polymorphism works with xs:anyType.
     */
    private void checkAnyType() {

        // f1: make sure only 1 ex:AnyValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f1']/ex:anyValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f1']/ex:anyValue/gsml:CGI_TermValue",
                doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:anyValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f1']/ex:anyValue/ex:TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f1']/ex:anyValue/ex:NullValue", doc);
        assertXpathEvaluatesTo(
                "1.0",
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:anyValue/gsml:CGI_NumericValue/gsml:principalValue",
                doc);
        assertXpathEvaluatesTo(
                "m",
                "//ex:PolymorphicFeature[@gml:id='f1']/ex:anyValue/gsml:CGI_NumericValue/gsml:principalValue/@uom",
                doc);

        // f2: make sure only 1 ex:anyValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f2']/ex:anyValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f2']/ex:anyValue/gsml:CGI_TermValue",
                doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:anyValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f2']/ex:anyValue/ex:TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f2']/ex:anyValue/ex:NullValue", doc);
        assertXpathEvaluatesTo("0",
                "//ex:PolymorphicFeature[@gml:id='f2']/ex:anyValue/ex:TermValue/gsml:value", doc);

        // f3: make sure only 1 ex:anyValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f3']/ex:anyValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f3']/ex:anyValue/gsml:CGI_TermValue",
                doc);
        assertXpathCount(1,
                "//ex:PolymorphicFeature[@gml:id='f3']/ex:anyValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f3']/ex:anyValue/ex:TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f3']/ex:anyValue/ex:NullValue", doc);
        assertXpathEvaluatesTo(
                "0.0",
                "//ex:PolymorphicFeature[@gml:id='f3']/ex:anyValue/gsml:CGI_NumericValue/gsml:principalValue",
                doc);
        assertXpathEvaluatesTo(
                "m",
                "//ex:PolymorphicFeature[@gml:id='f3']/ex:anyValue/gsml:CGI_NumericValue/gsml:principalValue/@uom",
                doc);

        // f4: make sure only 1 ex:NullValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f4']/ex:anyValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f4']/ex:anyValue/gsml:CGI_TermValue",
                doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f4']/ex:anyValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f4']/ex:anyValue/ex:TermValue", doc);
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f4']/ex:anyValue/ex:NullValue", doc);
        assertXpathEvaluatesTo("urn:ogc:def:nil:OGC::missing",
                "//ex:PolymorphicFeature[@gml:id='f4']/ex:anyValue/ex:NullValue/gsml:value", doc);

        // f5: make sure only 1 ex:anyValue is encoded
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f5']/ex:anyValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f5']/ex:anyValue/gsml:CGI_TermValue",
                doc);
        assertXpathCount(0,
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:anyValue/gsml:CGI_NumericValue", doc);
        assertXpathCount(1, "//ex:PolymorphicFeature[@gml:id='f5']/ex:anyValue/ex:TermValue", doc);
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f5']/ex:anyValue/ex:NullValue", doc);
        assertXpathEvaluatesTo("0",
                "//ex:PolymorphicFeature[@gml:id='f5']/ex:anyValue/ex:TermValue/gsml:value", doc);

        // f6: make sure nothing is encoded
        assertXpathCount(0, "//ex:PolymorphicFeature[@gml:id='f6']/ex:anyValue", doc);
    }
}

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml;

import org.geoserver.data.test.MockData;
import org.geoserver.wfs.WFSTestSupport;
import org.w3c.dom.Document;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;


public class GMLOutputFormatTest extends WFSTestSupport {
    public void testGML2() throws Exception {
        Document dom = getAsDOM("wfs?request=getfeature&version=1.0.0&outputFormat=gml2&typename="
                + MockData.BASIC_POLYGONS.getPrefix() + ":"
                + MockData.BASIC_POLYGONS.getLocalPart());
        assertEquals("FeatureCollection", dom.getDocumentElement().getLocalName());
        assertNotNull(getFirstElementByTagName(dom, "gml:outerBoundaryIs"));
        assertNull(getFirstElementByTagName(dom, "gml:exterior"));

        dom = getAsDOM("wfs?request=getfeature&version=1.1.0&outputFormat=gml2&typename="
                + MockData.BASIC_POLYGONS.getPrefix() + ":"
                + MockData.BASIC_POLYGONS.getLocalPart());
        assertEquals("FeatureCollection", dom.getDocumentElement().getLocalName());
        assertNotNull(getFirstElementByTagName(dom, "gml:outerBoundaryIs"));
        assertNull(getFirstElementByTagName(dom, "gml:exterior"));

        dom = getAsDOM(
                "wfs?request=getfeature&version=1.0.0&outputFormat=text/xml; subtype%3Dgml/2.1.2&typename="
                + MockData.BASIC_POLYGONS.getPrefix() + ":"
                + MockData.BASIC_POLYGONS.getLocalPart());
        assertEquals("FeatureCollection", dom.getDocumentElement().getLocalName());
        assertNotNull(getFirstElementByTagName(dom, "gml:outerBoundaryIs"));
        assertNull(getFirstElementByTagName(dom, "gml:exterior"));

        dom = getAsDOM(
                "wfs?request=getfeature&version=1.1.0&outputFormat=text/xml; subtype%3Dgml/2.1.2&typename="
                + MockData.BASIC_POLYGONS.getPrefix() + ":"
                + MockData.BASIC_POLYGONS.getLocalPart());
        assertEquals("FeatureCollection", dom.getDocumentElement().getLocalName());
        assertNotNull(getFirstElementByTagName(dom, "gml:outerBoundaryIs"));
        assertNull(getFirstElementByTagName(dom, "gml:exterior"));
    }

    public void testGML2GZIP() throws Exception {
        //        InputStream input = get( "wfs?request=getfeature&version=1.0.0&outputFormat=gml2-gzip&typename=" + 
        //            MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart());
        //        GZIPInputStream zipped = new GZIPInputStream( input );
        //        
        //        Document dom = dom( zipped );
        //        zipped.close();
        //        
        //        assertEquals( "FeatureCollection", dom.getDocumentElement().getLocalName() );
        //        assertNotNull( getFirstElementByTagName(dom, "gml:outerBoundaryIs"));
        //        assertNull( getFirstElementByTagName(dom, "gml:exterior")); 
    }

    public void testGML3() throws Exception {
        Document dom = getAsDOM("wfs?request=getfeature&version=1.0.0&outputFormat=gml3&typename="
                + MockData.BASIC_POLYGONS.getPrefix() + ":"
                + MockData.BASIC_POLYGONS.getLocalPart());
        assertEquals("FeatureCollection", dom.getDocumentElement().getLocalName());
        assertNull(getFirstElementByTagName(dom, "gml:outerBoundaryIs"));
        assertNotNull(getFirstElementByTagName(dom, "gml:exterior"));

        dom = getAsDOM("wfs?request=getfeature&version=1.1.0&outputFormat=gml3&typename="
                + MockData.BASIC_POLYGONS.getPrefix() + ":"
                + MockData.BASIC_POLYGONS.getLocalPart());
        assertEquals("FeatureCollection", dom.getDocumentElement().getLocalName());
        assertNull(getFirstElementByTagName(dom, "gml:outerBoundaryIs"));
        assertNotNull(getFirstElementByTagName(dom, "gml:exterior"));

        dom = getAsDOM(
                "wfs?request=getfeature&version=1.0.0&outputFormat=text/xml; subtype%3Dgml/3.1.1&typename="
                + MockData.BASIC_POLYGONS.getPrefix() + ":"
                + MockData.BASIC_POLYGONS.getLocalPart());
        assertEquals("FeatureCollection", dom.getDocumentElement().getLocalName());
        assertNull(getFirstElementByTagName(dom, "gml:outerBoundaryIs"));
        assertNotNull(getFirstElementByTagName(dom, "gml:exterior"));

        dom = getAsDOM(
                "wfs?request=getfeature&version=1.1.0&outputFormat=text/xml; subtype%3Dgml/3.1.1&typename="
                + MockData.BASIC_POLYGONS.getPrefix() + ":"
                + MockData.BASIC_POLYGONS.getLocalPart());
        assertEquals("FeatureCollection", dom.getDocumentElement().getLocalName());
        assertNull(getFirstElementByTagName(dom, "gml:outerBoundaryIs"));
        assertNotNull(getFirstElementByTagName(dom, "gml:exterior"));
    }
}

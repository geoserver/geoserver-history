package org.geoserver.wfs.v1_1;

import org.geoserver.wfs.WFSTestSupport;
import org.w3c.dom.Document;

public class TransactionTest extends WFSTestSupport {

    public void testInsert1() throws Exception {
        String xml = "<wfs:Transaction service=\"WFS\" version=\"1.1.0\" "
                + " xmlns:wfs=\"http://www.opengis.net/wfs\" "
                + " xmlns:gml=\"http://www.opengis.net/gml\" "
                + " xmlns:sf=\"http://cite.opengeospatial.org/gmlsf\">"
                + "<wfs:Insert handle=\"insert-1\">"
                + " <sf:PrimitiveGeoFeature gml:id=\"cite.gmlsf0-f01\">"
                + "  <gml:description>"
                + "Fusce tellus ante, tempus nonummy, ornare sed, accumsan nec, leo."
                + "Vivamus pulvinar molestie nisl."
                + "</gml:description>"
                + "<gml:name>Aliquam condimentum felis sit amet est.</gml:name>"
                //+ "<gml:name codeSpace=\"http://cite.opengeospatial.org/gmlsf\">cite.gmlsf0-f01</gml:name>"
                + "<sf:curveProperty>"
                + "  <gml:LineString gml:id=\"cite.gmlsf0-g01\" srsName=\"urn:x-ogc:def:crs:EPSG:6.11.2:4326\">"
                + "   <gml:posList>47.608284 19.034142 51.286873 16.7836 49.849854 15.764992</gml:posList>"
                + " </gml:LineString>"
                + "</sf:curveProperty>"
                + "<sf:intProperty>1025</sf:intProperty>"
                + "<sf:measurand>7.405E2</sf:measurand>"
                + "<sf:dateTimeProperty>2006-06-23T12:43:12+01:00</sf:dateTimeProperty>"
                + "<sf:decimalProperty>90.62</sf:decimalProperty>"
                + "</sf:PrimitiveGeoFeature>"
                + "</wfs:Insert>"
                + "<wfs:Insert handle=\"insert-2\">"
                + "<sf:AggregateGeoFeature gml:id=\"cite.gmlsf0-f02\">"
                + " <gml:description>"
                + "Duis nulla nisi, molestie vel, rhoncus a, ullamcorper eu, justo. Sed bibendum."
                + " Ut sem. Mauris nec nunc a eros aliquet pharetra. Mauris nonummy, pede et"
                + " tincidunt ultrices, mauris lectus fermentum massa, in ullamcorper lectus"
                + "felis vitae metus. Sed imperdiet sollicitudin dolor."
                + " </gml:description>"
                + " <gml:name codeSpace=\"http://cite.opengeospatial.org/gmlsf\">cite.gmlsf0-f02</gml:name>"
                + " <gml:name>Quisqué viverra</gml:name>"
                + " <gml:boundedBy>"
                + "   <gml:Envelope srsName=\"urn:x-ogc:def:crs:EPSG:6.11.2:4326\">"
                + "     <gml:lowerCorner>36.1 8.0</gml:lowerCorner>"
                + "    <gml:upperCorner>52.0 21.1</gml:upperCorner>"
                + "   </gml:Envelope>"
                + "  </gml:boundedBy>"
                + "   <sf:multiPointProperty>"
                + "<gml:MultiPoint srsName=\"urn:x-ogc:def:crs:EPSG:6.11.2:4326\">"
                + "<gml:pointMember>"
                + " <gml:Point><gml:pos>49.325176 21.036873</gml:pos></gml:Point>"
                + "</gml:pointMember>"
                + "<gml:pointMember>"
                + "  <gml:Point><gml:pos>36.142586 13.56189</gml:pos></gml:Point>"
                + "</gml:pointMember>"
                + "<gml:pointMember>"
                + "  <gml:Point><gml:pos>51.920937 8.014193</gml:pos></gml:Point>"
                + "</gml:pointMember>"
                + "</gml:MultiPoint>"
                + "</sf:multiPointProperty>"
                +

                "<sf:doubleProperty>2012.78</sf:doubleProperty>"
                + "  <sf:intRangeProperty>43</sf:intRangeProperty>"
                + " <sf:strProperty>"
                + "Donec ligulä pede, sodales iń, vehicula eu, sodales et, lêo."
                + "</sf:strProperty>"
                + "<sf:featureCode>AK121</sf:featureCode>"
                + "</sf:AggregateGeoFeature>"
//                + "<sf:EntitéGénérique gml:id=\"cite.gmlsf0-f03\">"
//                + "<gml:description>"
//                + "Suspendisse in odio sit amet lorem dictum semper. Integer ultrices purus"
//                + "vel orci. Donec felis massa, suscipit a, lacinia nec, rhoncus vulputate,"
//                + "tellus."
//                + "</gml:description>"
//                + "<gml:name codeSpace=\"http://cite.opengeospatial.org/gmlsf\">cite.gmlsf0-f03</gml:name>"
//                + "<gml:name>Duis egestas luctus libero</gml:name>"
//                + "<sf:attribut.Géométrie>"
//                + "<gml:LineString gml:id=\"cite.gmlsf0-g04\" srsName=\"urn:x-ogc:def:crs:EPSG:6.11.2:4326\">"
//                + "<gml:description>Curabitur ut enim at metus vehicula</gml:description>"
//                + "<gml:posList>47.35948 0.06282902 45.62291 -0.6651201 44.34261 -2.341187 45.08817 -2.696537 45.953514 -3.793995 47.21312 -2.773082 48.644974 -3.131115</gml:posList>"
//                + "</gml:LineString>" + "</sf:attribut.Géométrie>"
//                + " <sf:boolProperty>true</sf:boolProperty>"
//                + "<sf:str4Property>ghij</sf:str4Property>"
//                + "<sf:featureRef>cite.gmlsf0-f01</sf:featureRef>"
//                + "</sf:EntitéGénérique>" 
                + "</wfs:Insert>"
                + "</wfs:Transaction>";

        Document dom = postAsDOM("wfs", xml);
        assertEquals("wfs:TransactionResponse", dom.getDocumentElement()
                .getNodeName());
        assertTrue(dom.getElementsByTagName("ogc:FeatureId").getLength() > 0);
    }

    //	
    // public void testInsertWithNoSRS() throws Exception {
    // //1. do a getFeature
    // String getFeature = "<wfs:GetFeature " +
    // "service=\"WFS\" " +
    // "version=\"1.1.0\" " +
    // "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " +
    // "xmlns:ogc=\"http://www.opengis.net/ogc\" " +
    // "xmlns:wfs=\"http://www.opengis.net/wfs\" " +
    // "> " +
    // "<wfs:Query typeName=\"cgf:Points\"> " +
    // "<wfs:PropertyName>cite:id</wfs:PropertyName> " +
    // "</wfs:Query> " +
    // "</wfs:GetFeature>";
    //		
    // Document dom = postAsDOM( "wfs", getFeature );
    // int n = dom.getElementsByTagName( "gml:featureMember" ).getLength();
    //		
    // //perform an insert
    // String insert = "<wfs:Transaction service=\"WFS\" version=\"1.1.0\" " +
    // "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " +
    // "xmlns:ogc=\"http://www.opengis.net/ogc\" " +
    // "xmlns:wfs=\"http://www.opengis.net/wfs\" " +
    // "xmlns:gml=\"http://www.opengis.net/gml\"> " +
    // "<wfs:Insert > " +
    // "<cgf:Points>" +
    // "<cgf:pointProperty>" +
    // "<gml:Point>" +
    // "<gml:pos>100 100</gml:pos>" +
    // "</gml:Point>" +
    // "</cgf:pointProperty>" +
    // "<cgf:id>t0002</cgf:id>" +
    // "</cgf:Points>" +
    // "</wfs:Insert>" +
    // "</wfs:Transaction>";
    //	
    // dom = postAsDOM( "wfs", insert );
    //	
    // NodeList numberInserteds = dom.getElementsByTagName( "wfs:totalInserted"
    // );
    // Element numberInserted = (Element) numberInserteds.item( 0 );
    // assertNotNull( numberInserted );
    // assertEquals( "1", numberInserted.getFirstChild().getNodeValue() );
    //		
    // //do another get feature
    // dom = postAsDOM( "wfs", getFeature );
    // assertEquals( n+1, dom.getElementsByTagName(
    // "gml:featureMember").getLength() );
    // }
    //	
    // public void testInsertWithSRS() throws Exception {
    //		
    // //1. do a getFeature
    // String getFeature = "<wfs:GetFeature " +
    // "service=\"WFS\" " +
    // "version=\"1.1.0\" " +
    // "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " +
    // "xmlns:ogc=\"http://www.opengis.net/ogc\" " +
    // "xmlns:wfs=\"http://www.opengis.net/wfs\" " +
    // "> " +
    // "<wfs:Query typeName=\"cgf:Points\"> " +
    // "<wfs:PropertyName>cite:id</wfs:PropertyName> " +
    // "</wfs:Query> " +
    // "</wfs:GetFeature>";
    //		
    // Document dom = postAsDOM( "wfs", getFeature );
    // int n = dom.getElementsByTagName( "gml:featureMember" ).getLength();
    //		
    // //perform an insert
    // String insert = "<wfs:Transaction service=\"WFS\" version=\"1.1.0\" " +
    // "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " +
    // "xmlns:ogc=\"http://www.opengis.net/ogc\" " +
    // "xmlns:wfs=\"http://www.opengis.net/wfs\" " +
    // "xmlns:gml=\"http://www.opengis.net/gml\"> " +
    // "<wfs:Insert srsName=\"EPSG:4326\"> " +
    // "<cgf:Points>" +
    // "<cgf:pointProperty>" +
    // "<gml:Point>" +
    // "<gml:pos>1 1</gml:pos>" +
    // "</gml:Point>" +
    // "</cgf:pointProperty>" +
    // "<cgf:id>t0003</cgf:id>" +
    // "</cgf:Points>" +
    // "</wfs:Insert>" +
    // "</wfs:Transaction>";
    //	
    // dom = postAsDOM( "wfs", insert );
    //		
    // NodeList numberInserteds = dom.getElementsByTagName( "wfs:totalInserted"
    // );
    // Element numberInserted = (Element) numberInserteds.item( 0 );
    //		
    // assertNotNull( numberInserted );
    // assertEquals( "1", numberInserted.getFirstChild().getNodeValue() );
    //		
    // //do another get feature
    // getFeature = "<wfs:GetFeature " +
    // "service=\"WFS\" version=\"1.1.0\" " +
    // "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " +
    // "xmlns:ogc=\"http://www.opengis.net/ogc\" " +
    // "xmlns:wfs=\"http://www.opengis.net/wfs\" " +
    // "> " +
    // "<wfs:Query typeName=\"cgf:Points\"> " +
    // "<wfs:PropertyName>cite:id</wfs:PropertyName> " +
    // "</wfs:Query> " +
    // "</wfs:GetFeature>";
    // dom = postAsDOM( "wfs", getFeature );
    //	
    // NodeList pointsList = dom.getElementsByTagName( "cgf:Points" );
    // assertEquals( n+1, pointsList.getLength() );
    //		
    // //get the feature we inserted
    // for ( int i = 0; i < pointsList.getLength(); i++) {
    // Element points = (Element) pointsList.item( i );
    // NodeList ids = points.getElementsByTagName( "cgf:id" );
    // Element id = (Element) ids.item( 0 );
    // if ( "t0003".equals( id.getFirstChild().getNodeValue() ) ) {
    // NodeList gmlPoints = points.getElementsByTagName( "gml:Point" );
    // Element gmlPoint = (Element) gmlPoints.item( 0 );
    // String text = gmlPoint.getFirstChild().getFirstChild().getNodeValue();
    // String[] xy = text.split( "," );
    //				
    // assertFalse ( "1".equals( xy[ 0 ] ) );
    // assertFalse ( "1".equals( xy[ 1 ] ) );
    // }
    // }
    // }

}

package org.geoserver.wps;

import junit.framework.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import static org.custommonkey.xmlunit.XMLAssert.*;
public class DescribeProcessTest extends WPSTestSupport {

    //read-only test
    public static Test suite() {
        return new OneTimeTestSetup(new DescribeProcessTest());
    }
    
    public void test() throws Exception {
        Document d = getAsDOM( "wps?service=wps&request=describeprocess&identifier=buffer");
        print( d );
        
        assertEquals( "wps:ProcessDescriptions", d.getDocumentElement().getNodeName() );
        assertXpathExists( "/wps:ProcessDescriptions", d );
        
        String base = "/wps:ProcessDescriptions/wps:ProcessDescription/wps:DataInputs";
        
        //first parameter
        assertXpathExists( base + "/wps:Input[1]", d );
        assertXpathEvaluatesTo("buffer", base + "/wps:Input[1]/ows:Identifier/child::text()", d );
        assertXpathExists( base + "/wps:Input[1]/wps:LiteralData", d );

        assertXpathEvaluatesTo("double", base + "/wps:Input[1]/wps:LiteralData/ows:DataType/child::text()", d );
        
        //second parameter
        base += "/wps:Input[2]";
        assertXpathExists( base , d );
        assertXpathExists( base + "/wps:ComplexData", d );
        
        base += "/wps:ComplexData";
        assertXpathEvaluatesTo("text/xml; subtype=gml/2.1.2", 
                base + "/wps:Default/wps:Format/wps:MimeType/child::text()", d);
        assertXpathEvaluatesTo("text/xml; subtype=gml/2.1.2", 
                base + "/wps:Supported/wps:Format[1]/wps:MimeType/child::text()", d);
        assertXpathEvaluatesTo("text/xml; subtype=gml/3.1.1", 
                base + "/wps:Supported/wps:Format[2]/wps:MimeType/child::text()", d);
    
        //output
        base = "/wps:ProcessDescriptions/wps:ProcessDescription/wps:ProcessOutputs";
        assertXpathExists( base + "/wps:Output", d );
        assertXpathExists( base + "/wps:Output/wps:ComplexOutput", d );
    }
}

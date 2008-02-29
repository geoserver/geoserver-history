package org.geoserver.wcs;

import static org.custommonkey.xmlunit.XMLAssert.*;

import java.io.File;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.geoserver.wcs.test.WCSTestSupport;
import org.w3c.dom.Document;

public class GetCoverageSupportsStoreTrueTest extends WCSTestSupport {

    // @Override
    // protected String getDefaultLogConfiguration() {
    // return "/DEFAULT_LOGGING.properties";
    // }

    public void testKvpBasic() throws Exception {
        String request = "wcs?service=WCS&version=1.1.1&request=GetCoverage" + "&identifier="
                + layerId(WCSTestSupport.TASMANIA_BM)
                + "&BoundingBox=-90,-180,90,180,urn:ogc:def:crs:EPSG:4326"
                + "&GridBaseCRS=urn:ogc:def:crs:EPSG:4326" + "&format=geotiff&store=true";
        Document dom = getAsDOM(request);
        print(dom);
        checkValidationErrors(dom, WCS11_SCHEMA);
        assertXpathEvaluatesTo(WCSTestSupport.TASMANIA_BM.getLocalPart(),
                "wcs:Coverages/wcs:Coverage/ows:Title", dom);
        
        // grab the file path
        String path = xpath.evaluate("//ows:Reference/@xlink:href", dom);
        File wcsTemp = new File(dataDirectory.getDataDirectoryRoot(), "temp/wcs");
        File coverageFile = new File(wcsTemp, path.substring(path.lastIndexOf("/") + 1)).getAbsoluteFile();
        
        // make sure the tiff can be actually read
        ImageReader reader = ImageIO.getImageReadersByFormatName("tiff").next();
        reader.setInput(ImageIO.createImageInputStream(coverageFile));
        reader.read(0);
        reader.dispose();
    }
}
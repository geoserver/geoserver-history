package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.data.Status;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.FileRepresentation;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.geotools.geometry.GeneralEnvelope;

import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.jdom.Element;

/**
 * Restlet for Style resources
 * 
 * @author David Winslow <dwinslow@openplans.org> , The Open Planning Project
 */
public class CoverageResource extends MapResource {
    private DataConfig myDC;

    public Map getSupportedFormats(){
        Map m = new HashMap();
        m.put("html",
                new HTMLFormat("HTMLTemplates/coverage.ftl"){
                public Map readRepresentation(Representation rep){
                try{
                SAXBuilder builder = new SAXBuilder();
                Document doc = builder.build(rep.getStream());
                Map map = new HashMap();

                // TODO: Why does this stop working when I specify that the node has to be a span?

                map.put("Label", getText("//*[@class='label']", doc));
                map.put("Description", getText("//*[@class='description']", doc));
                map.put("Keywords", getList("//*[@class='keyword']", doc));
                map.put("DefaultStyle", getText("//*[@class='default-style']", doc));
                map.put("SupplementaryStyles", getList("//*[@class='supplementary-style']", doc));
                map.put("OnlineResource", getText("//*[@class='online-resource']", doc));
                map.put("CRS", getText("//*[@class='crs']", doc));
                map.put("SupportedRequestCRSs", getList("//*[@class='supported-request-crs']", doc));
                map.put("SupportedResponseCRSs", getList("//*[@class='supported-response-crs']", doc));
                // TODO: Special case for Envelope
                map.put("NativeFormat", getText("//*[@class='native-format']", doc));
                map.put("SupportedFormats", getList("//*[@class='supported-format']", doc));
                map.put("DefaultInterpolationMethod", getText("//*[@class='default-interpolation']", doc));
                map.put("InterpolationMethods", getList("//*[@class='supported-interpolation']", doc));

                return map;
                } catch (Exception e){
                    System.out.println("Couldn't read representation due to: " + e.getMessage());
                    return new HashMap();
                }
                }

                /**
                 * @param xpath the XPath to retrieve
                 * @param doc the Document from which to retrieve it
                 * @return the text body of the first matched element, or
                 *     null if none is matched
                 */
                private String getText(String xpath, Document doc) throws JDOMException{
                    XPath path = XPath.newInstance(xpath);
                    Element elem = (Element)path.selectSingleNode(doc);
                    return (elem == null ? null : elem.getText());
                }

                /**
                 * @param xpath the XPath to retrieve
                 * @param doc the Document from which to retrieve it
                 * @return a list of all the text nodes underneath the nodes matched by the XPath
                 */
                private List getList(String xpath, Document doc) throws JDOMException{
                    XPath path = XPath.newInstance(xpath);
                    List elems = (List)path.selectNodes(doc);
                    List results = new ArrayList();

                    Iterator it = elems.iterator();
                    while (it.hasNext()){
                        Element elem = (Element) it.next();
                        String text = elem.getText();
                        results.add(text);
                    }
                    return results;
                }
                }

             );
        m.put("json", new JSONFormat());
        m.put(null, m.get("html"));
        return m;
    }

    public CoverageResource(Context context,
            Request request,
            Response response,
            DataConfig myDataConfig) {
        super(context, request, response);
        myDC = myDataConfig;
    }

    public Map getMap(){
        String coverageStore = (String)getRequest().getAttributes().get("coveragestore");
        String coverageName = (String)getRequest().getAttributes().get("coverage");
        String qualified = coverageStore + ":" + coverageName;
        CoverageConfig cc = (CoverageConfig)myDC.getCoverages().get(qualified);
        return getCoverageConfigMap(cc);
    }

    public boolean allowPut(){return true;}

    protected void putMap(Map details) throws Exception{
        //TODO: create the coverage
    }

    private Map getCoverageConfigMap(CoverageConfig cc){
        Map m = new HashMap();
        m.put("WMSPath", cc.getWmsPath());
        m.put("CRS", cc.getCrs().getName());
        GeneralEnvelope env = cc.getEnvelope();
        List envPoints = new ArrayList();
        envPoints.add(env.getLowerCorner().getOrdinate(0));
        envPoints.add(env.getLowerCorner().getOrdinate(1));
        envPoints.add(env.getUpperCorner().getOrdinate(0));
        envPoints.add(env.getUpperCorner().getOrdinate(1));
        m.put("Envelope", envPoints);
        //m.put("CRSFull", cc.getCrs().toString());
        m.put("DefaultStyle", cc.getDefaultStyle());
        m.put("SupplementaryStyles", cc.getStyles());// TODO: does this return a list of strings or something else?
        m.put("Label", cc.getLabel());
        m.put("Description", cc.getDescription());
        m.put("OnlineResource", cc.getMetadataLink().getAbout()); // TODO: get the actual URL, this may take some digging
        m.put("Keywords", cc.getKeywords());
        m.put("SupportedRequestCRSs", cc.getRequestCRSs());
        m.put("SupportedResponseCRSs", cc.getResponseCRSs());
        m.put("NativeFormat", cc.getNativeFormat());
        m.put("SupportedFormats", cc.getSupportedFormats());
        m.put("DefaultInterpolationMethod", cc.getDefaultInterpolationMethod());
        m.put("InterpolationMethods", cc.getInterpolationMethods());
        return m;
    }
}

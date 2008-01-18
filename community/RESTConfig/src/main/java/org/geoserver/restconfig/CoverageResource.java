/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.geometry.GeneralEnvelope;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.opengis.coverage.grid.Format;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.CoverageStoreInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.MetaDataLink;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Restlet for Style resources
 *
 * @author David Winslow <dwinslow@openplans.org> , The Open Planning Project
 */
public class CoverageResource extends MapResource {
    private DataConfig myDC;
    private Data myData;

    public Map getSupportedFormats() {
        Map m = new HashMap();
        m.put("html",
            new HTMLFormat("HTMLTemplates/coverage.ftl") {
                public Map readRepresentation(Representation rep) {
                    try {
                        SAXBuilder builder = new SAXBuilder();
                        Document doc = builder.build(rep.getStream());
                        Map map = new HashMap();

                        // TODO: Why does this stop working when I specify that the node has to be a span?
                        map.put("Label", getText("//*[@class='label']", doc));
                        map.put("Description", getText("//*[@class='description']", doc));
                        map.put("Keywords", getList("//*[@class='keyword']", doc));
                        map.put("DefaultStyle", getText("//*[@class='default-style']", doc));
                        map.put("SupplementaryStyles",
                            getList("//*[@class='supplementary-style']", doc));
                        map.put("OnlineResource", getText("//*[@class='online-resource']", doc));
                        map.put("CRS", getText("//*[@class='crs']", doc));
                        map.put("SupportedRequestCRSs",
                            getList("//*[@class='supported-request-crs']", doc));
                        map.put("SupportedResponseCRSs",
                            getList("//*[@class='supported-response-crs']", doc));
                        // TODO: Special case for Envelope
                        map.put("NativeFormat", getText("//*[@class='native-format']", doc));
                        map.put("SupportedFormats", getList("//*[@class='supported-format']", doc));
                        map.put("DefaultInterpolationMethod",
                            getText("//*[@class='default-interpolation']", doc));
                        map.put("InterpolationMethods",
                            getList("//*[@class='supported-interpolation']", doc));

                        return map;
                    } catch (Exception e) {
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
                private String getText(String xpath, Document doc)
                    throws JDOMException {
                    XPath path = XPath.newInstance(xpath);
                    Element elem = (Element) path.selectSingleNode(doc);

                    return ((elem == null) ? null : elem.getText());
                }

                /**
                 * @param xpath the XPath to retrieve
                 * @param doc the Document from which to retrieve it
                 * @return a list of all the text nodes underneath the nodes matched by the XPath
                 */
                private List getList(String xpath, Document doc)
                    throws JDOMException {
                    XPath path = XPath.newInstance(xpath);
                    List elems = (List) path.selectNodes(doc);
                    List results = new ArrayList();

                    Iterator it = elems.iterator();

                    while (it.hasNext()) {
                        Element elem = (Element) it.next();
                        String text = elem.getText();
                        results.add(text);
                    }

                    return results;
                }
            });
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("coveragestore"));
        m.put(null, m.get("html"));

        return m;
    }

    public CoverageResource(Context context, Request request, Response response,
        ApplicationContext appCon) {
        super(context, request, response);
        myDC = (DataConfig) appCon.getBean("dataConfig");
        myData = (Data) appCon.getBean("data");
    }

    public CoverageResource(){
        super();
    }

    public void setDataConfig(DataConfig dc){
        myDC = dc;
    }

    public DataConfig getDataConfig(){
        return myDC;
    }

    public void setData(Data d){
        myData = d;
    }

    public Data getData(){
        return myData;
    }

    public Map getMap() {
        System.out.println("GETting with a CoverageResource");

        String coverageStore = (String) getRequest().getAttributes().get("coveragestore");
        String coverageName = (String) getRequest().getAttributes().get("coverage");
        String qualified = coverageStore + ":" + coverageName;
        CoverageConfig cc = (CoverageConfig) myDC.getCoverages().get(qualified);

        return getCoverageConfigMap(cc);
    }

    public boolean allowPut() {
        System.out.println("CoverageResource allows PUTs");

        return true;
    }

    public void putMap(Map details) throws Exception {
        //TODO: create the coverage
        String coverageStore = (String) getRequest().getAttributes().get("coveragestore");
        String coverageName = (String) getRequest().getAttributes().get("coverage");
        String qualified = coverageStore + ":" + coverageName;
        CoverageConfig cc = null;
        cc = (CoverageConfig) myDC.getCoverages().get(qualified);

        if (cc == null) {
            CoverageStoreInfo csi = myData.getFormatInfo(coverageStore);
            Format format = csi.getFormat();
            AbstractGridCoverage2DReader reader = (AbstractGridCoverage2DReader) csi.getReader(); // TODO acquire an instance somehow

            //cc = new CoverageConfig(coverageStore, format, reader, myDC);
        }

        if (details.get("WMSPath") != null) {
            cc.setWmsPath((String) details.get("WMSPath"));
        }

        if (details.get("CRS") != null) {
            CoordinateReferenceSystem crs = makeCRS((String) details.get("CRS"));

            if (crs != null) {
                cc.setCrs(crs);
            }
        }

        if (details.get("Envelope") != null) {
            GeneralEnvelope env = makeEnvelope((List) details.get("Envelope"));

            if (env != null) {
                cc.setEnvelope(env);
            }
        }

        if (details.get("DefaultStyle") != null) {
            cc.setDefaultStyle((String) details.get("DefaultStyle"));
        }

        if (details.get("SupplementaryStyles") != null) {
            ArrayList styles = new ArrayList();
            styles.addAll((List) details.get("SupplementaryStyles"));
            cc.setStyles(styles);
        }

        if (details.get("Label") != null) {
            cc.setLabel((String) details.get("Label"));
        }

        if (details.get("Description") != null) {
            cc.setDescription((String) details.get("Description"));
        }

        if (details.get("OnlineResource") != null) {
            cc.setMetadataLink(makeLink((String) details.get("OnlineResource")));
        }

        if (details.get("Keywords") != null) {
            cc.setKeywords((List) details.get("Keywords"));
        }

        if (details.get("SupportedRequestCRSs") != null) {
            cc.setRequestCRSs((List) details.get("SupportedRequestCRSs"));
        }

        if (details.get("SupportedResponseCRSs") != null) {
            cc.setResponseCRSs((List) details.get("SupportedResponseCRSs"));
        }

        if (details.get("NativeFormat") != null) {
            cc.setNativeFormat((String) details.get("NativeFormat"));
        }

        if (details.get("SupportedFormats") != null) {
            cc.setSupportedFormats((List) details.get("SupportedFormats"));
        }

        if (details.get("DefaultInterpolationMethod") != null) {
            cc.setDefaultInterpolationMethod((String) details.get("DefaultInterpolationMethod"));
        }

        if (details.get("InterpolationMethods") != null) {
            cc.setInterpolationMethods((List) details.get("InterpolationMethods"));
        }

        myDC.addCoverage(qualified, cc);
    }

    private MetaDataLink makeLink(String s) {
        return null;
    }

    private GeneralEnvelope makeEnvelope(List points) {
        return null;
    }

    private CoordinateReferenceSystem makeCRS(String crs) {
        return null;
    }

    private Map getCoverageConfigMap(CoverageConfig cc) {
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
        m.put("SupplementaryStyles", cc.getStyles()); // TODO: does this return a list of strings or something else?
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

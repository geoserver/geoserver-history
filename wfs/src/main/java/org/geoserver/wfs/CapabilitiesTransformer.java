/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import com.vividsolutions.jts.geom.Envelope;

import net.opengis.wfs.GetCapabilitiesType;

import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.ows.xml.v1_0.OWS;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.wfs.response.GetCapabilitiesResponse;
import org.geotools.factory.FactoryRegistry;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.v1_0.OGC;
import org.geotools.gml3.GML;
import org.geotools.xlink.XLINK;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.opengis.filter.expression.Function;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.FeatureTypeInfoTitleComparator;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Based on the <code>org.geotools.xml.transform</code> framework, does the job
 * of encoding a WFS 1.0 Capabilities document.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @author Chris Holmes
 * @author Justin Deoliveira
 *
 * @version $Id$
 */
public abstract class CapabilitiesTransformer extends TransformerBase {
    /** logger */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CapabilitiesTransformer.class.getPackage()
                                                                                       .getName());

    /** identifer of a http get + post request */
    private static final String HTTP_GET = "Get";
    private static final String HTTP_POST = "Post";

    /** wfs namespace */
    protected static final String WFS_URI = "http://www.opengis.net/wfs";

    /** xml schema namespace + prefix */
    protected static final String XSI_PREFIX = "xsi";
    protected static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";

    /** filter namesapce + prefix */
    protected static final String OGC_PREFIX = "ogc";
    protected static final String OGC_URI = OGC.NAMESPACE;

    /** wfs service */
    protected WFS wfs;

    /** catalog */
    protected Data catalog;

    /**
     * Creates a new CapabilitiesTransformer object.
     */
    public CapabilitiesTransformer(WFS wfs, Data catalog) {
        super();
        setNamespaceDeclarationEnabled(false);

        this.wfs = wfs;
        this.catalog = catalog;
    }

    /**
     * Transformer for wfs 1.0 capabilities document.
     *
     * @author Justin Deoliveira, The Open Planning Project
     *
     */
    public static class WFS1_0 extends CapabilitiesTransformer {
        public WFS1_0(WFS wfs, Data catalog) {
            super(wfs, catalog);
        }

        public Translator createTranslator(ContentHandler handler) {
            return new CapabilitiesTranslator1_0(handler);
        }

        class CapabilitiesTranslator1_0 extends TranslatorSupport {
            GetCapabilitiesType request;
            
            public CapabilitiesTranslator1_0(ContentHandler handler) {
                super(handler, null, null);
            }

            public void encode(Object object) throws IllegalArgumentException {
                request = (GetCapabilitiesType)object;
                String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(request.getBaseUrl(), wfs.getGeoServer().getProxyBaseUrl());
                
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute("", "version", "version", "", "1.0.0");
                attributes.addAttribute("", "xmlns", "xmlns", "", WFS_URI);

                NameSpaceInfo[] namespaces = catalog.getNameSpaces();

                for (int i = 0; i < namespaces.length; i++) {
                    NameSpaceInfo namespace = namespaces[i];
                    String prefix = namespace.getPrefix();
                    String uri = namespace.getURI();

                    if ("xml".equals(prefix)) {
                        continue;
                    }

                    String prefixDef = "xmlns:" + prefix;
                    attributes.addAttribute("", prefixDef, prefixDef, "", uri);
                }

                //filter
                attributes.addAttribute("", "xmlns:" + OGC_PREFIX, "xmlns:" + OGC_PREFIX, "",
                    OGC_URI);

                //xml schema
                attributes.addAttribute("", "xmlns:" + XSI_PREFIX, "xmlns:" + XSI_PREFIX, "",
                    XSI_URI);

                String locationAtt = XSI_PREFIX + ":schemaLocation";
                String locationDef = WFS_URI + " "
                    + ResponseUtils.appendPath(proxifiedBaseUrl,
                        "schemas/wfs/1.0.0/WFS-capabilities.xsd");
                attributes.addAttribute("", locationAtt, locationAtt, "", locationDef);

                start("WFS_Capabilities", attributes);

                handleService();
                handleCapability();
                handleFeatureTypes();
                handleFilterCapabilities();

                end("WFS_Capabilities");
            }

            /**
            * Encodes the wfs:Service element.
            *
            *         <pre>
            * &lt;xsd:complexType name="ServiceType"&gt;
            *         &lt;xsd:sequence&gt;
            *                 &lt;xsd:element name="Name" type="xsd:string"/&gt;
            *                 &lt;xsd:element ref="wfs:Title"/&gt;
            *                 &lt;xsd:element ref="wfs:Abstract" minOccurs="0"/&gt;
            *                 &lt;xsd:element ref="wfs:Keywords" minOccurs="0"/&gt;
            *                 &lt;xsd:element ref="wfs:OnlineResource"/&gt;
            *                 &lt;xsd:element ref="wfs:Fees" minOccurs="0"/&gt;
            *                 &lt;xsd:element ref="wfs:AccessConstraints" minOccurs="0"/&gt;
            *          &lt;/xsd:sequence&gt;
            * &lt;/xsd:complexType&gt;
            *
            *         </pre>
            *
            */
            private void handleService() {
                start("Service");
                element("Name", wfs.getName());
                element("Title", wfs.getTitle());
                element("Abstract", wfs.getAbstract());

                handleKeywords(wfs.getKeywords());

                String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(request.getBaseUrl(), wfs.getGeoServer().getProxyBaseUrl());
                
                element("OnlineResource", ResponseUtils.appendPath(proxifiedBaseUrl, "wfs"));
                element("Fees", wfs.getFees());
                element("AccessConstraints", wfs.getAccessConstraints());
                end("Service");
            }

            /**
             * Encodes the wfs:Keywords element.
             * <p>
             *
             *                 <pre>
             *         &lt;!-- Short words to help catalog searching.
             *                  Currently, no controlled vocabulary has
             *                 been defined. --&gt;
             *         &lt;xsd:element name="Keywords" type="xsd:string"/&gt;
             *                 </pre>
             *
             * </p>
             *
             */
            private void handleKeywords(String[] kwlist) {
                if (kwlist == null) {
                    handleKeywords((List) null);
                } else {
                    handleKeywords(Arrays.asList(kwlist));
                }
            }

            /**
             * Encodes the wfs:Keywords element.
             * <p>
             *
             *                 <pre>
             *         &lt;!-- Short words to help catalog searching.
             *                  Currently, no controlled vocabulary has
             *                 been defined. --&gt;
             *         &lt;xsd:element name="Keywords" type="xsd:string"/&gt;
             *                 </pre>
             *
             * </p>
             *
             */
            private void handleKeywords(List kwlist) {
                StringBuffer kwds = new StringBuffer();

                for (int i = 0; (kwlist != null) && (i < kwlist.size()); i++) {
                    kwds.append(kwlist.get(i));

                    if (i != (kwlist.size() - 1)) {
                        kwds.append(", ");
                    }
                }

                element("Keywords", kwds.toString());
            }

            /**
             * Encodes the wfs:Capability element.
             * <p>
             *
             *                 <pre>
             * &lt;xsd:complexType name="CapabilityType"&gt;
             *         &lt;xsd:sequence&gt;
             *                &lt;xsd:element name="Request" type="wfs:RequestType"/&gt;
             *                &lt;!-- The optional VendorSpecificCapabilities element lists any
             *                                 capabilities unique to a particular server.  Because the
             *                                 information is not known a priori, it cannot be constrained
             *                                 by this particular schema document.  A vendor-specific schema
             *                                 fragment must be supplied at the start of the XML capabilities
             *                                 document, after the reference to the general WFS_Capabilities
             *                                 schema. --&gt;
             *                &lt;xsd:element ref="wfs:VendorSpecificCapabilities" minOccurs="0"/&gt;
             *        &lt;/xsd:sequence&gt;
             * &lt;/xsd:complexType&gt;
             *                 </pre>
             *
             * </p>
             */
            private void handleCapability() {
                start("Capability");
                start("Request");
                handleGetCapabilities();
                handleDescribeFT();
                handleGetFeature();

                if (wfs.getServiceLevel() >= WFS.TRANSACTIONAL) {
                    handleTransaction();
                }

                if (wfs.getServiceLevel() == WFS.COMPLETE) {
                    handleLock();
                    handleFeatureWithLock();
                }

                end("Request");
                end("Capability");
            }

            /**
             * Encodes the wfs:GetCapabilities elemnt.
             * <p>
             *
             *                 <pre>
             * &lt;xsd:complexType name="GetCapabilitiesType"&gt;
             *        &lt;xsd:sequence&gt;
             *                &lt;xsd:element name="DCPType" type="wfs:DCPTypeType" maxOccurs="unbounded"/&gt;
             *        &lt;/xsd:sequence&gt;
             * &lt;/xsd:complexType&gt;
             *                 </pre>
             *
             * </p>
             */
            private void handleGetCapabilities() {
                String capName = "GetCapabilities";
                start(capName);
                handleDcpType(capName, HTTP_GET);
                handleDcpType(capName, HTTP_POST);
                end(capName);
            }

            /**
             * Encodes the wfs:DescribeFeatureType element.
             * <p>
             * <pre>
             * &lt;xsd:complexType name="DescribeFeatureTypeType"&gt;
             *         &lt;xsd:sequence&gt;
             *                &lt;xsd:element name="SchemaDescriptionLanguage"
             *                        type="wfs:SchemaDescriptionLanguageType"/&gt;
             *                &lt;xsd:element name="DCPType"
             *         type="wfs:DCPTypeType" maxOccurs="unbounded"/&gt;
             *        &lt;/xsd:sequence&gt;
             * &lt;/xsd:complexType&gt;
             * </pre>
             * </p>
             */
            private void handleDescribeFT() {
                String capName = "DescribeFeatureType";
                start(capName);
                start("SchemaDescriptionLanguage");
                element("XMLSCHEMA", null);
                end("SchemaDescriptionLanguage");

                handleDcpType(capName, HTTP_GET);
                handleDcpType(capName, HTTP_POST);
                end(capName);
            }

            /**
             * Encodes the wfs:GetFeature element.
             *
             * &lt;xsd:complexType name="GetFeatureTypeType"&gt;
             *         &lt;xsd:sequence&gt;
             *                &lt;xsd:element name="ResultFormat" type="wfs:ResultFormatType"/&gt;
             *                &lt;xsd:element name="DCPType" type="wfs:DCPTypeType" maxOccurs="unbounded"/&gt;
             *         &lt;/xsd:sequence&gt;
             * &lt;/xsd:complexType&gt;
             */
            private void handleGetFeature() {
                String capName = "GetFeature";
                start(capName);

                String resultFormat = "ResultFormat";
                start(resultFormat);

                //we accept numerous formats, but cite only allows you to have GML2
                if (wfs.getCiteConformanceHacks()) {
                    element("GML2", null);
                } else {
                    //FULL MONTY
                    Collection featureProducers = GeoServerExtensions.extensions(WFSGetFeatureOutputFormat.class);

                    Map dupes = new HashMap();
                    for (Iterator i = featureProducers.iterator(); i.hasNext();) {
                        WFSGetFeatureOutputFormat format = (WFSGetFeatureOutputFormat) i.next();
                        if (!dupes.containsKey(format.getCapabilitiesElementName())) {
                            element(format.getCapabilitiesElementName(), null);
                            dupes.put(format.getCapabilitiesElementName(), new Object());
                        }
                    }
                }

                end(resultFormat);

                handleDcpType(capName, HTTP_GET);
                handleDcpType(capName, HTTP_POST);
                end(capName);
            }

            /**
             * Encodes the wfs:Transaction element.
             * <p>
             * <pre>
             *  &lt;xsd:complexType name="TransactionType"&gt;
             *          &lt;xsd:sequence&gt;
             *                &lt;xsd:element name="DCPType" type="wfs:DCPTypeType" maxOccurs="unbounded"/&gt;
             *          &lt;/xsd:sequence&gt;
             *  &lt;/xsd:complexType&gt;
                            * </pre>
                            * </p>
             */
            private void handleTransaction() {
                String capName = "Transaction";
                start(capName);
                handleDcpType(capName, HTTP_GET);
                handleDcpType(capName, HTTP_POST);
                end(capName);
            }

            /**
             * Encodes the wfs:LockFeature element.
             * <p>
             * <pre>
             *  &lt;xsd:complexType name="LockFeatureTypeType"&gt;
             *          &lt;xsd:sequence&gt;
             *                &lt;xsd:element name="DCPType" type="wfs:DCPTypeType" maxOccurs="unbounded"/&gt;
             *         &lt;/xsd:sequence&gt;
             *        &lt;/xsd:complexType&gt;
             * </pre>
             * </p>
             */
            private void handleLock() {
                String capName = "LockFeature";
                start(capName);
                handleDcpType(capName, HTTP_GET);
                handleDcpType(capName, HTTP_POST);
                end(capName);
            }

            /**
             * Encodes the wfs:GetFeatureWithLock element.
             *
             * &lt;xsd:complexType name="GetFeatureTypeType"&gt;
             *         &lt;xsd:sequence&gt;
             *                &lt;xsd:element name="ResultFormat" type="wfs:ResultFormatType"/&gt;
             *                &lt;xsd:element name="DCPType" type="wfs:DCPTypeType" maxOccurs="unbounded"/&gt;
             *         &lt;/xsd:sequence&gt;
             * &lt;/xsd:complexType&gt;
             */
            private void handleFeatureWithLock() {
                String capName = "GetFeatureWithLock";
                start(capName);
                start("ResultFormat");
                //TODO: output format extensions
                element("GML2", null);
                end("ResultFormat");
                handleDcpType(capName, HTTP_GET);
                handleDcpType(capName, HTTP_POST);
                end(capName);
            }

            /**
             * Encodes a <code>DCPType</code> element.
             *        <p>
             *        <pre>
             *  &lt;!-- Available Distributed Computing Platforms (DCPs) are
             *                listed here.  At present, only HTTP is defined. --&gt;
             *        &lt;xsd:complexType name="DCPTypeType"&gt;
             *                &lt;xsd:sequence&gt;
             *                        &lt;xsd:element name="HTTP" type="wfs:HTTPType"/&gt;
             *                &lt;/xsd:sequence&gt;
             *        &lt;/xsd:complexType&gt;
             *
             *        </pre>
             *        </p>
             * @param capabilityName the URL of the onlineresource for HTTP GET
             *        method requests
             * @param httpMethod the URL of the onlineresource for HTTP POST method
             *        requests
             */
            private void handleDcpType(String capabilityName, String httpMethod) {
                String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(request.getBaseUrl(), wfs.getGeoServer().getProxyBaseUrl());

                if (proxifiedBaseUrl.endsWith("?")) {
                    proxifiedBaseUrl = proxifiedBaseUrl.substring(0, proxifiedBaseUrl.length() - 1);
                }

                if (HTTP_GET.equals(httpMethod)) {
                    proxifiedBaseUrl = ResponseUtils.appendPath(proxifiedBaseUrl, "wfs?request=" + capabilityName);
                } else if (HTTP_POST.equals(httpMethod)) {
                    proxifiedBaseUrl = ResponseUtils.appendPath(proxifiedBaseUrl, "wfs?");
                }

                start("DCPType");
                start("HTTP");

                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "onlineResource", "onlineResource", "", proxifiedBaseUrl);
                element(httpMethod, null, atts);

                end("HTTP");
                end("DCPType");
            }

            /**
             * Encodes the wfs:FeatureTYpeList element.
             * <p>
             *         <pre>
             * &lt;xsd:complexType name="FeatureTypeListType"&gt;
             *        &lt;xsd:sequence&gt;
             *                &lt;xsd:element name="Operations"
             *         type="wfs:OperationsType" minOccurs="0"/&gt;
             *                &lt;xsd:element name="FeatureType"
             *         type="wfs:FeatureTypeType" maxOccurs="unbounded"/&gt;
             *        &lt;/xsd:sequence&gt;
             * &lt;/xsd:complexType&gt;
             *         </pre>
             * </p>
             */
            private void handleFeatureTypes() {
                if (!wfs.isEnabled()) {
                    // should we return anything if we are disabled?
                }

                start("FeatureTypeList");
                start("Operations");

                if ((wfs.getServiceLevel() | WFS.SERVICE_BASIC) != 0) {
                    element("Query", null);
                }

                if ((wfs.getServiceLevel() | WFS.SERVICE_INSERT) != 0) {
                    element("Insert", null);
                }

                if ((wfs.getServiceLevel() | WFS.SERVICE_UPDATE) != 0) {
                    element("Update", null);
                }

                if ((wfs.getServiceLevel() | WFS.SERVICE_DELETE) != 0) {
                    element("Delete", null);
                }

                if ((wfs.getServiceLevel() | WFS.SERVICE_LOCKING) != 0) {
                    element("Lock", null);
                }

                end("Operations");

                List featureTypes = new ArrayList(catalog.getFeatureTypeInfos().values());
                Collections.sort(featureTypes, new FeatureTypeInfoTitleComparator());
                for (Iterator it = featureTypes.iterator(); it.hasNext();) {
                    FeatureTypeInfo ftype = (FeatureTypeInfo) it.next();

                    //can't handle ones that aren't enabled.
                    //and they shouldn't be handled, as they won't function.
                    //JD: deal with this
                    //if (ftype.isEnabled()) {
                    handleFeatureType(ftype);

                    //}
                }

                end("FeatureTypeList");
            }

            /**
             * Default handle of a FeatureTypeInfo content that writes the
             * latLongBBox as well as the GlobalBasic's parameters
             *
             * <p>
             *         <pre>
             * &lt;xsd:complexType name="FeatureTypeType"&gt;
             *        &lt;xsd:sequence&gt;
             *                &lt;xsd:element name="Name" type="xsd:QName"/&gt;
             *                &lt;xsd:element ref="wfs:Title" minOccurs="0"/&gt;
             *                &lt;xsd:element ref="wfs:Abstract" minOccurs="0"/&gt;
             *                &lt;xsd:element ref="wfs:Keywords" minOccurs="0"/&gt;
             *                &lt;xsd:element ref="wfs:SRS"/&gt;
             *                &lt;xsd:element name="Operations"
             *         type="wfs:OperationsType" minOccurs="0"/&gt;
             *                &lt;xsd:element name="LatLongBoundingBox"
             *         type="wfs:LatLongBoundingBoxType"
             *         minOccurs="0" maxOccurs="unbounded"/&gt;
             *                &lt;xsd:element name="MetadataURL"
             *         type="wfs:MetadataURLType"
             *         minOccurs="0" maxOccurs="unbounded"/&gt;
             *        &lt;/xsd:sequence&gt;
             * &lt;/xsd:complexType&gt;
             *         </pre>
             * </p>
             * @param ftype The FeatureType configuration to report capabilities
             *        on.
             *
             * @throws RuntimeException For any errors.
             */
            private void handleFeatureType(FeatureTypeInfo info) {
                Envelope bbox = null;

                try {
                    bbox = info.getLatLongBoundingBox();
                } catch (IOException e) {
                    String msg = "Could not calculate bbox for: " + info.getName();
                    LOGGER.log(Level.SEVERE, msg, e);

                    return;
                }

                start("FeatureType");
                element("Name", info.getName());
                element("Title", info.getTitle());
                element("Abstract", info.getAbstract());
                handleKeywords(info.getKeywords());

                /**
                 * @task REVISIT: should getSRS() return the full URL?
                 */
                element("SRS", "EPSG:" + info.getSRS());

                String minx = String.valueOf(bbox.getMinX());
                String miny = String.valueOf(bbox.getMinY());
                String maxx = String.valueOf(bbox.getMaxX());
                String maxy = String.valueOf(bbox.getMaxY());

                AttributesImpl bboxAtts = new AttributesImpl();
                bboxAtts.addAttribute("", "minx", "minx", "", minx);
                bboxAtts.addAttribute("", "miny", "miny", "", miny);
                bboxAtts.addAttribute("", "maxx", "maxx", "", maxx);
                bboxAtts.addAttribute("", "maxy", "maxy", "", maxy);

                element("LatLongBoundingBox", null, bboxAtts);

                end("FeatureType");
            }

            /**
             * Encodes the ogc:Filter_Capabilities element.
             * <p>
             * <pre>
             * &lt;xsd:element name="Filter_Capabilities"&gt;
             *        &lt;xsd:complexType&gt;
             *          &lt;xsd:sequence&gt;
             *                &lt;xsd:element name="Spatial_Capabilities" type="ogc:Spatial_CapabilitiesType"/&gt;
             *                &lt;xsd:element name="Scalar_Capabilities" type="ogc:Scalar_CapabilitiesType"/&gt;
             *          &lt;/xsd:sequence&gt;
             *        &lt;/xsd:complexType&gt;
             *&lt;/xsd:element&gt;
             * </pre>
             * </p>
             */
            private void handleFilterCapabilities() {
                String ogc = "ogc:";

                //REVISIT: for now I"m just prepending ogc onto the name element.
                //Is the proper way to only do that for the qname?  I guess it
                //would only really matter if we're going to be producing capabilities
                //documents that aren't qualified, and I don't see any reason to
                //do that.
                start(ogc + "Filter_Capabilities");
                start(ogc + "Spatial_Capabilities");
                start(ogc + "Spatial_Operators");
                element(ogc + "Disjoint", null);
                element(ogc + "Equals", null);
                element(ogc + "DWithin", null);
                element(ogc + "Beyond", null);
                element(ogc + "Intersect", null);
                element(ogc + "Touches", null);
                element(ogc + "Crosses", null);
                element(ogc + "Within", null);
                element(ogc + "Contains", null);
                element(ogc + "Overlaps", null);
                element(ogc + "BBOX", null);
                end(ogc + "Spatial_Operators");
                end(ogc + "Spatial_Capabilities");

                start(ogc + "Scalar_Capabilities");
                element(ogc + "Logical_Operators", null);
                start(ogc + "Comparison_Operators");
                element(ogc + "Simple_Comparisons", null);
                element(ogc + "Between", null);
                element(ogc + "Like", null);
                element(ogc + "NullCheck", null);
                end(ogc + "Comparison_Operators");
                start(ogc + "Arithmetic_Operators");
                element(ogc + "Simple_Arithmetic", null);

                handleFunctions(ogc); //djb: list functions

                end(ogc + "Arithmetic_Operators");
                end(ogc + "Scalar_Capabilities");
                end(ogc + "Filter_Capabilities");
            }

            /**
             * &lt;xsd:complexType name="FunctionsType"&gt;
             *         &lt;xsd:sequence&gt;
             *                 &lt;xsd:element name="Function_Names" type="ogc:Function_NamesType"/&gt;
             *         &lt;/xsd:sequence&gt;
             *         &lt;/xsd:complexType&gt;
             *
             */
            private void handleFunctions(String prefix) {
                start(prefix + "Functions");
                start(prefix + "Function_Names");

                Iterator it = FactoryRegistry.lookupProviders(Function.class);

                //Sort them up for easier visual inspection
                SortedSet sortedFunctions = new TreeSet(new Comparator() {
                            public int compare(Object o1, Object o2) {
                                String n1 = ((Function) o1)
                                    .getName();
                                String n2 = ((Function) o2).getName();

                                return n1.toLowerCase().compareTo(n2.toLowerCase());
                            }
                        });

                while (it.hasNext()) {
                    sortedFunctions.add(it.next());
                }

                //write them now that functions are sorted by name
                it = sortedFunctions.iterator();

                while (it.hasNext()) {
                    Function fe = (Function) it.next();

                    //TODO: as of now the geoapi Function interface 
                    // does not allow use to report back properly the number of 
                    // parameters, so we check for instances of FunctionExpression 
                    // for now
                    if (fe instanceof FunctionExpression) {
                        String funName = fe.getName();
                        int funNArgs = ((FunctionExpression) fe).getArgCount();

                        AttributesImpl atts = new AttributesImpl();
                        atts.addAttribute("", "nArgs", "nArgs", "", funNArgs + "");

                        element(prefix + "Function_Name", funName, atts);
                    }
                }

                end(prefix + "Function_Names");
                end(prefix + "Functions");
            }
        }
    }

    /**
     * Transformer for wfs 1.1 capabilities document.
     *
     * @author Justin Deoliveira, The Open Planning Project
     *
     */
    public static class WFS1_1 extends CapabilitiesTransformer {
        public WFS1_1(WFS wfs, Data catalog) {
            super(wfs, catalog);
        }

        public Translator createTranslator(ContentHandler handler) {
            return new CapabilitiesTranslator1_1(handler);
        }

        class CapabilitiesTranslator1_1 extends TranslatorSupport {
            GetCapabilitiesType request;
            
            public CapabilitiesTranslator1_1(ContentHandler handler) {
                super(handler, null, null);
            }

            public void encode(Object object) throws IllegalArgumentException {
                request = (GetCapabilitiesType)object;
                String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(request.getBaseUrl(), wfs.getGeoServer().getProxyBaseUrl());
                
                AttributesImpl attributes = attributes(new String[] {
                            "version", "1.1.0", "xmlns:xsi", XSI_URI, "xmlns", WFS_URI, "xmlns:wfs",
                            WFS_URI, "xmlns:ows", OWS.NAMESPACE, "xmlns:gml", GML.NAMESPACE,
                            "xmlns:ogc", OGC.NAMESPACE, "xmlns:xlink", XLINK.NAMESPACE,
                            "xsi:schemaLocation",
                            
                        org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE + " "
                            + ResponseUtils.appendPath(proxifiedBaseUrl, "schemas/wfs/1.1.0/wfs.xsd")
                        });

                NameSpaceInfo[] namespaces = catalog.getNameSpaces();

                for (int i = 0; i < namespaces.length; i++) {
                    NameSpaceInfo namespace = namespaces[i];

                    String prefix = namespace.getPrefix();
                    String uri = namespace.getURI();

                    //ignore xml prefix
                    if ("xml".equals(prefix)) {
                        continue;
                    }

                    String prefixDef = "xmlns:" + prefix;

                    attributes.addAttribute("", prefixDef, prefixDef, "", uri);
                }

                start("wfs:WFS_Capabilities", attributes);

                serviceIdentification();
                serviceProvider();
                operationsMetadata();
                featureTypeList();
                //supportsGMLObjectTypeList();
                filterCapabilities();

                end("wfs:WFS_Capabilities");
            }

            /**
             * Encodes the ows:ServiceIdentification element.
             * <p>
             * <pre>
             * &lt;complexType&gt;
             *         &lt;complexContent&gt;
             *           &lt;extension base="ows:DescriptionType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="ServiceType" type="ows:CodeType"&gt;
             *         &lt;annotation&gt;
             *             &lt;documentation&gt;A service type name from a registry of services.
             *             For example, the values of the codeSpace URI and name and code string may
             *             be "OGC" and "catalogue." This type name is normally used for
             *             machine-to-machine communication.&lt;/documentation&gt;
             *         &lt;/annotation&gt;
             *         &lt;/element&gt;
             *         &lt;element name="ServiceTypeVersion" type="ows:VersionType" maxOccurs="unbounded"&gt;
             *         &lt;annotation&gt;
             *             &lt;documentation&gt;Unordered list of one or more versions of this service
             *             type implemented by this server. This information is not adequate for
             *             version negotiation, and shall not be used for that purpose. &lt;/documentation&gt;
             *         &lt;/annotation&gt;
             *         &lt;/element&gt;
             *         &lt;element ref="ows:Fees" minOccurs="0"&gt;
             *         &lt;annotation&gt;
             *               &lt;documentation&gt;If this element is omitted, no meaning is implied. &lt;/documentation&gt;
             *         &lt;/annotation&gt;
             *         &lt;/element&gt;
             *         &lt;element ref="ows:AccessConstraints" minOccurs="0" maxOccurs="unbounded"&gt;
             *         &lt;annotation&gt;
             *               &lt;documentation&gt;Unordered list of access constraints applied to assure
             *               the protection of privacy or intellectual property, and any other
             *               restrictions on retrieving or using data from or otherwise using this
             *               server. The reserved value NONE (case insensitive) shall be used to
             *               mean no access constraints are imposed. If this element is omitted,
             *               no meaning is implied. &lt;/documentation&gt;
             *               &lt;/annotation&gt;
             *               &lt;/element&gt;
             *             &lt;/sequence&gt;
             *     &lt;/extension&gt;
             *   &lt;/complexContent&gt;
             *&lt;/complexType&gt;
             * </pre>
             * </p>
             *
             */
            void serviceIdentification() {
                start("ows:ServiceIdentification");

                element("ows:Title", wfs.getTitle());
                element("ows:Abstract", wfs.getAbstract());

                keywords(wfs.getKeywords());

                element("ows:ServiceType", "WFS");
                element("ows:ServiceTypeVersion", "1.1.0");

                element("ows:Fees", wfs.getFees());
                element("ows:AccessConstraints", wfs.getAccessConstraints());

                end("ows:ServiceIdentification");
            }

            /**
                 * Encodes the ows:ServiceProvider element.
                 * <p>
                 * <pre>
                 * &lt;complexType&gt;
                 *        &lt;sequence&gt;
                 *           &lt;element name="ProviderName" type="string"&gt;
                 *           &lt;annotation&gt;
                 *        &lt;documentation&gt;A unique identifier for the service provider organization. &lt;/documentation&gt;
                 *     &lt;/annotation&gt;
                     *     &lt;/element&gt;
                     *           &lt;element name="ProviderSite" type="ows:OnlineResourceType" minOccurs="0"&gt;
                     *     &lt;annotation&gt;
                     *        &lt;documentation&gt;Reference to the most relevant web site of the service provider. &lt;/documentation&gt;
                     *     &lt;/annotation&gt;
                     *     &lt;/element&gt;
                     *     &lt;element name="ServiceContact" type="ows:ResponsiblePartySubsetType"&gt;
                     *     &lt;annotation&gt;
                     *        &lt;documentation&gt;Information for contacting the service provider. The
                     *        OnlineResource element within this ServiceContact element should not be used
                     *        to reference a web site of the service provider. &lt;/documentation&gt;
                     *     &lt;/annotation&gt;
                     *     &lt;/element&gt;
                     *  &lt;/sequence&gt;
                     *&lt;/complexType&gt;
                 * </pre>
                 * </p>
                 *
                 */
            void serviceProvider() {
                start("ows:ServiceProvider");

                element("ows:ProviderName", null);
                element("ows:ProviderSite", null);
                element("ows:ServiceContact", null);

                end("ows:ServiceProvider");
            }

            /**
                 * Encodes the ows:OperationsMetadata element.
                 * <p>
                 * <pre>
                 * &lt;complexType&gt;
                 *        &lt;sequence&gt;
                 *                &lt;element ref="ows:Operation" minOccurs="2" maxOccurs="unbounded"&gt;
                 *                &lt;annotation&gt;
                 *                &lt;documentation&gt;Metadata for unordered list of all the (requests for) operations
                 *                that this server interface implements. The list of required and optional
                 *                operations implemented shall be specified in the Implementation Specification
                 *                for this service. &lt;/documentation&gt;
                 *                &lt;/annotation&gt;
                 *                &lt;/element&gt;
                 *                &lt;element name="Parameter" type="ows:DomainType" minOccurs="0" maxOccurs="unbounded"&gt;
                 *                &lt;annotation&gt;
                 *                        &lt;documentation&gt;Optional unordered list of parameter valid domains that each
                 *                        apply to one or more operations which this server interface implements. The
                 *                        list of required and optional parameter domain limitations shall be specified
                 *                        in the Implementation Specification for this service. &lt;/documentation&gt;
                 *                &lt;/annotation&gt;
                 *                &lt;/element&gt;
                 *                &lt;element name="Constraint" type="ows:DomainType" minOccurs="0" maxOccurs="unbounded"&gt;
                 *                &lt;annotation&gt;
                 *                        &lt;documentation&gt;Optional unordered list of valid domain constraints on
                 *                        non-parameter quantities that each apply to this server. The list of
                 *                        required and optional constraints shall be specified in the Implementation
                 *                        Specification for this service. &lt;/documentation&gt;
                 *                &lt;/annotation&gt;
                 *                &lt;/element&gt;
                 *                &lt;element ref="ows:ExtendedCapabilities" minOccurs="0"/&gt;
                 *        &lt;/sequence&gt;
                 *&lt;/complexType&gt;
                 * </pre>
                 * </p>
                 *
                 */
            void operationsMetadata() {
                start("ows:OperationsMetadata");

                getCapabilities();
                describeFeatureType();
                getFeature();

                if (wfs.getServiceLevel() == WFS.COMPLETE) {
                    lockFeature();
                    getFeatureWithLock();
                }

                if (wfs.getServiceLevel() >= WFS.TRANSACTIONAL) {
                    transaction();
                }

                end("ows:OperationsMetadata");
            }

            /**
                 * Encodes the GetCapabilities ows:Operation element.
                 *
                 */
            void getCapabilities() {
                Map.Entry[] parameters = new Map.Entry[] {
                        parameter("AcceptVersions", new String[] { "1.0.0", "1.1.0" }),
                        parameter("AcceptFormats", new String[] { "text/xml" })
                    //    				parameter( 
                    //    					"Sections", 
                    //    					new String[]{ 
                    //    						"ServiceIdentification", "ServiceProvider", "OperationsMetadata",
                    //    						"FeatureTypeList", "ServesGMLObjectTypeList", "SupportsGMLObjectTypeList", 
                    //    						"Filter_Capabilities"
                    //    					} 
                    //    				) 
                    };
                operation("GetCapabilities", parameters, true, true);
            }

            /**
                 * Encodes the DescribeFeatureType ows:Operation element.
                 */
            void describeFeatureType() {
                //TODO: process extension point
                Map.Entry[] parameters = new Map.Entry[] {
                        parameter("outputFormat", new String[] { "text/gml; subtype=gml/3.1.1" })
                    };

                operation("DescribeFeatureType", parameters, true, true);
            }

            /**
                 * Encodes the GetFeature ows:Operation element.
                 */
            void getFeature() {
                Map.Entry[] parameters = new Map.Entry[] {
                        parameter("resultType", new String[] { "results", "hits" }),
                        parameter("outputFormat", new String[] { "text/gml; subtype=gml/3.1.1" })
                    };

                operation("GetFeature", parameters, true, true);
            }

            /**
                 * Encodes the GetFeatureWithLock ows:Operation element.
                 */
            void getFeatureWithLock() {
                Map.Entry[] parameters = new Map.Entry[] {
                        parameter("resultType", new String[] { "results", "hits" }),
                        parameter("outputFormat", new String[] { "text/gml; subtype=gml/3.1.1" })
                    };

                operation("GetFeatureWithLock", parameters, true, true);
            }

            /**
                 * Encodes the LockFeature ows:Operation element.
                 */
            void lockFeature() {
                Map.Entry[] parameters = new Map.Entry[] {
                        parameter("releaseAction", new String[] { "ALL", "SOME" })
                    };

                operation("LockFeature", parameters, true, true);
            }

            /**
                 * Encodes the Transaction ows:Operation element.
                 */
            void transaction() {
                Map.Entry[] parameters = new Map.Entry[] {
                        parameter("inputFormat", new String[] { "text/gml; subtype=gml/3.1.1" }),
                        parameter("idgen",
                            new String[] { "GenerateNew", "UseExisting", "ReplaceDuplicate" }),
                        parameter("releaseAction", new String[] { "ALL", "SOME" })
                    };

                operation("Transaction", parameters, true, true);
            }

            /**
                 * Encdoes the wfs:FeatureTypeList element.
                 *<p>
                 *<pre>
                 * &lt;xsd:complexType name="FeatureTypeListType"&gt;
                 *      &lt;xsd:annotation&gt;
                 *         &lt;xsd:documentation&gt;
                 *            A list of feature types available from  this server.
                 *         &lt;/xsd:documentation&gt;
                 *      &lt;/xsd:annotation&gt;
                 *      &lt;xsd:sequence&gt;
                 *         &lt;xsd:element name="Operations"
                 *                      type="wfs:OperationsType"
                 *                      minOccurs="0"/&gt;
                 *         &lt;xsd:element name="FeatureType"
                 *                      type="wfs:FeatureTypeType"
                 *                      maxOccurs="unbounded"/&gt;
                 *      &lt;/xsd:sequence&gt;
                 *   &lt;/xsd:complexType&gt;
                 *</pre>
                 *</p>
                 */
            void featureTypeList() {
                start("FeatureTypeList");

                start("Operations");

                if ((wfs.getServiceLevel() | WFS.SERVICE_BASIC) != 0) {
                    element("Operation", "Query");
                }

                if ((wfs.getServiceLevel() | WFS.SERVICE_INSERT) != 0) {
                    element("Operation", "Insert");
                }

                if ((wfs.getServiceLevel() | WFS.SERVICE_UPDATE) != 0) {
                    element("Operation", "Update");
                }

                if ((wfs.getServiceLevel() | WFS.SERVICE_DELETE) != 0) {
                    element("Operation", "Delete");
                }

                if ((wfs.getServiceLevel() | WFS.SERVICE_LOCKING) != 0) {
                    element("Operation", "Lock");
                }

                end("Operations");

                List featureTypes = new ArrayList(catalog.getFeatureTypeInfos().values());
                Collections.sort(featureTypes, new FeatureTypeInfoTitleComparator());
                for (Iterator i = featureTypes.iterator(); i.hasNext();) {
                    FeatureTypeInfo featureType = (FeatureTypeInfo) i.next();
                    featureType(featureType);
                }

                end("FeatureTypeList");
            }

            /**
                 * Encodes the wfs:FeatureType element.
                 * <p>
                 *         <pre>
                 * &lt;xsd:complexType name="FeatureTypeType"&gt;
                 *      &lt;xsd:annotation&gt;
                 *         &lt;xsd:documentation&gt;
                 *            An element of this type that describes a feature in an application
                 *            namespace shall have an xml xmlns specifier, e.g.
                 *            xmlns:bo="http://www.BlueOx.org/BlueOx"
                 *         &lt;/xsd:documentation&gt;
                 *      &lt;/xsd:annotation&gt;
                 *      &lt;xsd:sequence&gt;
                 *         &lt;xsd:element name="Name" type="xsd:QName"&gt;
                 *            &lt;xsd:annotation&gt;
                 *               &lt;xsd:documentation&gt;
                 *                  Name of this feature type, including any namespace prefix
                 *               &lt;/xsd:documentation&gt;
                 *            &lt;/xsd:annotation&gt;
                 *         &lt;/xsd:element&gt;
                 *         &lt;xsd:element name="Title" type="xsd:string"&gt;
                 *            &lt;xsd:annotation&gt;
                 *               &lt;xsd:documentation&gt;
                 *                  Title of this feature type, normally used for display
                 *                  to a human.
                 *               &lt;/xsd:documentation&gt;
                 *            &lt;/xsd:annotation&gt;
                 *         &lt;/xsd:element&gt;
                 *         &lt;xsd:element name="Abstract" type="xsd:string" minOccurs="0"&gt;*            &lt;xsd:annotation&gt;
                 *               &lt;xsd:documentation&gt;
                 *                  Brief narrative description of this feature type, normally*                  used for display to a human.
                 *               &lt;/xsd:documentation&gt;
                 *            &lt;/xsd:annotation&gt;
                 *         &lt;/xsd:element&gt;
                 *         &lt;xsd:element ref="ows:Keywords" minOccurs="0" maxOccurs="unbounded"/&gt;
                 *         &lt;xsd:choice&gt;
                 *            &lt;xsd:sequence&gt;
                 *               &lt;xsd:element name="DefaultSRS"
                 *                            type="xsd:anyURI"&gt;
                 *                  &lt;xsd:annotation&gt;
                 *                     &lt;xsd:documentation&gt;
                 *                        The DefaultSRS element indicated which spatial
                 *                        reference system shall be used by a WFS to
                 *                        express the state of a spatial feature if not
                 *                        otherwise explicitly identified within a query
                 *                        or transaction request.  The SRS may be indicated
                 *                        using either the EPSG form (EPSG:posc code) or
                 *                        the URL form defined in subclause 4.3.2 of
                 *                        refernce[2].
                 *                     &lt;/xsd:documentation&gt;
                 *                  &lt;/xsd:annotation&gt;
                 *               &lt;/xsd:element&gt;
                 *               &lt;xsd:element name="OtherSRS"
                 *                            type="xsd:anyURI"
                 *                            minOccurs="0" maxOccurs="unbounded"&gt;
                 *                  &lt;xsd:annotation&gt;
                 *                     &lt;xsd:documentation&gt;
                 *                        The OtherSRS element is used to indicate other
                 *                        supported SRSs within query and transaction
                 *                        operations.  A supported SRS means that the
                 *                        WFS supports the transformation of spatial
                 *                        properties between the OtherSRS and the internal
                 *                        storage SRS.  The effects of such transformations
                 *                        must be considered when determining and declaring
                 *                        the guaranteed data accuracy.
                 *                     &lt;/xsd:documentation&gt;
                 *                  &lt;/xsd:annotation&gt;
                 *               &lt;/xsd:element&gt;
                 *            &lt;/xsd:sequence&gt;
                 *            &lt;xsd:element name="NoSRS"&gt;
                 *              &lt;xsd:complexType/&gt;
                 *            &lt;/xsd:element&gt;
                 *         &lt;/xsd:choice&gt;
                 *         &lt;xsd:element name="Operations"
                 *                      type="wfs:OperationsType"
                 *                      minOccurs="0"/&gt;
                 *         &lt;xsd:element name="OutputFormats"
                 *                      type="wfs:OutputFormatListType"
                 *                      minOccurs="0"/&gt;
                 *         &lt;xsd:element ref="ows:WGS84BoundingBox"
                 *                      minOccurs="1" maxOccurs="unbounded"/&gt;
                 *         &lt;xsd:element name="MetadataURL"
                 *                      type="wfs:MetadataURLType"
                 *                      minOccurs="0" maxOccurs="unbounded"/&gt;
                 *      &lt;/xsd:sequence&gt;
                 *   &lt;/xsd:complexType&gt;
                 *         </pre>
                 * </p>
                 * @param featureType
                 */
            void featureType(FeatureTypeInfo featureType) {
                String prefix = featureType.getNameSpace().getPrefix();
                String uri = featureType.getNameSpace().getURI();

                start("FeatureType", attributes(new String[] { "xmlns:" + prefix, uri }));

                element("Name", featureType.getName());
                element("Title", featureType.getTitle());
                element("Abstract", featureType.getAbstract());
                keywords(featureType.getKeywords());

                //default srs
                element("DefaultSRS", "urn:x-ogc:def:crs:EPSG:6.11.2:" + featureType.getSRS());

                //TODO: other srs's
                start("OutputFormats");

                Collection featureProducers = GeoServerExtensions.extensions(WFSGetFeatureOutputFormat.class);
                for (Iterator i = featureProducers.iterator(); i.hasNext();) {
                    WFSGetFeatureOutputFormat format = (WFSGetFeatureOutputFormat) i.next();
                    for ( Iterator f = format.getOutputFormats().iterator(); f.hasNext(); ) {
                        element( "Format", f.next().toString() );
                    }
                }
                    
                end("OutputFormats");

                Envelope bbox = null;

                try {
                    bbox = featureType.getLatLongBoundingBox();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                start("ows:WGS84BoundingBox");

                element("ows:LowerCorner", bbox.getMinX() + " " + bbox.getMinY());
                element("ows:UpperCorner", bbox.getMaxX() + " " + bbox.getMaxY());

                end("ows:WGS84BoundingBox");

                end("FeatureType");
            }

            /**
                 * Encodes the wfs:SupportsGMLObjectTypeList element.
                 *        <p>
                 *        <pre>
                 *&lt;xsd:complexType name="GMLObjectTypeListType"&gt;
                 *        &lt;xsd:sequence&gt;
                 *             &lt;xsd:element name="GMLObjectType" type="wfs:GMLObjectTypeType"
                 *               maxOccurs="unbounded"&gt;
                 *                &lt;xsd:annotation&gt;
                 *                   &lt;xsd:documentation&gt;
                 *                      Name of this GML object type, including any namespace prefix
                 *                   &lt;/xsd:documentation&gt;
                 *                &lt;/xsd:annotation&gt;
                 *             &lt;/xsd:element&gt;
                 *          &lt;/xsd:sequence&gt;
                 * &lt;/xsd:complexType&gt;
                 *        </pre>
                 *        </p>
                 */
            void supportsGMLObjectTypeList() {
                element("SupportsGMLObjectTypeList", null);
            }

            /**
                 * Encodes the ogc:Filter_Capabilities element.
                 * <p>
                 * <pre>
                 * *&lt;xsd:element name="Filter_Capabilities"&gt;
                 *      &lt;xsd:complexType&gt;
                 *         &lt;xsd:sequence&gt;
                 *            &lt;xsd:element name="Spatial_Capabilities"
                 *                         type="ogc:Spatial_CapabilitiesType"/&gt;
                 *            &lt;xsd:element name="Scalar_Capabilities"
                 *                         type="ogc:Scalar_CapabilitiesType"/&gt;
                 *            &lt;xsd:element name="Id_Capabilities"
                 *                         type="ogc:Id_CapabilitiesType"/&gt;
                 *         &lt;/xsd:sequence&gt;
                 *      &lt;/xsd:complexType&gt;
                 *   &lt;/xsd:element&gt;
                 * </pre>
                 * </p>
                 *
                 */
            void filterCapabilities() {
                start("ogc:Filter_Capabilities");

                start("ogc:Spatial_Capabilities");

                start("ogc:GeometryOperands");
                element("ogc:GeometryOperand", "gml:Envelope");
                element("ogc:GeometryOperand", "gml:Point");
                element("ogc:GeometryOperand", "gml:LineString");
                element("ogc:GeometryOperand", "gml:Polygon");
                end("ogc:GeometryOperands");

                start("ogc:SpatialOperators");
                element("ogc:SpatialOperator", null, attributes(new String[] { "name", "Disjoint" }));
                element("ogc:SpatialOperator", null, attributes(new String[] { "name", "Equals" }));
                element("ogc:SpatialOperator", null, attributes(new String[] { "name", "DWithin" }));
                element("ogc:SpatialOperator", null, attributes(new String[] { "name", "Beyond" }));
                element("ogc:SpatialOperator", null,
                    attributes(new String[] { "name", "Intersects" }));
                element("ogc:SpatialOperator", null, attributes(new String[] { "name", "Touches" }));
                element("ogc:SpatialOperator", null, attributes(new String[] { "name", "Crosses" }));
                element("ogc:SpatialOperator", null, attributes(new String[] { "name", "Contains" }));
                element("ogc:SpatialOperator", null, attributes(new String[] { "name", "Overlaps" }));
                element("ogc:SpatialOperator", null, attributes(new String[] { "name", "BBOX" }));
                end("ogc:SpatialOperators");

                end("ogc:Spatial_Capabilities");

                start("ogc:Scalar_Capabilities");

                element("ogc:LogicalOperators", null);

                start("ogc:ComparisonOperators");
                element("ogc:ComparisonOperator", "LessThan");
                element("ogc:ComparisonOperator", "GreaterThan");
                element("ogc:ComparisonOperator", "LessThanEqualTo");
                element("ogc:ComparisonOperator", "GreaterThanEqualTo");
                element("ogc:ComparisonOperator", "EqualTo");
                element("ogc:ComparisonOperator", "NotEqualTo");
                element("ogc:ComparisonOperator", "Like");
                element("ogc:ComparisonOperator", "Between");
                element("ogc:ComparisonOperator", "NullCheck");
                end("ogc:ComparisonOperators");

                start("ogc:ArithmeticOperators");
                element("ogc:SimpleArithmetic", null);

                functions();

                end("ogc:ArithmeticOperators");

                end("ogc:Scalar_Capabilities");

                start("ogc:Id_Capabilities");
                element("ogc:FID", null);
                element("ogc:EID", null);
                end("ogc:Id_Capabilities");

                end("ogc:Filter_Capabilities");
            }

            void functions() {
                start("ogc:Functions");

                Iterator itr = FactoryRegistry.lookupProviders(Function.class);

                if (itr.hasNext()) {
                    start("ogc:FunctionNames");

                    SortedSet sortedFunctions = new TreeSet(new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    String n1 = ((Function) o1).getName();
                                    String n2 = ((Function) o2).getName();

                                    return n1.toLowerCase().compareTo(n2.toLowerCase());
                                }
                            });

                    while (itr.hasNext()) {
                        sortedFunctions.add(itr.next());
                    }

                    //write them now that functions are sorted by name
                    itr = sortedFunctions.iterator();

                    while (itr.hasNext()) {
                        Function fe = (Function) itr.next();
                        String name = fe.getName();
                        int nargs = fe.getParameters().size();

                        element("ogc:FunctionName", name,
                            attributes(new String[] { "nArgs", "" + nargs }));
                    }

                    end("ogc:FunctionNames");
                }

                end("ogc:Functions");
            }

            /**
                 * Encodes the ows:Keywords element.
                 * <p>
                 * <pre>
                 * &lt;complexType name="KeywordsType"&gt;
                 *     &lt;annotation&gt;
                 *          &lt;documentation&gt;Unordered list of one or more commonly used or formalised word(s) or phrase(s) used to describe the subject. When needed, the optional "type" can name the type of the associated list of keywords that shall all have the same type. Also when needed, the codeSpace attribute of that "type" can reference the type name authority and/or thesaurus. &lt;/documentation&gt;
                 *          &lt;documentation&gt;For OWS use, the optional thesaurusName element was omitted as being complex information that could be referenced by the codeSpace attribute of the Type element. &lt;/documentation&gt;
                 *     &lt;/annotation&gt;
                 *     &lt;sequence&gt;
                 *          &lt;element name="Keyword" type="string" maxOccurs="unbounded"/&gt;
                 *          &lt;element name="Type" type="ows:CodeType" minOccurs="0"/&gt;
                 *     &lt;/sequence&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * </p>
                 * @param keywords
                 */
            void keywords(String[] keywords) {
                if ((keywords == null) || (keywords.length == 0)) {
                    return;
                }

                start("ows:Keywords");

                for (int i = 0; i < keywords.length; i++) {
                    element("ows:Keyword", keywords[i]);
                }

                end("ows:Keywords");
            }

            void keywords(List keywords) {
                keywords((String[]) keywords.toArray(new String[keywords.size()]));
            }

            /**
                 * Encodes the ows:Operation element.
                 * <p>
                 * <pre>
                 * &lt;complexType&gt;
                 *      &lt;sequence&gt;
                 *        &lt;element ref="ows:DCP" maxOccurs="unbounded"&gt;
                 *          &lt;annotation&gt;
                 *            &lt;documentation&gt;Unordered list of Distributed Computing Platforms (DCPs) supported for this operation. At present, only the HTTP DCP is defined, so this element will appear only once. &lt;/documentation&gt;
                 *          &lt;/annotation&gt;
                 *        &lt;/element&gt;
                 *        &lt;element name="Parameter" type="ows:DomainType" minOccurs="0" maxOccurs="unbounded"&gt;
                 *          &lt;annotation&gt;
                 *            &lt;documentation&gt;Optional unordered list of parameter domains that each apply to this operation which this server implements. If one of these Parameter elements has the same "name" attribute as a Parameter element in the OperationsMetadata element, this Parameter element shall override the other one for this operation. The list of required and optional parameter domain limitations for this operation shall be specified in the Implementation Specification for this service. &lt;/documentation&gt;
                 *          &lt;/annotation&gt;
                 *        &lt;/element&gt;
                 *        &lt;element name="Constraint" type="ows:DomainType" minOccurs="0" maxOccurs="unbounded"&gt;
                 *          &lt;annotation&gt;
                 *            &lt;documentation&gt;Optional unordered list of valid domain constraints on non-parameter quantities that each apply to this operation. If one of these Constraint elements has the same "name" attribute as a Constraint element in the OperationsMetadata element, this Constraint element shall override the other one for this operation. The list of required and optional constraints for this operation shall be specified in the Implementation Specification for this service. &lt;/documentation&gt;
                 *          &lt;/annotation&gt;
                 *        &lt;/element&gt;
                 *        &lt;element ref="ows:Metadata" minOccurs="0" maxOccurs="unbounded"&gt;
                 *          &lt;annotation&gt;
                 *            &lt;documentation&gt;Optional unordered list of additional metadata about this operation and its' implementation. A list of required and optional metadata elements for this operation should be specified in the Implementation Specification for this service. (Informative: This metadata might specify the operation request parameters or provide the XML Schemas for the operation request.) &lt;/documentation&gt;
                 *          &lt;/annotation&gt;
                 *        &lt;/element&gt;
                 *      &lt;/sequence&gt;
                 *      &lt;attribute name="name" type="string" use="required"&gt;
                 *        &lt;annotation&gt;
                 *          &lt;documentation&gt;Name or identifier of this operation (request) (for example, GetCapabilities). The list of required and optional operations implemented shall be specified in the Implementation Specification for this service. &lt;/documentation&gt;
                 *        &lt;/annotation&gt;
                 *      &lt;/attribute&gt;
                 *    &lt;/complexType&gt;
                 * </pre>
                 * </p>
                 *
                 * @param name
                 * @param parameters
                 * @param get
                 * @param post
                 */
            void operation(String name, Map.Entry[] parameters, boolean get, boolean post) {
                start("ows:Operation", attributes(new String[] { "name", name }));

                //dcp
                start("ows:DCP");
                start("ows:HTTP");

                String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(request.getBaseUrl(), wfs.getGeoServer().getProxyBaseUrl());

                if (proxifiedBaseUrl.endsWith("?")) {
                    proxifiedBaseUrl = proxifiedBaseUrl.substring(0, proxifiedBaseUrl.length() - 1);
                }

                if (proxifiedBaseUrl.indexOf('?') != -1) {
                    proxifiedBaseUrl = proxifiedBaseUrl.substring(0, proxifiedBaseUrl.indexOf('?'));
                }

                if (get) {
                    element("ows:Get", null, attributes(new String[] { "xlink:href", ResponseUtils.appendPath(proxifiedBaseUrl, "wfs?") }));
                }

                if (post) {
                    element("ows:Post", null, attributes(new String[] { "xlink:href", ResponseUtils.appendPath(proxifiedBaseUrl, "wfs?") }));
                }

                end("ows:HTTP");
                end("ows:DCP");

                //parameters
                for (int i = 0; i < parameters.length; i++) {
                    String pname = (String) parameters[i].getKey();
                    String[] pvalues = (String[]) parameters[i].getValue();

                    start("ows:Parameter", attributes(new String[] { "name", pname }));

                    for (int j = 0; j < pvalues.length; j++) {
                        element("ows:Value", pvalues[j]);
                    }

                    end("ows:Parameter");
                }

                end("ows:Operation");
            }

            AttributesImpl attributes(String[] nameValues) {
                AttributesImpl atts = new AttributesImpl();

                for (int i = 0; i < nameValues.length; i += 2) {
                    String name = nameValues[i];
                    String valu = nameValues[i + 1];

                    atts.addAttribute(null, null, name, null, valu);
                }

                return atts;
            }

            Map.Entry parameter(final String name, final String[] values) {
                return new Map.Entry() {
                        public Object getKey() {
                            return name;
                        }

                        public Object getValue() {
                            return values;
                        }

                        public Object setValue(Object value) {
                            return null;
                        }
                    };
            }
        }
    }
}

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geotools.catalog.GeoResource;
import org.geotools.catalog.GeoResourceInfo;
import org.geotools.feature.FeatureType;
import org.geotools.filter.FunctionExpression;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Based on the <code>org.geotools.xml.transform</code> framework, does the job
 * of encoding a WFS 1.0 Capabilities document.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @author Chris Holmes
 * @version $Id
 */
public class WFSCapsTransformer extends TransformerBase {
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(WFSCapsTransformer.class.getPackage()
                                                                                  .getName());
    /** identifer of a http get request ! */
    private static final String HTTP_GET = "Get";

    /** DOCUMENT ME! */
    private static final String HTTP_POST = "Post";

    /** DOCUMENT ME! */
    protected static final String WFS_URI = "http://www.opengis.net/wfs";

    /** DOCUMENT ME! */
    protected static final String CUR_VERSION = "1.0.0";

    /** DOCUMENT ME! */
    protected static final String XSI_PREFIX = "xsi";

    /** DOCUMENT ME! */
    protected static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";

    /** wfs service */
    private WFS wfs;
    
    /** catalog */
    private GeoServerCatalog catalog;
    
    /**
     * Creates a new WFSCapsTransformer object.
     */
    public WFSCapsTransformer(WFS wfs, GeoServerCatalog catalog) {
        super();
        setNamespaceDeclarationEnabled(false);
        
        this.wfs = wfs;
        this.catalog = catalog;
    }

    /**
     * DOCUMENT ME!
     *
     * @param handler DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Translator createTranslator(ContentHandler handler) {
        return new WFSCapsTranslator(handler);
    }

    /**
     * DOCUMENT ME!
     *
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id
     */
    private  class WFSCapsTranslator extends TranslatorSupport {
        /** epsg identifier!  */
        private static final String EPSG = "EPSG:";

        /**
         * Creates a new WFSCapsTranslator object.
         *
         * @param handler DOCUMENT ME!
         */
        public WFSCapsTranslator(ContentHandler handler) {
            super(handler, null, null);
        }

        /**
         * Encode the object.
         *
         * @param o The Object to encode.
         *
         * @throws IllegalArgumentException if the Object is not encodeable.
         */
        public void encode(Object o) throws IllegalArgumentException {
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "version", "version", "", CUR_VERSION);
            attributes.addAttribute("", "xmlns", "xmlns", "", WFS_URI);

            NamespaceSupport namespaces = catalog.getNamespaceSupport();
            for ( Enumeration p = namespaces.getPrefixes(); p.hasMoreElements(); ) {
            		String prefix = (String) p.nextElement();
            		String uri = namespaces.getURI( prefix );
            		
                String prefixDef = "xmlns:" + prefix;
                
                attributes.addAttribute("", prefixDef, prefixDef, "", uri);
            }

            attributes.addAttribute("", "xmlns:ogc", "xmlns:ogc", "",
                "http://www.opengis.net/ogc");

            String prefixDef = "xmlns:" + XSI_PREFIX;
            attributes.addAttribute("", prefixDef, prefixDef, "", XSI_URI);

            String locationAtt = XSI_PREFIX + ":schemaLocation";
            //JD: need to deal with this
//            String locationDef = WFS_URI + " " + request.getSchemaBaseUrl()
//                + "wfs/1.0.0/" + "WFS-capabilities.xsd"; //djb: this was pointing to the wrong location
//            attributes.addAttribute("", locationAtt, locationAtt, "",
//                locationDef);
            //JD
            
            start("WFS_Capabilities", attributes);

            handleService();
            handleCapability();
            handleFeatureTypes();
            handleFilterCapabilities();

            end("WFS_Capabilities");
        }

        /**
         * DOCUMENT ME!
         */
        private void handleService() {
            
            start("Service");
            element("Name", wfs.getName());
            element("Title", wfs.getTitle());
            element("Abstract", wfs.getAbstract());

            handleKeywords(wfs.getKeywords());

            URL or = wfs.getOnlineResource();
            element("OnlineResource", (or == null) ? "" : or.toExternalForm());
            element("Fees", wfs.getFees());
            element("AccessConstraints", wfs.getAccessConstraints());
            end("Service");
        }

        /**
         * DOCUMENT ME!
         *
         * @param kwlist
         */
        private void handleKeywords(String[] kwlist) {
        		if ( kwlist == null ) {
        			handleKeywords( (List) null );
        		}
        		else { 
        			handleKeywords( Arrays.asList( kwlist ) );
        		}
    		}

        private void handleKeywords( List kwlist ) {
        	
        		StringBuffer kwds = new StringBuffer();

            for (int i = 0; kwlist != null && i < kwlist.size(); i++) {
                kwds.append(kwlist.get(i));

                if ( i != kwlist.size() - 1) {
                    kwds.append(", ");
                }
            }

            element("Keywords", kwds.toString());	
        }
        
        /**
         * DOCUMENT ME!
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
         * DOCUMENT ME!
         */
        private void handleGetCapabilities() {
            String capName = "GetCapabilities";
            start(capName);
            handleDcpType(capName, HTTP_GET);
            handleDcpType(capName, HTTP_POST);
            end(capName);
        }

        /**
         * DOCUMENT ME!
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
         * DOCUMENT ME!
         */
        private void handleGetFeature() {
            String capName = "GetFeature";
            start(capName);

            String resultFormat = "ResultFormat";
            start(resultFormat);
            
            //DJB: okay, I'm adding all the supported formats
            //     we probably need a "strict cite" conformance option
            //     somewhere in the config files. (default = not scrict)
            //     Strict meaning only have "GML2" in here.
            //     Alternatively, we could have options for "supported output formats"
            //     in the config and people could check off the extras they want
            //     to allow.
            
            //JD: reenable feature response delegate stuff
//             element("GML2", null); // old implementation
            //FeatureResponseDelegateProducerSpi spi;
 

	        	//DJB: (see above comment, this fixes it0
	        	//      WFS config now has a "citeConformanceHacks" boolean in it.
	        	//      true --> only publish GML2 in caps file
	        	//      false -> publish all
            //JD: reanable 
            //boolean onlyGML2 = wfs.getCiteConformanceHacks();
            boolean onlyGML2 = true;
            
            if (onlyGML2)
            {
            		element("GML2",null);
            }
            else
	        {
            		//JD: renable
            		//FULL MONTY
//	            Iterator spi_it = FactoryFinder.factories(FeatureResponseDelegateProducerSpi.class);
//	            while (spi_it.hasNext()) 
//	            {
//	                spi = (FeatureResponseDelegateProducerSpi) spi_it.next();
//	                Set formats = spi.getSupportedFormats();
//	                Iterator it =formats.iterator();
//	                while (it.hasNext())
//	                {
//	                	String format = (String) it.next();
//	                	element( format, null);
//	                }
//	            }
	        }

            //So cite does not even like this.  Which is really lame, since it
            //validates according to our definition for now, and because the cite
            //tests have a bunch of other valid types (perhaps from galdos?). 
            //So I think perhaps we should just have it as a capability that 
            //is not advertised in the capabilities section for now.  And we 
            //should try to get the cite team to add it to their section. ch
            //element("GML2-GZIP", null);
            end(resultFormat);

            handleDcpType(capName, HTTP_GET);
            handleDcpType(capName, HTTP_POST);
            end(capName);
        }

        /**
         * DOCUMENT ME!
         */
        private void handleTransaction() {
            String capName = "Transaction";
            start(capName);
            handleDcpType(capName, HTTP_GET);
            handleDcpType(capName, HTTP_POST);
            end(capName);
        }

        /**
         * DOCUMENT ME!
         */
        private void handleLock() {
            String capName = "LockFeature";
            start(capName);
            handleDcpType(capName, HTTP_GET);
            handleDcpType(capName, HTTP_POST);
            end(capName);
        }

        /**
         * DOCUMENT ME!
         */
        private void handleFeatureWithLock() {
            String capName = "GetFeatureWithLock";
            start(capName);
            start("ResultFormat");
            element("GML2", null);
            end("ResultFormat");
            handleDcpType(capName, HTTP_GET);
            handleDcpType(capName, HTTP_POST);
            end(capName);
        }

        /**
         * Encodes a <code>DCPType</code> fragment for HTTP GET and POST
         * methods.
         *
         * @param capabilityName the URL of the onlineresource for HTTP GET
         *        method requests
         * @param httpMethod the URL of the onlineresource for HTTP POST method
         *        requests
         */
        private void handleDcpType(String capabilityName, String httpMethod) {
            
        	//JD: not sure how correct this is
//        		String baseUrl = request.getBaseUrl() + "wfs";
//            String url = null;
//
//            if (request.isDispatchedRequest()) {
//                url = baseUrl + "?";
//            } else {
//                url = baseUrl + "/" + capabilityName + "?";
//            }

        		String url = wfs.getOnlineResource().toString();
        		if ( url.endsWith("?") ) {
        			url = url.substring( 0, url.length()-1 );
        		}
        		
        		if ( HTTP_GET.equals( httpMethod ) ) {
        			url += "?request=" + capabilityName + "&";
        		}
        		else if ( HTTP_POST.equals( httpMethod ) ) {
        			url += "/" + capabilityName + "?";
        		}
        		
            start("DCPType");
            start("HTTP");

            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "onlineResource", "onlineResource", "", url);
            element(httpMethod, null, atts);

            end("HTTP");
            end("DCPType");
        }

        /**
         * DOCUMENT ME!
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

            List featureTypes;
			try {
				featureTypes = catalog.featureTypes();
			} 
			catch (IOException e) {
				throw new RuntimeException( e );
			}
            
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
         * @param ftype The FeatureType configuration to report capabilities
         *        on.
         *
         * @throws RuntimeException For any errors.
         */
        protected void handleFeatureType( FeatureTypeInfo info ) {
            
            Envelope bbox = null;

            try {
				bbox = info.latLongBoundingBox();
			} 
            catch (IOException e) {
            		String msg = "Could not calculate bbox for: " + info.name();
				LOGGER.log( Level.SEVERE, msg, e );
            		return ;
			}
                
            start("FeatureType");
            element("Name", info.name());
            element("Title", info.getTitle());
            element("Abstract", info.getAbstract());
            handleKeywords(info.getKeywords());

            /**
             * @task REVISIT: should getSRS() return the full URL?
             */
            //JD: pretty sure this is wrong
            element("SRS", EPSG + info.crs().getName().getCode());

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
         * DOCUMENT ME!
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
            
            handleFunctions(ogc);  //djb: list functions
            
            end(ogc + "Arithmetic_Operators");
            end(ogc + "Scalar_Capabilities");
            end(ogc + "Filter_Capabilities");
        }
        
        public  void handleFunctions(String prefix)
        {
        	 start(prefix +"Functions");
        	 start(prefix +"Function_Names");
        	 java.util.Iterator it = org.geotools.factory.FactoryFinder.factories(FunctionExpression.class);
             //Sort them up for easier visual inspection
        	 SortedSet sortedFunctions = new TreeSet(new Comparator(){
            	 	public int compare(Object o1, Object o2){
                        String n1 = ((FunctionExpression) o1).getName();
                        String n2 = ((FunctionExpression) o2).getName();
                        return n1.toLowerCase().compareTo(n2.toLowerCase());
            	 	}
             });
             while (  it.hasNext()   )
             {
            	sortedFunctions.add(it.next()); 
             }
             
             //write them now that functions are sorted by name
             FunctionExpression exp = null;
             it = sortedFunctions.iterator();
             while (  it.hasNext()   )
             {
                 FunctionExpression fe = (FunctionExpression) it.next();
                 String funName = fe.getName();
                 int    funNArgs= fe.getArgCount();
                 
                 AttributesImpl atts = new AttributesImpl();
                 atts.addAttribute("", "nArgs", "nArgs", "", funNArgs+"");
                 
                 element(prefix +"Function_Name",funName,atts);              
             }
             
             end(prefix +"Function_Names");
             end(prefix +"Functions");
        }
        
    }
    
 
}

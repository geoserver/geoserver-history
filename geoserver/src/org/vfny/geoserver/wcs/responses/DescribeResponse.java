package org.vfny.geoserver.wcs.responses;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.JTS;
import org.geotools.referencing.FactoryFinder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.OperationNotFoundException;
import org.opengis.referencing.operation.TransformException;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.requests.DescribeRequest;

import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class DescribeResponse implements Response {
	private static final Logger LOGGER = Logger.getLogger(
	"org.vfny.geoserver.responses");
	
	private static final String CURR_VER = "\"1.0.0\"";
	private static final String WCS_URL = "http://www.opengis.net/wcs";
	private static final String WCS_NAMESPACE = "\n  xmlns=\"" + WCS_URL + "\"";
	private static final String XLINK_URL = "\"http://www.w3.org/1999/xlink\"";
	private static final String XLINK_NAMESPACE = "\n  xmlns:xlink=" + XLINK_URL;
	private static final String OGC_URL = "\"http://www.opengis.net/ogc\"";
	private static final String OGC_NAMESPACE = "\n  xmlns:ogc=" + OGC_URL;
	private static final String GML_URL = "\"http://www.opengis.net/gml\"";
	private static final String GML_NAMESPACE = "\n  xmlns:gml=" + GML_URL;
	private static final String SCHEMA_URI = "\"http://www.w3.org/2001/XMLSchema-instance\"";
	private static final String XSI_NAMESPACE = "\n  xmlns:xsi=" + SCHEMA_URI;

	
	/** Fixed return footer information */
	private static final String FOOTER = "\n</CoverageDescription>";
	private DescribeRequest request;
	
	/** Main XML class for interpretation and response. */
	private String xmlResponse = new String();
	
	
    /**
     * The default datum factory.
     */
    protected  final DatumFactory datumFactory = FactoryFinder.getDatumFactory();

    /**
     * The default coordinate reference system factory.
     */
    protected  final static CRSFactory crsFactory = FactoryFinder.getCRSFactory();

    /**
     * The default math transform factory.
     */
    protected  final MathTransformFactory mtFactory = FactoryFinder.getMathTransformFactory();

    /**
     * The default transformations factory.
     */
    protected  final static CoordinateOperationFactory opFactory = FactoryFinder.getCoordinateOperationFactory();

	public void execute(Request request) throws WcsException {
		if (!(request instanceof DescribeRequest)) {
			throw new WcsException(
					"illegal request type, expected DescribeRequest, got "
					+ request);
		}
		
		DescribeRequest wcsRequest = (DescribeRequest) request;
		this.request = wcsRequest;
		LOGGER.finer("processing describe request" + wcsRequest);
		
		String outputFormat = wcsRequest.getOutputFormat();
		
		if (!outputFormat.equalsIgnoreCase("XMLSCHEMA")) {
			throw new WcsException("output format: " + outputFormat + " not "
					+ "supported by geoserver");
		}
		
		// generates response, using general function
		xmlResponse = generateCoverages(wcsRequest);
		
		if (!request.getWCS().getGeoServer().isVerbose()) {
			xmlResponse = xmlResponse.replaceAll(">\n[ \\t\\n]*", ">");
			xmlResponse = xmlResponse.replaceAll("\n[ \\t\\n]*", " ");
		}
	}
	
	public String getContentType(GeoServer gs) {
		return gs.getMimeType();
	}
	
	public String getContentEncoding() {
		return null;
	}
	
	public void writeTo(OutputStream out) throws WcsException {
		try {
			byte[] content = xmlResponse.getBytes();
			out.write(content);
		} catch (IOException ex) {
			throw new WcsException(ex, "", getClass().getName());
		}
	}
	
	private final String generateCoverages(DescribeRequest wcsRequest)
	throws WcsException {
		List requestedTypes = wcsRequest.getCoverages();
		
		// Initialize return information and intermediate return objects
		StringBuffer tempResponse = new StringBuffer();
		
		//ComplexType table = new ComplexType();
		if (requestedTypes.size() == 0) {
			//if there are no specific requested types then get all.
			requestedTypes = new ArrayList(wcsRequest.getWCS().getData()
					.getCoverageInfos()
					.keySet());
		}
		
		tempResponse.append("<?xml version=\"1.0\" encoding=\""
				+ wcsRequest.getGeoServer().getCharSet().displayName() + "\"?>"
				+ "\n<CoverageDescription version=" + CURR_VER + " ");
		
		tempResponse.append(WCS_NAMESPACE);
		tempResponse.append(XLINK_NAMESPACE);
		tempResponse.append(OGC_NAMESPACE);
		tempResponse.append(GML_NAMESPACE);
		tempResponse.append(XSI_NAMESPACE);
		tempResponse.append(" xsi:schemaLocation=\"" + WCS_URL + " " + request.getSchemaBaseUrl()
				+ "wcs/1.0.0/describeCoverage.xsd\">\n\n");

		tempResponse.append(generateSpecifiedCoverages(requestedTypes,
				wcsRequest.getWCS()));
		
		tempResponse.append(FOOTER);
		
		return tempResponse.toString();
	}
	
	private String generateSpecifiedCoverages(List requestedTypes, WCS gs)
	throws WcsException {
		String tempResponse = new String();
		String curCoverageName = new String();
		
		for (int i = 0; i < requestedTypes.size(); i++) {
			curCoverageName = requestedTypes.get(i).toString();
			
			CoverageInfo meta = gs.getData().getCoverageInfo(curCoverageName);
			
			if (meta == null) {
				throw new WcsException("Coverage " + curCoverageName + " does "
						+ "not exist on this server");
			}
			
			tempResponse = tempResponse + printElement(meta);
		}
		
		tempResponse = tempResponse + "\n\n";
		
		return tempResponse;
	}
	
	private static String printElement(CoverageInfo cv) {
		StringBuffer tempResponse = new StringBuffer();

		tempResponse.append("\n <CoverageOffering>");
			if( cv.getMetadataLink() != null ) {
				tempResponse.append("\n  <metadataLink about=\"" + cv.getMetadataLink().getAbout() 
						+ "\" metadataType=\"" + cv.getMetadataLink().getMetadataType() + "\"/>");				
			}
			
			String tmp = cv.getDescription();
			if( (tmp != null) && (tmp != "") ) {
				tempResponse.append("\n  <description>" + tmp + "</description>");
			}
			
			tmp = cv.getName();
			if( (tmp != null) && (tmp != "") ) {
				tempResponse.append("\n  <name>" + tmp + "</name>");
			}

			tmp = cv.getLabel();
			if( (tmp != null) && (tmp != "") ) {
				tempResponse.append("\n  <label>" + tmp + "</label>");
			}

			try {
				if( !cv.getCrs().getName().getCode().equalsIgnoreCase("WGS 84")) {
					final CoordinateReferenceSystem targetCRS = crsFactory.createFromWKT(
				    		"GEOGCS[\"WGS 84\",\n" 								 + 
				    		"DATUM[\"WGS_1984\",\n"								 + 
				    		"  SPHEROID[\"WGS 84\",\n" 							 + 
				    		"    6378137.0, 298.257223563,\n" 					 + 
				    		"    AUTHORITY[\"EPSG\",\"7030\"]],\n" 				 +
				    		"  AUTHORITY[\"EPSG\",\"6326\"]],\n"				 + 
				    		"  PRIMEM[\"Greenwich\", 0.0,\n" 					 +
				    		"    AUTHORITY[\"EPSG\",\"8901\"]],\n"				 + 
				    		"  UNIT[\"degree\", 0.017453292519943295],\n"		 + 
				    		"  AXIS[\"Lon\", EAST],\n"							 +
				    		"  AXIS[\"Lat\", NORTH],\n"							 +
				    		"AUTHORITY[\"EPSG\",\"4326\"]]");
					
					
				    final CoordinateReferenceSystem sourceCRS = cv.getCrs();
				    
				    final CoordinateOperation operation = opFactory.createOperation(sourceCRS, targetCRS);

				    MathTransform2D mathTransform = (MathTransform2D) operation.getMathTransform();
				    
				    if( mathTransform instanceof AffineTransform ) {
				    	Envelope envelope = cv.getEnvelope();
				    	Envelope targetEnvelope = JTS.transform(envelope, mathTransform);

				    	tempResponse.append("\n  <lonLatEnvelope" 
								+ " srsName=\"WGS84(DD)\""
								+">");
						tempResponse.append("\n   <gml:pos>" 
								+ targetEnvelope.getMinX() + " " + targetEnvelope.getMinY() 
								+ "</gml:pos>");
						tempResponse.append("\n   <gml:pos>" 
								+ targetEnvelope.getMaxX() + " " + targetEnvelope.getMaxY() 
								+ "</gml:pos>");
						tempResponse.append("\n   <gml:timePosition></gml:timePosition>");
						tempResponse.append("\n   <gml:timePosition></gml:timePosition>");
						tempResponse.append("\n  </lonLatEnvelope>");
				    } else {
				    	// @task TODO: Handle non-affine transforms
				    }
			    } else {
			    	tempResponse.append("\n  <lonLatEnvelope" 
			    			+ " srsName=\"WGS84(DD)\""
			    			+">");
			    	tempResponse.append("\n   <gml:pos>" 
			    			+ (cv.getEnvelope() != null ? cv.getEnvelope().getMinX() + " " + cv.getEnvelope().getMinY() : "") 
			    			+ "</gml:pos>");
			    	tempResponse.append("\n   <gml:pos>" 
			    			+ (cv.getEnvelope() != null ? cv.getEnvelope().getMaxX() + " " + cv.getEnvelope().getMaxY() : "") 
			    			+ "</gml:pos>");
			    	tempResponse.append("\n   <gml:timePosition></gml:timePosition>");
			    	tempResponse.append("\n   <gml:timePosition></gml:timePosition>");
			    	tempResponse.append("\n  </lonLatEnvelope>");
			    }
			} catch (OperationNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FactoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if( (cv.getKeywords() != null) && (cv.getKeywords().size() > 0) ) {
				tempResponse.append("\n  <keywords>");
				for( int i=0; i<cv.getKeywords().size(); i++)
					tempResponse.append("\n   <keyword>" + cv.getKeywords().get(i) 
							+ "</keyword>");
				tempResponse.append("\n  </keywords>");
			}

			tempResponse.append("\n  <domainSet>");
				tempResponse.append("\n   <spatialDomain>");
					tempResponse.append("\n    <gml:Envelope" 
							+ (cv.getSrsName() != null && cv.getSrsName() != "" ? " srsName=\"" + cv.getSrsName() + "\"" : "")
							+">");
						tempResponse.append("\n       <gml:pos>" 
								+ (cv.getEnvelope() != null ? cv.getEnvelope().getMinX() + " " + cv.getEnvelope().getMinY() : "") 
								+ "</gml:pos>");
						tempResponse.append("\n       <gml:pos>" 
								+ (cv.getEnvelope() != null ? cv.getEnvelope().getMaxX() + " " + cv.getEnvelope().getMaxY() : "") 
								+ "</gml:pos>");
					tempResponse.append("\n    </gml:Envelope>");
				tempResponse.append("\n   </spatialDomain>");
			tempResponse.append("\n  </domainSet>");
			
			// <TODO>rangeSet</TODO>
			tempResponse.append("\n  <rangeSet>");
				tempResponse.append("\n   <RangeSet>");
					tempResponse.append("\n    <name>TODO</name>");
					tempResponse.append("\n    <label>TODO</label>");
				tempResponse.append("\n   </RangeSet>");
			tempResponse.append("\n  </rangeSet>");
			
			if( ((cv.getRequestCRSs() != null) && (cv.getRequestCRSs().size() > 0)) 
					|| ((cv.getResponseCRSs() != null) && (cv.getResponseCRSs().size() > 0)) ) {
				tempResponse.append("\n  <supportedCRSs>");
				if( (cv.getRequestCRSs() != null) && (cv.getRequestCRSs().size() > 0) ) {
					for( int i=0; i<cv.getRequestCRSs().size(); i++)
						tempResponse.append("\n    <requestCRSs>" + cv.getRequestCRSs().get(i)
								+ "</requestCRSs>");
				}
				if( (cv.getResponseCRSs() != null) && (cv.getResponseCRSs().size() > 0) ) {
					for( int i=0; i<cv.getResponseCRSs().size(); i++)
						tempResponse.append("\n    <responseCRSs>" + cv.getResponseCRSs().get(i)
								+ "</responseCRSs>");
				}
				tempResponse.append("\n  </supportedCRSs>");
			}
			
			if( ((cv.getSupportedFormats() != null) && (cv.getSupportedFormats().size() > 0)) ) {
				tempResponse.append("\n  <supportedFormats" 
						+ (cv.getNativeFormat() != null && cv.getNativeFormat() != "" 
							? " nativeFormat=\"" + cv.getNativeFormat() + "\"" 
							: "") 
						+ ">");
				for( int i=0; i<cv.getSupportedFormats().size(); i++)
					tempResponse.append("\n    <formats>" + cv.getSupportedFormats().get(i)
							+ "</formats>");
				tempResponse.append("\n  </supportedFormats>");
			}
			
			if( ((cv.getInterpolationMethods() != null) && (cv.getInterpolationMethods().size() > 0)) ) {
				tempResponse.append("\n  <supportedInterpolations" 
						+ (cv.getDefaultInterpolationMethod() != null && cv.getDefaultInterpolationMethod() != "" 
							? " default=\"" + cv.getDefaultInterpolationMethod() + "\"" 
							: "") 
						+ ">");
				for( int i=0; i<cv.getInterpolationMethods().size(); i++)
					tempResponse.append("\n    <interpolationMethod>" + cv.getInterpolationMethods().get(i)
							+ "</interpolationMethod>");
				tempResponse.append("\n  </supportedInterpolations>");
			}
			
		tempResponse.append("\n </CoverageOffering>");
		return tempResponse.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.vfny.geoserver.responses.Response#abort()
	 */
	public void abort(Service gs) {
		// nothing to undo
	}
}

package org.vfny.geoserver.wcs.responses;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.JTS;
import org.geotools.referencing.FactoryFinder;
import org.geotools.resources.CRSUtilities;
//import org.geotools.referencing.crs.EPSGCRSAuthorityFactory;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.OperationNotFoundException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.global.CoverageCategory;
import org.vfny.geoserver.global.CoverageDimension;
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
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
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

	/**
	 * 
	 * @uml.property name="request"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private DescribeRequest request;

	
	/** Main XML class for interpretation and response. */
	private String xmlResponse = new String();

	/**
	 * The default datum factory.
	 * 
	 * @uml.property name="datumFactory"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	protected final DatumFactory datumFactory = FactoryFinder
		.getDatumFactory(null);


    /**
     * The default coordinate reference system factory.
     */
    //protected  final static CRSFactory crsFactory = FactoryFinder.getCRSFactory(new Hints(Hints.CRS_AUTHORITY_FACTORY,EPSGCRSAuthorityFactory.class));
    protected  final static CRSFactory crsFactory = FactoryFinder.getCRSFactory(new Hints(Hints.CRS_AUTHORITY_FACTORY,CRSAuthorityFactory.class));

	/**
	 * The default math transform factory.
	 * 
	 * @uml.property name="mtFactory"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	protected final MathTransformFactory mtFactory = FactoryFinder
		.getMathTransformFactory(null);

    /**
     * The default transformations factory.
     */
    protected  final static CoordinateOperationFactory opFactory = FactoryFinder.getCoordinateOperationFactory(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));

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
	
	public String getContentDisposition() {
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
				if(cv.getCrs() != null) {
					final CRSAuthorityFactory crsFactory = FactoryFinder.getCRSAuthorityFactory("EPSG", new Hints(Hints.CRS_AUTHORITY_FACTORY, CRSAuthorityFactory.class));
					final CoordinateOperationFactory opFactory = FactoryFinder.getCoordinateOperationFactory(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));
					final CoordinateReferenceSystem targetCRS = crsFactory.createCoordinateReferenceSystem("EPSG:4326");
					final CoordinateReferenceSystem sourceCRS = cv.getCrs();
					final CoordinateOperation operation = opFactory.createOperation(sourceCRS, targetCRS);
					final MathTransform mathTransform = (MathTransform) operation.getMathTransform();
					final Envelope envelope = cv.getEnvelope();
					final Envelope targetEnvelope = JTS.transform(envelope, mathTransform);
			    	final CoordinateSystem cs = sourceCRS.getCoordinateSystem();

			    	boolean lonFirst = true;
			    	if (cs.getAxis(0).getDirection().absolute().equals(AxisDirection.NORTH)) {
			    		lonFirst = false;
			    	}
			    	boolean swapXY = lonFirst;

			    	// latitude index
			        final int latIndex = lonFirst ? 1 : 0;

			        final AxisDirection latitude = cs.getAxis(latIndex).getDirection();
			        final AxisDirection longitude = cs.getAxis((latIndex + 1) % 2).getDirection();
			        final boolean[] reverse = new boolean[] {
			        		lonFirst ? !longitude.equals(AxisDirection.EAST) : !latitude.equals(AxisDirection.NORTH), 
			        		lonFirst ? !latitude.equals(AxisDirection.NORTH) : !longitude.equals(AxisDirection.EAST)
					};

					tempResponse.append("\n  <lonLatEnvelope" 
							+ " srsName=\"WGS84(DD)\""
							+">");
					tempResponse.append("\n   <gml:pos>" 
							+ (!swapXY ? (!reverse[(latIndex + 1) % 2] ? targetEnvelope.getMinX() : targetEnvelope.getMaxX()) : (!reverse[(latIndex + 1) % 2] ? targetEnvelope.getMinY() : targetEnvelope.getMaxY())) 
							+ " " 
							+ (!swapXY ? (!reverse[(latIndex + 1) % 2] ? targetEnvelope.getMinY() : targetEnvelope.getMaxY()) : (!reverse[(latIndex + 1) % 2] ? targetEnvelope.getMinX() : targetEnvelope.getMaxX())) 
							+ "</gml:pos>");
					tempResponse.append("\n   <gml:pos>" 
							+ (!swapXY ? (!reverse[latIndex] ? targetEnvelope.getMaxX() : targetEnvelope.getMinX()) : (!reverse[latIndex] ? targetEnvelope.getMaxY() : targetEnvelope.getMinY())) 
							+ " " 
							+ (!swapXY ? (!reverse[latIndex] ? targetEnvelope.getMaxY() : targetEnvelope.getMinY()) : (!reverse[latIndex] ? targetEnvelope.getMaxX() : targetEnvelope.getMinX()))
							+ "</gml:pos>");
					tempResponse.append("\n   <gml:timePosition></gml:timePosition>");
					tempResponse.append("\n   <gml:timePosition></gml:timePosition>");
					tempResponse.append("\n  </lonLatEnvelope>");
				} else {
					final Envelope envelope = cv.getEnvelope();
					
					tempResponse.append("\n  <lonLatEnvelope" 
							+ " srsName=\"WGS84(DD)\""
							+">");
					tempResponse.append("\n   <gml:pos>" 
							+ envelope.getMinX() 
							+ " " 
							+ envelope.getMinY() 
							+ "</gml:pos>");
					tempResponse.append("\n   <gml:pos>" 
							+ envelope.getMaxX() 
							+ " " 
							+ envelope.getMaxY()
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
					// Envelope
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
					
					// Grid
					GridGeometry g = cv.getGrid();
					InternationalString[] dimNames = cv.getDimensionNames();
					final int gridDimension = g.getGridRange().getDimension();
					
					//RectifiedGrid
					tempResponse.append("\n    <gml:RectifiedGrid"
							+ (g != null ? " dimension=\"" + gridDimension + "\"" : "")
							+">");
					
						String lowers = "", upers = "";
						for(int r=0; r<gridDimension; r++) {
							lowers += g.getGridRange().getLower(r) + " ";
							upers += g.getGridRange().getUpper(r) + " ";
						}
						tempResponse.append("\n       <gml:limits>");
							tempResponse.append("\n         <gml:GridEnvelope>");
								tempResponse.append("\n         <gml:low>" 
										+ (cv.getEnvelope() != null ? lowers : "") 
										+ "</gml:low>");
								tempResponse.append("\n         <gml:high>" 
										+ (cv.getEnvelope() != null ? upers : "") 
										+ "</gml:high>");
							tempResponse.append("\n         </gml:GridEnvelope>");
						tempResponse.append("\n       </gml:limits>");
						if(dimNames!=null)
							for(int dn=0;dn<dimNames.length;dn++)
								tempResponse.append("\n       <gml:axisName>" + dimNames[dn] +"</gml:axisName>");
						tempResponse.append("\n       <gml:origin>");
							tempResponse.append("\n       <gml:pos>" 
									+ (cv.getEnvelope() != null ? cv.getEnvelope().getMinX() + " " + cv.getEnvelope().getMaxY() : "") 
									+ "</gml:pos>");
						tempResponse.append("\n       </gml:origin>");
						tempResponse.append("\n       <gml:offsetVector>" + (cv.getEnvelope() != null ? (cv.getEnvelope().getMaxX() - cv.getEnvelope().getMinX())/(g.getGridRange().getUpper(0) - g.getGridRange().getLower(0)) : 0.0) + " 0.0</gml:offsetVector>");
						tempResponse.append("\n       <gml:offsetVector>0.0 " + (cv.getEnvelope() != null ? (cv.getEnvelope().getMinY() - cv.getEnvelope().getMaxY())/(g.getGridRange().getUpper(1) - g.getGridRange().getLower(1)) : -0.0) + "</gml:offsetVector>");
					tempResponse.append("\n    </gml:RectifiedGrid>");
					
					//Grid
/*					tempResponse.append("\n    <gml:Grid"
							+ (g != null ? " dimension=\"" + g.getGridRange().getDimension() + "\"" : "")
							+">");
					
						String lowers = "", upers = "";
						for(int r=0; r<g.getGridRange().getDimension(); r++) {
							lowers += g.getGridRange().getLower(r) + " ";
							upers += g.getGridRange().getUpper(r) + " ";
						}
						tempResponse.append("\n       <gml:limits>");
							tempResponse.append("\n         <gml:GridEnvelope>");
								tempResponse.append("\n         <gml:low>" 
										+ (cv.getEnvelope() != null ? lowers : "") 
										+ "</gml:low>");
								tempResponse.append("\n         <gml:high>" 
										+ (cv.getEnvelope() != null ? upers : "") 
										+ "</gml:high>");
							tempResponse.append("\n         </gml:GridEnvelope>");
						tempResponse.append("\n       </gml:limits>");
						if(dimNames!=null)
							for(int dn=0;dn<dimNames.length;dn++)
								tempResponse.append("\n       <gml:axisName>" + dimNames[dn] +"</gml:axisName>");
					tempResponse.append("\n    </gml:Grid>");
*/				
				tempResponse.append("\n   </spatialDomain>");
			tempResponse.append("\n  </domainSet>");
			
			// rangeSet
			CoverageDimension[] dims = cv.getDimensions();
			TreeSet nodataValues = new TreeSet();
			try {
				if(dims!=null) {
					int numSampleDimensions = dims.length;
					tempResponse.append("\n  <rangeSet>");
						tempResponse.append("\n   <RangeSet>");
							tempResponse.append("\n    <name>" + cv.getName() + "</name>");
							tempResponse.append("\n    <label>" + cv.getLabel() + "</label>");
							for(int sample=0;sample<numSampleDimensions;sample++) {
								CoverageCategory[] categories = dims[sample].getCategories();
								for(int c=0;c<categories.length;c++) {
									CoverageCategory cat = categories[c];
									tempResponse.append("\n      <axisDescription>");
										tempResponse.append("\n        <AxisDescription>");
											tempResponse.append("\n          <name>" + dims[sample].getName() + ":Category(" + cat.getName() + ")</name>");
											tempResponse.append("\n          <label>" + dims[sample].getDescription() + "</label>");
											tempResponse.append("\n          <values>");
												tempResponse.append("\n            <interval>");
													tempResponse.append("\n              <min>" + cat.getInterval().getMinimum(true) + "</min>");
													tempResponse.append("\n              <max>" + cat.getInterval().getMaximum(true) + "</max>");
												tempResponse.append("\n            </interval>");
											tempResponse.append("\n          </values>");
										tempResponse.append("\n        </AxisDescription>");
									tempResponse.append("\n      </axisDescription>");
								}
								Double[] nodata = dims[sample].getNullValues();
								if(nodata!=null)
									for(int nd=0;nd<nodata.length;nd++) {
										if(!nodataValues.contains(nodata[nd])) {
											nodataValues.add(nodata[nd]);
										}
									}
							}
							if(nodataValues.size() > 0) {
								tempResponse.append("\n      <nullValues>");
								if(nodataValues.size() == 1) {
									tempResponse.append("\n        <singleValue>" + (Double) nodataValues.first() + "</singleValue>");								
								} else {
									tempResponse.append("\n        <interval>");								
										tempResponse.append("\n          <min>" + (Double) nodataValues.first() + "</min>");								
										tempResponse.append("\n          <max>" + (Double) nodataValues.last() + "</max>");								
									tempResponse.append("\n        <interval>");								
								}
								tempResponse.append("\n      </nullValues>");
							}
						tempResponse.append("\n   </RangeSet>");
					tempResponse.append("\n  </rangeSet>");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
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

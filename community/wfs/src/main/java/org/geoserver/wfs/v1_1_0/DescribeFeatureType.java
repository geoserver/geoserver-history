package org.geoserver.wfs.v1_1_0;

//import java.util.List;
//
//import net.opengis.wfs.v1_1_0.DescribeFeatureTypeType;
//import net.opengis.wfs.v1_1_0.WFSFactory;
//
//import org.geoserver.data.GeoServerCatalog;
//import org.geoserver.data.feature.FeatureTypeInfo;
//import org.geoserver.wfs.WFS;
//import org.geoserver.wfs.WFSException;
//
//public class DescribeFeatureType {
//
//	WFS wfs;
//	GeoServerCatalog catalog;
//	
//	DescribeFeatureTypeType request;
//	
//	public DescribeFeatureType( WFS wfs, GeoServerCatalog catalog ) {
//		this.wfs = wfs;
//		this.catalog = catalog;
//	}
//	
//	public FeatureTypeInfo[] describeFeatureType() throws WFSException {
//		
//		return describeFeatureType( request() );
//	}
//	
//	  public void setOutputFormat(String outputFormat) {
//  		request().setOutputFormat( outputFormat );
//	}
//  
//	public String getOutputFormat() {
//		return request().getOutputFormat();
//	}
//  
//  	public void setTypeName(List typeNames) {
//		request().getTypeName().clear();
//		request().getTypeName().addAll( typeNames );
//	}
//
//  	public List getTypeName() {
//		return request().getTypeName();
//	}
//  
//	public FeatureTypeInfo[] describeFeatureType( 
//		DescribeFeatureTypeType request 
//	) throws WFSException {
//		
//		this.request = request;
//		
//		//delegate to 1.0 operation
//		org.geoserver.wfs.DescribeFeatureType op = 
//			new org.geoserver.wfs.DescribeFeatureType( wfs, catalog );
//		op.setOutputFormat( request.getOutputFormat() );
//		op.setTypeName( request.getTypeName() );
//		
//		return op.describeFeatureType();
//	}
//	
//	DescribeFeatureTypeType request() {
//		if ( request == null ) {
//			request = WFSFactory.eINSTANCE.createDescribeFeatureTypeType();
//		}
//		
//		return request;
//	}
//}

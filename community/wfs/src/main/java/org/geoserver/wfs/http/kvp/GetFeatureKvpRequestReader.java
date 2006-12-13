package org.geoserver.wfs.http.kvp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import net.opengis.wfs.QueryType;

import org.eclipse.emf.ecore.EObject;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.ows.EMFUtils;
import org.geoserver.wfs.WFSException;
import org.geotools.feature.FeatureType;
import org.geotools.filter.FilterFactory;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.spatial.BBOX;

import com.vividsolutions.jts.geom.Envelope;

public class GetFeatureKvpRequestReader extends WFSKvpRequestReader {

	/**
	 * Catalog used in qname parsing
	 */
	GeoServerCatalog catalog;
	/**
	 * Factory used in filter parsing
	 */
	FilterFactory filterFactory;
	
	public GetFeatureKvpRequestReader( 
		Class requestBean, GeoServerCatalog catalog, FilterFactory filterFactory 
	) {
		super(requestBean);
		this.catalog = catalog;
		this.filterFactory = filterFactory;
	}
	
	/**
	 * Performs additinon GetFeature kvp parsing requirements
	 */
	public Object read( Object request, Map kvp ) throws Exception {
		request = super.read(request, kvp);
		
		//make sure not both featureid and filter specified
		if ( kvp.containsKey( "featureId" ) && kvp.containsKey( "filter" ) ) {
			String msg = "featureid and filter both specified but are mutually exclusive";
			throw new WFSException( msg );
		}
		//make sure not both featureid and bbox specified
		if ( kvp.containsKey( "featureId" ) && kvp.containsKey( "bbox" ) ) {
			String msg = "featureid and bbox both specified but are mutually exclusive";
			throw new WFSException( msg );
		}
		//make sure not both filter and bbox specified
		if ( kvp.containsKey( "filter" ) && kvp.containsKey( "bbox" ) ) {
			String msg = "bbox and filter both specified but are mutually exclusive";
			throw new WFSException( msg );
		}
		
		//get feature has some additional parsing requirements
		EObject eObject = (EObject) request;
		
		//outputFormat
		if ( !EMFUtils.isSet( eObject, "outputFormat") ) {
			//set the default
			String version = (String) EMFUtils.get( eObject, "version" );
			if ( version != null && version.startsWith( "1.0" ) ) {
				EMFUtils.set( eObject, "outputFormat", "GML2" );	
			}
			else {
				EMFUtils.set( eObject, "outputFormat", "text/xml; subtype=gml/3.1.1" );
			}
		}
		
		//typeName
		if ( kvp.containsKey( "typeName" ) ) {
			//HACK, the kvp reader gives us a list of QName, need to wrap in 
			// another
			List typeName = (List) kvp.get( "typeName" );
			List list = new ArrayList();
			for ( Iterator itr = typeName.iterator(); itr.hasNext(); ) {
				QName qName = (QName) itr.next();
				List l = new ArrayList();
				l.add( qName );
				list.add( l );
			}
			
			kvp.put( "typeName", list );
			querySet( eObject, "typeName", list );
		}
		else {
			//check for featureId and infer typeName
			if ( kvp.containsKey( "featureId") ) {
				//use featureId to infer type Names
				List featureId = (List) kvp.get( "featureId" );
				
				ArrayList typeNames = new ArrayList();
				TypeNameKvpReader reader = new TypeNameKvpReader( catalog );
				for ( int i = 0; i < featureId.size(); i++ ) {
					String fid = (String) featureId.get( i );
					int pos = fid.indexOf(".");
	                if ( pos != -1 ) {
                		String typeName = fid.substring( 0, fid.lastIndexOf( "." ) );
                		fid = fid.substring( fid.lastIndexOf( "." ) + 1 );
                		
                		//update the fid in hte list ( for later when we parse the fid into a filter )
                		featureId.set( i, fid );
                		
                		//add to a list to set on the query
                		List l = new ArrayList();
                		l.add( reader.qName( typeName ) );
                		
                		typeNames.add( l );
	                }
				}
				
				querySet( eObject, "typeName", typeNames );
			}
		}
		
		//filter
		if ( kvp.containsKey( "filter" ) ) {
			querySet( eObject, "filter", (List) kvp.get( "filter" ) );
		}
		else if ( kvp.containsKey( "featureId" ) ) {
			//set filter from featureId
			List featureIdList = (List) kvp.get( "featureId" );
			List filters = new ArrayList();
		
			for ( Iterator i = featureIdList.iterator(); i.hasNext(); ) {
				String fid = (String) i.next();
				FeatureId featureId = filterFactory.featureId( fid );
				
				HashSet featureIds = new HashSet();
				featureIds.add( featureId );
				filters.add( filterFactory.id( featureIds ) );
			}
			
			querySet( eObject, "filter", filters );
		}
		else if ( kvp.containsKey( "bbox" ) ) {
			//set filter from bbox 
			Envelope bbox = (Envelope) kvp.get( "bbox" );
			
			List queries = (List) EMFUtils.get( eObject, "query" );
			List filters = new ArrayList();
			for ( Iterator q = queries.iterator(); q.hasNext(); ) {
				QueryType query = (QueryType) q.next();
				List typeName = query.getTypeName();
				Filter filter = null; 
				
				if ( typeName.size() > 1 ) {
					//TODO: not sure what to do here, just going to and them up
					List and = new ArrayList( typeName.size() );
					for ( Iterator t = typeName.iterator(); t.hasNext(); ) {
						
						and.add( bboxFilter( (QName)t.next(), bbox ) ); 
					}
					filter = filterFactory.and( and );
				}
				else {
					filter = bboxFilter( (QName) typeName.get( 0 ), bbox );
				}
				
				filters.add( filter );
			}
			
			querySet( eObject, "filter", filters );
		}
				
		//propertyName
		if ( kvp.containsKey( "propertyName") ) {
			querySet( eObject, "propertyName", (List) kvp.get( "propertyName" ) );
		}
		
		return request;
	}
	
	BBOX bboxFilter( QName typeName, Envelope bbox ) throws Exception {
		FeatureTypeInfo featureTypeInfo = null;
		FeatureType featureType = null;
		try {
			featureTypeInfo = 
				catalog.featureType( typeName.getPrefix(), typeName.getLocalPart() );
			featureType = featureTypeInfo.featureType();
		}
		catch( IOException e ) {
			String msg = "Unable to load feature type: " + typeName;
			throw new WFSException( msg, e );
		}
		
		//TODO: should this be applied to all geometries?
		String name = featureType.getDefaultGeometry().getName();
		return filterFactory.bbox( 
			name, bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY(), null	
		);
		
	}
	
	protected void querySet( EObject request, String property, List values ) 
		throws WFSException {
		
    	//no values specified, do nothing
    	if ( values == null ) 
    		return;
    	
    	List query = (List) EMFUtils.get( request, "query" );
		
		int m = values.size();
		int n = query.size();
		
		if ( m == 1 && n > 1 ) {
			//apply single value to all queries
			EMFUtils.set( query, property, values.get( 0 ) );
			return;
		}
		
		//match up sizes
		if ( m > n ) {
			
			if ( n == 0 ) {
				//make same size, with empty objects
				for ( int i = 0; i < m; i++ ) {
					query.add( wfsFactory.createQueryType() );
				}
			}
			else if ( n == 1 ) {
				//clone single object up to 
				EObject q = (EObject) query.get( 0 );
				for ( int i = 1; i < m; i++ ) {
					query.add( EMFUtils.clone( q, wfsFactory ) );
				}
				return;
			}
			else {
				//illegal
    			String msg = "Specified " + m + " " + property + " for " + n + " queries.";
    			throw new WFSException( msg );	
			}
		}
		
		EMFUtils.set( query, property, values );
	}

}

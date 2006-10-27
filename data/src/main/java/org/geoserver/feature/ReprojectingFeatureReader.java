package org.geoserver.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureReader;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.referencing.FactoryFinder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.OperationNotFoundException;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Decorating feature reader which reprojects feature geometries to a particular coordinate
 * reference system on the fly.
 * <p>
 * The coordinate reference system of feature geometries is looked up using 
 * {@link com.vividsolutions.jts.geom.Geometry#getUserData()}.
 * </p>
 * <p>
 * The {@link #defaultSource} attribute can be set to specify a coordinate refernence system 
 * to transform from when one is not specified by teh geometry itself. Leaving the property 
 * null specifies that the geometry will not be transformed.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class ReprojectingFeatureReader implements FeatureReader {

	/**
	 * The decorated reader
	 */
	FeatureReader delegate;
	/**
	 * The schema of reprojected features
	 */
	FeatureType schema;
	/**
	 * The target coordinate reference system
	 */
	CoordinateReferenceSystem target;
	
	/**
	 * Coordinate reference system to use when one is not 
	 * specified on an encountered geometry.
	 */
	CoordinateReferenceSystem defaultSource;
	
	/**
	 * MathTransform cache, keyed by source CRS
	 */
	HashMap/*<CoordinateReferenceSystem,GeometryCoordinateSequenceTransformer>*/ transformers;
	
	public ReprojectingFeatureReader( FeatureReader delegate, CoordinateReferenceSystem target ) 
		throws SchemaException, OperationNotFoundException, FactoryRegistryException, FactoryException {
		
		this.delegate = delegate;
		this.target = target;
		this.schema = FeatureTypes.transform( delegate.getFeatureType(), target );
		
		//create transform cache
		transformers = new HashMap();
		
		//cache "default" transform
		CoordinateReferenceSystem source = 
			delegate.getFeatureType().getDefaultGeometry().getCoordinateSystem();
		if ( source != null ) {
			MathTransform2D tx = (MathTransform2D) FactoryFinder.getCoordinateOperationFactory(null)
        		.createOperation(source,target).getMathTransform();
			
			GeometryCoordinateSequenceTransformer transformer = 
				new GeometryCoordinateSequenceTransformer();
			transformer.setMathTransform( tx );
			transformers.put( source, transformer );
		}
		else {
			//throw exception?
		}
	}
	
	public void setDefaultSource( CoordinateReferenceSystem defaultSource ) {
		this.defaultSource = defaultSource;
	}
	
	public FeatureType getFeatureType() {
		return schema;
	}

	public Feature next() throws IOException, IllegalAttributeException,
			NoSuchElementException {
		
		if (delegate == null) {
            throw new IllegalStateException("Reader has already been closed");
        }
		
		Feature feature = delegate.next();
		Object[] attributes = feature.getAttributes( null );
		
		for ( int i = 0; i < attributes.length; i++ ) {
			Object object = attributes[ i ];
			if ( object instanceof Geometry ) {
				//check for crs
				Geometry geometry = (Geometry) object;
				CoordinateReferenceSystem crs = (CoordinateReferenceSystem) geometry.getUserData();
				if ( crs == null ) {
					// no crs specified on geometry, check default
					if ( defaultSource != null ) {
						crs = defaultSource;
					}
				}
				
				if ( crs != null ) {
					//if equal, nothing to do
					if ( crs.equals( target ) )
						continue;
					
					GeometryCoordinateSequenceTransformer transformer = 
						(GeometryCoordinateSequenceTransformer) transformers.get( crs );
				
					if ( transformer == null ) {
						transformer = new GeometryCoordinateSequenceTransformer();
						MathTransform2D tx;
						try {
							tx = (MathTransform2D) FactoryFinder.getCoordinateOperationFactory(null)
									.createOperation(crs,target).getMathTransform();
						} catch ( Exception e ) {
							String msg = "Could not transform for crs: " + crs;
							throw (IOException) new IOException( msg ).initCause( e );
						}
						
		        		transformer.setMathTransform( tx );
		        		transformers.put( crs, transformer );
					}
					
					//do the transformation
					try {
						attributes[ i ] = transformer.transform( geometry );
					} 
					catch (TransformException e) {
						String msg = "Error occured transforming " + geometry.toString();
						throw (IOException) new IOException( msg ).initCause( e );
					}
				}
				
			}
		}
		
		return schema.create( attributes, feature.getID() );
		
	}

	public boolean hasNext() throws IOException {
		return delegate.hasNext();
	}

	public void close() throws IOException {
	   if (delegate == null) {
            throw new IOException("Reader has already been closed");
        }

        delegate.close();
        delegate = null;
        schema = null;
	}

}

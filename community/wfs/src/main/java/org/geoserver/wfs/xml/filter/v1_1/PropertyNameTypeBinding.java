package org.geoserver.wfs.xml.filter.v1_1;

import java.util.ArrayList;
import java.util.List;

import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.filter.expression.Value;
import org.geotools.filter.v1_0.OGCPropertyNameTypeBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.PropertyName;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A binding for ogc:PropertyName which does a special case check for an empty 
 * property name.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class PropertyNameTypeBinding extends OGCPropertyNameTypeBinding {

	public PropertyNameTypeBinding(FilterFactory filterFactory) {
		super(filterFactory);
	}
	
	public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
		PropertyName propertyName = (PropertyName) super.parse( instance, node, value );	
		if ( propertyName.getPropertyName() == null || "".equals( propertyName.getPropertyName().trim() ) ) {
			//we override here to create a property that evaluates to the union of all geometries
			return new GeometryUnionPropertyName( propertyName );
		}
		
		return propertyName;
	}
	
	private static class GeometryUnionPropertyName implements PropertyName {
		
		PropertyName delegate;
		
		public GeometryUnionPropertyName( PropertyName delegate ) {
			this.delegate = delegate;
		}
		
		public String getPropertyName() {
			return delegate.getPropertyName();
		}
		
		public Object evaluate(Object object) {
			if ( object instanceof Feature ) {
				Feature feature = (Feature) object;
				Geometry geometry = null;
				for ( int i = 0; i < feature.getNumberOfAttributes(); i++ ) {
					Object attribute = feature.getAttribute( i );
					if ( attribute instanceof Geometry ) {
						if ( geometry == null ) {
							geometry = (Geometry) attribute;
						}
						else {
							geometry = geometry.union( (Geometry) attribute );
						}
					}
				}
				
				return geometry;
			}
			
			//non feature, just delegate
			return delegate.evaluate( object );
		}

		public Object evaluate(Object object, Class target) {
			return new Value( evaluate( object ) ).value( target );
		}
		
		public void setPropertyName(String propertyName) {
			delegate.setPropertyName( propertyName );
		}

		public Object accept(ExpressionVisitor visitor, Object extraData ) {
			return delegate.accept( visitor, extraData );
		}
	}
}

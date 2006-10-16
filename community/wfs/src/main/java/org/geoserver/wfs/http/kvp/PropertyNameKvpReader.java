package org.geoserver.wfs.http.kvp;

import java.util.List;

import org.geoserver.http.util.KvpUtils;
import org.geoserver.ows.http.KvpParser;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.v1_0.OGCPropertyNameTypeBinding;
import org.opengis.filter.expression.PropertyName;

public class PropertyNameKvpReader extends KvpParser {

	FilterFactory filterFactory;
	
	public PropertyNameKvpReader( FilterFactory filterFactory ) {
		super( "propertyname", List.class );
		this.filterFactory = filterFactory;
	}
	
	public Object parse( String value ) throws Exception {
		List l1 =  KvpUtils.readNested( value );
		
		for ( int i = 0; i < l1.size(); i++ ) {
			List l2 = (List) l1.get( i );
			for ( int j = 0; j < l2.size(); j++ ) {
				String name = (String) l2.get( j );
				
				//strip off namespace prefix.
		        //Our feature model does not suppor tthe notion of namespace for attributes
		        // and there is a lot of code that expects this to be a non-qualified name.
		        // Also, the PropertyName interface doesn't support a namespace
				if ( name.indexOf( ":" ) != -1 ) {
					name = name.substring( name.indexOf( ":" ) + 1 );
				}
				
				PropertyName propertyName = filterFactory.property( name ); 
				l2.set( j, propertyName );
			}
		}
		
		return l1;
	}

}

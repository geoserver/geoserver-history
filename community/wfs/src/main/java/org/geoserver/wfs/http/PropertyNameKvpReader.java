package org.geoserver.wfs.http;

import java.util.List;

import org.geoserver.http.util.KvpUtils;
import org.geoserver.ows.http.KvpParser;
import org.geotools.filter.FilterFactory;

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
				l2.set( j, filterFactory.property( name ) );
			}
		}
		
		return l1;
	}

}

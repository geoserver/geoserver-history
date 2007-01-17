package org.geoserver.wfs.http.kvp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geoserver.http.util.KvpUtils;
import org.geoserver.ows.http.KvpParser;
import org.geotools.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * Parses kvp of the form 'sortBy=Field1 {A|D},Field2 {A|D}...' into a 
 * list of {@link org.opengis.filter.sort.SortBy}.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class SortByKvpReader extends KvpParser {

	FilterFactory filterFactory;
	
	public SortByKvpReader( FilterFactory filterFactory ) {
		super( "sortBy", List.class );
		this.filterFactory = filterFactory;
	}
	
	public Object parse( String value ) throws Exception {
		List values = KvpUtils.readNested( value );
		List sortBy = new ArrayList( values.size() );
		
		for ( Iterator j = values.iterator();  j.hasNext(); ) {
			List/*<String>*/ l1 = (List) j.next();
			List/*<SortBy>*/ l2 = new ArrayList();
			
			for ( Iterator i = l1.iterator(); i.hasNext(); ) {
				String s = (String) i.next();
				String[] nameOrder = s.split( " " );
				String propertyName = nameOrder[0];
				SortOrder order = SortOrder.ASCENDING;
				
				if ( nameOrder.length > 1 ) {
					if ( "D".equalsIgnoreCase( nameOrder[1] ) ) {
						order = SortOrder.DESCENDING;
					}
				}
				
				SortBy sort = filterFactory.sort( propertyName, order );
				l2.add( sort );
			}
			
			sortBy.add( l2 );
		}
		
		return sortBy;
	}

}

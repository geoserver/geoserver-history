package org.geoserver.wfs.http.kvp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.http.util.KvpUtils;
import org.geoserver.ows.http.KvpParser;

/**
 * Abstract kvp parser for parsing qualified names of the form "([prefix:]local)+".
 * <p>
 * This parser will parse strings of the above format into a list of 
 * {@link javax.xml.namespace.QName}
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class QNameKvpParser extends KvpParser {

	/**
	 * catalog for namespace lookups.
	 */
	GeoServerCatalog catalog;
	
	public QNameKvpParser( String key, GeoServerCatalog catalog ) {
		super( key, List.class);
		this.catalog = catalog;
	}

	/**
	 * Parses the string representation of type names into a list of {@link QName} 
	 * objects.
	 * 
	 */
	public Object parse( String value ) throws Exception {
	
		List values = KvpUtils.readFlat( value, "," );
		List qNames = new ArrayList( values.size() );
		
		for ( Iterator v = values.iterator(); v.hasNext(); ) {
			value = (String) v.next();
			qNames.add( qName( value ) );
		}
		
		return qNames;
	}
	
	/**
	 * Parses a single string representation of a type name, ( <prefix>:<local>, or <local> )
	 * into a {@link QName }.
	 * <p>
	 * If the latter form is supplied the QName is given the default namespace as specified in the 
	 * catalog.
	 * </p>
	 * @param value
	 * @return
	 */
	public QName qName( String value ) {
		int i = value.indexOf( ':' );
		if ( i != -1 ) {
			String prefix = value.substring( 0, i );
			String local = value.substring( i + 1 );
			
			String uri = catalog.getNamespaceSupport().getURI( prefix );
			
			return new QName( uri, local, prefix );
		}
		else {
			String uri = catalog.getNamespaceSupport().getURI( "" );
			String prefix = catalog.getNamespaceSupport().getPrefix( uri );
			String local = value;
			
			return  new QName( uri, local, prefix );
		}
	}
}

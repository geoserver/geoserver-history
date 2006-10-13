package org.geoserver.wfs.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geoserver.http.util.KvpUtils;
import org.geoserver.ows.http.KvpParser;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.v1_0.OGCBindingConfiguration;
import org.geotools.filter.v1_0.OGCConfiguration;
import org.geotools.filter.v1_0.OGCSchemaLocationResolver;
import org.geotools.filter.v1_0.OGCSchemaLocator;
import org.geotools.gml2.bindings.GMLBindingConfiguration;
import org.geotools.gml2.bindings.GMLSchemaLocationResolver;
import org.geotools.xlink.bindings.XLINKBindingConfiguration;
import org.geotools.xlink.bindings.XLINKSchemaLocationResolver;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.geotools.xs.bindings.XSBindingConfiguration;
import org.opengis.filter.Filter;
import org.picocontainer.MutablePicoContainer;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;

public class FilterKvpReader extends KvpParser {

	public FilterKvpReader() {
		super( "filter", List.class );
	}
	
	public Object parse( String value ) throws Exception {
		//create the parser
		Configuration configuration = new OGCConfiguration();
		Parser parser = new Parser( configuration );
		
		//seperate the individual filter strings
        List unparsed = KvpUtils.readFlat( value, KvpUtils.OUTER_DELIMETER );
        List filters = new ArrayList();
         
        Iterator i = unparsed.listIterator();
        while (i.hasNext()) {
        		String string = (String) i.next();
			InputStream input = new ByteArrayInputStream( string.getBytes() );
			 
			try {
				Filter filter = (Filter) parser.parse( input );
				if ( filter == null )
					throw new NullPointerException();
				
				filters.add( filter );
			}
			catch ( Exception e ) {
				String msg = "Unable to parse filter: " + string;
				throw new IllegalArgumentException( msg );
			}
        }

        return filters;

	}

}

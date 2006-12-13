package org.geoserver.wfs.http.kvp;

import java.util.Iterator;
import java.util.List;

import net.opengis.ows.v1_0_0.AcceptFormatsType;
import net.opengis.ows.v1_0_0.OWSFactory;

import org.geoserver.http.util.KvpUtils;
import org.geoserver.ows.http.KvpParser;

/**
 * Parses a kvp of the form "acceptFormats=format1,format2,...,formatN" into 
 * an instnaceof {@link net.opengis.ows.v1_0_0.AcceptFormatsType}.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class AcceptFormatsKvpParser extends KvpParser {

	public AcceptFormatsKvpParser() {
		super( "acceptFormats", AcceptFormatsType.class );
	}
	
	public Object parse(String value) throws Exception {
		List values = KvpUtils.readFlat( value );
		
		AcceptFormatsType acceptFormats = OWSFactory.eINSTANCE.createAcceptFormatsType();
		for ( Iterator v = values.iterator(); v.hasNext(); ) {
			acceptFormats.getOutputFormat().add( v.next() );
		}
		
		return acceptFormats;
	}
	

}

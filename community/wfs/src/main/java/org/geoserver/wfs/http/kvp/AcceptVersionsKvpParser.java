package org.geoserver.wfs.http.kvp;

import net.opengis.ows.v1_0_0.AcceptVersionsType;
import net.opengis.ows.v1_0_0.OWSFactory;

import org.geoserver.http.util.KvpUtils;
import org.geoserver.ows.http.KvpParser;

public class AcceptVersionsKvpParser extends KvpParser {

	
	public AcceptVersionsKvpParser() {
		super( "acceptversions", AcceptVersionsType.class );
	}

	public Object parse(String value) throws Exception {
		AcceptVersionsType acceptVersions = OWSFactory.eINSTANCE.createAcceptVersionsType();
		acceptVersions.getVersion().addAll( KvpUtils.readFlat( value, "," ) );
	
		return acceptVersions;
	}

}

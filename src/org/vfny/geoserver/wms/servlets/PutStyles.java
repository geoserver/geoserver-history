package org.vfny.geoserver.wms.servlets;

import java.util.Map;

import javax.servlet.ServletContext;

import org.vfny.geoserver.Response;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wms.requests.PutStylesKvpReader;
import org.vfny.geoserver.wms.responses.PutStylesResponse;

public class PutStyles extends WMService {

	ServletContext context;
	
	public PutStyles(ServletContext context) {
		this.context = context;
	}
	
	protected Response getResponseHandler() {
		return new PutStylesResponse(context);
	}

	protected KvpRequestReader getKvpReader(Map params) {
		return new PutStylesKvpReader(params);
	}

	protected XmlRequestReader getXmlRequestReader() {
		 throw new UnsupportedOperationException(
         	"request does not defines a POST encoding"
		 );
	}

}

package org.geoserver.rest;

import java.util.HashMap;
import java.util.Map;

import org.geoserver.test.GeoServerTestSupport;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;

public class MapResourceTest extends GeoServerTestSupport {
	
	private static class ExceptionThrowingMapResource extends MapResource{
		private Status myStatus;
		private Representation myRepresentation;
		
		public ExceptionThrowingMapResource(Representation rep, Status stat){
			myStatus = stat;
			myRepresentation = rep;
		}
		
		@Override
		public Object getMap() throws RestletException {
			throw new RestletException(myRepresentation, myStatus);
		}
		
		@Override
		public Map getSupportedFormats() {
			Map m = new HashMap();
			m.put("xml", new AutoXMLFormat());
			m.put(null, m.get("xml"));
			return m;
		}
		
		public Status getStatus(){
			return myStatus;
		}
		
		public Representation getRepresentation(){
			return myRepresentation;
		}
	}
	
	private ExceptionThrowingMapResource myResource;
	
	public void setUpInternal() throws Exception{
		super.setUpInternal();
		myResource = new ExceptionThrowingMapResource(
				new StringRepresentation("Error"),
				Status.CLIENT_ERROR_BAD_REQUEST);
	}
	
	public void testExceptionHandling(){
		Context con = new Context();
		Request req = new Request();
		Response resp = new Response(req);
		
		myResource.init(con, req, resp);
		myResource.handleGet();
		
		assertEquals(resp.getStatus(), myResource.getStatus());
		assertEquals(resp.getEntity(), myResource.getRepresentation());
	}

}

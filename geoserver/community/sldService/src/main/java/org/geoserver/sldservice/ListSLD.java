package org.geoserver.sldservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.vfny.geoserver.global.Data;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.XmlRepresentation;
public class ListSLD extends Restlet {
		private Data dt;
		public ListSLD(Context context,Data dt){
			super(context);
			this.dt=dt;	
		}
		
		public void handle(Request request, Response response) {
				
			
			String message = this.xmlSldList(request);	
			response.setEntity(new StringRepresentation(message, 
					MediaType.TEXT_XML));
		}

		private String xmlSldList(Request req){
			XMLSerializer xml = new XMLSerializer();
			xml.setRootName("slds");
			return  xml.write(this.jsonSldList(req));
		}
		private  JSONObject jsonSldList(Request req){
			Reference ref=req.getResourceRef();
			String bUrl=ref.toString();
			Map out = new HashMap();
			Map styles=  dt.getStyles();
			Iterator it = styles.keySet().iterator();
			String name;
			
			while (it.hasNext()){
				name= (String) it.next();
				out.put(name, bUrl+"/"+name);			
			}  
			JSONObject json = JSONObject.fromObject(out);
			return json;
		}
		
}

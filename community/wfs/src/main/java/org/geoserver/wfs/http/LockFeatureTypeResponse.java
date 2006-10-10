package org.geoserver.wfs.http;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import net.opengis.wfs.WFSLockFeatureResponseType;

import org.geoserver.http.util.ResponseUtils;
import org.geoserver.ows.Operation;
import org.geoserver.ows.ServiceException;
import org.geoserver.ows.http.Response;
import org.geoserver.wfs.WFS;
import org.opengis.filter.FeatureId;

public class LockFeatureTypeResponse extends Response {

	WFS wfs;
	
	public LockFeatureTypeResponse( WFS wfs ) {
		super( WFSLockFeatureResponseType.class );
		this.wfs = wfs;
	}
	
	public String getMimeType(Operation operation) throws ServiceException {
		return "text/xml";
	}

	public void write(Object value, OutputStream output, Operation operation)
			throws IOException, ServiceException {
		
		WFSLockFeatureResponseType lockResponse = (WFSLockFeatureResponseType) value;
		
		String indent = wfs.isVerbose() ? "   " : "";
		BufferedWriter writer = new BufferedWriter( 
			new OutputStreamWriter( output )	
		);
		
		//TODO: get rid of this hardcoding, and make a common utility to get all
        //these namespace imports, as everyone is using them, and changes should
        //go through to all the operations.
		
	    writer.write( "<?xml version=\"1.0\" encoding=\""  + wfs.getCharSet().displayName() + "\"?>" );
	    writer.write( "<WFS_LockFeatureResponse " + "\n" );
	    writer.write( indent + "xmlns=\"http://www.opengis.net/wfs\" " + "\n" );
	    writer.write( indent + "xmlns:ogc=\"http://www.opengis.net/ogc\" " + "\n" );
	    
	    writer.write( indent + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + "\n" );
	    writer.write( indent + "xsi:schemaLocation=\"http://www.opengis.net/wfs ");
        writer.write( ResponseUtils.appendPath( wfs.getSchemaBaseURL() , "wfs/1.0.0/WFS-transaction.xsd" ) );
        writer.write( "\">" + "\n" );
	        
        writer.write( indent + "<LockId>" + lockResponse.getLockId() + "</LockId>" + "\n" );

        if ( !lockResponse.getFeaturesNotLocked().isEmpty() ) {
        	if ( !lockResponse.getFeaturesLocked().isEmpty() ) {

                for (Iterator i = lockResponse.getFeaturesLocked().iterator(); i.hasNext();) {
                    writer.write( indent + indent);
                   
                    FeatureId featureId = (FeatureId) i.next();
                    String fid = (String) featureId.getIDs().iterator().next();
                    
                    writer.write( "<ogc:FeatureId fid=\"" + fid + "\"/>" + "\n");
                }

                writer.write( indent + "</FeaturesLocked>" + "\n");
        	}
        	

        	writer.write( "<FeaturesNotLocked>" + "\n");

            for (Iterator i = lockResponse.getFeaturesNotLocked().iterator(); i.hasNext();) {
            	writer.write( indent + indent);
                
                FeatureId featureId = (FeatureId) i.next();
                String fid = (String) featureId.getIDs().iterator().next();
                
                writer.write( "<ogc:FeatureId fid=\"" + fid + "\"/>" + "\n");
            }

            writer.write( "</FeaturesNotLocked>" + "\n");
       }
        
       writer.write( "</WFS_LockFeatureResponse>");
       writer.flush();
	}

}

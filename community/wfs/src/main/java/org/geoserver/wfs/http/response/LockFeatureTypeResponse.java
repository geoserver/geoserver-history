package org.geoserver.wfs.http.response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import net.opengis.wfs.LockFeatureResponseType;

import org.geoserver.http.util.ResponseUtils;
import org.geoserver.ows.Operation;
import org.geoserver.ows.ServiceException;
import org.geoserver.ows.http.Response;
import org.geoserver.wfs.WFS;
import org.opengis.filter.identity.FeatureId;

public class LockFeatureTypeResponse extends Response {

	WFS wfs;
	
	public LockFeatureTypeResponse( WFS wfs ) {
		super( LockFeatureResponseType.class );
		this.wfs = wfs;
	}
	
	public String getMimeType(Operation operation) throws ServiceException {
		return "text/xml";
	}

	public void write(Object value, OutputStream output, Operation operation)
			throws IOException, ServiceException {
		
		LockFeatureResponseType lockResponse = (LockFeatureResponseType) value;
		
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

        List featuresLocked = lockResponse.getFeaturesLocked().getFeatureId();
        List featuresNotLocked = lockResponse.getFeaturesNotLocked().getFeatureId();
        if ( !featuresNotLocked.isEmpty() ) {
        	if ( !featuresLocked.isEmpty() ) {

                for (Iterator i = featuresLocked.iterator(); i.hasNext();) {
                    writer.write( indent + indent);
                   
                    FeatureId featureId = (FeatureId) i.next();
                    writer.write( "<ogc:FeatureId fid=\"" + featureId + "\"/>" + "\n");
                }

                writer.write( indent + "</FeaturesLocked>" + "\n");
        	}
        	

        	writer.write( "<FeaturesNotLocked>" + "\n");

            for (Iterator i = featuresNotLocked.iterator(); i.hasNext();) {
            	writer.write( indent + indent);
                
                FeatureId featureId = (FeatureId) i.next();
                writer.write( "<ogc:FeatureId fid=\"" + featureId + "\"/>" + "\n");
            }

            writer.write( "</FeaturesNotLocked>" + "\n");
       }
        
       writer.write( "</WFS_LockFeatureResponse>");
       writer.flush();
	}

}

package org.geoserver.wfs.http.response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

import net.opengis.wfs.ActionType;
import net.opengis.wfs.InsertResultsType;
import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionResultsType;

import org.geoserver.http.util.ResponseUtils;
import org.geoserver.ows.Operation;
import org.geoserver.ows.ServiceException;
import org.geoserver.ows.http.Response;
import org.geoserver.wfs.WFS;
import org.opengis.filter.FeatureId;

public class TransactionResponse extends Response {

	private boolean verbose = false;
    private String indent = " ";
    private String offset = "";
    
    WFS wfs;
    
	public TransactionResponse( WFS wfs ) {
		super( TransactionResponseType.class );
		this.wfs = wfs;
	}

	public String getMimeType(Operation operation) throws ServiceException {
		return "text/xml";
	}
	
	public void write(Object value, OutputStream output, Operation operation)
			throws IOException, ServiceException {
		
		TransactionResponseType response = (TransactionResponseType) value;
		TransactionResultsType result = response.getTransactionResults();
		
		Writer writer = new OutputStreamWriter( output );
        writer = new BufferedWriter(writer);
        
		//boolean verbose = ConfigInfo.getInstance().formatOutput();
        //String indent = ((verbose) ? "\n" + OFFSET : " ");
		String encoding = wfs.getCharSet().displayName();
        String xmlHeader = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
        writer.write( xmlHeader );
        
        if (verbose) {
            writer.write("\n");
        }

        writer.write("<wfs:WFS_TransactionResponse");
        writer.write(indent + "version=\"1.0.0\"" );
        writer.write(indent + "xmlns:wfs=\"http://www.opengis.net/wfs\"");

        //if (insertResults.size() > 0){
        writer.write(indent + "xmlns:ogc=\"http://www.opengis.net/ogc\"");

        //}
        writer.write(indent
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        writer.write(indent);
        writer.write("xsi:schemaLocation=\"http://www.opengis.net/wfs ");
        String baseUrl = 
        	ResponseUtils.appendPath( wfs.getSchemaBaseURL(), "wfs/1.0.0/WFS-transaction.xsd" );
        
        writer.write(baseUrl);
        writer.write("\">");

        InsertResultsType insertResults = response.getInsertResults();
        if ( insertResults != null ) {
        	for ( Iterator i = insertResults.getFeature().iterator(); i.hasNext(); ) {
    			InsertedFeatureType insertedFeature = (InsertedFeatureType) i.next();
        	
        		writer.write( "<wfs:InsertResult" );
            	if ( insertedFeature.getHandle() != null ) {
            		writer.write( " handle=\"" + insertedFeature.getHandle() + "\"" );
            	}
            	writer.write( ">" );	
            	
            	for ( Iterator id = insertedFeature.getFeatureId().iterator(); id.hasNext(); ) {
            		FeatureId featureId = (FeatureId) id.next();
            		for ( Iterator s = featureId.getIDs().iterator(); s.hasNext(); ) {
            			String fid = (String) s.next();
            			writer.write( "<ogc:FeatureId fid=\"" + fid + "\"/>" );
            		}
            	}
            	
            	writer.write( "</wfs:InsertResult>" );
        	}
            
        }
        
        
        writer.write(indent + "<wfs:TransactionResult");
        
        if ( result.getHandle() != null) {
            writer.write(" handle=\"" + result.getHandle() + "\"");
        }

        writer.write(">");
        writer.write(indent + offset + "<wfs:Status>");
        writer.write(indent + offset + offset);

        //if there is an actino, that means we failed
        if ( !result.getAction().isEmpty() ) {
        	writer.write("<wfs:FAILED/>");
        }
        else {
        	writer.write("<wfs:SUCCESS/>");	
        }
    	
        writer.write(indent + offset + "</wfs:Status>");

        if ( !result.getAction().isEmpty() ) {
			ActionType action = (ActionType) result.getAction().get( 0 );
			if ( action.getLocator() != null) {
				writer.write(indent + offset + "<wfs:Locator>");
				writer.write( action.getLocator() + "</wfs:Locator>");
			}
  
			if ( action.getMessage() != null) {
				writer.write(indent + offset + "<wfs:Message>");
				ResponseUtils.writeEscapedString(writer, action.getMessage() );
				writer.write("</wfs:Message>");
			}
        }
        
        writer.write(indent + "</wfs:TransactionResult>");

        if (verbose) {
            writer.write("\n");
        }

        writer.write("</wfs:WFS_TransactionResponse>");
        writer.flush();
        writer.close();
	}

}

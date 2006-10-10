package org.geoserver.wfs.http;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

import net.opengis.wfs.InsertResultType;
import net.opengis.wfs.TransactionResultType;
import net.opengis.wfs.WFSTransactionResponseType;

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
		super( WFSTransactionResponseType.class );
		this.wfs = wfs;
	}

	public String getMimeType(Operation operation) throws ServiceException {
		return "text/xml";
	}
	
	public void write(Object value, OutputStream output, Operation operation)
			throws IOException, ServiceException {
		
		WFSTransactionResponseType response = (WFSTransactionResponseType) value;
		TransactionResultType result = response.getTransactionResult();
		
		Writer writer = new OutputStreamWriter( output );
        writer = new BufferedWriter(writer);
        
		//boolean verbose = ConfigInfo.getInstance().formatOutput();
        //String indent = ((verbose) ? "\n" + OFFSET : " ");
		String encoding = wfs.getCharSet().displayName();
        String xmlHeader = "<?xml version=\"1.0\" encoding=\"" + encoding
            + "\"?>";
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

        for ( Iterator i = response.getInsertResult().iterator(); i.hasNext(); ) {
        	InsertResultType insertResult = (InsertResultType) i.next();
        	writer.write( "<wfs:InsertResult" );
        	if ( insertResult.getHandle() != null ) {
        		writer.write( " handle=\"" + insertResult.getHandle() + "\"" );
        	}
        	writer.write( ">" );
        	
        	for ( Iterator f = insertResult.getFeatureId().iterator(); f.hasNext(); ) {
        		FeatureId featureId = (FeatureId) f.next();
        		for ( Iterator s = featureId.getIDs().iterator(); s.hasNext(); ) {
        			String fid = (String) s.next();
        			writer.write( "<ogc:FeatureId fid=\"" + fid + "\"/>" );
        		}
        	}
        	writer.write( "</wfs:InsertResult>" );
        }
        
        writer.write(indent + "<wfs:TransactionResult");
        
        if ( result.getHandle() != null) {
            writer.write(" handle=\"" + result.getHandle() + "\"");
        }

        writer.write(">");
        writer.write(indent + offset + "<wfs:Status>");
        writer.write(indent + offset + offset);

        if ( result.getStatus().getSUCCESS() != null ) {
        	writer.write("<wfs:SUCCESS/>");
        }
        else if ( result.getStatus().getPARTIAL() != null ) {
        	writer.write("<wfs:PARTIAL/>");
        }
        else if ( result.getStatus().getFAILED() != null ) {
        	writer.write("<wfs:FAILED/>");
        }
        
        writer.write(indent + offset + "</wfs:Status>");

        if ( result.getLocator() != null) {
            writer.write(indent + offset + "<wfs:Locator>");
            writer.write( result.getLocator() + "</wfs:Locator>");
        }

        if ( result.getMessage() != null) {
            writer.write(indent + offset + "<wfs:Message>");
            ResponseUtils.writeEscapedString(writer, result.getMessage() );
            writer.write("</wfs:Message>");
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

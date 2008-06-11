package org.geoserver.rest;

import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.data.Status;
import org.restlet.data.MediaType;

/**
 * An exception that specifies the Restlet representation and status code that
 * should be used to report it to the user.
 * 
 * @author David Winslow
 */
public class RestletException extends RuntimeException {
    Status myStatus;
    Representation myRepresentation;

    /**
     * @param r The Representation to report this error to the user
     * @param stat The Status to report to the client
     * @param e The actual Exception that occurred
     */
    public RestletException(Representation r, Status stat, Exception e){
    	super(e);
    	init(r, stat);
    }
    /**
     * @param r The Representation to report this error to the user
     * @param stat The Status to report to the client
     */
    public RestletException(Representation r, Status stat){
        init(r, stat);
    }

    /**
     * @param s The message to report this error to the user (will report mimetype as text/plain)
     * @param stat The Status to report to the client
     * @param e The actual Exception that occurred
     */
    public RestletException(String s, Status stat, Exception e){
    	super(e);
        init(new StringRepresentation(s + ":" + e.getMessage(), MediaType.TEXT_PLAIN), stat);
    }
    
    /**
     * @param s The message to report this error to the user (will report mimetype as text/plain)
     * @param stat The Status to report to the client
     */    
    public RestletException(String s, Status stat){
        init(new StringRepresentation(s, MediaType.TEXT_PLAIN), stat);
    }

    /**
     * Internal helper function so we can call the super constructor and still share initialization code within this class
     */
    private void init(Representation r, Status s){
    	myRepresentation = r;
    	myStatus = s;
    }

    /**
     * @return The Status associated with this exception
     */
    public Status getStatus(){
        return myStatus;
    }

    /**
     * @return the Representation associated with this exception
     */
    public Representation getRepresentation(){
        return myRepresentation;
    }
}

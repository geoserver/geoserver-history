package org.vfny.geoserver.issues;

public class IssueException extends Exception {

    /** long serialVersionUID field */
    private static final long serialVersionUID = -7581168927160848288L;

    public IssueException(){
        super();
    }

    public IssueException(String error){
        super(error);
    }
}
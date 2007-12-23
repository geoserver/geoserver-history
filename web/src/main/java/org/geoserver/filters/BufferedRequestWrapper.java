package org.geoserver.filters;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletInputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class BufferedRequestWrapper extends HttpServletRequestWrapper{
    protected HttpServletRequest myWrappedRequest;
    protected String myBuffer;
    protected ServletInputStream myStream = null;
    protected BufferedReader myReader = null;

    public BufferedRequestWrapper(HttpServletRequest req, String buff){
        super(req);
        myWrappedRequest = req;
        myBuffer = buff;
    }

    public ServletInputStream getInputStream() throws IOException{
        if (myStream == null){
            if (myReader == null){
                myStream = new BufferedRequestStream(myBuffer);
            } else {
                throw new IOException("Requesting a stream after a reader is already in use!!");
            }
        }

        return myStream;
    }

    public BufferedReader getReader() throws IOException{
        if (myReader == null){
            if (myStream == null){

                myReader = new BufferedReader(
                        new InputStreamReader(
                            new BufferedRequestStream(myBuffer) 
                            )
                        );
            } else {
                throw new IOException("Requesting a reader after a stream is already in use!!");
            }
        } 

        return myReader;
    }
    
}

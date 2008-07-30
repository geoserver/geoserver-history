package org.geoserver.web.acegi;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;

import org.geoserver.web.GeoServerApplication;

@SuppressWarnings("serial")
public class GeoServerSession extends WebSession{

    private Authentication myAuthentication;

    public GeoServerSession(Request request) {
        super(request);
    }

    public static GeoServerSession get() {
        return (GeoServerSession)Session.get();
    }

    public boolean authenticate(String username, String password){
        if (username == null) username = "";
        if (password == null) password = "";

        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(username, password);

        try{
            Authentication authentication = 
                findAuthenticationManager((GeoServerApplication)getApplication()).authenticate(authToken);

            setAuthentication(authentication);
            return true;
        } catch (BadCredentialsException e){
            //TODO: Log
        } catch (AuthenticationException e){
            //TODO: Log
        }
        return false;
    }

    private void setAuthentication(Authentication auth){
        myAuthentication = auth;
    }

    public Authentication getAuthentication(){
        return myAuthentication;
    }

    public void signout(){
        setAuthentication(null);
    }

    private static AuthenticationManager findAuthenticationManager(GeoServerApplication application){
        return (AuthenticationManager)application.getApplicationContext().getBean("authenticationManager");
    }
}

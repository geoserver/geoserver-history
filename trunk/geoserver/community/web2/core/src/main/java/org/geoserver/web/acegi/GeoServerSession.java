package org.geoserver.web.acegi;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.ui.rememberme.RememberMeServices;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.WebRequestCycle;

import org.geoserver.web.GeoServerApplication;

@SuppressWarnings("serial")
public class GeoServerSession extends WebSession{
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
            RememberMeServices rememberMeService = 
                findRememberMeServices((GeoServerApplication)getApplication());
            if (rememberMeService != null){
                rememberMeService.loginSuccess(
                        ((WebRequestCycle)WebRequestCycle.get()).getWebRequest().getHttpServletRequest(), 
                        ((WebRequestCycle)WebRequestCycle.get()).getWebResponse().getHttpServletResponse(), 
                        authentication
                        );
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (BadCredentialsException e){
            //TODO: Log
        } catch (AuthenticationException e){
            //TODO: Log
        }
 
        return false;
    }

    public Authentication getAuthentication(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null &&
                auth.getAuthorities().length == 1 &&
                "ROLE_ANONYMOUS".equals(auth.getAuthorities()[0].getAuthority())
           ) return null;

        return auth;
    }

    public void signout(){
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private static RememberMeServices findRememberMeServices(GeoServerApplication application){
        return (RememberMeServices)application.getApplicationContext().getBean("rememberMeServices");
    }

    private static AuthenticationManager findAuthenticationManager(GeoServerApplication application){
        return (AuthenticationManager)application.getApplicationContext().getBean("authenticationManager");
    }
}

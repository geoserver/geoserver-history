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

    public Authentication getAuthentication(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null &&
                auth.getAuthorities().length == 1 &&
                "ROLE_ANONYMOUS".equals(auth.getAuthorities()[0].getAuthority())
           ) return null;

        return auth;
    }
}

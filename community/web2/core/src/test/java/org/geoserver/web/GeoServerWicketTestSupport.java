package org.geoserver.web;

import org.geoserver.test.GeoServerTestSupport;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.apache.wicket.util.tester.WicketTester;

public abstract class GeoServerWicketTestSupport extends GeoServerTestSupport {
    public WicketTester tester;

    public void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
        GeoServerApplication app = 
            (GeoServerApplication) applicationContext.getBean("webApplication");
        tester = new WicketTester(app);
        app.init();
    }

    public GeoServerApplication getGeoServerApplication(){
        return GeoServerApplication.get();
    }

    public void login(){
        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(
            "admin",
            "geoserver",
            new GrantedAuthority[]{
                new GrantedAuthorityImpl("ROLE_ADMINISTRATOR")
            }
            )
        );
    }

    public void logout(){
        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(
            "anonymousUser",
            "",
            new GrantedAuthority[]{
                new GrantedAuthorityImpl("ROLE_ANONYMOUS")
            }
            )
        );
    }
}

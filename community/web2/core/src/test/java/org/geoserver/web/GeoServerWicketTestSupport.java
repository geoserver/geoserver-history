package org.geoserver.web;

import java.util.Locale;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.apache.wicket.Component;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IFormVisitorParticipant;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.geoserver.test.GeoServerTestSupport;
import org.geoserver.web.wicket.WicketHierarchyPrinter;

public abstract class GeoServerWicketTestSupport extends GeoServerTestSupport {
    public static WicketTester tester;

    public void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
        // prevent Wicket from bragging about us being in dev mode (and run
        // the tests as if we were in production all the time)
        System.setProperty("wicket.configuration", "deployment");
        
        // make sure that we check the english i18n when needed
        Locale.setDefault(Locale.ENGLISH);
        
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
    
    /**
     * Prints the specified component/page containment hierarchy to the standard output
     * <p>
     * Each line in the dump looks like: <componentId>(class) 'value'
     * @param c the component to be printed
     * @param dumpClass if enabled, the component classes are printed as well
     * @param dumpValue if enabled, the component values are printed as well
     */
    public void print(Component c, boolean dumpClass, boolean dumpValue) {
        WicketHierarchyPrinter.print(c, dumpClass, dumpValue);
    }
    
    public void prefillForm(final FormTester tester) {
        Form form = tester.getForm();
        form.visitChildren(new Component.IVisitor() {
            
            public Object component(Component component) {
                if(component instanceof FormComponent) {
                    FormComponent fc = (FormComponent) component;
                    String name = fc.getInputName();
                    String value = fc.getValue();
                    
                    tester.setValue(name, value);
                }
                return Component.IVisitor.CONTINUE_TRAVERSAL;
            }
        });
    }
}

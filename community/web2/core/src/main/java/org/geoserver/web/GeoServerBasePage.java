/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.acegisecurity.Authentication;
import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Resource;
import org.apache.wicket.Session;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.breadcrumb.BreadCrumbBar;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServer;
import org.geoserver.web.acegi.GeoServerSession;
import org.geoserver.web.admin.ServerAdminPage;

/**
 * Base class for web pages in GeoServer web application.
 * <ul>
 * <li>The basic layout</li>
 * <li>An OO infrastructure for common elements location</li>
 * <li>An infrastructure for locating subpages in the Spring context and
 * creating links</li>
 * </ul>
 * 
 * TODO: breadcrumb automated cration. This can be done by using a list of
 * {@link BookmarkablePageInfo} instances that needs to be passed to each page,
 * a custom PageLink subclass that provides that information, and some code
 * coming from {@link BreadCrumbBar}. <br>
 * See also this discussion on the wicket users mailing list:
 * http://www.nabble.com/Bread-crumbs-based-on-pages%2C-not-panels--tf2244730.html#a6225855
 * 
 * @author Andrea Aaime, The Open Planning Project
 * @author Justin Deoliveira, The Open Planning Project
 */
public class GeoServerBasePage extends WebPage {

	@SuppressWarnings("serial")
    public GeoServerBasePage() {

        // login form
        WebMarkupContainer loginForm = new WebMarkupContainer("loginform");
        add(loginForm);
        final Authentication user = GeoServerSession.get().getAuthentication();
        final boolean anonymous = user == null;
        loginForm.setVisible(anonymous);

        WebMarkupContainer logoutForm = new WebMarkupContainer("logoutform");
        logoutForm.setVisible(user != null);

        add(logoutForm);
        logoutForm.add(new Label("username", anonymous ? "Nobody" : user.getName()));

        // home page link
        add( new BookmarkablePageLink( "home", GeoServerHomePage.class )
            .add( new Label( "label", new StringResourceModel( "home", (Component)null, null ) )  ) );
        
        // dev buttons
        WebMarkupContainer devButtons = new WebMarkupContainer("devButtons");
        add(devButtons);
        devButtons.add(new AjaxFallbackLink("clearCache"){
            @Override
            public void onClick(AjaxRequestTarget target) {
                getGeoServerApplication().clearWicketCaches();
            }
        });

        final Map<Category,List<MenuPageInfo>> links = splitByCategory(
            getGeoServerApplication().getBeansOfType(MenuPageInfo.class)
        );

        List<MenuPageInfo> standalone = links.containsKey(null) 
            ? links.get(null)
            : new ArrayList<MenuPageInfo>();
        links.remove(null);

        List<Category> categories = new ArrayList(links.keySet());
        Collections.sort(categories);

        add(new ListView("category", categories){
            public void populateItem(ListItem item){
                Category category = (Category)item.getModelObject();
                item.add(new Label("category.header", new StringResourceModel(category.getNameKey(), (Component) null, null)));
                item.add(new ListView("category.links", links.get(category)){
                    public void populateItem(ListItem item){
                        MenuPageInfo info = (MenuPageInfo)item.getModelObject();
                        BookmarkablePageLink link = new BookmarkablePageLink("link", info.getComponentClass());
                        link.add(new Label("link.label", new StringResourceModel(info.getTitleKey(), (Component) null, null)));
                        if(info.getIcon() != null) {
                            link.add(new Image("link.icon", new ResourceReference(info.getComponentClass(), info.getIcon())));
                        } else {
                            link.add(new Image("link.icon", new ResourceReference(GeoServerBasePage.class, "img/icons/silk/wrench.png")));
                        }
                        item.add(link);
                    }
                });
            }
        });

        add(new ListView("standalone", standalone){
                    public void populateItem(ListItem item){
                        MenuPageInfo info = (MenuPageInfo)item.getModelObject();
                        item.add(new BookmarkablePageLink("link", info.getComponentClass())
                            .add(new Label("link.label", new StringResourceModel(info.getTitleKey(), (Component) null, null)))
                        );
                    }
                }
        );

        devButtons.setVisible(Application.DEVELOPMENT.equalsIgnoreCase(
                getApplication().getConfigurationType())); 
        add(new FeedbackPanel("feedback"));
    }

    /**
     * Returns the application instance.
     */
    protected GeoServerApplication getGeoServerApplication() {
        return (GeoServerApplication) getApplication();
    }
    
    @Override
    public GeoServerSession getSession() {
        return (GeoServerSession) super.getSession();
    }

    /**
     * Convenience method for pages to get access to the geoserver
     * configuration.
     */
    protected GeoServer getGeoServer() {
        return getGeoServerApplication().getGeoServer();
    }

    /**
     * Convenience method for pages to get access to the catalog.
     */
    protected Catalog getCatalog() {
        return getGeoServerApplication().getCatalog();
    }
    
    @SuppressWarnings("serial")
    private static class SignInForm extends StatelessForm {
        private String password;
        private String username;

        public SignInForm(final String id){
            super(id);
            setModel(new CompoundPropertyModel(this));
            add(new TextField("username"));
            add(new PasswordTextField("password"));
        }

        @Override
        public final void onSubmit(){
            if (username == null || username.length() == 0){
                Session.get().warn("No username provided!");
                return;
            }

            if (signIn(username, password)){
                if (!continueToOriginalDestination()) {
                    setResponsePage(getApplication().getHomePage());
                    Session.get().info("You have successfully signed in!");
                }
            } else {
                Session.get().error("Unknown username/password");
            }
        }

        private final boolean signIn(String username, String password) {
            return GeoServerSession.get().authenticate(username, password);
        }
    }

    private static Map<Category,List<MenuPageInfo>> splitByCategory(List<MenuPageInfo> pages){
        Collections.sort(pages);
        HashMap<Category,List<MenuPageInfo>> map = new HashMap<Category,List<MenuPageInfo>>();

        for (MenuPageInfo page : pages){
            Category cat = page.getCategory();

            if (!map.containsKey(cat)) 
                map.put(cat, new ArrayList<MenuPageInfo>());

            map.get(cat).add(page);
        }

        return map;
    }
}

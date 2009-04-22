/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.security.user;

import java.util.logging.Level;

import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.security.GeoserverUserDao;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.wicket.ConfirmationAjaxLink;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

/**
 * A page listing users, allowing for removal, addition and linking to an edit page
 */
@SuppressWarnings("serial")
public class UserPage extends GeoServerSecuredPage {

    private GeoServerTablePanel<User> users;

    public UserPage() {
        UserListProvider provider = new UserListProvider();
        users = new GeoServerTablePanel<User>("table", provider) {

            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<User> property) {
                if (property == UserListProvider.USERNAME) {
                    return editUserLink(id, itemModel, property);
                } else if (property == UserListProvider.ROLES) {
                    return new Label(id, property.getModel(itemModel));
                } else if (property == UserListProvider.ADMIN) {
                    return new Label(id, property.getModel(itemModel));
                } else if (property == UserListProvider.REMOVE) {
                    // don't allow one to remove the admin
                    if("admin".equals(((User) itemModel.getObject()).getUsername()))
                        return new Label(id, new Model("-"));
                    else
                        return removeUserLink(id, itemModel);
                } else {
                    throw new RuntimeException("Uknown property " + property);
                }

            }
        };
        users.setOutputMarkupId(true);
        add(users);
        add(addUserLink());
    }
    
    ConfirmationAjaxLink removeUserLink(String id, IModel itemModel) {
        String username = ((User) itemModel.getObject()).getUsername();
        IModel confirmRemoveModel = new ParamResourceModel("confirmRemoveUser", this, username);
        return new ConfirmationAjaxLink(id, itemModel, new ParamResourceModel("removeUser", this, username), confirmRemoveModel) {

            @Override
            protected void onClick(AjaxRequestTarget target) {
                try {
                    String username = ((User) getModelObject()).getUsername();
                    if(username.equals("admin"));
                    GeoserverUserDao dao = GeoserverUserDao.get();
                    dao.removeUser(username);
                    dao.storeUsers();
                } catch(Exception e) {
                    LOGGER.log(Level.SEVERE, "Error occurred while removing the user and saving out the result", e);
                    error(new ParamResourceModel("saveError", this, e.getMessage()));
                    target.addComponent(feedbackPanel);
                }
                target.addComponent(users);
            }
            
        };
    }

    AjaxLink addUserLink() {
        return new AjaxLink("addUser", new Model()) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(new NewUserPage());
            }

        };
    }

    Component editUserLink(String id, IModel itemModel, Property<User> property) {
        return new SimpleAjaxLink(id, itemModel, property.getModel(itemModel)) {

            @Override
            protected void onClick(AjaxRequestTarget target) {
                setResponsePage(new EditUserPage((UserDetails) getModelObject()));
            }

        };
    }

}

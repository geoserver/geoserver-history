package org.geoserver.web.acegi;

import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;

import org.geoserver.web.GeoServerBasePage;

public class SigninPage extends GeoServerBasePage {
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
            if (signIn(username, password)){
                if (!continueToOriginalDestination()) {
                    setResponsePage(getApplication().getHomePage());
                }
            } else {
                error("Unknown username/password");
            }
        }

        private final boolean signIn(String username, String password) {
            return GeoServerSession.get().authenticate(username, password);
        }
    }

    public SigninPage(){
        add(new SignInForm("signin"));
    }
}

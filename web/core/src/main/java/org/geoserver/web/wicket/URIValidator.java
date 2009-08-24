package org.geoserver.web.wicket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

@SuppressWarnings("serial")
public class URIValidator extends AbstractValidator {

    @Override
    protected void onValidate(IValidatable validatable) {
        String uri = (String) validatable.getValue();
        try {
            new URI(uri);
        } catch(Exception e) {
            error(validatable, "invalidURI", Collections.singletonMap("uri", uri));
        }

    }

}

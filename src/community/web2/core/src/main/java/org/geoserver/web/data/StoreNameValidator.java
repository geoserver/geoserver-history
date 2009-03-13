package org.geoserver.web.data;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.web.GeoServerApplication;

/**
 * Check it's not already configured (we assume another validator already dealt
 * with the value non being null or empty, so here we just have to check it's
 * not in the catalog
 * 
 * @author Andrea Aime - OpenGeo
 */
@SuppressWarnings("serial")
public class StoreNameValidator implements IValidator {
    Class<? extends StoreInfo> storeType;

    public StoreNameValidator(Class<? extends StoreInfo> storeType) {
        this.storeType = storeType;
    }

    public void validate(IValidatable validatable) {
        String value = (String) validatable.getValue();

        Catalog catalog = GeoServerApplication.get().getCatalog();
        if (catalog.getStoreByName(value, storeType) != null) {
            ValidationError error = new ValidationError();
            error.setMessage("The Data Source name '" + value
                    + "' is already used");
            validatable.error(error);
        }
    }

}

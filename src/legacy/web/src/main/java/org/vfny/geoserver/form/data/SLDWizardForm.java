/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionForm;


public class SLDWizardForm extends ActionForm {
    private String selectedFeatureTypeName;

    public String getTypeName() {
        return selectedFeatureTypeName;
    }

    public void setTypeName(String typeName) {
        this.selectedFeatureTypeName = typeName;
    }
}

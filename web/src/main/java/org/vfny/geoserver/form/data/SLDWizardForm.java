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

package org.geoserver.security.web;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class EditablePanel extends Panel {

	public EditablePanel(String id, IModel inputModel) {
		super(id);

		TextField field = new TextField("textfield", inputModel);
		add(field);

		field.add(new AjaxFormComponentUpdatingBehavior("onblur") {
			protected void onUpdate(AjaxRequestTarget target) {
			}
		});
	}
}

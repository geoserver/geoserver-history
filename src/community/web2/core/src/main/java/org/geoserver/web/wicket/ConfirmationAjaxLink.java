package org.geoserver.web.wicket;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * A {@link SimpleAjaxLink} that asks a confirmation by using a Javascript confirm
 * dialog before carrying out its job
 * @author Andrea Aime - OpenGeo
 *
 */
public abstract class ConfirmationAjaxLink extends SimpleAjaxLink {
    IModel confirm;

    public ConfirmationAjaxLink(String id, IModel linkModel, IModel labelModel,
            IModel confirm) {
        super(id, linkModel, labelModel);
        this.confirm = confirm;
    }

    @Override
    protected AjaxLink buildAjaxLink(IModel linkModel) {
        return new AjaxLink("link", linkModel) {

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                return new AjaxPreprocessingCallDecorator(super
                        .getAjaxCallDecorator()) {

                    @Override
                    public CharSequence preDecorateScript(CharSequence script) {
                        return "if(!confirm('" + confirm.getObject()
                                + "')) return false;" + script;
                    }
                };
            }

            @Override
            public void onClick(AjaxRequestTarget target) {
                ConfirmationAjaxLink.this.onClick(target);
            }

        };
    }

}

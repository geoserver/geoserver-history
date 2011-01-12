package org.geoserver.gwc.web;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class StatusBar extends Panel {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("deprecation")
    public StatusBar(final String id, final IModel<Number> limitModel,
            final IModel<Number> progressModel) {
        super(id);
        add(HeaderContributor.forCss(StatusBar.class, "statusbar.css"));

        WebMarkupContainer usageBar = new WebMarkupContainer("statusBarProgress");
        WebMarkupContainer excessBar = new WebMarkupContainer("statusBarExcess");

        final double limit = limitModel.getObject().doubleValue();
        final double used = progressModel.getObject().doubleValue();
        final double excess = used - limit;

        int usedPercentage;
        int excessPercentage;

        final int progressWidth = 200;// progress bar with, i.e. 100%

        if (excess > 0) {
            excessPercentage = (int) Math.round((excess * progressWidth) / used);
            usedPercentage = progressWidth - excessPercentage;
        } else {
            usedPercentage = (int) Math.round(used * progressWidth / limit);
            excessPercentage = 0;
        }

        usageBar.add(new AttributeModifier("style", true, new Model<String>("width: "
                + usedPercentage + "px;")));

        String redStyle = "width: " + excessPercentage + "px; left: "
                + (1 + progressWidth - excessPercentage) + "px;";
        excessBar.add(new AttributeModifier("style", true, new Model<String>(redStyle)));

        add(usageBar);
        add(excessBar);
    }

}

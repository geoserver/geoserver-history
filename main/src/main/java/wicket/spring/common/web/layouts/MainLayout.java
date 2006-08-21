package wicket.spring.common.web.layouts;

import wicket.markup.html.basic.Label;
import wicket.markup.html.border.Border;
import wicket.markup.html.link.ExternalLink;
import wicket.model.StringResourceModel;

public class MainLayout extends Border {

	public MainLayout(String id) {
		super(id);
		//TODO: create & add any components that always appear on every page
        //e.g. add(new MenuPanel());
		add(new Label("pageTitle", new StringResourceModel("page.title", this, null)));
		add(new ExternalLink("linkGeoserver", new StringResourceModel("link.geoserver", this, null)));
		add(new ExternalLink("linkLicense", new StringResourceModel("link.license", this, null)));
		//TODO get Title from GeoServer bean
		add(new Label("title", "My GeoServer"));
		add(new ExternalLink("labelCreditsUrl", new StringResourceModel("label.credits.url", this, null)));
	}

//	/* (non-Javadoc)
//	 * @see wicket.MarkupContainer#newMarkupResourceStream(java.lang.Class)
//	 */
//	public IResourceStream newMarkupResourceStream(Class layoutClass) {
//		return new UrlResourceStream(layoutClass.getResource("MainLayout.html"));
//	}
	
}

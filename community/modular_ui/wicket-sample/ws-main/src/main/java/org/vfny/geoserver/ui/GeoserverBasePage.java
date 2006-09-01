package org.vfny.geoserver.ui;

import java.util.List;

import wicket.AttributeModifier;
import wicket.behavior.HeaderContributor;
import wicket.extensions.markup.html.repeater.data.ListDataProvider;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.WebPage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.image.Image;
import wicket.markup.html.link.ExternalLink;
import wicket.markup.html.link.PageLink;
import wicket.model.Model;
import wicket.model.PropertyModel;

public abstract class GeoserverBasePage extends WebPage {
	protected String message;

	public GeoserverBasePage() {
		// title
		add(new Label("title", new Model("Geoserver " + getHeaderTitle())));
		// meta - description
		WebMarkupContainer metaDescription = new WebMarkupContainer(
				"metaDescription");
		metaDescription.add(new AttributeModifier("content", true, new Model(
				getPageDescription())));
		add(metaDescription);
		// meta - keyword
		WebMarkupContainer metaKeywords = new WebMarkupContainer("metaKeywords");
		metaKeywords.add(new AttributeModifier("content", true, new Model(
				getKeywords())));
		add(metaKeywords);
		// css style (as a resource from classpath)
		add(HeaderContributor.forCss(GeoserverBasePage.class, "style.css"));
		add(new Image("logo", "logo.gif"));

		// dynamic error messages
		add(new Label("messages", new PropertyModel(this, "message")));

		// subpages menu
		add(new ComponentDataView("menu", new ListDataProvider(
				getSubPageLinks())));

		// title and abstract
		add(new Label("pageTitle", new Model(getPageTitle())));
		add(new Label("pageAbstract", new Model(getPageAbstract())));
	}

	protected abstract String getPageTitle();

	protected abstract String getPageAbstract();

	protected abstract List getSubPageLinks();

	protected ExternalLink buildExternalLink(String href, String text,
			String title) {
		ExternalLink link = new ExternalLink("link", new Model(href));
		link.add(new AttributeModifier("title", new Model(title)));
		link.add(new Label("linkText", new Model(text)));
		return link;
	}

	protected PageLink buildPageLink(Class pageClass, String text, String title) {
		PageLink link = new PageLink("link", pageClass);
		link.add(new AttributeModifier("title", new Model(title)));
		link.add(new Label("linkText", new Model(text)));
		link.setAutoEnable(true);
		return link;
	}

	protected abstract String getHeaderTitle();

	protected String getKeywords() {
		return "keywords";
	}

	protected String getPageDescription() {
		return "This is the page description";
	}

	public String getMessage() {
		return message;
	}
}

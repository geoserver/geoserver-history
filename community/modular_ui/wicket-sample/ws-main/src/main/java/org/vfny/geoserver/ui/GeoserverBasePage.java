package org.vfny.geoserver.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vfny.geoserver.ui.plugin.BookmarkablePageInfo;

import wicket.AttributeModifier;
import wicket.ResourceReference;
import wicket.behavior.HeaderContributor;
import wicket.extensions.breadcrumb.BreadCrumbBar;
import wicket.extensions.markup.html.repeater.data.ListDataProvider;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.WebPage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.image.Image;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.model.Model;
import wicket.model.PropertyModel;

/**
 * Base page for all geoserver pages. Defines:
 * <ul>
 * <li>The basic layout</li>
 * <li>An OO infrastructure for common elements location</li>
 * <li>An infrastructure for locating subpages in the Spring context and
 * creating links</li>
 * </ul>
 * 
 * TODO: breadcrumb automated cration.
 * This can be done by using a list of {@link BookmarkablePageInfo} instances
 * that needs to be passed to each page, a custom PageLink subclass that provides
 * that information, and some code coming from {@link BreadCrumbBar}. <br>
 * See also this discussion on the wicket users mailing list:
 * http://www.nabble.com/Bread-crumbs-based-on-pages%2C-not-panels--tf2244730.html#a6225855
 * 
 * @author wolf
 * 
 */
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
		add(new Image("logo", new ResourceReference(GeoserverBasePage.class,
				"logo.gif")));

		// dynamic error messages
		add(new Label("messages", new PropertyModel(this, "message")));

		// subpages menu
		add(new ComponentDataView("menu", new ListDataProvider(
				getSubPageLinks())));

		// title and abstract
		add(new Label("pageTitle", new Model(getPageTitle())));
		add(new Label("pageAbstract", new Model(getPageAbstract())));

		// breadcrumbs
		add(new BreadCrumbBar("breadcrumb"));
	}

	protected abstract String getPageTitle();

	protected abstract String getPageAbstract();

	protected abstract List getSubPageLinks();

	/**
	 * Builds a standard page link given a bookmarkable page info object
	 * 
	 * @param pageInfo
	 * @return
	 */
	protected BookmarkablePageLink buildPageLink(BookmarkablePageInfo pageInfo) {
		BookmarkablePageLink link = new BookmarkablePageLink("link", pageInfo
				.getPageClass());
		link.add(new AttributeModifier("title", new Model(pageInfo
				.getLinkTooltip())));
		link.add(new Label("linkText", new Model(pageInfo.getLinkName())));
		link.setAutoEnable(true);
		return link;
	}

	/**
	 * Standard behaviour is that all pluggable sub-pages are built from
	 * instances of a specific subclass of bookmarkable page info.<br>
	 * This method looks up the page infos instances of the specified class and
	 * builds bookmarkable links out of them.
	 * 
	 * @param bookmarkablePageInfoClass
	 * @return
	 */
	protected List buildLinksForPageType(Class bookmarkablePageInfoClass) {
		// TODO: make sure the bookmarkable page info class is a subclass of
		// BookrmakablePageInfo
		List result = new ArrayList();
		List infos = getGeoserverApplication().getBeansOfType(
				bookmarkablePageInfoClass);
		for (Iterator it = infos.iterator(); it.hasNext();) {
			BookmarkablePageInfo info = (BookmarkablePageInfo) it.next();
			result.add(buildPageLink(info));
		}

		return result;
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

	protected GeoserverApplication getGeoserverApplication() {
		return (GeoserverApplication) getApplication();
	}
}

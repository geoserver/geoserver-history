package org.vfny.geoserver.ui.plugin;

/**
 * Basic informations to create links to a page: link name, tooltip and page
 * class. This should be internationalized. <br>
 * This class is intended for spring conext bean creation, in order to describe
 * page plugins. Subclasses should be created for links of a certain kind, so
 * that the Spring lookup mechanisms finds out only the desidered kind of page.
 * 
 * @author wolf
 * 
 */
public class BookmarkablePageInfo {
	String linkName;

	String linkTooltip;

	Class pageClass;

	public BookmarkablePageInfo(Class pageClass, String linkName,
			String linkTooltip) {
		super();
		this.pageClass = pageClass;
		this.linkName = linkName;
		this.linkTooltip = linkTooltip;
	}

	public String getLinkName() {
		return linkName;
	}

	public String getLinkTooltip() {
		return linkTooltip;
	}

	public Class getPageClass() {
		return pageClass;
	}
}

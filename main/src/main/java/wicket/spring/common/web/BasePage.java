package wicket.spring.common.web;

import wicket.markup.html.WebPage;
import wicket.markup.html.border.Border;
import wicket.spring.common.web.layouts.MainLayout;

/**
 * Base page class. This is mainly here to provide some consistent look and feel
 * 
 * @author Alessio Fabiani (AlFa)
 */
public abstract class BasePage extends WebPage {
	private Border border;

	/**
	 * Contruct
	 */
	public BasePage()
	{
		// Create border and add it to the page
		border = new MainLayout("border");
		border.setTransparentResolver(true);
		super.add(border);
	}

}
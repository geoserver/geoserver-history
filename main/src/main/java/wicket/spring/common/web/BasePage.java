package wicket.spring.common.web;

import wicket.markup.html.WebPage;
import wicket.markup.html.border.Border;
import wicket.model.StringResourceModel;
import wicket.spring.common.web.layouts.ApplicationLayer;
import wicket.spring.common.web.layouts.ConfigurationLayer;
import wicket.spring.common.web.layouts.MainLayout;

/**
 * Base page class. This is mainly here to provide some consistent look and feel
 * 
 * @author Alessio Fabiani (AlFa)
 */
public abstract class BasePage extends WebPage {
	private Border border;
	
	private Border layer;

	/**
	 * Contruct
	 */
	public BasePage()
	{
		// Create border and add it to the page
		final String layerType = new StringResourceModel("layer", this, null).getString();
		if(layerType.equals("application"))
			layer = new ApplicationLayer("layerBorder");
		else if(layerType.equals("configuration")) {
			layer = new ConfigurationLayer("layerBorder");
		}
		layer.setTransparentResolver(true);

		border = new MainLayout("border", layer);
		border.setTransparentResolver(true);
		super.add(border);
	}

}
package wicket.spring.common.web;

import wicket.markup.html.link.Link;
import wicket.spring.direct.web.DirectPage;
import wicket.spring.proxy.web.ProxyPage;
import wicket.spring.wcs.web.WCSHelloWorld;

/**
 * Welcome Page
 * 
 * @author Alessio Fabiani (AlFa)
 * 
 */
public class WelcomePage extends BasePage {
	public WelcomePage() {
		add(new Link("direct-link") {

			public void onClick() {
				setResponsePage(new DirectPage());
			}

		});

		add(new Link("proxy-link") {

			public void onClick() {
				setResponsePage(new ProxyPage());
			}

		});

		add(new Link("wcs-link") {

			public void onClick() {
				setResponsePage(new WCSHelloWorld());
			}

		});

	}
}

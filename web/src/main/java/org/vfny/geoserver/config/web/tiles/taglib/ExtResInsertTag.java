package org.vfny.geoserver.config.web.tiles.taglib;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.tiles.InsertTag;
import org.apache.struts.tiles.TilesUtil;
import org.apache.struts.util.ResponseUtils;

import sun.io.ByteToCharConverter;

public class ExtResInsertTag extends InsertTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8363215424989241582L;

	/**
	 * Do an include of specified page.
	 * This method is used internally to do all includes from this class. It delegates
	 * the include call to the TilesUtil.doInclude().
	 * @param page The page that will be included
	 * @throws ServletException - Thrown by call to pageContext.include()
	 * @throws IOException - Thrown by call to pageContext.include()
	 */
	protected void doInclude(String page)
		throws ServletException, IOException {
		TilesUtil.doInclude(page, pageContext);
/*		try {
			URL u =  pageContext.getServletContext().getResource(page);
			if (u != null) {
				this.processUrl(u.toString());
			}
		} catch (Exception e) {
			System.out.println("include problem: " + e.getMessage());
		}
*/		/*try {
			URL u =  pageContext.getServletContext().getResource(page);
			if (u != null) {
				final ByteToCharConverter btc = ByteToCharConverter.getDefault(); 
				InputStream in = (InputStream)u.getContent();
				byte[] buf = new byte[255];
				int numRead = in.read(buf);
				while (numRead != -1) {
					pageContext.getOut().write(btc.convertAll(buf), 0, numRead);
					numRead = in.read(buf);
				}
			}
		}
		catch (Exception e) {
			System.out.println("include problem: " + e.getMessage());
		}*/
	}
}

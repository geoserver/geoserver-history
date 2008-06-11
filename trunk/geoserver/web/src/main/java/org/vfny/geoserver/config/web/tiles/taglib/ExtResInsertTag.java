/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config.web.tiles.taglib;


/**
 * EXPERIMENT FOR THE UI Modularization
 *
 * @author fabiania
 *
 */
public class ExtResInsertTag /*extends InsertTag*/ {
    /**
     *
     */

    /*
    private static final long serialVersionUID = -8363215424989241582L;

    */

    /**
    * Do an include of specified page.
    * This method is used internally to do all includes from this class. It delegates
    * the include call to the TilesUtil.doInclude().
    * @param page The page that will be included
    * @throws ServletException - Thrown by call to pageContext.include()
    * @throws IOException - Thrown by call to pageContext.include()
    */

    /*
    protected void doInclude(String page)
    throws ServletException, IOException {
         //TilesUtil.doInclude(page, pageContext);
         final JspAwareRequestContext jsp = new JspAwareRequestContext(pageContext);
         jsp.
                         try {
          URL u =  pageContext.getServletContext().getResource(page);
          if (u != null) {
          this.processUrl(u.toString());
          }
          } catch (Exception e) {
          System.out.println("include problem: " + e.getMessage());
          }
                          try {
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
          }
    }*/
}

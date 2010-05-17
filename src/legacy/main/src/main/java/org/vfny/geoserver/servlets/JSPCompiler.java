/*
 * Created on Feb 23, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.servlets;

import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.util.Requests;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * JSPCompiler purpose.
 * <p>
 * Description of JSPCompiler ...
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dgricci $ (last modification)
 * @version $Id$
 */
public class JSPCompiler extends HttpServlet {
    String[] pages;
    int spot = 0;

    public void init(ServletConfig config) throws ServletException {
        ResourceBundle rb = ResourceBundle.getBundle(getClass().getName());
        int n = Integer.parseInt(rb.getString("numURLs"));
        pages = new String[n];

        for (int i = 0; i < n; i++) {
            pages[i] = rb.getString("url." + (i + 1));
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        if (spot < pages.length) {
            try {
                String base = Requests.getBaseUrl(request, null);
                ServletContext session = request.getSession().getServletContext();

                synchronized (session) {
                    UserContainer u2 = new UserContainer();
                    u2.setUsername("compiler");
                    session.setAttribute(UserContainer.SESSION_KEY, u2);
                }

                doLoad(base + pages[spot], response);

                synchronized (session) {
                    session.setAttribute(UserContainer.SESSION_KEY, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            spot = spot + 1;
        } else {
            doForward(response);
        }
    }

    private void doForward(HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html");

        ServletOutputStream os = response.getOutputStream();
        os.print("<html>\n");
        os.print("<head><title>GeoServer - Loading</title>\n");
        os.print("  <meta content=\"text/css\" http-equiv=\"content-style-type\">\n");
        os.print("  <style type=\"text/css\">\n");
        os.print("    <!-- @import url(\"/geoserver/style.css\"); -->\n");
        os.print("  </style>\n");
        os.print("  <link type=\"image/gif\" href=\"gs.gif\" rel=\"icon\"><!-- mozilla --> \n");
        os.print("  <link href=\"gs.ico\" rel=\"SHORTCUT ICON\"><!-- ie -->\n");
        os.print("</head>\n");
        os.print("<body onload=\"javascript:window.location.replace('welcome.do')\"><br><center>\n");
        os.print("<table width=\"60%\" height=\"60%\"><tr><td>\n");
        os.print("<center>\n");
        os.print("  <span class=\"project\">\n");
        os.print("    <a href=\"http://geoserver.org/\">GeoServer</a>\n");
        os.print("  </span>\n");
        os.print("  <span class=\"license\">\n");
        os.print("    <a href=\"http://geoserver.org/display/GEOSDOC/License\">&copy;</a>\n");
        os.print("  </span>\n");
        os.print("  <h1>LOADING ...</h1>\n");
        os.print("<center><br>\n");
        os.print("<center><h2>Please Wait</h2><center>\n");
        os.print("</table></td></tr>\n");
        os.print("</center></body>\n");
        os.print("</html>");
    }

    private void doLoad(String url, HttpServletResponse response)
        throws ServletException, IOException {
        URL u = new URL(url);

        try {
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.connect();

            String s = con.getResponseMessage();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            while (br.ready())
                br.readLine();

            con.disconnect();
        } catch (Exception e) {
            // should not stop compilation ...
            System.err.println("ERROR compiling " + url);
        }

        response.setContentType("text/html");

        ServletOutputStream os = response.getOutputStream();
        os.print("<html>\n");
        os.print("<head><title>GeoServer - Loading</title>\n");
        os.print("  <meta content=\"text/css\" http-equiv=\"content-style-type\">\n");
        os.print("  <style type=\"text/css\">\n");
        os.print("    <!-- @import url(\"/geoserver/style.css\"); -->\n");
        os.print("  </style>\n");
        os.print("  <link type=\"image/gif\" href=\"gs.gif\" rel=\"icon\"><!-- mozilla --> \n");
        os.print("  <link href=\"gs.ico\" rel=\"SHORTCUT ICON\"><!-- ie -->\n");
        os.print("</head>\n");
        os.print(
            "<body onload=\"javascript:window.location.replace('JSPCompiler')\"><br><center>\n");
        os.print("<table width=\"60%\" height=\"60%\"><tr><td>\n");
        os.print("<center>\n");
        os.print("  <span class=\"project\">\n");
        os.print("    <a href=\"http://geoserver.org/\">GeoServer</a>\n");
        os.print("  </span>\n");
        os.print("  <span class=\"license\">\n");
        os.print("    <a href=\"http://geoserver.org/display/GEOSDOC/License\">&copy;</a>\n");
        os.print("  </span>\n");
        os.print("  <h1>LOADING ...</h1>\n");
        os.print("<center><br>\n");
        os.print("<center><h2>Please Wait</h2><center>\n");
        os.print("<center><h2>" + (int) ((100 * (spot * 1.0)) / (pages.length * 1.0))
            + "% Completed</h2><center>\n");
        os.print("</table></td></tr>\n");
        os.print("</center></body>\n");
        os.print("</html>");
    }
}


package org.vfny.geoserver.servlets.wfs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Simple tester for WFS post requests. Can be called two ways. If
 *  called with no parameters, it displays the form, otherwise it displays
 *  the result page.
 *
 * @author  Doug Cates: Moxi Media Inc.
 * @version 1.0
 */
public class TestWfsPost extends HttpServlet {
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Tests a WFS post request using a form entry.";
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String requestString = request.getParameter("body");
        String urlString = request.getParameter("url");
        if ((requestString == null) || (urlString == null)) {
            StringBuffer urlInfo = request.getRequestURL();
            if (urlInfo.indexOf("?") != -1) {
                urlInfo.delete(urlInfo.indexOf("?"), urlInfo.length());
            }
            String geoserverUrl = urlInfo.substring(0,urlInfo.indexOf("/", 8)) + request.getContextPath();
            response.setContentType("text/html");
            out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>TestWfsPost</title>");
            out.println("</head>");
            out.println("<script language=\"JavaScript\">");
            out.println("function doNothing() {");
            out.println("}");
            out.println("function sendRequest() {");
            out.println("  if (checkURL()==true) {");
            out.print("    document.frm.action = \"");
            out.print(urlInfo.toString());
            out.print("\";\n");
            out.println("    document.frm.target = \"_blank\";");
            out.println("    document.frm.submit();");
            out.println("  }");
            out.println("}");
            out.println("function checkURL() {");
            out.println("  if (document.frm.url.value==\"\") {");
            out.println("    alert(\"Please give URL before you sumbit this form!\");");
            out.println("    return false;");
            out.println("  } else {");
            out.println("    return true;");
            out.println("  }");
            out.println("}");
            out.println("function clearRequest() {");
            out.println("document.frm.body.value = \"\";");
            out.println("}");
            out.println("</script>");
            out.println("<body>");
            out.println("<form name=\"frm\" action=\"JavaScript:doNothing()\" method=\"POST\">");
            out.println("<table align=\"center\" cellspacing=\"2\" cellpadding=\"2\" border=\"0\">");
            out.println("<tr>");
            out.println("<td><b>URL:</b></td>");
            out.print("<td><input name=\"url\" value=\"");
            out.print(geoserverUrl);
            out.print("/wfs/GetFeature\" size=\"70\" MAXLENGTH=\"100\"/></td>\n");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td><b>Request:</b></td>");
            out.println("<td><textarea cols=\"60\" rows=\"24\" name=\"body\"></textarea></td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("<table align=\"center\">");
            out.println("<tr>");
            out.println("<td><input type=\"button\" value=\"Clear\" onclick=\"clearRequest()\"></td>");
            out.println("<td><input type=\"button\" value=\"Submit\" onclick=\"sendRequest()\"></td>");
            out.println("<td></td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } else {
            response.setContentType("application/xml");
            BufferedReader xmlIn = null;
            PrintWriter xmlOut = null;
            StringBuffer sbf = new StringBuffer();
            String resp = null;
            try {
                URL u = new URL(urlString);
                java.net.HttpURLConnection acon = (java.net.HttpURLConnection) u.openConnection();
                acon.setAllowUserInteraction(false);
                acon.setRequestMethod("POST");
                acon.setDoOutput(true);
                acon.setDoInput(true);
                acon.setUseCaches(false);
                acon.setRequestProperty("Content-Type", "application/xml");
                xmlOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(acon.getOutputStream())));
                xmlOut = new java.io.PrintWriter(acon.getOutputStream());
                xmlOut.write(requestString);
                xmlOut.flush();
                xmlIn = new BufferedReader(new InputStreamReader(acon.getInputStream()));
                String line;
                while ((line = xmlIn.readLine()) != null) {
                    out.print(line);
                }
            }
            catch(Exception e) {
                out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                out.println("<servlet-exception>");
                out.println(e.toString());
                out.println("</servlet-exception>");
            }
            finally {
                try {
                    if (xmlIn != null)  xmlIn.close();
                }
                catch (Exception e1) {
                    out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    out.println("<servlet-exception>");
                    out.println(e1.toString());
                    out.println("</servlet-exception>");
                }
                try {
                    if (xmlOut != null)  xmlOut.close();
                }
                catch (Exception e2) {
                    out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    out.println("<servlet-exception>");
                    out.println(e2.toString());
                    out.println("</servlet-exception>");
                }
            }
        }
        out.close();
    }
    
}

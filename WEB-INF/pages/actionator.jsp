<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ page import="org.apache.struts.action.*" %>

<table class="actions">
  <tbody>
<%  String forwards = (String) request.getAttribute("actionForwards");
    String[] array = forwards.split(":");
    ActionServlet servlet =
        (ActionServlet) application.getAttribute(Action.ACTION_SERVLET_KEY);
    for (int index = 0; index < array.length; index ++) { %>    
    <tr>
      <td>
        <html:form action="<%= array[index] %>">
          <html:submit>
            <%= array[index] %>
          </html:submit>
        </html:form>
      </td>
    </tr>
<%  } %>
    </tr>
  </tbody>
</table>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ page import="org.apache.struts.action.*" %>

<logic:notEmpty name="actionForwards">
  <table class="actions">
    <tbody>
<%  // Access actionForwards - could not figure out how to with tags
    String forwards = (String) request.getAttribute("actionForwards");
    String[] array = forwards.split(":"); 
	
    System.out.println("ACTIONATOR");
    for (int index = 0; index < array.length; index ++) {
%>
      <tr>
        <td>
          <html:link style="action"
                     forward="<%= array[index] %>"
                     titleKey="<%= array[ index ]+".short" %>">
            <bean:message key="<%= array[ index ]+".label" %>"/>
          </html:link>
        </td>
      </tr>
<%  } %>
    </tbody>
  </table>
</logic:notEmpty>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ page import="org.apache.struts.action.*" %>

<span class="locator">
	<%
		String forwards = (String) request.getAttribute("locationForwards");
		String[] array = forwards.split(":");
		ActionServlet servlet = (ActionServlet) application.getAttribute(Action.ACTION_SERVLET_KEY);

		for (int index = 0; index < array.length-1; index ++) {
			%>
			<html:link forward="<%= array[index] %>">
				<%= array[index] %>
			</html:link> |
			<%
		}
	%>
	<%= array[array.length-1] %>
</span>
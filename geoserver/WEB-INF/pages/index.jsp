<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<span style="visible:false">
  <tiles:insert value="/WEB-INF/pages/sidebarApplication.jsp"/>
  <tiles:insert value="/WEB-INF/pages/Welcome.jsp"/>
</span>
<logic:redirect forward="welcome"/>

<%--

Redirect default requests to Welcome global ActionForward.
By using a redirect, the user-agent will change address to match the path of our Welcome ActionForward. 

--%>

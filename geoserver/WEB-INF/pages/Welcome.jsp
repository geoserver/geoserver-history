<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<html:html locale="true">
<head>
<title><bean:message key="welcome.title"/></title>
<html:base/>
</head>
<body bgcolor="white">

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <font color="red">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </font>
</logic:notPresent>

<h3><bean:message key="welcome.heading"/></h3>
<p><html:link forward="mainmenu"><bean:message key="welcome.message"/></html:link></p>
<html:errors/>
<logic:present name="test">
<b>
	<bean:write name="test" property="name"/><BR>
	<bean:write name="test" property="title"/><BR>
</b>
</logic:present>

</body>
</html:html>

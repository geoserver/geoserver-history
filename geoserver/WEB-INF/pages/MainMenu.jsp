<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<html:html locale="true">

<head>
<title><bean:message key="welcome.title"/></title>
<html:base/>
</head>

<body bgcolor="white">

<bean:message key="link.validationConfig"/><BR>
<html:link forward="systemConfigMenu"><bean:message key="link.systemConfig"/></html:link>

</body>

</html:html>
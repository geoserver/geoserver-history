<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<html:html locale="true">

<head>
<title><bean:message key="welcome.title"/></title>
<html:base/>
</head>

<body bgcolor="white">

<bean:message key="label.validationConfig"/><BR>
<html:link forward="systemConfigMenu"><bean:message key="label.systemConfig"/></html:link>

</body>

</html:html>
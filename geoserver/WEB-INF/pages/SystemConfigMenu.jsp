<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<html:html locale="true">

<head>
<title><bean:message key="welcome.title"/></title>
<html:base/>
</head>

<body bgcolor="white">

<BR>
	<html:link forward="wfsConfigMenu"><bean:message key="link.wfsConfig"/><BR></html:link>
<BR>
	<bean:message key="link.wmsConfig"/><BR>
<BR>
	<bean:message key="link.catalogConfig"/><BR>

</body>

</html:html>
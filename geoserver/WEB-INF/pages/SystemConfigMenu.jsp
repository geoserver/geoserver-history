<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<html:html locale="true">

<head>
<title><bean:message key="welcome.title"/></title>
<html:base/>
</head>

<body bgcolor="white">

<BR>
	<html:link forward="wfsConfigMenu"><bean:message key="label.wfsConfig"/><BR></html:link>
<BR>
	<bean:message key="label.wmsConfig"/><BR>
<BR>
	<bean:message key="label.catalogConfig"/><BR>

</body>

</html:html>
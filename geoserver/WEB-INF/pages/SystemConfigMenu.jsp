<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<html:html locale="true">

<head>
<title><bean:message key="welcome.title"/></title>
<html:base/>
</head>

<body bgcolor="white">

<BR>
	<html:link forward="wfsConfigMenu"><bean:message key="label.wfsConfig"/></html:link><BR>
<BR>
	<html:link forward="wmsConfigMenu"><bean:message key="label.wmsConfig"/></html:link><BR>
<BR>
	<html:link forward="dataConfigMenu"><bean:message key="label.dataConfig"/></html:link><BR>

</body>

</html:html>
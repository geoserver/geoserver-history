<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>

<html:html locale="true">

<head>
<title><tiles:getAsString name="title"/></title>
<html:base/>
<LINK rel="stylesheet" type="text/css" href="<html:rewrite forward='baseStyle'/>">
</head>

<body bgcolor="white">

<table border=0 width=1024 valign="top">
	<tr>
		<td colspan=2>
			<tiles:insert attribute="header"/>
		</td>
	</tr>
	<tr>
		<td width=192 valign="top">
			<tiles:insert attribute="sidebar"/>
		</td>
		<td>
			<tiles:insert attribute="body"/>
		</td>
	</tr>
</table>

</body>
</html:html>
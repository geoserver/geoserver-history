<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>

<html:html locale="true">

<head>
<title><tiles:getAsString name="title"/></title>
<html:base/>
</head>

<body bgcolor="white">

<table border=1 width=100%>
	<tr>
		<td colspan=2>
			<tiles:insert attribute="header"/>
		</td>
	</tr>
	<tr>
		<td width=192>
			<tiles:insert attribute="sidebar"/>
		</td>
		<td>
			<tiles:insert attribute="body"/>
		</td>
	</tr>
</table>

</body>
</html:html>
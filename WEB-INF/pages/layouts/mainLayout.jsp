<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<html:html locale="true">

<bean:define id="titleKey">
	<tiles:getAsString name='title'/>
</bean:define>

<head>
<title><bean:message key="<%= titleKey %>"/></title>
<html:base/>
<LINK rel="stylesheet" type="text/css" href="<html:rewrite forward='baseStyle'/>">
</head>

<body bgcolor="white">

<table border=0 width=1024 valign="top">
	<tr>
		<td colspan=2>
			<table width=100% border=0>
				<tr>
					<td width=378>
						<tiles:insert attribute="logo"/>
					</td>
					<td valign="bottom" align="left">
						<tiles:insert attribute="location"/>
					</td>
					<td align="right" valign="bottom">
						<tiles:insert attribute="menu"/>
					</td>
				</tr>
				<tr>
				<td colspan=3><hr></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td width=192 valign="top">
			<table border=0 width=100% height=100%>
				<tr>
					<td valign="top" align="left">
						<tiles:insert attribute="status"/>
					</td>
				</tr>
				<tr>
					<td valign="top" align="left">
						<tiles:insert attribute="buttons"/>
					</td>
				</tr>
				<tr>
					<td valign="top" align="left">
						<tiles:insert attribute="messages"/>
					</td>
				</tr>
			</table>
		</td>
		<td>
			<tiles:insert attribute="body"/>
		</td>
	</tr>
</table>

</body>
</html:html>
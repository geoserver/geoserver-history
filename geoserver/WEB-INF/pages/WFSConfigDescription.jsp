<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>

<html:html locale="true">

<head>
<title><bean:message key="welcome.title"/></title>
<html:base/>
</head>

<body bgcolor="white">

<table border=1 width=100%>
<tr>
<tiles:insert attribute="header"/>
</tr>
<tr>
	<td width=192>
		<table border=1 width=100%>
			<tr>
				<td>
					Status
				</td>
			</tr>
			<tr>
				<td>
					Try Out<BR>
					Save<BR>
					Load<BR>
				</td>
			</tr>
			<tr>
				<td>
					Actions
				</td>
			</tr>
		</table>
	</td>
	<td colspan=2>
		<table border=1 width=100%>
			<tr><td>
				<bean:message key="label.description"/> <bean:message key="label.contents"/>
			</td></tr>
			<tr><td>
			<table border=1 width=100%>
			
			<html:form action="WFSConfigDescriptionSubmit" focus="name">
			<tr><td align="right"><bean:message key="label.name"/>:</td><td><html:text property="name" size="50"/></td></tr>
			<tr><td align="right"><bean:message key="label.title"/>:</td><td><html:text property="title"/></td></tr>
			<tr><td align="right"><bean:message key="label.accessConstraints"/>:</td><td><html:text property="accessConstraints"/></td></tr>
			<tr><td align="right"><bean:message key="label.fees"/>:</td><td><html:text property="fees"/></td></tr>
			<tr><td align="right"><bean:message key="label.maintainer"/>:</td><td><html:text property="maintainer"/></td></tr>
			<tr><td align="right"><bean:message key="label.keywords"/>:</td><td><html:textarea property="keywords"/></td></tr>
			<tr><td align="right"><bean:message key="label.abstract"/>:</td><td><html:textarea property="_abstract"/></td></tr>
			<tr><td align="right">&nbsp;</td><td><html:submit/><html:reset/></td></tr>
			</html:form>
			
			</table>
			</td></tr>
		</table>
	</td>
</tr>
</table>

</body>
</html:html>
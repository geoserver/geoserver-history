<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<table border=1 width=100%>
	<html:errors/><BR>
	
	<html:form action="WFSConfigDescriptionSubmit" focus="name">
	<tr><td align="right"><bean:message key="label.name"/>:</td><td><html:text property="name" size="50"/></td></tr>
	<tr><td align="right"><bean:message key="label.title"/>:</td><td><html:text property="title" size="50"/></td></tr>
	<tr><td align="right"><bean:message key="label.accessConstraints"/>:</td><td><html:text property="accessConstraints" size="50"/></td></tr>
	<tr><td align="right"><bean:message key="label.fees"/>:</td><td><html:text property="fees" size="50"/></td></tr>
	<tr><td align="right"><bean:message key="label.maintainer"/>:</td><td><html:text property="maintainer" size="50"/></td></tr>
	<tr><td align="right"><bean:message key="label.keywords"/>:</td><td><html:textarea property="keywords"/></td></tr>
	<tr><td align="right"><bean:message key="label.abstract"/>:</td><td><html:textarea property="_abstract"/></td></tr>
	<tr><td align="right">&nbsp;</td><td><html:submit/><html:reset/></td></tr>
	</html:form>
</table>
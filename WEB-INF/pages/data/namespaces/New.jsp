<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>
	<html:form action="DataConfigNamespacesNew">
		<tr><td>
			<bean:message key="label.prefix"/>:
		</td><td>
			<html:text property="prefix" size="60"/>
		</td></tr>
		<tr><td>&nbsp;</td>
		<td>
			<html:submit>
				<bean:message key="label.new"/>
			</html:submit>
		</td></tr>
	</html:form>
</table>
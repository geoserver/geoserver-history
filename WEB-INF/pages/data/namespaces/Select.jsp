<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>
	<html:form action="DataConfigNamespacesSelect">
		<tr><td>
			<bean:message key="label.namespaces"/>:
		</td><td>
			<html:select property="selectedNamespace">
				<html:options property="namespaces"/>
			</html:select>
		</td></tr>
		<tr><td>&nbsp;</td>
		<td>
			<html:submit property="action">
				<bean:message key="label.edit"/>
			</html:submit>
			<html:submit property="action">
				<bean:message key="label.delete"/>
			</html:submit>
		</td></tr>
		
		<tr>
			<td>&nbsp;</td>
			<td>
				<html:link forward="dataConfigNamespacesNewPage">
					<bean:message key="label.createNewNamespace"/>
				</html:link>
			</td>	
		</tr>		
	</html:form>
</table>
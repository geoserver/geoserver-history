<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>
	<html:errors/><BR>
	
	<html:form action="DataConfigNamespacesSubmit">
	
	<tr><td>
		<bean:message key="label.namespaces"/>:
		</td><td>
		<html:select property="selectedNamespace">
			<html:options property="namespaces"/>
		</html:select>
		
		</td><td>
		
		<html:submit property="action" value="new">
			<bean:message key="label.new"/>
		</html:submit>
		<BR>
		<html:submit property="action" value="edit">
			<bean:message key="label.edit"/>
		</html:submit>
		<BR>
		<html:submit property="action" value="delete">
			<bean:message key="label.delete"/>
		</html:submit>
		<BR>
			
	</td></tr>

	<tr><td align="right">
		<bean:message key="label.default"/>:
	</td><td colspan=2 align="left">
		<html:checkbox property="_default"/>
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.URI"/>
	</td><td colspan=2 align="left">
		<html:text property="URI" size="60"/>
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.prefix"/>
	</td><td colspan=2 align="left">
		<html:text property="prefix" size="60"/>
	</td></tr>
	
	<tr><td align="right">&nbsp;</td><td colspan=2><html:submit property="action" value="submit"/><html:reset/></td></tr>						
	
	</html:form>
</table>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>
	<html:errors/><BR>
	
	<html:form action="CatalogConfigNamespacesSubmit">
	
	<tr><td>
		<beam:message key="label.styles"/>:
		</td><td>
		<html:select property="selectedStyle">
			<html:options collection="styles"/>
		</html:select>
		
		</td><td>
		
		<html:submit property="new" value="<bean:message key="label.new"/>"/><BR>
		<html:submit property="edit" value="<bean:message key="label.edit"/>"/><BR>
		<html:submit property="delete" value="<bean:message key="label.delete"/>"/><BR>			
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.styleID"/>:
	</td><td colspan=2 align="left">
		<html:text property="styleID" size="60"/>
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.default"/>:
	</td><td colspan=2 align="left">
		<html:checkbox property="default"/>
	</td></tr>

	<tr><td align="right">
		<bean:message key="label.filename"/>
	</td><td colspan=2 align="left">
		<html:text property="filename" size="60"/>
	</td></tr>
	
	<tr><td align="right">&nbsp;</td><td colspan=2><html:submit/><html:reset/></td></tr>						
	
	</html:form>
</table>
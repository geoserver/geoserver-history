<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>
	<html:form action="DataConfigAttributeTypesSubmit">

	<tr>
		<td valign="top" align="right">	
			<bean:message key="label.type"/>
		</td><td valign="top" align="left">
		<html:select property="selectedType">
			<html:options property="attributeTypes"/>
		</html:select>
	</td></tr><tr>
	<td valign="top" align="right">
		<bean:message key="label.isRef"/>:
	</td><td valign="top" align="left">
		<html:checkbox property="ref"/>
	</td></tr>
	<td valign="top" align="right">
		<bean:message key="label.isNillible"/>:
	</td><td valign="top" align="left">
		<html:checkbox property="nillible"/>
	</td></tr>
	<td valign="top" align="right">
		<bean:message key="label.minOccurs"/>:
	</td><td valign="top" align="left">
		<html:text property="minOccurs" size="3"/>
	</td></tr>
	<td valign="top" align="right">
		<bean:message key="label.maxOccurs"/>:
	</td><td valign="top" align="left">
		<html:text property="maxOccurs" size="3"/>
	</td></tr>
	<tr><td>&nbsp;</td><td valign="top" align="left">
		<html:textarea property="fragment" rows="10" cols="60"/>
	</td></tr>	
	<tr><td>&nbsp;</td><td valign="top" align="left">
		<html:submit/><html:reset/>
	</td></tr>

	</html:form>
	
</table>
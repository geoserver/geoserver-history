<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=1 width=100%>
<tr><td>
<table border=0 width=100%>

	<html:form action="/config/data/dataStoresNew">

	<tr><td valign="top" align="right">	
		<bean:message key="label.dataStoreDescription"/>:
	</td>
	<td align="left">
		<html:select property="selectedDescription">
			<html:options property="dataStoreDescriptions"/>
		</html:select>
	</td></tr>
	<tr><td align="right">
		<bean:message key="label.dataStoreID"/>:
	</td>
	<td align="left">
		<html:text property="dataStoreID"/>
	</td></tr>
	<tr><td>&nbsp;</td><td align="left">
		<html:submit>
			<bean:message key="label.new"/>
		</html:submit>
	</td></tr>
	
	</html:form>
	
</table>
</td></tr>
</table>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>

	<html:form action="DataConfigDataStoresNew">

	<tr><td valign="top">	
		<html:select property="selectedDescription">
			<html:options property="dataStoreDescriptions"/>
		</html:select>
	</td></tr>
	<tr><td>
		<bean:message key="label.dataStoreID"/><html:text property="dataStoreID"/>
	</td></tr>
	<tr><td>

		<html:submit property="action" value="new">
			<bean:message key="label.new"/>
		</html:submit>
	</td></tr>
	
	</html:form>
</table>
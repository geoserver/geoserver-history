<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=1 width=100%>
<tr><td>
<table border=0 width=100%>

	<html:form action="DataConfigDataStoresSelect">
	<tr><td valign="top" align="left">
		<bean:message key="label.dataStoreID"/>:
	</td><td>
		<html:select property="selectedDataStoreId">
			<html:options name="Config.Data" property="dataStoreIds"/>
		</html:select>
	</td></tr>
	<tr><td>&nbsp;</td><td valign="top" align="left">
		<html:submit property="buttonAction">
			<bean:message key="label.edit"/>
		</html:submit>
	</td></tr>
	<tr><td>&nbsp;</td><td valign="top" align="left">
		<html:submit property="buttonAction">
			<bean:message key="label.delete"/>
		</html:submit>
	</td></tr>

	<tr>
		<td>&nbsp;</td>
		<td><html:link forward="dataConfigDataStoresNew"><bean:message key="label.createNewDataStore"/></html:link></td>	
	</tr>
	
	</html:form>

</table>
</td></tr>
</table>